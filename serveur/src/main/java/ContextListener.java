//import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.avaje.ebean.Ebean;

import fmin362.models.User;

public class ContextListener implements ServletContextListener {
	//private ServletContext context = null;

    public void contextInitialized(ServletContextEvent event) {
    	//context = event.getServletContext();
    	//System.out.println("Context path @ "+context.getContextPath());

    	if(Ebean.find(User.class).findRowCount() == 0)
        {
			User user = new User();
        	user.setUsername("admin");
        	//user.setPasswd("admin");
        	user.setIs_admin(true);
        	User.save(user);
        	System.out.println("Default user admin successfully created.");
        }
    }
    
    public void contextDestroyed(ServletContextEvent event) {
    	// contextInitialized et contextDestroyed sont obligatoires
    }
}
