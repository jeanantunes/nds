package br.com.abril.nds.repository;

import java.math.BigDecimal;
import java.util.List;

import br.com.abril.nds.dto.ContasApagarConsultaPorDistribuidorDTO;
import br.com.abril.nds.dto.ContasApagarConsultaPorProdutoDTO;
import br.com.abril.nds.dto.filtro.FiltroContasAPagarDTO;

public interface ContasAPagarRepository {
	
	Integer pesquisarPorDistribuidorCount(FiltroContasAPagarDTO filtro);
	
	List<ContasApagarConsultaPorDistribuidorDTO> pesquisarPorDistribuidor(FiltroContasAPagarDTO filtro);
	
	/**
	 * Busca de Contas a Pagar por Produto
	 * @param filtro
	 * @return List<ContasApagarConsultaPorProdutoDTO>
	 */
	List<ContasApagarConsultaPorProdutoDTO> pesquisarPorProduto(FiltroContasAPagarDTO filtro);

	BigDecimal buscarTotalPesquisarPorDistribuidor(
			FiltroContasAPagarDTO filtro, boolean desconto);
	
	/**
	 * Busca quantidade de Contas a Pagar por Produto
	 * @param filtro
	 * @return Integer
	 */
	Integer pesquisarCountPorProduto(FiltroContasAPagarDTO filtro);
}