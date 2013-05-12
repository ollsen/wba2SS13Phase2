

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import user.Userdb;

@Path( "user" )
public class UserResource {
	
	private static Userdb supporter = new Userdb();
	
	@GET
	@Produces(MediaType.APPLICATION_XML)
	public Userdb getSupporterList() {
		return supporter;
	}
	
	//@GET( "supporter/{profile}" )
	public String message(@PathParam("supporter") String user) {
		return String.format("Benutzer %s", user);
	}
}
