package br.com.abril.nds.service;

import java.util.List;
import java.util.Map;

import br.com.abril.nds.dto.AbastecimentoBoxCotaDTO;
import br.com.abril.nds.dto.AbastecimentoDTO;
import br.com.abril.nds.dto.EntregadorDTO;
import br.com.abril.nds.dto.MapaCotaDTO;
import br.com.abril.nds.dto.MapaProdutoCotasDTO;
import br.com.abril.nds.dto.ProdutoAbastecimentoDTO;
import br.com.abril.nds.dto.ProdutoEdicaoMapaDTO;
import br.com.abril.nds.dto.ProdutoMapaDTO;
import br.com.abril.nds.dto.ProdutoMapaRotaDTO;
import br.com.abril.nds.dto.filtro.FiltroMapaAbastecimentoDTO;
import br.com.abril.nds.vo.ProdutoEdicaoVO;

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
	
	/**
	 * Obtém dados do Mapa de Abastecimento Por Box
	 * 
	 * @param filtro
	 * @return
	 */
	Map<String, ProdutoMapaDTO> obterMapaDeImpressaoPorBox(
			FiltroMapaAbastecimentoDTO filtro);
	
	/**
	 * Obtém dados do Mapa de Abastecimento por Box/Rota
	 * 
	 * @param filtro
	 * @return
	 */
	Map<String, Map<String, ProdutoMapaRotaDTO>> obterMapaDeImpressaoPorBoxRota(
			FiltroMapaAbastecimentoDTO filtro);

	/**
	 * Obtém dados do Mapa de Abastecimento por Produto Edição
	 * 
	 * @param filtro
	 * @return
	 */
	ProdutoEdicaoMapaDTO obterMapaDeImpressaoPorProdutoEdicao(
			FiltroMapaAbastecimentoDTO filtro);

	/**
	 * Obtém dados do Mapa de Abastecimento por Cota
	 * 
	 * @param filtro
	 * @return
	 */
	MapaCotaDTO obterMapaDeImpressaoPorCota(FiltroMapaAbastecimentoDTO filtro);

	/**
	 * Obtém dados do Mapa de Abastecimento por Produto com quebra por Cota
	 * 
	 * @param filtro
	 * @return
	 */
	List<MapaProdutoCotasDTO> obterMapaDeImpressaoPorProdutoQuebrandoPorCota(
			FiltroMapaAbastecimentoDTO filtro);
	
	/**
	 * Obtém dados do Mapa de Abastecimento por Box/Rota
	 * 
	 * @param filtro
	 * @return List<ProdutoAbastecimentoDTO> - coleção de {@link br.com.abril.nds.dto.ProdutoAbastecimentoDTO}
	 */
	List<ProdutoAbastecimentoDTO> obterMapaAbastecimentoPorBoxRota(FiltroMapaAbastecimentoDTO filtro);
	
	/**
	 * Obtem quantidade de registros retornados pelo filtro de "obterMapaAbastecimentoPorBoxRota"
	 * 
	 * @param filtro
	 * @return Long - Quantidade
	 */
	Long countObterMapaAbastecimentoPorBoxRota(FiltroMapaAbastecimentoDTO filtro);
	

	/**
	 * Obtém dados do Mapa de Abastecimento por Cota
	 * 
	 * @param filtro
	 * @return List<ProdutoAbastecimentoDTO> - coleção de {@link br.com.abril.nds.dto.ProdutoAbastecimentoDTO}
	 */
	List<ProdutoAbastecimentoDTO> obterMapaAbastecimentoPorCota(FiltroMapaAbastecimentoDTO filtro);
	
	/**
	 * Obtem quantidade de registros retornados pelo filtro de "obterMapaAbastecimentoPorCota"
	 * 
	 * @param filtro
	 * @return Long - Quantidade
	 */
	Long countObterMapaAbastecimentoPorCota(FiltroMapaAbastecimentoDTO filtro);

	/**
	 * Obtém dados do Mapa de Abastecimento por Produto Edição
	 * 
	 * @param filtro
	 * @return
	 */
	List<ProdutoAbastecimentoDTO> obterMapaAbastecimentoPorProdutoEdicao(FiltroMapaAbastecimentoDTO filtro);

	/**
	 * Obtem quantidade de registros retornados pelo filtro de "obterMapaAbastecimentoPorProdutoEdicao"
	 * 
	 * @param filtro
	 * @return Long - Quantidade
	 */
	Long countObterMapaAbastecimentoPorProdutoEdicao(FiltroMapaAbastecimentoDTO filtro);


	/**
	 * Obtém dados do Mapa de Abastecimento por com quebra por Cota
	 * 
	 * @param filtro
	 * @return
	 */
	public List<ProdutoAbastecimentoDTO> obterMapaDeAbastecimentoPorProdutoQuebrandoPorCota(
			FiltroMapaAbastecimentoDTO filtro);

	/**
	 * Obtem quantidade de registros retornados pelo filtro de "obterMapaDeImpressaoPorProdutoQuebrandoPorCota"
	 * 
	 * @param filtro
	 * @return
	 */
	Long countObterMapaDeAbastecimentoPorProdutoQuebrandoPorCota(FiltroMapaAbastecimentoDTO filtro);

	/**
	 * Obtém Dados para Grid de Mapa por Entregador
	 * 
	 * @param filtro
	 * @return
	 */
	List<ProdutoAbastecimentoDTO> obterMapaDeAbastecimentoPorEntregador(
			FiltroMapaAbastecimentoDTO filtro);

	/**
	 * Count  de pesquisa por entregador
	 * 
	 * @param filtro
	 * @return
	 */
	Long countObterMapaDeAbastecimentoPorEntregador(
			FiltroMapaAbastecimentoDTO filtro);
	
	/**
	 * Obtém Mapa de Entregador
	 * 
	 * @param filtro
	 * @return
	 */
	Map<EntregadorDTO, Map<Long, MapaProdutoCotasDTO>> obterMapaDeImpressaoPorEntregador(
			FiltroMapaAbastecimentoDTO filtro);
	
	Map<Integer, Map<String, Map<String, Map<ProdutoEdicaoVO, Map<String, Integer>>>>> obterMapaDeImpressaoPorBoxRotaQuebraCota(
            FiltroMapaAbastecimentoDTO filtro);

	Map<Integer, Map<ProdutoEdicaoVO, Map<String, Integer>>> obterMapaDeImpressaoPorBoxQuebraPorCota(
	        FiltroMapaAbastecimentoDTO filtro);
	
	List<ProdutoAbastecimentoDTO> obterDadosAbastecimentoBoxVersusCota(FiltroMapaAbastecimentoDTO filtro);
	
	// AbastecimentoBoxCotaDTO obterMapaDeImpressaoPorBoxVersusCotaQuebrandoPorCota(FiltroMapaAbastecimentoDTO filtro, final Map<String, Object> parameters);
	
	List<AbastecimentoBoxCotaDTO> obterMapaDeImpressaoPorBoxVersusCotaQuebrandoPorCota(FiltroMapaAbastecimentoDTO filtro, final Map<String, Object> parameters);	
}
