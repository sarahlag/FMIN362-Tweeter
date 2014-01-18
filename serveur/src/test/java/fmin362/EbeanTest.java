

package fmin362;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.SqlRow;
import com.avaje.ebean.SqlUpdate;

import fmin362.models.Tag;
import fmin362.models.Tweet;
import fmin362.models.User;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;

import junit.framework.TestCase;

public class EbeanTest extends TestCase{
		
    // http://www.avaje.org/ebean/getstarted_props.html
    public void testUseEbean() {
		
		String sql = "select count(*) as count from dual";
		SqlRow row = Ebean.createSqlQuery(sql).findUnique();
		
		Integer i = row.getInteger("count");
		
                assertNotNull(row);
                System.out.println("[EBEAN TEST RESULT]Got "+i+"  - DataSource good.");
	}
    
    public void testQuery() throws FileNotFoundException {
        Tweet e = new Tweet();
        
	e.setUsername("test");
	e.setComment("something");
        
        User u = e.getUser();
		
		// will insert
	Tweet.save(e);
	e.setComment("changed");
		
		// this will update
	Tweet.update(e);
		
		// find the inserted entity by its id
	Tweet e2 = Ebean.find(Tweet.class, e.getId());
                
        assertEquals(e2.getComment(), "changed");
	System.out.println("[EBEAN TEST RESULT]Got "+e2.getComment());
        
        List<Tweet> find = Ebean.find(Tweet.class).findList();
        for (int i=0; i<find.size(); i++)
            System.out.println("c="+find.get(i).getComment());
		
	Tweet.delete(e);
        User.delete(u);
        
		// can use delete by id when you don't have the bean
		//Ebean.delete(ESimple.class, e.getId());
    }

    public void testDeleteTweet() throws FileNotFoundException { // teste si objet user est bien mis à jour si tweet supprimé
	Tweet e = new Tweet();
	e.setUsername("test");
	Tweet.save(e);

	User u = e.getUser();

	System.out.println("[EBEAN DELETE TWEET-USER RESULT] "+e.getUsername()+","+u.getTweets().get(0).getId());

	Tweet.delete(e);
	assertTrue(u.getTweets().isEmpty());

        User.delete(u);
    }

    public void testTags() throws FileNotFoundException { // teste si tags bien ajoutés / supprimés
	Tweet e = new Tweet();
	e.setUsername("test");

	e.addTags(" tag1, tag2,, ");

	Tag tag2 = e.getTags().get(1);

	Tweet.save(e);

	assertTrue(e.getTags().size() == 2);
	
	System.out.println("[EBEAN TAG TEST RESULT] "+e.getId()+", "+e.getTags().size());
	for (int i=0; i<e.getTags().size(); i++)
		System.out.println("[EBEAN TAG TEST RESULT]Got '"+e.getTags().get(i).getTagname()+"'");
	//System.out.println("[EBEAN TAG TEST RESULT]Got '"+e.getTags().get(1).getTagname()+"'");
	
	//Tweet.delete(e);
    //User.delete(u);
	
	// on enlève un tag
	
	// ces lignes MARCHENT PAS GRRR si je les met dans fonction Tag.delete(tag) ... en test
	 String s = "delete from tweet_tag where tag_id = :id";
	 SqlUpdate update = Ebean.createSqlUpdate(s);
	 update.setParameter("id", tag2.getId());
	 Ebean.execute(update);
    Ebean.delete(tag2);
    e.getTags().remove(tag2);
    Ebean.refreshMany(e, "tags");
    
	System.out.println("[EBEAN TAG TEST RESULT]After Del Tag, got "+e.getTags().size()+" tags");
	for (int i=0; i<e.getTags().size(); i++)
		System.out.println("[EBEAN TAG TEST RESULT]Got '"+e.getTags().get(i).getTagname()+"'");
	
	assertTrue(e.getTags().size() == 1);
	
    }
    
    public void testPersist()
    {
    	Tweet e = Ebean.find(Tweet.class, 3);
    	User u = e.getUser();
    	Tag tag1 = e.getTags().get(0);
    	
    	System.out.println("[EBEAN PERSIST] got "+e.getTags().size()+" tags");
    	for (int i=0; i<e.getTags().size(); i++)
    		System.out.println("[EBEAN PERSIST]Got '"+e.getTags().get(i).getTagname()+"'");
    	
    	assertTrue(e.getTags().size() == 1);
    	
    	Tweet.delete(e);
        User.delete(u);
    	Ebean.delete(tag1);
    }
    
    public void testYaml() throws IOException
    {
   	  	Yaml yaml = new Yaml();
		try {
			InputStream in = new FileInputStream("initbdd.yml");			
			List<Tweet> list = new ArrayList<Tweet>();
		    for (Object data : yaml.loadAll(in)) {
		    	Tweet tw = new Tweet();
		    	tw.fromMap((Map<?, ?>) data, "");
		    	list.add(tw);
		    }
		    
			assertTrue(list.size() > 0 );
			System.out.println("[YAML TEST] got "+list.get(0).getComment());
		
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		};
    }
}



 