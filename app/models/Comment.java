package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.NotNull;

@Entity(name="comment")
public class Comment {

	@Id
	@SequenceGenerator(name = "COMMENT_SEQUENCE", sequenceName = "COMMENT_SEQUENCE", allocationSize = 1, initialValue = 0)
	@GeneratedValue(strategy = GenerationType.TABLE)
	private Long id;
	
	@Column
	@NotNull
	private String msg;

	public Comment(String msg) {
		this.setMsg(msg);
	}
	
	public Comment() {
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long sequence) {
		this.id = sequence;
	}

	@Override
	public String toString() {
		return "Comment N: " + id + "\n Message : " + this.msg;
	}
}