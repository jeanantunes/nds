package br.com.abril.nds.repository.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.RotaRoteiroDTO;
import br.com.abril.nds.model.cadastro.Rota;
import br.com.abril.nds.model.cadastro.TipoBox;
import br.com.abril.nds.model.cadastro.pdv.RotaPDV;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.RotaRepository;
import br.com.abril.nds.util.StringUtil;
import br.com.abril.nds.vo.PaginacaoVO.Ordenacao;

@Repository
public class RotaRepositoryImpl extends AbstractRepositoryModel<Rota, Long>
		implements RotaRepository {

	public RotaRepositoryImpl() {
		super(Rota.class);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<RotaRoteiroDTO> buscarRotasRoteiroAssociacao(String sortname, String sortorder){
		
		StringBuilder hql = new StringBuilder("select new ");
		hql.append(RotaRoteiroDTO.class.getCanonicalName())
		   .append("(r.id, r.descricaoRota, r.roteiro.descricaoRoteiro, a.id)")
		   .append(" from AssociacaoVeiculoMotoristaRota a right join a.rota r ")
		   .append(" where r.roteiro.id is not null ")
		   .append(" and (a.rota.id = r.id or a.rota.id is null) ");
		
		if ("descricaoRota".equals(sortname)){
			
			hql.append(" order by r.descricaoRota ").append("asc".equals(sortorder) ? "asc" : "desc" );
		} else {
			
			hql.append(" order by r.roteiro.descricaoRoteiro ").append("asc".equals(sortorder) ? "asc" : "desc" );
		}
		
		Query query = this.getSession().createQuery(hql.toString());
		
		return query.list();
	}
	

	@SuppressWarnings("unchecked")
	public List<Rota> buscarRota(String sortname, Ordenacao ordenacao){
		
		Criteria criteria =  getSession().createCriteria(Rota.class);
		if(Ordenacao.ASC ==  ordenacao){
			criteria.addOrder(Order.asc(sortname));
		}else if(Ordenacao.DESC ==  ordenacao){
			criteria.addOrder(Order.desc(sortname));
		}
		return criteria.setCacheable(true).list();
	}
	

	@SuppressWarnings("unchecked")
	public List<Rota> buscarRotaPorRoteiro(Long roteiroId, String sortname, Ordenacao ordenacao ){
		
		Criteria criteria =  getSession().createCriteria(Rota.class);
		criteria.add(Restrictions.eq("roteiro.id", roteiroId));
		if(Ordenacao.ASC ==  ordenacao){
			criteria.addOrder(Order.asc(sortname));
		}else if(Ordenacao.DESC ==  ordenacao){
			criteria.addOrder(Order.desc(sortname));
		}
		
		criteria.setCacheable(true);
		
		return  criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();
	}
	
	@SuppressWarnings("unchecked")
	public void atualizaOrdenacao(Rota rota ){
		Criteria criteria =  getSession().createCriteria(Rota.class);
		criteria.add(Restrictions.eq("roteiro", rota.getRoteiro()));
		criteria.add(Restrictions.ge("ordem", rota.getOrdem()));
		criteria.add(Restrictions.ne("id", rota.getId()));
		criteria.addOrder(Order.asc("ordem"));
		List<Rota> rotas = criteria.list();
		Integer ordem = rota.getOrdem();
		for(Rota entity : rotas  ){
			ordem++;
			entity.setOrdem(ordem);
			merge(entity);

		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Rota> buscarRotaPorNome(Long roteiroId, String rotaNome, MatchMode matchMode) {
		Criteria criteria = getSession().createCriteria(Rota.class);
		criteria.add(Restrictions.eq("roteiro.id", roteiroId));
		if (!StringUtil.isEmpty(rotaNome)) {
		    criteria.add(Restrictions.ilike("descricaoRota", rotaNome, matchMode));
		}
		return criteria.list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Rota> buscarRotaDeBox(Long idBox) {
		
		StringBuilder hql  = new StringBuilder();
			
		hql.append(" SELECT rota FROM Rota rota ");
		hql.append(" JOIN rota.roteiro roteiro  ");
		hql.append(" JOIN roteiro.roteirizacao.box box ");
		hql.append(" WHERE 1=1 ");
		
		if(idBox != null) {
			hql.append( "and box.id = :idBox ");		
		} else {
			hql.append(" and box.tipoBox = :especial ");
		}
		
		hql.append(" GROUP BY rota ");
	
		Query query = getSession().createQuery(hql.toString());
		
		if (idBox != null) {
			query.setParameter("idBox", idBox);
		} else {
			query.setParameter("especial", TipoBox.ESPECIAL);
		}
		
		return query.list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Rota> buscarRotaDeRoteiro(String descRoteiro) {
		
		String hql = " select rota from Rota rota where rota.roteiro.descricaoRoteiro=:descRoteiro ";
		
		Query query = getSession().createQuery(hql);
		query.setParameter("descRoteiro", descRoteiro);
		
		return query.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<Rota> obterRotasPorCota(Integer numeroCota) {
		
		Criteria criteria =  getSession().createCriteria(Rota.class, "rota");
		criteria.createAlias("rota.rotaPDVs","rotaPdv");
		criteria.createAlias("rotaPdv.pdv","pdv");
		criteria.createAlias("pdv.cota","cota");
		criteria.add(Restrictions.eq("cota.numeroCota", numeroCota));
		criteria.addOrder(Order.asc("rota.descricaoRota"));
		
		return criteria.list();
	}
	
	@Override
	public Long obterQtdRotasPorCota(Integer numeroCota) {
		
		StringBuilder hql = new StringBuilder("select distinct ");
		hql.append(" count(rota.id) ")
		   .append(" from Rota rota ")
		   .append(" join rota.rotaPDVs rotaPDV ")
		   .append(" join rotaPDV.pdv pdv ")
		   .append(" join pdv.cota cota ")
		   .append(" where cota.numeroCota = :numeroCota ");
		
		Query query = this.getSession().createQuery(hql.toString());
		query.setParameter("numeroCota", numeroCota);
		
		return (Long) query.uniqueResult();
	}
	
	
	@Override
	public Integer buscarMaiorOrdemRota(Long idRoteiro){
		Criteria criteria  = getSession().createCriteria(Rota.class);
		criteria.add(Restrictions.eq("roteiro.id", idRoteiro));
		criteria.setProjection(Projections.max("ordem"));  
		return (Integer) criteria.uniqueResult();  
	}

	@Override
	public Rota obterRotaPorPDV(Long idPDV, Long idCota) {
		
		StringBuilder hql = new StringBuilder(" select rot from Rota rot ");
		
		hql.append(" join rot.rotaPDVs rotaPdv ")
		
		   .append(" where rotaPdv.pdv.id = :idPDV ")
		   
		   .append(" and rotaPdv.pdv.caracteristicas.pontoPrincipal = :principal ")
		   
		   .append(" and rotaPdv.pdv.cota.id = :idCota ");
		
		Query query = this.getSession().createQuery(hql.toString());
		query.setParameter("idPDV", idPDV);
		query.setParameter("principal", true);
		query.setParameter("idCota", idCota);
		query.setMaxResults(1);
		
		return (Rota) query.uniqueResult();
	}

	/**
	 * @see br.com.abril.nds.repository.RotaRepository#obterRotasNaoAssociadasAoRoteiro(java.lang.Long)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Rota> obterRotasNaoAssociadasAoRoteiro(Long roteiroID) {
		
		Criteria criteria = this.getSession().createCriteria(Rota.class);
		
		criteria.add(Restrictions.ne("roteiro.id", roteiroID));
		
		return criteria.list();
	}

	/**
	 * Busca lista de RotaPDV por Rota
	 * @param rotaId
	 * @return List<RotaPDV>
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<RotaPDV> buscarRotaPDVPorRota(Long rotaId) {
		
        String hql = " select rotaPDV from RotaPDV rotaPDV where rotaPDV.rota.id = :rotaId ";
		
		Query query = getSession().createQuery(hql);
		
		query.setParameter("rotaId", rotaId);
		
		return query.list();
	}

	@Override
	public void removerPDV(Long pdvsExclusao, Long rotaId) {
		String hql = " DELETE FROM ROTA_PDV where pdv_id = :rotaPdvId and ROTA_ID = :rotaId ";
			
		Query query = getSession().createSQLQuery(hql.toString());
		
		query.setParameter("rotaPdvId", pdvsExclusao);
		query.setParameter("rotaId", rotaId);
		query.executeUpdate();
		
	}
	
}