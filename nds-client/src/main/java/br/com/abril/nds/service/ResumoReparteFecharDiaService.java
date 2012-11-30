package br.com.abril.nds.service;

import java.util.Date;
import java.util.List;

import br.com.abril.nds.dto.ReparteFecharDiaDTO;
import br.com.abril.nds.dto.ResumoReparteFecharDiaDTO;
import br.com.abril.nds.vo.PaginacaoVO;


public interface ResumoReparteFecharDiaService {
	
	List<ReparteFecharDiaDTO> obterValorReparte(Date dataOperacaoDistribuidor, boolean soma);

	List<ReparteFecharDiaDTO> obterValorDiferenca(Date dataOperacao, boolean soma, String tipoDiferenca);

	List<ReparteFecharDiaDTO> obterValorTransferencia(Date dataOperacao, boolean soma);

	List<ReparteFecharDiaDTO> obterValorDistribuido(Date dataOperacao, boolean soma);

	List<ReparteFecharDiaDTO> obterResumoReparte(Date dataOperacao, PaginacaoVO paginacao);
	
	ResumoReparteFecharDiaDTO obterResumoGeralReparte(Date dataOperacao);
	
	/**
     * Conta o total de registros de lançamentos expedidos na data
     * 
     * @param data
     *            data para recuperação da qtde de lançamentos expedidos
     * @return total de lançamentos expedidos na data
     */
    Long contarLancamentosExpedidos(Date data);
	
}
