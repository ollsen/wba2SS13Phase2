package de.steinleostolski.swing;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JButton;

public class Login {

	private JFrame frame;
	private JTextField textField;
	private JPasswordField passwordField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Login window = new Login();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Login() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panel = new JPanel();
		frame.getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(null);
		
		JLabel lblLogin = new JLabel("Login");
		lblLogin.setBounds(182, 54, 61, 16);
		panel.add(lblLogin);
		
		JLabel lblUsername = new JLabel("Username");
		lblUsername.setBounds(101, 104, 69, 16);
		panel.add(lblUsername);
		
		textField = new JTextField();
		textField.setBounds(182, 98, 134, 28);
		panel.add(textField);
		textField.setColumns(10);
		
		JLabel lblPassword = new JLabel("Password");
		lblPassword.setBounds(101, 146, 69, 16);
		panel.add(lblPassword);
		
		passwordField = new JPasswordField();
		passwordField.setBounds(182, 140, 134, 28);
		panel.add(passwordField);
		
		JButton btnLogin = new JButton("Login");
		btnLogin.setBounds(154, 197, 117, 29);
		panel.add(btnLogin);
	}
}
