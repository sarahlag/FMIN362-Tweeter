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
        TweetResource tr = new TweetResource();
        assertNotNull(tr.get());
    }
    
    public void testCopyFile() throws IOException {              
        Date date = new Date();
        InputStream uploadedInputStream = FileUtils.newInputStream("~/test.jpg");
        TweetResource.copyFile(uploadedInputStream, "public/images/"+date+"test.jpg");
        
        assertTrue(FileUtils.exists("public/images/"+date+"test.jpg"));
    }

    
}
