package br.com.abril.nds.integracao.ems0140.processor;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.abril.nds.integracao.engine.MessageProcessor;
import br.com.abril.nds.integracao.engine.log.NdsiLoggerFactory;import br.com.abril.nds.integracao.model.canonic.EMS0140Input;
import br.com.abril.nds.integracao.model.canonic.EMS0140InputItem;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.fiscal.NotaFiscalEntradaFornecedor;
import br.com.abril.nds.model.integracao.Message;
import br.com.abril.nds.repository.AbstractRepository;
import br.com.abril.nds.repository.ProdutoRepository;
import br.com.abril.nds.repository.TipoProdutoRepository;

@Component
public class EMS0140MessageProcessor extends AbstractRepository implements MessageProcessor {

	@Autowired
	private NdsiLoggerFactory ndsiLoggerFactory;
	
	@Autowired
	private TipoProdutoRepository tipoProdutoRepository;
	
	@Autowired
	private ProdutoRepository produtoRepository;
	
	@Override
	public void preProcess(AtomicReference<Object> tempVar) {
		tempVar.set(new ArrayList<NotaFiscalEntradaFornecedor>());
	}

	@Override
	public void processMessage(Message message) {
		EMS0140Input input = (EMS0140Input) message.getBody();
		
		Integer distribuidor = input.getDistribuidor();
		System.out.println(distribuidor);
	}
	
	private Produto getProduto(EMS0140InputItem item) {
		
		return null;
	}
	
	@Override
	public void posProcess(Object tempVar) {
		
	}

}
