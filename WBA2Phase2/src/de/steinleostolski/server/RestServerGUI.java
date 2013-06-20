package de.steinleostolski.server;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URI;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.jivesoftware.smack.XMPPException;

import com.sun.grizzly.http.SelectorThread;
import com.sun.jersey.api.container.grizzly.GrizzlyServerFactory;

import de.steinleostolski.xmpp.PubsubClient;

public class RestServerGUI extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JButton startButton;
	private JButton stopButton;
	private JLabel statusLabel;
	private SelectorThread srv;
	private InetAddress ip;
	
	public static PubsubClient pubsub;
	
	private final String username = "restfull";
	private final String password = "restful";
	private final String jid = username+"@localhost";

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {

			@Override
			public void run() {
				RestServerGUI rsg = new RestServerGUI();
				rsg.show();
			}
			
		});
	}
	
	public RestServerGUI() {
		initialize();
		login();
	}

	private final void initialize() {
		JPanel panel = new JPanel(new GridBagLayout());
		panel.setVisible(true);
		
		final JLabel restfulLabel = new JLabel("");
		startButton = new JButton("Start Server");
		stopButton = new JButton("Stop Server");
		statusLabel = new JLabel("");
		GridBagConstraints gbc = new GridBagConstraints();
		
		//gbc.anchor = GridBagConstraints.WEST;
		gbc.insets = new Insets(2,2,2,2);
		gbc.gridx = 0;
		gbc.gridy = 0;
		//gbc.fill = GridBagConstraints.BOTH;
		gbc.gridwidth = 2;
		gbc.weighty = 1.0;
		//gbc.fill = GridBagConstraints.BOTH;
		//gbc.anchor = GridBagConstraints.EAST;
		
		panel.add(restfulLabel, gbc);
		
		//gbc.anchor = GridBagConstraints.CENTER;
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 1;
		panel.add(startButton, gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 1;
		
		panel.add(stopButton, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.gridwidth = 2;
		panel.add(statusLabel, gbc);
		
		stopButton.setEnabled(false);
		
		startButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				int port = 4434;
				
				try {
					ip = InetAddress.getLocalHost();
					String url = "http://"+ip.getHostAddress()+":"+String.valueOf(port);
					srv = GrizzlyServerFactory.create( url );
					if(srv.isRunning()) {
						startButton.setEnabled(false);
						stopButton.setEnabled(true);
						statusLabel.setText("online");
						statusLabel.setForeground(Color.green);
						restfulLabel.setText(url);
						
					}
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
			}
			
		});
		
		stopButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				if (srv.isRunning()) {
					srv.stopEndpoint();
					startButton.setEnabled(true);
					stopButton.setEnabled(false);
					statusLabel.setText("offline");
					statusLabel.setForeground(Color.black);
				}
			}
			
		});
		
		
		
		
		add(panel);
		setTitle("RESTFul");
		setResizable(false);
        setSize(350, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
	}
	private void login() {
		pubsub = new PubsubClient();
		try {
			pubsub.login(username, password);
		} catch (XMPPException e) {
			System.out.println("Fehlgeschlagen");
		}
		
		pubsub.setJID(jid);
	}
}
