package br.com.abril.nds.repository;

import br.com.abril.nds.model.cadastro.DescricaoTipoEntrega;
import br.com.abril.nds.model.cadastro.TipoEntrega;

/**
 * Interface para o Repository de TipoEntrega
 * 
 * @author Discover Technology.
 */
public interface TipoEntregaRepository extends Repository<TipoEntrega, Long> {
	
	/**
	 * Obtém um tipo de entrega de acordo com o parâmetro informado.
	 * 
	 * @param descricaoTipoEntrega - descrição do tipo de entrega
	 * 
	 * @return TipoEntrega
	 */
	TipoEntrega buscarPorDescricaoTipoEntrega(DescricaoTipoEntrega descricaoTipoEntrega);
}
