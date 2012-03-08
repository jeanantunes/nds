package br.com.abril.nds.service;

import java.util.List;

import br.com.abril.nds.dto.RecebimentoFisicoDTO;
import br.com.abril.nds.model.estoque.ItemRecebimentoFisico;
import br.com.abril.nds.model.estoque.RecebimentoFisico;
import br.com.abril.nds.model.fiscal.ItemNotaFiscal;
import br.com.abril.nds.model.fiscal.NotaFiscal;
import br.com.abril.nds.model.seguranca.Usuario;

public interface RecebimentoFisicoService {
	
	 void adicionarRecebimentoFisico(RecebimentoFisico recebimentoFisico);
	 
	 List<RecebimentoFisicoDTO> obterListaItemRecebimentoFisico(Long idNotaFiscal);
	 
	 void alterarOrSalvarDiferencaRecebimentoFisico(List<RecebimentoFisicoDTO> listaRecebimentoFisicoDTO,
				ItemRecebimentoFisico itemRecebimentoFisico);
	 
	
	 
	 void alterarItemNotaRecebimentoFisico(RecebimentoFisicoDTO recebimentoFisicoDTO);
	 
	 void excluirItemNotaRecebimentoFisico(ItemNotaFiscal itemNota);
	 
	 void inserirDadosRecebimentoFisico(Usuario userLogado,NotaFiscal notaFiscal, List<RecebimentoFisicoDTO> listaItensNota);
	 
	 
}
