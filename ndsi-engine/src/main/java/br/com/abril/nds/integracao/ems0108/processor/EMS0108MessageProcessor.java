package br.com.abril.nds.integracao.ems0108.processor;

import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.atomic.AtomicReference;

import org.hibernate.Query;
import org.springframework.stereotype.Component;

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
	public void preProcess(AtomicReference<Object> tempVar) {
		// TODO Auto-generated method stub
	}

	/**
	 * Processa as linhas do arquivo da interface EMS0108
	 */
	@Override
	public void processMessage(Message message) {
		EMS0108Input input = (EMS0108Input) message.getBody();
		
		
		// TODO:Deverá procurar a publicacao
		// se não existir loga
		
		
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
			numeroDias = produtoEdicao.getPeb(); // REGRA CRIADA DA MENTE INSANA DO PROGRAMADOR (não esta na EMS) !!!!
		} else {
			// FIXME Não encontrou o produto. Realizar Log
			// Passar para a próxima linha 
			// ISSO TA ERRADO, deve-se INSERIR NA TABELA PRODUTO_EDICAO DE ACORDO COM A EMS!!!!
			return;
		}

		// Determinar datas. Realizar insert ou update
		Date dataLcto = null;
		Date dataRec = null;
		Date dataCriacaoArquivo = (Date) message.getHeader().get(
				"FILE_CREATION_DATE"); // ISSO DEVE SER A DATA DE OPERACAO!!!!

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
			lancamento.setNumeroReprogramacoes(null);
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
			lancamento.setNumeroReprogramacoes(null);
			lancamento.setProdutoEdicao(produtoEdicao);
			lancamento.setAlteradoInteface(true);
		}
	}

	@Override
	public void posProcess(Object tempVar) {
		// TODO Auto-generated method stub
	}
	
}