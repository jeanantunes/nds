package br.com.abril.nds.integracao.ems0108.processor;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;

import org.hibernate.Query;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.integracao.ems0108.inbound.EMS0108Input;
import br.com.abril.nds.integracao.engine.MessageProcessor;
import br.com.abril.nds.integracao.engine.data.Message;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.repository.impl.AbstractRepository;

@Component
public class EMS0108MessageProcessor extends AbstractRepository implements
		MessageProcessor {

	private EMS0108MessageProcessor() {

	}

	@Override
	public void preProcess() {
		// TODO Auto-generated method stub
	}

	@Override
	public void preProcess(Message message) {
		// TODO Auto-generated method stub
	}
	
	/**
	 * Processa as linhas do arquivo da interface EMS0108
	 */
	@Override
	public void processMessage(Message message) {
		EMS0108Input input = (EMS0108Input) message.getBody();
		// Obter o produto
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT pe ");
		sql.append("FROM   ProdutoEdicao pe ");
		sql.append("	   JOIN FETCH pe.produto p ");
		sql.append("WHERE ");
		sql.append("	   pe.numeroEdicao = :numeroEdicao ");
		sql.append("	   AND p.codigo    = :codigoProduto ");

		Query query = getSession().createQuery(sql.toString());

		query.setParameter("numeroEdicao", input.getEdicao());
		query.setParameter("codigoProduto", input.getCodigoPublicacao()
				.toString());

		ProdutoEdicao produtoEdicao = null;
		Produto produto = null;

		int numeroDias;

		produtoEdicao = ((ProdutoEdicao) query.uniqueResult());
		if (null != produtoEdicao) {
			produto = produtoEdicao.getProduto();
			numeroDias = produtoEdicao.getPeb();
		} else {
			// FIXME Não encontrou o produto. Realizar Log
			// Passar para a próxima linha
			return;
		}

		// Determinar datas. Realizar insert ou update
		Date dataLcto = null;
		Date dataRec = null;
		Date dataCriacaoArquivo = (Date) message.getHeader().get(
				"FILE_CREATION_DATE");

		Lancamento lancamento = new Lancamento();

		if (input.getEdicao() != null
				&& input.getDataLancamentoRecolhimentoProduto().compareTo(
						dataCriacaoArquivo) >= 0) {

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
			lancamento.setNumeroReprogramacoes(0);
			lancamento.setProdutoEdicao(produtoEdicao);
			lancamento.setReparte(BigInteger.valueOf(0));

			// lancamento.setStatus(status);
			// lancamento.setTipoLancamento(tipoLancamento);
			getSession().persist(lancamento);
		}

		if (input.getEdicaoRecolhimento() != null
				&& input.getDataLancamentoRecolhimentoProduto().before(
						dataCriacaoArquivo)) {
			dataLcto = input.getDataLancamentoRecolhimentoProduto();
			dataRec = input.getDataLancamentoRecolhimentoProduto();

			sql = new StringBuilder();
			sql.append("SELECT la FROM Lancamento la ");
			sql.append("	   JOIN FETCH pe.produto p ");
			sql.append("WHERE  p.codigo = :codigoProduto ");

			// edição de lançamento
			if (input.getEdicao() != null) {
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
			// TODO Solicitar revisão modelo de numeroReprogramacoes: mapear
			// para Integer em vez de int. Deve ser null em vez de 0
			lancamento.setNumeroReprogramacoes(0);
			lancamento.setProdutoEdicao(produtoEdicao);
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