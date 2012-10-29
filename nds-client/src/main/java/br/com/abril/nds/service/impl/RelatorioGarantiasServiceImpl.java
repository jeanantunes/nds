package br.com.abril.nds.service.impl;

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
				
		//MOCK
		TipoGarantia tipoGarantia = TipoGarantia.CAUCAO_LIQUIDA/*filtro.getTipoGarantia()*/;//Alterar tipo no filtro para TipoGarantia
		//
		
		FlexiGridDTO<RelatorioDetalheGarantiaDTO> to = new FlexiGridDTO<RelatorioDetalheGarantiaDTO>();
		to.setGrid(this.cotaGarantiaRepository.obterDetalheGarantiaCadastrada(tipoGarantia, filtro.getDataBaseCalculo() , filtro.getPaginacao().getSortColumn(), filtro.getPaginacao().getSortOrder()));
		to.setTotalGrid(this.cotaGarantiaRepository.obterCountDetalheGarantiaCadastrada(tipoGarantia, filtro.getDataBaseCalculo()).intValue());

		return to;
	}

}
