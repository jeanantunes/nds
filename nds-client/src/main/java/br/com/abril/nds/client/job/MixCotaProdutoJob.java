package br.com.abril.nds.client.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import br.com.abril.nds.repository.MixCotaProdutoRepository;

public class MixCotaProdutoJob implements Job {

	private MixCotaProdutoRepository mixCotaProdutoRepository;
	
public MixCotaProdutoJob() {
		
		ClassPathXmlApplicationContext applicationContext = 
			new ClassPathXmlApplicationContext("applicationContext.xml");
		
		this.mixCotaProdutoRepository = applicationContext.getBean(MixCotaProdutoRepository.class);
	}
	
	
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		mixCotaProdutoRepository.execucaoQuartz();		
	}

}
