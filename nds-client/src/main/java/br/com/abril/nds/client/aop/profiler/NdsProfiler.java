package br.com.abril.nds.client.aop.profiler;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;import org.slf4j.LoggerFactory;


@Aspect
public class NdsProfiler {

	private static final Logger LOGGER = LoggerFactory.getLogger(NdsProfiler.class);
	
	private static final int START_TIME_POSITION_COLON = 170;
	
	@Around("execution(* br.com.abril.nds.repository.*.*(..)) || execution(* br.com.abril.nds.dao.*.*(..))")
	public Object profileRepository(ProceedingJoinPoint pjp) throws Throwable {
		
		long start = System.currentTimeMillis();
		//LOGGER.info("Repository: Chamando método: "+ pjp.toShortString() +".");
		
		StringBuilder msgRep = new StringBuilder("");
		int indexRep = 0;
		StringBuilder spacesRep = new StringBuilder("");
		
		Object output = pjp.proceed();
		
		long elapsedTime = System.currentTimeMillis() - start;	
		StringBuilder str = new StringBuilder("")
			.append("Repository: Tempo total da execução do método ")
			.append(pjp.toShortString().replace("execution(", "").replace("))", ")"))
			.append(": ")
			.append(String.format("%5d", elapsedTime).toString())
			.append(" ms (")
			.append(String.format("%5.3f", ((double) elapsedTime / 1000)))
			.append(" s).");
		
		indexRep = str.toString().lastIndexOf(':');
		while(indexRep < START_TIME_POSITION_COLON) {
			spacesRep.append(" ");
			indexRep++;
		}
		msgRep.append(str.substring(0, str.toString().lastIndexOf(':')));
		msgRep.append(spacesRep.toString());
		msgRep.append(str.substring(str.toString().lastIndexOf(':'), str.length()));
		
		LOGGER.info(msgRep.toString());
		return output;
		
	}
	
	@Around("execution(* br.com.abril.nds.service.*.*(..))")
	public Object profileService(ProceedingJoinPoint pjp) throws Throwable {
		
		long start = System.currentTimeMillis();
		//LOGGER.info("Service: Chamando método: "+ pjp.toShortString() +".");
		
		StringBuilder msgRep = new StringBuilder("");
		int indexServ = 0;
		StringBuilder spacesServ = new StringBuilder("");
		
		Object output = pjp.proceed();
		
		long elapsedTime = System.currentTimeMillis() - start;
		
		StringBuilder str = new StringBuilder("")
			.append("Service   : Tempo total da execução do método ")
			.append(pjp.toShortString().replace("execution(", "").replace("))", ")") )
			.append(": ")
			.append(String.format("%5d", elapsedTime))
			.append(" ms (")
			.append(String.format("%5.3f", ((double) elapsedTime / 1000)))
			.append(" s).");
		
		indexServ = str.toString().lastIndexOf(':');
		
		while(indexServ < START_TIME_POSITION_COLON) {
			spacesServ.append(" ");
			indexServ++;
		}
		msgRep.append(str.substring(0, str.toString().lastIndexOf(':')));
		msgRep.append(spacesServ.toString());
		msgRep.append(str.substring(str.toString().lastIndexOf(':'), str.length()));
		
		LOGGER.info(msgRep.toString());
		return output;
		
	}

}
