package de.steinleostolski.client;

import java.util.Scanner;



import com.sun.jersey.api.client.*;

import de.steinleostolski.user.CtProfile;
import de.steinleostolski.user.Cttickets;
import de.steinleostolski.user.ObjectFactory;
import de.steinleostolski.user.Stknowhow;
import de.steinleostolski.user.Userdb;
import de.steinleostolski.user.CtProfile.KnowHows;

public class UserTestClient {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int auswahl = 0;
		Scanner in = new Scanner(System.in);
		System.out.println("1:User erstellen");
		System.out.println("2:User löschen");
		auswahl = in.nextInt();
		in.nextLine();
		if(auswahl == 1) {
		try {
			 
			Client client = Client.create();
	 
			WebResource webResource = client
			   .resource("http://localhost:4434/user/add/");
	 
			Userdb user = new ObjectFactory().createUserdb();
			CtProfile profile = new ObjectFactory().createCtProfile();
			KnowHows knowhow = new ObjectFactory().createCtProfileKnowHows();
			Cttickets tickets = new ObjectFactory().createCttickets();
			
			
			profile.setVorname("Vorname");
			profile.setNachname("Nachname");
			profile.setStandort("CGN");
			profile.setStatus("supporter");
			
			knowhow.getKnowHow().add(Stknowhow.HARDWARE);
			knowhow.getKnowHow().add(Stknowhow.SOFTWARE);
			profile.setKnowHows(knowhow);
			profile.setTickets(tickets);
			user.getUser().add(profile);
			
	 
			ClientResponse response = webResource.accept("MediaType.APPLICATION_XML")
					.post(ClientResponse.class, user);
	 
			if (response.getStatus() != 201) {
				throw new RuntimeException("Failed : HTTP error code : "
				     + response.getStatus());
			}
	 
			System.out.println("Output from Server .... \n");
			String output = response.getEntity(String.class);
			System.out.println(output);
	 
		  } catch (Exception e) {
	 
			e.printStackTrace();
	 
		  }
		} else if(auswahl == 2) {
		System.out.println("id eingeben:");
		String id = in.nextLine();
		try {
			Client client = Client.create();
			WebResource webResource = client
					   .resource("http://localhost:4434/user/"+id+"/delete");
			
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
		}
	} 
	}
}
