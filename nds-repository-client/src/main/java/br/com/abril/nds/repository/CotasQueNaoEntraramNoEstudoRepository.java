package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.dto.CotaQueNaoEntrouNoEstudoDTO;
import br.com.abril.nds.dto.CotasQueNaoEntraramNoEstudoQueryDTO;

public interface CotasQueNaoEntraramNoEstudoRepository {

	List<CotaQueNaoEntrouNoEstudoDTO> buscaCotasQuerNaoEntraramNoEstudo(
			CotasQueNaoEntraramNoEstudoQueryDTO queryDTO);

}
