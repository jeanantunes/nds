package br.com.abril.nds.service;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import br.com.abril.nds.dto.TelefoneAssociacaoDTO;
import br.com.abril.nds.model.cadastro.TelefoneCota;
import br.com.abril.nds.model.cadastro.TelefoneFornecedor;

public interface TelefoneService {
	
	List<TelefoneAssociacaoDTO> buscarTelefonesCota(Long idCota, Set<Long> idsIgnorar);
	
	void salvarTelefonesCota(List<TelefoneCota> listaTelefonesCota);
	
	void removerTelefonesCota(Collection<Long> listaTelefonesCota);
	
	List<TelefoneAssociacaoDTO> buscarTelefonesFornecedor(Long idFornecedor, Set<Long> idsIgnorar);
	
	void salvarTelefonesFornecedor(List<TelefoneFornecedor> listaTelefonesFornecedor);
	
	void removerTelefonesFornecedor(Collection<Long> listaTelefonesFornecedor);
}