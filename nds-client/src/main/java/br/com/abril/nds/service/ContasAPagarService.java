package br.com.abril.nds.service;

import java.util.List;

import br.com.abril.nds.dto.ContasAPagarConsultaProdutoDTO;
import br.com.abril.nds.dto.ContasApagarConsultaPorProdutoDTO;

public interface ContasAPagarService {
	List<ContasAPagarConsultaProdutoDTO> pesquisaProdutoContasAPagar(String codigoProduto, Long edicao);
	
	List<ContasApagarConsultaPorProdutoDTO> pesquisaContasAPagarPorProduto(List<Long> produtoEdicaoID);

}

