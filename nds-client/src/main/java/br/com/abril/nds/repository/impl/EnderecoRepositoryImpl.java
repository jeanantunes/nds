package br.com.abril.nds.repository.impl;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.cadastro.Endereco;
import br.com.abril.nds.model.dne.Localidade;
import br.com.abril.nds.repository.EnderecoRepository;

/**
 * Repositorio responsavel por controlar os dados referentes a entidade
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
		   .append(" and e.id not in (select efi.id from EnderecoFiador efi where id in (:ids)) ")
		   .append(" and e.id not in (select ef.id from EnderecoFornecedor ef where id in (:ids)) ")
		   .append(" and e.id not in (select et.id from EnderecoTransportador et where id in (:ids)) ")
		   .append(" and e.id not in (select ep.id from EnderecoPDV ep where id in (:ids)) ");
		
		Query query = this.getSession().createQuery(hql.toString());
		query.setParameterList("ids", idsEndereco);
		
		query.executeUpdate();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Endereco> buscarEnderecosPessoa(Long idPessoa, Set<Long> idsIgnorar) {
		StringBuilder hql = new StringBuilder("select endereco ");
		hql.append(" from Endereco endereco ")
		   .append(" where endereco.pessoa.id = :idPessoa ");
		
		if (idsIgnorar != null && !idsIgnorar.isEmpty()){
			hql.append(" and endereco.id not in (:idsIgnorar) ");
		}
		
		Query query = this.getSession().createQuery(hql.toString());
		query.setParameter("idPessoa", idPessoa);
		
		if (idsIgnorar != null && !idsIgnorar.isEmpty()){
			query.setParameterList("idsIgnorar", idsIgnorar);
		}
		
		return query.list();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<String> obterMunicipiosCotas() {
		StringBuilder hql = new StringBuilder("select endereco.cidade ");
		hql.append(" from EnderecoCota enderecoCota ")
		   .append(" join enderecoCota.endereco endereco ")
		   .append(" group by endereco.cidade"); 

		Query query = this.getSession().createQuery(hql.toString());

		return query.list();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<String> obterUnidadeFederacaoBrasil() {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select uf.sigla ")
		   .append(" from UnidadeFederacao uf ")
		   .append(" where uf.pais.sigla = :siglaBrasil ")
		   .append(" order by uf.sigla "); 

		Query query = this.getSession().createQuery(hql.toString());
		
		query.setParameter("siglaBrasil", "BR");
		
		return query.list();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<Localidade> obterLocalidadesPorUF(String siglaUF) {

		StringBuilder hql = new StringBuilder();
		
		hql.append(" from Localidade localidade ")
		   .append(" where localidade.uf.sigla = :siglaUF ")
		   .append(" order by localidade.nome "); 

		Query query = this.getSession().createQuery(hql.toString());
		
		query.setParameter("siglaUF", siglaUF);
		
		return query.list();
	}
	
	
}