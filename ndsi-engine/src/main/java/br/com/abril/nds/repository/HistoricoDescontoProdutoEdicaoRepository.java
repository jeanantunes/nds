package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.dto.TipoDescontoDTO;
import br.com.abril.nds.dto.filtro.FiltroTipoDescontoDTO;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.desconto.HistoricoDescontoProdutoEdicao;

/**
 * Interface que define as regras de acesso a dados
 * para as pesquisas de desconto do ProdutoEdicao
 * 
 */
public interface HistoricoDescontoProdutoEdicaoRepository extends Repository<HistoricoDescontoProdutoEdicao, Long> {

	/**
	 * Retorna os descontos do ProdutoEdicao
	 * @param filtro - filtro de cosnulta
	 * @return List<DescontoDistribuidor> 
	 */
	List<TipoDescontoDTO> buscarDescontos(FiltroTipoDescontoDTO filtro);
	
	/**
	 * Retorna a quantidade de descontos do ProdutoEdicao
	 * @param filtro - filtro de cosnulta
	 * @return Integer
	 */
	Integer buscarQuantidadeDescontos(FiltroTipoDescontoDTO filtro);
		
	/**
	 * Retorna o ultimo desconto valido do ProdutoEdicao
	 * 
	 * @return HistoricoDescontoProdutoEdicao
	 */
	HistoricoDescontoProdutoEdicao buscarUltimoDescontoValido(ProdutoEdicao produtoEdicao);
	
}
