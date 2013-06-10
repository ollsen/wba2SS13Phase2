package de.steinleostolski.client2.panels;


import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.StringReader;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import de.steinleostolski.client2.Application;
import de.steinleostolski.settings.Settings;
import de.steinleostolski.user.Userdb;
import de.steinleostolski.xmpp.PubsubClient;

public class SettingsPanel extends JPanel {
	
	private Application app; 
	private PubsubClient pubsub;
	private Userdb user;
	
	private JList itFieldList;
	private JScrollPane itFieldScroll;
	private JTextField addField;
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 489852620191460633L;

	public SettingsPanel(Application app, PubsubClient pubsub, Userdb user) {
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
		
		JButton btnMainMenu = new JButton("Main Menu");
		JButton addBtn = new JButton("Hinzufügen");
		JButton delBtn = new JButton("Entfernen");
		addField = new JTextField();
		
		
		try {
			Object[] listData = loadItFields();
			itFieldList = new JList(listData);
		} catch (JAXBException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		itFieldList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		itFieldScroll = new JScrollPane(itFieldList,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		itFieldScroll.setPreferredSize(new Dimension(150, 100));
		
		gbc.anchor= GridBagConstraints.SOUTHWEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 1; gbc.weighty = 1;
		gbc.gridx = 0; gbc.gridy = 0;
		gbc.gridwidth = 1;
		panel.add(itFieldScroll, gbc);
		
		gbc.gridx++;
		panel.add(delBtn, gbc);
		
		gbc.gridx--;
		gbc.gridy++;
		
		panel.add(addField, gbc);
		
		gbc.gridx++;
		panel.add(addBtn,gbc);
		
		gbc.gridx++;
		panel.add(btnMainMenu, gbc);
		
		btnMainMenu.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				app.changePanel(6, 0);
			}
		});
		
		addBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					saveItField();
				} catch (JAXBException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} finally {
					addField.setText("");
				}
			}
		});
		
		delBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					removeItField();
				} catch (JAXBException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		});
	}

	protected void removeItField() throws JAXBException {
		Client client = Client.create();
		String itField = itFieldList.getModel().getElementAt(itFieldList.getSelectedIndex()).toString();
		try { 
			WebResource webResource = client
			   .resource("http://localhost:4434/settings/remove?itfield="+itField);
			
			System.out.println("http://localhost:4434/settings/remove?itfield="+itField);
			ClientResponse response = webResource.accept(MediaType.APPLICATION_XML)
					.delete(ClientResponse.class);
	 
			if (response.getStatus() != 201) {
				throw new RuntimeException("Failed : HTTP error code : "
				     + response.getStatus());
			}
		} catch (Exception e) {
				 
			e.printStackTrace();
			 
		} finally {
			itFieldList.setListData(loadItFields());
		}
	}

	protected void saveItField() throws JAXBException {
		Client client = Client.create();
		try { 
			WebResource webResource = client
			   .resource("http://localhost:4434/settings/addfield/");
			
			ClientResponse response = webResource.accept("text/plain")
					.post(ClientResponse.class, addField.getText());
	 
			if (response.getStatus() != 201) {
				throw new RuntimeException("Failed : HTTP error code : "
				     + response.getStatus());
			}
		} catch (Exception e) {
				 
			e.printStackTrace();
			 
		} finally {
			itFieldList.setListData(loadItFields());
		}
	}

	private Object[] loadItFields() throws JAXBException {
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

}
