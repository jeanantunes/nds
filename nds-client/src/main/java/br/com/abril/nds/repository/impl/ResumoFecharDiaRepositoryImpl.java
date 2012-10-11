package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.aprovacao.StatusAprovacao;
import br.com.abril.nds.model.estoque.TipoDiferenca;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.repository.ResumoFecharDiaRepository;

@Repository
public class ResumoFecharDiaRepositoryImpl  extends AbstractRepository implements ResumoFecharDiaRepository {

	@Override
	public BigDecimal obterValorReparte(Date dataOperacaoDistribuidor) {
		
		StringBuilder hql = new StringBuilder();
		hql.append(" SELECT SUM(pe.precoVenda) from Lancamento l ");
		hql.append(" JOIN l.produtoEdicao as pe ");
		hql.append(" WHERE l.dataLancamentoPrevista = :dataOperacaoDistribuidor ");
		hql.append(" AND l.status IN ( :listaStatus )");
		
		Query query = super.getSession().createQuery(hql.toString());
		
		List<StatusLancamento> listaStatus = new ArrayList<>();
		
		listaStatus.add(StatusLancamento.CONFIRMADO);
		listaStatus.add(StatusLancamento.BALANCEADO);
		listaStatus.add(StatusLancamento.ESTUDO_FECHADO);
		
		query.setParameter("dataOperacaoDistribuidor", dataOperacaoDistribuidor);
		
		query.setParameterList("listaStatus", listaStatus);
		
		return (BigDecimal) query.uniqueResult();
	}

	@Override
	public BigDecimal obterValorSobras(Date dataOperacaoDistribuidor) {
		
		StringBuilder hql = new StringBuilder();
		hql.append(" SELECT SUM(pe.precoVenda) from LancamentoDiferenca ld ");
		hql.append(" JOIN ld.diferenca as dif ");
		hql.append(" JOIN dif.produtoEdicao as pe ");
		hql.append(" WHERE dif.dataMovimento = :dataOperacaoDistribuidor ");
		hql.append(" AND ld.status = :status ");
		hql.append(" AND dif.tipoDiferenca IN (:listaTipoDiferenca) ");
		
		Query query = super.getSession().createQuery(hql.toString());
		
		List<TipoDiferenca> listaTipoDiferenca = new ArrayList<TipoDiferenca>();
		
		listaTipoDiferenca.add(TipoDiferenca.SOBRA_DE);
		listaTipoDiferenca.add(TipoDiferenca.SOBRA_EM);		
		
		query.setParameter("dataOperacaoDistribuidor", dataOperacaoDistribuidor);
		
		query.setParameter("status", StatusAprovacao.APROVADO);
		
		query.setParameterList("listaTipoDiferenca", listaTipoDiferenca);
		
		return (BigDecimal) query.uniqueResult();
	}

	@Override
	public BigDecimal obterValorFaltas(Date dataOperacaoDistribuidor) {
		StringBuilder hql = new StringBuilder();
		hql.append(" SELECT SUM(pe.precoVenda) from LancamentoDiferenca ld ");
		hql.append(" JOIN ld.diferenca as dif ");
		hql.append(" JOIN dif.produtoEdicao as pe ");
		hql.append(" WHERE dif.dataMovimento = :dataOperacaoDistribuidor ");
		hql.append(" AND ld.status = :status ");
		hql.append(" AND dif.tipoDiferenca IN (:listaTipoDiferenca) ");
		
		Query query = super.getSession().createQuery(hql.toString());
		
		List<TipoDiferenca> listaTipoDiferenca = new ArrayList<TipoDiferenca>();
		
		listaTipoDiferenca.add(TipoDiferenca.FALTA_DE);
		listaTipoDiferenca.add(TipoDiferenca.FALTA_EM);		
		
		query.setParameter("dataOperacaoDistribuidor", dataOperacaoDistribuidor);
		
		query.setParameter("status", StatusAprovacao.APROVADO);
		
		query.setParameterList("listaTipoDiferenca", listaTipoDiferenca);
		
		return (BigDecimal) query.uniqueResult();
	}

}
