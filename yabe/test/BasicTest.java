import org.junit.*;

import java.util.*;

import play.test.*;
import models.*;

public class BasicTest extends UnitTest {
	//每次测试前清空前面测试产生的例子
	@Before
	public void setup() {
		Fixtures.deleteDatabase();
	}

	//默认测试例子
    @Test
    public void aVeryImportantThingToTest() {
        assertEquals(2, 1 + 1);
    }
    /*****************测试User************************/
    @Test
    public void createAndRetriveUser() {
    	new User("demo@oopsplay.org", "123", "savage").save();
    	User savageUser = User.find("byEmail", "demo@oopsplay.org").first();
    	
    	//测试
    	assertNotNull(savageUser);
    	assertEquals("savage", savageUser.fullname);
    }
    
    @Test
    public void tryConnectAsUser() {
    	new User("demo@oopsplay.org", "123", "savage").save();
    	//测试
    	assertNotNull(User.connect("demo@oopsplay.org", "123"));
    	assertNull(User.connect("dhl@oopsplay.org", "123"));
    	assertNull(User.connect("demo@oopsplay.org", "456"));
    }

    /******************测试Post***********************/
    @Test
    public void createPost() {
    	//创建并保存用户
    	User savage = new User("demo2@oopsplay.org", "123", "savage").save();
    	//发表新的文章
    	new Post(savage, "Title", "Hello savage").save();
    	
    	//测试文章是否已经发表 成功
    	assertEquals(1, Post.count());
    	
    	//读取savage用户发布的所有文章
    	List<Post> savagePost = Post.find("byAuthor", savage).fetch();
    	
    	//测试
    	assertEquals(1, savagePost.size());
    	Post firstPost = savagePost.get(0);
    	assertNotNull(firstPost);
    	assertEquals(savage, firstPost.author);
    	assertEquals("Title", firstPost.title);
    	assertEquals("Hello savage", firstPost.content);
    	assertNotNull(firstPost.postedAt);
    	
    }
    
    @Test
    public void useTheCommentsRelation() {
    	User savage = new User("demo4@oopsplay.org", "123", "savage").save();
    	Post savagePost = new Post(savage, "Title3", "Hi I am dh13").save();
    	
    	savagePost.addComment("dh01", "good job");
    	savagePost.addComment("dh02", "great");
    	
    	assertEquals(1, User.count());
    	assertEquals(1, Post.count());
    	assertEquals(2, Comment.count());
    	
    	//检查数据库中的文章、评论
    	savagePost = Post.find("byAuthor", savage).first();
    	assertNotNull(savagePost);
    	assertEquals(2, savagePost.comments.size());
    	assertEquals("dh01", savagePost.comments.get(0).author);
    	
    	//删除文章
    	savagePost.delete();
    	//检查所有评论都已删除
    	assertEquals(1, User.count());
    	assertEquals(0, Post.count());
    	assertEquals(0, Comment.count());
    }
    
    @Test
    public void fullTest() {
    	Fixtures.loadModels("data.yml");
    	//计数
    	assertEquals(2, User.count());
    	assertEquals(3, Post.count());
    	assertEquals(3, Comment.count());
    	
    }
    
    @Test
    public void testTags() {
    	User savage = new User("demo5@oopsplay.org", "123", "savage").save();
    	
    	Post savagePost = new Post(savage, "My first post", "Hello world!!!!!").save();
    	Post anotherPost= new Post(savage, "My Second post", "Hello aaaaaa!!!!!").save();
    	
    	assertEquals(0, Post.findTaggedWith("Red").size());
    	
    	savagePost.tagItWith("Red").tagItWith("Blue").save();
    	anotherPost.tagItWith("Red").tagItWith("Green").save();
    	
    	assertEquals(2, Post.findTaggedWith("Red").size());
    	assertEquals(1, Post.findTaggedWith("Blue").size());
    	assertEquals(1, Post.findTaggedWith("Green").size());
    }
}
