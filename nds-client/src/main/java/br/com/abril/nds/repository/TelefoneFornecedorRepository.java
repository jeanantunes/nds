package br.com.abril.nds.repository;

import java.util.List;
import java.util.Set;

import br.com.abril.nds.dto.TelefoneAssociacaoDTO;
import br.com.abril.nds.model.cadastro.TelefoneFornecedor;

public interface TelefoneFornecedorRepository extends Repository<TelefoneFornecedor, Long> {

	List<TelefoneAssociacaoDTO> buscarTelefonesFornecedor(Long idFornecedor, Set<Long> idsIgnorar);
	
	void removerTelefonesFornecedor(List<Long> listaTelefonesFornecedor);
}