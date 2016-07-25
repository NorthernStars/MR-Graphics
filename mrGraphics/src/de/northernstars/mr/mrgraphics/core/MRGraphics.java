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

import de.northernstars.mr.mrgraphics.gui.FrontendFrame;
import de.northernstars.mr.mrgraphics.gui.MainFrame;
import de.northernstars.mr.mrgraphics.gui.PlayField;
import de.northernstars.mr.mrgraphics.network.GraphicsConnection;
import de.northernstars.mr.mrgraphics.network.Worker;

public class MRGraphics implements WindowListener {

	private static final Logger log = LogManager.getLogger();
	private String host;
	private int port;
	private boolean fullscreenMode = false;
	private int[] windowSize;
	private int[] windowPosition;
	private int screen;

	private MainFrame gui;
	private FrontendFrame frontent;
	private GraphicsConnection connection;
	private Worker worker;

	private Options options = new Options();
	private CommandLine cmdLine;
	
	private boolean mQuietMode = false;
	private boolean mAutoConnect = false;

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
		
		if( mAutoConnect )
			connectToServer();
			
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
	private void startGraphics() {

		log.info("Starting mrGraphics");

		gui = new MainFrame(fullscreenMode, windowSize, windowPosition, screen);
		gui.setVisible(true);
		gui.setPlayField(new PlayField());
		gui.getPlayField().updateUI();
		gui.addWindowListener(this);

		if( !mQuietMode ){
			frontent = new FrontendFrame(this);
			frontent.addWindowListener(this);
			frontent.setVisible(true);
		}

	}

	/**
	 * Connects to server
	 */
	public boolean connectToServer() {
		try {

			// establish connection
			log.info("Connecting to {}:{}", host, port);
			connection = new GraphicsConnection(InetAddress.getByName(host),
					port);
			connection.establishConnection(InetAddress.getByName(host), port);

			// start background worker
			worker = new Worker(this);
			new Thread(worker).start();
			return true;

		} catch (UnknownHostException e) {
			log.error( "Cannot connect to server: {}", e.getMessage() );
		}
		
		return false;
	}

	/**
	 * Disconnects from server
	 */
	public boolean disconnectFromServer() {
		if (worker != null) {
			worker.setActive(false);
			log.info("Disconnected from server {}:{}", host, port);
		}
		return true;
	}

	/**
	 * Parses command line options
	 */
	private void parseCommandLineOptions() {
		if (cmdLine.hasOption("help")) {
			showCommandlineHelp("MR-Graphics command line options:", options);
		}

		host = cmdLine.getOptionValue("server", "127.0.0.1");
		port = Integer.parseInt(cmdLine.getOptionValue("port", "9060"));
		windowSize = new int[2];
		windowSize[0] = Integer
				.parseInt(cmdLine.getOptionValue("width", "800"));
		windowSize[1] = Integer.parseInt(cmdLine
				.getOptionValue("height", "600"));

		windowPosition = new int[2];
		windowPosition[0] = Integer.parseInt(cmdLine
				.getOptionValue("left", "0"));
		windowPosition[1] = Integer
				.parseInt(cmdLine.getOptionValue("top", "0"));

		screen = Integer.parseInt(cmdLine.getOptionValue("display", "0"));

		if( cmdLine.hasOption("fullscreen") ){
			fullscreenMode = true;
		}
		
		if( cmdLine.hasOption("quiet") ){
			mQuietMode = true;
			mAutoConnect = true;
		}
		
		if( cmdLine.hasOption("autoconnect") ){
			mAutoConnect = true;
		}
		

		log.debug("Command line options: server={}:{}, quiet={}, autoconnect={}, fullscreen={}, screen={}, windowWidth={}, windowHeight={}, left={}, right={}",
				host, port, mQuietMode, mAutoConnect, fullscreenMode, screen, windowSize[0], windowSize[1], windowPosition[0], windowPosition[1]);
		
		
	}

	/**
	 * Creates options
	 */
	private Options createOptions() {
		final Options options = new Options();

		options.addOption("s", "server", true,
				"Remote server ip. Default is 127.0.0.1");
		options.addOption("p", "port", true,
				"Remote server port. Default is 9060");
		options.addOption("w", "width", true,
				"Window width in px if no fullscreen. Default is 800px.");
		options.addOption("h", "height", true,
				"Window height in px if no fullscreen. Default is 600px.");
		options.addOption("l", "left", true,
				"Window left offset in px if no fullscreen.Default is 0px.");
		options.addOption("t", "top", true,
				"Window top offset in px if no fullscreen.Default is 0px.");
		options.addOption("f", "fullscreen", false,
				"Enable fullscreen mode. Off by default.");
		options.addOption("d", "display", true,
				"Number of display/screen where to show szenario gui. Default is 0.");
		options.addOption("a", "autoconnect", false,
				"Auto connect to server using default ip and port or specified by -s and -p.");
		options.addOption("q", "quiet", false,
				"Starts without control interface gui, automatically uses auto connect (see -a).");		
		options.addOption("?", "help", false, "Show help.");

		return options;
	}

	/**
	 * Shows command line help
	 * 
	 * @param aHelpString
	 * @param aCommandLineOptions
	 */
	public void showCommandlineHelp(String aHelpString,
			Options aCommandLineOptions) {

		HelpFormatter vHelpFormatter = new HelpFormatter();
		vHelpFormatter.setWidth(120);
		vHelpFormatter.setLongOptPrefix("-");
		vHelpFormatter.setSyntaxPrefix("");
		vHelpFormatter.printHelp(aHelpString, aCommandLineOptions);

		System.exit(0);
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
	public void windowActivated(WindowEvent e) {
	}

	@Override
	public void windowClosed(WindowEvent e) {		
		if( (mAutoConnect && e.getWindow() == gui) || (!mAutoConnect && e.getWindow() == frontent ) ){
			if( worker != null ){
				worker.setActive(false);
				gui.dispose();
	
				if( frontent != null ){
					frontent.removeWindowListener(this);
					frontent.dispose();
				}
				log.debug("Application closed");
			}
		}
	}

	@Override
	public void windowClosing(WindowEvent e) {
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
	}

	@Override
	public void windowIconified(WindowEvent e) {
	}

	@Override
	public void windowOpened(WindowEvent e) {
	}

	/**
	 * @return the host
	 */
	public String getHost() {
		return host;
	}

	/**
	 * @param host
	 *            the host to set
	 */
	public void setHost(String host) {
		this.host = host;
	}

	/**
	 * @return the port
	 */
	public int getPort() {
		return port;
	}

	/**
	 * @param port
	 *            the port to set
	 */
	public void setPort(int port) {
		this.port = port;
	}

	/**
	 * @return the windowSize
	 */
	public int[] getWindowSize() {
		return getGui().getWindowSize();
	}

	/**
	 * @param windowSize
	 *            the windowSize to set
	 */
	public void setWindowSize(int[] windowSize) {
		getGui().setWindowSize(windowSize);
	}

	/**
	 * @return the windowPosition
	 */
	public int[] getWindowPosition() {
		return getGui().getWindowPosition();
	}

	/**
	 * @param windowPosition
	 *            the windowPosition to set
	 */
	public void setWindowPosition(int[] windowPosition) {
		getGui().setWindowPosition(windowPosition);
	}

	/**
	 * @return the screen
	 */
	public int getScreen() {
		return getGui().getScreen();
	}

	/**
	 * @param screen
	 *            the screen to set
	 */
	public void setScreen(int screen) {
		getGui().setScreen(screen);
	}

	/**
	 * @return the fullscreenMode
	 */
	public boolean isFullscreenMode() {
		return fullscreenMode;
	}

	/**
	 * @param fullscreenMode
	 *            the fullscreenMode to set
	 */
	public void setFullscreenMode(boolean fullscreenMode) {
		this.fullscreenMode = fullscreenMode;
		getGui().setFullscreenMode(fullscreenMode);
	}
	
	public boolean isAutoconnect(){
		return mAutoConnect;
	}
	
	public boolean isQuietMode(){
		return mQuietMode;
	}

}
