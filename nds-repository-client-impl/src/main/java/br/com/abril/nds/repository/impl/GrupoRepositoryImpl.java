package br.com.abril.nds.repository.impl;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.persister.collection.CollectionPropertyNames;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.DiaSemana;
import br.com.abril.nds.model.cadastro.GrupoCota;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.GrupoRepository;
import br.com.abril.nds.util.StringUtil;

@Repository
public class GrupoRepositoryImpl extends AbstractRepositoryModel<GrupoCota, Long> implements GrupoRepository {

	public GrupoRepositoryImpl() {
		super(GrupoCota.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<GrupoCota> obterGruposCota(Date data) {
		
		Criteria criteria = super.getSession().createCriteria(GrupoCota.class);

		addDataVigencia(data, criteria);
		
		return criteria.list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<DiaSemana> obterDiasOperacaoDiferenciadaCota(final Integer numeroCota, final Date dataOperacao) {
	    Criteria criteria = super.getSession().createCriteria(GrupoCota.class);
	    criteria.createAlias("diasRecolhimento", "dia");
	    criteria.createAlias("cotas", "cota");
	    
	    addDataVigencia(dataOperacao, criteria);
	    criteria.add(Restrictions.eq("cota.numeroCota", numeroCota));
	    
	    criteria.setProjection(Projections.distinct(Projections.property("dia."+ CollectionPropertyNames.COLLECTION_ELEMENTS)));
	    
		
		return (List<DiaSemana>) criteria.list();
		
	}
	
	@Override
	public Boolean existeGrupoCota(String nome, Long idGrupo, Date dataOperacao) {
	    
	    Criteria criteria = getSession().createCriteria(GrupoCota.class);
	    
	    addDataVigencia(dataOperacao, criteria);
	    
	    criteria.add(Restrictions.eq("nome", nome));
	    if (idGrupo != null) {
	        criteria.add(Restrictions.ne("id", idGrupo));
	    }
	    
	    criteria.setProjection(Projections.rowCount());
	    
		return (Long) criteria.uniqueResult() > 0;
	}
	
	@Override
	public Integer countTodosGrupos(Date dataOperacao) {

		Criteria criteria = super.getSession().createCriteria(GrupoCota.class); 
		addDataVigencia(dataOperacao, criteria);
		criteria.setProjection(Projections.rowCount());
		return ((Long)criteria.list().get(0)).intValue();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<GrupoCota> obterGruposAtivos(String sortname, String sortorder, boolean includeHistory) {
		
	    Criteria criteria = getSession().createCriteria(GrupoCota.class);
	    
	    if(!StringUtil.isEmpty(sortname) && !StringUtil.isEmpty(sortorder)) {
	        if("asc".equals(sortorder)){
	            criteria.addOrder(Order.asc(sortname));
	        }else{
	            criteria.addOrder(Order.desc(sortname));
	        }
        }
	    
	    if(!includeHistory){
            criteria.add(Restrictions.isNull("dataFimVigencia"));
        }else{
            criteria.addOrder(Order.asc("dataFimVigencia"));
        }
	    
		return criteria.list();
	}

	@Override
	public String obterNomeGrupoPorCota(Long idCota, Long idGrupoIgnorar, Date dataOperacao) {
	    
	    Criteria criteria = getSession().createCriteria(GrupoCota.class);
	    criteria.createAlias("cotas", "cota");
	    
	    addDataVigencia(dataOperacao, criteria);
	    
	    criteria.add(Restrictions.eq("cota.id", idCota));
	    if (idGrupoIgnorar != null){
            criteria.add(Restrictions.ne("id", idGrupoIgnorar));
        }
		
	    criteria.setProjection(Projections.property("nome"));
        
        
        return (String) criteria.uniqueResult();
	}
	
	@Override
	public String obterNomeGrupoPorMunicipio(String municipio, Long idGrupoIgnorar, Date dataOperacao) {
	    
	    Criteria criteria = getSession().createCriteria(GrupoCota.class);
	    criteria.createAlias("municipios", "municipio");
	    
	    addDataVigencia(dataOperacao, criteria);
	    
	    criteria.add(Restrictions.eq("municipio."+ CollectionPropertyNames.COLLECTION_ELEMENTS, municipio));
	    
	    if (idGrupoIgnorar != null){
	        criteria.add(Restrictions.ne("id", idGrupoIgnorar));
	    }
	    
	    criteria.setProjection(Projections.property("nome"));
		
		
		return (String) criteria.uniqueResult();
	}

    /**
     * @param dataOperacao
     * @param criteria
     */
    private void addDataVigencia(Date dataOperacao, Criteria criteria) {
        criteria.add(Restrictions.le("dataInicioVigencia", dataOperacao));
        
        criteria.add(Restrictions.or(Restrictions.isNull("dataFimVigencia"), Restrictions.ge("dataFimVigencia", dataOperacao)));
    }
	
	@Override
	@SuppressWarnings("unchecked")
	public Set<Long> obterIdsCotasGrupo(Long idGrupo){
		
		StringBuilder hql = new StringBuilder("select cota.id ");
		hql.append(" from GrupoCota g ")
		   .append(" join g.cotas cota ")
		   .append(" where g.id = :idGrupo ");
		
		Query query = this.getSession().createQuery(hql.toString());
		query.setParameter("idGrupo", idGrupo);
		
		return new HashSet<Long>(query.list());
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public Set<String> obterMunicipiosCotasGrupo(Long idGrupo){
		
		StringBuilder hql = new StringBuilder("select municipios ");
		hql.append(" from GrupoCota g ")
		   .append(" join g.municipios municipios ")
		   .append(" where g.id = :idGrupo ");
		
		Query query = this.getSession().createQuery(hql.toString());
		query.setParameter("idGrupo", idGrupo);
		
		return new HashSet<String>(query.list());
	}
}
