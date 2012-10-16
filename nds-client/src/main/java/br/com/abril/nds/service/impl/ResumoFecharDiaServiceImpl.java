package br.com.abril.nds.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.ReparteFecharDiaDTO;
import br.com.abril.nds.repository.ResumoFecharDiaRepository;
import br.com.abril.nds.service.ResumoFecharDiaService;

@Service
public class ResumoFecharDiaServiceImpl  implements ResumoFecharDiaService {

	@Autowired
	private ResumoFecharDiaRepository resumoFecharDiaRepository;
	
	@Override
	@Transactional
	public List<ReparteFecharDiaDTO> obterValorReparte(Date dataOperacaoDistribuidor, boolean soma) {		
		return resumoFecharDiaRepository.obterValorReparte(dataOperacaoDistribuidor, soma);
	}

	@Override
	@Transactional
	public List<ReparteFecharDiaDTO> obterValorDiferenca(Date dataOperacao, boolean soma, String tipoDiferenca) {		
		return this.resumoFecharDiaRepository.obterValorDiferenca(dataOperacao, soma, tipoDiferenca);
	}

	@Override
	@Transactional
	public List<ReparteFecharDiaDTO> obterValorTransferencia(Date dataOperacao, boolean soma) {		 
		return this.resumoFecharDiaRepository.obterValorTransferencia(dataOperacao, soma);
	}

	@Override
	@Transactional
	public List<ReparteFecharDiaDTO> obterValorDistribuido(Date dataOperacao, boolean soma) {		 
		return this.resumoFecharDiaRepository.obterValorDistribuido(dataOperacao, soma);
	}

	@Override
	@Transactional
	public List<ReparteFecharDiaDTO> obterResumoReparte(Date dataOperacao) {
		return this.resumoFecharDiaRepository.obterResumoReparte(dataOperacao);
	}

}
