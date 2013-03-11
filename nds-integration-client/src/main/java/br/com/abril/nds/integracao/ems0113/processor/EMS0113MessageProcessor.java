package br.com.abril.nds.integracao.ems0113.processor;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicReference;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.abril.nds.integracao.engine.MessageProcessor;
import br.com.abril.nds.integracao.engine.log.NdsiLoggerFactory;
import br.com.abril.nds.integracao.model.canonic.EMS0113Input;
import br.com.abril.nds.model.cadastro.DescontoLogistica;
import br.com.abril.nds.model.integracao.EventoExecucaoEnum;
import br.com.abril.nds.model.integracao.Message;
import br.com.abril.nds.repository.AbstractRepository;
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
	
	@Override
	public void preProcess(AtomicReference<Object> tempVar) {
		// TODO Auto-generated method stub
	}

	@Override
	public void processMessage(Message message) {

		EMS0113Input input = (EMS0113Input) message.getBody();
		
		if(distribuidorService.isDistribuidor(input.getCodigoDistribuidor())){
			
			DescontoLogistica descontoLogistica = descontoLogisticaService.obterPorTipoDesconto(input.getTipoDesconto());
			
			if (null != descontoLogistica ) {
								
				descontoLogistica.setPercentualDesconto(input.getPercentDesconto());
				descontoLogistica.setPercentualPrestacaoServico(input.getPercentPrestServico());
				descontoLogistica.setDataInicioVigencia(input.getDataInicioDesconto());
				
				getSession().merge(descontoLogistica);
			} else {
				descontoLogistica = new DescontoLogistica();
				
				descontoLogistica.setId(null);//auto increment
				descontoLogistica.setTipoDesconto(input.getTipoDesconto());
				// Divisão por 100 Realizado em conjunto com Cesar Pop Punk
				// Divisão por 100 retirada em conjunto com Cesar 06/03/2013
				descontoLogistica.setPercentualDesconto(input.getPercentDesconto());
				descontoLogistica.setPercentualPrestacaoServico(input.getPercentPrestServico());
				descontoLogistica.setDataInicioVigencia(input.getDataInicioDesconto());
				
				getSession().persist(descontoLogistica);
			}
		}

		else{
			ndsiLoggerFactory.getLogger().logInfo(message, EventoExecucaoEnum.SEM_DOMINIO, "Codigo do Distribuidor invalido: "+input.getCodigoDistribuidor());
		}


		

	}

	@Override
	public void posProcess(Object tempVar) {
		// TODO Auto-generated method stub
	}
	
}
