package br.com.abril.nds.repository.impl;

import java.util.Collection;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.cadastro.Endereco;
import br.com.abril.nds.repository.EnderecoRepository;

/**
 * Repositório responsável por controlar os dados referentes a entidade
 * {@link br.com.abril.nds.model.cadastro.Endereco}
 * 
 * @author Discover Technology
 *
 */
@Repository
public class EnderecoRepositoryImpl extends AbstractRepository<Endereco, Long> implements
		EnderecoRepository {

	/**
	 * Construtor.
	 */
	public EnderecoRepositoryImpl() {

		super(Endereco.class);
	}
	
	/**
	 * @see br.com.abril.nds.repository.EnderecoRepository#removerEnderecos(Collection)
	 */
	public void removerEnderecos(Collection<Long> idsEndereco) {
		
		StringBuilder hql = new StringBuilder("delete from Endereco e ");
		
		hql.append(" where e.id in (:ids) ")
		   .append(" and e.id not in (select ec.id from EnderecoCota ec where id in (:ids)) ")
		   .append(" and e.id not in (select ee.id from EnderecoEntregador ee where id in (:ids)) ")
		   .append(" and e.id not in (select ef.id from EnderecoFornecedor ef where id in (:ids)) ");
		
		Query query = this.getSession().createQuery(hql.toString());
		query.setParameterList("ids", idsEndereco);
		
		query.executeUpdate();
	}
}
