package me.xwang.sif.entity;

public class KLBVertice {
	public long x;
	public long y;
	public long u;
	public long v;
	public String getVerticeInfo() {
		return "[("+x+","+y+")("+u+","+v+")]";
	}
}
