package br.com.abril.nds.controllers.devolucao;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.client.vo.ConsultaEncalheDetalheReparteVO;
import br.com.abril.nds.client.vo.ConsultaEncalheDetalheVO;
import br.com.abril.nds.client.vo.ConsultaEncalheVO;
import br.com.abril.nds.client.vo.ResultadoConsultaEncalheDetalheReparteVO;
import br.com.abril.nds.client.vo.ResultadoConsultaEncalheDetalheVO;
import br.com.abril.nds.client.vo.ResultadoConsultaEncalheVO;
import br.com.abril.nds.controllers.BaseController;
import br.com.abril.nds.dto.ConsultaEncalheDTO;
import br.com.abril.nds.dto.ConsultaEncalheDetalheDTO;
import br.com.abril.nds.dto.CotaReparteProdutoDTO;
import br.com.abril.nds.dto.InfoConsultaEncalheDTO;
import br.com.abril.nds.dto.InfoConsultaEncalheDetalheDTO;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaEncalheDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaEncalheDetalheDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.enums.TipoParametroSistema;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.TipoBox;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.service.BoxService;
import br.com.abril.nds.service.ChamadaEncalheService;
import br.com.abril.nds.service.ConsultaEncalheService;
import br.com.abril.nds.service.CotaService;
import br.com.abril.nds.service.FornecedorService;
import br.com.abril.nds.service.ProdutoEdicaoService;
import br.com.abril.nds.service.integracao.DistribuidorService;
import br.com.abril.nds.service.integracao.ParametroSistemaService;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.Constantes;
import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.Util;
import br.com.abril.nds.util.export.FileExporter;
import br.com.abril.nds.util.export.FileExporter.FileType;
import br.com.abril.nds.vo.DebitoCreditoCotaVO;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.abril.nds.vo.ValidacaoVO;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

/**
 * 
 * Classe responsável por controlar as ações da pagina de Consulta de Encalhe.
 * 
 * @author Discover Technology
 *
 */
@Resource
@Path(value="/devolucao/consultaEncalhe")
@Rules(Permissao.ROLE_RECOLHIMENTO_CONSULTA_ENCALHE_COTA)
public class ConsultaEncalheController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConsultaEncalheController.class);

	   
	@Autowired
	private FornecedorService fornecedorService;
	
	@Autowired
	private CotaService cotaService;
	
	@Autowired
	private ProdutoEdicaoService produtoEdicaoService;
	
	@Autowired
	private Result result;
	
	@Autowired
	private HttpSession session;
	
	@Autowired
	private ConsultaEncalheService consultaEncalheService;
	
	
	@Autowired
	private ChamadaEncalheService chamadaEncalheService;
	
	@Autowired
	private HttpServletResponse httpResponse;
	
	@Autowired
	private DistribuidorService distribuidorService;
	
	@Autowired
	private ParametroSistemaService parametroSistemaService;
	
	@Autowired
	private BoxService boxService;
	
	private static final String FILTRO_SESSION_ATTRIBUTE = "filtroPesquisaConsultaEncalhe";
	
	
	private static final String FILTRO_DETALHE_REPARTE_SESSION_ATTRIBUTE= "filtroPesquisaConsultaDetalheReparte";
	
	private static final String CONSULTA_ENCALHE_DETALHE_REPARTE_LISTA = "filtroPesquisaConsultaDetalheReparte_lista";
	
	
	private static final String FILTRO_DETALHE_SESSION_ATTRIBUTE = "filtroPesquisaConsultaEncalheDetalhe";
	

	private static final String SUFIXO_DIA = "º Dia";
	
	@Path("/")
	public void index(){
		
		carregarComboFornecedores();
		result.include("listaBoxes", carregarBoxes(boxService.buscarTodos(TipoBox.ENCALHE)));
		result.include("data", DateUtil.formatarDataPTBR(distribuidorService.obterDataOperacaoDistribuidor()));
		
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

		FiltroConsultaEncalheDTO filtroConsultaEncalhe = obterFiltroExportacao();
		
		if(filtroConsultaEncalhe == null) {
			result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.WARNING, "Efetuar pesquisa."),Constantes.PARAM_MSGS).recursive().serialize();
			throw new ValidacaoException(TipoMensagem.WARNING, "Efetuar Pesquisa.");
		}
		filtroConsultaEncalhe.setCodigoProduto(null);
		filtroConsultaEncalhe.setNumeroEdicao(null);
		filtroConsultaEncalhe.setIdProdutoEdicao(null);
		InfoConsultaEncalheDTO infoConsultaEncalhe = consultaEncalheService.pesquisarEncalhe(filtroConsultaEncalhe);

		List<ConsultaEncalheVO> listaConsultaEncalheVO =  getListaConsultaEncalheVO(infoConsultaEncalhe.getListaConsultaEncalhe(), filtroConsultaEncalhe);
		
		ResultadoConsultaEncalheVO resultadoPesquisa = new ResultadoConsultaEncalheVO();
		
		carregarResultadoConsultaEncalhe(resultadoPesquisa, infoConsultaEncalhe, filtroConsultaEncalhe);
		
		FileExporter.to("consulta-encalhe", fileType).inHTTPResponse(
				this.getNDSFileHeader(), 
				filtroConsultaEncalhe, 
				resultadoPesquisa, 
				listaConsultaEncalheVO,
				ConsultaEncalheVO.class, this.httpResponse);
		
	}
	
	
	
	/**
	 * Método responsável por carregar o combo de fornecedores.
	 */
	private void carregarComboFornecedores() {
		
		List<Fornecedor> listaFornecedor = fornecedorService.obterFornecedoresAtivos();
		
		List<ItemDTO<Long, String>> listaFornecedoresCombo = new ArrayList<ItemDTO<Long,String>>();
		
		for (Fornecedor fornecedor : listaFornecedor) {
			listaFornecedoresCombo.add(new ItemDTO<Long, String>(fornecedor.getId(), fornecedor.getJuridica().getRazaoSocial()));
		}
		
		result.include("listaFornecedores",listaFornecedoresCombo );
	}

	
	/**
	 * Trata mensagens de erro, caso tenha mensagem lança exceção de erro.
	 * 
	 * @param mensagensErro
	 */
	private void tratarErro(List<String> mensagensErro){
		
		ValidacaoVO validacao = new ValidacaoVO();
		
		validacao.setTipoMensagem(TipoMensagem.ERROR);
		
		if(!mensagensErro.isEmpty()){
			
			validacao.setListaMensagens(mensagensErro);
			
			throw new ValidacaoException(validacao);
			
		}
	}
	
	/**
	 * Valida a Data Recolhimento.
	 *  
	 * @param dataRecolhimento
	 * 
	 * @return Date - dataRecolhimento
	 */
	private Date validarDataRecolhimento(String dataRecolhimento) {
				
		tratarErro(validarPreenchimentoObrigatorio(dataRecolhimento));

		tratarErro(validarFormatoDataRecolhimento(dataRecolhimento));		
		
		return DateUtil.parseData(dataRecolhimento, "dd/MM/yyyy");

	}
	
	/**
	 * Valida o formato  da dataRecolhimento informada.
	 * 
	 * @param dataRecolhimento
	 *
	 * @return List<String>
	 */
	private List<String> validarFormatoDataRecolhimento(String dataRecolhimento){
		
		List<String> mensagens = new ArrayList<String>();
		
		if (!DateUtil.isValidDate(dataRecolhimento, "dd/MM/yyyy")) {
			
			mensagens.add("O campo Data de é inválido");
		} 
		
		
		return mensagens;
	}
	
	/**
	 * Valida o preenchimento obrigatório do campo informado.
	 * 
	 * @param dataRecolhimento
	 * 
	 * @return List<String>
	 */
	private List<String> validarPreenchimentoObrigatorio(String dataRecolhimento){
		
		 List<String> mensagens = new ArrayList<String>();
		
		if (dataRecolhimento == null || dataRecolhimento.isEmpty()) {
			
			mensagens.add("O preenchimento do período é obrigatório");
		} 
		
		return mensagens;
	}
	
	/**
	 * Configura paginação da lista de resultados
	 * 
	 * @param filtro
	 * @param sortorder
	 * @param sortname
	 * @param page
	 * @param rp
	 */
	private void configurarPaginacaoPesquisa(FiltroConsultaEncalheDTO filtro, 
											String sortorder, String sortname,
											int page, int rp) {

		if (filtro != null) {

			PaginacaoVO paginacao = new PaginacaoVO(page, rp, sortorder);

			filtro.setPaginacao(paginacao);

			filtro.setOrdenacaoColuna((Util.getEnumByStringValue(FiltroConsultaEncalheDTO.OrdenacaoColuna.values(),sortname)));
		}
	}
	
	
	private void configurarPaginacaoPesquisaReparte(FiltroConsultaEncalheDTO filtro, 
			String sortorder, String sortname,
			int page, int rp) {

	if (filtro != null) {
	
	PaginacaoVO paginacao = new PaginacaoVO(page, rp, sortorder);
	
	filtro.setPaginacao(paginacao);
	
	filtro.setOrdenacaoColuna((Util.getEnumByStringValue(FiltroConsultaEncalheDTO.OrdenacaoColuna.values(),sortname)));
	}
	}
	
	/**
	 * Configura paginação da lista de resultados
	 * 
	 * @param filtro
	 * @param sortorder
	 * @param sortname
	 * @param page
	 * @param rp
	 */
	private void configurarPaginacaoPesquisaDetalhe(FiltroConsultaEncalheDetalheDTO filtro, 
											String sortorder, String sortname,
											int page, int rp) {

		if (filtro != null) {

			PaginacaoVO paginacao = new PaginacaoVO(page, rp, sortorder);

			filtro.setPaginacao(paginacao);

			filtro.setOrdenacaoColunaDetalhe((Util.getEnumByStringValue(FiltroConsultaEncalheDetalheDTO.OrdenacaoColunaDetalhe.values(),sortname)));
		}
	}
	
	/**
	 * Faz a pesquisa de ConsultaEncalhe.
	 * 
	 * @param dataRecolhimento
	 * @param idFornecedor
	 * @param numeroCota
	 * @param sortorder
	 * @param sortname
	 * @param page
	 * @param rp
	 */
	@Post
	@Path("/pesquisar")
	public void pesquisar(String dataRecolhimentoInicial, String dataRecolhimentoFinal, Long idFornecedor, Integer numeroCota, Integer codigoProduto, Integer idBox, Integer numeroEdicao, String sortorder, String sortname, int page, int rp){
		
	  
		FiltroConsultaEncalheDTO filtro = getFiltroConsultaEncalheDTO(dataRecolhimentoInicial, dataRecolhimentoFinal, idFornecedor, numeroCota, codigoProduto, idBox, numeroEdicao);
		
		configurarPaginacaoPesquisa(filtro, sortorder, sortname, page, rp);
		
		tratarFiltro(filtro);
		
		efetuarPesquisa(filtro);
	}
	
	@Post
	@Path("/pesquisarDetalheReparte")
	public void pesquisarDetalheReparte(Long idProdutoEdicao, Long idFornecedor, Integer numeroCota, 
			String dataRecolhimento, String dataMovimento, String sortorder, String sortname, int page, int rp) {
		
		FiltroConsultaEncalheDTO filtro = ((FiltroConsultaEncalheDTO) this.session.getAttribute(FILTRO_SESSION_ATTRIBUTE));
		
		FiltroConsultaEncalheDTO filtro1 = new FiltroConsultaEncalheDTO();
		
		filtro1.setCodigoProduto(filtro.getCodigoProduto());
		filtro1.setDataRecolhimentoFinal(filtro.getDataRecolhimentoFinal());
		filtro1.setDataRecolhimentoInicial(filtro.getDataRecolhimentoInicial());
		filtro1.setIdBox(filtro.getIdBox());
		filtro1.setIdCota(filtro.getIdCota());
		filtro1.setIdFornecedor(filtro.getIdFornecedor());
	
		
		
		filtro1.setIdProdutoEdicao(idProdutoEdicao);
		
		configurarPaginacaoPesquisaReparte(filtro1, sortorder, sortname, page, rp);
		
		tratarFiltroReparte(filtro1);
		
		
		efetuarPesquisaReparte(filtro1);
	
		
		
		
	}
	
	

	/**
	 * Executa a pesquisa de consulta encalhe.
	 *  
	 * @param filtro
	 */
	private void efetuarPesquisa(FiltroConsultaEncalheDTO filtro) {
		
		InfoConsultaEncalheDTO infoConsultaEncalhe = consultaEncalheService.pesquisarEncalhe(filtro);
		
		List<ConsultaEncalheDTO> listaResultado = infoConsultaEncalhe.getListaConsultaEncalhe();
		
		if (listaResultado == null || listaResultado.isEmpty()) {
			  this.session.removeAttribute(FILTRO_DETALHE_SESSION_ATTRIBUTE);
				this.session.removeAttribute(FILTRO_SESSION_ATTRIBUTE);
			throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum registro encontrado.");
		}
		
		ResultadoConsultaEncalheVO resultadoPesquisa = new ResultadoConsultaEncalheVO();
		
		carregarResultadoConsultaEncalhe(resultadoPesquisa, infoConsultaEncalhe, filtro);

		Integer quantidadeRegistros = infoConsultaEncalhe.getQtdeConsultaEncalhe();
		
		List<ConsultaEncalheVO> listaResultadosVO = getListaConsultaEncalheVO(listaResultado, filtro);
		
		TableModel<CellModelKeyValue<ConsultaEncalheVO>> tableModel = new TableModel<CellModelKeyValue<ConsultaEncalheVO>>();

		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listaResultadosVO));
		
		tableModel.setTotal( (quantidadeRegistros!= null) ? quantidadeRegistros : 0);
		
		tableModel.setPage(filtro.getPaginacao().getPaginaAtual());
		
		resultadoPesquisa.setTableModel(tableModel);
		
		carregaListaDebitoCreditoCota(resultadoPesquisa, infoConsultaEncalhe.getListaDebitoCreditoCota());
		
		result.use(Results.json()).withoutRoot().from(resultadoPesquisa).recursive().serialize();
	}
	
	/**
	 * Carrega os dados sumarizados da pesquisa no objeto ResultadoConsultaEncalheVO.
	 * 
	 * @param resultadoPesquisa
	 * @param infoConsultaEncalhe
	 */
	private void carregarResultadoConsultaEncalhe(ResultadoConsultaEncalheVO resultadoPesquisa, 
												  InfoConsultaEncalheDTO infoConsultaEncalhe, 
												  FiltroConsultaEncalheDTO filtro) {
		
		String valorVendaDia = ( infoConsultaEncalhe.getValorVendaDia() != null ) ? infoConsultaEncalhe.getValorVendaDia().toString() : "0" ;
		String valorDebitoCredito = ( infoConsultaEncalhe.getValorDebitoCredito() != null ) ? infoConsultaEncalhe.getValorDebitoCredito().toString() : "0" ;
		String valorPagar = ( infoConsultaEncalhe.getValorPagar() != null ) ? infoConsultaEncalhe.getValorPagar().toString() : "0" ;
		String valorReparte = (infoConsultaEncalhe.getValorReparte() != null) ? infoConsultaEncalhe.getValorReparte().toString() : "0";
		String valorEncalhe = (infoConsultaEncalhe.getValorEncalhe() != null) ? infoConsultaEncalhe.getValorEncalhe().toString() : "0";

		resultadoPesquisa.setValorReparte(valorReparte);
		resultadoPesquisa.setValorEncalhe(valorEncalhe);
		resultadoPesquisa.setValorVendaDia(valorVendaDia);
		resultadoPesquisa.setValorDebitoCredito(valorDebitoCredito);
		resultadoPesquisa.setValorPagar(valorPagar);
		
	}
	
	/**
	 * Faz a pesquisa de ConsultaEncalheDetalhe.
	 * 
	 * @param dataOperacao
	 * @param idProdutoEdicao
	 * @param idFornecedor
	 * @param idCota
	 * @param sortorder
	 * @param sortname
	 * @param page
	 * @param rp
	 */
	@Post
	@Path("/pesquisarDetalhe")
	public void pesquisarDetalhe(Long idProdutoEdicao, Long idFornecedor, Integer numeroCota, 
			String dataRecolhimento, String dataMovimento, String sortorder, String sortname, int page, int rp) {
		
		FiltroConsultaEncalheDetalheDTO filtro = new FiltroConsultaEncalheDetalheDTO();
		
		filtro.setDataRecolhimento(DateUtil.parseData(dataRecolhimento, "dd/MM/yyyy"));
		filtro.setDataMovimento(DateUtil.parseData(dataMovimento, "dd/MM/yyyy"));
		filtro.setNumeroCota(numeroCota);
		filtro.setIdProdutoEdicao(idProdutoEdicao);
		
		configurarPaginacaoPesquisaDetalhe(filtro, sortorder, sortname, page, rp);
		
		tratarFiltroDetalhe(filtro);
		
		efetuarPesquisaDetalhe(filtro);
		
	}
	
	@Get
	@Path("/gerarSlip")
	public void gerarSlip(String dataRecolhimentoInicial, String dataRecolhimentoFinal, Long idFornecedor, Integer numeroCota, Integer codigoProduto, Integer idBox) throws IOException {
		
		
		FiltroConsultaEncalheDTO filtro = getFiltroConsultaEncalheDTO(dataRecolhimentoInicial,
				dataRecolhimentoFinal, idFornecedor, numeroCota, codigoProduto, idBox, null);

		
		
		byte[] slip =  consultaEncalheService.gerarDocumentosConferenciaEncalhe(filtro);
		
		if(slip != null) { 
			escreverArquivoParaResponse(slip, "slip");
		} else {
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, "Nenhum slip encontra."));
		}
		
	}

	private FiltroConsultaEncalheDTO getFiltroConsultaEncalheDTO(String dataRecolhimentoInicial, 
			String dataRecolhimentoFinal, 
			Long idFornecedor, 
			Integer numeroCota, 
			Integer codigoProduto,
			Integer idBox,
			Integer numeroEdicao) {
		
		if(idFornecedor == null || idFornecedor < 0) {
			idFornecedor = null;
		}
		
		Date dataRecDistribuidorInicial = validarDataRecolhimento(dataRecolhimentoInicial);
		Date dataRecDistribuidorFinal = validarDataRecolhimento(dataRecolhimentoFinal);
		
		FiltroConsultaEncalheDTO filtro = new FiltroConsultaEncalheDTO();
		
		filtro.setDataRecolhimentoInicial(dataRecDistribuidorInicial);
		
		filtro.setDataRecolhimentoFinal(dataRecDistribuidorFinal);
		
		filtro.setIdFornecedor(idFornecedor);
		
		filtro.setCodigoProduto(codigoProduto);
		
		filtro.setNumeroEdicao(numeroEdicao);
		
		filtro.setIdBox(idBox);
		
		if(numeroCota != null) {
			Cota cota  = cotaService.obterPorNumeroDaCota(numeroCota);
			filtro.setNumCota(numeroCota);
			
			if(cota!=null) {
				filtro.setIdCota(cota.getId());
			}
			
		}
		
		if(numeroEdicao != null) {
			
			if(codigoProduto == null) {
				throw new ValidacaoException(TipoMensagem.WARNING, "Favor informar o código do produto para efetuar a pesquisa.");
			}
			
			ProdutoEdicao produtoEdicao  = produtoEdicaoService.obterProdutoEdicaoPorCodProdutoNumEdicao(codigoProduto.toString(), numeroEdicao.toString());
			filtro.setNumeroEdicao(numeroEdicao);
			
			if(produtoEdicao!=null) {
				filtro.setIdProdutoEdicao(produtoEdicao.getId());
			}
		}
		
		return filtro;
	}

	/**
	 * Executa a pesquisa de consulta encalhe.
	 * 
	 * @param filtro
	 */
	private void efetuarPesquisaDetalhe(FiltroConsultaEncalheDetalheDTO filtro) {
		
		InfoConsultaEncalheDetalheDTO infoConsultaEncalhe = consultaEncalheService.pesquisarEncalheDetalhe(filtro);
		
		List<ConsultaEncalheDetalheDTO> listaResultado = infoConsultaEncalhe.getListaConsultaEncalheDetalhe();
		
		if (listaResultado == null || listaResultado.isEmpty()) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum registro encontrado.");
		}
		
		Integer quantidadeRegistros = infoConsultaEncalhe.getQtdeConsultaEncalheDetalhe();
		
		List<ConsultaEncalheDetalheVO> listaResultadosVO = getListaConsultaEncalheDetalheVO(listaResultado);
		
		TableModel<CellModelKeyValue<ConsultaEncalheDetalheVO>> tableModel = new TableModel<CellModelKeyValue<ConsultaEncalheDetalheVO>>();

		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listaResultadosVO));
		
		tableModel.setTotal( (quantidadeRegistros!= null) ? quantidadeRegistros : 0);
		
		tableModel.setPage(filtro.getPaginacao().getPaginaAtual());
		
		ResultadoConsultaEncalheDetalheVO resultadoPesquisa = new ResultadoConsultaEncalheDetalheVO();
		
		resultadoPesquisa.setTableModel(tableModel);
		
		carregarResultadoConsultaEncalheDetalhe(resultadoPesquisa, infoConsultaEncalhe);
		
		result.use(Results.json()).withoutRoot().from(resultadoPesquisa).recursive().serialize();
	}
	
	
	private void efetuarPesquisaReparte(FiltroConsultaEncalheDTO filtro) {
		
		 this.session.setAttribute(FILTRO_DETALHE_REPARTE_SESSION_ATTRIBUTE,filtro);
		
		InfoConsultaEncalheDTO infoConsultaEncalhe = consultaEncalheService.pesquisarReparte(filtro);
		
		List<ConsultaEncalheDTO> listaResultado1 = infoConsultaEncalhe.getListaConsultaEncalhe();
		
		if (listaResultado1 == null || listaResultado1.isEmpty()) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum registro encontrado.");
		}
		
		 List <CotaReparteProdutoDTO> listaResultado = new  ArrayList();
		for (ConsultaEncalheDTO ce: listaResultado1 ) {
			CotaReparteProdutoDTO lista = new CotaReparteProdutoDTO();
			lista.setReparte(ce.getReparte().longValue());
			lista.setEncalhe(ce.getEncalhe()!= null ? ce.getEncalhe().longValue():0L);
			lista.setIdBox(ce.getIdBox());
			lista.setNumeroCota(ce.getIdCota().intValue());
			lista.setNomeBox(ce.getNomeBox());
			lista.setNomeCota(ce.getNomeCota());
			listaResultado.add(lista);
		}
		
	
		if (listaResultado == null || listaResultado.isEmpty()) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum registro encontrado.");
		}
		
		Integer quantidadeRegistros = listaResultado.size();
		
		List<ConsultaEncalheDetalheReparteVO> listaResultadosVO = getListaConsultaEncalheDetalheReparteVO(listaResultado);
		
		TableModel<CellModelKeyValue<ConsultaEncalheDetalheReparteVO>> tableModel = new TableModel<CellModelKeyValue<ConsultaEncalheDetalheReparteVO>>();

		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listaResultadosVO));
		
		tableModel.setTotal( (quantidadeRegistros!= null) ? quantidadeRegistros : 0);
		
		tableModel.setPage( filtro.getPaginacao().getPaginaAtual());
		
		ResultadoConsultaEncalheDetalheReparteVO resultadoPesquisa = new ResultadoConsultaEncalheDetalheReparteVO();
		
		resultadoPesquisa.setTableModel(tableModel);
		
		 this.session.setAttribute(CONSULTA_ENCALHE_DETALHE_REPARTE_LISTA,listaResultadosVO);
	
		
		result.use(Results.json()).withoutRoot().from(resultadoPesquisa).recursive().serialize();
	}
	
	/**
	 * Carrega os dados de cabeçalho da pesquisa no objeto ResultadoConsultaEncalheDetalheVO.
	 * 
	 * @param resultadoPesquisa
	 * @param infoConsultaEncalheDetalhe
	 */
	private void carregarResultadoConsultaEncalheDetalhe(ResultadoConsultaEncalheDetalheVO resultadoPesquisa, InfoConsultaEncalheDetalheDTO infoConsultaEncalheDetalhe) {
		
		resultadoPesquisa.setDataOperacao(infoConsultaEncalheDetalhe.getDataOperacao());
		resultadoPesquisa.setCodigoProduto(infoConsultaEncalheDetalhe.getProdutoEdicao().getProduto().getCodigo());
		resultadoPesquisa.setNomeProduto(infoConsultaEncalheDetalhe.getProdutoEdicao().getProduto().getNome());
		resultadoPesquisa.setNumeroEdicao(infoConsultaEncalheDetalhe.getProdutoEdicao().getNumeroEdicao());
		
	}
	
	/**
	 * Obtém valor qtde formatada.
	 * 
	 * @param qtde
	 * 
	 * @return String
	 */
	private String getValorQtdeIntegerFormatado(Integer qtde) {
		return (qtde != null) ? qtde.toString() : "";
	}
		
	/**
	 * Obtém lista de ConsultaEncalheVO a partir de um lista de ConsultaEncalheDTO
	 * 
	 * @param listaConsultaEncalheDTO
	 * @param filtro 
	 * 
	 * @return List - ConsultaEncalheVO
	 */
	private List<ConsultaEncalheVO> getListaConsultaEncalheVO( List<ConsultaEncalheDTO> listaConsultaEncalheDTO, FiltroConsultaEncalheDTO filtro ) {
		
		List<ConsultaEncalheVO> listaResultadosVO = new ArrayList<ConsultaEncalheVO>();
		
		if (listaConsultaEncalheDTO == null || listaConsultaEncalheDTO.isEmpty()) {
			
			return listaResultadosVO;
		}
		
		ConsultaEncalheVO consultaEncalheVO = null;
		
		String idProdutoEdicao 		= null;
		String codigoProduto 		= null;
		String nomeProduto 			= null;
		String numeroEdicao 		= null;
		String precoVenda 			= null;
		String precoComDesconto 	= null;
		String reparte 				= null;
		String encalhe 				= null;
		String idFornecedor			= null;
		String idCota				= null;
		String fornecedor			= null;
		String valor 				= null;
		String valorComDesconto		= null;
		String recolhimento 		= null;
		String dataRecolhimento		= null;
		String dataMovimento		= null;
		
		for(ConsultaEncalheDTO consultaEncalheDTO : listaConsultaEncalheDTO) {
			
			idProdutoEdicao		= (consultaEncalheDTO.getIdProdutoEdicao() != null) ? consultaEncalheDTO.getIdProdutoEdicao().toString() : "";
			codigoProduto 		= (consultaEncalheDTO.getCodigoProduto() != null) ? consultaEncalheDTO.getCodigoProduto() : "";
			nomeProduto 		= (consultaEncalheDTO.getNomeProduto() != null) ? consultaEncalheDTO.getNomeProduto() : "";
			numeroEdicao 		= (consultaEncalheDTO.getNumeroEdicao() != null) ? consultaEncalheDTO.getNumeroEdicao().toString() : "";
			precoVenda 			= CurrencyUtil.formatarValor(consultaEncalheDTO.getPrecoVenda());
			precoComDesconto 	= CurrencyUtil.formatarValorQuatroCasas(consultaEncalheDTO.getPrecoComDesconto());
			reparte 			= getValorQtdeIntegerFormatado(consultaEncalheDTO.getReparte() == null ? 0 : consultaEncalheDTO.getReparte().intValue());
			encalhe 			= getValorQtdeIntegerFormatado(consultaEncalheDTO.getEncalhe() == null ? 0 : consultaEncalheDTO.getEncalhe().intValue());
			idFornecedor		= (consultaEncalheDTO.getIdFornecedor()!=null) ? consultaEncalheDTO.getIdFornecedor().toString() : "";
			idCota				= (consultaEncalheDTO.getIdCota()!=null) ? consultaEncalheDTO.getIdCota().toString() : "";
			fornecedor			= (consultaEncalheDTO.getFornecedor()!=null) ? consultaEncalheDTO.getFornecedor() : "";
			valor 				= CurrencyUtil.formatarValor(consultaEncalheDTO.getValor());
			valorComDesconto	= CurrencyUtil.formatarValorQuatroCasas(consultaEncalheDTO.getValorComDesconto());
			dataRecolhimento	= (consultaEncalheDTO.getDataDoRecolhimentoDistribuidor() != null) ? DateUtil.formatarDataPTBR(consultaEncalheDTO.getDataDoRecolhimentoDistribuidor()) : "" ;
			dataMovimento		= (consultaEncalheDTO.getDataMovimento() != null) ? DateUtil.formatarDataPTBR(consultaEncalheDTO.getDataMovimento()) : "" ;
			
			boolean diaUnico = DateUtils.isSameDay(filtro.getDataRecolhimentoInicial(), filtro.getDataRecolhimentoFinal());
			
			if( !diaUnico ) {
				
				if(consultaEncalheDTO.getDataDoRecolhimentoDistribuidor()!=null) {
					
					recolhimento = DateUtil.formatarDataPTBR(consultaEncalheDTO.getDataDoRecolhimentoDistribuidor());
					
				} else {
					
					recolhimento = "";
					
				}
			
			} else {
				
				if(consultaEncalheDTO.getRecolhimento() != null) {
					
					recolhimento = consultaEncalheDTO.getRecolhimento().toString() + SUFIXO_DIA;
					
				} else {
					
					recolhimento = "";
				}
			}
			
			consultaEncalheVO = new ConsultaEncalheVO();
			
			consultaEncalheVO.setIdProdutoEdicao(idProdutoEdicao);
			consultaEncalheVO.setCodigoProduto(codigoProduto);
			consultaEncalheVO.setNomeProduto(nomeProduto);
			consultaEncalheVO.setNumeroEdicao(numeroEdicao);
			consultaEncalheVO.setPrecoVenda(precoVenda);
			consultaEncalheVO.setPrecoComDesconto(precoComDesconto);
			consultaEncalheVO.setReparte(reparte);
			consultaEncalheVO.setEncalhe(encalhe);
			consultaEncalheVO.setIdCota(idCota);
			consultaEncalheVO.setIdFornecedor(idFornecedor);
			consultaEncalheVO.setFornecedor(fornecedor);
			consultaEncalheVO.setValor(valor);
			consultaEncalheVO.setValorComDesconto(valorComDesconto);
			consultaEncalheVO.setRecolhimento(recolhimento);
			consultaEncalheVO.setDataMovimento(dataMovimento);
			consultaEncalheVO.setDataRecolhimento(dataRecolhimento);
			consultaEncalheVO.setIndPossuiObservacaoConferenciaEncalhe(consultaEncalheDTO.getIndPossuiObservacaoConferenciaEncalhe());
			listaResultadosVO.add(consultaEncalheVO);
		}
		
		return listaResultadosVO;
	}
	
	private List<ConsultaEncalheDetalheReparteVO> getListaConsultaEncalheDetalheReparteVO(List<CotaReparteProdutoDTO> listaConsultaEncalheDetalheDTO ) {
		
		List<ConsultaEncalheDetalheReparteVO> listaResultadosVO = new ArrayList<ConsultaEncalheDetalheReparteVO>();
		
		ConsultaEncalheDetalheReparteVO consultaEncalheDetalheReparteVO = null;
		
	
		
		
		for(CotaReparteProdutoDTO consultaEncalheDetalheDTO: listaConsultaEncalheDetalheDTO) {
			
			
			
			consultaEncalheDetalheReparteVO = new ConsultaEncalheDetalheReparteVO(); 
			
			consultaEncalheDetalheReparteVO.setNumeroCota(consultaEncalheDetalheDTO.getNumeroCota().toString());
			consultaEncalheDetalheReparteVO.setNomeCota(consultaEncalheDetalheDTO.getNomeCota());
			consultaEncalheDetalheReparteVO.setReparte(consultaEncalheDetalheDTO.getReparte().longValue());
			consultaEncalheDetalheReparteVO.setEncalhe(consultaEncalheDetalheDTO.getEncalhe().longValue());
			consultaEncalheDetalheReparteVO.setIdBox(consultaEncalheDetalheDTO.getIdBox());
			consultaEncalheDetalheReparteVO.setNomeBox(consultaEncalheDetalheDTO.getNomeBox());
			listaResultadosVO.add(consultaEncalheDetalheReparteVO);
		}
		
		return listaResultadosVO;
	}
	
	
private List<ConsultaEncalheDetalheVO> getListaConsultaEncalheDetalheVO(List<ConsultaEncalheDetalheDTO> listaConsultaEncalheDetalheDTO ) {
		
		List<ConsultaEncalheDetalheVO> listaResultadosVO = new ArrayList<ConsultaEncalheDetalheVO>();
		
		ConsultaEncalheDetalheVO consultaEncalheDetalheVO = null;
		
		String numeroCota = null;
		String nomeCota = null;
		String observacao = null;
		
		for(ConsultaEncalheDetalheDTO consultaEncalheDetalheDTO: listaConsultaEncalheDetalheDTO) {
			
			numeroCota = (consultaEncalheDetalheDTO.getNumeroCota() != null) ? consultaEncalheDetalheDTO.getNumeroCota().toString() : "";
			nomeCota = (consultaEncalheDetalheDTO.getNomeCota() != null) ? consultaEncalheDetalheDTO.getNomeCota() : "";
			observacao = (consultaEncalheDetalheDTO.getObservacao() != null) ? consultaEncalheDetalheDTO.getObservacao() : "";
			
			consultaEncalheDetalheVO = new ConsultaEncalheDetalheVO(); 
			
			consultaEncalheDetalheVO.setNumeroCota(numeroCota);
			consultaEncalheDetalheVO.setNomeCota(nomeCota);
			consultaEncalheDetalheVO.setObservacao(observacao);
			
			listaResultadosVO.add(consultaEncalheDetalheVO);
		}
		
		return listaResultadosVO;
	}

	/*
	 * Obtém o filtro para exportação.
	 */
	private FiltroConsultaEncalheDTO obterFiltroExportacao() {
		
		FiltroConsultaEncalheDTO filtro = (FiltroConsultaEncalheDTO) this.session.getAttribute(FILTRO_SESSION_ATTRIBUTE);
		
		if (filtro != null) {
			
			if (filtro.getPaginacao() != null) {
				
				filtro.getPaginacao().setPaginaAtual(null);
				filtro.getPaginacao().setQtdResultadosPorPagina(null);
			}
			
			if(filtro.getIdCota() != null) {
				
				Cota cota = cotaService.obterPorId(filtro.getIdCota());
				
				if(cota!=null) {
					
					String nomeResp = cotaService.obterNomeResponsavelPorNumeroDaCota(cota.getNumeroCota());
					filtro.setNumCota(cota.getNumeroCota());
					filtro.setNomeCota(nomeResp);
					
				}
				
			}
			
			if (filtro.getIdFornecedor() != null && filtro.getIdFornecedor()>0) {
				
				Fornecedor fornecedor = this.fornecedorService.obterFornecedorPorId(filtro.getIdFornecedor());
				
				if (fornecedor != null) {
					
					PessoaJuridica juridica = fornecedor.getJuridica();
					
					if(juridica!=null) {
						filtro.setNomeFornecedor(juridica.getRazaoSocial());
					}
				}
			} else {
				filtro.setNomeFornecedor("TODOS");
			}
		}
		
		return filtro;
	}
	
	/**
	 * Executa tratamento de paginação em função de alteração do filtro de pesquisa.
	 * 
	 * @param filtro
	 */
	private void tratarFiltro(FiltroConsultaEncalheDTO filtro) {

		FiltroConsultaEncalheDTO filtroSession = (FiltroConsultaEncalheDTO) session.getAttribute(FILTRO_SESSION_ATTRIBUTE);
		
		if (filtroSession != null && !filtroSession.equals(filtro) && filtroSession.getPaginacao() != null) {

			filtroSession.getPaginacao().setPaginaAtual(1);
		}
		
		session.setAttribute(FILTRO_SESSION_ATTRIBUTE, filtro);
	}
	
	
	private void tratarFiltroReparte(FiltroConsultaEncalheDTO filtro) {

		FiltroConsultaEncalheDTO filtroSession = (FiltroConsultaEncalheDTO) session.getAttribute(FILTRO_DETALHE_REPARTE_SESSION_ATTRIBUTE);
		
		if (filtroSession != null && !filtroSession.equals(filtro) && filtroSession.getPaginacao() != null) {

			filtroSession.getPaginacao().setPaginaAtual(1);
		}
		
		session.setAttribute(FILTRO_SESSION_ATTRIBUTE, filtro);
	}
	
	
	/**
	 * Executa tratamento de paginação em função de alteração do filtro de pesquisa.
	 * 
	 * @param filtro
	 */
	private void tratarFiltroDetalhe(FiltroConsultaEncalheDetalheDTO filtro) {

		FiltroConsultaEncalheDetalheDTO filtroSession = (FiltroConsultaEncalheDetalheDTO) session.getAttribute(FILTRO_DETALHE_SESSION_ATTRIBUTE);
		
		if (filtroSession != null && !filtroSession.equals(filtro)) {

			filtroSession.getPaginacao().setPaginaAtual(1);
		}
		
		session.setAttribute(FILTRO_DETALHE_SESSION_ATTRIBUTE, filtro);
	}
	
	
	
	
	
	
	 /**
     * Delete older files in a directory until only those matching the given
     * constraints remain.
     *
     * @param minCount Always keep at least this many files.
     * @param minAge Always keep files younger than this age.
     * @return if any files were deleted.
     */
    public  boolean deleteOlderFiles(File dir,  long minAge) {
       
        final File[] files = dir.listFiles();
        if (files == null) return false;
        // Sort with newest files first
        Arrays.sort(files, new Comparator<File>() {
            @Override
            public int compare(File lhs, File rhs) {
                return (int) (rhs.lastModified() - lhs.lastModified());
            }
        });
        // Keep at least minCount files
        boolean deleted = false;
        for (int i = 0; i < files.length; i++) {
            final File file = files[i];
            // Keep files newer than minAge
            final long age = System.currentTimeMillis() - file.lastModified();
            if (age > minAge) {
                if (file.delete()) {
                    LOGGER.warn( "Deleted old file " + file);
                    deleted = true;
                }
            }
        }
        return deleted;
    }
	
	/**
	 * Disponibiliza o arquivo para a realização do download.
	 * 
	 * @param arquivo
	 * @param nomeArquivo
	 * @throws IOException
	 */
	private void escreverArquivoParaResponse(byte[] arquivo, String nomeArquivo) throws IOException {
		
		
		// gravar arquivo no diretorio do web ftp
	try {	
		String dirBanca = parametroSistemaService.buscarParametroPorTipoParametro(TipoParametroSistema.PATH_INTERFACE_BANCAS_EXPORTACAO).getValor();
		
		String path  = dirBanca+"/../slip/slip"+DateUtil.formatarData(new Date(),"ddMMyyyyHHmmss")+".pdf";
		File fslip = new File(dirBanca+"/../slip") ;
		if ( !fslip.exists() )
			fslip.mkdir();
   	 
		FileOutputStream arq = new FileOutputStream(path);
   	   
		arq.write(arquivo);
		
		arq.close();
		
		// remover arquivos com mais de 7 dias 
		
		deleteOlderFiles(fslip,7*24*60*60*1000);
	} catch (Exception e) {
		LOGGER.error("Erro gravando arquivo slip no diretorio /opt/ambiente1/parametros_nds/slip ");
	}
		
		this.httpResponse.setContentType("application/pdf");
		
		this.httpResponse.setHeader("Content-Disposition", "attachment; filename="+nomeArquivo +".pdf");

		OutputStream output = this.httpResponse.getOutputStream();
		
		output.write(arquivo);

		httpResponse.getOutputStream().close();
		
		result.use(Results.nothing());
		
	}
	
	/**
	 * Carrega a lista de debitos e créditos [List<DebitoCreditoCotaDTO>] para o obejto ResultadoConsultaEncalheVO.
	 * 
	 * @param resultadoConsultaEncalheVO
	 * @param listaDebitoCreditoCota
	 */
	private void carregaListaDebitoCreditoCota(ResultadoConsultaEncalheVO resultadoConsultaEncalheVO, List<DebitoCreditoCotaVO> listaDebitoCreditoCota) {
	
		resultadoConsultaEncalheVO.setTableModelDebitoCredito(new TableModel<CellModelKeyValue<DebitoCreditoCotaVO>>());
		
		resultadoConsultaEncalheVO.getTableModelDebitoCredito().setRows(CellModelKeyValue.toCellModelKeyValue(listaDebitoCreditoCota));
		resultadoConsultaEncalheVO.getTableModelDebitoCredito().setTotal((listaDebitoCreditoCota!= null) ? listaDebitoCreditoCota.size() : 0);
		resultadoConsultaEncalheVO.getTableModelDebitoCredito().setPage(1);
		
	}

	/**
	 * Carrega a lista de Boxes
	 * @return 
	 */
	private List<ItemDTO<Integer, String>> carregarBoxes(List<Box> listaBoxes){
		
		sortByCodigo(listaBoxes);
		
		List<ItemDTO<Integer, String>> boxes = new ArrayList<ItemDTO<Integer,String>>();
				
		for(Box box : listaBoxes){
			
			boxes.add(new ItemDTO<Integer, String>(box.getCodigo(),box.getCodigo() + " - " + box.getNome()));
		}
		
		return boxes;			
	}
	

	private void sortByCodigo(List<Box> listaBoxes) {
		Collections.sort(listaBoxes, new Comparator<Box>() {
			@Override
			public int compare(Box box1, Box box2) {
				if(box1.getCodigo()==null)
					return -1;
				return box1.getCodigo().compareTo(box2.getCodigo());
			}
		});
	}
	
	
	
		
		
	@Get
	public void exportarDetalhe(FileType fileType) throws IOException {

		FiltroConsultaEncalheDTO filtro = (FiltroConsultaEncalheDTO) this.session.getAttribute(FILTRO_DETALHE_REPARTE_SESSION_ATTRIBUTE);
		
		if(filtro == null) {
			result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.WARNING, "Nenhum registro encontrado"),Constantes.PARAM_MSGS).recursive().serialize();
			throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum registro encontrado");
		}
		
		
		ProdutoEdicao pe = produtoEdicaoService.buscarPorID(filtro.getIdProdutoEdicao());
		if ( pe != null ) {
			filtro.setCodigoProduto(Integer.parseInt(pe.getProduto().getCodigo()));
			filtro.setNumeroEdicao(pe.getNumeroEdicao().intValue());
		}
		
		List <ConsultaEncalheDetalheReparteVO> listaConsultaEncalheVO = (List <ConsultaEncalheDetalheReparteVO>) this.session.getAttribute(CONSULTA_ENCALHE_DETALHE_REPARTE_LISTA);
		
		if(listaConsultaEncalheVO == null|| listaConsultaEncalheVO.size() == 0) {
			result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.WARNING, "Nenhum registro encontrado"),Constantes.PARAM_MSGS).recursive().serialize();
			throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum registro encontrado");
		}
		
		FileExporter.to("consulta-encalhe-detalhe", fileType).inHTTPResponse(
				this.getNDSFileHeader(), 
				filtro, 
				null,
				listaConsultaEncalheVO,
				ConsultaEncalheDetalheReparteVO.class, this.httpResponse);
		
	}

}