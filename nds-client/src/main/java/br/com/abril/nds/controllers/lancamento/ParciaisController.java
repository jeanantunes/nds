package br.com.abril.nds.controllers.lancamento;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.controllers.BaseController;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.ParcialDTO;
import br.com.abril.nds.dto.ParcialVendaDTO;
import br.com.abril.nds.dto.PeriodoParcialDTO;
import br.com.abril.nds.dto.RedistribuicaoParcialDTO;
import br.com.abril.nds.dto.filtro.FiltroParciaisDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.model.planejamento.StatusLancamentoParcial;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.serialization.custom.FlexiGridJson;
import br.com.abril.nds.service.CalendarioService;
import br.com.abril.nds.service.FornecedorService;
import br.com.abril.nds.service.LancamentoParcialService;
import br.com.abril.nds.service.ParciaisService;
import br.com.abril.nds.service.PeriodoLancamentoParcialService;
import br.com.abril.nds.service.ProdutoEdicaoService;
import br.com.abril.nds.service.ProdutoService;
import br.com.abril.nds.service.integracao.DistribuidorService;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.Constantes;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.TableModel;
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

@Resource
@Path("/parciais")
@Rules(Permissao.ROLE_LANCAMENTO_PARCIAIS)
public class ParciaisController extends BaseController {

	private static final String FILTRO_SESSION = "filtroParcial";
	
	private static final String FILTRO_SESSION_DETALHE = "filtroParcialDetalhe";
	
	private static String FILTRO_DATA_LANCAMENTO = "filtroDataLancamento";
	
	private static String FILTRO_DATA_RECEBIMENTO = "filtroDataRecebimento";
	
	private static String FILTRO_ID_PRODUTO_EDICAO = "filtroIdProdutoEdicao";
	
	@Autowired
	private HttpSession session;
		
	@Autowired
	private Result result;
	
	@Autowired
	private FornecedorService fornecedorService;
	
	@Autowired
	private ProdutoService produtoService;
	
	@Autowired
	private LancamentoParcialService lancamentoParcialService;
	
	@Autowired
	private DistribuidorService distribuidorService;
	
	@Autowired
	private PeriodoLancamentoParcialService periodoLancamentoParcialService;
	
	@Autowired
	private ProdutoEdicaoService produtoEdicaoService;
	
	@Autowired
	private ParciaisService parciaisService;
	
	@Autowired
	private HttpServletResponse httpResponse;
	
	@Autowired
	private CalendarioService calendarioService;
		
	public ParciaisController(Result result){
		this.result = result;
	}
	
	public void parciais() {
		
	}
	
	/**
	 * Inicializa dados da tela
	 */
	@Path("/")
	public void index() {
		
		session.removeAttribute(FILTRO_SESSION);
		session.removeAttribute(FILTRO_SESSION_DETALHE);
		
		carregarComboFornecedores();
		
		carregarComboStatus();
		
		result.forwardTo(ParciaisController.class).parciais();
	}
	
	/**
	 * Método responsável por carregar o combo de fornecedores.
	 */
	private void carregarComboFornecedores() {
		
		List<Fornecedor> listaFornecedor = fornecedorService.obterFornecedoresAtivos();
		
		List<ItemDTO<Long, String>> listaFornecedoresCombo = new ArrayList<ItemDTO<Long,String>>();
		
		for (Fornecedor fornecedor : listaFornecedor) {
			listaFornecedoresCombo.add(new ItemDTO<Long, String>(fornecedor.getId(), fornecedor.getJuridica().getNomeFantasia()));
		}
		
		result.include("listaFornecedores",listaFornecedoresCombo );
	}

	/**
	 * Método responsável por carregar o combo de fornecedores.
	 */
	private void carregarComboStatus() {
				
		List<ItemDTO<String, String>> listaComboStatus = new ArrayList<ItemDTO<String,String>>();
		
		for (StatusLancamento status : StatusLancamento.values()) {
			listaComboStatus.add(new ItemDTO<String, String>( status.name(), status.getDescricao()));
		}
		
		result.include("listaStatus",listaComboStatus );
	}
	
	/**
	 * Pesquisa de parciais
	 */
	@Post
	public void pesquisarParciais(FiltroParciaisDTO filtro, Integer page, Integer rp, String sortname, String sortorder) {
		
		filtro.setPaginacao(new PaginacaoVO(page, rp, sortorder,sortname));
		
		this.validarEntrada(filtro);
		this.tratarFiltro(filtro, false);
		
		TableModel<CellModelKeyValue<ParcialDTO>> tableModel = efetuarConsulta(filtro);
		
		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
	}
	
	/**
	 * Pesquisa de parciais
	 */
	@Post
	public void pesquisarPeriodosParciais(FiltroParciaisDTO filtro, Integer page, Integer rp, String sortname, String sortorder) {
		
		filtro.setPaginacao(new PaginacaoVO(page, rp, sortorder,sortname));
		
		this.validarEntrada(filtro);
		this.tratarFiltro(filtro, true);
		
		TableModel<CellModelKeyValue<PeriodoParcialDTO>> tableModel = efetuarConsultaPeriodos(filtro);
		
		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
	}
	
	private void validarEntrada(FiltroParciaisDTO filtro) {
		
		if(filtro.getDataInicial()!=null && !filtro.getDataInicial().trim().isEmpty() && !DateUtil.isValidDatePTBR(filtro.getDataInicial()))
			throw new ValidacaoException(TipoMensagem.WARNING, "Data inicial pesquisada é inválida.");
		
		if(filtro.getDataFinal()!=null && !filtro.getDataFinal().trim().isEmpty() && !DateUtil.isValidDatePTBR(filtro.getDataFinal()))
				throw new ValidacaoException(TipoMensagem.WARNING, "Data final pesquisada é inválida.");
		
		
	}
	

	/**
	 * Executa tratamento de paginação em função de alteração do filtro de pesquisa.
	 * 
	 * @param filtroResumoExpedicao
	 */
	private void tratarFiltro(FiltroParciaisDTO filtroAtual, boolean detalhes) {

		FiltroParciaisDTO filtroSession = (FiltroParciaisDTO) session
				.getAttribute(detalhes ? FILTRO_SESSION_DETALHE : FILTRO_SESSION);
		
		if (filtroSession != null && !filtroSession.equals(filtroAtual)) {

			filtroAtual.getPaginacao().setPaginaAtual(1);
		}
		
		session.setAttribute(detalhes ? FILTRO_SESSION_DETALHE : FILTRO_SESSION, filtroAtual);
	}

	/**
	 * Efetua a consulta e monta a estrutura do grid de parciais.
	 * @param filtro
	 * @return 
	 */	
	private TableModel<CellModelKeyValue<ParcialDTO>> efetuarConsulta(FiltroParciaisDTO filtro) {
		
		List<ParcialDTO> listaParciais = lancamentoParcialService.buscarLancamentosParciais(filtro);
						
		Integer totalRegistros = lancamentoParcialService.totalBuscaLancamentosParciais(filtro);
		
		TableModel<CellModelKeyValue<ParcialDTO>> tableModel = new TableModel<CellModelKeyValue<ParcialDTO>>();

		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listaParciais));
		
		tableModel.setPage(filtro.getPaginacao().getPaginaAtual());
		
		tableModel.setTotal(totalRegistros);
		
		return tableModel;
	}
	
	/**
	 * Efetua a consulta e monta a estrutura do grid de parciais.
	 * @param filtro
	 * @return 
	 */	
	private TableModel<CellModelKeyValue<PeriodoParcialDTO>> efetuarConsultaPeriodos(FiltroParciaisDTO filtro) {
		
		List<PeriodoParcialDTO> listaPeriodo = periodoLancamentoParcialService.obterPeriodosParciais(filtro);
		
		Integer totalRegistros = periodoLancamentoParcialService.totalObterPeriodosParciais(filtro);
				
		TableModel<CellModelKeyValue<PeriodoParcialDTO>> tableModel = new TableModel<CellModelKeyValue<PeriodoParcialDTO>>();

		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listaPeriodo));
		
		tableModel.setPage(1);
		
		tableModel.setTotal(totalRegistros);
		
		return tableModel;
	}
	
	@Post
	public void pesquisarProdutoPorNumero(String codigoProduto) {
		
		if(codigoProduto == null) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Código do produto inválido!");
		}
		
		Produto produto = this.produtoService.obterProdutoPorCodigo(codigoProduto);

		if (produto == null) {

			throw new ValidacaoException(TipoMensagem.WARNING, "Produto \"" + codigoProduto + "\" não encontrado!");
			
		} else {
						
			result.use(Results.json()).withoutRoot().from(produto).recursive().serialize();
		}		
	}
	
	@Post
	public void pesquisarParciaisVenda(Date dtLcto, Date dtRcto, Long idProdutoEdicao, Long idPeriodo) {
	
	
		this.session.setAttribute(FILTRO_DATA_LANCAMENTO,dtLcto);
		
		this.session.setAttribute(FILTRO_DATA_RECEBIMENTO,dtRcto);
		
		this.session.setAttribute(FILTRO_ID_PRODUTO_EDICAO,idProdutoEdicao);
		
		List<ParcialVendaDTO> listaParcialVenda = this.parciaisService.obterDetalhesVenda(dtLcto, dtRcto, idProdutoEdicao, idPeriodo);
		
		TableModel<CellModelKeyValue<ParcialVendaDTO>> tableModel = new TableModel<CellModelKeyValue<ParcialVendaDTO>>();
		
		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listaParcialVenda));
		
		tableModel.setPage(1);
		
		tableModel.setTotal(listaParcialVenda.size());

		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
	}
	
	@Post
	public void obterPebDoProduto(String codigoProduto, String edicaoProduto, Integer periodos) {
		
		Integer pebProduto = parciaisService.calcularPebParcial(codigoProduto,Long.parseLong(edicaoProduto),periodos);
		
		result.use(Results.json()).withoutRoot().from(pebProduto).recursive().serialize();
	}
	
	
	/**
	 * Insere períodos ao Lançamento Parcial
	 */
	@Post
	@Rules(Permissao.ROLE_LANCAMENTO_PARCIAIS_ALTERACAO)
	public void gerarPeriodosParcias(Integer peb, Integer qtde, Long idProdutoEdicao) {
		
		parciaisService.gerarPeriodosParcias(idProdutoEdicao, qtde, getUsuarioLogado());
		
		result.use(Results.json()).withoutRoot().from("").recursive().serialize();		
	}
	
	/**
	 * Insere períodos ao Lançamento Parcial
	 */
	@Post
	@Rules(Permissao.ROLE_LANCAMENTO_PARCIAIS_ALTERACAO)
	public void inserirPeriodo(Date dataRecolhimento, Long idProdutoEdicao) {
		
		this.parciaisService.inserirNovoPeriodo(idProdutoEdicao, dataRecolhimento, getUsuarioLogado());
		
		ValidacaoVO successMessage = new ValidacaoVO(TipoMensagem.SUCCESS, "Período incluído com sucesso!");
		
		this.result.use(Results.json()).withoutRoot().from(successMessage).recursive().serialize();		
	}
	
	/**
	 * Excluir PeriodoLancamentoParcial por id do Lancamento
	 * 
	 * @param idLancamento
	 */
	@Post
	@Rules(Permissao.ROLE_LANCAMENTO_PARCIAIS_ALTERACAO)
	public void excluirPeriodoParcial(Long idLancamento) {
		
		parciaisService.excluirPeriodo(idLancamento);
		
		result.use(Results.json()).withoutRoot().from("").recursive().serialize();	
	}
	
	
	/**
	 * Insere períodos ao Lançamento Parcial
	 */
	@Post
	@Rules(Permissao.ROLE_LANCAMENTO_PARCIAIS_ALTERACAO)
	public void editarPeriodoParcial(Long idLancamento, String dataLancamento, String dataRecolhimento) {
		
		Date lancamento = DateUtil.parseDataPTBR(dataLancamento);
		Date recolhimento = DateUtil.parseDataPTBR(dataRecolhimento);
		
		if( lancamento == null )
			throw new ValidacaoException(TipoMensagem.WARNING, "Data de Lancaçmento não válida.");
		
		validarDatasPeriodoLancamento(lancamento, recolhimento);
				
		parciaisService.alterarPeriodo(idLancamento, lancamento, recolhimento, getUsuarioLogado());
		
		result.use(Results.json()).withoutRoot().from("").recursive().serialize();		
	}

	private void validarDatasPeriodoLancamento(Date lancamento, Date recolhimento) {
		
		if( recolhimento == null ){
			throw new ValidacaoException(TipoMensagem.WARNING, "Data de Recolhimento não válida.");
		}
		
		if(DateUtil.isDataInicialMaiorDataFinal(lancamento, recolhimento)) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Data de Lançamento é inferior a de Recolhimento");
		}	
	}
		
	/**
	 * Exporta os dados da pesquisa.
	 * 
	 * @param fileType - tipo de arquivo
	 * 
	 * @throws IOException Exceção de E/S
	 */
	@Get
	public void exportar(FileType fileType) throws IOException {
		
		FiltroParciaisDTO filtro = (FiltroParciaisDTO) session.getAttribute(FILTRO_SESSION);
		
		filtro.setPaginacao(null);
		
		List<ParcialDTO> listaParciais = lancamentoParcialService.buscarLancamentosParciais(filtro);
		
		if(listaParciais.isEmpty()) {
			throw new ValidacaoException(TipoMensagem.WARNING,"A última pesquisa realizada não obteve resultado.");
		}
		
		FileExporter.to("lancamentos_parciais", fileType).inHTTPResponse(this.getNDSFileHeader(), filtro, 
				listaParciais, ParcialDTO.class, this.httpResponse);
		
		result.nothing();
	}
	
	/**
	 * Exporta os dados da pesquisa de períodos.
	 * 
	 * @param fileType - tipo de arquivo
	 * 
	 * @throws IOException Exceção de E/S
	 */
	@Get
	public void exportarPeriodos(FileType fileType) throws IOException {
		
		FiltroParciaisDTO filtro = (FiltroParciaisDTO) session.getAttribute(FILTRO_SESSION_DETALHE);
		
		filtro.setPaginacao(null);
		
		List<PeriodoParcialDTO> listaPeriodos = periodoLancamentoParcialService.obterPeriodosParciais(filtro);
		
		if(listaPeriodos.isEmpty()) {
			throw new ValidacaoException(TipoMensagem.WARNING,"A última pesquisa realizada não obteve resultado.");
		}
		
		if(filtro.getStatus()!=null && !filtro.getStatus().trim().isEmpty()) {
			filtro.setStatus(StatusLancamentoParcial.valueOf(filtro.getStatus()).toString());
		}
		
		FileExporter.to("periodos_parciais", fileType).inHTTPResponse(this.getNDSFileHeader(), filtro, 
				listaPeriodos, PeriodoParcialDTO.class, this.httpResponse);
		
		result.nothing();
	}
	
	/**
	 * Exporta os dados da pesquisa de períodos.
	 * 
	 * @param fileType - tipo de arquivo
	 * 
	 * @throws IOException Exceção de E/S
	 */
	@Get
	public void exportarDetalhesVenda(FileType fileType, Long idPeriodo) throws IOException {
		
		List<ParcialVendaDTO> listaParcialVenda = new ArrayList<ParcialVendaDTO>();
		
		
		Date lcto = (Date) this.session.getAttribute(FILTRO_DATA_LANCAMENTO);
		
		Date recto = (Date) this.session.getAttribute(FILTRO_DATA_RECEBIMENTO);
		
		Long idProdutoEdicao = (Long) this.session.getAttribute(FILTRO_ID_PRODUTO_EDICAO);

		if ((lcto!=null) && (recto!=null) && (idProdutoEdicao!=null)){
		    listaParcialVenda = this.parciaisService.obterDetalhesVenda(lcto, recto, idProdutoEdicao, idPeriodo);
		}		
		
		if(listaParcialVenda.isEmpty()) {

			throw new ValidacaoException(TipoMensagem.WARNING,"A última pesquisa realizada não obteve resultado.");
		}

		FileExporter.to("detalhes_venda", fileType).inHTTPResponse(this.getNDSFileHeader(), null,
				listaParcialVenda, ParcialVendaDTO.class, this.httpResponse);

		result.nothing();
	}
	
	@Post
	@Rules(Permissao.ROLE_LANCAMENTO_PARCIAIS_ALTERACAO)
	public void pesquisarRedistribuicao(Long idPeriodo){
		
		List<RedistribuicaoParcialDTO> list = parciaisService.obterRedistribuicoesParciais(idPeriodo);
		
		result.use(FlexiGridJson.class).from(list).total(list.size()).page(1).serialize();
	}
	
	@Post
	@Rules(Permissao.ROLE_LANCAMENTO_PARCIAIS_ALTERACAO)
	public void incluirRedistribuicao(RedistribuicaoParcialDTO redistribuicaoDTO){

		parciaisService.incluirRedistribuicaoParcial(redistribuicaoDTO);
		
		result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Operação realizada com sucesso."),
				Constantes.PARAM_MSGS).recursive().serialize();		
	}
	
	@Post
	@Rules(Permissao.ROLE_LANCAMENTO_PARCIAIS_ALTERACAO)
	public void excluirRedistribuicao(Long idLancamentoRedistribuicao){
		
		parciaisService.excluirLancamentoParcial(idLancamentoRedistribuicao);
		
		result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Operação realizada com sucesso."),
				Constantes.PARAM_MSGS).recursive().serialize();		
	}
	
	@Post
	@Rules(Permissao.ROLE_LANCAMENTO_PARCIAIS_ALTERACAO)
	public void editarRedistribuicao(RedistribuicaoParcialDTO redistribuicaoDTO){
		
		parciaisService.salvarRedistribuicaoParcial(redistribuicaoDTO);
		
		result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Operação realizada com sucesso."),
				Constantes.PARAM_MSGS).recursive().serialize();
	}
	
	@Post
	@Rules(Permissao.ROLE_LANCAMENTO_PARCIAIS_ALTERACAO)
	public void validarDiaUtil(Date dataLancamento, Date dataRecolhimento){
		
		validarDatasPeriodoLancamento(dataLancamento, dataRecolhimento);
		
		String mensagemAlerta ="";
		
		if (!calendarioService.isDiaUtil(dataLancamento)){
			mensagemAlerta = "Data de lançamento não é um dia util deseja prosseguir ? ";
		}
		
		if (!calendarioService.isDiaUtil(dataRecolhimento)){
			
			if(mensagemAlerta.trim().isEmpty() ){
				mensagemAlerta = "Data de recolhimento não é um dia util deseja prosseguir ? ";
			}else{
				mensagemAlerta = "Data de lançamento e data de recolhimento não são dias uteis deseja prosseguir ? ";
			}
		}
		
		result.use(Results.json()).from(mensagemAlerta, "result").recursive().serialize();
	}
		
}
