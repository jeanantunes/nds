package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.dto.TipoDescontoDTO;
import br.com.abril.nds.dto.filtro.FiltroTipoDescontoDTO;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.desconto.HistoricoDescontoProduto;

/**
 * Interface que define as regras de acesso a dados
 * para as pesquisas de desconto do Produto
 * 
 * @author Discover Technology
 */
public interface HistoricoDescontoProdutoRepository extends Repository<HistoricoDescontoProduto, Long> {

	/**
	 * Retorna os descontos do Produto
	 * @param filtro - filtro de cosnulta
	 * @return List<TipoDescontoDTO> 
	 */
	List<TipoDescontoDTO> buscarDescontos(FiltroTipoDescontoDTO filtro);
	
	/**
	 * Retorna a quantidade de descontos do Produto
	 * @param filtro - filtro de cosnulta
	 * @return Integer
	 */
	Integer buscarQuantidadeDescontos(FiltroTipoDescontoDTO filtro);
		
	/**
	 * Retorna o ultimo desconto valido do Produto
	 * 
	 * @return HistoricoDescontoProduto
	 */
	HistoricoDescontoProduto buscarUltimoDescontoValido(Produto produto);
	
}
