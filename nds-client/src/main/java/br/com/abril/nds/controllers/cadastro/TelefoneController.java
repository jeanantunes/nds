package br.com.abril.nds.controllers.cadastro;

import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;

@Resource
@Path("/cadastro/telefone")
public class TelefoneController {

	public static final String LISTA_TELEFONES_SALVAR_SESSAO = "listaTelefonesSalvarSessao";
	
	public static final String LISTA_TELEFONES_REMOVER_SESSAO = "listaTelefonesRemoverSessao";
	
	@Path("/")
	public void index(){}
	
	public void pesquisarTelefones(){
		//TODO: chamar service para carregar telefones
	}
	
	public void adicionarTelefone(){
		//TODO: setar telefone na sessão
	}
	
	public void removerTelefone(){
		//TODO: setar telefone na sessão
	}
}
