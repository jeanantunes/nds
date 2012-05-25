package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.model.cadastro.TipoEntrega;

/**
 * Interface para o Repository de TipoEntrega
 * 
 * @author Discover Technology.
 */
public interface TipoEntregaRepository extends Repository<TipoEntrega, Long> {
	
	/**
	 * Pesquisa os tipos de entrega paginado.
	 * 
	 * @param codigo
	 * @param descricao
	 * @param periodicidade
	 * @param sortname
	 * @param sortorder
	 * @param page
	 * @param rp
	 * @return
	 */
	List<TipoEntrega> pesquisarTiposEntrega(Long codigo, String descricao, String periodicidade,
			String sortname, String sortorder, int page, int rp);
	
	/**
	 * Pesquisa a quantidade de tipos de entrega.
	 * 
	 * @param codigo
	 * @param descricao
	 * @param periodicidade
	 * @return
	 */
	Integer pesquisarQuantidadeTiposEntrega(Long codigo, String descricao, String periodicidade);
}
