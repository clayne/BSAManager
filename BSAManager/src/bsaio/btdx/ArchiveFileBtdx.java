package bsaio.btdx;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

import com.frostwire.util.LongSparseArray;

import bsaio.ArchiveEntry;
import bsaio.ArchiveFile;
import bsaio.ArchiveInputStream;
import bsaio.DBException;
import bsaio.HashCode;
import bsaio.btdx.ArchiveEntryDX10.DX10Chunk;
import bsaio.displayables.DisplayableArchiveEntry;
import bsaio.displayables.DisplayableArchiveEntryDX10;
import tools.io.FileChannelRAF;

public class ArchiveFileBtdx extends ArchiveFile {

	public enum BsaFileType {
		GNRL, DX10
	};

	private BsaFileType				bsaFileType;				// in BTDX id
	
	private boolean					hasDDSFiles		= false;

	private boolean					hasKTXFiles		= false;

	private boolean					hasASTCFiles	= false;
	
	private boolean					hasMaterials 	= false;

	private LongSparseArray<String>	filenameHashToFileNameMap;

	public ArchiveFileBtdx(FileChannel file, String fileName) {
		super(SIG.BTDX, file, fileName);
	}

	/**
	 * CAUTION Super HEAVY WEIGHT!!
	 * 
	 * @return
	 */
	@Override
	public List<ArchiveEntry> getEntries() {
		ArrayList<ArchiveEntry> ret = new ArrayList<ArchiveEntry>();
		int filesToLoad = fileCount;
		int currentProgress = 0;
		try {
			for (int f = 0; f < folderHashToFolderMap.size(); f++) {
				Folder folder = folderHashToFolderMap.get(folderHashToFolderMap.keyAt(f));
				if (folder.fileToHashMap == null) {
					loadFolder(folder);
				}
				for (int i = 0; i < folder.fileToHashMap.size(); i++)
					ret.add(folder.fileToHashMap.get(folder.fileToHashMap.keyAt(i)));

				filesToLoad -= folder.folderFileCount;
				int newProgress = (filesToLoad * 100) / fileCount;
				if (newProgress >= currentProgress + 5) {
					currentProgress = newProgress;

				}
			}

		} catch (IOException e) {
			System.out.println("ArchiveFile Exception for filename:  " + e + " " + e.getStackTrace() [0]);
		}

		return ret;

	}

	@Override
	public ArchiveEntry getEntry(String fullFileName) {
		fullFileName = fullFileName.toLowerCase();
		fullFileName = fullFileName.trim();
		if (fullFileName.indexOf("/") != -1) {
			StringBuilder buildName = new StringBuilder(fullFileName);
			int sep;
			while ((sep = buildName.indexOf("/")) >= 0) {
				buildName.replace(sep, sep + 1, "\\");
			}
			fullFileName = buildName.toString();
		}

		int pathSep = fullFileName.lastIndexOf("\\");
		String folderName = fullFileName.substring(0, pathSep);
		long folderHash = new HashCode(folderName, true).getHash();
		Folder folder = folderHashToFolderMap.get(folderHash);

		if (folder != null) {
			// do we need to load the files in this folder?
			if (folder.fileToHashMap == null) {
				System.out.println("BTDX folderName not indexed " + folderName);
				return null;
			}

			String fileName = fullFileName.substring(pathSep + 1);
			long fileHashCode = new HashCode(fileName, false).getHash();
			String bsaFileName = filenameHashToFileNameMap.get(fileHashCode);

			if (bsaFileName != null) {
				if (bsaFileName.equals(fileName)) {
					return folder.fileToHashMap.get(fileHashCode);
				} else {
					System.out.println("BSA File name mismatch: " + bsaFileName + " " + fileName);
				}
			}
		}

		return null;
	}

	@Override
	protected void loadFolder(Folder folder) throws IOException {
		throw new UnsupportedOperationException("BTDX is loaded at inital load time, so this should never be called");
	}

	@Override
	public InputStream getInputStream(ArchiveEntry entry) throws IOException {
		if (in == null) {
			throw new IOException("Archive file is not open");
		}

		if (bsaFileType == BsaFileType.DX10) {
			return new ArchiveInputStreamDX10(in, entry);
		} else {
			return new ArchiveInputStream(in, entry);
		}
	}

	@Override
	public ByteBuffer getByteBuffer(ArchiveEntry entry) throws IOException {
		if (in == null) {
			throw new IOException("Archive file is not open");
		}

		if (bsaFileType == BsaFileType.DX10) {
			return ArchiveInputStreamDX10.getByteBuffer(in, entry, false);
		} else {
			return ArchiveInputStream.getByteBuffer(in, entry, false);
		}
	}

	@Override
	public ByteBuffer getByteBuffer(ArchiveEntry entry, boolean allocateDirect) throws IOException {
		if (in == null) {
			throw new IOException("Archive file is not open");
		}

		if (bsaFileType == BsaFileType.DX10) {
			return ArchiveInputStreamDX10.getByteBuffer(in, entry, allocateDirect);
		} else {
			return ArchiveInputStream.getByteBuffer(in, entry, allocateDirect);
		}
	}

	@Override
	public void load(boolean isForDisplay) throws DBException, IOException {
		in = new FileChannelRAF(file, "r");// needed elsewhere
		FileChannel ch = file;

		long pos = 0;
		
		// load header
		byte[] header = new byte[24];
		int count = ch.read(ByteBuffer.wrap(header), pos);
		pos += header.length;
		if (count != 24)
			throw new EOFException("Archive header is incomplete");

		String id = new String(header, 0, 4);
		if (!id.equals("BTDX"))
			throw new DBException("Archive file is not BTDX id " + id + " " + fileName);
		version = getInteger(header, 4);
		if (version > 2)
			throw new DBException("BSA version " + version + " is not supported " + fileName);
		if (version == 2)
			System.out.println("BSA version " + version + " is not supported " + fileName);

		String type = new String(header, 8, 4); // GRNL or DX10
		if (type.equals("GNRL"))
			bsaFileType = BsaFileType.GNRL;
		else if (type.equals("DX10"))
			bsaFileType = BsaFileType.DX10;
		else
			throw new DBException("BSA bsaFileType " + type + " is not supported " + fileName);

		fileCount = getInteger(header, 12);
		long nameTableOffset = getLong(header, 16);
		// end of header read 24 bytes long

		// but we are going to jump to the name table (which is after the file records)
		
		pos = nameTableOffset;		

		// ready
		String[] fileNames = new String[fileCount];

		// load fileNameBlock
		byte[] nameBuffer = new byte[0x10000];

		for (int i = 0; i < fileCount; i++) {
			byte[] b = new byte[2];
			count = ch.read(ByteBuffer.wrap(b), pos);
			pos += b.length;
			int len = getShort(b, 0);

			count = ch.read(ByteBuffer.wrap(nameBuffer, 0, len), pos);
			pos += len;
			nameBuffer [len] = 0;// null terminate (FIXME: why?)

			String filename = new String(nameBuffer, 0, len);
			fileNames [i] = filename;
			
			hasDDSFiles = hasDDSFiles || filename.endsWith("dds");

			hasKTXFiles = hasKTXFiles || filename.endsWith("ktx");

			hasASTCFiles = hasASTCFiles || filename.endsWith("astc");
			
			hasMaterials = hasMaterials || filename.endsWith("bgsm") || filename.endsWith("bgem");
		}

		// build up a trival folderhash from all the file names
		// and preload the archive entries from the data above
		// reset to below header
		pos = 24;

		folderHashToFolderMap = new LongSparseArray<Folder>();
		filenameHashToFileNameMap = new LongSparseArray<String>(fileCount);

		byte[] buffer = null;
		if (bsaFileType == BsaFileType.GNRL) {
			// we can read it all up front in this case
			buffer = new byte[fileCount * 36];
			count = ch.read(ByteBuffer.wrap(buffer), pos);
			pos += buffer.length;
		} else {
			buffer = new byte[24];
		}

		for (int i = 0; i < fileCount; i++) {
			
			String fullFileName = fileNames [i].toLowerCase();
			fullFileName = fullFileName.trim();
			if (fullFileName.indexOf("/") != -1) {
				StringBuilder buildName = new StringBuilder(fullFileName);
				int sep;
				while ((sep = buildName.indexOf("/")) >= 0) {
					buildName.replace(sep, sep + 1, "\\");
				}
				fullFileName = buildName.toString();
			}				
			
			int pathSep = fullFileName.lastIndexOf("\\");
			pathSep = pathSep == -1 ? 0 : pathSep;
			String folderName = fullFileName.substring(0, pathSep);
			long folderHash = new HashCode(folderName, true).getHash();
			Folder folder = folderHashToFolderMap.get(folderHash);

			if (folder == null) {
				folder = new Folder(0, -1, isForDisplay);
				folder.fileToHashMap = new LongSparseArray<ArchiveEntry>();
				folderHashToFolderMap.put(folderHash, folder);
			}

			String fileName = fullFileName.substring(pathSep + 1).trim();
			long fileHashCode = new HashCode(fileName, false).getHash();
			filenameHashToFileNameMap.put(fileHashCode, fileName);

			if (bsaFileType == BsaFileType.GNRL) {
				ArchiveEntry entry;
				if (isForDisplay)
					entry = new DisplayableArchiveEntry(this, folderName, fileName);
				else
					entry = new ArchiveEntry(this, folderName, fileName);

				// int nameHash = getInteger(buffer, i*36+0);// 00 - name hash?
				// String ext = new String(buffer, 4,i*36+ 4); // 04 - extension
				// int dirHash = getInteger(buffer, i*36+8); // 08 - directory hash?
				// int unk0C = getInteger(buffer, i*36+12); // 0C - flags? 00100100
				long offset = getLong(buffer, i * 36 + 16); // 10 - relative to start of file
				int packedLen = getInteger(buffer, i * 36 + 24); // 18 - packed length (zlib)
				int unpackedLen = getInteger(buffer, i * 36 + 28); // 1C - unpacked length
				int unk20 = getInteger(buffer, i * 36 + 32); // 20 - BAADF00D

				entry.setFileOffset(offset);
				entry.setFileLength(unpackedLen);
				entry.setCompressed(packedLen != 0 && packedLen != unpackedLen);

				int compLen = packedLen;
				if (compLen == 0)
					compLen = unk20; // what

				entry.setCompressedLength(compLen);
				folder.fileToHashMap.put(fileHashCode, entry);
				folder.folderFileCount++;
			} else {
				ArchiveEntryDX10 entry;
				if (isForDisplay)
					entry = new DisplayableArchiveEntryDX10(this, folderName, fileName);
				else
					entry = new ArchiveEntryDX10(this, folderName, fileName);

				count = ch.read(ByteBuffer.wrap(buffer), pos);
				pos += buffer.length;
				// int nameHash = getInteger(buffer, 0);// 00 - name hash?
				// String ext = new String(buffer, 4, 4); // 04 - extension
				// int dirHash = getInteger(buffer, 8); // 08 - directory hash?
				// int unk0C= buffer[12]& 0xff; //
				entry.numChunks = buffer [13] & 0xff; //
				entry.chunkHdrLen = getShort(buffer, 14); // - size of one chunk header
				entry.height = getShort(buffer, 16); //
				entry.width = getShort(buffer, 18); //					
				entry.numMips = buffer [20] & 0xff; //
				entry.format = buffer [21] & 0xff; // - DXGI_FORMAT
				entry.unk16 = getShort(buffer, 22); // - 0800

				if (entry.numChunks != 0) {
					entry.chunks = new DX10Chunk[entry.numChunks];
					//read them all off at once
					byte[] chunkBuffer = new byte[entry.numChunks * 24];
					count = ch.read(ByteBuffer.wrap(chunkBuffer), pos);
					pos += chunkBuffer.length;
					for (int c = 0; c < entry.numChunks; c++) {
						entry.chunks [c] = new DX10Chunk();
						entry.chunks [c].offset = getLong(chunkBuffer, (c * 24) + 0); // 00
						entry.chunks [c].packedLen = getInteger(chunkBuffer, (c * 24) + 8); // 08
						entry.chunks [c].unpackedLen = getInteger(chunkBuffer, (c * 24) + 12); // 0C
						entry.chunks [c].startMip = getShort(chunkBuffer, (c * 24) + 16); // 10
						entry.chunks [c].endMip = getShort(chunkBuffer, (c * 24) + 18); // 12
						entry.chunks [c].unk14 = getInteger(chunkBuffer, (c * 24) + 20); // 14 - BAADFOOD
					}
				}

				folder.fileToHashMap.put(fileHashCode, entry);
				folder.folderFileCount++;
			}
		}
	}

	@Override
	public boolean hasNifOrKf() {
		return bsaFileType == BsaFileType.GNRL;
	}

	@Override
	public boolean hasTextureFiles() {
		return hasDDSFiles || hasKTXFiles || hasASTCFiles; 
	}

	@Override
	public boolean hasSounds() {
		return bsaFileType == BsaFileType.GNRL;
	}

	@Override
	public boolean hasDDS() {
		return hasDDSFiles;
	}
	
	@Override
	public boolean hasKTX() {
		return hasKTXFiles;
	}

	@Override
	public boolean hasASTC() {
		return hasASTCFiles;
	}
	
	@Override
	public boolean hasMaterials() {
		return hasMaterials;
	}

}