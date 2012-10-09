package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.dto.ContasAPagarConsultaProdutoDTO;

public interface ContasAPagarRepository {
	
	 List<ContasAPagarConsultaProdutoDTO> pesquisaProdutoContasAPagar(String codigoProduto, Long edicao);

}
