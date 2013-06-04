package de.steinleostolski.xmpp;

import java.awt.Container;
import java.awt.EventQueue;
import java.util.EventObject;

import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;

import org.jivesoftware.smack.XMPPException;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Color;

public class ConnectUser extends JabberSmackAPI  {

	private JFrame frame;
	private JTextField txtUser;
	private JTextField txtPassword;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ConnectUser window = new ConnectUser();
					window.frame.setVisible(true);
				
			} catch (Exception e) {
					//e.printStackTrace();
				
					}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public ConnectUser() {
		initialize();
	
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		
		frame = new JFrame();
		frame.getContentPane().setBackground(Color.GRAY);
		frame.setBounds(100, 100, 500, 299);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblWelcomeUser = new JLabel("Welcome User");
		lblWelcomeUser.setBounds(64, 50, 112, 16);
		frame.getContentPane().add(lblWelcomeUser);
		
		JLabel lblLogin = new JLabel("Login:");
		lblLogin.setBounds(83, 95, 43, 16);
		frame.getContentPane().add(lblLogin);
		
		txtUser = new JTextField();
		txtUser.setBounds(83, 134, 134, 28);
		frame.getContentPane().add(txtUser);
		txtUser.setColumns(10);
		
		txtPassword = new JTextField();
		txtPassword.setBounds(83, 174, 134, 28);
		frame.getContentPane().add(txtPassword);
		txtPassword.setColumns(10);
		
		JButton btnSend = new JButton("Send");
		
		btnSend.setBounds(243, 175, 117, 29);
		frame.getContentPane().add(btnSend);
		
		
		JLabel lblUser = new JLabel("User:");
		lblUser.setBounds(6, 140, 32, 16);
		frame.getContentPane().add(lblUser);
		
		JLabel lblPassword = new JLabel("Password:");
		lblPassword.setBounds(6, 180, 75, 16);
		frame.getContentPane().add(lblPassword);
		
		final JLabel lblLoginFailed = new JLabel("Login Failed");
		lblLoginFailed.setEnabled(false);
		lblLoginFailed.setForeground(Color.GRAY);
		lblLoginFailed.setBounds(93, 214, 134, 16);
		frame.getContentPane().add(lblLoginFailed);
	
	
	btnSend.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent arg0) {
		try {
			login(txtUser.getText(),txtPassword.getText());
		} catch (XMPPException e) {
			//e.printStackTrace();
			lblLoginFailed.setForeground(Color.RED);
		}
		System.out.println(""+connection.isAuthenticated());
		if (connection.isAuthenticated()==true){
		MainMenu.main(null);
		frame.dispose();}
		}
	});
	
	}
	
}
