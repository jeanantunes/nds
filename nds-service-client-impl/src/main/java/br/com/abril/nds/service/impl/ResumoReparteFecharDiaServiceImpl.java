package br.com.abril.nds.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.ReparteFecharDiaDTO;
import br.com.abril.nds.dto.fechamentodiario.SumarizacaoReparteDTO;
import br.com.abril.nds.repository.FechamentoDiarioConsolidadoReparteRepository;
import br.com.abril.nds.repository.FechamentoDiarioLancamentoReparteRepository;
import br.com.abril.nds.repository.FechamentoDiarioRepository;
import br.com.abril.nds.repository.ResumoReparteFecharDiaRepository;
import br.com.abril.nds.service.FecharDiaService;
import br.com.abril.nds.service.ResumoReparteFecharDiaService;
import br.com.abril.nds.vo.PaginacaoVO;


@Service
public class ResumoReparteFecharDiaServiceImpl  implements ResumoReparteFecharDiaService {

	@Autowired
	private ResumoReparteFecharDiaRepository resumoFecharDiaRepository;
	
	@Autowired
	private FechamentoDiarioRepository fechamentoDiarioRepository;
	
	@Autowired
	private FecharDiaService fecharDiaService;
	
	@Autowired
	private FechamentoDiarioConsolidadoReparteRepository consolidadoReparteRepository;
	
	@Autowired
	private FechamentoDiarioLancamentoReparteRepository fechamentoDiarioLancamentoReparteRepository;
	
	@Override
	@Transactional
	public List<ReparteFecharDiaDTO> obterResumoReparte(Date dataOperacao, PaginacaoVO paginacao) {
		
		 List<ReparteFecharDiaDTO> reparteFecharDiaDTOs = null;
		
		if(fecharDiaService.isDiaComFechamentoRealizado(dataOperacao)) {
			
			reparteFecharDiaDTOs = fechamentoDiarioLancamentoReparteRepository.obterLancametosReparte(dataOperacao, paginacao);
			
		} else {
			
			Date dataReparteHistoico = fechamentoDiarioRepository.obterDataUltimoFechamento(dataOperacao);
			
			reparteFecharDiaDTOs = this.resumoFecharDiaRepository.obterResumoReparte(dataOperacao, paginacao, dataReparteHistoico);
		}
	
		return reparteFecharDiaDTOs;
	}

    @Override
    @Transactional(readOnly = true)
    public Long contarLancamentosExpedidos(Date data) {
        
    	Objects.requireNonNull(data, "Data para contagem dos lançamentos expedidos não deve ser nula!");
        
    	if(fecharDiaService.isDiaComFechamentoRealizado(data)){
    		
    		return fechamentoDiarioLancamentoReparteRepository.countLancametosReparte(data);
    	}
    	else{
    		
    		return resumoFecharDiaRepository.contarLancamentosExpedidos(data);
    	}
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public SumarizacaoReparteDTO obterSumarizacaoReparte(Date data) {
        
    	Objects.requireNonNull(data, "Data para sumarização do reparte não deve ser nula!");
        
    	SumarizacaoReparteDTO reparteDTO = null;
    	
        if(fecharDiaService.isDiaComFechamentoRealizado(data)){
        	
        	reparteDTO = consolidadoReparteRepository.obterSumarizacaoReparte(data);
        } else {
        	
        	reparteDTO = resumoFecharDiaRepository.obterSumarizacaoReparte(data);
        }
        
        return reparteDTO;
    }

}
