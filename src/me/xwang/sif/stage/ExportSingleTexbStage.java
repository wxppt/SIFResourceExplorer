package me.xwang.sif.stage;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.imageio.ImageIO;

import me.xwang.sif.controller.ApplicationContext;
import me.xwang.sif.logic.TexbLogic;

public class ExportSingleTexbStage {
	private final Stage stage;

	public void initToggleBtn(Parent root) {
		ToggleGroup tg = new ToggleGroup();
		RadioButton rb1 = (RadioButton) root.lookup("#tg1");
		RadioButton rb2 = (RadioButton) root.lookup("#tg2");
		rb1.setToggleGroup(tg);
		rb2.setToggleGroup(tg);
	}

	public ExportSingleTexbStage(final Stage stg) throws Exception {
		stage = new Stage();
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.initOwner(stg);
		stage.setTitle("导出Texture文件");
		final Parent root = FXMLLoader.load(getClass().getResource(
				"ExportSingleTexb.fxml"));
		Scene scene = new Scene(root);
		stage.setScene(scene);
		initToggleBtn(root);
		((Button) root.lookup("#okButton"))
				.setOnAction(new EventHandler<ActionEvent>() {
					public void handle(ActionEvent arg0) {
						stage.hide();
						if (ApplicationContext.getCurrentTexb().tag == null) {
							exportContainer();
							return;
						}
						if (((RadioButton) root.lookup("#tg1")).isSelected()) {
							exportContainer();
						} else {
							exportPictures();
						}
					}
				});
		((Button) root.lookup("#cancelButton"))
				.setOnAction(new EventHandler<ActionEvent>() {
					public void handle(ActionEvent arg0) {
						stage.hide();
					}
				});

		stage.show();

	}

	public void exportContainer() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("导出Texture容器");
		fileChooser.getExtensionFilters().add(
				new ExtensionFilter("png图片文件", "*.png"));
		fileChooser.setInitialDirectory(ApplicationContext.getTracedFile());
		String path = ApplicationContext.getCurrentTexb().path;
		if (path != null) {
			String name = new File(path).getName().replaceAll("\\.texb.*",
					".png");
			fileChooser.setInitialFileName(name);
		}
		File file = fileChooser.showSaveDialog(ApplicationContext.getStage());
		if (file != null) {
			System.out.println(file.getAbsolutePath());
			BufferedImage img = (BufferedImage) ApplicationContext
					.getCurrentTexb().awtImg;
			try {
				ImageIO.write(img, "png", file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void exportPictures() {
		DirectoryChooser chooser = new DirectoryChooser();
		chooser.setTitle("导出Texture图片");
		chooser.setInitialDirectory(ApplicationContext.getTracedFile());
		final File file = chooser.showDialog(ApplicationContext.getStage());
		if (file != null) {
			Task<Void> task = new Task<Void>() {
				@Override
				protected Void call() throws Exception {
					String rootPath = file.getAbsolutePath();
					TexbLogic logic = new TexbLogic();
					HashMap<File, BufferedImage> map = logic
							.getSplitImages(ApplicationContext.getCurrentTexb());
					updateProgress(0, map.keySet().size());
					updateMessage("0/" + map.keySet().size());
					int index = 0;
					for (final File dstFile : map.keySet()) {
						try {
							ImageIO.write(map.get(dstFile), "png", new File(
									rootPath + "\\" + dstFile.getName()));
						} catch (IOException e) {
							e.printStackTrace();
						}
						index++;
						updateProgress(index, map.keySet().size());
						updateMessage(index + "/" + map.keySet().size());
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
}
