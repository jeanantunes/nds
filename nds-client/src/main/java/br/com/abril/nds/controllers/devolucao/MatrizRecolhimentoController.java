package br.com.abril.nds.controllers.devolucao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.SerializationUtils;

import br.com.abril.nds.client.util.PaginacaoUtil;
import br.com.abril.nds.client.vo.FiltroPesquisaMatrizRecolhimentoVO;
import br.com.abril.nds.client.vo.ProdutoRecolhimentoFormatadoVO;
import br.com.abril.nds.client.vo.ProdutoRecolhimentoVO;
import br.com.abril.nds.client.vo.ResultadoResumoBalanceamentoVO;
import br.com.abril.nds.client.vo.ResumoPeriodoBalanceamentoVO;
import br.com.abril.nds.client.vo.ValidacaoVO;
import br.com.abril.nds.dto.BalanceamentoRecolhimentoDTO;
import br.com.abril.nds.dto.ProdutoRecolhimentoDTO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.integracao.service.DistribuidorService;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.service.FornecedorService;
import br.com.abril.nds.service.RecolhimentoService;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.Constantes;
import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.MathUtil;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.TipoBalanceamentoRecolhimento;
import br.com.abril.nds.util.TipoMensagem;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

/**
 * Controller responsável pela Matriz de Recolhimento.
 * 
 * @author Discover Technology
 *
 */
@Resource
@Path("/devolucao/balanceamentoMatriz")
public class MatrizRecolhimentoController {

	@Autowired
	private HttpSession httpSession;
	
	@Autowired
	private Result result;

	@Autowired
	private RecolhimentoService recolhimentoService;
	
	@Autowired
	private FornecedorService fornecedorService;
	
	@Autowired
	private DistribuidorService distribuidorService;
	
	private static final String ATRIBUTO_SESSAO_FILTRO_PESQUISA_BALANCEAMENTO_RECOLHIMENTO = "filtroPesquisaBalanceamentoRecolhimento";
	
	private static final String ATRIBUTO_SESSAO_BALANCEAMENTO_RECOLHIMENTO = "balanceamentoRecolhimento";
	
	private static final String ATRIBUTO_SESSAO_BALANCEAMENTO_ALTERADO = "balanceamentoAlterado";
	
	@Get
	@Path("/")
	public void index() {
		
		List<Fornecedor> fornecedores = this.fornecedorService.obterFornecedores(true, SituacaoCadastro.ATIVO);

		removerAtributoAlteracaoSessao();
		
		this.result.include("fornecedores", fornecedores);
	}
	
	@Post
	@Path("/pesquisar")
	public void pesquisar(Integer numeroSemana, Date dataPesquisa, List<Long> listaIdsFornecedores) {
		
		this.validarDadosPesquisa(dataPesquisa, listaIdsFornecedores);
		
		BalanceamentoRecolhimentoDTO balanceamentoRecolhimento = 
				this.obterBalanceamentoRecolhimento(dataPesquisa,
													numeroSemana,
													listaIdsFornecedores,
													TipoBalanceamentoRecolhimento.AUTOMATICO,
													false);
		
		ResultadoResumoBalanceamentoVO resultadoResumoBalanceamento = 
			this.obterResultadoResumoBalanceamento(balanceamentoRecolhimento);
		
		removerAtributoAlteracaoSessao();
		
		configurarFiltropesquisa(numeroSemana, dataPesquisa, listaIdsFornecedores);
		
		this.result.use(Results.json()).from(resultadoResumoBalanceamento, "result").recursive().serialize();
	}
	
	@Post
	@Path("/confirmar")
	public void confirmar() {
		
		BalanceamentoRecolhimentoDTO balanceamentoRecolhimento = 
			(BalanceamentoRecolhimentoDTO)
				this.httpSession.getAttribute(ATRIBUTO_SESSAO_BALANCEAMENTO_RECOLHIMENTO);
		
		validarBloqueioMatrizFechada(balanceamentoRecolhimento);
		
		FiltroPesquisaMatrizRecolhimentoVO filtro = obterFiltroSessao();
		
		recolhimentoService.confirmarBalanceamentoRecolhimento(
													balanceamentoRecolhimento.getMatrizRecolhimento(),
													filtro.getNumeroSemana(),
													obterUsuario());
		
		removerAtributoAlteracaoSessao();
		
		result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS,
			"Balanceamento da matriz de recolhimento confirmado com sucesso!"), Constantes.PARAM_MSGS)
				.recursive().serialize();
	}
	
	@Post
	@Path("/balancearPorEditor")
	public void balancearPorEditor() {
		
		this.validarBloqueioMatrizFechada(null);
		
		FiltroPesquisaMatrizRecolhimentoVO filtro = obterFiltroSessao();
		
		this.validarDadosPesquisa(filtro.getDataPesquisa(), filtro.getListaIdsFornecedores());
		
		BalanceamentoRecolhimentoDTO balanceamentoRecolhimento = 
			this.obterBalanceamentoRecolhimento(filtro.getDataPesquisa(),
												filtro.getNumeroSemana(),
												filtro.getListaIdsFornecedores(),
												TipoBalanceamentoRecolhimento.EDITOR,
												true);
		
		ResultadoResumoBalanceamentoVO resultadoResumoBalanceamento = 
			this.obterResultadoResumoBalanceamento(balanceamentoRecolhimento);
		
		removerAtributoAlteracaoSessao();
		
		this.result.use(Results.json()).from(resultadoResumoBalanceamento, "result").recursive().serialize();
	}
	
	@Post
	@Path("/balancearPorValor")
	public void balancearPorValor() {
		
		this.validarBloqueioMatrizFechada(null);
		
		FiltroPesquisaMatrizRecolhimentoVO filtro = obterFiltroSessao();
		
		this.validarDadosPesquisa(filtro.getDataPesquisa(), filtro.getListaIdsFornecedores());
		
		BalanceamentoRecolhimentoDTO balanceamentoRecolhimento = 
			this.obterBalanceamentoRecolhimento(filtro.getDataPesquisa(),
												filtro.getNumeroSemana(),
												filtro.getListaIdsFornecedores(),
												TipoBalanceamentoRecolhimento.VALOR,
												true);
		
		ResultadoResumoBalanceamentoVO resultadoResumoBalanceamento = 
			this.obterResultadoResumoBalanceamento(balanceamentoRecolhimento);
		
		removerAtributoAlteracaoSessao();
		
		this.result.use(Results.json()).from(resultadoResumoBalanceamento, "result").recursive().serialize();
	}
	
	@Post
	@Path("/salvar")
	public void salvar() {
		
		BalanceamentoRecolhimentoDTO balanceamentoRecolhimento = 
			(BalanceamentoRecolhimentoDTO)
				this.httpSession.getAttribute(ATRIBUTO_SESSAO_BALANCEAMENTO_RECOLHIMENTO);
		
		this.validarBloqueioMatrizFechada(balanceamentoRecolhimento);
		
		recolhimentoService.salvarBalanceamentoRecolhimento(
			balanceamentoRecolhimento.getMatrizRecolhimento(), obterUsuario());
		
		removerAtributoAlteracaoSessao();
		
		result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS,
			"Balanceamento da matriz de recolhimento salvo com sucesso!"), Constantes.PARAM_MSGS)
				.recursive().serialize();
	}
	
	@Post
	@Path("/exibirMatrizFornecedor")
	public void exibirMatrizFornecedor(String dataFormatada, String sortorder,
									   String sortname, Integer page, Integer rp) {
		
		BalanceamentoRecolhimentoDTO balanceamentoRecolhimento =
			(BalanceamentoRecolhimentoDTO)
				httpSession.getAttribute(ATRIBUTO_SESSAO_BALANCEAMENTO_RECOLHIMENTO);
	
		if (balanceamentoRecolhimento == null
				|| balanceamentoRecolhimento.getMatrizRecolhimento() == null
				|| balanceamentoRecolhimento.getMatrizRecolhimento().isEmpty()) {
			
			throw new ValidacaoException(
				TipoMensagem.WARNING, "Não houve carga de informações para o período escolhido!");
		}
		
		List<ProdutoRecolhimentoDTO> listaProdutoRecolhimento =
			new ArrayList<ProdutoRecolhimentoDTO>();
		
		Date data = null;
		
		if (dataFormatada != null && !dataFormatada.trim().isEmpty()) {
		
			data = DateUtil.parseDataPTBR(dataFormatada);
		}
		
		if (data != null) {
			
			listaProdutoRecolhimento =
				balanceamentoRecolhimento.getMatrizRecolhimento().get(data);
			
		} else {
			
			for (Map.Entry<Date,List<ProdutoRecolhimentoDTO>> entry :
					balanceamentoRecolhimento.getMatrizRecolhimento().entrySet()) {
			
				listaProdutoRecolhimento.addAll(entry.getValue());
			}
		}
		
		if (listaProdutoRecolhimento != null && !listaProdutoRecolhimento.isEmpty()) {
		
			PaginacaoVO paginacao = new PaginacaoVO(page, rp, sortorder);
			
			processarBalanceamento(listaProdutoRecolhimento, balanceamentoRecolhimento.isMatrizFechada(),
								   paginacao, sortname);
		} else {
			
			this.result.use(Results.json()).from(Results.nothing()).serialize();
		}
	}
	
	@Post
	@Path("/voltarConfiguracaoOriginal")
	public void voltarConfiguracaoOriginal() {
		
		this.validarBloqueioMatrizFechada(null);
		
		FiltroPesquisaMatrizRecolhimentoVO filtro = obterFiltroSessao();
		
		BalanceamentoRecolhimentoDTO balanceamentoRecolhimento = 
			this.obterBalanceamentoRecolhimento(filtro.getDataPesquisa(),
												filtro.getNumeroSemana(),
												filtro.getListaIdsFornecedores(),
												TipoBalanceamentoRecolhimento.AUTOMATICO,
												true);
		
		ResultadoResumoBalanceamentoVO resultadoResumoBalanceamento = 
			this.obterResultadoResumoBalanceamento(balanceamentoRecolhimento);
		
		removerAtributoAlteracaoSessao();
		
		this.result.use(Results.json()).from(resultadoResumoBalanceamento, "result").recursive().serialize();
	}
	
	@Post
	@Path("/reprogramarSelecionados")
	public void reprogramarSelecionados(List<ProdutoRecolhimentoFormatadoVO> listaProdutoRecolhimento,
										String novaDataFormatada, String dataAntigaFormatada) {
		
		this.validarBloqueioMatrizFechada(null);
		
		FiltroPesquisaMatrizRecolhimentoVO filtro = obterFiltroSessao();
		
		this.validarDadosReprogramar(novaDataFormatada, filtro.getNumeroSemana());
		
		Date novaData = DateUtil.parseDataPTBR(novaDataFormatada);
		
		this.validarPeriodoReprogramacao(filtro.getNumeroSemana(), novaData);
		
		this.validarListaParaReprogramacao(listaProdutoRecolhimento);
		
		Date dataAntiga = DateUtil.parseDataPTBR(dataAntigaFormatada);
		
		this.atualizarMapaRecolhimento(listaProdutoRecolhimento, novaData, dataAntiga);
		
		this.adicionarAtributoAlteracaoSessao();
		
		this.result.use(Results.json()).from(Results.nothing()).serialize();
	}
	
	@Post
	@Path("/reprogramarRecolhimentoUnico")
	public void reprogramarRecolhimentoUnico(ProdutoRecolhimentoFormatadoVO produtoRecolhimento,
										     String dataAntigaFormatada) {
		
		this.validarBloqueioMatrizFechada(null);
		
		String novaDataFormatada = produtoRecolhimento.getNovaData();
		
		Date novaData = DateUtil.parseDataPTBR(novaDataFormatada);
		
		FiltroPesquisaMatrizRecolhimentoVO filtro = obterFiltroSessao();
		
		this.validarDadosReprogramar(novaDataFormatada, filtro.getNumeroSemana());
		
		this.validarPeriodoReprogramacao(filtro.getNumeroSemana(), novaData);
		
		List<ProdutoRecolhimentoFormatadoVO> listaProdutoRecolhimento = new ArrayList<ProdutoRecolhimentoFormatadoVO>();
		
		if (produtoRecolhimento != null){
			
			listaProdutoRecolhimento.add(produtoRecolhimento);
		}
		
		this.validarListaParaReprogramacao(listaProdutoRecolhimento);
		
		Date dataAntiga = DateUtil.parseDataPTBR(dataAntigaFormatada);
		
		this.atualizarMapaRecolhimento(listaProdutoRecolhimento, novaData, dataAntiga);
		
		this.adicionarAtributoAlteracaoSessao();
		
		this.result.use(Results.json()).from(Results.nothing()).serialize();
	}
	
	@Post
	@Path("/atualizarResumoBalanceamento")
	public void atualizarResumoBalanceamento() {
		
		BalanceamentoRecolhimentoDTO balanceamentoRecolhimento = 
			(BalanceamentoRecolhimentoDTO)
				this.httpSession.getAttribute(ATRIBUTO_SESSAO_BALANCEAMENTO_RECOLHIMENTO);
		
		if (balanceamentoRecolhimento == null
				|| balanceamentoRecolhimento.getMatrizRecolhimento() == null
				|| balanceamentoRecolhimento.getMatrizRecolhimento().isEmpty()) {
			
			throw new ValidacaoException(
				TipoMensagem.WARNING, "Não houve carga de informações para o período escolhido!");
		}
		
		ResultadoResumoBalanceamentoVO resultadoResumoBalanceamento = 
			this.obterResultadoResumoBalanceamento(balanceamentoRecolhimento);
		
		this.result.use(Results.json()).from(resultadoResumoBalanceamento, "result").recursive().serialize();
	}

	@Post
	@Path("/verificarBalanceamentosAlterados")
	public void verificarBalanceamentosAlterados() {
		
		Boolean balanceamentoAlterado =
			(Boolean) this.httpSession.getAttribute(ATRIBUTO_SESSAO_BALANCEAMENTO_ALTERADO);
		
		if (balanceamentoAlterado == null) {
			
			balanceamentoAlterado = false;
		}
		
		this.result.use(Results.json()).from(balanceamentoAlterado.toString(), "result").serialize();
	}
	
	/**
	 * Configura o filtro informado na tela e o armazena na sessão.
	 * 
	 * @param numeroSemana - número da semana
	 * @param dataPesquisa - data da pesquisa
	 * @param listaIdsFornecedores - lista de identificadores de fornecedores
	 */
	private void configurarFiltropesquisa(Integer numeroSemana, Date dataPesquisa, List<Long> listaIdsFornecedores) {
		
		FiltroPesquisaMatrizRecolhimentoVO filtro =
			new FiltroPesquisaMatrizRecolhimentoVO(numeroSemana, dataPesquisa, listaIdsFornecedores);
		
		this.httpSession.setAttribute(ATRIBUTO_SESSAO_FILTRO_PESQUISA_BALANCEAMENTO_RECOLHIMENTO,
									  filtro);
	}
	
	/**
	 * Obtém o filtro para pesquisa da sessão.
	 * 
	 * @return filtro
	 */
	private FiltroPesquisaMatrizRecolhimentoVO obterFiltroSessao() {
		
		FiltroPesquisaMatrizRecolhimentoVO filtro =
			(FiltroPesquisaMatrizRecolhimentoVO)
				this.httpSession.getAttribute(ATRIBUTO_SESSAO_FILTRO_PESQUISA_BALANCEAMENTO_RECOLHIMENTO);
		
		if (filtro == null) {
			
			throw new ValidacaoException(
				TipoMensagem.WARNING, "Filtro para a pesquisa não encontrado!");
		}
		
		return filtro;
	}
	
	/**
	 * Verifica se a matriz está fechada para não permitir ações indevidas.
	 * 
	 * @param balanceamentoRecolhimento - objeto que contém informações do balanceamento
	 */
	private void validarBloqueioMatrizFechada(BalanceamentoRecolhimentoDTO balanceamentoRecolhimento) {
		
		if (balanceamentoRecolhimento == null) {
			
			balanceamentoRecolhimento = 
				(BalanceamentoRecolhimentoDTO)
					this.httpSession.getAttribute(ATRIBUTO_SESSAO_BALANCEAMENTO_RECOLHIMENTO);
		}
		
		if (balanceamentoRecolhimento == null
				|| balanceamentoRecolhimento.isMatrizFechada()) {
			
			throw new ValidacaoException(
				new ValidacaoVO(TipoMensagem.WARNING,
								"Ação não permitida! A matriz já se encontra fechada!"));
		}
	}
	
	/**
	 * Adiciona um indicador, que informa se houve reprogramação de produtos, na sessão.
	 */
	private void adicionarAtributoAlteracaoSessao() {
		
		this.httpSession.setAttribute(ATRIBUTO_SESSAO_BALANCEAMENTO_ALTERADO, true);
	}
	
	/**
	 * Remove um indicador, que informa se houve reprogramação de produtos, da sessão.
	 */
	private void removerAtributoAlteracaoSessao() {
		
		this.httpSession.setAttribute(ATRIBUTO_SESSAO_BALANCEAMENTO_ALTERADO, null);
	}
	
	/**
	 * Método que atualiza o mapa de recolhimento de acordo com as escolhas do usuário
	 * 
	 * @param listaProdutoRecolhimento - lista de produtos a serem alterados
	 * @param novaData - nova data de recolhimento
	 * @param dataAntiga - data antiga de recolhimento
	 */
	private void atualizarMapaRecolhimento(List<ProdutoRecolhimentoFormatadoVO> listaProdutoRecolhimento,
										   Date novaData, Date dataAntiga) {
		
		BalanceamentoRecolhimentoDTO balanceamentoRecolhimentoSessao =
			(BalanceamentoRecolhimentoDTO)
				httpSession.getAttribute(ATRIBUTO_SESSAO_BALANCEAMENTO_RECOLHIMENTO);
		
		TreeMap<Date, List<ProdutoRecolhimentoDTO>> matrizRecolhimentoSessao =
			balanceamentoRecolhimentoSessao.getMatrizRecolhimento();
		
		TreeMap<Date, List<ProdutoRecolhimentoDTO>> matrizRecolhimento =
			clonarMapaRecolhimento(matrizRecolhimentoSessao);
		
		List<ProdutoRecolhimentoDTO> listaProdutoRecolhimentoRemover =
			new ArrayList<ProdutoRecolhimentoDTO>();
		
		List<ProdutoRecolhimentoDTO> listaProdutoRecolhimentoAdicionar =
			new ArrayList<ProdutoRecolhimentoDTO>();
		
		montarListasParaAlteracaoMapa(listaProdutoRecolhimento,
										matrizRecolhimento,
										listaProdutoRecolhimentoAdicionar,
										listaProdutoRecolhimentoRemover,
										dataAntiga);
		
		removerEAdicionarMapa(matrizRecolhimento,
							  listaProdutoRecolhimentoAdicionar,
							  listaProdutoRecolhimentoRemover,
							  novaData);
		
		this.validarSequencia(matrizRecolhimento);
		
		balanceamentoRecolhimentoSessao.setMatrizRecolhimento(matrizRecolhimento);
		
		this.httpSession.setAttribute(ATRIBUTO_SESSAO_BALANCEAMENTO_RECOLHIMENTO,
									  balanceamentoRecolhimentoSessao);
	}
	
	/**
	 * Cria uma cópia do mapa da matriz de recolhimento.
	 * Isso é necessário pois se houver alterações na cópia,
	 * não altera os valores do mapa original por referência.
	 * 
	 * @param matrizRecolhimentoSessao - matriz de recolhimento da sesão
	 * 
	 * @return cópia do mapa da matriz de recolhimento
	 */
	@SuppressWarnings("unchecked")
	private TreeMap<Date, List<ProdutoRecolhimentoDTO>> clonarMapaRecolhimento(
								Map<Date, List<ProdutoRecolhimentoDTO>> matrizRecolhimentoSessao) {
		
		byte[] mapSerialized =
			SerializationUtils.serialize(matrizRecolhimentoSessao);

		TreeMap<Date, List<ProdutoRecolhimentoDTO>> matrizRecolhimento =
			(TreeMap<Date, List<ProdutoRecolhimentoDTO>>) SerializationUtils.deserialize(mapSerialized);
		
		return matrizRecolhimento;
	}

	/**
	 * Monta as listas para alteração do mapa da matriz de recolhimento
	 * 
	 * @param listaProdutoRecolhimento - lista de produtos de recolhimento
	 * @param matrizRecolhimento - matriz de recolhimento
	 * @param listaProdutoRecolhimentoAdicionar - lista de produtos que serão adicionados
	 * @param listaProdutoRecolhimentoRemover - lista de produtos que serão removidos
	 * @param dataAntiga - data antiga de recolhimento
	 */
	private void montarListasParaAlteracaoMapa(List<ProdutoRecolhimentoFormatadoVO> listaProdutoRecolhimento,
											   Map<Date, List<ProdutoRecolhimentoDTO>> matrizRecolhimento,   									 
											   List<ProdutoRecolhimentoDTO> listaProdutoRecolhimentoAdicionar,
											   List<ProdutoRecolhimentoDTO> listaProdutoRecolhimentoRemover,
											   Date dataAntiga) {
		
		List<ProdutoRecolhimentoDTO> listaProdutoRecolhimentoSessao = null;
		
		if (dataAntiga != null) {
			
			listaProdutoRecolhimentoSessao = matrizRecolhimento.get(dataAntiga);
			
		} else {
		
			listaProdutoRecolhimentoSessao = new ArrayList<ProdutoRecolhimentoDTO>();
			
			for (Map.Entry<Date, List<ProdutoRecolhimentoDTO>> entry : matrizRecolhimento.entrySet()) {
				
				listaProdutoRecolhimentoSessao.addAll(entry.getValue());
			}
		}
		
		for (ProdutoRecolhimentoFormatadoVO produtoRecolhimento : listaProdutoRecolhimento) {
			
			for (ProdutoRecolhimentoDTO produtoRecolhimentoDTO : listaProdutoRecolhimentoSessao) {
				
				if (produtoRecolhimentoDTO.getIdLancamento().equals(
						Long.valueOf(produtoRecolhimento.getIdLancamento()))) {
					
					listaProdutoRecolhimentoRemover.add(produtoRecolhimentoDTO);
					
					produtoRecolhimentoDTO.setSequencia(
						Integer.valueOf(produtoRecolhimento.getSequencia()));
					
					listaProdutoRecolhimentoAdicionar.add(produtoRecolhimentoDTO);
					
					break;
				}
			}
		}
	}
	
	/**
	 * Remove e adiona os produtos no mapa da matriz de recolhimento.
	 * 
	 * @param matrizRecolhimento - mapa da matriz de recolhimento
	 * @param listaProdutoRecolhimentoAdicionar - lista de produtos que serão adicionados
	 * @param listaProdutoRecolhimentoRemover - lista de produtos que serão removidos
	 * @param novaData - nova data de recolhimento
	 */
	private void removerEAdicionarMapa(Map<Date, List<ProdutoRecolhimentoDTO>> matrizRecolhimento,   									 
		     						   List<ProdutoRecolhimentoDTO> listaProdutoRecolhimentoAdicionar,
		     						   List<ProdutoRecolhimentoDTO> listaProdutoRecolhimentoRemover,
		     						   Date novaData) {
		
		//Remover do mapa
		for (ProdutoRecolhimentoDTO produtoRecolhimentoDTO : listaProdutoRecolhimentoRemover) {
		
			List<ProdutoRecolhimentoDTO> listaProdutoRecolhimentoDTO =
				matrizRecolhimento.get(produtoRecolhimentoDTO.getNovaData());
			
			listaProdutoRecolhimentoDTO.remove(produtoRecolhimentoDTO);
			
			if (listaProdutoRecolhimentoDTO.isEmpty()) {
				
				matrizRecolhimento.remove(produtoRecolhimentoDTO.getNovaData());
				
			} else {
				
				matrizRecolhimento.put(produtoRecolhimentoDTO.getNovaData(),
											 listaProdutoRecolhimentoDTO);
			}
		}
		
		//Adicionar no mapa
		for (ProdutoRecolhimentoDTO produtoRecolhimentoDTO : listaProdutoRecolhimentoAdicionar) {
			
			if (!produtoRecolhimentoDTO.isPossuiChamada()) {
			
				List<ProdutoRecolhimentoDTO> listaProdutoRecolhimentoDTO =
					matrizRecolhimento.get(novaData);
				
				if (listaProdutoRecolhimentoDTO == null) {
					
					listaProdutoRecolhimentoDTO = new ArrayList<ProdutoRecolhimentoDTO>();
				}
				
				listaProdutoRecolhimentoDTO.add(produtoRecolhimentoDTO);
				
				produtoRecolhimentoDTO.setNovaData(novaData);
				
				matrizRecolhimento.put(novaData, listaProdutoRecolhimentoDTO);
				
			} else {
				
				Date dataAntiga = produtoRecolhimentoDTO.getNovaData();
				
				List<ProdutoRecolhimentoDTO> listaProdutoRecolhimentoDTO =
					matrizRecolhimento.get(dataAntiga);
				
				if (listaProdutoRecolhimentoDTO == null) {
					
					listaProdutoRecolhimentoDTO = new ArrayList<ProdutoRecolhimentoDTO>();
				}
				
				listaProdutoRecolhimentoDTO.add(produtoRecolhimentoDTO);
				
				matrizRecolhimento.put(dataAntiga, listaProdutoRecolhimentoDTO);
			}
		}
	}
	
	/**
	 * Método que processa os balanceamentos para exibição no grid.
	 * 
	 * @param listaProdutoRecolhimento - lista de produtos de recolhimento 
	 * @param matrizFechada - flag que indica se a matriz está fechada
	 * @param paginacao - paginação
	 * @param sortname - nome da coluna para ordenação
	 */
	private void processarBalanceamento(List<ProdutoRecolhimentoDTO> listaProdutoRecolhimento,
										boolean matrizFechada, PaginacaoVO paginacao, String sortname) {
		
		List<ProdutoRecolhimentoVO> listaProdutoRecolhimentoVO =
			new LinkedList<ProdutoRecolhimentoVO>();
		
		ProdutoRecolhimentoVO produtoRecolhimentoVO = null;
		
		BigDecimal precoDesconto = BigDecimal.ZERO;
		
		for (ProdutoRecolhimentoDTO produtoRecolhimentoDTO : listaProdutoRecolhimento) {
			
			produtoRecolhimentoVO = new ProdutoRecolhimentoVO();
			
			produtoRecolhimentoVO.setIdLancamento(produtoRecolhimentoDTO.getIdLancamento().toString());
			
			produtoRecolhimentoVO.setSequencia(produtoRecolhimentoDTO.getSequencia());
				
			produtoRecolhimentoVO.setCodigoProduto(produtoRecolhimentoDTO.getCodigoProduto());
			
			produtoRecolhimentoVO.setNomeProduto(produtoRecolhimentoDTO.getNomeProduto());
			
			produtoRecolhimentoVO.setNumeroEdicao(produtoRecolhimentoDTO.getNumeroEdicao());
			
			produtoRecolhimentoVO.setPrecoVenda(produtoRecolhimentoDTO.getPrecoVenda());
			
			precoDesconto = produtoRecolhimentoDTO.getPrecoVenda();
			
			if (produtoRecolhimentoDTO.getDesconto() != null) {
				
				precoDesconto = precoDesconto.subtract(produtoRecolhimentoDTO.getDesconto());
			}
			
			produtoRecolhimentoVO.setPrecoDesconto(precoDesconto);

			produtoRecolhimentoVO.setNomeFornecedor(produtoRecolhimentoDTO.getNomeFornecedor());
			
			produtoRecolhimentoVO.setNomeEditor(produtoRecolhimentoDTO.getNomeEditor());
			
			if (produtoRecolhimentoDTO.getParcial() != null) {
				produtoRecolhimentoVO.setParcial(produtoRecolhimentoDTO.getParcial().getDescricao());
			} else {
				produtoRecolhimentoVO.setParcial("Não");
			}
				
			produtoRecolhimentoVO.setBrinde(
				(produtoRecolhimentoDTO.isPossuiBrinde()) ? "Sim" : "Não");

			produtoRecolhimentoVO.setDataLancamento(produtoRecolhimentoDTO.getDataLancamento());

			produtoRecolhimentoVO.setDataRecolhimento(
				produtoRecolhimentoDTO.getDataRecolhimentoDistribuidor());
			
			produtoRecolhimentoVO.setEncalheSede(
				MathUtil.round(produtoRecolhimentoDTO.getExpectativaEncalheSede(), 2));
			
			produtoRecolhimentoVO.setEncalheAtendida(
				MathUtil.round(produtoRecolhimentoDTO.getExpectativaEncalheAtendida(), 2));
				
			produtoRecolhimentoVO.setEncalhe(
				MathUtil.round(produtoRecolhimentoDTO.getExpectativaEncalhe(), 2));
			
			produtoRecolhimentoVO.setValorTotal(produtoRecolhimentoDTO.getValorTotal());
			
			produtoRecolhimentoVO.setNovaData(produtoRecolhimentoDTO.getNovaData());
			
			produtoRecolhimentoVO.setBloqueioMatrizFechada(matrizFechada);
			
			produtoRecolhimentoVO.setBloqueioDataRecolhimento(
				produtoRecolhimentoDTO.isPossuiChamada());
			
			listaProdutoRecolhimentoVO.add(produtoRecolhimentoVO);
		}
		
		int totalRegistros = listaProdutoRecolhimentoVO.size();
		
		listaProdutoRecolhimentoVO =
			PaginacaoUtil.paginarEOrdenarEmMemoria(listaProdutoRecolhimentoVO,
												   paginacao, sortname);
				
		TableModel<CellModelKeyValue<ProdutoRecolhimentoFormatadoVO>> tableModel =
			new TableModel<CellModelKeyValue<ProdutoRecolhimentoFormatadoVO>>();
		
		tableModel.setPage(paginacao.getPaginaAtual());
		tableModel.setTotal(totalRegistros);
		
		List<CellModelKeyValue<ProdutoRecolhimentoFormatadoVO>> listaCellModel =
			new ArrayList<CellModelKeyValue<ProdutoRecolhimentoFormatadoVO>>();
		
		CellModelKeyValue<ProdutoRecolhimentoFormatadoVO> cellModel = null;
		
		for (ProdutoRecolhimentoVO vo : listaProdutoRecolhimentoVO) {
			
			ProdutoRecolhimentoFormatadoVO produtoRecolhimento = this.formatarProdutoRecolhimento(vo);
			
			cellModel =
				new CellModelKeyValue<ProdutoRecolhimentoFormatadoVO>(Integer.valueOf(vo.getIdLancamento()),
															 		  produtoRecolhimento);
			
			listaCellModel.add(cellModel);
		}
		
		tableModel.setRows(listaCellModel);
		
		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
	}
	
	/**
	 * Método que formata os valores para serem exibidos na tela.
	 * 
	 * @param produtoRecolhimento - produto recolhimento
	 * 
	 * @return produto recolhimento formatado
	 */
	private ProdutoRecolhimentoFormatadoVO formatarProdutoRecolhimento(ProdutoRecolhimentoVO produtoRecolhimento) {
	
		ProdutoRecolhimentoFormatadoVO produtoRecolhimentoFormatado =
			new ProdutoRecolhimentoFormatadoVO();
		
		produtoRecolhimentoFormatado.setIdLancamento(produtoRecolhimento.getIdLancamento());		
			
		produtoRecolhimentoFormatado.setIdLancamento(
			(produtoRecolhimento.getIdLancamento() != null) ? produtoRecolhimento.getIdLancamento().toString() : null);
		
		produtoRecolhimentoFormatado.setSequencia(
			(produtoRecolhimento.getSequencia() != null) ? produtoRecolhimento.getSequencia().toString() : null);
			
		produtoRecolhimentoFormatado.setCodigoProduto(produtoRecolhimento.getCodigoProduto());
		
		produtoRecolhimentoFormatado.setNomeProduto(produtoRecolhimento.getNomeProduto());
		
		produtoRecolhimentoFormatado.setNumeroEdicao(
			(produtoRecolhimento.getNumeroEdicao() != null) ? produtoRecolhimento.getNumeroEdicao().toString() : null);
		
		if (produtoRecolhimento.getPrecoVenda() != null) {
			produtoRecolhimentoFormatado.setPrecoVenda(CurrencyUtil.formatarValor(produtoRecolhimento.getPrecoVenda()));
		} else {
			produtoRecolhimentoFormatado.setPrecoVenda(null);
		}
		
		if (produtoRecolhimento.getPrecoDesconto() != null) {
			produtoRecolhimentoFormatado.setPrecoDesconto(
				CurrencyUtil.formatarValor(produtoRecolhimento.getPrecoDesconto()));
		} else {
			produtoRecolhimentoFormatado.setPrecoDesconto(null);
		}

		produtoRecolhimentoFormatado.setNomeFornecedor(produtoRecolhimento.getNomeFornecedor());
		
		produtoRecolhimentoFormatado.setNomeEditor(produtoRecolhimento.getNomeEditor());
		
		produtoRecolhimentoFormatado.setParcial(produtoRecolhimento.getParcial());
			
		produtoRecolhimentoFormatado.setBrinde(produtoRecolhimento.getBrinde());

		if (produtoRecolhimento.getDataLancamento() != null) {
			produtoRecolhimentoFormatado.setDataLancamento(
				DateUtil.formatarDataPTBR(produtoRecolhimento.getDataLancamento()));
		} else {
			produtoRecolhimentoFormatado.setDataLancamento(null);
		}
		
		if (produtoRecolhimento.getDataRecolhimento() != null) {
			produtoRecolhimentoFormatado.setDataRecolhimento(
				DateUtil.formatarDataPTBR(produtoRecolhimento.getDataRecolhimento()));
		} else {
			produtoRecolhimentoFormatado.setDataRecolhimento(null);
		}
		
		produtoRecolhimentoFormatado.setEncalheSede(
			(produtoRecolhimento.getEncalheSede() != null) ? produtoRecolhimento.getEncalheSede().toString() : null);
		
		produtoRecolhimentoFormatado.setEncalheAtendida(
			(produtoRecolhimento.getEncalheAtendida() != null) ? produtoRecolhimento.getEncalheAtendida().toString() : null);
			
		produtoRecolhimentoFormatado.setEncalhe(
			(produtoRecolhimento.getEncalhe() != null) ? produtoRecolhimento.getEncalhe().toString() : null);
		
		if (produtoRecolhimento.getValorTotal() != null) {
			produtoRecolhimentoFormatado.setValorTotal(CurrencyUtil.formatarValor(produtoRecolhimento.getValorTotal()));
		} else {
			produtoRecolhimentoFormatado.setValorTotal(null);
		}
		
		if (produtoRecolhimento.getNovaData() != null) {
			produtoRecolhimentoFormatado.setNovaData(DateUtil.formatarDataPTBR(produtoRecolhimento.getNovaData()));
		} else {
			produtoRecolhimentoFormatado.setNovaData(null);
		}
		
		produtoRecolhimentoFormatado.setBloqueioMatrizFechada(produtoRecolhimento.isBloqueioMatrizFechada());
		
		produtoRecolhimentoFormatado.setBloqueioDataRecolhimento(produtoRecolhimento.isBloqueioDataRecolhimento());		
		
		return produtoRecolhimentoFormatado;
	}
	
	/**
	 * Valida os dados da pesquisa.
	 *  
	 * @param numeroSemana - número da semana
	 * @param dataPesquisa - data da pesquisa
	 * @param listaIdsFornecedores - lista de id's dos fornecedores
	 */
	private void validarDadosPesquisa(Date dataPesquisa, List<Long> listaIdsFornecedores) {
		
		List<String> listaMensagens = new ArrayList<String>();
		
		if (dataPesquisa == null) {
			
			listaMensagens.add("O preenchimento do campo [Data] é obrigatório!");
			
		}
		
		if (listaIdsFornecedores == null || listaIdsFornecedores.isEmpty()) {
			
			listaMensagens.add("O preenchimento do campo [Fornecedor] é obrigatório!");
		}
		
		if (!listaMensagens.isEmpty()) {
			
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, listaMensagens));
		}
	}
	
	/**
	 * Valida os dados para reprogramação.
	 * 
	 * @param data - data para reprogramação
	 * @param numeroSemana - número da semana
	 */
	private void validarDadosReprogramar(String data, Integer numeroSemana) {
		
		if (data == null || data.trim().isEmpty()) {
			
			throw new ValidacaoException(
				new ValidacaoVO(TipoMensagem.WARNING, "O preenchimento da data é obrigatório!"));
		}
		
		if (!DateUtil.isValidDatePTBR(data)) {
			
			throw new ValidacaoException(
				new ValidacaoVO(TipoMensagem.WARNING, "Data inválida!"));
		}
		
		if (numeroSemana == null) {
			
			throw new ValidacaoException(
				new ValidacaoVO(TipoMensagem.WARNING, "Semana inválida!"));
		}
	}
	
	/**
	 * Valida a sequência dos produtos da matriz de recolhimento
	 * 
	 * @param matrizRecolhimento - matriz de recolhimento
	 */
	private void validarSequencia(Map<Date, List<ProdutoRecolhimentoDTO>> matrizRecolhimento) {
		
		Set<Integer> sequenciasValidas = new TreeSet<Integer>();
		
		Set<Integer> sequenciasInvalidas = new TreeSet<Integer>();
		
		for (Map.Entry<Date, List<ProdutoRecolhimentoDTO>> entry : matrizRecolhimento.entrySet()) {
			
			List<ProdutoRecolhimentoDTO> listaProdutoRecolhimentoDTO = entry.getValue();
		
			for (ProdutoRecolhimentoDTO produtoRecolhimentoDTO : listaProdutoRecolhimentoDTO) {
				
				boolean adicionada = sequenciasValidas.add(produtoRecolhimentoDTO.getSequencia());
				
				if (!adicionada) {
					
					sequenciasInvalidas.add(produtoRecolhimentoDTO.getSequencia());
				}
			}
		}
		
		if (!sequenciasInvalidas.isEmpty()) {
			
			throw new ValidacaoException(
				new ValidacaoVO(TipoMensagem.WARNING,
					"O campo [SM] não pode ser duplicado! A(s) SM(s) duplicadas são: " + sequenciasInvalidas));
		}
		
	}
	
	/**
	 * Valida a data de reprogramação de acordo com o período da semana.
	 * 
	 * @param numeroSemana - número da semana
	 * @param novaData - nova data de recolhimento
	 */
	private void validarPeriodoReprogramacao(Integer numeroSemana, Date novaData) {
		
		Distribuidor distribuidor = this.distribuidorService.obter();
		
		if (distribuidor == null) {
			
			throw new RuntimeException("Dados do distribuidor inexistentes!");
		}
		
		Date dataInicioSemana = DateUtil.obterDataDaSemanaNoAno(
			numeroSemana, distribuidor.getInicioSemana().getCodigoDiaSemana());
		
		Date dataFimSemana = DateUtil.adicionarDias(dataInicioSemana, 6);
		
		boolean dataValidaSemana =
			DateUtil.validarDataEntrePeriodo(novaData, dataInicioSemana, dataFimSemana);
		
		if (!dataValidaSemana) {
			
			throw new ValidacaoException(TipoMensagem.WARNING,
				"A data deve estar entre " + DateUtil.formatarDataPTBR(dataInicioSemana) + " e " 
				+ DateUtil.formatarDataPTBR(dataFimSemana) + ", referente à semana " + numeroSemana);
		}
	}
	
	/**
	 * Valida a lista de produtos informados na tela para reprogramação.
	 * 
	 * @param listaProdutoRecolhimento - lista de produtos de recolhimento
	 */
	private void validarListaParaReprogramacao(List<ProdutoRecolhimentoFormatadoVO> listaProdutoRecolhimento) {
		
		if (listaProdutoRecolhimento == null || listaProdutoRecolhimento.isEmpty()) {
			
			throw new ValidacaoException(TipoMensagem.WARNING,
				"É necessário selecionar ao menos um produto para realizar a reprogramação!");
		}
	}
	
	/**
	 * Obtém a matriz de balanceamento de recolhimento.
	 * 
	 * @param dataBalanceamento - data de balanceamento
	 * @param numeroSemana - número da semana
	 * @param listaIdsFornecedores - lista de identificadores dos fornecedores
	 * @param tipoBalanceamentoRecolhimento - tipo de balanceamento de recolhimento
	 * @param forcarBalanceamento - indicador para forçar a sugestão através do balanceamento
	 * 
	 * @return - objeto contendo as informações do balanceamento
	 */
	private BalanceamentoRecolhimentoDTO obterBalanceamentoRecolhimento(Date dataBalanceamento,
																		Integer numeroSemana,
																		List<Long> listaIdsFornecedores,
																		TipoBalanceamentoRecolhimento tipoBalanceamentoRecolhimento,
																		boolean forcarBalanceamento) {
		
		BalanceamentoRecolhimentoDTO balanceamentoRecolhimento = null;
		
		if (numeroSemana != null && listaIdsFornecedores != null) {

			balanceamentoRecolhimento = 
				this.recolhimentoService.obterMatrizBalanceamento(numeroSemana,
																  listaIdsFornecedores,
																  tipoBalanceamentoRecolhimento,
																  forcarBalanceamento);
			
			this.httpSession.setAttribute(ATRIBUTO_SESSAO_BALANCEAMENTO_RECOLHIMENTO,
										  balanceamentoRecolhimento);
		}
		
		if (balanceamentoRecolhimento == null
				|| balanceamentoRecolhimento.getMatrizRecolhimento() == null
				|| balanceamentoRecolhimento.getMatrizRecolhimento().isEmpty()) {
			
			throw new ValidacaoException(
				TipoMensagem.WARNING, "Não houve carga de informações para o período escolhido!");
		}
		
		return balanceamentoRecolhimento;
	}
	
	/**
	 * Obtém o resumo do período de balanceamento de acordo com a data da pesquisa
	 * e a lista de id's dos fornecedores.
	 */
	private ResultadoResumoBalanceamentoVO obterResultadoResumoBalanceamento(
											BalanceamentoRecolhimentoDTO balanceamentoRecolhimento) {
		
		if (balanceamentoRecolhimento == null
				|| balanceamentoRecolhimento.getMatrizRecolhimento() == null
				|| balanceamentoRecolhimento.getMatrizRecolhimento().isEmpty()) {
			
			return null;
		}
		
		List<ResumoPeriodoBalanceamentoVO> resumoPeriodoBalanceamento =
			new ArrayList<ResumoPeriodoBalanceamentoVO>();
		
		for (Map.Entry<Date, List<ProdutoRecolhimentoDTO>> entry : balanceamentoRecolhimento.getMatrizRecolhimento().entrySet()) {
			
			Date dataRecolhimento = entry.getKey();
			
			ResumoPeriodoBalanceamentoVO itemResumoPeriodoBalanceamento = new ResumoPeriodoBalanceamentoVO();
			
			itemResumoPeriodoBalanceamento.setData(dataRecolhimento);
			
			List<ProdutoRecolhimentoDTO> listaProdutosRecolhimento = entry.getValue();
			
			if (listaProdutosRecolhimento != null && !listaProdutosRecolhimento.isEmpty()) {
				
				boolean exibeDestaque = false;
				
				Long qtdeTitulos = Long.valueOf(listaProdutosRecolhimento.size());
				Long qtdeTitulosParciais = 0L;
				
				BigDecimal pesoTotal = BigDecimal.ZERO;
				BigDecimal qtdeExemplares = BigDecimal.ZERO;
				BigDecimal valorTotal = BigDecimal.ZERO;
				
				for (ProdutoRecolhimentoDTO produtoRecolhimento : listaProdutosRecolhimento) {
					
					if (produtoRecolhimento.getExpectativaEncalheAtendida() != null
							&& produtoRecolhimento.getExpectativaEncalheAtendida().doubleValue() > 0) {
						
						exibeDestaque = true;
					}
					
					if (produtoRecolhimento.getParcial() != null) {
						
						qtdeTitulosParciais++;
					}
					
					if (produtoRecolhimento.getPeso() != null) {
						
						pesoTotal = pesoTotal.add(produtoRecolhimento.getPeso());
					}
					
					if (produtoRecolhimento.getValorTotal() != null) {
						
						valorTotal = valorTotal.add(produtoRecolhimento.getValorTotal());
					}
					
					if (produtoRecolhimento.getExpectativaEncalhe() != null) {
						
						qtdeExemplares = qtdeExemplares.add(produtoRecolhimento.getExpectativaEncalhe());
					}
				}
				
				boolean excedeCapacidadeDistribuidor = false;
				
				if (balanceamentoRecolhimento.getCapacidadeRecolhimentoDistribuidor() != null) {
				
					excedeCapacidadeDistribuidor =
						(balanceamentoRecolhimento.getCapacidadeRecolhimentoDistribuidor()
							.compareTo(qtdeExemplares) == -1);
				}
				
				itemResumoPeriodoBalanceamento.setExcedeCapacidadeDistribuidor(
					excedeCapacidadeDistribuidor);
				
				itemResumoPeriodoBalanceamento.setExibeDestaque(exibeDestaque);
				itemResumoPeriodoBalanceamento.setPesoTotal(pesoTotal);
				itemResumoPeriodoBalanceamento.setQtdeExemplares(MathUtil.round(qtdeExemplares, 2));
				itemResumoPeriodoBalanceamento.setQtdeTitulos(qtdeTitulos);
				
				itemResumoPeriodoBalanceamento.setQtdeTitulosParciais(qtdeTitulosParciais);
				
				itemResumoPeriodoBalanceamento.setValorTotal(valorTotal);
			}
			
			resumoPeriodoBalanceamento.add(itemResumoPeriodoBalanceamento);
		}
		
		ResultadoResumoBalanceamentoVO resultadoResumoBalanceamento = new ResultadoResumoBalanceamentoVO();
		
		resultadoResumoBalanceamento.setMatrizFechada(balanceamentoRecolhimento.isMatrizFechada());
		
		resultadoResumoBalanceamento.setListaResumoPeriodoBalanceamento(resumoPeriodoBalanceamento);
		
		resultadoResumoBalanceamento.setCapacidadeRecolhimentoDistribuidor(
			balanceamentoRecolhimento.getCapacidadeRecolhimentoDistribuidor());
		
		return resultadoResumoBalanceamento;
	}
	
	/**
	 * Obtém usuário logado.
	 * 
	 * @return usuário logado
	 */
	private Usuario obterUsuario() {
		
		//TODO: Aguardando definição de como será obtido o usuário logado
		
		Usuario usuario = new Usuario();
		
		usuario.setId(1L);
		usuario.setNome("Usuário da Silva");
		
		return usuario;
	}
	
}