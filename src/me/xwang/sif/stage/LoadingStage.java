package me.xwang.sif.stage;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ProgressIndicator;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class LoadingStage {
	private final Stage stage;

	public LoadingStage(final Stage stg, Task<Void> task,
			final MyCallBack callback) throws Exception {
		stage = new Stage();
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.initStyle(StageStyle.UNDECORATED);
		stage.initOwner(stg);
		stage.setTitle("Loading...");
		Parent root = FXMLLoader.load(getClass().getResource("Loading.fxml"));
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();

		ProgressIndicator pi = (ProgressIndicator) root.lookup("#progress");
		// ChangeListener
		pi.progressProperty().addListener(new ChangeListener<Number>() {
			public void changed(ObservableValue<? extends Number> observable,
					Number oldValue, Number newValue) {
				if (newValue.intValue() == 1) {
					if (callback != null) {
						callback.run();
					}
					stage.hide();
				}
			}
		});

		// Bind
		pi.progressProperty().bind(task.progressProperty());

		// Run
		Thread th = new Thread(task);
		th.setDaemon(true);
		th.start();
	}
}
