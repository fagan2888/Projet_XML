package controller;

import java.io.File;

import javax.swing.JFileChooser;

import util.Rechercher;

public class GUIController {

	private Rechercher model;
	
	public GUIController(Rechercher model) {
		this.model = model;
	}
	
	public void notifySetFile(File file) {
		model.setXmlFile(file);
	}
	
	public void notifySendQuery() throws Exception {
		File created = model.execute();
		Process p = new ProcessBuilder("explorer.exe", "/select,"+created.getAbsolutePath()).start();
	}
}
