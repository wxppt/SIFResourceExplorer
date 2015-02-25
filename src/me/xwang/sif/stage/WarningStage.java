package me.xwang.sif.stage;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class WarningStage {
	private final Stage stage;

	public WarningStage(final Stage stg, String detail) throws Exception {
		String[] msgArr = { "rippi：开扇失败...", "（·8·）：我的水杯找不到了...",
				"花阳：谁来救救我...", "Maki：纳尼所列，依米哇嘎奈...", "希：Nozomi power不足...",
				"KKE：刚刚谁走音了？", "果果：吃到青椒了...", "海爷：下次一定会赢...",
				"妮可：niconiconi..." };
		String msg = msgArr[(int) (Math.random() * msgArr.length)];
		stage = new Stage();
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.setAlwaysOnTop(true);
		stage.initOwner(stg);
		stage.setTitle(msg.split("：")[0]);
		Parent root = FXMLLoader.load(getClass().getResource("Warning.fxml"));
		((Label) root.lookup("#messageLabel")).setText(msg.split("：")[1]);
		((Label) root.lookup("#detailsLabel")).setText(detail);
		((Button) root.lookup("#okButton"))
				.setOnAction(new EventHandler<ActionEvent>() {
					public void handle(ActionEvent event) {
						stage.hide();
					}
				});
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();

	}
}
