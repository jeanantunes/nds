package br.com.abril.nds.repository.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.AliasToBeanConstructorResultTransformer;
import org.hibernate.transform.ResultTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.dto.EnderecoAssociacaoDTO;
import br.com.abril.nds.model.cadastro.Endereco;
import br.com.abril.nds.model.cadastro.EnderecoFornecedor;
import br.com.abril.nds.model.cadastro.TipoEndereco;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.EnderecoFornecedorRepository;

@Repository
public class EnderecoFornecedorRepositoryImpl extends AbstractRepositoryModel<EnderecoFornecedor, Long> 
											  implements EnderecoFornecedorRepository {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(EnderecoFiadorRepositoryImpl.class);

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

		ResultTransformer resultTransformer = null; 
        
        try {
            resultTransformer = new AliasToBeanConstructorResultTransformer(
                    EnderecoAssociacaoDTO.class.getConstructor(Long.class, Endereco.class, boolean.class, TipoEndereco.class));
        } catch (Exception e) {
            String message = "Erro criando result transformer para classe: "
                    + EnderecoAssociacaoDTO.class.getName();
            LOGGER.error(message, e);
            throw new RuntimeException(message, e);
        }
		
		query.setResultTransformer(resultTransformer);
		
		query.setParameter("idFornecedor", idFornecedor);
		
		return query.list();
	}

}
