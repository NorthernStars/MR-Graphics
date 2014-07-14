package de.northernstars.mr.mrgraphics.gui;

import java.awt.BorderLayout;
import java.awt.DisplayMode;
import java.awt.GraphicsEnvironment;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.Cursor;
import java.awt.event.KeyEvent;

@SuppressWarnings("serial")
public class MainFrame extends JFrame {

	private static final Logger log = LogManager.getLogger();
	
	private JPanel contentPane;
	private PlayField playField;
	
	/**
	 * Create the frame.
	 * @param fullscreenMode	Set {@code true} to show gui in fullscreen mode.
	 * @param windowSize		Window size [width, height] if {@code fullscreenMode} is {@code false}.
	 * @param windowPosition	Window position [left, top] if {@code fullscreenMode} is {@code false}.
	 */
	public MainFrame(boolean fullscreenMode, int[] windowSize, int[] windowPosition) {		
		GraphicsEnvironment lge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		DisplayMode mode = lge.getDefaultScreenDevice().getDisplayMode();
		
		setTitle("MR Graphics");
		setUndecorated(true);
		setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		setAlwaysOnTop(false);		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		if( fullscreenMode ){
			log.debug( "Showing frame in fullscreen mode " + Integer.toString(mode.getWidth()) + "x" + Integer.toString(mode.getHeight()) );
			setBounds(0, 0, (int) mode.getWidth(), (int) mode.getHeight());
		} else {
			log.debug( "Showing frame in window mode " + Integer.toString(windowSize[0]) + "x" + Integer.toString(windowSize[1])
					+ " at " + Integer.toString(windowPosition[0]) +", " + Integer.toString(windowPosition[1]) );
			setBounds(windowPosition[0], windowPosition[1], windowSize[0], windowSize[1]);
		}
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {			
			@Override
			public boolean dispatchKeyEvent(KeyEvent event) {
				if( event.getKeyCode() == KeyEvent.VK_ESCAPE ){
					dispose();
				}
				return false;
			}
		});
	}

	/**
	 * @return the playField
	 */
	public PlayField getPlayField() {
		return playField;
	}

	/**
	 * @param field the playField to set
	 */
	public void setPlayField(PlayField field) {
		contentPane.removeAll();
		
		playField = field;
		contentPane.add(playField, BorderLayout.CENTER);
	}
	

}
