package fmin362.model;

import com.avaje.ebean.Ebean;
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
    
    static private String clearTag(String t) // enlève les ' ' en trop
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


