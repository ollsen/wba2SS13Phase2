package de.steinleostolski.server;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBException;

import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.pubsub.PayloadItem;
import org.jivesoftware.smackx.pubsub.SimplePayload;

import de.steinleostolski.jaxb.Ressource;
import de.steinleostolski.settings.Settings;
import de.steinleostolski.ticket.CtAntwort;
import de.steinleostolski.ticket.CtInfo.SupporterList.Supporter;
import de.steinleostolski.ticket.CtTicket.Antworten;
import de.steinleostolski.ticket.Ticket;
import de.steinleostolski.tickets.StZustand;
import de.steinleostolski.tickets.Ticketlist;
import de.steinleostolski.tickets.Ticketlist.Teintrag;
import de.steinleostolski.tickets.Ticketlist.Teintrag.Tags;
import de.steinleostolski.user.Userdb;
import de.steinleostolski.xmpp.PubsubClient;



@Path("ticket")
public class TicketRessource extends Ressource{
	
	private static String schemaLoc;
	private PubsubClient pubsub;
	
	private final String username = "restfull";
	private final String password = "restful";
	private final String jid = username+"@localhost";

	@Override
	@GET
	@Produces(MediaType.APPLICATION_XML)
	public Ticketlist get() throws JAXBException, IOException {
		Ticketlist ticketlist = new Ticketlist();
		ticketlist = (Ticketlist) unmarshal(Ticketlist.class, "ticketliste.xml");
		return ticketlist;
	}
	
	@GET
	@Produces(MediaType.APPLICATION_XML)
	@Path("{id}")
	public Ticket getTicket(@PathParam("id") BigInteger id) throws JAXBException, IOException{
		Ticket ticket = new Ticket();
		ticket = (Ticket) unmarshal(Ticket.class, "tickets/"+id+".xml");
		
		return ticket;
	}
	
	@GET
	@Path("query")
	public Ticketlist getQuery(@QueryParam("zustand") String zustand) throws JAXBException, IOException {
		Ticketlist ticketlist = get();
		
		Ticketlist newList = new Ticketlist();
		
		for(int i = 0; i < ticketlist.getTeintrag().size(); i++) {
			System.out.println(ticketlist.getTeintrag().get(i).getZustand().toString().toLowerCase());
			if(ticketlist.getTeintrag().get(i).getZustand().toString().toLowerCase().equals(zustand)) {
				newList.getTeintrag().add(ticketlist.getTeintrag().get(i));
			}
		}
		
		return newList;
	}

	@POST
	@Consumes(MediaType.APPLICATION_XML)
	@Path("add")
	public Response post(Ticket ticket) throws JAXBException, IOException {
		Ticketlist ticketlist = get();
		int ticketId = 1;
		if(ticketlist.getTeintrag().size() != 0)
		ticketId = ticketlist.getTeintrag().get(ticketlist.getTeintrag().size()-1)
				.getTicketId().intValue()+1;
		
		
		ticket.setId(BigInteger.valueOf(ticketId));
		schemaLoc = "http://example.org/ticket ../../schema/TicketSchema.xsd";
		marshal(Ticket.class, ticket, "tickets/"+ticketId+".xml", schemaLoc);
		
		Teintrag tEintrag = new Teintrag();
		tEintrag.setTags(new Tags());
		tEintrag.setTicketId(BigInteger.valueOf(ticketId));
		tEintrag.setErstellerId(ticket.getInfos().getUser().getId());
		tEintrag.setBetreff(ticket.getInfos().getBetreff());
		tEintrag.setDatum(ticket.getInfos().getDatum());
		tEintrag.setZustand(ticket.getInfos().getZustand());
		tEintrag.setBearbeitungszustand(false);
		for(int i = 0; i < ticket.getInfos().getTags().getTag().size(); i++) {
			tEintrag.getTags().getTag().add(
					ticket.getInfos().getTags().getTag().get(i));
		}
		
		ticketlist.getTeintrag().add(tEintrag);
		
		schemaLoc = "http://example.org/ticket ../schema/TListeSchema.xsd";
		marshal(Ticketlist.class, ticketlist, "ticketliste.xml", schemaLoc);
		
		Userdb userdb = new Userdb();
		userdb = (Userdb) unmarshal(Userdb.class, "user.xml");
		
		BigInteger userId = ticket.getInfos().getUser().getId();
		
		for(int i = 0; i < userdb.getUser().size(); i++) {
			if(userdb.getUser().get(i).getId().equals(userId))
				userdb.getUser().get(i).getTickets().getTicketId()
				.add(BigInteger.valueOf(ticketId));
		}
		
		schemaLoc = "http://example.org/ticket ../schema/UListeSchema.xsd";
		marshal(Userdb.class, userdb, "user.xml", schemaLoc);
		
		String result = "Ticket mit der id: "+ticket.getId()+" hinzugefügt";
		
		if(RestServerGUI.pubsub == null) {
			System.out.println("login");
			login();
		}
		
		SimplePayload simplePl = new SimplePayload("notification", "pubsub:ticket:notification",
				"<notification xmlns='pubsub:ticket:notification'>" +
				"<ticketId>"+ticket.getId()+"</ticketId>"+
				"<type>new ticket</type>" +
				"<date>"+ticket.getInfos().getDatum()+"</date>" +
				"<tag>"+ticket.getInfos().getTags().getTag().get(0)+"</tag>"+
				"</notification>");
		
		try {
			RestServerGUI.pubsub.sendPayloadItem(ticket.getInfos().getTags().getTag().get(0), simplePl);
		} catch (XMPPException e) {
			System.out.println("Fehler");
			e.printStackTrace();
		}
		
		return Response.status(201).entity(result).build();
	}

	@DELETE
	@Consumes(MediaType.APPLICATION_XML)
	@Path("{id}/delete")
	public Response delete(@PathParam("id") BigInteger id) throws JAXBException, IOException {
		Ticketlist ticketlist = get();
		
		String result = null;
		String result2 = null;
		String result3 = null;
		for(int i = 0; i < ticketlist.getTeintrag().size(); i++) {
			if(ticketlist.getTeintrag().get(i).getTicketId().equals(id)) {
				ticketlist.getTeintrag().remove(i);
				result = "Eintrag in Liste entfernt";
			} else {
				result = "Eintrag in Liste nicht gefunden";
			}
		}
		
		schemaLoc = "http://example.org/ticket ../schema/TListeSchema.xsd";
		marshal(Ticketlist.class, ticketlist, "ticketliste.xml", schemaLoc);
		
		Userdb userdb = (Userdb) unmarshal(Userdb.class, "user.xml");
		int zaehler = 0;
		for(int i = 0; i < userdb.getUser().size(); i++) {
			for(int j = 0; j < userdb.getUser().get(i).getTickets()
					.getTicketId().size(); j++) {
				if (userdb.getUser().get(i).getTickets().getTicketId()
						.get(j).equals(id)) {
					userdb.getUser().get(i).getTickets().getTicketId().remove(j);
					zaehler++;
				}
			}
		}
		
		result2 = String.valueOf(zaehler)+"Einträge wurden aus Userliste entfernt";
		
		schemaLoc = "http://example.org/ticket ../schema/UListeSchema.xsd";
		marshal(Userdb.class, userdb, "user.xml", schemaLoc);
		
		File file = new File("src/de/steinleostolski/xml/tickets/"+id+".xml");
		
		file.delete();
		
		result3 = id+".xml entfernt";
		
		return Response.noContent().entity(result).entity(result2).entity(result3).build();
	}

	@PUT
	@Consumes(MediaType.APPLICATION_XML)
	@Path("{id}/editSupporter")
	public Response setStatus(@PathParam("id") BigInteger id, Ticket newTicket) throws JAXBException, IOException {
		Ticket ticket = getTicket(id);
		
		ticket = newTicket;
		
		schemaLoc = "http://example.org/ticket ../../schema/TicketSchema.xsd";
		marshal(Ticket.class, ticket, "tickets/"+id+".xml", schemaLoc);
		
		Ticketlist ticketlist = get();
		for(Teintrag eintrag : ticketlist.getTeintrag()) {
			if(eintrag.getTicketId().equals(id)) {
				eintrag.setBearbeitungszustand(true);
				break;
			}
		}
		
		schemaLoc = "http://example.org/ticket ../schema/TListeSchema.xsd";
		marshal(Ticketlist.class, ticketlist, "ticketliste.xml", schemaLoc);
		
		return Response.status(201).build();
	}
	
	@PUT
	@Consumes(MediaType.APPLICATION_XML)
	@Path("{id}/release")
	public Response releaseTicket(@PathParam("id") BigInteger id) throws JAXBException, IOException {
		Ticket ticket = getTicket(id);
		ticket.getInfos().setInBearbeitung(false);
		schemaLoc = "http://example.org/ticket ../../schema/TicketSchema.xsd";
		marshal(Ticket.class, ticket, "tickets/"+id+".xml", schemaLoc);
		
		Ticketlist ticketlist = get();
		for(Teintrag eintrag : ticketlist.getTeintrag()) {
			if(eintrag.getTicketId().equals(id)) {
				eintrag.setBearbeitungszustand(false);
				break;
			}
		}
		
		schemaLoc = "http://example.org/ticket ../schema/TListeSchema.xsd";
		marshal(Ticketlist.class, ticketlist, "ticketliste.xml", schemaLoc);
		
		return Response.status(201).build();
	}
	
	@PUT
	@Consumes(MediaType.APPLICATION_XML)
	@Path("{id}/close")
	public Response closeTicket(@PathParam("id") BigInteger id) throws JAXBException, IOException {
		Ticket ticket = getTicket(id);
		ticket.getInfos().setInBearbeitung(false);
		ticket.getInfos().setZustand("geschlossen");
		schemaLoc = "http://example.org/ticket ../../schema/TicketSchema.xsd";
		marshal(Ticket.class, ticket, "tickets/"+id+".xml", schemaLoc);
		
		Ticketlist ticketlist = get();
		for(Teintrag eintrag : ticketlist.getTeintrag()) {
			if(eintrag.getTicketId().equals(id)) {
				eintrag.setBearbeitungszustand(false);
				eintrag.setZustand("geschlossen");
				break;
			}
		}
		
		schemaLoc = "http://example.org/ticket ../schema/TListeSchema.xsd";
		marshal(Ticketlist.class, ticketlist, "ticketliste.xml", schemaLoc);
		
		return Response.status(201).build();
	}
	
	@PUT
	@Consumes(MediaType.APPLICATION_XML)
	@Path("{id}/answer")
	public Response addAnswer(@PathParam("id") BigInteger id, Ticket newTicket) throws JAXBException, IOException {
		Ticket ticket = getTicket(id);
		
		ticket.getAntworten().getAntwort().add(newTicket.getAntworten().getAntwort().get(0));
		
		schemaLoc = "http://example.org/ticket ../../schema/TicketSchema.xsd";
		marshal(Ticket.class, ticket, "tickets/"+id+".xml", schemaLoc);
		
		
		return Response.status(201).build();
	}
	
	@PUT
	@Consumes(MediaType.APPLICATION_XML)
	@Path("{id}/set")
	public Response setStatus(@PathParam("id") BigInteger id,
			@QueryParam("status")String zustand) throws JAXBException, IOException {
		Ticket ticket = getTicket(id);
		
		ticket.getInfos().setZustand(zustand);
		
		schemaLoc = "http://example.org/ticket ../../schema/TicketSchema.xsd";
		marshal(Ticket.class, ticket, "tickets/"+id+".xml", schemaLoc);
		
		Ticketlist tList = get();
		
		for(int i = 0; i < tList.getTeintrag().size(); i++) {
			if(tList.getTeintrag().get(i).getTicketId().equals(id))
				tList.getTeintrag().get(i).setZustand(zustand);
		}
		
		schemaLoc = "http://example.org/ticket ../schema/TListeSchema.xsd";
		marshal(Ticketlist.class, tList, "ticketliste.xml", schemaLoc);
		
		SettingResource sres = new SettingResource();
		Settings settings = sres.get();
		
		SimplePayload simplePl = new SimplePayload("notification", "pubsub:ticket:notification",
				"<notification xmlns='pubsub:ticket:notification'>" +
				"<ticketId>"+ticket.getId()+"</ticketId>"+
				"<type>status geändert</type>" +
				"<date>"+ticket.getInfos().getDatum()+"</date>" +
				"<tag>"+ticket.getInfos().getTags().getTag().get(0)+"</tag>"+
				"</notification>");
		
		for(int i = 0; i < settings.getITBereich().getFachbereich().size(); i++) {
			try {
				pubsub.sendPayloadItem(settings.getITBereich().getFachbereich().get(i), simplePl);
			} catch (XMPPException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return Response.status(201).build();
	}
	
	private void login() {
		pubsub = new PubsubClient(RestServerGUI.adress);
		try {
			pubsub.login(username, password);
		} catch (XMPPException e) {
			System.out.println("Fehlgeschlagen");
		}
		
		pubsub.setJID(jid);
	}
	
	

}
