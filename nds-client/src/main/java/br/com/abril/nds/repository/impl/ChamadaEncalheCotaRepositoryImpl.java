package br.com.abril.nds.repository.impl;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.planejamento.ChamadaEncalheCota;
import br.com.abril.nds.repository.ChamadaEncalheCotaRepository;

@Repository
public class ChamadaEncalheCotaRepositoryImpl extends AbstractRepository<ChamadaEncalheCota, Long> implements ChamadaEncalheCotaRepository {

	public ChamadaEncalheCotaRepositoryImpl() {
		super(ChamadaEncalheCota.class);
	}
	
	
	public List<ChamadaEncalheCota> obterListaChamaEncalheCota(
			Integer numeroCota, 
			Date dataRecolhimento, 
			Long idProdutoEdicao, 
			boolean indPesquisaCEFutura, 
			boolean conferido) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select chamadaEncalheCota ");
		
		hql.append(" from ChamadaEncalheCota chamadaEncalheCota ");
		
		hql.append(" where ");
		
		hql.append(" chamadaEncalheCota.cota.numeroCota = :numeroCota ");
		
		hql.append(" and chamadaEncalheCota.conferido = :conferido ");
		
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

		query.setParameter("dataRecolhimento", dataRecolhimento);
		
		if(idProdutoEdicao!=null) {
			query.setParameter("idProdutoEdicao", idProdutoEdicao);
		}
		
		return  query.list();
		
	}
	
	public Long obterQtdListaChamaEncalheCota(
			Integer numeroCota, 
			Date dataRecolhimento, 
			Long idProdutoEdicao, 
			boolean indPesquisaCEFutura, 
			boolean conferido) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select count(chamadaEncalheCota.id) ");
		
		hql.append(" from ChamadaEncalheCota chamadaEncalheCota ");
		
		hql.append(" where ");
		
		hql.append(" chamadaEncalheCota.cota.numeroCota = :numeroCota ");
		
		hql.append(" and chamadaEncalheCota.conferido = :conferido ");
		
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

		//query.setParameter("dataRecolhimento", dataRecolhimento);
		
		query.setParameter("dataOperacao", dataRecolhimento);
		
		if(idProdutoEdicao!=null) {
			
			query.setParameter("idProdutoEdicao", idProdutoEdicao);
		}
		
		Long qtde = (Long) query.uniqueResult();
		
		return (qtde == null) ? 0 : qtde;
		
	}
	
}
