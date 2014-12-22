package br.com.abril.nds.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.EncalheFecharDiaDTO;
import br.com.abril.nds.dto.ResumoEncalheFecharDiaDTO;
import br.com.abril.nds.dto.VendaFechamentoDiaDTO;
import br.com.abril.nds.repository.FechamentoDiarioConsolidadoEncalheRepository;
import br.com.abril.nds.repository.FechamentoDiarioLancamentoEncalheRepository;
import br.com.abril.nds.repository.FechamentoDiarioMovimentoVendaEncalheRepository;
import br.com.abril.nds.repository.ResumoEncalheFecharDiaRepository;
import br.com.abril.nds.service.FecharDiaService;
import br.com.abril.nds.service.ResumoEncalheFecharDiaService;
import br.com.abril.nds.vo.PaginacaoVO;


@Service
public class ResumoEncalheFecharDiaServiceImpl implements ResumoEncalheFecharDiaService {
	
	@Autowired
	private ResumoEncalheFecharDiaRepository resumoEncalheFecharDiaRepository;
	
	@Autowired
	private FecharDiaService fecharDiaService;
	
	@Autowired
	private FechamentoDiarioConsolidadoEncalheRepository  consolidadoEncalheRepository;
	
	@Autowired
	private FechamentoDiarioLancamentoEncalheRepository lancamentoEncalheRepository;
	
	@Autowired
	private FechamentoDiarioMovimentoVendaEncalheRepository fechamentoDiarioMovimentoVendaEncalheRepository;

	@Override
	@Transactional(readOnly = true)
	public ResumoEncalheFecharDiaDTO obterResumoGeralEncalhe(Date dataOperacao) {
	    
		if (fecharDiaService.isDiaComFechamentoRealizado(dataOperacao)){
			
			return consolidadoEncalheRepository.obterResumoGeralEncalhe(dataOperacao);
		} else {
			
			return resumoEncalheFecharDiaRepository.obterResumoEncalhe(dataOperacao);
		}
	
	}

	@Override
	@Transactional(readOnly = true)
	public List<EncalheFecharDiaDTO> obterDadosGridEncalhe(Date data, PaginacaoVO paginacao) {
		
		if (fecharDiaService.isDiaComFechamentoRealizado(data)){
			
			return lancamentoEncalheRepository.obterDadosGridEncalhe(data, paginacao);
		}
		else{
			
			return this.resumoEncalheFecharDiaRepository.obterDadosGridEncalhe(data, paginacao);
		}
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<VendaFechamentoDiaDTO> obterDadosVendaEncalhe(Date dataOperacao, PaginacaoVO paginacao){		
		
		if (fecharDiaService.isDiaComFechamentoRealizado(dataOperacao)){
			
			return fechamentoDiarioMovimentoVendaEncalheRepository.obterDadosVendaEncalhe(dataOperacao, paginacao);
		}
		else{
		
			return this.resumoEncalheFecharDiaRepository.obterDadosVendaEncalhe(dataOperacao, paginacao);
		}
	}

    /**
     * {@inheritDoc}
     */
	@Override
    @Transactional(readOnly = true)
    public Long contarProdutoEdicaoEncalhe(Date data) {
        
		Objects.requireNonNull(data, "Data para contagem dos produtos conferidos no encalhe não deve ser nula!");
        
        if (fecharDiaService.isDiaComFechamentoRealizado(data)){
			
        	return lancamentoEncalheRepository.countDadosGridEncalhe(data);
		}
		else{
			
			return resumoEncalheFecharDiaRepository.contarProdutoEdicaoEncalhe(data);
		}
        
    }

	/**
	 * {@inheritDoc}
	 */
    @Override
    @Transactional(readOnly = true)
    public Long contarVendasEncalhe(Date data) {
        
    	Objects.requireNonNull(data, "Data para contagem das vendas de encalhe não deve ser nula!");
        
    	 if (fecharDiaService.isDiaComFechamentoRealizado(data)){
 			
 			return fechamentoDiarioMovimentoVendaEncalheRepository.countDadosVendaEncalhe(data);
 		}
 		else{
    	
 			return resumoEncalheFecharDiaRepository.contarVendasEncalhe(data);
 		}
    }

}
