package br.com.abril.nds.controllers.distribuicao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.client.util.PessoaUtil;
import br.com.abril.nds.client.vo.ProdutoVO;
import br.com.abril.nds.controllers.BaseController;
import br.com.abril.nds.dto.CotaQueNaoRecebeExcecaoDTO;
import br.com.abril.nds.dto.CotaQueRecebeExcecaoDTO;
import br.com.abril.nds.dto.ProdutoNaoRecebidoDTO;
import br.com.abril.nds.dto.ProdutoRecebidoDTO;
import br.com.abril.nds.dto.filtro.FiltroDTO;
import br.com.abril.nds.dto.filtro.FiltroExcecaoSegmentoParciaisDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.distribuicao.ExcecaoProdutoCota;
import br.com.abril.nds.model.distribuicao.TipoClassificacaoProduto;
import br.com.abril.nds.model.distribuicao.TipoExcecao;
import br.com.abril.nds.model.distribuicao.TipoSegmentoProduto;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.service.CotaService;
import br.com.abril.nds.service.ExcecaoSegmentoParciaisService;
import br.com.abril.nds.service.FornecedorService;
import br.com.abril.nds.service.ProdutoService;
import br.com.abril.nds.service.TipoClassificacaoProdutoService;
import br.com.abril.nds.service.UsuarioService;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.Constantes;
import br.com.abril.nds.util.ItemAutoComplete;
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

@Path("/distribuicao/excecaoSegmentoParciais")
@Resource()
public class ExcecaoSegmentoParciaisController extends BaseController {

	private static final String FILTRO_SESSION_ATTRIBUTE = "filtroExcecaoSegmentoParciaisDTO";
	
    private static final ValidacaoVO VALIDACAO_VO_SUCESSO = new ValidacaoVO(TipoMensagem.SUCCESS,
            "Operação realizada com sucesso.");
	
	@Autowired
	private ExcecaoSegmentoParciaisService excecaoSegmentoParciaisService;
	
	@Autowired
	private ProdutoService produtoService;
	
	@Autowired
	private FornecedorService fornecedorService;
	
	@Autowired
	private CotaService cotaService;
	
	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private Result result;

	@Autowired
	private HttpSession session;
	
	@Autowired
	private HttpServletResponse httpResponse;
	
	@Autowired
	private TipoClassificacaoProdutoService classificacao;
	
	@Rules(Permissao.ROLE_DISTRIBUICAO_EXCECAO_SEGMENTO_PARCIAIS)
	public void index(){
		this.carregarComboClassificacao();
	}
		
	private void carregarComboClassificacao(){
		List<TipoClassificacaoProduto> classificacoes = classificacao.obterTodos();
		result.include("listaClassificacao", classificacoes);
	}
	
	@Post("pesquisarProdutosRecebidosPelaCota")
	public void pesquisarProdutosRecebidosPelaCota(FiltroExcecaoSegmentoParciaisDTO filtro, String sortorder, String sortname, int page, int rp ){
	
		filtro.setPaginacao(new PaginacaoVO(page, rp, sortorder, sortname));

		validarEntradaFiltroCota(filtro);
		
		filtro.getCotaDto().setNomePessoa(PessoaUtil.removerSufixoDeTipo(filtro.getCotaDto().getNomePessoa()));
		
		List<ProdutoRecebidoDTO> listaProdutoRecebidoDto = this.excecaoSegmentoParciaisService.obterProdutosRecebidosPelaCota(filtro);
		
		guardarFiltroNaSession(filtro);
		
		TableModel<CellModelKeyValue<ProdutoNaoRecebidoDTO>> tableModel = new TableModel<CellModelKeyValue<ProdutoNaoRecebidoDTO>>();
		
		configurarTableModelComPaginacao(listaProdutoRecebidoDto, tableModel, filtro);
		
		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
	}
	
	@Post("/pesquisarProdutosNaoRecebidosPelaCota")
	public void pesquisarProdutosNaoRecebidosPelaCota(FiltroExcecaoSegmentoParciaisDTO filtro, String sortorder, String sortname, int page, int rp ){
		
		validarEntradaFiltroCota(filtro);
		
		filtro.getCotaDto().setNomePessoa(PessoaUtil.removerSufixoDeTipo(filtro.getCotaDto().getNomePessoa()));
		
		List<ProdutoNaoRecebidoDTO> listaProdutoNaoRecebidoDto = this.excecaoSegmentoParciaisService.obterProdutosNaoRecebidosPelaCota(filtro);

		TableModel<CellModelKeyValue<ProdutoNaoRecebidoDTO>> tableModel = new TableModel<CellModelKeyValue<ProdutoNaoRecebidoDTO>>();
		
		configurarTableModelSemPaginacao(listaProdutoNaoRecebidoDto, tableModel);
		
		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
	}
	
	@Post
	public void pesquisarCotasQueRecebemExcecao(FiltroExcecaoSegmentoParciaisDTO filtro, String sortorder, String sortname, int page, int rp ){
		filtro.setPaginacao(new PaginacaoVO(page, rp, sortorder, sortname));

		validarEntradaFiltroProduto(filtro);
		
		filtro.getProdutoDto().setCodigoProduto(filtro.getProdutoDto().getCodigoProduto());
		
		List<CotaQueRecebeExcecaoDTO> listaCotaQueRecebeExcecaoDto = this.excecaoSegmentoParciaisService.obterCotasQueRecebemExcecaoPorProduto(filtro);
		
		guardarFiltroNaSession(filtro);
		
		TableModel<CellModelKeyValue<CotaQueRecebeExcecaoDTO>> tableModel = new TableModel<CellModelKeyValue<CotaQueRecebeExcecaoDTO>>();
		
		configurarTableModelComPaginacao(listaCotaQueRecebeExcecaoDto, tableModel, filtro);
		
		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
	}
	
	@Post
	public void pesquisarCotasQueNaoRecebemExcecao(FiltroExcecaoSegmentoParciaisDTO filtro, String sortorder, String sortname, int page, int rp ){
		validarEntradaFiltroProduto(filtro);
		
		List<CotaQueNaoRecebeExcecaoDTO> listaCotaQueNaoRecebeExcecaoDto = this.excecaoSegmentoParciaisService.obterCotasQueNaoRecebemExcecaoPorProduto(filtro);
		
		TableModel<CellModelKeyValue<CotaQueNaoRecebeExcecaoDTO>> tableModel = new TableModel<CellModelKeyValue<CotaQueNaoRecebeExcecaoDTO>>();
		
		configurarTableModelSemPaginacao(listaCotaQueNaoRecebeExcecaoDto, tableModel);
		
		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
	}
	
	@Post
	public void excluirExcecaoProduto(Long id){
		this.excecaoSegmentoParciaisService.excluirExcecaoProduto(id);
		
		result.use(Results.json()).from(VALIDACAO_VO_SUCESSO,"result").recursive().serialize();
	}
	
	@Post
	public void inserirExcecaoProdutoNaCota(String[] listaIdProduto, FiltroExcecaoSegmentoParciaisDTO filtro){
		ExcecaoProdutoCota element = null;
		Cota cota = null;
		Usuario usuario = usuarioService.getUsuarioLogado();
		TipoExcecao tipoExcecao = null;
		
	/*	if (listaIdProduto.length == 0 ) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum produto selecionado.");
		}
		
		validarEntradaFiltroCota(filtro);*/

		if (filtro.getCotaDto().getNumeroCota() != null && !filtro.getCotaDto().getNumeroCota().equals(0)) {
			cota = (cotaService.obterPorNumeroDaCota(filtro.getCotaDto().getNumeroCota()));
		}else {
			cota =(cotaService.obterPorNome(PessoaUtil.removerSufixoDeTipo(filtro.getCotaDto().getNomePessoa()))).get(0);
		}
		
		if (filtro.isExcecaoSegmento()) {
			tipoExcecao = TipoExcecao.SEGMENTO;
		}else{
			tipoExcecao = TipoExcecao.PARCIAL;
		}
		
		List<ExcecaoProdutoCota> listaExcessaoProdutoCota = new ArrayList<>();
		
		for (String idProduto : listaIdProduto) {
			element = new ExcecaoProdutoCota();
//			element.setProduto(produtoService.obterProdutoPorID(idProduto));
			//codigo ICD vindo da tela
			element.setCodigoICD(idProduto);
			
			
			TipoClassificacaoProduto tcp = new TipoClassificacaoProduto();
			tcp.setId(filtro.getProdutoDto().getIdClassificacaoProduto());
			element.setTipoClassificacaoProduto(tcp);
			
			element.setCota(cota);
			element.setUsuario(usuario);
			element.setDataAlteracao(new Date());
			element.setTipoExcecao(tipoExcecao);
			
			listaExcessaoProdutoCota.add(element);
		}
		
		this.excecaoSegmentoParciaisService.inserirListaExcecao(listaExcessaoProdutoCota);
		
		result.use(Results.json()).from(VALIDACAO_VO_SUCESSO,"result").recursive().serialize();
	}
	
	@Post
	public void inserirCotaNaExcecao(Integer[] listaNumeroCota, FiltroExcecaoSegmentoParciaisDTO filtro){
		ExcecaoProdutoCota element;
		Produto produto = null;
		Usuario usuario = usuarioService.getUsuarioLogado();
		TipoExcecao tipoExcecao = null;
		
		if (listaNumeroCota.length == 0 ) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Nenhuma cota foi selecionada.");
		}
		
		validarEntradaFiltroProduto(filtro);
		
        if (StringUtils.isNotEmpty(filtro.getProdutoDto().getCodigoProduto())
                && !"0".equals(filtro.getProdutoDto().getCodigoProduto())) {
			produto = produtoService.obterProdutoPorCodigo(filtro.getProdutoDto().getCodigoProduto());
		}
		
		if (filtro.isExcecaoSegmento()) {
			tipoExcecao = TipoExcecao.SEGMENTO;
		}else{
			tipoExcecao = TipoExcecao.PARCIAL;
		}
		
		List<ExcecaoProdutoCota> listaExcessaoProdutoCota = new ArrayList<>();
		
		for (Integer numeroCota : listaNumeroCota) {
			element = new ExcecaoProdutoCota();
			element.setProduto(produto);
			element.setCota(cotaService.obterPorNumeroDaCota(numeroCota));
			TipoClassificacaoProduto tcp = new TipoClassificacaoProduto();
			tcp.setId(filtro.getProdutoDto().getIdClassificacaoProduto());
			element.setTipoClassificacaoProduto(tcp);
			element.setUsuario(usuario);
			element.setDataAlteracao(new Date());
			element.setTipoExcecao(tipoExcecao);
			
			listaExcessaoProdutoCota.add(element);
		}
		
		this.excecaoSegmentoParciaisService.inserirListaExcecao(listaExcessaoProdutoCota);
		
		result.use(Results.json()).from(VALIDACAO_VO_SUCESSO,"result").recursive().serialize();
	}
	
	@Post("autoCompletarPorNomeProdutoNaoRecebidoPelaCota")
	public void autoCompletarPorNomeProdutoNaoRecebidoPelaCota(FiltroExcecaoSegmentoParciaisDTO filtro){
		List<ProdutoNaoRecebidoDTO> listaProdutosNaoRecebidosPelaCota = this.excecaoSegmentoParciaisService.obterProdutosNaoRecebidosPelaCota(filtro);
		
		List<ItemAutoComplete> listaProdutosNaoRecebidosPelaCotaAutoComplete = new ArrayList<ItemAutoComplete>();
		
		if (listaProdutosNaoRecebidosPelaCota != null && !listaProdutosNaoRecebidosPelaCota.isEmpty()) {
			
			for (ProdutoNaoRecebidoDTO produtoNaoRecebidoDTO : listaProdutosNaoRecebidosPelaCota) {
				listaProdutosNaoRecebidosPelaCotaAutoComplete.add(new ItemAutoComplete(produtoNaoRecebidoDTO.getNomeProduto(), null, produtoNaoRecebidoDTO));
			}
		}
		
		this.result.use(Results.json()).from(listaProdutosNaoRecebidosPelaCotaAutoComplete, "result").include("value", "chave").serialize();
	}
	
	@Post
	public void autoCompletarPorNomeCotaQueNaoRecebeExcecao(FiltroExcecaoSegmentoParciaisDTO filtro){
		List<CotaQueNaoRecebeExcecaoDTO> listaCotaQueNaoRecebeExcecaoDTO = this.excecaoSegmentoParciaisService.autoCompletarPorNomeCotaQueNaoRecebeExcecao(filtro);
		
		List<ItemAutoComplete> listaCotaQueNaoRecebeExcecaoDTOAutoComplete = new ArrayList<ItemAutoComplete>();
		
		if (listaCotaQueNaoRecebeExcecaoDTO != null && !listaCotaQueNaoRecebeExcecaoDTO.isEmpty()) {
			
			for (CotaQueNaoRecebeExcecaoDTO cotaQueNaoRecebeExcecaoDTO : listaCotaQueNaoRecebeExcecaoDTO) {
				listaCotaQueNaoRecebeExcecaoDTOAutoComplete.add(new ItemAutoComplete(cotaQueNaoRecebeExcecaoDTO.getNomePessoa(), null, cotaQueNaoRecebeExcecaoDTO));
			}
		}
		
		this.result.use(Results.json()).from(listaCotaQueNaoRecebeExcecaoDTOAutoComplete, "result").include("value", "chave").serialize();
	}
	
	@Post
	public void pesquisarPorCodigoProdutoAutoComplete(String codigo){
		
		pesquisarPorCodigoProduto(codigo);
	}
	
	@Post
	public void pesquisarPorCodigoProduto(String codigoProduto){
		Produto produto = null;
		TipoSegmentoProduto tipoSegmentoProduto = null;
		ArrayList<Object> objects = new ArrayList<>();
		Long idClassificacao;
				
		produto = produtoService.obterProdutoPorCodigo(codigoProduto);
		
		if (produto != null) {
			PessoaJuridica juridica = fornecedorService.obterFornecedorUnico(produto.getCodigo()).getJuridica();
			tipoSegmentoProduto = produto.getTipoSegmentoProduto();

			objects.add(produto);
			objects.add(juridica);
			objects.add(tipoSegmentoProduto);
		}else {
            throw new ValidacaoException(TipoMensagem.WARNING, "Produto com o código \"" + codigoProduto
                    + "\" não encontrado!");
		}	
		
		result.use(Results.json()).from(objects, "result").serialize();
	}
	
	@Post
	public void autoCompletarProduto(String nome) {
		
		List<Produto> listaProduto = null;
		
		if (StringUtils.isNumeric(nome)) {
			
			listaProduto = this.produtoService.obterProdutoLikeCodigo(nome);
		}
		else {
			
			listaProduto = this.produtoService.obterProdutoLikeNome(nome, Constantes.QTD_MAX_REGISTROS_AUTO_COMPLETE);
		}
		
		List<ItemAutoComplete> listaProdutos = new ArrayList<ItemAutoComplete>();
		
		if (listaProduto != null && !listaProduto.isEmpty()) {
			ProdutoVO produtoAutoComplete = null;
			
			for (Produto produto : listaProduto) {
				produtoAutoComplete = new ProdutoVO(produto.getCodigo(),produto.getNome(),produto);
				
				PessoaJuridica juridica = fornecedorService.obterFornecedorUnico(produto.getCodigo()).getJuridica();
				
				produtoAutoComplete.setNomeFantasia(juridica.getNomeFantasia());
				produtoAutoComplete.setRazaoSocial(juridica.getRazaoSocial());
				if (produto.getTipoSegmentoProduto() != null) {
					produtoAutoComplete.setTipoSegmentoProduto(produto.getTipoSegmentoProduto().getDescricao());
				}
				
				ItemAutoComplete itemAutoComplete =
					new ItemAutoComplete(produtoAutoComplete.getNumero(), produtoAutoComplete.getLabel(), produtoAutoComplete);
				
				listaProdutos.add(itemAutoComplete);
			}
		}
		
		result.use(Results.json()).from(listaProdutos, "result").include("value", "chave").serialize();
	}
	
	@SuppressWarnings("unchecked")
	@Get
	public void exportar(FileType fileType, boolean porCota) throws IOException {
		
		List listaDto = null;
		Class classDto = null;
		String fileName = null;
		
		FiltroExcecaoSegmentoParciaisDTO filtro = (FiltroExcecaoSegmentoParciaisDTO) session.getAttribute(FILTRO_SESSION_ATTRIBUTE);
		
		if (porCota) {
			listaDto = this.excecaoSegmentoParciaisService.obterProdutosRecebidosPelaCota(filtro);
			classDto = ProdutoRecebidoDTO.class;
			fileName = "Produtos_Recebidos";
		}else {
			listaDto = this.excecaoSegmentoParciaisService.obterCotasQueRecebemExcecaoPorProduto(filtro);
			classDto = CotaQueRecebeExcecaoDTO.class;
			fileName = "Cotas_que_Recebem_Excecao";
		}
		FileExporter.to(fileName, fileType).inHTTPResponse(this.getNDSFileHeader(), filtro, null, listaDto,
				classDto, this.httpResponse);
		
		result.nothing();
	}
	
	private void validarEntradaFiltroCota(FiltroExcecaoSegmentoParciaisDTO filtro) {
		if((filtro.getCotaDto().getNumeroCota() == null || filtro.getCotaDto().getNumeroCota() == 0) && 
				(filtro.getCotaDto().getNomePessoa() == null || filtro.getCotaDto().getNomePessoa().trim().isEmpty()))
            throw new ValidacaoException(TipoMensagem.WARNING, "Código ou nome da cota é obrigatório.");
	}
	
	private void validarEntradaFiltroProduto(FiltroExcecaoSegmentoParciaisDTO filtro) {
		if((filtro.getProdutoDto().getCodigoProduto() == null || filtro.getProdutoDto().getCodigoProduto().trim().isEmpty()) && 
				(filtro.getProdutoDto().getNomeProduto() == null || filtro.getProdutoDto().getNomeProduto().trim().isEmpty()))
            throw new ValidacaoException(TipoMensagem.WARNING, "Código ou nome do produto é obrigatório.");
	}
	
	@SuppressWarnings({ "rawtypes", "unused" })
	private void validarLista(List list, String mensagem){
		if (list == null || list.isEmpty()) {
			throw new ValidacaoException(TipoMensagem.WARNING, mensagem);
		}
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private TableModel configurarTableModelSemPaginacao( List listaDto, TableModel tableModel){
		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listaDto));

		tableModel.setPage(1);

		tableModel.setTotal(listaDto.size());
		
		return tableModel;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked"})
	private TableModel configurarTableModelComPaginacao( List listaDto, TableModel tableModel, FiltroDTO filtro){
		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listaDto));

		tableModel.setPage(filtro.getPaginacao().getPaginaAtual());

		tableModel.setTotal(filtro.getPaginacao().getQtdResultadosTotal());
		
		return tableModel;
	}
	
	private void guardarFiltroNaSession(FiltroExcecaoSegmentoParciaisDTO filtro) {
		
		FiltroExcecaoSegmentoParciaisDTO filtroSession = (FiltroExcecaoSegmentoParciaisDTO) session.getAttribute(FILTRO_SESSION_ATTRIBUTE);
		
		if (filtroSession != null && !filtroSession.equals(filtro)){
			filtro.getPaginacao().setPaginaAtual(1);
		}
		
		session.setAttribute(FILTRO_SESSION_ATTRIBUTE, filtro);
	}
}
