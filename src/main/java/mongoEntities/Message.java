package mongoEntities;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Reference;
import org.mongodb.morphia.annotations.Text;

import java.util.Date;

/**
 * Created by vkonov on 2/10/16.
 */

@Entity("messages")
public class Message {
    @Id
    private ObjectId id;
    private String authorName;
    @Reference
    private User author;
    @Text
    private String content;
    private String place;
    private Date datePublished;

    public Date getDatePublished() {
        return datePublished;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public void setDatePublished(Date datePublished) {
        this.datePublished = datePublished;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) throws Exception {
        if(content.length() > 140)
            throw new Exception("Message content cannot be more than 140 symbols!");
        if(content.length() < 3)
            throw new Exception("Message content cannot be less than 3 symbols!");

        this.content = content;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }
}
