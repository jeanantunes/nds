package br.com.abril.nds.repository;

import java.util.Date;
import java.util.List;

import br.com.abril.nds.dto.ValidacaoConfirmacaoDeExpedicaoFecharDiaDTO;
import br.com.abril.nds.dto.ValidacaoControleDeAprovacaoFecharDiaDTO;
import br.com.abril.nds.dto.ValidacaoLancamentoFaltaESobraFecharDiaDTO;
import br.com.abril.nds.dto.ValidacaoRecebimentoFisicoFecharDiaDTO;
import br.com.abril.nds.model.aprovacao.StatusAprovacao;

public interface FecharDiaRepository {
	
	boolean existeCobrancaParaFecharDia(Date diaDeOperaoMenosUm);

	boolean existeNotaFiscalSemRecebimentoFisico(Date dataOperacaoDistribuidor);

	List<ValidacaoRecebimentoFisicoFecharDiaDTO> obterNotaFiscalComRecebimentoFisicoNaoConfirmado(Date dataOperacaoDistribuidor);

	Boolean existeConfirmacaoDeExpedicao(Date dataOperacao);

	List<ValidacaoConfirmacaoDeExpedicaoFecharDiaDTO> obterConfirmacaoDeExpedicao(Date dataOperacaoDistribuidor);

	List<ValidacaoLancamentoFaltaESobraFecharDiaDTO> existeLancamentoFaltasESobrasPendentes(Date dataOperacaoDistribuidor);

	List<ValidacaoControleDeAprovacaoFecharDiaDTO> obterPendenciasDeAprovacao(Date dataOperacao, StatusAprovacao pendente);

}
