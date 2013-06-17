package de.steinleostolski.client2.panels;


import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.StringReader;
import java.math.BigInteger;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.table.DefaultTableModel;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;

import de.steinleostolski.client2.Application;
import de.steinleostolski.user.Userdb;
import de.steinleostolski.xmpp.PubsubClient;

public class ViewUserPanel extends JPanel {
	
	private Application app; 
	private PubsubClient pubsub;
	private Userdb user;
	private Userdb userdb;
	
	private DefaultTableModel tableModel;
	private JLabel idLField;
	private JLabel nameLField;
	private JLabel jabberLField;
	private JLabel standortLField;
	private JLabel userLevelLField;
	private JPanel fachgebietPanel;
	private JButton removeBtn;
	private BigInteger userId;
	private Vector<Vector<String>> data;
	private Vector<String> columnNames;
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 489852620191460633L;

	public ViewUserPanel(Application app, PubsubClient pubsub, Userdb user) {
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
		summaryPanel.setPreferredSize(new Dimension(300, 420));
		
		JButton btnMainMenu = new JButton("Main Menu");
		removeBtn = new JButton("User entfernen");
		removeBtn.setEnabled(false);
		gbc.anchor= GridBagConstraints.NORTHWEST;
		gbc.insets = new Insets(4,4,4,4);
		gbc.weightx = 1; gbc.weighty = 1;
		gbc.gridx = 0; gbc.gridy = 0;
		gbc.gridwidth = 2;
		panel.add(listPanel, gbc);
		
		gbc.gridwidth = 1;
		gbc.gridx = 2;
		panel.add(summaryPanel, gbc);
		
		gbc.gridx = 0;
		gbc.gridy++;
		
		panel.add(removeBtn, gbc);
		
		gbc.gridx++;
		panel.add(btnMainMenu, gbc);
		
		btnMainMenu.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				app.changePanel(2, 0);
			}
		});
		
		removeBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				removeUser();
			}
		});
		
	}

	protected void removeUser() {
		try {
			Client client = Client.create();
			WebResource webResource = client
					   .resource("http://localhost:4434/user/"+userId+"/delete");
			
			ClientResponse response = webResource.accept("MediaType.APPLICATION_XML")
					.delete(ClientResponse.class);
			
			if (response.getStatus() != 200) {
				throw new RuntimeException("Failed : HTTP error code : "
				     + response.getStatus());
			}
			
			data = setUserData();
			tableModel.setDataVector(data, columnNames);
		} catch (UniformInterfaceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

	private JPanel createSummaryPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		
		JLabel idLabel = new JLabel("ID:");
		JLabel nameLabel = new JLabel("Name:");
		JLabel jabberLabel = new JLabel("Jabbername:");
		JLabel standortLabel = new JLabel("Standort:");
		JLabel userLevelLabel = new JLabel("User Level:");
		JLabel fachgebietLabel = new JLabel("Fachgebiete:");
		
		idLField = new JLabel("");
		nameLField = new JLabel("");
		jabberLField = new JLabel("");
		standortLField = new JLabel("");
		userLevelLField = new JLabel("");
		fachgebietPanel = new JPanel();
		fachgebietPanel.setLayout(new BoxLayout(fachgebietPanel, BoxLayout.Y_AXIS));
		fachgebietPanel.setPreferredSize(new Dimension(175, 200));
		
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 0; gbc.gridy = 0;
		gbc.weighty = 1;
		gbc.insets = new Insets(4, 4, 4, 4);
		panel.add(idLabel, gbc);
		gbc.gridx++;
		panel.add(idLField, gbc);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx--; gbc.gridy++;
		panel.add(nameLabel, gbc);
		gbc.gridx++;
		panel.add(nameLField, gbc);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx--; gbc.gridy++;
		panel.add(jabberLabel, gbc);
		//gbc.fill = GridBagConstraints.NONE;
		gbc.gridx++;
		panel.add(jabberLField, gbc);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx--; gbc.gridy++;
		panel.add(standortLabel, gbc);
		gbc.gridx++;
		panel.add(standortLField, gbc);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx--; gbc.gridy++;
		panel.add(userLevelLabel, gbc);
		gbc.gridx++;
		panel.add(userLevelLField, gbc);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx--; gbc.gridy++;
		panel.add(fachgebietLabel, gbc);
		gbc.gridx++;
		panel.add(fachgebietPanel, gbc);
		
		
		return panel;
	}

	private JPanel createListPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		
		columnNames = new Vector<String>();
		columnNames.addElement("ID");
		columnNames.addElement("Vorname");
		columnNames.addElement("Nachname");
		columnNames.addElement("Standort");
		
		data = new Vector<Vector<String>>();
		try {
			data = setUserData();
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		tableModel = new DefaultTableModel() {

		    /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
		    public boolean isCellEditable(int row, int column) {
		       //all cells false
		       return false;
		    }
		};
		
		tableModel.setDataVector(data, columnNames);
		
		GridBagConstraints gbc = new GridBagConstraints();
		final JTable userTable = new JTable(tableModel);
		JScrollPane tableScrollPane = new JScrollPane(userTable, 
				ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, 
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		
		panel.add(tableScrollPane);
		
		userTable.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				int row = userTable.rowAtPoint(e.getPoint());
				int id = Integer.valueOf(userTable.getValueAt(row, 0).toString());
				userId = BigInteger.valueOf(id);
				loadUser(userId);
				removeBtn.setEnabled(true);
			}
		});
		
		return panel;
	}

	protected void loadUser(BigInteger userId) {
		for(int i = 0; i < userdb.getUser().size(); i++) {
			if(userdb.getUser().get(i).getId().equals(userId)) {
				idLField.setText(userdb.getUser().get(i).getId().toString());
				nameLField.setText(userdb.getUser().get(i).getVorname()+" "
						+userdb.getUser().get(i).getNachname());
				jabberLField.setText(userdb.getUser().get(i).getJabber());
				standortLField.setText(userdb.getUser().get(i).getVorname());
				userLevelLField.setText(userdb.getUser().get(i).getStatus());
				

				fachgebietPanel.removeAll();
				fachgebietPanel.validate();
				fachgebietPanel.repaint();
				if(userdb.getUser().get(i).getKnowHows().getKnowHow().size() != 0) {
			        for(int j = 0; j < userdb.getUser().get(i).getKnowHows().getKnowHow().size(); j++) {
			        	fachgebietPanel.add(new JLabel(userdb.getUser().get(i).getKnowHows().getKnowHow().get(j)));
			        }
				}
				
			}
		}
	}

	private Vector<Vector<String>> setUserData() throws JAXBException {
		Vector<Vector<String>> data = new Vector<Vector<String>>();
		
		Client client = Client.create();
		WebResource webResource = client
				   .resource("http://localhost:4434/user/");
	    // lets get the XML as a String
	    String text = webResource.accept("application/xml").get(String.class);
	    JAXBContext jc = JAXBContext.newInstance(Userdb.class);
		Unmarshaller unmarshaller = jc.createUnmarshaller();
		StringReader reader = new StringReader(text);
		userdb = (Userdb) unmarshaller.unmarshal(reader);
		
		for(int i = 0; i < userdb.getUser().size(); i++) {
			Vector<String> vec = new Vector<String>();
			vec.addElement(userdb.getUser().get(i).getId().toString());
			vec.addElement(userdb.getUser().get(i).getVorname());
			vec.addElement(userdb.getUser().get(i).getNachname());
			vec.addElement(userdb.getUser().get(i).getStandort());
			data.addElement(vec);
		}
		
		return data;
	}
	
	
}
