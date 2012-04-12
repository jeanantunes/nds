package br.com.abril.nds.service;

import java.util.Collection;
import java.util.List;

import br.com.abril.nds.dto.TelefoneAssociacaoDTO;

public interface TelefoneService {
	
	void cadastrarTelefone(List<TelefoneAssociacaoDTO> listaTelefones);
	
	void cadastrarTelefone(TelefoneAssociacaoDTO associacaoTelefone);
	
	void removerTelefones(Collection<Long> listaTelefones);
}