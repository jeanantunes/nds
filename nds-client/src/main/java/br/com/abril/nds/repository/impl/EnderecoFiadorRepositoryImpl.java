package br.com.abril.nds.repository.impl;

import java.util.List;
import java.util.Set;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.EnderecoAssociacaoDTO;
import br.com.abril.nds.model.cadastro.Endereco;
import br.com.abril.nds.model.cadastro.EnderecoFiador;
import br.com.abril.nds.repository.EnderecoFiadorRepository;

@Repository
public class EnderecoFiadorRepositoryImpl extends
		AbstractRepository<EnderecoFiador, Long> implements
		EnderecoFiadorRepository {

	public EnderecoFiadorRepositoryImpl() {
		super(EnderecoFiador.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<EnderecoAssociacaoDTO> buscaEnderecosFiador(Long idFiador, Set<Long> idsIgnorar) {
		
		StringBuilder hql = new StringBuilder("select new ");
		hql.append(EnderecoAssociacaoDTO.class.getCanonicalName())
		   .append(" (t.principal, t.endereco, t.tipoEndereco, enderecoPessoa) ")
		   .append(" from EnderecoFiador t, Endereco enderecoPessoa ")
		   .append(" where t.fiador.id = :idFiador ")
		   .append("   and t.endereco.id = enderecoPessoa.id ");
		
		if (idsIgnorar != null && !idsIgnorar.isEmpty()){
			hql.append(" and t.endereco.id not in (:idsIgnorar) ");
		}
		
		hql.append(" order by t.tipoEndereco ");
		
		Query query = this.getSession().createQuery(hql.toString());
		query.setParameter("idFiador", idFiador);
		
		if (idsIgnorar != null && !idsIgnorar.isEmpty()){
			query.setParameterList("idsIgnorar", idsIgnorar);
		}
		
		return query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Endereco> buscarEnderecosPessoaPorFiador(Long idFiador) {
		
		StringBuilder hql = new StringBuilder("select c.pessoa.enderecos ");
		hql.append(" from Fiador c ")
		   .append(" where c.id = :idFiador");
		
		Query query = this.getSession().createQuery(hql.toString());
		query.setParameter("idFiador", idFiador);
		
		return query.list();
	}
	
	@Override
	public EnderecoFiador buscarEnderecoPorEnderecoFiador(Long idEndereco, Long idFiador) {
		
		StringBuilder hql = new StringBuilder("select t from EnderecoFiador t ");
		hql.append(" where t.endereco.id = :idEndereco ")
		   .append(" and   t.fiador.id   = :idFiador");
		
		Query query = this.getSession().createQuery(hql.toString());
		query.setParameter("idEndereco", idEndereco);
		query.setParameter("idFiador", idFiador);
		query.setMaxResults(1);
		
		return (EnderecoFiador) query.uniqueResult();
	}
	
	@Override
	public void excluirEnderecosFiador(Long idFiador){
		
		Query query = this.getSession().createQuery("delete from EnderecoFiador where fiador.id = :idFiador ");
		query.setParameter("idFiador", idFiador);
		
		query.executeUpdate();
	}
}