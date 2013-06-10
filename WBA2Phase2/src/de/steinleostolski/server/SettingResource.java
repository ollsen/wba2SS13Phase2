package de.steinleostolski.server;

import java.io.IOException;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBException;

import de.steinleostolski.jaxb.Ressource;
import de.steinleostolski.settings.Settings;
import de.steinleostolski.ticket.Ticket;

@Path("settings")
public class SettingResource extends Ressource {

	private static String schemaLoc;

	@Override
	@GET
	@Produces(MediaType.APPLICATION_XML)
	public Settings get() throws JAXBException, IOException {
		Settings setting = new Settings();
		setting = (Settings) unmarshal(Settings.class, "settings.xml");
		return setting;
	}
	
	@POST
	@Produces(MediaType.APPLICATION_XML)
	@Path("additfield")
	public Response post(Settings postsetting) throws JAXBException, IOException {
		Settings setting = get();
		
		setting.getITBereich().getFachbereich().add(postsetting.getITBereich().getFachbereich().get(0));
		
		schemaLoc = "http://example.org/ticket ../../schema/SettingsSchema.xsd";
		marshal(Settings.class, setting, "settings.xml", schemaLoc);
		String result = "Eintrag hinzugefügt";
		return Response.status(201).entity(result).build();
	}
	
}
