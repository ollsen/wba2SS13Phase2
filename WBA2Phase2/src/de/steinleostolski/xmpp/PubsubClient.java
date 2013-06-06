package de.steinleostolski.xmpp;

import java.util.Map;
import java.util.TreeMap;

import javax.swing.DefaultListModel;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.pubsub.PubSubManager;

public class PubsubClient {
	
	private XMPPConnection connection;
	 
    private PubSubManager pubsubMgr;
 
    private String jid;
    
    private String username;
 
    private String selectedNode;
    
    private Map<String, DefaultListModel> nodeMessages = new TreeMap<String, DefaultListModel>();

    public void login(String userName, String password) throws XMPPException
    {
    ConnectionConfiguration config = new ConnectionConfiguration("localhost",5222, "Work");
    connection = new XMPPConnection(config);
 
    connection.connect();
    connection.login(userName, password);
    }
    
    public void setUsername(String username) {
    	this.username = username;
    }
    
    public String getUsername() {
    	return username;
    }
    public XMPPConnection getConnection() {
    	return connection;
    }
}