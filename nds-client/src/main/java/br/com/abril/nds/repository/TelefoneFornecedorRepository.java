package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.model.cadastro.TelefoneFornecedor;

public interface TelefoneFornecedorRepository extends Repository<TelefoneFornecedor, Long> {

	List<TelefoneFornecedor> buscarTelefonesFornecedor(Long idFornecedor);
	
	void removerTelefonesFornecedor(List<Long> listaTelefonesFornecedor);
}