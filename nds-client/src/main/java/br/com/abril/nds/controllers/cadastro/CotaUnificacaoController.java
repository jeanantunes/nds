package br.com.abril.nds.controllers.cadastro;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.vo.CotaVO;
import br.com.abril.nds.controllers.BaseController;
import br.com.abril.nds.dto.ControleCotaDTO;
import br.com.abril.nds.dto.CotaUnificacaoDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.serialization.custom.FlexiGridJson;
import br.com.abril.nds.service.ControleCotaService;
import br.com.abril.nds.service.CotaUnificacaoService;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

/**
 * Controller responsável pela tela de consulta, cadastro e alteração de Unificação de Cotas. 
 * 
 * @author Discover Technology
 *
 */

@Resource
@Path("/cadastro/cotaUnificacao")
//@Rules(Permissao.ROLE_FINANCEIRO_PARAMETROS_COBRANCA)
public class CotaUnificacaoController extends BaseController {
	
	@Autowired
	private CotaUnificacaoService cotaUnificacaoService;
	
	@Autowired
	private ControleCotaService controleCotaService;
	
	@Autowired
    private Result result;
	
	@Autowired
	private HttpSession httpSession;
	
    /**
     * Método de chamada da página
     */
    @Get
    @Path("/")
    public void cotaUnificacao(){ 
   		
	}
    
    /**
     * Método de consulta de unificação de cotas
     * 
     * @param numeroCotaCentralizadora
     */
	@Post
	public void consultarCotaUnificacao(Integer numeroCotaCentralizadora){
		
		List<CotaVO> cotasCentralizadas = 
			this.cotaUnificacaoService.obterCotasCentralizadas(numeroCotaCentralizadora);
		
		result.use(Results.json()).from(cotasCentralizadas, "result").recursive().serialize();
	}

	/**
     * Método de cadastro/alteração de unificação de cotas
     * 
     * @param cotaUnificacaoId
     * @param numeroCotaCentralizadora
     * @param numeroCotaCentralizada
     */
	@Post
	public void cadastrarCotaUnificacao(Integer numeroCotaCentralizadora, 
			                            List<Integer> numeroCotasCentralizadas){
		
		this.cotaUnificacaoService.salvarCotaUnificacao(numeroCotaCentralizadora, numeroCotasCentralizadas);

		result.use(Results.json()).from("").serialize();
	}
	
	
	@Post
	public void cadastrarCotaUnificacaoMaster(Integer numeroCotaCentralizadora, 
			                            List<Integer> numeroCotasCentralizadas){
		
		this.controleCotaService.salvarCotaUnificacao(numeroCotaCentralizadora, numeroCotasCentralizadas);

		result.use(Results.json()).from("").serialize();
	}
	
	/**
     * Método de exclusão de unificação de cotas
     * 
     * @param cotaUnificacaoId
     */
	@Post
	public void excluirCotaUnificacao(Integer cotaUnificadora){
		
		this.cotaUnificacaoService.removerCotaUnificacao(cotaUnificadora);
		
		this.result.use(Results.json()).from("").serialize();
	}
	
	
	/**
     * Método de exclusão de unificação de cotas
     * 
     * @param cotaUnificacaoId
     */
	@Post
	public void excluirCotaUnificacaoMaster(Long id){
		
		this.controleCotaService.excluirControleCota(id);
		
		this.result.use(Results.json()).from("").serialize();
	}
	
	@Post
	public void editarCotaUnificacao(Integer cotaUnificadora){
		
		List<CotaVO> cotas = new ArrayList<CotaVO>();
		
		cotas.add(this.cotaUnificacaoService.obterCota(cotaUnificadora, true));
		
		cotas.addAll(this.cotaUnificacaoService.obterCotasCentralizadas(cotaUnificadora));
		
		result.use(Results.json()).from(cotas, "result").recursive().serialize();
	}
	
	@Post
	public void consultarCotasUnificadas(){
		
		List<CotaUnificacaoDTO> cotasUnificadas = this.cotaUnificacaoService.obterCotasUnificadas();
		
		if (cotasUnificadas != null){
			
			result.use(FlexiGridJson.class).from(cotasUnificadas).page(1).total(cotasUnificadas.size()).serialize();
			return;
		}
		
		cotasUnificadas = this.cotaUnificacaoService.obterCotasUnificadas();
		
		result.use(FlexiGridJson.class).from(cotasUnificadas).page(1).total(cotasUnificadas.size()).serialize();
	}
	
	@Post
	public void consultarCotasUnificadasMaster(){
		
		List<ControleCotaDTO> cotasUnificadas = this.controleCotaService.buscarControleCota();
		
		if (cotasUnificadas != null){
			
			result.use(FlexiGridJson.class).from(cotasUnificadas).page(1).total(cotasUnificadas.size()).serialize();
			return;
		}
		
		return;
	}
	
	@Post
	public void buscarCota(Integer numeroCota, boolean edicao){
		
		CotaVO cotaVO = 
			this.cotaUnificacaoService.obterCota(numeroCota, edicao);
		
		if (cotaVO == null) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, String.format("Cota %s não encontrada.", numeroCota));
			
		} else {

			this.result.use(Results.json()).from(cotaVO, "result").recursive().serialize();
		}
		
	}
}
