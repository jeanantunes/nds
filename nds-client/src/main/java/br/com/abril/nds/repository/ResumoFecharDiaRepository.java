package br.com.abril.nds.repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import br.com.abril.nds.dto.ReparteFecharDiaDTO;

public interface ResumoFecharDiaRepository {
	
	List<ReparteFecharDiaDTO> obterValorDiferenca(Date dataOperacao, boolean soma, String tipoDiferenca);

	BigDecimal obterValorTransferencia(Date dataOperacao);

	List<ReparteFecharDiaDTO> obterValorReparte(Date dataOperacaoDistribuidor, boolean soma);

	BigDecimal obterValorDistribuido(Date dataOperacao);

	List<ReparteFecharDiaDTO> obterResumoReparte(Date dataOperacao);

}
