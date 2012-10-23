package br.com.abril.nds.service;

import br.com.abril.nds.dto.FlexiGridDTO;
import br.com.abril.nds.dto.RelatorioServicosEntregaDTO;
import br.com.abril.nds.dto.filtro.FiltroRelatorioServicosEntregaDTO;

public interface RelatorioServicosEntregaService {

	FlexiGridDTO<RelatorioServicosEntregaDTO> pesquisar(FiltroRelatorioServicosEntregaDTO filtro);
}
