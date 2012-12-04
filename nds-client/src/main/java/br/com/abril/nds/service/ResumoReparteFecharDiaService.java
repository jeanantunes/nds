package br.com.abril.nds.service;

import java.util.Date;
import java.util.List;

import br.com.abril.nds.dto.ReparteFecharDiaDTO;
import br.com.abril.nds.dto.fechamentodiario.SumarizacaoReparteDTO;
import br.com.abril.nds.vo.PaginacaoVO;


public interface ResumoReparteFecharDiaService {
	
    SumarizacaoReparteDTO obterSumarizacaoReparte(Date data);
	
	List<ReparteFecharDiaDTO> obterResumoReparte(Date dataOperacao, PaginacaoVO paginacao);
	
	/**
     * Conta o total de registros de lançamentos expedidos na data
     * 
     * @param data
     *            data para recuperação da qtde de lançamentos expedidos
     * @return total de lançamentos expedidos na data
     */
    Long contarLancamentosExpedidos(Date data);
	
}
