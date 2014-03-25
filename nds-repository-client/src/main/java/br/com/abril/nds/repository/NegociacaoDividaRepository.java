package br.com.abril.nds.repository;

import java.math.BigDecimal;
import java.util.List;

import br.com.abril.nds.dto.ConsultaFollowupNegociacaoDTO;
import br.com.abril.nds.dto.NegociacaoDividaDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaNegociacaoDivida;
import br.com.abril.nds.dto.filtro.FiltroFollowupNegociacaoDTO;
import br.com.abril.nds.model.financeiro.Negociacao;

public interface NegociacaoDividaRepository extends Repository<Negociacao, Long> {

	List<NegociacaoDividaDTO> obterNegociacaoPorCota(FiltroConsultaNegociacaoDivida filtro);

	Long obterCotaPorNumeroCount(FiltroConsultaNegociacaoDivida filtro);

	Negociacao obterNegociacaoPorCobranca(Long id);
	
	Negociacao obterNegociacaoPorDivida(Long id);

	List<Negociacao> obterNegociacaoPorComissaoCota(Long idCota);
	
	List<ConsultaFollowupNegociacaoDTO>obterNegociacaoFollowup(FiltroFollowupNegociacaoDTO filtro);
	
	Long obterQuantidadeNegociacaoFollowup(FiltroFollowupNegociacaoDTO filtro);
	
	Long obterIdCobrancaPor(Long idNegociacao);

	Negociacao obterNegociacaoPorMovFinanceiroId(Long movFinanId);

	BigDecimal obterValorPagoDividaNegociadaComissao(Long negociacaoId);
}