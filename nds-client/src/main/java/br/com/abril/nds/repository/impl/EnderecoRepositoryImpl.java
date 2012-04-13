package br.com.abril.nds.repository.impl;

import java.util.Collection;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.cadastro.Endereco;
import br.com.abril.nds.repository.EnderecoRepository;

@Repository
public class EnderecoRepositoryImpl extends AbstractRepository<Endereco, Long> implements
		EnderecoRepository {

	public EnderecoRepositoryImpl() {
		super(Endereco.class);
	}
	
	public void removerEnderecos(Collection<Long> idsEndereco) {
		
		StringBuilder hql = new StringBuilder("delete from Endereco e ");
		hql.append(" where e.id in (:ids) ")
		   .append(" and e.id not in (select tc.endereco.id from EnderecoCota tc where tc.endereco.id in (:ids)) ")
		   .append(" and e.id not in (select td.endereco.id from EnderecoFornecedor td where td.endereco.id in (:ids)) ")
		   .append(" and e.id not in (select tf.endereco.id from EnderecoFiador tf where tf.endereco.id in (:ids)) ");
		
		Query query = this.getSession().createQuery(hql.toString());
		query.setParameterList("ids", idsEndereco);
		
		query.executeUpdate();
	}
}
