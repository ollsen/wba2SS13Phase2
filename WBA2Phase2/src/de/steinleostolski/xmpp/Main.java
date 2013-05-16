package de.steinleostolski.xmpp;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.pubsub.AccessModel;
import org.jivesoftware.smackx.pubsub.ConfigureForm;
import org.jivesoftware.smackx.pubsub.FormType;
import org.jivesoftware.smackx.pubsub.Item;
import org.jivesoftware.smackx.pubsub.LeafNode;
import org.jivesoftware.smackx.pubsub.PubSubManager;
import org.jivesoftware.smackx.pubsub.PublishModel;

public class Main {
	
	private static XMPPConnection connection;

	/**
	 * @param args
	 * @throws XMPPException 
	 */
	public static void main(String[] args) throws XMPPException {


		ConnectionConfiguration config = new ConnectionConfiguration("localhost",5222, "Work");
	    connection = new XMPPConnection(config);
	    
	    connection.connect();
	    connection.login("admin", "password");
	    
		PubSubManager mgr = new PubSubManager(connection);

	    // Create the node
		LeafNode leaf = mgr.createNode("testNode");
		ConfigureForm form = new ConfigureForm(FormType.submit);
		form.setAccessModel(AccessModel.open);
		form.setDeliverPayloads(false);
		form.setNotifyRetract(true);
		form.setPersistentItems(true);
		form.setPublishModel(PublishModel.open);
	      
		leaf.sendConfigurationForm(form);
		
		send();
	}
	
	public static void send() throws XMPPException {
		// Create a pubsub manager using an existing Connection
		PubSubManager mgr = new PubSubManager(connection);

		// Get the node
		LeafNode node = mgr.getNode("testNode");

		// Publish an Item, let service set the id
		node.send(new Item());

		// Publish an Item with the specified id
		node.send(new Item("123abc"));
	}

}
