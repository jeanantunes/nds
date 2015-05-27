package br.com.abril.nds.client.aop.profiler;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StopWatch;

@Aspect
public class NdsProfiler {

	private static final Logger LOGGER = LoggerFactory.getLogger(NdsProfiler.class);
	
	private static final String INICIO = "Início";
	
	private static final String FIM = "Fim";
	
	private static final String EXECUCAO = "Execução";
	
	@Around("execution(* br.com.abril.nds.repository.*.*(..)) || execution(* br.com.abril.nds.dao.*.*(..))")
	public Object profileRepository(ProceedingJoinPoint pjp) throws Throwable {
		
		Class<? extends Object> clazz = pjp.getTarget().getClass();
		
		Object output = null;
		
		StopWatch stopWatch = new StopWatch();
		
		stopWatch.start();
		
		try {
			
			output = pjp.proceed();
			
		} finally {
			
			stopWatch.stop();
			
			LOGGER.info("{};{};{};{};{};{};{};{};{}", EXECUCAO, "Repository", clazz, pjp.toShortString().replace("execution(", "").replace("))", ")"), stopWatch.getTotalTimeMillis(), "ms", stopWatch.getTotalTimeSeconds(), "s", pjp.getArgs());
		}
		
		return output;
		
	}
	
	@Around("execution(* br.com.abril.nds.service.*.*(..))")
	public Object profileService(ProceedingJoinPoint pjp) throws Throwable {
		
		Class<? extends Object> clazz = pjp.getTarget().getClass();
		
		Object output = null;
		
		StopWatch stopWatch = new StopWatch();
		
		stopWatch.start();
		
		try {
			
			LOGGER.info("{};{};{};{};{};{};{};{};{}", INICIO, "Service", clazz, pjp.toShortString().replace("execution(", "").replace("))", ")"), stopWatch.getTotalTimeMillis(), "ms", stopWatch.getTotalTimeSeconds(), "s", pjp.getArgs());
			output = pjp.proceed();
			
		} finally {
			
			stopWatch.stop();
			
			LOGGER.info("{};{};{};{};{};{};{};{};{}", FIM, "Service", clazz, pjp.toShortString().replace("execution(", "").replace("))", ")"), stopWatch.getTotalTimeMillis(), "ms", stopWatch.getTotalTimeSeconds(), "s", pjp.getArgs());
		}
		
		return output;
	}

}
