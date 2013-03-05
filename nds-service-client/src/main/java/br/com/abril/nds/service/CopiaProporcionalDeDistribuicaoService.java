package br.com.abril.nds.service;

import java.math.BigInteger;

import br.com.abril.nds.client.vo.CopiaProporcionalDeDistribuicaoVO;
import br.com.abril.nds.client.vo.ProdutoDistribuicaoVO;

public interface CopiaProporcionalDeDistribuicaoService {
	
	/**
	 * Confirma a copia proporcional de estudo
	 * @param vo
	 * @return
	 */
	public Long confirmarCopiarProporcionalDeEstudo(CopiaProporcionalDeDistribuicaoVO vo);
	
	/**
	 * Obtem produtoDistribuicaoVO pelo id do estudo
	 * @param idEstudo
	 * @return
	 */
	public ProdutoDistribuicaoVO obterProdutoDistribuicaoPorEstudo(BigInteger idEstudo);
}
