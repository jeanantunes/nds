package br.com.abril.nds.controllers.devolucao;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.CapaDTO;
import br.com.abril.nds.dto.CotaEmissaoDTO;
import br.com.abril.nds.dto.DistribuidorDTO;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.filtro.FiltroEmissaoCE;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.integracao.service.DistribuidorService;
import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.Rota;
import br.com.abril.nds.model.cadastro.Roteiro;
import br.com.abril.nds.model.cadastro.TipoBox;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.serialization.custom.FlexiGridJson;
import br.com.abril.nds.service.BoxService;
import br.com.abril.nds.service.ChamadaEncalheService;
import br.com.abril.nds.service.FornecedorService;
import br.com.abril.nds.service.RoteirizacaoService;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.TipoMensagem;
import br.com.abril.nds.util.export.FileExporter;
import br.com.abril.nds.util.export.FileExporter.FileType;
import br.com.abril.nds.util.export.NDSFileHeader;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;

@Resource
@Path("/emissaoCE")
public class EmissaoCEController {

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
		

		if(filtro.getDtRecolhimentoDe()!= null && filtro.getDtRecolhimentoAte() != null
				&& DateUtil.isDataInicialMaiorDataFinal(filtro.getDtRecolhimentoDe(), filtro.getDtRecolhimentoAte()))
			throw new ValidacaoException(TipoMensagem.WARNING, "Intervalo de Dt. Recolhimento inválido, o valor inicial é maior que o final.");
		
		session.setAttribute(FILTRO_SESSION_ATTRIBUTE, filtro);
	
		List<CotaEmissaoDTO> lista = chamadaEncalheService.obterDadosEmissaoChamadasEncalhe(filtro); 
		
		result.use(FlexiGridJson.class).from(lista).page(1).total(lista.size()).serialize();
		
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
	private List<ItemDTO<Long, String>> carregarBoxes(List<Box> listaBoxes){
		
		List<ItemDTO<Long, String>> boxes = new ArrayList<ItemDTO<Long,String>>();
				
		for(Box box : listaBoxes){
			
			boxes.add(new ItemDTO<Long, String>(box.getId(),box.getCodigo() + " - " + box.getNome()));
		}
		
		return boxes;			
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
			
			listaRotas.add(new ItemDTO<Long, String>(rota.getId(),rota.getCodigoRota()));
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
		
		switch (distribuidorService.obter().getTipoImpressaoCE()) {
		case MODELO_1:
			result.forwardTo(EmissaoCEController.class).modelo1();
			break;
		case MODELO_2:
			result.forwardTo(EmissaoCEController.class).modelo2();
			break;

		default:
			break;
		}
		
		
	}

	public void modelo1() {
				
		setDados();		
				
	}
	
	public void setDados() {
		
		FiltroEmissaoCE filtro = (FiltroEmissaoCE) session.getAttribute(FILTRO_SESSION_ATTRIBUTE);
		
		List<CotaEmissaoDTO> cotasEmissao = chamadaEncalheService.obterDadosImpressaoEmissaoChamadasEncalhe(filtro);	
				
		DistribuidorDTO dadosDistribuidor = distribuidorService.obterDadosEmissao();
		
		if(filtro.getDtRecolhimentoAte()!=null && filtro.getDtRecolhimentoAte()!=null)
			filtro.setPersonalizada(false);
		
		if(filtro.getPersonalizada()) {
			List<CapaDTO> capas =  chamadaEncalheService.obterIdsCapasChamadaEncalhe(filtro.getDtRecolhimentoDe(), filtro.getDtRecolhimentoAte());
			result.include("capas", capas);
		}
		
		result.include("cotasEmissao", cotasEmissao);
		
		result.include("dadosDistribuidor", dadosDistribuidor);
		
		result.include("withCapa", filtro.getCapa());
		
		result.include("personalizada", filtro.getPersonalizada());
				
	}
	
	public void modelo2() {
		
		setDados();
	}

	/**
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
	
	/**
	 * Exporta os dados da pesquisa.
	 * 
	 * @param fileType - tipo de arquivo
	 * 
	 * @throws IOException Exceção de E/S
	 */
	@Get
	public void exportar(FileType fileType) throws IOException {
		
		FiltroEmissaoCE filtro = (FiltroEmissaoCE) session.getAttribute(FILTRO_SESSION_ATTRIBUTE);

		List<CotaEmissaoDTO> lista = chamadaEncalheService.obterDadosEmissaoChamadasEncalhe(filtro); 
	
		if(lista.isEmpty()) {
			lista = new ArrayList<CotaEmissaoDTO>();
		}
		
		FileExporter.to("emissao_ce", fileType).inHTTPResponse(this.getNDSFileHeader(), filtro, null, 
				lista, CotaEmissaoDTO.class, this.httpResponse);
		
		result.nothing();
	}
	
	/**
	 * Método que obtém o usuário logado
	 * 
	 * @return usuário logado
	 */
	public Usuario getUsuario() {
		//TODO getUsuario
		Usuario usuario = new Usuario();
		usuario.setId(1L);
		return usuario;
	}
}
