package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.ValidacaoConfirmacaoDeExpedicaoFecharDiaDTO;
import br.com.abril.nds.dto.ValidacaoGeracaoCobrancaFecharDiaDTO;
import br.com.abril.nds.dto.ValidacaoLancamentoFaltaESobraFecharDiaDTO;
import br.com.abril.nds.dto.ValidacaoRecebimentoFisicoFecharDiaDTO;
import br.com.abril.nds.model.StatusCobranca;
import br.com.abril.nds.model.StatusConfirmacao;
import br.com.abril.nds.model.aprovacao.StatusAprovacao;
import br.com.abril.nds.model.cadastro.FormaCobranca;
import br.com.abril.nds.model.financeiro.GrupoMovimentoFinaceiro;
import br.com.abril.nds.model.fiscal.StatusRecebimento;
import br.com.abril.nds.model.movimentacao.Movimento;
import br.com.abril.nds.model.movimentacao.TipoMovimento;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.repository.AbstractRepository;
import br.com.abril.nds.repository.FecharDiaRepository;

@Repository
public class FecharDiaRepositoryImpl extends AbstractRepository implements FecharDiaRepository {

	@Override
	public boolean existeCobrancaParaFecharDia(Date diaDeOperaoMenosUm) {
		StringBuilder hql = new StringBuilder();
		hql.append(" select count(c.id) from Cobranca c where ");		
		hql.append(" c.statusCobranca = :statusCobranca");		
		hql.append(" and c.dataVencimento = :diaDeOperaoMenosUm ");
		
		Query query = super.getSession().createQuery(hql.toString());
		
		query.setParameter("statusCobranca", StatusCobranca.NAO_PAGO);
		
		query.setParameter("diaDeOperaoMenosUm", diaDeOperaoMenosUm);
		
		return (long)query.uniqueResult() == 0L ? false : true;
	}

	@Override
	public boolean existeNotaFiscalSemRecebimentoFisico(Date dataOperacaoDistribuidor) {

		StringBuilder hql = new StringBuilder();

		hql.append(" SELECT notaFiscal from NotaFiscalEntradaFornecedor notaFiscal ");		
		hql.append(" WHERE notaFiscal.statusRecebimento = :statusRecebimentoNF  ");		
		
		Query query = super.getSession().createQuery(hql.toString());
		
		query.setParameter("statusRecebimentoNF", StatusRecebimento.SALVO);
		
		return query.list().isEmpty() ? true : false;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ValidacaoRecebimentoFisicoFecharDiaDTO> obterNotaFiscalComRecebimentoFisicoNaoConfirmado(Date dataOperacaoDistribuidor) {
		StringBuilder hql = new StringBuilder();

		hql.append(" select numero as numeroNotaFiscal from NotaFiscalEntradaFornecedor notaFiscal ");		
		hql.append(" WHERE notaFiscal.statusRecebimento = :statusRecebimentoNF  ");
		hql.append(" GROUP BY numero");
		
		Query query = super.getSession().createQuery(hql.toString());
		
		query.setResultTransformer(new AliasToBeanResultTransformer(ValidacaoRecebimentoFisicoFecharDiaDTO.class));
		
		query.setParameter("statusRecebimentoNF", StatusRecebimento.SALVO);
		
		return query.list();
	}

	@Override
	public Boolean existeConfirmacaoDeExpedicao(Date dataOperacaoDistribuidor) {
		StringBuilder jpql = new StringBuilder();

		jpql.append(" SELECT CASE WHEN COUNT(lancamento) > 0 THEN true ELSE false END ");	
		jpql.append(" FROM Lancamento lancamento ");
		jpql.append(" WHERE lancamento.dataLancamentoDistribuidor = :dataOperacao ")
		    .append("   AND lancamento.status = :status ")
		    .append("   AND lancamento.produtoEdicao.ativo = :ativo ");
		
		Query query = getSession().createQuery(jpql.toString());
		
		query.setParameter("ativo", true);
		query.setParameter("status", StatusLancamento.BALANCEADO);
		query.setParameter("dataOperacao", dataOperacaoDistribuidor);
		
		return (Boolean) query.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ValidacaoConfirmacaoDeExpedicaoFecharDiaDTO> obterConfirmacaoDeExpedicao(Date dataOperacaoDistribuidor) {
		
		StringBuilder jpql = new StringBuilder();
		
		jpql.append(" SELECT produto.codigo AS codigo,");
		jpql.append(" produto.nome AS nomeProduto,");
		jpql.append(" pe.numeroEdicao AS edicao ");
		
		jpql.append(" FROM Lancamento AS lancamento ");
		jpql.append(" JOIN lancamento.produtoEdicao AS pe ");
		jpql.append(" left JOIN lancamento.estudo  estudo ");
		jpql.append(" JOIN pe.produto AS produto ");
		jpql.append(" join produto.fornecedores fornecedor ");
		jpql.append(" WHERE  lancamento.dataLancamentoDistribuidor = :dataOperacaoDistribuidor ");
		
		// jpql.append(" and estudo.status = :statusEstudo ");
				
		jpql.append(" AND  lancamento.status=:status ");
		jpql.append(" AND  lancamento.produtoEdicao.ativo = :ativo ");
		jpql.append(" GROUP BY produto.codigo, produto.nome, pe.numeroEdicao ");
		
		Query query = super.getSession().createQuery(jpql.toString());
		
		List<StatusLancamento> listaLancamentos = new ArrayList<StatusLancamento>();
		listaLancamentos.add(StatusLancamento.BALANCEADO);
		

		//query.setParameter("statusEstudo", StatusLancamento.ESTUDO_FECHADO);
		query.setParameter("ativo", true);
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
		jpql.append(" produto.nome AS nomeProduto,");
		jpql.append(" pe.numeroEdicao AS edicao, ");
		jpql.append(" diferenca.tipoDiferenca AS inconsistencia ");
		
		jpql.append(" FROM Diferenca as diferenca ");
		jpql.append(" JOIN diferenca.produtoEdicao as pe ");
		jpql.append(" JOIN pe.produto as produto ");
		jpql.append(" WHERE diferenca.statusConfirmacao = :statusConfirmacao ")
		    .append(" AND  diferenca.dataMovimento = :dataOperacaoDistribuidor ");
		jpql.append(" GROUP BY produto.codigo, produto.nomeComercial, pe.numeroEdicao, diferenca.tipoDiferenca ");
		
		Query query = getSession().createQuery(jpql.toString());
		
		query.setParameter("statusConfirmacao", StatusConfirmacao.PENDENTE);
		query.setParameter("dataOperacaoDistribuidor", dataOperacaoDistribuidor);
		
		query.setResultTransformer(new AliasToBeanResultTransformer(ValidacaoLancamentoFaltaESobraFecharDiaDTO.class));
		
		return query.list();
	}

	@Override
	public boolean existePendenciasDeAprovacao(Date dataOperacao, StatusAprovacao statusAprovacao, 
			List<TipoMovimento> tiposMovimentoVerificaAprovacao) {
		
		StringBuilder sql = new StringBuilder(" select SUM(qtde) from ( " );
			    
		sql.append(" 	select count(ID) as qtde from MOVIMENTO_ESTOQUE ");       
		sql.append(" 	where DATA_CRIACAO = :dataOperacao ");
		sql.append(" 	and STATUS = :statusAprovacao ");
		
		if (tiposMovimentoVerificaAprovacao != null && !tiposMovimentoVerificaAprovacao.isEmpty()) {
			sql.append(" 	and (TIPO_MOVIMENTO_ID in (:tiposMovimentoVerificaAprovacao)) ");
		}
		
		sql.append("	union ");
		sql.append(" 	select count(ID) as qtde from MOVIMENTO_ESTOQUE_COTA ");        
		sql.append("    where DATA_CRIACAO = :dataOperacao "); 
		sql.append(" 	and STATUS = :statusAprovacao "); 
		
		if (tiposMovimentoVerificaAprovacao != null && !tiposMovimentoVerificaAprovacao.isEmpty()) {
			sql.append(" 	and (TIPO_MOVIMENTO_ID in (:tiposMovimentoVerificaAprovacao)) ");
		}

		sql.append(" 	union ");
		sql.append(" 	select count(ID) as qtde from MOVIMENTO_FINANCEIRO_COTA  ");       
		sql.append(" 	where DATA_CRIACAO = :dataOperacao "); 
		sql.append(" 	and STATUS = :statusAprovacao  ");

		if (tiposMovimentoVerificaAprovacao != null && !tiposMovimentoVerificaAprovacao.isEmpty()) {
			sql.append(" 	and (TIPO_MOVIMENTO_ID in (:tiposMovimentoVerificaAprovacao)) ");
		}
		
		sql.append(" ) as soma ");
		
		Query query = getSession().createSQLQuery(sql.toString());
		
		query.setParameter("dataOperacao", dataOperacao);
		query.setParameter("statusAprovacao", statusAprovacao.name());
		
		if (tiposMovimentoVerificaAprovacao != null && !tiposMovimentoVerificaAprovacao.isEmpty()) {
			
			List<Long> ids = new ArrayList<Long>();
			
			for(TipoMovimento tipo : tiposMovimentoVerificaAprovacao) {
				ids.add(tipo.getId());
			}
			
			query.setParameterList("tiposMovimentoVerificaAprovacao", ids);
		}
		
		return ((BigDecimal)query.uniqueResult()).intValue() > 0;
	}

	@Override
	public boolean existeMatrizRecolhimentoSalva(Date dataOperacaoDistribuidor) {

		StringBuilder jpql = new StringBuilder();
		
		jpql.append(" SELECT count(l.id) ");
		jpql.append(" FROM Lancamento l ");
		jpql.append(" WHERE l.dataRecolhimentoDistribuidor = :dataOperacaoDistribuidor ");
		jpql.append(" AND l.status = :matrizSalva ");
				
		Query query = getSession().createQuery(jpql.toString());
		
		query.setParameter("dataOperacaoDistribuidor", dataOperacaoDistribuidor);
		query.setParameter("matrizSalva", StatusLancamento.EM_BALANCEAMENTO_RECOLHIMENTO);

		return ((Long) query.uniqueResult()) > 0;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ValidacaoGeracaoCobrancaFecharDiaDTO> obterFormasDeCobranca() {
		StringBuilder jpql = new StringBuilder();
		
		jpql.append(" SELECT fc.tipoFormaCobranca as tipoFormaCobranca, ");
		jpql.append(" fc.id as formaCobrancaId ");
		jpql.append(" FROM PoliticaCobranca pc ");
		jpql.append(" JOIN pc.formaCobranca as fc ");
		jpql.append(" WHERE pc.ativo = true ");
				
		Query query = getSession().createQuery(jpql.toString());
		
		query.setResultTransformer(new AliasToBeanResultTransformer(ValidacaoGeracaoCobrancaFecharDiaDTO.class));
		
		return query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ValidacaoGeracaoCobrancaFecharDiaDTO> obterDiasDaConcentracao(FormaCobranca formaCobranca){
			 
			StringBuilder hql = new StringBuilder();
			
			hql.append("Select ccc.codigoDiaSemana as diaDoMes ");
			
			hql.append("FROM ConcentracaoCobrancaCota as ccc ");
			hql.append("JOIN ccc.formaCobranca as fc ");
			hql.append("WHERE fc.id = :idFormaCobranca");
			
			Query query = getSession().createQuery(hql.toString());
			
			query.setParameter("idFormaCobranca", formaCobranca.getId());
			
			query.setResultTransformer(new AliasToBeanResultTransformer(ValidacaoGeracaoCobrancaFecharDiaDTO.class));
			
			return query.list();		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<Movimento> obterMovimentosPorStatusData(List<GrupoMovimentoFinaceiro> gruposMovimentoFinanceiro,
														Date dataMovimento, StatusAprovacao statusAprovacao) {

		StringBuilder hql = new StringBuilder();

		hql.append(" SELECT movimento ");

		hql.append(" FROM MovimentoFinanceiroCota movimento ");
		hql.append(" WHERE movimento.data = :dataMovimento ");
		hql.append("  AND  movimento.status = :statusAprovacao ");
		
		
		if (gruposMovimentoFinanceiro != null) {
		
			hql.append(" AND movimento.tipoMovimento.grupoMovimentoFinaceiro IN (:gruposMovimentoFinanceiro) ");
		}
		
		Query query = getSession().createQuery(hql.toString());

		query.setParameter("dataMovimento", dataMovimento);
		query.setParameter("statusAprovacao", statusAprovacao);
				
		if (gruposMovimentoFinanceiro != null) {
		
			query.setParameterList("gruposMovimentoFinanceiro", gruposMovimentoFinanceiro);
		}

		return query.list();
	}
	
}
