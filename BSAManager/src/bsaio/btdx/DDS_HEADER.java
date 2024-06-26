package bsaio.btdx;

/**
 * Copy of ba2extract dds.h file in java
 * Similar to DDSImage
 * @author phil
 *
 */
public class DDS_HEADER
{
	//--------------------------------------------------------------------------------------
	// dds.h
	//
	// This header defines constants and structures that are useful when parsing 
	// DDS files.  DDS files were originally designed to use several structures
	// and constants that are native to DirectDraw and are defined in ddraw.h,
	// such as DDSURFACEDESC2 and DDSCAPS2.  This file defines similar 
	// (compatible) constants and structures so that one can use DDS files 
	// without needing to include ddraw.h.
	//--------------------------------------------------------------------------------------

	int dwSize;

	int dwHeaderFlags;

	int dwHeight;

	int dwWidth;

	int dwPitchOrLinearSize;

	int dwDepth; // only if DDS_HEADER_FLAGS_VOLUME is set in dwHeaderFlags

	int dwMipMapCount;

	int[] dwReserved1 = new int[11];

	DDS_PIXELFORMAT ddspf;

	int dwSurfaceFlags;

	int dwCubemapFlags;

	int[] dwReserved2 = new int[3];

	int DDS_MAGIC = 0x20534444; // "DDS "

	static class DDS_PIXELFORMAT
	{
		int dwSize;

		int dwFlags;

		int dwFourCC;

		int dwRGBBitCount;

		int dwRBitMask;

		int dwGBitMask;

		int dwBBitMask;

		int dwABitMask;

		public DDS_PIXELFORMAT()
		{
		}

		public DDS_PIXELFORMAT(int dwSize, int dwFlags, int dwFourCC, int dwRGBBitCount, int dwRBitMask, int dwGBitMask, int dwBBitMask,
				int dwABitMask)
		{
			this.dwSize = dwSize;
			this.dwFlags = dwFlags;
			this.dwFourCC = dwFourCC;
			this.dwRGBBitCount = dwRGBBitCount;
			this.dwRBitMask = dwRBitMask;
			this.dwGBitMask = dwGBitMask;
			this.dwBBitMask = dwBBitMask;
			this.dwABitMask = dwABitMask;
		}
	};

	public static class DDS_HEADER_DXT10
	{
		int dxgiFormat;//DXGI_FORMAT
		int resourceDimension;//D3D10_RESOURCE_DIMENSION
		int miscFlag;// see DDS_RESOURCE_MISC_FLAG
		int arraySize;
		int miscFlags2;// see DDS_MISC_FLAGS2
	}

	static int DDS_FOURCC = 0x00000004; // DDPF_FOURCC

	int DDS_RGB = 0x00000040; // DDPF_RGB

	int DDS_RGBA = 0x00000041; // DDPF_RGB | DDPF_ALPHAPIXELS

	int DDS_LUMINANCE = 0x00020000; // DDPF_LUMINANCE

	int DDS_ALPHA = 0x00000002; // DDPF_ALPHA

	DDS_PIXELFORMAT DDSPF_DXT1 = new DDS_PIXELFORMAT(8 * 4, DDS_FOURCC, MAKEFOURCC('D', 'X', 'T', '1'), 0, 0, 0, 0, 0);

	DDS_PIXELFORMAT DDSPF_DXT2 = new DDS_PIXELFORMAT(8 * 4, DDS_FOURCC, MAKEFOURCC('D', 'X', 'T', '2'), 0, 0, 0, 0, 0);

	DDS_PIXELFORMAT DDSPF_DXT3 = new DDS_PIXELFORMAT(8 * 4, DDS_FOURCC, MAKEFOURCC('D', 'X', 'T', '3'), 0, 0, 0, 0, 0);

	DDS_PIXELFORMAT DDSPF_DXT4 = new DDS_PIXELFORMAT(8 * 4, DDS_FOURCC, MAKEFOURCC('D', 'X', 'T', '4'), 0, 0, 0, 0, 0);

	DDS_PIXELFORMAT DDSPF_DXT5 = new DDS_PIXELFORMAT(8 * 4, DDS_FOURCC, MAKEFOURCC('D', 'X', 'T', '5'), 0, 0, 0, 0, 0);

	DDS_PIXELFORMAT DDSPF_ATI2 = new DDS_PIXELFORMAT(8 * 4, DDS_FOURCC, MAKEFOURCC('A', 'T', 'I', '2'), 0, 0, 0, 0, 0);
	
	DDS_PIXELFORMAT DDSPF_A8R8G8B8 = new DDS_PIXELFORMAT(8 * 4, DDS_RGBA, 0, 32, 0x00ff0000, 0x0000ff00, 0x000000ff, 0xff000000);

	DDS_PIXELFORMAT DDSPF_A1R5G5B5 = new DDS_PIXELFORMAT(8 * 4, DDS_RGBA, 0, 16, 0x00007c00, 0x000003e0, 0x0000001f, 0x00008000);

	DDS_PIXELFORMAT DDSPF_A4R4G4B4 = new DDS_PIXELFORMAT(8 * 4, DDS_RGBA, 0, 16, 0x00000f00, 0x000000f0, 0x0000000f, 0x0000f000);

	DDS_PIXELFORMAT DDSPF_R8G8B8 = new DDS_PIXELFORMAT(8 * 4, DDS_RGB, 0, 24, 0x00ff0000, 0x0000ff00, 0x000000ff, 0x00000000);

	DDS_PIXELFORMAT DDSPF_R5G6B5 = new DDS_PIXELFORMAT(8 * 4, DDS_RGB, 0, 16, 0x0000f800, 0x000007e0, 0x0000001f, 0x00000000);

	// This indicates the DDS_HEADER_DXT10 extension is present (the format is in dxgiFormat)
	DDS_PIXELFORMAT DDSPF_DX10 = new DDS_PIXELFORMAT(8 * 4, DDS_FOURCC, MAKEFOURCC('D', 'X', '1', '0'), 0, 0, 0, 0, 0);

	int DDS_HEADER_FLAGS_TEXTURE = 0x00001007; // DDSD_CAPS | DDSD_HEIGHT | DDSD_WIDTH | DDSD_PIXELFORMAT 

	int DDS_HEADER_FLAGS_MIPMAP = 0x00020000; // DDSD_MIPMAPCOUNT

	int DDS_HEADER_FLAGS_VOLUME = 0x00800000; // DDSD_DEPTH

	int DDS_HEADER_FLAGS_PITCH = 0x00000008; // DDSD_PITCH

	int DDS_HEADER_FLAGS_LINEARSIZE = 0x00080000; // DDSD_LINEARSIZE

	int DDS_SURFACE_FLAGS_TEXTURE = 0x00001000; // DDSCAPS_TEXTURE

	int DDS_SURFACE_FLAGS_MIPMAP = 0x00400008; // DDSCAPS_COMPLEX | DDSCAPS_MIPMAP

	int DDS_SURFACE_FLAGS_CUBEMAP = 0x00000008; // DDSCAPS_COMPLEX

	int DDS_CUBEMAP_POSITIVEX = 0x00000600; // DDSCAPS2_CUBEMAP | DDSCAPS2_CUBEMAP_POSITIVEX

	int DDS_CUBEMAP_NEGATIVEX = 0x00000a00; // DDSCAPS2_CUBEMAP | DDSCAPS2_CUBEMAP_NEGATIVEX

	int DDS_CUBEMAP_POSITIVEY = 0x00001200; // DDSCAPS2_CUBEMAP | DDSCAPS2_CUBEMAP_POSITIVEY

	int DDS_CUBEMAP_NEGATIVEY = 0x00002200; // DDSCAPS2_CUBEMAP | DDSCAPS2_CUBEMAP_NEGATIVEY

	int DDS_CUBEMAP_POSITIVEZ = 0x00004200; // DDSCAPS2_CUBEMAP | DDSCAPS2_CUBEMAP_POSITIVEZ

	int DDS_CUBEMAP_NEGATIVEZ = 0x00008200; // DDSCAPS2_CUBEMAP | DDSCAPS2_CUBEMAP_NEGATIVEZ

	int DDS_CUBEMAP_ALLFACES = (DDS_CUBEMAP_POSITIVEX | DDS_CUBEMAP_NEGATIVEX | DDS_CUBEMAP_POSITIVEY | DDS_CUBEMAP_NEGATIVEY
			| DDS_CUBEMAP_POSITIVEZ | DDS_CUBEMAP_NEGATIVEZ);

	int DDS_FLAGS_VOLUME = 0x00200000; // DDSCAPS2_VOLUME

	public static int MAKEFOURCC(char ch0, char ch1, char ch2, char ch3)
	{
		return (ch0 & 0xff) | ((ch1 & 0xff) << 8) | ((ch2 & 0xff) << 16) | ((ch3 & 0xff) << 24);
	}

	// Subset here matches D3D10_RESOURCE_DIMENSION and D3D11_RESOURCE_DIMENSION
	//enum DDS_RESOURCE_DIMENSION
	//{
	int DDS_DIMENSION_TEXTURE1D = 2;
	int DDS_DIMENSION_TEXTURE2D = 3;
	int DDS_DIMENSION_TEXTURE3D = 4;
	//};
	// Subset here matches D3D10_RESOURCE_MISC_FLAG and D3D11_RESOURCE_MISC_FLAG
	//enum DDS_RESOURCE_MISC_FLAG
	//{
	int DDS_RESOURCE_MISC_TEXTURECUBE = 0x4;
	//};

	//enum DDS_MISC_FLAGS2
	//{
	int DDS_MISC_FLAGS2_ALPHA_MODE_MASK = 0x7;
	//};

	//enum DDS_ALPHA_MODE
	//{
	int DDS_ALPHA_MODE_UNKNOWN = 0;
	int DDS_ALPHA_MODE_STRAIGHT = 1;
	int DDS_ALPHA_MODE_PREMULTIPLIED = 2;
	int DDS_ALPHA_MODE_OPAQUE = 3;
	int DDS_ALPHA_MODE_CUSTOM = 4;
	//};

	//DXGI_FORMAT

	static int DXGI_FORMAT_UNKNOWN = 0, DXGI_FORMAT_R32G32B32A32_TYPELESS = 1, DXGI_FORMAT_R32G32B32A32_FLOAT = 2,
			DXGI_FORMAT_R32G32B32A32_UINT = 3, DXGI_FORMAT_R32G32B32A32_SINT = 4, DXGI_FORMAT_R32G32B32_TYPELESS = 5,
			DXGI_FORMAT_R32G32B32_FLOAT = 6, DXGI_FORMAT_R32G32B32_UINT = 7, DXGI_FORMAT_R32G32B32_SINT = 8,
			DXGI_FORMAT_R16G16B16A16_TYPELESS = 9, DXGI_FORMAT_R16G16B16A16_FLOAT = 10, DXGI_FORMAT_R16G16B16A16_UNORM = 11,
			DXGI_FORMAT_R16G16B16A16_UINT = 12, DXGI_FORMAT_R16G16B16A16_SNORM = 13, DXGI_FORMAT_R16G16B16A16_SINT = 14,
			DXGI_FORMAT_R32G32_TYPELESS = 15, DXGI_FORMAT_R32G32_FLOAT = 16, DXGI_FORMAT_R32G32_UINT = 17, DXGI_FORMAT_R32G32_SINT = 18,
			DXGI_FORMAT_R32G8X24_TYPELESS = 19, DXGI_FORMAT_D32_FLOAT_S8X24_UINT = 20, DXGI_FORMAT_R32_FLOAT_X8X24_TYPELESS = 21,
			DXGI_FORMAT_X32_TYPELESS_G8X24_UINT = 22, DXGI_FORMAT_R10G10B10A2_TYPELESS = 23, DXGI_FORMAT_R10G10B10A2_UNORM = 24,
			DXGI_FORMAT_R10G10B10A2_UINT = 25, DXGI_FORMAT_R11G11B10_FLOAT = 26, DXGI_FORMAT_R8G8B8A8_TYPELESS = 27;
	static final int DXGI_FORMAT_R8G8B8A8_UNORM = 28;
	static final int DXGI_FORMAT_R8G8B8A8_UNORM_SRGB = 29;
	static int DXGI_FORMAT_R8G8B8A8_UINT = 30,
			DXGI_FORMAT_R8G8B8A8_SNORM = 31, DXGI_FORMAT_R8G8B8A8_SINT = 32, DXGI_FORMAT_R16G16_TYPELESS = 33,
			DXGI_FORMAT_R16G16_FLOAT = 34, DXGI_FORMAT_R16G16_UNORM = 35, DXGI_FORMAT_R16G16_UINT = 36, DXGI_FORMAT_R16G16_SNORM = 37,
			DXGI_FORMAT_R16G16_SINT = 38, DXGI_FORMAT_R32_TYPELESS = 39, DXGI_FORMAT_D32_FLOAT = 40, DXGI_FORMAT_R32_FLOAT = 41,
			DXGI_FORMAT_R32_UINT = 42, DXGI_FORMAT_R32_SINT = 43, DXGI_FORMAT_R24G8_TYPELESS = 44, DXGI_FORMAT_D24_UNORM_S8_UINT = 45,
			DXGI_FORMAT_R24_UNORM_X8_TYPELESS = 46, DXGI_FORMAT_X24_TYPELESS_G8_UINT = 47, DXGI_FORMAT_R8G8_TYPELESS = 48,
			DXGI_FORMAT_R8G8_UNORM = 49, DXGI_FORMAT_R8G8_UINT = 50, DXGI_FORMAT_R8G8_SNORM = 51, DXGI_FORMAT_R8G8_SINT = 52,
			DXGI_FORMAT_R16_TYPELESS = 53, DXGI_FORMAT_R16_FLOAT = 54, DXGI_FORMAT_D16_UNORM = 55, DXGI_FORMAT_R16_UNORM = 56,
			DXGI_FORMAT_R16_UINT = 57, DXGI_FORMAT_R16_SNORM = 58, DXGI_FORMAT_R16_SINT = 59, DXGI_FORMAT_R8_TYPELESS = 60;

	static final int DXGI_FORMAT_R8_UNORM = 61;

	int DXGI_FORMAT_R8_UINT = 62, DXGI_FORMAT_R8_SNORM = 63, DXGI_FORMAT_R8_SINT = 64, DXGI_FORMAT_A8_UNORM = 65, DXGI_FORMAT_R1_UNORM = 66,
			DXGI_FORMAT_R9G9B9E5_SHAREDEXP = 67, DXGI_FORMAT_R8G8_B8G8_UNORM = 68, DXGI_FORMAT_G8R8_G8B8_UNORM = 69,
			DXGI_FORMAT_BC1_TYPELESS = 70;

	static final int DXGI_FORMAT_BC1_UNORM = 71;

	static final int DXGI_FORMAT_BC1_UNORM_SRGB = 72;

	static int DXGI_FORMAT_BC2_TYPELESS = 73;

	static final int DXGI_FORMAT_BC2_UNORM = 74;

	static final int DXGI_FORMAT_BC2_UNORM_SRGB = 75;

	static int DXGI_FORMAT_BC3_TYPELESS = 76;

	static final int DXGI_FORMAT_BC3_UNORM = 77;

	static final int DXGI_FORMAT_BC3_UNORM_SRGB = 78;

	static int DXGI_FORMAT_BC4_TYPELESS = 79;

	static int DXGI_FORMAT_BC4_UNORM = 80;

	static final int DXGI_FORMAT_BC4_SNORM = 81;

	static int DXGI_FORMAT_BC5_TYPELESS = 82;

	static final int DXGI_FORMAT_BC5_UNORM = 83;

	static final int DXGI_FORMAT_BC5_SNORM = 84;

	static int DXGI_FORMAT_B5G6R5_UNORM = 85;

	static int DXGI_FORMAT_B5G5R5A1_UNORM = 86;

	static final int DXGI_FORMAT_B8G8R8A8_UNORM = 87;

	static int DXGI_FORMAT_B8G8R8X8_UNORM = 88;

	static int DXGI_FORMAT_R10G10B10_XR_BIAS_A2_UNORM = 89;

	static int DXGI_FORMAT_B8G8R8A8_TYPELESS = 90;

	static int DXGI_FORMAT_B8G8R8A8_UNORM_SRGB = 91;

	static int DXGI_FORMAT_B8G8R8X8_TYPELESS = 92;

	static int DXGI_FORMAT_B8G8R8X8_UNORM_SRGB = 93;

	static int DXGI_FORMAT_BC6H_TYPELESS = 94;

	static int DXGI_FORMAT_BC6H_UF16 = 95;

	static int DXGI_FORMAT_BC6H_SF16 = 96;

	static int DXGI_FORMAT_BC7_TYPELESS = 97;

	static final int DXGI_FORMAT_BC7_UNORM = 98;

	static int DXGI_FORMAT_BC7_UNORM_SRGB = 99;

	static int DXGI_FORMAT_AYUV = 100;

	static int DXGI_FORMAT_Y410 = 101;

	static int DXGI_FORMAT_Y416 = 102;

	static int DXGI_FORMAT_NV12 = 103;

	static int DXGI_FORMAT_P010 = 104;

	static int DXGI_FORMAT_P016 = 105;

	static int DXGI_FORMAT_420_OPAQUE = 106;

	static int DXGI_FORMAT_YUY2 = 107;

	static int DXGI_FORMAT_Y210 = 108;

	static int DXGI_FORMAT_Y216 = 109;

	static int DXGI_FORMAT_NV11 = 110;

	static int DXGI_FORMAT_AI44 = 111;

	static int DXGI_FORMAT_IA44 = 112;

	static int DXGI_FORMAT_P8 = 113;

	static int DXGI_FORMAT_A8P8 = 114;

	static int DXGI_FORMAT_B4G4R4A4_UNORM = 115;

	static int DXGI_FORMAT_FORCE_UINT = 0xffffffff;

}
