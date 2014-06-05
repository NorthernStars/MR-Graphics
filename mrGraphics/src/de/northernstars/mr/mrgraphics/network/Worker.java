package de.northernstars.mr.mrgraphics.network;

import java.net.SocketTimeoutException;

import mrscenariofootball.core.data.worlddata.server.WorldData;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.northernstars.mr.mrgraphics.core.MRGraphics;

public class Worker implements Runnable {

	public static int timeout = 500;
	private static final Logger log = LogManager.getLogger();
	
	private MRGraphics mCore;
	private boolean active = true;
	
	public Worker(MRGraphics core) {
		mCore = core;
	}
	
	
	
	@Override
	public void run() {
		log.debug("Network worker is running");
		
		while(active){
			
			if( mCore.getConnection().isConnected() ){	
				
				try {
					// get worlddata
					String xml = mCore.getConnection().getDatagrammString(timeout);
					
					if( xml != null && !xml.isEmpty() ){
						
						log.debug("Recieved worlddata");
//						System.out.println(xml);
						WorldData wData = WorldData.unmarshallXMLPositionDataPackageString(xml);
						
						// update graphics
						mCore.getGui().getPlayField().setWorld(wData.copy());
						mCore.getGui().getPlayField().updateUI();
						
					}	
				
				} catch (SocketTimeoutException e) {
					log.warn("Recieved socket timeout");
				}
				
			}
		}
		
		log.debug("Network worker stopped");
	}



	/**
	 * @param active the active to set
	 */
	public void setActive(boolean active) {
		this.active = active;
	}

}
