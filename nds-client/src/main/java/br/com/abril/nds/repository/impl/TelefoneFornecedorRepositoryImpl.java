package br.com.abril.nds.repository.impl;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

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
	public List<TelefoneFornecedor> buscarTelefonesFornecedor(Long idFornecedor) {
		StringBuilder hql = new StringBuilder("from TelefoneFornecedor ");
		hql.append(" where fornecedor.id = :idFornecedor ");
		
		Query query = this.getSession().createQuery(hql.toString());
		query.setParameter("idFornecedor", idFornecedor);
		
		return query.list();
	}

	@Override
	public void removerTelefonesFornecedor(List<Long> listaTelefonesFornecedor) {
		StringBuilder hql = new StringBuilder("delete from TelefoneFornecedor ");
		hql.append(" where id in (:idsFornecedor) ");
		
		Query query = this.getSession().createQuery(hql.toString());
		query.setParameterList("idsFornecedor", listaTelefonesFornecedor);
		
		query.executeUpdate();
	}
}