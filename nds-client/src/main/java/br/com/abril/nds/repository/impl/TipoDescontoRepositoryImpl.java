package br.com.abril.nds.repository.impl;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.cadastro.TipoDesconto;
import br.com.abril.nds.repository.TipoDescontoRepository;

@Repository
public class TipoDescontoRepositoryImpl extends AbstractRepositoryModel<TipoDesconto, Long> implements TipoDescontoRepository {
	
	public TipoDescontoRepositoryImpl() {
		super(TipoDesconto.class);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<TipoDesconto> obterTiposDescontoCota(Long idCota){
	
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select desconto from Cota cota JOIN cota.tiposDescontoCota desconto ")
		.append(" where cota.id = :idCota ");
		
		Query query = getSession().createQuery(hql.toString());
		query.setParameter("idCota",idCota);
		
		return query.list();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<TipoDesconto> obterTipoDescontoNaoReferenciadosComCota(Long idCota){
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select desconto from TipoDesconto desconto  ")
			.append(" where desconto.id not in ( ")
						.append(" select tipoDesconto.id from Cota cota JOIN cota.tiposDescontoCota tipoDesconto ")
						.append(" where cota.id = :idCota ) ");
		
		Query query = getSession().createQuery(hql.toString());
		query.setParameter("idCota",idCota);
		
		return query.list();
	}

}
