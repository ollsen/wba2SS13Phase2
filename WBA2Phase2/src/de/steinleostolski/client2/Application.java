package de.steinleostolski.client2;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.pubsub.Item;
import org.jivesoftware.smackx.pubsub.ItemPublishEvent;
import org.jivesoftware.smackx.pubsub.LeafNode;
import org.jivesoftware.smackx.pubsub.PayloadItem;
import org.jivesoftware.smackx.pubsub.listener.ItemEventListener;

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
import de.steinleostolski.payload.Notification;
import de.steinleostolski.user.Userdb;
import de.steinleostolski.xmpp.ItemEventCoordinator;
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
	private ViewUserPanel vuPanel;
	private NewUserPanel nuPanel;
	private SettingsPanel sPanel;
	private int currentIndex;
	
	private JButton notificationBtn;
	private Thread thread;
	private JFrame notifyFrame;
	
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
		getNodes();
	}


	private void initUI() {
		panelList = new ArrayList<JPanel>();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(800, 600);
		setTitle("Ticket System");
		
		mmPanel = new MainMenuPanel(this, pubsub, user);
		euPanel = new EditUserPanel(this, pubsub, user);
		vuPanel = new ViewUserPanel(this, pubsub, user);
		vtlPanel = new ViewTicketlistPanel(this, pubsub, user);
		nuPanel = new NewUserPanel(this, pubsub, user);
		ntPanel = new NewTicketPanel(this, pubsub, user);
		sPanel = new SettingsPanel(this, pubsub, user);
		vtPanel = new ViewTicketPanel(this, pubsub, user);
		panelList.add(0, mmPanel); panelList.add(1, euPanel);
		panelList.add(2, vuPanel); panelList.add(3, vtlPanel);
		panelList.add(4, nuPanel); panelList.add(5, ntPanel);
		panelList.add(6, sPanel); panelList.add(7, vtPanel);
		getContentPane().add(panelList.get(0), BorderLayout.CENTER);
		currentIndex = 0;
		
		// Benachrichtigungsbutton
		notificationBtn = new JButton("Benachrichtigung");
		getContentPane().add(notificationBtn, BorderLayout.SOUTH);
		
		notificationBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				openNotifyFrame();
			}
		});
	}
	
	@SuppressWarnings("deprecation")
	protected void openNotifyFrame() {
		if(thread.isAlive()) {
			thread.stop();
			notificationBtn.setForeground(Color.BLACK);
		}
			
	    notifyFrame.setSize(200, 600);
	    notifyFrame.show();
	}

	public void changePanel(int oldPanel, int newPanel) {
		panelList.get(oldPanel).setVisible(false);
		getContentPane().add(panelList.get(newPanel), BorderLayout.CENTER);
		panelList.get(newPanel).setVisible(true);
		currentIndex = newPanel;
		
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
		
		if(newPanel ==1 ) {
			euPanel.refresh();
		} else
		if (newPanel == 3) {
			vtlPanel.refresh();
		} else 
		if(newPanel == 5) {
			ntPanel.refreshItFields();
		} 
		
		if (newPanel == 7 && oldPanel != 0) {
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
	

	private void getNodes() {
		notifyFrame = new JFrame();
		notifyFrame.setLayout(new GridLayout(10, 1, 0, 4));
		
		if(user.getUser().get(0).getKnowHows().getKnowHow().size() != 0) {
			for(int i = 0; i < user.getUser().get(0).getKnowHows().getKnowHow().size(); i++) {
				try {
					LeafNode leaf = pubsub.getLeafNode(user.getUser().get(0).getKnowHows().getKnowHow()
							.get(i));
					
					leaf.addItemEventListener(new ItemEventListener() {

						@Override
						public void handlePublishedItems(
								ItemPublishEvent items) {
							
//							try {
//								Clip clip = AudioSystem.getClip();
//								AudioInputStream inputStream = AudioSystem.getAudioInputStream(new File(
//										"src/de/steinleostolski/client2/sound/alarma.wav"));
//								clip.open(inputStream);
//						        clip.start(); 
//							} catch (Exception e) {
//								// TODO Auto-generated catch block
//								e.printStackTrace();
//							} 
							Collection<? extends Item> itemss = items.getItems();
					        for (Item item : itemss) {
					                  PayloadItem pi = (PayloadItem) item;
					                  
					                  JAXBContext jc;
									try {
										blinkingButton();
										jc = JAXBContext.newInstance(Notification.class);
										Unmarshaller unmarshaller = jc.createUnmarshaller();

										StringReader reader = new StringReader(pi.getPayload().toXML());
										Notification notify = (Notification) unmarshaller.unmarshal(reader);
										createNotifyPanel(notify);
										
									} catch (JAXBException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
					          		  
									
					        }
						}
					});
				} catch (XMPPException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
	}
	
	protected void createNotifyPanel(final Notification notify) {
		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(200, 150));
		panel.setLayout(new GridLayout(4, 1));
		JLabel label = new JLabel("neues Ticket erstellt");
		JLabel label2 = new JLabel("ID: "+notify.getTicketId());
		JLabel label3 = new JLabel("Fachgebiet: "+notify.getTag());
		JLabel label4 = new JLabel("Datum: "+notify.getDate());
		panel.add(label); panel.add(label2); panel.add(label3);
		panel.add(label4);
		notifyFrame.getContentPane().add(panel);
		notifyFrame.validate();
		notifyFrame.repaint();
		
		panel.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				notifyFrame.dispose();
				try {
					vtPanel.loadTicket(notify.getTicketId());
				} catch (JAXBException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				changePanel(currentIndex, 7);
				
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseClicked(MouseEvent event) {
				try {
					vtPanel.loadTicket(notify.getTicketId());
				} catch (JAXBException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				changePanel(0, 7);
			}
		});
	}
	
	private void blinkingButton() {
		thread = new Thread() {

			@Override
			public void run() {
				while(true) {
					SwingUtilities.invokeLater(new Runnable() {
						
						@Override
						public void run() {
							if(notificationBtn.getForeground() == Color.BLACK)
								notificationBtn.setForeground(Color.RED);
							else
								notificationBtn.setForeground(Color.BLACK);
						}
					});
					try {
						Thread.sleep(300);
					} catch (InterruptedException e) {
					}
					notificationBtn.repaint();
				}
			}
			
		};
		thread.setPriority(1);
		thread.start();
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
