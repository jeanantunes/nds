package br.com.abril.nds.service;

import java.util.List;

import br.com.abril.nds.dto.ProdutoDTO;
import br.com.abril.nds.dto.filtro.FiltroImpressaoNFEDTO;
import br.com.abril.nds.model.envio.nota.NotaEnvio;
import br.com.abril.nds.model.fiscal.nota.NotaFiscal;

public interface ImpressaoNFEService {

	/**
	 * Retorna uma lista de produtos que tiveram expedição confirmada (e constam na matriz de lançamento do dia)
	 * @return
	 */
	List<ProdutoDTO> obterProdutosExpedicaoConfirmada(FiltroImpressaoNFEDTO filtro);
	
	/**
	 * Obtem uma lista de NFe baseada no filtro informado
	 * 
	 * @param filtro
	 * @return
	 */
	List<br.com.abril.nds.dto.NotasCotasImpressaoNfeDTO> buscarCotasParaImpressaoNFe(FiltroImpressaoNFEDTO filtro);
	
	/**
	 * Obtem o total de NFe baseado no filtro informado
	 * 
	 * @param filtro
	 * @return
	 */
	Integer buscarNFeParaImpressaoTotalQtd(FiltroImpressaoNFEDTO filtro);
	
	/**
	 * Obtem uma lista de NF baseada no filtro informado
	 * @param filtro
	 * @return
	 */
	List<NotaFiscal> buscarNotasParaImpressaoNFe(FiltroImpressaoNFEDTO filtro);
	
	/**
	 * Obtem uma lista de NE baseada no filtro informado
	 * @param filtro
	 * @return
	 */
	List<NotaEnvio> buscarNotasEnvioParaImpressaoNFe(FiltroImpressaoNFEDTO filtro);
	
	
	List<br.com.abril.nds.dto.NotasCotasImpressaoNfeDTO> obterNotafiscalImpressao(FiltroImpressaoNFEDTO filtro);

	byte[] imprimirNFe(FiltroImpressaoNFEDTO filtro);
}
