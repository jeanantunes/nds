package br.com.abril.nds.integracao.ems0113.processor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.integracao.engine.MessageProcessor;
import br.com.abril.nds.integracao.engine.data.Message;
import br.com.abril.nds.integracao.engine.log.NdsiLoggerFactory;
import br.com.abril.nds.integracao.model.canonic.EMS0113Input;
import br.com.abril.nds.integracao.service.DistribuidorService;
import br.com.abril.nds.model.cadastro.DescontoLogistica;
import br.com.abril.nds.model.integracao.EventoExecucaoEnum;
import br.com.abril.nds.repository.impl.AbstractRepository;

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
	
	@Override
	public void preProcess() {
		// TODO Auto-generated method stub
	}

	@Override
	public void preProcess(Message message) {
		// TODO Auto-generated method stub
	}
	
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
			
			getSession().persist(descontoLogistica);
			
		}

		else{
			ndsiLoggerFactory.getLogger().logInfo(message, EventoExecucaoEnum.SEM_DOMINIO, "Codigo do Distribuidor invalido: "+input.getCodigoDistribuidor());
		}


		

	}

	@Override
	public void posProcess() {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void posProcess(Message message) {
		// TODO Auto-generated method stub
	}
	
}
