package br.com.abril.nds.service.impl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.FlexiGridDTO;
import br.com.abril.nds.dto.RelatorioDetalheGarantiaDTO;
import br.com.abril.nds.dto.RelatorioGarantiasDTO;
import br.com.abril.nds.dto.filtro.FiltroRelatorioGarantiasDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.repository.CotaGarantiaRepository;
import br.com.abril.nds.service.RelatorioGarantiasService;
import br.com.abril.nds.vo.ValidacaoVO;

@Service
public class RelatorioGarantiasServiceImpl implements RelatorioGarantiasService {
	
	@Autowired
	private CotaGarantiaRepository cotaGarantiaRepository;

	@Transactional
	@Override
	public FlexiGridDTO<RelatorioGarantiasDTO> gerarTodasGarantias(FiltroRelatorioGarantiasDTO filtro) {
		
		FlexiGridDTO<RelatorioGarantiasDTO> to = new FlexiGridDTO<RelatorioGarantiasDTO>();
		to.setGrid(this.cotaGarantiaRepository.obterGarantiasCadastradas(filtro));
		
		to.setTotalGrid(this.cotaGarantiaRepository.obterCountGarantiasCadastradas(filtro).intValue());

		return to;
	}

	@Transactional
	@Override
	public FlexiGridDTO<RelatorioDetalheGarantiaDTO> gerarPorTipoGarantia(FiltroRelatorioGarantiasDTO filtro) {
		
		this.validaFiltroDetalhe(filtro);
		
		FlexiGridDTO<RelatorioDetalheGarantiaDTO> to = new FlexiGridDTO<RelatorioDetalheGarantiaDTO>();
		to.setGrid(this.cotaGarantiaRepository.obterDetalheGarantiaCadastrada(filtro));
		
		to.setTotalGrid(this.cotaGarantiaRepository.obterCountDetalheGarantiaCadastrada(filtro).intValue());

		return to;
	}

	private void validaFiltroDetalhe(FiltroRelatorioGarantiasDTO filtro){
		
		if (filtro.getDataBaseCalculo()==null){
			
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, "A data base de cálculo deve ser informada."));
		}
		
		if (filtro.getTipoGarantia()==null){
		
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, "O tipo de garantia deve ser informado."));
		}

	}
}
