package br.com.abril.nds.controllers.devolucao;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.controllers.BaseController;
import br.com.abril.nds.dto.BoletoEmBrancoDTO;
import br.com.abril.nds.dto.CotaEmissaoDTO;
import br.com.abril.nds.dto.DadosImpressaoEmissaoChamadaEncalhe;
import br.com.abril.nds.dto.DistribuidorDTO;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.filtro.FiltroEmissaoCE;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.Rota;
import br.com.abril.nds.model.cadastro.Roteiro;
import br.com.abril.nds.model.cadastro.TipoBox;
import br.com.abril.nds.model.cadastro.TipoImpressaoCE;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.serialization.custom.FlexiGridJson;
import br.com.abril.nds.service.BoletoService;
import br.com.abril.nds.service.BoxService;
import br.com.abril.nds.service.ChamadaEncalheService;
import br.com.abril.nds.service.FornecedorService;
import br.com.abril.nds.service.RoteirizacaoService;
import br.com.abril.nds.service.integracao.DistribuidorService;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.export.FileExporter;
import br.com.abril.nds.util.export.FileExporter.FileType;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

@Resource
@Path("/emissaoCE")
@Rules(Permissao.ROLE_RECOLHIMENTO_EMISSAO_CE)
public class EmissaoCEController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmissaoCEController.class);
    
	private static final String FILTRO_SESSION_ATTRIBUTE = "filtroEmissaoCE";
	
	private static final String BOLETOS_EM_BRANCO = "boletosEmBranco";
	
	private static final String DADOS_IMPRESSAO_CHAMADA_ENCALHE = "dadosImpressaoChamadaEncalhe";
	
	@Autowired
	private FornecedorService fornecedorService;
	
	@Autowired
	private BoxService boxService;
	
	@Autowired
	private RoteirizacaoService roteirizacaoService;
	
	@Autowired
	private DistribuidorService distribuidorService;
	
	@Autowired
	private BoletoService boletoService;
	
	@Autowired
	private ChamadaEncalheService chamadaEncalheService;
		
	@Autowired
	private HttpServletResponse httpResponse;
		
	@Autowired
	private HttpSession session;
			
	@Autowired
	private Result result;
	
	public void emissaoCE() {
		
	}
	
	/**
	 * Inicializa dados da tela
	 */
	@Path("/")
	public void index() {
		
		session.setAttribute(FILTRO_SESSION_ATTRIBUTE, null);
		
		session.setAttribute(BOLETOS_EM_BRANCO, null);
		
		session.setAttribute(DADOS_IMPRESSAO_CHAMADA_ENCALHE, null);
		
		String data = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
		result.include("data",data);		
		result.include("listaBoxes",carregarBoxes(boxService.buscarTodos(TipoBox.LANCAMENTO)));
		result.include("listaRotas",carregarRota(roteirizacaoService.buscarRotas()));
		result.include("listaRoteiros",carregarRoteiros(roteirizacaoService.buscarRoteiros()));
		result.include("listaFornecedores",carregarComboFornecedores(fornecedorService.obterFornecedoresAtivos()));
		
		result.forwardTo(EmissaoCEController.class).emissaoCE();
	}

	@Post
	public void pesquisar(FiltroEmissaoCE filtro, String sortname, String sortorder) {
		
        session.setAttribute(BOLETOS_EM_BRANCO, null);
		
		session.setAttribute(DADOS_IMPRESSAO_CHAMADA_ENCALHE, null);
		
		filtro.setOrdenacao(sortorder);
		
		filtro.setColunaOrdenacao(sortname);
		
		this.setFiltroSessao(filtro);
	
		final List<CotaEmissaoDTO> lista = chamadaEncalheService.obterDadosEmissaoChamadasEncalhe(filtro); 
		
		if(lista == null || lista.isEmpty()){
			
			throw new ValidacaoException(TipoMensagem.WARNING,"Nenhum dado foi encontrado!");
		}
		
		result.use(FlexiGridJson.class).from(lista).page(1).total(lista.size()).serialize();
	}
		
	private void validarCamposPesquisa(FiltroEmissaoCE filtro) {
		
		if(filtro.getDtRecolhimentoDe() == null){
			throw new ValidacaoException(TipoMensagem.WARNING, "O campo [Dt. Recolhimento] é inválido, o valor deve ser informado.");
		}
		
		if(filtro.getDtRecolhimentoAte() == null){
			throw new ValidacaoException(TipoMensagem.WARNING, "O campo [Até] é inválido, o valor deve ser informado.");
		}
		
		if(filtro.getDtRecolhimentoDe()!= null && filtro.getDtRecolhimentoAte() != null
				&& DateUtil.isDataInicialMaiorDataFinal(filtro.getDtRecolhimentoDe(), filtro.getDtRecolhimentoAte()))
			throw new ValidacaoException(TipoMensagem.WARNING, "Intervalo de Dt. Recolhimento inválido, o valor inicial é maior que o final.");
		
		if(filtro.getCodigoBoxDe() != null && filtro.getCodigoBoxAte() != null) {
			
			if(filtro.getCodigoBoxDe().compareTo(filtro.getCodigoBoxAte()) == 1) {
				throw new ValidacaoException(TipoMensagem.WARNING, 
						"Intervalo de Box inválido, o valor inicial é maior que o final.");
			}
		}
	}

	/**
	 * Método responsável por carregar o combo de fornecedores.
	 * @return 
	 */
	private List<ItemDTO<Long, String>> carregarComboFornecedores(List<Fornecedor> listaFornecedor) {
		
		List<ItemDTO<Long, String>> listaFornecedoresCombo = new ArrayList<ItemDTO<Long,String>>();
		
		for (Fornecedor fornecedor : listaFornecedor) {
			listaFornecedoresCombo.add(new ItemDTO<Long, String>(fornecedor.getId(), fornecedor.getJuridica().getRazaoSocial()));
		}
		
		return listaFornecedoresCombo;	
	}
	
	/**
	 * Carrega a lista de Boxes
	 * @return 
	 */
	private List<ItemDTO<Integer, String>> carregarBoxes(List<Box> listaBoxes){
		
		sortByCodigo(listaBoxes);
		
		List<ItemDTO<Integer, String>> boxes = new ArrayList<ItemDTO<Integer,String>>();
				
		for(Box box : listaBoxes){
			
			boxes.add(new ItemDTO<Integer, String>(box.getCodigo(),box.getCodigo() + " - " + box.getNome()));
		}
		
		return boxes;			
	}

	private void sortByCodigo(List<Box> listaBoxes) {
		Collections.sort(listaBoxes, new Comparator<Box>() {
			@Override
			public int compare(Box box1, Box box2) {
				if(box1.getCodigo()==null)
					return -1;
				return box1.getCodigo().compareTo(box2.getCodigo());
			}
		});
	}
		
	/**
	 * Retorna uma lista de Rota no formato ItemDTO
	 * @param rotas
	 * @return 
	 * @return List<ItemDTO<Long, String>>
	 */
	private List<ItemDTO<Long, String>> carregarRota(List<Rota> rotas){
		
		List<ItemDTO<Long, String>> listaRotas = new ArrayList<ItemDTO<Long,String>>();
		
		for(Rota rota : rotas){
			
			listaRotas.add(new ItemDTO<Long, String>(rota.getId(),rota.getDescricaoRota()));
		}
		
		return listaRotas;
	}
	
	/**
	 * Retorna uma lista de Roteiro no formato ItemDTO
	 * @param roteiros
	 * @return 
	 * @return List<ItemDTO<Long, String>>
	 */
	private List<ItemDTO<Long, String>> carregarRoteiros(List<Roteiro> roteiros){
		
		List<ItemDTO<Long, String>> listaRoteiros = new ArrayList<ItemDTO<Long,String>>();
		
		for(Roteiro rota : roteiros){
			
			listaRoteiros.add(new ItemDTO<Long, String>(rota.getId(),rota.getDescricaoRoteiro()));
		}
		
		return listaRoteiros;
	}

	private DadosImpressaoEmissaoChamadaEncalhe obterDadosImpressaoCE(FiltroEmissaoCE filtro){
		
		final TipoImpressaoCE tipoImpressao = this.distribuidorService.tipoImpressaoCE();
		
		filtro.setTipoImpressao(tipoImpressao);
		
		if(TipoImpressaoCE.MODELO_1.equals(tipoImpressao)) {
			
			filtro.setQtdProdutosPorPagina(20);
			filtro.setQtdCapasPorPagina(81);
			filtro.setQtdMaximaProdutosComTotalizacao(17);
		} else {
			
			filtro.setQtdProdutosPorPagina(25);
			filtro.setQtdCapasPorPagina(49);
			filtro.setQtdMaximaProdutosComTotalizacao(18);
		}

		
		return chamadaEncalheService.obterDadosImpressaoEmissaoChamadasEncalhe(filtro);
	}
	
	private String obterMsgCotasSemOperacaoDiferenciada(List<CotaEmissaoDTO> cotasSemOperacaoDiferenciada) {
		
		StringBuilder msg = new StringBuilder();

		msg.append(" As cotas abaixo não possuem operação diferenciada: ");
		
		for(CotaEmissaoDTO cota : cotasSemOperacaoDiferenciada) {
			
			msg.append(cota.getNomeCota())
			.append(" - ")
			.append(cota.getNumCota())
			.append(" <br/> ");
			
		}
		
		return msg.toString();
		
	}
	
	@Post
	@Path("/obterDadosImpressaoBoletosEmBranco")
	public void obterDadosImpressaoBoletosEmBranco(boolean verificarReemissao) { 
		
		session.setAttribute(BOLETOS_EM_BRANCO, null);
		
		FiltroEmissaoCE filtro = getFiltroSessao();
		
		boolean boletosEmitidosNoPeriodo = this.boletoService.existeBoletoAntecipadoPeriodoRecolhimentoECota(filtro.getNumCotaDe(),
				                                                                                             filtro.getNumCotaAte(), 
				                                                                                             filtro.getDtRecolhimentoDe(), 
				                                                                                             filtro.getDtRecolhimentoAte());

		Map<String, Object> resultados = new HashMap<>();
		
		if (!verificarReemissao || !boletosEmitidosNoPeriodo){
			
			List<Integer> cotasOperacaoDiferenciada = chamadaEncalheService.obterCotasComOperacaoDiferenciada(filtro);
			
			if(cotasOperacaoDiferenciada == null || cotasOperacaoDiferenciada.isEmpty()) {
				throw new ValidacaoException(TipoMensagem.WARNING, "Nenhuma cota com operação diferenciada encontrada!");
			}
			
			filtro.setCotasOperacaoDiferenciada(cotasOperacaoDiferenciada);
			
			List<CotaEmissaoDTO> cotasSemOperacaoDiferenciada = chamadaEncalheService.obterCotasSemOperacaoDiferenciada(filtro);
			
			DadosImpressaoEmissaoChamadaEncalhe dados = this.obterDadosImpressaoCE(filtro);
			
			if (dados == null){
				
				throw new ValidacaoException(TipoMensagem.WARNING,"Não foi possível Emitir o Boleto em Branco !");
			}
			
			List<BoletoEmBrancoDTO> boletosEmBranco = this.obterDadosBoletosEmBrancoPorListaCE(dados.getCotasEmissao(), filtro);
			
			if (boletosEmBranco!=null && boletosEmBranco.size() > 0){
				
				this.boletoService.salvaBoletosAntecipado(boletosEmBranco);
				
				session.setAttribute(BOLETOS_EM_BRANCO, boletosEmBranco);	
				
			} else {
				
				throw new ValidacaoException(TipoMensagem.WARNING,"Não foi possível Emitir o Boleto em Branco !");
				
			}
			
			
			
			if(cotasSemOperacaoDiferenciada!=null && !cotasSemOperacaoDiferenciada.isEmpty()){
				resultados.put("msgCotaSemOperacaoDiferenciada", obterMsgCotasSemOperacaoDiferenciada(cotasSemOperacaoDiferenciada));
			}
			
			resultados.put("existemBoletosEmBranco", Boolean.TRUE);
			
			result.use(Results.json()).from(resultados,"result").recursive().serialize();
	    }
		else{
			
			resultados.put("existemBoletosEmBranco", Boolean.FALSE);
			
			result.use(Results.json()).from(resultados,"result").recursive().serialize();
			
		}
	}

	public void imprimirCE() {
						
		TipoImpressaoCE tipoImpressao = this.distribuidorService.tipoImpressaoCE();
		
		if(tipoImpressao != null){
			
			switch (tipoImpressao) {
			
				case MODELO_1:
					
					result.forwardTo(EmissaoCEController.class).modelo1();
					
					break;
				case MODELO_2:
					
					result.forwardTo(EmissaoCEController.class).modelo2();
					
					break;
	
				default:
					
					break;
			}
		} else{
			
			result.nothing();
		}
	}

//    public void imprimirCENovo(FiltroEmissaoCE filtro) {
//        
//	    byte[] notasGeradas = null;
//	    
//	    try {
//            
//            notasGeradas = this.chamadaEncalheService.gerarEmissaoCE(filtro);
//    
//            if (notasGeradas != null) {
//    
//                DateFormat sdf = new SimpleDateFormat("yyyy-MM-ddhhmmss");
//        
//                this.httpResponse.setHeader("Content-Disposition", "attachment; filename=chamada-encalhe" + sdf.format(new Date()) + ".pdf");
//        
//                OutputStream output;
//        
//                output = this.httpResponse.getOutputStream();
//        
//                output.write(notasGeradas);
//        
//                httpResponse.getOutputStream().close();
//        
//                result.use(Results.nothing());
//            }
//        } catch (ValidacaoException e) {
//            LOGGER.error("Erro de validação ao gerar arquivos de chamada de encalhe: " + e.getMessage(), e);
//            result.use(Results.json()).from(e.getValidacao(), Constantes.PARAM_MSGS).recursive().serialize();
//        } catch (Exception e) {
//            LOGGER.error("Erro ao gerar arquivo(s) de chamada(s) de encalhe(s): " + e.getMessage(), e);
//            result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.ERROR, e.getMessage()), Constantes.PARAM_MSGS).recursive().serialize();
//        }
//        
//    }
	
	public void modelo1() {
				
		setDados(TipoImpressaoCE.MODELO_1);		
	}
	
    public void modelo2() {
		
		setDados(TipoImpressaoCE.MODELO_2);
	}
	
	public void setDados(TipoImpressaoCE tipoImpressao) {

		FiltroEmissaoCE filtro = getFiltroSessao();
		
		DadosImpressaoEmissaoChamadaEncalhe dados = this.obterDadosImpressaoCE(filtro);

		boolean apresentaCapas = (filtro.getCapa() == null) ? false : filtro.getCapa();
		
		boolean apresentaCapasPersonalizadas = (filtro.getPersonalizada() == null) ? false : filtro.getPersonalizada();
		
		if (dados == null){
		    
			throw new ValidacaoException(TipoMensagem.ERROR, "Não foi possível Emitir a CE !");
		}
		
		DistribuidorDTO dadosDistribuidor = distribuidorService.obterDadosEmissao();
				
		if(apresentaCapas && !apresentaCapasPersonalizadas) {
			result.include("capasPaginadas", dados.getCapasPaginadas());
		}
		
		if(apresentaCapas && !apresentaCapasPersonalizadas) {
			result.include("capasPaginadas", dados.getCapasPaginadas());
		}
		
		String dataRecolhimento = null;
		
		if(filtro.getDtRecolhimentoDe().equals(filtro.getDtRecolhimentoAte()))
			dataRecolhimento =  DateUtil.formatarDataPTBR(filtro.getDtRecolhimentoDe());
		else
			dataRecolhimento =  "De " + DateUtil.formatarDataPTBR(filtro.getDtRecolhimentoDe()) 
							 + " até " + DateUtil.formatarDataPTBR(filtro.getDtRecolhimentoAte());
		
		result.include("dataRecolhimento",  dataRecolhimento);
			
		result.include("cotasEmissao", dados.getCotasEmissao());
		
		result.include("dadosDistribuidor", dadosDistribuidor);
		
		result.include("withCapa", filtro.getCapa());
		
		result.include("personalizada", filtro.getPersonalizada());		
	}
	
	/**
	 * Obtem Map com Cota e Valor Total Liquido da C.E.
	 * @param listaCE
	 * @param filtro
	 * @return List<BoletoEmBrancoDTO>
	 */
	private List<BoletoEmBrancoDTO> obterDadosBoletosEmBrancoPorListaCE(List<CotaEmissaoDTO> listaCE, 
			                                                            FiltroEmissaoCE filtro){
		
		List<BoletoEmBrancoDTO> boletosEmBranco = new ArrayList<BoletoEmBrancoDTO>();
		
		for (CotaEmissaoDTO ceDTO : listaCE){

			BoletoEmBrancoDTO bbDTO = this.boletoService.obterDadosBoletoEmBrancoPorCE(ceDTO,filtro.getDtRecolhimentoDe(),filtro.getDtRecolhimentoAte());
			
			if (bbDTO!=null){
			    
				boletosEmBranco.add(bbDTO);
			}
		}
		
		return boletosEmBranco;
	}

	/**
	 * Exporta os dados da pesquisa.
	 * 
	 * @param fileType - tipo de arquivo
	 * 
	 * @throws IOException Exceção de E/S
	 */
	@SuppressWarnings("deprecation")
	@Get
	public void exportar(FileType fileType) throws IOException {
		
		FiltroEmissaoCE filtro = getFiltroSessao();

		List<CotaEmissaoDTO> lista = chamadaEncalheService.obterDadosEmissaoChamadasEncalhe(filtro); 
		
		if(lista.isEmpty()) {
			
			lista = new ArrayList<CotaEmissaoDTO>();
		}
		
		FileExporter.to("emissao_ce", fileType).inHTTPResponse(this.getNDSFileHeader(), filtro, null, 
				lista, CotaEmissaoDTO.class, this.httpResponse);
		
		result.nothing();
	}

	/**
	 * Exibe o(s) boleto(s) em branco em formato PDF.
	 * @throws Exception
	 */
	@Get
	@Path("/imprimeBoletoEmBranco")
	public void imprimeBoletoEmBranco() throws Exception{

		@SuppressWarnings("unchecked")
		List<BoletoEmBrancoDTO> boletosEmBranco = (List<BoletoEmBrancoDTO>) session.getAttribute(BOLETOS_EM_BRANCO);
		
        if (boletosEmBranco == null || boletosEmBranco.size() <= 0){
		    
			throw new ValidacaoException(TipoMensagem.ERROR, "Não foi possível Emitir os Boletos Em Branco !");
		}
		
		byte[] b = boletoService.geraImpressaoBoletosEmBranco(boletosEmBranco);

		this.httpResponse.setContentType("application/pdf");
		
		this.httpResponse.setHeader("Content-Disposition", "attachment; filename=boleto_em_branco.pdf");

		OutputStream output = this.httpResponse.getOutputStream();
		
		output.write(b);
		
		httpResponse.flushBuffer();
	}

	private void setFiltroSessao(FiltroEmissaoCE filtro) {
		
		validarCamposPesquisa(filtro);
		
		session.setAttribute(FILTRO_SESSION_ATTRIBUTE, filtro);
	}
	
	private FiltroEmissaoCE getFiltroSessao() {
		
		FiltroEmissaoCE filtro = (FiltroEmissaoCE) session.getAttribute(FILTRO_SESSION_ATTRIBUTE);
		
		if (filtro == null) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, "É necessario realizar a pesquisa primeiro !");
		}
		
		return filtro;
	}
	
}
