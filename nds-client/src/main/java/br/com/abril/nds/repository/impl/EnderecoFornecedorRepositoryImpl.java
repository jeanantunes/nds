package br.com.abril.nds.repository.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.transform.ResultTransformer;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.EnderecoAssociacaoDTO;
import br.com.abril.nds.model.cadastro.EnderecoFornecedor;
import br.com.abril.nds.repository.EnderecoFornecedorRepository;

@Repository
public class EnderecoFornecedorRepositoryImpl extends AbstractRepository<EnderecoFornecedor, Long> 
											  implements EnderecoFornecedorRepository {

	public EnderecoFornecedorRepositoryImpl() {
		super(EnderecoFornecedor.class);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<EnderecoAssociacaoDTO> obterEnderecosFornecedor(Long idFornecedor) {

		StringBuilder hql = new StringBuilder();
		
		hql.append(" select enderecoFornecedor.id as id, enderecoFornecedor.endereco as endereco, ")
		   .append(" enderecoFornecedor.principal as enderecoPrincipal, ")
		   .append(" enderecoFornecedor.tipoEndereco as tipoEndereco ")
		   .append(" from EnderecoFornecedor enderecoFornecedor ")
		   .append(" where enderecoFornecedor.fornecedor.id = :idFornecedor ");

		Query query = getSession().createQuery(hql.toString());

		ResultTransformer resultTransformer = new AliasToBeanResultTransformer(EnderecoAssociacaoDTO.class);
		
		query.setResultTransformer(resultTransformer);
		
		query.setParameter("idFornecedor", idFornecedor);
		
		return query.list();
	}

}
