package me.xwang.sif.data.po;

public class UnitPO {
	/**
	 * 单元id
	 */
	public int unit_id;
	/**
	 * 单元NUM = ID
	 */
	public int unit_number;
	/**
	 * 链接unit_type表
	 */
	public int unit_type_id;
	/**
	 * 前置名
	 */
	public String eponym;
	/**
	 * 名字
	 */
	public String name;

	public int normal_card_id;
	public int rank_max_card_id;
	public int love_max_card_id;
	public int level_max_card_id;
	public int all_max_card_id;

	public String normal_live_asset;
	public String rank_max_live_asset;
	public String love_max_live_asset;
	public String level_max_live_asset;
	public String level_love_max_live_asset;

	public int normal_unit_navi_asset_id;
	public int rank_max_unit_navi_asset_id;
	/**
	 * 链接unit_rarity表
	 */
	public int rarity;
	/**
	 * 链接unit_attribute表
	 */
	public int attribute_id;

	// public int deck_cost;
	// public int default_unit_skill_id;
	// public String unit_skill_scene_asset;
	
	/**
	 * 链接asset_voice表
	 */
	public int skill_asset_voice_id;
	/**
	 * 链接unit_skill表
	 */
	public int default_leader_skill_id;
	/**
	 * 说的话？
	 */
	public String unit_message;
	/**
	 * cv
	 */
	public String unit_description;
	/**
	 * 低档绊
	 */
	public int before_love_max;
	/**
	 * 高档绊
	 */
	public int after_love_max;
	/**
	 * 低档等级
	 */
	public int before_level_max;
	/**
	 * 高档等级
	 */
	public int after_level_max;
	/**
	 * 能不能升级
	 */
	public int disable_rank_up;
	/**
	 * 链接unit_level_up_pattern表
	 */
	public int unit_level_up_pattern_id;
	/**
	 * 最大生命值
	 */
	public int hp_max;
	public int smile_max;
	public int pure_max;
	public int cool_max;
	/**
	 * 到满级的总消耗?
	 */
	public int rank_up_cost;
	@Override
	public String toString() {
		return "UnitPO [unit_id=" + unit_id + ", unit_number=" + unit_number
				+ ", unit_type_id=" + unit_type_id + ", eponym=" + eponym
				+ ", name=" + name + ", normal_card_id=" + normal_card_id
				+ ", rank_max_card_id=" + rank_max_card_id
				+ ", love_max_card_id=" + love_max_card_id
				+ ", level_max_card_id=" + level_max_card_id
				+ ", all_max_card_id=" + all_max_card_id
				+ ", normal_live_asset=" + normal_live_asset
				+ ", rank_max_live_asset=" + rank_max_live_asset
				+ ", love_max_live_asset=" + love_max_live_asset
				+ ", level_max_live_asset=" + level_max_live_asset
				+ ", level_love_max_live_asset=" + level_love_max_live_asset
				+ ", normal_unit_navi_asset_id=" + normal_unit_navi_asset_id
				+ ", rank_max_unit_navi_asset_id="
				+ rank_max_unit_navi_asset_id + ", rarity=" + rarity
				+ ", attribute_id=" + attribute_id + ", skill_asset_voice_id="
				+ skill_asset_voice_id + ", default_leader_skill_id="
				+ default_leader_skill_id + ", unit_message=" + unit_message
				+ ", unit_description=" + unit_description
				+ ", before_love_max=" + before_love_max + ", after_love_max="
				+ after_love_max + ", before_level_max=" + before_level_max
				+ ", after_level_max=" + after_level_max + ", disable_rank_up="
				+ disable_rank_up + ", unit_level_up_pattern_id="
				+ unit_level_up_pattern_id + ", hp_max=" + hp_max
				+ ", smile_max=" + smile_max + ", pure_max=" + pure_max
				+ ", cool_max=" + cool_max + ", rank_up_cost=" + rank_up_cost
				+ "]";
	}
	
	
}
