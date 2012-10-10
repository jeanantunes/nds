package br.com.abril.nds.repository.impl;

import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.estoque.CEDevolucaoFornecedor;
import br.com.abril.nds.repository.CEDevolucaoFornecedorRepository;

/**
 * Implementação do repositório para acesso à entidade
 * {@link CEDevolucaoFornecedor}
 * 
 * @author Discover Technology
 */
@Repository
public class CEDevolucaoFornecedorRepositoryImpl extends
        AbstractRepositoryModel<CEDevolucaoFornecedor, Long> implements
        CEDevolucaoFornecedorRepository {

    public CEDevolucaoFornecedorRepositoryImpl() {
        super(CEDevolucaoFornecedor.class);
    }

}
