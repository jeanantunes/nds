package br.com.abril.nds.service;

import java.math.BigInteger;
import java.util.List;

import br.com.abril.nds.client.vo.DetalheInterfaceVO;
import br.com.abril.nds.client.vo.DetalheProcessamentoVO;
import br.com.abril.nds.dto.InterfaceDTO;
import br.com.abril.nds.dto.ProcessoDTO;
import br.com.abril.nds.dto.filtro.FiltroDetalheProcessamentoDTO;
import br.com.abril.nds.dto.filtro.FiltroInterfacesDTO;

/**
 * Interface que define as regras de acesso a serviços referentes a entidade
 * {@link br.com.abril.nds.model.integracao.LogExecucao }  
 * @author infoA2
 */
public interface PainelProcessamentoService {

	/**
	 * Busca as interfaces
	 * @param filtro 
	 * @return List<LogExecucao>
	 */
	public List<InterfaceDTO> listarInterfaces(FiltroInterfacesDTO filtro);

	/**
	 * Busca os processos
	 * @return List<ProcessoDTO>
	 */
	public List<ProcessoDTO> listarProcessos();

	/**
	 * Retorna a lista de mensagens de processamento de interface
	 * @param codigoLogExecucao
	 * @param filtro 
	 * @param orderBy
	 * @param ordenacao
	 * @param initialResult
	 * @param maxResults
	 * @return List<LogExecucaoMensagem>
	 */
	public List<DetalheProcessamentoVO> listardetalhesProcessamentoInterface(FiltroDetalheProcessamentoDTO filtro);
	
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

	public Long listarTotaldetalhesProcessamentoInterface(FiltroDetalheProcessamentoDTO filtro);

	public BigInteger listarTotalInterfaces(FiltroInterfacesDTO filtro);
	
}
