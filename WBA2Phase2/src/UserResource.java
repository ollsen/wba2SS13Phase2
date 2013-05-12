

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import user.CtProfile;
import user.Userdb;

@Path( "user" )
public class UserResource {
	
	private static Userdb user = new Userdb();
	
	public void unmarshal() throws IOException, JAXBException{
		JAXBContext jc = JAXBContext.newInstance(Userdb.class);
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
	public Userdb getProfile() throws IOException, JAXBException{
		unmarshal();
		return user;
		
	}
	
}
