package fmin362;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UploadServlet extends HttpServlet
{
    @Override
    protected void doGet( HttpServletRequest req, HttpServletResponse resp )
            throws ServletException, IOException
    {
        // target/cargo/configurations/glassfish3x/cargo-domain/config/        
        resp.setContentType( "text/html" );
        resp.setCharacterEncoding( "UTF-8" );
        resp.setStatus( 200 );
        PrintWriter out = resp.getWriter();
	out.println(req.getRequestURL()+"</br> <img src='images/hello.jpg' />");
    }
    
    /*
        /upload
    http://localhost:9000/FMIN362-Tweeter/images/hello.jpg // correct
        /upload/jikosdf
    http://localhost:9000/FMIN362-Tweeter/upload/images/hello.jpg // pas bon
    
    */
    
}
