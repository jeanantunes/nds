package br.com.abril.nds.repository.impl;

import java.util.HashMap;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.TipoMovimentoDTO;
import br.com.abril.nds.dto.filtro.FiltroTipoMovimento;
import br.com.abril.nds.dto.filtro.FiltroTipoMovimento.ColunaOrdenacao;
import br.com.abril.nds.model.movimentacao.TipoMovimento;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.TipoMovimentoRepository;

/**
 * Classe de implementação referente ao acesso a dados da entidade 
 * {@link br.com.abril.nds.model.movimentacao.TipoMovimento}
 * 
 * @author Discover Technology
 */
@Repository
public class TipoMovimentoRepositoryImpl extends AbstractRepositoryModel<TipoMovimento, Long> 
										 implements TipoMovimentoRepository {

	@SuppressWarnings("unchecked")
	public List<TipoMovimento> obterTiposMovimento() {
		
		Criteria criteria = super.getSession().createCriteria(TipoMovimento.class);
		
		criteria.add(Restrictions.eq("aprovacaoAutomatica", false));
		
		criteria.addOrder(Order.asc("descricao"));
		
		return criteria.list();
	}
	
	/**
	 * Construtor padrão.
	 */
	public TipoMovimentoRepositoryImpl() {
		super(TipoMovimento.class);
	}

	@SuppressWarnings("unchecked")
	public List<TipoMovimentoDTO> obterTiposMovimento(FiltroTipoMovimento filtro) {
		
		HashMap<String, Object> param = new HashMap<String, Object>();
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select tipo.id as codigo, ");
		
		hql.append(" 		tipo.descricao as descricao, ");
		hql.append("		tipo.class as grupoOperacao, ");	
		hql.append("		tipo.operacaoEstoque as operacaoEstoque, ");
		hql.append("		tipo.operacaoFinaceira as operacaoFinanceira, ");
		hql.append("		tipo.aprovacaoAutomatica as aprovacao, ");
		hql.append("		tipo.incideDivida as incideDivida, ");
		hql.append("		tipo.grupoMovimentoFinaceiro as grupoMovimentoFinaceiro, ");
		hql.append("		tipo.grupoMovimentoEstoque as grupoMovimentoEstoque ");
		
				
		gerarFromWhere(filtro, hql, param);
		
		gerarOrdenacao(filtro, hql);		
				
		Query query =  getSession().createQuery(hql.toString());
				
		setParameters(query, param);
		
		query.setResultTransformer(new AliasToBeanResultTransformer(
				TipoMovimentoDTO.class));
		
		if(filtro.getPaginacao()!= null && filtro.getPaginacao().getPosicaoInicial() != null) 
			query.setFirstResult(filtro.getPaginacao().getPosicaoInicial());
		
		if(filtro.getPaginacao()!= null && filtro.getPaginacao().getQtdResultadosPorPagina() != null) 
			query.setMaxResults(filtro.getPaginacao().getQtdResultadosPorPagina());
		
		return query.list();
		
	}
	
	public Integer countObterTiposMovimento(FiltroTipoMovimento filtro) {
		
		HashMap<String, Object> param = new HashMap<String, Object>();
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select count(tipo) ");
	
		gerarFromWhere(filtro, hql, param);
				
		Query query =  getSession().createQuery(hql.toString());
				
		setParameters(query, param);
		
		Long qtde = (Long) query.uniqueResult();
		
		return (qtde==null) ? 0 : qtde.intValue();
		
	}

	private void gerarFromWhere(FiltroTipoMovimento filtro, StringBuilder hql, HashMap<String, Object> param) {
		
		hql.append(" from TipoMovimento tipo ");
		
		if(filtro.getCodigo() != null) {
			
			hql.append(param.isEmpty()?" where ":" and ");
			hql.append(" id =:codigo ");
			param.put("codigo", filtro.getCodigo());
		}
		
		if(filtro.getDescricao() != null) {
			
			hql.append(param.isEmpty()?" where ":" and ");
			hql.append(" upper(descricao) like :descricao ");
			param.put("descricao", "%" + filtro.getDescricao().toUpperCase() + "%");
		}
		
	}

	private void gerarOrdenacao(FiltroTipoMovimento filtro, StringBuilder hql) {
		
		String sortOrder = filtro.getPaginacao().getOrdenacao().name();
		ColunaOrdenacao coluna = FiltroTipoMovimento.ColunaOrdenacao.getPorDescricao(filtro.getPaginacao().getSortColumn());
		
		String nome = null;
		
		switch(coluna) {
			case CODIGO:
				nome = " codigo ";
				break;
			case DESCRICAO: 
				nome = " descricao ";
				break;
			case GRUPO_OPERACAO:
				nome = " grupoOperacao ";
				break;
			case OPERACAO:
				nome = " operacaoEstoque, operacaoFinanceira ";
				break;
			case APROVACAO:
				nome = " aprovacao ";
				break;
			case INCIDE_DIVIDA:
				nome = " incideDivida ";
				break;
			default:
				break;
		}
		
		hql.append( " order by " + nome + sortOrder + " ");
		
	}

	
	
	
	
}
