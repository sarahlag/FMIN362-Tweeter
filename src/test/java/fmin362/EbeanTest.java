

package fmin362;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.SqlRow;
import fmin362.model.Tweet;
import junit.framework.TestCase;

public class EbeanTest extends TestCase{
    // http://www.avaje.org/ebean/getstarted_props.html
    public void testUseEbean() {
		
		String sql = "select count(*) as count from dual";
		SqlRow row = 
			Ebean.createSqlQuery(sql)
			.findUnique();
		
		Integer i = row.getInteger("count");
		
                assertNotNull(row);
                System.out.println("[EBEAN TEST RESULT]Got "+i+"  - DataSource good.");
	}
    
    public void testQuery() {
        Tweet e = new Tweet();
	e.setUsername("test");
	e.setComment("something");
		
		// will insert
	Ebean.save(e);
	e.setComment("changed");
		
		// this will update
	Ebean.update(e);
		
		// find the inserted entity by its id
	Tweet e2 = Ebean.find(Tweet.class, e.getId());
        
        assertEquals(e2.getComment(), "changed");
	System.out.println("[EBEAN TEST RESULT]Got "+e2.getComment());
		
	Ebean.delete(e);
		// can use delete by id when you don't have the bean
		//Ebean.delete(ESimple.class, e.getId());
    }
}
