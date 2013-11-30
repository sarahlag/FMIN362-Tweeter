package fmin362.resources;

import fmin362.model.Tweet;
import java.util.Date;
import java.util.List;
import javax.naming.InitialContext;
import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path( "/tweets" ) // http://localhost:9000/FMIN362-Tweeter/resources/tweets
public class TweetResource
{

    @GET
    @Produces( MediaType.APPLICATION_JSON )
    public List<Tweet> get()
            throws Exception
    {
        UserTransaction utx = null;
        try {

            // Lookup
            InitialContext ic = new InitialContext();
            utx = ( UserTransaction ) ic.lookup( "java:comp/UserTransaction" );
            EntityManager em = ( EntityManager ) ic.lookup( "java:comp/env/persistence/EntityManager" );

            // Transaciton begin
            utx.begin();
            em.joinTransaction();

            Tweet newtweet = new Tweet( );
            newtweet.setUsername("toto");
            newtweet.setComment("hello world!");
            em.persist( newtweet );

            Tweet newtweet2 = new Tweet( );
            newtweet2.setUsername("titi");
            newtweet2.setComment("bouh");
            em.persist( newtweet2 );
            
            List<Tweet> listtweets = em.createQuery( "select m from Tweet m" ).getResultList(); 

            utx.commit();

            return listtweets;

        } catch ( Exception ex ) {

            try {
                if ( utx != null ) {
                    utx.setRollbackOnly();
                }
            } catch ( Exception rollbackEx ) {
                // Impossible d'annuler les changements, vous devriez logguer une erreur,
                // voir envoyer un email à l'exploitant de l'application.
            }
            throw new Exception( ex );

        }

    }
}
