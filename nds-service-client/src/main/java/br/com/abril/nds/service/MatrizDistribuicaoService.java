package br.com.abril.nds.service;

import java.util.List;
import br.com.abril.nds.client.vo.ProdutoDistribuicaoVO;
import br.com.abril.nds.client.vo.TotalizadorProdutoDistribuicaoVO;
import br.com.abril.nds.dto.filtro.FiltroLancamentoDTO;

public interface MatrizDistribuicaoService {
	
	/**
	 *  Obtém a matriz de distribuicao 
	 * @param filtro
	 * @return
	 */
	public TotalizadorProdutoDistribuicaoVO obterMatrizDistribuicao(FiltroLancamentoDTO filtro);
	
	public void reabrirEstudos(List<ProdutoDistribuicaoVO> produtosDistribuicao);
	
	public void excluirEstudos(List<ProdutoDistribuicaoVO> produtosDistribuicao);
	
}
