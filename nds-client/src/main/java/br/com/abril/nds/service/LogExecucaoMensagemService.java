package br.com.abril.nds.service;

import java.util.List;

import br.com.abril.nds.dto.InterfaceDTO;
import br.com.abril.nds.model.integracao.LogExecucaoMensagem;
import br.com.abril.nds.vo.PaginacaoVO.Ordenacao;

/**
 * Interface que define as regras de acesso a serviços referentes a entidade
 * {@link br.com.abril.nds.model.integracao.LogExecucao }  
 * @author infoA2
 */
public interface LogExecucaoMensagemService {

	/**
	 * Busca os LogExecucao respeitando as restricoes parametrizadas.
	 * @param orderBy
	 * @param ordenacao
	 * @param initialResult
	 * @param maxResults
	 * @return List<LogExecucao>
	 */
	public List<InterfaceDTO> listarInterfaces();
	
	/**
	 * Retorna a lista de mensagens de processamento de interface
	 * @return List<LogExecucaoMensagem>
	 */
	public List<LogExecucaoMensagem> listarProcessamentoInterface(Long codigoLogExecucao, String orderBy, Ordenacao ordenacao, int initialResult, int maxResults);
	
	/**
	 * Retorna a quantidade de mensagens de processamento de interface
	 * @param codigoLogExecucao
	 * @return Long
	 */
	public Long quantidadeProcessamentoInterface(Long codigoLogExecucao);
	
}
