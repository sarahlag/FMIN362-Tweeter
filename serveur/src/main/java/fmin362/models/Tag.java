package fmin362.models;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.SqlUpdate;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Version;

@Entity
public class Tag implements Serializable{
    private static final long serialVersionUID = 1L;
    
    @SequenceGenerator(name="seq_tag_name", sequenceName="tag_seq") 
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="seq_tag_name")
    @Id
    private Long id;
    
    @Column (unique=true, nullable=false)
    private String tagname;
    
    @Version
    public Timestamp lastUpdate;

    public Tag() {
        this.tagname = "";
    }
    
    static public Tag findByName(String tagname) {
        String name = clearTag(tagname);
        List<Tag> tags = Ebean.find(Tag.class).findList();
        if (tags == null)
            return null;
        for (int i=0; i<tags.size(); i++)
            if (tags.get(i).getTagname().equals(name))
                return tags.get(i);
        return null;
    }
    
    public boolean nameEquals(String tagname) {
    	String name = clearTag(tagname);
    	return this.tagname.equals(name);
    }
            
    static public void delete(String names) {
    	List<Tweet> tweets = Tweet.findByTagnames(names);
    	if (tweets == null)
    		return;
        String[] tags = names.split(",");
		if (tags == null || tags[0].equals(""))
			return;
		for(int i=0; i<tags.length;i++) // enlève les tags
		{
			Tag tag = Tag.findByName(tags[i]);
			if (tag == null)
				continue;
			SqlUpdate update = Ebean.createSqlUpdate("delete from tweet_tag where tag_id = :id");
			update.setParameter("id", tag.getId());
			Ebean.execute(update);
			Ebean.delete(tag);
			
		}
		for (int j=0; j<tweets.size();j++) {
			tweets.get(j).removeTags(names);
			Ebean.refreshMany(tweets.get(j), "tags");
        }
    }
    
    static public String clearTag(String t) // enlève les ' ' en trop
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
    
    static public boolean save(Tag tag) {
        if (tag == null || tag.getTagname() == null || tag.getTagname().isEmpty())
            return false;
        Ebean.save(tag);
        return true;
    }
    
    static public boolean fusion(String tagOld, String tagNew) {
    	Tag tag1 = findByName(tagOld), tag2 = findByName(tagNew);
    	if (tag1 == tag2 || tag1 == null || tagNew.isEmpty())
			return false;
    	if (tag2 == null) // on renomme
		{
			tag1.setTagname(tagNew);
			Ebean.update(tag1);
		}
    	else
    		return false;
    	/*else // on fusionne
		{
			// faut les listes des tweets
			List<Tweet> tweets = Tweet.findByTagnames(tagOld);
			// aot -> meta
			SqlUpdate update = Ebean.createSqlUpdate("delete from tweet_tag where tag_id = :id");
			update.setParameter("id", tag1.getId());
			Ebean.execute(update);
			Ebean.delete(tag1);
			
			for (int i=0; i<tweets.size(); i++) {
				tweets.get(i).getTags().remove(tag1);
				Ebean.refreshMany(tweets.get(i), "tags");
				
				//tweets.get(i).getTags().add(tag2);
				//tweets.get(i).addTag(tagNew);
				if (tweets.get(i).getTags().contains(tag2))
					continue;
				
				update = Ebean.createSqlUpdate("insert into tweet_tag values (:tweet_id, :tag_id)");
				update.setParameter("tweet_id", tweets.get(i).getId());
				update.setParameter("tag_id", tag2.getId());
				Ebean.execute(update);
				Ebean.refreshMany(tweets.get(i), "tags");
				//Ebean.update(tweets.get(i));
				//Ebean.saveManyToManyAssociations(tweets.get(i), "tags");
			}
		}*/
    	return true;
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

    public String getTagname() {
        return tagname;
    }

    public void setTagname(String tagname) {
	if (tagname == null || tagname.isEmpty())
		return;
	String name = clearTag(tagname);
        this.tagname = clearTag(name);
    }
    
    public Timestamp getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(Timestamp lastUpdate) {
		this.lastUpdate = lastUpdate;
	}
}


