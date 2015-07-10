package models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import play.db.jpa.Model;

/**
 * 文章类
 * @author BJ
 * 
 */
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
	/**
	 * Set<Tag> tags , Post和Tag是多对多关系
	 */
	@ManyToMany(cascade=CascadeType.PERSIST)
	public Set<Tag> tags;
	
	public Post(User author, String title, String content){
		this.comments = new ArrayList<Comment>();
		this.author = author;
		this.title = title;
		this.content = content;
		this.postedAt = new Date(); 
		this.tags = new TreeSet<Tag>();
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
	public Post tagItWith(String name) {
		tags.add(Tag.findOrCreateByName(name));
		return this;
	}
	/**
	 * 获取指定tag的所有文章
	 * @param tag 指定的标签
	 * @return List<Post> 包含该tag的文章列表
	 */
	public static List<Post> findTaggedWith(String tag) {
		return Post.find("select distinct p from Post p join p.tags as t where t.name=?", tag).fetch();
	}
}
