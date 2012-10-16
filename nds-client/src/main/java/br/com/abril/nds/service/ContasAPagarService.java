package br.com.abril.nds.service;

import java.util.List;

import br.com.abril.nds.dto.ContasAPagarConsignadoDTO;
import br.com.abril.nds.dto.ContasAPagarConsultaProdutoDTO;
import br.com.abril.nds.dto.ContasAPagarEncalheDTO;
import br.com.abril.nds.dto.ContasAPagarFaltasSobrasDTO;
import br.com.abril.nds.dto.ContasAPagarGridPrincipalFornecedorDTO;
import br.com.abril.nds.dto.ContasAPagarGridPrincipalProdutoDTO;
import br.com.abril.nds.dto.ContasAPagarParcialDTO;
import br.com.abril.nds.dto.FlexiGridDTO;
import br.com.abril.nds.dto.filtro.FiltroContasAPagarDTO;

public interface ContasAPagarService {
	
	/** Pesquisa do filtro de produtos */
	List<ContasAPagarConsultaProdutoDTO> pesquisarProdutos(FiltroContasAPagarDTO filtro);
	
	/** Pesquisa quando filtro selecionado por produtos */
	ContasAPagarGridPrincipalProdutoDTO pesquisarPorProduto(FiltroContasAPagarDTO filtro, String sortname, String sortorder, int rp, int page);

	/** Pesquisa da parcial de produtos (link na grid, coluna Tipo) */
	FlexiGridDTO<ContasAPagarParcialDTO> pesquisarParcial(FiltroContasAPagarDTO filtro, String sortname, String sortorder, int rp, int page);
	
	/** Pesquisa quando filtro selecionado por distribuidores */
	ContasAPagarGridPrincipalFornecedorDTO pesquisarPorDistribuidor(FiltroContasAPagarDTO filtro, String sortname, String sortorder, int rp, int page);
	
	/** Pesquisa de consignados (link na grid da pesquisa por fornecedores, coluna consignado)  */
	List<ContasAPagarConsignadoDTO> pesquisarDetalheConsignado(FiltroContasAPagarDTO filtro, String sortname, String sortorder, int rp, int page);
	
	/** Pesquisa de encalhe (link na grid da pesquisa por fornecedores, coluna encalhe)  */
	List<ContasAPagarEncalheDTO> pesquisarDetalheEncalhe(FiltroContasAPagarDTO filtro, String sortname, String sortorder, int rp, int page);

	/** Pesquisa de faltas e sobras (link na grid da pesquisa por fornecedores, coluna faltas e sobras)  */
	List<ContasAPagarFaltasSobrasDTO> pesquisarDetalheFaltasSobras(FiltroContasAPagarDTO filtro, String sortname, String sortorder, int rp, int page);
}

