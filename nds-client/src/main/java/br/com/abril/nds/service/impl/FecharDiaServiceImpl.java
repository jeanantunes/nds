package br.com.abril.nds.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.ValidacaoConfirmacaoDeExpedicaoFecharDiaDTO;
import br.com.abril.nds.dto.ValidacaoControleDeAprovacaoFecharDiaDTO;
import br.com.abril.nds.dto.ValidacaoLancamentoFaltaESobraFecharDiaDTO;
import br.com.abril.nds.dto.ValidacaoRecebimentoFisicoFecharDiaDTO;
import br.com.abril.nds.model.aprovacao.StatusAprovacao;
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
	public boolean existeNotaFiscalSemRecebimentoFisico(Date dataOperacaoDistribuidor) {		 
		return this.fecharDiaRepository.existeNotaFiscalSemRecebimentoFisico(dataOperacaoDistribuidor);
	}

	@Override
	@Transactional
	public List<ValidacaoRecebimentoFisicoFecharDiaDTO> obterNotaFiscalComRecebimentoFisicoNaoConfirmado(Date dataOperacaoDistribuidor) {
		
		return this.fecharDiaRepository.obterNotaFiscalComRecebimentoFisicoNaoConfirmado(dataOperacaoDistribuidor);
	}

	@Override
	@Transactional
	public Boolean existeConfirmacaoDeExpedicao(Date dataOperacao) {		 
		return this.fecharDiaRepository.existeConfirmacaoDeExpedicao(dataOperacao);
	}

	@Override
	@Transactional
	public List<ValidacaoConfirmacaoDeExpedicaoFecharDiaDTO> obterConfirmacaoDeExpedicao(Date dataOperacaoDistribuidor) {		 
		return this.fecharDiaRepository.obterConfirmacaoDeExpedicao(dataOperacaoDistribuidor);
	}

	@Override
	@Transactional
	public Boolean existeLancamentoFaltasESobrasPendentes(Date dataOperacaoDistribuidor) {		
		return this.fecharDiaRepository.existeLancamentoFaltasESobrasPendentes(dataOperacaoDistribuidor).isEmpty() ? true : false;
	}

	@Override
	@Transactional
	public List<ValidacaoLancamentoFaltaESobraFecharDiaDTO> obterLancamentoFaltasESobras(Date dataOperacaoDistribuidor) {
		return this.fecharDiaRepository.existeLancamentoFaltasESobrasPendentes(dataOperacaoDistribuidor);
	}

	@Override
	@Transactional
	public List<ValidacaoControleDeAprovacaoFecharDiaDTO> obterPendenciasDeAprovacao(Date dataOperacao, StatusAprovacao pendente) {
		 
		return this.fecharDiaRepository.obterPendenciasDeAprovacao(dataOperacao,pendente);
	}

}
