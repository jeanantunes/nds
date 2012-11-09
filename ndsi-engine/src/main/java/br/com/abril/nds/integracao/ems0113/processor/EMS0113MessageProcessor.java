package br.com.abril.nds.integracao.ems0113.processor;

import java.util.concurrent.atomic.AtomicReference;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.abril.nds.integracao.engine.MessageProcessor;
import br.com.abril.nds.integracao.engine.data.Message;
import br.com.abril.nds.integracao.engine.log.NdsiLoggerFactory;
import br.com.abril.nds.integracao.model.canonic.EMS0113Input;
import br.com.abril.nds.integracao.service.DistribuidorService;
import br.com.abril.nds.model.cadastro.DescontoLogistica;
import br.com.abril.nds.model.integracao.EventoExecucaoEnum;
import br.com.abril.nds.repository.impl.AbstractRepository;
import br.com.abril.nds.service.DescontoLogisticaService;
import br.com.abril.nds.service.DescontoService;

/**
 * @author Jones.Costa
 * @version 1.0
 */
@Component

public class EMS0113MessageProcessor extends AbstractRepository implements MessageProcessor  {



	@Autowired
	private NdsiLoggerFactory ndsiLoggerFactory;
	
	@Autowired
	private DistribuidorService distribuidorServiceImpl;
	
	@Autowired
	private DescontoLogisticaService descontoLogisticaService;
	
	@Override
	public void preProcess(AtomicReference<Object> tempVar) {
		// TODO Auto-generated method stub
	}

	@Override
	public void processMessage(Message message) {

		EMS0113Input input = (EMS0113Input) message.getBody();
		
		if(distribuidorServiceImpl.isDistribuidor(input.getCodigoDistribuidor())){
			
			DescontoLogistica descontoLogistica = descontoLogisticaService.obterPorTipoDesconto(input.getTipoDesconto());
			
			if (null != descontoLogistica ) {
								
				// Divisão por 100 Realizado em conjunto com Cesar Pop Punk
				descontoLogistica.setPercentualDesconto(input.getPercentDesconto().floatValue()/100);
				descontoLogistica.setPercentualPrestacaoServico(input.getPercentPrestServico().floatValue()/100);
				descontoLogistica.setDataInicioVigencia(input.getDataInicioDesconto());
				
				getSession().merge(descontoLogistica);
			} else {
				descontoLogistica = new DescontoLogistica();
				
				descontoLogistica.setId(null);//auto increment
				descontoLogistica.setTipoDesconto(input.getTipoDesconto());
				// Divisão por 100 Realizado em conjunto com Cesar Pop Punk
				descontoLogistica.setPercentualDesconto(input.getPercentDesconto().floatValue()/100);
				descontoLogistica.setPercentualPrestacaoServico(input.getPercentPrestServico().floatValue()/100);
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
