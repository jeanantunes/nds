package br.com.abril.nds.controllers.devolucao;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.controllers.BaseController;
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

@Resource
@Path("/emissaoCE")
@Rules(Permissao.ROLE_RECOLHIMENTO_EMISSAO_CE)
public class EmissaoCEController extends BaseController {

	private static final String FILTRO_SESSION_ATTRIBUTE = "filtroEmissaoCE";
	
	@Autowired
	private FornecedorService fornecedorService;
	
	@Autowired
	private BoxService boxService;
	
	@Autowired
	private RoteirizacaoService roteirizacaoService;
	
	@Autowired
	private DistribuidorService distribuidorService;
	
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
		
		filtro.setOrdenacao(sortorder);
		filtro.setColunaOrdenacao(sortname);
		
		this.setFiltroSessao(filtro);
	
		List<CotaEmissaoDTO> lista = chamadaEncalheService.obterDadosEmissaoChamadasEncalhe(filtro); 
		
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

	public void modelo1() {
				
		setDados(TipoImpressaoCE.MODELO_1);		
				
	}
	
	public void setDados(TipoImpressaoCE tipoImpressao) {
		
		FiltroEmissaoCE filtro = getFiltroSessao();
		
		if(TipoImpressaoCE.MODELO_1.equals(tipoImpressao)) {
			filtro.setQtdProdutosPorPagina(20);
			filtro.setQtdCapasPorPagina(81);
			filtro.setQtdMaximaProdutosComTotalizacao(19);
		} else {
			filtro.setQtdProdutosPorPagina(25);
			filtro.setQtdCapasPorPagina(49);
			filtro.setQtdMaximaProdutosComTotalizacao(20);
		}
		
		boolean apresentaCapas = (filtro.getCapa() == null) ? false : filtro.getCapa();
		
		boolean apresentaCapasPersonalizadas = (filtro.getPersonalizada() == null) ? false : filtro.getPersonalizada();
		
		DadosImpressaoEmissaoChamadaEncalhe dados = 
				chamadaEncalheService.obterDadosImpressaoEmissaoChamadasEncalhe(
						filtro);	
				
		DistribuidorDTO dadosDistribuidor = distribuidorService.obterDadosEmissao();
				
		if(apresentaCapas && !apresentaCapasPersonalizadas) {
			result.include("capasPaginadas", dados.getCapasPaginadas());
		}
			
		result.include("cotasEmissao", dados.getCotasEmissao());
		
		result.include("dadosDistribuidor", dadosDistribuidor);
		
		result.include("withCapa", filtro.getCapa());
		
		result.include("personalizada", filtro.getPersonalizada());
				
	}
	
	public void modelo2() {
		
		setDados(TipoImpressaoCE.MODELO_2);
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
		
		FiltroEmissaoCE filtro = getFiltroSessao();

		List<CotaEmissaoDTO> lista = chamadaEncalheService.obterDadosEmissaoChamadasEncalhe(filtro); 
	
		if(lista.isEmpty()) {
			lista = new ArrayList<CotaEmissaoDTO>();
		}
		
		FileExporter.to("emissao_ce", fileType).inHTTPResponse(this.getNDSFileHeader(), filtro, null, 
				lista, CotaEmissaoDTO.class, this.httpResponse);
		
		result.nothing();
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
