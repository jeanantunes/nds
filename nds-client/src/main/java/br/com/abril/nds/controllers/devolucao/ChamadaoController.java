package br.com.abril.nds.controllers.devolucao;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.client.util.DataHolder;
import br.com.abril.nds.client.util.PaginacaoUtil;
import br.com.abril.nds.client.vo.ChamadaoVO;
import br.com.abril.nds.client.vo.ResultadoChamadaoVO;
import br.com.abril.nds.controllers.BaseController;
import br.com.abril.nds.dto.ConsignadoCotaChamadaoDTO;
import br.com.abril.nds.dto.ConsultaChamadaoDTO;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.ResumoConsignadoCotaChamadaoDTO;
import br.com.abril.nds.dto.filtro.FiltroChamadaoDTO;
import br.com.abril.nds.dto.filtro.FiltroChamadaoDTO.OrdenacaoColunaChamadao;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Editor;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.PessoaFisica;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.service.ChamadaoService;
import br.com.abril.nds.service.CotaService;
import br.com.abril.nds.service.EditorService;
import br.com.abril.nds.service.FornecedorService;
import br.com.abril.nds.service.LancamentoService;
import br.com.abril.nds.service.integracao.DistribuidorService;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.Util;
import br.com.abril.nds.util.export.FileExporter;
import br.com.abril.nds.util.export.FileExporter.FileType;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.abril.nds.vo.ValidacaoVO;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

/**
 * Classe responsável pelo controle das ações referentes à
 * tela de chamadão de publicações.
 * 
 * @author Discover Technology
 */
@Resource
@Path("/devolucao/chamadao")
@Rules(Permissao.ROLE_RECOLHIMENTO_CHAMADAO)
public class ChamadaoController extends BaseController {

	@Autowired
	private Result result;
	
	@Autowired
	private HttpSession session;
	
	@Autowired
	private HttpServletResponse response;
	
	@Autowired
	private ChamadaoService chamadaoService;
	
	@Autowired
	private FornecedorService fornecedorService;
	
	@Autowired
	private DistribuidorService distribuidorService;
	
	@Autowired
	private CotaService cotaService;
	
	@Autowired
	private EditorService editorService;
	
	@Autowired
	private LancamentoService lancamentoService;
	
	private static final String FILTRO_PESQUISA_CONSIGNADOS_SESSION_ATTRIBUTE = "filtroPesquisaConsignados";
	
	private static final String QTD_REGISTROS_PESQUISA_CONSIGNADOS_SESSION_ATTRIBUTE = "qtdRegistrosPesquisaConsignados";
	
	private static final String DATA_HOLDER_ACTION_KEY = "chamadaoHolder";
	
	@Get
	@Path("/")
	public void index() {
		
		List<ItemDTO<Long, String>> listaFornecedoresCombo =
			this.carregarComboFornecedores();
			
		List<ItemDTO<Long, String>> listaEditoresCombo = 
			this.carregarComboEditores();
		
		result.include("listaFornecedores", listaFornecedoresCombo);
		result.include("listaEditores", listaEditoresCombo);
	}
	
	@Get
	@Path("/popularGridFollowUp")
	public void popularGridFollowUp(Integer numeroCota, String data) {			
		result.include("numeroCotaFollowUp", numeroCota);
		result.include("dataCotaFollowUp", data);
		result.forwardTo(ChamadaoController.class).index();
	}
	
	
	@Get
	public void exportar(FileType fileType) throws IOException {
				
		if (fileType == null) {
			
			throw new ValidacaoException(TipoMensagem.ERROR, "Tipo de arquivo não encontrado!");
		}
		
		FiltroChamadaoDTO filtroSessao = this.obterFiltroParaExportacao();
		
		ConsultaChamadaoDTO consultaChamadaoDTO = null;
		
		if (filtroSessao.isChamadaEncalhe()) {
			
			consultaChamadaoDTO = 
				this.chamadaoService.obterConsignadosComChamadao(filtroSessao);
			
		} else {
			
			consultaChamadaoDTO = 
				this.chamadaoService.obterConsignados(filtroSessao);
		}
		
		List<ConsignadoCotaChamadaoDTO> listaConsignadoCotaChamadaoDTO =
				consultaChamadaoDTO.getListaConsignadoCotaChamadaoDTO();
		
		ResumoConsignadoCotaChamadaoDTO resumoConsignadoCotaChamadao = 
			consultaChamadaoDTO.getResumoConsignadoCotaChamadao();
		
		List<ChamadaoVO> listaChamadao =
			this.getListaChamadaoVO(listaConsignadoCotaChamadaoDTO);
		
		ResultadoChamadaoVO resultadoChamadao = null;
		
		if (resumoConsignadoCotaChamadao != null) {
			
			String valorTotalFormatado = null;
			
			if (resumoConsignadoCotaChamadao.getValorTotal() != null) {
			
				valorTotalFormatado =
					CurrencyUtil.formatarValor(resumoConsignadoCotaChamadao.getValorTotal());
			}
			
			resumoConsignadoCotaChamadao.setQtdProdutosTotal(
				(long) listaConsignadoCotaChamadaoDTO.size());
			
			resultadoChamadao =
				new ResultadoChamadaoVO(null, resumoConsignadoCotaChamadao.getQtdProdutosTotal(),
										resumoConsignadoCotaChamadao.getQtdExemplaresTotal(),
										valorTotalFormatado);
		}
		
		FileExporter.to("chamadao", fileType)
			.inHTTPResponse(this.getNDSFileHeader(), filtroSessao, resultadoChamadao, 
					listaChamadao, ChamadaoVO.class, this.response);
	}
	
	/**
	 * Obtém o filtro de pesquisa para exportação.
	 * 
	 * @return filtro
	 */
	private FiltroChamadaoDTO obterFiltroParaExportacao() {
		
		FiltroChamadaoDTO filtroSessao =
			(FiltroChamadaoDTO)
				this.session.getAttribute(FILTRO_PESQUISA_CONSIGNADOS_SESSION_ATTRIBUTE);
		
		if (filtroSessao != null) {
			
			if (filtroSessao.getPaginacao() != null) {
				
				filtroSessao.getPaginacao().setPaginaAtual(null);
				filtroSessao.getPaginacao().setQtdResultadosPorPagina(null);
			}
			
			if (filtroSessao.getNumeroCota() != null) {
		
				Cota cota = this.cotaService.obterPorNumeroDaCota(filtroSessao.getNumeroCota());
				
				if (cota != null) {
					
					String nomeCota = null;
					
					if (cota.getPessoa() instanceof PessoaJuridica) {
						
						PessoaJuridica pessoaJuridica = (PessoaJuridica) cota.getPessoa();
						
						nomeCota = pessoaJuridica.getRazaoSocial();
					
					} else if (cota.getPessoa() instanceof PessoaFisica) {
						
						PessoaFisica pessoaFisica = (PessoaFisica) cota.getPessoa();
						
						nomeCota = pessoaFisica.getNome();
					}
					
					filtroSessao.setNomeCota(nomeCota);
				}
			}
			
			if (filtroSessao.getIdFornecedor() != null) {
				
				Fornecedor fornecedor =
					this.fornecedorService.obterFornecedorPorId(filtroSessao.getIdFornecedor());
				
				if (fornecedor != null) {
					
					filtroSessao.setNomeFornecedor(fornecedor.getJuridica().getRazaoSocial());
				}
			}
			
			if (filtroSessao.getIdEditor() != null) {
				
				Editor editor = this.editorService.obterEditorPorId(filtroSessao.getIdEditor());
				
				if (editor != null) {
					
					filtroSessao.setNomeEditor(editor.getPessoaJuridica().getRazaoSocial());
				}
			}
		}
		
		return filtroSessao;
	}

	
	/**
	 * Método responsável por carregar o combo de editores.
	 */
	private List<ItemDTO<Long, String>> carregarComboEditores() {

		List<Editor> editores = editorService.obterEditoresDesc();
		
		List<ItemDTO<Long, String>> listaEditoresCombo = new ArrayList<ItemDTO<Long, String>>();
		
		for (Editor editor : editores) {
			
			listaEditoresCombo.add(
				new ItemDTO<Long, String>(
					editor.getId(), editor.getPessoaJuridica().getRazaoSocial()));
		}
		
		return listaEditoresCombo;
	}
	
	/**
	 * Método responsável por carregar o combo de fornecedores.
	 */
	private List<ItemDTO<Long, String>> carregarComboFornecedores() {
		
		List<Fornecedor> listaFornecedor =
			fornecedorService.obterFornecedoresPorProduto(null, null);
		
		List<ItemDTO<Long, String>> listaFornecedoresCombo =
			new ArrayList<ItemDTO<Long,String>>();
		
		for (Fornecedor fornecedor : listaFornecedor) {
			listaFornecedoresCombo.add(
				new ItemDTO<Long, String>(fornecedor.getId(), fornecedor.getJuridica().getRazaoSocial())
			);
		}
			
		return listaFornecedoresCombo;
	}
	
	@Post
	@Path("/pesquisarConsignados")
	public void pesquisarConsignados(Integer numeroCota, String dataChamadaoFormatada, Long idFornecedor,
									 Long idEditor, boolean chamadaEncalhe, String sortorder, String sortname, int page, int rp) {
		
		this.validarEntradaDadosPesquisa(numeroCota, dataChamadaoFormatada, chamadaEncalhe);
		
		Date dataChamadao = DateUtil.parseDataPTBR(dataChamadaoFormatada);
		
		FiltroChamadaoDTO filtro = this.carregarFiltroPesquisa(numeroCota, dataChamadao, idFornecedor, idEditor, chamadaEncalhe, sortorder, sortname, page, rp);
		
		ConsultaChamadaoDTO consultaChamadaoDTO = null;
		
		if (filtro.isChamadaEncalhe()) {
		
			consultaChamadaoDTO = this.chamadaoService.obterConsignadosComChamadao(filtro);
			
		} else {
		
			consultaChamadaoDTO = this.chamadaoService.obterConsignados(filtro);
		}
		
		
		if (consultaChamadaoDTO.getListaConsignadoCotaChamadaoDTO() == null || consultaChamadaoDTO.getListaConsignadoCotaChamadaoDTO().isEmpty()) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum registro encontrado.");
			
		} else {
			
			PaginacaoUtil.armazenarQtdRegistrosPesquisa(
				this.session,
				QTD_REGISTROS_PESQUISA_CONSIGNADOS_SESSION_ATTRIBUTE,
				consultaChamadaoDTO.getListaConsignadoCotaChamadaoDTO().size());
						
			this.processarConsignados(consultaChamadaoDTO.getListaConsignadoCotaChamadaoDTO(), 
									  consultaChamadaoDTO.getResumoConsignadoCotaChamadao(),
									  filtro,
									  consultaChamadaoDTO.getQuantidadeTotalConsignados().intValue());
		}
		
		result.use(Results.json());
	}
	
	@Post
	@Path("/confirmarChamadao")
	@Rules(Permissao.ROLE_RECOLHIMENTO_CHAMADAO_ALTERACAO)
	public void confirmarChamadao(List<ConsignadoCotaChamadaoDTO> listaChamadao,
								  boolean chamarTodos, List<Long> idsIgnorados,
								  String novaDataChamadaoFormatada,boolean reprogramacao) {
		
		FiltroChamadaoDTO filtroSessao = (FiltroChamadaoDTO) this.session.getAttribute(FILTRO_PESQUISA_CONSIGNADOS_SESSION_ATTRIBUTE);
		
		FiltroChamadaoDTO filtro  = 
			this.configurarFiltroTela(filtroSessao.getNumeroCota(),
									  filtroSessao.getDataChamadao(),
									  filtroSessao.getIdFornecedor(),
									  filtroSessao.getIdEditor(),
									  filtroSessao.isChamadaEncalhe());
		
		this.validarDadosConfirmarChamadao(listaChamadao, chamarTodos, novaDataChamadaoFormatada, filtroSessao.isChamadaEncalhe(), reprogramacao);
		
		Date novaDataChamadao = null;
		
		if (filtroSessao.isChamadaEncalhe()) {
			
			novaDataChamadao = DateUtil.parseDataPTBR(novaDataChamadaoFormatada);
		}
		
		this.chamadaoService.confirmarChamadao(listaChamadao, filtro, chamarTodos, idsIgnorados, getUsuarioLogado(), novaDataChamadao);
		
		result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Chamadão realizado com sucesso!"), "result").recursive().serialize();
	}

	/**
	 * Valida os dados para confirmação do chamadão.
	 * 
	 * @param listaChamadao - lista de consignados para o chamadão
	 * @param chamarTodos - indica se todas as publicações da cota serão recolhidas
	 * @param novaDataChamadaoFormatada - nova data para chamadão formatada
	 * @param isChamadaEncalhe - indica se os consignados possuem chamada de encalhe
	 */
	private void validarDadosConfirmarChamadao(List<ConsignadoCotaChamadaoDTO> listaChamadao,
											   boolean chamarTodos,
											   String novaDataChamadaoFormatada,
											   boolean isChamadaEncalhe,
											   boolean reprogramacao) {
		
		if (!chamarTodos && listaChamadao == null) {
			
			if(reprogramacao){
				throw new ValidacaoException(TipoMensagem.WARNING,
						"É necessário selecionar pelo menos um produto para reprogramação.");
			}
			
			throw new ValidacaoException(TipoMensagem.WARNING,
					"É necessário selecionar pelo menos um produto para realizar o chamadão!");
		}
		
		if (isChamadaEncalhe) {
		
			if (novaDataChamadaoFormatada == null
					|| novaDataChamadaoFormatada.trim().isEmpty()
					|| !DateUtil.isValidDatePTBR(novaDataChamadaoFormatada)) {
				
				throw new ValidacaoException(TipoMensagem.WARNING,
					"É necessário informar a nova data de chamadão!");
			}
		}
	}
	
	@Post
	@Path("/cancelarChamadao")
	public void cancelarChamadao(List<ConsignadoCotaChamadaoDTO> listaChamadao,
			  					 boolean chamarTodos) {
		
		if (!chamarTodos && listaChamadao == null) {
			
			throw new ValidacaoException(TipoMensagem.WARNING,
				"É necessário selecionar pelo menos um produto para realizar o cancelamento do chamadão.");
		}
		
		FiltroChamadaoDTO filtroSessao =
				(FiltroChamadaoDTO) 
					this.session.getAttribute(FILTRO_PESQUISA_CONSIGNADOS_SESSION_ATTRIBUTE);
		
		FiltroChamadaoDTO filtro  = 
			this.configurarFiltroTela(filtroSessao.getNumeroCota(),
									  filtroSessao.getDataChamadao(),
									  filtroSessao.getIdFornecedor(),
									  filtroSessao.getIdEditor(),
									  filtroSessao.isChamadaEncalhe());
		
		this.chamadaoService.cancelarChamadao(listaChamadao, filtro, chamarTodos);
		
		result.use(Results.json()).from(
				new ValidacaoVO(TipoMensagem.SUCCESS, "Chamadão cancelado com sucesso!"),
								"result").recursive().serialize();
	}
	
	@Post
	public void validarMatrizRecolhimentoConfirmada(String dataPesquisa) {
		
		boolean matrizConfirmadaNaData = 
			this.lancamentoService.existeMatrizRecolhimentoConfirmado(
				DateUtil.parseDataPTBR(dataPesquisa));
		
		this.result.use(Results.json()).withoutRoot().from(matrizConfirmadaNaData).serialize();
	}
	
	/**
	 * Processa o resultado dos consignados da cota.
	 *  
	 * @param listaConsignadoCotaChamadaoDTO - lista de consignados da cota
	 * @param resumoConsignadoCotaChamadao - resumo dos consignados da cota
	 * @param filtro - filtro da pesquisa
	 * @param qtdeTotalRegistros - quantidade total de registros
	 */
	private void processarConsignados(List<ConsignadoCotaChamadaoDTO> listaConsignadoCotaChamadaoDTO,
									  ResumoConsignadoCotaChamadaoDTO resumoConsignadoCotaChamadao,
									  FiltroChamadaoDTO filtro,
									  Integer qtdeTotalRegistros) {
		
		List<ChamadaoVO> listaChamadao =
			this.getListaChamadaoVO(listaConsignadoCotaChamadaoDTO);
		
		TableModel<CellModelKeyValue<ChamadaoVO>> tableModel =
			new TableModel<CellModelKeyValue<ChamadaoVO>>();
		
		tableModel.setPage(filtro.getPaginacao().getPaginaAtual());
		tableModel.setTotal(qtdeTotalRegistros);
				
		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listaChamadao));
		
		Long qtdProdutosTotal = null;
		
		BigInteger qtdExemplaresTotal = null;
		
		String valorTotalFormatado = null;
		
		if (resumoConsignadoCotaChamadao != null) {
			
			qtdProdutosTotal = resumoConsignadoCotaChamadao.getQtdProdutosTotal();
			
			qtdExemplaresTotal = resumoConsignadoCotaChamadao.getQtdExemplaresTotal();
			
			if (resumoConsignadoCotaChamadao.getValorTotal() != null) {
			
				valorTotalFormatado = CurrencyUtil.formatarValorQuatroCasas(resumoConsignadoCotaChamadao.getValorTotal());	
			}
		}
		
		ResultadoChamadaoVO resultadoChamadao =
			new ResultadoChamadaoVO(tableModel, qtdProdutosTotal,
									qtdExemplaresTotal, valorTotalFormatado);
		
		result.use(Results.json()).withoutRoot().from(resultadoChamadao).recursive().serialize();
	}

	/**
	 * Obtém uma lista de ChamadaoVO.
	 * 
	 * @param listaConsignadoCotaChamadaoDTO - lista de consignados para o chamadão
	 * 
	 * @return lista de ChamadaoVO
	 */
	private List<ChamadaoVO> getListaChamadaoVO(List<ConsignadoCotaChamadaoDTO> listaConsignadoCotaChamadaoDTO) {
		
		List<ChamadaoVO> listaChamadao = new LinkedList<ChamadaoVO>();
		
		ChamadaoVO chamadaoVO = null;
		
		for (ConsignadoCotaChamadaoDTO consignadoCotaChamadao : listaConsignadoCotaChamadaoDTO) {
			
			chamadaoVO = new ChamadaoVO();
			
			chamadaoVO.setCodigo(consignadoCotaChamadao.getCodigoProduto());
			chamadaoVO.setProduto(consignadoCotaChamadao.getNomeProduto());
			chamadaoVO.setEdicao(consignadoCotaChamadao.getNumeroEdicao().toString());
			
			chamadaoVO.setPrecoVenda(
				CurrencyUtil.formatarValor(consignadoCotaChamadao.getPrecoVenda()));
			
			if (consignadoCotaChamadao.getPrecoDesconto() != null) {
				
				chamadaoVO.setPrecoDesconto(CurrencyUtil.formatarValorQuatroCasas(consignadoCotaChamadao.getPrecoDesconto()));
				
			} else {
				chamadaoVO.setPrecoDesconto("");
			}
			
			chamadaoVO.setReparte(String.valueOf(consignadoCotaChamadao.getReparte().longValue()));
			chamadaoVO.setFornecedor(consignadoCotaChamadao.getNomeFornecedor());
			
			chamadaoVO.setDataRecolhimento(
				DateUtil.formatarDataPTBR(consignadoCotaChamadao.getDataRecolhimento()));
			
			chamadaoVO.setValorTotal(
				CurrencyUtil.formatarValor(consignadoCotaChamadao.getValorTotal()));
			
			chamadaoVO.setValorTotalDesconto(
				CurrencyUtil.formatarValorQuatroCasas(consignadoCotaChamadao.getValorTotalDesconto()));

			String idLancamento = consignadoCotaChamadao.getIdLancamento() != null ? 
					consignadoCotaChamadao.getIdLancamento().toString() : null;

			chamadaoVO.setIdLancamento(idLancamento);

			chamadaoVO.setBrinde(consignadoCotaChamadao.isPossuiBrinde() ? "Sim" : "Não");
			
			chamadaoVO.setChecked(getCheckedFromDataHolder(idLancamento));
			
			listaChamadao.add(chamadaoVO);
		}
		
		return listaChamadao;
	}
	
	private boolean getCheckedFromDataHolder(String idLancamento) {
		
		DataHolder dataHolder = (DataHolder) this.session.getAttribute(DataHolder.SESSION_ATTRIBUTE_NAME);
		
		if (dataHolder != null) {

			return Boolean.parseBoolean(dataHolder.getData(DATA_HOLDER_ACTION_KEY, idLancamento, "checado"));
		}
		
		return Boolean.FALSE;
	}
	
	/**
	 * Valida a entrada de dados para pesquisa de consignados da cota.
	 * 
	 * @param numeroCota - número da cota
	 * @param dataChamadaoFormatada - data do chamadão
	 * @param chamadaEncalhe 
	 */
	private void validarEntradaDadosPesquisa(Integer numeroCota, String dataChamadaoFormatada, boolean chamadaEncalhe) {
		
		List<String> msgs = new ArrayList<String>();
		
		if (numeroCota == null) {
			
			msgs.add("O preenchimento da cota é obrigatório!");
		}
		
		Date dataChamadao = DateUtil.parseDataPTBR(dataChamadaoFormatada);
		
		if (!chamadaEncalhe && dataChamadao == null) {
			
			msgs.add("O preenchimento do campo [Data Chamadão] é obrigatório!");
		}
		
		if (dataChamadao != null) {
			
			Date dataAtual = DateUtil.removerTimestamp(this.distribuidorService.obterDataOperacaoDistribuidor());
			
			if (dataChamadao.compareTo(dataAtual) <= 0) {
				
				msgs.add("A Data do Chamadão deve ser maior que a data de operação!");
			}
		}
		
		if (!msgs.isEmpty()){
			
			throw new ValidacaoException(TipoMensagem.WARNING, msgs);
		}
	}
	
	/**
	 * Carrega o filtro da pesquisa de consignados da cota.
	 * 
	 * @param numeroCota - número da cota
	 * @param dataChamadao - data do chamadão
	 * @param idFornecedor - id do fornecedor
	 * @param idEditor - id do editor
	 * @param chamadaEncalhe - indica se a busca irá obter chamadas de encalhe
	 * @param sortorder - ordenação
	 * @param sortname - coluna para ordenação
	 * @param page - página atual
	 * @param rp - quantidade de registros para exibição
	 * 
	 * @return filtro
	 */
	private FiltroChamadaoDTO carregarFiltroPesquisa(Integer numeroCota,
													 Date dataChamadao,
													 Long idFornecedor,
													 Long idEditor,
													 boolean chamadaEncalhe,
													 String sortorder, 
													 String sortname, 
													 int page, 
													 int rp) {
		
		FiltroChamadaoDTO filtroAtual =
			this.configurarFiltroTela(
				numeroCota, dataChamadao, idFornecedor, idEditor, chamadaEncalhe);
		
		this.configurarPaginacaoPesquisa(filtroAtual, sortorder, sortname, page, rp);
		
		FiltroChamadaoDTO filtroSessao =
			(FiltroChamadaoDTO) 
				this.session.getAttribute(FILTRO_PESQUISA_CONSIGNADOS_SESSION_ATTRIBUTE);
		
		PaginacaoUtil.calcularPaginaAtual(
			this.session,
			QTD_REGISTROS_PESQUISA_CONSIGNADOS_SESSION_ATTRIBUTE,
			FILTRO_PESQUISA_CONSIGNADOS_SESSION_ATTRIBUTE,
			filtroAtual, filtroSessao);
		
		return filtroAtual;
	}

	/**
	 * Carrega o filtro com os dados informados na tela.
	 * 
	 * @param numeroCota - número da cota
	 * @param dataChamadao - data do chamadão
	 * @param idFornecedor - id do fornecedor
	 * @param idEditor - id do editor
	 * @param chamadaEncalhe - indica se a busca irá obter chamadas de encalhe
	 * 
	 * @return filtro
	 */
	private FiltroChamadaoDTO configurarFiltroTela(Integer numeroCota,
												   Date dataChamadao,
												   Long idFornecedor,
												   Long idEditor,
												   boolean chamadaEncalhe) {
		
		FiltroChamadaoDTO filtro =
			new FiltroChamadaoDTO(numeroCota, dataChamadao, idFornecedor, idEditor, chamadaEncalhe);
		
		return filtro;
	}
	
	/**
	 * Configura a paginação do filtro de pesquisa de consignados da cota.
	 * 
	 * @param filtro - filtro da pesquisa
	 * @param sortorder - ordenação
	 * @param sortname - coluna para ordenação
	 * @param page - página atual
	 * @param rp - quantidade de registros para exibição
	 */
	private void configurarPaginacaoPesquisa(FiltroChamadaoDTO filtro, 
											 String sortorder,
											 String sortname, 
											 int page, 
											 int rp) {
		
		if (filtro != null) {
			
			PaginacaoVO paginacao = new PaginacaoVO(page, rp, sortorder);
	
			filtro.setPaginacao(paginacao);

			filtro.setOrdenacaoColuna(
				Util.getEnumByStringValue(OrdenacaoColunaChamadao.values(), sortname));
		}
	}
	
	
}
