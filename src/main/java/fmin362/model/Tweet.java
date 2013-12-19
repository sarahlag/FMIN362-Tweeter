
package fmin362.model;

import com.avaje.ebean.Ebean;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;

@Entity
public class Tweet implements Serializable{
    private static final long serialVersionUID = 1L;

    /*
	@ManyToMany(cascade=CascadeType.ALL)
	public List<Tag> tags;
	*/
    
    @SequenceGenerator(name="seq_tweet_name", sequenceName="tweet_seq") 
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="seq_tweet_name")
    @Id
    private Long id;
    
    //@Column (nullable=false)
    @ManyToOne
    @JoinColumn(name="USER_ID")
    private User user;
    @Column
    private String tags; // @TODO Add a Tag model
    @Column
    private String comment = "";
    @Column( name = "message_date" )
    @Temporal( javax.persistence.TemporalType.DATE )
    private Date date;
    @Column
    private String photo_url;
    @Column
    private String photo_date;
    @Column
    private String photo_place;

    public Tweet() {
        this.date = new Date();
        this.comment = "";
        this.photo_url = "";
        this.photo_date = "";
        this.photo_place = "";
        this.user = new User();
        this.tags = "";
    }
    
    static public boolean save(Tweet tweet) {
        if (tweet.getUsername() == null || tweet.getUsername().isEmpty())
            return false;
        Ebean.save(tweet);
        return true;
    }
    
    static public boolean update(Tweet tweet) {
        if (tweet.getUsername() == null || tweet.getUsername().isEmpty())
            return false;
        Ebean.update(tweet);
        return true;
    }
    
    static public boolean delete(Tweet tweet) {
        tweet.user.removeTweet(tweet);
        Ebean.update(tweet.user);
        Ebean.delete(tweet);
        return true;
    }
    
    /* ====================
        Getters and Setters
       ==================== */
    
    public Long getId() { // obligée d'avoir getter et setter pour id et date : autrement, erreur : Bean property fmin362.model.Tweet.id/date has  missing readMethod  missing writeMethod
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    public Date getDate() { // obligée d'avoir getter et setter pour id et date : autrement, erreur : Bean property fmin362.model.Tweet.id/date has  missing readMethod  missing writeMethod
        return this.date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
    
    public User getUser() {
        return this.user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }
    
    //////
    
    public String getUsername() {
        return user.getUsername();
    }

    public void setUsername(String username) {
        User user = User.findByName(username);
        if (this.user.equals(user)) // si le nom d'utilisateur ne change pas, on ne fait rien
            return;
        this.user.removeTweet(this); // il faut enlever le tweet à l'utilisateur actuel
        if (user == null) // nouveau user n'existe pas
        {
            user = new User();
            user.setUsername(username);
            Ebean.save(user);
            this.user = user;
        }
        else
            this.user = user;
        user.addTweet(this);
        Ebean.update(this.user);
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getPhoto_url() {
        return photo_url;
    }

    public void setPhoto_url(String photo_url) {
        this.photo_url = photo_url;
    }

    public String getPhoto_date() {
        return photo_date;
    }

    public void setPhoto_date(String photo_date) {
        this.photo_date = photo_date;
    }

    public String getPhoto_place() {
        return photo_place;
    }

    public void setPhoto_place(String photo_place) {
        this.photo_place = photo_place;
    }
    
}
