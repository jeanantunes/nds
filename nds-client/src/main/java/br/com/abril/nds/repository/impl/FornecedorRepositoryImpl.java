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
		
		hql.append(" select fornecedores from Produto p ");
		hql.append(" join p.fornecedores fornecedores ");
		
		hql.append(" where fornecedores.situacaoCadastro = :situacaoCadastro ");
		
		if (codigoProduto != null && codigoProduto.length() > 0) {
			hql.append(" and p.codigo = :codigoProduto ");
		}
		
		hql.append(" group by fornecedores ");
		
		Query query = getSession().createQuery(hql.toString());
		
		query.setParameter("situacaoCadastro", SituacaoCadastro.ATIVO);
		
		if (codigoProduto != null && codigoProduto.length() > 0) {
			query.setParameter("codigoProduto", codigoProduto);
		}
		
		return query.list();
		
	}
	
	
}
