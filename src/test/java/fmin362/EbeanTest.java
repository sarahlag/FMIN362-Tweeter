

package fmin362;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Query;
import com.avaje.ebean.SqlRow;
import fmin362.model.Tweet;
import fmin362.model.User;
import fmin362.model.Tag;
import java.util.List;
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
    
    public void testQuery() {
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

    public void testDeleteTweet() { // teste si objet user est bien mis à jour si tweet supprimé
	Tweet e = new Tweet();
	e.setUsername("test");
	Tweet.save(e);

	User u = e.getUser();

	System.out.println("[EBEAN DELETE TWEET-USER RESULT] "+e.getUsername()+","+u.getTweets().get(0).getId());

	Tweet.delete(e);
	assertTrue(u.getTweets().isEmpty());

        User.delete(u);
    }

    public void testTags() { // teste si tags bien ajoutés / supprimés
	Tweet e = new Tweet();
	e.setUsername("test");
	Tweet.save(e);

	User u = e.getUser();

	e.addTags(" tag1,  tag2,, ");
	Tag tag1 = e.getTags().get(0);
	Tag tag2 = e.getTags().get(1);

	//Tweet.save(e);

	System.out.println("[EBEAN TAG TEST RESULT] "+e.getTags().size());
	System.out.println("[EBEAN TAG TEST RESULT]Got '"+e.getTags().get(0).getTagname()+"'");
	System.out.println("[EBEAN TAG TEST RESULT]Got '"+e.getTags().get(1).getTagname()+"'");
	
	Tweet.delete(e);
        User.delete(u);
	
	String sql = "select tag_id from tweet_tag";
	List<SqlRow> rows = Ebean.createSqlQuery(sql).findList();

	assertTrue(rows.size() == 0);

	Ebean.delete(tag1);
	Ebean.delete(tag2);
    }

}
