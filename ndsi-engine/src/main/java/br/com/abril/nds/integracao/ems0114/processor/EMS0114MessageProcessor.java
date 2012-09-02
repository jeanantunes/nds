package br.com.abril.nds.integracao.ems0114.processor;

import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.abril.nds.integracao.engine.MessageHeaderProperties;
import br.com.abril.nds.integracao.engine.MessageProcessor;
import br.com.abril.nds.integracao.engine.data.Message;
import br.com.abril.nds.integracao.engine.log.NdsiLoggerFactory;
import br.com.abril.nds.integracao.model.canonic.EMS0114Input;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.integracao.EventoExecucaoEnum;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.model.planejamento.TipoLancamento;
import br.com.abril.nds.repository.impl.AbstractRepository;

@Component
public class EMS0114MessageProcessor extends AbstractRepository implements
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

		EMS0114Input input = (EMS0114Input) message.getBody();
		if (input == null) {
			this.ndsiLoggerFactory.getLogger().logError(
					message, EventoExecucaoEnum.ERRO_INFRA, "NAO ENCONTROU o Arquivo");
			return;
		}

		// Validar Distribuidor:
		Integer codDistribuidorSistema = (Integer) message.getHeader().get(
				MessageHeaderProperties.CODIGO_DISTRIBUIDOR);
		Integer codigoDistribuidorArquivo = Integer.parseInt(
				input.getCodDistrib());
		if (codDistribuidorSistema.longValue() != codigoDistribuidorArquivo.longValue()) {
			this.ndsiLoggerFactory.getLogger().logWarning(message,
					EventoExecucaoEnum.RELACIONAMENTO,
					"Distribuidor nao encontrato. Código: " 
					+ codDistribuidorSistema);
			return;
		}

		// Validar Produto/Edicao
		final String codigoProduto = input.getCodProd();
		final Long edicao = input.getEdicao();
		ProdutoEdicao produtoEdicao = this.obterProdutoEdicao(codigoProduto,
				edicao);
		if (produtoEdicao == null) {
			this.ndsiLoggerFactory.getLogger().logError(message,
					EventoExecucaoEnum.RELACIONAMENTO,
					"Impossivel realizar Insert/update - Nenhum resultado encontrado para Produto: "
							+ codigoProduto
							+ " e Edicao: " + edicao
							+ " na tabela produto_edicao");
			return;
		}		

		final Date dataRecolhimento = input.getDataRecolhimento();
		final Date dataGeracaoArquivo = input.getDataGeracaoArq();
		Lancamento lancamento = this.getLancamentoRecolhimentoMaisProximo(
				produtoEdicao, dataRecolhimento, dataGeracaoArquivo);
		if (lancamento == null) {
			this.ndsiLoggerFactory.getLogger().logError(message,
					EventoExecucaoEnum.RELACIONAMENTO,
					"SEM LANCAMENTOS com RECOLHIMENTO para Produto: "
							+ codigoProduto
							+ " e Edicao: " + edicao
							+ " na tabela produto_edicao");
			return;
		}
		
		
			
//			Lancamento lancamento = this.findLancamento(message);

			criarLancamentoConformeInput(lancamento, produtoEdicao, message);

	}

	/**
	 * Obtém o Produto Edição cadastrado previamente.
	 * 
	 * @param codigoPublicacao Código da Publicação.
	 * @param edicao Número da Edição.
	 * 
	 * @return
	 */
	private ProdutoEdicao obterProdutoEdicao(String codigoPublicacao,
			Long edicao) {

		try {

			Criteria criteria = this.getSession().createCriteria(
					ProdutoEdicao.class, "produtoEdicao");

			criteria.createAlias("produtoEdicao.produto", "produto");
			criteria.setFetchMode("produto", FetchMode.JOIN);

			criteria.add(Restrictions.eq("produto.codigo", codigoPublicacao));
			criteria.add(Restrictions.eq("produtoEdicao.numeroEdicao", edicao));

			return (ProdutoEdicao) criteria.uniqueResult();

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}	
	
	/**
	 * Obtém o Lançamento com a data de recolhimento mais próximo da data de 
	 * recolhimento desejado.
	 *  
	 * @param produtoEdicao
	 * @param dataRecolhimento Data de Recolhimento da Edição.
	 * @param dataGeracaoArquivo Data de Geração do Arquivo.
	 * 
	 * @return
	 */
	private Lancamento getLancamentoRecolhimentoMaisProximo(
			ProdutoEdicao produtoEdicao, Date dataRecolhimento, 
			Date dataGeracaoArquivo) {
		
		StringBuilder sql = new StringBuilder();
		
		sql.append("SELECT lcto FROM Lancamento lcto ");
		sql.append("      JOIN FETCH lcto.produtoEdicao pe ");
		sql.append("    WHERE pe = :produtoEdicao ");
		sql.append("      AND lcto.dataRecolhimentoPrevista > :dataGeracaoArquivo ");
		sql.append("      AND lcto.dataRecolhimentoPrevista = :dataRecolhimento ");
		sql.append(" ORDER BY lcto.dataRecolhimentoPrevista ASC");
		
		Query query = getSession().createQuery(sql.toString());
		query.setParameter("produtoEdicao", produtoEdicao);
		query.setDate("dataGeracaoArquivo", dataGeracaoArquivo);
		query.setDate("dataRecolhimento", dataRecolhimento);
		
		query.setMaxResults(1);
		query.setFetchSize(1);
		
		return (Lancamento) query.uniqueResult();
	}
	
	
	private void criarLancamentoConformeInput(Lancamento lancamento,
			ProdutoEdicao produtoEdicao, Message message) {
		EMS0114Input input = (EMS0114Input) message.getBody();

		if (lancamento != null) {

			if (!lancamento.getProdutoEdicao().getNumeroEdicao()
					.equals(produtoEdicao.getNumeroEdicao())) {

				lancamento.setProdutoEdicao(produtoEdicao);
				ndsiLoggerFactory.getLogger().logInfo(
						message,
						EventoExecucaoEnum.INF_DADO_ALTERADO,
						"Atualizacao do Produto Edicao para "
								+ produtoEdicao.getNumeroEdicao());
			}

			if (!lancamento.getDataRecolhimentoDistribuidor().equals(
					input.getDataRecolhimento())) {

				lancamento.setDataRecolhimentoPrevista(input
						.getDataRecolhimento());
				ndsiLoggerFactory.getLogger().logInfo(
						message,
						EventoExecucaoEnum.INF_DADO_ALTERADO,
						"Atualizacao da Data de Recolhimento Distribuidor para "
								+ input.getDataRecolhimento());
			}

		} else {

			Calendar data = Calendar.getInstance();

			lancamento = new Lancamento();
			lancamento.setDataCriacao(data.getTime());
			lancamento.setDataStatus(data.getTime());
			lancamento.setReparte(BigInteger.valueOf(0));
			lancamento.setDataLancamentoDistribuidor(data.getTime());
			lancamento.setDataLancamentoPrevista(data.getTime());
			lancamento.setStatus(StatusLancamento.EXPEDIDO);
			lancamento.setProdutoEdicao(produtoEdicao);

			if (produtoEdicao.isParcial()) {

				lancamento.setTipoLancamento(TipoLancamento.PARCIAL);

			} else {

				lancamento.setTipoLancamento(TipoLancamento.LANCAMENTO);
			}

			data.add(Calendar.DAY_OF_MONTH, produtoEdicao.getPeb());
			lancamento.setDataRecolhimentoDistribuidor(data.getTime());

			lancamento.setProdutoEdicao(produtoEdicao);
			lancamento.setDataRecolhimentoPrevista(input.getDataRecolhimento());

			getSession().persist(lancamento);
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