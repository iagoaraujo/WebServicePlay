package controllers;

import java.util.Map;

import models.Comment;
import models.Post;
import models.dao.GenericDAO;
import models.dao.GenericDAOImpl;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;


public class Application extends Controller {
	
	@Transactional
	public static Result getAllPosts() {
		return ok(Json.toJson(getDao().findAllByClassName("post")));
	}
	
	public static Result index() {
		return ok(views.html.cadastro.render());
	}
	
	@Transactional
	public static Result createPost() {
		Map<String, String[]> parametros = Controller.request().body().asFormUrlEncoded();
		Post post = new Post(parametros.get("msg")[0]);
		getDao().merge(post);
		getDao().flush();
		return ok();
	}
	
	@Transactional
	public static Result getPostById(Long id) {
	    Post post = (Post) getDao().findByEntityId(Post.class, id);
	    if(post==null) {
	    	return badRequest("Id nao cadastrado no sistema");
	    }
	    return ok(Json.toJson(post));
	}
	
	@Transactional
	public static Result editPost(Long id) {
		Post post = (Post) getDao().findByEntityId(Post.class, id);
		if(post==null) {
	    	return badRequest("Id nao cadastrado no sistema");
	    }
		Map<String, String[]> parametros = Controller.request().body().asFormUrlEncoded();
		String msg = parametros.get("msg")[0];
		post.setMsg(msg);
	    return ok();
	}
	
	@Transactional
	public static Result deletePost(Long id) {
		Post post = (Post) getDao().findByEntityId(Post.class, id);
		if(post==null) {
	    	return badRequest("Id nao cadastrado no sistema");
	    }
		getDao().remove(post);
		getDao().flush();
		return ok();
	}
	
	public static GenericDAO getDao() {
		return new GenericDAOImpl();
	}
	
	@Transactional
	public static Result getAllComments(Long id){
		Post post = (Post) getDao().findByEntityId(Post.class, id);
		if(post==null){
			return badRequest("Post nao cadastrado no sistema");
		}
		return ok(Json.toJson(post.getComments()));
	}
	
	@Transactional
	public static Result createComment(Long id){
		Post post = (Post) getDao().findByEntityId(Post.class, id);
		if(post==null){
			return badRequest("Post nao cadastrado no sistema");
		}
		Map<String, String[]> parametros = Controller.request().body().asFormUrlEncoded();
		String msg = parametros.get("msg")[0];
		post.addComment(msg);
		getDao().merge(post);
		getDao().flush();
		return ok();
	}
	
	@Transactional
	public static Result getCommentById(Long id, Long id2 ){
		Post post = (Post) getDao().findByEntityId(Post.class, id);
		if(post==null){
			return badRequest("Post nao cadastrado no sistema");
		}
		for(Comment comment: post.getComments()){
			if(comment.getId().equals(id2)){
				return ok(Json.toJson(comment));
			}
		}
		
		return badRequest("Comment nao cadastrado no sistema");
	}
	
	@Transactional
	public static Result deleteComment(Long id, Long id2){
		Post post = (Post) getDao().findByEntityId(Post.class, id);
		Comment commentPost = null;
		if(post==null){
			return badRequest("Post nao cadastrado no sistema");
		}
		for(Comment comment: post.getComments()){
			if(comment.getId().equals(id2)){
				commentPost = comment;
			}
		}
		if(commentPost==null){
			return badRequest("Comment nao cadastrado no sistema");
		}else{
			post.removeComment(commentPost.getId());
			getDao().remove(commentPost);
			getDao().merge(post);
			getDao().flush();
			return ok();
		}
		
	}
	
	@Transactional
	public static Result editComment(Long id, Long id2){
		Post post = (Post) getDao().findByEntityId(Post.class, id);
		if(post==null){
			return badRequest("Post nao cadastrado no sistema");
		}
		Map<String, String[]> parametros = Controller.request().body().asFormUrlEncoded();
		String msg = parametros.get("msg")[0];
		for(Comment comment: post.getComments()){
			if(comment.getId().equals(id2)){
				comment.setMsg(msg);
				getDao().merge(comment);
				getDao().flush();
				return ok(Json.toJson(comment));
			}
		}
		
		return badRequest("Comment nao cadastrado no sistema");
	}
	
	@Transactional
	public static Result getHeadAllPosts() {
		return ok(Json.toJson(head(getDao().findAllByClassName("post"))));
	}
	
	@Transactional
	public static Result getHeadFromPost(Long id) {
		Post post = (Post) getDao().findByEntityId(Post.class, id);
	    if(post==null) {
	    	return badRequest("Id nao cadastrado no sistema");
	    }
	    return ok(Json.toJson(head(post)));
	}
	
	@Transactional
	public static Result getHeadAllCommentsFromPost(Long id) {
		Post post = (Post) getDao().findByEntityId(Post.class, id);
		if(post==null){
			return badRequest("Post nao cadastrado no sistema");
		}
		return ok(Json.toJson(head(post.getComments())));
	}
	
	@Transactional
	public static Result getHeadFromComment(Long id, Long id2) {
		Post post = (Post) getDao().findByEntityId(Post.class, id);
		if(post==null){
			return badRequest("Post nao cadastrado no sistema");
		}
		for(Comment comment: post.getComments()){
			if(comment.getId().equals(id2)){
				return ok(Json.toJson(head(comment)));
			}
		}
		return badRequest("Comment nao cadastrado no sistema");
	}
	
	private static String head(Object obj) {
		return "Content-Type: application/json   Content-Length: "
				+ obj.toString().length();
	}
}
