package fmin362.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path( "/tweets" ) // http://localhost:9000/FMIN362-Tweeter/resources/tweets
public class TweetResource
{

    @GET
    public String get()
    {
        return "This is the Tweet Resource";
    }

}
