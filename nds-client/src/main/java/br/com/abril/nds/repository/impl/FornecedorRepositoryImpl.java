package br.com.abril.nds.repository.impl;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.repository.FornecedorRepository;

/**
 * Classe de implementação referente ao acesso a dados da entidade
 * {@link br.com.abril.nds.model.cadastro.Fornecedor}
 * 
 * @author william.machado
 * 
 */
@Repository
public class FornecedorRepositoryImpl extends
		AbstractRepository<Fornecedor, Long> implements FornecedorRepository {

	/**
	 * Construtor padrão.
	 */
	public FornecedorRepositoryImpl() {
		super(Fornecedor.class);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Fornecedor> obterFornecedoresAtivos() {

		String hql = "from Fornecedor fornecedor "
				+ " join fetch fornecedor.juridica "
				+ " where fornecedor.situacaoCadastro = :situacaoCadastro";

		Query query = super.getSession().createQuery(hql);

		query.setParameter("situacaoCadastro", SituacaoCadastro.ATIVO);

		return query.list();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Fornecedor> obterFornecedores() {

		String hql = "from Fornecedor fornecedor "
				+ " join fetch fornecedor.juridica ";

		Query query = super.getSession().createQuery(hql);
		return query.list();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Fornecedor> obterFornecedores(String cnpj) {
		String hql = "from Fornecedor fornecedor "
				+ " join fetch fornecedor.juridica "
				+ "where fornecedor.juridica.cnpj like :cnpj ";

		Query query = getSession().createQuery(hql);
		query.setParameter("cnpj", cnpj+"%");

		return query.list();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Fornecedor> obterFornecedores(boolean permiteBalanceamento,
			SituacaoCadastro... situacoes) {
		StringBuilder hql = new StringBuilder("from Fornecedor fornecedor ");
		hql.append("join fetch fornecedor.juridica juridica ");
		hql.append("where fornecedor.permiteBalanceamento = :permiteBalanceamento ");
		hql.append("and fornecedor.situacaoCadastro in (:situacoes) ");
		hql.append("order by juridica.nomeFantasia ");

		Query query = getSession().createQuery(hql.toString());
		query.setParameter("permiteBalanceamento", permiteBalanceamento);
		query.setParameterList("situacoes", situacoes);
		return query.list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Fornecedor> obterFornecedoresDeProduto(String codigoProduto) {
		
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select p.fornecedores from Produto p ");
		
		if (codigoProduto != null) {
			hql.append(" where ");
			hql.append(" p.codigo = :codigoProduto ");
		}
		
		Query query = getSession().createQuery(hql.toString());
		
		if (codigoProduto != null) {
			query.setParameter("codigoProduto", codigoProduto);
		}
		
		return query.list();
		
	}
	
	
}
