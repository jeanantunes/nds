package br.com.abril.nds.service;

import java.util.Date;
import java.util.List;

import br.com.abril.nds.dto.ReparteFecharDiaDTO;


public interface ResumoFecharDiaService {
	
	List<ReparteFecharDiaDTO> obterValorReparte(Date dataOperacaoDistribuidor, boolean soma);

	List<ReparteFecharDiaDTO> obterValorDiferenca(Date dataOperacao, boolean soma, String tipoDiferenca);

	List<ReparteFecharDiaDTO> obterValorTransferencia(Date dataOperacao, boolean soma);

	List<ReparteFecharDiaDTO> obterValorDistribuido(Date dataOperacao, boolean soma);

	List<ReparteFecharDiaDTO> obterResumoReparte(Date dataOperacao);

}
