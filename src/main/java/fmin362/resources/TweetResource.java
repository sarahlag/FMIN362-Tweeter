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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Map;
import org.h2.store.fs.FileUtils;
//import org.apache.commons.io.FileUtils;

@Path( "/tweets" ) // http://localhost:9000/FMIN362-Tweeter/resources/tweets
public class TweetResource
{
    private final String SERVER_UPLOAD_LOCATION_FOLDER = getUploadPath();
        
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
    @Path("/post")
    @Consumes( MediaType.MULTIPART_FORM_DATA )
    public Response post(FormDataMultiPart form) {
        FormDataBodyPart filePart = form.getField("photofile");
        FormDataBodyPart username = form.getField("username");
        FormDataBodyPart comment = form.getField("comment");
        FormDataBodyPart photodate = form.getField("photodate");
        FormDataBodyPart photoloc = form.getField("photoloc");
        FormDataBodyPart tags = form.getField("tags");

        Tweet newtweet = new Tweet();
        newtweet.setUsername(username.getValueAs(String.class));
        newtweet.setComment(comment.getValueAs(String.class));
        newtweet.setPhoto_date(photodate.getValueAs(String.class));
        newtweet.setPhoto_place(photoloc.getValueAs(String.class));
        newtweet.setTags(tags.getValueAs(String.class));
        
        Ebean.save(newtweet);
        String photourl = uploadFile(filePart, newtweet.getId()+"-"+newtweet.getDate().toString().replaceAll(" ", "_").replaceAll(":", "-"));
        newtweet.setPhoto_url(photourl);
        Ebean.update(newtweet);
        
        String result = "Tweet saved";
        return Response.status(201).entity(result).build();
    }
       
    @POST
    @Path( "/post" )
    @Consumes( MediaType.APPLICATION_FORM_URLENCODED )
    public Response post(@FormParam("u") String username,
                                @FormParam("c") String comment,
                                @FormParam("url") String photourl,
                                @FormParam("pdate") String photodate,
                                @FormParam("ploc") String photoloc,
                                @FormParam("tags") String tags) throws FileNotFoundException
    {
	String result = "Tweet saved\n";
        
        Tweet newtweet = new Tweet();
        newtweet.setUsername(username);
        newtweet.setComment(comment);
        newtweet.setPhoto_date(photodate);
        newtweet.setPhoto_place(photoloc);
        newtweet.setTags(tags);
        Ebean.save(newtweet);
        
        String real_photourl = uploadFile(photourl, newtweet.getId()+"-"+newtweet.getDate().toString().replaceAll(" ", "_").replaceAll(":", "-"));
        newtweet.setPhoto_url(real_photourl);
        Ebean.update(newtweet);
        
	return Response.status(201).entity(result).build();
    }
    
    /* ================ */
    /* UPLOAD           */
    /* ================ */
    
    private String uploadFile(FormDataBodyPart filePart, String filename)
    {
        String realFilename = filePart.getContentDisposition().getFileName();
        InputStream fileInputStream = filePart.getValueAs(InputStream.class);
        String filePath = SERVER_UPLOAD_LOCATION_FOLDER + filename + getExt(realFilename);
        copyFile(fileInputStream, filePath);
        return filePath;
    }
    
    private String uploadFile(String realFilename, String filename) throws FileNotFoundException
    {
        InputStream fileInputStream = new FileInputStream(realFilename);
        String filePath = SERVER_UPLOAD_LOCATION_FOLDER + filename + getExt(realFilename);
        copyFile(fileInputStream, filePath);
        return filePath;
    }

    private static void copyFile(InputStream uploadedInputStream, String serverLocation) {
        try {
		OutputStream outputStream = new FileOutputStream(new File(serverLocation));
		int read = 0;
		byte[] bytes = new byte[1024];

		while ((read = uploadedInputStream.read(bytes)) != -1) {
			outputStream.write(bytes, 0, read);
		}
                        
		outputStream.flush();
		outputStream.close();

		uploadedInputStream.close();
	} catch (IOException e) {
		e.printStackTrace();
	}

    }
    
    private String getExt(String filename)
    {
        int index_ext = filename.lastIndexOf('.');
        return filename.substring(index_ext, filename.length());
    }
    
    private String getUploadPath()
    {
        ClassLoader cl = ClassLoader.getSystemClassLoader();
                URL[] urls = ((URLClassLoader)cl).getURLs();
        if (urls == null || urls.length < 1)
            return "./";
        String folder = urls[0].toString(); // [path]/FMIN362-Tweeter/target/cargo/installs/glassfish-3.1.2.2/glassfish3/glassfish/modules/glassfish.jar 
        if (folder.startsWith("file:"))
            folder = folder.substring(5);
        int i = folder.indexOf("target");
        folder = folder.substring(0, i);
        
        return folder + "upload/";
    }
    
}
