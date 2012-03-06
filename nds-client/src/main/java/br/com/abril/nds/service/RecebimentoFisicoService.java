package br.com.abril.nds.service;

import java.util.List;

import br.com.abril.nds.dto.RecebimentoFisicoDTO;
import br.com.abril.nds.model.estoque.ItemRecebimentoFisico;
import br.com.abril.nds.model.estoque.RecebimentoFisico;
import br.com.abril.nds.model.fiscal.ItemNotaFiscal;

public interface RecebimentoFisicoService {
	
	 void adicionarRecebimentoFisico(RecebimentoFisico recebimentoFisico);
	 
	 List<RecebimentoFisicoDTO> obterListaItemRecebimentoFisico(Long idNotaFiscal);
	 
	 void alterarOrSalvarDiferencaRecebimentoFisico(List<RecebimentoFisicoDTO> listaRecebimentoFisicoDTO,
				ItemRecebimentoFisico itemRecebimentoFisico);
	 
	 void inserirItemNotaRecebimentoFisico(RecebimentoFisicoDTO itemNotaDTO);
	 
	 void alterarItemNotaRecebimentoFisico(RecebimentoFisicoDTO itemNotaDTO);
	 
	 void excluirItemNotaRecebimentoFisico(ItemNotaFiscal itemNota);
	 
	 
}
