package br.com.abril.nds.repository.impl;

import java.util.HashMap;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.PeriodoParcialDTO;
import br.com.abril.nds.dto.filtro.FiltroParciaisDTO;
import br.com.abril.nds.dto.filtro.FiltroParciaisDTO.ColunaOrdenacao;
import br.com.abril.nds.model.planejamento.PeriodoLancamentoParcial;
import br.com.abril.nds.repository.PeriodoLancamentoParcialRepository;

@Repository
public class PeriodoLancamentoParcialRepositoryImpl extends AbstractRepository<PeriodoLancamentoParcial, Long> 
			implements PeriodoLancamentoParcialRepository {

	public PeriodoLancamentoParcialRepositoryImpl() {
		super(PeriodoLancamentoParcial.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PeriodoParcialDTO> obterPeriodosParciais(FiltroParciaisDTO filtro) {
				
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select rownum as periodo, ");
		hql.append(" 		lancamento.dataLancamentoDistribuidor as dataLancamento, ");
		hql.append(" 		lancamento.dataRecolhimentoDistribuidor as dataRecolhimento, ");
		hql.append(" 		estudo.reparte as reparte, ");
		
		hql.append("		(select sum(movimento.qtde) from ConferenciaEncalhe conferecia");
		hql.append("		 	join conferencia.movimentoEstoqueCota movimento ");
		hql.append("		 	join conferencia.lancamento lancamentoC ");
		hql.append("		 	where lancamentoC.id=lancamento.id)");
		hql.append(" 		as encalhe, ");
		
		hql.append(" 		10 as vendas, ");
		hql.append(" 		10 as vendaAcumulada, ");
		hql.append(" 		10 as percVenda ");
		
		hql.append(getSqlFromEWhereLancamentosParciais(filtro));
				
		//hql.append(getOrderByLancamentosParciais(filtro));
		
		Query query =  getSession().createQuery(hql.toString());
		
//		HashMap<String, Object> param = buscarParametrosLancamentosParciais(filtro);
//		
//		for(String key : param.keySet()){
//			query.setParameter(key, param.get(key));
//		}
//		
		query.setResultTransformer(new AliasToBeanResultTransformer(
				PeriodoParcialDTO.class));
		
//		if(filtro.getPaginacao().getQtdResultadosPorPagina() != null) 
//			query.setFirstResult(filtro.getPaginacao().getPosicaoInicial());
//		
//		if(filtro.getPaginacao().getQtdResultadosPorPagina() != null) 
//			query.setMaxResults(filtro.getPaginacao().getQtdResultadosPorPagina());
				
		return query.list();
	}
	
	private String getSqlFromEWhereLancamentosParciais(FiltroParciaisDTO filtro) {
		
		StringBuilder hql = new StringBuilder();
	

		hql.append(" from Lancamento lancamento ");
		hql.append(" join lancamento.produtoEdicao produtoEdicao ");
		hql.append(" left join lancamento.estudo estudo ");
		
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
			hql.append( (usarAnd ? " and ":" where ") + " lancamentoParcial.lancamentoInicial>=:dtInicial ");
			usarAnd = true;
		}
		
		if(filtro.getDataFinalDate() != null) { 
			hql.append( (usarAnd ? " and ":" where ") + " lancamentoParcial.recolhimentoFinal<=:dtFinal ");
			usarAnd = true;
		}
		
		if(filtro.getStatus() != null) { 
			hql.append( (usarAnd ? " and ":" where ") + " lancamentoParcial.status=:status ");
			usarAnd = true;
		}

		return hql.toString();
	}

	private String getOrderByLancamentosParciais(FiltroParciaisDTO filtro){
		
		if(filtro.getPaginacao() == null || filtro.getPaginacao().getSortColumn() == null){
			return "";
		}
		
		ColunaOrdenacao coluna = ColunaOrdenacao.getPorDescricao(filtro.getPaginacao().getSortColumn());
		
		StringBuilder hql = new StringBuilder();
		
		switch (coluna) {
			case CODIGO_PRODUTO:	
				hql.append(" order by produto.codigo ");
				break;
			case DATA_LANCAMENTO:
				hql.append(" order by lancamentoParcial.lancamentoInicial ");
				break;
			case DATA_RECOLHIMENTO:
				hql.append(" order by lancamentoParcial.recolhimentoFinal ");
				break;	
			case NOME_FORNECEDOR:
				hql.append(" order by juridica.razaoSocial ");
				break;	
			case NOME_PRODUTO:
				hql.append(" order by produto.descricao ");
				break;	
			case NUM_EDICAO:
				hql.append(" order by produtoEdicao.numeroEdicao ");
				break;	
			case STATUS_PARCIAL:
				hql.append(" order by lancamentoParcial.status ");
				break;	
			default:
				hql.append(" order by lancamentoParcial.recolhimentoFinal ");
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
		
		hql.append(" select count(lancamentoParcial) ");
		
		hql.append(getSqlFromEWhereLancamentosParciais(filtro));
		
		Query query =  getSession().createQuery(hql.toString());
		
		HashMap<String, Object> param = buscarParametrosLancamentosParciais(filtro);
		
		for(String key : param.keySet()){
			query.setParameter(key, param.get(key));
		}	
		
		Long totalRegistros = (Long) query.uniqueResult();
		
		return (totalRegistros == null) ? 0 : totalRegistros.intValue();
	}
}
