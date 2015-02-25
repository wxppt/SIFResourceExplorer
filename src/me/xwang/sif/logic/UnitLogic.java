package me.xwang.sif.logic;

import me.xwang.sif.data.UnitData;

public class UnitLogic {

	public Integer[] getUnitIds() {
		UnitData data = UnitData.getInstance();
		Integer[] ids = data.getUnitIds();
		return ids;
	}

}
