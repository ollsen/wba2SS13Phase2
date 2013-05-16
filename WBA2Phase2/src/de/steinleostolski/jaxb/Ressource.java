package de.steinleostolski.jaxb;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;



public abstract class Ressource {
	
	final static String xmlDir = "src/de/steinleostolski/xml/";
	
	@SuppressWarnings("rawtypes")
	public Object unmarshal(Class clazz, String xml) throws JAXBException, IOException {
		Object object = new Object();
		JAXBContext jc = JAXBContext.newInstance(clazz);
		Unmarshaller unmarshaller = jc.createUnmarshaller();
		InputStream inputStream = new FileInputStream(xmlDir+xml);
		Reader reader = new InputStreamReader(inputStream,"UTF-8");
	    try {
	    	object = (Object) unmarshaller.unmarshal(reader);
	    } finally {
	    	reader.close();
	    }
		return object;
	}
	
	@SuppressWarnings("rawtypes")
	public void marshal(Class clazz, Object object, String xml, String schemaLoc) throws JAXBException {
		JAXBContext jc = JAXBContext.newInstance(clazz);
		Marshaller marshaller = jc.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
		marshaller.setProperty("jaxb.schemaLocation", schemaLoc);
		marshaller.marshal(object, new File(xmlDir+xml));
	}
	
	@GET
	@Produces(MediaType.APPLICATION_XML)
	public abstract Object get() throws JAXBException, IOException;
	
	@POST
	public Response post() {
		return null;
	}
	
	@PUT
	public Response put() {
		return null;
	}
	
	@DELETE
	public Response delete() {
		return null;
	}
	
}
