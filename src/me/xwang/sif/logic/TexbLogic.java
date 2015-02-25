package me.xwang.sif.logic;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.zip.Inflater;

import me.xwang.sif.data.TexbReader;
import me.xwang.sif.entity.KLBImage;
import me.xwang.sif.entity.KLBTexture;
import me.xwang.sif.entity.KLBVertice;

public class TexbLogic {
	public HashMap<File,BufferedImage> getSplitImages(KLBTexture texture) {
		HashMap<File,BufferedImage> map = new HashMap<File,BufferedImage>();
		for(KLBImage img: texture.imgs) {
			long x1 = img.vertices[0].u;
			long y1 = img.vertices[0].v;
			long x2 = img.vertices[img.vertexLen-1].u;
			long y2 = img.vertices[img.vertexLen-1].v;
			BufferedImage bimg = ((BufferedImage)texture.awtImg).getSubimage((int)x1, (int)y1, (int)(x2 - x1), (int)(y2 - y1));
			File f = new File(img.path.replaceAll("\\.imag.*", ""));
			map.put(f, bimg);
		}
		return map;
	}
	public BufferedImage getFitImage(KLBTexture texture) {
		ArrayList<KLBVertice> vertices = new ArrayList<KLBVertice>();
		for(KLBImage img: texture.imgs) {
			vertices.add(img.vertices[0]);
			vertices.add(img.vertices[img.vertexLen-1]);
		}
		Collections.sort(vertices, new Comparator<KLBVertice>() {
			public int compare(KLBVertice v1, KLBVertice v2) {
				return (int) ((v1.u + v1.v) - (v2.u + v2.v));
			}
		});
		long x1 = vertices.get(0).u;
		long y1 = vertices.get(0).v;
		long x2 = vertices.get(vertices.size()-1).u;
		long y2 = vertices.get(vertices.size()-1).v;
		final BufferedImage dst = ((BufferedImage)texture.awtImg).getSubimage((int)x1, (int)y1, (int)(x2 - x1), (int)(y2 - y1));
		return dst;
	}
	
	public KLBTexture read(File texbFile) throws Exception {
		TexbReader tl = new TexbReader(texbFile);
		KLBTexture f = new KLBTexture();
		f.tag = tl.readString(4);
		f.size = tl.readU32();
		f.pathlen = tl.readU16();
		f.path = tl.readString(f.pathlen);
		f.path = f.path.replaceAll("^T", "");
		f.containerWidth = tl.readU16();
		f.containerHeight = tl.readU16();
		tl.readByte();
		String typeCode = tl.readBin(1);
		f.type = getImgType(Integer.valueOf(typeCode.substring(5), 2));
		f.isCompressed = typeCode.charAt(4) == '1';
		f.isMipMap = typeCode.charAt(3) == '1';
		f.isDoubleBuff = typeCode.charAt(2) == '1';
		f.pixelFormat = getPixelType(Integer.valueOf(typeCode.substring(0, 2),
				2));
		f.vertexLen = tl.readU16();
		f.indexLen = tl.readU16();
		f.imgCnt = tl.readU16();
		for (int i = 0; i < f.imgCnt; i++) {
			KLBImage img = new KLBImage();
			img.tag = tl.readString(4);
			img.size = tl.readU16();
			img.pathlen = tl.readU16();
			img.path = tl.readString(img.pathlen);
			img.path = img.path.replaceAll("^I", "");
			String flag = tl.readHex(2);
			if (!flag.equals("FFFF")) {
				Logger.log("未知的Flag：" + flag);
				continue;
			}
			int imgTypeFlag = tl.readU16();
			int[] flagArr = { tl.readU8(), tl.readU8(), (int) tl.readU32() };
			if (flagArr[0] != 0 || flagArr[1] != 0 || flagArr[2] != 1) {
				Logger.log("未知的Flag组：" + flagArr[0] + "," + flagArr[1] + ","
						+ flagArr[2]);
				continue;
			}
			if (imgTypeFlag >= 5) {
				imgTypeFlag = tl.readU8();
				if (imgTypeFlag == 1) {
					Logger.log("该图片是ScrollBar");
				} else if (imgTypeFlag == 5) {
					Logger.log("该图片是Scale9");
				} else {
					Logger.log("该图片的KLB类型未知");
				}
				throw new RuntimeException("Not Implements.");
			}
			tl.readU16();
			img.vertexLen = tl.readU8();
			img.indexLen = tl.readU8();
			img.width = tl.readU16();
			img.height = tl.readU16();
			img.centerX = tl.readU16();
			img.centerY = tl.readU16();
			ArrayList<KLBVertice> vertices = new ArrayList<KLBVertice>();
			for (int j = 0; j < img.vertexLen; j++) {
				KLBVertice vertice = new KLBVertice();
				vertice.x = tl.readU32() / 0x10000;
				vertice.y = tl.readU32() / 0x10000;
				vertice.u = tl.readU32() * f.containerWidth / 0x10000;
				vertice.v = tl.readU32() * f.containerHeight / 0x10000;
				vertices.add(vertice);
			}
			Collections.sort(vertices, new Comparator<KLBVertice>() {
				public int compare(KLBVertice v1, KLBVertice v2) {
					return (int) ((v1.u + v1.v) - (v2.u + v2.v));
				}
			});
			img.vertices = vertices.toArray(new KLBVertice[vertices.size()]);

			img.indeces = new byte[img.indexLen];
			for (int j = 0; j < img.indexLen; j++) {
				img.indeces[j] = tl.readByte();
			}
			f.imgs.add(img);
		}
		long compressType = tl.readU32();
		byte[] bitmap = tl.getFlatBitMap();
		if (f.isCompressed) {
			if (compressType != 0) {
				Logger.log("未知的压缩格式");
				return null;
			}
			bitmap = decompress(bitmap);
		}

		int[] pixelArr = new int[f.containerWidth * f.containerHeight];
		if (f.pixelFormat.equals("4444 RGBA")) {
			int ptr = 0;
			for (int i = 0; i < bitmap.length; i++) {
				if ((bitmap[i] & 0x0F) == 0x0F) {
					pixelArr[ptr] = (0xFF) << 24; // A
				} else {
					pixelArr[ptr] = (bitmap[i] & 0x0F) << 28; // A
				}
				pixelArr[ptr] += (bitmap[i + 1] & 0xF0) << 16; // R
				pixelArr[ptr] += (bitmap[i + 1] & 0x0F) << 12; // G
				pixelArr[ptr] += (bitmap[i] & 0xF0); // B
				i++;
				ptr++;
			}
		} else if (f.pixelFormat.equals("5551 RGBA")) {
			int ptr = 0;
			for (int i = 0; i < bitmap.length; i++) {
				if ((bitmap[i] & 0x01) == 1) {
					pixelArr[ptr] = (0xFF) << 28; // A
				} else {
					pixelArr[ptr] = (0x00) << 28; // A
				}
				pixelArr[ptr] += (bitmap[i + 1] & 0xF8) << 16; // R
				pixelArr[ptr] += (bitmap[i + 1] & 0x07) << 13; // G
				pixelArr[ptr] += (bitmap[i] & 0xC0) << 5; // G
				pixelArr[ptr] += (bitmap[i] & 0x3E) << 2; // B
				i++;
				ptr++;
			}
		} else if (f.pixelFormat.equals("565 RGB")) {
			int ptr = 0;
			for (int i = 0; i < bitmap.length; i++) {
				pixelArr[ptr] = (0xFF) << 28; // A
				pixelArr[ptr] += (bitmap[i + 1] & 0xF8) << 16; // R
				pixelArr[ptr] += (bitmap[i + 1] & 0x07) << 13; // G
				pixelArr[ptr] += (bitmap[i] & 0xE0) << 5; // G
				pixelArr[ptr] += (bitmap[i] & 0x1F) << 3; // B
				i++;
				ptr++;
			}
		} else if (f.pixelFormat.equals("Byte")) {
			int ptr = 0;
			for (int i = 0; i < bitmap.length; i++) {
				pixelArr[ptr] = (bitmap[i + 3] & 0xFF) << 24; // A
				pixelArr[ptr] += (bitmap[i] & 0xFF) << 16; // R
				pixelArr[ptr] += (bitmap[i + 1] & 0xFF) << 8; // G
				pixelArr[ptr] += (bitmap[i + 2] & 0xFF); // B
				i += 3;
				ptr++;
			}
		}
		BufferedImage image = new BufferedImage(f.containerWidth,
				f.containerHeight, BufferedImage.TYPE_INT_ARGB);
		image.setRGB(0, 0, f.containerWidth, f.containerHeight, pixelArr, 0,
				f.containerWidth);
		tl.close();
		f.awtImg = image;
		return f;
	}

	private String getImgType(int code) {
		String[] arr = { "ALPHA", "LUMA", "LUMALPHA", "RGB", "RGBA" };
		return arr[code];
	}

	private String getPixelType(int code) {
		String[] arr = { "565 RGB", "5551 RGBA", "4444 RGBA", "Byte" };
		return arr[code];
	}

	private byte[] decompress(byte[] data) {
		byte[] output = new byte[0];

		Inflater decompresser = new Inflater();
		decompresser.reset();
		decompresser.setInput(data);

		ByteArrayOutputStream o = new ByteArrayOutputStream(data.length);
		try {
			byte[] buf = new byte[1024];
			while (!decompresser.finished()) {
				int i = decompresser.inflate(buf);
				o.write(buf, 0, i);
			}
			output = o.toByteArray();
		} catch (Exception e) {
			output = data;
			e.printStackTrace();
		} finally {
			try {
				o.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		decompresser.end();
		return output;
	}
	
	public String getLinkedTexbFile(File imagFile) {
		try {
			TexbReader reader = new TexbReader(imagFile);
			reader.readString(4);
			long len = reader.readU32();
			String s = reader.readString(len).trim();
			reader.close();
			return s;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
