package br.com.abril.nds.integracao.ems0125.processor;

import java.util.concurrent.atomic.AtomicReference;

import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.abril.nds.integracao.engine.MessageProcessor;
import br.com.abril.nds.integracao.engine.log.NdsiLoggerFactory;
import br.com.abril.nds.integracao.model.canonic.EMS0125Input;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.integracao.EventoExecucaoEnum;
import br.com.abril.nds.model.integracao.Message;
import br.com.abril.nds.repository.AbstractRepository;

@Component
public class EMS0125MessageProcessor extends AbstractRepository implements
		MessageProcessor {

	@Autowired
	private NdsiLoggerFactory ndsiLoggerFactory;

	@Override
	public void preProcess(AtomicReference<Object> tempVar) {
		// TODO Auto-generated method stub
	}

	@Override
	public void processMessage(Message message) {
		
		EMS0125Input input = (EMS0125Input) message.getBody();

		if(input.getCodProd()==null || input.getCodProd().trim().equals("")){
			
			ndsiLoggerFactory.getLogger().logError(
				message,
				EventoExecucaoEnum.RELACIONAMENTO,
				"Código do Produto Nulo ou Vazio."
				+ " Produto "+ input.getCodProd());
			return;
		}
		
		if(input.getEdicao()==null || input.getEdicao().intValue()==0){
			
			ndsiLoggerFactory.getLogger().logError(
				message,
				EventoExecucaoEnum.RELACIONAMENTO,
				"Edição do Produto Nula ou Vazia."
						+ " Produto "+ input.getCodProd() 
						+ " Edição "+ input.getEdicao());
			
			return;
		}
		
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT pe  ");
		sql.append("FROM ProdutoEdicao pe ");
		sql.append("JOIN FETCH pe.produto p ");
		sql.append("WHERE ");
		sql.append("     pe.numeroEdicao = :numeroEdicao ");
		sql.append("  AND   p.codigo = :codigo ");

		Query query = getSession().createQuery(sql.toString());
		query.setParameter("numeroEdicao", input.getEdicao());
		query.setParameter("codigo", input.getCodProd());

		ProdutoEdicao produtoEdicao = null;

		produtoEdicao = (ProdutoEdicao) query.uniqueResult();

		if (null != produtoEdicao) {

			if(input.getChamadaCapa()!=null && !produtoEdicao.getChamadaCapa().equals(input.getChamadaCapa())){
				 ndsiLoggerFactory.getLogger().logInfo(message,
						EventoExecucaoEnum.INF_DADO_ALTERADO,
						"Alteração da Chamada de Capa"
						+" de "+produtoEdicao.getChamadaCapa()
						+" para "+input.getChamadaCapa()
						+" Produto "+input.getCodProd()
						+" Edição " + input.getEdicao() );
				
				 produtoEdicao.setChamadaCapa(input.getChamadaCapa());
				 this.getSession().merge(produtoEdicao);
			} else {
			
				// Não encontrou o Produto / ProdutoEdicao Realizar Log
				ndsiLoggerFactory.getLogger().logWarning(
					message,
					EventoExecucaoEnum.SEM_DOMINIO,
					"Produto " + input.getCodProd() + " Edição "
							+ input.getEdicao() + " não encontrado.");
			}
		}

	}
	
	@Override
	public void posProcess(Object tempVar) {
		// TODO Auto-generated method stub
	}
	
}
