package fmin362;

import fmin362.model.Tweet;
import fmin362.resources.TweetResource;
import junit.framework.TestCase;

public class TweetResourceTest extends TestCase {
    
    public void testget() {
        TweetResource tr = new TweetResource();
        assertNotNull(tr.get());
    }
}
