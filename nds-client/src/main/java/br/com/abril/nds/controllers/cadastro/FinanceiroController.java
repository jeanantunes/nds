package br.com.abril.nds.controllers.cadastro;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.vo.FinanceiroVO;
import br.com.abril.nds.client.vo.ValidacaoVO;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.TipoCobranca;
import br.com.abril.nds.service.BancoService;
import br.com.abril.nds.service.CotaService;
import br.com.abril.nds.service.FinanceiroService;
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
	private CotaService cotaService;

	@Autowired
	private HttpSession session;
	
	@Autowired
	private Validator validator;	
	
	private static List<ItemDTO<Integer,String>> listaBancos =  new ArrayList<ItemDTO<Integer,String>>();

    private static List<ItemDTO<TipoCobranca,String>> listaTiposCobranca =  new ArrayList<ItemDTO<TipoCobranca,String>>();
    
    /**
	 * Constante que representa o nome do atributo com os dados de 'cota cobranca'
	 * armazenado na sessão para serem persistidos na base. 
	 */
	public static String ATRIBUTO_SESSAO_FINANCEIRO_SALVAR = "financeiroSalvarSessao";
	
	public FinanceiroController(Result result) {
		this.result = result;
	}

	@Path("/")
	public void index() {
		preCarregamento();
	}
	
	public void preCarregamento(){
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
	 * @throws Mensagens de Validação.
	 * @param idCota
	 */
	@Post
	@Path("/editarFinanceiro")
	public void editarFinanceiro(Long idCota){
		
		validar();
		
		if (idCota==null) {
			throw new ValidacaoException(TipoMensagem.WARNING, "O código da cota informado náo é válido.");
		} 

		FinanceiroVO cotaCobranca = this.financeiroService.obterDadosCotaCobranca(idCota);
		
		if (cotaCobranca==null) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Nenhuma cota encontrada para o código de cota informado.");
		} 
		
		result.use(Results.json()).from(cotaCobranca,"result").recursive().serialize();
	}
	

	/**
	 * Método responsável pos inserir na sessão os dados da aba "financeiro" do cadastro de cota.
	 * @param cotaCobranca: Value Object com os dados cadastrados ou alterados pelo usuário
	 */
	@SuppressWarnings("static-access")
	@Post
	@Path("/postarFinanceiroSessao")
	public void postarFinanceiroSessao(FinanceiroVO cotaCobranca){	
		
		validar();
		
		if (cotaCobranca.isRecebeEmail()){
			Cota cota = this.cotaService.obterPorNumeroDaCota(cotaCobranca.getNumCota());
			if (cota.getPessoa().getEmail()==null){
				throw new ValidacaoException(TipoMensagem.WARNING, "Cadastre um e-mail para a cota ou desmarque a opção de envio de email.");
			}
		}
		
	    this.session.setAttribute(this.ATRIBUTO_SESSAO_FINANCEIRO_SALVAR, cotaCobranca);
	    
	    result.nothing();
	}
	
	
	/**
	 * Método responsável por persistir os dados da aba "financeiro" no banco de dados.
	 */
    public void postarFinanceiro(Long idCota){
    	
		@SuppressWarnings("static-access")
		FinanceiroVO cotaCobranca = (FinanceiroVO) this.session.getAttribute(this.ATRIBUTO_SESSAO_FINANCEIRO_SALVAR);
		
		//COTA SENDO CADASTRADA OU EDITADA
		cotaCobranca.setIdCota(idCota);
		
	    this.financeiroService.postarDadosCotaCobranca(cotaCobranca);	
    }

	
	/**
	 * Método responsável pela validação dos dados e rotinas.
	 */
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
