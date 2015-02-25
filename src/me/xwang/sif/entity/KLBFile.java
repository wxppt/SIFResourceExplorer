package me.xwang.sif.entity;

import java.io.File;
import java.net.URI;

public class KLBFile extends File {
	public static int UNKNOWN = -1;
	public static int DIR = 0;
	public static int IMAG = 1;
	public static int TEXB = 2;
	public static int DB = 3;
	private static final long serialVersionUID = 755970631802952992L;
	private int type;
	private boolean valid;

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public boolean isValid() {
		return valid;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}

	public KLBFile(File parent, String child) {
		super(parent, child);
	}

	public KLBFile(String parent, String child) {
		super(parent, child);
	}

	public KLBFile(String pathname) {
		super(pathname);
	}

	public KLBFile(File file) {
		super(file.getAbsolutePath());
	}

	public KLBFile(URI uri) {
		super(uri);
	}

	@Override
	public String toString() {
		if (valid) {
			return this.getName() + "   (" + this.getAbsolutePath() + ")";
		} else {
			return "F) "+this.getName() + "   (" + this.getAbsolutePath() + ")";
		}
	}
}
