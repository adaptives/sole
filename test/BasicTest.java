import org.junit.*;
import java.util.*;
import play.test.*;
import models.*;

public class BasicTest extends UnitTest {

	@Before
	public void setUp() {
		Fixtures.deleteAll();
	}
	
	@After
	public void tearDown() {
		
	}
	
    @Test
    public void testCreateAndRetrieveUser() {
    	User bobTheUser = new User("bob@bobbobbob.net", "bobspwd", "Bob The User");
    	bobTheUser.save();
    	
    	User retrievedBob = User.find("byEmail", "bob@bobbobbob.net").first();
    	
    	//tests
    	assertNotNull(retrievedBob);
    	assertEquals(bobTheUser, retrievedBob);
    }
    
    @Test
    public void testConnectUser() {
    	User bobTheUser = new User("bob@bobbobbob.net", "bobspwd", "Bob The User");
    	bobTheUser.save();
    	
    	assertNotNull(User.connect("bob@bobbobbob.net", "bobspwd"));    	
    	assertNull(User.connect("bob@bobbobbob.net", "bobswrongpwd"));
    	assertNull(User.connect("tom@tomtomtom.net", "tomsdoestnhaveanaccount"));
    }
    
    @Test
    public void testCreateAndRetrievePost() {
    	User bobTheUser = new User("bob@bobbobbob.net", "bobspwd", "Bob The User");
    	bobTheUser.save();
    	
    	Post post = new Post(bobTheUser, "Play is awesome", "Play is an awesome framework");
    	post.save();
    	
    	//tests
    	assertEquals(1, Post.count());
    	List<Post> bobPosts = Post.find("byAuthor", bobTheUser).fetch();
    	assertEquals(1, bobPosts.size());
    	Post firstPost = bobPosts.get(0);
    	assertEquals(post.title, firstPost.title);
    	assertEquals(post.content, firstPost.content);
    	assertEquals(post.author, firstPost.author);
    	assertEquals(post.postedAt, firstPost.postedAt);
    }
    
    @Test
    public void testCreateAndRetrieveComments() {
    	User bobTheUser = new User("bob@bobbobbob.net", "bobspwd", "Bob The User");
    	bobTheUser.save();
    	
    	Post post = new Post(bobTheUser, "Play is awesome", "Play is an awesome framework");
    	post.save();
    	
    	Comment comment = new Comment("Bob", 
    								  "This is my wonderful and insightful comment", 
    								  post);
    	comment.save();
    	
    	List<Comment> retrievedComments = Comment.find("byAuthor", "Bob").fetch();
    	assertEquals(1, retrievedComments.size());
    	Comment retrievedComment = retrievedComments.get(0);
    	assertNotNull(retrievedComment);
    	assertEquals(comment, retrievedComment);
    }
    
    @Test
    public void testAddAndDeleteComment() {
    	User bobTheUser = new User("bob@bobbobbob.net", "bobspwd", "Bob The User");
    	bobTheUser.save();
    	
    	Post post = new Post(bobTheUser, "Play is awesome", "Play is an awesome framework");
    	post.save();
    	
    	post.
    		addComment("Bob", "This is Bob's comment").
    			addComment("Tom", "This is another comment");
    	
    	List<Comment> comments = Comment.find("byPost", post).fetch();
    	
    	assertEquals(2, comments.size());
    	assertEquals("Bob", comments.get(0).author);
    	assertEquals("This is Bob's comment", comments.get(0).content);
    	assertEquals(post, comments.get(0).post);
    	assertEquals("Tom", comments.get(1).author);
    	assertEquals("This is another comment", comments.get(1).content);
    	assertEquals(post, comments.get(1).post);
    	
    	post.delete();
    	
    	assertEquals(1, User.count());
    	assertEquals(0, Post.count());
    	assertEquals(0, Comment.count());
    }

}
