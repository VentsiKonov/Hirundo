package mongoEntities;

import org.bson.types.ObjectId;
import org.eclipse.jetty.util.security.Credential;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Indexed;
import org.mongodb.morphia.annotations.Reference;

import java.util.Date;
import java.util.List;

/**
 * Created by vkonov on 2/9/16.
 */

@Entity("users")
public class User {
    public ObjectId getId() {
        return id;
    }

    @Id
    private ObjectId id;
    @Indexed
    private String username;
    private String email;
    private String passwordHash;
    private Date registrationDate;
    @Reference
    private List<User> following;
    private boolean verified;

    public boolean isVerified() {
        return verified;
    }
    public void setVerified(boolean verified) {
        this.verified = verified;
    }
    public Date getRegistrationDate() {
        return registrationDate;
    }
    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPasswordHash() {
        return passwordHash;
    }
    public void setPassword(String password) {
        this.passwordHash = Credential.MD5.digest(password).substring(4); // "MD5:################" -> "################"
    }
    public void setPasswordHash(String passwordHash){
        this.passwordHash = passwordHash;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void addFollowee(User u){
        following.add(u);
    }
    public boolean removeFollowee(User u){
        return following.removeIf((User i) -> i.getId().equals(u.getId()));
    }
    public List<User> getFollowees(){
        return following;
    }
}
