package fmin362;

import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerResponse;
import com.sun.jersey.spi.container.ContainerResponseFilter;

/**
 *
 * @author slaghribi
 */
public class CORSFilter implements ContainerResponseFilter
{
  private static final String ORIGINHEADER = "Origin";
  private static final String ACAOHEADER = "Access-Control-Allow-Origin";
  private static final String ACRHHEADER = "Access-Control-Request-Headers";
  private static final String ACAHHEADER = "Access-Control-Allow-Headers";

  public CORSFilter()
  {
  }

  @Override
  public ContainerResponse filter(final ContainerRequest request, final ContainerResponse response)
  {
      
      System.out.println("filterrrrrrrrrrr");
      
    final String requestOrigin = "*"; //request.getHeaderValue(ORIGINHEADER);
    response.getHttpHeaders().add(ACAOHEADER, requestOrigin);

    final String requestHeaders = "*"; //request.getHeaderValue(ACRHHEADER);
    response.getHttpHeaders().add(ACAHHEADER, requestHeaders);
    
    return response;
  }
}
