package br.com.abril.nds.service;

import java.util.List;

import br.com.abril.nds.dto.FlexiGridDTO;
import br.com.abril.nds.dto.RelatorioServicosEntregaDTO;
import br.com.abril.nds.dto.RelatorioServicosEntregaDetalheDTO;
import br.com.abril.nds.dto.filtro.FiltroRelatorioServicosEntregaDTO;

public interface RelatorioServicosEntregaService {

	FlexiGridDTO<RelatorioServicosEntregaDTO> pesquisar(FiltroRelatorioServicosEntregaDTO filtro);
	
	List<RelatorioServicosEntregaDetalheDTO> pesquisarDetalhe(FiltroRelatorioServicosEntregaDTO filtro);
}
