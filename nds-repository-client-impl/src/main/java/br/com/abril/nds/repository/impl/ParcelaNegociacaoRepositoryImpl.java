package br.com.abril.nds.repository.impl;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.financeiro.ParcelaNegociacao;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.ParcelaNegociacaoRepository;

@Repository
public class ParcelaNegociacaoRepositoryImpl extends AbstractRepositoryModel<ParcelaNegociacao, Long> 
											 implements ParcelaNegociacaoRepository {

	public ParcelaNegociacaoRepositoryImpl() {
		super(ParcelaNegociacao.class);
	}

	@Override
	public int excluirPorNegociacao(Long idNegociacao) {

		StringBuilder hql = new StringBuilder();
		
		hql.append("delete from ParcelaNegociacao pn where pn.negociacao.id = :idNegociacao ");
	
		Query query = this.getSession().createQuery(hql.toString());
		
		query.setParameter("idNegociacao", idNegociacao);

		return query.executeUpdate();
	}
}