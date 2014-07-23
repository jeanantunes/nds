package br.com.abril.nds.controllers.cadastro;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.client.vo.BaseComboVO;
import br.com.abril.nds.client.vo.FiltroValeDescontoVO;
import br.com.abril.nds.client.vo.ValeDescontoVO;
import br.com.abril.nds.client.vo.ValeDescontoVO.PublicacaoCuponada;
import br.com.abril.nds.controllers.BaseController;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Editor;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.serialization.custom.FlexiGridJson;
import br.com.abril.nds.service.EditorService;
import br.com.abril.nds.service.FornecedorService;
import br.com.abril.nds.service.ProdutoEdicaoService;
import br.com.abril.nds.service.ValeDescontoService;
import br.com.abril.nds.util.ItemAutoComplete;
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
@Rules(Permissao.ROLE_CADASTRO_VALE_DESCONTO)
@Path("/cadastro/valeDesconto")
public class ValeDescontoController extends BaseController {	
	
	@Autowired
	private Result result;
	
	@Autowired
	private HttpServletResponse httpServletResponse;

	@Autowired
	private HttpSession session;
	
	@Autowired
	private ValeDescontoService valeDescontoService;
	
	@Autowired
	private ProdutoEdicaoService produtoEdicaoService;
	
	@Autowired
	private EditorService editorService;
	
	@Autowired
	private FornecedorService fornecedorService;
	
	private static final String FILTRO_SESSION_ATTRIBUTE = "filtroValeDesconto";
	
	@Path("/")
	public void index() {
		
		this.carregarCombos();
	}
	
	private void carregarCombos() {
		
		this.result.include("editores", parseComboEditor(this.editorService.obterEditoresDesc()));
		this.result.include("fornecedores", parseComboFornecedor(this.fornecedorService.obterFornecedoresDesc()));
		this.result.include("situacoes", parseComboSituacoes());
	}
	
	/**
	 * Parse para combo.
	 * 
	 * @param listaEditor
	 * @return
	 */
	private List<BaseComboVO> parseComboEditor(List<Editor> listaEditor) {
		
		List<BaseComboVO> listaBaseComboVO = new ArrayList<BaseComboVO>();
		
		listaBaseComboVO.add(new BaseComboVO(0L, ""));
		
		for (Editor editor : listaEditor) {
			listaBaseComboVO.add(new BaseComboVO(editor.getId(), editor.getPessoaJuridica().getNome()));
		}
		
		return listaBaseComboVO;
	}
	
	/**
	 * Parse para combo.
	 * 
	 * @param listaFornecedor
	 * @return
	 */
	private List<BaseComboVO> parseComboFornecedor(List<Fornecedor> listaFornecedor) {
		
		List<BaseComboVO> listaBaseComboVO = new ArrayList<BaseComboVO>();

		listaBaseComboVO.add(new BaseComboVO(0L, ""));
		
		for (Fornecedor fornecedor : listaFornecedor) {
			listaBaseComboVO.add(new BaseComboVO(fornecedor.getId(), fornecedor.getJuridica().getNomeFantasia()));
		}
		
		return listaBaseComboVO;
	}
	
	private List<StatusLancamento> parseComboSituacoes() {
		
		List<StatusLancamento> listaStatusLancamento = new ArrayList<StatusLancamento>();

		listaStatusLancamento.add(StatusLancamento.CONFIRMADO);
		listaStatusLancamento.add(StatusLancamento.PLANEJADO);
		listaStatusLancamento.add(StatusLancamento.EM_BALANCEAMENTO_RECOLHIMENTO);
		listaStatusLancamento.add(StatusLancamento.BALANCEADO_RECOLHIMENTO);
		listaStatusLancamento.add(StatusLancamento.EM_RECOLHIMENTO);
		listaStatusLancamento.add(StatusLancamento.RECOLHIDO);
		listaStatusLancamento.add(StatusLancamento.FECHADO);

		return listaStatusLancamento;
	}

	@Post
	@Path("/pesquisar")
	public void pesquisar(FiltroValeDescontoVO filtro, String sortname, String sortorder, int page, int rp) {

		this.configurarFiltro(filtro, sortname, sortorder, page, rp);
		
		Long total = this.valeDescontoService.obterCountPesquisaPorFiltro(filtro);
		
		if (total == null || total.equals(BigInteger.ZERO)) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum registro encontrado.");
		}
		
		List<ValeDescontoVO> valesDesconto = this.valeDescontoService.obterPorFiltro(filtro);
		
		this.result.use(FlexiGridJson.class).from(valesDesconto).page(page).total(total.intValue()).serialize();
	}
	
	@Get
	@Path("/obterPorId")
	public void obterValeDescontoPorId(Long id) {
		
		ValeDescontoVO valeDesconto = this.valeDescontoService.obterPorId(id);
		
		if (valeDesconto == null) {
			
			this.result.nothing();

		} else {
			
			this.result.use(Results.json()).withoutRoot().from(valeDesconto).recursive().serialize();
		}	
	}
	
	@Get
	@Path("/obterPorCodigo")
	public void obterValeDescontoPorCodigo(String codigo) {
		
		ValeDescontoVO valeDesconto = this.valeDescontoService.obterPorCodigo(codigo);
		
		if (valeDesconto == null) {
			
			this.result.nothing();

		} else {
			
			this.result.use(Results.json()).withoutRoot().from(valeDesconto).recursive().serialize();
		}		
	}
	
	@Get
	@Path("/sugerirProximaEdicao")
	public void sugerirProximaEdicao(String codigo) {
		
		ValeDescontoVO valeDesconto = this.valeDescontoService.sugerirProximaEdicao(codigo);
		
		this.result.use(Results.json()).withoutRoot().from(valeDesconto).recursive().serialize();
	}
	
	@Post
	@Path("/salvar")
	public void salvar(ValeDescontoVO valeDesconto) {
		
		this.valeDescontoService.salvar(valeDesconto.toValeDesconto());
		
		this.result.use(Results.json()).from(
				new ValidacaoVO(TipoMensagem.SUCCESS, "Vale Desconto cadastrado com sucesso!"), "mensagens").recursive().serialize();
	}
	
	@Post
	@Path("/remover")
	public void remover(Long id) {
		
		this.valeDescontoService.remover(id);
		
		this.result.use(Results.json()).from(
				new ValidacaoVO(TipoMensagem.SUCCESS, "Vale Desconto removido com sucesso!"), "mensagens").recursive().serialize();
	}
	
	@Get
	@Path("/autoCompleteValesDesconto")
	public void autoCompleteValesDesconto(String filtro) {
		
		List<ValeDescontoVO> valesDesconto = this.valeDescontoService.obterPorCodigoOuNome(filtro);

		List<ItemAutoComplete> autocomplete = new ArrayList<ItemAutoComplete>();
		
		for (ValeDescontoVO valeDesconto : valesDesconto) {

			autocomplete.add(new ItemAutoComplete(
					"{\"codigo\":\"" + valeDesconto.getCodigo() + "\", \"nome\":\"" + valeDesconto.getNome() + "\", \"edicao\":\"" + valeDesconto.getNumeroEdicao() + "\"}",  
					valeDesconto.getCodigo() + " - " + valeDesconto.getNome() + " - " + valeDesconto.getNumeroEdicao(), 
					new Object[]{valeDesconto.getId()}));
		}
		
		this.result.use(Results.json()).from(autocomplete, "result").recursive().serialize();
	}
	
	@Get
	@Path("/autoCompleteProdutosCuponados")
	public void autoCompleteProdutosCuponados(String filtro) {
		
		List<PublicacaoCuponada> publicacoes = this.valeDescontoService.obterCuponadasPorCodigoOuNome(filtro);
		
		List<ItemAutoComplete> autocomplete = new ArrayList<ItemAutoComplete>();
		
		for (PublicacaoCuponada cuponada : publicacoes) {
			
			autocomplete.add(new ItemAutoComplete(
					cuponada.getCodigo() + " - " + cuponada.getNome() + " - " + cuponada.getNumeroEdicao(), 
					null, 
					new Object[]{cuponada.getId()}));
		}
		
		this.result.use(Results.json()).from(autocomplete, "result").recursive().serialize();
	}
	
	@Get
	@Path("/exportar")
	public void exportar(FileType fileType) throws IOException {
		
		FiltroValeDescontoVO filtro = (FiltroValeDescontoVO) this.session.getAttribute(FILTRO_SESSION_ATTRIBUTE);

		filtro.getPaginacaoVO().setPaginaAtual(null);
		filtro.getPaginacaoVO().setQtdResultadosPorPagina(null);

		List<ValeDescontoVO> valesDesconto = this.valeDescontoService.obterPorFiltro(filtro);

		FileExporter.to("vales_desconto", fileType).inHTTPResponse(
			this.getNDSFileHeader(), filtro, valesDesconto, ValeDescontoVO.class, this.httpServletResponse
		);
		
		this.result.nothing();
	}
	
	@Get
	@Path("/validarEdicao")
	public void validarEdicaoProduto(String codigoProduto, Long numeroEdicao) {

		PublicacaoCuponada publicacao = 
				this.valeDescontoService.obterCuponadasPorCodigoEdicao(codigoProduto, numeroEdicao);
		
		if (publicacao == null) {
			
			this.result.use(Results.json()).withoutRoot().from(Boolean.FALSE).serialize();
		
		} else {
		
			this.result.use(Results.json()).withoutRoot().from(publicacao).serialize();
		}
	}
	
	private void configurarFiltro(FiltroValeDescontoVO filtro, String sortname, String sortorder, int page, int rp) {

		filtro.setPaginacaoVO(new PaginacaoVO(page, rp, sortorder, sortname));
		
		this.session.setAttribute(FILTRO_SESSION_ATTRIBUTE, filtro);
	}
}
