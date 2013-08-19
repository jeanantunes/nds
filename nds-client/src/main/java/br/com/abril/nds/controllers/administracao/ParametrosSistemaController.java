package br.com.abril.nds.controllers.administracao;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.controllers.BaseController;
import br.com.abril.nds.dto.ParametroSistemaGeralDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.service.integracao.ParametroSistemaService;
import br.com.abril.nds.vo.ValidacaoVO;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

@Resource
@Path("/administracao/parametrosSistema")
@Rules(Permissao.ROLE_ADMINISTRACAO_PARAMETROS_SISTEMA)
public class ParametrosSistemaController extends BaseController {

	@Autowired
	private Result result;
	
	@Autowired
	private ParametroSistemaService psService;
	
	
	/**
	 * Busca os parâmetros gerais do sistema.
	 * 
	 * @return  
	 */
	@Get
	@Path("/")
	public ParametroSistemaGeralDTO index() {
		
		ParametroSistemaGeralDTO dto = psService.buscarParametroSistemaGeral();
		return dto;
	}
	
	/**
	 * Salva os parâmetros do sistema.
	 * 
	 * @param dto
	 * @param imgLogoSistema
	 */
	@Rules(Permissao.ROLE_ADMINISTRACAO_PARAMETROS_SISTEMA_ALTERACAO)
	public void salvar(ParametroSistemaGeralDTO dto) {
			
		// Salvar:
		psService.salvar(dto, null, null);
		
		result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Parâmetros salvos com sucesso."), "result").recursive().serialize();
	}	
}
