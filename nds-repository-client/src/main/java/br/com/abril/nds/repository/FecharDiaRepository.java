package br.com.abril.nds.repository;

import java.util.Date;
import java.util.List;

import br.com.abril.nds.dto.ValidacaoConfirmacaoDeExpedicaoFecharDiaDTO;
import br.com.abril.nds.dto.ValidacaoGeracaoCobrancaFecharDiaDTO;
import br.com.abril.nds.dto.ValidacaoLancamentoFaltaESobraFecharDiaDTO;
import br.com.abril.nds.dto.ValidacaoRecebimentoFisicoFecharDiaDTO;
import br.com.abril.nds.model.aprovacao.StatusAprovacao;
import br.com.abril.nds.model.cadastro.FormaCobranca;
import br.com.abril.nds.model.financeiro.GrupoMovimentoFinaceiro;
import br.com.abril.nds.model.movimentacao.Movimento;
import br.com.abril.nds.model.movimentacao.TipoMovimento;

public interface FecharDiaRepository {
	
	boolean existeCobrancaParaFecharDia(Date diaDeOperaoMenosUm);

	boolean existeNotaFiscalSemRecebimentoFisico(Date dataOperacaoDistribuidor);

	List<ValidacaoRecebimentoFisicoFecharDiaDTO> obterNotaFiscalComRecebimentoFisicoNaoConfirmado(Date dataOperacaoDistribuidor);

	Boolean existeConfirmacaoDeExpedicao(Date dataOperacao);

	List<ValidacaoConfirmacaoDeExpedicaoFecharDiaDTO> obterConfirmacaoDeExpedicao(Date dataOperacaoDistribuidor);

	List<ValidacaoLancamentoFaltaESobraFecharDiaDTO> existeLancamentoFaltasESobrasPendentes(Date dataOperacaoDistribuidor);

	boolean existePendenciasDeAprovacao(Date dataOperacao, StatusAprovacao pendente, List<TipoMovimento> tiposMovimentoVerificaAprovacao);
	
	boolean existeMatrizRecolhimentoSalva(Date dataOperacaoDistribuidor);

	List<ValidacaoGeracaoCobrancaFecharDiaDTO> obterFormasDeCobranca();

	List<ValidacaoGeracaoCobrancaFecharDiaDTO> obterDiasDaConcentracao(FormaCobranca fc);
	
	List<Movimento> obterMovimentosPorStatusData(List<GrupoMovimentoFinaceiro> gruposMovimentoFinanceiro,
			Date dataMovimento, StatusAprovacao statusAprovacao);
	
}
