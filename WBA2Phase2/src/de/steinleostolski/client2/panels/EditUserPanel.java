package de.steinleostolski.client2.panels;


import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import de.steinleostolski.client2.Application;
import de.steinleostolski.user.Userdb;
import de.steinleostolski.xmpp.PubsubClient;

public class EditUserPanel extends JPanel {
	
	private Application app; 
	private PubsubClient pubsub;
	private Userdb user;
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 489852620191460633L;

	public EditUserPanel(Application app, PubsubClient pubsub, Userdb user) {
		this.app = app;
		this.pubsub = pubsub;
		this.user = user;
		initUI();
	}

	private void initUI() {
		JPanel panel = new JPanel();
		add(panel);
		setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		
		JButton btnMainMenu = new JButton("Main Menu");
		JLabel platzhalter = new JLabel("Edit Profile");
		
		gbc.anchor= GridBagConstraints.NORTHWEST;
		gbc.weightx = 1; gbc.weighty = 1;
		gbc.gridx = 0; gbc.gridy = 0;
		panel.add(platzhalter, gbc);
		
		gbc.gridx = 1;
		panel.add(btnMainMenu, gbc);
		
		btnMainMenu.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				app.changePanel(1, 0);
			}
		});
		
	}

}
