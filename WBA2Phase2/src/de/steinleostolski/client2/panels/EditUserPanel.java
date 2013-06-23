package de.steinleostolski.client2.panels;


import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.Popup;
import javax.swing.PopupFactory;
import javax.swing.ScrollPaneConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.jivesoftware.smack.XMPPException;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import de.steinleostolski.client2.Application;
import de.steinleostolski.client2.LoginWindow;
import de.steinleostolski.settings.Settings;
import de.steinleostolski.user.Userdb;
import de.steinleostolski.xmpp.PubsubClient;

public class EditUserPanel extends JPanel {
	
	private Application app; 
	private PubsubClient pubsub;
	private Userdb user;
	
	private JTextField vornameField;
	private JTextField nachnameField;
	private JTextField standortField;
	private JList itFieldList;
	private JScrollPane itFieldScroll;
	private JList userItFieldList;
	private JScrollPane userItFieldScroll;
	private DefaultListModel userListData;
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 489852620191460633L;

	public EditUserPanel(Application app, PubsubClient pubsub, Userdb user) {
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
		
		JButton changeBtn = new JButton("Aktualisieren");
		JButton btnMainMenu = new JButton("Main Menu");
		JLabel vornameLabel = new JLabel("Vorname");
		JLabel nachnameLabel = new JLabel("Nachname");
		JLabel standortLabel = new JLabel("Standort");
		JLabel itFieldLabel = new JLabel("Fachgebiete");
		
		vornameField = new JTextField();
		nachnameField = new JTextField();
		standortField = new JTextField();
		
		userListData = new DefaultListModel();
		itFieldList = new JList();
		userItFieldList = new JList();
		
		
		
		itFieldList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		itFieldScroll = new JScrollPane(itFieldList,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		itFieldScroll.setPreferredSize(new Dimension(150, 100));
		
		userItFieldList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		userItFieldScroll = new JScrollPane(userItFieldList,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		userItFieldScroll.setPreferredSize(new Dimension(150, 100));
		

		try {
			loadProfile();
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		gbc.insets = new Insets(2, 2, 2, 2);
		gbc.anchor= GridBagConstraints.NORTHWEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 1; gbc.weighty = 1;
		gbc.gridx = 0; gbc.gridy = 0;
		
		panel.add(vornameLabel, gbc);
		gbc.gridx++;
		panel.add(vornameField, gbc);
		gbc.gridx--;
		gbc.gridy++;
		panel.add(nachnameLabel, gbc);
		gbc.gridx++;
		panel.add(nachnameField, gbc);
		gbc.gridx--;
		gbc.gridy++;
		panel.add(standortLabel, gbc);
		gbc.gridx++;
		panel.add(standortField, gbc);
		gbc.gridx--;
		gbc.gridy++;
		panel.add(itFieldLabel, gbc);
		gbc.gridy++;
		panel.add(userItFieldScroll, gbc);
		gbc.gridx++;
		panel.add(itFieldScroll, gbc);
		
		gbc.gridy++;
		gbc.gridx--;
		
		panel.add(changeBtn, gbc);
		gbc.gridx++;
		panel.add(btnMainMenu, gbc);
		
		try {
			itFieldList.setModel(loadItFields());
		} catch (JAXBException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		itFieldList.addMouseListener(new MouseAdapter() {
			  public void mouseClicked(MouseEvent e ) {
				    if ( e.getClickCount() == 2 ) {
				      addListItem(itFieldList.getModel().getElementAt(itFieldList.getSelectedIndex()).toString());
				    }
				  }
				});
		
		userItFieldList.addMouseListener(new MouseAdapter() {
			  public void mouseClicked(MouseEvent e ) {
				    if ( e.getClickCount() == 2 ) {
				      removeListItem(userItFieldList.getModel().getElementAt(userItFieldList.getSelectedIndex()).toString());
				    }
				  }
				});
		
		changeBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				safeProfile();
			}
		});
		
		btnMainMenu.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				app.changePanel(1, 0);
			}
		});
		
	}

	protected void safeProfile() {
		user.getUser().get(0).setVorname(vornameField.getText());
		user.getUser().get(0).setNachname(nachnameField.getText());
		user.getUser().get(0).setStandort(vornameField.getText());
		
		List<String> utfl = new ArrayList<String>();
		for(int i = 0; i < userItFieldList.getModel().getSize(); i++) {
			utfl.add(userItFieldList.getModel().getElementAt(i).toString());
		}
		
		for(String list : utfl) {
			if(!user.getUser().get(0).getKnowHows().getKnowHow().contains(list)) {
				user.getUser().get(0).getKnowHows().getKnowHow()
				.add(list);
				try {
					pubsub.subscribeLeafNode(list);
				} catch (XMPPException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			}
		}
		
		for(String knowhow : user.getUser().get(0).getKnowHows().getKnowHow()) {
			if(!utfl.contains(knowhow)) {
				user.getUser().get(0).getKnowHows().getKnowHow().remove(knowhow);
				try {
					pubsub.unsubscribeLeafNode(knowhow);
				} catch (XMPPException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			}
		}
		
//		for(int i = 0; i < userItFieldList.getModel().getSize(); i++) {
//			if(!user.getUser().get(0).getKnowHows().getKnowHow().contains(userItFieldList.getModel().getElementAt(i).toString())) {
//				user.getUser().get(0).getKnowHows().getKnowHow()
//				.add(userItFieldList.getModel().getElementAt(i).toString());
//				
//				try {
//					pubsub.subscribeLeafNode(userItFieldList.getModel().getElementAt(i).toString());
//				} catch (XMPPException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//		}
		
		Client client = Client.create();
		
		try { 
			WebResource webResource = client
			   .resource("http://"+LoginWindow.adress+":4434/user/"+user.getUser().get(0).getId().toString()+"/edit/");
			
			ClientResponse response = webResource.accept("application/xml")
					.put(ClientResponse.class, user);
	 
			if (response.getStatus() != 200) {
				throw new RuntimeException("Failed : HTTP error code : "
				     + response.getStatus());
			}
			showPopup(response.getEntity(String.class));
		} catch (Exception e) {
				 
			e.printStackTrace();
			 
		} finally {
			
		}
		
		
	}

	protected void addListItem(String string) {
		DefaultListModel model = (DefaultListModel) userItFieldList.getModel();
		model.addElement(string);
		model = (DefaultListModel) itFieldList.getModel();
		model.removeElement(string);
		
	}

	protected void removeListItem(String string) {
		DefaultListModel model = (DefaultListModel) itFieldList.getModel();
		model.addElement(string);
		model = (DefaultListModel) userItFieldList.getModel();
		model.removeElement(string);
	}
	private void loadProfile() throws JAXBException {
		userListData = new DefaultListModel();
		
		int listSize = user.getUser().get(0).getKnowHows().getKnowHow().size();
		
		for(int i = 0; i < listSize; i++) {
			userListData.addElement(user.getUser().get(0).getKnowHows().getKnowHow().get(i));
		}
		userItFieldList.setModel(userListData);
		vornameField.setText(user.getUser().get(0).getVorname());
		nachnameField.setText(user.getUser().get(0).getNachname());
		standortField.setText(user.getUser().get(0).getStandort());
		
	}

	private DefaultListModel loadItFields() throws JAXBException {
		DefaultListModel itFieldData = new DefaultListModel();
		Client client = Client.create();
		WebResource webResource = client
				   .resource("http://"+LoginWindow.adress+":4434/settings/");
	    // lets get the XML as a String
	    String text = webResource.accept("application/xml").get(String.class);
	    JAXBContext jc = JAXBContext.newInstance(Settings.class);
		Unmarshaller unmarshaller = jc.createUnmarshaller();
		StringReader reader = new StringReader(text);
		Settings itFields = (Settings) unmarshaller.unmarshal(reader);
		
		for(int i = 0; i < itFields.getITBereich().getFachbereich().size(); i++) {
			int treffer = 0;
			if(userListData.size() == 0) {
				itFieldData.addElement(itFields.getITBereich().getFachbereich().get(i));
			} else {
				for(int j = 0; j < userListData.size(); j++) {
					if(itFields.getITBereich().getFachbereich().get(i).equals(userListData.get(j))) {
						treffer++;
						break;
					}
				}
				if(treffer == 0)
					itFieldData.addElement(itFields.getITBereich().getFachbereich().get(i));
			}
		}
		
		
		return itFieldData;
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
				app.changePanel(1, 0);
			}
		});
	}
	
	public void refresh() {
		try {
			loadProfile();
			itFieldList.setModel(loadItFields());
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
