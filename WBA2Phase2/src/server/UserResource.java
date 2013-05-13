package server;


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

import user.Userdb;

@Path( "user" )
public class UserResource {
	
	private static Userdb user = new Userdb();
	private static JAXBContext jc;
	
	public void unmarshal() throws IOException, JAXBException{
		jc = JAXBContext.newInstance(Userdb.class);
		Unmarshaller unmarshaller = jc.createUnmarshaller();
		InputStream inputStream = new FileInputStream("src/xml/user.xml");
		Reader reader = new InputStreamReader(inputStream,"UTF-8");
	    try {
	    	user = (Userdb) unmarshaller.unmarshal(reader);
	    } finally {
	    	reader.close();
	    }
	}
	
	@GET
	@Produces(MediaType.APPLICATION_XML)
	public Userdb getProfile() throws IOException, JAXBException {
		unmarshal();
		System.out.println("get");
		return user;
		
	}
	
	@GET
	@Produces(MediaType.APPLICATION_XML)
	@Path("{id}")
	public Userdb getProfile(@PathParam("id") String id) throws IOException, JAXBException {
		unmarshal();
		Userdb profile = new Userdb();
		for(int i = 0; i < user.getUser().size(); i++) {
			if(user.getUser().get(i).getId().contains(id))
				profile.getUser().add(user.getUser().get(i));
		}
		return profile;
		
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_XML)
	@Path("add")
	public Response addProfile(Userdb newUser) throws IOException, JAXBException {
		unmarshal();
		user.getUser().add(newUser.getUser().get(0));
		marshal();
		String result = "profile saved";
		return Response.status(201).entity(result).build();
		
	}
	
	@DELETE
	@Consumes(MediaType.APPLICATION_XML)
	@Path("delete/{id}")
	public Response deleteProfile(@PathParam("id") String id) throws IOException, JAXBException{
		unmarshal();
		System.out.println("delete");
		String result = null;
		for(int i = 0; i < user.getUser().size(); i++) {
			if(user.getUser().get(i).getId().contains(id)) {
				user.getUser().remove(i);
				result = "Benutzer gelöscht";
			} else {
				result = "Benutzer nicht gefunden";
			}
		}
		marshal();
		System.out.println(result);
		return Response.noContent().entity(result).build();
	}

	private void marshal() throws JAXBException, IOException {
		
		Marshaller marshaller = jc.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
		marshaller.marshal(user, new File("src/xml/user.xml"));
	}
	
}
