package br.com.abril.nds.controllers.devolucao;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.client.vo.ConsultaEncalheDetalheVO;
import br.com.abril.nds.client.vo.ConsultaEncalheVO;
import br.com.abril.nds.client.vo.ResultadoConsultaEncalheDetalheVO;
import br.com.abril.nds.client.vo.ResultadoConsultaEncalheVO;
import br.com.abril.nds.dto.ConsultaEncalheDTO;
import br.com.abril.nds.dto.ConsultaEncalheDetalheDTO;
import br.com.abril.nds.dto.InfoConsultaEncalheDTO;
import br.com.abril.nds.dto.InfoConsultaEncalheDetalheDTO;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaEncalheDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaEncalheDetalheDTO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.integracao.service.DistribuidorService;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.service.ConsultaEncalheService;
import br.com.abril.nds.service.CotaService;
import br.com.abril.nds.service.FornecedorService;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.TipoMensagem;
import br.com.abril.nds.util.Util;
import br.com.abril.nds.util.export.FileExporter;
import br.com.abril.nds.util.export.FileExporter.FileType;
import br.com.abril.nds.util.export.NDSFileHeader;
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
public class ConsultaEncalheController {

	@Autowired
	private FornecedorService fornecedorService;
	
	@Autowired
	private CotaService cotaService;
	
	@Autowired
	private Result result;
	
	@Autowired
	private HttpSession session;
	
	@Autowired
	private ConsultaEncalheService consultaEncalheService;
	
	@Autowired
	private HttpServletResponse httpResponse;
	
	private static final String FILTRO_SESSION_ATTRIBUTE = "filtroPesquisaConsultaEncalhe";
	
	private static final String FILTRO_DETALHE_SESSION_ATTRIBUTE = "filtroPesquisaConsultaEncalheDetalhe";
	
	@Autowired
	private DistribuidorService distribuidorService;
	
	private static final String SUFIXO_DIA = "º Dia";
	
	@Path("/")
	@Rules(Permissao.ROLE_RECOLHIMENTO_CONSULTA_ENCALHE_COTA)
	public void index(){
		
		carregarComboFornecedores();
		
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

		InfoConsultaEncalheDTO infoConsultaEncalhe = consultaEncalheService.pesquisarEncalhe(filtroConsultaEncalhe);

		List<ConsultaEncalheVO> listaConsultaEncalheVO =  getListaConsultaEncalheVO(infoConsultaEncalhe.getListaConsultaEncalhe());
		
		ResultadoConsultaEncalheVO resultadoPesquisa = new ResultadoConsultaEncalheVO();
		
		carregarResultadoConsultaEncalhe(resultadoPesquisa, infoConsultaEncalhe);
		
		FileExporter.to("consulta-encalhe", fileType).inHTTPResponse(
				this.getNDSFileHeader(), 
				filtroConsultaEncalhe, 
				resultadoPesquisa, 
				listaConsultaEncalheVO,
				ConsultaEncalheVO.class, this.httpResponse);
		
	}
	
	/*
	 * Obtém os dados do cabeçalho de exportação.
	 * 
	 * @return NDSFileHeader
	 */
	private NDSFileHeader getNDSFileHeader() {
		
		NDSFileHeader ndsFileHeader = new NDSFileHeader();
		
		Distribuidor distribuidor = this.distribuidorService.obter();
		
		if (distribuidor != null) {
			
			ndsFileHeader.setNomeDistribuidor(distribuidor.getJuridica().getRazaoSocial());
			ndsFileHeader.setCnpjDistribuidor(distribuidor.getJuridica().getCnpj());
		}
		
		ndsFileHeader.setData(new Date());
		
		ndsFileHeader.setNomeUsuario(this.getUsuario().getNome());
		
		return ndsFileHeader;
	}
	
	//TODO: não há como reconhecer usuario, ainda
	private Usuario getUsuario() {
		
		Usuario usuario = new Usuario();
		
		usuario.setId(1L);
		
		usuario.setNome("Jornaleiro da Silva");
		
		return usuario;
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
			
			mensagens.add("O preenchimento do perído é obrigatório");
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
	public void pesquisar(String dataRecolhimentoInicial, String dataRecolhimentoFinal, Long idFornecedor, Integer numeroCota,  String sortorder, String sortname, int page, int rp){
		
		FiltroConsultaEncalheDTO filtro = getFiltroConsultaEncalheDTO(dataRecolhimentoInicial,
				dataRecolhimentoFinal, idFornecedor, numeroCota);
		
		configurarPaginacaoPesquisa(filtro, sortorder, sortname, page, rp);
		
		tratarFiltro(filtro);
		
		efetuarPesquisa(filtro);
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
			throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum registro encontrado.");
		}
		
		Integer quantidadeRegistros = infoConsultaEncalhe.getQtdeConsultaEncalhe();
		
		List<ConsultaEncalheVO> listaResultadosVO = getListaConsultaEncalheVO(listaResultado);
		
		TableModel<CellModelKeyValue<ConsultaEncalheVO>> tableModel = new TableModel<CellModelKeyValue<ConsultaEncalheVO>>();

		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listaResultadosVO));
		
		tableModel.setTotal( (quantidadeRegistros!= null) ? quantidadeRegistros : 0);
		
		tableModel.setPage(filtro.getPaginacao().getPaginaAtual());
		
		ResultadoConsultaEncalheVO resultadoPesquisa = new ResultadoConsultaEncalheVO();
		
		resultadoPesquisa.setTableModel(tableModel);
		
		carregarResultadoConsultaEncalhe(resultadoPesquisa, infoConsultaEncalhe);
		
		result.use(Results.json()).withoutRoot().from(resultadoPesquisa).recursive().serialize();
	}
	
	/**
	 * Carrega os dados sumarizados da pesquisa no objeto ResultadoConsultaEncalheVO.
	 * 
	 * @param resultadoPesquisa
	 * @param infoConsultaEncalhe
	 */
	private void carregarResultadoConsultaEncalhe(ResultadoConsultaEncalheVO resultadoPesquisa, InfoConsultaEncalheDTO infoConsultaEncalhe) {
		
		String valorReparte = ( infoConsultaEncalhe.getValorReparte() != null ) ? infoConsultaEncalhe.getValorReparte().toString() : "0" ; 
		String valorEncalhe = ( infoConsultaEncalhe.getValorEncalhe() != null ) ? infoConsultaEncalhe.getValorEncalhe().toString() : "0" ;
		String valorVendaDia = ( infoConsultaEncalhe.getValorVendaDia() != null ) ? infoConsultaEncalhe.getValorVendaDia().toString() : "0" ;
		String valorDebitoCredito = ( infoConsultaEncalhe.getValorDebitoCredito() != null ) ? infoConsultaEncalhe.getValorDebitoCredito().toString() : "0" ;
		String valorPagar = ( infoConsultaEncalhe.getValorPagar() != null ) ? infoConsultaEncalhe.getValorPagar().toString() : "0" ;
		
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
	public void pesquisarDetalhe(Long idProdutoEdicao, Long idFornecedor, Long idCota, String dataRecolhimento, String dataMovimento, String sortorder, String sortname, int page, int rp) {
		
		FiltroConsultaEncalheDetalheDTO filtro = new FiltroConsultaEncalheDetalheDTO();
		
		filtro.setDataRecolhimento(DateUtil.parseData(dataRecolhimento, "dd/MM/yyyy"));
		filtro.setDataMovimento(DateUtil.parseData(dataMovimento, "dd/MM/yyyy"));
		filtro.setIdCota(idCota);
		filtro.setIdProdutoEdicao(idProdutoEdicao);
		
		configurarPaginacaoPesquisaDetalhe(filtro, sortorder, sortname, page, rp);
		
		tratarFiltroDetalhe(filtro);
		
		efetuarPesquisaDetalhe(filtro);
		
	}
	
	@Get
	@Path("/gerarSlip")
	public void gerarSlip(String dataRecolhimentoInicial, String dataRecolhimentoFinal, Long idFornecedor, Integer numeroCota) throws IOException {
		
		FiltroConsultaEncalheDTO filtro = getFiltroConsultaEncalheDTO(dataRecolhimentoInicial,
				dataRecolhimentoFinal, idFornecedor, numeroCota);
		
		byte[] slip =  consultaEncalheService.gerarDocumentosConferenciaEncalhe(filtro);
		
		if(slip != null) { 
			escreverArquivoParaResponse(slip, "slip");
		} else {
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, "Nenhum slip encontra."));
		}
		
	}

	private FiltroConsultaEncalheDTO getFiltroConsultaEncalheDTO(String dataRecolhimentoInicial,
			String dataRecolhimentoFinal, Long idFornecedor, Integer numeroCota) {
		
		if(idFornecedor == null || idFornecedor < 0) {
			idFornecedor = null;
		}
		
		Date dataRecDistribuidorInicial = validarDataRecolhimento(dataRecolhimentoInicial);
		Date dataRecDistribuidorFinal = validarDataRecolhimento(dataRecolhimentoFinal);
		
		FiltroConsultaEncalheDTO filtro = new FiltroConsultaEncalheDTO();
		
		filtro.setDataRecolhimentoInicial(dataRecDistribuidorInicial);
		filtro.setDataRecolhimentoFinal(dataRecDistribuidorFinal);
		
		filtro.setIdFornecedor(idFornecedor);
		
		if(numeroCota != null) {
			Cota cota  = cotaService.obterPorNumeroDaCota(numeroCota);
			if(cota!=null) {
				filtro.setIdCota(cota.getId());
			}
			
		} else {
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, "O preenchimento da cota é obrigatório."));
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
	 * 
	 * @return List - ConsultaEncalheVO
	 */
	private List<ConsultaEncalheVO> getListaConsultaEncalheVO( List<ConsultaEncalheDTO> listaConsultaEncalheDTO ) {
		
		List<ConsultaEncalheVO> listaResultadosVO = new ArrayList<ConsultaEncalheVO>();
		
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
			precoComDesconto 	= CurrencyUtil.formatarValor(consultaEncalheDTO.getPrecoComDesconto());
			reparte 			= getValorQtdeIntegerFormatado(consultaEncalheDTO.getReparte().intValue());
			encalhe 			= getValorQtdeIntegerFormatado(consultaEncalheDTO.getEncalhe().intValue());
			idFornecedor		= (consultaEncalheDTO.getIdFornecedor()!=null) ? consultaEncalheDTO.getIdFornecedor().toString() : "";
			idCota				= (consultaEncalheDTO.getIdCota()!=null) ? consultaEncalheDTO.getIdCota().toString() : "";
			fornecedor			= (consultaEncalheDTO.getFornecedor()!=null) ? consultaEncalheDTO.getFornecedor() : "";
			valor 				= CurrencyUtil.formatarValor(consultaEncalheDTO.getValor());
			valorComDesconto	= CurrencyUtil.formatarValor(consultaEncalheDTO.getValor());
			dataRecolhimento	= (consultaEncalheDTO.getDataDoRecolhimentoDistribuidor() != null) ? DateUtil.formatarDataPTBR(consultaEncalheDTO.getDataDoRecolhimentoDistribuidor()) : "" ;
			dataMovimento		= (consultaEncalheDTO.getDataMovimento() != null) ? DateUtil.formatarDataPTBR(consultaEncalheDTO.getDataMovimento()) : "" ;
			
			if(consultaEncalheDTO.getRecolhimento()<=0) {
				recolhimento = DateUtil.formatarDataPTBR(consultaEncalheDTO.getDataDoRecolhimentoDistribuidor());
			} else {
				recolhimento = (consultaEncalheDTO.getRecolhimento()!=null) ? (consultaEncalheDTO.getRecolhimento().toString() + SUFIXO_DIA) : "" ;
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
			
			listaResultadosVO.add(consultaEncalheVO);
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
		
		FiltroConsultaEncalheDTO filtro = 
				(FiltroConsultaEncalheDTO) this.session.getAttribute(FILTRO_SESSION_ATTRIBUTE);
		
		if (filtro != null) {
			
			if (filtro.getPaginacao() != null) {
				
				filtro.getPaginacao().setPaginaAtual(null);
				filtro.getPaginacao().setQtdResultadosPorPagina(null);
			}
			
			if(filtro.getIdCota() != null) {
				
				Cota cota = cotaService.obterPorId(filtro.getIdCota());
				
				if(cota!=null) {
					
					String nomeResp = cotaService.obterNomeResponsavelPorNumeroDaCota(cota.getNumeroCota());

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

		FiltroConsultaEncalheDTO filtroSession = 
				(FiltroConsultaEncalheDTO) session.getAttribute(FILTRO_SESSION_ATTRIBUTE);
		
		if (filtroSession != null && !filtroSession.equals(filtro)) {

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

		FiltroConsultaEncalheDetalheDTO filtroSession = 
				(FiltroConsultaEncalheDetalheDTO) session.getAttribute(FILTRO_DETALHE_SESSION_ATTRIBUTE);
		
		if (filtroSession != null && !filtroSession.equals(filtro)) {

			filtroSession.getPaginacao().setPaginaAtual(1);
		}
		
		session.setAttribute(FILTRO_DETALHE_SESSION_ATTRIBUTE, filtro);
	}
	
	/**
	 * Disponibiliza o arquivo para a realização do download.
	 * 
	 * @param arquivo
	 * @param nomeArquivo
	 * @throws IOException
	 */
	private void escreverArquivoParaResponse(byte[] arquivo, String nomeArquivo) throws IOException {
		
		this.httpResponse.setContentType("application/pdf");
		
		this.httpResponse.setHeader("Content-Disposition", "attachment; filename="+nomeArquivo +".pdf");

		OutputStream output = this.httpResponse.getOutputStream();
		
		output.write(arquivo);

		httpResponse.getOutputStream().close();
		
		result.use(Results.nothing());
		
	}

}
