package br.com.abril.nds.controllers.lancamento;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.FuroProdutoDTO;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.service.FuroProdutoService;
import br.com.abril.nds.service.ProdutoEdicaoService;
import br.com.abril.nds.service.ProdutoService;
import br.com.abril.nds.util.Constantes;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.ItemAutoComplete;
import br.com.abril.nds.util.Util;
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
	public void index(){
		
	}
	
	@Post
	public void pesquisar(String codigo, String produto, Long edicao, String dataLancamento){
		
		if (this.validarDadosEntradaPesquisa(codigo, edicao, dataLancamento)){
		
			FuroProdutoDTO furoProdutoDTO = null;
			try {
				furoProdutoDTO = produtoEdicaoService.obterProdutoEdicaoPorCodigoEdicaoDataLancamento(
						codigo, produto, edicao, new SimpleDateFormat(Constantes.DATE_PATTERN_PT_BR).parse(dataLancamento));
			} catch (Exception e) {
				result.use(Results.json()).from(new String[]{Constantes.TIPO_MSG_ERROR, 
						"Erro ao pesquisar produto: " + e.getMessage()}, Constantes.PARAM_MSGS).serialize();
				result.forwardTo(FuroProdutoController.class).index();
				return;
			}
			
			if (furoProdutoDTO != null){
				
				String path = furoProdutoDTO.getPathImagem();
				furoProdutoDTO.setPathImagem(null);
				File imagem = null;
				for (String ext : Constantes.EXTENSOES_IMAGENS){
					imagem = new File(path + ext);
					
					if (imagem.exists()){
						furoProdutoDTO.setPathImagem(path.substring(
										path.indexOf(VRaptorRequestHolder.currentRequest().getRequest().getContextPath())) + ext);
						break;
					}
				}
				result.use(Results.json()).from(furoProdutoDTO, "result").serialize();
			} else {
				result.use(Results.json()).from(new String[]{Constantes.TIPO_MSG_WARNING, "Nenhum registro encontrado."}, Constantes.PARAM_MSGS).serialize();
			}
		}
		
		result.forwardTo(FuroProdutoController.class).index();
	}
	
	private boolean validarDadosEntradaPesquisa(String codigo, Long edicao, String dataLancamento) {
		
		boolean valido = true;
		List<String> listaMensagemValidacao = new ArrayList<String>();
		
		if (codigo == null || codigo.isEmpty()){
			valido = false;
			listaMensagemValidacao.add(Constantes.TIPO_MSG_ERROR);
			listaMensagemValidacao.add("Código é obrigatório.");
		}
		
		if (edicao == null){
			valido = false;
			
			if (listaMensagemValidacao.isEmpty()){
				listaMensagemValidacao.add(Constantes.TIPO_MSG_ERROR);
			}
			
			listaMensagemValidacao.add("Edição é obrigatório.");
		}
		
		if (dataLancamento == null){
			valido = false;
			
			if (listaMensagemValidacao.isEmpty()){
				listaMensagemValidacao.add(Constantes.TIPO_MSG_ERROR);
			}
			
			listaMensagemValidacao.add("Data Lançamento é obrigatório.");
		} else if (!DateUtil.isValidDate(dataLancamento, null)){
			valido = false;
			
			if (listaMensagemValidacao.isEmpty()){
				listaMensagemValidacao.add(Constantes.TIPO_MSG_ERROR);
			}
			
			listaMensagemValidacao.add("Valor inválido: Data Lançamento.");
		}
		
		if (!listaMensagemValidacao.isEmpty()){
			result.use(Results.json()).from(listaMensagemValidacao, Constantes.PARAM_MSGS).serialize();
		}
		
		return valido;
	}
	
	@Post
	public void pesquisarPorNomeProduto(String nomeProduto){
		
		List<Produto> listaProdutoEdicao = null;
		try {
			listaProdutoEdicao = this.produtoService.obterProdutoPorNomeProduto(nomeProduto);
		} catch (Exception e) {
			result.use(Results.json()).from(new String[]{Constantes.TIPO_MSG_ERROR, 
					"Erro ao pesquisar produto: " + e.getMessage()}, Constantes.PARAM_MSGS).serialize();
			result.forwardTo(FuroProdutoController.class).index();
			return;
		}
		
		if (listaProdutoEdicao != null){
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
		}
		
		result.forwardTo(FuroProdutoController.class).index();
	}

	@Post
	public void confirmarFuro(Long idProdutoEdicao, String novaData, Long idLancamento){
		
		if (this.validarDadosEntradaConfirmarFuro(idProdutoEdicao, novaData, idLancamento)){
			try {
				this.furoProdutoService.efetuarFuroProduto(idProdutoEdicao, idLancamento, new SimpleDateFormat(Constantes.DATE_PATTERN_PT_BR).parse(novaData), this.getIdUsuario());
				result.use(Results.json()).from(new String[]{Constantes.TIPO_MSG_SUCCESS, 
						"Furo efetuado com sucesso."}, Constantes.PARAM_MSGS).serialize();
			} catch (Exception e){
				result.use(Results.json()).from(new String[]{Constantes.TIPO_MSG_ERROR, 
						"Erro ao confirmar furo do produto: " + e.getMessage()}, Constantes.PARAM_MSGS).serialize();
			}
		}
		
		result.forwardTo(FuroProdutoController.class).index();
	}
	
	private Long getIdUsuario() {
		// TODO pendente
		return 1L;
	}

	private boolean validarDadosEntradaConfirmarFuro(Long idProdutoEdicao, String novaData, Long idLancamento) {
		
		boolean valido = true;
		List<String> listaMensagemValidacao = new ArrayList<String>();
		
		if (idProdutoEdicao == null){
			valido = false;
			listaMensagemValidacao.add(Constantes.TIPO_MSG_ERROR);
			listaMensagemValidacao.add("Id produto edição é obrigatório.");
		}
		
		if (idLancamento == null){
			valido = false;
			
			if (listaMensagemValidacao.isEmpty()){
				listaMensagemValidacao.add(Constantes.TIPO_MSG_ERROR);
			}
			
			listaMensagemValidacao.add("Lançamento é obrigatório");
		}
		
		if (novaData == null){
			valido = false;
			
			if (listaMensagemValidacao.isEmpty()){
				listaMensagemValidacao.add(Constantes.TIPO_MSG_ERROR);
			}
			
			listaMensagemValidacao.add("Nova Data é obrigatório.");
		} else if (!DateUtil.isValidDate(novaData, null)){
			valido = false;
			
			if (listaMensagemValidacao.isEmpty()){
				listaMensagemValidacao.add(Constantes.TIPO_MSG_ERROR);
			}
			
			listaMensagemValidacao.add("Valor inválido: Nova Data.");
		}
		
		if (!listaMensagemValidacao.isEmpty()){
			result.use(Results.json()).from(listaMensagemValidacao, Constantes.PARAM_MSGS).serialize();
		}
		
		return valido;
	}
}
