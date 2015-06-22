package br.com.abril.nds.controllers.interceptor;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import br.com.abril.nds.client.annotation.Public;
import br.com.abril.nds.controllers.ErrorController;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.serialization.custom.PlainJSONSerialization;
import br.com.abril.nds.util.Constantes;
import br.com.abril.nds.util.ExceptionUtil;
import br.com.abril.nds.util.Util;
import br.com.abril.nds.vo.ValidacaoVO;
import br.com.caelum.vraptor.InterceptionException;
import br.com.caelum.vraptor.Intercepts;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.Validator;
import br.com.caelum.vraptor.core.InterceptorStack;
import br.com.caelum.vraptor.interceptor.ExecuteMethodInterceptor;
import br.com.caelum.vraptor.interceptor.Interceptor;
import br.com.caelum.vraptor.ioc.RequestScoped;
import br.com.caelum.vraptor.resource.ResourceMethod;
import br.com.caelum.vraptor.view.Results;
import org.springframework.beans.factory.annotation.Autowired;
import java.io.*;

import com.google.gson.Gson;

@RequestScoped
@Intercepts(before=ExecuteMethodInterceptor.class)
public class ValidacaoInterceptor implements Interceptor {
	
    private static final Logger LOGGER = LoggerFactory.getLogger(ValidacaoInterceptor.class);

	private Result result;
	
	private HttpServletRequest request;
	
	private Validator validator;
	
	
	public ValidacaoInterceptor(Result result, HttpServletRequest request, Validator validator) {
		
		this.result = result;
		this.request = request;
		this.validator = validator;
	}
	
	
	
	
	@Override
	public void intercept(InterceptorStack stack, 
						  ResourceMethod method,
						  Object resourceInstance) throws InterceptionException {

		try {			
			stack.next(method, resourceInstance);
			
		} catch (Exception throwable ) {
		
            Throwable cause = ExceptionUtil.getRootCause(throwable);
			
			if (cause instanceof ValidacaoException) {
              if ( ((ValidacaoException ) cause).getValidacao().getTipoMensagem() == TipoMensagem.ERROR ){
				 LOGGER.error(throwable.getMessage(), throwable);
              }
              else
              if ( ((ValidacaoException ) cause).getValidacao().getTipoMensagem() == TipoMensagem.WARNING) {
            	LOGGER.warn(throwable.getMessage(), throwable); 
              }
				this.tratarExcecaoValidacao((ValidacaoException) cause);
			
			} else {
				LOGGER.error(throwable.getMessage(), throwable);
				validator.onErrorRedirectTo(ErrorController.class).showError(cause);
				
				this.tratarExecoesGenericas(cause);
			}
		}
	}

	            /**
     * Trata as validações a partir da ValidacaoException lançada
     * 
     * @param validacaoException
     */
	private void tratarExcecaoValidacao(ValidacaoException validacaoException) {

		if (Util.isAjaxUpload(request)) {
			
			result.use(PlainJSONSerialization.class).from(
				validacaoException.getValidacao(), Constantes.PARAM_MSGS).recursive().serialize();
		
		} else  if (Util.isAjaxRequest(request)) {
		
			result.use(Results.json()).from(
				validacaoException.getValidacao(), Constantes.PARAM_MSGS).recursive().serialize();
			
		} else {

			result.include(Constantes.PARAM_MSGS, new Gson().toJson(validacaoException.getValidacao()));
			
			if (validacaoException.getUrl() != null) {
				
				result.use(Results.page()).forwardTo(validacaoException.getUrl());
				
			} else {
				
				result.use(Results.page()).defaultView();
			}
		}			
	}

	            /**
     * Método que trata as exceções genéricas.
     * 
     * @param throwable
     */
	private void tratarExecoesGenericas(Throwable throwable) {

		String message = "Ocorreu um erro inesperado: " + throwable.getMessage();

		ValidacaoVO validacao = new ValidacaoVO(TipoMensagem.ERROR, message);
		
		if (Util.isAjaxUpload(request)) {
			
			result.use(PlainJSONSerialization.class).from(validacao, Constantes.PARAM_MSGS).recursive().serialize();
		
		} else  if (Util.isAjaxRequest(request)) {
			
			result.use(Results.json()).from(validacao, Constantes.PARAM_MSGS).recursive().serialize();	
		
		} else {
			
			result.redirectTo(ErrorController.class).showError(throwable);
		}
	}
	
	@Override
	public boolean accepts(ResourceMethod method) {
		return !(method.getMethod().isAnnotationPresent(Public.class) || method.getResource().getType().isAnnotationPresent(Public.class));
	}
}
