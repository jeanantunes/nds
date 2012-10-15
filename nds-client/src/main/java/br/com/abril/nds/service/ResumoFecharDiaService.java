package br.com.abril.nds.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import br.com.abril.nds.dto.ReparteFecharDiaDTO;


public interface ResumoFecharDiaService {
	
	List<ReparteFecharDiaDTO> obterValorReparte(Date dataOperacaoDistribuidor, boolean soma);

	List<ReparteFecharDiaDTO> obterValorDiferenca(Date dataOperacao, boolean soma, String tipoDiferenca);

	BigDecimal obterValorTransferencia(Date dataOperacao);

	BigDecimal obterValorDistribuido(Date dataOperacao);

	List<ReparteFecharDiaDTO> obterResumoReparte(Date dataOperacao);

}
