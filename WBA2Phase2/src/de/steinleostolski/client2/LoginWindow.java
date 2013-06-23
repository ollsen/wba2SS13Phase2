package de.steinleostolski.client2;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.xml.bind.JAXBException;

import org.jivesoftware.smack.XMPPException;

import de.steinleostolski.server.RestServerGUI;
import de.steinleostolski.xmpp.PubsubClient;

public class LoginWindow extends JFrame {
	
	private JTextField username;
	private JTextField adressField;
	private JPasswordField password;
	private JLabel status;
	private PubsubClient pubsub;
	
	public static String adress;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public LoginWindow() {

        initUI();
    }
	

	private final void initUI() {
		JPanel panel = new JPanel();
		panel.setBorder(BorderFactory.createEmptyBorder(30, 15, 30, 15));
		panel.setLayout(new GridLayout(4, 2, 10, 10));
		
		panel.add(new JLabel("Adresse"));
		adressField = new JTextField("localhost");
		panel.add(adressField);
		panel.add(new JLabel("Benutzername"));
		username = new JTextField();
		panel.add(username);
		panel.add(new JLabel("Kennwort"));
		password = new JPasswordField();
		panel.add(password);
		status = new JLabel();
		panel.add(status);
		
		JButton loginB = new JButton("login");
		panel.add(loginB);
		
		add(panel);
		
		setTitle("Login");
		setResizable(false);
        setSize(350, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        username.addKeyListener(new KeyAdapter() {
        	
        	public void keyReleased(KeyEvent e) {
        		if(e.getKeyCode() == KeyEvent.VK_ENTER)
        			login();
        	}
		});
        
        password.addKeyListener(new KeyAdapter() {
        	
        	public void keyReleased(KeyEvent e) {
        		if(e.getKeyCode() == KeyEvent.VK_ENTER)
        			login();
        	}
		});
        
        loginB.addKeyListener(new KeyAdapter() {
        	
        	public void keyReleased(KeyEvent e) {
        		if(e.getKeyCode() == KeyEvent.VK_ENTER)
        			login();
        	}
		});
        
        loginB.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				login();
			}
		});
        
        
	}
	public void login() {
		pubsub = new PubsubClient(adressField.getText());
		try {
			pubsub.login(username.getText(), password.getText());
		} catch (XMPPException e) {
			status.setForeground(Color.RED);
			status.setText("fehlgeschlagen");
		}
		
		if(pubsub.getConnection().isAuthenticated()) {
			adress = adressField.getText();
			pubsub.setJID(username.getText()+"@localhost");
			pubsub.setUsername(username.getText());
			status.setForeground(Color.GREEN);
			status.setText("erfolgreich");
			dispose();
			Application app = new Application(pubsub);
			app.setVisible(true);
		}
    }

}
