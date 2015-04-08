package models;

import javax.persistence.Entity;

import play.data.validation.Email;
import play.data.validation.MaxSize;
import play.data.validation.MinSize;
import play.data.validation.Required;
import play.db.jpa.Model;

@Entity
public class User extends Model {
	@Required
	@MinSize(8)
	public String name;
	
	@Required
	@Email
	public String email;
	
	@Required
	@MaxSize(1024)
	public String password;
	
	@Required
	@MaxSize(1024)
	public String address;
	
	public String toString() {
		return email;
	}

}
