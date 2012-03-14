package br.com.abril.nds.repository.impl;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.TelefoneAssociacaoDTO;
import br.com.abril.nds.model.cadastro.TelefoneFornecedor;
import br.com.abril.nds.repository.TelefoneFornecedorRepository;

@Repository
public class TelefoneFornecedorRepositoryImpl extends AbstractRepository<TelefoneFornecedor, Long>
											  implements TelefoneFornecedorRepository {

	public TelefoneFornecedorRepositoryImpl() {
		super(TelefoneFornecedor.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TelefoneAssociacaoDTO> buscarTelefonesFornecedor(Long idFornecedor, Set<Long> idsIgnorar) {
		StringBuilder hql = new StringBuilder("select new ");
		hql.append(TelefoneAssociacaoDTO.class.getCanonicalName())
		   .append(" (t.principal, t.telefone, t.tipoTelefone) ")
		   .append(" from TelefoneFornecedor t ")
		   .append(" where t.fornecedor.id = :idFornecedor ");
		
		if (idsIgnorar != null && !idsIgnorar.isEmpty()){
			hql.append(" and t.telefone.id not in (:idsIgnorar) ");
		}
		
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
		hql.append(" where id in (:idsFornecedor) ");
		
		Query query = this.getSession().createQuery(hql.toString());
		query.setParameterList("idsFornecedor", listaTelefonesFornecedor);
		
		query.executeUpdate();
	}
}