package br.com.abril.nds.controllers.administracao;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.integracao.service.DistribuidorService;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.service.CobrancaService;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;

@Resource
@Path("/administracao/fecharDia")
public class FecharDiaController {
	
	@Autowired
	private CobrancaService cobrancaService;
	
	@Autowired
	private DistribuidorService distribuidorService;
	
	@Autowired
	private Result result;
	
	@Path("/")
	@Rules(Permissao.ROLE_ADMINISTRACAO_FECHAR_DIA)
	public void index(){
		Distribuidor distribuidor = this.distribuidorService.obter();
		result.include("dataOperacao", distribuidor.getDataOperacao());
	}
	
	@Post
	public void inicializarValidacoes(){
		Distribuidor distribuidor = this.distribuidorService.obter();
		boolean cobranca = this.cobrancaService.existeCobrancaParaFecharDia(distribuidor.getDataOperacao());
	}

}
