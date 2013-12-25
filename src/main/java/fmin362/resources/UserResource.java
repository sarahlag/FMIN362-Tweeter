package fmin362.resources;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

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
}
