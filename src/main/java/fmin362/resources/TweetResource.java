package fmin362.resources;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Query;
import com.sun.jersey.api.view.Viewable;
import com.sun.jersey.core.header.ContentDisposition;
import fmin362.model.Tweet;
import java.io.IOException;
import java.io.InputStream;
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
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataBodyPart;
import com.sun.jersey.multipart.FormDataMultiPart;
import com.sun.jersey.multipart.FormDataParam;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import org.h2.store.fs.FileUtils;
//import org.apache.commons.io.FileUtils;

@Path( "/tweets" ) // http://localhost:9000/FMIN362-Tweeter/resources/tweets
public class TweetResource
{
    private static final String SERVER_UPLOAD_LOCATION_FOLDER = "/home/dem/public/images/";
    
    @GET
    @Path( "/get" )
    @Produces( MediaType.APPLICATION_JSON )
    public List<Tweet> get()
            //throws Exception
    {           
            Query<Tweet> find = Ebean.find(Tweet.class);
            return find.findList();
    }

    @POST
    @Path("/post_multipart")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response uploadFile(FormDataMultiPart form) {
		 FormDataBodyPart filePart = form.getField("photofile");
                 
		 ContentDisposition headerOfFilePart =  filePart.getContentDisposition();

		 InputStream fileInputStream = filePart.getValueAs(InputStream.class);

		 String filePath = SERVER_UPLOAD_LOCATION_FOLDER + headerOfFilePart.getFileName();

		// save the file to the server
		copyFile(fileInputStream, filePath);
                //FileUtils.copyFile(file, photofile);
                 
                 
		String output = "File "+filePart.getName()+"\nsaved to server location using FormDataMultiPart : " + filePath;

                return Response.status(Response.Status.OK).entity(output).build();
		//return Response.status(200).entity(new Viewable("/wall")).build();

	}

	// save uploaded file to a defined location on the server
	public static void copyFile(InputStream uploadedInputStream, String serverLocation) {

		try {
			OutputStream outpuStream = new FileOutputStream(new File(serverLocation));
			int read = 0;
			byte[] bytes = new byte[1024];

			while ((read = uploadedInputStream.read(bytes)) != -1) {
				outpuStream.write(bytes, 0, read);
			}
                        
			outpuStream.flush();
			outpuStream.close();

			uploadedInputStream.close();
		} catch (IOException e) {

			e.printStackTrace();
		}

	}
    
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
        
        //File photofile = new File(photourl);
        
        
	return Response.status(201).entity(result).build();
    }
    
}
