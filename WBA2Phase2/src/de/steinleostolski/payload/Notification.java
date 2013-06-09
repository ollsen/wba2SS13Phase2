package de.steinleostolski.payload;

import java.math.BigInteger;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
	    "ticketId",
	    "type",
	    "date",
	    "tag"
	})
@XmlRootElement(name = "notification")
public class Notification {

	@XmlElement(name = "ticketId", required = true)
	protected BigInteger ticketId;
	
	@XmlElement(name = "type", required = true)
	protected String type;
	
	@XmlElement(name = "date", required = true)
	protected XMLGregorianCalendar date;
	
	@XmlElement(name = "tag", required = true)
	protected String tag;

	public BigInteger getTicketId() {
		return ticketId;
	}

	public void setTicketId(BigInteger ticketId) {
		this.ticketId = ticketId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public XMLGregorianCalendar getDate() {
		return date;
	}

	public void setDate(XMLGregorianCalendar date) {
		this.date = date;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}
}
