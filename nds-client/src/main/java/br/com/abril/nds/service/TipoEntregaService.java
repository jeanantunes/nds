package br.com.abril.nds.service;

import java.math.BigDecimal;
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

	/**
	 * Pesquisa Tipos de Entrega. (paginado).
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
	 * Pesquisa a quantidade de registros.
	 * 
	 * @param codigo
	 * @param descricao
	 * @param periodicidade
	 * @return
	 */
	Integer pesquisarQuantidadeTiposEntrega(Long codigo, String descricao, String periodicidade);
	
	/**
	 * Deleta um Tipo de Entrega.
	 * 
	 * @param id
	 */
	void removerTipoEntrega(Long id);
	
	/**
	 * Salva um tipo de entrega.
	 * 
	 * @param id
	 * @param descricao
	 * @param taxaFixa
	 * @param percentualFaturamento
	 * @param baseCalculo
	 * @param periodicidadeCadastro
	 * @param diaSemana
	 * @param diaMes
	 */
	void salvarTipoEntrega(Long id, String descricao, BigDecimal taxaFixa, Integer percentualFaturamento,
			String baseCalculo, String periodicidadeCadastro, Integer diaSemana, Integer diaMes);
		
}
