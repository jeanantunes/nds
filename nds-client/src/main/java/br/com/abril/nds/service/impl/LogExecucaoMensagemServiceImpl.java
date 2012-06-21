package br.com.abril.nds.service.impl;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.InterfaceDTO;
import br.com.abril.nds.model.integracao.LogExecucao;
import br.com.abril.nds.model.integracao.LogExecucaoMensagem;
import br.com.abril.nds.repository.LogExecucaoRepository;
import br.com.abril.nds.service.LogExecucaoMensagemService;
import br.com.abril.nds.vo.PaginacaoVO.Ordenacao;

/**
 * Classe de implementação referente ao serviço da entidade
 * {@link br.com.abril.nds.model.integracao.LogExecucao}
 * 
 * @author InfoA2
 */
@Service
public class LogExecucaoMensagemServiceImpl implements LogExecucaoMensagemService {

	private final static String PONTO = ".";
	private final static String DELIMITADOR_PONTO = "\\.";
	
	@Autowired
	private LogExecucaoRepository logExecucaoRepository;

	/**
	 * Busca os LogExecucao respeitando as restricoes parametrizadas.
	 * @param orderBy
	 * @param ordenacao
	 * @param initialResult
	 * @param maxResults
	 * @return List<LogExecucao>
	 */
	@Transactional(readOnly = true)
	@Override
	public List<InterfaceDTO> listarInterfaces() {
		return getInterfaceList(logExecucaoRepository.obterInterfaces());
	}

	/**
	 * Popula a lista de DTO para exibir na Grid
	 * @param listaLogExecucao
	 * @return
	 */
	private List<InterfaceDTO> getInterfaceList(List<LogExecucao> listaLogExecucao) {
		
		List<InterfaceDTO> listaInterface = new ArrayList<InterfaceDTO>();
		
		InterfaceDTO interfaceDTO = null;
		
		SimpleDateFormat sdfData = new SimpleDateFormat("dd/MM/yyyy");
		SimpleDateFormat sdfHora = new SimpleDateFormat("HH:mm");
		
		String extensaoArquivo = "";
		
		for (LogExecucao logExecucao : listaLogExecucao) {
			interfaceDTO = new InterfaceDTO();
			interfaceDTO.setIdLogProcessamento(logExecucao.getId().toString());
			interfaceDTO.setDataProcessmento( sdfData.format(logExecucao.getDataInicio() ));
			interfaceDTO.setHoraProcessamento( sdfHora.format(logExecucao.getDataInicio() ));
			
			if (logExecucao.getListLogExecucaoMensagem() != null && !logExecucao.getListLogExecucaoMensagem().isEmpty()) {
				// Teoricamente, todos os registros terão as mesmas extensões. Neste caso, pega o primeiro registro (Caso a lista não seja vazia) e resgata a extensão.
				extensaoArquivo = logExecucao.getListLogExecucaoMensagem().get(0).getNomeArquivo();
				extensaoArquivo = PONTO + extensaoArquivo.split(DELIMITADOR_PONTO)[extensaoArquivo.split(DELIMITADOR_PONTO).length-1];
				interfaceDTO.setExtensaoArquivo(extensaoArquivo);
			}
			
			interfaceDTO.setNome(logExecucao.getNomeLoginUsuario());
			interfaceDTO.setStatus(logExecucao.getStatus().toString());
			
			listaInterface.add(interfaceDTO);
			
		}
		
		return listaInterface;
		
	}

	/* (non-Javadoc)
	 * @see br.com.abril.nds.service.LogExecucaoMensagemService#listarProcessamentoInterface(java.lang.Long)
	 */
	@Transactional(readOnly = true)
	@Override
	public List<LogExecucaoMensagem> listarProcessamentoInterface(Long codigoLogExecucao, String orderBy, Ordenacao ordenacao, int initialResult, int maxResults) {
		return logExecucaoRepository.obterMensagensLogInterface(codigoLogExecucao, orderBy, ordenacao, initialResult, maxResults);
	}

	/* (non-Javadoc)
	 * @see br.com.abril.nds.service.LogExecucaoMensagemService#quantidadeProcessamentoInterface(java.lang.Long)
	 */
	@Transactional(readOnly = true)
	@Override
	public Long quantidadeProcessamentoInterface(Long codigoLogExecucao) {
		return logExecucaoRepository.quantidadeMensagensLogInterface(codigoLogExecucao);
	}
	
}
