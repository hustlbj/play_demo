package controllers;

import play.*;
import play.libs.Codec;
import play.libs.Images;
import play.cache.*;
import play.data.validation.Required;
import play.mvc.*;

import java.awt.Image;
import java.util.*;

import models.*;

public class Application extends Controller {

    public static void index() {
    	//从数据库中选择postedAt最近的一篇Post
    	Post frontPost = Post.find("order by postedAt desc").first();
    	//按postedAt时间顺序从新到老排序，跳过第一条后选择最多10条Post
    	List<Post> olderPosts = Post.find("order by postedAt desc").from(1).fetch(10);
    	//渲染对应的index.html模板
        render(frontPost, olderPosts);
    }
    
    @Before
    static void addDefaults() {
    	renderArgs.put("blogTitle", Play.configuration.getProperty("blog.title"));
    	renderArgs.put("blogBaseline", Play.configuration.getProperty("blog.baseline"));
    }

    /**
     * 
     * @param id show()方法自动获取HTTP中的id参数，这个参数可以是HTTP查询字符串、URL路径或者表单请求体
     * 如果收到的HTTP参数id不合法，框架会为show()中的id赋空值，并且在日志中增加一条错误
     * @return render 渲染views/Application/show.html
     * 
     */
    public static void show(Long id) {
    	Post post = Post.findById(id);
    	//生成一个随机ID隐藏到show页面的表单中
    	String randomID = Codec.UUID();
    	render(post, randomID);
    }
    
    /**
     * 添加一条评论
     * @param postId 文章post对应的Id
     * @param author 评论时输入的名字字符串，不是文章post的作者User author
     * @param content 评论内容字符串
     */
    public static void postComment(
    		Long postId, 
    		@Required(message="Author is required") String author, 
    		@Required(message="A message is required") String content,
    		@Required(message="Please type the code") String code,
    		String randomID)
    {
    	Post post = Post.findById(postId);
    	validation.equals(code.trim().toLowerCase(), Cache.get(randomID).toString().toLowerCase());
    	if (validation.hasErrors())
    		render("Application/show.html", post);
    	post.addComment(author, content);
    	//在连续的两个Action之间传递信息
    	flash.success("Thanks for posting %s", author);
    	//评论完之后调用Application.show刷新这篇文章post
    	Cache.delete(randomID);
    	show(postId);
    }
    
    /**
     * 直接调用Play的libs.Images生成验证码
     */
    public static void captcha(String id)
    {
    	Images.Captcha captcha = Images.captcha();
    	//在服务器端存储验证码的key，临时数据可以用play的缓存进行存储。为了便于获取验证码信息，程序需生成唯一的id
    	String code = captcha.getText("#E4EAFD");
    	//绑定ID和验证码，到时候还是用ID来从cache中取出验证码的值
    	Cache.set(id, code, "10min");
    	renderBinary(captcha);
    }
}
