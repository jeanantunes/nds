package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.dto.NegociacaoDividaDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaNegociacaoDivida;
import br.com.abril.nds.model.financeiro.Negociacao;

public interface NegociacaoDividaRepository extends Repository<Negociacao, Long> {

	List<NegociacaoDividaDTO> obterCotaPorNumero(FiltroConsultaNegociacaoDivida filtro);

	Long obterCotaPorNumeroCount(FiltroConsultaNegociacaoDivida filtro);

	Negociacao obterNegociacaoPorCobranca(Long id);
}