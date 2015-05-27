package br.com.abril.nds.integracao.ems0118.processor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.concurrent.atomic.AtomicReference;

import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.abril.nds.integracao.ems0118.inbound.EMS0118Input;
import br.com.abril.nds.integracao.engine.MessageProcessor;
import br.com.abril.nds.integracao.engine.log.NdsiLoggerFactory;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.integracao.EventoExecucaoEnum;
import br.com.abril.nds.model.integracao.Message;
import br.com.abril.nds.repository.AbstractRepository;

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
	public void preProcess(AtomicReference<Object> tempVar) {
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
			produtoEdicao.setPrecoVenda(tratarValoresNulo(input.getPreco(),produtoEdicao.getPrecoVenda()));
			produtoEdicao.setPrecoPrevisto(tratarValoresNulo( input.getPreco(),produtoEdicao.getPrecoPrevisto()));

			// Define valor de custo
			double preco = produtoEdicao.getPrecoVenda().doubleValue();
			double fator = distribuidor.getFatorDesconto().doubleValue();
			fator = 1 - fator / 100;
			double precoCusto = preco * fator;

			// Atualiza valor de custo
					
			if(precoCusto>0){
			
				this.ndsiLoggerFactory.getLogger().logInfo(
					message,
					EventoExecucaoEnum.INF_DADO_ALTERADO,
					"Atualização do Preço"
					+" de "+produtoEdicao.getPrecoCusto()
					+" para "+new BigDecimal(precoCusto).setScale(2, RoundingMode.HALF_DOWN)
					+" Produto " + input.getCodigoPublicacao() + " Edição " + input.getEdicao() );
			
				produtoEdicao.setPrecoCusto(new BigDecimal(precoCusto).setScale(2, RoundingMode.HALF_DOWN));
				this.getSession().merge(produtoEdicao);
			}
		} else {
			// NAO ENCONTROU Produto/Edicao, DEVE LOGAR
			ndsiLoggerFactory.getLogger().logError(
					message,
					EventoExecucaoEnum.RELACIONAMENTO,
					"Produto "
					+ input.getCodigoPublicacao() + "  Edição "
					+ input.getEdicao() 
					+" não encontrado.");

		}

	}

	private BigDecimal tratarValoresNulo(BigDecimal valor1, BigDecimal valor2) {
		
		if (valor1 != null) {
			return valor1;
		} else if (valor2 != null) {
			return valor2;
		}
		
		return valor1 == null ? valor2 != null ? valor2 : BigDecimal.ZERO : valor1;
	}
	
	@Override
	public void posProcess(Object tempVar) {
		// TODO Auto-generated method stub
	}
	
}