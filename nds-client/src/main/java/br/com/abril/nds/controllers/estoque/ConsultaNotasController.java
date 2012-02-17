package br.com.abril.nds.controllers.estoque;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.fiscal.TipoNotaFiscal;
import br.com.abril.nds.service.FornecedorService;
import br.com.abril.nds.service.TipoNotaFiscalService;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;

@Resource
@Path("/consultaNotas")
public class ConsultaNotasController {

	private Result result;
	
	@Autowired
	private FornecedorService fornecedorService;

	@Autowired
	private TipoNotaFiscalService tipoNotaFiscalService;
	
	public ConsultaNotasController() { }
	
	public ConsultaNotasController(Result result) {
		this.result = result;
	}

	public void index() {
		preencherCombos();
	}

	public void pesquisarNotas() {
		
	}

	public void preencherCombos() {

		List<Fornecedor> fornecedores = fornecedorService.obterFornecedores();

		List<TipoNotaFiscal> tiposNotaFiscal = tipoNotaFiscalService.obterTiposNotasFiscais();

		result.include("fornecedores", fornecedores);
		result.include("tiposNotaFiscal", tiposNotaFiscal);		
	}
}
