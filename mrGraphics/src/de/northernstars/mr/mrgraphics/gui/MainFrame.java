package de.northernstars.mr.mrgraphics.gui;

import java.awt.BorderLayout;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;

import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import java.awt.event.KeyEvent;

@SuppressWarnings("serial")
public class MainFrame extends MRGraphicsFrame {

	private JPanel contentPane;
	private PlayField playField;
	
	/**
	 * Constructor
	 * @param fullscreenMode	Set {@code true} to enable fullscreen mode.
	 * @param windowSize		{@link Integer} array [width, height] of window size if {@code fullScreenMode} is {@code false}.
	 * @param windowPosition	{@link Integer} array [left, top] of window position if {@code fullScreenMode} is {@code false}.
	 * @param screen			{@link Integer} of screen number (starting by 0).
	 */
	public MainFrame(boolean fullscreenMode, int[] windowSize, int[] windowPosition, int screen) {	
		super(fullscreenMode, windowSize, windowPosition, screen);
		
		setTitle("MR Graphics");
		setAlwaysOnTop(true);
		
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
