package br.com.abril.nds.authentication;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;


public class AjaxAwareAuthenticationEntryPoint extends LoginUrlAuthenticationEntryPoint {

	private static final String REQUESTED_WITH = "X-Requested-With";


	public AjaxAwareAuthenticationEntryPoint(String loginFormUrl) {
		super(loginFormUrl);
	}
	
	
	@Override
	public void commence(HttpServletRequest request,
			HttpServletResponse response, AuthenticationException authException)
			throws IOException, ServletException {
		 if(request.getHeader(REQUESTED_WITH) != null && request.getHeader(REQUESTED_WITH).equals("XMLHttpRequest")){
	            ((HttpServletResponse)response).sendError(601, "Session expired");
	        }else{
	            super.commence(request, response, authException);
	        }
	}
}
