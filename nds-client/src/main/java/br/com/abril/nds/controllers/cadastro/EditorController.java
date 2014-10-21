package br.com.abril.nds.controllers.cadastro;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.client.util.PessoaUtil;
import br.com.abril.nds.client.vo.EditorVO;
import br.com.abril.nds.controllers.BaseController;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Editor;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.service.DescontoService;
import br.com.abril.nds.service.EditorService;
import br.com.abril.nds.service.FornecedorService;
import br.com.abril.nds.service.PessoaJuridicaService;
import br.com.abril.nds.service.integracao.DistribuidorService;
import br.com.abril.nds.util.ItemAutoComplete;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

@Resource
@Path("/cadastro/editor")
@Rules(Permissao.ROLE_CADASTRO_EDITOR)
public class EditorController extends BaseController {
	
	@Autowired
	private Result result;

	@Autowired
	private EditorService editorService;
	
	@Autowired
	private DescontoService descontoService;
	
	@Autowired
	private FornecedorService fornecedorService;
	
	@Autowired
	private PessoaJuridicaService pessoaJuridicaService;
	
	@Autowired
	private DistribuidorService distribuidorService;

	@Path("/")
	public void index() {
		
	}

	/**
	 * Metodo a ser ultilizado pelo componente autocomplete.js
	 * @param codigo
	 */
	@Post
	public void pesquisarPorCodigoAutoComplete(String codigo) {
		
		pesquisarPorCodigo(Long.valueOf(codigo));
	}
	
	/**
     * Efetua consulta de cota pelo número informado
     * 
     * @param numeroCota - número da cota
     */
	@Post
	public void pesquisarPorCodigo(Long codigoEditor) {
		
		if(codigoEditor == null) {
			
            throw new ValidacaoException(TipoMensagem.WARNING, "O código do Editor deve ser informado!");
		}
		
		Editor editor = this.editorService.obterEditorPorCodigo(codigoEditor);

		if (editor == null) {

            throw new ValidacaoException(TipoMensagem.WARNING, "Editor \"" + codigoEditor + "\" não encontrado!");
			
		} else {
			
			String nomeExibicao = PessoaUtil.obterNomeExibicaoPeloTipo(editor.getPessoaJuridica());
			
			EditorVO editorVO = new EditorVO(editor.getId(), editor.getCodigo(), nomeExibicao);
			
			this.result.use(Results.json()).from(editorVO, "result").recursive().serialize();
		}		
	}

	/**
	 * Efetua consulta pelo nome do editor informado, utilizado para auto complete da tela
	 * 
	 * @param nomeEditor - nome do editor
	 */
	@Post
	public void autoCompletarPorNome(String nomeEditor) {
		
		nomeEditor = PessoaUtil.removerSufixoDeTipo(nomeEditor);
		
		List<Editor> listaEditores = this.editorService.obterEditoresPorNomePessoa(nomeEditor);
		
		List<ItemAutoComplete> listaCotasAutoComplete = new ArrayList<ItemAutoComplete>();
		
		if (listaEditores != null && !listaEditores.isEmpty()) {
			
			for (Editor editor : listaEditores) {
				
				String nomeExibicao = PessoaUtil.obterNomeExibicaoPeloTipo(editor.getPessoaJuridica());
					
				EditorVO editorVO = new EditorVO(editor.getId(), editor.getCodigo(), nomeExibicao);
				
	
				listaCotasAutoComplete.add(new ItemAutoComplete(nomeExibicao, null, editorVO));
			}
		}
		
		this.result.use(Results.json()).from(listaCotasAutoComplete, "result").include("value", "chave").serialize();
	}

	/**
	 * Metodo utilizado para componente autocomplet.js
	 * @param nome
	 */
	@Post
	public void autoCompletarPorNomeAutoComplete(String nome) {
		
		autoCompletarPorNome(nome);
	}

	/**
	 * Auto complete para numeroCota. Casos de pesquisa por nome onde existem cotas com nomes iguais.
	 * @param editoresVO
	 * @return List<ItemAutoComplete>
	 */
	private List<ItemAutoComplete> getAutocompleteNumeroCota(List<EditorVO> editoresVO){
		
		List<ItemAutoComplete> listaCotasAutoComplete = new ArrayList<ItemAutoComplete>();
		
		for (EditorVO editorVO : editoresVO){
			
			String numeroExibicao = editorVO.getCodigo().toString();

			listaCotasAutoComplete.add(new ItemAutoComplete(numeroExibicao, null, editorVO));
		}
		
		return listaCotasAutoComplete;
	}
	
	/**
	 * Efetua consulta pelo nome da cota informado
	 * 
	 * @param nomeCota - nome da cota
	 */
	@Post
	public void pesquisarPorNome(String nomeEditor) {
		
		nomeEditor = PessoaUtil.removerSufixoDeTipo(nomeEditor);
		
		List<Editor> editores = this.editorService.obterEditoresPorNomePessoa(nomeEditor);
		
		if (editores == null) {
		
            throw new ValidacaoException(TipoMensagem.WARNING, "Cota \"" + nomeEditor + "\" não encontrada!");
		}
		
		List<EditorVO> editoresVO = new ArrayList<EditorVO>();
		
		for (Editor editor : editores){
		
		    String nomeExibicao = PessoaUtil.obterNomeExibicaoPeloTipo(editor.getPessoaJuridica());
		    
		    EditorVO editorVO = new EditorVO(editor.getId(), editor.getCodigo(), nomeExibicao);
		    
		    editoresVO.add(editorVO);
		}
		
		if (editoresVO.size() > 1){
			
			this.result.use(Results.json()).from(this.getAutocompleteNumeroCota(editoresVO), "result").include("value", "chave").serialize();
		}
		else{
		
		    this.result.use(Results.json()).from(editoresVO.get(0), "result").recursive().serialize();
		}
	}
	
	/**
	 * Metodo a ser ultilizado pelo componente autocomplet.js 
	 * @param nome
	 */
	@Post
	public void pesquisarPorNomeAutoComplete(String nome) {
		
		pesquisarPorNome(nome);
	}
	
}