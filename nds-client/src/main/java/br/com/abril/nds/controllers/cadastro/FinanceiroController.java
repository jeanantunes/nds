package br.com.abril.nds.controllers.cadastro;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.vo.FinanceiroVO;
import br.com.abril.nds.client.vo.ValidacaoVO;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaBoletosCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaBoletosCotaDTO.OrdenacaoColunaBoletos;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.StatusCobranca;
import br.com.abril.nds.model.cadastro.ConcentracaoCobrancaCota;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.FormaCobranca;
import br.com.abril.nds.model.cadastro.ParametroCobrancaCota;
import br.com.abril.nds.model.cadastro.TipoCobranca;
import br.com.abril.nds.model.financeiro.Boleto;
import br.com.abril.nds.service.BancoService;
import br.com.abril.nds.service.CotaService;
import br.com.abril.nds.service.FinanceiroService;
import br.com.abril.nds.util.CellModel;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.TipoMensagem;
import br.com.abril.nds.util.Util;
import br.com.abril.nds.vo.PaginacaoVO;
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
    
    private static final String FILTRO_PESQUISA_SESSION_ATTRIBUTE = "filtroPesquisaConsultaCotasCobranca";
    
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
	
	/**
	 * Método responsável por obter os parametros de cobranca da Cota para a aba 'Financeiro'.
	 * @throws Mensagens de Validação.
	 * @param idCota
	 */
	@Post
	@Path("/populaFinanceiro")
	public void populaFinanceiro(Long idCota){
		
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
    @SuppressWarnings("static-access")
	public void postarFinanceiro(Long idCota){
    	
		FinanceiroVO cotaCobranca = (FinanceiroVO) this.session.getAttribute(this.ATRIBUTO_SESSAO_FINANCEIRO_SALVAR);
		
		//COTA SENDO CADASTRADA OU EDITADA
		cotaCobranca.setIdCota(idCota);
		
	    this.financeiroService.postarDadosCotaCobranca(cotaCobranca);	
	    
	    this.session.removeAttribute(this.ATRIBUTO_SESSAO_FINANCEIRO_SALVAR);
    }

    
    
    
    
    
    
    
    
    
    
    
    
    /**
     * !!!
     */
    @Post
	@Path("/consultaFormasCobranca")
	public void consultaFormasCobranca(Long idCota){
		
		//VALIDACOES
		validar();
		
		
		
		/*
		//CONFIGURAR PAGINA DE PESQUISA
		FiltroConsultaFormasCobrancaCotaDTO filtroAtual = new FiltroConsultaFormasCobrancaCotaDTO(idCota);
		
		PaginacaoVO paginacao = new PaginacaoVO(page, rp, sortorder);
		filtroAtual.setPaginacao(paginacao);
		filtroAtual.setOrdenacaoColuna(Util.getEnumByStringValue(OrdenacaoColunaFormasCobranca.values(), sortname));
	
		FiltroConsultaFormasCobrancaCotaDTO filtroSessao = (FiltroConsultaFormasCobrancaCotaDTO) this.httpSession.getAttribute(FILTRO_PESQUISA_SESSION_ATTRIBUTE);
		
		if (filtroSessao != null && !filtroSessao.equals(filtroAtual)) {
		
			filtroAtual.getPaginacao().setPaginaAtual(1);
		}
		
		this.httpSession.setAttribute(FILTRO_PESQUISA_SESSION_ATTRIBUTE, filtroAtual);
		*/
		
		
		
		//BUSCA FORMAS DE COBRANCA DA COTA
		List<FormaCobranca> formasCobranca = this.financeiroService.obterFormasCobrancaPorCota(/*filtroAtual*/);
		
		//CARREGA DIRETO DA ENTIDADE PARA A TABELA
		List<CellModel> listaModelo = new LinkedList<CellModel>();
		
		if (formasCobranca.size()==0) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum registro encontrado.");
		} 
		
		
		
		
		
		
		String concentracaoCobrancaExibir = "";
		/*
		Cota cota = this.cotaService.obterPorId(idCota);
		ParametroCobrancaCota parametroCobranca = cota.getParametroCobranca();
		Set<ConcentracaoCobrancaCota> concentracoesCobranca = parametroCobranca.getConcentracaoCobrancaCota();
		if ((concentracoesCobranca!=null)&&(concentracoesCobranca.size() > 0)){
			for (ConcentracaoCobrancaCota itemConcentracaoCobranca:concentracoesCobranca){
			    concentracaoCobrancaExibir=concentracaoCobrancaExibir+"/"+itemConcentracaoCobranca.getDiaSemana().name();
		    }
		}
        */
		for (FormaCobranca formaCobranca : formasCobranca){	
			listaModelo.add(new CellModel(1,
					                      "Fornecedor",
					                      concentracaoCobrancaExibir,
					                      formaCobranca.getTipoCobranca().getDescTipoCobranca(),
					                      formaCobranca.getBanco().getNome()+" : "+formaCobranca.getBanco().getAgencia()+" : "+formaCobranca.getBanco().getConta()+"-"+formaCobranca.getBanco().getDvConta(),
										  ""
                      					  )
              );
		}	
		
		
		
		
		
		TableModel<CellModel> tm = new TableModel<CellModel>();

		
		
		//DEFINE TOTAL DE REGISTROS NO TABLEMODEL
		//tm.setTotal( (int) this.financeiroService.obterQuantidadeFormasCobrancaPorCota(filtroAtual));
		tm.setTotal( 10 );
		
		
		
		//DEFINE CONTEUDO NO TABLEMODEL
		tm.setRows(listaModelo);
		
		
		
		//DEFINE PAGINA ATUAL NO TABLEMODEL
		//tm.setPage(filtroAtual.getPaginacao().getPaginaAtual());
		tm.setPage(1);
		
		
		
		//PREPARA RESULTADO PARA A VIEW (HASHMAP)
		Map<String, TableModel<CellModel>> resultado = new HashMap<String, TableModel<CellModel>>();
		resultado.put("TblModelFormasCobranca", tm);
		
		//RETORNA HASHMAP EM FORMATO JSON PARA A VIEW
		result.use(Results.json()).withoutRoot().from(resultado).recursive().serialize();
        
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
