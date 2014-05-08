package de.northernstars.mr.mrgraphics.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.GraphicsEnvironment;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import java.awt.Cursor;
import java.awt.event.KeyEvent;

@SuppressWarnings("serial")
public class MainFrame extends JFrame {

	private JPanel contentPane;
	private PlayField playField;

	/**
	 * Create the frame.
	 */
	public MainFrame() {
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		
		GraphicsEnvironment lge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		DisplayMode mode = lge.getDefaultScreenDevice().getDisplayMode();
		
		setTitle("MR Graphics");
		setUndecorated(true);
		setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		setAlwaysOnTop(false);		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		setBounds(0, 0, (int) screen.getWidth(), (int) screen.getHeight());
		setBounds(0, 0, (int) mode.getWidth(), (int) mode.getHeight());
//		setBounds(100, 100, 800, 600);
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
