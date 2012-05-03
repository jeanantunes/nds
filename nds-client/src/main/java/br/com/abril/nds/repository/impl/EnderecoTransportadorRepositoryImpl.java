package br.com.abril.nds.repository.impl;

import java.util.Set;

import org.hibernate.Query;

import br.com.abril.nds.model.cadastro.EnderecoTransportador;
import br.com.abril.nds.repository.EnderecoTransportadorRepository;

public class EnderecoTransportadorRepositoryImpl extends AbstractRepository<EnderecoTransportador, Long> implements
		EnderecoTransportadorRepository {

	public EnderecoTransportadorRepositoryImpl() {
		super(EnderecoTransportador.class);
	}

	@Override
	public EnderecoTransportador buscarEnderecoPorEnderecoTransportador(
			Long idEndereco, Long idTransportador) {
		
		StringBuilder hql = new StringBuilder("select t from EnderecoTransportador t ");
		hql.append(" where t.endereco.id = :idEndereco ")
		   .append(" and   t.transportador.id   = :idTransportador");
		
		Query query = this.getSession().createQuery(hql.toString());
		query.setParameter("idEndereco", idEndereco);
		query.setParameter("idTransportador", idTransportador);
		query.setMaxResults(1);
		
		return (EnderecoTransportador) query.uniqueResult();
	}

	@Override
	public void removerEnderecosTransportador(Set<Long> listaEnderecosRemover) {
		
		Query query = this.getSession().createQuery(
				"delete from EnderecoTransportador e where e.endereco.id in (:listaEnderecosRemover) ");
		
		query.setParameterList("listaEnderecosRemover", listaEnderecosRemover);
		
		query.executeUpdate();
	}
}