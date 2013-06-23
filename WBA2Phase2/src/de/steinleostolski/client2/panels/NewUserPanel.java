package de.steinleostolski.client2.panels;


import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.Popup;
import javax.swing.PopupFactory;

import org.jivesoftware.smack.AccountManager;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.sasl.SASLMechanism.Response;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import de.steinleostolski.client2.Application;
import de.steinleostolski.client2.LoginWindow;
import de.steinleostolski.user.CtProfile;
import de.steinleostolski.user.CtProfile.KnowHows;
import de.steinleostolski.user.Cttickets;
import de.steinleostolski.user.Userdb;
import de.steinleostolski.xmpp.PubsubClient;

public class NewUserPanel extends JPanel {
	
	private Application app; 
	private PubsubClient pubsub;
	private Userdb user;
	
	private JTextField vornameField;
	private JTextField nachnameField;
	private JTextField standortField;
	private JComboBox userLevelCBox;
	private JPasswordField pwField;

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
		
		vornameField = new JTextField(16);
		nachnameField = new JTextField(16);
		standortField = new JTextField(16);
		userLevelCBox = new JComboBox();
		pwField = new JPasswordField(16);
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
		
		sendBtn.addActionListener(new ActionListener() {
			
			@SuppressWarnings("deprecation")
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Userdb newUser = new Userdb();
				newUser.getUser().add(new CtProfile());
				newUser.getUser().get(0).setKnowHows(new KnowHows());
				newUser.getUser().get(0).setTickets(new Cttickets());
				
				String jabberId = vornameField.getText().
						substring(0,1).toLowerCase()+nachnameField.getText().toLowerCase();
				
				newUser.getUser().get(0).setJabber(jabberId);
				newUser.getUser().get(0).setVorname(vornameField.getText());
				newUser.getUser().get(0).setNachname(nachnameField.getText());
				newUser.getUser().get(0).setStandort(standortField.getText());
				newUser.getUser().get(0).setStatus(userLevelCBox.getSelectedItem().toString());
				
				AccountManager am = new AccountManager(pubsub.getConnection());
				try {
					am.createAccount(jabberId, pwField.getText());
				} catch (XMPPException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				Client client = Client.create();
				try { 
					WebResource webResource = client
					   .resource("http://"+LoginWindow.adress+":4434/user/add/");
					
					ClientResponse response = webResource.accept("MediaType.APPLICATION_XML")
							.post(ClientResponse.class, newUser);
			 
					if (response.getStatus() != 201) {
						throw new RuntimeException("Failed : HTTP error code : "
						     + response.getStatus());
					}
					showPopup(response.getEntity(String.class));
				} catch (Exception e) {
						 
					e.printStackTrace();
					 
				}
				
			}
		});
	}
	
	private void showPopup(String status) {
		int x = getSize().width / 2;
	    int y = getSize().height / 2;
		JPanel popupPanel = new JPanel(new BorderLayout());
		JLabel statusLabel = new JLabel(status);
		JButton popupButton = new JButton("Schliessen");
		popupPanel.add(statusLabel, BorderLayout.CENTER);
		popupPanel.add(popupButton, BorderLayout.SOUTH);
		PopupFactory factory = PopupFactory.getSharedInstance();
		final Popup popup = factory.getPopup(new JFrame(), popupPanel, x, y);
		popup.show();
		
		popupButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				popup.hide();
				app.changePanel(4, 0);
				resetFields();
			}
		});
	}
	
	protected void resetFields() {
		vornameField.setText("");
		nachnameField.setText("");
		pwField.setText("");
		standortField.setText("");
		userLevelCBox.setSelectedIndex(0);
	}

}
