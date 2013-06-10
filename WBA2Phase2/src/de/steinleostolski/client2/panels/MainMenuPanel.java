package de.steinleostolski.client2.panels;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.StringReader;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

import de.steinleostolski.client2.Application;
import de.steinleostolski.user.Userdb;
import de.steinleostolski.xmpp.PubsubClient;

public class MainMenuPanel extends JPanel {
	
	private JLabel idField;
	private JLabel nameField;
	private JLabel standortField;
	private JLabel userlevelField;
	private JLabel anzahlTicketsfield;
	
	private Application app;
	private Userdb user;
	private PubsubClient pubsub;

	/**
	 * 
	 */
	private static final long serialVersionUID = 5218170682835075706L;

	public MainMenuPanel(Application app, PubsubClient pubsub, Userdb user) {
		this.app = app;
		this.pubsub = pubsub;
		this.user = user;
		initUI();
	}

	private void initUI() {
		Dimension buttonDim = new Dimension(150,25);
		setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		
		JPanel leftNaviPanel = createLeftNavi();
		
		JPanel profilePanel = loadProfilePanel();
		profilePanel.setBorder(BorderFactory.createTitledBorder("Profil"));
		profilePanel.setPreferredSize(new Dimension(400, 450));
		
		
		
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 1;
		gbc.gridheight = 5;
		add(leftNaviPanel,gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 0;
		//gbc.gridheight = 2;
		gbc.weightx = 1;
		//gbc.anchor = GridBagConstraints.NORTH;
		add(profilePanel, gbc);
		
	}
	
	private JPanel createLeftNavi() {
		JPanel panel = new JPanel();
		Dimension buttonDim = new Dimension(150,25);
		
		
		JButton btnViewTickets = new JButton("View Tickets");
		JButton btnEditProfile = new JButton("Edit Profile");
		JButton btnNewTicket = new JButton("New Ticket");
		JButton btnNewUser = new JButton("New User");
		JButton btnViewUsers = new JButton("View Users");
		JButton btnSettings = new JButton ("Settings");
		panel.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		
		gbc.insets = new Insets(8,16,8,16);
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 1;
		
		switch(UserLevel.valueOf(user.getUser().get(0).getStatus().toUpperCase())) {
		case USER:
			add(btnEditProfile,gbc);
			gbc.gridy++;
			add(btnViewTickets,gbc);
			gbc.gridy++;
			add(btnNewTicket,gbc);
			break;
		case SUPPORTER:
			add(btnEditProfile,gbc);
			gbc.gridy++;
			add(btnViewTickets,gbc);
			break;
		case ADMIN:
			add(btnEditProfile,gbc);
			gbc.gridy++;
			add(btnViewTickets,gbc);
			gbc.gridy++;
			add(btnViewUsers,gbc);
			gbc.gridy++;
			add(btnNewTicket,gbc);
			gbc.gridy++;
			add(btnNewUser,gbc);
			gbc.gridy++;
			add(btnSettings, gbc);
		}
		
		btnEditProfile.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				app.changePanel(0, 1);
			}
		});
		
		btnViewTickets.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				app.changePanel(0, 3);
			}
		});
		
		btnViewUsers.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				app.changePanel(0, 2);
			}
		});
		
		btnNewTicket.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				app.changePanel(0, 5);
			}
		});
		
		btnNewUser.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				app.changePanel(0, 4);
			}
		});
		
		btnSettings.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				app.changePanel(0, 6);
			}
		});
		
		return panel;
	}

	private JPanel loadProfilePanel() {
		JPanel panel = new JPanel();
		
		JLabel idLabel = new JLabel("ID");
		JLabel nameLabel = new JLabel("Name:");
		JLabel standortLabel = new JLabel("Standort:");
		JLabel userlevelLabel = new JLabel("Level:");
		JLabel anzahlTicketsLabel = new JLabel("Anzahl Tickets:");
		
		idField = new JLabel("loading");
		nameField = new JLabel("loading");
		standortField = new JLabel("loading");
		userlevelField = new JLabel("loading");
		anzahlTicketsfield = new JLabel("loading");
		
		refreshProfile();
		
		panel.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		
		gbc.insets = new Insets(2,8,2,8);
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 0.2;
		gbc.weighty = 0.2;
		
		panel.add(idLabel, gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 0;
		
		panel.add(idField, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 1;
		panel.add(nameLabel, gbc);
		
		gbc.gridx = 1;
		panel.add(nameField, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 2;
		panel.add(standortLabel, gbc);
		
		gbc.gridx = 1;
		panel.add(standortField, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 3;
		panel.add(userlevelLabel, gbc);
		
		gbc.gridx = 1;
		panel.add(userlevelField, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 4;
		gbc.gridheight = 3;
		panel.add(anzahlTicketsLabel, gbc);
		
		gbc.gridx = 1;
		panel.add(anzahlTicketsfield, gbc);
		return panel;
	}
	
	public void refreshProfile() {
		idField.setText(String.valueOf(user.getUser().get(0).getId()));
		nameField.setText(user.getUser().get(0).getVorname()+" "
				+user.getUser().get(0).getNachname());
		standortField.setText(user.getUser().get(0).getStandort());
		userlevelField.setText(user.getUser().get(0).getStatus());
		anzahlTicketsfield.setText(String.valueOf(user.getUser().get(0).getTickets().getTicketId().size()));
	}
	
	public enum UserLevel {
		USER, SUPPORTER, ADMIN
	}

}
