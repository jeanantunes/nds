package br.com.abril.nds.repository.impl;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.financeiro.BaixaCobranca;
import br.com.abril.nds.repository.BaixaCobrancaRepository;


/**
 * Classe de implementação referente ao acesso a dados da entidade 
 * {@link br.com.abril.nds.model.financeiro.BaixaCobranca}
 * 
 * @author Discover Technology
 *
 */
@Repository
public class BaixaCobrancaRepositoryImpl extends AbstractRepository<BaixaCobranca,Long> implements BaixaCobrancaRepository {

	
	/**
	 * Construtor padrão
	 */
	public BaixaCobrancaRepositoryImpl() {
		super(BaixaCobranca.class);
	}

}
