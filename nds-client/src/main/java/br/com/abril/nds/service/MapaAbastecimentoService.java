package br.com.abril.nds.service;

import java.util.HashMap;
import java.util.List;

import br.com.abril.nds.dto.AbastecimentoDTO;
import br.com.abril.nds.dto.ProdutoAbastecimentoDTO;
import br.com.abril.nds.dto.ProdutoMapaDTO;
import br.com.abril.nds.dto.filtro.FiltroMapaAbastecimentoDTO;

public interface MapaAbastecimentoService {

	/**
	 * Obtem dados referentes ao Mapa de Abastecimento
	 * 
	 * @param filtro
	 * @return List - AbastecimentoDTO
	 */
	List<AbastecimentoDTO> obterDadosAbastecimento(FiltroMapaAbastecimentoDTO filtro);
	
	/**
	 * Obtem quantidade de registros retornados pelo filtro de "obterDadosAbastecimento"
	 * 
	 * @param filtro
	 * @return Long - Quantidade
	 */
	Long countObterDadosAbastecimento(FiltroMapaAbastecimentoDTO filtro);

	/**
	 * Obtém detalhes - Mapa de Abastecimento
	 * 
	 * @param idBox
	 * @param filtro
	 * @return
	 */
	List<ProdutoAbastecimentoDTO> obterDetlhesDadosAbastecimento(Long idBox,
			FiltroMapaAbastecimentoDTO filtro);
	
	HashMap<String, ProdutoMapaDTO> obterMapaDeImpressaoPorBox(
			FiltroMapaAbastecimentoDTO filtro);
	
}
