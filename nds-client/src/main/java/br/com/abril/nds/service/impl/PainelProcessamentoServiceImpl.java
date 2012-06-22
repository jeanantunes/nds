package br.com.abril.nds.service.impl;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.dto.InterfaceDTO;
import br.com.abril.nds.dto.ProcessoDTO;
import br.com.abril.nds.model.estoque.ControleFechamentoEncalhe;
import br.com.abril.nds.model.integracao.LogExecucao;
import br.com.abril.nds.model.integracao.LogExecucaoMensagem;
import br.com.abril.nds.model.integracao.StatusExecucaoEnum;
import br.com.abril.nds.repository.BaixaCobrancaService;
import br.com.abril.nds.repository.LogExecucaoRepository;
import br.com.abril.nds.service.DistribuidorService;
import br.com.abril.nds.service.ExpedicaoService;
import br.com.abril.nds.service.FechamentoEncalheService;
import br.com.abril.nds.service.HistoricoSituacaoCotaService;
import br.com.abril.nds.service.PainelProcessamentoService;
import br.com.abril.nds.vo.PaginacaoVO.Ordenacao;

/**
 * Classe de implementação referente ao serviço da entidade
 * {@link br.com.abril.nds.model.integracao.LogExecucao}
 * 
 * @author InfoA2
 */
@Service
public class PainelProcessamentoServiceImpl implements PainelProcessamentoService {

	private final static String PONTO = ".";
	private final static String DELIMITADOR_PONTO = "\\.";

	private final static String STATUS_OPERACIONAL = "Status Operacional";
	private final static String CONFERENCIA_ENCALHE_FINALIZADA = "Conferencia Encalhe Finalizada";
	private final static String EXPEDICAO_CONFIRMADA = "Expedição Confirmada";
	private final static String SUSPENSAO_JORNALEIRO = "Suspensão Jornaleiro";
	private final static String BAIXA_BANCARIA_BOLETO = "Baixa Bancaria Boleto";
	private final static String COBRANCA_GERADA = "Cobrança Gerada";
	private final static String ULTIMA_MATRIZ_LANC_BALANCEADA = "Ultima Matriz Lanc Balanceada";
	private final static String ULTIMA_MATRIZ_REC_BALANCEADA = "Ultima Matriz Rec Balanceada";

	private SimpleDateFormat sdfData = new SimpleDateFormat("dd/MM/yyyy");
	private SimpleDateFormat sdfHora = new SimpleDateFormat("HH:mm");
	
	@Autowired
	private LogExecucaoRepository logExecucaoRepository;

	@Autowired
	private FechamentoEncalheService fechamentoEncalheService;

	@Autowired
	private DistribuidorService distribuidorService;

	@Autowired
	private ExpedicaoService expedicaoService;

	@Autowired
	private HistoricoSituacaoCotaService historicoSituacaoCotaService;

	@Autowired
	private BaixaCobrancaService baixaCobrancaService;

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

	/* (non-Javadoc)
	 * @see br.com.abril.nds.service.LogExecucaoMensagemService#listarProcessos()
	 */
	@Override
	public List<ProcessoDTO> listarProcessos() {
		
		Date dataOperacao = distribuidorService.obter().getDataOperacao();
		
		List<ProcessoDTO> processos = new ArrayList<ProcessoDTO>();
		
		// Adiciona estado Status Operacional
		processos.add(this.getProcessoStatusOperacional());
		
		// Adiciona estado fechamento encalhe
		processos.add(this.getProcessoEstadoFechamentoEncalhe(dataOperacao));

		// Adiciona estado expedição confirmada
		processos.add(this.getProcessoExpedicaoConfirmaca(dataOperacao));

		// Adiciona o processo de suspensão de jornaleiro
		processos.add(this.getProcessoSuspensaoJornaleiro(dataOperacao));

		// Adiciona a última baixa automática realizada
		processos.add(this.getProcessoBaixaAutomatica(dataOperacao));

		return processos;
	}

	/**
	 * Retorna o estado operacional do sistema
	 * @return ProcessoDTO
	 */
	private ProcessoDTO getProcessoStatusOperacional() {
		ProcessoDTO processoStatusOperacional = new ProcessoDTO();
		processoStatusOperacional.setNome(STATUS_OPERACIONAL);
		processoStatusOperacional.setSistemaOperacional(true);
		// Colocar o estado, data e hora do estado operacional
		return processoStatusOperacional;
	}

	/**
	 * Retorna o estado do fechamento de encalhe
	 * @param dataOperacao
	 * @return ProcessoDTO
	 */
	private ProcessoDTO getProcessoEstadoFechamentoEncalhe(Date dataOperacao) {
		
		ProcessoDTO processoEstadoFechamentoEncalhe = new ProcessoDTO();
		processoEstadoFechamentoEncalhe.setNome(CONFERENCIA_ENCALHE_FINALIZADA);
		
		ControleFechamentoEncalhe controleFechamentoEncalhe = fechamentoEncalheService.buscaControleFechamentoEncalhePorData(dataOperacao);
		
		if (controleFechamentoEncalhe != null) {
			// Mostra os dados do fechamento diário
			processoEstadoFechamentoEncalhe.setStatus(StatusExecucaoEnum.SUCESSO.toString());
			processoEstadoFechamentoEncalhe.setDataProcessmento(sdfData.format(controleFechamentoEncalhe.getDataEncalhe()));
			processoEstadoFechamentoEncalhe.setHoraProcessamento(sdfHora.format(controleFechamentoEncalhe.getDataEncalhe()));
		} else {
			// Caso não tenha um fechamento do dia, busca o último fechamento realizado
			Date dataUltimoFechamentoEncalhe = fechamentoEncalheService.buscaDataUltimoControleFechamentoEncalhe();
			processoEstadoFechamentoEncalhe.setStatus(StatusExecucaoEnum.FALHA.toString());
			processoEstadoFechamentoEncalhe.setDataProcessmento(sdfData.format(dataUltimoFechamentoEncalhe));
			processoEstadoFechamentoEncalhe.setHoraProcessamento(sdfHora.format(dataUltimoFechamentoEncalhe));
		}
		
		return processoEstadoFechamentoEncalhe;
	}
	
	/**
	 * Retorna processo de expedição confirmada para o dia
	 * @param dataOperacao
	 * @return ProcessoDTO
	 */
	private ProcessoDTO getProcessoExpedicaoConfirmaca(Date dataOperacao) {
		
		ProcessoDTO processoExpedicaoConfirmacao = new ProcessoDTO();
		processoExpedicaoConfirmacao.setNome(EXPEDICAO_CONFIRMADA);
		
		Date dataUltimaExpedicao = expedicaoService.obterDataUltimaExpedicaoDia(dataOperacao);
		
		if (dataUltimaExpedicao != null) {
			// Mostra os dados da última expedição do dia
			processoExpedicaoConfirmacao.setStatus(StatusExecucaoEnum.SUCESSO.toString());
			processoExpedicaoConfirmacao.setDataProcessmento(sdfData.format(dataUltimaExpedicao));
			processoExpedicaoConfirmacao.setHoraProcessamento(sdfHora.format(dataUltimaExpedicao));
		} else {
			// Caso não tenha um fechamento do dia, busca o último fechamento realizado
			dataUltimaExpedicao = expedicaoService.obterDataUltimaExpedicao();
			processoExpedicaoConfirmacao.setStatus(StatusExecucaoEnum.FALHA.toString());
			processoExpedicaoConfirmacao.setDataProcessmento(sdfData.format(dataUltimaExpedicao));
			processoExpedicaoConfirmacao.setHoraProcessamento(sdfHora.format(dataUltimaExpedicao));
		}
		
		return processoExpedicaoConfirmacao;
	}

	/**
	 * Retorna o último processo de suspensão de jornaleiro do dia (ou o último realizado) 
	 * @param dataOperacao
	 * @return ProcessoDTO
	 */
	private ProcessoDTO getProcessoSuspensaoJornaleiro(Date dataOperacao) {
		ProcessoDTO processoSuspensaoJornaleiro = new ProcessoDTO();
		processoSuspensaoJornaleiro.setNome(SUSPENSAO_JORNALEIRO);
		
		Date dataUltimaSuspensaoCota = historicoSituacaoCotaService.buscarUltimaSuspensaoCotasDia(dataOperacao);
		
		if (dataUltimaSuspensaoCota != null) {
			// Mostra os dados da última suspensão de jornaleiro do dia
			processoSuspensaoJornaleiro.setStatus(StatusExecucaoEnum.SUCESSO.toString());
			processoSuspensaoJornaleiro.setDataProcessmento(sdfData.format(dataUltimaSuspensaoCota));
			processoSuspensaoJornaleiro.setHoraProcessamento(sdfHora.format(dataUltimaSuspensaoCota));
		} else {
			// Caso não tenha uma suspensão no dia, busca a última suspensão realizada
			dataUltimaSuspensaoCota = historicoSituacaoCotaService.buscarDataUltimaSuspensaoCotas();
			processoSuspensaoJornaleiro.setStatus(StatusExecucaoEnum.FALHA.toString());
			processoSuspensaoJornaleiro.setDataProcessmento(sdfData.format(dataUltimaSuspensaoCota));
			processoSuspensaoJornaleiro.setDataProcessmento(sdfData.format(dataUltimaSuspensaoCota));
		}
		
		return processoSuspensaoJornaleiro;
	}

	/**
	 * Retorna o último processo de baixa automática do dia (ou o último realizado)
	 * @param dataOperacao
	 * @return ProcessoDTO
	 */
	private ProcessoDTO getProcessoBaixaAutomatica(Date dataOperacao) {
		ProcessoDTO processoBaixaAutomatica = new ProcessoDTO();
		processoBaixaAutomatica.setNome(BAIXA_BANCARIA_BOLETO);
		
		Date dataUltimaBaixaAutomatica = baixaCobrancaService.buscarUltimaBaixaAutomaticaDia(dataOperacao);
		
		if (dataUltimaBaixaAutomatica != null) {
			processoBaixaAutomatica.setStatus(StatusExecucaoEnum.SUCESSO.toString());
			processoBaixaAutomatica.setDataProcessmento(sdfData.format(dataUltimaBaixaAutomatica));
			processoBaixaAutomatica.setDataProcessmento(sdfData.format(dataUltimaBaixaAutomatica));
		} else {
			dataUltimaBaixaAutomatica = baixaCobrancaService.buscarDiaUltimaBaixaAutomatica();
			processoBaixaAutomatica.setStatus(StatusExecucaoEnum.FALHA.toString());
			processoBaixaAutomatica.setDataProcessmento(sdfData.format(dataUltimaBaixaAutomatica));
			processoBaixaAutomatica.setDataProcessmento(sdfData.format(dataUltimaBaixaAutomatica));
		}
		
		return processoBaixaAutomatica;
	}

	/**
	 * Retorna o último processo de cobrança automática gerada
	 * @param dataOperacao
	 * @return ProcessoDTO
	 */
	private ProcessoDTO getProcessoCobrancaGerada(Date dataOperacao) {
		ProcessoDTO processoBaixaAutomatica = new ProcessoDTO();
		processoBaixaAutomatica.setNome(COBRANCA_GERADA);
		
		/*if (!= null) {
			
		}*/
		
		return processoBaixaAutomatica;
	}

}
