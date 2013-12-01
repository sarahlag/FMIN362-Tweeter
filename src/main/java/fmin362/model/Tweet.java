
package fmin362.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;

@Entity
public class Tweet implements Serializable{
    private static final long serialVersionUID = 1L;

    /*
	@ManyToMany(cascade=CascadeType.ALL)
	public List<Tag> tags;
	@Required
	@ManyToOne
	@JoinColumn(name="USER_ID")
	public User author;*/
    
    @Id
    @GeneratedValue( strategy = GenerationType.SEQUENCE )
    private Long id;
    
    @Column //(unique=true, nullable=false)
    private String username; // @TODO Add a User model
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
        this.username = "";
        this.tags = "";
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
    
    //////
    
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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
