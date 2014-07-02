package br.com.abril.nds.service.job;


import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
//import br.com.abril.nds.service.EstoqueProdutoService;

@DisallowConcurrentExecution
public class AtualizaEstoqueJob implements Job {
    
    @Autowired
    //private EstoqueProdutoService estoqueProdutoService;
    
    @Override
    public void execute(JobExecutionContext arg0) throws JobExecutionException {
        //this.estoqueProdutoService.atualizarEstoqueProdutoCota();
    }
}
