package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.model.cadastro.TelefoneCota;

public interface TelefoneCotaRepository extends Repository<TelefoneCota, Long> {

	
	List<TelefoneCota> buscarTelefonesCota(Long idCota);
	
	void removerTelefonesCota(List<Long> listaTelefonesCota);
}
