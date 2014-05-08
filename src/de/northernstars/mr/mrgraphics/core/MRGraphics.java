package de.northernstars.mr.mrgraphics.core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.northernstars.mr.mrgraphics.gui.MainFrame;
import de.northernstars.mr.mrgraphics.gui.PlayField;

public class MRGraphics {

	private static final Logger log = LogManager.getLogger();
	
	private MainFrame gui;
	
	public MRGraphics() {
		log.error("Starting mrGraphics");
		gui = new MainFrame();
		gui.setVisible(true);
		gui.setPlayField( new PlayField() );		
		gui.getPlayField().updateUI();
	}
	
	public static void main(String[] args) {
		new MRGraphics();
	}

}
