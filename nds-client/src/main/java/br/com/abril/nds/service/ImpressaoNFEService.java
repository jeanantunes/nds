package br.com.abril.nds.service;

import java.util.Date;
import java.util.List;

import br.com.abril.nds.dto.ProdutoLancamentoDTO;
import br.com.abril.nds.dto.filtro.FiltroImpressaoNFEDTO;
import br.com.abril.nds.model.cadastro.Fornecedor;

public interface ImpressaoNFEService {

	/**
	 * Retorna uma lista de produtos que tiveram expedição confirmada (e constam na matriz de lançamento do dia)
	 * @param data TODO
	 * @return
	 */
	List<ProdutoLancamentoDTO> obterProdutosExpedicaoConfirmada(List<Fornecedor> fornecedores, Date data);
	
	/**
	 * Obtem uma lista de NFe baseada no filtro informado
	 * 
	 * @param filtro
	 * @return
	 */
	List<br.com.abril.nds.dto.CotasImpressaoNfeDTO> buscarCotasParaImpressaoNFe(FiltroImpressaoNFEDTO filtro);
	
	/**
	 * Obtem o total de NFe baseado no filtro informado
	 * 
	 * @param filtro
	 * @return
	 */
	Integer buscarNFeParaImpressaoTotalQtd(FiltroImpressaoNFEDTO filtro);
	
	
}
