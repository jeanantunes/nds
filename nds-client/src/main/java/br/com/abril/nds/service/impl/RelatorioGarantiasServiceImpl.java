package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.FlexiGridDTO;
import br.com.abril.nds.dto.RelatorioDetalheGarantiaDTO;
import br.com.abril.nds.dto.RelatorioGarantiasDTO;
import br.com.abril.nds.dto.filtro.FiltroRelatorioGarantiasDTO;
import br.com.abril.nds.service.RelatorioGarantiasService;

@Service
public class RelatorioGarantiasServiceImpl implements RelatorioGarantiasService {

	@Transactional
	@Override
	public FlexiGridDTO<RelatorioGarantiasDTO> gerarTodasGarantias(
			FiltroRelatorioGarantiasDTO filtro, String sortname, String sortorder, int rp, int page) {
		
		FlexiGridDTO<RelatorioGarantiasDTO> to = new FlexiGridDTO<RelatorioGarantiasDTO>();
		
		RelatorioGarantiasDTO dto = new RelatorioGarantiasDTO();
		
		dto.setQtdCotas(99);
		dto.setTipoGarantia("Imóvel");
		dto.setVlrTotal(BigDecimal.TEN);
		
		to.setGrid(new ArrayList<RelatorioGarantiasDTO>());
		to.getGrid().add(dto);
		to.getGrid().add(dto);
		to.getGrid().add(dto);
		to.getGrid().add(dto);
		to.getGrid().add(dto);
		to.getGrid().add(dto);
		to.getGrid().add(dto);
		to.getGrid().add(dto);
		to.getGrid().add(dto);
		to.getGrid().add(dto);
		
		to.setTotalGrid(30);
		
		return to;
		// TODO Auto-generated method stub
		//return null;
	}

	@Transactional
	@Override
	public FlexiGridDTO<RelatorioDetalheGarantiaDTO> gerarPorTipoGarantia(
			FiltroRelatorioGarantiasDTO filtro, String sortname, String sortorder, int rp, int page) {
		
		//MOCK
		FlexiGridDTO<RelatorioDetalheGarantiaDTO> to = new FlexiGridDTO<RelatorioDetalheGarantiaDTO>();
		
		RelatorioDetalheGarantiaDTO dto = new RelatorioDetalheGarantiaDTO();
		
		dto.setCota(999);
		dto.setFaturamento(BigDecimal.TEN);
		dto.setGarantia("Fiador");
		dto.setGarantiaFaturamento(BigDecimal.TEN);
		dto.setNome("José Maria ");
		dto.setVencto(new Date());
		dto.setVlrGarantia(BigDecimal.TEN);
		
		to.setGrid(new ArrayList<RelatorioDetalheGarantiaDTO>());
		to.getGrid().add(dto);
		to.getGrid().add(dto);
		to.getGrid().add(dto);
		to.getGrid().add(dto);
		to.getGrid().add(dto);
		to.getGrid().add(dto);
		to.getGrid().add(dto);
		to.getGrid().add(dto);
		to.getGrid().add(dto);
		
		to.setTotalGrid(30);
		
		return to;
		
		// TODO Auto-generated method stub
		//return null;
	}

}
