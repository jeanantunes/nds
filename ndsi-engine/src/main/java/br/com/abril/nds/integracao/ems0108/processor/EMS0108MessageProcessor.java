package br.com.abril.nds.integracao.ems0108.processor;

import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.atomic.AtomicReference;

import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.abril.nds.integracao.ems0108.inbound.EMS0108Input;
import br.com.abril.nds.integracao.engine.MessageProcessor;
import br.com.abril.nds.integracao.engine.data.Message;
import br.com.abril.nds.integracao.engine.log.NdsiLoggerFactory;
import br.com.abril.nds.integracao.service.DistribuidorService;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.integracao.EventoExecucaoEnum;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.repository.impl.AbstractRepository;

@Component
public class EMS0108MessageProcessor extends AbstractRepository implements
		MessageProcessor {

	
	@Autowired
	private NdsiLoggerFactory ndsiLoggerFactory;

	@Autowired
	private DistribuidorService distribuidorService;
	
	private EMS0108MessageProcessor() {

	}

	@Override
	public void preProcess(AtomicReference<Object> tempVar) {
		// TODO Auto-generated method stub
	}

	/**
	 * Processa as linhas do arquivo da interface EMS0108
	 */
	@Override
	public void processMessage(Message message) {
		EMS0108Input input = (EMS0108Input) message.getBody();
		
		// Verifica se existe Produto		
		Produto produto = recuperaProduto(input.getCodigoPublicacao()); 
		if (null == produto) {
			ndsiLoggerFactory.getLogger().logWarning(
					message,
					EventoExecucaoEnum.HIERARQUIA,
					String.format( "Produto %1$s não encontrado.", input.getCodigoPublicacao() )
				);
			return ;
		} else {
			// caso exista atualiza o peso do produto
			produto.setPeso(input.getPesoProduto());
		}
		
		
		//Verifica se existe Produto Edicao 
		ProdutoEdicao produtoEdicaoLancamento = this.recuperarProdutoEdicao(input.getCodigoPublicacao(), input.getEdicaoLancamento());		
		if (null == produtoEdicaoLancamento) {
			produtoEdicaoLancamento = inserirProdutoEdicao(input, produto);
			ndsiLoggerFactory.getLogger().logWarning(
					message,
					EventoExecucaoEnum.INF_DADO_ALTERADO,
					String.format( "Produto %1$s Edicao %2$s cadastrada. Necessário atualizar o preço.", input.getCodigoPublicacao(), produtoEdicaoLancamento.getNumeroEdicao().toString() )
				);
		}

		
		
		// Determinar datas. Realizar insert ou update
		Date dataLcto = null;
		Date dataRec = null;
		Date dataOperacao = distribuidorService.obter().getDataOperacao();
		Lancamento lancamento = new Lancamento();
/*
		if (input.getEdicaoRecolhimento() != null
				&& input.getDataLancamentoRecolhimentoProduto().compareTo(
						dataOperacao) >= 0) {

			dataLcto = input.getDataLancamentoRecolhimentoProduto();

			// Soma o número de dias a recolher
			Calendar cal = Calendar.getInstance();
			cal.setTime(dataLcto);
			cal.add(Calendar.DATE, numeroDias); 
			dataRec = cal.getTime();

			// Insert
			lancamento.setDataCriacao(new Date());
			lancamento.setDataLancamentoDistribuidor(dataLcto);
			lancamento.setDataLancamentoPrevista(dataLcto);
			lancamento.setDataRecolhimentoDistribuidor(dataRec);
			lancamento.setDataRecolhimentoPrevista(dataRec);
			lancamento.setDataStatus(new Date());
			lancamento.setNumeroReprogramacoes(null);
			lancamento.setProdutoEdicao(produtoEdicaoLancamento);
			lancamento.setReparte(BigInteger.valueOf(0));

			// lancamento.setStatus(status);
			// lancamento.setTipoLancamento(tipoLancamento);
			getSession().persist(lancamento);
		}

		if (input.getEdicaoRecolhimento() != null
				&& input.getDataLancamentoRecolhimentoProduto().before(
						dataOperacao)) {
			dataLcto = input.getDataLancamentoRecolhimentoProduto();
			dataRec = input.getDataLancamentoRecolhimentoProduto();

			sql = new StringBuilder();
			sql.append("SELECT la FROM Lancamento la ");
			sql.append("	   JOIN FETCH pe.produto p ");
			sql.append("WHERE  p.codigo = :codigoProduto ");

			// edição de lançamento
			if (input.getEdicaoLancamento() != null) {
				sql.append("	   la.dataLancamentoPrevista = :dataComparar ");
			}
			// edição recolhimento
			if (input.getEdicaoRecolhimento() != null) {
				sql.append("	   p.dataRecolhimentoPrevista = :dataComparar ");
			}

			query = getSession().createQuery(sql.toString());
			query.setMaxResults(1);
			query.setParameter("codigoProduto", produto.getCodigo());
			query.setParameter("dataComparar", dataLcto);

			lancamento = null;

			lancamento = (Lancamento) query.uniqueResult();
			if (null != lancamento) {
				// FIXME Não encontrou lancamento. Realizar Log
				// Passar para a próxima linha
				return;
			}

			// update
			lancamento.setDataLancamentoDistribuidor(dataLcto);
			lancamento.setDataLancamentoPrevista(dataLcto);
			lancamento.setDataRecolhimentoDistribuidor(dataRec);
			lancamento.setDataRecolhimentoPrevista(dataRec);
			lancamento.setDataStatus(new Date());
			lancamento.setNumeroReprogramacoes(null);
			lancamento.setProdutoEdicao(produtoEdicaoLancamento);
			lancamento.setAlteradoInteface(true);
		}
		*/
	}

	private ProdutoEdicao inserirProdutoEdicao(EMS0108Input input, Produto produto) {
		
		ProdutoEdicao produtoEdicao = new ProdutoEdicao();
		
		produtoEdicao.setProduto(produto);
		produtoEdicao.setNumeroEdicao(input.getEdicaoLancamento());
		produtoEdicao.setPeso(input.getPesoProduto());
		produtoEdicao.setCodigoDeBarras(input.getCodigoBarrasFisicoProduto());
		
		// setar default baseado no produto		
		produtoEdicao.setAtivo(true);
		produtoEdicao.setPacotePadrao(produto.getPacotePadrao());
		produtoEdicao.setPeb(produto.getPeb());
		
		this.getSession().persist(produtoEdicao);
		
		return produtoEdicao;
	}

	private ProdutoEdicao recuperarProdutoEdicao(String codigoPublicacao,
			Long edicao) {
		
		// Obter o produto
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT pe ");
		sql.append("FROM   ProdutoEdicao pe ");
		sql.append("	   JOIN FETCH pe.produto p ");
		sql.append("WHERE ");
		sql.append("	   pe.numeroEdicao = :numeroEdicao ");
		sql.append("	   AND p.codigo    = :codigoProduto ");

		Query query = getSession().createQuery(sql.toString());

		query.setParameter("numeroEdicao", edicao);
		query.setParameter("codigoProduto", codigoPublicacao);

		return (ProdutoEdicao) query.uniqueResult();
	}

	private Produto recuperaProduto(String codigoPublicacao) {
		// Obter o produto
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT p ");
		sql.append("FROM   Produto p ");
		sql.append("WHERE ");
		sql.append("	   AND p.codigo    = :codigoProduto ");

		Query query = getSession().createQuery(sql.toString());

		query.setParameter("codigoProduto", codigoPublicacao);

		return (Produto) query.uniqueResult();
	}

	@Override
	public void posProcess(Object tempVar) {
		// TODO Auto-generated method stub
	}
	
}