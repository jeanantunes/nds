package br.com.abril.nds.interceptor;

import java.lang.reflect.Method;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import br.com.abril.nds.client.annotation.Public;
import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.controllers.ErrorController;
import br.com.abril.nds.controllers.HomeController;
import br.com.abril.nds.controllers.InicialController;
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

import com.google.gson.Gson;

@RequestScoped
@Intercepts(before=ExecuteMethodInterceptor.class)
public class URLSecurityInterceptor implements Interceptor {

	private Logger logger = Logger.getLogger(URLSecurityInterceptor.class);

	private Result result;

	private HttpServletRequest request;

	private Validator validator;

	public URLSecurityInterceptor(Result result, HttpServletRequest request, Validator validator) {

		this.result = result;
		this.request = request;
		this.validator = validator;

	}

	@Override
	public void intercept(InterceptorStack stack, 
			ResourceMethod method,
			Object resourceInstance) throws InterceptionException {

		try {			

			Method[] methods = resourceInstance.getClass().getMethods();

			boolean authorize = false;

			/*
			 *  Se o metodo nao for anotado,
			 *  sera verificado nos outros metodos se ha alguma restricao de seguranca.
			 *  Se houver, verifica-se se o usuario logado tem a permissao que 
			 *  o metodo anotado necessita, e aplica ao metodo invocado.
			 */
			if(!method.getMethod().isAnnotationPresent(Rules.class)) {

				int totalMetodosAnotados = 0;
				
				for(Method m : methods) {

					if(m.isAnnotationPresent(Rules.class)) {

						totalMetodosAnotados++;

						Collection<? extends GrantedAuthority> auths = SecurityContextHolder.getContext().getAuthentication().getAuthorities();

						for(GrantedAuthority auth : auths) {

							Rules rule = m.getAnnotation(Rules.class);

							if(auth.getAuthority().toString().equals(rule.value().toString())) {
								authorize = true;
								break;
							}

						}

					}

					if(authorize) break;

				}

				//Se forem iguais, a classe nao possui nenhuma restricao de seguranca (nenhum metodo anotado)
				if(methods.length == (methods.length - totalMetodosAnotados)) authorize = true;

			} else {
				authorize = true;
			}

			if((resourceInstance instanceof HomeController || resourceInstance instanceof InicialController) || authorize)
				stack.next(method, resourceInstance);

			if(!(resourceInstance instanceof HomeController || resourceInstance instanceof InicialController) && !authorize)
				throw new Exception("Acesso Negado.");

		} catch (Throwable throwable ) {

			logger.error(throwable.getMessage(), throwable);

			Throwable cause = ExceptionUtil.getRootCause(throwable);

			if (cause instanceof ValidacaoException) {

				this.tratarExcessaoValidacao((ValidacaoException) cause);

			} else {

				validator.onErrorRedirectTo(ErrorController.class).showError(cause);

				this.tratarExcessoesGenericas(cause);
			}

		}
	}


	/**
	 * Trata as validações a partir da ValidacaoException lançada
	 * 
	 * @param validacaoException
	 */
	private void tratarExcessaoValidacao(ValidacaoException validacaoException) {

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
	private void tratarExcessoesGenericas(Throwable throwable) {

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
