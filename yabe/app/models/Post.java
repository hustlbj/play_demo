package models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import play.db.jpa.Model;

@Entity
public class Post extends Model {
	public String title;
	public Date postedAt;
	//Lob注解通知JPA使用长文本数据类型存储文章的主体内容
	@Lob
	public String content;
	//ManyToOne注解声明Post类与User类的多对一关系
	@ManyToOne
	public User author;
	//mappedBy属性用于通知JPA，关系是由Comment类中的post属性所维护的
	//当用JPS定义双向关系时，告诉哪一边是关系维护方就显得特别重要
	@OneToMany(mappedBy="post", cascade=CascadeType.ALL)
	public List<Comment> comments;
	
	public Post(User author, String title, String content){
		this.comments = new ArrayList<Comment>();
		this.author = author;
		this.title = title;
		this.content = content;
		this.postedAt = new Date(); 
	}
	//对一个文章发表评论
	public Post addComment(String author, String content) {
		Comment newComment = new Comment(this, author, content);
		this.comments.add(newComment);
		this.save();
		return this;
	}
	public Post previous() {
		return Post.find("postedAt < ? order by postedAt desc", postedAt).first();
	}
	public Post next() {
		return Post.find("postedAt > ? order by postedAt asc", postedAt).first();
	}
}
