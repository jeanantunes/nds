package br.com.abril.nds.repository.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.persister.collection.CollectionPropertyNames;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.ProdutoEmissaoDTO;
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
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select diaRecolhimento	");
		
		hql.append(" from GrupoCota g ");

		hql.append(" inner join g.diasRecolhimento diaRecolhimento	");
	
		hql.append(" left join g.cotas cota ");
		
		hql.append(" left join g.municipios municipio ");
		
		hql.append(" where ");
		
		hql.append(" g.dataInicioVigencia <= :dataOperacao and ");
		
		hql.append(" ( g.dataFimVigencia is null or g.dataFimVigencia >= :dataOperacao ) and ");
		
		hql.append(" ( ");
	    
		hql.append(" cota.numeroCota = :numeroCota ");
		
		hql.append(" or municipio = ");
			
		hql.append(" 	( ");
		
		hql.append(" 	select max(ender.cidade) from Cota cota "); 
			
		hql.append(" 	inner join cota.pdvs pdv ");
			
		hql.append(" 	inner join pdv.enderecos enderecoPDV ");
		
		hql.append("    inner join enderecoPDV.endereco ender  ");
			
		hql.append(" 	where cota.numeroCota = :numeroCota and ");
		
		hql.append("	pdv.caracteristicas.pontoPrincipal = true and	");
		
		hql.append(" 	enderecoPDV.principal = true ");
		
		hql.append(" 	) ");
		
		hql.append(" ) ");
		
		
		hql.append(" group by diaRecolhimento ");
		
		Query query = getSession().createQuery(hql.toString());
		
		query.setParameter("dataOperacao", dataOperacao);
		
		query.setParameter("numeroCota", numeroCota);
		
		return (List<DiaSemana>) query.list();
		
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
	
	/**
	 * Obtém lista de GrupoCota(Operação diferenciada)
	 * @param idCota
	 * @param dataOperacao
	 * @return List<GrupoCota>
	 */ 
	@SuppressWarnings("unchecked")
	@Override
	public List<GrupoCota> obterListaGrupoCotaPorCotaId(Long idCota, Date dataOperacao){
		
		StringBuilder hql = new StringBuilder("select g ");
		hql.append(" from GrupoCota g ")
		   .append(" left join g.municipios municipio ")
		   .append(" left join g.cotas cota ")
		   .append(" where ")
		   .append(" ( ")
		   .append("     cota.id = :idCota ")
		   .append("     or municipio in (select e.cidade ")
		   .append("                      from PDV pdv ")
		   .append("                      join pdv.enderecos enderecoPdv   ")
           .append("                      join enderecoPdv.endereco e   ")
           .append("                      join pdv.cota c ")
		   .append("                      where c.id = :idCota  ")
		   .append("                      and pdv.caracteristicas.pontoPrincipal is true ")
		   .append("                     ) ")
	       .append(" ) ")
		   .append(" and ( g.dataInicioVigencia is null or g.dataInicioVigencia <= :dataOperacao ) ")
		   .append(" and ( g.dataFimVigencia is null or g.dataFimVigencia >= :dataOperacao ) ");
		   
		Query query = this.getSession().createQuery(hql.toString());
		
		query.setParameter("idCota", idCota);
		
		query.setParameter("dataOperacao", dataOperacao);
		
		return (List<GrupoCota>) query.list();
	}	
	
	
	@Override
	@SuppressWarnings("unchecked")
	public Map<Long, List<GrupoCota>> obterListaGrupoCotaPorDataOperacao(Date dataOperacao){
		
		StringBuilder hql = new StringBuilder("select g ");
		hql.append(" from GrupoCota g ")
		   .append(" left join g.municipios municipio ")
		   .append(" left join g.cotas cota ")
		   .append(" where ")
		   .append(" ( municipio in (select e.cidade ")
		   .append("                      from PDV pdv ")
		   .append("                      join pdv.enderecos enderecoPdv   ")
           .append("                      join enderecoPdv.endereco e   ")
           .append("                      join pdv.cota c ")
		   .append("                      where pdv.caracteristicas.pontoPrincipal is true ")
		   .append("                     ) ")
	       .append(" ) ")
		   .append(" and ( g.dataInicioVigencia is null or g.dataInicioVigencia <= :dataOperacao ) ")
		   .append(" and ( g.dataFimVigencia is null or g.dataFimVigencia >= :dataOperacao ) ");
		   
		Query query = this.getSession().createQuery(hql.toString());
		
		Map<Long, List<GrupoCota>> mapGrupoCotas = new HashMap<>(); 
		
		query.setParameter("dataOperacao", dataOperacao);
		
		List<GrupoCota> listaProdutoEmissaoCota = query.list();
		
		for (GrupoCota grupoCota : listaProdutoEmissaoCota) {
		    
		    List<GrupoCota> gruposCotas = mapGrupoCotas.get(grupoCota.getId());
		    
		    if (gruposCotas == null) {
		        
		        gruposCotas = new ArrayList<GrupoCota>();
		    }
		    
		    gruposCotas.add(grupoCota);
		    
		    mapGrupoCotas.put(grupoCota.getId(), gruposCotas);
		}
		
		return mapGrupoCotas;
	}
	
	
	/**
	 * Obtém lista de GrupoCota(Operação diferenciada)
	 * @param numeroCota
	 * @param dataOperacao
	 * @return List<GrupoCota>
	 */ 
	@SuppressWarnings("unchecked")
	@Override
	public List<GrupoCota> obterListaGrupoCotaPorNumeroCota(Integer numeroCota, Date dataOperacao){
		
		StringBuilder hql = new StringBuilder("select g ");
		hql.append(" from GrupoCota g ")
		   .append(" left join g.municipios municipio ")
		   .append(" left join g.cotas cota ")
		   .append(" where ")
		   .append(" ( ")
		   .append("     cota.numeroCota = :numeroCota ")
		   .append("     or municipio in (select e.cidade ")
		   .append("                      from PDV pdv ")
		   .append("                      join pdv.enderecos enderecoPdv   ")
           .append("                      join enderecoPdv.endereco e   ")
           .append("                      join pdv.cota c ")
		   .append("                      where c.numeroCota = :numeroCota  ")
		   .append("                      and pdv.caracteristicas.pontoPrincipal is true ")
		   .append("                     ) ")
	       .append(" ) ")
		   .append(" and ( g.dataInicioVigencia is null or g.dataInicioVigencia <= :dataOperacao ) ")
		   .append(" and ( g.dataFimVigencia is null or g.dataFimVigencia >= :dataOperacao ) ");
		   
		Query query = this.getSession().createQuery(hql.toString());
		
		query.setParameter("numeroCota", numeroCota);
		
		query.setParameter("dataOperacao", dataOperacao);
		
		return (List<GrupoCota>) query.list();
	}	
}
