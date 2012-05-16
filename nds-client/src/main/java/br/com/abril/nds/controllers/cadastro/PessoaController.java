package br.com.abril.nds.controllers.cadastro;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.util.PessoaUtil;
import br.com.abril.nds.model.cadastro.Pessoa;
import br.com.abril.nds.service.PessoaService;
import br.com.abril.nds.util.ItemAutoComplete;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

@Resource
@Path("/pessoa")
public class PessoaController {
	
	@Autowired
	private Result result;
	
	@Autowired
	private PessoaService pessoaService;

	@Path("/")
	public void index() {}

	@Post
	public void autoCompletarPorNome(String nomePessoa) {
		
		nomePessoa = PessoaUtil.removerSufixoDeTipo(nomePessoa);
		
		List<Pessoa> listaPessoa = pessoaService.obterPessoasPorNome(nomePessoa);
		
		List<ItemAutoComplete> listaCotasAutoComplete = new ArrayList<ItemAutoComplete>();
		
		if (listaPessoa != null && !listaPessoa.isEmpty()) {
			
			for (Pessoa pessoa : listaPessoa) {
				
				String nomeExibicao = PessoaUtil.obterNomeExibicaoPeloTipo(pessoa);
					
				listaCotasAutoComplete.add(new ItemAutoComplete(nomeExibicao, null, pessoa.getId()));
			}
		}
		
		this.result.use(Results.json()).from(listaCotasAutoComplete, "result").include("value", "chave").serialize();
	}
}
