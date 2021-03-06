package fmin362.models;

import com.avaje.ebean.Ebean;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Version;

import org.codehaus.jackson.annotate.JsonIgnore;

@Entity
@Table(name="app_user") // pour enlever warning - user étant réservé dans sql
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @SequenceGenerator(name="seq_user_name", sequenceName="user_seq") 
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="seq_user_name")
    @Id
    private Long id;
    @Column (unique=true, nullable=false)
    private String username;
    
    @Column
    @JsonIgnore
    private String passwd;
    @Column
    private boolean is_admin;
    
    @OneToMany
    @JsonIgnore
    private List<Tweet> tweets;
    
    @Version
    @JsonIgnore
    public Timestamp lastUpdate;
    
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
    
    static public boolean save(User user) {
        if (user.getUsername() == null || user.getUsername().isEmpty())
            return false;
        Ebean.save(user);
        return true;
    }
    
    static public boolean update(User user) {
        if (user == null || user.getUsername() == null || user.getUsername().isEmpty())
            return false;
        Ebean.update(user);
        return true;
    }
        
    static public void delete(User user) {
    	if (user == null)
    		return;
        user.removeAllTweets();
        Ebean.delete(user);
    }
    
    static public void delete(String names) {
    	String[] users = names.split(",");
    	if (users == null)
    		return;
    	for(int i=0; i<users.length; i++)
    	{
    		users[i] = clearName(users[i]);
    		User user = User.findByName(users[i]);
    		delete(user);
    	}
    }
    
    static public String clearName(String t) // enlève les ' ' en trop
    {
    	if (t==null || t.isEmpty() || t == " ")
    		return ""; 
    	int index=0;
    	for (int i=0; i<t.length() && t.charAt(i)==' '; i++)
    		index++;
    	t = t.substring(index);
    	index=t.length();
    	for (int i=t.length()-1; i>0 && t.charAt(i)==' '; i--)
    		index--;
    	t = t.substring(0, index);
    	return t;
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

    public Timestamp getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(Timestamp lastUpdate) {
		this.lastUpdate = lastUpdate;
	}
}
