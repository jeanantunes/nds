package br.com.abril.nds.repository.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.ParcialVendaDTO;
import br.com.abril.nds.dto.PeriodoParcialDTO;
import br.com.abril.nds.dto.filtro.FiltroParciaisDTO;
import br.com.abril.nds.dto.filtro.FiltroParciaisDTO.ColunaOrdenacaoPeriodo;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.model.planejamento.PeriodoLancamentoParcial;
import br.com.abril.nds.repository.PeriodoLancamentoParcialRepository;

@Repository
public class PeriodoLancamentoParcialRepositoryImpl extends AbstractRepositoryModel<PeriodoLancamentoParcial, Long> 
			implements PeriodoLancamentoParcialRepository {

	public PeriodoLancamentoParcialRepositoryImpl() {
		super(PeriodoLancamentoParcial.class);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<PeriodoParcialDTO> obterPeriodosParciais(FiltroParciaisDTO filtro) {
				
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select produtoEdicao.id as idProdutoEdicao, ");
		hql.append("		lancamento.dataLancamentoDistribuidor as dataLancamento, ");
		hql.append(" 		lancamento.dataRecolhimentoDistribuidor as dataRecolhimento, ");
		hql.append("		produtoEdicao.origemInterface as geradoPorInterface, ");
		
		hql.append("		sum(mCota.qtde) as reparte,  ");
		
		hql.append(" 		(select sum(movCota.qtde) from Lancamento lancamentoSupl ");
		hql.append("		 	left join lancamentoSupl.movimentoEstoqueCotas movCota ");
		hql.append("			join lancamentoSupl.produtoEdicao pe ");
		hql.append("		 where pe.id = produtoEdicao.id ");
		hql.append("		    and lancamentoSupl.tipoLancamento = 'SUPLEMENTAR' ");
		hql.append("			and lancamentoSupl.dataLancamentoDistribuidor >= lancamento.dataLancamentoDistribuidor ");
		hql.append("			and lancamentoSupl.dataLancamentoDistribuidor <= lancamento.dataRecolhimentoDistribuidor) ");		
		hql.append(" 		as suplementacao, ");
		
		hql.append("		(select sum(movimento.qtde) from ConferenciaEncalhe conferencia ");
		hql.append("		 	join conferencia.movimentoEstoqueCota movimento ");
		hql.append("		 	join conferencia.chamadaEncalheCota chamadaEncalheCota ");
		hql.append("		 	join chamadaEncalheCota.chamadaEncalhe chamadaEncalhe ");
		hql.append("			where chamadaEncalhe.dataRecolhimento >= lancamento.dataLancamentoDistribuidor ");
		hql.append("			and chamadaEncalhe.dataRecolhimento <= lancamento.dataRecolhimentoDistribuidor ");
		hql.append("			and chamadaEncalhe.produtoEdicao.id = lancamento.produtoEdicao.id ");
		hql.append("			group by chamadaEncalhe.id) ");
		hql.append(" 		 as encalhe, ");
		
		hql.append("		sum(mCota.qtde) - (select sum(movimento.qtde) from ConferenciaEncalhe conferencia ");
		hql.append("		 	join conferencia.movimentoEstoqueCota movimento ");
		hql.append("		 	join conferencia.chamadaEncalheCota chamadaEncalheCota ");
		hql.append("		 	join chamadaEncalheCota.chamadaEncalhe chamadaEncalhe ");
		hql.append("			where chamadaEncalhe.dataRecolhimento >= lancamento.dataLancamentoDistribuidor ");
		hql.append("			and chamadaEncalhe.dataRecolhimento <= lancamento.dataRecolhimentoDistribuidor ");
		hql.append("			and chamadaEncalhe.produtoEdicao.id = lancamento.produtoEdicao.id ");
		hql.append("			group by chamadaEncalhe.id) as vendas, ");
		
		hql.append("		sum(mCota.qtde) - (select sum(movimento.qtde) from ConferenciaEncalhe conferencia ");
		hql.append("		 	join conferencia.movimentoEstoqueCota movimento ");
		hql.append("		 	join conferencia.chamadaEncalheCota chamadaEncalheCota ");
		hql.append("		 	join chamadaEncalheCota.chamadaEncalhe chamadaEncalhe ");
		hql.append("			where chamadaEncalhe.dataRecolhimento >= lancamento.dataLancamentoDistribuidor ");
		hql.append("			and chamadaEncalhe.dataRecolhimento <= lancamento.dataRecolhimentoDistribuidor ");
		hql.append("			and chamadaEncalhe.produtoEdicao.id = lancamento.produtoEdicao.id ");
		hql.append("			group by chamadaEncalhe.id) as vendaCE, ");
		
		hql.append(" 		(select sum(movCota.qtde) from Lancamento lancamentoSupl ");
		hql.append("		 	left join lancamentoSupl.movimentoEstoqueCotas movCota ");
		hql.append("			join lancamentoSupl.produtoEdicao pe ");
		hql.append("		 where pe.id = produtoEdicao.id ");
		hql.append("			and lancamentoSupl.dataLancamentoDistribuidor <= lancamento.dataRecolhimentoDistribuidor) ");		
		hql.append(" 		as reparteAcum, ");
		
		hql.append("		((select sum(liMCota.qtde) from Lancamento lancamentoInicial ");
		hql.append("			 left join lancamentoInicial.movimentoEstoqueCotas liMCota ");
		hql.append("			where lancamentoInicial.dataLancamentoDistribuidor=lancamentoParcial.lancamentoInicial ");
		hql.append("			and lancamentoInicial.produtoEdicao.id=produtoEdicao.id) ");
		hql.append("		- (select sum(movimento.qtde) from ConferenciaEncalhe conferencia ");
		hql.append("		 	join conferencia.movimentoEstoqueCota movimento ");
		hql.append("		 	join conferencia.chamadaEncalheCota chamadaEncalheCota ");
		hql.append("		 	join chamadaEncalheCota.chamadaEncalhe chamadaEncalhe ");
		hql.append("			where chamadaEncalhe.dataRecolhimento >= lancamentoParcial.lancamentoInicial ");
		hql.append("			and chamadaEncalhe.dataRecolhimento <= lancamentoParcial.recolhimentoFinal ");
		hql.append("			and chamadaEncalhe.produtoEdicao.id = lancamento.produtoEdicao.id ");
		hql.append("			group by chamadaEncalhe.id)) ");
		hql.append(" 		 as vendaAcumulada, ");
		

		hql.append("		round((((select sum(liMCota.qtde) from Lancamento lancamentoInicial ");
		hql.append("			 left join lancamentoInicial.movimentoEstoqueCotas liMCota ");
		hql.append("			where lancamentoInicial.dataLancamentoDistribuidor=lancamentoParcial.lancamentoInicial ");
		hql.append("			and lancamentoInicial.produtoEdicao.id=produtoEdicao.id) ");
		hql.append("		- (select sum(movimento.qtde) from ConferenciaEncalhe conferencia ");
		hql.append("		 	join conferencia.movimentoEstoqueCota movimento ");
		hql.append("		 	join conferencia.chamadaEncalheCota chamadaEncalheCota ");
		hql.append("		 	join chamadaEncalheCota.chamadaEncalhe chamadaEncalhe ");
		hql.append("			where chamadaEncalhe.dataRecolhimento >= lancamentoParcial.lancamentoInicial ");
		hql.append("			and chamadaEncalhe.dataRecolhimento <= lancamentoParcial.recolhimentoFinal ");
		hql.append("			and chamadaEncalhe.produtoEdicao.id = lancamento.produtoEdicao.id ");
		hql.append("			group by chamadaEncalhe.id)) ");
		
		hql.append(" 		/(select sum(movCota.qtde) from Lancamento lancamentoSupl ");
		hql.append("		 	left join lancamentoSupl.movimentoEstoqueCotas movCota ");
		hql.append("			join lancamentoSupl.produtoEdicao pe ");
		hql.append("		 where pe.id = produtoEdicao.id ");
		hql.append("			and lancamentoSupl.dataLancamentoDistribuidor <= lancamento.dataRecolhimentoDistribuidor)) ");
		hql.append("		 * 100 )");
		hql.append(" 		as percVendaAcumulada, ");
		
		
		
		hql.append("		  ((sum(mCota.qtde) - (select sum(movimento.qtde) from ConferenciaEncalhe conferencia ");
		hql.append("		 	join conferencia.movimentoEstoqueCota movimento ");
		hql.append("		 	join conferencia.chamadaEncalheCota chamadaEncalheCota ");
		hql.append("		 	join chamadaEncalheCota.chamadaEncalhe chamadaEncalhe ");
		hql.append("			where chamadaEncalhe.dataRecolhimento >= lancamento.dataLancamentoDistribuidor ");
		hql.append("			and chamadaEncalhe.dataRecolhimento <= lancamento.dataRecolhimentoDistribuidor ");
		hql.append("			and chamadaEncalhe.produtoEdicao.id = lancamento.produtoEdicao.id ");
		hql.append("			group by chamadaEncalhe.id)) ");
		hql.append(" 		   /sum(mCota.qtde)) * 100 ");
		hql.append("		 as percVenda, ");
		
		hql.append("		 lancamento.id as idLancamento ");
		
		hql.append(getSqlFromEWherePeriodosParciais(filtro));
		
	
		hql.append(" group by periodo ");
				
		hql.append(getOrderByPeriodosParciais(filtro));
				
		Query query =  getSession().createQuery(hql.toString());
		
		HashMap<String, Object> param = buscarParametrosLancamentosParciais(filtro);
		
		for(String key : param.keySet()){
			query.setParameter(key, param.get(key));
		}
		
		query.setResultTransformer(new AliasToBeanResultTransformer(
				PeriodoParcialDTO.class));
		
		if(filtro.getPaginacao()!= null && filtro.getPaginacao().getPosicaoInicial() != null) 
			query.setFirstResult(filtro.getPaginacao().getPosicaoInicial());
		
		if(filtro.getPaginacao()!= null && filtro.getPaginacao().getQtdResultadosPorPagina() != null) 
			query.setMaxResults(filtro.getPaginacao().getQtdResultadosPorPagina());
		
		return query.list();
	}
	
	private String getSqlFromEWherePeriodosParciais(FiltroParciaisDTO filtro) {
		
		StringBuilder hql = new StringBuilder();
			
		hql.append(" from PeriodoLancamentoParcial periodo ");
		hql.append(" join periodo.lancamentoParcial lancamentoParcial ");
		hql.append(" join periodo.lancamento lancamento ");
		hql.append(" join lancamento.produtoEdicao produtoEdicao ");
		hql.append(" join produtoEdicao.produto produto ");
		hql.append(" left join lancamento.estudo estudo ");
		hql.append(" left join lancamento.movimentoEstoqueCotas mCota ");
		hql.append(" join produto.fornecedores fornecedor ");
		hql.append(" join fornecedor.juridica juridica ");
		
		boolean usarAnd = false;
		
		if(filtro.getCodigoProduto() != null) { 
			hql.append( (usarAnd ? " and ":" where ") + " produto.codigo =:codProduto ");
			usarAnd = true;
		}
		
		if(filtro.getNomeProduto() != null) { 
			hql.append( (usarAnd ? " and ":" where ") + " lower(produto.nome)like:nomeProduto ");
			usarAnd = true;
		}
		
		if(filtro.getEdicaoProduto() != null){ 
			hql.append( (usarAnd ? " and ":" where ") + " produtoEdicao.numeroEdicao=:edProduto ");
			usarAnd = true;
		}
		
		if(filtro.getIdFornecedor() != null) { 
			hql.append( (usarAnd ? " and ":" where ") + " fornecedor.id=:idFornecedor ");
			usarAnd = true;
		}
		
		if(filtro.getDataInicialDate() != null) { 
			hql.append( (usarAnd ? " and ":" where ") + " periodo.lancamento.dataLancamentoDistribuidor>=:dtInicial ");
			usarAnd = true;
		}
		
		if(filtro.getDataFinalDate() != null) { 
			hql.append( (usarAnd ? " and ":" where ") + " periodo.lancamento.dataRecolhimentoDistribuidor<=:dtFinal ");
			usarAnd = true;
		}
		
		if(filtro.getStatus() != null) { 
			hql.append( (usarAnd ? " and ":" where ") + " periodo.status=:status ");
			usarAnd = true;
		}
		
		return hql.toString();
	}

	private String getOrderByPeriodosParciais(FiltroParciaisDTO filtro){
		
		if(filtro.getPaginacao() == null || filtro.getPaginacao().getSortColumn() == null){
			return "";
		}
		
		ColunaOrdenacaoPeriodo coluna = ColunaOrdenacaoPeriodo.getPorDescricao(filtro.getPaginacao().getSortColumn());
		
		StringBuilder hql = new StringBuilder();
		
		switch (coluna) {
			
			case DATA_LANCAMENTO:	
				hql.append(" order by dataLancamento ");
				break;
			case DATA_RECOLHIMENTO:	
				hql.append(" order by dataRecolhimento ");
				break;
			case ENCALHE:	
				hql.append(" order by encalhe ");
				break;
			case PERC_VENDA:
				hql.append(" order by percVenda ");
				break;
			case PERIODO:
				hql.append(" order by dataLancamento ");
				break;	
			case REPARTE:
				hql.append(" order by reparte ");
				break;	
			case VENDA_ACUMULADA:
				hql.append(" order by vendaAcumulada ");
				break;	
			case VENDAS:
				hql.append(" order by vendas ");
				break;	
			default:
				hql.append(" order by dataLancamento ");
		}
		
		if (filtro.getPaginacao().getOrdenacao() != null) {
			hql.append( filtro.getPaginacao().getOrdenacao().toString());
		}
		
		return hql.toString();
	}

	/**
	 * Retorna os parametros da consulta de dividas.
	 * @param filtro
	 * @return HashMap<String,Object>
	 */
	private HashMap<String,Object> buscarParametrosLancamentosParciais(FiltroParciaisDTO filtro){
		
		HashMap<String,Object> param = new HashMap<String, Object>();
				
		if(filtro.getCodigoProduto() != null) 
			param.put("codProduto", filtro.getCodigoProduto());
		
		if(filtro.getNomeProduto() != null) 
			param.put("nomeProduto", filtro.getNomeProduto().toLowerCase());
		
		if(filtro.getEdicaoProduto() != null) 
			param.put("edProduto", filtro.getEdicaoProduto());
		
		if(filtro.getIdFornecedor() != null) 
			param.put("idFornecedor", filtro.getIdFornecedor());
		
		if(filtro.getDataInicialDate() != null) 
			param.put("dtInicial", filtro.getDataInicialDate());
		
		if(filtro.getDataFinalDate() != null) 
			param.put("dtFinal", filtro.getDataFinalDate());
		
		if(filtro.getStatus() != null) 
			param.put("status", filtro.getStatusEnum());
	
		return param;
	}

	@Override
	public Integer totalObterPeriodosParciais(FiltroParciaisDTO filtro) {
		
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select count(periodo) ");
		
		hql.append(getSqlFromEWherePeriodosParciais(filtro));
		
		Query query =  getSession().createQuery(hql.toString());
		
		HashMap<String, Object> param = buscarParametrosLancamentosParciais(filtro);
		
		for(String key : param.keySet()){
			query.setParameter(key, param.get(key));
		}	
		
		Long totalRegistros = (Long) query.uniqueResult();
		
		return (totalRegistros == null) ? 0 : totalRegistros.intValue();
	}
	
	public PeriodoLancamentoParcial obterPeriodoPorIdLancamento(Long idLancamento) {
		
		Criteria criteria = super.getSession().createCriteria(PeriodoLancamentoParcial.class,"periodo");
				
		criteria.add(Restrictions.eq("periodo.lancamento.id", idLancamento));
				
		criteria.setMaxResults(1);
		
		return (PeriodoLancamentoParcial) criteria.uniqueResult();
	}

	@Override
	public Boolean verificarValidadeNovoPeriodoParcial(Long idLancamento,
			Date dataLancamento, Date dataRecolhimento) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select count(periodo) ");
		
		hql.append(" from PeriodoLancamentoParcial periodo ");
		
		hql.append(" join periodo.lancamentoParcial lancamentoParcial ");
		
		hql.append(" join lancamentoParcial.periodos periodos ");
		
		hql.append(" where periodos.lancamento.id !=:idLancamento ");
		
		hql.append(" and ( (periodos.lancamento.dataLancamentoDistribuidor>=:dataLancamento ");
		
		hql.append(" 	and periodos.lancamento.dataLancamentoDistribuidor<=:dataRecolhimento) ");
		
		hql.append(" 	or (periodos.lancamento.dataRecolhimentoDistribuidor>=:dataLancamento ");
		
		hql.append(" 	and periodos.lancamento.dataRecolhimentoDistribuidor<=:dataRecolhimento) )");
		
		
		Query query =  getSession().createQuery(hql.toString());
		
		query.setParameter("idLancamento", idLancamento);
		query.setParameter("dataLancamento", dataLancamento);
		query.setParameter("dataRecolhimento", dataRecolhimento);
		
		Long count = (Long) query.uniqueResult();
		
		return (count == null || count == 0) ? true : false;
	}
	
	/**
	 * Obtem detalhes das vendas do produtoEdição nas datas de Lancamento e Recolhimento
	 * @param dataLancamento
	 * @param dataRecolhimento
	 * @param idProdutoRdicao
	 * @return List<ParcialVendaDTO>
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<ParcialVendaDTO> obterDetalhesVenda(Date dataLancamento, Date dataRecolhimento, Long idProdutoEdicao) {
		
        StringBuilder hql = new StringBuilder();
		
		hql.append(" select cota.numeroCota as numeroCota, ");
		hql.append(" pessoa.nome as nomeCota, ");
		hql.append(" epc.qtdeRecebida as reparte, ");
		hql.append(" epc.qtdeDevolvida as encalhe, ");
	    hql.append(" case when conferencia.juramentada = true then conferencia.qtde else 0 end as vendaJuramentada ");
		
		hql.append(" from ConferenciaEncalhe conferencia ");
		hql.append("	join conferencia.movimentoEstoqueCota movimento ");
		hql.append("	join conferencia.chamadaEncalheCota chamadaEncalheCota ");
		hql.append("	join chamadaEncalheCota.chamadaEncalhe chamadaEncalhe ");
		hql.append("	join movimento.estoqueProdutoCota epc ");
		hql.append("	join movimento.cota cota ");
		hql.append("	join epc.produtoEdicao produtoEdicao ");
		hql.append("	join cota.pessoa pessoa ");
		
		hql.append("	where chamadaEncalhe.dataRecolhimento >= :dataLancamento ");
		hql.append("	and chamadaEncalhe.dataRecolhimento <= :dataRecolhimento ");
		hql.append("	and produtoEdicao.id = :idProdutoEdicao ");
				
		Query query =  getSession().createQuery(hql.toString());
		
		query.setParameter("dataLancamento",dataLancamento);
		query.setParameter("dataRecolhimento",dataRecolhimento);
		query.setParameter("idProdutoEdicao",idProdutoEdicao);
		
		query.setResultTransformer(new AliasToBeanResultTransformer(ParcialVendaDTO.class));
		
		return query.list();
	}

	@Override
	public Lancamento obterLancamentoPosterior(Long idProdutoEdicao, Date dataRecolhimento) {
					
		Criteria criteria = super.getSession().createCriteria(Lancamento.class,"lancamento");
		
		criteria.createAlias("lancamento.periodoLancamentoParcial","periodo");
		
		criteria.createAlias("lancamento.produtoEdicao","produtoEdicao");
		
		criteria.add(Restrictions.eq("produtoEdicao.id", idProdutoEdicao));
		
		criteria.add(Restrictions.gt("lancamento.dataLancamentoDistribuidor", dataRecolhimento));
		
		criteria.addOrder(Order.asc("lancamento.dataLancamentoDistribuidor"));  
		
		criteria.setMaxResults(1);
		
		return (Lancamento) criteria.uniqueResult();
	
	}
	
	@Override
	public Lancamento obterLancamentoAnterior(Long idProdutoEdicao, Date dataLancamento) {
					
		Criteria criteria = super.getSession().createCriteria(Lancamento.class,"lancamento");
		
		criteria.createAlias("lancamento.periodoLancamentoParcial","periodo");
		
		criteria.createAlias("lancamento.produtoEdicao","produtoEdicao");
		
		criteria.add(Restrictions.eq("produtoEdicao.id", idProdutoEdicao));
		
		criteria.add(Restrictions.lt("lancamento.dataLancamentoDistribuidor", dataLancamento));
		
		criteria.addOrder(Order.asc("lancamento.dataLancamentoDistribuidor"));  
		
		criteria.setMaxResults(1);
		
		return (Lancamento) criteria.uniqueResult();
	
	}
}
