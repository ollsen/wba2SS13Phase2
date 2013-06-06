package de.steinleostolski.client;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.BoxLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import javax.swing.JProgressBar;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.io.IOException;
import java.io.StringReader;

import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JTable;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

import de.steinleostolski.jaxb.Ressource;
import de.steinleostolski.payload.Notification;
import de.steinleostolski.user.Userdb;
import de.steinleostolski.xmpp.PubsubClient;

public class Menu extends JFrame {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private PubsubClient pubsub;
	private Ressource res;
	private JLabel lbldfield;
	private JLabel lbNamefield;
	private JLabel lbStatusfield;
	private JLabel lbTicketsfield;


	
	/**
	 * Create the frame.
	 */
	public Menu(PubsubClient pubsub) {
		this.pubsub = pubsub;
		setTitle("Hauptmenü");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
		
		JPanel MenuPanel = new JPanel();
		MenuPanel.setAlignmentY(Component.TOP_ALIGNMENT);
		MenuPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		contentPane.add(MenuPanel);
		MenuPanel.setLayout(new BoxLayout(MenuPanel, BoxLayout.Y_AXIS));
		
		JPanel profilePanel = new JPanel();
		profilePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		contentPane.add(profilePanel);
		profilePanel.setLayout(null);
		
		JLabel lblProfile = new JLabel("Profile");
		lblProfile.setBounds(6, 6, 61, 16);
		profilePanel.add(lblProfile);
		
		JLabel lblName = new JLabel("Name");
		lblName.setBounds(6, 66, 61, 16);
		profilePanel.add(lblName);
		
		JLabel lblId = new JLabel("ID");
		lblId.setBounds(6, 46, 61, 16);
		profilePanel.add(lblId);
		
		JLabel lblTickets = new JLabel("Tickets");
		lblTickets.setBounds(6, 103, 61, 16);
		profilePanel.add(lblTickets);
		
		JLabel lblStatus = new JLabel("Status");
		lblStatus.setBounds(6, 84, 61, 16);
		profilePanel.add(lblStatus);
		
		lbldfield = new JLabel("loading");
		lbldfield.setBounds(79, 46, 135, 16);
		profilePanel.add(lbldfield);
		
		lbNamefield = new JLabel("loading");
		lbNamefield.setBounds(79, 66, 135, 16);
		profilePanel.add(lbNamefield);
		
		lbStatusfield = new JLabel("loading");
		lbStatusfield.setBounds(79, 84, 135, 16);
		profilePanel.add(lbStatusfield);
		
		lbTicketsfield = new JLabel("loading");
		lbTicketsfield.setBounds(79, 103, 135, 16);
		profilePanel.add(lbTicketsfield);
		
		JPanel panel = new JPanel();
		panel.setAlignmentY(Component.TOP_ALIGNMENT);
		contentPane.add(panel);
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		
		JProgressBar progressBar = new JProgressBar();
		panel.add(progressBar);
		
		contentPane.setSize(800, 600);
		try {
			loadProfile();
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void loadProfile() throws JAXBException, IOException {
		Client client = Client.create();
		WebResource webResource = client
				   .resource("http://localhost:4434/users/");
	    // lets get the XML as a String
	    String text = webResource.accept("application/xml").get(String.class);
	    System.out.println(text);
	    JAXBContext jc = JAXBContext.newInstance(Userdb.class);
		Unmarshaller unmarshaller = jc.createUnmarshaller();
		StringReader reader = new StringReader(text);
		Userdb userdb = (Userdb) unmarshaller.unmarshal(reader);
		Userdb user = new Userdb();
		
		for(int i = 0; i < userdb.getUser().size(); i++) {
			if(userdb.getUser().get(i).getJabber().equalsIgnoreCase(pubsub.getUsername())) {
				user.getUser().add(userdb.getUser().get(i));
			}
		}
		
		lbldfield.setText(user.getUser().get(0).getId().toString());
		lbNamefield.setText(user.getUser().get(0).getVorname()+" "
				+user.getUser().get(0).getNachname());
		lbStatusfield.setText(user.getUser().get(0).getStatus());
		lbTicketsfield.setText(String.valueOf(user.getUser().get(0).getTickets().getTicketId().size()));
	}
}
