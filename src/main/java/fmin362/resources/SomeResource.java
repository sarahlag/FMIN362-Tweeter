package fmin362.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path( "/some" ) // http://localhost:9000/FMIN362-Tweeter/resources/some
public class SomeResource
{

    @GET
    public String someResource()
    {
        return "Hello World! This is Some Resource";
    }

}
