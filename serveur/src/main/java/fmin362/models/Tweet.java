
package fmin362.models;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Query;

import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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

import fmin362.models.Tag;
import fmin362.models.Tweet;
import fmin362.resources.TweetResource;

@Entity
public class Tweet implements Serializable{
    private static final long serialVersionUID = 1L;

	@SequenceGenerator(name="seq_tweet_name", sequenceName="tweet_seq") 
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="seq_tweet_name")
    @Id
    private Long id;
    
    @ManyToOne 
    @JoinColumn(name="user_id")
    private User user;
    @ManyToMany (cascade=CascadeType.ALL)
    private List<Tag> tags;
    @Column(name="message")
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
   
    /* ====================
    	Ebean
   		==================== */
    
    static public boolean save(Tweet tweet) throws FileNotFoundException {
        if (tweet.getUsername() == null || tweet.getUsername().isEmpty())
            return false;
        Ebean.save(tweet);
        Ebean.update(tweet.user);
        
        // photo_url -> copyFile
        if (!tweet.getPhoto_url().isEmpty())
        {
        	String real_photourl = TweetResource.uploadFile(tweet.getPhoto_url(), tweet);
        	if (!real_photourl.isEmpty()) 
        	{        
        	    tweet.setPhoto_url(real_photourl);
                Tweet.update(tweet);
        	} 
        }
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
        //qtweet.tags.clear();
        //Ebean.update(tweet);
        Ebean.update(tweet.user);
        Ebean.delete(tweet);
    }
    
    /* ====================
    	User et tags
   		==================== */
        
    ///////// tags
   
    public boolean addTag(String tagname) {
    	tagname = Tag.clearTag(tagname);
        if (tagname == null || tagname.isEmpty())
            return false;
        Tag tag = Tag.findByName(tagname);
        if (this.tags.contains(tag)) // si a déjà ce tag, on ne fait rien
            return false;
        if (tag == null)
        {
            tag = new Tag();
            tag.setTagname(tagname);
            //if (!Tag.save(tag))
            	//return false;
        }
        this.tags.add(tag);
        return true;
    }
    
    public void removeTag(Tag tag) {
    	if (tag == null)
            return;
        this.tags.remove(tag);
    }
    
    public void removeTag(String tagname) {
        Tag tag = Tag.findByName(tagname);
        if (tag == null)
            return;
        this.tags.remove(tag);
    }
    
    public void addTags(String t) {
        if (t == null || t.isEmpty())
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

    ///////// username
    
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
	return true;
    }
    
    /* ====================
    	Recherche
   		==================== */
    
    public static List<Tweet> findBy(String criteria)
    {
    	String tags, users;
    	Query<Tweet> find = Ebean.find(Tweet.class);
    	List<Tweet> tweets = new ArrayList<Tweet>();
    	
    	if (criteria == null || criteria.equals("current") || criteria.isEmpty())
    		return find.findList();
    	
    	if (criteria.contains("users:")){
			int j,i= criteria.indexOf("users:")+6;
			if (criteria.substring(i).contains("+"))
				j = criteria.indexOf("+");
			else
				j = criteria.length();
			users = criteria.substring(i,j);
			List<Tweet> tweetsuser = Tweet.findByUsernames(users);
			tweets = tweetsuser;
		}
    	
    	if (criteria.contains("tags:")){
			int j,i= criteria.indexOf("tags:")+5;
			if (criteria.substring(i).contains("+"))
				j = criteria.indexOf("+");
			else
				j = criteria.length();
			tags = criteria.substring(i,j);

			List<Tweet> tweetstags = Tweet.findByTagnames(tags);
			if (!tweetstags.isEmpty())
			{
				if (tweets.isEmpty()) // il n'y a de contrainte sur user, on peut renvoyer directement findByTags
					tweets = tweetstags;
				else // sinon, il faut trier dans les résultats précédents ...
				{
					Iterator<Tweet> tw = tweets.iterator();
					while (tw.hasNext())
					{
						if (!tw.next().hasAtLeastOneOfTags(tags))
							tw.remove();
					}
				}
			}
    	}
    	return tweets;
    }
    
    public static List<Tweet> findByUsernames(String usernames)
    {
    	List<Tweet> tweets = new ArrayList<Tweet>();
		String[] users = usernames.split(",");
		for(int i=0; i<users.length;i++)
		{
			User user = User.findByName(users[i]); 
			if (user == null)
				continue;
			
			List<Tweet> fromJpaRequest = Ebean.find(Tweet.class).where().in("user", user).findList();
			for(int j=0; j<fromJpaRequest.size(); j++)
				if(!tweets.contains(fromJpaRequest.get(j)))
					tweets.add(fromJpaRequest.get(j));
		}
		return tweets;
    }
    
    public static List<Tweet> findByTagnames(String tagnames)
    {
    	List<Tweet> tweets = new ArrayList<Tweet>();
		String[] tags = tagnames.split(",");
		for(int i=0; i<tags.length;i++)
		{
			Tag tag = Tag.findByName(tags[i]); 
			if (tag == null)
				continue;
			
			List<Tweet> fromJpaRequest = Ebean.find(Tweet.class).where().in("tags", tag).findList();
			for(int j=0; j<fromJpaRequest.size(); j++)
				if(!tweets.contains(fromJpaRequest.get(j)))
					tweets.add(fromJpaRequest.get(j));
		}
		return tweets;
    }
    
    public boolean hasAtLeastOneOfTags(String t)
	{
		String[] tableTags = t.split(",");
		for(int i=0; i<tableTags.length;i++)
		{
			if (tableTags[i].isEmpty())
				continue;
			// on vérifie si tags contient tableTags[i]
			for (int j=0; j<tags.size();j++)
				if (tags.get(j).nameEquals(tableTags[i]))
					return true;
		}
		return false;
	}
    
    /* ====================
		Conversion
	   ==================== */
    
    @SuppressWarnings("unchecked")
	public void fromMap(Map<?, ?> map, String fullpath)
    {
    	setUsername(map.get("username").toString());
    	this.comment = map.get("comment").toString();
    	this.photo_url = fullpath + map.get("photo_url").toString();
    	this.photo_date = map.get("photo_date").toString();
    	this.photo_place = map.get("photo_place").toString();
    	
		List<String> list = (List<String>) map.get("tags");
    	String tags = "";
    	for (int i=0; i<list.size(); i++)
    		tags += list.get(i)+",";
    	System.out.println("[FROM MAP] adding tags:"+tags);
    	addTags(tags);
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
    
    public Date getDate() {
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
