package br.com.abril.nds.repository.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.ValidacaoConfirmacaoDeExpedicaoFecharDiaDTO;
import br.com.abril.nds.dto.ValidacaoLancamentoFaltaESobraFecharDiaDTO;
import br.com.abril.nds.dto.ValidacaoRecebimentoFisicoFecharDiaDTO;
import br.com.abril.nds.model.StatusCobranca;
import br.com.abril.nds.model.StatusConfirmacao;
import br.com.abril.nds.model.fiscal.StatusNotaFiscalEntrada;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.repository.FecharDiaRepository;

@Repository
public class FecharDiaRepositoryImpl extends AbstractRepository implements FecharDiaRepository {

	@Override
	public boolean existeCobrancaParaFecharDia(Date diaDeOperaoMenosUm) {
		StringBuilder hql = new StringBuilder();
		hql.append(" from Cobranca c where ");		
		hql.append(" c.statusCobranca = :statusCobranca");		
		hql.append(" and c.dataVencimento = :diaDeOperaoMenosUm ");
		
		Query query = super.getSession().createQuery(hql.toString());
		
		query.setParameter("statusCobranca", StatusCobranca.NAO_PAGO);
		
		query.setParameter("diaDeOperaoMenosUm", diaDeOperaoMenosUm);
		
		return query.list().isEmpty() ? false : true;
	}

	@Override
	public boolean existeNotaFiscalSemRecebimentoFisico(Date dataOperacaoDistribuidor) {

		StringBuilder hql = new StringBuilder();

		hql.append(" select notaFiscal from NotaFiscalEntradaFornecedor notaFiscal ");		
		hql.append("WHERE notaFiscal.statusNotaFiscal != :statusNF AND notaFiscal.dataEmissao = :dataOperacao ");
		
		Query query = super.getSession().createQuery(hql.toString());
		
		query.setParameter("statusNF", StatusNotaFiscalEntrada.RECEBIDA);
		query.setParameter("dataOperacao", dataOperacaoDistribuidor);
		
		return query.list().isEmpty() ? true : false;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ValidacaoRecebimentoFisicoFecharDiaDTO> obterNotaFiscalComRecebimentoFisicoNaoConfirmado(Date dataOperacaoDistribuidor) {
		StringBuilder hql = new StringBuilder();

		hql.append(" select numero as numeroNotaFiscal from NotaFiscalEntradaFornecedor notaFiscal ");		
		hql.append("WHERE notaFiscal.statusNotaFiscal != :statusNF AND notaFiscal.dataEmissao = :dataOperacao ");
		
		Query query = super.getSession().createQuery(hql.toString());
		
		query.setResultTransformer(new AliasToBeanResultTransformer(ValidacaoRecebimentoFisicoFecharDiaDTO.class));
		
		query.setParameter("statusNF", StatusNotaFiscalEntrada.RECEBIDA);
		query.setParameter("dataOperacao", dataOperacaoDistribuidor);
		
		return query.list();
	}

	@Override
	public Boolean existeConfirmacaoDeExpedicao(Date dataOperacaoDistribuidor) {
		StringBuilder jpql = new StringBuilder();

		jpql.append(" SELECT CASE WHEN COUNT(lancamento) > 0 THEN true ELSE false END ");	
		jpql.append(" FROM Lancamento lancamento ");
		jpql.append(" WHERE lancamento.dataLancamentoDistribuidor = :dataOperacao ")
		    .append("   AND lancamento.status NOT IN (:status) ");
		
		Query query = getSession().createQuery(jpql.toString());
		
		List<StatusLancamento> listaLancamentos = new ArrayList<StatusLancamento>();
		
		listaLancamentos.add(StatusLancamento.EXPEDIDO);
		listaLancamentos.add(StatusLancamento.CANCELADO);
		
		query.setParameterList("status", listaLancamentos);
		query.setParameter("dataOperacao", dataOperacaoDistribuidor);
		
		return !(Boolean) query.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ValidacaoConfirmacaoDeExpedicaoFecharDiaDTO> obterConfirmacaoDeExpedicao(Date dataOperacaoDistribuidor) {
		
		StringBuilder jpql = new StringBuilder();
		
		jpql.append(" SELECT produto.codigo AS codigo,");
		jpql.append(" produto.descricao AS nomeProduto,");
		jpql.append(" pe.numeroEdicao AS edicao ");
		
		jpql.append("FROM Lancamento AS lancamento ");
		jpql.append("JOIN lancamento.produtoEdicao AS pe ");
		jpql.append("JOIN pe.produto AS produto ");
		jpql.append("WHERE  lancamento.dataLancamentoDistribuidor = :dataOperacaoDistribuidor ");
		jpql.append("AND  lancamento.status NOT IN (:status) ");		
		
		Query query = super.getSession().createQuery(jpql.toString());
		
		List<StatusLancamento> listaLancamentos = new ArrayList<StatusLancamento>();
		listaLancamentos.add(StatusLancamento.EXPEDIDO);
		listaLancamentos.add(StatusLancamento.CANCELADO);
		
		query.setParameterList("status", listaLancamentos);
		query.setParameter("dataOperacaoDistribuidor", dataOperacaoDistribuidor);
		
		query.setResultTransformer(new AliasToBeanResultTransformer(ValidacaoConfirmacaoDeExpedicaoFecharDiaDTO.class));
		
		return query.list();
		 
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ValidacaoLancamentoFaltaESobraFecharDiaDTO> existeLancamentoFaltasESobrasPendentes(Date dataOperacaoDistribuidor) {		 
		StringBuilder jpql = new StringBuilder();

		jpql.append("SELECT produto.codigo AS codigo,");
		jpql.append(" produto.descricao AS nomeProduto,");
		jpql.append(" pe.numeroEdicao AS edicao, ");
		jpql.append(" diferenca.tipoDiferenca AS inconsistencia ");
		
		jpql.append(" FROM Diferenca as diferenca ");
		jpql.append(" JOIN diferenca.produtoEdicao as pe ");
		jpql.append(" JOIN pe.produto as produto ");
		jpql.append(" WHERE diferenca.statusConfirmacao = :statusConfirmacao ")
		    .append(" AND  diferenca.dataMovimento = :dataOperacaoDistribuidor ");
		
		Query query = getSession().createQuery(jpql.toString());
		
		query.setParameter("statusConfirmacao", StatusConfirmacao.PENDENTE);
		query.setParameter("dataOperacaoDistribuidor", dataOperacaoDistribuidor);
		
		query.setResultTransformer(new AliasToBeanResultTransformer(ValidacaoLancamentoFaltaESobraFecharDiaDTO.class));
		
		return query.list();
	}

}
