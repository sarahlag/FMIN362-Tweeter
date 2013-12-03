package fmin362.resources;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Query;
import fmin362.model.Tweet;
import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
/*import javax.naming.InitialContext;
import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;*/
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path( "/tweets" ) // http://localhost:9000/FMIN362-Tweeter/resources/tweets
public class TweetResource
{

    @GET
    @Path( "/get" )
    @Produces( MediaType.APPLICATION_JSON )
    public List<Tweet> get()
            //throws Exception
    {
            /*
            Tweet newtweet = new Tweet( );
            newtweet.setUsername("toto");
            newtweet.setComment("hello world!");
            newtweet.setTags("ready, go");

            Tweet newtweet2 = new Tweet( );
            newtweet2.setUsername("titi");
            newtweet2.setComment("bouh");
            newtweet2.setTags("hey, ho");

            Ebean.save(newtweet);
            Ebean.save(newtweet2);//*/
            
            Query<Tweet> find = Ebean.find(Tweet.class);
            return find.findList();
    }
    
    /*@POST
    @Path( "/post_json" )
    @Consumes( MediaType.APPLICATION_JSON )
    public Response post_json(Tweet t)
    {
	String result = "Tweet saved : " + t;
	return Response.status(201).entity(result).build();
 
    }*/
    
    @POST
    @Path( "/post_urlencoded" )
    @Consumes( MediaType.APPLICATION_FORM_URLENCODED )
    public Response post_urlencoded(@FormParam("u") String username,
                                @FormParam("c") String comment,
                                @FormParam("url") String photourl,
                                @FormParam("pdate") String photodate,
                                @FormParam("ploc") String photoloc,
                                @FormParam("tags") String tags)
    {
	String result = "Tweet saved\n";
        
        Tweet newtweet = new Tweet( );
        newtweet.setUsername(username);
        newtweet.setComment(comment);
        newtweet.setPhoto_url(photourl);
        newtweet.setPhoto_date(photodate);
        newtweet.setPhoto_place(photoloc);
        newtweet.setTags(tags);
        Ebean.save(newtweet);
	return Response.status(201).entity(result).build();
    }
    
}
