package fmin362.resources;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Query;

import fmin362.models.User;

@Path( "/users" ) // http://localhost:9000/FMIN362-Tweeter/resources/users
public class UserResource {
	@GET
    @Path( "/get" )
    @Produces( MediaType.APPLICATION_JSON )
    public List<User> get()
    {           
            Query<User> find = Ebean.find(User.class);
            return find.findList();
    }
	
	@POST
    @Path( "/login" )
    @Consumes( MediaType.APPLICATION_FORM_URLENCODED )
	@Produces( MediaType.TEXT_PLAIN ) // + ";" + MediaType.APPLICATION_JSON
    public Response login(	@FormParam("username") String username,
                            @FormParam("passwd") String passwd ) 
    {       
        User user = User.findByName(username);
        if (user == null)
        	return Response.status(404).entity("User "+username+" doesn't exist").build(); // 404: Not Found
        if (!passwd.equals(user.getPasswd()))
        	return Response.status(403).entity("Wrong password").build(); // 403: Forbidden
        if (user.isIs_admin())
        	return Response.status(221).entity("Login successful as admin").build(); // 221: ici, indique que user est admin
        return Response.status(220).entity("Login successful").build(); // 220: ici, indique que user n'est pas admin
    }
	
	@POST
    @Path( "/register" )
    @Consumes( MediaType.APPLICATION_FORM_URLENCODED )
	@Produces( MediaType.TEXT_PLAIN )
    public Response register (	@FormParam("username") String username,
                            	@FormParam("passwd") String passwd ) 
    {       
		User user = User.findByName(username);
		if (user != null)
			return Response.status(403).entity("Username "+username+" already in use").build(); // 403: Forbidden
		user = new User();
		user.setUsername(username);
		user.setPasswd(passwd);
		if (!User.save(user))
			return Response.status(403).entity("Username "+username+" incorrect").build(); // 403: Forbidden
		
		return Response.status(200).entity("Successfully registered").build();
		// faire négociation contenu
    }
}
