package br.com.abril.nds.repository;

import java.math.BigInteger;
import java.util.List;

import br.com.abril.nds.client.vo.ProdutoDistribuicaoVO;
import br.com.abril.nds.dto.filtro.FiltroDistribuicaoDTO;
import br.com.abril.nds.model.planejamento.Lancamento;

public interface DistribuicaoRepository extends Repository<Lancamento, Long> {

	/**
	 * @param filtro
	 * @return
	 */
	public List<ProdutoDistribuicaoVO> obterMatrizDistribuicao(FiltroDistribuicaoDTO filtro);
	
	/**
	 * @param idEstudo
	 * @return
	 */
	public ProdutoDistribuicaoVO obterProdutoDistribuicaoPorEstudo(BigInteger idEstudo);
	
	public ProdutoDistribuicaoVO obterMatrizDistribuicaoPorEstudo(BigInteger id);
	
}
