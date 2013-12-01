
package fmin362.it;

import java.net.HttpURLConnection;
import java.net.URL;
import static junit.framework.Assert.assertEquals;
import junit.framework.TestCase;

public class WebappIT extends TestCase {
    private String baseUrl;

    public void setUp() throws Exception
    {
        super.setUp();
        String port = System.getProperty("servlet.port");
        this.baseUrl = "http://localhost:" + port + "/FMIN362-Tweeter";
    }

    public void testCallIndexPage() throws Exception
    {
        URL url = new URL(this.baseUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.connect();
        assertEquals(200, connection.getResponseCode());
    }
}
