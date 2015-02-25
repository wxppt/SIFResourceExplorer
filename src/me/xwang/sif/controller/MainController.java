package me.xwang.sif.controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.stage.DirectoryChooser;

import javax.imageio.ImageIO;

import me.xwang.sif.entity.KLBFile;
import me.xwang.sif.entity.KLBTexture;
import me.xwang.sif.logic.CardLogic;
import me.xwang.sif.logic.IconLogic;
import me.xwang.sif.logic.Logger;
import me.xwang.sif.logic.TexbLogic;
import me.xwang.sif.logic.UnitLogic;
import me.xwang.sif.stage.ExportSingleTexbStage;
import me.xwang.sif.stage.LoadingStage;
import me.xwang.sif.stage.ProgressStage;
import me.xwang.sif.stage.WarningStage;
import me.xwang.sif.vo.CardVO;
import me.xwang.sif.vo.IconVO;

public class MainController {
	@FXML
	private TreeView<KLBFile> fileTreeView;
	@FXML
	private ImageView imageView;
	@FXML
	private Label imgNameLabel;
	@FXML
	private Label imgInfoLabel;
	@FXML
	private ListView<CardVO> cardListView;
	@FXML
	private ListView<IconVO> iconListView;
	
	@FXML
	public void onExportAllIcon() {
		if (iconListView.getItems().isEmpty()) {
			try {
				new WarningStage(ApplicationContext.getStage(),
						"请确认该栏目下有没有文件");
			} catch (Exception e) {
				e.printStackTrace();
			}
			return;
		}
		// 选择路径
		DirectoryChooser chooser = new DirectoryChooser();
		chooser.setTitle("选择导出路径");
		chooser.setInitialDirectory(ApplicationContext.getTracedFile());
		final File file = chooser.showDialog(ApplicationContext.getStage());
		if (file != null) {
			Task<Void> task = new Task<Void>() {
				protected Void call() throws Exception {
					UnitLogic unitLogic = new UnitLogic();
					IconLogic iconLogic = new IconLogic();
					Integer[] avas = unitLogic.getUnitIds();

					// init
					updateMessage("0/" + avas.length);
					updateProgress(0, avas.length);
					for (int i = 0; i < avas.length; i++) {
						BufferedImage img = iconLogic.getIconImage(avas[i],false);
						ImageIO.write(img, "png", new File(file, avas[i] + "_0"
								+ ".png"));
						img = iconLogic.getIconImage(avas[i],true);
						ImageIO.write(img, "png", new File(file, avas[i] + "_1"
								+ ".png"));
						updateMessage((i + 1) + "/" + avas.length);
						updateProgress((i + 1), avas.length);
					}
					return null;
				}
			};
			try {
				new ProgressStage(ApplicationContext.getStage(), task);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@FXML
	public void onIconSecClicked() {
		if (fileTreeView.getRoot().getChildren().size() != 2) {
			return;
		}
		KLBFile assetRoot = null;
		KLBFile dbRoot = null;
		for (TreeItem<KLBFile> node : fileTreeView.getRoot().getChildren()) {
			if (node.getValue().getName().equals("db")) {
				dbRoot = node.getValue();
			} else {
				assetRoot = node.getValue();
			}
		}
		File cardDbFile = new File(dbRoot.getAbsolutePath(), "unit/card.db_");
		File unitDbFile = new File(dbRoot.getAbsolutePath(), "unit/unit.db_");
		ApplicationContext.setCardDbPath(cardDbFile.getAbsolutePath());
		ApplicationContext.setUnitDbPath(unitDbFile.getAbsolutePath());
		ApplicationContext.setAssertParent(assetRoot.getParentFile());
		UnitLogic logic = new UnitLogic();
		
		Integer[] ids = logic.getUnitIds();
		for (int i = 0; i < ids.length; i++) {
			IconVO unitVO = new IconVO();
			unitVO.unit_id = ids[i];
			unitVO.awake = false;
			iconListView.getItems().add(unitVO);
			IconVO unitVO2 = new IconVO();
			unitVO2.unit_id = ids[i];
			unitVO2.awake = true;
			iconListView.getItems().add(unitVO2);
		}
	}

	@FXML
	public void onExportAllNavi() {
		if (cardListView.getItems().isEmpty()) {
			try {
				new WarningStage(ApplicationContext.getStage(),
						"请确认该栏目下有没有文件");
			} catch (Exception e) {
				e.printStackTrace();
			}
			return;
		}
		// 选择路径
		DirectoryChooser chooser = new DirectoryChooser();
		chooser.setTitle("选择导出路径");
		chooser.setInitialDirectory(ApplicationContext.getTracedFile());
		final File file = chooser.showDialog(ApplicationContext.getStage());
		if (file != null) {
			Task<Void> task = new Task<Void>() {

				protected Void call() throws Exception {
					CardLogic naviLogic = new CardLogic();
					Integer[] avas = naviLogic.getCardIds();

					// init
					updateMessage("0/" + avas.length);
					updateProgress(0, avas.length);
					for (int i = 0; i < avas.length; i++) {
						BufferedImage img = naviLogic.getCardImage(avas[i]);
						ImageIO.write(img, "png", new File(file, avas[i]
								+ ".png"));
						updateMessage((i + 1) + "/" + avas.length);
						updateProgress((i + 1), avas.length);
					}
					return null;
				}
			};
			try {
				new ProgressStage(ApplicationContext.getStage(), task);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@FXML
	public void onCardSecClicked() {
		if (fileTreeView.getRoot().getChildren().size() != 2) {
			return;
		}
		KLBFile assetRoot = null;
		KLBFile dbRoot = null;
		for (TreeItem<KLBFile> node : fileTreeView.getRoot().getChildren()) {
			if (node.getValue().getName().equals("db")) {
				dbRoot = node.getValue();
			} else {
				assetRoot = node.getValue();
			}
		}
		File cardDbFile = new File(dbRoot.getAbsolutePath(), "unit/card.db_");
		File unitDbFile = new File(dbRoot.getAbsolutePath(), "unit/unit.db_");
		ApplicationContext.setCardDbPath(cardDbFile.getAbsolutePath());
		ApplicationContext.setUnitDbPath(unitDbFile.getAbsolutePath());
		ApplicationContext.setAssertParent(assetRoot.getParentFile());
		CardLogic logic = new CardLogic();
		// TODO
		Integer[] avas = logic.getCardIds();
		for (int i = 0; i < avas.length; i++) {
			CardVO cardVO = new CardVO();
			cardVO.id = avas[i];
			cardListView.getItems().add(cardVO);
		}
	}

	@FXML
	public void onExportSingle() {
		try {
			if (ApplicationContext.getCurrentTexb() != null) {
				new ExportSingleTexbStage(ApplicationContext.getStage());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void onFileTreeKeyPressed(KeyEvent event) {
		// if (event.getCode() == KeyCode.DELETE) {
		// Collection<TreeItem<KLBFile>> coll = fileTreeView
		// .getSelectionModel().getSelectedItems();
		// final TreeItem<KLBFile> treeRoot = fileTreeView.getRoot();
		// treeRoot.getChildren().removeAll(coll);
		// }
	}

	@FXML
	public void onExportAllImg(ActionEvent event) {
		TreeItem<KLBFile> root = null;
		// find assets节点
		for (TreeItem<KLBFile> node : fileTreeView.getRoot().getChildren()) {
			if (node.getValue().getName().equals("assets")) {
				root = node;
				break;
			}
		}
		// assets节点不存在
		if (root == null) {
			try {
				new WarningStage(ApplicationContext.getStage(),
						"没有导入assets文件夹哦...");
			} catch (Exception e) {
				e.printStackTrace();
			}
			return;
		}
		// assets节点下为空
		if (root.getChildren().size() == 0) {
			try {
				new WarningStage(ApplicationContext.getStage(),
						"assets文件夹是空的哦...");
			} catch (Exception e) {
				e.printStackTrace();
			}
			return;
		}
		// 选择路径
		DirectoryChooser chooser = new DirectoryChooser();
		chooser.setTitle("选择导出路径");
		chooser.setInitialDirectory(ApplicationContext.getTracedFile());
		final TreeItem<KLBFile> texbRoot = root;
		final File file = chooser.showDialog(ApplicationContext.getStage());
		if (file != null) {
			Task<Void> task = new Task<Void>() {
				ArrayList<KLBFile> l = new ArrayList<KLBFile>();

				protected Void call() throws Exception {
					// init
					dfs(texbRoot);
					updateMessage("0/" + l.size());
					updateProgress(0, l.size());

					TexbLogic logic = new TexbLogic();
					int index = 0;
					for (KLBFile texbFile : l) {
						KLBTexture texture;
						try {
							texture = logic.read(texbFile);
						} catch (Exception e) {
							e.printStackTrace();
							Logger.log("Error in reading: " + texbFile);
							index++;
							updateMessage(index + "/" + l.size());
							updateProgress(index, l.size());
							continue;
						}
						HashMap<File, BufferedImage> map = logic
								.getSplitImages(texture);
						for (final File dstFile : map.keySet()) {
							String dirPath = file.getAbsolutePath() + "\\"
									+ dstFile.getPath();
							File dir = new File(dirPath.replaceAll(
									dstFile.getName() + ".*", ""));
							if (!dir.exists()) {
								dir.mkdirs();
							}
							ImageIO.write(map.get(dstFile), "png", new File(
									dirPath));
						}
						index++;
						updateMessage(index + "/" + l.size());
						updateProgress(index, l.size());
					}
					return null;
				}

				private void dfs(TreeItem<KLBFile> item) {
					if (item.getValue().getType() == KLBFile.DIR) {
						for (final TreeItem<KLBFile> child : item.getChildren()) {
							dfs(child);
						}
					} else if (item.getValue().getType() == KLBFile.TEXB) {
						l.add(item.getValue());
					}
				}
			};
			try {
				new ProgressStage(ApplicationContext.getStage(), task);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@FXML
	public void onGenerateCard(ActionEvent event) {
		System.out.println("generate card");
	}

	@FXML
	public void onImportAssets(ActionEvent event) {
		DirectoryChooser chooser = new DirectoryChooser();
		chooser.setTitle("导入assets文件夹...");
		chooser.setInitialDirectory(ApplicationContext.getTracedFile());
		final File file = chooser.showDialog(ApplicationContext.getStage());
		if (file != null) {
			if (!file.getName().equals("assets")) {
				try {
					new WarningStage(ApplicationContext.getStage(),
							"这个文件夹的名字并不是assets哦...");
				} catch (Exception e) {
					e.printStackTrace();
				}
				return;
			}
			ApplicationContext.setTracedFile(file.getParentFile());
			try {
				new LoadingStage(ApplicationContext.getStage(),
						new Task<Void>() {
							protected Void call() throws Exception {
								TreeItem<KLBFile> root = fileTreeView.getRoot();
								dfs(file, root);
								updateProgress(1, 1);
								return null;
							}

							private boolean dfs(File file,
									TreeItem<KLBFile> root) {
								boolean flag = false;
								if (file.isDirectory()) {
									KLBFile dirFile = new KLBFile(file);
									dirFile.setValid(false);
									dirFile.setType(KLBFile.DIR);
									TreeItem<KLBFile> item = new TreeItem<KLBFile>(
											dirFile);
									File[] fs = file.listFiles();
									for (int i = 0; i < fs.length; i++) {
										if (dfs(fs[i], item)) {
											flag = true;
										}
									}
									if (flag) {
										root.getChildren().add(item);
									}
									return flag;
								} else {
									KLBFile validFile = new KLBFile(file);
									validFile.setValid(true);
									if (file.getName().endsWith(".texb")) {
										validFile.setType(KLBFile.TEXB);
										TreeItem<KLBFile> item = new TreeItem<KLBFile>(
												validFile);
										root.getChildren().add(item);
										return true;
									} else if (file.getName().endsWith(".imag")) {
										validFile.setType(KLBFile.IMAG);
									} else {
										validFile.setType(KLBFile.UNKNOWN);
									}
									return false;
								}
							}
						}, null);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@FXML
	public void onImportDb(ActionEvent event) {
		DirectoryChooser chooser = new DirectoryChooser();
		chooser.setTitle("导入db文件夹...");
		chooser.setInitialDirectory(ApplicationContext.getTracedFile());
		final File file = chooser.showDialog(ApplicationContext.getStage());
		if (file != null) {
			if (!file.getName().equals("db")) {
				try {
					new WarningStage(ApplicationContext.getStage(),
							"这个文件夹的名字并不是db哦...");
				} catch (Exception e) {
					e.printStackTrace();
				}
				return;
			}
			ApplicationContext.setTracedFile(file.getParentFile());
			try {
				new LoadingStage(ApplicationContext.getStage(),
						new Task<Void>() {

							protected Void call() throws Exception {
								TreeItem<KLBFile> root = fileTreeView.getRoot();
								dfs(file, root);
								updateProgress(1, 1);
								return null;
							}

							private boolean dfs(File file,
									TreeItem<KLBFile> root) {
								boolean flag = false;
								if (file.isDirectory()) {
									KLBFile dirFile = new KLBFile(file);
									dirFile.setValid(false);
									dirFile.setType(KLBFile.DIR);
									TreeItem<KLBFile> item = new TreeItem<KLBFile>(
											dirFile);
									File[] fs = file.listFiles();
									for (int i = 0; i < fs.length; i++) {
										if (dfs(fs[i], item)) {
											flag = true;
										}
									}
									if (flag) {
										root.getChildren().add(item);
									}
									return flag;
								} else {
									KLBFile validFile = new KLBFile(file);
									validFile.setValid(true);
									if (file.getName().endsWith(".db_")
											|| file.getName().endsWith(".db")) {
										validFile.setType(KLBFile.DB);
										TreeItem<KLBFile> item = new TreeItem<KLBFile>(
												validFile);
										root.getChildren().add(item);
										return true;
									} else {
										validFile.setType(KLBFile.UNKNOWN);
									}
									return false;
								}
							}
						}, null);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
