package de.northernstars.mr.mrgraphics.network;

import java.net.InetAddress;
import mrservermisc.network.BasicUDPServerConnection;
import mrservermisc.network.handshake.server.ConnectionAcknowlege;
import mrservermisc.network.handshake.server.ConnectionEstablished;
import mrservermisc.network.handshake.server.ConnectionRequest;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GraphicsConnection extends BasicUDPServerConnection {

	public GraphicsConnection(InetAddress aTargetAddress, int aTargetPort) {
		super(aTargetAddress, aTargetPort);
	}

	private static final Logger log = LogManager.getLogger();
	
	public static int port = 9060;
	private boolean mIsConnectionEstablished = false;
	
	public boolean establishConnection(InetAddress host, int port){
		
		try {
		
			log.info( "Establishing connection: " + toString() );
			
			ConnectionRequest vRequestToVision = new ConnectionRequest( "defaultserver" );
			log.debug( "Sending handshake: " + vRequestToVision.toString() );
			sendDatagrammString( vRequestToVision.toXMLString() );
			
			ConnectionAcknowlege vVisionAcknowledge = ConnectionAcknowlege.unmarshallXMLConnectionAcknowlegeString( getDatagrammString( 1000 ) );
			
			log.debug( "Recieving Acknowledge: " + vVisionAcknowledge );
			
			if( vVisionAcknowledge != null && vVisionAcknowledge.isConnectionAllowed() ){
				
				ConnectionEstablished vVisionConnectionEstablished = new ConnectionEstablished();
				log.debug( "Sending Acknowledge: " + vVisionConnectionEstablished );
				sendDatagrammString( vVisionConnectionEstablished.toXMLString() );
				
				mIsConnectionEstablished = true;
				return mIsConnectionEstablished;
				
			}
	
		} catch ( Exception vException ) {
	
			log.error( "Could not establish connection to vision: " + vException.getLocalizedMessage() );
			log.catching( Level.ERROR, vException );
	        
		}

		log.debug( "Could not establish connection" );
		return false;
		
	}
	
}