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
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.persister.collection.CollectionPropertyNames;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.CotaEmissaoDTO;
import br.com.abril.nds.dto.filtro.FiltroEmissaoCE;
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
	
    public List<Integer> obterCotasComOperacaoDiferenciada(FiltroEmissaoCE filtro) {
    	
    	StringBuilder sql = new StringBuilder();
    	
    	sql.append(" select cota.numero_cota as numeroCota ");
    	sql.append(" from cota ");
    	sql.append(" inner join (  ");
    	sql.append(getSQLCotasOperacaoDiferenciadas());
    	sql.append(" ) as cod on ( cod.idCota = cota.id ) ");
		Query query = this.getSession().createSQLQuery(sql.toString());
		
		query.setParameter("dataInicio", filtro.getDtRecolhimentoDe());
		query.setParameter("dataFim", filtro.getDtRecolhimentoAte());
		query.setParameter("numeroCotaInicio", filtro.getNumCotaDe());
		query.setParameter("numeroCotaFim", filtro.getNumCotaAte());
		
		((SQLQuery) query).addScalar("numeroCota", StandardBasicTypes.INTEGER);
		
		return query.list();

    }
    

    public List<CotaEmissaoDTO> obterCotasSemOperacaoDiferenciada(FiltroEmissaoCE filtro) {
    	
    	StringBuilder sql = new StringBuilder();
    	
    	sql.append(" select cota.numero_cota as numCota, pessoa.razao_social as nomeCota ");
    	
    	sql.append(" from cota ");

    	sql.append(" inner join pessoa on (pessoa.id = cota.pessoa_id) ");
    	
    	sql.append(" where ");
    	
    	sql.append(" cota.numero_cota between :numeroCotaInicio and :numeroCotaFim ");
    	
    	sql.append(" and cota.id not in  ( ");
    	
    	sql.append(getSQLCotasOperacaoDiferenciadas());
    	
    	sql.append(" ) ");
		
    	Query query = this.getSession().createSQLQuery(sql.toString());
		
		query.setParameter("dataInicio", filtro.getDtRecolhimentoDe());
		query.setParameter("dataFim", filtro.getDtRecolhimentoAte());
		query.setParameter("numeroCotaInicio", filtro.getNumCotaDe());
		query.setParameter("numeroCotaFim", filtro.getNumCotaAte());
		
		((SQLQuery) query).addScalar("numCota", StandardBasicTypes.INTEGER);
		((SQLQuery) query).addScalar("nomeCota", StandardBasicTypes.STRING);
		
		((SQLQuery) query).setResultTransformer(new AliasToBeanResultTransformer(CotaEmissaoDTO.class));

		
		return query.list();

    }
	
	private StringBuilder getSQLCotasOperacaoDiferenciadas() {
    	
    	StringBuilder sql = new StringBuilder();
    	
    	sql.append(" select operaDiferenciada.idCota ");
    	
		sql.append(" from ");
    	
		sql.append(" ( ");

		sql.append(" select c.id as idCota ");
		sql.append(" from cota c ");
		sql.append(" inner join pdv on ( pdv.cota_id = c.id and pdv.ponto_principal = true ) ");
		sql.append(" inner join endereco_pdv on ( endereco_pdv.pdv_id = pdv.id and endereco_pdv.principal = true ) 	");
		sql.append(" inner join endereco on ( endereco.id = endereco_pdv.endereco_id )  		");
		sql.append(" inner join ( select gm.localidade as localidade ");
		sql.append(" from grupo_cota gc ");
		sql.append(" inner join grupo_municipio as gm on (gm.grupo_cota_id = gc.id)	");
		sql.append(" where ");
		sql.append(" gc.data_vigencia_inicio <= :dataInicio and ");
		sql.append(" ( gc.data_vigencia_fim is null or gc.data_vigencia_fim >= :dataFim ) ) as localidade_diferenciada ");
		sql.append(" on (localidade_diferenciada.localidade = endereco.cidade ) ");
		sql.append(" where ");
		sql.append(" c.numero_cota between :numeroCotaInicio and :numeroCotaFim ");
		sql.append(" group by c.id ");
		
		sql.append(" union ");
		
		sql.append(" select c.id as idCota ");
		sql.append(" from grupo_cota gc ");
		sql.append(" inner join cota_grupo as cg on (cg.grupo_cota_id = gc.id)		");
		sql.append(" inner join cota as c on (c.id = cg.cota_id) 	");
		sql.append(" where ");
		sql.append(" c.numero_cota between :numeroCotaInicio and :numeroCotaFim and ");
		sql.append(" gc.data_vigencia_inicio <= :dataInicio and ");
		sql.append(" ( gc.data_vigencia_fim is null or gc.data_vigencia_fim >= :dataFim ) ");
		sql.append(" group by c.id ");
		
		sql.append(" ) as operaDiferenciada ");
		
		sql.append(" group by operaDiferenciada.idCota ");
		
		return sql;
    }
	
	@SuppressWarnings("unchecked")
	@Override
	public List<DiaSemana> obterDiasRecolhimentoOperacaoDiferenciada(
			final Integer numeroCota, 
			final Date dataInicio, 
			final Date dataFim) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select diaRecolhimento	");
		
		hql.append(" from GrupoCota g ");

		hql.append(" inner join g.diasRecolhimento diaRecolhimento	");
	
		hql.append(" left join g.cotas cota ");
		
		hql.append(" left join g.municipios municipio ");
		
		hql.append(" where ");
		
		hql.append(" g.dataInicioVigencia <= :dataInicio and ");
		
		hql.append(" ( g.dataFimVigencia is null or g.dataFimVigencia >= :dataFim ) and ");
		
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
		
		query.setParameter("dataInicio", dataInicio);
		
		query.setParameter("dataFim", dataFim);
		
		query.setParameter("numeroCota", numeroCota);
		
		return (List<DiaSemana>) query.list();
		
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
	
	@Override
	@SuppressWarnings("unchecked")
	public List<GrupoCota> obterGruposAtivos(String sortname, String sortorder, boolean includeHistory) {
		
	    Criteria criteria = getSession().createCriteria(GrupoCota.class);
	    
	    if(!StringUtil.isEmpty(sortname) && !StringUtil.isEmpty(sortorder)) {
	        if("asc".equals(sortorder)) {
	            criteria.addOrder(Order.asc(sortname));
	        } else {
	            criteria.addOrder(Order.desc(sortname));
	        }
        }
	    
	    if(!includeHistory) {
            criteria.add(Restrictions.isNull("dataFimVigencia"));
        } else {
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
	    if (idGrupoIgnorar != null) {
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
