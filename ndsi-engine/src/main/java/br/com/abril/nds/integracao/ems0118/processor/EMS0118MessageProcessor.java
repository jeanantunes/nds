package br.com.abril.nds.integracao.ems0118.processor;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.integracao.ems0118.inbound.EMS0118Input;
import br.com.abril.nds.integracao.engine.MessageProcessor;
import br.com.abril.nds.integracao.engine.data.Message;
import br.com.abril.nds.integracao.engine.log.NdsiLoggerFactory;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.integracao.EventoExecucaoEnum;
import br.com.abril.nds.repository.impl.AbstractRepository;

/**
 * @author Jones.Costa
 * @version 1.0
 */
@Component
public class EMS0118MessageProcessor extends AbstractRepository implements
		MessageProcessor {

	@Autowired
	private NdsiLoggerFactory ndsiLoggerFactory;

	@Override
	public void preProcess() {
		// TODO Auto-generated method stub
	}

	@Override
	public void processMessage(Message message) {

		EMS0118Input input = (EMS0118Input) message.getBody();

		// Obter fator de desconto
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT dist FROM  Distribuidor dist ");

		Query query = getSession().createQuery(sql.toString());
		Distribuidor distribuidor = (Distribuidor) query.uniqueResult();

		// Obter Produto/Edição
		StringBuilder cmd = new StringBuilder();
		cmd.append("SELECT prodEdicao FROM ProdutoEdicao prodEdicao ");
		cmd.append("			JOIN FETCH prodEdicao.produto prodCod");
		cmd.append(" WHERE ");
		cmd.append("	prodCod.codigo = :codigoProduto ");
		cmd.append(" AND	prodEdicao.numeroEdicao = :numeroEdicao ");

		Query consulta = getSession().createQuery(cmd.toString());
		consulta.setParameter("codigoProduto", input.getCodigoPublicacao());
		consulta.setParameter("numeroEdicao", input.getEdicao());

		ProdutoEdicao produtoEdicao = (ProdutoEdicao) consulta.uniqueResult();
		if (null != produtoEdicao) {
			// Atualiza valor de venda (PREÇO CAPA)
			produtoEdicao.setPrecoVenda(input.getPreco());

			// Define valor de custo
			double preco = input.getPreco().doubleValue();
			double fator = distribuidor.getFatorDesconto().doubleValue();
			fator = 1 - fator / 100;
			double precoCusto = preco * fator;

			// Atualiza valor de custo
			produtoEdicao.setPrecoCusto(new BigDecimal(precoCusto).setScale(2,
					RoundingMode.HALF_DOWN));

			System.out.println("breakpoint");

		} else {
			// NAO ENCONTROU Produto/Edicao, DEVE LOGAR
			ndsiLoggerFactory.getLogger().logError(
					message,
					EventoExecucaoEnum.RELACIONAMENTO,
					"Nenhum resultado encontrado para Produto: "
							+ input.getCodigoPublicacao() + " e Edição: "
							+ input.getEdicao() + " na tabela produto_edicao");

		}

	}

	@Override
	public void posProcess() {
		// TODO Auto-generated method stub
	}
	
}
