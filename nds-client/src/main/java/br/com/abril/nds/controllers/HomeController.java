package br.com.abril.nds.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;


@Resource
@Path("/index")
public class HomeController {

	
	@Autowired
	private Result result;
	@Autowired
	private HttpServletRequest request;

	@Get
	@Path("/")
	public void index() {

	}
}
