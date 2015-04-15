package br.com.abril.nds.repository.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import br.com.abril.nds.model.cadastro.RegimeTributario;
import br.com.abril.nds.repository.AbstractRepositoryModel;
import br.com.abril.nds.repository.RegimeTributarioRepository;

/**
 * Repositorio responsavel por controlar os dados referentes a entidade
 * {@link br.com.abril.nds.model.fiscal.notafiscal.RegimeTributario}
 * 
 * @author Discover Technology
 *
 */
@Repository
public class RegimeTributarioRepositoryImpl extends AbstractRepositoryModel<RegimeTributario, Long> implements
	RegimeTributarioRepository {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(RegimeTributarioRepositoryImpl.class);
	
	/**
	 * Construtor.
	 */
	public RegimeTributarioRepositoryImpl() {

		super(RegimeTributario.class);
	}

}