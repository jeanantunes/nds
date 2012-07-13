package br.com.abril.nds.controllers.financeiro;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.client.vo.DividaGeradaVO;
import br.com.abril.nds.client.vo.RotaRoteiroVO;
import br.com.abril.nds.dto.GeraDividaDTO;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.filtro.FiltroDividaGeradaDTO;
import br.com.abril.nds.exception.GerarCobrancaValidacaoException;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.integracao.service.DistribuidorService;
import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.FormaCobranca;
import br.com.abril.nds.model.cadastro.Pessoa;
import br.com.abril.nds.model.cadastro.PessoaFisica;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.cadastro.Rota;
import br.com.abril.nds.model.cadastro.Roteirizacao;
import br.com.abril.nds.model.cadastro.Roteiro;
import br.com.abril.nds.model.cadastro.TipoBox;
import br.com.abril.nds.model.cadastro.TipoCobranca;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.serialization.custom.CustomMapJson;
import br.com.abril.nds.service.BoxService;
import br.com.abril.nds.service.CotaService;
import br.com.abril.nds.service.GerarCobrancaService;
import br.com.abril.nds.service.ImpressaoDividaService;
import br.com.abril.nds.service.ParametroCobrancaCotaService;
import br.com.abril.nds.service.RoteirizacaoService;
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
	private RoteirizacaoService roteirizacaoService;
	
	@Autowired
	private ImpressaoDividaService dividaService;
	
	@Autowired
	private CotaService cotaService;
	
	@Autowired
	private DistribuidorService distribuidorService;

	@Autowired
	private ParametroCobrancaCotaService financeiroService;
	
	@Autowired
	private BoxService boxService;
	

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
	@Rules(Permissao.ROLE_FINANCEIRO_IMPRESSAO_BOLETOS)
	public void index(){
		
		carregarTiposCobranca();
		carregarBoxes();
		carregarRota();
		carregarRoteiro();
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
	
	/**
	 * Carrega a lista de Boxes
	 */
	private void carregarBoxes(){
		
		List<ItemDTO<Long, String>> boxes = new ArrayList<ItemDTO<Long,String>>();
		
		List<Box> listaBoxes = boxService.buscarTodos(TipoBox.LANCAMENTO);
		
		for(Box box : listaBoxes){
			
			boxes.add(new ItemDTO<Long, String>(box.getId(),box.getCodigo()));
		}
			
		result.include("listaBoxes",boxes);
	}
	
	/**
	 * Carrega a lista de Rotas
	 */
	private void carregarRota(){
		
		List<Rota> rotas = roteirizacaoService.buscarRotas();
			
		result.include("listaRotas",getRotas(rotas));
	} 
	
	/**
	 * Retorna uma lista de Rota no formato ItemDTO
	 * @param rotas
	 * @return List<ItemDTO<Long, String>>
	 */
	private List<ItemDTO<Long, String>> getRotas(List<Rota> rotas){
		
		List<ItemDTO<Long, String>> listaRotas = new ArrayList<ItemDTO<Long,String>>();
		
		for(Rota rota : rotas){
			
			listaRotas.add(new ItemDTO<Long, String>(rota.getId(),rota.getCodigoRota()));
		}
		
		return listaRotas;
	}
	
	/**
	 * Carrega as listas de Roteiros
	 */
	private void carregarRoteiro(){
		
		List<Roteiro> roteiros = roteirizacaoService.buscarRoteiros();
			
		result.include("listaRoteiros",getRoteiros(roteiros));
	}
	
	/**
	 * Retorna uma lista de roteiros no formato ItemDTO
	 * @param roteiros - lista de roteiros
	 * @return List<ItemDTO<Long, String>> 
	 */
	private List<ItemDTO<Long, String>> getRoteiros(List<Roteiro> roteiros){
		
		List<ItemDTO<Long, String>> listaRoteiros = new ArrayList<ItemDTO<Long,String>>();
		
		for(Roteiro roteiro : roteiros){
			
			listaRoteiros.add(new ItemDTO<Long, String>(roteiro.getId(),roteiro.getDescricaoRoteiro()));
		}
		return listaRoteiros;
	}
	
	@Post
	public void recarregarListaRotas(Long roteiro){
		
		List<Rota> rotas = null;
		
		if(roteiro!= null){

			rotas = roteirizacaoService.buscarRotaPorRoteiro(roteiro);
		}
		else{
			
			rotas = roteirizacaoService.buscarRotas();
		}
	
		result.use(Results.json()).from(getRotas(rotas), "result").recursive().serialize();
	}
	
	@Post
	public void recarregarRoteiroRota(Long idBox){
		
		List<Roteiro> roteirosBox = null;
		
		List<Rota> rotas = null;
		
		if(idBox!= null){
			
			roteirosBox = roteirizacaoService.buscarRoteiroDeBox(idBox);
			rotas = roteirizacaoService.buscarRotaDeBox(idBox);
		}
		else{
			
			roteirosBox = roteirizacaoService.buscarRoteiros();
			rotas = roteirizacaoService.buscarRotas();
		}
		
		result.use(CustomMapJson.class).put("rotas", getRotas(rotas)).put("roteiros", getRoteiros(roteirosBox)).serialize();
	}
	
	@Post
	public void gerarDivida(){

		try {
			this.gerarCobrancaService.gerarCobranca(null, this.getUsuario().getId(), true, new HashSet<String>());
		} catch (GerarCobrancaValidacaoException e) {
			
			throw e.getValidacaoException();
		}
		
		throw new ValidacaoException(TipoMensagem.SUCCESS, "As dividas foram geradas com sucesso.");
	}
	
	@Post
	@Path("/consultar")
	public void consultarDividas(String dataMovimento,Long box, Long rota,
								 Long roteiro,Integer numCota,TipoCobranca tipoCobranca, String sortorder, 
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
		
		Roteirizacao roteirizacao = roteirizacaoService.buscarRoteirizacaoDeCota(numeroCota);
		
		RotaRoteiroVO rotaRoteiroVO = new RotaRoteiroVO();
		
		if(roteirizacao!= null){
			
			rotaRoteiroVO.setBox( (roteirizacao.getPdv()!= null 
									&& roteirizacao.getPdv().getCota()!= null 
									&& roteirizacao.getPdv().getCota().getBox()!= null)
									
									? roteirizacao.getPdv().getCota().getBox().getId():null);
			
			
			rotaRoteiroVO.setRota((roteirizacao.getRota()!= null)
					
									? roteirizacao.getRota().getId():null);
			
			rotaRoteiroVO.setRoteiro((roteirizacao.getRota()!= null 
										&& roteirizacao.getRota().getRoteiro()!= null)
					
										?roteirizacao.getRota().getRoteiro().getId():null);
			
			FormaCobranca formaCobrancaPrincipal = this.financeiroService.obterFormaCobrancaPrincipalCota(roteirizacao.getPdv().getCota().getId());
			
			rotaRoteiroVO.setTipoCobranca( (formaCobrancaPrincipal!= null)? formaCobrancaPrincipal.getTipoCobranca():null);
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
			
			if(filtro.getIdBox()!= null){
				Box box = boxService.buscarPorId(filtro.getIdBox());
				
				if(box!= null){
					filtro.setCodigoBox(box.getCodigo());
				}
			}
			
			if(filtro.getIdRota()!= null){
				Rota rota = roteirizacaoService.buscarRotaPorId(filtro.getIdRota());
				if(rota!= null){
					filtro.setRota(rota.getCodigoRota());
				}
			}
			
			if(filtro.getIdRoteiro()!= null){
				Roteiro roteiro = roteirizacaoService.buscarRoteiroPorId(filtro.getIdRoteiro());
				if(roteiro!= null){
					filtro.setRoteiro(roteiro.getDescricaoRoteiro());
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
			dividaGeradaVO.setSuportaEmail( (divida.getSuportaEmail()==null)?Boolean.FALSE.toString():divida.getSuportaEmail().toString());
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


