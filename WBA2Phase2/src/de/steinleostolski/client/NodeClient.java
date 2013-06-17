package de.steinleostolski.client;

import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Scanner;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.ServiceDiscoveryManager;
import org.jivesoftware.smackx.packet.DiscoverInfo;
import org.jivesoftware.smackx.packet.DiscoverItems;
import org.jivesoftware.smackx.pubsub.AccessModel;
import org.jivesoftware.smackx.pubsub.Affiliation;
import org.jivesoftware.smackx.pubsub.ConfigureForm;
import org.jivesoftware.smackx.pubsub.FormType;
import org.jivesoftware.smackx.pubsub.Item;
import org.jivesoftware.smackx.pubsub.ItemPublishEvent;
import org.jivesoftware.smackx.pubsub.LeafNode;
import org.jivesoftware.smackx.pubsub.PayloadItem;
import org.jivesoftware.smackx.pubsub.PubSubManager;
import org.jivesoftware.smackx.pubsub.PublishModel;
import org.jivesoftware.smackx.pubsub.SimplePayload;
import org.jivesoftware.smackx.pubsub.Subscription;
import org.jivesoftware.smackx.pubsub.listener.ItemEventListener;

public class NodeClient {

	/**
	 * @param args
	 * @throws XMPPException 
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void main(String[] args) throws XMPPException {
		ConnectionConfiguration config = new ConnectionConfiguration("localhost",5222, "Work");
		XMPPConnection con = new XMPPConnection(config);
		Scanner in = new Scanner(System.in);
		
		System.out.println("Benutzername: ");
		String username = in.nextLine();
		System.out.println("Passwort: ");
		String password = in.nextLine();
		if(username != null || password != null) {
			con.connect();
			con.login(username, password);
		}
		else
			System.out.println("Fehler!");
		String jid = username+"@localhost";
		PubSubManager mgr = new PubSubManager(con);
		LeafNode leaf;
		List<Affiliation> affiliations;
		int auswahl = 0;
		
		System.out.println("1: Node erstellen:");
		System.out.println("2: Nachrichten empfangen");
		System.out.println("3: Abonnierte Leafs anzeigen");
		System.out.println("4: unsubscribe Leafs");
		System.out.println("5: subscribe Leaf");
		System.out.println("6: nachricht senden");
		System.out.println("7: Items entfernen");

		auswahl = in.nextInt();
		in.nextLine();
		
		String nodeName;
		
		switch(auswahl) {
		case 1:
			System.out.println("name: ");
			nodeName = in.nextLine();
			
			ConfigureForm form = new ConfigureForm(FormType.submit);
		    form.setAccessModel(AccessModel.open);
		    form.setDeliverPayloads(true);
		    form.setNotifyRetract(true);
		    form.setPersistentItems(false);
		    form.setPublishModel(PublishModel.open);
		    leaf = mgr.createNode(nodeName);
		    leaf.sendConfigurationForm(form);
		    
		    System.out.println("Node nodeName erstellt");
		    
		    break;
		case 2:
			affiliations = mgr.getAffiliations();
			for(int i = 0; i < affiliations.size(); i++) {
				System.out.println(affiliations.get(i).getNodeId());
			}
			System.out.println("wählen Sie aus:");
			nodeName = in.nextLine();
			leaf = mgr.getNode(nodeName);
			
			//List itemss = leaf.getItems();
			
			//System.out.println(itemss.size());
			
			//System.out.println(leaf.discoverItems().toXML());
			/*Collection<? extends Item> items = leaf.getItems("SUbCIDzip0xEnf9Q1a3Ei4BVk3ruGkgrk94K3MMn");
			
			for (Item item : items) {
                PayloadItem pi = (PayloadItem) item;


                System.out.println("item: " + pi.toXML());
			}*/
			
			leaf.addItemEventListener(new ItemEventCoordinator());
			leaf.subscribe(jid);
			
			try {
				Thread.sleep( 1000 * 300);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case 3:
			//Collection<? extends Item> items = node.getItems();
			//List<Subscription> subscriptions = mgr.getSubscriptions();
			affiliations = mgr.getAffiliations();
			for(int i = 0; i < affiliations.size(); i++) {
				System.out.println(affiliations.get(i).getNodeId());
				leaf = mgr.getNode(affiliations.get(i).getNodeId());
				System.out.println(leaf.getSubscriptions().get(0).getJid());
			}
			break;
		case 4:
			affiliations = mgr.getAffiliations();
			for(int i = 0; i < affiliations.size(); i++) {
				System.out.println(affiliations.get(i).getNodeId());
			}
			System.out.println("wählen Sie aus:");
			nodeName = in.nextLine();
			leaf = mgr.getNode(nodeName);
			leaf.unsubscribe(jid);
			break;
		case 5:
			System.out.println("Node wählen:");
			nodeName = in.nextLine();
			leaf = mgr.getNode(nodeName);
			System.out.println(leaf.subscribe(jid).getId());
			break;
		case 6:
			affiliations = mgr.getAffiliations();
			for(int i = 0; i < affiliations.size(); i++) {
				System.out.println(affiliations.get(i).getNodeId());
			}
			System.out.println("wählen Sie aus:");
			nodeName = in.nextLine();
			leaf = mgr.getNode(nodeName);
			System.out.println("itemname:");
			String itemName = in.nextLine();
			// Datum und Uhrzeit
			GregorianCalendar gCalendar = new GregorianCalendar();
			Date currentDate = new Date();
			gCalendar.setTime(currentDate);
			XMLGregorianCalendar xmlCalendar = null;
			try {
				xmlCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(gCalendar);
				} catch (DatatypeConfigurationException ex) {
			}
			leaf.publish(new PayloadItem(itemName + System.currentTimeMillis(), 
			          new SimplePayload("notification", "pubsub:ticket:notification", 
			        		  "<notification xmlns='pubsub:ticket:notification'><ticketId>12345</ticketId><type>new ticket</type><date>"+xmlCalendar+"</date><tag>Hardware</tag></notification>")));
			break;
		case 7:
			affiliations = mgr.getAffiliations();
			for(int i = 0; i < affiliations.size(); i++) {
				System.out.println(affiliations.get(i).getNodeId());
				mgr.deleteNode(affiliations.get(i).getNodeId());
			}
			//System.out.println("wählen Sie aus:");
			//nodeName = in.nextLine();
			//mgr.deleteNode(nodeName);
			/*leaf = mgr.getNode(nodeName);
			leaf.addItemDeleteListener(new ItemDeleteCoordinator());
			leaf.deleteAllItems();*/
		}
	}
}
