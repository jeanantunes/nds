package br.com.abril.nds.repository.impl;

import java.util.Date;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.planejamento.ChamadaEncalhe;
import br.com.abril.nds.model.planejamento.TipoChamadaEncalhe;
import br.com.abril.nds.repository.ChamadaEncalheRepository;

@Repository
public class ChamadaEncalheRepositoryImpl extends AbstractRepository<ChamadaEncalhe,Long> implements ChamadaEncalheRepository{

	public ChamadaEncalheRepositoryImpl() {
		super(ChamadaEncalhe.class);
	}
	
	public ChamadaEncalhe obterPorNumeroEdicaoEDataRecolhimento(ProdutoEdicao produtoEdicao,
																Date dataFinalRecolhimento,
																TipoChamadaEncalhe tipoChamadaEncalhe) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select chamadaEncalhe from ChamadaEncalhe chamadaEncalhe ")
			.append(" where chamadaEncalhe.finalRecolhimento =:dataFinalRecolhimento ")
			.append(" and chamadaEncalhe.tipoChamadaEncalhe =:tipoChamadaEncalhe ")
			.append(" and chamadaEncalhe.produtoEdicao =:produtoEdicao ");
		
		Query query = this.getSession().createQuery(hql.toString());
		
		query.setParameter("tipoChamadaEncalhe", tipoChamadaEncalhe);
		query.setParameter("produtoEdicao", produtoEdicao);
		query.setParameter("dataFinalRecolhimento", dataFinalRecolhimento);
		
		query.setMaxResults(1);
		
		return (ChamadaEncalhe) query.uniqueResult();
	}

}
