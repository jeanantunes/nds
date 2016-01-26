package br.com.abril.nds.client.job;


import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.service.EstoqueProdutoService;
import br.com.abril.nds.service.impl.EstoqueProdutoServiceImpl;

public class AtualizaEstoqueJob implements Job {
    
	private static final Logger LOGGER = LoggerFactory.getLogger(AtualizaEstoqueJob.class);	
	
    @Autowired
    private EstoqueProdutoService estoqueProdutoService;
    
    @Override
    public void execute(JobExecutionContext arg0) throws JobExecutionException {
    	LOGGER.warn("DIERENCA_ESTOQUE ATUALIZAESTOQUEJOB INICIO");
    	synchronized (AtualizaEstoqueJob.class) {
            this.estoqueProdutoService.atualizarEstoqueProdutoCota();
		}
    	LOGGER.warn("DIERENCA_ESTOQUE ATUALIZAESTOQUEJOB FIM");
    }
}
