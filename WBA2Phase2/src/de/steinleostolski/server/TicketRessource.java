package de.steinleostolski.server;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBException;
import de.steinleostolski.jaxb.Ressource;
import de.steinleostolski.ticket.Ticket;
import de.steinleostolski.tickets.StZustand;
import de.steinleostolski.tickets.Ticketlist;
import de.steinleostolski.tickets.Ticketlist.Teintrag;
import de.steinleostolski.tickets.Ticketlist.Teintrag.Tags;
import de.steinleostolski.user.Userdb;



@Path("ticket")
public class TicketRessource extends Ressource{
	
	private static String schemaLoc;

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
		int ticketId = ticketlist.getTeintrag().get(ticketlist.getTeintrag().size()-1)
				.getTicketId().intValue()+1;
		
		
		ticket.setId(BigInteger.valueOf(ticketId));
		schemaLoc = "http://example.org/ticket ../../schema/TicketSchema.xsd";
		marshal(Ticket.class, ticket, "tickets/"+ticketId+".xml", schemaLoc);
		
		Teintrag tEintrag = new Teintrag();
		tEintrag.setTags(new Tags());
		tEintrag.setTicketId(BigInteger.valueOf(ticketId));
		tEintrag.setBetreff(ticket.getInfos().getBetreff());
		tEintrag.setDatum(ticket.getInfos().getDatum());
		tEintrag.setZustand(StZustand.valueOf(ticket.getInfos().getZustand().toString()));
		for(int i = 0; i < ticket.getInfos().getTags().getTag().size(); i++) {
			tEintrag.getTags().getTag().add(
					ticket.getInfos().getTags().getTag().get(i).value());
		}
		
		ticketlist.getTeintrag().add(tEintrag);
		
		schemaLoc = "http://example.org/ticket ../schema/TListeSchema.xsd";
		marshal(Ticketlist.class, ticketlist, "ticketliste.xml", schemaLoc);
		
		Userdb userdb = new Userdb();
		userdb = (Userdb) unmarshal(Userdb.class, "user.xml");
		
		BigInteger userId = ticket.getInfos().getUser().getId();
		BigInteger supporterId = ticket.getInfos()
				.getSupporterList().getSupporter().get(0).getId();
		
		for(int i = 0; i < userdb.getUser().size(); i++) {
			if(userdb.getUser().get(i).getId().equals(userId) ||
					userdb.getUser().get(i).getId().equals(supporterId))
				userdb.getUser().get(i).getTickets().getTicketId()
				.add(BigInteger.valueOf(ticketId));
		}
		
		schemaLoc = "http://example.org/ticket ../schema/UListeSchema.xsd";
		marshal(Userdb.class, userdb, "user.xml", schemaLoc);
		
		String result = "Ticket mit der id: "+ticket.getId()+" hinzugefügt";
		
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
	@Path("{id}/edit")
	public Response setStatus(@PathParam("id") BigInteger id) {
		return Response.status(201).build();
	}
	
	@PUT
	@Consumes(MediaType.APPLICATION_XML)
	@Path("{id}/status")
	public Response setStatus(@PathParam("id") BigInteger id,
			@QueryParam("setStatus")String zustand) throws JAXBException, IOException {
		Ticket ticket = getTicket(id);
		
		ticket.getInfos().setZustand(de.steinleostolski.ticket.StZustand.fromValue(zustand));
		Ticketlist tList = get();
		
		for(int i = 0; i < tList.getTeintrag().size(); i++) {
			if(tList.getTeintrag().get(i).getTicketId().equals(id))
				tList.getTeintrag().get(i).setZustand(StZustand.valueOf(zustand));
		}
		
		return Response.status(201).build();
	}
	
	
	

}
