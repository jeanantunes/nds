package br.com.abril.nds.service;

import java.util.List;

import br.com.abril.nds.dto.RecebimentoFisicoDTO;
import br.com.abril.nds.model.estoque.ItemRecebimentoFisico;
import br.com.abril.nds.model.estoque.RecebimentoFisico;

public interface RecebimentoFisicoService {
	
	List<RecebimentoFisicoDTO> obterListaItemRecebimentoFisico(Long idNotaFiscal);
	
	void adicionarRecebimentoFisico(RecebimentoFisico recebimentoFisico);
	List<RecebimentoFisicoDTO> obterItemNotaPorCnpjNota(String cnpj, String numeroNota, String serieNota );
	 
	void alterarOrSalvarDiferencaRecebimentoFisico(List<RecebimentoFisicoDTO> listaRecebimentoFisicoDTO,
				ItemRecebimentoFisico itemRecebimentoFisico);
}
