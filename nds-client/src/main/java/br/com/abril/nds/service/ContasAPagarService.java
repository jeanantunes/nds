package br.com.abril.nds.service;

import java.util.List;

import br.com.abril.nds.dto.ContasAPagarConsultaProdutoDTO;

public interface ContasAPagarService {
	List<ContasAPagarConsultaProdutoDTO> pesquisaProdutoContasAPagar(String codigoProduto, Long edicao);

}
