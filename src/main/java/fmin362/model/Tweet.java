
package fmin362.model;

import com.avaje.ebean.Ebean;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;

@Entity
public class Tweet implements Serializable{
    private static final long serialVersionUID = 1L;
    
    @SequenceGenerator(name="seq_tweet_name", sequenceName="tweet_seq") 
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="seq_tweet_name")
    @Id
    private Long id;
    
    @ManyToOne
    @JoinColumn(name="USER_ID")
    private User user;
    //@Column
    @ManyToMany///(cascade=CascadeType.ALL)
    private List<Tag> tags;
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
        this.tags = new ArrayList<Tag>();
    }
   
    static public boolean save(Tweet tweet) {
        if (tweet.getUsername() == null || tweet.getUsername().isEmpty())
            return false;
        Ebean.save(tweet);
        return true;
    }
    
    static public boolean update(Tweet tweet) {
        if (tweet == null || tweet.getUsername() == null || tweet.getUsername().isEmpty())
            return false;
        Ebean.update(tweet);
        return true;
    }
    
    static public void delete(Tweet tweet) {
        if (tweet == null)
            return;
        tweet.user.removeTweet(tweet);
        Ebean.update(tweet.user);
        //tweet.tags.clear();
        //Ebean.update(tweet);
        Ebean.delete(tweet);
    }
        
    /* ====================
        Getters and Setters
       ==================== */
   
    public void addTag(String tagname) {
        if (tagname == null || tagname.isEmpty())
            return;
        Tag tag = Tag.findByName(tagname);
        if (this.getTags().contains(tag)) // si a déjà ce tag, on ne fait rien
            return;
        if (tag == null)
        {
            tag = new Tag();
            tag.setTagname(tagname);
            Ebean.save(tag);
        }
        this.getTags().add(tag);
    }
    
    public void removeTag(String tagname) {
        Tag tag = Tag.findByName(tagname);
        if (tag == null)
            return;
        this.tags.remove(tag);
    }
    
    public void addTags(String t) {
        if (t == null)
            return;
        String[] tableTags = t.split(",");
        for(int i=0; i<tableTags.length;i++)
            addTag(tableTags[i]);
    }
       
    public void removeTags(String t) {
        if (t == null)
            return;
        String[] tableTags = t.split(",");
        for(int i=0; i<tableTags.length;i++)
            removeTag (tableTags[i]);

    }
    
    public String getUsername() {
        return user.getUsername();
    }

    public boolean setUsername(String username) {
        User user = User.findByName(username);
        if (this.user.equals(user)) // si le nom d'utilisateur ne change pas, on ne fait rien
            return false;
        this.user.removeTweet(this); // il faut enlever le tweet à l'utilisateur actuel
        if (user == null) // nouveau user n'existe pas
        {
            user = new User();
            user.setUsername(username);
            if (!User.save(user))
		return false;
            this.user = user;
        }
        else
            this.user = user;
        user.addTweet(this);
        User.update(this.user);
	return true;
    }
    
    //////
    
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
    
    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }
    
    //////
    
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
