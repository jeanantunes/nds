package br.com.abril.nds.controllers.cadastro;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.vo.ParametroCobrancaVO;
import br.com.abril.nds.client.vo.ValidacaoVO;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.ParametroCobrancaDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaParametrosCobrancaDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaParametrosCobrancaDTO.OrdenacaoColunaParametrosCobranca;
import br.com.abril.nds.model.cadastro.Banco;
import br.com.abril.nds.model.cadastro.FormaEmissao;
import br.com.abril.nds.model.cadastro.TipoCobranca;
import br.com.abril.nds.service.BancoService;
import br.com.abril.nds.service.ParametroCobrancaCotaService;
import br.com.abril.nds.service.ParametroCobrancaService;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.TipoMensagem;
import br.com.abril.nds.util.Util;
import br.com.abril.nds.vo.PaginacaoVO;
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
public class ParametroCobrancaController {
	
	@Autowired
	private ParametroCobrancaService parametroCobrancaService;
	
	@Autowired
	private BancoService bancoService;
	
	@Autowired
	private ParametroCobrancaCotaService financeiroService;
	
    private Result result;
    
    private HttpSession httpSession;
    
    private static List<ItemDTO<Integer,String>> listaBancos =  new ArrayList<ItemDTO<Integer,String>>();

    private static List<ItemDTO<TipoCobranca,String>> listaTiposCobranca =  new ArrayList<ItemDTO<TipoCobranca,String>>();
    
    private static List<ItemDTO<FormaEmissao,String>> listaFormasEmissao =  new ArrayList<ItemDTO<FormaEmissao,String>>();
    
    private static final String FILTRO_PESQUISA_SESSION_ATTRIBUTE = "filtroPesquisaParametrosCobranca";
    
    
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
    public void index(){ 
    	
    	listaBancos.clear();
    	listaTiposCobranca.clear();
    	listaFormasEmissao.clear();
    	listaBancos = this.bancoService.getComboBancos();
    	listaTiposCobranca = this.financeiroService.getComboTiposCobranca();
    	//listaFormasEmissao = this.financeiroService.getComboFormasEmissao();
		result.include("listaBancos",listaBancos);
		result.include("listaTiposCobranca",listaTiposCobranca);
		result.include("listaFormasEmissao",listaFormasEmissao);
		
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
		FiltroConsultaParametrosCobrancaDTO filtroAtual = new FiltroConsultaParametrosCobrancaDTO(idBanco, tipoCobranca);
		PaginacaoVO paginacao = new PaginacaoVO(page, rp, sortorder);
		filtroAtual.setPaginacao(paginacao);
		filtroAtual.setOrdenacaoColuna(Util.getEnumByStringValue(OrdenacaoColunaParametrosCobranca.values(), sortname));
	
		FiltroConsultaParametrosCobrancaDTO filtroSessao = (FiltroConsultaParametrosCobrancaDTO) this.httpSession.getAttribute(FILTRO_PESQUISA_SESSION_ATTRIBUTE);
		
		if (filtroSessao != null && !filtroSessao.equals(filtroAtual)) {
			filtroAtual.getPaginacao().setPaginaAtual(1);
		}
		
		this.httpSession.setAttribute(FILTRO_PESQUISA_SESSION_ATTRIBUTE, filtroAtual);

		//BUSCA BANCOS
		List<ParametroCobrancaVO> parametrosCobranca = this.parametroCobrancaService.obterParametrosCobranca(filtroAtual);	
		int qtdRegistros = this.parametroCobrancaService.obterQuantidadeParametrosCobranca(filtroAtual);
		
		TableModel<CellModelKeyValue<ParametroCobrancaVO>> tableModel =
				new TableModel<CellModelKeyValue<ParametroCobrancaVO>>();
			
		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(parametrosCobranca));
		tableModel.setPage(page);
		tableModel.setTotal(qtdRegistros);

		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
	}
	
	
	/**
	 * Método responsável pela inclusão de novo parametro de cobrança
	 * @param tipoCobranca
	 * @param idBanco
	 * @param valorMinimoEmissao
	 * @param taxamulta
	 * @param vrMulta
	 * @param taxaJuros
	 * @param instrucoes
	 * @param acumulaDivida
	 * @param vencimentoDiaUtil
	 * @param cobrancaUnificada
	 * @param EnvioEmail
	 * @param formaEmissao
	 */
	@Post
	@Path("/novoParametroCobranca")
	public void novoParametroCobranca(ParametroCobrancaDTO parametros){
		
		Banco banco = this.bancoService.obterBancoPorId(1l);//!!!

	    //PoliticaCobranca politicaCobranca = new PoliticaCobranca();
	
        //this.parametroCobrancaService.incluir();
        
        result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Forma de cobrança cadastrada com sucesso."),"result").recursive().serialize();
	}
	
	
	

	@Post
	@Path("/buscaParametroCobranca")
	public void buscaParametroCobranca(long idParametro){

	}
	

	

	@Post
	@Path("/desativaParametroCobranca")
	public void desativaParametroCobranca(long idParametro){
		result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Parâmetro de cobrança desativado com sucesso."),"result").recursive().serialize();
    }
	
}
