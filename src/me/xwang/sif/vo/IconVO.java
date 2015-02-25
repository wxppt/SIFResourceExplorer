package me.xwang.sif.vo;

public class IconVO {
	public int unit_id;
	public boolean awake;
	@Override
	public String toString() {
		return unit_id +"  "+ (awake?"觉醒":"一般");
	}
	
	
}
