package br.com.abril.nds.repository.impl;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.planejamento.EstudoCota;
import br.com.abril.nds.repository.EstudoCotaRepository;

/**
 * Classe de implementação referente ao acesso a dados da entidade 
 * {@link br.com.abril.nds.model.planejamento.EstudoCota}.
 * 
 * @author Discover Technology
 *
 */
@Repository
public class EstudoCotaRepositoryImpl extends AbstractRepository<EstudoCota, Long> implements EstudoCotaRepository {
	
	/**
	 * Construtor.
	 */
	public EstudoCotaRepositoryImpl() {
		
		super(EstudoCota.class);
	}

	@Override
	public EstudoCota obterEstudoCota(Integer numeroCota, Date dataReferencia) {
		
		String hql = " from EstudoCota estudoCota"
				   + " where estudoCota.cota.numeroCota = :numeroCota"
				   + " and estudoCota.estudo.dataLancamento >= :dataReferencia";
		
		Query query = super.getSession().createQuery(hql);

		query.setParameter("numeroCota", numeroCota);
		
		query.setParameter("dataReferencia", dataReferencia);
		
		query.setMaxResults(1);
		
		return (EstudoCota) query.uniqueResult();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<EstudoCota> obterEstudoCotaPorDataProdutoEdicao(Date dataLancamento, Long idProdutoEdicao) {
			
		String hql = " from EstudoCota estudoCota "
				   + " where estudoCota.estudo.dataLancamento= :dataLancamento " 
				   + " and estudoCota.estudo.produtoEdicao.id=:";
		
		Query query = super.getSession().createQuery(hql);
		
		query.setParameter("dataLancamento", dataLancamento);
		
		query.setParameter("idProdutoEdicao", idProdutoEdicao);
		
		return query.list();
	}
	
	@Override
	public EstudoCota obterEstudoCota(Date dataLancamento, Long idProdutoEdicao, Long idCota) {
			
		String hql = " from EstudoCota estudoCota "
				   + " where estudoCota.estudo.dataLancamento= :dataLancamento " 
				   + " and estudoCota.estudo.produtoEdicao.id=: " 
				   + " and estudoCota.cota.id ";
		
		Query query = super.getSession().createQuery(hql);
		
		query.setParameter("dataLancamento", dataLancamento);
		
		query.setParameter("idProdutoEdicao", idProdutoEdicao);
		
		query.setParameter("idCota", idCota);

		query.setMaxResults(1);
		
		return (EstudoCota) query.uniqueResult();
	}

}
