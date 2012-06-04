package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.model.integracao.LogExecucao;
import br.com.abril.nds.vo.PaginacaoVO.Ordenacao;

/**
 * Interface que define as regras de acesso a dados referentes a entidade
 * {@link br.com.abril.nds.model.integracao.LogExecucao }  
 * @author InfoA2
 */
public interface LogExecucaoRepository extends Repository<LogExecucao,Long> {

	/**
	 * Busca os LogExecucao respeitando as restricoes parametrizadas.
	 * @param orderBy
	 * @param ordenacao
	 * @param initialResult
	 * @param maxResults
	 * @return List<LogExecucao>
	 */
	public List<LogExecucao> buscaPaginada(String orderBy, Ordenacao ordenacao, int initialResult, int maxResults);
	
}
