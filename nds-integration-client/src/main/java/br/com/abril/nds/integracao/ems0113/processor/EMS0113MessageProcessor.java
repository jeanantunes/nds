package br.com.abril.nds.integracao.ems0113.processor;

import java.util.concurrent.atomic.AtomicReference;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.abril.nds.enums.integracao.MessageHeaderProperties;
import br.com.abril.nds.integracao.engine.MessageProcessor;
import br.com.abril.nds.integracao.engine.log.NdsiLoggerFactory;
import br.com.abril.nds.integracao.model.canonic.EMS0113Input;
import br.com.abril.nds.model.cadastro.DescontoLogistica;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.integracao.EventoExecucaoEnum;
import br.com.abril.nds.model.integracao.Message;
import br.com.abril.nds.repository.AbstractRepository;
import br.com.abril.nds.repository.FornecedorRepository;
import br.com.abril.nds.service.DescontoLogisticaService;
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
	private DescontoLogisticaService descontoLogisticaService;
	
	@Autowired
	private FornecedorRepository fornecedorRepository;
	
	@Override
	public void preProcess(AtomicReference<Object> tempVar) {
		// TODO Auto-generated method stub
	}

	@Override
	public void processMessage(Message message) {

		EMS0113Input input = (EMS0113Input) message.getBody();
		
		if(distribuidorService.isDistribuidor(input.getCodigoDistribuidor())) {
			
			this.inserirDescontoLogistica(input, message);
		}

		else{
			ndsiLoggerFactory.getLogger().logInfo(message, EventoExecucaoEnum.SEM_DOMINIO, "Codigo do Distribuidor invalido: "+input.getCodigoDistribuidor());
		}

	}
	
	/*
	 * Inseri registro de Desconto de Logistica caso não exista registro na base de dados
	 */
	private void inserirDescontoLogistica(EMS0113Input input,Message message){
		
	    String codigoDistribuidor = 
                message.getHeader().get(MessageHeaderProperties.CODIGO_DISTRIBUIDOR.getValue()).toString();
        
        Fornecedor fornecedor = this.fornecedorRepository.obterFornecedorPorCodigoInterface(Integer.valueOf(codigoDistribuidor));
	    
        if (fornecedor == null) {
            
            ndsiLoggerFactory.getLogger().logInfo(message, EventoExecucaoEnum.RELACIONAMENTO,
                "Fornecedor/Distribuidor não encontrado. " + codigoDistribuidor);
            
            return;
        }
        
		DescontoLogistica descontoLogistica =
	        descontoLogisticaService.obterDescontoLogistica(input.getTipoDesconto(),
	                                                        fornecedor.getId(),
	                                                        input.getDataInicioDesconto(),
	                                                        input.getPercentDesconto());
	    
		if(descontoLogistica == null) {
			
			descontoLogistica = new DescontoLogistica();
			descontoLogistica.setTipoDesconto(input.getTipoDesconto());
			descontoLogistica.setPercentualDesconto(input.getPercentDesconto());
			descontoLogistica.setPercentualPrestacaoServico(input.getPercentPrestServico());
			descontoLogistica.setDataInicioVigencia(input.getDataInicioDesconto());
			descontoLogistica.setFornecedor(fornecedor);
						
			getSession().persist(descontoLogistica);
			
			ndsiLoggerFactory.getLogger().logInfo(message
					, EventoExecucaoEnum.INF_DADO_ALTERADO
					, "Desconto logística incluido com sucesso. Tipo Desconto: "+ descontoLogistica.getTipoDesconto());
		}else{
			
			ndsiLoggerFactory.getLogger().logInfo(message
					, EventoExecucaoEnum.INF_DADO_ALTERADO
					, "Desconto logística já existe. Tipo Desconto: "+ descontoLogistica.getTipoDesconto());
			
		}
	}

	@Override
	public void posProcess(Object tempVar) {
		// TODO Auto-generated method stub
	}
	
}
