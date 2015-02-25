package me.xwang.sif.entity;

public class KLBImage {
	public String tag;
	public long size;
	public long pathlen;
	public String path;
	public int vertexLen;
	public int indexLen;
	public int width;
	public int height;
	public int centerX;
	public int centerY;
	public KLBVertice[] vertices;
	public byte[] indeces;
	
	public String getImageInfo() {
		StringBuilder sb = new StringBuilder();
		sb.append("Tag: " + tag + "\n");
		sb.append("Size: " + size + "\n");
		sb.append("Path: " + path + "\n");
		sb.append("VertexLen: " + vertexLen + "\n");
		sb.append("IndexLen: " + indexLen + "\n");
		sb.append("Width: " + width + "\n");
		sb.append("Height: " + height + "\n");
		sb.append("CenterX: " + centerX + "\n");
		sb.append("CenterY: " + centerY + "\n");
		sb.append("Vertices: \n");
		for(int i = 0; i < vertices.length; i++) {
			sb.append("\t"+vertices[i].getVerticeInfo() + "\n");
		}
		sb.append("Indeces: (");
		for(int i = 0; i < indeces.length; i++) {
			sb.append(indeces[i] + ",");
		}
		sb.deleteCharAt(sb.length()-1);
		sb.append(")\n");
		return sb.toString();
	}
}
