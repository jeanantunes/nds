package br.com.abril.nds.service;

import br.com.abril.nds.dto.InfoGeralExtratoEdicaoDTO;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;

public interface ExtratoEdicaoService {
	
	/**
	 * Obtem um dto com uma lista de ExtratoEdicao e o saldoTotal referente a esta.
	 * 
	 * @param numeroEdicao
	 * @param codigoProduto
	 * 
	 * @return InfoGeralExtratoEdicaoDTO
	 */
	public InfoGeralExtratoEdicaoDTO obterInfoGeralExtratoEdicao(String codigoProduto, Long numeroEdicao);

	/**
	 * Obtém produtoEdicao.
	 * 
	 * @param codigoProduto
	 * @param numeroEdicao
	 * @return ProdutoEdicao
	 */
	public ProdutoEdicao obterProdutoEdicao(String codigoProduto, Long numeroEdicao);
	
	/**
	 * Obtem a razão social do fornecedor de um produto.
	 * 
	 * @param codigoProduto
	 * @return String
	 */
	public String obterRazaoSocialFornecedorDeProduto(String codigoProduto);
	
}
