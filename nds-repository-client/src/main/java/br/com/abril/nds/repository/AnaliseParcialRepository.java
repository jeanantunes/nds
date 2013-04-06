package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.dto.AnaliseParcialDTO;
import br.com.abril.nds.dto.filtro.AnaliseParcialQueryDTO;
import br.com.abril.nds.model.planejamento.EstudoCota;

public interface AnaliseParcialRepository {
	

	EstudoCota buscarPorId(Long id);

	List<AnaliseParcialDTO> buscaAnaliseParcialPorEstudo(
			AnaliseParcialQueryDTO queryDTO);

	void atualizaReparte(Long estudoId, Long numeroCota, Double reparte);

	void liberar(Long id);

}
