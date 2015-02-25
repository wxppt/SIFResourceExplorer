package me.xwang.sif.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import me.xwang.sif.controller.ApplicationContext;
import me.xwang.sif.data.po.CardPO;

public class CardData {
	// Test
	// public static void main(String[] args) {
	// ApplicationContext.setCardDbPath(new File("card.db").getAbsolutePath());
	// System.out.println(CardData.getInstance().getCardIdList().length);
	// System.out.println(CardData.getInstance().getCardInfo(1));
	// System.out.println(CardData.getInstance().getCardLayerAsset(1));
	// System.out.println(CardData.getInstance().getLayersIdInfo(1)[8]);
	// }

	private static CardData instance;
	private Connection conn;

	private CardData() {
		init(ApplicationContext.getCardDbPath());
	}

	public static CardData getInstance() {
		return instance == null ? (instance = new CardData()) : instance;
	}

	private void init(String path) {
		try {
			Class.forName("org.sqlite.JDBC");
			conn = DriverManager.getConnection("jdbc:sqlite:" + path);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public int[] getLayersIdInfo(int card_base_id) {
		int[] layers = new int[10];
		try {
			PreparedStatement stat = conn
					.prepareStatement("select * from card_base_m where card_base_id="
							+ card_base_id + ";");
			ResultSet rs = stat.executeQuery();
			while (rs.next()) {
				layers[rs.getInt("layer_order")] = rs.getInt("card_layer_id");
			}
			stat.close();
			rs.close();
			return layers;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String getCardLayerAsset(int card_layer_id) {
		try {
			String colName = "card_layer_asset";
			PreparedStatement stat = conn.prepareStatement("select " + colName
					+ " from card_layer_m where card_layer_id=?;");
			stat.setInt(1, card_layer_id);
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

	public CardPO getCardInfo(int card_id) {
		try {
			CardPO info = new CardPO();
			PreparedStatement stat = conn
					.prepareStatement("select * from card_m where card_id=?;");
			stat.setInt(1, card_id);
			ResultSet rs = stat.executeQuery();
			if (rs.next()) {
				info.card_id = rs.getInt("card_id");
				info.card_base_id = rs.getInt("card_base_id");
				info.unit_navi_asset_id = rs.getInt("unit_navi_asset_id");
				info.navi_layer_order = rs.getInt("navi_layer_order");
				info.navi_move_x = rs.getInt("navi_move_x");
				info.navi_move_y = rs.getInt("navi_move_y");
				info.navi_rotation = rs.getInt("navi_rotation");
				info.navi_size_ratio = rs.getDouble("navi_size_ratio");
			}
			stat.close();
			rs.close();
			return info;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public Integer[] getCardIdList() {
		try {
			Statement stat = conn.createStatement();
			ResultSet rs = stat.executeQuery("select card_id from card_m;");
			ArrayList<Integer> list = new ArrayList<Integer>();
			while (rs.next()) {
				list.add(rs.getInt("card_id"));
			}
			stat.close();
			rs.close();
			return list.toArray(new Integer[list.size()]);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
