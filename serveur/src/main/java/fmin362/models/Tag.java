package fmin362.models;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.SqlUpdate;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

@Entity
public class Tag implements Serializable{
    private static final long serialVersionUID = 1L;
    
    @SequenceGenerator(name="seq_tag_name", sequenceName="tag_seq") 
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="seq_tag_name")
    @Id
    private Long id;
    
    @Column (unique=true, nullable=false)
    private String tagname;

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
        
    static public void delete(Tag tag) {
        if (tag == null)
        	return;
        List<Tweet> tweets = Tweet.findByTagnames(tag.getTagname());
        if (tweets == null)
    		return;
        for (int j=0; j<tweets.size();j++) {
			//tweets.get(j).getTags().remove(tag);
			
					 String s = "delete from tweet_tag where tag_id = :id";
					 SqlUpdate update = Ebean.createSqlUpdate(s);
					 update.setParameter("id", tag.getId());
					 Ebean.execute(update);

			
			//Ebean.refreshMany(tweets.get(j), "tags");
			//Ebean.saveManyToManyAssociations(tweets.get(j), "tags");
			
			//Ebean.deleteManyToManyAssociations(tweets.get(j), "tags");
			//Tweet.update(tweets.get(j));

        }
        Ebean.delete(tag);
        for (int j=0; j<tweets.size();j++){
        	tweets.get(j).getTags().remove(tag);
        	Ebean.refreshMany(tweets.get(j), "tags");
        	//Tweet.update(tweets.get(j));
        }
        System.out.println("TEST1111!!!1 u="+tweets.get(0).getUsername()+" size="+tweets.get(0).getTags().size());
    }
    
    static public void delete(String names) {
    	List<Tweet> tweets = Tweet.findByTagnames(names);
    	if (tweets == null)
    		return;
    	for (int j=0; j<tweets.size();j++) {
			tweets.get(j).removeTags(names);
			Tweet.update(tweets.get(j));
			Ebean.deleteManyToManyAssociations(tweets.get(j), "tags");
        }
        String[] tags = names.split(",");
		if (tags == null || tags[0].equals(""))
			return;
		for(int i=0; i<tags.length;i++) // enlève les tags
		{
			Tag tag = Tag.findByName(tags[i]);
			if (tag == null)
				continue;
			Ebean.delete(tag);
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
}


