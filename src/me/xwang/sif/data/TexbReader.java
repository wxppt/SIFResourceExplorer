package me.xwang.sif.data;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.zip.InflaterInputStream;

public class TexbReader {
	private DataInputStream dis;

	public TexbReader(File file) throws Exception {
		FileInputStream fis = new FileInputStream(file);
		dis = new DataInputStream(fis);

	}

	public void close() throws Exception {
		dis.close();
	}

	public String readString(long len) throws Exception {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < len; i++) {
			sb.append((char) dis.readUnsignedByte());
		}
		return sb.toString();
	}

	public long readU32() throws Exception {
		long u32 = 0;
		long b1 = dis.readUnsignedByte();
		long b2 = dis.readUnsignedByte();
		long b3 = dis.readUnsignedByte();
		long b4 = dis.readUnsignedByte();
		u32 = (b1 << 24) + (b2 << 16) + (b3 << 8) + b4;
		return u32;
	}

	public int readU16() throws Exception {
		int u16 = 0;
		int b1 = dis.readUnsignedByte();
		int b2 = dis.readUnsignedByte();
		u16 = (b1 << 8) + b2;
		return u16;
	}

	public int readU8() throws Exception {
		return dis.readUnsignedByte();
	}

	public byte readByte() throws Exception {
		return dis.readByte();
	}

	public String readHex(int len) throws Exception {
		StringBuilder sb = new StringBuilder();
		byte[] b = new byte[len];
		dis.read(b);
		for (int i = 0; i < b.length; i++) {
			String hex = Integer.toHexString(b[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			sb.append(hex.toUpperCase());
		}
		return sb.toString();
	}

	public String readBin(int len) throws Exception {
		StringBuilder sb = new StringBuilder();
		byte[] b = new byte[len];
		dis.read(b);
		for (int i = 0; i < b.length; i++) {
			String bin = Integer.toBinaryString(b[i] & 0xFF);
			for (int j = 8; j > bin.length(); j--) {
				sb.append(0);
			}
			sb.append(bin);
		}
		return sb.toString();
	}

	public byte[] getDecompressedBitMap() throws Exception {
		InflaterInputStream iis = new InflaterInputStream(dis);
		ByteArrayOutputStream o = new ByteArrayOutputStream(1024);
		int i = 1024;
		byte[] buf = new byte[i];

		while ((i = iis.read(buf, 0, i)) > 0) {
			o.write(buf, 0, i);
		}

		return o.toByteArray();
	}

	public byte[] getFlatBitMap() throws Exception {
		byte[] b = new byte[1024];
		int len = 0;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);
		while ((len = dis.read(b)) != -1) {
			dos.write(b, 0, len);
		}
		return baos.toByteArray();
	}
}
