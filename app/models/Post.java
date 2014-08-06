package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.NotNull;

@Entity(name="post")
public class Post {

	@Id
	@SequenceGenerator(name = "EVENTO_SEQUENCE", sequenceName = "EVENTO_SEQUENCE", allocationSize = 1, initialValue = 0)
	@GeneratedValue(strategy = GenerationType.TABLE)
	private Long id;
	
	@Column
	@NotNull
	private String msg;
	
	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
	@JoinColumn 
	private List<Comment> comments;

	public Post(String msg) {
		this.comments = new ArrayList<Comment>();
		this.msg = msg;
	}

	public Post() {
		this(null);
	}
	
	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public void addComment(String msg) {
		this.comments.add(new Comment(msg));
	}

	public List<Comment> getComments() {
		return comments;
	}

	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long  id) {
		this.id = id;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof Post)) {
			return false;
		}
		Post otherPost = (Post) obj;
		return otherPost.getMsg().equals(this.msg);
	}

	public Comment getComment(Long sequence) {
		for (Comment c : this.comments) {
			if (c.getId().toString().equals(sequence.toString())) {
				return c;
			}
		}
		return null;
	}

	public void removeComment(Long sequence) {
		Comment c = new Comment();
		c.setId(sequence);
		comments.remove(c);
	}

	@Override
	public String toString() {
		return "Post N: " + getId() + "\n Message: " + this.msg;
	}
}
