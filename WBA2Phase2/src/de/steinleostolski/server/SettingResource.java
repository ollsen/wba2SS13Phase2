package de.steinleostolski.server;

import java.awt.Color;
import java.io.IOException;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBException;

import org.jivesoftware.smack.XMPPException;

import de.steinleostolski.jaxb.Ressource;
import de.steinleostolski.settings.Settings;
import de.steinleostolski.ticket.Ticket;
import de.steinleostolski.user.Userdb;
import de.steinleostolski.xmpp.PubsubClient;

@Path("settings")
public class SettingResource extends Ressource {

	private static String schemaLoc;
	private PubsubClient pubsub;
	
	private final String username = "restfull";
	private final String password = "restful";
	private final String jid = username+"@localhost";

	@Override
	@GET
	@Produces(MediaType.APPLICATION_XML)
	public Settings get() throws JAXBException, IOException {
		Settings setting = new Settings();
		setting = (Settings) unmarshal(Settings.class, "settings.xml");
		return setting;
	}
	
	@POST
	@Consumes("text/plain")
	@Path("addfield")
	public Response post(String itFieldNew) throws JAXBException, IOException {
		Settings setting = get();
		setting.getITBereich().getFachbereich().add(itFieldNew);
		
		schemaLoc = "http://example.org/ticket ../../schema/SettingsSchema.xsd";
		marshal(Settings.class, setting, "settings.xml", schemaLoc);
		String result = "Eintrag hinzugefügt";
		
		if(pubsub == null) {
			login();
		}
		
		try {
			pubsub.createLeafNode(itFieldNew);
		} catch (XMPPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		return Response.status(201).entity(result).build();
	}
	
	@DELETE
	@Consumes(MediaType.APPLICATION_XML)
	@Path("delete")
	public Response delete(@QueryParam("itfield") String itFieldRemove) throws JAXBException, IOException {
		Settings setting = get();
		for (int i = 0; i < setting.getITBereich().getFachbereich().size(); i++) {
			if(setting.getITBereich().getFachbereich().get(i).equals(itFieldRemove)) {
				setting.getITBereich().getFachbereich().remove(i);
			}
		}
		schemaLoc = "http://example.org/ticket ../../schema/SettingsSchema.xsd";
		marshal(Settings.class, setting, "settings.xml", schemaLoc);
		
		Userdb userdb = (Userdb) unmarshal(Userdb.class, "user.xml");
		
		for(int i = 0; i < userdb.getUser().size(); i++) {
			for (int j = 0; j < userdb.getUser().get(i).getKnowHows().getKnowHow()
					.size(); j++) {
				if(userdb.getUser().get(i).getKnowHows().getKnowHow().get(j)
						.equalsIgnoreCase(itFieldRemove)) {
					userdb.getUser().get(i).getKnowHows().getKnowHow().remove(j);
					break;
				}
			}
		}
		
		if(pubsub == null) {
			login();
		}
		try {
			pubsub.removeLeafNode(itFieldRemove);
		} catch (XMPPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String result = "Eintrag entfernt";
		return Response.status(201).entity(result).build();
		
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
