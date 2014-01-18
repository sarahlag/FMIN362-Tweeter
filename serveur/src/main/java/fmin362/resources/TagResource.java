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
import com.sun.jersey.multipart.FormDataBodyPart;
import com.sun.jersey.multipart.FormDataMultiPart;

import fmin362.models.Tag;
import fmin362.models.Tweet;
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
    public Response deleteTag( FormDataMultiPart form )
    {       
    	String username = form.getField("username").getValueAs(String.class);
    	String names = form.getField("names").getValueAs(String.class);
    	
    	User admin = User.findByName(username);
    	if (admin == null || !admin.isIs_admin())
    		return Response.status(403).entity("You are not allowed to do that").build();
    	
    	Tag.delete(names);
        return Response.status(200).entity("Tag(s) deleted").build();
    }
	
	@POST
    @Path( "/rename" )
    @Consumes( MediaType.MULTIPART_FORM_DATA )
    public Response renameTag( FormDataMultiPart form )
    {       
    	String username = form.getField("username").getValueAs(String.class);
    	String oldtag = form.getField("oldtag").getValueAs(String.class);
    	String newtag = form.getField("newtag").getValueAs(String.class);
    	
    	User admin = User.findByName(username);
    	if (admin == null || !admin.isIs_admin())
    		return Response.status(403).entity("You are not allowed to do that").build();
    	
    	if (!Tag.fusion(oldtag, newtag))
    		return Response.status(405).entity("An error occured").build(); // 405 : Method Not Allowed
    	
        return Response.status(200).entity("Tag renamed").build();
    }
}
