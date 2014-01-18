package fmin362.resources;

import com.avaje.ebean.Ebean;

import fmin362.models.Tweet;
import fmin362.models.User;

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

import javax.ws.rs.DefaultValue;
import javax.ws.rs.QueryParam;
import javax.ws.rs.PathParam;

import com.sun.jersey.multipart.file.DefaultMediaTypePredictor.CommonMediaTypes;

@Path( "/tweets" ) // http://localhost:9000/FMIN362-Tweeter/resources/tweets
public class TweetResource
{
    private static String SERVER_UPLOAD_LOCATION_FOLDER = "";
            
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
    
    @GET
    @Path("/{image}")
    @Produces("image/*") // renvoyer image, ou tweet, selon la requete
    public Response getImage(@PathParam("image") String image) {
    	File f = new File(SERVER_UPLOAD_LOCATION_FOLDER + image);
    	if (!f.exists())
    		return Response.noContent().build();
    	
    	return Response.ok(f, CommonMediaTypes.getMediaTypeFromFile(f)).build();
    }
    
    @POST
    @Path("/post")
    @Consumes( MediaType.MULTIPART_FORM_DATA )
    @Produces( MediaType.APPLICATION_JSON )
    public List<Tweet> post( FormDataMultiPart form ) throws FileNotFoundException
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
        
        // ajout de l'url de l'image
        String photourl = uploadFile(photofile, newtweet);
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
        
        newtweet.setPhoto_url(photourl);
        
        if (!Tweet.save(newtweet))
            return Response.status(405).entity("Tweet not saved\n").build(); // 405 Method Not Allowed

        return Response.status(201).entity("Tweet saved\n").build(); // 201 Resource Created
    }
    
    @POST
    @Path( "/delete" )
    @Consumes( MediaType.MULTIPART_FORM_DATA )
    public Response deleteTweet( FormDataMultiPart form )
    {       
    	String username = form.getField("username").toString();
    	Long id = form.getField("id").getValueAs(Long.class);
    	
    	Tweet tw = Ebean.find(Tweet.class, id);
    	User admin = User.findByName(username);
    	if (tw == null)
    		return Response.status(404).entity("Tweet not found").build();
    	if (!tw.getUsername().equals(username) || admin == null || !admin.isIs_admin() )
    		return Response.status(403).entity("You are not allowed to do that").build();
     	
    	Tweet.delete(tw);
        return Response.status(200).entity("Tweet "+id+" deleted").build();
    }
    
    /*
    
	 //Fusion entre deux tags
	 public static Result fusionTags(){
		 if (request().accepts("application/json")) {
			 Form<String> form = Form.form(String.class).bindFromRequest();
			 String tagOld = form.field("fusionTagOld").value(), tagNew = form.field("fusionTagNew").value();;
			 System.out.println("fusionTag: "+tagOld+"->"+tagNew);
			 boolean r = Tag.fusionTags(tagOld, tagNew);
			 if (r == false)
				 return badRequest("Une erreur est survenue. Vérifier les paramètres.");
			 return redirect(routes.Application.listTweetsBy()); 
		 }
		 return badRequest();
	 }
    //Modification d'un tweet
	public static Result modifyTweet() throws IOException
	{		
		Form<Tweet> form = Form.form(Tweet.class).bindFromRequest();
		MultipartFormData body = request().body().asMultipartFormData();
		
		Tweet tweet = Tweet.find.byId(Long.parseLong(form.field("id").value()));

		String username, comment, photodate, photolocation, tags;
		username = form.field("username").value();
		comment = form.field("comment").value();
		photodate = form.field("photodate").value();
		photolocation = form.field("photolocation").value();
		tags = form.field("tags").value();
		
		FilePart picture = body.getFile("photo");
		
		if (!username.isEmpty() && !username.equals(tweet.author.userName)) //ok
		{
			System.out.println("Username "+tweet.author.userName+" modified to "+username);
			tweet.changeUser(username);
		}
		if (!comment.isEmpty() && !comment.equals(tweet.commentaire)) //ok
		{
			System.out.println("Comment "+tweet.commentaire+" modified to "+comment);
			tweet.commentaire = comment;
		}

		if (picture != null) //ok
		{
			File file = picture.getFile();
			String filename = tweet.id + "-" + tweet.creationDate + "-"
					+ picture.getFilename();
			System.out.println("Photourl "+tweet.photoURL+" modified to "+filename);
			File photofile = new File("public/images/" + filename);
			FileUtils.copyFile(file, photofile);
			tweet.photoURL = filename;
		}
		
		if (!photodate.isEmpty() && !photodate.equals(tweet.photoDate)) //ok
		{
			System.out.println("photodate "+tweet.photoDate+" modified to "+photodate);
			tweet.photoDate = photodate;
		}
				
		if (!photolocation.isEmpty() && !photolocation.equals(tweet.photoAdresse)) //ok
		{
			System.out.println("photolocation "+tweet.photoAdresse+" modified to "+photolocation);
			tweet.photoAdresse = photolocation;
		}
		
		if (tags != null && !tweet.printTags().equals(tags)) //ok
		{
			System.out.println("Tags "+tweet.printTags()+" modified to "+tags);
			tweet.clearTags();
			tweet.addTags(tags);
		}
		Ebean.update(tweet);
		
		return redirect(routes.Application.listTweetsBy());
	}*/
    
    /* ================ */
    /* UPLOAD           */
    /* ================ */
    
    public String uploadFile(FormDataBodyPart filePart, Tweet tw)
    {
        if (filePart == null)
            return "";
        String filename = tw.getId()+"-"+tw.getDate().toString().replaceAll(" ", "_").replaceAll(":", "-");
        String realFilename = filePart.getContentDisposition().getFileName();
        if (realFilename == null || realFilename.isEmpty())
            return "";
        InputStream fileInputStream = filePart.getValueAs(InputStream.class);
        String name = filename + getExt(realFilename);
        copyFile(fileInputStream, SERVER_UPLOAD_LOCATION_FOLDER + name);
        return name;
    }
    
    public static String uploadFile(String realFilename, Tweet tw) throws FileNotFoundException
    {
        if (realFilename == null || realFilename.isEmpty())
            return "";
        String filename = tw.getId()+"-"+tw.getDate().toString().replaceAll(" ", "_").replaceAll(":", "-");
        InputStream fileInputStream = new FileInputStream(realFilename); // throws FileNotFoundException si n'existe pas
        String name = filename + getExt(realFilename);
        copyFile(fileInputStream,  SERVER_UPLOAD_LOCATION_FOLDER + name);
        return name;
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
    
    private static String getExt(String filename)
    {
        if (filename == null || filename.equals(""))
            return "";
        int index_ext = filename.lastIndexOf('.');
        if (index_ext >= 0)
            return filename.substring(index_ext, filename.length());
        return "";
    }
    
    public static void setUploadPath(String folder)
    {	
    	// fonction appelée 1 seule fois, dans ContextListener.contextInitialized()
    	if (!SERVER_UPLOAD_LOCATION_FOLDER.isEmpty())
    		return;
    	
        // vérifie si répertoire upload existe, le crée sinon
        File dir = new File(folder + "upload/");
        if (!dir.exists())
        	dir.mkdir();
        
        System.out.println("server upload folder @ "+ folder + "upload/");
        SERVER_UPLOAD_LOCATION_FOLDER = folder + "upload/"; // [path]/[project dir]/upload/
    }
    
}
