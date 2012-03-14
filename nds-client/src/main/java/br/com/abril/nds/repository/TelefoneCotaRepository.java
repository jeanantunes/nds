package br.com.abril.nds.repository;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import br.com.abril.nds.dto.TelefoneAssociacaoDTO;
import br.com.abril.nds.model.cadastro.TelefoneCota;

public interface TelefoneCotaRepository extends Repository<TelefoneCota, Long> {

	
	List<TelefoneAssociacaoDTO> buscarTelefonesCota(Long idCota, Set<Long> idsIgnorar);
	
	void removerTelefonesCota(Collection<Long> listaTelefonesCota);
}
