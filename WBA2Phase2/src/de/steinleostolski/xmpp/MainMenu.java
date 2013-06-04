package de.steinleostolski.xmpp;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.BorderLayout;
import javax.swing.JSpinner;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.Font;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.ImageIcon;

public class MainMenu extends JabberSmackAPI {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainMenu window = new MainMenu();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}System.out.println(""+connection.isAuthenticated());
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainMenu() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 600, 400);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblOffeneTickets = new JLabel("Offene Tickets:");
		lblOffeneTickets.setBounds(37, 68, 97, 25);
		frame.getContentPane().add(lblOffeneTickets);
		
		JLabel label = new JLabel("23");
		label.setFont(new Font("Lucida Grande", Font.BOLD, 24));
		label.setForeground(Color.ORANGE);
		label.setBounds(144, 64, 40, 25);
		frame.getContentPane().add(label);
		
		JButton btnNewButton = new JButton("Anschauen");
		btnNewButton.setBounds(29, 105, 117, 29);
		frame.getContentPane().add(btnNewButton);
		
		JLabel lblMeineTickets = new JLabel("Meine Tickets:");
		lblMeineTickets.setBounds(37, 206, 95, 16);
		frame.getContentPane().add(lblMeineTickets);
		
		JLabel label_1 = new JLabel("7");
		label_1.setFont(new Font("Lucida Grande", Font.BOLD, 22));
		label_1.setForeground(Color.GREEN);
		label_1.setBounds(153, 202, 61, 16);
		frame.getContentPane().add(label_1);
		
		JButton btnAnschauen = new JButton("Anschauen");
		btnAnschauen.setBounds(29, 245, 117, 29);
		frame.getContentPane().add(btnAnschauen);
		
		JProgressBar progressBar = new JProgressBar();
		progressBar.setValue(56);
		progressBar.setBounds(37, 336, 291, 20);
		frame.getContentPane().add(progressBar);
		
		JLabel label_2 = new JLabel("23 / 100");
		label_2.setBounds(340, 336, 61, 16);
		frame.getContentPane().add(label_2);
		
		JLabel lblTagesziel = new JLabel("Tagesziel");
		lblTagesziel.setBounds(37, 319, 61, 16);
		frame.getContentPane().add(lblTagesziel);
		
		JLabel lblBenutzer = new JLabel("Benutzer:");
		lblBenutzer.setBounds(340, 186, 61, 16);
		frame.getContentPane().add(lblBenutzer);
		
		JLabel lblDavidOstolski = new JLabel("David Ostolski");
		lblDavidOstolski.setBounds(422, 186, 134, 16);
		frame.getContentPane().add(lblDavidOstolski);
		
		JLabel lblStatus = new JLabel("Status:");
		lblStatus.setBounds(340, 214, 61, 16);
		frame.getContentPane().add(lblStatus);
		
		JLabel lblSupporter = new JLabel("Supporter");
		lblSupporter.setBounds(422, 214, 61, 16);
		frame.getContentPane().add(lblSupporter);
		
		JLabel lblOnlineSeid = new JLabel("Online seit:");
		lblOnlineSeid.setBounds(340, 250, 75, 16);
		frame.getContentPane().add(lblOnlineSeid);
		
		JLabel lblh = new JLabel("2h");
		lblh.setBounds(422, 250, 61, 16);
		frame.getContentPane().add(lblh);
		
		JLabel lblNewLabel = new JLabel("New label");
		lblNewLabel.setIcon(new ImageIcon("/Users/derdavid/Desktop/Red_triangle_alert_icon.png"));
		lblNewLabel.setBounds(313, 24, 281, 150);
		frame.getContentPane().add(lblNewLabel);
	}
}
