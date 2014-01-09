package br.com.abril.nds.controllers.cadastro;

import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.controllers.BaseController;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.CotaUnificacao;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.service.CotaUnificacaoService;
import br.com.abril.nds.util.Constantes;
import br.com.abril.nds.vo.ValidacaoVO;
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
@Rules(Permissao.ROLE_FINANCEIRO_PARAMETROS_COBRANCA_ALTERACAO)
public class CotaUnificacaoController extends BaseController {
	
	@Autowired
	private CotaUnificacaoService cotaUnificacaoService;
	
    private Result result;
	    
    private HttpSession httpSession;
    
    /**
	 * Construtor da classe
	 * 
	 * @param result
	 * @param httpSession
	 * @param httpResponse
	 */
	public CotaUnificacaoController(Result result, HttpSession httpSession, HttpServletResponse httpResponse) {
		
		this.result = result;
		
		this.httpSession = httpSession;
	}
   
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
	@Path("/consultarCotaUnificacao")
	public void consultarCotaUnificacao(Integer numeroCotaCentralizadora){
		
		CotaUnificacao cotaUnificacao = this.cotaUnificacaoService.obterCotaUnificacaoPorCotaCentralizadora(numeroCotaCentralizadora);
		
		result.use(Results.json()).from(cotaUnificacao, "result").recursive().serialize();
	}
	
	/**
     * Método de validação de cota para unificação
     * 
     * @param cotaUnificacaoId
     * @param numeroCota
     */
	@Post
	@Path("/validarCotaUnificacao")
	public void validarCotaUnificacao(Long cotaUnificacaoId, Integer numeroCota){
		
		if(cotaUnificacaoId != null){
		
		    this.cotaUnificacaoService.validaAlteracaoUnificacaoCota(cotaUnificacaoId, numeroCota);
		}
		else{
			
			this.cotaUnificacaoService.validaNovaUnificacaoCota(numeroCota);
		}
	
        result.nothing();
	}

	/**
     * Método de cadastro/alteração de unificação de cotas
     * 
     * @param cotaUnificacaoId
     * @param numeroCotaCentralizadora
     * @param numeroCotaCentralizada
     */
	@Post
	@Path("/cadastrarCotaUnificacao")
	public void cadastrarCotaUnificacao(Long cotaUnificacaoId, 
			                            Integer numeroCotaCentralizadora, 
			                            List<Integer> numeroCotasCentralizadas){
		
		if (numeroCotaCentralizadora == null){
			
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, "Informe a Cota Centralizadora !"));
		}
		
        if (numeroCotasCentralizadas == null || numeroCotasCentralizadas.isEmpty()){
			
        	throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, "Informe ao menos uma Cota para Centralizar !"));
		}
        	
    	this.cotaUnificacaoService.salvarCotaUnificacao(cotaUnificacaoId, 
    			                                        numeroCotaCentralizadora, 
    			                                        numeroCotasCentralizadas);

		result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Unificação de Cotas Cadastrada."),Constantes.PARAM_MSGS).recursive().serialize();
	}
	
	/**
     * Método de exclusão de unificação de cotas
     * 
     * @param cotaUnificacaoId
     */
	@Post
	@Path("/excluirCotaUnificacao")
	public void excluirCotaUnificacao(Long cotaUnificacaoId){
		
		this.cotaUnificacaoService.removerCotaUnificacao(cotaUnificacaoId);
	
        result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Unificação de Cotas Excluída."),Constantes.PARAM_MSGS).recursive().serialize();
	}
}
