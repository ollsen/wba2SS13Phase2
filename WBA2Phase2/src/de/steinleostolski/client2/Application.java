package de.steinleostolski.client2;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.GridBagLayout;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

import de.steinleostolski.client2.panels.EditUserPanel;
import de.steinleostolski.client2.panels.MainMenuPanel;
import de.steinleostolski.client2.panels.NewTicketPanel;
import de.steinleostolski.client2.panels.NewUserPanel;
import de.steinleostolski.client2.panels.SettingsPanel;
import de.steinleostolski.client2.panels.ViewTicketPanel;
import de.steinleostolski.client2.panels.ViewTicketlistPanel;
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
	private MainMenuPanel mmPanel;
	private NewTicketPanel ntPanel;
	private EditUserPanel euPanel;
	private ViewTicketlistPanel vtlPanel;
	private ViewTicketPanel vtPanel;
	
	private List<JPanel> panelList;
	
	public Application(PubsubClient pubsub) {
		this.pubsub = pubsub;
		user = new Userdb();
		try {
			loadProfile();
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		initUI();
	}

	private void initUI() {
		panelList = new ArrayList<JPanel>();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(800, 600);
		setTitle("Ticket System");
		
		mmPanel = new MainMenuPanel(this, pubsub, user);
		euPanel = new EditUserPanel(this, pubsub, user);
		ViewUserPanel vuPanel = new ViewUserPanel(this, pubsub, user);
		vtlPanel = new ViewTicketlistPanel(this, pubsub, user);
		NewUserPanel nuPanel = new NewUserPanel(this, pubsub, user);
		ntPanel = new NewTicketPanel(this, pubsub, user);
		SettingsPanel sPanel = new SettingsPanel(this, pubsub, user);
		vtPanel = new ViewTicketPanel(this, pubsub, user);
		panelList.add(0, mmPanel); panelList.add(1, euPanel);
		panelList.add(2, vuPanel); panelList.add(3, vtlPanel);
		panelList.add(4, nuPanel); panelList.add(5, ntPanel);
		panelList.add(6, sPanel); panelList.add(7, vtPanel);
		getContentPane().add(panelList.get(0), BorderLayout.CENTER);
	}
	
	public void changePanel(int oldPanel, int newPanel) {
		panelList.get(oldPanel).setVisible(false);
		getContentPane().add(panelList.get(newPanel), BorderLayout.CENTER);
		panelList.get(newPanel).setVisible(true);
		
		if(newPanel == 0) {
			try {
				loadProfile();
				mmPanel.refreshProfile();
			} catch (JAXBException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else
		
		if(newPanel ==1) {
			euPanel.refresh();
		} else
		
		if(newPanel == 5) {
			ntPanel.refreshItFields();
		} 
		
		if (newPanel == 7) {
			try {
				vtPanel.loadTicket(vtlPanel.getTicketId());
			} catch (JAXBException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
	}
	
	public void loadProfile() throws JAXBException, IOException {
		
		if(user.getUser().size() != 0) {
			user.getUser().remove(0);
		}
		Client client = Client.create();
		WebResource webResource = client
				   .resource("http://localhost:4434/user/");
	    // lets get the XML as a String
	    String text = webResource.accept("application/xml").get(String.class);
	    JAXBContext jc = JAXBContext.newInstance(Userdb.class);
		Unmarshaller unmarshaller = jc.createUnmarshaller();
		StringReader reader = new StringReader(text);
		Userdb userdb = (Userdb) unmarshaller.unmarshal(reader);
		
		for(int i = 0; i < userdb.getUser().size(); i++) {
			if(userdb.getUser().get(i).getJabber().equalsIgnoreCase(pubsub.getUsername())) {
				user.getUser().add(userdb.getUser().get(i));
			}
		}
		
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
