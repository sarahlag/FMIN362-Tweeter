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
import com.sun.jersey.multipart.FormDataMultiPart;

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
	@Produces( MediaType.TEXT_PLAIN )
    public Response login(	@FormParam("username") String username,
                            @FormParam("passwd") String passwd,
                            @FormParam("newpasswd") String newpasswd,
                            @FormParam("newpasswd_verif") String newpasswd_verif ) 
    {       
        User user = User.findByName(username);
        if (user == null)
        	return Response.status(404).entity("User "+username+" doesn't exist").build(); // 404: Not Found
        if (!passwd.equals(user.getPasswd()))
        	return Response.status(403).entity("Wrong password").build(); // 403: Forbidden
        
        if (newpasswd != null || newpasswd_verif != null)
        {
        	if (newpasswd == null || newpasswd_verif == null)
        		return Response.status(403).entity("This action requires all fields").build(); // 403: Forbidden
        	if (newpasswd.isEmpty())
        		return Response.status(403).entity("A password cannot be empty").build(); // 403: Forbidden
        	if (!newpasswd.equals(newpasswd_verif))
        		return Response.status(403).entity("The new passwords don't match").build(); // 403: Forbidden
        	user.setPasswd(newpasswd);
        	User.update(user);
        }
        
        if (user.isIs_admin())
        	return Response.status(200).entity("Login successful as admin").build();
        return Response.status(200).entity("Login successful").build();
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
    }
	
	@POST
    @Path( "/delete" )
    @Consumes( MediaType.MULTIPART_FORM_DATA )
    public Response deleteUser( FormDataMultiPart form )
    {       
    	String username = form.getField("username").toString();
    	String names = form.getField("names").toString();
    	
    	User admin = User.findByName(username);
    	if (admin == null || !admin.isIs_admin())
    		return Response.status(403).entity("You are not allowed to do that").build();
    	       	
    	// af
        return Response.status(200).entity("User(s) deleted").build();
    }
}
