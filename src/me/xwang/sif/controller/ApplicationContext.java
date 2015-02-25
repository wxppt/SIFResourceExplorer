package me.xwang.sif.controller;

import java.io.File;

import javafx.stage.Stage;
import me.xwang.sif.entity.KLBTexture;

public class ApplicationContext {
	private static Stage stage;
	private static File tracedFile = new File("/");
	private static KLBTexture currentTexb;
	private static String cardDbPath;
	private static String unitDbPath;
	private static File assertParent;
	

	public static File getAssertParent() {
		return assertParent;
	}

	public static void setAssertParent(File assertParent) {
		ApplicationContext.assertParent = assertParent;
	}

	public static String getCardDbPath() {
		return cardDbPath;
	}

	public static void setCardDbPath(String cardDbPath) {
		ApplicationContext.cardDbPath = cardDbPath;
	}

	public static String getUnitDbPath() {
		return unitDbPath;
	}

	public static void setUnitDbPath(String unitDbPath) {
		ApplicationContext.unitDbPath = unitDbPath;
	}

	public static KLBTexture getCurrentTexb() {
		return currentTexb;
	}

	public static void setCurrentTexb(KLBTexture currentTexb) {
		ApplicationContext.currentTexb = currentTexb;
	}

	public static Stage getStage() {
		return stage;
	}

	public static void setStage(Stage stage) {
		ApplicationContext.stage = stage;
	}

	public static File getTracedFile() {
		return tracedFile;
	}

	public static void setTracedFile(File tracedFile) {
		ApplicationContext.tracedFile = tracedFile;
	}
}
