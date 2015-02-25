package me.xwang.sif;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javafx.concurrent.Task;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import javax.imageio.ImageIO;

public class Main {
	public static void main(String[] args) throws IOException {

		// load source images
		BufferedImage image = ImageIO.read(new File("image.png"));
		BufferedImage overlay = ImageIO.read(new File("overlay.png"));

		// create the new image, canvas size is the max. of both image sizes
		int w = Math.max(image.getWidth(), overlay.getWidth());
		int h = Math.max(image.getHeight(), overlay.getHeight());
		BufferedImage combined = new BufferedImage(w, h,
				BufferedImage.TYPE_INT_ARGB);

		// paint both images, preserving the alpha channels
		Graphics g = combined.getGraphics();
		g.drawImage(image, 0, 0, null);
		g.drawImage(overlay, 0, 0, null);

		// Save as new image
		ImageIO.write(combined, "PNG", new File("combined.png"));
	}

	public void start(Stage stage) {
		Task<Void> task = new Task<Void>() {
			@Override
			public Void call() {
				for (int i = 1; i < 10; i++) {
					try {
						Thread.sleep(3000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					System.out.println(i);
					updateProgress(i, 10);
				}
				return null;
			}
		};

		ProgressBar updProg = new ProgressBar();
		updProg.progressProperty().bind(task.progressProperty());

		Thread th = new Thread(task);
		th.setDaemon(true);
		th.start();

		StackPane layout = new StackPane();
		layout.setStyle("-fx-background-color: cornsilk; -fx-padding: 10;");
		layout.getChildren().add(updProg);

		stage.setScene(new Scene(layout));
		stage.show();
	}
}