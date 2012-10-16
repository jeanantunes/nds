package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.dto.ContasAPagarConsultaProdutoDTO;
import br.com.abril.nds.dto.ContasApagarConsultaPorProdutoDTO;
import br.com.abril.nds.dto.filtro.FiltroContasAPagarDTO;

public interface ContasAPagarRepository {
	
	 List<ContasAPagarConsultaProdutoDTO> pesquisarProdutos(FiltroContasAPagarDTO filtro);
	 
	 List<ContasApagarConsultaPorProdutoDTO> pesquisarPorProduto(FiltroContasAPagarDTO dto);

}
