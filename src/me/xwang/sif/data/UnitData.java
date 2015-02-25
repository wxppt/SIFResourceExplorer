package me.xwang.sif.data;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import me.xwang.sif.controller.ApplicationContext;
import me.xwang.sif.data.po.UnitPO;

public class UnitData {
	private static UnitData instance;
	private Connection conn;

	private UnitData() {
		init(ApplicationContext.getUnitDbPath());
	}

	public static UnitData getInstance() {
		return instance == null ? (instance = new UnitData()) : instance;
	}

	private void init(String path) {
		try {
			Class.forName("org.sqlite.JDBC");
			conn = DriverManager.getConnection("jdbc:sqlite:" + path);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Integer[] getUnitIds() {
		try {
			Statement stat = conn.createStatement();
			ResultSet rs = stat.executeQuery("select unit_id from unit_m;");
			ArrayList<Integer> list = new ArrayList<Integer>();
			while (rs.next()) {
				list.add(rs.getInt("unit_id"));
			}
			stat.close();
			rs.close();
			return list.toArray(new Integer[list.size()]);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public String getCardLayerAsset(int unit_navi_asset_id) {
		try {
			String colName = "unit_navi_asset";
			PreparedStatement stat = conn.prepareStatement("select " + colName
					+ " from unit_navi_asset_m where unit_navi_asset_id="+unit_navi_asset_id+";");
			ResultSet rs = stat.executeQuery();
			String result = null;
			if (rs.next()) {
				result = rs.getString(colName);
			}
			stat.close();
			rs.close();
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public UnitPO getUnitPO(int unit_id) {
		try {
			UnitPO po = new UnitPO();
			Field[] fields = po.getClass().getDeclaredFields();
			PreparedStatement stat = conn
					.prepareStatement("select * from unit_m where unit_id="
							+ unit_id + ";");
			ResultSet rs = stat.executeQuery();
			if (rs.next()) {
				for (int i = 0; i < fields.length; i++) {
					if (fields[i].getType().equals(java.lang.String.class)) {
						fields[i].set(po, rs.getString(fields[i].getName()));
					} else if (fields[i].getType().equals(int.class)) {
						fields[i].setInt(po, rs.getInt(fields[i].getName()));
					}
				}
			}
			stat.close();
			rs.close();
			return po;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public String getAttrName(int attribute_id) {
		try {
			String colName = "name";
			PreparedStatement stat = conn.prepareStatement("select " + colName
					+ " from unit_attribute_m where attribute_id="+attribute_id+";");
			ResultSet rs = stat.executeQuery();
			String result = null;
			if (rs.next()) {
				result = rs.getString(colName);
			}
			stat.close();
			rs.close();
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public String getRarityName(int rarity) {
		try {
			String colName = "name";
			PreparedStatement stat = conn.prepareStatement("select " + colName
					+ " from unit_rarity_m where unit_rarity_id="+rarity+";");
			ResultSet rs = stat.executeQuery();
			String result = null;
			if (rs.next()) {
				result = rs.getString(colName);
			}
			stat.close();
			rs.close();
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void main(String[] args) {
		UnitData.getInstance().init("unit.db");
		System.out.println(UnitData.getInstance().getUnitPO(1));
	}
}
