package br.com.abril.nds.integracao.ems0113.processor;

import java.util.concurrent.atomic.AtomicReference;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.abril.nds.integracao.engine.MessageProcessor;
import br.com.abril.nds.integracao.engine.log.NdsiLoggerFactory;import br.com.abril.nds.integracao.model.canonic.EMS0113Input;
import br.com.abril.nds.model.cadastro.DescontoLogistica;
import br.com.abril.nds.model.cadastro.HistoricoDescontoLogistica;
import br.com.abril.nds.model.integracao.EventoExecucaoEnum;
import br.com.abril.nds.model.integracao.Message;
import br.com.abril.nds.repository.AbstractRepository;
import br.com.abril.nds.service.DescontoLogisticaService;
import br.com.abril.nds.service.HistoricoDescontoLogisticaService;
import br.com.abril.nds.service.integracao.DistribuidorService;

/**
 * @author Jones.Costa
 * @version 1.0
 */
@Component
public class EMS0113MessageProcessor extends AbstractRepository implements MessageProcessor  {

	@Autowired
	private NdsiLoggerFactory ndsiLoggerFactory;
	
	@Autowired
	private DistribuidorService distribuidorService;
	
	@Autowired
	private HistoricoDescontoLogisticaService historicoDescontoLogisticaService;
	
	@Autowired
	private DescontoLogisticaService descontoLogisticaService;
	
	@Override
	public void preProcess(AtomicReference<Object> tempVar) {
		// TODO Auto-generated method stub
	}

	@Override
	public void processMessage(Message message) {

		EMS0113Input input = (EMS0113Input) message.getBody();
		
		if(distribuidorService.isDistribuidor(input.getCodigoDistribuidor())) {
			
			this.inserirDescontoLogistica(input, message);
			
			this.processarDadosHistoricoDesconto(input, message);
		}

		else{
			ndsiLoggerFactory.getLogger().logInfo(message, EventoExecucaoEnum.SEM_DOMINIO, "Codigo do Distribuidor invalido: "+input.getCodigoDistribuidor());
		}

	}
	
	/*
	 * Processa os dados referente ao historico dos desconto de logistica, insere e altera registros.
	 */
	private void processarDadosHistoricoDesconto(EMS0113Input input,Message message){
		
		//Verifica se ja existe historico criado para um determinado tipo de desconto com a mesma data de inicio de vigencia
		HistoricoDescontoLogistica historicoDescontoLogistica = 
				historicoDescontoLogisticaService.obterDesconto(input.getTipoDesconto(), input.getDataInicioDesconto());
		
		if (historicoDescontoLogistica != null ) {
			
			//Se o campo data de processamento do historico for null, quer dizer que o desconto do historico não foi replicado para o desconto logistica no fechamento diario
			if(historicoDescontoLogistica.getDataProcessamento() == null){
				
				historicoDescontoLogistica.setPercentualDesconto(input.getPercentDesconto());
				historicoDescontoLogistica.setPercentualPrestacaoServico(input.getPercentPrestServico());
				historicoDescontoLogistica.setDataInicioVigencia(input.getDataInicioDesconto());
				
				getSession().merge(historicoDescontoLogistica);
				
				ndsiLoggerFactory.getLogger().logInfo(
						message
						, EventoExecucaoEnum.INF_DADO_ALTERADO
						, "Historico desconto logística atualizado com sucesso. Tipo Desconto: "+ historicoDescontoLogistica.getTipoDesconto());
			}else{
				
				ndsiLoggerFactory.getLogger().logInfo(
						message
						, EventoExecucaoEnum.REGISTRO_JA_EXISTENTE
						, "Historico desconto logística já foi processado no fechamento diário. Tipo Desconto: "+ historicoDescontoLogistica.getTipoDesconto());
			}
			
		} else {
			
			historicoDescontoLogistica = new HistoricoDescontoLogistica();
			
			historicoDescontoLogistica.setId(null);
			historicoDescontoLogistica.setTipoDesconto(input.getTipoDesconto());
			historicoDescontoLogistica.setPercentualDesconto(input.getPercentDesconto());
			historicoDescontoLogistica.setPercentualPrestacaoServico(input.getPercentPrestServico());
			historicoDescontoLogistica.setDataInicioVigencia(input.getDataInicioDesconto());
			
			getSession().persist(historicoDescontoLogistica);
			
			ndsiLoggerFactory.getLogger().logInfo(
					message
					, EventoExecucaoEnum.INF_DADO_ALTERADO
					, "Historico desconto logística inserido com sucesso. Tipo Desconto: "+ input.getTipoDesconto());
		}
	}
	
	/*
	 * Inseri registro de Desconto de Logistica caso não exista registro na base de dados
	 */
	private void inserirDescontoLogistica(EMS0113Input input,Message message){
		
		DescontoLogistica descontoLogistica = descontoLogisticaService.obterPorTipoDesconto(input.getTipoDesconto());
		
		if(descontoLogistica == null){
			
			descontoLogistica = new DescontoLogistica();
			descontoLogistica.setTipoDesconto(input.getTipoDesconto());
			descontoLogistica.setPercentualDesconto(input.getPercentDesconto());
			descontoLogistica.setPercentualPrestacaoServico(input.getPercentPrestServico());
			descontoLogistica.setDataInicioVigencia(input.getDataInicioDesconto());
			
			getSession().persist(descontoLogistica);
			
			ndsiLoggerFactory.getLogger().logInfo(message
					, EventoExecucaoEnum.INF_DADO_ALTERADO
					, "Desconto logística incluido com sucesso. Tipo Desconto: "+ descontoLogistica.getTipoDesconto());
		}
	}

	@Override
	public void posProcess(Object tempVar) {
		// TODO Auto-generated method stub
	}
	
}
