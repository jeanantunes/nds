package br.com.abril.nds.repository.impl;

import java.util.Collection;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.cadastro.TelefoneEntregador;
import br.com.abril.nds.repository.TelefoneEntregadorRepository;

/**
 * Repositório de dados referentes a entidade
 * {@link br.com.abril.nds.model.cadastro.TelefoneEntregador}
 * 
 * @author Discover Technology
 *
 */
@Repository
public class TelefoneEntregadorRepositoryImpl extends
		AbstractRepository<TelefoneEntregador, Long> implements
		TelefoneEntregadorRepository {

	/**
	 * Construtor.
	 */
	public TelefoneEntregadorRepositoryImpl() {
		super(TelefoneEntregador.class);
	}

	/**
	 * @see br.com.abril.nds.repository.TelefoneEntregadorRepository#removerTelefoneEntregadorPorIdEntregador(java.lang.Long)
	 */
	@Override
	public void removerTelefoneEntregadorPorIdEntregador(Long idEntregador) {

		StringBuilder hql = new StringBuilder();

		hql.append(" delete from TelefoneEntregador telefoneEntregador ")
		   .append(" where telefoneEntregador.entregador.id = :idEntregador");

		Query query = getSession().createQuery(hql.toString());

		query.setParameter("idEntregador", idEntregador);

		query.executeUpdate();
	}

	/**
	 * @see br.com.abril.nds.repository.TelefoneEntregadorRepository#removerTelefonesEntregador(java.util.Collection)
	 */
	@Override
	public void removerTelefonesEntregador(Collection<Long> listaTelefonesEntregador) {

		StringBuilder hql = new StringBuilder("delete from TelefoneEntregador ");

		hql.append(" where telefone.id in (:idsEntregador) ");

		Query query = this.getSession().createQuery(hql.toString());

		query.setParameterList("idsEntregador", listaTelefonesEntregador);

		query.executeUpdate();
	}
	
}
