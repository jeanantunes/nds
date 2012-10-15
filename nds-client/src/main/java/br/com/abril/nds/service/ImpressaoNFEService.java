package br.com.abril.nds.service;

import java.util.Date;
import java.util.List;

import br.com.abril.nds.dto.ProdutoLancamentoDTO;
import br.com.abril.nds.model.cadastro.Fornecedor;

public interface ImpressaoNFEService {

	/**
	 * Retorna uma lista de produtos que tiveram expedição confirmada (e constam na matriz de lançamento do dia)
	 * @param data TODO
	 * @return
	 */
	public List<ProdutoLancamentoDTO> obterProdutosExpedicaoConfirmada(List<Fornecedor> fornecedores, Date data);
	
}
