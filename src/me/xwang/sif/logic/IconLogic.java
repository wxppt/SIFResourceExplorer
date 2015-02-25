package me.xwang.sif.logic;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import me.xwang.sif.controller.ApplicationContext;
import me.xwang.sif.data.UnitData;
import me.xwang.sif.data.po.UnitPO;
import me.xwang.sif.entity.KLBTexture;

public class IconLogic {
	private File root;
	private String[] bk = { "", "com_win_19.png", "com_win_20.png",
			"com_win_18.png", "", "com_win_21.png" };
	private String[] star = { "", "com_icon_05.png", "com_icon_06.png",
			"com_icon_07.png", "com_icon_08.png", "com_icon_09.png",
			"com_icon_10.png", "com_icon_11.png", "com_icon_12.png" };

	public IconLogic() {
		root = new File(ApplicationContext.getAssertParent(),
				"assets/image/ui/common/");
	}

	public BufferedImage getIconImage(int unit_id, boolean isAweak) {
		UnitPO po = UnitData.getInstance().getUnitPO(unit_id);
		String iconPath = isAweak ? po.rank_max_live_asset
				: po.normal_live_asset;
		String bkName = bk[po.attribute_id];
		String starName = star[po.rarity*(isAweak?2:1)];
		ArrayList<BufferedImage> l = new ArrayList<BufferedImage>();
		TexbLogic logic = new TexbLogic();
		try {
			String bkTexbPath = logic
					.getLinkedTexbFile(new File(root, bkName + ".imag"));
			KLBTexture bkTexb = logic.read(new File(ApplicationContext.getAssertParent(), bkTexbPath));
			HashMap<File, BufferedImage> map = logic.getSplitImages(bkTexb);
			for(File f: map.keySet()) {
				if(f.getName().equals(bkName)) {
					l.add(map.get(f));
					break;
				}
			}
			String iconTexbPath = logic
					.getLinkedTexbFile(new File(ApplicationContext.getAssertParent(),iconPath + ".imag"));
			KLBTexture iconTexb = logic.read(new File(ApplicationContext.getAssertParent(), iconTexbPath));
			map = logic.getSplitImages(iconTexb);
			for(File f: map.keySet()) {
				if(f.getName().equals(new File(iconPath).getName())) {
					l.add(map.get(f));
					break;
				}
			}
			String starTexbPath = logic
					.getLinkedTexbFile(new File(root, starName + ".imag"));
			KLBTexture starTexb = logic.read(new File(ApplicationContext.getAssertParent(), starTexbPath));
			map = logic.getSplitImages(starTexb);
			for(File f: map.keySet()) {
				if(f.getName().equals(starName)) {
					l.add(map.get(f));
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ImgLogic().merge(l);
	}
	
	// public static void main(String[] args) {
	// ApplicationContext.setAssertParent(new File("d:/ll"));
	// ApplicationContext.setCardDbPath("card.db");
	// ApplicationContext.setUnitDbPath("unit.db");
	// BufferedImage img = new IconLogic().getIconImage(1, false);
	// try {
	// ImageIO.write(img, "png", new File("icon.png"));
	// } catch (IOException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// }
}
