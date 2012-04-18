package br.com.abril.nds.controllers.cadastro;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.vo.CotaCobrancaVO;
import br.com.abril.nds.client.vo.ValidacaoVO;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.TipoCobranca;
import br.com.abril.nds.service.BancoService;
import br.com.abril.nds.service.FinanceiroService;
import br.com.abril.nds.util.Constantes;
import br.com.abril.nds.util.TipoMensagem;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.Validator;
import br.com.caelum.vraptor.validator.Message;
import br.com.caelum.vraptor.view.Results;

@Resource
@Path("/cadastro/financeiro")
public class FinanceiroController {
	
	private Result result;

	@Autowired
	private FinanceiroService financeiroService;
	
	@Autowired
	private BancoService bancoService;

	@Autowired
	private HttpSession session;
	
	@Autowired
	private Validator validator;	
	
	private static List<ItemDTO<Integer,String>> listaBancos =  new ArrayList<ItemDTO<Integer,String>>();

    private static List<ItemDTO<TipoCobranca,String>> listaTiposCobranca =  new ArrayList<ItemDTO<TipoCobranca,String>>();
    
	
	public FinanceiroController(Result result) {

		this.result = result;
	}

	@Path("/")
	public void index() {
		

		this.session.removeAttribute(Constantes.ATRIBUTO_SESSAO_FINANCEIRO_SALVAR);
		
		listaBancos = this.bancoService.getComboBancos();
		listaTiposCobranca = this.financeiroService.getComboTiposCobranca();
		result.include("listaBancos",listaBancos);
		result.include("listaTiposCobranca",listaTiposCobranca);
	}
	
	
	
	/*
	@Post
	@Path("/buscaComboBancos")
	public void buscaComboBancos(TipoCobranca tipoCobranca){
		listaBancos = this.financeiroService.getComboBancosTipoCobranca(tipoCobranca);
		result.include("listaBancos",listaBancos);
	}
    */

	
	
	/**
	 * Método responsável por obter os parametros de cobranca da Cota para a aba 'Financeiro'.
	 * @param idCota
	 */
	@Post
	@Path("/editarFinanceiro")
	public void editarFinanceiro(Integer numeroCota){
		
		if (numeroCota==null){
		    throw new ValidacaoException(TipoMensagem.WARNING, "informe a cota.");
		}

		CotaCobrancaVO cotaCobranca = this.financeiroService.obterDadosCotaCobranca(numeroCota);
		
		if (cotaCobranca==null) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum registro encontrado.");
		} 
		
		result.use(Results.json()).from(cotaCobranca,"result").recursive().serialize();
	}
	

	
	@Post
	@Path("/postarFinanceiro")
	public void postarFinanceiro(CotaCobrancaVO cotaCobranca){
		
		validar();
		
	    this.financeiroService.postarDadosCotaCobranca(cotaCobranca);
		
	    this.session.setAttribute(Constantes.ATRIBUTO_SESSAO_FINANCEIRO_SALVAR, cotaCobranca);
		
	    result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Parâmetros de cobrança alterados com sucesso."),Constantes.PARAM_MSGS).recursive().serialize();
	}

	
	
	public void validar(){
		if (validator.hasErrors()) {
			List<String> mensagens = new ArrayList<String>();
			for (Message message : validator.getErrors()) {
				mensagens.add(message.getMessage());
			}
			ValidacaoVO validacao = new ValidacaoVO(TipoMensagem.WARNING, mensagens);
			throw new ValidacaoException(validacao);
		}
	}
	
}
