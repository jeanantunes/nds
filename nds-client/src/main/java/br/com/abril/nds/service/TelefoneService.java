package br.com.abril.nds.service;

import java.util.List;

import br.com.abril.nds.model.cadastro.TelefoneCota;
import br.com.abril.nds.model.cadastro.TelefoneFornecedor;

public interface TelefoneService {
	
	List<TelefoneCota> buscarTelefonesCota(Long idCota);
	
	void salvarTelefonesCota(List<TelefoneCota> listaTelefonesCota);
	
	void removerTelefonesCota(List<TelefoneCota> listaTelefonesCota);
	
	List<TelefoneFornecedor> buscarTelefonesFornecedor(Long idFornecedor);
	
	void salvarTelefonesFornecedor(List<TelefoneFornecedor> listaTelefonesFornecedor);
	
	void removerTelefonesFornecedor(List<TelefoneFornecedor> listaTelefonesFornecedor);
}