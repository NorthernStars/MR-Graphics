package de.northernstars.mr.mrgraphics.core;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.northernstars.mr.mrgraphics.gui.MainFrame;
import de.northernstars.mr.mrgraphics.gui.PlayField;
import de.northernstars.mr.mrgraphics.network.GraphicsConnection;
import de.northernstars.mr.mrgraphics.network.Worker;

public class MRGraphics implements WindowListener {

	private static final Logger log = LogManager.getLogger();
	private static String host;
	private static int port;
	private static boolean fullscreenMode = false;
	private static int[] windowSize;
	private static int[] windowPosition;
	private static int screen;

	private MainFrame gui;
	private GraphicsConnection connection;
	private Worker worker;

	private Options options = new Options();
	private CommandLine cmdLine;

	/**
	 * Constructor
	 * 
	 * @param args
	 *            Command line arguments
	 */
	public MRGraphics(String[] args) {
		
		try {
			
			options = createOptions();			
			CommandLineParser cmdParser = new GnuParser();
			cmdLine = cmdParser.parse(options, args);
			parseCommandLineOptions();
			
		} catch (ParseException e) {
			log.error("Can not parse command line options");
		}
		
		startGraphics();
		
	}

	/**
	 * Default constructor
	 */
	public MRGraphics() {
		this(new String[0]);
	}
	
	/**
	 * Starts graphics
	 */
	private void startGraphics(){
		
		log.error("Starting mrGraphics");
		gui = new MainFrame(fullscreenMode, windowSize, windowPosition, screen);
		
		gui.addWindowListener(this);
		gui.setVisible(true);
		gui.setPlayField(new PlayField());
		gui.getPlayField().updateUI();

		try {

			// establish connection
			connection = new GraphicsConnection(InetAddress.getByName(host),
					port);
			connection.establishConnection(InetAddress.getByName(host), port);

			// start background worker
			worker = new Worker(this);
			new Thread(worker).start();

		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Parses command line options
	 */
	private void parseCommandLineOptions(){
		if( cmdLine.hasOption("help") ){
			showCommandlineHelp( "MR-Graphics command line options:", options );
		}
		
		host = cmdLine.getOptionValue("server", "127.0.0.1");
		port = Integer.parseInt( cmdLine.getOptionValue("port", "9060") );
		windowSize = new int[2];
		windowSize[0] = Integer.parseInt( cmdLine.getOptionValue("width", "800") );
		windowSize[1] = Integer.parseInt( cmdLine.getOptionValue("height", "600") );
		
		windowPosition = new int[2];
		windowPosition[0] = Integer.parseInt( cmdLine.getOptionValue("left", "0") );
		windowPosition[1] = Integer.parseInt( cmdLine.getOptionValue("top", "0") );
		
		screen = Integer.parseInt( cmdLine.getOptionValue("display", "0") );
		
		if( cmdLine.hasOption("fullscreen") ){
			fullscreenMode = true;
		}
	}

	/**
	 * Creates options
	 */
	private Options createOptions(){
		final Options options = new Options();
		
		options.addOption("s", "server", true, "Remote server ip. Default is 127.0.0.1");
		options.addOption("p", "port", true, "Remote server port. Default is 9060");
		options.addOption("w", "width", true, "Window width in px if no fullscreen. Default is 800px.");
		options.addOption("h", "height", true, "Window height in px if no fullscreen. Default is 600px.");
		options.addOption("l", "left", true, "Window left offset in px if no fullscreen.Default is 0px.");
		options.addOption("t", "top", true, "Window top offset in px if no fullscreen.Default is 0px.");
		options.addOption("f", "fullscreen", false, "Enable fullscreen mode. Off by default.");
		options.addOption("d", "display", true, "Number of display/screen where to show szenario gui. Default is 0.");
		options.addOption("?", "help", false, "Show help.");
		
		return options;
	}

	/**
	 * Shows command line help
	 * @param aHelpString
	 * @param aCommandLineOptions
	 */
	public void showCommandlineHelp( String aHelpString, Options aCommandLineOptions ) {

        HelpFormatter vHelpFormatter = new HelpFormatter();
        vHelpFormatter.setWidth( 120 );
        vHelpFormatter.setLongOptPrefix( "-" );
        vHelpFormatter.setSyntaxPrefix( "" );
        vHelpFormatter.printHelp( aHelpString, aCommandLineOptions );

        System.exit( 0 );
    }

	/**
	 * MAIN FUNCTION
	 * 
	 * @param args
	 *            Command line arguments
	 */
	public static void main(String[] args) {
		new MRGraphics(args);
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

	@Override
	public void windowActivated(WindowEvent e) {}

	@Override
	public void windowClosed(WindowEvent e) {
		if( worker != null ){
			worker.setActive(false);
			log.debug("Application closed");
		}
	}

	@Override
	public void windowClosing(WindowEvent e) {}

	@Override
	public void windowDeactivated(WindowEvent e) {}

	@Override
	public void windowDeiconified(WindowEvent e) {}

	@Override
	public void windowIconified(WindowEvent e) {}

	@Override
	public void windowOpened(WindowEvent e) {}

}
