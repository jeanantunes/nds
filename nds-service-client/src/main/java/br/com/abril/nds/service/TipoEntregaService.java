package br.com.abril.nds.service;

import java.util.List;

import br.com.abril.nds.model.cadastro.TipoEntrega;

/**
 * Interface de serviços para TipoEntrega
 * 
 * @author Discover Technology
 */
public interface TipoEntregaService {
	
	/**
	 * Busca por ID um tipo de Entrega.
	 * 
	 * @param id - PK
	 * @return
	 */
	TipoEntrega obterTipoEntrega(Long id);
	
	/**
	 * Lista todos os Tipos de Entrega.
	 * 
	 * @return List<TipoEntrega>
	 */
	List<TipoEntrega> obterTodos();
	
}
