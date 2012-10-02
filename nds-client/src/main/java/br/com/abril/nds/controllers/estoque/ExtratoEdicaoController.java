package br.com.abril.nds.controllers.estoque;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.dto.ExtratoEdicaoDTO;
import br.com.abril.nds.dto.InfoGeralExtratoEdicaoDTO;
import br.com.abril.nds.dto.filtro.FiltroExtratoEdicaoDTO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.integracao.service.DistribuidorService;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.service.ExtratoEdicaoService;
import br.com.abril.nds.util.CellModel;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.TipoMensagem;
import br.com.abril.nds.util.export.FileExporter;
import br.com.abril.nds.util.export.FileExporter.FileType;
import br.com.abril.nds.util.export.NDSFileHeader;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.abril.nds.vo.ValidacaoVO;
import br.com.caelum.vraptor.Path;
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
public class ExtratoEdicaoController {
	
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
	
	@Rules(Permissao.ROLE_ESTOQUE_EXTRATO_EDICAO)
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
	
	public void obterProdutoEdicao(String codigo, Long edicao) {
		
		String resultado = "";
		
		if(codigo!=null && !codigo.trim().isEmpty() && edicao != null) {
			
			ProdutoEdicao produtoEdicao = extratoEdicaoService.obterProdutoEdicao(codigo, edicao);
		
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
	public void pesquisaExtratoEdicao(String codigoProduto, 
									  Long numeroEdicao,
									  String nomeProduto,
									  BigDecimal precoCapa,
									  String nomeFornecedor, int page, int rp) throws ValidacaoException {
		
		FiltroExtratoEdicaoDTO filtroExtratoEdicaoDTO = this.montarFiltro(codigoProduto, nomeProduto, numeroEdicao, precoCapa, nomeFornecedor, page, rp);
		
		TableModel<CellModel> tableModel = null;
		
		Map<String, Object> resultado = new HashMap<String, Object>();
		
		List<String> listaWarningMsg = validarParametrosPesquisa(codigoProduto, numeroEdicao);
		
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
		
		this.session.setAttribute(FILTRO_PESQUISA_SESSION_ATTRIBUTE, filtro);
		
		return filtro;
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
			
			String dataMovimento 		= DateUtil.formatarDataPTBR(extrato.getDataMovimento());
			String descTipoMovimento 	= extrato.getDescMovimento();
			String qtdEntrada 			= extrato.getQtdEdicaoEntrada().doubleValue() < 0.0D ? "-" : extrato.getQtdEdicaoEntrada().toString();
			String qtdSaida 			= extrato.getQtdEdicaoSaida().doubleValue() < 0.0D ? "-" : extrato.getQtdEdicaoSaida().toString();
			String qtdParcial 			= extrato.getQtdParcial().toString();
			String destacarValor		= (extrato.getQtdParcial().doubleValue() < 0.0D) ? "S" : "N";
			
			listaModeloGenerico.add(new CellModel(extrato.getIdMovimento().intValue(), dataMovimento, descTipoMovimento, qtdEntrada, qtdSaida, qtdParcial, destacarValor));
			
		}
		
		tableModel.setPage(1);
		tableModel.setTotal(listaModeloGenerico.size());
		tableModel.setRows(listaModeloGenerico);
		
		return tableModel;
		
	}
	
}
