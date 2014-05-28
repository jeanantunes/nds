package br.com.abril.nds.component.impl;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Component;

import br.com.abril.nds.component.ConferenciaEncalheAsyncComponent;
import br.com.abril.nds.dto.ConferenciaEncalheDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.movimentacao.ControleConferenciaEncalheCota;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.service.ConferenciaEncalheService;
import br.com.abril.nds.service.job.AtualizaEstoqueJob;


@Component
public class ConferenciaEncalheAsyncComponentImpl implements ConferenciaEncalheAsyncComponent {

	@Autowired
	private ConferenciaEncalheService conferenciaEncalheService;

	@Autowired
	private SchedulerFactoryBean schedulerFactoryBean;
	
	@Override
	@Async
	public void finalizarConferenciaEncalheAsync(
			ControleConferenciaEncalheCota controleConfEncalheCota, 
			List<ConferenciaEncalheDTO> listaConferenciaEncalhe, 
			Set<Long> listaIdConferenciaEncalheParaExclusao,
			Usuario usuario,
			boolean indConferenciaContingencia,
			BigDecimal reparte) {
		
		try {
			
			conferenciaEncalheService.finalizarConferenciaEncalhe(
					controleConfEncalheCota, 
					listaConferenciaEncalhe, 
					listaIdConferenciaEncalheParaExclusao,
					usuario,
					indConferenciaContingencia, 
					reparte);

			agendarAgoraAtualizacaoEstoqueProdutoConf();
			
			conferenciaEncalheService.sinalizarFimProcessoEncalhe(controleConfEncalheCota.getCota().getNumeroCota());
			
			
		} catch(Exception e) {
			
			conferenciaEncalheService.sinalizarErroProcessoEncalhe(controleConfEncalheCota.getCota().getNumeroCota(), e);
			
		}
		
	}
	
	
	
	@Override
	@Async
	public void salvarConferenciaEncalhe(
			final ControleConferenciaEncalheCota controleConfEncalheCota, 
			final List<ConferenciaEncalheDTO> listaConferenciaEncalhe, 
			final Set<Long> listaIdConferenciaEncalheParaExclusao,
			final Usuario usuario, 
			final boolean indConferenciaContingencia) {
		
		try {
			
			conferenciaEncalheService.salvarDadosConferenciaEncalhe(
					controleConfEncalheCota, 
					listaConferenciaEncalhe, 
					listaIdConferenciaEncalheParaExclusao, 
					usuario, 
					indConferenciaContingencia);
		
			conferenciaEncalheService.sinalizarFimProcessoEncalhe(controleConfEncalheCota.getCota().getNumeroCota());

		
		} catch(Exception e){
			
			conferenciaEncalheService.sinalizarErroProcessoEncalhe(controleConfEncalheCota.getCota().getNumeroCota(), e);
			
		} 
		
	}
	

	private void agendarAgoraAtualizacaoEstoqueProdutoConf() {

		Scheduler scheduler = schedulerFactoryBean.getScheduler();
		
	    JobDetail job = newJob(AtualizaEstoqueJob.class).build();
	    
	    Trigger trigger = newTrigger().startNow().build();
        
		try {
			
			scheduler.scheduleJob(job, trigger);
		
		} catch (SchedulerException e) {
        
			throw new ValidacaoException(TipoMensagem.WARNING, "Falha na atualização de estoque de produtos");
		
		}
		
    }
	
}
