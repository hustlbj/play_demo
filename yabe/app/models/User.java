package models;

import javax.persistence.Entity;

import play.db.jpa.Model;

//Entity注解 标记User类为受管理的额JPA实体，并且超类Model提供了一套有用的JPA方法用于持久化
@Entity
public class User extends Model {
	//JPA实体必须有@Id属性，play.db.jpa.Model超类提供了自动生成的数字id主键
	public String email;
	public String password;
	public String fullname;
	public boolean isAdmin;
	
	public User(String email, String password, String fullname) {
		this.email = email;
		this.password = password;
		this.fullname = fullname;
	}
	
	public static User connect(String email, String password) {
		return find("byEmailAndPassword", email, password).first();
	}
}
