package br.com.abril.nds.controllers;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.util.ExceptionUtil;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;

@Resource
public class ErrorController {

	@Autowired
	private Result result;
	
	public void showError(Throwable throwable) {

		String stackTrace = ExceptionUtil.getStackTrace(throwable);
		
		result.include("stackTrace", stackTrace);
		result.include("message", throwable.getMessage());
	}
	
}
