package de.steinleostolski.xmpp;

import java.util.Map;
import java.util.TreeMap;

import javax.swing.DefaultListModel;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.pubsub.AccessModel;
import org.jivesoftware.smackx.pubsub.ConfigureForm;
import org.jivesoftware.smackx.pubsub.FormType;
import org.jivesoftware.smackx.pubsub.LeafNode;
import org.jivesoftware.smackx.pubsub.PubSubManager;
import org.jivesoftware.smackx.pubsub.PublishModel;

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
    
    public void createLeafNode(String nodeName) throws XMPPException {
    	PubSubManager mgr = new PubSubManager(connection);
    	ConfigureForm form = new ConfigureForm(FormType.submit);
	    form.setAccessModel(AccessModel.open);
	    form.setDeliverPayloads(true);
	    form.setNotifyRetract(true);
	    form.setPersistentItems(false);
	    form.setPublishModel(PublishModel.publishers);
	    
	    LeafNode leaf = mgr.createNode(nodeName);
	    leaf.sendConfigurationForm(form);
    }
    
    public void removeLeafNode(String nodeName) throws XMPPException {
    	PubSubManager mgr = new PubSubManager(connection);
    	mgr.deleteNode(nodeName);
    }
    
    public void getSubscriptionId(String nodeName) {
    	
    }
    
    public void setJID(String jid) {
    	this.jid = jid;
    }
    
    public String getJID() {
    	return jid;
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