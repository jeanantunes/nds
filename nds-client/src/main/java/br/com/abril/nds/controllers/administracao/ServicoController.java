package br.com.abril.nds.controllers.administracao;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;

@Resource
@Path("/servico/cadastroServico")
public class ServicoController {


	@Autowired
	private Result result;
	
	@Path("/")
	public void index() {
		
	}
	
	@Path("/pesquisarServicos")
	public void pesquisarServicos(String codigo, 
								  String descricao, 
								  String periodicidade,
								  String sortorder, 
								  String sortname, 
								  int page, 
								  int rp) {
		
	}

	@Path("/salvarServico")
	public void salvarServico(String codigo, String descricao, Double taxaFixa,
			boolean isIsento, String periodicidade, String baseCalculo, Double percentualCalculoBase) {
		
		
		
		
	}
	
	private boolean validarServico(String codigo, String descricao, Double taxaFixa,
			boolean isIsento, String periodicidade, String baseCalculo, Double percentualCalculoBase) {
		
		// TODO: validar dados.
		
		return true;
	}
	
}
