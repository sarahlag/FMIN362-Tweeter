
package fmin362.it;

import java.net.HttpURLConnection;
import java.net.URL;

import javax.ws.rs.core.MediaType;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.representation.Form;

import junit.framework.TestCase;

public class WebappIT extends TestCase {
    private String baseUrl;
    private Client client;

    public void setUp() throws Exception
    {
        super.setUp();
        String port = System.getProperty("servlet.port");
        this.baseUrl = "http://localhost:" + port + "/FMIN362-Tweeter";
        this.client = Client.create();
    }

    public void testIndexPage() throws Exception
    {
    	Form f = new Form();
    	WebResource webResource;
    	HttpURLConnection connection;
    	URL url;
    	ClientResponse result;
    	
        url = new URL(this.baseUrl);
        connection = (HttpURLConnection) url.openConnection();
        connection.connect();
        assertEquals(200, connection.getResponseCode());
        
        // test login
        url = new URL(this.baseUrl + "/resources/users/login");
        connection = (HttpURLConnection) url.openConnection();
        connection.connect();
        assertEquals(405, connection.getResponseCode()); // erreur car pas d'identifiant
       
        webResource = this.client.resource(url.toString());

        f.add("username", "admin");
        f.add("passwd", "");
        result = webResource.accept(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class, f);
        assertEquals(403, result.getStatus()); // interdit car mauvais mot de passe
        result.close();
        
        f.clear();
        f.add("username", "admin");
        f.add("passwd", "admin");
        result = webResource.accept(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class, f);
        assertEquals(200, result.getStatus()); // ok
        result.close();
        
        // test register
        url = new URL(this.baseUrl + "/resources/users/register");
        webResource = this.client.resource(url.toString());
        
        f.clear();
        f.add("username", "admin");
        f.add("passwd", "admin");
        result = webResource.accept(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class, f);
        assertEquals(403, result.getStatus()); // interdit car admin existe déjà
        result.close();
        
        url = new URL(this.baseUrl + "/resources/users/register");
        f.clear();
        f.add("username", "test");
        f.add("passwd", "test");
        result = webResource.accept(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class, f);
        assertEquals(201, result.getStatus()); // ok
        result.close();
    }
    
    public void testWallPage() throws Exception
    {
    	URL url;
    	HttpURLConnection connection;
    	
        url = new URL(this.baseUrl + "/wall.html");
        connection = (HttpURLConnection) url.openConnection();
        connection.connect();
        assertEquals(200, connection.getResponseCode());
        
        url = new URL(this.baseUrl + "/resources/tweets/get");
        connection = (HttpURLConnection) url.openConnection();
        connection.connect();
        assertEquals(200, connection.getResponseCode());
    }
    
    public void testGetTweet() throws Exception
    {
    	Form f = new Form();
    	WebResource webResource;
    	HttpURLConnection connection;
    	URL url;
    	ClientResponse result;
    	
        url = new URL(this.baseUrl + "/resources/tweets/get");
        connection = (HttpURLConnection) url.openConnection();
        connection.connect();
        assertEquals(200, connection.getResponseCode());
        
        // test post tweet
        url = new URL(this.baseUrl + "/resources/tweets/post");
        webResource = this.client.resource(url.toString());
        
        f.add("c", "comment");
        result = webResource.accept(MediaType.APPLICATION_FORM_URLENCODED).post(ClientResponse.class, f);
        assertEquals(405, result.getStatus()); // interdit form doit au moins contenir username
        result.close();        
    }
}
