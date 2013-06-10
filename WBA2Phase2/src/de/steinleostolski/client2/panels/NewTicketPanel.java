package de.steinleostolski.client2.panels;


import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.security.auth.Subject;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.Popup;
import javax.swing.PopupFactory;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ListDataListener;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import de.steinleostolski.client2.Application;
import de.steinleostolski.settings.Settings;
import de.steinleostolski.ticket.CtInfo;
import de.steinleostolski.ticket.CtInfo.SupporterList;
import de.steinleostolski.ticket.CtInfo.Tags;
import de.steinleostolski.ticket.CtInfo.User;
import de.steinleostolski.ticket.CtTicket.Antworten;
import de.steinleostolski.ticket.Ticket;
import de.steinleostolski.user.Userdb;
import de.steinleostolski.xmpp.PubsubClient;

public class NewTicketPanel extends JPanel {
	
	private Application app; 
	private PubsubClient pubsub;
	private Userdb user;
	
	private JTextField subjectField;
	private JComboBox priorityCBox;
	private JTextArea descriptionTArea;
	private JScrollPane descrptionScroll;
	private JList itFieldList;
	private JScrollPane itFieldScroll;
	
	private Popup popup;
	private JButton popupButton = new JButton("schliessen");
	

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
		
		JButton btnMainMenu = new JButton("Abbrechen");
		JLabel subjectLabel = new JLabel("Betreff");
		JLabel priorityLabel = new JLabel("Priorität");
		JLabel itFieldLabel = new JLabel("Fachgebiet");
		JLabel descriptionLabel = new JLabel("Beschreibung");
		
		subjectField = new JTextField(32);
		priorityCBox = new JComboBox();
		
		
		try {
			Object[] listData = loadItFields();
			itFieldList = new JList(listData);
		} catch (JAXBException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		itFieldList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		itFieldScroll = new JScrollPane(itFieldList,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		itFieldScroll.setPreferredSize(new Dimension(150, 100));
		
		descriptionTArea = new JTextArea(16, 8);
		descriptionTArea.setLineWrap(true);
		descriptionTArea.setWrapStyleWord(true);
		
		
		descrptionScroll = new JScrollPane(descriptionTArea, 
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
		panel.add(itFieldLabel, gbc);
		
		gbc.gridx++;
		panel.add(itFieldScroll, gbc);
		
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
			public void actionPerformed(ActionEvent event) {
				app.changePanel(5, 0);
			}
		});
		
		sendBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				
				Ticket ticket = new Ticket();
				
				ticket.setInfos(new CtInfo());
				ticket.setAntworten(new Antworten());
				ticket.getInfos().setTags(new Tags());
				ticket.getInfos().setSupporterList(new SupporterList());
				ticket.getInfos().setUser(new User());
				
				ticket.getInfos().getUser().setId(user.getUser().get(0).getId());
				ticket.getInfos().getUser().setValue(user.getUser().get(0).getVorname()+" "
						+user.getUser().get(0).getNachname());
				ticket.getInfos().setStandort(user.getUser().get(0).getStandort());
				
				ticket.getInfos().setDatum(getDate());
				ticket.getInfos().setBetreff(subjectField.getText());
				ticket.getInfos().setZustand(priorityCBox.getSelectedItem().toString());
				ticket.getInfos().setBeschreibung(descriptionTArea.getText());
				
				ListModel lm = itFieldList.getModel();
				int[] sel = itFieldList.getSelectedIndices();
				for (int i = 0; i < sel.length; i++) {
					ticket.getInfos().getTags().getTag().add(lm.getElementAt(sel[i]).toString());
				}
				
				Client client = Client.create();
				try { 
					WebResource webResource = client
					   .resource("http://localhost:4434/ticket/add/");
					
					ClientResponse response = webResource.accept("MediaType.APPLICATION_XML")
							.post(ClientResponse.class, ticket);
			 
					if (response.getStatus() != 201) {
						throw new RuntimeException("Failed : HTTP error code : "
						     + response.getStatus());
					}
				    int x = getSize().width / 2;
				    int y = getSize().height / 2;
					JPanel popupPanel = new JPanel(new BorderLayout());
					JLabel statusLabel = new JLabel(response.getEntity(String.class));
					popupPanel.add(statusLabel, BorderLayout.CENTER);
					popupPanel.add(popupButton, BorderLayout.SOUTH);
					PopupFactory factory = PopupFactory.getSharedInstance();
					popup = factory.getPopup(new JFrame(), popupPanel, x, y);
					popup.show();
					
					
				} catch (Exception e) {
						 
					e.printStackTrace();
					 
				}
			}
		});
		
		popupButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				resetFields();
				popup.hide();
				app.changePanel(5, 0);
			}
		});
	}
	
	public void refreshItFields() {
		try {
			itFieldList.setListData(loadItFields());
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Object[] loadItFields() throws JAXBException {
		Object[] itFieldList;
		Client client = Client.create();
		WebResource webResource = client
				   .resource("http://localhost:4434/settings/");
	    // lets get the XML as a String
	    String text = webResource.accept("application/xml").get(String.class);
	    JAXBContext jc = JAXBContext.newInstance(Settings.class);
		Unmarshaller unmarshaller = jc.createUnmarshaller();
		StringReader reader = new StringReader(text);
		Settings itFields = (Settings) unmarshaller.unmarshal(reader);
		itFieldList = new Object[itFields.getITBereich().getFachbereich().size()];
		
		for(int i = 0; i < itFields.getITBereich().getFachbereich().size(); i++) {
			itFieldList[i] = itFields.getITBereich().getFachbereich().get(i);
		}
		return itFieldList;
	}

	protected void resetFields() {
		subjectField.setText("");
		priorityCBox.setSelectedIndex(0);
		descriptionTArea.setText("");
		itFieldList.clearSelection();
	}

	private XMLGregorianCalendar getDate() {
		// Datum und Uhrzeit
		GregorianCalendar gCalendar = new GregorianCalendar();
		Date currentDate = new Date();
		gCalendar.setTime(currentDate);
		XMLGregorianCalendar xmlCalendar = null;
		try {
			xmlCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(gCalendar);
			} catch (DatatypeConfigurationException ex) {
		}
		return xmlCalendar;
	}
}
