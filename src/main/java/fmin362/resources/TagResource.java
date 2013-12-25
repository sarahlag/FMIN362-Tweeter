package fmin362.resources;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Query;

import fmin362.models.Tag;

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
}
