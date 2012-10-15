package br.com.abril.nds.repository.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.ReparteFecharDiaDTO;
import br.com.abril.nds.model.aprovacao.StatusAprovacao;
import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque;
import br.com.abril.nds.model.estoque.TipoDiferenca;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.repository.ResumoFecharDiaRepository;

@Repository
public class ResumoFecharDiaRepositoryImpl  extends AbstractRepository implements ResumoFecharDiaRepository {

	@SuppressWarnings("unchecked")
	@Override
	public List<ReparteFecharDiaDTO> obterValorReparte(Date dataOperacaoDistribuidor, boolean soma) {
		
		StringBuilder hql = new StringBuilder();
		
		if(soma){
			hql.append(" SELECT SUM(pe.precoVenda) as valorTotalReparte ");			
		}else{
			hql.append(" SELECT p.codigo as codigo,  ");
			hql.append(" p.nome as nomeProduto, ");
			hql.append(" pe.numeroEdicao as numeroEdicao ");
		}
		hql.append(" from Lancamento l ");
		hql.append(" JOIN l.produtoEdicao as pe ");
		hql.append(" JOIN pe.produto as p ");
		hql.append(" WHERE l.dataLancamentoPrevista = :dataOperacaoDistribuidor ");
		hql.append(" AND l.status IN ( :listaStatus )");
		
		Query query = super.getSession().createQuery(hql.toString());
		
		List<StatusLancamento> listaStatus = new ArrayList<StatusLancamento>();
		
		listaStatus.add(StatusLancamento.CONFIRMADO);
		listaStatus.add(StatusLancamento.BALANCEADO);
		listaStatus.add(StatusLancamento.ESTUDO_FECHADO);
		
		query.setParameter("dataOperacaoDistribuidor", dataOperacaoDistribuidor);
		
		query.setParameterList("listaStatus", listaStatus);
		
		query.setResultTransformer(new AliasToBeanResultTransformer(ReparteFecharDiaDTO.class));
		
		return query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ReparteFecharDiaDTO> obterValorDiferenca(Date dataOperacaoDistribuidor, boolean soma, String tipoDiferenca) {
		
		StringBuilder hql = new StringBuilder();
		
		if(soma){
			if(tipoDiferenca.equals("sobra")){
				hql.append(" SELECT SUM(pe.precoVenda) as sobras ");				
			}else{
				hql.append(" SELECT SUM(pe.precoVenda) as faltas ");
			}
		}else{
			hql.append(" SELECT p.codigo as codigo,  ");
			hql.append(" p.nome as nomeProduto, ");
			hql.append(" pe.numeroEdicao as numeroEdicao ");
		}
		
		hql.append(" FROM LancamentoDiferenca ld");
		hql.append(" JOIN ld.diferenca as dif ");
		hql.append(" JOIN dif.produtoEdicao as pe ");
		hql.append(" JOIN pe.produto as p ");
		hql.append(" WHERE dif.dataMovimento = :dataOperacaoDistribuidor ");
		hql.append(" AND ld.status = :status ");
		hql.append(" AND dif.tipoDiferenca IN (:listaTipoDiferenca) ");
		
		Query query = super.getSession().createQuery(hql.toString());
		
		List<TipoDiferenca> listaTipoDiferenca = new ArrayList<TipoDiferenca>();
		
		if(tipoDiferenca.equals("sobra")){
			listaTipoDiferenca.add(TipoDiferenca.SOBRA_DE);
			listaTipoDiferenca.add(TipoDiferenca.SOBRA_EM);			
		}else if(tipoDiferenca.equals("falta")){
			listaTipoDiferenca.add(TipoDiferenca.FALTA_DE);
			listaTipoDiferenca.add(TipoDiferenca.FALTA_EM);		
		}
		
		query.setParameter("dataOperacaoDistribuidor", dataOperacaoDistribuidor);
		
		query.setParameter("status", StatusAprovacao.APROVADO);
		
		query.setParameterList("listaTipoDiferenca", listaTipoDiferenca);
		
		query.setResultTransformer(new AliasToBeanResultTransformer(ReparteFecharDiaDTO.class));
		
		return query.list();
	}

	@Override
	public BigDecimal obterValorTransferencia(Date dataOperacaoDistribuidor) {
		 
		StringBuilder hql = new StringBuilder();
		hql.append(" SELECT SUM(pe.precoVenda) from MovimentoEstoque me ");
		hql.append(" JOIN me.tipoMovimento as tm ");
		hql.append(" JOIN me.produtoEdicao as pe ");
		hql.append(" WHERE me.dataCriacao = :dataOperacaoDistribuidor ");
		hql.append(" AND tm.grupoMovimentoEstoque IN (:listaGrupoMovimentoEstoque) ");
		
		Query query = super.getSession().createQuery(hql.toString());
		
		List<GrupoMovimentoEstoque> listaGrupoMovimentoEstoque = new ArrayList<GrupoMovimentoEstoque>();
		
		listaGrupoMovimentoEstoque.add(GrupoMovimentoEstoque.TRANSFERENCIA_ENTRADA_LANCAMENTO);
		listaGrupoMovimentoEstoque.add(GrupoMovimentoEstoque.TRANSFERENCIA_ENTRADA_PRODUTOS_DANIFICADOS);
		listaGrupoMovimentoEstoque.add(GrupoMovimentoEstoque.TRANSFERENCIA_ENTRADA_RECOLHIMENTO);
		listaGrupoMovimentoEstoque.add(GrupoMovimentoEstoque.TRANSFERENCIA_ENTRADA_SUPLEMENTAR);
		
		query.setParameter("dataOperacaoDistribuidor", dataOperacaoDistribuidor);
		
		query.setParameterList("listaGrupoMovimentoEstoque", listaGrupoMovimentoEstoque);
		
		return (BigDecimal) query.uniqueResult();
	}

	@Override
	public BigDecimal obterValorDistribuido(Date dataOperacaoDistribuidor) {
		 
		StringBuilder hql = new StringBuilder();
		hql.append(" SELECT SUM(pe.precoVenda) from MovimentoEstoque me ");
		hql.append(" JOIN me.tipoMovimento as tm ");
		hql.append(" JOIN me.produtoEdicao as pe ");
		hql.append(" WHERE me.dataCriacao = :dataOperacaoDistribuidor ");
		hql.append(" AND tm.grupoMovimentoEstoque = :grupoMovimento ");
		hql.append(" AND me.status = :status ");
		
		Query query = super.getSession().createQuery(hql.toString());
		
				
		query.setParameter("dataOperacaoDistribuidor", dataOperacaoDistribuidor);		
		query.setParameter("grupoMovimento", GrupoMovimentoEstoque.ENVIO_JORNALEIRO);		
		query.setParameter("status", StatusAprovacao.APROVADO);		
		
		
		return (BigDecimal) query.uniqueResult();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ReparteFecharDiaDTO> obterResumoReparte(Date dataOperacaoDistribuidor ){
		StringBuilder hql = new StringBuilder();
		
		hql.append(" SELECT p.codigo as codigo,  ");
		hql.append(" p.nome as nomeProduto, ");
		hql.append(" pe.numeroEdicao as numeroEdicao, ");
		hql.append(" pe.precoVenda as precoVenda ");
		
//		hql.append(" ( SELECT COUNT(ld.id) from LancamentoDiferenca ld ");
//		hql.append(" JOIN ld.diferenca as dif ");
//		hql.append(" JOIN dif.produtoEdicao as pe ");
//		hql.append(" WHERE dif.dataMovimento = :dataOperacaoDistribuidor ");
//		hql.append(" AND ld.status = :status ");
//		hql.append(" AND dif.tipoDiferenca IN (:listaTipoDiferenca) ) as sobras ");
		
		
		hql.append(" from Lancamento l ");
		hql.append(" JOIN l.produtoEdicao as pe ");
		hql.append(" JOIN pe.produto as p ");
		hql.append(" WHERE l.dataLancamentoPrevista = :dataOperacaoDistribuidor ");
		hql.append(" AND l.status IN ( :listaStatus )");
		
		Query query = super.getSession().createQuery(hql.toString());
		
		List<StatusLancamento> listaStatus = new ArrayList<>();
		
		listaStatus.add(StatusLancamento.CONFIRMADO);
		listaStatus.add(StatusLancamento.BALANCEADO);
		listaStatus.add(StatusLancamento.ESTUDO_FECHADO);
		
		List<TipoDiferenca> listaTipoDiferenca = new ArrayList<TipoDiferenca>();
		
		listaTipoDiferenca.add(TipoDiferenca.SOBRA_DE);
		listaTipoDiferenca.add(TipoDiferenca.SOBRA_EM);	
		
		query.setParameter("dataOperacaoDistribuidor", dataOperacaoDistribuidor);
		
		query.setParameterList("listaStatus", listaStatus);
		
		query.setParameter("status", StatusAprovacao.APROVADO);
		
		query.setParameterList("listaTipoDiferenca", listaTipoDiferenca);
		
		query.setResultTransformer(new AliasToBeanResultTransformer(ReparteFecharDiaDTO.class));
		
		
		
		return query.list();
	}

}
