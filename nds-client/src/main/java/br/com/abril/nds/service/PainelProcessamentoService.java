package br.com.abril.nds.service;

import java.util.List;

import br.com.abril.nds.client.vo.DetalheInterfaceVO;
import br.com.abril.nds.client.vo.DetalheProcessamentoVO;
import br.com.abril.nds.dto.InterfaceDTO;
import br.com.abril.nds.dto.ProcessoDTO;
import br.com.abril.nds.vo.PaginacaoVO.Ordenacao;

/**
 * Interface que define as regras de acesso a serviços referentes a entidade
 * {@link br.com.abril.nds.model.integracao.LogExecucao }  
 * @author infoA2
 */
public interface PainelProcessamentoService {

	/**
	 * Busca as interfaces
	 * @return List<LogExecucao>
	 */
	public List<InterfaceDTO> listarInterfaces();

	/**
	 * Busca os processos
	 * @return List<ProcessoDTO>
	 */
	public List<ProcessoDTO> listarProcessos();

	/**
	 * Retorna a lista de mensagens de processamento de interface
	 * @param codigoLogExecucao
	 * @param orderBy
	 * @param ordenacao
	 * @param initialResult
	 * @param maxResults
	 * @return List<LogExecucaoMensagem>
	 */
	public List<DetalheProcessamentoVO> listardetalhesProcessamentoInterface(Long codigoLogExecucao);
	
	/**
	 * Retorna a quantidade de mensagens de processamento de interface
	 * @param codigoLogExecucao
	 * @return Long
	 */
	public Long quantidadeProcessamentoInterface(Long codigoLogExecucao);

	/**
	 * Retorna o estado operacional do sistema, sendo:
	 * Encerrado
	 * Fechamento
	 * Offline
	 * Operando
	 * @return String
	 */
	public String obterEstadoOperacional();
	
	/**
	 * @return List<DetalheInterfaceVO>
	 */
	public List<DetalheInterfaceVO> listarDetalhesInterface(Long codigoLogExecucao);
	
}
