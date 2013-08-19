package br.com.abril.nds.repository.impl;

import java.util.List;
import java.util.Set;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.EnderecoAssociacaoDTO;
import br.com.abril.nds.model.cadastro.EnderecoTransportador;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.EnderecoTransportadorRepository;

@Repository
public class EnderecoTransportadorRepositoryImpl extends AbstractRepositoryModel<EnderecoTransportador, Long> implements
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

	@Override
	public void excluirEnderecosPorIdTransportador(Long id) {
		
		Query query = this.getSession().createQuery("delete from EnderecoTransportador where transportador.id = :id");
		query.setParameter("id", id);
		
		query.executeUpdate();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<EnderecoAssociacaoDTO> buscarEnderecosTransportador(Long id, Set<Long> idsIgnorar) {
		
		StringBuilder hql = new StringBuilder("select new ");
		hql.append(EnderecoAssociacaoDTO.class.getCanonicalName())
		   .append(" (t.id, t.principal, t.endereco, t.tipoEndereco, enderecoPessoa) ")
		   .append(" from EnderecoTransportador t, Endereco enderecoPessoa ")
		   .append(" where t.transportador.pessoaJuridica.id = :id ")
		   .append("   and t.endereco.id = enderecoPessoa.id ");
		
		if (idsIgnorar != null && !idsIgnorar.isEmpty()){
			hql.append(" and t.endereco.id not in (:idsIgnorar) ");
		}
		
		hql.append(" order by t.tipoEndereco ");
		
		Query query = this.getSession().createQuery(hql.toString());
		query.setParameter("id", id);
		
		if (idsIgnorar != null && !idsIgnorar.isEmpty()){
			query.setParameterList("idsIgnorar", idsIgnorar);
		}
		
		return query.list();
	}

	@Override
	public boolean verificarEnderecoPrincipalTransportador(Long id,
			Set<Long> idsIgnorar) {
		
		StringBuilder hql = new StringBuilder("select t.id ");
		hql.append(" from EnderecoTransportador t ")
		   .append(" where t.transportador.id = :id and t.principal = :indTrue ");
		
		if (idsIgnorar != null && !idsIgnorar.isEmpty()){
			
			hql.append(" and t.endereco.id not in (:idsIgnorar) ");
		}
		
		Query query = this.getSession().createQuery(hql.toString());
		
		if (idsIgnorar != null && !idsIgnorar.isEmpty()){
			
			query.setParameterList("idsIgnorar", idsIgnorar);
		}
		
		query.setParameter("id", id);
		query.setParameter("indTrue", true);
		query.setMaxResults(1);
		
		return query.uniqueResult() != null;
	}
}