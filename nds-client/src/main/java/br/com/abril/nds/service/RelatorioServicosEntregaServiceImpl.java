package br.com.abril.nds.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.FlexiGridDTO;
import br.com.abril.nds.dto.RelatorioServicosEntregaDTO;
import br.com.abril.nds.dto.filtro.FiltroRelatorioServicosEntregaDTO;

@Service
public class RelatorioServicosEntregaServiceImpl implements RelatorioServicosEntregaService {

	@Transactional
	@Override
	public FlexiGridDTO<RelatorioServicosEntregaDTO> pesquisar(FiltroRelatorioServicosEntregaDTO filtro) {

		// Mock
		RelatorioServicosEntregaDTO dto = new RelatorioServicosEntregaDTO();
		dto.setDescricaoRota("rota");
		dto.setDescricaoRoteiro("roteiro");
		dto.setNomeJornaleiro("cota");
		dto.setNomeTransportador("transportador");
		dto.setNumeroCota(1);
		dto.setValor(BigDecimal.TEN);
		
		List<RelatorioServicosEntregaDTO> list = new ArrayList<RelatorioServicosEntregaDTO>();
		list.add(dto);
		list.add(dto);
		list.add(dto);
		list.add(dto);
		
		FlexiGridDTO<RelatorioServicosEntregaDTO> flexiDTO = new FlexiGridDTO<RelatorioServicosEntregaDTO>();
		flexiDTO.setGrid(list);
		flexiDTO.setTotalGrid(4);
		
		return flexiDTO;
	}

}
