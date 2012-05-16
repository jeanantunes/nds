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
import br.com.abril.nds.dto.filtro.FiltroParametrosCobrancaDTO;
import br.com.abril.nds.dto.filtro.FiltroParametrosCobrancaDTO.OrdenacaoColunaParametrosCobranca;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.FormaEmissao;
import br.com.abril.nds.model.cadastro.TipoCobranca;
import br.com.abril.nds.model.cadastro.TipoFormaCobranca;
import br.com.abril.nds.service.BancoService;
import br.com.abril.nds.service.DistribuidorService;
import br.com.abril.nds.service.FornecedorService;
import br.com.abril.nds.service.ParametroCobrancaCotaService;
import br.com.abril.nds.service.PoliticaCobrancaService;
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
import br.com.caelum.vraptor.Validator;
import br.com.caelum.vraptor.validator.Message;
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
	private PoliticaCobrancaService politicaCobrancaService;
	
	@Autowired
	private BancoService bancoService;
	
	@Autowired
	private DistribuidorService distribuidorService;
	
	@Autowired
	private FornecedorService fornecedorService;
	
	@Autowired
	private ParametroCobrancaCotaService financeiroService;
	
	@Autowired
	private Validator validator;	
	
    private Result result;
    
    private HttpSession httpSession;
    
    private static List<ItemDTO<Integer,String>> listaBancos =  new ArrayList<ItemDTO<Integer,String>>();

    private static List<ItemDTO<TipoCobranca,String>> listaTiposCobranca =  new ArrayList<ItemDTO<TipoCobranca,String>>();
    
    private static List<ItemDTO<FormaEmissao,String>> listaFormasEmissao =  new ArrayList<ItemDTO<FormaEmissao,String>>();
    
    private static List<ItemDTO<Long,String>> listaFornecedores =  new ArrayList<ItemDTO<Long,String>>();
    
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
    	listaFornecedores.clear();
    	
    	listaBancos = this.bancoService.getComboBancos();
    	listaTiposCobranca = this.financeiroService.getComboTiposCobranca();
    	listaFormasEmissao = this.financeiroService.getComboFormasEmissao();
    	listaFornecedores = this.fornecedorService.buscarComboFornecedores();
    	
		result.include("listaBancos",listaBancos);
		result.include("listaTiposCobranca",listaTiposCobranca);
		result.include("listaFormasEmissao",listaFormasEmissao);
		result.include("listaFornecedores",listaFornecedores);
		
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

		//BUSCA BANCOS
		List<ParametroCobrancaVO> parametrosCobranca = this.politicaCobrancaService.obterDadosPoliticasCobranca(filtroAtual);	
		
		if ((parametrosCobranca==null)||(parametrosCobranca.size()<=0)) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum registro encontrado.");
		} 
		
		int qtdRegistros = this.politicaCobrancaService.obterQuantidadePoliticasCobranca(filtroAtual);
		
		TableModel<CellModelKeyValue<ParametroCobrancaVO>> tableModel =
				new TableModel<CellModelKeyValue<ParametroCobrancaVO>>();
			
		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(parametrosCobranca));
		tableModel.setPage(page);
		tableModel.setTotal(qtdRegistros);

		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
	}
	
	
	
	/**
	 * Método responsável pela inclusão de novo parametro de cobrança
	 * @param ParametroCobrancaDTO
	 */
	@Post
	@Path("/novoParametroCobranca")
	public void novoParametroCobranca(ParametroCobrancaDTO parametros, String tipoFormaCobranca, List<Long> listaIdsFornecedores){

		
		if ((tipoFormaCobranca!=null)&&(!"".equals(tipoFormaCobranca))){
			parametros.setTipoFormaCobranca(TipoFormaCobranca.valueOf(tipoFormaCobranca));
		}
        
		parametros.setFornecedoresId(listaIdsFornecedores);
		
		parametros = formatarParametros(parametros);
		
		validarParametros(parametros);
		
		this.politicaCobrancaService.postarPoliticaCobranca(parametros);	
        
        result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Parâmetro de cobrança cadastrada com sucesso."),"result").recursive().serialize();
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
	
	
	
	/**
	 *Formata os dados dos Parâmetros de cobrança, apagando valores que não são compatíveis com o Tipo de Cobranca escolhido.
	 * @param ParametroCobrancaDTO
	 */
	private ParametroCobrancaDTO formatarParametros(ParametroCobrancaDTO parametros){
		/*
		if (parametros.getTipoFormaCobranca()==TipoFormaCobranca.SEMANAL){
			parametros.setDiaDoMes(null);
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
			parametros.setValorMinimo(null);
			parametros.setTaxaJuros(null);
			parametros.setTaxaMulta(null);
			parametros.setValorMulta(null);
	    }
		else if ((parametros.getTipoCobranca()==TipoCobranca.CHEQUE)||(parametros.getTipoCobranca()==TipoCobranca.DINHEIRO)){
			parametros.setIdBanco(null);
		}    
		else if (parametros.getTipoCobranca()==TipoCobranca.OUTROS){
			parametros.setIdBanco(null);
			parametros.setValorMinimo(null);
		}    
        */
		return parametros;
		
	}
	
	
	
	/**
	 * Método responsável pela validação dos dados dos Parâmetros de Cobrança.
	 * @param ParametroCobrancaDTO
	 */
	public void validarParametros(ParametroCobrancaDTO parametros){
		/*
		validar();
		
		if(parametros.getTipoCobranca()==null){
			throw new ValidacaoException(TipoMensagem.WARNING, "Escolha um Tipo de Pagamento.");
		}
		
		if (parametros.getTipoFormaCobranca()==null){
			throw new ValidacaoException(TipoMensagem.WARNING, "Selecione um tipo de concentração de Pagamentos.");
		}
		
		if(parametros.getTipoFormaCobranca()==TipoFormaCobranca.MENSAL){
			if (parametros.getDiaDoMes()==null){
				throw new ValidacaoException(TipoMensagem.WARNING, "Para o tipo de cobrança Mensal é necessário informar o dia do mês.");
			}
			else{
				if ((parametros.getDiaDoMes()>31)||(parametros.getDiaDoMes()<1)){
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
		
		if (parametros.getIdBanco()==null){
		    if ((parametros.getTipoCobranca()==TipoCobranca.BOLETO)||
		    	(parametros.getTipoCobranca()==TipoCobranca.BOLETO_EM_BRANCO)||
		    	(parametros.getTipoCobranca()==TipoCobranca.TRANSFERENCIA_BANCARIA)||
		    	(parametros.getTipoCobranca()==TipoCobranca.DEPOSITO)){
		    	throw new ValidacaoException(TipoMensagem.WARNING, "Para o Tipo de Cobrança selecionado é necessário a escolha de um Banco.");
		    }
		}
		
		Distribuidor distribuidor = this.distribuidorService.obter();
		
		if (parametros.isEvioEmail()){
			if (distribuidor.getJuridica().getEmail()==null){
				throw new ValidacaoException(TipoMensagem.WARNING, "Cadastre um e-mail para o distribuidor ou desmarque a opção de envio de email.");
			}
		}
		*/
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
