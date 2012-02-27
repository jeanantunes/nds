package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.dto.RecebimentoFisicoDTO;
import br.com.abril.nds.model.estoque.ItemRecebimentoFisico;
import br.com.abril.nds.model.estoque.RecebimentoFisico;

public interface RecebimentoFisicoRepository extends Repository<RecebimentoFisico, Long> {
	
	List<RecebimentoFisicoDTO>  obterItemNotaPorCnpjNota(String cnpj, String numeroNota, String serieNota );
	 void alterarOrSalvarDiferencaRecebimentoFisico(List<RecebimentoFisicoDTO> listaRecebimentoFisicoDTO,
				ItemRecebimentoFisico itemRecebimentoFisico);
}