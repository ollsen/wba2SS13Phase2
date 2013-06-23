package de.steinleostolski.client2.panels;


import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.StringReader;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
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
import de.steinleostolski.client2.LoginWindow;
import de.steinleostolski.ticket.CtAntwort;
import de.steinleostolski.ticket.CtAntwort.Supporter;
import de.steinleostolski.ticket.Ticket;
import de.steinleostolski.user.Userdb;
import de.steinleostolski.xmpp.PubsubClient;

public class ViewTicketPanel extends JPanel {
	
	private Application app; 
	private Userdb user;
	private Ticket ticket;
	
	private JPanel mainPanel;
	private JPanel AnswerMainPanel;
	private Iterator<String> iterator;
	
	private JTextArea datumField;
	private JTextArea betreffField;
	private JTextArea creatorField;
	private JTextArea standortField;
	private JTextArea priorityField;
	private JTextArea inProgressField;
	private JTextArea descriptionTArea;
	private JTextArea itFieldField;
	private JTextArea answerTArea;
	private JButton sendBtn;
	private JButton takeBtn;
	private JButton releaseBtn;
	private JButton closeBtn;
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 489852620191460633L;

	public ViewTicketPanel(Application app, PubsubClient pubsub, Userdb user) {
		this.app = app;
		this.user = user;
		initUI();
	}

	private void initUI() {
		mainPanel = new JPanel();
		
		JScrollPane scrollPanel = new JScrollPane(mainPanel, 
				ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, 
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPanel.setPreferredSize(new Dimension(800, 575));
		add(scrollPanel);
		mainPanel.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		
		JPanel infoPanel = loadInfoPanel();
		JPanel answerPanel = loadAnswerPanel();
		
		JButton btnMainMenu = new JButton("Main Menu");
		takeBtn = new JButton("Ticket übernehmen");
		releaseBtn = new JButton("Ticket freigeben");
		closeBtn = new JButton("Ticket Schließen");
		
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 4;
		gbc.anchor = GridBagConstraints.NORTH;
		mainPanel.add(infoPanel, gbc);
		gbc.gridy++;
		gbc.gridwidth = 1;
		gbc.anchor = GridBagConstraints.WEST;
		mainPanel.add(btnMainMenu, gbc);
		gbc.gridx++;
		mainPanel.add(takeBtn, gbc);
		mainPanel.add(releaseBtn, gbc);
		gbc.gridx++;
		mainPanel.add(closeBtn, gbc);
		gbc.gridx = 0;
		gbc.gridy++;
		gbc.gridwidth = 4;
		gbc.anchor = GridBagConstraints.NORTH;
		
		takeBtn.setVisible(false);
		releaseBtn.setVisible(false);
		closeBtn.setVisible(false);
		mainPanel.add(answerPanel, gbc);
		
		btnMainMenu.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				app.changePanel(7, 3);
			}
		});
		
		takeBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				safeNewSupporter();
			}
		});
		
		releaseBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				releaseTicket();
			}
		});
		
		closeBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				closeTicket();
			}
		});
	}
	


	private JPanel loadInfoPanel() {
		JPanel panel = new JPanel();
		panel.setBorder(BorderFactory.createTitledBorder("Info"));
		panel.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		
		JLabel datumLabel = new JLabel("Datum:");
		JLabel betreffLabel = new JLabel("Betreff:");
		JLabel creatorLabel = new JLabel("Ersteller:");
		JLabel standortLabel = new JLabel("Standort:");
		JLabel itFieldLabel = new JLabel("Fachgebiet:");
		JLabel priorityLabel = new JLabel("Zustand:");
		JLabel inProgressLabel = new JLabel("Es bearbeitet für Sie:");
		JLabel descriptionLabel =  new JLabel("Beschreibung:");
		
		datumField = new JTextArea(1, 32);
		datumField.setBackground(panel.getBackground());
		datumField.setEditable(false);
		betreffField = new JTextArea();
		betreffField.setBackground(panel.getBackground());
		betreffField.setEditable(false);
		creatorField = new JTextArea();
		creatorField.setBackground(panel.getBackground());
		creatorField.setEditable(false);
		standortField = new JTextArea();
		standortField.setBackground(panel.getBackground());
		standortField.setEditable(false);
		itFieldField = new JTextArea();
		itFieldField.setBackground(panel.getBackground());
		itFieldField.setEditable(false);
		priorityField = new JTextArea();
		priorityField.setBackground(panel.getBackground());
		priorityField.setEditable(false);
		inProgressField = new JTextArea();
		inProgressField.setBackground(panel.getBackground());
		inProgressField.setEditable(false);
		descriptionTArea = new JTextArea(2, 48);
		descriptionTArea.setBackground(panel.getBackground());
		descriptionTArea.setEditable(false);
		
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 0; gbc.gridy = 0;
		gbc.insets = new Insets(2, 2, 2, 2);
		gbc.weighty = 1;
		panel.add(datumLabel, gbc);
		gbc.gridx++;
		panel.add(datumField, gbc);
		gbc.gridx--; gbc.gridy++;
		panel.add(betreffLabel, gbc);
		gbc.gridx++;
		panel.add(betreffField, gbc);
		gbc.gridx--; gbc.gridy++;
		panel.add(creatorLabel, gbc);
		gbc.gridx++;
		panel.add(creatorField, gbc);
		gbc.gridx--; gbc.gridy++;
		panel.add(standortLabel, gbc);
		gbc.gridx++;
		panel.add(standortField, gbc);
		gbc.gridx--; gbc.gridy++;
		panel.add(itFieldLabel, gbc);
		gbc.gridx++;
		panel.add(itFieldField, gbc);
		gbc.gridx--; gbc.gridy++;
		panel.add(priorityLabel, gbc);
		gbc.gridx++;
		panel.add(priorityField, gbc);
		gbc.gridx--; gbc.gridy++;
		panel.add(inProgressLabel, gbc);
		gbc.gridx++;
		panel.add(inProgressField, gbc);
		gbc.gridx--; gbc.gridy++;
		panel.add(descriptionLabel, gbc);
		gbc.gridy++;
		gbc.gridwidth = 2;
		panel.add(descriptionTArea, gbc);
		
		
		return panel;
	}

	private JPanel loadAnswerPanel() {
		
		AnswerMainPanel = new JPanel();
		AnswerMainPanel.setBorder(BorderFactory.createTitledBorder("Antworten"));
		AnswerMainPanel.setLayout(new BoxLayout(AnswerMainPanel, BoxLayout.Y_AXIS));
		
		JPanel answerField = setAnswer();
		AnswerMainPanel.add(answerField);
		
		return AnswerMainPanel;
	}

	private JPanel setAnswer() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		JLabel answerLabel = new JLabel("Antwort verfassen:");
		answerTArea = new JTextArea(8, 48);
		answerTArea.setLineWrap(true);
		answerTArea.setWrapStyleWord(true);
		JScrollPane answerScrollPane = new JScrollPane(answerTArea, 
				ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, 
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		gbc.insets = new Insets(2, 2, 2, 2);
		gbc.anchor = GridBagConstraints.WEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 1;
		gbc.gridx = 0; gbc.gridy = 0;
		panel.add(answerLabel, gbc);
		gbc.gridy++;
		gbc.gridwidth = 2;
		panel.add(answerScrollPane, gbc);
		
		return panel;
	}
	
	private JPanel loadAnswer(int i) {
		JPanel panel = new JPanel();
		
		panel.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		
		JLabel userLabel = new JLabel("Benutzer:");
		JLabel datumLabel = new JLabel("Datum:");
		JLabel antwortLabel = new JLabel("Antwort:");
		JTextArea userField = new JTextArea(ticket.getAntworten().getAntwort().get(i)
				.getSupporter().getValue(),1,32);
		userField.setLineWrap(true);
		userField.setWrapStyleWord(true);
		userField.setBackground(panel.getBackground());
		JTextArea datumField = new JTextArea(gregorianToDate(ticket.getAntworten()
				.getAntwort().get(i).getDatum()));
		datumField.setBackground(panel.getBackground());
		JTextArea antwortTArea = new JTextArea(ticket.getAntworten().getAntwort().get(i)
				.getAntwort());
		antwortTArea.setColumns(48);
		antwortTArea.setBackground(panel.getBackground());
		
		gbc.insets = new Insets(2, 2, 2, 2);
		gbc.anchor = GridBagConstraints.WEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 1;
		gbc.gridx = 0; gbc.gridy = 0;
		panel.add(userLabel, gbc);
		gbc.gridx++;
		panel.add(userField, gbc);
		gbc.gridx--; gbc.gridy++;
		panel.add(datumLabel, gbc);
		gbc.gridx++;
		panel.add(datumField, gbc);
		gbc.gridx--; gbc.gridy++;
		panel.add(antwortLabel, gbc);
		gbc.gridy++;
		gbc.gridwidth = 2;
		gbc.insets = new Insets(2, 2, 16, 2);
		panel.add(antwortTArea, gbc);
		
		return panel;
	}
	
	protected void safeNewSupporter() {
		
		de.steinleostolski.ticket.CtInfo.SupporterList.Supporter supporter = new de.steinleostolski.ticket.CtInfo.SupporterList.Supporter();
		supporter.setId(user.getUser().get(0).getId());
		supporter.setValue(user.getUser().get(0).getVorname()+" "+user.getUser().get(0).getNachname());
		ticket.getInfos().getSupporterList().getSupporter().add(supporter);
		ticket.getInfos().setInBearbeitung(true);
		
		Client client = Client.create();
		
		try {
			WebResource webResource = client
					   .resource("http://"+LoginWindow.adress+":4434/ticket/"+ticket.getId().toString()+"/editSupporter");
					
				ClientResponse response = webResource.accept("application/xml")
							.put(ClientResponse.class, ticket);
			 
				if (response.getStatus() != 201) {
					throw new RuntimeException("Failed : HTTP error code : "
						   + response.getStatus());
				}
			} catch (Exception e) {
						 
				e.printStackTrace();
					 
			} finally {
				try {
					loadTicket(ticket.getId());
				} catch (JAXBException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
	}
	
	protected void releaseTicket() {
		Client client = Client.create();
			
		try {
			WebResource webResource = client
						.resource("http://"+LoginWindow.adress+":4434/ticket/"+ticket.getId().toString()+"/release");
						
			ClientResponse response = webResource.accept("application/xml")
								.put(ClientResponse.class, ticket);
				 
			if (response.getStatus() != 201) {
				throw new RuntimeException("Failed : HTTP error code : "
						+ response.getStatus());
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				loadTicket(ticket.getId());
			} catch (JAXBException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	protected void closeTicket() {
		Client client = Client.create();
			
		try {
			WebResource webResource = client
					.resource("http://"+LoginWindow.adress+":4434/ticket/"+ticket.getId().toString()+"/close");
						
			ClientResponse response = webResource.accept("application/xml")
					.put(ClientResponse.class);
				 
			if (response.getStatus() != 201) {
				throw new RuntimeException("Failed : HTTP error code : "
						+ response.getStatus());
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				loadTicket(ticket.getId());
			} catch (JAXBException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	
	

	protected void safeAnswer(CtAntwort answer) {
		
		ticket.getAntworten().getAntwort().add(answer);
		
		Client client = Client.create();
		
		try { 
			WebResource webResource = client
			   .resource("http://"+LoginWindow.adress+":4434/ticket/"+ticket.getId().toString()+"/answer/");
			
			ClientResponse response = webResource.accept("application/xml")
					.put(ClientResponse.class, ticket);
	 
			if (response.getStatus() != 201) {
				throw new RuntimeException("Failed : HTTP error code : "
				     + response.getStatus());
			}
		} catch (Exception e) {
				 
			e.printStackTrace();
			 
		} finally {
			try {
				loadTicket(ticket.getId());
			} catch (JAXBException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
	}
	
	public void loadTicket(BigInteger ticketId) throws JAXBException {
		Client client = Client.create();
		WebResource webResource = client
				   .resource("http://"+LoginWindow.adress+":4434/ticket/"+ticketId.toString());
	    // lets get the XML as a String
	    String text = webResource.accept("application/xml").get(String.class);
	    JAXBContext jc = JAXBContext.newInstance(Ticket.class);
		Unmarshaller unmarshaller = jc.createUnmarshaller();
		StringReader reader = new StringReader(text);
		ticket = (Ticket) unmarshaller.unmarshal(reader);
		
		datumField.setText(gregorianToDate(ticket.getInfos().getDatum()));
		betreffField.setText(ticket.getInfos().getBetreff());
		creatorField.setText(ticket.getInfos().getUser().getValue());
		standortField.setText(ticket.getInfos().getStandort());
		iterator = ticket.getInfos().getTags().getTag().iterator();
		itFieldField.setText("");
		while(iterator.hasNext()) {
			itFieldField.append(iterator.next());
			if(iterator.hasNext())
				itFieldField.append("\n");
		}
		priorityField.setText(ticket.getInfos().getZustand());
		if(ticket.getInfos().getSupporterList().getSupporter().size() == 0) {
			inProgressField.setText("");
		} else {
			inProgressField.setText(ticket.getInfos().getSupporterList().getSupporter()
					.get(ticket.getInfos().getSupporterList().getSupporter().size()-1)
					.getValue());
		}
		descriptionTArea.setLineWrap(true);
		descriptionTArea.setWrapStyleWord(true);
		descriptionTArea.setText(ticket.getInfos().getBeschreibung());
		
		if(!user.getUser().get(0).getStatus().contains("User")) {
			if(!ticket.getInfos().isInBearbeitung() &&
					ticket.getInfos().getSupporterList().getSupporter().size() == 0) {
				takeBtn.setVisible(true);
				releaseBtn.setVisible(false);
				closeBtn.setVisible(false);
			} else if(ticket.getInfos().isInBearbeitung() &&
					ticket.getInfos().getSupporterList().getSupporter().
					get(ticket.getInfos().getSupporterList().getSupporter().
							size()-1).getId().equals(user.getUser().get(0).getId())) {
				takeBtn.setVisible(false);
				releaseBtn.setVisible(true);
				closeBtn.setVisible(true);
			} else if (ticket.getInfos().getUser().getId().equals(user.getUser().get(0)
					.getId())) {
				takeBtn.setVisible(false);
				releaseBtn.setVisible(false);
				closeBtn.setVisible(false);
			}
		}
		
		refreshAnswerPanel();
	}
	
	public void refreshAnswerPanel() {
		if(AnswerMainPanel.getComponentCount() != 0) {
			AnswerMainPanel.removeAll();
		}
		
		if(ticket != null && ticket.getAntworten().getAntwort().size() != 0) {
			for(int i = 0; i < ticket.getAntworten().getAntwort().size(); i++) {
				JPanel aPanel = loadAnswer(i);
				AnswerMainPanel.add(aPanel);
			}
		}
		
		if(ticket.getInfos().getUser().getId().equals(user.getUser().get(0).getId()) &&
				!ticket.getInfos().getZustand().equals("geschlossen")) {
			JPanel answerField = setAnswer();
			AnswerMainPanel.add(answerField);

			sendBtn = new JButton("Senden");
			AnswerMainPanel.add(sendBtn);
		} else if(ticket.getInfos().isInBearbeitung() &&
				user.getUser().get(0).getId().equals(
						ticket.getInfos().getSupporterList().getSupporter().get(
								ticket.getInfos().getSupporterList().getSupporter()
								.size()-1).getId())) {
			
			JPanel answerField = setAnswer();
			AnswerMainPanel.add(answerField);

			sendBtn = new JButton("Senden");
			AnswerMainPanel.add(sendBtn);
		}
		
		if(ticket.getAntworten().getAntwort().size() == 0 && !ticket.getInfos().isInBearbeitung()
				&& !ticket.getInfos().getUser().getId().equals(user.getUser().get(0).getId())) {
			AnswerMainPanel.setVisible(false);
		} else {
			AnswerMainPanel.setVisible(true);
		}
		
		AnswerMainPanel.repaint();
		
		if(sendBtn != null) {
			sendBtn.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent arg0) {
					CtAntwort antwort = new CtAntwort();
					Supporter supporter = new Supporter();
					supporter.setId(user.getUser().get(0).getId());
					supporter.setValue(user.getUser().get(0).getVorname()+" "
							+user.getUser().get(0).getNachname());
					antwort.setSupporter(supporter);
					antwort.setDatum(getDate());
					antwort.setAntwort(answerTArea.getText());
					safeAnswer(antwort);
				}
			});
		}
		
	}


	private String gregorianToDate(XMLGregorianCalendar datum) {
		DateFormat formatter = new SimpleDateFormat("dd.MM.yy HH:mm");
		String formatData = formatter.format(datum.toGregorianCalendar().getTime());
		return formatData;
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
