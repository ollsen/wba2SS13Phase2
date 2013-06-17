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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.table.DefaultTableModel;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.datatype.XMLGregorianCalendar;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;

import de.steinleostolski.client2.Application;
import de.steinleostolski.tickets.Ticketlist;
import de.steinleostolski.user.Userdb;
import de.steinleostolski.xmpp.PubsubClient;

public class ViewTicketlistPanel extends JPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6147709322502976652L;
	private Application app;
	private PubsubClient pubsub;
	private Userdb user;
	private Ticketlist ticketlist;
	private DefaultTableModel tableModel ;
	private Vector<String> columnNames;
	private JComboBox filterComboBox;
	private Vector<Vector<String>> data;
	
	private BigInteger ticketId;
	

	public enum UserLevel {
		USER, SUPPORTER, ADMIN
	}

	public ViewTicketlistPanel(Application app, PubsubClient pubsub, Userdb user) {
		this.app = app;
		this.pubsub = pubsub;
		this.user = user;
		initUI();
	}

	private void initUI() {
		JPanel panel = new JPanel();
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.anchor = GridBagConstraints.NORTHWEST;
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
		
		filterComboBox = new JComboBox();
		
		columnNames = new Vector<String>();
		columnNames.addElement("ID");
		columnNames.addElement("Datum");
		columnNames.addElement("Betreff");
		columnNames.addElement("Zustand");
		
		data = new Vector<Vector<String>>();
		
		switch (UserLevel.valueOf(user.getUser().get(0).getStatus().toUpperCase())) {
		case SUPPORTER:
			filterComboBox.addItem("Offene Tickets");
			filterComboBox.addItem("Erstellte Tickets");
			filterComboBox.addItem("Tickets in Bearbeitung");
			filterComboBox.addItem("Geschlossene Tickets");
			
			data = filterTicketList(filterComboBox.getSelectedItem().toString());
			break;
		case ADMIN:
			filterComboBox.addItem("Offene Tickets");
			filterComboBox.addItem("Erstellte Tickets");
			filterComboBox.addItem("Tickets in Bearbeitung");
			filterComboBox.addItem("Geschlossene Tickets");
			filterComboBox.addItem("Alle Tickets");
			filterComboBox.addItem("Alle Geschlossene Tickets");
			
			data = filterTicketList(filterComboBox.getSelectedItem().toString());
			break;
		default:
			filterComboBox.addItem("Erstellte Tickets");
			filterComboBox.addItem("Geschlossene Tickets");
			data = filterTicketList(filterComboBox.getSelectedItem().toString());
			break;
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
		
		final JButton viewBtn = new JButton("Ticket anzeigen");
		final JButton removeBtn = new JButton("Ticket entfernen");
		viewBtn.setEnabled(false);
		removeBtn.setEnabled(false);
		
		gbc.insets = new Insets(2, 2, 2, 2);
		gbc.anchor = GridBagConstraints.WEST;
		gbc.gridx = 0; gbc.gridy = 0;
		gbc.gridwidth = 2;
		panel.add(filterComboBox, gbc);
		gbc.gridy++;
		gbc.gridwidth = 3;
		panel.add(tableScrollPane, gbc);
		gbc.gridy++;
		gbc.gridwidth = 1;
		panel.add(viewBtn, gbc);
		gbc.gridx++;
		panel.add(removeBtn, gbc);
		
		filterComboBox.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String selectedItem = filterComboBox.getSelectedItem().toString();
				data = filterTicketList(selectedItem);
				tableModel.setDataVector(data, columnNames);
			}
		});
		
		userTable.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				viewBtn.setEnabled(true);
				removeBtn.setEnabled(true);
				int row = userTable.rowAtPoint(e.getPoint());
				int id = Integer.valueOf(userTable.getValueAt(row, 0).toString());
				ticketId = BigInteger.valueOf(id);
				
			}
		});
		
		viewBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				app.changePanel(3, 7);
			}
		});
		
		removeBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				deleteTicket();
			}
		});
		
		return panel;
	}
	
	protected void deleteTicket() {
		try {
			Client client = Client.create();
			WebResource webResource = client
					   .resource("http://localhost:4434/ticket/"+ticketId+"/delete");
			
			ClientResponse response = webResource.accept("MediaType.APPLICATION_XML")
					.delete(ClientResponse.class);
			
			if (response.getStatus() != 200) {
				throw new RuntimeException("Failed : HTTP error code : "
				     + response.getStatus());
			}
			data = filterTicketList(filterComboBox.getSelectedItem().toString());
			tableModel.setDataVector(data, columnNames);
		} catch (UniformInterfaceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

	private Vector<Vector<String>> filterTicketList(String filter) {
		Vector<Vector<String>> filterData = new Vector<Vector<String>>();
		try {
			loadTicketlist();
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(filter == "Offene Tickets") {
			for(int i = 0; i < ticketlist.getTeintrag().size(); i++) {
				if(!ticketlist.getTeintrag().get(i).isBearbeitungszustand() &&
						!ticketlist.getTeintrag().get(i).getZustand().equals("geschlossen")) {
					Vector<String> vec = new Vector<String>();
					vec.addElement(ticketlist.getTeintrag().get(i).getTicketId().toString());
					vec.addElement(gregorianToDate(ticketlist.getTeintrag().get(i).getDatum()));
					vec.addElement(ticketlist.getTeintrag().get(i).getBetreff());
					vec.addElement(ticketlist.getTeintrag().get(i).getZustand());
					filterData.addElement(vec);
				}
			}
			return filterData;
		} else if(filter == "Erstellte Tickets") {
			for(int i = 0; i < ticketlist.getTeintrag().size(); i++) {
				if(ticketlist.getTeintrag().get(i).getErstellerId()
						.equals(user.getUser().get(0).getId())) {
					Vector<String> vec = new Vector<String>();
					vec.addElement(ticketlist.getTeintrag().get(i).getTicketId().toString());
					vec.addElement(gregorianToDate(ticketlist.getTeintrag().get(i).getDatum()));
					vec.addElement(ticketlist.getTeintrag().get(i).getBetreff());
					vec.addElement(ticketlist.getTeintrag().get(i).getZustand());
					filterData.addElement(vec);
				}
			}
		} else if(filter == "Tickets in Bearbeitung") {
			for(int i = 0; i < ticketlist.getTeintrag().size(); i++) {
				if(ticketlist.getTeintrag().get(i).getErstellerId()
						.equals(user.getUser().get(0).getId()) &&
						ticketlist.getTeintrag().get(i).isBearbeitungszustand()) {
					Vector<String> vec = new Vector<String>();
					vec.addElement(ticketlist.getTeintrag().get(i).getTicketId().toString());
					vec.addElement(gregorianToDate(ticketlist.getTeintrag().get(i).getDatum()));
					vec.addElement(ticketlist.getTeintrag().get(i).getBetreff());
					vec.addElement(ticketlist.getTeintrag().get(i).getZustand());
					filterData.addElement(vec);
				}
			}
		} else if(filter == "Geschlossene Tickets") {
			for(int i = 0; i < ticketlist.getTeintrag().size(); i++) {
				if(ticketlist.getTeintrag().get(i).getErstellerId()
						.equals(user.getUser().get(0).getId()) &&
						ticketlist.getTeintrag().get(i).getZustand().equals("geschlossen")) {
					Vector<String> vec = new Vector<String>();
					vec.addElement(ticketlist.getTeintrag().get(i).getTicketId().toString());
					vec.addElement(gregorianToDate(ticketlist.getTeintrag().get(i).getDatum()));
					vec.addElement(ticketlist.getTeintrag().get(i).getBetreff());
					vec.addElement(ticketlist.getTeintrag().get(i).getZustand());
					filterData.addElement(vec);
				}
			}
		} else if(filter == "Alle Tickets") {
			for(int i = 0; i < ticketlist.getTeintrag().size(); i++) {
				Vector<String> vec = new Vector<String>();
				vec.addElement(ticketlist.getTeintrag().get(i).getTicketId().toString());
				vec.addElement(gregorianToDate(ticketlist.getTeintrag().get(i).getDatum()));
				vec.addElement(ticketlist.getTeintrag().get(i).getBetreff());
				vec.addElement(ticketlist.getTeintrag().get(i).getZustand());
				filterData.addElement(vec);
			}
		} else if(filter == "Alle Geschlossene Tickets") {
			for(int i = 0; i < ticketlist.getTeintrag().size(); i++) {
				if(ticketlist.getTeintrag().get(i).getZustand().equals("geschlossen")) {
					Vector<String> vec = new Vector<String>();
					vec.addElement(ticketlist.getTeintrag().get(i).getTicketId().toString());
					vec.addElement(gregorianToDate(ticketlist.getTeintrag().get(i).getDatum()));
					vec.addElement(ticketlist.getTeintrag().get(i).getBetreff());
					vec.addElement(ticketlist.getTeintrag().get(i).getZustand());
					filterData.addElement(vec);
				}
			}
		}
		
		return filterData;
	}

	private void loadTicketlist() throws JAXBException {
		Client client = Client.create();
		WebResource webResource = client
				   .resource("http://localhost:4434/ticket/");
	    // lets get the XML as a String
	    String text = webResource.accept("application/xml").get(String.class);
	    JAXBContext jc = JAXBContext.newInstance(Ticketlist.class);
		Unmarshaller unmarshaller = jc.createUnmarshaller();
		StringReader reader = new StringReader(text);
		ticketlist = (Ticketlist) unmarshaller.unmarshal(reader);
		sortTicketlist();
	}

	private String gregorianToDate(XMLGregorianCalendar datum) {
		DateFormat formatter = new SimpleDateFormat("dd.MM.yy hh:mm");
		String formatData = formatter.format(datum.toGregorianCalendar().getTime());
		return formatData;
	}
	
	public BigInteger getTicketId() {
		return ticketId;
	}
	
	public void sortTicketlist() {
		Ticketlist sortList = new Ticketlist();
		int listSize = ticketlist.getTeintrag().size();
		
		for(int i = 0; i < listSize; i++) {
			if(ticketlist.getTeintrag().get(i).getZustand().equals("kritisch")) {
				sortList.getTeintrag().add(ticketlist.getTeintrag().get(i));
			}
		}
			
		
		for(int i = 0; i < listSize; i++) {
			if(ticketlist.getTeintrag().get(i).getZustand().equals("hoch")) {
				sortList.getTeintrag().add(ticketlist.getTeintrag().get(i));
			}
		}
		
		for(int i = 0; i < listSize; i++) {
			if(ticketlist.getTeintrag().get(i).getZustand().equals("normal")) {
				sortList.getTeintrag().add(ticketlist.getTeintrag().get(i));
			}
		}
		
		for(int i = 0; i < listSize; i++) {
			if(ticketlist.getTeintrag().get(i).getZustand().equals("niedrig")) {
				sortList.getTeintrag().add(ticketlist.getTeintrag().get(i));
			}
		}
		
		ticketlist = sortList;
	}

}
