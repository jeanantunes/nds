package br.com.abril.nds.service;

import java.util.List;

import br.com.abril.nds.dto.TipoMovimentoDTO;
import br.com.abril.nds.dto.filtro.FiltroTipoMovimento;
import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque;
import br.com.abril.nds.model.estoque.TipoMovimentoEstoque;
import br.com.abril.nds.model.movimentacao.TipoMovimento;
import br.com.abril.nds.model.seguranca.Usuario;

/**
 * Interface que define serviços referentes a entidade
 * {@link br.com.abril.nds.model.movimentacao.TipoMovimento}
 * 
 * @author Discover Technology
 */
public interface TipoMovimentoService {

	/**
	 * Obtém os tipos de movimento.
	 * 
	 * @return {@link List<TipoMovimento>}
	 */
	List<TipoMovimento> obterTiposMovimento();
	
	/**
	 * Obtém lista de tipos de movimento filtrada
	 * 
	 * @param filtro
	 * @return
	 */
	List<TipoMovimentoDTO> obterTiposMovimento(FiltroTipoMovimento filtro);

	/**
	 * Count da busca "obterTiposMovimento"
	 * 
	 * @param filtro
	 * @return
	 */
	Integer countObterTiposMovimento(FiltroTipoMovimento filtro);

	/**
	 * Salva novo Tipo de Movimento
	 * 
	 * @param tipoMovimentoDTO
	 */
	void salvarTipoMovimento(TipoMovimentoDTO tipoMovimentoDTO);

	/**
	 * Atualiza um tipo de movimento existente
	 * 
	 * @param tipoMovimentoDTO
	 * @param usuario 
	 */
	void editarTipoMovimento(TipoMovimentoDTO tipoMovimentoDTO, Usuario usuario);

	/**
	 * Remove Tipo de Movimento do banco
	 * 
	 * @param codigo
	 * @param usuario 
	 */
	void excluirTipoMovimento(Long codigo, Usuario usuario);
	
	TipoMovimentoEstoque buscarTipoMovimentoEstoque(GrupoMovimentoEstoque grupoMovimentoEstoque);

	List<TipoMovimentoEstoque> buscarTiposMovimentoEstoque(List<GrupoMovimentoEstoque> gruposMovimentoEstoque);
}
