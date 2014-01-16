import java.util.Enumeration;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import fmin362.models.User;

public class ContextListener implements ServletContextListener {
	private ServletContext context = null;

    public void contextInitialized(ServletContextEvent event) {
    	/*String attr;
        context = event.getServletContext();
        System.out.println("Context is important, folks. Here are the attributes:");
        Enumeration<?> attributes = context.getAttributeNames();
        while(attributes.hasMoreElements())
        {
        	attr = attributes.nextElement().toString();
            System.out.println("**"+attr+"**"+context.getAttribute(attr));
        }       
        System.out.println("Context path @ "+context.getContextPath());*/
        
        User user = User.findByName("admin");
        if (user == null)
        {
        	user = new User();
        	user.setUsername("admin");
        	//user.setPasswd("admin");
        	user.setIs_admin(true);
        	User.save(user);
        	System.out.println("Default user admin successfully created.");
        }
    }
    public void contextDestroyed(ServletContextEvent event) {
    }
}
