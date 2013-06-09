package de.steinleostolski.client2;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

import de.steinleostolski.client2.panels.EditUserPanel;
import de.steinleostolski.client2.panels.MainMenuPanel;
import de.steinleostolski.client2.panels.NewTicketPanel;
import de.steinleostolski.client2.panels.NewUserPanel;
import de.steinleostolski.client2.panels.ViewTicketPanel;
import de.steinleostolski.client2.panels.ViewUserPanel;
import de.steinleostolski.user.Userdb;
import de.steinleostolski.xmpp.PubsubClient;

public class Application extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -915354554495610098L;
	
	private PubsubClient pubsub;
	private Userdb user;
	
	private List<JPanel> panelList;
	
	public Application(PubsubClient pubsub) {
		this.pubsub = pubsub;
		user = new Userdb();
		initUI();
	}

	private void initUI() {
		panelList = new ArrayList<JPanel>();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(800, 600);
		setTitle("Ticket System");
		
		MainMenuPanel mmPanel = new MainMenuPanel(this, pubsub, user);
		EditUserPanel euPanel = new EditUserPanel(this, pubsub, user);
		ViewUserPanel vuPanel = new ViewUserPanel(this, pubsub, user);
		ViewTicketPanel vtPanel = new ViewTicketPanel(this, pubsub, user);
		NewUserPanel nuPanel = new NewUserPanel(this, pubsub, user);
		NewTicketPanel ntPanel = new NewTicketPanel(this, pubsub, user);
		panelList.add(0, mmPanel); panelList.add(1, euPanel);
		panelList.add(2, vuPanel); panelList.add(3, vtPanel);
		panelList.add(4, nuPanel); panelList.add(5, ntPanel);
		getContentPane().add(panelList.get(0), BorderLayout.CENTER);
	}
	
	public void changePanel(int oldPanel, int newPanel) {
		panelList.get(oldPanel).setVisible(false);
		getContentPane().add(panelList.get(newPanel), BorderLayout.CENTER);
		panelList.get(newPanel).setVisible(true);
		
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@SuppressWarnings("deprecation")
			public void run() {
				try {
					LoginWindow login = new LoginWindow();
					login.show();
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

}
