package br.com.abril.nds.repository.impl;

import java.util.Date;
import java.util.List;

import org.apache.xmlbeans.impl.xb.xsdschema.RestrictionDocument.Restriction;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.planejamento.ChamadaEncalheCota;
import br.com.abril.nds.repository.ChamadaEncalheCotaRepository;

@Repository
public class ChamadaEncalheCotaRepositoryImpl extends AbstractRepositoryModel<ChamadaEncalheCota, Long> implements ChamadaEncalheCotaRepository {

	public ChamadaEncalheCotaRepositoryImpl() {
		super(ChamadaEncalheCota.class);
	}
	
	/*
	 * (non-Javadoc)
	 * @see br.com.abril.nds.repository.ChamadaEncalheCotaRepository#obterListaIdProdutoEdicaoChamaEncalheCota(java.lang.Integer, java.util.Date, boolean, boolean)
	 */
	@SuppressWarnings("unchecked")
	public List<Long> obterListaIdProdutoEdicaoChamaEncalheCota (
			Integer numeroCota, 
			Date dataOperacao, 
			boolean indPesquisaCEFutura, 
			boolean conferido) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select chamadaEncalheCota.chamadaEncalhe.produtoEdicao.id ");
		
		hql.append(" from ChamadaEncalheCota chamadaEncalheCota ");
		
		hql.append(" where ");
		
		hql.append(" chamadaEncalheCota.cota.numeroCota = :numeroCota ");
		
		hql.append(" and chamadaEncalheCota.fechado = :conferido ");
		
		if(indPesquisaCEFutura) {
			hql.append(" and chamadaEncalheCota.chamadaEncalhe.dataRecolhimento >= :dataOperacao ");
		} else {
			hql.append(" and chamadaEncalheCota.chamadaEncalhe.dataRecolhimento = :dataOperacao ");
		}
		
		Query query = this.getSession().createQuery(hql.toString());
		
		query.setParameter("numeroCota", numeroCota);

		query.setParameter("conferido", conferido);
		
		query.setParameter("dataOperacao", dataOperacao);
		
		return  query.list();
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see br.com.abril.nds.repository.ChamadaEncalheCotaRepository#obterQtdListaChamaEncalheCota(java.lang.Integer, java.util.Date, java.lang.Long, boolean, boolean)
	 */
	public Long obterQtdListaChamaEncalheCota(
			Integer numeroCota, 
			Date dataOperacao, 
			Long idProdutoEdicao, 
			boolean indPesquisaCEFutura, 
			boolean conferido) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select count(chamadaEncalheCota.id) ");
		
		hql.append(" from ChamadaEncalheCota chamadaEncalheCota ");
		
		hql.append(" where ");
		
		hql.append(" chamadaEncalheCota.cota.numeroCota = :numeroCota ");
		
		hql.append(" and chamadaEncalheCota.fechado = :conferido ");
		
		if(indPesquisaCEFutura) {
			hql.append(" and chamadaEncalheCota.chamadaEncalhe.dataRecolhimento >= :dataOperacao ");
		} else {
			hql.append(" and chamadaEncalheCota.chamadaEncalhe.dataRecolhimento = :dataOperacao ");
		}
		
		if(idProdutoEdicao!=null) {
			hql.append(" and chamadaEncalheCota.chamadaEncalhe.produtoEdicao.id = :idProdutoEdicao ");
		}
		
		Query query = this.getSession().createQuery(hql.toString());
		
		query.setParameter("numeroCota", numeroCota);
		
		query.setParameter("conferido", conferido);
		
		query.setParameter("dataOperacao", dataOperacao);
		
		if(idProdutoEdicao!=null) {
			query.setParameter("idProdutoEdicao", idProdutoEdicao);
		}
		
		Long qtde = (Long) query.uniqueResult();
		
		return (qtde == null) ? 0 : qtde;
		
	}

	/*
	 * (non-Javadoc)
	 * @see br.com.abril.nds.repository.ChamadaEncalheCotaRepository#obterListaChamaEncalheCota(java.lang.Integer, java.util.Date, java.lang.Long, boolean, boolean)
	 */
	@SuppressWarnings("unchecked")
	public List<ChamadaEncalheCota> obterListaChamaEncalheCota(
			Integer numeroCota, 
			Date dataOperacao, 
			Long idProdutoEdicao, 
			boolean indPesquisaCEFutura, 
			boolean conferido) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select chamadaEncalheCota ");
		
		hql.append(" from ChamadaEncalheCota chamadaEncalheCota ");
		
		hql.append(" where ");
		
		hql.append(" chamadaEncalheCota.cota.numeroCota = :numeroCota ");
		
		hql.append(" and chamadaEncalheCota.fechado = :conferido ");
		
		if(indPesquisaCEFutura) {
			hql.append(" and chamadaEncalheCota.chamadaEncalhe.dataRecolhimento >= :dataOperacao ");
		} else {
			hql.append(" and chamadaEncalheCota.chamadaEncalhe.dataRecolhimento = :dataOperacao ");
		}
		
		if(idProdutoEdicao!=null) {
			hql.append(" and chamadaEncalheCota.chamadaEncalhe.produtoEdicao.id = :idProdutoEdicao ");
		}
		
		Query query = this.getSession().createQuery(hql.toString());
		
		query.setParameter("numeroCota", numeroCota);
		
		query.setParameter("conferido", conferido);
		
		query.setParameter("dataOperacao", dataOperacao);
		
		if(idProdutoEdicao!=null) {
			query.setParameter("idProdutoEdicao", idProdutoEdicao);
		}
		
		return query.list();
		
	}
	
	public ChamadaEncalheCota buscarPorChamadaEncalheECota(Long idChamadaEncalhe,Long idCota){
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select o from ChamadaEncalheCota o ")
			.append(" WHERE o.cota.id =:idCota ")
			.append(" AND o.chamadaEncalhe.id=:idChamadaEncalhe ");
		
		Query query  = getSession().createQuery(hql.toString());
		query.setParameter("idChamadaEncalhe", idChamadaEncalhe);
		query.setParameter("idCota", idCota);
		
		return (ChamadaEncalheCota)query.uniqueResult();
		
	}
}
