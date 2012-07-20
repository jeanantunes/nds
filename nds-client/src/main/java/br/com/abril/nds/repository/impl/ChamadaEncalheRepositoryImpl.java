package br.com.abril.nds.repository.impl;

import java.util.Date;
import java.util.List;

import org.hibernate.FetchMode;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.planejamento.ChamadaEncalhe;
import br.com.abril.nds.model.planejamento.TipoChamadaEncalhe;
import br.com.abril.nds.repository.ChamadaEncalheRepository;

@Repository
public class ChamadaEncalheRepositoryImpl extends AbstractRepositoryModel<ChamadaEncalhe,Long> implements ChamadaEncalheRepository{

	public ChamadaEncalheRepositoryImpl() {
		super(ChamadaEncalhe.class);
	}
	
	public ChamadaEncalhe obterPorNumeroEdicaoEDataRecolhimento(ProdutoEdicao produtoEdicao,
																Date dataRecolhimento,
																TipoChamadaEncalhe tipoChamadaEncalhe) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select chamadaEncalhe from ChamadaEncalhe chamadaEncalhe ")
			.append(" where chamadaEncalhe.dataRecolhimento = :dataRecolhimento ")
			.append(" and chamadaEncalhe.tipoChamadaEncalhe = :tipoChamadaEncalhe ")
			.append(" and chamadaEncalhe.produtoEdicao = :produtoEdicao ");
		
		Query query = this.getSession().createQuery(hql.toString());
		
		query.setParameter("tipoChamadaEncalhe", tipoChamadaEncalhe);
		query.setParameter("produtoEdicao", produtoEdicao);
		query.setParameter("dataRecolhimento", dataRecolhimento);
		
		query.setMaxResults(1);
		
		return (ChamadaEncalhe) query.uniqueResult();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<ChamadaEncalhe> obterChamadasEncalhePor(Date dataOperacao, Long idCota) {
		
		try {
			
			return super.getSession().createCriteria(ChamadaEncalhe.class, "chamadaEncalhe")
					.createAlias("chamadaEncalhe.chamadaEncalheCotas", "chamadaEncalheCotas")
					.setFetchMode("chamadaEncalheCotas", FetchMode.JOIN)
					.setFetchMode("chamadaEncalhe.produtoEdicao", FetchMode.JOIN)
					.setFetchMode("chamadaEncalheCotas.cota", FetchMode.JOIN)
					.add(Restrictions.eq("chamadaEncalhe.dataRecolhimento", dataOperacao))
					.add(Restrictions.eq("chamadaEncalheCotas.cota.id", idCota)).list();
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public ChamadaEncalhe obterPorNumeroEdicaoEMaiorDataRecolhimento(ProdutoEdicao produtoEdicao,
																	 TipoChamadaEncalhe tipoChamadaEncalhe) {

		StringBuilder hql = new StringBuilder();
		
		hql.append(" select chamadaEncalhe from ChamadaEncalhe chamadaEncalhe ")
		.append(" where chamadaEncalhe.dataRecolhimento = (select max(chm.dataRecolhimento) from ChamadaEncalhe chm ) ")
		.append(" and chamadaEncalhe.tipoChamadaEncalhe = :tipoChamadaEncalhe ")
		.append(" and chamadaEncalhe.produtoEdicao = :produtoEdicao ");
		
		Query query = this.getSession().createQuery(hql.toString());
		
		query.setParameter("tipoChamadaEncalhe", tipoChamadaEncalhe);
		query.setParameter("produtoEdicao", produtoEdicao);
		
		query.setMaxResults(1);
		
		return (ChamadaEncalhe) query.uniqueResult();
	}

}
