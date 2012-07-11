package br.com.abril.nds.repository.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.TipoProduto;
import br.com.abril.nds.repository.TipoProdutoRepository;
import br.com.abril.nds.util.StringUtil;
import br.com.abril.nds.vo.PaginacaoVO.Ordenacao;

/**
 * Classe de implementacao referente ao acesso a dados da entidade 
 * {@link br.com.abril.nds.model.cadastro.TipoProduto}
 * 
 * @author Discover Technology
 *
 */
@Repository
public class TipoProdutoRepositoryImpl extends AbstractRepositoryModel<TipoProduto,Long> implements TipoProdutoRepository {

	public TipoProdutoRepositoryImpl() {
		super(TipoProduto.class);
	}

	/* (non-Javadoc)
	 * @see br.com.abril.nds.repository.TipoProdutoRepository#busca(java.lang.String, java.lang.String, br.com.abril.nds.vo.PaginacaoVO.Ordenacao, int, int)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<TipoProduto> busca(String nomeTipoProduto, Long codigo, String codigoNCM, String codigoNBM, String orderBy,
			Ordenacao ordenacao, int initialResult, int maxResults) {
		
		Criteria criteria = addRestrictions(nomeTipoProduto, codigo, codigoNCM, codigoNBM);
		
		orderBy = (orderBy.equals("ncm")?"n.codigo":orderBy);
		
		if (Ordenacao.ASC == ordenacao) {
			criteria.addOrder(Order.asc(orderBy));
		} else if (Ordenacao.DESC == ordenacao) {
			criteria.addOrder(Order.desc(orderBy));
		}
		
		if (maxResults >= 0 && initialResult >= 0 ) {
			criteria.setMaxResults(maxResults);
			criteria.setFirstResult(initialResult);
		}
		return criteria.list();
	}

	/* (non-Javadoc)
	 * @see br.com.abril.nds.repository.TipoProdutoRepository#quantidade(java.lang.String)
	 */
	@Override
	public Long quantidade(String nomeTipoProduto, Long codigo, String codigoNCM, String codigoNBM) {
		
		Criteria criteria = addRestrictions(nomeTipoProduto, codigo, codigoNCM, codigoNBM);
		criteria.setProjection(Projections.rowCount());
		
		return (Long) criteria.list().get(0);
	}
	
	/**
	 * Adiciona as restricoes a consulta.
	 * @param nomeTipoProduto
	 * @param codigo
	 * @param codigoNCM
	 * @return
	 */
	private Criteria addRestrictions(String nomeTipoProduto, Long codigo, String codigoNCM, String codigoNBM) {
		
		Criteria criteria = getSession().createCriteria(TipoProduto.class,"tipoProduto");
		criteria.createAlias("tipoProduto.ncm", "n");
		
		if (!StringUtil.isEmpty(nomeTipoProduto)){
			criteria.add(Restrictions.ilike("descricao", nomeTipoProduto, MatchMode.ANYWHERE));
		}
		
		if (codigo != null) {
			criteria.add(Restrictions.eq("codigo", codigo));
		}
		
		if (!StringUtil.isEmpty(codigoNCM)) {
			criteria.add(Restrictions.eq("n.codigo", Long.parseLong(codigoNCM)));
		}
		
		if (!StringUtil.isEmpty(codigoNBM)) {
			criteria.add(Restrictions.eq("codigoNBM", codigoNBM));
		}
		
		return criteria;
	}

	/* (non-Javadoc)
	 * @see br.com.abril.nds.repository.TipoProdutoRepository#hasProdutoVinculado(br.com.abril.nds.model.cadastro.TipoProduto)
	 */
	@Override
	public boolean hasProdutoVinculado(TipoProduto tipoProduto) {
		
		Criteria criteria = getSession().createCriteria(Produto.class);
		
		criteria.add(Restrictions.eq("tipoProduto", tipoProduto));
		criteria.setProjection(Projections.rowCount());
		
		Long quantidade = (Long) criteria.list().get(0);
		
		return quantidade > 0;
	}

	/* (non-Javadoc)
	 * @see br.com.abril.nds.repository.TipoProdutoRepository#getMaxCodigo()
	 */
	@Override
	public Long getMaxCodigo() {
		
		Criteria criteria = getSession().createCriteria(TipoProduto.class);
		
		criteria.setProjection(Projections.max("codigo"));
		
		return (Long) criteria.list().get(0);
	}

	
	/**
	 * Obtem tipo de produto por código
	 * @param codigo
	 * @return TipoProduto
	 */
	@Override
	public TipoProduto obterPorCodigo(Long codigo) {
        Criteria criteria = getSession().createCriteria(TipoProduto.class);
		
		criteria.add(Restrictions.eq("codigo", codigo));
		
		return (TipoProduto) criteria.uniqueResult();
	}
	
}
