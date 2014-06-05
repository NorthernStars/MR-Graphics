package de.northernstars.mr.mrgraphics.core;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.northernstars.mr.mrgraphics.gui.MainFrame;
import de.northernstars.mr.mrgraphics.gui.PlayField;
import de.northernstars.mr.mrgraphics.network.GraphicsConnection;
import de.northernstars.mr.mrgraphics.network.Worker;

public class MRGraphics {

	private static final Logger log = LogManager.getLogger();
	private static String host = "192.168.0.109";
	private static int port = 9060;
	
	private MainFrame gui;
	private GraphicsConnection connection;
	private Runnable worker;
	
	public MRGraphics() {
		log.error("Starting mrGraphics");
		gui = new MainFrame();		
		gui.setVisible(true);
		gui.setPlayField( new PlayField() );		
		gui.getPlayField().updateUI();
	
		try {
			
			// establish connection
			connection = new GraphicsConnection(InetAddress.getByName(host), port);
			connection.establishConnection(InetAddress.getByName(host), port);
			
			// start background worker
			worker = new Worker(this);
			new Thread(worker).start();
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		new MRGraphics();
	}

	/**
	 * @return the gui
	 */
	public MainFrame getGui() {
		return gui;
	}

	/**
	 * @return the connection
	 */
	public GraphicsConnection getConnection() {
		return connection;
	}

}
