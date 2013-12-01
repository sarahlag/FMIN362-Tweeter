package fmin362.resources;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Query;
import fmin362.model.Tweet;
import java.util.List;
import java.util.ArrayList;
/*import javax.naming.InitialContext;
import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;*/
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path( "/tweets" ) // http://localhost:9000/FMIN362-Tweeter/resources/tweets
public class TweetResource
{

    @GET
    @Produces( MediaType.APPLICATION_JSON )
    public List<Tweet> get()
            //throws Exception
    {
        Tweet newtweet = new Tweet( );
            newtweet.setUsername("toto");
            newtweet.setComment("hello world!");
            newtweet.setTags("ready, go");

            Tweet newtweet2 = new Tweet( );
            newtweet2.setUsername("titi");
            newtweet2.setComment("bouh");
            newtweet2.setTags("hey, ho");

            Ebean.save(newtweet);
            Ebean.save(newtweet2);
            
            Query<Tweet> find = Ebean.find(Tweet.class);
        return find.findList();
    }
}
