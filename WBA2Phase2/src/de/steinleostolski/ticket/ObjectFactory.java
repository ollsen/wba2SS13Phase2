//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2013.06.10 at 01:12:03 PM MESZ 
//


package de.steinleostolski.ticket;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the de.steinleostolski.ticket package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: de.steinleostolski.ticket
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link CtInfo.SupporterList }
     * 
     */
    public CtInfo.SupporterList createCtInfoSupporterList() {
        return new CtInfo.SupporterList();
    }

    /**
     * Create an instance of {@link CtInfo.SupporterList.Supporter }
     * 
     */
    public CtInfo.SupporterList.Supporter createCtInfoSupporterListSupporter() {
        return new CtInfo.SupporterList.Supporter();
    }

    /**
     * Create an instance of {@link CtTicket }
     * 
     */
    public CtTicket createCtTicket() {
        return new CtTicket();
    }

    /**
     * Create an instance of {@link CtAntwort.Supporter }
     * 
     */
    public CtAntwort.Supporter createCtAntwortSupporter() {
        return new CtAntwort.Supporter();
    }

    /**
     * Create an instance of {@link CtInfo.Tags }
     * 
     */
    public CtInfo.Tags createCtInfoTags() {
        return new CtInfo.Tags();
    }

    /**
     * Create an instance of {@link CtInfo }
     * 
     */
    public CtInfo createCtInfo() {
        return new CtInfo();
    }

    /**
     * Create an instance of {@link CtInfo.User }
     * 
     */
    public CtInfo.User createCtInfoUser() {
        return new CtInfo.User();
    }

    /**
     * Create an instance of {@link Ticket }
     * 
     */
    public Ticket createTicket() {
        return new Ticket();
    }

    /**
     * Create an instance of {@link CtTicket.Antworten }
     * 
     */
    public CtTicket.Antworten createCtTicketAntworten() {
        return new CtTicket.Antworten();
    }

    /**
     * Create an instance of {@link CtAntwort }
     * 
     */
    public CtAntwort createCtAntwort() {
        return new CtAntwort();
    }

}
