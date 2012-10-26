package br.com.abril.nds.service.impl;

import java.util.Calendar;

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
	public FlexiGridDTO<RelatorioGarantiasDTO> gerarTodasGarantias(FiltroRelatorioGarantiasDTO filtro) {
		
		// TODO Auto-generated method stub
		return null;
	}

	@Transactional
	@Override
	public FlexiGridDTO<RelatorioDetalheGarantiaDTO> gerarPorTipoGarantia(FiltroRelatorioGarantiasDTO filtro) {
		
		FlexiGridDTO<RelatorioDetalheGarantiaDTO> to = new FlexiGridDTO<RelatorioDetalheGarantiaDTO>();
		to.setGrid(this.cotaGarantiaRepository.obterDetalheGarantiaCadastrada(TipoGarantia.valueOf(filtro.getTipoGarantia()), Calendar.getInstance().getTime()));
		to.setTotalGrid(this.cotaGarantiaRepository.obterCountDetalheGarantiaCadastrada(TipoGarantia.valueOf(filtro.getTipoGarantia()), Calendar.getInstance().getTime()).intValue());

		return to;
	}

}
