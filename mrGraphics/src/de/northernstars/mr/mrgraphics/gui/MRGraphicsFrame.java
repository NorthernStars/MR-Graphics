package de.northernstars.mr.mrgraphics.gui;

import java.awt.Cursor;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import javax.swing.JFrame;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@SuppressWarnings("serial")
public abstract class MRGraphicsFrame extends JFrame {

	protected final Logger log = LogManager.getLogger();
	protected boolean fullscreenMode = false;
	protected int[] windowSize;
	protected int[] windowPosition;
	protected int screen = 0;

	/**
	 * Constructor
	 * 
	 * @param fullscreenMode
	 *            Set {@code true} to enable fullscreen mode.
	 * @param windowSize
	 *            {@link Integer} array [width, height] of window size if
	 *            {@code fullScreenMode} is {@code false}.
	 * @param windowPosition
	 *            {@link Integer} array [left, top] of window position if
	 *            {@code fullScreenMode} is {@code false}.
	 * @param screen
	 *            {@link Integer} of screen number (starting by 0).
	 */
	public MRGraphicsFrame(boolean fullscreenMode, int[] windowSize,
			int[] windowPosition, int screen) {
		this.fullscreenMode = fullscreenMode;
		if( windowSize == null ){
			this.windowSize = new int[]{0, 0};
		} else {
			this.windowSize = windowSize;
		}
		if( windowPosition == null ){
			this.windowPosition = new int[]{0, 0};
		} else {
			this.windowPosition = windowPosition;
		}
		this.screen = screen;

		showOnScreen(screen);
	}

	/**
	 * Shows a {@link MRGraphicsFrame} on a specific screen
	 * 
	 * @param screen
	 *            {@link Integer} of screen number.
	 * @param frame
	 *            {@link MRGraphicsFrame} to show on the screen.
	 */
	public void showOnScreen(int screen) {
		GraphicsEnvironment ge = GraphicsEnvironment
				.getLocalGraphicsEnvironment();
		GraphicsDevice[] gd = ge.getScreenDevices();
		GraphicsDevice dev = null;

		setVisible(false);
		dispose();

		// get screen
		if (screen > -1 && screen < gd.length) {
			dev = gd[screen];
		} else if (gd.length > 0) {
			dev = gd[0];
			screen = 0;
		}

		this.screen = screen;
		log.debug("Try to show frame on screen " + Integer.toString(screen));

		// FULLSCREEN
		if (fullscreenMode) {

			if (dev != null) {

				// Set fullscreen mode
				setUndecorated(true);
				setResizable(false);
				
				if (dev.isFullScreenSupported()) {
					
					// Exlusive mode
					dev.setFullScreenWindow(this);
					log.debug("Showing frame in exclusive fullscreen mode");
					
				} else {
					
					// Scaled mode
					windowSize = new int[] {
							dev.getDefaultConfiguration().getBounds().width,
							dev.getDefaultConfiguration().getBounds().height };
					windowPosition = new int[] {
							dev.getDefaultConfiguration().getBounds().x,
							dev.getDefaultConfiguration().getBounds().y };
					updateBounds(windowSize, windowPosition);
					windowPosition = new int[]{0, 0};
					
				}

			} else {
				log.error("No screens found");
			}

		}
		// NO FULLSCREEN
		else {

			// try to revert full screen
			if (dev != null) {
				dev.setFullScreenWindow(null);
			}

			setUndecorated(false);

			// update bounds
			if (dev != null) {

				// calculate new display position
				int x = dev.getDefaultConfiguration().getBounds().x
						+ windowPosition[0];
				int y = dev.getDefaultConfiguration().getBounds().y
						+ windowPosition[1];
				windowPosition = new int[] { x, y };
				
				updateBounds(windowSize, windowPosition);

			} else if (gd.length > 0) {
				updateBounds();
			}

		}

		setVisible(true);

	}

	/**
	 * Updates bounds
	 */
	protected void updateBounds() {
		updateBounds(windowSize, windowPosition);
	}

	/**
	 * Updates bounds
	 * 
	 * @param fullscreenMode
	 * @param windowSize
	 * @param windowPosition
	 */
	protected void updateBounds(int[] windowSize, int[] windowPosition) {
		
		dispose();
		log.debug("Showing frame in window mode "
				+ Integer.toString(windowSize[0]) + "x"
				+ Integer.toString(windowSize[1]) + " at "
				+ Integer.toString(windowPosition[0]) + ", "
				+ Integer.toString(windowPosition[1]));

		setBounds(windowPosition[0], windowPosition[1], windowSize[0],
				windowSize[1]);
		setResizable(true);
		setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}

	public void setFullscreenMode(boolean fullscreenMode) {
		this.fullscreenMode = fullscreenMode;
		showOnScreen(screen);
	}

	public void setWindowSize(int[] windowSize) {
		this.windowSize = windowSize;
		updateBounds();
	}

	public void setWindowPosition(int[] windowPosition) {
		this.windowPosition = windowPosition;
		showOnScreen(screen);
	}

	public void setScreen(int screen) {
		this.screen = screen;
		showOnScreen(screen);
	}

	public int getScreen() {
		return screen;
	}

	public boolean isFullscreenMode() {
		return fullscreenMode;
	}

	public int[] getWindowSize() {
		return windowSize;
	}

	public int[] getWindowPosition() {
		return windowPosition;
	}

}
