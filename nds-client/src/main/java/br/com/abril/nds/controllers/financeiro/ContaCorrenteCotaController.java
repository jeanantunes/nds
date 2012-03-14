package br.com.abril.nds.controllers.financeiro;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.service.CotaService;
import br.com.abril.nds.service.FornecedorService;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;

@Resource
public class ContaCorrenteCotaController {
		
	private Result result;

	private HttpServletRequest request;
	
	@Autowired
	private CotaService cotaService;
	
	public ContaCorrenteCotaController(Result result,HttpServletRequest request){
		this.result = result;
		this.request = request;
	}
	
	public void index() {

	
	}
	
	public void buscarCota(Integer numeroCota){
		//TODO: buscar nome da cota pelo numero
		cotaService.obterPorNumeroDaCota(numeroCota);
	}

}
