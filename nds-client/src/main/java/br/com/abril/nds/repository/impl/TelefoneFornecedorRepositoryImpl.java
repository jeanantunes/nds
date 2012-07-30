package br.com.abril.nds.repository.impl;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.TelefoneAssociacaoDTO;
import br.com.abril.nds.model.cadastro.Telefone;
import br.com.abril.nds.model.cadastro.TelefoneFornecedor;
import br.com.abril.nds.repository.TelefoneFornecedorRepository;

@Repository
public class TelefoneFornecedorRepositoryImpl extends AbstractRepositoryModel<TelefoneFornecedor, Long>
											  implements TelefoneFornecedorRepository {

	public TelefoneFornecedorRepositoryImpl() {
		super(TelefoneFornecedor.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TelefoneAssociacaoDTO> buscarTelefonesFornecedor(Long idFornecedor, Set<Long> idsIgnorar) {
		StringBuilder hql = new StringBuilder("select new ");
		hql.append(TelefoneAssociacaoDTO.class.getCanonicalName())
		   .append(" (t.principal, t.telefone, t.tipoTelefone, telefonePessoa) ")
		   .append(" from TelefoneFornecedor t, Telefone telefonePessoa ")
		   .append(" where t.fornecedor.id = :idFornecedor ")
		   .append("   and t.telefone.id = telefonePessoa.id ");
		
		if (idsIgnorar != null && !idsIgnorar.isEmpty()){
			hql.append(" and t.telefone.id not in (:idsIgnorar) ");
		}
		
		hql.append(" order by t.tipoTelefone ");
		
		Query query = this.getSession().createQuery(hql.toString());
		query.setParameter("idFornecedor", idFornecedor);
		
		if (idsIgnorar != null && !idsIgnorar.isEmpty()){
			query.setParameterList("idsIgnorar", idsIgnorar);
		}
		
		return query.list();
	}

	@Override
	public void removerTelefonesFornecedor(Collection<Long> listaTelefonesFornecedor) {
		StringBuilder hql = new StringBuilder("delete from TelefoneFornecedor ");
		hql.append(" where telefone.id in (:idsFornecedor) ");
		
		Query query = this.getSession().createQuery(hql.toString());
		query.setParameterList("idsFornecedor", listaTelefonesFornecedor);
		
		query.executeUpdate();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Telefone> buscarTelefonesPessoaPorFornecedor(Long idFornecedor) {
		StringBuilder hql = new StringBuilder("select c.juridica.telefones ");
		hql.append(" from Fornecedor c ")
		   .append(" where c.id = :idFornecedor");
		
		Query query = this.getSession().createQuery(hql.toString());
		query.setParameter("idFornecedor", idFornecedor);
		
		return query.list();
	}
	
	@Override
	public TelefoneFornecedor obterTelefoneFornecedor(Long idTelefone, Long idFornecedor) {
		
		StringBuilder hql = new StringBuilder("select t from TelefoneFornecedor t ");
		hql.append(" where t.telefone.id = :idTelefone ")
		   .append(" and   t.fornecedor.id   = :idFornecedor");
		
		Query query = this.getSession().createQuery(hql.toString());
		query.setParameter("idTelefone", idTelefone);
		query.setParameter("idFornecedor", idFornecedor);
		
		query.setMaxResults(1);
		
		return (TelefoneFornecedor) query.uniqueResult();
	}
}