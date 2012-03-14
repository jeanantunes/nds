package br.com.abril.nds.controllers.financeiro;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;

@Resource
public class ContaCorrenteCotaController {
	
	private Result result;

	private HttpServletRequest request;
	
	public ContaCorrenteCotaController(Result result,HttpServletRequest request){
		this.result = result;
		this.request = request;
	}
	
	public void index() {

	
	}
	
	public void buscarCota(Integer numeroCota){
		//TODO: buscar nome da cota pelo numero
	}

}
