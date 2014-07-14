package de.northernstars.mr.mrgraphics.gui;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import java.awt.GridLayout;

import javax.swing.border.TitledBorder;
import javax.swing.JLabel;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.SwingConstants;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JCheckBox;

import de.northernstars.mr.mrgraphics.core.MRGraphics;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

@SuppressWarnings("serial")
public class FrontendFrame extends JFrame {

	private JPanel contentPane;
	private JButton btnConnect;
	private JTextField txtServerIP;
	private JTextField txtServerPort;
	private JTextField txtWindowPositionX;
	private JTextField txtWindowPositionY;
	private JTextField txtWindowSizeWidth;
	private JTextField txtWindowSizeHeight;
	private JCheckBox chkFullscreenMode;

	private MRGraphics mCore;
	private JTextField txtScreenNumber;

	/**
	 * Create the frame.
	 */
	public FrontendFrame(MRGraphics aCore) {

		mCore = aCore;
		boolean enabled = !mCore.isFullscreenMode();

		setTitle("MR Graphics Control Interface");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 600, 300);

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[] { 0, 0 };
		gbl_contentPane.rowHeights = new int[] { 0, 0, 0 };
		gbl_contentPane.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gbl_contentPane.rowWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
		contentPane.setLayout(gbl_contentPane);

		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(null, "Connection Settings",
				TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GridBagConstraints gbc_panel_1 = new GridBagConstraints();
		gbc_panel_1.insets = new Insets(0, 0, 5, 0);
		gbc_panel_1.fill = GridBagConstraints.BOTH;
		gbc_panel_1.gridx = 0;
		gbc_panel_1.gridy = 0;
		contentPane.add(panel_1, gbc_panel_1);
		panel_1.setLayout(new GridLayout(0, 5, 5, 0));

		JLabel lblServerIP = new JLabel("Server IP:");
		lblServerIP.setHorizontalAlignment(SwingConstants.RIGHT);
		panel_1.add(lblServerIP);

		txtServerIP = new JTextField(mCore.getHost());
		panel_1.add(txtServerIP);
		txtServerIP.setColumns(10);

		JLabel lblServerPort = new JLabel("Server Port:");
		lblServerPort.setHorizontalAlignment(SwingConstants.RIGHT);
		panel_1.add(lblServerPort);

		txtServerPort = new JTextField(Integer.toString(mCore.getPort()));
		panel_1.add(txtServerPort);
		txtServerPort.setColumns(10);

		btnConnect = new JButton("Connect");
		btnConnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				btnConnect.setText("Reconnect");

				new Thread(new Runnable() {

					@Override
					public void run() {
						String host = txtServerIP.getText().trim();
						int port = Integer.parseInt(txtServerPort.getText()
								.trim());

						if (mCore.disconnectFromServer()) {
							setTitle("MR-Graphics - disconnected");

							mCore.setHost(host);
							mCore.setPort(port);
							if (mCore.connectToServer()) {
								setTitle("MR-Graphics - connected");
							}
						}
					}

				}).start();

			}
		});
		btnConnect.setMnemonic('C');
		panel_1.add(btnConnect);

		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "Scenario GUI Settings",
				TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 1;
		contentPane.add(panel, gbc_panel);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[] { 0, 0, 118, 0 };
		gbl_panel.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0 };
		gbl_panel.columnWeights = new double[] { 0.0, 0.0, 0.0,
				Double.MIN_VALUE };
		gbl_panel.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
				Double.MIN_VALUE };
		panel.setLayout(gbl_panel);

		chkFullscreenMode = new JCheckBox("Fullscreen");
		chkFullscreenMode.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				boolean enabled = chkFullscreenMode.isSelected();

				mCore.setFullscreenMode(enabled);
				txtWindowPositionX.setEnabled(!enabled);
				txtWindowPositionY.setEnabled(!enabled);
				txtWindowSizeHeight.setEnabled(!enabled);
				txtWindowSizeWidth.setEnabled(!enabled);
				updateGuiParameters();
			}
		});
		chkFullscreenMode.setHorizontalAlignment(SwingConstants.LEFT);
		chkFullscreenMode.setSelected(mCore.getGui().isFullscreenMode());

		GridBagConstraints gbc_chkFullscreenMode = new GridBagConstraints();
		gbc_chkFullscreenMode.anchor = GridBagConstraints.WEST;
		gbc_chkFullscreenMode.gridwidth = 3;
		gbc_chkFullscreenMode.insets = new Insets(0, 0, 5, 0);
		gbc_chkFullscreenMode.gridx = 0;
		gbc_chkFullscreenMode.gridy = 0;
		panel.add(chkFullscreenMode, gbc_chkFullscreenMode);

		JLabel lblX = new JLabel("X");
		GridBagConstraints gbc_lblX = new GridBagConstraints();
		gbc_lblX.insets = new Insets(0, 0, 5, 5);
		gbc_lblX.gridx = 1;
		gbc_lblX.gridy = 1;
		panel.add(lblX, gbc_lblX);

		JLabel lblY = new JLabel("Y");
		GridBagConstraints gbc_lblY = new GridBagConstraints();
		gbc_lblY.insets = new Insets(0, 0, 5, 0);
		gbc_lblY.gridx = 2;
		gbc_lblY.gridy = 1;
		panel.add(lblY, gbc_lblY);

		JLabel lblWindowPosition = new JLabel("Window Position:");
		lblWindowPosition.setHorizontalAlignment(SwingConstants.RIGHT);
		GridBagConstraints gbc_lblWindowPosition = new GridBagConstraints();
		gbc_lblWindowPosition.anchor = GridBagConstraints.WEST;
		gbc_lblWindowPosition.insets = new Insets(0, 0, 5, 5);
		gbc_lblWindowPosition.gridx = 0;
		gbc_lblWindowPosition.gridy = 2;
		panel.add(lblWindowPosition, gbc_lblWindowPosition);

		txtWindowPositionX = new JTextField(Integer.toString(mCore
				.getWindowPosition()[0]));
		txtWindowPositionX.setEnabled(enabled);
		txtWindowPositionX.setHorizontalAlignment(SwingConstants.CENTER);
		txtWindowPositionX.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int[] windowPosition = mCore.getWindowPosition();
				windowPosition[0] = Integer.parseInt(txtWindowPositionX
						.getText().trim());
				mCore.setWindowPosition(windowPosition);
			}
		});
		GridBagConstraints gbc_txtWindowPositionX = new GridBagConstraints();
		gbc_txtWindowPositionX.insets = new Insets(0, 0, 5, 5);
		gbc_txtWindowPositionX.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtWindowPositionX.gridx = 1;
		gbc_txtWindowPositionX.gridy = 2;
		panel.add(txtWindowPositionX, gbc_txtWindowPositionX);
		txtWindowPositionX.setColumns(10);

		txtWindowPositionY = new JTextField(Integer.toString(mCore
				.getWindowPosition()[1]));
		txtWindowPositionY.setEnabled(enabled);
		txtWindowPositionY.setHorizontalAlignment(SwingConstants.CENTER);
		txtWindowPositionY.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int[] windowPosition = mCore.getWindowPosition();
				windowPosition[1] = Integer.parseInt(txtWindowPositionY
						.getText().trim());
				mCore.setWindowPosition(windowPosition);
			}
		});
		GridBagConstraints gbc_txtWindowPositionY = new GridBagConstraints();
		gbc_txtWindowPositionY.insets = new Insets(0, 0, 5, 0);
		gbc_txtWindowPositionY.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtWindowPositionY.gridx = 2;
		gbc_txtWindowPositionY.gridy = 2;
		panel.add(txtWindowPositionY, gbc_txtWindowPositionY);
		txtWindowPositionY.setColumns(10);

		JLabel lblWidth = new JLabel("Width");
		GridBagConstraints gbc_lblWidth = new GridBagConstraints();
		gbc_lblWidth.insets = new Insets(0, 0, 5, 5);
		gbc_lblWidth.gridx = 1;
		gbc_lblWidth.gridy = 3;
		panel.add(lblWidth, gbc_lblWidth);

		JLabel lblHeight = new JLabel("Height");
		GridBagConstraints gbc_lblHeight = new GridBagConstraints();
		gbc_lblHeight.insets = new Insets(0, 0, 5, 0);
		gbc_lblHeight.gridx = 2;
		gbc_lblHeight.gridy = 3;
		panel.add(lblHeight, gbc_lblHeight);

		JLabel lblWindowSize = new JLabel("Window Size:");
		lblWindowSize.setHorizontalAlignment(SwingConstants.RIGHT);
		GridBagConstraints gbc_lblWindowSize = new GridBagConstraints();
		gbc_lblWindowSize.anchor = GridBagConstraints.EAST;
		gbc_lblWindowSize.insets = new Insets(0, 0, 5, 5);
		gbc_lblWindowSize.gridx = 0;
		gbc_lblWindowSize.gridy = 4;
		panel.add(lblWindowSize, gbc_lblWindowSize);

		txtWindowSizeWidth = new JTextField(Integer.toString(mCore
				.getWindowSize()[0]));
		txtWindowSizeWidth.setEnabled(enabled);
		txtWindowSizeWidth.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int[] windowSize = mCore.getWindowSize();
				windowSize[0] = Integer.parseInt(txtWindowSizeWidth.getText()
						.trim());
				mCore.setWindowSize(windowSize);
			}
		});
		txtWindowSizeWidth.setHorizontalAlignment(SwingConstants.CENTER);
		GridBagConstraints gbc_txtWindowSizeWidth = new GridBagConstraints();
		gbc_txtWindowSizeWidth.insets = new Insets(0, 0, 5, 5);
		gbc_txtWindowSizeWidth.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtWindowSizeWidth.gridx = 1;
		gbc_txtWindowSizeWidth.gridy = 4;
		panel.add(txtWindowSizeWidth, gbc_txtWindowSizeWidth);
		txtWindowSizeWidth.setColumns(10);

		txtWindowSizeHeight = new JTextField(Integer.toString(mCore
				.getWindowSize()[1]));
		txtWindowSizeHeight.setEnabled(enabled);
		txtWindowSizeHeight.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int[] windowSize = mCore.getWindowSize();
				windowSize[1] = Integer.parseInt(txtWindowSizeHeight.getText()
						.trim());
				mCore.setWindowSize(windowSize);
			}
		});
		txtWindowSizeHeight.setHorizontalAlignment(SwingConstants.CENTER);
		GridBagConstraints gbc_txtWindowSizeHeight = new GridBagConstraints();
		gbc_txtWindowSizeHeight.insets = new Insets(0, 0, 5, 0);
		gbc_txtWindowSizeHeight.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtWindowSizeHeight.gridx = 2;
		gbc_txtWindowSizeHeight.gridy = 4;
		panel.add(txtWindowSizeHeight, gbc_txtWindowSizeHeight);
		txtWindowSizeHeight.setColumns(10);

		JLabel lblScreenNumber = new JLabel("Screen Number:");
		lblScreenNumber.setHorizontalAlignment(SwingConstants.RIGHT);
		GridBagConstraints gbc_lblScreenNumber = new GridBagConstraints();
		gbc_lblScreenNumber.anchor = GridBagConstraints.EAST;
		gbc_lblScreenNumber.insets = new Insets(0, 0, 0, 5);
		gbc_lblScreenNumber.gridx = 0;
		gbc_lblScreenNumber.gridy = 5;
		panel.add(lblScreenNumber, gbc_lblScreenNumber);

		txtScreenNumber = new JTextField(Integer.toString(mCore.getScreen()));
		txtScreenNumber.setHorizontalAlignment(SwingConstants.CENTER);
		txtScreenNumber.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mCore.setScreen(Integer.parseInt(txtScreenNumber.getText()
						.trim()));
				updateGuiParameters();
			}
		});
		GridBagConstraints gbc_txtScreenNumber = new GridBagConstraints();
		gbc_txtScreenNumber.insets = new Insets(0, 0, 0, 5);
		gbc_txtScreenNumber.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtScreenNumber.gridx = 1;
		gbc_txtScreenNumber.gridy = 5;
		panel.add(txtScreenNumber, gbc_txtScreenNumber);
		txtScreenNumber.setColumns(10);
	}
	
	private void updateGuiParameters(){
		txtWindowPositionX.setText( Integer.toString(mCore.getWindowPosition()[0]) );
		txtWindowPositionY.setText( Integer.toString(mCore.getWindowPosition()[1]) );
		txtWindowSizeHeight.setText( Integer.toString(mCore.getWindowSize()[0]) );
		txtWindowSizeWidth.setText( Integer.toString(mCore.getWindowSize()[0]) );
		txtScreenNumber.setText( Integer.toString(mCore.getScreen()) );
	}
}
