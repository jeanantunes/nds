package br.com.abril.nds.service;

import java.util.List;

import br.com.abril.nds.model.cadastro.Brinde;


/**
 * Interface que define serviços referentes a entidade
 * {@link br.com.abril.nds.model.cadastro.Brinde}
 * 
 * @author Discover Technology
 */
public interface BrindeService {

	List<Brinde> obterBrindes();
}
