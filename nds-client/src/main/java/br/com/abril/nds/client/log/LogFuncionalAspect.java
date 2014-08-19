package br.com.abril.nds.client.log;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import br.com.abril.nds.client.log.LogFuncionalUtil.StatusLog;

@Component
@Aspect
public class LogFuncionalAspect {
	
	@SuppressWarnings("rawtypes")
	@Around(value = "@annotation(logFuncional)", argNames = "joinPoint, logFuncional")
	public Object logar(ProceedingJoinPoint joinPoint,LogFuncional logFuncional) throws Throwable {
		
		if (logFuncional == null) {
			
			return null;
		}
		
		Object retVal = null;
		
		String nomeUsuario = SecurityContextHolder.getContext().getAuthentication().getName();
		
		Class clazz = joinPoint.getTarget().getClass();
		
		LogFuncionalUtil.logar(logFuncional.value(),StatusLog.INCIADO,nomeUsuario, clazz);
		
		StopWatch stopWatch = new StopWatch();
		
		stopWatch.start();
		
		try {
			
			retVal = joinPoint.proceed();
			
		} finally {
			
			stopWatch.stop();
			
			LogFuncionalUtil.logar(logFuncional.value(),StatusLog.FINALIZADO,nomeUsuario,clazz,stopWatch.getTotalTimeMillis());
		}
		
		return retVal;
	}
	
}
