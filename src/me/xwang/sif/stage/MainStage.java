package me.xwang.sif.stage;

import java.awt.image.BufferedImage;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.stage.Stage;
import me.xwang.sif.controller.ApplicationContext;
import me.xwang.sif.entity.KLBFile;
import me.xwang.sif.entity.KLBTexture;
import me.xwang.sif.logic.CardLogic;
import me.xwang.sif.logic.IconLogic;
import me.xwang.sif.logic.TexbLogic;
import me.xwang.sif.vo.CardVO;
import me.xwang.sif.vo.IconVO;

public class MainStage extends Application {
	private WritableImage curImg;

	public void init(TreeView<KLBFile> fileTreeView) {
		final TreeItem<KLBFile> treeRoot = new TreeItem<KLBFile>();
		fileTreeView.setRoot(treeRoot);
		fileTreeView.setShowRoot(false);
		treeRoot.setExpanded(true);
	}

	@Override
	@SuppressWarnings("unchecked")
	public void start(Stage stage) throws Exception {
		ApplicationContext.setStage(stage);
		final Parent root = FXMLLoader
				.load(getClass().getResource("Main.fxml"));
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.setTitle("SIF Resource Explorer");
		stage.show();
		final TreeView<KLBFile> fileTreeView = (TreeView<KLBFile>) root
				.lookup("#fileTreeView");
		fileTreeView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		fileTreeView.getSelectionModel().selectedItemProperty()
				.addListener(new ChangeListener<TreeItem<KLBFile>>() {
					@Override
					public void changed(
							ObservableValue<? extends TreeItem<KLBFile>> observable,
							TreeItem<KLBFile> oldValue,
							final TreeItem<KLBFile> newValue) {
						if (newValue == null) {
							return;
						}
						final KLBFile klbFile = newValue.getValue();
						if (klbFile.getType() != KLBFile.TEXB) {
							return;
						}
						try {
							new LoadingStage(ApplicationContext.getStage(),
									new Task<Void>() {
										protected Void call() {
											try {
												TexbLogic logic = new TexbLogic();
												KLBTexture texture;
												texture = logic.read(klbFile);
												BufferedImage showImg = logic
														.getFitImage(texture);
												curImg = SwingFXUtils
														.toFXImage(showImg,
																null);
												ApplicationContext
														.setCurrentTexb(texture);
												updateProgress(1, 1);
											} catch (Exception e) {
												e.printStackTrace();
											}
											return null;
										}
									}, new MyCallBack() {
										public void run() {
											final Label namelb = (Label) root
													.lookup("#imgNameLabel");
											namelb.setText(klbFile.getName());
											final ImageView imageView = (ImageView) root
													.lookup("#imageView");
											imageView.setImage(curImg);
											final Label infolb = (Label) root
													.lookup("#imgInfoLabel");
											infolb.setText(ApplicationContext
													.getCurrentTexb()
													.getTexbInfo());

										}
									});

						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
		final ListView<CardVO> cardListView = (ListView<CardVO>) root
				.lookup("#cardListView");
		cardListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		cardListView.getSelectionModel().selectedItemProperty()
				.addListener(new ChangeListener<CardVO>() {
					@Override
					public void changed(
							ObservableValue<? extends CardVO> observable,
							CardVO oldValue, CardVO newValue) {
						if (newValue == null) {
							return;
						}
						final int id = newValue.id;
						try {
							new LoadingStage(ApplicationContext.getStage(),
									new Task<Void>() {
										protected Void call() {
											try {
												CardLogic logic = new CardLogic();
												BufferedImage showImg = logic.getCardImage(id);
												curImg = SwingFXUtils
														.toFXImage(showImg,
																null);
												KLBTexture texture = new KLBTexture();
												texture.path = id+".png";
												texture.awtImg = showImg;
												ApplicationContext
														.setCurrentTexb(texture);
												updateProgress(1, 1);
											} catch (Exception e) {
												e.printStackTrace();
											}
											return null;
										}
									}, new MyCallBack() {
										public void run() {
											final Label namelb = (Label) root
													.lookup("#imgNameLabel");
											namelb.setText(id+"");
											final ImageView imageView = (ImageView) root
													.lookup("#imageView");
											imageView.setImage(curImg);
											final Label infolb = (Label) root
													.lookup("#imgInfoLabel");
											infolb.setText("TODO Navi Info");
										}
									});

						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					
				});
		final ListView<IconVO> iconListView = (ListView<IconVO>) root
				.lookup("#iconListView");
		iconListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		iconListView.getSelectionModel().selectedItemProperty()
				.addListener(new ChangeListener<IconVO>() {
					@Override
					public void changed(
							ObservableValue<? extends IconVO> observable,
							IconVO oldValue, IconVO newValue) {
						if (newValue == null) {
							return;
						}
						final IconVO iconVO = newValue;
						try {
							new LoadingStage(ApplicationContext.getStage(),
									new Task<Void>() {
										protected Void call() {
											try {
												IconLogic logic = new IconLogic();
												BufferedImage showImg = logic.getIconImage(iconVO.unit_id, iconVO.awake);
												curImg = SwingFXUtils
														.toFXImage(showImg,
																null);
												KLBTexture texture = new KLBTexture();
												texture.path = iconVO+".png";
												texture.awtImg = showImg;
												ApplicationContext
														.setCurrentTexb(texture);
												updateProgress(1, 1);
											} catch (Exception e) {
												e.printStackTrace();
											}
											return null;
										}
									}, new MyCallBack() {
										public void run() {
											final Label namelb = (Label) root
													.lookup("#imgNameLabel");
											namelb.setText(iconVO+"");
											final ImageView imageView = (ImageView) root
													.lookup("#imageView");
											imageView.setImage(curImg);
											final Label infolb = (Label) root
													.lookup("#imgInfoLabel");
											infolb.setText("TODO Icon Info");
										}
									});

						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					
				});
		init(fileTreeView);
	}

	public static void show(String[] args) {
		launch(args);
	}
}
