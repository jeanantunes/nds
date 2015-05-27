package br.com.abril.nds.controllers.administracao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.controllers.BaseController;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.ParametroSistemaGeralDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.model.cadastro.TipoAtividade;
import br.com.abril.nds.model.fiscal.nota.Identificacao.FormatoImpressao;
import br.com.abril.nds.model.fiscal.nota.Identificacao.ProcessoEmissao;
import br.com.abril.nds.model.fiscal.nota.Identificacao.TipoAmbiente;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.service.ParametrosDistribuidorService;
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
	
	@Autowired
	private ParametrosDistribuidorService pdService;
	
	
	/**
	 * Busca os parâmetros gerais do sistema.
	 * 
	 * @return  
	 */
	@Get
	@Path("/")
	public ParametroSistemaGeralDTO index() {
		
		ParametroSistemaGeralDTO dto = psService.buscarParametroSistemaGeral();
		
		List<ItemDTO<String, String>> tiposAmbientes = new ArrayList<ItemDTO<String, String>>();
		for(TipoAmbiente ta : TipoAmbiente.values()) {
			tiposAmbientes.add(new ItemDTO<String, String>(ta.name(), ta.getDescricao()));
		}
		
		List<ItemDTO<String, String>> processosEmissao = new ArrayList<ItemDTO<String, String>>();
		for(ProcessoEmissao pe : ProcessoEmissao.values()) {
			processosEmissao.add(new ItemDTO<String, String>(pe.name(), pe.getDescricao()));
		}
		
		List<ItemDTO<String, String>> formatosImpressao = new ArrayList<ItemDTO<String, String>>();
		formatosImpressao.add(new ItemDTO<String, String>(FormatoImpressao.PAISAGEM.name(), FormatoImpressao.PAISAGEM.getDescricao()));
		
		result.include("parametroSistemaGeralDTO", dto);
		result.include("tiposAmbientes", tiposAmbientes);
		result.include("processosEmissaoNFe", processosEmissao);
		result.include("formatosImpressao", formatosImpressao);
		
		boolean utilizaFTF = pdService.getParametrosDistribuidor().getTipoAtividade().equals(TipoAtividade.PRESTADOR_FILIAL)
				&& dto.getNfeInformacoesTipoEmissor().equals(ProcessoEmissao.EMISSAO_NFE_APLICATIVO_CONTRIBUINTE.name());
		result.include("utilizaFTF", utilizaFTF);
						
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
