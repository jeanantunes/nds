package br.com.abril.nds.service;

import java.util.List;

import br.com.abril.nds.dto.ContasAPagarConsultaProdutoDTO;
import br.com.abril.nds.dto.ContasAPagarParcialDTO;
import br.com.abril.nds.dto.ContasApagarConsultaPorProdutoDTO;
import br.com.abril.nds.dto.filtro.FiltroContasAPagarDTO;

public interface ContasAPagarService {
	
	/** Pesquisa do filtro de produtos */
	List<ContasAPagarConsultaProdutoDTO> pesquisarProdutos(FiltroContasAPagarDTO filtro);
	
	/** Pesquisa quando filtro selecionado por produtos */
	List<ContasApagarConsultaPorProdutoDTO> pesquisarPorProduto(FiltroContasAPagarDTO filtro);

	/** Pesquisa da parcial de produtos (link na grid, coluna Tipo) */
	List<ContasAPagarParcialDTO> pesquisarParcial(FiltroContasAPagarDTO filtro);
}

