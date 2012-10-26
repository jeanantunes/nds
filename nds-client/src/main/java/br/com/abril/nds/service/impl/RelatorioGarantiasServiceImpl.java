package br.com.abril.nds.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.FlexiGridDTO;
import br.com.abril.nds.dto.RelatorioDetalheGarantiaDTO;
import br.com.abril.nds.dto.RelatorioGarantiasDTO;
import br.com.abril.nds.dto.filtro.FiltroRelatorioGarantiasDTO;
import br.com.abril.nds.model.cadastro.TipoGarantia;
import br.com.abril.nds.repository.CotaGarantiaRepository;
import br.com.abril.nds.service.RelatorioGarantiasService;

@Service
public class RelatorioGarantiasServiceImpl implements RelatorioGarantiasService {
	
	@Autowired
	private CotaGarantiaRepository cotaGarantiaRepository;

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
		
		FlexiGridDTO<RelatorioDetalheGarantiaDTO> to = new FlexiGridDTO<RelatorioDetalheGarantiaDTO>();
		
		List<RelatorioDetalheGarantiaDTO> detalhe = this.cotaGarantiaRepository.obterDetalheGarantiaCadastrada(TipoGarantia.valueOf(filtro.getTipoGarantia()), Calendar.getInstance().getTime());
		
		for (RelatorioDetalheGarantiaDTO item : detalhe){
			to.setGrid(new ArrayList<RelatorioDetalheGarantiaDTO>());
			to.getGrid().add(item);
		}
		
		to.setTotalGrid(this.cotaGarantiaRepository.obterCountDetalheGarantiaCadastrada(TipoGarantia.valueOf(filtro.getTipoGarantia()), Calendar.getInstance().getTime()).intValue());

		return to;
		
		// TODO Auto-generated method stub
		//return null;
	}

}
