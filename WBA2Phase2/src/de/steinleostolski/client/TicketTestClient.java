package de.steinleostolski.client;

import java.math.BigInteger;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Scanner;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import de.steinleostolski.ticket.CtAntwort;
import de.steinleostolski.ticket.CtInfo;
import de.steinleostolski.ticket.CtInfo.SupporterList;
import de.steinleostolski.ticket.CtInfo.SupporterList.Supporter;
import de.steinleostolski.ticket.CtInfo.Tags;
import de.steinleostolski.ticket.CtInfo.User;
import de.steinleostolski.ticket.CtTicket.Antworten;
import de.steinleostolski.ticket.ObjectFactory;
import de.steinleostolski.ticket.StKnowhow;
import de.steinleostolski.ticket.StZustand;
import de.steinleostolski.ticket.Ticket;

public class TicketTestClient {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int auswahl = 0;
		Scanner in = new Scanner(System.in);
		System.out.println("1:Ticket erstellen");
		System.out.println("2:Ticket löschen");
		System.out.println("3:Ticketstatus setzen");
		System.out.println("4:Ticket bearbeiten");

		auswahl = in.nextInt();
		in.nextLine();
		if(auswahl == 1) {
			try {
				 
				Client client = Client.create();
		 
				WebResource webResource = client
				   .resource("http://localhost:4434/ticket/add/");
				
				Ticket ticket = new ObjectFactory().createTicket();
				
				Supporter supporter = new Supporter();
				supporter.setId(BigInteger.valueOf(3));
				supporter.setValue("Michael Schmidt");
	
				// Datum und Uhrzeit
				GregorianCalendar gCalendar = new GregorianCalendar();
				Date currentDate = new Date();
				gCalendar.setTime(currentDate);
				XMLGregorianCalendar xmlCalendar = null;
				try {
					xmlCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(gCalendar);
					} catch (DatatypeConfigurationException ex) {
				}
				
				
				ticket.setInfos(new CtInfo());
				ticket.getInfos().setSupporterList(new SupporterList());
				ticket.getInfos().setTags(new Tags());
				ticket.getInfos().setUser(new User());
				ticket.setAntworten(new Antworten());
				ticket.getInfos().setBeschreibung("Testbeschreibung");
				ticket.getInfos().setBetreff("Testbetreff");
				ticket.getInfos().setDatum(xmlCalendar);
				ticket.getInfos().setStandort("CGN");
				ticket.getInfos().getSupporterList().getSupporter().add(supporter);
				ticket.getInfos().getTags().getTag().add(StKnowhow.HARDWARE);
				ticket.getInfos().getUser().setId(BigInteger.valueOf(1));
				ticket.getInfos().getUser().setValue("Max Mustermann");
				ticket.getInfos().setZustand(StZustand.NORMAL);
				
		 
				ClientResponse response = webResource.accept("MediaType.APPLICATION_XML")
						.post(ClientResponse.class, ticket);
		 
				if (response.getStatus() != 201) {
					throw new RuntimeException("Failed : HTTP error code : "
					     + response.getStatus());
				}
		 
				System.out.println("Output from Server .... \n");
				String output = response.getEntity(String.class);
				System.out.println(output);
		 
			  } catch (Exception e) {
		 
				e.printStackTrace();
		 
			  } finally {
					in.close();
				}

		} else if (auswahl == 2) {
			System.out.println("id eingeben:");
			String id = in.nextLine();
			try {
				Client client = Client.create();
				WebResource webResource = client
						   .resource("http://localhost:4434/ticket/"+id+"/delete");
				
				ClientResponse response = webResource.accept("MediaType.APPLICATION_XML")
						.delete(ClientResponse.class);
				
				if (response.getStatus() != 200) {
					throw new RuntimeException("Failed : HTTP error code : "
					     + response.getStatus());
				}
		 
				System.out.println("Output from Server .... \n");
				String output = response.getEntity(String.class);
				System.out.println(output);
				
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				in.close();
			}
		} else if(auswahl == 3) {
			System.out.println("id eingeben:");
			String id = in.nextLine();
			System.out.println("status: ");
			String status = in.nextLine();
			try {
				Client client = Client.create();
				WebResource webResource = client
						   .resource("http://localhost:4434/ticket/"+id+"/set?status="+status);
				
				ClientResponse response = webResource.accept("MediaType.APPLICATION_XML")
						.put(ClientResponse.class);
				
				if (response.getStatus() != 201) {
					throw new RuntimeException("Failed : HTTP error code : "
					     + response.getStatus());
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				in.close();
			}
		} else if(auswahl == 4) {
			System.out.println("ticketid eingeben:");
			String tid = in.nextLine();
			
			System.out.println("userid:");
			BigInteger uit = BigInteger.valueOf(in.nextLong());
			in.nextLine();
			
			System.out.println("Antwort:");
			String antwortText = in.nextLine();
			
			CtAntwort antwort = new CtAntwort();
			
			antwort.getSupporter().setId(uit);
			
			try {
				Client client = Client.create();
				WebResource webResource = client
						   .resource("http://localhost:4434/ticket/"+tid+"/answer");
				
				ClientResponse response = webResource.accept("MediaType.APPLICATION_XML")
						.put(ClientResponse.class);
				
				if (response.getStatus() != 201) {
					throw new RuntimeException("Failed : HTTP error code : "
					     + response.getStatus());
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				in.close();
			}
			
		}
		
		
	}

}
