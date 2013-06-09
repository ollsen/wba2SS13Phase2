package de.steinleostolski.client2.panels;


import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

import de.steinleostolski.client2.Application;
import de.steinleostolski.user.Userdb;
import de.steinleostolski.xmpp.PubsubClient;

public class NewTicketPanel extends JPanel {
	
	private Application app; 
	private PubsubClient pubsub;
	private Userdb user;
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 489852620191460633L;

	public NewTicketPanel(Application app, PubsubClient pubsub, Userdb user) {
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
		
		JLabel platzhalter = new JLabel("New Ticket");
		
		JButton btnMainMenu = new JButton("Abbrechen");
		JLabel subjectLabel = new JLabel("Betreff");
		JLabel priorityLabel = new JLabel("Priorität");
		JLabel descriptionLabel = new JLabel("Beschreibung");
		
		JTextField subjectField = new JTextField(32);
		JComboBox priorityCBox = new JComboBox();
		JTextArea descriptionTArea = new JTextArea(16, 8);
		descriptionTArea.setLineWrap(true);
		JScrollPane descrptionScroll = new JScrollPane(descriptionTArea, 
				ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		
		JButton sendBtn = new JButton("Senden");
		
		priorityCBox.addItem("Niedrig");
		priorityCBox.addItem("Normal");
		priorityCBox.addItem("Hoch");
		
		gbc.insets = new Insets(2, 2, 2, 2);
		gbc.anchor= GridBagConstraints.WEST;
		gbc.weightx = 1; gbc.weighty = 1;
		gbc.gridx = 0; gbc.gridy = 0;
		panel.add(subjectLabel, gbc);
		
		gbc.gridx++;
		panel.add(subjectField, gbc);
		
		gbc.gridx--; gbc.gridy++;
		panel.add(priorityLabel, gbc);
		
		gbc.gridx++;
		panel.add(priorityCBox, gbc);
		
		gbc.gridx--; gbc.gridy++;
		panel.add(descriptionLabel, gbc);
		
		gbc.gridy++;
		gbc.gridwidth = 2;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		panel.add(descrptionScroll, gbc);
		
		gbc.gridy++;
		gbc.gridwidth = 1;
		gbc.fill = GridBagConstraints.NONE;
		panel.add(sendBtn, gbc);
		
		gbc.gridx++;
		panel.add(btnMainMenu, gbc);
		
		btnMainMenu.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				app.changePanel(5, 0);
			}
		});
		
	}

}
