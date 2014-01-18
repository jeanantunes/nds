package br.com.abril.nds.repository.impl;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
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

		criteria.add(Restrictions.lt("dataCadastro", data));
		
		return criteria.list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<DiaSemana> obterDiasOperacaoDiferenciadaCota(Integer numeroCota) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" SELECT distinct(dia)					");
		hql.append(" FROM GrupoCota grupoCota 				");
		hql.append(" join grupoCota.diasRecolhimento dia  	");
		hql.append(" join grupoCota.cotas cota 				");
		hql.append(" WHERE cota.numeroCota = :numeroCota 	");
		
		Query query = this.getSession().createQuery(hql.toString());
		
		query.setParameter("numeroCota", numeroCota);
		
		return (List<DiaSemana>) query.list();
		
	}
	
	@Override
	public Boolean existeGrupoCota(String nome, Long idGrupo) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select case when count(grupoCota) = 0 then false else true end ");
		hql.append(" from GrupoCota grupoCota where grupoCota.nome = :nome ");
		
		if (idGrupo != null) {
			
			hql.append(" and grupoCota.id <> :idGrupo ");
		}
		
		Query query = this.getSession().createQuery(hql.toString());
		
		query.setParameter("nome", nome);
		
		if (idGrupo != null) {
			
			query.setParameter("idGrupo", idGrupo);
		}
		
		return (Boolean) query.uniqueResult();
	}
	
	@Override
	public Integer countTodosGrupos() {

		Criteria criteria = super.getSession().createCriteria(GrupoCota.class); 
		criteria.setProjection(Projections.rowCount());
		return ((Long)criteria.list().get(0)).intValue();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<GrupoCota> obterGrupos(String sortname, String sortorder) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select grupoCota from GrupoCota grupoCota ");
		
		if(!StringUtil.isEmpty(sortname) && !StringUtil.isEmpty(sortorder)) {
		
			if (sortname.equals("nome")) {
				hql.append(" ORDER BY grupoCota.nome ");
			}
			
			hql.append(sortorder);
		}
		
		Query query = this.getSession().createQuery(hql.toString());
				
		return query.list();
	}

	@Override
	public String obterNomeGrupoPorCota(Long idCota, Long idGrupoIgnorar) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select grupoCota.nome from GrupoCota grupoCota ")
		   .append(" join grupoCota.cotas cota ")
		   .append(" where cota.id = :idCota ");
		
		if (idGrupoIgnorar != null){
			
			hql.append(" and grupoCota.id != :idGrupoIgnorar ");
		}
		
		Query query = this.getSession().createQuery(hql.toString());
		
		query.setParameter("idCota", idCota);
		
		if (idGrupoIgnorar != null){
			
			query.setParameter("idGrupoIgnorar", idGrupoIgnorar);
		}
		
		return (String) query.uniqueResult();
	}
	
	@Override
	public String obterNomeGrupoPorMunicipio(String municipio) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select grupoCota.nome from GrupoCota grupoCota ")
		   .append(" join grupoCota.municipios mun ")
		   .append(" where :municipio in (mun) ");
		
		Query query = this.getSession().createQuery(hql.toString());
		
		query.setParameter("municipio", municipio);
		
		return (String) query.uniqueResult();
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