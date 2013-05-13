package server;


import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.annotation.XmlRootElement;

@Path( "message" )
public class MessageResource {
	@GET
	@Path( "serverinfo" )
	@Produces (MediaType.TEXT_XML)
	public static ServerInfo serverinfo() {
		ServerInfo info = new ServerInfo();
	    info.server = System.getProperty( "os.name" )+" "+System.getProperty( "os.version" );
	    return info;
	}
	
	@XmlRootElement
	static class ServerInfo {
		public String server;
	}
}
