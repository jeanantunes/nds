package br.com.abril.nds.controllers.estoque;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.controllers.BaseController;
import br.com.abril.nds.dto.ExtratoEdicaoDTO;
import br.com.abril.nds.dto.InfoGeralExtratoEdicaoDTO;
import br.com.abril.nds.dto.filtro.FiltroExtratoEdicaoDTO;
import br.com.abril.nds.dto.filtro.FiltroProdutoDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.service.ExtratoEdicaoService;
import br.com.abril.nds.service.integracao.DistribuidorService;
import br.com.abril.nds.util.CellModel;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.export.FileExporter;
import br.com.abril.nds.util.export.FileExporter.FileType;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.abril.nds.vo.ValidacaoVO;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

/**
 * Componente controller do extrato de edição.
 * 
 * @author michel.jader
 *
 */
@Resource
@Path("/estoque/extratoEdicao")
@Rules(Permissao.ROLE_ESTOQUE_EXTRATO_EDICAO)
public class ExtratoEdicaoController extends BaseController {
	
	private static final String GRID_RESULT = "gridResult";
	private static final String SALDO_TOTAL_EXTRATO_EDICAO = "saldoTotalExtratoEdicao";
	private static final String DESTACAR_SALDO_TOTAL_EXTRATO_EDICAO = "destacarSaldoTotalExtratoEdicao";
	private static final String FILTRO_PESQUISA_SESSION_ATTRIBUTE = "filtroPesquisaExtratoEdicao";
	
	@Autowired
	private ExtratoEdicaoService extratoEdicaoService;
	
	@Autowired
	private DistribuidorService distribuidorService;
	
	private HttpServletResponse response;
	
	private HttpSession session;
	
	private Result result;
	
	public ExtratoEdicaoController(Result result, 
								   HttpServletResponse response,
								   HttpSession session) {
		
		this.result = result;
		this.response = response;
		this.session = session;
	}
	
	@Path("/")
	public void index(){
		
		this.session.removeAttribute(FILTRO_PESQUISA_SESSION_ATTRIBUTE);
	}
	
	/**
	 * Obtem e serializa a razão social do fornecedor do produto.
	 * @param codigo
	 */
	public void obterFornecedorDeProduto(String codigo) {
		
		String resultado = "";
		
		if(codigo!=null && !codigo.trim().isEmpty()) {
		
			resultado = extratoEdicaoService.obterRazaoSocialFornecedorDeProduto(codigo);
			
			if(resultado == null) {
				resultado = "";
			}
			
		} 
		
		result.use(Results.json()).withoutRoot().from(resultado).serialize();
		
	}
	
	public void obterProdutoEdicao(FiltroProdutoDTO filtro, Long edicao) {
		
		String resultado = "";
		
		if(filtro.getCodigo() != null && !filtro.getCodigo().trim().isEmpty() && edicao != null) {
			
			ProdutoEdicao produtoEdicao = extratoEdicaoService.obterProdutoEdicao(filtro.getCodigo(), edicao);
		
			if(produtoEdicao!=null) {
				
				resultado = (produtoEdicao.getPrecoVenda()!=null) ? produtoEdicao.getPrecoVenda().toString() : "0.00";
				
			}
			
		}
		
		result.use(Results.json()).from(resultado, "result").serialize();
		
	}
	
	/**
	 * Pesquisa o Extrato da Edição e o serializa.
	 * 
	 * @throws Exception
	 */
	@Post
	@Path("/pesquisaExtratoEdicao")
	public void pesquisaExtratoEdicao(FiltroProdutoDTO filtro, 
									  Long numeroEdicao,
									  BigDecimal precoCapa,
									  int page, int rp) throws ValidacaoException {
		
		FiltroExtratoEdicaoDTO filtroExtratoEdicaoDTO = this.montarFiltro(filtro.getCodigo(), filtro.getNome(), numeroEdicao, precoCapa, filtro.getFornecedor(), page, rp);
		
		TableModel<CellModel> tableModel = null;
		
		Map<String, Object> resultado = new HashMap<String, Object>();
		
		List<String> listaWarningMsg = validarParametrosPesquisa(filtro.getCodigo(), numeroEdicao);
		
		if(!listaWarningMsg.isEmpty()) {
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, listaWarningMsg));
		}
		
		InfoGeralExtratoEdicaoDTO infoGeralExtratoEdicao = extratoEdicaoService.obterInfoGeralExtratoEdicao(filtroExtratoEdicaoDTO);
		
		if(	infoGeralExtratoEdicao == null || 
			infoGeralExtratoEdicao.getListaExtratoEdicao()==null ||
			infoGeralExtratoEdicao.getListaExtratoEdicao().isEmpty()) {

			throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum registro encontrado.");
			
		}
		
		tableModel = obterTableModelParaListaExtratoEdicao(infoGeralExtratoEdicao.getListaExtratoEdicao());
		
		String destacarValorSaldo = (infoGeralExtratoEdicao.getSaldoTotalExtratoEdicao().doubleValue() < 0.0D) ? "S" : "N";
		
		resultado.put(GRID_RESULT, tableModel);
		
		resultado.put(SALDO_TOTAL_EXTRATO_EDICAO, infoGeralExtratoEdicao.getSaldoTotalExtratoEdicao());
		
		resultado.put(DESTACAR_SALDO_TOTAL_EXTRATO_EDICAO, destacarValorSaldo);
		
		result.use(Results.json()).withoutRoot().from(resultado).recursive().serialize();
	}
	
	/**
	 * Exporta os dados da pesquisa.
	 * 
	 * @param fileType - tipo de arquivo
	 * 
	 * @throws IOException Exceção de E/S
	 */
	public void exportar(FileType fileType) throws IOException {
		
		FiltroExtratoEdicaoDTO filtro = 
			(FiltroExtratoEdicaoDTO) this.session.getAttribute(FILTRO_PESQUISA_SESSION_ATTRIBUTE);

		if (filtro == null) {
			result.redirectTo("index");
			return;
		}

		List<ExtratoEdicaoDTO> listaExtratoEdicao = null;
		
		InfoGeralExtratoEdicaoDTO infoGeralExtratoEdicao = null;
		
		infoGeralExtratoEdicao = 
			extratoEdicaoService.obterInfoGeralExtratoEdicao(
					filtro);

		if (	infoGeralExtratoEdicao == null || 
				infoGeralExtratoEdicao.getListaExtratoEdicao() == null ||
				infoGeralExtratoEdicao.getListaExtratoEdicao().isEmpty() ) {
			
			result.redirectTo("index");
			return;
			
		}
		
		listaExtratoEdicao = infoGeralExtratoEdicao.getListaExtratoEdicao();
		
		FileExporter.to("extrato-edicao", fileType)
			.inHTTPResponse(this.getNDSFileHeader(), filtro, infoGeralExtratoEdicao, 
				listaExtratoEdicao, ExtratoEdicaoDTO.class, this.response);
	}
	
	/*
	 * Monta o filtro com os dados da pesquisa.
	 * 
	 * @param codigoProduto - código do produto
	 * @param nomeProduto - nome do produto
	 * @param numeroEdicao - número da edição
	 * @param precoCapa - preço de capa
	 * @param nomeFornecedor - nome do fornecedor
	 */
	private FiltroExtratoEdicaoDTO montarFiltro(String codigoProduto,
											    String nomeProduto,
											    Long numeroEdicao,
											    BigDecimal precoCapa,
											    String nomeFornecedor, int page, int rp) {
		
		FiltroExtratoEdicaoDTO filtro = new FiltroExtratoEdicaoDTO();

		filtro.setCodigoProduto(codigoProduto);
		
		filtro.setNumeroEdicao(numeroEdicao);
		
		filtro.setNomeProduto(nomeProduto);
		
		filtro.setPrecoCapa(precoCapa);
		
		filtro.setNomeFornecedor(nomeFornecedor);
		
		PaginacaoVO vo = new PaginacaoVO();
		vo.setPaginaAtual(page);
		vo.setQtdResultadosPorPagina(rp);
		filtro.setPaginacao(vo);
		
		
		FiltroExtratoEdicaoDTO filtroSessao = (FiltroExtratoEdicaoDTO) this.session.getAttribute(FILTRO_PESQUISA_SESSION_ATTRIBUTE);
		if (filtroSessao != null && !filtroSessao.equals(filtro)) {
			
			filtro.getPaginacao().setPaginaAtual(1);
			
		}
		
		this.session.setAttribute(FILTRO_PESQUISA_SESSION_ATTRIBUTE, filtro);
		
		return filtro;
	}
	
	private List<String> validarParametrosPesquisa(String codigoProduto, Long numeroEdicao) {

		List<String> msgWarningValidacao = new LinkedList<String>();
		
		if(codigoProduto == null || codigoProduto.isEmpty() ) {
			msgWarningValidacao.add("O preenchimento do campo código é obrigatório!.");
		}
		
		if(numeroEdicao == null) {
			msgWarningValidacao.add("O preenchimento do campo edição é obrigatório!.");
		}
		
		return msgWarningValidacao;
		
	}

	private TableModel<CellModel> obterTableModelParaListaExtratoEdicao(List<ExtratoEdicaoDTO> listaExtratoEdicao) {

		TableModel<CellModel> tableModel = new TableModel<CellModel>();
		
		List<CellModel> listaModeloGenerico = new LinkedList<CellModel>();
		
		for(ExtratoEdicaoDTO extrato : listaExtratoEdicao) {
			
			int idMovimento				= (extrato.getIdMovimento() != null) ? extrato.getIdMovimento().intValue() : -1;
			String dataMovimento 		= DateUtil.formatarDataPTBR(extrato.getDataMovimento());
			String descTipoMovimento 	= extrato.getDescMovimento();
			String qtdEntrada 			= extrato.getQtdEdicaoEntrada() != null ? extrato.getQtdEdicaoEntrada().toString() :"";
			String qtdSaida 			= extrato.getQtdEdicaoSaida() != null ? extrato.getQtdEdicaoSaida().toString() : "";
			String qtdParcial 			= (extrato.getQtdParcial() != null) ? extrato.getQtdParcial().toString() : "";
			String destacarValor		= (extrato.getQtdParcial().doubleValue() < 0.0D) ? "S" : "N";
			
			listaModeloGenerico.add(new CellModel(idMovimento, dataMovimento, descTipoMovimento, qtdEntrada, qtdSaida, qtdParcial, destacarValor));
			
		}
		
		tableModel.setPage(1);
		tableModel.setTotal(listaModeloGenerico.size());
		tableModel.setRows(listaModeloGenerico);
		
		return tableModel;
		
	}
	
}