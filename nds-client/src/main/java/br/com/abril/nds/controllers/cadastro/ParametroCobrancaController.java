package br.com.abril.nds.controllers.cadastro;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.client.vo.BancoVO;
import br.com.abril.nds.client.vo.ParametroCobrancaVO;
import br.com.abril.nds.controllers.BaseController;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.ParametroCobrancaDTO;
import br.com.abril.nds.dto.filtro.FiltroParametrosCobrancaDTO;
import br.com.abril.nds.dto.filtro.FiltroParametrosCobrancaDTO.OrdenacaoColunaParametrosCobranca;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.FormaEmissao;
import br.com.abril.nds.model.cadastro.PoliticaCobranca;
import br.com.abril.nds.model.cadastro.TipoCobranca;
import br.com.abril.nds.model.cadastro.TipoCota;
import br.com.abril.nds.model.cadastro.TipoFormaCobranca;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.serialization.custom.FlexiGridJson;
import br.com.abril.nds.service.BancoService;
import br.com.abril.nds.service.CotaService;
import br.com.abril.nds.service.FormaCobrancaService;
import br.com.abril.nds.service.FornecedorService;
import br.com.abril.nds.service.ParametroCobrancaCotaService;
import br.com.abril.nds.service.PoliticaCobrancaService;
import br.com.abril.nds.util.Util;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.abril.nds.vo.ValidacaoVO;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

/**
 * Controller responsável pela tela de consulta, cadastro e alteração de Bancos. 
 * 
 * @author Discover Technology
 *
 */
@Resource
@Path("/distribuidor/parametroCobranca")
@Rules(Permissao.ROLE_FINANCEIRO_PARAMETROS_COBRANCA)
public class ParametroCobrancaController extends BaseController {
	
	@Autowired
	private PoliticaCobrancaService politicaCobrancaService;
	
	@Autowired
	private BancoService bancoService;
	
	@Autowired
	private FornecedorService fornecedorService;
	
	@Autowired
	private ParametroCobrancaCotaService parametroCobrancaCotaService;
	
	@Autowired
	private CotaService cotaService;
	
	@Autowired
	private FormaCobrancaService formaCobrancaService;
	
    private Result result;
    
    private HttpSession httpSession;
    
    private static List<ItemDTO<Integer,String>> listaBancos =  new ArrayList<ItemDTO<Integer,String>>();

    private static List<ItemDTO<TipoCobranca,String>> listaTiposCobranca =  new ArrayList<ItemDTO<TipoCobranca,String>>();
    
    private static List<ItemDTO<FormaEmissao,String>> listaFormasEmissao =  new ArrayList<ItemDTO<FormaEmissao,String>>();
    
    private static List<ItemDTO<Long,String>> listaFornecedores =  new ArrayList<ItemDTO<Long,String>>();
    
    private static final String FILTRO_PESQUISA_SESSION_ATTRIBUTE = "filtroPesquisaParametrosCobranca";
    
    private static List<ItemDTO<TipoCota,String>> listaTiposCota = new ArrayList<ItemDTO<TipoCota,String>>();
    
    /**
	 * Construtor da classe
	 * @param result
	 * @param httpSession
	 * @param httpResponse
	 */
	public ParametroCobrancaController(Result result, HttpSession httpSession, HttpServletResponse httpResponse) {
		this.result = result;
		this.httpSession = httpSession;
	}
   
	
	
    /**
     * Método de chamada da página
     */
    @Get
    @Path("/")
    public void index(){ 
    	
    	listaBancos.clear();
    	listaTiposCobranca.clear();
    	listaFornecedores.clear();
    	listaTiposCota.clear();
    	
    	listaBancos = this.bancoService.getComboBancos(true);
    	listaTiposCobranca = this.parametroCobrancaCotaService.getComboTiposCobranca();
    	listaFornecedores = this.fornecedorService.obterFornecedoresIdNome(null, null);
    	listaTiposCota = this.cotaService.getComboTiposCota();
    	
		result.include("listaBancos", listaBancos);
		result.include("listaTiposCobranca", listaTiposCobranca);
		result.include("listaFornecedores", listaFornecedores);
		result.include("listaTiposCota", listaTiposCota);
	}
    
    

    /**
     * Método de consulta de Parametros de Cobrança
     * @param idBanco: ID do Banco
     * @param TipoCobranca
     * @param sortorder
     * @param sortname
     * @param page
     * @param rp
     * @throws Mensagem de nenhum registro encontrado
     */
	@Post
	@Path("/consultaParametrosCobranca")
	public void consultaParametrosCobranca(Long idBanco, 
			                               TipoCobranca tipoCobranca,
			                               String sortorder, 
			                               String sortname, 
			                               int page, 
			                               int rp){
		
		//CONFIGURAR PAGINA DE PESQUISA
		FiltroParametrosCobrancaDTO filtroAtual = new FiltroParametrosCobrancaDTO(idBanco, tipoCobranca);
		PaginacaoVO paginacao = new PaginacaoVO(page, rp, sortorder);
		filtroAtual.setPaginacao(paginacao);
		filtroAtual.setOrdenacaoColuna(Util.getEnumByStringValue(OrdenacaoColunaParametrosCobranca.values(), sortname));
	
		FiltroParametrosCobrancaDTO filtroSessao = (FiltroParametrosCobrancaDTO) this.httpSession.getAttribute(FILTRO_PESQUISA_SESSION_ATTRIBUTE);
		
		if (filtroSessao != null && !filtroSessao.equals(filtroAtual)) {
			filtroAtual.getPaginacao().setPaginaAtual(1);
		}
		
		this.httpSession.setAttribute(FILTRO_PESQUISA_SESSION_ATTRIBUTE, filtroAtual);
		int qtdRegistros = this.politicaCobrancaService.obterQuantidadePoliticasCobranca(filtroAtual);
		
		
		
		if (qtdRegistros <=0) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum registro encontrado.");
		} 
		List<ParametroCobrancaVO> parametrosCobranca = this.politicaCobrancaService.obterDadosPoliticasCobranca(filtroAtual);

		result.use(FlexiGridJson.class).from(parametrosCobranca).page(page).total(qtdRegistros).serialize();
	}
	
	
	
	/**
	 * Método responsável pela inclusão de novo parametro de cobrança
	 * @param ParametroCobrancaDTO
	 */
	@Post
	@Path("/postarParametroCobranca")
	@Rules(Permissao.ROLE_FINANCEIRO_PARAMETROS_COBRANCA_ALTERACAO)
	public void postarParametroCobranca(ParametroCobrancaDTO parametros) {

		PoliticaCobranca politica = politicaCobrancaService.obterPoliticaCobrancaPrincipal();
		
		if ((politica==null || politica.getId().equals(parametros.getIdPolitica())) && !parametros.isPrincipal()){
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Defina ao menos um [Parâmetro de Cobrança] como [Principal].");
		}
		
		parametros = formatarParametros(parametros);
		
		validarParametros(parametros);
		
		this.politicaCobrancaService.postarPoliticaCobranca(parametros);
        
        result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Parâmetro de cobrança cadastrado com sucesso."),"result").recursive().serialize();
	}
	
	

	/**
	 * Obtém dados do parametro de cobrança
	 * @param idPolitica
	 */
	@Post
	@Path("/obterParametroCobranca")
	public void obterParametroCobranca(Long idPolitica) {
				
		if (idPolitica==null) {
			throw new ValidacaoException(TipoMensagem.WARNING, "O código da política de cobrança informado náo é válido.");
		} 

		ParametroCobrancaDTO parametroCobranca = this.politicaCobrancaService.obterDadosPoliticaCobranca(idPolitica);
		
		if (parametroCobranca==null) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Nenhuma política de cobrança encontrada.");
		}
		
		result.use(Results.json()).from(parametroCobranca,"result").recursive().serialize();
	}
	
	
	/**
	 * Obtém dados do banco escolhido
	 * @param idBanco
	 */
	@Post
	@Path("/obterDadosBancarios")
	public void obterDadosBancarios(Long idBanco){
		BancoVO bancoVO = this.bancoService.obterDadosBanco(idBanco);
		result.use(Results.json()).from(bancoVO, "result").recursive().serialize();
	}

	
	
	/**
	 * Obtém formas de emissão de acordo com o Tipo de Cobrança
	 * @param idBanco
	 */
	@Post
	@Path("/obterFormasEmissao")
	public void obterFormasEmissao(TipoCobranca tipoCobranca){
		listaFormasEmissao.clear();
		if (tipoCobranca==null){
			listaFormasEmissao = this.politicaCobrancaService.getComboFormasEmissao();
		}
		else{
			listaFormasEmissao = this.politicaCobrancaService.getComboFormasEmissaoTipoCobranca(tipoCobranca); 
		}
		result.use(Results.json()).from(listaFormasEmissao, "result").recursive().serialize();
	}

	
	
    /**
     * Desativa política de cobrança
     * @param idPolitica
     */
	@Post
	@Path("/desativaParametroCobranca")
	@Rules(Permissao.ROLE_FINANCEIRO_PARAMETROS_COBRANCA_ALTERACAO)
	public void desativaParametroCobranca(Long idPolitica){
		
		this.politicaCobrancaService.dasativarPoliticaCobranca(idPolitica);
		
		result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Parâmetro de cobrança desativado com sucesso."),"result").recursive().serialize();
    }
	
	
	
	/**
	 *Formata os dados dos Parâmetros de cobrança, apagando valores que não são compatíveis com o Tipo de Cobranca escolhido.
	 * @param ParametroCobrancaDTO
	 */
	private ParametroCobrancaDTO formatarParametros(ParametroCobrancaDTO parametros){
		
		if (parametros.getTipoFormaCobranca()==TipoFormaCobranca.SEMANAL){
			parametros.setDiasDoMes(null);
		}
		
		if (parametros.getTipoFormaCobranca()==TipoFormaCobranca.MENSAL){
			parametros.setDomingo(false);
			parametros.setSegunda(false);
			parametros.setTerca(false);
			parametros.setQuarta(false);
			parametros.setQuinta(false);
			parametros.setSexta(false);
			parametros.setSabado(false);
		}
		
		if ((parametros.getTipoCobranca()==TipoCobranca.BOLETO_EM_BRANCO)){
			parametros.setTaxaJuros(null);
			parametros.setTaxaMulta(null);
			parametros.setValorMulta(null);
	    }
		else if ((parametros.getTipoCobranca()==TipoCobranca.CHEQUE)||(parametros.getTipoCobranca()==TipoCobranca.DINHEIRO)){
			parametros.setIdBanco(null);
		} else if (parametros.getTipoCobranca()==TipoCobranca.OUTROS){
			parametros.setIdBanco(null);
		} else if ((parametros.getTipoCobranca()==TipoCobranca.BOLETO_AVULSO)){
			parametros.setTaxaJuros(null);
			parametros.setTaxaMulta(null);
			parametros.setValorMulta(null);
	    }   
        
		return parametros;
		
	}

	
	
	/**
	 * Método responsável pela validação dos dados dos Parâmetros de Cobrança.
	 * @param ParametroCobrancaDTO
	 */
	public void validarParametros(ParametroCobrancaDTO parametros){
				
		if(parametros.getTipoCobranca()==null){
			throw new ValidacaoException(TipoMensagem.WARNING, "Escolha um Tipo de Pagamento.");
		}
		
		if (parametros.getTipoFormaCobranca()==null){
			throw new ValidacaoException(TipoMensagem.WARNING, "Selecione um tipo de concentração de Pagamentos.");
		}
		
		if (parametros.getFormaEmissao()==null){
			throw new ValidacaoException(TipoMensagem.WARNING, "Preencha o campo Impressão.");
		}
		
		if(parametros.getIdFornecedorPadrao() == null) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Escolha um Fornecedor Padrão.");
		}
		
		if (parametros.getIdBanco()==null){
		    if ((parametros.getTipoCobranca()==TipoCobranca.BOLETO)||
		    	(parametros.getTipoCobranca()==TipoCobranca.BOLETO_EM_BRANCO)||
		    	(parametros.getTipoCobranca()==TipoCobranca.TRANSFERENCIA_BANCARIA)||
		    	(parametros.getTipoCobranca()==TipoCobranca.DEPOSITO)){
		    	throw new ValidacaoException(TipoMensagem.WARNING, "Para o Tipo de Cobrança selecionado é necessário a escolha de um Banco.");
		    }
		}
		
		if(parametros.getTipoFormaCobranca()==TipoFormaCobranca.MENSAL || parametros.getTipoFormaCobranca()==TipoFormaCobranca.QUINZENAL){
			if (parametros.getDiasDoMes()==null || parametros.getDiasDoMes().isEmpty() || parametros.getDiasDoMes().get(0)==null){
				throw new ValidacaoException(TipoMensagem.WARNING, "Para o tipo de cobrança Mensal é necessário informar o dia do mês.");
			}
			else{
				if ((parametros.getDiasDoMes().get(0)>31)||(parametros.getDiasDoMes().get(0)<1)){
					throw new ValidacaoException(TipoMensagem.WARNING, "Dia do mês inválido.");
				}
			}
		}
		
		if(parametros.getTipoFormaCobranca()==TipoFormaCobranca.QUINZENAL){
			if ((parametros.getDiasDoMes().get(0)==null) || (parametros.getDiasDoMes().get(1)==null)){
				throw new ValidacaoException(TipoMensagem.WARNING, "Para o tipo de cobrança Quinzenal é necessário informar dois dias do mês.");
			}
			else{
				if ((parametros.getDiasDoMes().get(0)>31)||(parametros.getDiasDoMes().get(0)<1)||(parametros.getDiasDoMes().get(1)>31)||(parametros.getDiasDoMes().get(1)<1)){
					throw new ValidacaoException(TipoMensagem.WARNING, "Dia do mês inválido.");
				}
			}
		}
		
		if(parametros.getTipoFormaCobranca()==TipoFormaCobranca.SEMANAL){
			if((!parametros.isDomingo())&&
			   (!parametros.isSegunda())&&
			   (!parametros.isTerca())&&
			   (!parametros.isQuarta())&&
			   (!parametros.isQuinta())&&
			   (!parametros.isSexta())&&
			   (!parametros.isSabado())){
				throw new ValidacaoException(TipoMensagem.WARNING, "Para o tipo de cobrança Semanal é necessário marcar ao menos um dia da semana.");      	
			}
		}
		
		
		if(parametros.getTipoCobranca().equals(TipoCobranca.BOLETO_AVULSO)) {
			
			boolean isTipoBoleto = formaCobrancaService.validarFormaCobrancaPorBanco(parametros.getIdBanco());
			
			if (!isTipoBoleto) {
				throw new ValidacaoException(TipoMensagem.WARNING, "Para Forma de Cobrança Boleto Avulso é necessário utilizar um Banco trabalhe com boleto.");   
			}
		}
		
		
	}
}
