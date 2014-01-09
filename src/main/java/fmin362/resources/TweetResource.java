package fmin362.resources;

import com.avaje.ebean.Ebean;

import fmin362.models.Tweet;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.sun.jersey.multipart.FormDataBodyPart;
import com.sun.jersey.multipart.FormDataMultiPart;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.QueryParam;
import javax.ws.rs.PathParam;

@Path( "/tweets" ) // http://localhost:9000/FMIN362-Tweeter/resources/tweets
public class TweetResource
{
    private final String SERVER_UPLOAD_LOCATION_FOLDER = getUploadPath();
        
    @GET
    @Path("{id}")
    @Produces( MediaType.APPLICATION_OCTET_STREAM )
    public Response find(@PathParam("id") Long id) throws URISyntaxException 
    {
    	Tweet t = Ebean.find(Tweet.class, id);
    	URI uri = new URI("file://"+t.getPhoto_url());
        return Response.ok().contentLocation(uri).build();
    }

    @GET
    @Path( "/get" )
    @Produces( MediaType.APPLICATION_JSON )
    public List<Tweet> get(	@DefaultValue("1") 	@QueryParam("p") int page,
    						@DefaultValue("0")	@QueryParam("by") int nbAffichage, 
    						@DefaultValue("current") @QueryParam("c") String criteria )
    {           
        List<Tweet> list = Tweet.findBy(criteria);
        if (nbAffichage==0)
    		return list;
        
        int step = (page-1)*nbAffichage;
        int limit = page*nbAffichage;
        if (step >= list.size())
        	step = list.size()-1;
        if (limit >= list.size())
        	limit = list.size()-1;
        
        return list.subList(step, limit);
    }

    @POST
    @Path("/post")
    @Consumes( MediaType.MULTIPART_FORM_DATA )
    @Produces( MediaType.APPLICATION_JSON )
    public List<Tweet> post( FormDataMultiPart form )
    { 
        FormDataBodyPart photofile = form.getField("photofile");
        FormDataBodyPart username = form.getField("username");
        FormDataBodyPart comment = form.getField("comment");
        FormDataBodyPart photodate = form.getField("photodate");
        FormDataBodyPart photoloc = form.getField("photoloc");
        FormDataBodyPart tags = form.getField("tags");

        Tweet newtweet = new Tweet();
        if (username != null)
            newtweet.setUsername(username.getValueAs(String.class));
        if (comment != null)
            newtweet.setComment(comment.getValueAs(String.class));
        if (photodate != null)
            newtweet.setPhoto_date(photodate.getValueAs(String.class));
        if (photoloc != null)
            newtweet.setPhoto_place(photoloc.getValueAs(String.class));
        if (tags != null)
            newtweet.addTags(tags.getValueAs(String.class));
        
        if (!Tweet.save(newtweet))
            return Ebean.find(Tweet.class).findList();
        
        String photourl = uploadFile(photofile, newtweet.getId()+"-"+newtweet.getDate().toString().replaceAll(" ", "_").replaceAll(":", "-"));
        if (photourl.isEmpty())
            return Ebean.find(Tweet.class).findList();
        
        newtweet.setPhoto_url(photourl);
        Tweet.update(newtweet);
          
	return Ebean.find(Tweet.class).findList();     
    }
       
    @POST
    @Path( "/post" )
    @Consumes( MediaType.APPLICATION_FORM_URLENCODED )
    public Response post(	@FormParam("u") String username,
                                @FormParam("p") String passwd,
                                @FormParam("c") String comment,
                                @FormParam("url") String photourl,
                                @FormParam("pdate") String photodate,
                                @FormParam("ploc") String photoloc,
                                @FormParam("tags") String tags) throws FileNotFoundException
    {       
        Tweet newtweet = new Tweet();
        newtweet.setUsername(username);
        newtweet.setComment(comment);
        newtweet.setPhoto_date(photodate);
        newtweet.setPhoto_place(photoloc);
        newtweet.addTags(tags);
        
        if (!Tweet.save(newtweet))
            return Response.status(405).entity("Tweet not saved\n").build(); // 405 Method Not Allowed
                
        String real_photourl = uploadFile(photourl, newtweet.getId()+"-"+newtweet.getDate().toString().replaceAll(" ", "_").replaceAll(":", "-"));
	if (!real_photourl.isEmpty()) 
	{        
	    newtweet.setPhoto_url(real_photourl);
            Tweet.update(newtweet);
	}        
	return Response.status(201).entity("Tweet saved\n").build(); // 201 Resource Created
    }
    
    /* ================ */
    /* UPLOAD           */
    /* ================ */
    
    private String uploadFile(FormDataBodyPart filePart, String filename)
    {
        if (filePart == null)
            return "";
        String realFilename = filePart.getContentDisposition().getFileName();
        if (realFilename == null || realFilename.isEmpty())
            return "";
        InputStream fileInputStream = filePart.getValueAs(InputStream.class);
        String fullname = SERVER_UPLOAD_LOCATION_FOLDER + filename + getExt(realFilename);
        copyFile(fileInputStream, fullname);
        return fullname;
    }
    
    private String uploadFile(String realFilename, String filename) throws FileNotFoundException
    {
        if (realFilename == null || realFilename.isEmpty())
            return "";
        InputStream fileInputStream = new FileInputStream(realFilename); // throws FileNotFoundException si n'existe pas
        String fullname = SERVER_UPLOAD_LOCATION_FOLDER + filename + getExt(realFilename);
        copyFile(fileInputStream,  fullname);
        return fullname;
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
        if (filename == null || filename.equals(""))
            return "";
        int index_ext = filename.lastIndexOf('.');
        if (index_ext >= 0)
            return filename.substring(index_ext, filename.length());
        return "";
    }
    
    private String getUploadPath()
    {
        ClassLoader cl = ClassLoader.getSystemClassLoader();
                URL[] urls = ((URLClassLoader)cl).getURLs();
        if (urls == null || urls.length < 1)
            return "./";
        String folder = urls[0].toString(); // [path]/[project dir]/target/cargo/installs/glassfish-3.1.2.2/glassfish3/glassfish/modules/glassfish.jar 
        if (folder.startsWith("file:"))
            folder = folder.substring(5);
        int i = folder.indexOf("target");
        folder = folder.substring(0, i);
        
        return folder + "upload/"; // [path]/[project dir]/upload/
    }
}
