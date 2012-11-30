package br.com.abril.nds.repository;

import java.util.Date;
import java.util.List;

import br.com.abril.nds.dto.ReparteFecharDiaDTO;
import br.com.abril.nds.dto.fechamentodiario.SumarizacaoReparteDTO;
import br.com.abril.nds.vo.PaginacaoVO;

/**
 * @deprecated Repositórios devem estar associados a uma entidade no modelo.
 * Considerar refatoração, extinguindo este repositório e enviando oa métodos para
 * reposítórios que estejam "associados" com as consultas efetuadas
 * 
 */
@Deprecated
public interface ResumoReparteFecharDiaRepository {

	SumarizacaoReparteDTO obterSumarizacaoReparte(Date data);

	List<ReparteFecharDiaDTO> obterResumoReparte(Date data);

    /**
     * Recupera as informações dos lançamentos expedidos na data
     * 
     * @param data
     *            data para recuperação das informações dos lançamentos
     *            expedidos
     * @param paginacao
     *            parâmetros para paginação dos resultados
     * @return lista com os lançamentos expedidos na data, paginados de acordo
     *         com os parâmetros recebidos
     */
	List<ReparteFecharDiaDTO> obterResumoReparte(Date data, PaginacaoVO paginacao);
	
    /**
     * Conta o total de registros de lançamentos expedidos na data
     * 
     * @param data
     *            data para recuperação da qtde de lançamentos expedidos
     * @return total de lançamentos expedidos na data
     */
	Long contarLancamentosExpedidos(Date data);

}
