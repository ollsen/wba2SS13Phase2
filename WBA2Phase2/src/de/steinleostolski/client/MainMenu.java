package de.steinleostolski.client;

import java.awt.Dimension;
import java.awt.Insets;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class MainMenu extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MainMenu() {
		initUI();
	}

	public final void initUI() {
		JPanel basic = new JPanel();
        basic.setLayout(new BoxLayout(basic, BoxLayout.Y_AXIS));
        add(basic);

        basic.add(Box.createVerticalGlue());

        JPanel bottom = new JPanel();
        bottom.setAlignmentX(1f);
        bottom.setLayout(new BoxLayout(bottom, BoxLayout.X_AXIS));
        
        JPanel profilePanel = new JPanel();
        profilePanel.setAlignmentY(1f);
        profilePanel.setLayout(new BoxLayout(profilePanel, BoxLayout.Y_AXIS));

        JButton ok = new JButton("OK");
        JButton close = new JButton("Close");

        JLabel titel = new JLabel("Titel");
        
        bottom.add(ok);
        bottom.add(Box.createRigidArea(new Dimension(5, 0)));
        bottom.add(close);
        bottom.add(Box.createRigidArea(new Dimension(15, 0)));

        profilePanel.add(titel);
        profilePanel.add(Box.createRigidArea(new Dimension(5, 0)));
        basic.add(bottom);
        basic.add(Box.createRigidArea(new Dimension(0, 15)));
        basic.add(profilePanel);

        setTitle("Hauptmenü");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
	}
	
	
}
