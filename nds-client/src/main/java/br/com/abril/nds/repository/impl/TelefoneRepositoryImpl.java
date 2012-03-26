package br.com.abril.nds.repository.impl;

import java.util.Collection;
import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.cadastro.Telefone;
import br.com.abril.nds.repository.TelefoneRepository;

@Repository
public class TelefoneRepositoryImpl extends AbstractRepository<Telefone, Long> implements TelefoneRepository {

	public TelefoneRepositoryImpl() {
		super(Telefone.class);
	}

	@Override
	public void removerTelefones(Collection<Long> idsTelefones) {
		StringBuilder hql = new StringBuilder("delete from Telefone t ");
		hql.append(" where t.id in (:ids) ")
		   .append(" and t.id not in (select id from TelefoneCota tc where id in (:ids)) ")
		   .append(" and t.id not in (select id from TelefoneFornecedor where id in (:ids)) ");
		
		Query query = this.getSession().createQuery(hql.toString());
		query.setParameterList("ids", idsTelefones);
		
		query.executeUpdate();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Telefone> buscarTelefonesPessoa(Long idPessoa) {
		StringBuilder hql = new StringBuilder("select p.telefones ");
		hql.append(" from Pessoa p ")
		   .append(" where p.id = :idPessoa");
		
		Query query = this.getSession().createQuery(hql.toString());
		query.setParameter("idPessoa", idPessoa);
		
		return query.list();
	}
}