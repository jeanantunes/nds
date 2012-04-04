package br.com.abril.nds.controllers.financeiro;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.vo.DividaGeradaVO;
import br.com.abril.nds.client.vo.RotaRoteiroVO;
import br.com.abril.nds.controllers.exception.ValidacaoException;
import br.com.abril.nds.dto.GeraDividaDTO;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.filtro.FiltroDividaGeradaDTO;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.Pessoa;
import br.com.abril.nds.model.cadastro.PessoaFisica;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.cadastro.RotaRoteiroOperacao;
import br.com.abril.nds.model.cadastro.TipoCobranca;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.service.CotaService;
import br.com.abril.nds.service.DistribuidorService;
import br.com.abril.nds.service.GerarCobrancaService;
import br.com.abril.nds.service.ImpressaoDividaService;
import br.com.abril.nds.service.RotaRoteiroOperacaoService;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.TipoMensagem;
import br.com.abril.nds.util.Util;
import br.com.abril.nds.util.export.FileExporter;
import br.com.abril.nds.util.export.FileExporter.FileType;
import br.com.abril.nds.util.export.NDSFileHeader;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

@Resource
@Path("/financeiro/impressaoBoletos")
public class ImpressaoBoletosController {

	private static final String FILTRO_SESSION_ATTRIBUTE = "pesquisaGeraDivida";

	private static final String BOLETO = "Boletos";

	private static final String DIVIDAS = "Dividas";

	private static final String DIVIDA_SESSION_ATTRIBUTE = "arquivoDivida";
	
	@Autowired
	private Result result;
	
	@Autowired
	private GerarCobrancaService gerarCobrancaService;
	
	@Autowired
	private RotaRoteiroOperacaoService rotaRoteiroOperacaoService;
	
	@Autowired
	private ImpressaoDividaService dividaService;
	
	@Autowired
	private CotaService cotaService;
	
	@Autowired
	private DistribuidorService distribuidorService;
	
	@Autowired
	private HttpSession session;
	
	@Autowired
	private HttpServletResponse httpResponse;
	
	public enum TipoImpressao{
		BOLETO,DIVIDA
	}
	
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

		if(!gerarCobrancaService.validarDividaGeradaDataOperacao()){
			throw new ValidacaoException(TipoMensagem.WARNING, "Já foram geradas dividas para data de operação.");
		}
		

		this.gerarCobrancaService.gerarCobranca(null, this.getUsuario().getId());
		
		throw new ValidacaoException(TipoMensagem.SUCCESS, "As dividas foram geradas com sucesso.");
	}
	
	@Post
	@Path("/consultar")
	public void consultarDividas(String dataMovimento,String box, String rota,
								 String roteiro,Integer numCota,TipoCobranca tipoCobranca, String sortorder, 
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
	 * Exporta os dados da pesquisa.
	 * 
	 * @param fileType - tipo de arquivo
	 * 
	 * @throws IOException Exceção de E/S
	 */
	@Get
	public void exportar(FileType fileType) throws IOException {
		
		FiltroDividaGeradaDTO filtro = this.obterFiltroExportacao();
		
		List<GeraDividaDTO> listaDividasGeradas = dividaService.obterDividasGeradas(filtro);
		
		FileExporter.to("conta-corrente-cota", fileType)
			.inHTTPResponse(this.getNDSFileHeader(), filtro, null, 
				listaDividasGeradas, GeraDividaDTO.class, this.httpResponse);
	}
	
	/*
	 * Obtém o filtro para exportação.
	 */
	private FiltroDividaGeradaDTO obterFiltroExportacao() {
		
		FiltroDividaGeradaDTO filtro = 
			(FiltroDividaGeradaDTO) this.session.getAttribute(FILTRO_SESSION_ATTRIBUTE);
		
		if (filtro != null) {
			
			if (filtro.getPaginacao() != null) {
				
				filtro.getPaginacao().setPaginaAtual(null);
				filtro.getPaginacao().setQtdResultadosPorPagina(null);
			}
			
			if (filtro.getNumeroCota() != null) {
				
				Cota cota =
					this.cotaService.obterPorNumeroDaCota(filtro.getNumeroCota());
				
				if (cota != null) {
					
					Pessoa pessoa = cota.getPessoa();
					
					if (pessoa instanceof PessoaFisica) {
						
						filtro.setNomeCota(((PessoaFisica) pessoa).getNome());
												
					} else if (pessoa instanceof PessoaJuridica) {
						
						filtro.setNomeCota(((PessoaJuridica) pessoa).getRazaoSocial());
					}
				}
			}
		}
		
		return filtro;
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
		
		tableModel.setPage(filtro.getPaginacao().getPaginaAtual());
		
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
			dividaGeradaVO.setRota((divida.getRota()== null)?"": divida.getRota());
			dividaGeradaVO.setRoteiro((divida.getRoteiro()==null)?"": divida.getRoteiro());
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
	
	@Post
	@Path("/validarImpressaoDivida")
	public void validarImpressaoDivida(String nossoNumero) throws Exception{
		
		byte[] arquivo = dividaService.gerarArquivoImpressao(nossoNumero);
		
		if(arquivo == null){
			throw new ValidacaoException(TipoMensagem.WARNING, "Divida não encontrado para impressão");
		}
		
		session.setAttribute(DIVIDA_SESSION_ATTRIBUTE, arquivo);
		
		result.use(Results.json()).from(Boolean.TRUE.toString(),"result").serialize();
	}

	
	@Post
	@Path("/validarImpressaoDividas")
	public void validarImpressaoDividaEmMassa(String tipoImpressao) throws Exception{
		
		FiltroDividaGeradaDTO filtro = obterFiltroExportacao();
		
		TipoCobranca tipoCobranca = filtro.getTipoCobranca();
		
		String message = "Não foi encontrado Dividas para impressão.";
		
		byte[] arquivo = null;
		
		if("BOLETO".equals(tipoImpressao)){
			
			message = "Não foi encontrado Boletos para impressão.";
			
			if(tipoCobranca != null && !TipoCobranca.BOLETO.equals(filtro.getTipoCobranca())){
				throw new ValidacaoException(TipoMensagem.WARNING, message);
			}
			
			filtro.setTipoCobranca(TipoCobranca.BOLETO);
		}
		else{
			if(TipoCobranca.BOLETO.equals(filtro.getTipoCobranca())){
				throw new ValidacaoException(TipoMensagem.WARNING, message);
			}
		}
	
		arquivo = dividaService.gerarArquivoImpressao(filtro);
		
		if(arquivo == null){
			throw new ValidacaoException(TipoMensagem.WARNING, message);
		}
		
		filtro.setTipoCobranca(tipoCobranca);
		
		session.setAttribute(DIVIDA_SESSION_ATTRIBUTE, arquivo);
		
		result.use(Results.json()).from(tipoImpressao,"result").serialize();
	}
	
	@Get
	@Path("/imprimirDivida")
	public void imprimirDivida(String nossoNumero) throws Exception{
		
		byte[]arquivo = (byte[]) session.getAttribute(DIVIDA_SESSION_ATTRIBUTE);
		
		imprimirDividas(arquivo,nossoNumero);
		
		session.setAttribute(DIVIDA_SESSION_ATTRIBUTE,null);
	}
	
	@Get
	@Path("/imprimirBoletosEmMassa")
	public void imprimirBoletosEmMassa() throws Exception{
		
		byte[]arquivo = (byte[]) session.getAttribute(DIVIDA_SESSION_ATTRIBUTE);
		
		imprimirDividas(arquivo,BOLETO);
		
		session.setAttribute(DIVIDA_SESSION_ATTRIBUTE,null);
	}
	
	@Get
	@Path("/imprimirDividasEmMassa")
	public void imprimirDividasEmMassa() throws Exception{
		
		byte[]arquivo = (byte[]) session.getAttribute(DIVIDA_SESSION_ATTRIBUTE);
		
		imprimirDividas(arquivo,DIVIDAS);
		
		session.setAttribute(DIVIDA_SESSION_ATTRIBUTE,null);
	}
	
	/**
	 * Renderiza o arquivo de impressão de dividas
	 * @param filtro
	 * @throws IOException
	 */
	private void imprimirDividas(byte[] arquivo,String nameArquivo) throws IOException{
		
		this.httpResponse.setContentType("application/pdf");
		this.httpResponse.setHeader("Content-Disposition", "attachment; filename="+nameArquivo +".pdf");

		OutputStream output = this.httpResponse.getOutputStream();
		output.write(arquivo);

		httpResponse.getOutputStream().close();
		
		result.use(Results.nothing());
	}

	@Post
	@Path("/enviarDivida")
	public void enviarDivida(String nossoNumero) throws Exception{
		
		dividaService.enviarArquivoPorEmail(nossoNumero);
		
		result.use(Results.nothing());
		
		throw new ValidacaoException(TipoMensagem.SUCCESS, "Divida "+nossoNumero+" enviada com sucesso.");
		
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
	
	/*
	 * Obtém os dados do cabeçalho de exportação.
	 * 
	 * @return NDSFileHeader
	 */
	private NDSFileHeader getNDSFileHeader() {
		
		NDSFileHeader ndsFileHeader = new NDSFileHeader();
		
		Distribuidor distribuidor = this.distribuidorService.obter();
		
		if (distribuidor != null) {
			
			ndsFileHeader.setNomeDistribuidor(distribuidor.getJuridica().getRazaoSocial());
			ndsFileHeader.setCnpjDistribuidor(distribuidor.getJuridica().getCnpj());
		}
		
		ndsFileHeader.setData(new Date());
		
		ndsFileHeader.setNomeUsuario(this.getUsuario().getNome());
		
		return ndsFileHeader;
	}
	
	//TODO: não há como reconhecer usuario, ainda
	private Usuario getUsuario() {
		
		Usuario usuario = new Usuario();
		
		usuario.setId(1L);
		
		usuario.setNome("Jornaleiro da Silva");
		
		return usuario;
	}
	
}


