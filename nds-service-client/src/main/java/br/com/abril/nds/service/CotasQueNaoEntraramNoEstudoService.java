package br.com.abril.nds.service;

import java.util.List;

import br.com.abril.nds.dto.CotaQueNaoEntrouNoEstudoDTO;
import br.com.abril.nds.dto.CotasQueNaoEntraramNoEstudoQueryDTO;

public interface CotasQueNaoEntraramNoEstudoService {

	List<CotaQueNaoEntrouNoEstudoDTO> buscaCotasQuerNaoEntraramNoEstudo(
			CotasQueNaoEntraramNoEstudoQueryDTO queryDTO);

}
