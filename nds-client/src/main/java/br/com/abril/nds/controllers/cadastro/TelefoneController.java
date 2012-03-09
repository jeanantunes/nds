package br.com.abril.nds.controllers.cadastro;

import br.com.abril.nds.util.CellModel;
import br.com.abril.nds.util.TableModel;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

@Resource
@Path("/cadastro/telefone")
public class TelefoneController {

	public static final String LISTA_TELEFONES_SALVAR_SESSAO = "listaTelefonesSalvarSessao";
	
	public static final String LISTA_TELEFONES_REMOVER_SESSAO = "listaTelefonesRemoverSessao";
	
	private Result result;
	
	public TelefoneController(Result result){
		this.result = result;
	}
	
	@Path("/")
	public void index(){}
	
	public void pesquisarTelefones(){
		//TODO: chamar service para carregar telefones
		TableModel<CellModel> tabela = new TableModel<CellModel>();
		
		this.result.use(Results.json()).from(tabela, "result").recursive().serialize();
	}
	
	public void adicionarTelefone(){
		//TODO: setar telefone na sessão
	}
	
	public void removerTelefone(){
		//TODO: setar telefone na sessão
	}
}
