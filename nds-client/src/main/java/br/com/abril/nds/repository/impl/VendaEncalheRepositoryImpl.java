package br.com.abril.nds.repository.impl;

import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.estoque.VendaEncalhe;
import br.com.abril.nds.repository.VendaEncalheRepository;

/**
 * Classe de implementação de acesso a dados referentes a entidade
 * {@link br.com.abril.nds.model.estoque.VendaEncalhe}  
 * 
 * @author Discover Technology
 *
 */
@Repository
public class VendaEncalheRepositoryImpl extends AbstractRepository<VendaEncalhe, Long> implements VendaEncalheRepository {
	
	public VendaEncalheRepositoryImpl() {
		super(VendaEncalhe.class);
	}
}
