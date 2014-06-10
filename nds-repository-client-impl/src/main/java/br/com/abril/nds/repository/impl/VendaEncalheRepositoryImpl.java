package br.com.abril.nds.repository.impl;

import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.estoque.VendaProduto;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.VendaEncalheRepository;

/**
 * Classe de implementação de acesso a dados referentes a entidade
 * {@link br.com.abril.nds.model.estoque.VendaProduto}  
 * 
 * @author Discover Technology
 *
 */
@Repository
public class VendaEncalheRepositoryImpl extends AbstractRepositoryModel<VendaProduto, Long> implements VendaEncalheRepository {
	
	public VendaEncalheRepositoryImpl() {
		super(VendaProduto.class);
	}
}
