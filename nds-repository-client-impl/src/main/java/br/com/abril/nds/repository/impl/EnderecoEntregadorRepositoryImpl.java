package br.com.abril.nds.repository.impl;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.cadastro.EnderecoEntregador;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.EnderecoEntregadorRepository;

/**
 * 
 * 
 * @author Discover Technology
 *
 */
@Repository
public class EnderecoEntregadorRepositoryImpl extends AbstractRepositoryModel<EnderecoEntregador, Long> 
											  implements EnderecoEntregadorRepository {

	/**
	 * Construtor.
	 */
	public EnderecoEntregadorRepositoryImpl() {
		super(EnderecoEntregador.class);
	}

	/**
	 * @see br.com.abril.nds.repository.EnderecoEntregadorRepository#removerEnderecosEntregadorPorIdEntregador(java.lang.Long)
	 */
	@Override
	public void removerEnderecosEntregadorPorIdEntregador(Long idEntregador) {

		StringBuilder hql = new StringBuilder();

		hql.append(" delete from EnderecoEntregador enderecoEntregador ")
		   .append(" where enderecoEntregador.entregador.id = :idEntregador");

		Query query = getSession().createQuery(hql.toString());

		query.setParameter("idEntregador", idEntregador);

		query.executeUpdate();
	}
	
}
