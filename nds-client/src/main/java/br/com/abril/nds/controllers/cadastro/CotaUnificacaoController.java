package br.com.abril.nds.controllers.cadastro;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.vo.CotaVO;
import br.com.abril.nds.controllers.BaseController;
import br.com.abril.nds.dto.CotaUnificacaoDTO;
import br.com.abril.nds.serialization.custom.FlexiGridJson;
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
	
	public static String UNIFICACOES = "unificacoes";
	
	@Autowired
	private CotaUnificacaoService cotaUnificacaoService;
	
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
		
		Long politicaCobrancaId = 
			(Long) this.httpSession.getAttribute(ParametroCobrancaController.ID_EDICAO);
		
		List<CotaVO> cotasCentralizadas = 
			this.cotaUnificacaoService.obterCotasCentralizadas(numeroCotaCentralizadora, politicaCobrancaId);
		
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
        
		List<CotaUnificacaoDTO> lista = this.getUnificacoesSessao();
		
		for (CotaUnificacaoDTO dto : lista){
			
			if (dto.getNumeroCota().equals(numeroCotaCentralizadora)){
				
				lista.remove(dto);
				break;
			}
		}
		
		CotaUnificacaoDTO dto = new CotaUnificacaoDTO();
		dto.setNumeroCota(numeroCotaCentralizadora);
		
		ArrayList<CotaVO> cotas = new ArrayList<CotaVO>();
		for (Integer c :numeroCotasCentralizadas){
			
			cotas.add(new CotaVO(c, null));
		}
		
		dto.setCotas(cotas);
		
		lista.add(dto);
		
		this.httpSession.setAttribute(UNIFICACOES, lista);

		result.use(Results.json()).from("").serialize();
	}
	
	/**
     * Método de exclusão de unificação de cotas
     * 
     * @param cotaUnificacaoId
     */
	@Post
	public void excluirCotaUnificacao(Integer cotaUnificadora){
		
		List<CotaUnificacaoDTO> lista = this.getUnificacoesSessao();
		
		for (int index = 0 ; index < lista.size() ; index++){
			
			if (lista.get(index).getNumeroCota().equals(cotaUnificadora)){
				
				lista.remove(index);
				break;
			}
		}
		
		this.httpSession.setAttribute(UNIFICACOES, lista);
		
		this.result.use(Results.json()).from("").serialize();
	}
	
	@Post
	public void editarCotaUnificacao(Integer cotaUnificadora){
		
		List<CotaVO> cotas = new ArrayList<CotaVO>();
		for (CotaUnificacaoDTO dto : this.getUnificacoesSessao()){
			
			if (dto.getNumeroCota().equals(cotaUnificadora)){
				
				cotas.add(this.cotaUnificacaoService.obterCota(dto.getNumeroCota(), true, null));
				
				for (CotaVO vo : dto.getCotas()){
					
					cotas.add(this.cotaUnificacaoService.obterCota(vo.getNumero(), true, null));
				}
				
				break;
			}
		}
		
		result.use(Results.json()).from(cotas, "result").recursive().serialize();
	}
	
	@Post
	public void consultarCotasUnificadas(){
		
		List<CotaUnificacaoDTO> cotasUnificadas = this.getUnificacoesSessao();
		
		if (cotasUnificadas != null){
			
			result.use(FlexiGridJson.class).from(cotasUnificadas).page(1).total(cotasUnificadas.size()).serialize();
			return;
		}
		
		Long idEdicao = 
			(Long) this.httpSession.getAttribute(ParametroCobrancaController.ID_EDICAO);
		
		if (idEdicao == null){
			
			cotasUnificadas = new ArrayList<CotaUnificacaoDTO>();
		} else {
			
			cotasUnificadas = this.cotaUnificacaoService.obterCotasUnificadas(idEdicao);
		}
		
		result.use(FlexiGridJson.class).from(cotasUnificadas).page(1).total(cotasUnificadas.size()).serialize();
	}
	
	@Post
	public void buscarCota(Integer numeroCota, boolean edicao){
		
		CotaVO cotaVO = 
			this.cotaUnificacaoService.obterCota(numeroCota, edicao,
				(Long) this.httpSession.getAttribute(ParametroCobrancaController.ID_EDICAO));
		
		result.use(Results.json()).from(cotaVO, "result").recursive().serialize();
	}
	
	@SuppressWarnings("unchecked")
	private List<CotaUnificacaoDTO> getUnificacoesSessao(){
		
		List<CotaUnificacaoDTO> lista = 
			(List<CotaUnificacaoDTO>) this.httpSession.getAttribute(UNIFICACOES);
		
		if (lista == null){
			
			lista = new LinkedList<CotaUnificacaoDTO>();
			this.httpSession.setAttribute(UNIFICACOES, lista);
		}
		
		return lista;
	}
}
