package de.steinleostolski.server;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimerTask;

import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import de.steinleostolski.jaxb.Ressource;
import de.steinleostolski.tickets.Ticketlist;

public class Task extends TimerTask {

	@Override
	public void run() {
		Ticketlist ticketlist = new Ticketlist();
		TicketRessource tres = new TicketRessource();
		
		try {
			ticketlist = tres.get();
		} catch (JAXBException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		for(int i = 0; i < ticketlist.getTeintrag().size(); i++) {
			GregorianCalendar date = ticketlist.getTeintrag().get(i).getDatum().toGregorianCalendar();
			date.add(GregorianCalendar.DAY_OF_MONTH, 10);
			
			if(getDate().after(date) && !ticketlist.getTeintrag().get(i).getZustand()
					.equalsIgnoreCase("kritisch")) {
				ticketlist.getTeintrag().get(i).setZustand("kritisch");
				
				
				try {
					tres.setStatus(ticketlist.getTeintrag().get(i).getTicketId(),
							"kritisch");
				} catch (JAXBException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
	}
	
	private GregorianCalendar getDate() {
		// Datum und Uhrzeit
		GregorianCalendar gCalendar = new GregorianCalendar();
		Date currentDate = new Date();
		gCalendar.setTime(currentDate);
		return gCalendar;
	}

}
