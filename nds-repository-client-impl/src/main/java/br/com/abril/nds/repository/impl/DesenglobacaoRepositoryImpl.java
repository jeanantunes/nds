package br.com.abril.nds.repository.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.model.distribuicao.Desenglobacao;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.DesenglobacaoRepository;

@Repository
public class DesenglobacaoRepositoryImpl extends AbstractRepositoryModel<Desenglobacao, Long> implements DesenglobacaoRepository {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(DesenglobacaoRepositoryImpl.class);


    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    public DesenglobacaoRepositoryImpl() {
	super(Desenglobacao.class);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Desenglobacao> obterDesenglobacaoPorCota(Integer numeroCota) {

	StringBuilder hql = new StringBuilder(" from Desenglobacao d ");

	if(numeroCota != null){
	    hql.append("where d.cotaEnglobada.numeroCota = :numeroCota ");
	}
	Query query = getSession().createQuery(hql.toString());
	if(numeroCota != null){
	    query.setParameter("numeroCota", numeroCota);			
	}
	return query.list();
    }

    @Transactional(readOnly = true)
    public List<Desenglobacao> obterDesenglobacaoPorCotaDesenglobada(Integer numeroCota) {

	StringBuilder hql = new StringBuilder("");
	hql.append(" from Desenglobacao d where d.cotaDesenglobada.numeroCota = :numeroCota ");
	Query query = getSession().createQuery(hql.toString());
	query.setParameter("numeroCota", numeroCota);
	return query.list();
    }
    
    @Transactional(readOnly = true)
    public Desenglobacao obterDesenglobacao(Integer numeroCotaDesenglobada, Integer numeroCotaEnglobada) {

		StringBuilder hql = new StringBuilder();
		
		hql.append(" from Desenglobacao d where d.cotaDesenglobada.numeroCota = :numeroCotaDesenglobada ");
		hql.append(" and d.cotaEnglobada.numeroCota = :numeroCotaEnglobada ");
		
		Query query = getSession().createQuery(hql.toString());
		
		query.setParameter("numeroCotaDesenglobada", numeroCotaDesenglobada);
		query.setParameter("numeroCotaEnglobada", numeroCotaEnglobada);
		
		return (Desenglobacao) query.uniqueResult();
    }

    @Transactional(readOnly = true)
    public Float verificaPorcentagemCota(Long cotaId) {
	return ((Number) getSession().createCriteria(Desenglobacao.class)
		.add(Restrictions.eq("cotaDesenglobada.id", cotaId))
		.setProjection(Projections.sum("englobadaPorcentagemCota"))
		.uniqueResult()).floatValue();
    }

    @Override
    public void inserirCotasDesenglobadas(final List<Desenglobacao> cotasDesenglobadas) {

		for (Desenglobacao desenglobacao : cotasDesenglobadas) {
			
			Desenglobacao desenglobacaoExistente = 
				this.obterDesenglobacao(
					desenglobacao.getCotaDesenglobada().getNumeroCota(), 
						desenglobacao.getCotaEnglobada().getNumeroCota());
			
			if (desenglobacaoExistente != null) {
				
				desenglobacaoExistente.setPorcentagemCota(desenglobacao.getPorcentagemCota());
				
				alterar(desenglobacaoExistente);
				
			} else {
			
				adicionar(desenglobacao);
			}
		}
    }

    @Override
    public boolean removerPorCotaDesenglobada(Long idCota) {
	boolean res=Boolean.TRUE;
	try {
	    String hql = "delete from Desenglobacao d where d.cotaDesenglobada.id = :idCota";
	    getSession().createQuery(hql).setLong("idCota", idCota)
	    .executeUpdate();
	} catch (Exception e) {
	    LOGGER.error(e.getMessage(), e);
	    res=Boolean.FALSE;
	}
	return res;
    }
}
