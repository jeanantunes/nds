package br.com.abril.nds.integracao.ems0125.processor;

import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.integracao.engine.MessageProcessor;
import br.com.abril.nds.integracao.engine.data.Message;
import br.com.abril.nds.integracao.engine.log.NdsiLoggerFactory;
import br.com.abril.nds.integracao.model.canonic.EMS0125Input;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.integracao.EventoExecucaoEnum;
import br.com.abril.nds.repository.impl.AbstractRepository;

@Component
public class EMS0125MessageProcessor extends AbstractRepository implements
		MessageProcessor {

	@Autowired
	private NdsiLoggerFactory ndsiLoggerFactory;

	@Override
	public void preProcess() {
		// TODO Auto-generated method stub
	}

	@Override
	public void processMessage(Message message) {
		EMS0125Input input = (EMS0125Input) message.getBody();

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

		if (null == produtoEdicao) {

			// Não encontrou o Produto / ProdutoEdicao Realizar Log
			ndsiLoggerFactory.getLogger().logWarning(
					message,
					EventoExecucaoEnum.SEM_DOMINIO,
					"Produto " + input.getCodProd() + " e Produto Edicao "
							+ input.getEdicao() + "nao encontrado.");
			return;
		}

		produtoEdicao.setChamadaCapa(input.getCodBarra());
		ndsiLoggerFactory.getLogger().logInfo(message,
				EventoExecucaoEnum.INF_DADO_ALTERADO,
				"Atualizacao do ProdutoEdicao " + input.getEdicao());

	}
	
	@Override
	public void posProcess() {
		// TODO Auto-generated method stub
	}
	
}
