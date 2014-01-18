package fmin362.resources;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Query;
import com.sun.jersey.multipart.FormDataMultiPart;

import fmin362.models.Tag;
import fmin362.models.User;

@Path( "/tags" ) // http://localhost:9000/FMIN362-Tweeter/resources/tags
public class TagResource {
	@GET
    @Path( "/get" )
    @Produces( MediaType.APPLICATION_JSON )
    public List<Tag> get()
    {           
            Query<Tag> find = Ebean.find(Tag.class);
            return find.findList();
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

    	Tag.delete(names);
        return Response.status(200).entity("Tag(s) deleted").build();
    }
}
