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
public class FornecedorRepositoryImpl extends AbstractRepository<Fornecedor, Long> 
									  implements FornecedorRepository {

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
}
