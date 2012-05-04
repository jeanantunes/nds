package br.com.abril.nds.controllers.cadastro;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.service.CotaGarantiaService;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;



@Resource
@Path("/cadastro/garantia")
public class CotaGarantiaController {
	
	
	
	@Autowired
	private CotaGarantiaService cotaGarantiaService;
	
	
	private Result result;


	public CotaGarantiaController(Result result) {
		super();
		this.result = result;
	}
	
	
}
