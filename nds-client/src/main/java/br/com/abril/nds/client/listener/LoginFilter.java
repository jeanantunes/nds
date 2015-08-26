
package br.com.abril.nds.client.listener;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginFilter implements Filter {




    @Override
    public void doFilter(ServletRequest request, ServletResponse response, 
            FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

      try {
        String path=req.getContextPath()+"/";
        // Java 1.8 stream API used here
        Cookie [] cookies = req.getCookies();
        String windowname =null;
        int ks=-1;
        int kn=-1;
        if ( cookies != null ) {
        
         
         for (int i=0; i < cookies.length; i++) {
        	 
        	 if (cookies[i].getName().equals("JSESSIONID")) {
        	  ks=i;
        	 
        
        	 }
        	 if (cookies[i].getName().equals("WINDOWNAME")) {
           	  kn=i;
           	 
           	 }
        	
         }
         
  
         
         try {
        	 if ( kn >= 0 ) {
             windowname = cookies[kn].getValue();
             req.getSession().setAttribute("WINDOWNAME", windowname);
        	 }
            }catch (Exception e) {
            	
            	e.printStackTrace();
            	
            }
        // if we don't have the user already in session, check our cookie MY_SESSION_COOKIE
       
        }
        /*
      
        	String sessionid = req.getSession().getId();
        	try {
        	//	sessionid = cookies[ks].getValue();
        	} catch (Exception e ){
        		
        		
        	}
     // be careful overwriting: JSESSIONID may have been set with other flags
        	*/
            Map  map = ( Map ) req.getSession().getAttribute("WINDOWNAMES");
            if (  map == null ) {
            	 map = new HashMap();
            	 req.getSession().setAttribute("WINDOWNAMES",map);
            }
            if (windowname != null && !map.containsKey(windowname))
               map.put(windowname,req.getSession().getId());
            
        	//resp.setHeader("SET-COOKIE", "WINDOWNAMES=" + map.size() + ";expires=-1;path=/nds-client/");
            Cookie cookie1 = new Cookie("WINDOWNAMES", ""+map.size());
            cookie1.setMaxAge(-1);
         
            cookie1.setPath(path);
            resp.addCookie(cookie1); 
        
            chain.doFilter(request, response);
      } catch (Exception ee) {
    	  ee.printStackTrace();
      }
      
    }

    public HttpServletResponse  issueCookieHttpOnly(HttpServletResponse response, 
            String cookieName, 
            String cookieValue, 
            String cookiePath, 
            long maxAgeInSeconds) {

        Date expireDate= new Date();
        expireDate.setTime (expireDate.getTime() + (1000 * maxAgeInSeconds));
        // The following pattern does not work for IE.
        // DateFormat df = new SimpleDateFormat("dd MMM yyyy kk:mm:ss z");

        // This pattern works for Firefox, Chrome, Safari and Opera, as well as IE.
        DateFormat df = new SimpleDateFormat("EEE, dd-MMM-yyyy kk:mm:ss z");
        df.setTimeZone(TimeZone.getTimeZone("GMT"));
        String cookieExpire = df.format(expireDate);

        StringBuilder sb = new StringBuilder(cookieName);
        sb.append("=");
        sb.append(cookieValue);
        sb.append(";expires=");
        sb.append(cookieExpire);
        sb.append(";path=");
        sb.append(cookiePath);
        sb.append(";HttpOnly");
        
        response.setHeader("SET-COOKIE", sb.toString());
        return response;
    }
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }

}