package br.com.abril.nds.repository;

import java.math.BigDecimal;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.AliasToBeanResultTransformer;

import br.com.abril.nds.dto.ConsultaFollowupNegociacaoDTO;
import br.com.abril.nds.dto.ConsultaNegociacaoDividaDTO;
import br.com.abril.nds.dto.NegociacaoDividaDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaNegociacaoDivida;
import br.com.abril.nds.dto.filtro.FiltroConsultaNegociacoesDTO;
import br.com.abril.nds.dto.filtro.FiltroFollowupNegociacaoDTO;
import br.com.abril.nds.model.financeiro.GrupoMovimentoFinaceiro;
import br.com.abril.nds.model.financeiro.Negociacao;

public interface NegociacaoDividaRepository extends Repository<Negociacao, Long> {

	List<NegociacaoDividaDTO> obterNegociacaoPorCota(FiltroConsultaNegociacaoDivida filtro);

	Long obterCotaPorNumeroCount(FiltroConsultaNegociacaoDivida filtro);

	Negociacao obterNegociacaoPorCobranca(Long id);
	
	Negociacao obterNegociacaoPorDivida(Long id);

	Negociacao obterNegociacaoPorComissaoCota(Long idCota);
	
	List<ConsultaFollowupNegociacaoDTO>obterNegociacaoFollowup(FiltroFollowupNegociacaoDTO filtro);
	
	Long obterQuantidadeNegociacaoFollowup(FiltroFollowupNegociacaoDTO filtro);
	
	List<String> obterListaNossoNumeroPorNegociacao(Long idNegociacao);
	
	Long obterIdCobrancaPor(Long idNegociacao);

	Negociacao obterNegociacaoPorMovFinanceiroId(Long movFinanId);

	BigDecimal obterValorPagoDividaNegociadaComissao(Long negociacaoId);

    boolean verificarAtivacaoCotaAposPgtoParcela(Long idCobranca);

    /**
     * Atualiza o valor da valor da divida com a soma do valor da divda e o seu movimento financeiro.
     * @param idConsolidado
     * @param grupoMovimentoFinaceiros
     */
	public abstract void updateValorDividaValorMovimento(final Long idConsolidado,
			final List<GrupoMovimentoFinaceiro> grupoMovimentoFinaceiros);

	public abstract void removeNegociacaoMovimentoFinanceiroByIdConsolidadoAndGrupos(
			Long idConsolidado, List<GrupoMovimentoFinaceiro> grupoMovimentoFinaceiros);

	List<ConsultaNegociacaoDividaDTO> buscarNegociacaoDivida(FiltroConsultaNegociacoesDTO filtro);
}