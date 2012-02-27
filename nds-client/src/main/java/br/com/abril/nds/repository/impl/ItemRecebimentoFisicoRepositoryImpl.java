package br.com.abril.nds.repository.impl;

import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.estoque.ItemRecebimentoFisico;
import br.com.abril.nds.repository.ItemRecebimentoFisicoRepository;

/**
 * Classe de implementação referente ao acesso a dados da entidade
 * {@link br.com.abril.nds.model.cadastro.Fornecedor}
 * 
 * @author william.machado
 * 
 */
@Repository
public class ItemRecebimentoFisicoRepositoryImpl extends
		AbstractRepository<ItemRecebimentoFisico, Long> implements ItemRecebimentoFisicoRepository {

	/**
	 * Construtor padrão.
	 */
	public ItemRecebimentoFisicoRepositoryImpl() {
		super(ItemRecebimentoFisico.class);
	}

}
