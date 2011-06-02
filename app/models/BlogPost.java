package models;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import other.utils.StringUtils;

import play.db.jpa.Model;

@Entity
public class BlogPost extends Model {
	
	@ManyToOne
	public SocialUser author;
	
	public String title;
	public String sanitizedTitle;
	public Date postedAt;
	public Date lastUpdatedAt;
	public String content;
	
	@ManyToMany
	public Set<Tag> tags;
	
	@OneToMany
	public Set<Comment> comments;
	
	public BlogPost(SocialUser author, 
					String title,  
					String content) {
		this.author = author;
		this.title = title;
		this.sanitizedTitle = StringUtils.replaceSpaceWithDashes(title); 
		this.postedAt = new Date();
		this.lastUpdatedAt = postedAt;
		this.content = content;
		this.tags = new TreeSet<Tag>();
		this.comments = new TreeSet<Comment>();
		//create();
	}
	
	public static BlogPost findBySanitizedTitleAndDate(String year, 
													   String month, 
													   String day, 
													   String sanitizedTitle) {
		BlogPost retVal = null;
		String query = "select bp from BlogPost bp where bp.sanitizedTitle = ?";
		List<BlogPost> blogPosts = BlogPost.find(query, sanitizedTitle).fetch();
		for(BlogPost blogPost : blogPosts) {
			Date postedAt = blogPost.postedAt;
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
			if((year+month+day).equals(dateFormat.format(postedAt))) {
				retVal = blogPost;
				break;
			}
		}
		return retVal;
	}
	
	public BlogPost tagWith(String tag) {
		tags.add(Tag.findOrCreateByName(tag));
        return this;
	}
	
	public static List<BlogPost> tail(int count) {
		List<BlogPost> blogPosts = 
			BlogPost.find("select b from BlogPost b order by b.lastUpdatedAt desc").fetch(1, count);
		return blogPosts;
	}
	
	@Override
	public String toString() {
		return this.title;
	}
}
