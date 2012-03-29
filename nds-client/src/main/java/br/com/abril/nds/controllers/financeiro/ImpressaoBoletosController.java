package br.com.abril.nds.controllers.financeiro;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.vo.DividaGeradaVO;
import br.com.abril.nds.client.vo.RotaRoteiroVO;
import br.com.abril.nds.client.vo.ValidacaoVO;
import br.com.abril.nds.controllers.exception.ValidacaoException;
import br.com.abril.nds.dto.GeraDividaDTO;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.filtro.FiltroDividaGeradaDTO;
import br.com.abril.nds.model.cadastro.RotaRoteiroOperacao;
import br.com.abril.nds.model.cadastro.TipoCobranca;
import br.com.abril.nds.service.GerarCobrancaService;
import br.com.abril.nds.service.ImpressaoDividaService;
import br.com.abril.nds.service.RotaRoteiroOperacaoService;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.Constantes;
import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.DateUtil;
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

@Resource
@Path("financeiro/impressaoBoletos")
public class ImpressaoBoletosController {

	private static final String FILTRO_SESSION_ATTRIBUTE = "pesquisaGeraDivida";
	
	@Autowired
	private Result result;
	
	@Autowired
	private GerarCobrancaService gerarCobrancaService;
	
	@Autowired
	private RotaRoteiroOperacaoService rotaRoteiroOperacaoService;
	
	@Autowired
	private ImpressaoDividaService dividaService;
	
	@Autowired
	private HttpSession session;
	
	@Autowired
	private HttpServletResponse httpResponse;
	
	public ImpressaoBoletosController(){
	}
	
	@Get
	@Path("/")
	public void index(){
		
		carregarTiposCobranca();
	}
	
	
	/**
	 * Carrega as os tipos de cobrança no combo da tela.
	 */
	private void carregarTiposCobranca(){
		
		List<ItemDTO<TipoCobranca, String>> listaTipoCobranca = new ArrayList<ItemDTO<TipoCobranca,String>>();
		
		for(TipoCobranca tipo : TipoCobranca.values()){
			
			listaTipoCobranca.add(new ItemDTO<TipoCobranca, String>(tipo,tipo.getDescTipoCobranca()));
		}
			
		result.include("listaTipoCobranca",listaTipoCobranca);
	}
	
	@Post
	public void gerarDivida(){
		//trecho que simula possível demora no processamento dessa rotina
		try {
			Thread.sleep(5000L);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		this.gerarCobrancaService.gerarCobranca(null, this.getIdUsuario());
		
		this.result.use(Results.json()).from("", "result").serialize();
	}
	
	private Long getIdUsuario() {
		// TODO pendente
		return 1L;
	}
	
	@Post
	@Path("financeiro/impressaoBoletos/consultar")
	public void consultarDividas(String dataMovimento,String box, String rota,
								 String roteiro,int numCota,TipoCobranca tipoCobranca, String sortorder, 
								 String sortname, int page, int rp){
		
		isDataMovimento(dataMovimento);
		
		Date data = DateUtil.parseDataPTBR(dataMovimento);
		
		FiltroDividaGeradaDTO filtro = new FiltroDividaGeradaDTO(data, box,rota,roteiro,numCota,tipoCobranca);
		
		configurarPaginacaoPesquisa(filtro, sortorder, sortname, page, rp);
		
		tratarFiltro(filtro);
		
		efetuarConsulta(filtro);
	}
	
	@Post
	@Path("/pesquisarInfoCota")
	public void pesquisarInfoCota(int numeroCota){
		
		RotaRoteiroOperacao operacao = rotaRoteiroOperacaoService.obterRotaRoteiroImpressaoDividaCota(numeroCota);
		
		RotaRoteiroVO rotaRoteiroVO = new RotaRoteiroVO();
		
		if(operacao!= null){
			
			rotaRoteiroVO.setBox( (operacao.getCota()!= null && operacao.getCota().getBox()!= null) 
									? operacao.getCota().getBox().getCodigo():"");
			
			rotaRoteiroVO.setRota((operacao.getRota()!= null)
									? operacao.getRota().getCodigoRota():"");
			
			rotaRoteiroVO.setRoteiro((operacao.getRoteiro()!= null)
									?operacao.getRoteiro().getDescricaoRoteiro():"");
			
			rotaRoteiroVO.setTipoCobranca( ( operacao.getCota()!= null 
											&& operacao.getCota().getParametroCobranca()!= null 
											&& operacao.getCota().getParametroCobranca().getFormaCobranca()!= null)
											? operacao.getCota().getParametroCobranca().getFormaCobranca().getTipoCobranca():null);
		}
		
		result.use(Results.json()).from(rotaRoteiroVO,"result").serialize();
	}
	
	/**
	 * Efetua a consulta e monta a estrutura do grid de dividas geradas.
	 * @param filtro
	 */
	private void efetuarConsulta(FiltroDividaGeradaDTO filtro) {
		
		List<GeraDividaDTO> listaDividasGeradas = dividaService.obterDividasGeradas(filtro) ;
		
		if (listaDividasGeradas == null || listaDividasGeradas.isEmpty()){
			throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum registro encontrado.");
		}
		
		Long totalRegistros = dividaService.obterQuantidadeDividasGeradas(filtro);
		
		List<DividaGeradaVO> listaDividasGeradasVO = getListaDividaGeradaVO(listaDividasGeradas);

		TableModel<CellModelKeyValue<DividaGeradaVO>> tableModel = new TableModel<CellModelKeyValue<DividaGeradaVO>>();

		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listaDividasGeradasVO));
		
		tableModel.setPage(1);
		
		tableModel.setTotal( (totalRegistros == null)?0:totalRegistros.intValue());
		
		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
		
	}

	/**
	 * Retorna lista de dividas geradas 'VO'
	 * @param listaGeraDividaDTO
	 * @return List<DividaGeradaVO>
	 */
	private List<DividaGeradaVO> getListaDividaGeradaVO(List<GeraDividaDTO> listaGeraDividaDTO){
		
		List<DividaGeradaVO> listaDividasGeradasVO = new LinkedList<DividaGeradaVO>();
		
		DividaGeradaVO dividaGeradaVO = null;
		
		for (GeraDividaDTO divida : listaGeraDividaDTO) {
			
			dividaGeradaVO = new DividaGeradaVO();
			dividaGeradaVO.setBox(divida.getBox());
			dividaGeradaVO.setDataEmissao(DateUtil.formatarDataPTBR(divida.getDataEmissao()));
			dividaGeradaVO.setDataVencimento(DateUtil.formatarDataPTBR(divida.getDataVencimento()));
			dividaGeradaVO.setNomeCota(divida.getNomeCota());
			dividaGeradaVO.setNumeroCota(String.valueOf(divida.getNumeroCota()));
			dividaGeradaVO.setRota(divida.getRota());
			dividaGeradaVO.setRoteiro(divida.getRoteiro());
			dividaGeradaVO.setSuportaEmail(divida.getSuportaEmail().toString());
			dividaGeradaVO.setTipoCobranca( (divida.getTipoCobranca()!= null)? divida.getTipoCobranca().getDescTipoCobranca():"");
			dividaGeradaVO.setValor( CurrencyUtil.formatarValor(divida.getValor()));
			dividaGeradaVO.setVias(String.valueOf(divida.getVias()));
			dividaGeradaVO.setNossoNumero(divida.getNossoNumero());
			
			listaDividasGeradasVO.add(dividaGeradaVO);
		}
		
		return listaDividasGeradasVO;
	}
	
	/**
	 * Verifica se divididas foram geradas em uma determinada data de vencimento.
	 * @param dataMovimento
	 * @return Boolean
	 */
	@Post
	@Path("/validarPesquisaDivida")
	public void validarPesquisaDivida(String dataMovimento){
		
		isDataMovimento(dataMovimento);
		
		Boolean isPesquisa = dividaService.validarDividaGerada(DateUtil.parseDataPTBR(dataMovimento));
		
		result.use(Results.json()).from(isPesquisa.toString(),"result").serialize();
		
	}
	
	/**
	 * Configura paginação do grid de pesquisa.
	 * @param filtro
	 * @param sortorder
	 * @param sortname
	 * @param page
	 * @param rp
	 */
	private void configurarPaginacaoPesquisa(FiltroDividaGeradaDTO filtro,String sortorder,String sortname,int page, int rp) {

		if (filtro != null) {
		
			PaginacaoVO paginacao = new PaginacaoVO(page, rp, sortorder);
			
			filtro.setPaginacao(paginacao);
			
			String[] sortNames = sortname.split(",");
			
			List<FiltroDividaGeradaDTO.ColunaOrdenacao> ordenacaoColunas = new ArrayList<FiltroDividaGeradaDTO.ColunaOrdenacao>();
			
			for(String sort : sortNames){
				
				ordenacaoColunas.add(Util.getEnumByStringValue(FiltroDividaGeradaDTO.ColunaOrdenacao.values(),sort));
			}
			
			filtro.setListaColunaOrdenacao(ordenacaoColunas);
		}
	}
	
	@Get
	@Path("/imprimirDivida")
	public void imprimirDivida(String nossoNumero) throws Exception{
		
		byte[] arquivo = dividaService.gerarArquivoImpressao(nossoNumero);
		
		this.httpResponse.setContentType("application/pdf");
		this.httpResponse.setHeader("Content-Disposition", "attachment; filename= Dividas Geradas.pdf");

		OutputStream output = this.httpResponse.getOutputStream();
		output.write(arquivo);

		httpResponse.flushBuffer();
	}
	
	@Get
	@Path("/imprimirDividas")
	public void imprimirDividas() throws Exception{
		
		FiltroDividaGeradaDTO filtroSession = (FiltroDividaGeradaDTO) session
				.getAttribute(FILTRO_SESSION_ATTRIBUTE);
		
		//filtroSession.setPaginacao(paginacao)
		
		byte[] arquivo =  dividaService.gerarArquivoImpressao(null);
		
		this.httpResponse.setContentType("application/pdf");
		this.httpResponse.setHeader("Content-Disposition", "attachment; filename= Dividas Geradas.pdf");

		OutputStream output = this.httpResponse.getOutputStream();
		output.write(arquivo);

		httpResponse.flushBuffer();
	}
	
	@Post
	@Path("/enviarDivida")
	public void enviarDivida(String nossoNumero) throws Exception{
		
		dividaService.enviarArquivoPorEmail(nossoNumero);
		
		result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Divida "+nossoNumero+" enviado com sucesso."),Constantes.PARAM_MSGS).recursive().serialize();
	}

	
	/**
	 * Executa tratamento de paginação em função de alteração do filtro de pesquisa.
	 * 
	 * @param filtroResumoExpedicao
	 */
	private void tratarFiltro(FiltroDividaGeradaDTO filtro) {

		FiltroDividaGeradaDTO filtroSession = (FiltroDividaGeradaDTO) session
				.getAttribute(FILTRO_SESSION_ATTRIBUTE);
		
		if (filtroSession != null && !filtroSession.equals(filtro)) {

			filtro.getPaginacao().setPaginaAtual(1);
		}
		
		session.setAttribute(FILTRO_SESSION_ATTRIBUTE, filtro);
	}
	
	/**
	 * Valida o preenchimento do campo data do filtro de pesquisa
	 * @param dataMovimento
	 */
	private void isDataMovimento(String dataMovimento){
		
		if (dataMovimento == null || dataMovimento.isEmpty()) {
			throw new ValidacaoException(TipoMensagem.WARNING,"O preenchimento do campo \"Data\" é obrigatório." );
		}
		
		if (!DateUtil.isValidDate(dataMovimento, "dd/MM/yyyy")) {
			throw new ValidacaoException(TipoMensagem.WARNING,"Data inválida." );
		}
	}
}


