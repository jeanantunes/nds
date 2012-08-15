package br.com.abril.nds.controllers.lancamento;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.client.vo.ValidacaoVO;
import br.com.abril.nds.dto.FuroProdutoDTO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.service.FuroProdutoService;
import br.com.abril.nds.service.ProdutoEdicaoService;
import br.com.abril.nds.service.ProdutoService;
import br.com.abril.nds.util.Constantes;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.ItemAutoComplete;
import br.com.abril.nds.util.TipoMensagem;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.ioc.spring.VRaptorRequestHolder;
import br.com.caelum.vraptor.view.Results;

@Resource
@Path("/lancamento/furoProduto")
public class FuroProdutoController {

	@Autowired
	private ProdutoEdicaoService produtoEdicaoService;
	
	@Autowired
	private FuroProdutoService furoProdutoService;
	
	@Autowired
	private ProdutoService produtoService;
	
	private Result result;
	
	public FuroProdutoController(Result result){
		this.result = result;
	}
	
	@Get
	@Path("/")
	@Rules(Permissao.ROLE_LANCAMENTO_FURO_PRODUTO)
	public void index(){
		
	}
	
	@Post
	public void pesquisar(String codigo, String produto, Long edicao, String dataLancamento) throws Exception{
		
		this.validarDadosEntradaPesquisa(codigo, edicao, dataLancamento);
		
		FuroProdutoDTO furoProdutoDTO = null;
		try {
			furoProdutoDTO = produtoEdicaoService.obterProdutoEdicaoPorCodigoEdicaoDataLancamento(
					codigo, produto, edicao, new SimpleDateFormat(Constantes.DATE_PATTERN_PT_BR).parse(dataLancamento));
		} catch (Exception e) {
			
			if (e instanceof ValidacaoException){
				throw e;
			} else {
				throw new ValidacaoException(TipoMensagem.ERROR, "Erro ao pesquisar produto: " + e.getMessage());
			}
		}
		
		if (furoProdutoDTO != null){
			
			String path = furoProdutoDTO.getPathImagem();
			path = path.replace("\\", "/");
			furoProdutoDTO.setPathImagem(null);
			File imagem = null;
			for (String ext : Constantes.EXTENSOES_IMAGENS){
				imagem = new File(path + ext);
				
				String raizApp = VRaptorRequestHolder.currentRequest().getRequest().getContextPath();
				if (imagem.exists()){
					furoProdutoDTO.setPathImagem(path.substring(
									path.indexOf(raizApp)) + ext);
					break;
				}
			}
			result.use(Results.json()).from(furoProdutoDTO, "result").serialize();
		} else {
			throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum registro encontrado.");
		}
		
		result.forwardTo(FuroProdutoController.class).index();
	}
	
	private void validarDadosEntradaPesquisa(String codigo, Long edicao, String dataLancamento) {
		List<String> listaMensagemValidacao = new ArrayList<String>();
		
		if (codigo == null || codigo.isEmpty()){
			listaMensagemValidacao.add("Código é obrigatório.");
		}
		
		if (edicao == null){
			listaMensagemValidacao.add("Edição é obrigatório.");
		}
		
		if (dataLancamento == null || dataLancamento.isEmpty()){
			listaMensagemValidacao.add("Data Lançamento é obrigatório.");
		} else if (!DateUtil.isValidDatePTBR(dataLancamento)){
			listaMensagemValidacao.add("Valor inválido: Data Lançamento.");
		}
		
		if (!listaMensagemValidacao.isEmpty()){
			ValidacaoVO validacaoVO = new ValidacaoVO(TipoMensagem.ERROR, listaMensagemValidacao);
			throw new ValidacaoException(validacaoVO);
		}
	}
	
	@Post
	public void pesquisarPorNomeProduto(String nomeProduto) throws Exception{
		
		List<Produto> listaProdutoEdicao = null;
		try {
			listaProdutoEdicao = this.produtoService.obterProdutoLikeNomeProduto(nomeProduto);
		} catch (Exception e) {
			if (e instanceof ValidacaoException){
				throw e;
			} else {
				throw new ValidacaoException(TipoMensagem.ERROR, "Erro ao pesquisar produto: " + e.getMessage());
			}
		}
		
		if (listaProdutoEdicao != null && !listaProdutoEdicao.isEmpty()){
			List<ItemAutoComplete> listaProdutos = new ArrayList<ItemAutoComplete>();
			for (Produto produto : listaProdutoEdicao){
				listaProdutos.add(
						new ItemAutoComplete(
								produto.getNome(), 
								null,
								new FuroProdutoDTO(
										produto.getCodigo())));
			}
			
			result.use(Results.json()).from(listaProdutos, "result").include("value", "chave").serialize();
		} else {
		
			result.use(Results.json()).from("", "result").serialize();
		}
		
		result.forwardTo(FuroProdutoController.class).index();
	}

	@Post
	public void confirmarFuro(String codigoProduto, Long idProdutoEdicao, String novaData, 
			Long idLancamento) throws Exception{
		
		validarDadosEntradaConfirmarFuro(codigoProduto, idProdutoEdicao, novaData, idLancamento);
		
		try {
			this.furoProdutoService.efetuarFuroProduto(codigoProduto, 
					idProdutoEdicao, idLancamento, 
					new SimpleDateFormat(Constantes.DATE_PATTERN_PT_BR).parse(novaData), this.getIdUsuario());
			
			result.use(Results.json()).from(
					new ValidacaoVO(TipoMensagem.SUCCESS, "Operação efetuada com sucesso."), 
					Constantes.PARAM_MSGS).recursive().serialize();
		} catch (Exception e){
			if (e instanceof ValidacaoException){
				throw e;
			} else {
				throw new ValidacaoException(TipoMensagem.ERROR, "Erro ao efetuar furo: " + e.getMessage());
			}
		}
		
		result.forwardTo(FuroProdutoController.class).index();
	}
	
	@Post
	public void buscarNomeProduto(String codigoProduto){
		String nomeProduto = this.produtoService.obterNomeProdutoPorCodigo(codigoProduto);
		
		if (nomeProduto == null){
			this.result.use(Results.json()).from("").serialize();
		} else {
			this.result.use(Results.json()).from(nomeProduto, "result").serialize();
		}
	}
	
	private void validarDadosEntradaConfirmarFuro(String codigoProduto, Long idProdutoEdicao, 
			String novaData, Long idLancamento) {
		
		List<String> listaMensagemValidacao = new ArrayList<String>();
		
		if (codigoProduto == null || codigoProduto.isEmpty()){
			listaMensagemValidacao.add("Código produto é obrigatório.");
		}
		
		if (idProdutoEdicao == null){
			listaMensagemValidacao.add("Id produto edição é obrigatório.");
		}
		
		if (idLancamento == null){
			listaMensagemValidacao.add("Lançamento é obrigatório");
		}
		
		if (novaData == null || novaData.isEmpty()){
			listaMensagemValidacao.add("Nova Data é obrigatório.");
		} else if (!DateUtil.isValidDatePTBR(novaData)){
			listaMensagemValidacao.add("Valor inválido: Nova Data.");
		}
		
		if (!listaMensagemValidacao.isEmpty()){
			ValidacaoVO validacaoVO = new ValidacaoVO(TipoMensagem.ERROR, listaMensagemValidacao);
			throw new ValidacaoException(validacaoVO);
		}
	}
	
	private Long getIdUsuario() {
		// TODO pendente
		return 1L;
	}
}
