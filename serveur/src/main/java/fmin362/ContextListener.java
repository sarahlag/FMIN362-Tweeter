
package fmin362;

import javax.servlet.ServletContext;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Map;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.avaje.ebean.Ebean;

import org.yaml.snakeyaml.Yaml;

import fmin362.models.Tweet;
import fmin362.models.User;
import fmin362.resources.TweetResource;

public class ContextListener implements ServletContextListener {
	private ServletContext context = null;
	private static String SERVER_ROOT_FOLDER;
		
    public void contextInitialized(ServletContextEvent event) {
    	context = event.getServletContext();
    	SERVER_ROOT_FOLDER = getServerRootPath();
    	TweetResource.setUploadPath(SERVER_ROOT_FOLDER);
    	System.out.println("server root folder @ "+SERVER_ROOT_FOLDER); 
    	
    	// si la base de donnée est vide
    	if (Ebean.find(Tweet.class).findRowCount() == 0 && new File(SERVER_ROOT_FOLDER + "initbdd.yml").exists())
    	{
    		// création de l'admin
    		User user = new User();
        	user.setUsername("admin");
        	//user.setPasswd("admin");
        	user.setIs_admin(true);
        	User.save(user);
        	System.out.println("Default user admin successfully created.");
        	
        	// ajout de tweets
    		Yaml yaml = new Yaml();
    		try {
    			InputStream in = new FileInputStream(SERVER_ROOT_FOLDER + "initbdd.yml");			
    		    for (Object data : yaml.loadAll(in)) {
    		    	Tweet tw = new Tweet();
    		    	tw.fromMap((Map<?, ?>) data, SERVER_ROOT_FOLDER);    		    	
    		    	Tweet.save(tw);
    		    }			
    		} catch (FileNotFoundException e) {
    			e.printStackTrace();
    		}
    	}
    }
    
    public void contextDestroyed(ServletContextEvent event) {
    	// contextInitialized et contextDestroyed sont obligatoires
    }
    
    private String getServerRootPath()
	{   
    	// alt.
    	/*ClassLoader cl = ClassLoader.getSystemClassLoader();
        URL[] urls = ((URLClassLoader)cl).getURLs();
        if (urls == null || urls.length < 1)
        	SERVER_UPLOAD_LOCATION_FOLDER = "./";
        String folder = urls[0].toString();*/
    	
    	//[path]/target/cargo/configurations/glassfish3x/cargo-domain/...:...
    	String folder = (String) context.getAttribute("org.apache.catalina.jsp_classpath");
        if (folder.startsWith("file:"))
            folder = folder.substring(5);
        int i = folder.indexOf("target");
        folder = folder.substring(0, i);
		return folder;
	}	
}

