package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.dto.EnderecoAssociacaoDTO;
import br.com.abril.nds.model.cadastro.EnderecoFornecedor;

public interface EnderecoFornecedorRepository extends Repository<EnderecoFornecedor, Long> {


	/**
	 * Método que retorna todos os endereços referentes ao fornecedor em questão
	 * 
	 * @param idFornecedor
	 * 
	 * @return List<EnderecoAssociacaoDTO>
	 */
	List<EnderecoAssociacaoDTO> obterEnderecosFornecedor(Long idFornecedor);
}
