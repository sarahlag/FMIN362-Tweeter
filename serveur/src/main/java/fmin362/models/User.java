package fmin362.models;

import com.avaje.ebean.Ebean;

import java.io.Serializable;
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

    /*public static boolean deleteUsers(String t)
	{		
		if (t == null)
			return false;
		String[] userNames = t.split(",");
		if (userNames == null || userNames[0].equals(""))
			return false;
		for(int i=0; i<userNames.length;i++)
		{
			User user = User.findByUserName(userNames[i]);
			if (user == null)
				continue;
			for (int j=0; j<user.tweets.size();j++)
				Tweet.deleteTweet(user.tweets.get(j).id);
			find.ref(user.id).delete();	
			System.out.println("User "+userNames[i]+" a été correctement supprimé.");
		}
		return true;
	}*/
    
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
