package br.com.abril.nds.client.job;


import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.service.EstoqueProdutoService;

public class AtualizaEstoqueJob implements Job {
    
    @Autowired
    private EstoqueProdutoService estoqueProdutoService;
    
    @Override
    public void execute(JobExecutionContext arg0) throws JobExecutionException {
    	
    	synchronized (AtualizaEstoqueJob.class) {
            this.estoqueProdutoService.atualizarEstoqueProdutoCota();
		}
    }
}
