package controllers;

import java.util.Map;

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
	    	return notFound("Id nao cadastrado no sistema");
	    }
	    return ok(Json.toJson(post));
	}
	
	@Transactional
	public static Result editPost(Long id) {
		Post post = (Post) getDao().findByEntityId(Post.class, id);
		if(post==null) {
	    	return notFound("Id nao cadastrado no sistema");
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
	    	return notFound("Id nao cadastrado no sistema");
	    }
		getDao().remove(post);
		getDao().flush();
		return ok();
	}
	
	public static GenericDAO getDao() {
		return new GenericDAOImpl();
	}
}
