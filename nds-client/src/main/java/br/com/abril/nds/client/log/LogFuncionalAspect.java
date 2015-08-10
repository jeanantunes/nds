package br.com.abril.nds.client.log;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import br.com.abril.nds.client.log.LogFuncionalUtil.StatusLog;

@Component
@Aspect
public class LogFuncionalAspect {

	
	
    
	 

	
	
	@SuppressWarnings("rawtypes")
	@Around(value = "@annotation(logFuncional)", argNames = "joinPoint, logFuncional")
	public Object logar(ProceedingJoinPoint joinPoint, LogFuncional logFuncional) throws Throwable {
		
		if (logFuncional == null) {
			
			return null;
		}
		
		Object retVal = null;
	
		
		
		String nomeUsuario = SecurityContextHolder.getContext().getAuthentication().getName();
		String remoteAddress = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest().getRemoteAddr();
		nomeUsuario+="/"+remoteAddress;
		Class clazz = joinPoint.getTarget().getClass();
		
		LogFuncionalUtil.logar(this.extrairFuncionalidade(logFuncional, clazz),this.extrairMetodo(joinPoint),StatusLog.INCIADO,nomeUsuario, clazz);
		
		StopWatch stopWatch = new StopWatch();
		
		stopWatch.start();
		
		try {
			
			retVal = joinPoint.proceed();
			
		} finally {
			
			stopWatch.stop();
			
			LogFuncionalUtil.logar(this.extrairFuncionalidade(logFuncional, clazz),this.extrairMetodo(joinPoint),StatusLog.FINALIZADO,nomeUsuario,clazz,stopWatch.getTotalTimeMillis());
		}
		
		return retVal;
	}
	
	private String extrairMetodo(JoinPoint joinPoint) {
		
		return joinPoint.getSignature().getDeclaringTypeName() + "#" + joinPoint.getSignature().getName();
	}
	
	private String extrairFuncionalidade(LogFuncional logFuncional, Class<?> clazz) {
		
		if (logFuncional != null && !logFuncional.value().isEmpty()) {

			return logFuncional.value();
		}
		
		return clazz.getSimpleName();
	}	
}