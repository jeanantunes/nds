package br.com.abril.nds.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.client.vo.DetalheInterfaceVO;
import br.com.abril.nds.client.vo.DetalheProcessamentoVO;
import br.com.abril.nds.dto.ConsultaInterfacesDTO;
import br.com.abril.nds.dto.InterfaceDTO;
import br.com.abril.nds.dto.ProcessoDTO;
import br.com.abril.nds.integracao.service.DistribuidorService;
import br.com.abril.nds.model.StatusOperacional;
import br.com.abril.nds.model.estoque.ControleFechamentoEncalhe;
import br.com.abril.nds.model.integracao.LogExecucaoMensagem;
import br.com.abril.nds.model.integracao.StatusExecucaoEnum;
import br.com.abril.nds.repository.BaixaCobrancaService;
import br.com.abril.nds.repository.LogExecucaoRepository;
import br.com.abril.nds.service.ConsolidadoFinanceiroService;
import br.com.abril.nds.service.ExpedicaoService;
import br.com.abril.nds.service.FechamentoEncalheService;
import br.com.abril.nds.service.HistoricoSituacaoCotaService;
import br.com.abril.nds.service.LancamentoService;
import br.com.abril.nds.service.PainelProcessamentoService;

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

	private final static String ALERTA = "Alerta";
	private final static String FATAL = "Fatal";
	private final static String SUCESSO = "Sucesso";
	private final static String INDEFINIDO = "Indefinido";

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
	
	private final static String ACAO = "ACAO";
	private final static String DETALHE = "DETALHE";
	private final static String LOCAL = "LOCAL";
	
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

	@Autowired
	private ConsolidadoFinanceiroService consolidadoFinanceiroService;

	@Autowired
	private LancamentoService lancamentoService;

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
	private List<InterfaceDTO> getInterfaceList(List<ConsultaInterfacesDTO> listaLogExecucao) {
		
		List<InterfaceDTO> listaInterface = new ArrayList<InterfaceDTO>();
		
		InterfaceDTO interfaceDTO = null;
		
		String extensaoArquivo = "";
		
		for (ConsultaInterfacesDTO logExecucao : listaLogExecucao) {
			interfaceDTO = new InterfaceDTO();
			interfaceDTO.setIdLogProcessamento(logExecucao.getId().toString());
			interfaceDTO.setDataProcessmento( sdfData.format(logExecucao.getDataInicio() ));
			interfaceDTO.setHoraProcessamento( sdfHora.format(logExecucao.getDataInicio() ));
			
			//if (logExecucao.getListLogExecucaoMensagem() != null && !logExecucao.getListLogExecucaoMensagem().isEmpty()) {
				// Teoricamente, todos os registros terão as mesmas extensões. Neste caso, pega o primeiro registro (Caso a lista não seja vazia) e resgata a extensão.
				//extensaoArquivo = logExecucao.getListLogExecucaoMensagem().get(0).getNomeArquivo();
				extensaoArquivo = logExecucao.getNomeArquivo();
				extensaoArquivo = PONTO + extensaoArquivo.split(DELIMITADOR_PONTO)[extensaoArquivo.split(DELIMITADOR_PONTO).length-1];
				interfaceDTO.setExtensaoArquivo(extensaoArquivo);
			//}
			
			interfaceDTO.setNome(logExecucao.getNome());
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
	public List<DetalheProcessamentoVO> listardetalhesProcessamentoInterface(Long codigoLogExecucao) {
		List<DetalheProcessamentoVO> lista = new ArrayList<DetalheProcessamentoVO>();
		DetalheProcessamentoVO detalheProcessamentoVO = null;
		for (LogExecucaoMensagem logExecucaoMensagem : logExecucaoRepository.obterMensagensErroLogInterface(codigoLogExecucao)) {
			String tipoErro = "";
			detalheProcessamentoVO = new DetalheProcessamentoVO();
			detalheProcessamentoVO.setMensagem(logExecucaoMensagem.getMensagem());
			detalheProcessamentoVO.setNumeroLinha(logExecucaoMensagem.getNumeroLinha().toString());
			switch (logExecucaoMensagem.getLogExecucao().getStatus()) {
				case AVISO:
					tipoErro = ALERTA;
					break;
				case FALHA:
					tipoErro = FATAL;
					break;
				case SUCESSO:
					tipoErro = SUCESSO;
					break;
				default:
					tipoErro = INDEFINIDO;
					break;
			}
			detalheProcessamentoVO.setTipoErro(tipoErro);
			lista.add(detalheProcessamentoVO);
		}
		return lista;
	}

	/* (non-Javadoc)
	 * @see br.com.abril.nds.service.LogExecucaoMensagemService#listarProcessos()
	 */
	@Override
	public List<ProcessoDTO> listarProcessos() {
		
		Date dataOperacao = distribuidorService.obter().getDataOperacao();
		
		List<ProcessoDTO> processos = new ArrayList<ProcessoDTO>();
		
		// Adiciona estado Status Operacional
		processos.add(this.getProcessoStatusOperacional(dataOperacao));
		
		// Adiciona estado fechamento encalhe
		processos.add(this.getProcessoEstadoFechamentoEncalhe(dataOperacao));

		// Adiciona estado expedição confirmada
		processos.add(this.getProcessoExpedicaoConfirmaca(dataOperacao));

		// Adiciona o processo de suspensão de jornaleiro
		processos.add(this.getProcessoSuspensaoJornaleiro(dataOperacao));

		// Adiciona a última baixa automática realizada
		processos.add(this.getProcessoBaixaAutomatica(dataOperacao));

		// Adiciona dados da última cobrança gerada
		processos.add(this.getProcessoCobrancaGerada(dataOperacao));

		// Adiciona a última matriz de lançamento balanceada
		processos.add(this.getUltimaMatrizLancamento(dataOperacao));

		// Adiciona a última matriz de recolhimento balanceada
		processos.add(this.getUltimaMatrizRecolhimento(dataOperacao));

		return processos;
	}

	/**
	 * Retorna o estado operacional do sistema
	 * @return ProcessoDTO
	 */
	private ProcessoDTO getProcessoStatusOperacional(Date dataOperacao) {
		ProcessoDTO processoStatusOperacional = new ProcessoDTO();
		processoStatusOperacional.setNome(STATUS_OPERACIONAL);

		ControleFechamentoEncalhe controleFechamentoEncalhe = fechamentoEncalheService.buscaControleFechamentoEncalhePorData(dataOperacao);
		
		if (controleFechamentoEncalhe != null) {
			// Mostra os dados do fechamento diário
			processoStatusOperacional.setStatus(StatusExecucaoEnum.SUCESSO.toString());
			processoStatusOperacional.setDataProcessmento(sdfData.format(controleFechamentoEncalhe.getDataEncalhe()));
			processoStatusOperacional.setHoraProcessamento(sdfHora.format(controleFechamentoEncalhe.getDataEncalhe()));
		} else {
			// Caso não tenha um fechamento do dia, busca o último fechamento realizado
			Date dataUltimoFechamentoEncalhe = fechamentoEncalheService.buscaDataUltimoControleFechamentoEncalhe();
			processoStatusOperacional.setStatus(StatusExecucaoEnum.FALHA.toString());
			if (dataUltimoFechamentoEncalhe != null) {
				processoStatusOperacional.setDataProcessmento(sdfData.format(dataUltimoFechamentoEncalhe));
				processoStatusOperacional.setHoraProcessamento(sdfHora.format(dataUltimoFechamentoEncalhe));
			} else {
				processoStatusOperacional.setDataProcessmento("");
				processoStatusOperacional.setHoraProcessamento("");
			}
		}
		
		processoStatusOperacional.setSistemaOperacional(true);
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
			if (dataUltimoFechamentoEncalhe != null) {
				processoEstadoFechamentoEncalhe.setDataProcessmento(sdfData.format(dataUltimoFechamentoEncalhe));
				processoEstadoFechamentoEncalhe.setHoraProcessamento(sdfHora.format(dataUltimoFechamentoEncalhe));
			} else {
				processoEstadoFechamentoEncalhe.setDataProcessmento("");
				processoEstadoFechamentoEncalhe.setHoraProcessamento("");
			}
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
			if (dataUltimaExpedicao != null) {
				processoExpedicaoConfirmacao.setDataProcessmento(sdfData.format(dataUltimaExpedicao));
				processoExpedicaoConfirmacao.setHoraProcessamento(sdfHora.format(dataUltimaExpedicao));
			} else {
				processoExpedicaoConfirmacao.setDataProcessmento("");
				processoExpedicaoConfirmacao.setHoraProcessamento("");
			}
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
			if (dataUltimaSuspensaoCota != null) {
				processoSuspensaoJornaleiro.setDataProcessmento(sdfData.format(dataUltimaSuspensaoCota));
				processoSuspensaoJornaleiro.setHoraProcessamento(sdfHora.format(dataUltimaSuspensaoCota));
			} else {
				processoSuspensaoJornaleiro.setDataProcessmento("");
				processoSuspensaoJornaleiro.setHoraProcessamento("");
			}
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
			processoBaixaAutomatica.setHoraProcessamento(sdfHora.format(dataUltimaBaixaAutomatica));
		} else {
			dataUltimaBaixaAutomatica = baixaCobrancaService.buscarDiaUltimaBaixaAutomatica();
			processoBaixaAutomatica.setStatus(StatusExecucaoEnum.FALHA.toString());
			if (dataUltimaBaixaAutomatica != null) {
				processoBaixaAutomatica.setDataProcessmento(sdfData.format(dataUltimaBaixaAutomatica));
				processoBaixaAutomatica.setHoraProcessamento(sdfHora.format(dataUltimaBaixaAutomatica));
			} else {
				processoBaixaAutomatica.setDataProcessmento("");
				processoBaixaAutomatica.setHoraProcessamento("");
			}
		}
		
		return processoBaixaAutomatica;
	}

	/**
	 * Retorna o último processo de cobrança automática gerada
	 * @param dataOperacao
	 * @return ProcessoDTO
	 */
	private ProcessoDTO getProcessoCobrancaGerada(Date dataOperacao) {
		ProcessoDTO processoCobrancaGerada = new ProcessoDTO();
		processoCobrancaGerada.setNome(COBRANCA_GERADA);

		Date dataUltimaCobrancaGerada = consolidadoFinanceiroService.buscarUltimaDividaGeradaDia(dataOperacao);

		if (dataUltimaCobrancaGerada != null) {
			processoCobrancaGerada.setStatus(StatusExecucaoEnum.SUCESSO.toString());
			processoCobrancaGerada.setDataProcessmento(sdfData.format(dataUltimaCobrancaGerada));
			processoCobrancaGerada.setHoraProcessamento(sdfHora.format(dataUltimaCobrancaGerada));
		} else {
			dataUltimaCobrancaGerada = consolidadoFinanceiroService.buscarDiaUltimaDividaGerada();
			processoCobrancaGerada.setStatus(StatusExecucaoEnum.FALHA.toString());
			if (dataUltimaCobrancaGerada != null) {
				processoCobrancaGerada.setDataProcessmento(sdfData.format(dataUltimaCobrancaGerada));
				processoCobrancaGerada.setHoraProcessamento(sdfHora.format(dataUltimaCobrancaGerada));
			} else {
				processoCobrancaGerada.setDataProcessmento("");
				processoCobrancaGerada.setHoraProcessamento("");
			}
		}
		
		return processoCobrancaGerada;
		
	}

	/**
	 * Retorna o último processo de matriz de lançamento balanceado
	 * @param dataOperacao
	 * @return ProcessoDTO
	 */
	private ProcessoDTO getUltimaMatrizLancamento(Date dataOperacao) {
		ProcessoDTO processoUltimaMatrizLancamento = new ProcessoDTO();
		processoUltimaMatrizLancamento.setNome(ULTIMA_MATRIZ_LANC_BALANCEADA);

		Date dataUltimaMatrizLancamento = lancamentoService.buscarUltimoBalanceamentoLancamentoRealizadoDia(dataOperacao);

		if (dataUltimaMatrizLancamento != null) {
			processoUltimaMatrizLancamento.setStatus(StatusExecucaoEnum.SUCESSO.toString());
			processoUltimaMatrizLancamento.setDataProcessmento(sdfData.format(dataUltimaMatrizLancamento));
			processoUltimaMatrizLancamento.setHoraProcessamento(sdfHora.format(dataUltimaMatrizLancamento));
		} else {
			dataUltimaMatrizLancamento = lancamentoService.buscarDiaUltimoBalanceamentoLancamentoRealizado();
			processoUltimaMatrizLancamento.setStatus(StatusExecucaoEnum.FALHA.toString());
			if (dataUltimaMatrizLancamento != null) {
				processoUltimaMatrizLancamento.setDataProcessmento(sdfData.format(dataUltimaMatrizLancamento));
				processoUltimaMatrizLancamento.setHoraProcessamento(sdfHora.format(dataUltimaMatrizLancamento));
			} else {
				processoUltimaMatrizLancamento.setDataProcessmento("");
				processoUltimaMatrizLancamento.setHoraProcessamento("");
			}
		}
		
		return processoUltimaMatrizLancamento;
		
	}

	/**
	 * Retorna o último processo de matriz de recolhimento balanceado 
	 * @param dataOperacao
	 * @return ProcessoDTO
	 */
	private ProcessoDTO getUltimaMatrizRecolhimento(Date dataOperacao) {
		ProcessoDTO processoUltimaMatrizRecolhimento = new ProcessoDTO();
		processoUltimaMatrizRecolhimento.setNome(ULTIMA_MATRIZ_REC_BALANCEADA);

		Date dataUltimaMatrizRecolhimento = lancamentoService.buscarUltimoBalanceamentoRecolhimentoRealizadoDia(dataOperacao);

		if (dataUltimaMatrizRecolhimento != null) {
			processoUltimaMatrizRecolhimento.setStatus(StatusExecucaoEnum.SUCESSO.toString());
			processoUltimaMatrizRecolhimento.setDataProcessmento(sdfData.format(dataUltimaMatrizRecolhimento));
			processoUltimaMatrizRecolhimento.setHoraProcessamento(sdfHora.format(dataUltimaMatrizRecolhimento));
		} else {
			dataUltimaMatrizRecolhimento = lancamentoService.buscarDiaUltimoBalanceamentoRecolhimentoRealizado();
			processoUltimaMatrizRecolhimento.setStatus(StatusExecucaoEnum.FALHA.toString());
			if (dataUltimaMatrizRecolhimento != null) {
				processoUltimaMatrizRecolhimento.setDataProcessmento(sdfData.format(dataUltimaMatrizRecolhimento));
				processoUltimaMatrizRecolhimento.setHoraProcessamento(sdfHora.format(dataUltimaMatrizRecolhimento));
			} else {
				processoUltimaMatrizRecolhimento.setDataProcessmento("");
				processoUltimaMatrizRecolhimento.setHoraProcessamento("");
			}
		}
		
		return processoUltimaMatrizRecolhimento;
		
	}

	/* (non-Javadoc)
	 * @see br.com.abril.nds.service.PainelProcessamentoService#obterEstadoOperacional()
	 */
	@Override
	public String obterEstadoOperacional() {
		Date dataOperacao = distribuidorService.obter().getDataOperacao();

		ControleFechamentoEncalhe controleFechamentoEncalhe = fechamentoEncalheService.buscaControleFechamentoEncalhePorData(dataOperacao);
		// Se existe um controle de fechamento, significa que o dia já foi encerrado
		if (controleFechamentoEncalhe != null) {
			return StatusOperacional.ENCERRADO.getDescricao();
		}
		
		// Um registro foi encontrado, logo, está em processo de fechamento
		if ( fechamentoEncalheService.buscarUltimoFechamentoEncalheDia(dataOperacao) != null ) {
			return StatusOperacional.FECHAMENTO.getDescricao();
		}

		// Nenhum registro foi encontrado, logo, o sistema ainda está em operação
		return StatusOperacional.OPERANDO.getDescricao();
	}

	@Override
	@Transactional(readOnly = true)
	public List<DetalheInterfaceVO> listarDetalhesInterface(Long codigoLogExecucao) {
		List<DetalheInterfaceVO> lista = new ArrayList<DetalheInterfaceVO>();
		DetalheInterfaceVO detalheInterfaceVO = null;
		String info = null;
		Map<String, String> mapaInformacoes = new HashMap<String, String>(); 
		for (LogExecucaoMensagem logExecucaoMensagem : logExecucaoRepository.obterMensagensLogInterface(codigoLogExecucao)) {
			info = logExecucaoMensagem.getMensagemInfo();
			if (info == null) {
				// Se info retornar nulo, vai para o próximo registro do laço
				continue;
			}

			mapaInformacoes = toMap(info);
			
			detalheInterfaceVO = new DetalheInterfaceVO();
			detalheInterfaceVO.setAcao(mapaInformacoes.get(ACAO));
			detalheInterfaceVO.setDetalhe(mapaInformacoes.get(DETALHE));
			detalheInterfaceVO.setLocal(mapaInformacoes.get(LOCAL));
			lista.add(detalheInterfaceVO);
		}
		return lista;
	}

	/**
	 * Retorna um mapa baseado em uma String
	 * @param String
	 * @return Map<String, String>
	 */
	private Map<String, String> toMap(String input) {
		// assumes that input is properly formed e.g. "k1=v1,k2=v2"
		Map<String, String> map = new HashMap<String, String>();
	    String[] array = input.split(";");
	    for (String str : array) {
	        String[] pair = str.split("=");
	        map.put(pair[0], pair[1]);
	    }
	    return map;
	}
	
}
