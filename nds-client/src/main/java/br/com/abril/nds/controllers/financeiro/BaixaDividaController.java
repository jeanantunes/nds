package br.com.abril.nds.controllers.financeiro;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.vo.CobrancaVO;
import br.com.abril.nds.client.vo.ValidacaoVO;
import br.com.abril.nds.dto.PagamentoDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaDividasCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaDividasCotaDTO.OrdenacaoColunaDividas;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.PoliticaCobranca;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.service.BoletoService;
import br.com.abril.nds.service.CalendarioService;
import br.com.abril.nds.service.CobrancaService;
import br.com.abril.nds.service.DistribuidorService;
import br.com.abril.nds.service.PoliticaCobrancaService;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.Constantes;
import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.TipoBaixaCobranca;
import br.com.abril.nds.util.TipoMensagem;
import br.com.abril.nds.util.Util;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

@Resource
@Path("/financeiro/divida")
public class BaixaDividaController {

	private Result result;
	
	
	private HttpSession httpSession;
	
	@Autowired
	private CobrancaService cobrancaService;
	
	
	@Autowired
	private BoletoService boletoService;
	
	
	@Autowired
	private PoliticaCobrancaService politicaCobrancaService;
	
	@Autowired
	private DistribuidorService distribuidorService;
	
	@Autowired
	private CalendarioService calendarioService;
	

	private static final String FILTRO_PESQUISA_SESSION_ATTRIBUTE = "filtroPesquisaConsultaDividas";
	   
	public BaixaDividaController(Result result,HttpSession httpSession) {
		this.result = result;
		this.httpSession = httpSession;
	}
		
	@Get
	@Path("/manual")
	public void manual() {
		
	}
	


	/**
	 * Método responsavel pela busca de dívida individual
	 * @param nossoNumero
	 */
	@Post
	@Path("/buscaDivida")
	public void buscaDivida(String nossoNumero){
		
		if ((nossoNumero==null)||("".equals(nossoNumero.trim()))){
		    throw new ValidacaoException(TipoMensagem.WARNING, "Digite o número da cota ou o número da cobrança.");
		}
		
		CobrancaVO cobranca = this.boletoService.obterDadosCobranca(nossoNumero);
		if (cobranca==null) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum registro encontrado.");
		} 
		result.use(Results.json()).from(cobranca,"result").recursive().serialize();
	}
	
	
	
	/**
	 * Método responsável pela busca de dívidas(Cobranças Geradas)
	 * @param numCota
	 * @param sortorder
	 * @param sortname
	 * @param page
	 * @param rp
	 */
	@Post
	@Path("/buscaDividas")
	public void buscaDividas(Integer numCota,
			                 Date dataVencimento,
			                 String sortorder, 
			                 String sortname, 
			                 int page, 
			                 int rp){

		if ((numCota==null)||(numCota<=0)){
		    throw new ValidacaoException(TipoMensagem.WARNING, "Digite o número da cota.");
		}

        //CONFIGURAR PAGINA DE PESQUISA
		FiltroConsultaDividasCotaDTO filtroAtual = new FiltroConsultaDividasCotaDTO(numCota,dataVencimento);
		PaginacaoVO paginacao = new PaginacaoVO(page, rp, sortorder);
		filtroAtual.setPaginacao(paginacao);
		filtroAtual.setOrdenacaoColuna(Util.getEnumByStringValue(OrdenacaoColunaDividas.values(), sortname));
	
		FiltroConsultaDividasCotaDTO filtroSessao = (FiltroConsultaDividasCotaDTO) this.httpSession.getAttribute(FILTRO_PESQUISA_SESSION_ATTRIBUTE);
		
		if (filtroSessao != null && !filtroSessao.equals(filtroAtual)) {
		
			filtroAtual.getPaginacao().setPaginaAtual(1);
		}
		
		this.httpSession.setAttribute(FILTRO_PESQUISA_SESSION_ATTRIBUTE, filtroAtual);
		
		//BUSCA COBRANCAS //!!
		List<CobrancaVO> cobrancas = this.cobrancaService.obterDadosCobrancasPorCota(filtroAtual);
		
		if (cobrancas.size()==0) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum registro encontrado.");
		} 
		
		int qtdRegistros = this.cobrancaService.obterQuantidadeCobrancasPorCota(filtroAtual);
			
		TableModel<CellModelKeyValue<CobrancaVO>> tableModel = new TableModel<CellModelKeyValue<CobrancaVO>>();
			
		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(cobrancas));
		tableModel.setPage(page);
		tableModel.setTotal(qtdRegistros);

		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();

	}
	
	
	
    private Usuario obterUsuario() {
		
		//TODO: obter usuário
		
		Usuario usuario = new Usuario();
		usuario.setId(1L);
		return usuario;
	}
	
    
    
	
	/**
	 * Método responsável pela baixa de cobrança individual manualmente.	
	 * @param nossoNumero
	 * @param valor
	 * @param dataVencimento
	 * @param desconto
	 * @param juros
	 * @param multa
	 * @throws Mensagem de validação
	 */
	@Post
	@Path("/baixaManualCobranca")
	public void baixaManualCobranca(String nossoNumero, 
					              String valor,
					              Date dataVencimento,
					              String desconto, 
					              String juros,
					              String multa) {        
        
		Distribuidor distribuidor = distribuidorService.obter();
		
		Date dataNovoMovimento =
			calendarioService.adicionarDiasUteis(distribuidor.getDataOperacao(), 1);
		
        BigDecimal valorConvertido = CurrencyUtil.converterValor(valor);
        BigDecimal jurosConvertido = CurrencyUtil.converterValor(juros);
        BigDecimal multaConvertida = CurrencyUtil.converterValor(multa);
        BigDecimal descontoConvertido = CurrencyUtil.converterValor(desconto);

        if (descontoConvertido.compareTo(
        		valorConvertido.add(jurosConvertido).add(multaConvertida)) == 1) {
        	
        	throw new ValidacaoException(TipoMensagem.WARNING,
        		"O desconto não deve ser maior do que o valor a pagar.");
        }

        PagamentoDTO pagamento = new PagamentoDTO();
		pagamento.setDataPagamento(dataNovoMovimento);
		pagamento.setNossoNumero(nossoNumero);
		pagamento.setNumeroRegistro(null);
		pagamento.setValorPagamento(valorConvertido);
		pagamento.setValorJuros(jurosConvertido);
		pagamento.setValorMulta(multaConvertida);
		pagamento.setValorDesconto(descontoConvertido);
		
		PoliticaCobranca politicaPrincipal = this.politicaCobrancaService.obterPoliticaCobrancaPrincipal();

		boletoService.baixarBoleto(TipoBaixaCobranca.MANUAL, pagamento, obterUsuario(),
								   null,politicaPrincipal , distribuidor,
								   dataNovoMovimento, null);
			
		result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Boleto "+nossoNumero+" baixado com sucesso."),Constantes.PARAM_MSGS).recursive().serialize();
	}
	
}
