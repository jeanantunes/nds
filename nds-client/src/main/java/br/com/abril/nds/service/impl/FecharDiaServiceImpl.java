package br.com.abril.nds.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.ValidacaoRecebimentoFisicoFecharDiaDTO;
import br.com.abril.nds.repository.FecharDiaRepository;
import br.com.abril.nds.service.FecharDiaService;
import br.com.abril.nds.util.DateUtil;

@Service
public class FecharDiaServiceImpl implements FecharDiaService {
	
	@Autowired
	private FecharDiaRepository fecharDiaRepository;

	@Override
	@Transactional
	public boolean existeCobrancaParaFecharDia(Date dataOperacaoDistribuidor) {
		Date diaDeOperaoMenosUm = DateUtil.subtrairDias(dataOperacaoDistribuidor, 1);
		return this.fecharDiaRepository.existeCobrancaParaFecharDia(diaDeOperaoMenosUm);
	}

	@Override
	@Transactional
	public boolean existeNotaFiscalSemRecebimentoFisico() {		 
		return this.fecharDiaRepository.existeNotaFiscalSemRecebimentoFisico();
	}

	@Override
	@Transactional
	public List<ValidacaoRecebimentoFisicoFecharDiaDTO> obterNotaFiscalComRecebimentoFisicoNaoConfirmado() {
		
		return this.fecharDiaRepository.obterNotaFiscalComRecebimentoFisicoNaoConfirmado();
	}

}
