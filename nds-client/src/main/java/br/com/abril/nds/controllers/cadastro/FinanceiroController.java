package br.com.abril.nds.controllers.cadastro;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.vo.ValidacaoVO;
import br.com.abril.nds.dto.FormaCobrancaDTO;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.ParametroCobrancaDTO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.TipoCobranca;
import br.com.abril.nds.model.cadastro.TipoFormaCobranca;
import br.com.abril.nds.service.BancoService;
import br.com.abril.nds.service.CotaService;
import br.com.abril.nds.service.FinanceiroService;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.Constantes;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.TipoMensagem;
import br.com.caelum.vraptor.Get;
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
    
    private HttpServletResponse httpResponse;
	
	private static List<ItemDTO<Integer,String>> listaBancos =  new ArrayList<ItemDTO<Integer,String>>();

    private static List<ItemDTO<TipoCobranca,String>> listaTiposCobranca =  new ArrayList<ItemDTO<TipoCobranca,String>>();
     
    
    /**
	 * Constante que representa o nome do atributo com os dados de 'cota cobranca'
	 * armazenado na sessão para serem persistidos na base. 
	 */
	public static String ATRIBUTO_SESSAO_FINANCEIRO_SALVAR = "financeiroSalvarSessao";
	
	
	/**
	 * Construtor da classe
	 * @param result
	 * @param session
	 * @param httpResponse
	 */
	public FinanceiroController(Result result, HttpSession httpSession, HttpServletResponse httpResponse) {
		this.result = result;
		this.session = httpSession;
		this.httpResponse = httpResponse;
	}
	
	
	/**
	 * Método de chamada da página
	 * Pré-carrega itens da pagina com informações default.
	 */
	@Get
	public void index() {
		preCarregamento();
	}
	
	
	/**
	 * Método de Pré-carregamento de itens da pagina com informações default.
	 */
	public void preCarregamento(){
		listaBancos = this.bancoService.getComboBancos();
		listaTiposCobranca = this.financeiroService.getComboTiposCobranca();
		result.include("listaBancos",listaBancos);
		result.include("listaTiposCobranca",listaTiposCobranca);
		/*
		Set<Fornecedor> fornecedores = this.cotaService.obterFornecedoresCota(1l);//PARAMETRO IDCOTA
		result.include("fornecedores",fornecedores);
		*/
	}
	
	
	/**
	 * Método responsável por obter os parametros de cobranca da Cota para a aba 'Financeiro'.
	 * @throws Mensagens de Validação.
	 * @param idCota
	 */
	@Post
	@Path("/obterParametroCobranca")
	public void obterParametroCobranca(Long idCota){
		
		validar();
		
		if (idCota==null) {
			throw new ValidacaoException(TipoMensagem.WARNING, "O código da cota informado náo é válido.");
		} 

		ParametroCobrancaDTO parametroCobranca = this.financeiroService.obterDadosParametroCobrancaPorCota(idCota);
		
		if (parametroCobranca==null) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Nenhuma cota encontrada para o código de cota informado.");
		} 

		result.use(Results.json()).from(parametroCobranca,"result").recursive().serialize();
	}
	
	
	/**
	 * Método responsável por obter os dados da uma forma de cobranca do parametro de cobranca da Cota para a aba 'Financeiro'.
	 * @throws Mensagens de Validação.
	 * @param idFormaCobranca
	 */
	@Post
	@Path("/obterFormaCobranca")
	public void obterFormaCobranca(Long idFormaCobranca){
		
		validar();
		
		if (idFormaCobranca==null) {
			throw new ValidacaoException(TipoMensagem.WARNING, "O código da forma de cobrança informado náo é válido.");
		} 

		FormaCobrancaDTO formaCobranca = this.financeiroService.obterDadosFormaCobranca(idFormaCobranca);
		
		if (formaCobranca==null) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Nenhuma forma de cobrança encontrada para o código de forma de cobrança.");
		} 
		
		result.use(Results.json()).from(formaCobranca,"result").recursive().serialize();
	}

	
	/**
     * Retorna formas de cobrança da cota para preencher a grid da view
     * @param idCota
     */
    @Post
	@Path("/obterFormasCobranca")
	public void obterFormasCobranca(Long idCota){
		
		//VALIDACOES
		validar();	
		
		//BUSCA FORMAS DE COBRANCA DA COTA
		List<FormaCobrancaDTO> listaFormasCobranca = this.financeiroService.obterDadosFormasCobrancaPorCota(idCota);
		int qtdRegistros = this.financeiroService.obterQuantidadeFormasCobrancaCota(idCota);
				
		TableModel<CellModelKeyValue<FormaCobrancaDTO>> tableModel =
				new TableModel<CellModelKeyValue<FormaCobrancaDTO>>();
			
		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listaFormasCobranca));
		tableModel.setPage(1);
		tableModel.setTotal(qtdRegistros);
		
		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
    }

    
	/**
	 * Método responsável por postar na sessão os dados da aba "financeiro" do cadastro de cota.
	 * @param cotaCobranca: Data Transfer Object com os dados cadastrados ou alterados pelo usuário
	 */
	@SuppressWarnings("static-access")
	@Post
	@Path("/postarParametroCobrancaSessao")
	public void postarParametroCobrancaSessao(ParametroCobrancaDTO parametroCobranca){	
		
		validar();
		
	    this.session.setAttribute(this.ATRIBUTO_SESSAO_FINANCEIRO_SALVAR, parametroCobranca);

	    result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Parametros de Cobrança Cadastrados."),Constantes.PARAM_MSGS).recursive().serialize();
	}
	
	
	/**
	 *Formata os dados de FormaCobranca, apagando valores que não são compatíveis com o Tipo de Cobranca escolhido.
	 * @param formaCobranca
	 */
	private FormaCobrancaDTO formatarFormaCobranca(FormaCobrancaDTO formaCobranca){
		if ((formaCobranca.getTipoCobranca()==TipoCobranca.BOLETO)||(formaCobranca.getTipoCobranca()==TipoCobranca.BOLETO_EM_BRANCO)){
	    	formaCobranca.setNumBanco("");
			formaCobranca.setNomeBanco("");
			formaCobranca.setAgencia("");
			formaCobranca.setAgenciaDigito("");
			formaCobranca.setConta("");
		    formaCobranca.setContaDigito("");
	    }
		else if ((formaCobranca.getTipoCobranca()==TipoCobranca.CHEQUE)||(formaCobranca.getTipoCobranca()==TipoCobranca.TRANSFERENCIA_BANCARIA)){
			formaCobranca.setRecebeEmail(false);
		}    
		else if (formaCobranca.getTipoCobranca()==TipoCobranca.DEPOSITO){
			formaCobranca.setRecebeEmail(false);
			formaCobranca.setNumBanco("");
			formaCobranca.setNomeBanco("");
			formaCobranca.setAgencia("");
			formaCobranca.setAgenciaDigito("");
			formaCobranca.setConta("");
		    formaCobranca.setContaDigito("");
		}    
		else{
			formaCobranca.setRecebeEmail(false);
			formaCobranca.setNumBanco("");
			formaCobranca.setNomeBanco("");
			formaCobranca.setAgencia("");
			formaCobranca.setAgenciaDigito("");
			formaCobranca.setConta("");
		    formaCobranca.setContaDigito("");
		    formaCobranca.setIdBanco(0);
		}
		return formaCobranca;
	}
	
	
	@Post
	@Path("/postarFormaCobranca")
	public void postarFormaCobranca(FormaCobrancaDTO formaCobranca, String tipoFormaCobranca, List<Long> listaIdsFornecedores){	
		
		formaCobranca = formatarFormaCobranca(formaCobranca);
		
		validarFormaCobranca(formaCobranca);
		
		formaCobranca.setFornecedoresId(listaIdsFornecedores);
		formaCobranca.setTipoFormaCobranca(TipoFormaCobranca.valueOf(tipoFormaCobranca));
		this.financeiroService.postarFormaCobranca(formaCobranca);	
	    
		result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Forma de Cobrança Cadastrado."),Constantes.PARAM_MSGS).recursive().serialize();
	}
	
	
	/**
	 * Método responsável por persistir os dados da aba "financeiro" no banco de dados.
	 */
    @SuppressWarnings("static-access")
	public void postarParametroCobranca(Long idCota){
    	
		ParametroCobrancaDTO parametroCobranca = (ParametroCobrancaDTO) this.session.getAttribute(this.ATRIBUTO_SESSAO_FINANCEIRO_SALVAR);
		
		//COTA SENDO CADASTRADA OU EDITADA
		parametroCobranca.setIdCota(idCota);
		
	    this.financeiroService.postarParametroCobranca(parametroCobranca);	
	    
	    this.session.removeAttribute(this.ATRIBUTO_SESSAO_FINANCEIRO_SALVAR);
    }
  
    
    /**
     * Método responsável por desativar Forma de Cobranca
     * @param idFormaCobranca
     */
    @Post
	@Path("/excluirFormaCobranca")
	public void excluirFormaCobranca(Long idFormaCobranca){	

		this.financeiroService.excluirFormaCobranca(idFormaCobranca);	
	    
		result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Forma de Cobrança Excluida."),Constantes.PARAM_MSGS).recursive().serialize();
	}
    
    
    /**
	 * Exibe o contrato em formato PDF.
	 * @param idCota
	 * @throws Exception
	 */
	@Get
	@Path("/imprimeContrato")
	public void imprimeContrato(Long idCota) throws Exception{

		byte[] b = this.financeiroService.geraImpressaoContrato(idCota);

		this.httpResponse.setContentType("application/pdf");
		this.httpResponse.setHeader("Content-Disposition", "attachment; filename=contrato.pdf");

		OutputStream output = this.httpResponse.getOutputStream();
		output.write(b);

		httpResponse.flushBuffer();
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
	
	
	/**
	 * Método responsável pela validação dos dados da Forma de Cobranca.
	 */
	public void validarFormaCobranca(FormaCobrancaDTO formaCobranca){
		
		validar();
		
		if(formaCobranca.getTipoCobranca()==null){
			throw new ValidacaoException(TipoMensagem.WARNING, "Escolha um Tipo de Pagamento.");
		}
		
		if (formaCobranca.getIdBanco()<=0){
		    if ((formaCobranca.getTipoCobranca()==TipoCobranca.BOLETO)||
		    	(formaCobranca.getTipoCobranca()==TipoCobranca.BOLETO_EM_BRANCO)||
		    	(formaCobranca.getTipoCobranca()==TipoCobranca.CHEQUE)||
		    	(formaCobranca.getTipoCobranca()==TipoCobranca.TRANSFERENCIA_BANCARIA)||
		    	(formaCobranca.getTipoCobranca()==TipoCobranca.DEPOSITO)){
		    	throw new ValidacaoException(TipoMensagem.WARNING, "Para o Tipo de Cobrança selecionado é necessário a escolha de um Banco.");
		    }
		}
		
		if ((formaCobranca.getTipoCobranca()==TipoCobranca.CHEQUE)||
		    (formaCobranca.getTipoCobranca()==TipoCobranca.TRANSFERENCIA_BANCARIA)){
			
			if((formaCobranca.getNomeBanco()==null) || (!"".equals(formaCobranca.getNomeBanco()))){
				throw new ValidacaoException(TipoMensagem.WARNING, "Para o Tipo de Cobrança selecionado é necessário digitar o nome do Banco.");
			}
			if((formaCobranca.getNumBanco()==null) || (!"".equals(formaCobranca.getNumBanco()))){
				throw new ValidacaoException(TipoMensagem.WARNING, "Para o Tipo de Cobrança selecionado é necessário digitar o numero do Banco.");
			}
			
			if((formaCobranca.getConta()==null) || (!"".equals(formaCobranca.getConta()))){
				throw new ValidacaoException(TipoMensagem.WARNING, "Para o Tipo de Cobrança selecionado é necessário digitar o numero da Conta.");
			}
			if((formaCobranca.getContaDigito()==null) || (!"".equals(formaCobranca.getContaDigito()))){
				throw new ValidacaoException(TipoMensagem.WARNING, "Para o Tipo de Cobrança selecionado é necessário digitar o dígito da Conta.");
			}
			
			if((formaCobranca.getAgencia()==null) || (!"".equals(formaCobranca.getAgencia()))){
				throw new ValidacaoException(TipoMensagem.WARNING, "Para o Tipo de Cobrança selecionado é necessário digitar o numero da Agência.");
			}
			if((formaCobranca.getAgenciaDigito()==null) || (!"".equals(formaCobranca.getAgenciaDigito()))){
				throw new ValidacaoException(TipoMensagem.WARNING, "Para o Tipo de Cobrança selecionado é necessário digitar o dígito da Agência.");
			}
		}
		
		if (formaCobranca.isRecebeEmail()){
			Cota cota = this.cotaService.obterPorId(formaCobranca.getIdCota());
			if (cota.getPessoa().getEmail()==null){
				throw new ValidacaoException(TipoMensagem.WARNING, "Cadastre um e-mail para a cota ou desmarque a opção de envio de email.");
			}
		}
	}
	
	
}
