package br.com.abril.nds.integracao.ems0126.processor;

import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.integracao.engine.MessageProcessor;
import br.com.abril.nds.integracao.engine.data.Message;
import br.com.abril.nds.integracao.engine.log.NdsiLoggerFactory;
import br.com.abril.nds.integracao.model.canonic.EMS0126Input;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.integracao.EventoExecucaoEnum;
import br.com.abril.nds.repository.impl.AbstractRepository;

/**
 * @author Jones.Costa
 * @version 1.0
 */
@Component
public class EMS0126MessageProcessor extends AbstractRepository implements
		MessageProcessor {

	@Autowired
	private NdsiLoggerFactory ndsiLoggerFactory;

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

		EMS0126Input input = (EMS0126Input) message.getBody();

		// Localizar Produto/Edicao
		StringBuilder sql = new StringBuilder();

		sql.append("SELECT prodEdicao FROM ProdutoEdicao prodEdicao ");
		sql.append("			JOIN FETCH prodEdicao.produto prodCod");
		sql.append(" WHERE ");
		sql.append("	prodCod.codigo = :codigoProduto ");
		sql.append(" AND	prodEdicao.numeroEdicao = :numeroEdicao ");

		Query query = getSession().createQuery(sql.toString());
		query.setParameter("codigoProduto", input.getCodigoProduto());
		query.setParameter("numeroEdicao", input.getEdicao());

		ProdutoEdicao produtoEdicao = (ProdutoEdicao) query.uniqueResult();

		if (null != produtoEdicao) {
			// Inserir codigo de barras
			produtoEdicao.setCodigoDeBarras(input.getCodigoBarras());

		} else {
			ndsiLoggerFactory.getLogger().logError(
					message,
					EventoExecucaoEnum.RELACIONAMENTO,
					"Nenhum resultado encontrado para Produto: "
							+ input.getCodigoProduto() + " e Edição: "
							+ input.getEdicao() + " na tabela produto_edicao");

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
