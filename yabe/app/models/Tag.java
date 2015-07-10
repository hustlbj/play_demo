package models;

import java.util.List;
import java.util.Map;

import javax.persistence.Entity;

import play.db.jpa.Model;

/**
 * Tag类作为标签类对文章进行分类管理
 * @author BJ
 *
 */
@Entity
public class Tag extends Model implements Comparable<Tag> {
	public String name;
	
	private Tag(String name) {
		this.name = name;
	}
	public String toString() {
		return name;
	}
	@Override
	public int compareTo(Tag arg0) {
		// TODO Auto-generated method stub
		return name.compareTo(arg0.name);
	}
	/**
	 * 工厂方法，先查找不存在再创建
	 * @param name tag名
	 * @return 叫name的tag对象
	 */
	public static Tag findOrCreateByName(String name) {
		Tag tag = Tag.find("byName", name).first();
		if (tag == null) {
			tag = new Tag(name);
		}
		return tag;
	}
	
	public static List<Map> getCloud() {
		List<Map> result = Tag.find("select new map(t.name as tag, count(p.id) as pound) from Post p join p.tags as t group by t.name order by t.name").fetch();
		return result;
	}

}
