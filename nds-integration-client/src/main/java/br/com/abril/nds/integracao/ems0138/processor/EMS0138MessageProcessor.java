package br.com.abril.nds.integracao.ems0138.processor;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import org.hibernate.Query;
import org.lightcouch.CouchDbClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.abril.nds.integracao.engine.MessageProcessor;
import br.com.abril.nds.integracao.engine.log.NdsiLoggerFactory;
import br.com.abril.nds.model.fiscal.NotaFiscalSaidaFornecedor;
import br.com.abril.nds.model.fiscal.nota.NotaFiscal;
import br.com.abril.nds.model.integracao.Message;
import br.com.abril.nds.repository.AbstractRepository;
import br.com.abril.nds.repository.ProdutoEdicaoRepository;
import br.com.abril.nds.service.integracao.DistribuidorService;

@Component
public class EMS0138MessageProcessor extends AbstractRepository implements MessageProcessor {

	private static final Logger LOGGER = LoggerFactory.getLogger(EMS0138MessageProcessor.class);

	@Autowired
	private DistribuidorService distribuidorService;
	
	@Autowired
	private ProdutoEdicaoRepository produtoEdicaoRepository;
	
	@Autowired
	private NdsiLoggerFactory ndsiLoggerFactory; 
	
	@Override
	public void preProcess(AtomicReference<Object> tempVar) {

		List<NotaFiscalSaidaFornecedor> notasFiscais = obterNotasFiscais();
		
		tempVar.set(notasFiscais);
		
	}

	@Override
	public void processMessage(Message message) {
		
		CouchDbClient cdbc = null;
		
		String codigoDistribuidor = distribuidorService.obter().getCodigoDistribuidorDinap();
		
		List<NotaFiscalSaidaFornecedor> notasFiscais = (List<NotaFiscalSaidaFornecedor>) message.getBody();
		
		for(NotaFiscalSaidaFornecedor nf : notasFiscais) {
		
			try {
				//nf.setTipoDocumento("EMS0139");
				cdbc = this.getCouchDBClient(codigoDistribuidor);
				cdbc.save(nf);
			} catch(Exception e) {
				LOGGER.error("Erro executando importação de Chamada Encalhe Prodin.", e);
			} finally {
				if (cdbc != null) {
					cdbc.shutdown();
				}			
			}
				
		}

	}

	@SuppressWarnings("unchecked")
	private List<NotaFiscalSaidaFornecedor> obterNotasFiscais() {

		StringBuilder hql = new StringBuilder();
		hql.append(" select nf ")
			.append("from NotaFiscalSaidaFornecedor nf ");
		
		Query query = this.getSession().createQuery(hql.toString());
		
		return query.list();
		
	}

	@Override
	public void posProcess(Object tempVar) {
		// TODO Auto-generated method stub

	}

}
