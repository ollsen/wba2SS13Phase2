package de.steinleostolski.client2.panels;


import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import de.steinleostolski.client2.Application;
import de.steinleostolski.user.Userdb;
import de.steinleostolski.xmpp.PubsubClient;

public class NewUserPanel extends JPanel {
	
	private Application app; 
	private PubsubClient pubsub;
	private Userdb user;
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 489852620191460633L;

	public NewUserPanel(Application app, PubsubClient pubsub, Userdb user) {
		this.app = app;
		this.pubsub = pubsub;
		this.user = user;
		initUI();
	}

	private void initUI() {
		JPanel panel = new JPanel();
		add(panel);
		panel.setLayout(new GridBagLayout());
		
		
		GridBagConstraints gbc = new GridBagConstraints();
		
		JButton btnMainMenu = new JButton("Abbrechen");
		JLabel vornameLabel = new JLabel("Vorname");
		JLabel nachnameLabel = new JLabel("Nachname");
		JLabel standortLabel = new JLabel("Standort");
		JLabel userLevelLabel = new JLabel("User Level");
		JLabel pwLabel = new JLabel("Passwort");
		
		JTextField vornameField = new JTextField(16);
		JTextField nachnameField = new JTextField(16);
		JTextField standortField = new JTextField(16);
		JComboBox userLevelCBox = new JComboBox();
		JPasswordField pwField = new JPasswordField(16);
		JButton sendBtn = new JButton("Benutzer anlegen");
		
		userLevelCBox.addItem("User");
		userLevelCBox.addItem("Supporter");
		userLevelCBox.addItem("Admin");
		
		gbc.insets = new Insets(8,16,8,16);
		gbc.anchor = GridBagConstraints.WEST;
		//gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 1;
		gbc.weighty = 1;
		panel.add(vornameLabel, gbc);
		
		gbc.gridx = 1;
		panel.add(vornameField, gbc);
		
		gbc.gridx = 0;
		gbc.gridy++;
		panel.add(nachnameLabel, gbc);
		
		gbc.gridx = 1;
		panel.add(nachnameField, gbc);
		
		gbc.gridx = 0;
		gbc.gridy++;
		panel.add(pwLabel, gbc);
		
		gbc.gridx = 1;
		panel.add(pwField, gbc);
		
		gbc.gridx = 0; 
		gbc.gridy++;
		panel.add(standortLabel, gbc);
		
		gbc.gridx = 1;
		panel.add(standortField, gbc);
		
		gbc.gridx = 0; 
		gbc.gridy++;
		panel.add(userLevelLabel, gbc);
		
		gbc.gridx = 1;
		panel.add(userLevelCBox, gbc);
		
		gbc.gridx = 0; 
		gbc.gridy++;
		gbc.insets = new Insets(0,6,0,0);
		panel.add(sendBtn, gbc);
		
		gbc.gridx = 1;
		panel.add(btnMainMenu, gbc);
		btnMainMenu.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				app.changePanel(4, 0);
			}
		});
		
	}

}
