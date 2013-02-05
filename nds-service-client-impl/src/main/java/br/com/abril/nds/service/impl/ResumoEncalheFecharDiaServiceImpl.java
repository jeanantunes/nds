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
import br.com.abril.nds.repository.ResumoEncalheFecharDiaRepository;
import br.com.abril.nds.service.ResumoEncalheFecharDiaService;
import br.com.abril.nds.vo.PaginacaoVO;


@Service
public class ResumoEncalheFecharDiaServiceImpl implements ResumoEncalheFecharDiaService {
	
	@Autowired
	private ResumoEncalheFecharDiaRepository resumoEncalheFecharDiaRepository;

	@Override
	@Transactional
	public ResumoEncalheFecharDiaDTO obterResumoGeralEncalhe(Date dataOperacao) {
	    return resumoEncalheFecharDiaRepository.obterResumoEncalhe(dataOperacao);
	
	}

	@Override
	@Transactional
	public List<EncalheFecharDiaDTO> obterDadosGridEncalhe(Date data, PaginacaoVO paginacao) {
		return this.resumoEncalheFecharDiaRepository.obterDadosGridEncalhe(data, paginacao);
	}
	
	@Override
	@Transactional
	public List<VendaFechamentoDiaDTO> obterDadosVendaEncalhe(Date dataOperacao, PaginacaoVO paginacao){		
		return this.resumoEncalheFecharDiaRepository.obterDadosVendaEncalhe(dataOperacao, paginacao);
	}

    /**
     * {@inheritDoc}
     */
	@Override
    @Transactional(readOnly = true)
    public Long contarProdutoEdicaoEncalhe(Date data) {
        Objects.requireNonNull(data, "Data para contagem dos produtos conferidos no encalhe não deve ser nula!");
        return resumoEncalheFecharDiaRepository.contarProdutoEdicaoEncalhe(data);
    }

	/**
	 * {@inheritDoc}
	 */
    @Override
    @Transactional(readOnly = true)
    public Long contarVendasEncalhe(Date data) {
        Objects.requireNonNull(data, "Data para contagem das vendas de encalhe não deve ser nula!");
        return resumoEncalheFecharDiaRepository.contarVendasEncalhe(data);
    }

}
