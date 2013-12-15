package fmin362;

import com.sun.jersey.multipart.FormDataMultiPart;
import fmin362.model.Tweet;
import fmin362.resources.TweetResource;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import junit.framework.TestCase;

import org.h2.store.fs.FileUtils;

public class TweetResourceTest extends TestCase {
    
    public void testget() {
        String t = "qjgsjqkgjsqkfjsdlfkqj.jpg";
        int index_ext = t.lastIndexOf('.');
        String ext = t.substring(index_ext, t.length());
        assertEquals(ext, ".jpg");
        
        //TweetResource tr = new TweetResource();
        //assertNotNull(tr.get());
    }
    
}
