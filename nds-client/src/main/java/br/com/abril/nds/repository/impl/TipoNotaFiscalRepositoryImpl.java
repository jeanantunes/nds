package br.com.abril.nds.repository.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.cadastro.TipoAtividade;
import br.com.abril.nds.model.fiscal.GrupoNotaFiscal;
import br.com.abril.nds.model.fiscal.TipoNotaFiscal;
import br.com.abril.nds.model.fiscal.TipoOperacao;
import br.com.abril.nds.model.fiscal.nota.Serie;
import br.com.abril.nds.repository.TipoNotaFiscalRepository;
import br.com.abril.nds.util.StringUtil;
import br.com.abril.nds.vo.PaginacaoVO.Ordenacao;

@Repository
public class TipoNotaFiscalRepositoryImpl extends AbstractRepositoryModel<TipoNotaFiscal, Long> 
										  implements TipoNotaFiscalRepository {

	private final static String CFOP = "cfop";
	private final static String CODIGO_CFOP = ".codigo";
	private final static String PORCENTAGEM = "%";
	
	public TipoNotaFiscalRepositoryImpl() {
		super(TipoNotaFiscal.class);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<TipoNotaFiscal> obterTiposNotasFiscais() {

		String hql = " from TipoNotaFiscal tipoNotaFiscal ";
		
		Query query = getSession().createQuery(hql);
		
		return query.list();
	}
	
	
	@SuppressWarnings("unchecked")
	public List<TipoNotaFiscal> obterTiposNotasFiscais(TipoOperacao tipoOperacao) {

		String hql = " from TipoNotaFiscal tipoNotaFiscal where tipoNotaFiscal.tipoOperacao = :tipoOperacao  ";
		
		Query query = getSession().createQuery(hql);
		query.setParameter("tipoOperacao", tipoOperacao);
		
		return query.list();
	}

	@Override
	public TipoNotaFiscal obterTipoNotaFiscal(GrupoNotaFiscal grupoNotaFiscal) {
		
		String hql = " from TipoNotaFiscal tipoNotaFiscal where tipoNotaFiscal.grupoNotaFiscal = :grupoNotaFiscal  ";
		
		Query query = getSession().createQuery(hql);
		
		query.setParameter("grupoNotaFiscal", grupoNotaFiscal);
		
		return (TipoNotaFiscal) query.uniqueResult();
	}

	/* (non-Javadoc)
	 * @see br.com.abril.nds.repository.TipoNotaFiscalRepository#obterTiposNotasFiscais(java.lang.String, java.lang.String, br.com.abril.nds.model.cadastro.TipoAtividade, java.lang.String, br.com.abril.nds.vo.PaginacaoVO.Ordenacao, int, int)
	 */
	@Override
	public List<TipoNotaFiscal> obterTiposNotasFiscais(String cfop, String tipoNota, TipoAtividade tipoAtividade, String orderBy, Ordenacao ordenacao, Integer initialResult, Integer maxResults) {

		Criteria criteria = addRestrictions(cfop, tipoNota, tipoAtividade);
		
		if (!StringUtil.isEmpty(orderBy) && ordenacao != null) {

			// Caso tenha que fazer ordenação pelo código do CFOP
			if (orderBy.toLowerCase().startsWith(CFOP)) {
				orderBy = orderBy + CODIGO_CFOP;
			}

			if(Ordenacao.ASC ==  ordenacao) {
				criteria.addOrder(Order.asc(orderBy));
			} else if(Ordenacao.DESC ==  ordenacao) {
				criteria.addOrder(Order.desc(orderBy));
			}
		}
		
		if (maxResults != null)
			criteria.setMaxResults(maxResults);
		
		if (initialResult != null)
			criteria.setFirstResult(initialResult);
		
		return criteria.list();
		
	}

	/* (non-Javadoc)
	 * @see br.com.abril.nds.repository.TipoNotaFiscalRepository#obterQuantidadeTiposNotasFiscais(java.lang.String, java.lang.String, br.com.abril.nds.model.cadastro.TipoAtividade)
	 */
	@Override
	public Long obterQuantidadeTiposNotasFiscais(String cfop, String tipoNota, TipoAtividade tipoAtividade) {
		Criteria criteria = addRestrictions(cfop, tipoNota, tipoAtividade);
		criteria.setProjection(Projections.rowCount());

		return (Long)criteria.list().get(0);
	}
	
	/**
	 * Adiciona restrições
	 * @param codigoBox
	 * @param tipoNota
	 * @param tipoAtividade
	 * @return
	 */
	private Criteria addRestrictions(String cfop, String tipoNota, TipoAtividade tipoAtividade) {
		Criteria criteria =  getSession().createCriteria(TipoNotaFiscal.class);	

		// Relacionamentos com a tabela CFOP
		criteria.createCriteria("cfopEstado", "cfopEstado", Criteria.LEFT_JOIN);
		criteria.createCriteria("cfopOutrosEstados", "cfopOutrosEstados", Criteria.LEFT_JOIN);

		if(!StringUtil.isEmpty(cfop)) {
			// Busca apenas pelo código (sem o primeiro dígito de identificação dentro do estado e fora do estado) 
			if (cfop.length() == 3) {
				criteria.add(Restrictions.or(Restrictions.ilike("cfopEstado.codigo", PORCENTAGEM + cfop), Restrictions.ilike("cfopOutrosEstados.codigo", PORCENTAGEM + cfop)));
			} else {
				criteria.add(Restrictions.or(Restrictions.eq("cfopEstado.codigo", cfop), Restrictions.eq("cfopOutrosEstados.codigo", cfop)));
			}
		}
		
		if(!StringUtil.isEmpty(tipoNota)) {
			criteria.add(Restrictions.ilike("nopDescricao", PORCENTAGEM + tipoNota.toLowerCase() + PORCENTAGEM));
		}
		
		if (tipoAtividade != null) {
			criteria.add(Restrictions.eq("tipoAtividade", tipoAtividade));
		}
		
		return criteria;
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<TipoNotaFiscal> obterTiposNotasFiscaisPorTipoAtividadeDistribuidor(TipoAtividade tipoAtividade) {

		Criteria criteria =  getSession().createCriteria(TipoNotaFiscal.class);	

		criteria.add(Restrictions.eq("tipoAtividade", tipoAtividade));
		
		return criteria.list();
	}
}
