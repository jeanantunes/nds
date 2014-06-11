package br.com.abril.nds.repository.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.ParcialDTO;
import br.com.abril.nds.dto.filtro.FiltroParciaisDTO;
import br.com.abril.nds.dto.filtro.FiltroParciaisDTO.ColunaOrdenacao;
import br.com.abril.nds.model.planejamento.LancamentoParcial;
import br.com.abril.nds.model.planejamento.TipoLancamento;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.LancamentoParcialRepository;

@Repository
public class LancamentoParcialRepositoryImpl extends AbstractRepositoryModel<LancamentoParcial, Long> 
			implements LancamentoParcialRepository  {

	public LancamentoParcialRepositoryImpl() {
		super(LancamentoParcial.class);
	}

	@Override
	public LancamentoParcial obterLancamentoPorProdutoEdicao(
			Long idProdutoEdicao) {
		
		Criteria criteria = getSession().createCriteria(LancamentoParcial.class);
		
		criteria.add(Restrictions.eq("produtoEdicao.id", idProdutoEdicao));
		
		Object lancamento = criteria.uniqueResult();
		
		return (lancamento!=null) ? (LancamentoParcial) lancamento : null ;	
	}

	@Override
	public LancamentoParcial obterLancamentoParcial(Long idProdutoEdicao, Date dataRecolhimentoFinal) {
		
		Criteria criteria = getSession().createCriteria(LancamentoParcial.class);
		
		criteria.add(Restrictions.eq("produtoEdicao.id", idProdutoEdicao));
		
		criteria.add(Restrictions.eq("recolhimentoFinal", dataRecolhimentoFinal));
		
		Object lancamento = criteria.uniqueResult();
		
		return (lancamento!=null) ? (LancamentoParcial) lancamento : null ;	
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ParcialDTO> buscarLancamentosParciais(FiltroParciaisDTO filtro) {
		
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select distinct lancamentoParcial.id as idLancamentoParcial, produtoEdicao.id as idProdutoEdicao, ");
		hql.append(" 		lancamentoParcial.lancamentoInicial as dataLancamento, ");
		hql.append(" 		lancamentoParcial.recolhimentoFinal as dataRecolhimento, ");
		hql.append(" 		produto.codigo as codigoProduto, ");
		hql.append(" 		produto.nome as nomeProduto, ");
		hql.append(" 		produtoEdicao.numeroEdicao as numEdicao, ");
		hql.append(" 		juridica.razaoSocial as nomeFornecedor, ");
		hql.append(" 		lancamentoParcial.status as statusParcial, ");
		hql.append("        produtoEdicao.precoVenda as precoCapa, ");
		hql.append("        produtoEdicao.origem as origem ");
		
		hql.append(getSqlFromEWhereLancamentosParciais(filtro));
		
		hql.append(getOrderByLancamentosParciais(filtro));
		
		Query query =  getSession().createQuery(hql.toString());
		
		HashMap<String, Object> param = buscarParametrosLancamentosParciais(filtro);
		
		setParameters(query, param);
		
		query.setResultTransformer(new AliasToBeanResultTransformer(
				ParcialDTO.class));
		
		if(filtro.getPaginacao()!= null){

			if(filtro.getPaginacao().getQtdResultadosPorPagina() != null) 
				query.setFirstResult(filtro.getPaginacao().getPosicaoInicial());
			
			if(filtro.getPaginacao().getQtdResultadosPorPagina() != null) 
				query.setMaxResults(filtro.getPaginacao().getQtdResultadosPorPagina());
		}
		
		return query.list();
	}
	
	private String getSqlFromEWhereLancamentosParciais(FiltroParciaisDTO filtro) {
		
		StringBuilder hql = new StringBuilder();

		hql.append(" from Lancamento lancamento ");
		hql.append(" join lancamento.periodoLancamentoParcial periodo ");
		hql.append(" join periodo.lancamentoParcial lancamentoParcial ");
		hql.append(" join lancamentoParcial.produtoEdicao produtoEdicao ");
		hql.append(" join produtoEdicao.produto produto ");
		hql.append(" join produto.fornecedores fornecedor ");
		hql.append(" join fornecedor.juridica juridica ");
		
		hql.append(" where periodo.status <> 'CANCELADO' ");
		
		hql.append(" and lancamentoParcial.status <> 'CANCELADO' ");
		
		hql.append(" and lancamento.tipoLancamento =:tipoLancamento");
		
		if(filtro.getCodigoProduto() != null) { 
			hql.append(" and  produto.codigo =:codProduto ");
		}
		
		if(filtro.getNomeProduto() != null) { 
			hql.append(" and  lower(produto.nome)like:nomeProduto ");
		}
		
		if(filtro.getEdicaoProduto() != null){ 
			hql.append( " and  produtoEdicao.numeroEdicao=:edProduto ");
		}
		
		if(filtro.getIdFornecedor() != null) { 
			hql.append(" and  fornecedor.id=:idFornecedor ");
		}
		
		if(filtro.getDataInicialDate() != null) { 
			hql.append(" and  lancamentoParcial.lancamentoInicial>=:dtInicial ");
		}
		
		if(filtro.getDataFinalDate() != null) { 
			hql.append(" and  lancamentoParcial.lancamentoInicial<=:dtFinal ");
		}
		
		if(filtro.getStatus() != null) { 
			hql.append( " and  lancamento.status=:status ");
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
				hql.append(" order by produto.nome ");
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
		
		param.put("tipoLancamento", TipoLancamento.LANCAMENTO);
		
		if(filtro.getCodigoProduto() != null) 
			param.put("codProduto", filtro.getCodigoProduto());
		
		if(filtro.getNomeProduto() != null) 
			param.put("nomeProduto", "%" + filtro.getNomeProduto().toLowerCase() + "%");
		
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
	public Integer totalbuscaLancamentosParciais(FiltroParciaisDTO filtro) {
		
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select count(distinct lancamentoParcial.id ) ");
		
		hql.append(getSqlFromEWhereLancamentosParciais(filtro));
		
		Query query =  getSession().createQuery(hql.toString());
		
		HashMap<String, Object> param = buscarParametrosLancamentosParciais(filtro);
		
		setParameters(query, param);
		Long totalRegistros = (Long) query.uniqueResult();
		
		return (totalRegistros == null) ? 0 : totalRegistros.intValue();
	}

}
