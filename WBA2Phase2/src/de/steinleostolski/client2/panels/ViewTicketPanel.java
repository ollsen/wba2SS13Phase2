package de.steinleostolski.client2.panels;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;

import de.steinleostolski.client2.Application;
import de.steinleostolski.user.Userdb;
import de.steinleostolski.xmpp.PubsubClient;

public class ViewTicketPanel extends JPanel {
	
	private Application app;
	private PubsubClient pubsub;
	private Userdb user;

	public ViewTicketPanel(Application app, PubsubClient pubsub, Userdb user) {
		this.app = app;
		this.pubsub = pubsub;
		this.user = user;
		initUI();
	}

	private void initUI() {
		JPanel panel = new JPanel();
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.anchor= GridBagConstraints.WEST;
		add(panel, gbc);
		panel.setLayout(new GridBagLayout());
		
		
		JPanel listPanel = createListPanel();
		JPanel summaryPanel = createSummaryPanel();
		
		summaryPanel.setBorder(BorderFactory.createTitledBorder("Info"));
		summaryPanel.setPreferredSize(new Dimension(250, 300));
		
		JButton btnMainMenu = new JButton("Main Menu");
		gbc.anchor= GridBagConstraints.NORTHWEST;
		gbc.insets = new Insets(4,4,4,4);
		gbc.weightx = 1; gbc.weighty = 1;
		gbc.gridx = 0; gbc.gridy = 0;
		
		panel.add(listPanel, gbc);
		
		gbc.gridx++;
		panel.add(summaryPanel, gbc);
		
		gbc.gridx--;
		gbc.gridy++;
		panel.add(btnMainMenu, gbc);
		
		btnMainMenu.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				app.changePanel(3, 0);
			}
		});
		
	}

	private JPanel createSummaryPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		
		return panel;
	}

	private JPanel createListPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		
		String[] columnNames = {"First Name",
                "Last Name",
                "Sport",
                "# of Years",
                "Vegetarian"};
		
		Object[][] data = {
			    {"Kathy", "Smith",
			     "Snowboarding", new Integer(5), new Boolean(false)},
			    {"John", "Doe",
			     "Rowing", new Integer(3), new Boolean(true)},
			    {"Sue", "Black",
			     "Knitting", new Integer(2), new Boolean(false)},
			    {"Jane", "White",
			     "Speed reading", new Integer(20), new Boolean(true)},
			    {"Joe", "Brown",
			     "Pool", new Integer(10), new Boolean(false)}
			};
		
		GridBagConstraints gbc = new GridBagConstraints();
		JTable ticketTable = new JTable(data, columnNames);
		JScrollPane tableScrollPane = new JScrollPane(ticketTable, 
				ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		
		panel.add(tableScrollPane);
		return panel;
	}

}
