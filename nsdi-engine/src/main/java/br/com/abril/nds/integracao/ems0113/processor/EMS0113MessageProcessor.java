package br.com.abril.nds.integracao.ems0113.processor;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.abril.nds.integracao.engine.MessageProcessor;
import br.com.abril.nds.integracao.engine.data.Message;
import br.com.abril.nds.integracao.engine.log.NdsiLoggerFactory;
import br.com.abril.nds.integracao.model.canonic.EMS0113Input;
import br.com.abril.nds.integracao.service.impl.DistribuidorServiceImpl;
import br.com.abril.nds.model.cadastro.DescontoLogistica;
import br.com.abril.nds.model.integracao.EventoExecucaoEnum;

/**
 * @author Jones.Costa
 * @version 1.0
 */
@Component
public class EMS0113MessageProcessor implements MessageProcessor {

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private NdsiLoggerFactory ndsiLoggerFactory;
	
	@Autowired
	private DistribuidorServiceImpl distribuidorServiceImpl;
	
	@Override
	public void processMessage(Message message) {

		EMS0113Input input = (EMS0113Input) message.getBody();
		
		if(distribuidorServiceImpl.isDistribuidor(input.getCodigoDistribuidor())){
			
			DescontoLogistica descontoLogistica = new DescontoLogistica();
			
			descontoLogistica.setId(null);//auto increment
			descontoLogistica.setTipoDesconto(input.getTipoDesconto());
			descontoLogistica.setPercentualDesconto(input.getPercentDesconto());
			descontoLogistica.setPercentualPrestacaoServico(input.getPercentPrestServico());
			descontoLogistica.setDataInicioVigencia(input.getDataInicioDesconto());
			
			entityManager.persist(descontoLogistica);
			
		}

		else{
			ndsiLoggerFactory.getLogger().logInfo(message, EventoExecucaoEnum.SEM_DOMINIO, "Codigo do Distribuidor invalido: "+input.getCodigoDistribuidor());
		}


		

	}

}
