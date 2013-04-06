package br.com.abril.nds.service;

import java.util.List;

import br.com.abril.nds.dto.AnaliseParcialDTO;
import br.com.abril.nds.dto.filtro.AnaliseParcialQueryDTO;
import br.com.abril.nds.model.planejamento.EstudoCota;

public interface AnaliseParcialService {

	EstudoCota buscarPorId(Long id);

	List<AnaliseParcialDTO> buscaAnaliseParcialPorEstudo(
			AnaliseParcialQueryDTO queryDTO);

	void atualizaReparte(Long estudoId, Long numeroCota, Double reparte);

	void liberar(Long id);


	

}
