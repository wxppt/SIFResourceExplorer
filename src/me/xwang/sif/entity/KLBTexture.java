package me.xwang.sif.entity;

import java.awt.Image;
import java.io.File;
import java.util.ArrayList;

public class KLBTexture {
	public File file;
	public String tag;
	public long size;
	public long pathlen;
	public String path;
	public int containerWidth;
	public int containerHeight;
	public String type;
	public boolean isCompressed;
	public boolean isMipMap;
	public boolean isDoubleBuff;
	public String pixelFormat;
	public int vertexLen;
	public int indexLen;
	public int imgCnt;
	public ArrayList<KLBImage> imgs = new ArrayList<KLBImage>();
	public Image awtImg;

	public String getTexbInfo() {
		StringBuilder sb = new StringBuilder();
		sb.append("Tag: " + tag + "\n");
		sb.append("Size: " + size + "\n");
		sb.append("Path: " + path + "\n");
		sb.append("ContainerWidth: " + containerWidth + "\n");
		sb.append("ContainerHeight: " + containerHeight + "\n");
		sb.append("Type: " + type + "\n");
		sb.append("Compressed: " + isCompressed + "\n");
		sb.append("MipMap: " + isMipMap + "\n");
		sb.append("DoubleBuff: " + isDoubleBuff + "\n");
		sb.append("PixelFormat: " + pixelFormat + "\n");
		sb.append("VertexLen: " + vertexLen + "\n");
		sb.append("IndexLen: " + indexLen + "\n");
		sb.append("ImgCnt: " + imgCnt + "\n");
		for (KLBImage img : imgs) {
			sb.append("----------\n");
			sb.append(img.getImageInfo());
		}
		return sb.toString();
	}
}
