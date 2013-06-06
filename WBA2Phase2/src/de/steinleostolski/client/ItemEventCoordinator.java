package de.steinleostolski.client;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.Collection;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import org.jivesoftware.smackx.pubsub.Item;
import org.jivesoftware.smackx.pubsub.ItemPublishEvent;
import org.jivesoftware.smackx.pubsub.PayloadItem;
import org.jivesoftware.smackx.pubsub.listener.ItemEventListener;

import de.steinleostolski.jaxb.Ressource;
import de.steinleostolski.payload.Notification;

public class ItemEventCoordinator extends Ressource implements ItemEventListener {
	
	
	@Override
	public void handlePublishedItems(ItemPublishEvent items) {
		System.out.println("Item count: " + items.getItems().size());
		Collection<? extends Item> itemss = items.getItems();
        for (Item item : itemss) {
                  PayloadItem pi = (PayloadItem) item;
                  
                  JAXBContext jc;
				try {
					jc = JAXBContext.newInstance(Notification.class);
					Unmarshaller unmarshaller = jc.createUnmarshaller();

					String payloadXml = pi.getPayload().toXML();
					StringReader reader = new StringReader(payloadXml);
					Notification notify = (Notification) unmarshaller.unmarshal(reader);
					System.out.println("Neue benachrichtigung");
					System.out.println("ticket id: "+ notify.getTicketId());
					System.out.println("datum: "+notify.getDate());
					System.out.println("type:" + notify.getType());
					System.out.println("tag: "+notify.getTag());
					
					
				} catch (JAXBException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
          		  
				
        }
	}

	@Override
	@GET
	@Produces("application/xml")
	public Object get() throws JAXBException, IOException {
		// TODO Auto-generated method stub
		return null;
	}

}
