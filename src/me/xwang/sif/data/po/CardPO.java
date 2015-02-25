package me.xwang.sif.data.po;

public class CardPO {
	public int card_id;
	public int card_base_id;
	public int unit_navi_asset_id;
	public int navi_layer_order;
	public int navi_rotation;
	public int navi_move_x;
	public int navi_move_y;
	public double navi_size_ratio;

	@Override
	public String toString() {
		return "CardInfo [card_id=" + card_id + ", card_base_id="
				+ card_base_id + ", unit_navi_asset_id=" + unit_navi_asset_id
				+ ", navi_layer_order=" + navi_layer_order + ", navi_rotation="
				+ navi_rotation + ", navi_move_x=" + navi_move_x
				+ ", navi_move_y=" + navi_move_y + ", navi_size_ratio="
				+ navi_size_ratio + "]";
	}
}
