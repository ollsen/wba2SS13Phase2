package de.steinleostolski.server;

import java.io.IOException;
import java.math.BigInteger;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBException;

import de.steinleostolski.jaxb.Ressource;
import de.steinleostolski.tickets.Ticketlist;
import de.steinleostolski.user.Userdb;

@Path("user")
public class UserResource extends Ressource {
	
	private static String schemaLoc;

	@GET
	@Produces(MediaType.APPLICATION_XML)
	public Userdb get() throws JAXBException, IOException {
		Userdb userdb = (Userdb) unmarshal(Userdb.class, "user.xml");
		return userdb;
	}

	@GET
	@Produces(MediaType.APPLICATION_XML)
	@Path("{id}")
	public Userdb get(@PathParam("id") BigInteger id) throws JAXBException, IOException {
		Userdb userdb = get();
		Userdb user = new Userdb();
		for(int i = 0; i < userdb.getUser().size(); i++) {
			if(userdb.getUser().get(i).getId().equals(id)) {
				user.getUser().add(userdb.getUser().get(i));
			}
		}
		return user;
	}
	
	@GET
	@Produces(MediaType.APPLICATION_XML)
	@Path("{id}/tickets")
	public Ticketlist getUserTickets(@PathParam("id") BigInteger id) 
			throws JAXBException, IOException {
		Userdb user = get(id);
		Ticketlist ticketlist = (Ticketlist) unmarshal(Ticketlist.class, "ticketliste.xml");
		Ticketlist userTList = new Ticketlist();
		for(int i = 0; i < ticketlist.getTeintrag().size(); i++) {
			for(int j = 0; j < user.getUser().get(0).getTickets()
					.getTicketId().size(); j++) {
				if(user.getUser().get(0).getTickets().getTicketId().get(j).equals(
						ticketlist.getTeintrag().get(i).getTicketId())) {
					userTList.getTeintrag().add(ticketlist.getTeintrag().get(i));
				}
			}
		}
		return userTList;
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_XML)
	@Path("add")
	public Response addProfile(Userdb newUser) throws IOException, JAXBException {
		Userdb userdb = (Userdb) unmarshal(Userdb.class, "user.xml");
		int userid = userdb.getUser().get(userdb.getUser().size()-1)
				.getId().intValue()+1;
		newUser.getUser().get(0).setId(BigInteger.valueOf(userid));
		userdb.getUser().add(newUser.getUser().get(0));
		
		schemaLoc = "http://example.org/ticket ../schema/UListeSchema.xsd";
		marshal(Userdb.class, userdb, "user.xml", schemaLoc);
		
		String result = "profile saved";
		return Response.status(201).entity(result).build();
		
	}
	
	@DELETE
	@Consumes(MediaType.APPLICATION_XML)
	@Path("{id}/delete")
	public Response deleteProfile(@PathParam("id") BigInteger id) throws IOException, JAXBException{
		Userdb userdb = (Userdb) unmarshal(Userdb.class, "user.xml");
		System.out.println("delete");
		String result = null;
		for(int i = 0; i < userdb.getUser().size(); i++) {
			if(userdb.getUser().get(i).getId().equals(id)) {
				userdb.getUser().remove(i);
				result = "Benutzer gelöscht";
			} else {
				result = "Benutzer nicht gefunden";
			}
		}
		schemaLoc = "http://example.org/ticket ../schema/UListeSchema.xsd";
		marshal(Userdb.class, userdb, "user.xml", schemaLoc);
		
		System.out.println(result);
		return Response.noContent().entity(result).build();
	}
}
