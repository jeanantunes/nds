package br.com.abril.nds.repository.impl;

import java.util.Date;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.estoque.EstoqueProdutoCotaJuramentado;
import br.com.abril.nds.repository.EstoqueProdutoCotaJuramentadoRepository;

@Repository
public class EstoqueProdutoCotaJuramentadoRepositoryImpl extends AbstractRepositoryModel<EstoqueProdutoCotaJuramentado, Long> 
														 implements EstoqueProdutoCotaJuramentadoRepository {

	/**
	 * Construtor padrão.
	 */
	public EstoqueProdutoCotaJuramentadoRepositoryImpl() {
		
		super(EstoqueProdutoCotaJuramentado.class);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public EstoqueProdutoCotaJuramentado buscarEstoquePorProdutoECotaNaData(Long idProdutoEdicao, 
																	  		Long idCota,
																	  		Date data) {

		if (idProdutoEdicao == null || idCota == null || data == null) {
			
			throw new IllegalArgumentException("Informe os parâmetros corretamente!");
		}
		
		Criteria criteria = super.getSession().createCriteria(EstoqueProdutoCotaJuramentado.class);
		
		criteria.add(Restrictions.eq("produtoEdicao.id", idProdutoEdicao));
		criteria.add(Restrictions.eq("cota.id", idCota));
		criteria.add(Restrictions.eq("data", data));
				
		criteria.setMaxResults(1);
		
		return (EstoqueProdutoCotaJuramentado) criteria.uniqueResult();
	}
	
}
