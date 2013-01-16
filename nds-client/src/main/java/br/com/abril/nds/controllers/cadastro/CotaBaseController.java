package br.com.abril.nds.controllers.cadastro;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.dto.filtro.FiltroCotaBaseDTO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.service.CotaBaseService;
import br.com.abril.nds.util.TipoMensagem;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

@Resource
@Path("/cadastro/cotaBase")
public class CotaBaseController {
	
	@Autowired
	private CotaBaseService cotaBaseService;
	
	@Autowired
	private Result result;
	
	@Path("/")
	@Rules(Permissao.ROLE_CADASTRO_COTA_BASE)
	public void index(){
		System.out.println("OLÁ MUNDO");
	}
	
	@Post
	@Path("/pesquisarCotaBase")
	public void pesquisarCotaBase(Integer numeroCota){
		if(numeroCota == null) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Número da cota inválido!");
		}
		
		FiltroCotaBaseDTO filtro = this.cotaBaseService.obterDadosFiltro(numeroCota);		
		
		this.result.use(Results.json()).from(filtro, "result").recursive().serialize();		
	}
	
	
	
		

	
	

}
