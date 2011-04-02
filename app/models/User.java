package models;

import javax.persistence.*;

import java.util.*;

import play.*;
import play.db.jpa.*;
import play.libs.*;
import play.data.validation.*;

@Entity
public class User extends Model {

    @Email
    @Required
    public String email;
    
    @Required
    public String passwordHash;
    
    @Required
    public String name;
    
    @ManyToMany(cascade=CascadeType.PERSIST)
    public Set<Role> roles;
    
    public String needConfirmation;
    
    // ~~~~~~~~~~~~ 
    
    public User(String email, String password, String name) {
        this.email = email;
        this.passwordHash = password;//Codec.hexMD5(password);
        this.name = name;
        this.needConfirmation = Codec.UUID();
        create();
    }
    
    // ~~~~~~~~~~~~ 
    
    public boolean checkPassword(String password) {
        return passwordHash.equals(Codec.hexMD5(password));
    }

    // ~~~~~~~~~~~~ 
    
//    public List<Post> getRecentsPosts() {
//        return Post.find("postedBy = ? order by postedAt", this).fetch(1, 10);
//    }
//
//    public Long getPostsCount() {
//        return Post.count("postedBy", this);
//    }
//
//    public Long getTopicsCount() {
//        return Post.count("select count(distinct t) from Topic t, Post p, User u where p.postedBy = ? and p.topic = t", this);
//    }
    
    // ~~~~~~~~~~~~ 
    
    public static User findByEmail(String email) {
        return find("email", email).first();
    }

    public static User findByRegistrationUUID(String uuid) {
        return find("needConfirmation", uuid).first();
    }

    public static List<User> findAll(int page, int pageSize) {
        return User.all().fetch(page, pageSize);
    }

    public static boolean isEmailAvailable(String email) {
        return findByEmail(email) == null;
    }

//	@Override
//	public int hashCode() {
//		final int prime = 31;
//		int result = super.hashCode();
//		result = prime * result + ((email == null) ? 0 : email.hashCode());
//		result = prime * result + ((name == null) ? 0 : name.hashCode());
//		result = prime
//				* result
//				+ ((needConfirmation == null) ? 0 : needConfirmation.hashCode());
//		result = prime * result
//				+ ((passwordHash == null) ? 0 : passwordHash.hashCode());
//		result = prime * result + ((roles == null) ? 0 : roles.hashCode());
//		return result;
//	}
//
//	@Override
//	public boolean equals(Object obj) {
//		if (this == obj)
//			return true;
//		if (!super.equals(obj))
//			return false;
//		if (getClass() != obj.getClass())
//			return false;
//		User other = (User) obj;
//		if (email == null) {
//			if (other.email != null)
//				return false;
//		} else if (!email.equals(other.email))
//			return false;
//		if (name == null) {
//			if (other.name != null)
//				return false;
//		} else if (!name.equals(other.name))
//			return false;
//		if (needConfirmation == null) {
//			if (other.needConfirmation != null)
//				return false;
//		} else if (!needConfirmation.equals(other.needConfirmation))
//			return false;
//		if (passwordHash == null) {
//			if (other.passwordHash != null)
//				return false;
//		} else if (!passwordHash.equals(other.passwordHash))
//			return false;
//		if (roles == null) {
//			if (other.roles != null)
//				return false;
//		} else if (!roles.equals(other.roles))
//			return false;
//		return true;
//	}

	@Override
	public String toString() {
		return "User [email=" + email + ", passwordHash=" + passwordHash
				+ ", name=" + name + ", roles=" + roles + ", needConfirmation="
				+ needConfirmation + "]";
	}
	
	
    
}

