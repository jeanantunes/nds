package br.com.abril.nds.controllers.administracao;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.dto.filtro.FecharDiaDTO;
import br.com.abril.nds.integracao.service.DistribuidorService;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.service.CobrancaService;
import br.com.abril.nds.service.PoliticaCobrancaService;
import br.com.abril.nds.util.Constantes;
import br.com.abril.nds.util.DateUtil;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

@Resource
@Path("/administracao/fecharDia")
public class FecharDiaController {
	
	@Autowired
	private CobrancaService cobrancaService;
	
	@Autowired
	private DistribuidorService distribuidorService;
	
	@Autowired
	private PoliticaCobrancaService politicaCobrancaService;
	
	@Autowired
	private Result result;
	
	@Path("/")
	@Rules(Permissao.ROLE_ADMINISTRACAO_FECHAR_DIA)
	public void index(){
		Distribuidor distribuidor = this.distribuidorService.obter();
		result.include("dataOperacao", DateUtil.formatarData(distribuidor.getDataOperacao(), Constantes.DATE_PATTERN_PT_BR));
	}
	
	@Post
	public void inicializarValidacoes(){
		Distribuidor distribuidor = this.distribuidorService.obter();
		FecharDiaDTO dto = new FecharDiaDTO();
		dto.setBaixaBancaria(this.cobrancaService.existeCobrancaParaFecharDia(distribuidor.getDataOperacao()));
		
		result.use(Results.json()).withoutRoot().from(dto).recursive().serialize();
	}

}
