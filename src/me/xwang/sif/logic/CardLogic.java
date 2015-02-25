package me.xwang.sif.logic;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import me.xwang.sif.controller.ApplicationContext;
import me.xwang.sif.data.CardData;
import me.xwang.sif.data.UnitData;
import me.xwang.sif.data.po.CardPO;
import me.xwang.sif.entity.KLBTexture;

public class CardLogic {
	private File rootPath;

	public static void main(String[] args) throws IOException {
		ApplicationContext.setAssertParent(new File("d:/ll"));
		ApplicationContext.setCardDbPath("card.db");
		ApplicationContext.setUnitDbPath("unit.db");
		BufferedImage img = new CardLogic().getCardImage(331);
		ImageIO.write(img, "png", new File("test.png"));
	}

	public CardLogic() {
		this.rootPath = ApplicationContext.getAssertParent();
	}

	public Integer[] getCardIds() {
		Integer[] ids = CardData.getInstance().getCardIdList();
		return ids;
	}

	public BufferedImage getCardImage(int card_id) {
		CardPO info = CardData.getInstance().getCardInfo(card_id);
		int[] layerIds = CardData.getInstance().getLayersIdInfo(
				info.card_base_id);
		BufferedImage[] images = new BufferedImage[10];
		TexbLogic texbl = new TexbLogic();
		for (int i = 0; i < layerIds.length; i++) {
			if (layerIds[i] == 0) {
				continue;
			}
			String imagPath = CardData.getInstance().getCardLayerAsset(
					layerIds[i])
					+ ".imag";
			String path = texbl.getLinkedTexbFile(new File(rootPath, imagPath));
			KLBTexture texture = null;
			try {
				texture = texbl.read(new File(rootPath, path));
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}
			images[i] = texbl.getFitImage(texture);
		}
		String naviImagPath = UnitData.getInstance().getCardLayerAsset(
				info.unit_navi_asset_id)
				+ ".imag";
		String naviPath = texbl.getLinkedTexbFile(new File(rootPath,
				naviImagPath));
		KLBTexture naviTexb;
		try {
			naviTexb = texbl.read(new File(rootPath, naviPath));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		BufferedImage naviImg = (BufferedImage) naviTexb.awtImg;
		ImgLogic imgl = new ImgLogic();
		naviImg = imgl.resize(naviImg, info.navi_size_ratio);
		naviImg = imgl.move(naviImg, info.navi_move_x, info.navi_move_y);
		if (info.navi_layer_order != 1) {
			images[info.navi_layer_order] = naviImg;
		}
		ArrayList<BufferedImage> list = new ArrayList<BufferedImage>();
		for (int i = 0; i < images.length; i++) {
			if (images[i] != null) {
				list.add(images[i]);
			}
		}
		BufferedImage output = imgl.merge(list);
		return output;
	}
}
