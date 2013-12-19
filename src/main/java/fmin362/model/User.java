package fmin362.model;

import com.avaje.ebean.Ebean;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import org.codehaus.jackson.annotate.JsonIgnore;

@Entity
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @SequenceGenerator(name="seq_user_name", sequenceName="user_seq") 
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="seq_user_name")
    @Id
    private Long id;
    @Column (unique=true, nullable=false)
    private String username;
    
    @Column
    private String passwd;
    @Column
    private boolean is_admin;
    
    @OneToMany
    @JsonIgnore
    private List<Tweet> tweets;
    
    public User() {
        this.username = "";
        this.passwd = "";
        this.is_admin = false;
        this.tweets = new ArrayList<Tweet>();
    }
    
    static public User findByName(String name) {
        List<User> users = Ebean.find(User.class).findList();
        if (users == null)
            return null;
        for (int i=0; i<users.size(); i++)
            if (users.get(i).getUsername().equals(name))
                return users.get(i);
        return null;
    }
    
    public void addTweet(Tweet tweet) {
        this.tweets.add(tweet);
    }
    
    public void removeTweet(Tweet tweet) {
        this.tweets.remove(tweet);
    }
    
    public void removeAllTweets() {
        this.tweets.removeAll(tweets);
        Ebean.update(this);
    }
    
    static public void delete(User user) {
        user.removeAllTweets();
        Ebean.delete(user);
    }
    
    /* ====================
        Getters and Setters
       ==================== */

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Tweet> getTweets() {
        return tweets;
    }

    public void setTweets(List<Tweet> tweets) {
        this.tweets = tweets;
    }
    
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public boolean isIs_admin() {
        return is_admin;
    }

    public void setIs_admin(boolean is_admin) {
        this.is_admin = is_admin;
    }

    
}
