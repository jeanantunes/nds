package br.com.abril.nds.repository.impl;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.cadastro.Endereco;
import br.com.abril.nds.model.dne.Bairro;
import br.com.abril.nds.model.dne.Localidade;
import br.com.abril.nds.model.dne.Logradouro;
import br.com.abril.nds.repository.EnderecoRepository;
import br.com.abril.nds.vo.EnderecoVO;

/**
 * Repositorio responsavel por controlar os dados referentes a entidade
 * {@link br.com.abril.nds.model.cadastro.Endereco}
 * 
 * @author Discover Technology
 *
 */
@Repository
public class EnderecoRepositoryImpl extends AbstractRepositoryModel<Endereco, Long> implements
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
		   .append(" and e.id not in (select ec.endereco.id from EnderecoCota ec where ec.endereco.id in (:ids)) ")
		   .append(" and e.id not in (select ee.endereco.id from EnderecoEntregador ee where ee.endereco.id in (:ids)) ")
		   .append(" and e.id not in (select efi.endereco.id from EnderecoFiador efi where efi.endereco.id in (:ids)) ")
		   .append(" and e.id not in (select ef.endereco.id from EnderecoFornecedor ef where ef.endereco.id in (:ids)) ")
		   .append(" and e.id not in (select et.endereco.id from EnderecoTransportador et where et.endereco.id in (:ids)) ")
		   .append(" and e.id not in (select ed.endereco.id from EnderecoDistribuidor ed where ed.endereco.id in (:ids)) ")
		   .append(" and e.id not in (select eed.endereco.id from EnderecoEditor eed where eed.endereco.id in (:ids)) ")
		   .append(" and e.id not in (select ep.endereco.id from EnderecoPDV ep where ep.endereco.id in (:ids)) ");
		
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
	/*
	@Override
	@SuppressWarnings("unchecked")
	public List<String> obterUnidadeFederacaoBrasil() {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select uf.sigla ")
		   .append(" from UnidadeFederacao uf ")
		   .append(" order by uf.sigla "); 

		Query query = this.getSession().createQuery(hql.toString());
		
		return query.list();
	}
*/
	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<String> obterLocalidadesPorUFNome(String nome, String siglaUF) {

		Criteria criteria = super.getSession().createCriteria(Localidade.class);

		criteria.createAlias("unidadeFederacao", "uf");

		criteria.add(Restrictions.and(Restrictions.eq("uf.sigla", siglaUF),
									  Restrictions.ilike("nome",
											  nome, MatchMode.ANYWHERE)));

		return criteria.list();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public EnderecoVO obterEnderecoPorCep(String cep) {

		StringBuilder hql = new StringBuilder();
		
		hql.append(" select ")
		   .append(" logradouro.localidade.unidadeFederacao.sigla as uf, ")
		   .append(" logradouro.localidade.codigoMunicipioIBGE as codigoCidadeIBGE, ")
		   .append(" logradouro.localidade.nome as localidade, ")
		   .append(" logradouro.tipoLogradouro as tipoLogradouro, ")
		   .append(" logradouro.nome as logradouro, ")
		   .append(" _bairro.id as codigoBairro, ")
		   .append(" _bairro.nome as bairro ")
		   .append(" from Logradouro logradouro, Bairro _bairro ")
		   .append(" where logradouro.cep = :cep ")
		   .append(" and logradouro.chaveBairroInicial = _bairro.id ");

		Query query = this.getSession().createQuery(hql.toString());
		
		query.setResultTransformer(new AliasToBeanResultTransformer(EnderecoVO.class));
		
		query.setParameter("cep", cep);
		
		return (EnderecoVO) query.uniqueResult();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<String> obterBairrosPorCodigoIBGENome(String nome, Long codigoIBGE) {

		Criteria criteria = super.getSession().createCriteria(Bairro.class);

		criteria.createAlias("localidade", "localidade");

		criteria.add(Restrictions.and(Restrictions.eq("localidade.codigoMunicipioIBGE", codigoIBGE),
									  Restrictions.ilike("nome",
											  nome, MatchMode.ANYWHERE)));

		return criteria.list();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<String> obterLogradourosPorCodigoBairroNome(Long codigoBairro, String nomeLogradouro) {

		Criteria criteria = super.getSession().createCriteria(Logradouro.class);

		criteria.add(Restrictions.and(Restrictions.eq("chaveBairroInicial", codigoBairro),
									  Restrictions.ilike("nome",
											  nomeLogradouro, MatchMode.ANYWHERE)));

		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> obterBairrosPorCidade(String cidade) {
		StringBuilder hql = new StringBuilder("select distinct(endereco.bairro) ");
		hql.append(" from EnderecoCota enderecoCota ")
		   .append(" join enderecoCota.endereco endereco ")
		   .append(" where endereco.cidade = :cidade")
		   .append(" and endereco.bairro is not null")
		   .append(" group by endereco.bairro");

		Query query = this.getSession().createQuery(hql.toString());

		query.setParameter("cidade", cidade);
		
		return query.list();
	}
	
	

	@SuppressWarnings("unchecked")
	@Override
	public List<String> obterListaLocalidadeCotas() {
		
		StringBuffer hql = new StringBuffer();
		
		hql.append(" select distinct(endereco.cidade) ");
		
		hql.append(" from Cota cota 				");
		
		hql.append(" inner join cota.enderecos as enderecoCota			");
		
		hql.append(" inner join enderecoCota.endereco as endereco		");
		
		hql.append(" where enderecoCota.principal = :indPrincipal  	");
						
		Query query = getSession().createQuery(hql.toString());
		
		query.setParameter("indPrincipal", true);
		
		return query.list();
		
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<String> pesquisarLocalidades(String nomeLocalidade) {
		
		Query query = 
				this.getSession().createQuery("select distinct(e.cidade) from Endereco e where e.cidade like :nome ");
		query.setParameter("nome", "%" + nomeLocalidade + "%");
		
		return query.list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<String> pesquisarLogradouros(String nomeLogradouro) {
		
		Query query = 
				this.getSession().createQuery("select distinct(e.logradouro) from Endereco e where e.logradouro like :nome ");
		query.setParameter("nome", "%" + nomeLogradouro + "%");
		
		return query.list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<String> pesquisarBairros(String nomeBairro) {

		Query query = this.getSession().createQuery(
				"select distinct(e.bairro) from Endereco e where e.bairro like :nome ");
		query.setParameter("nome", "%" + nomeBairro + "%");

		return query.list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<String> obterLocalidadesPorUF(String uf) {

		Query query = this.getSession().createQuery("select distinct(e.cidade) from Endereco e where e.uf = :uf");
		query.setParameter("uf", uf);

		return query.list();
	}
	
	
}