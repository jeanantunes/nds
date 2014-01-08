package br.com.abril.nds.controllers.expedicao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.controllers.BaseController;
import br.com.abril.nds.dto.CotaExemplaresDTO;
import br.com.abril.nds.dto.FornecedorExemplaresDTO;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.filtro.FiltroViewNotaFiscalDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Rota;
import br.com.abril.nds.model.cadastro.Roteiro;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.fiscal.NaturezaOperacao;
import br.com.abril.nds.model.fiscal.TipoDestinatario;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.repository.NaturezaOperacaoRepository;
import br.com.abril.nds.serialization.custom.CustomJson;
import br.com.abril.nds.serialization.custom.FlexiGridJson;
import br.com.abril.nds.service.CotaService;
import br.com.abril.nds.service.FornecedorService;
import br.com.abril.nds.service.GeracaoNFeService;
import br.com.abril.nds.service.NotaFiscalService;
import br.com.abril.nds.service.RoteirizacaoService;
import br.com.abril.nds.service.TipoNotaFiscalService;
import br.com.abril.nds.service.integracao.DistribuidorService;
import br.com.abril.nds.util.Constantes;
import br.com.abril.nds.util.Intervalo;
import br.com.abril.nds.util.export.FileExporter;
import br.com.abril.nds.util.export.FileExporter.FileType;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.abril.nds.vo.PaginacaoVO.Ordenacao;
import br.com.abril.nds.vo.ValidacaoVO;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

@Resource
@Path("/expedicao/geracaoNFe")
@Rules(Permissao.ROLE_NFE_GERACAO_NFE)
public class GeracaoNFeController extends BaseController {

	@Autowired
	private Result result;
	
	@Autowired
	private DistribuidorService distribuidorService;
	
	@Autowired 
	private GeracaoNFeService geracaoNFeService;
	
	@Autowired 
	private NotaFiscalService notaFiscalService;
	
	@Autowired
	private FornecedorService fornecedorService;
	
	@Autowired
	private TipoNotaFiscalService tipoNotaFiscalService;
	
	@Autowired
	private HttpServletResponse httpServletResponse;

	@Autowired
	private CotaService cotaService;
	
	@Autowired
	private RoteirizacaoService roteirizacaoService;
	
	@Autowired
	private HttpServletRequest request;
	
	
	@Autowired
	private NaturezaOperacaoRepository naturezaOperacaoRepository;
	
	private static final String FILTRO_SESSION_NOTA_FISCAL = "filtroNotaFiscal";
	
	@Path("/")
	public void index() {
		
		this.obterFornecedoresDestinatarios();
		this.obterTodosFornecedoresAtivos();
		this.iniciarComboRoteiro();
		this.iniciarComboRota();
		this.obterTiposDestinatarios();
		this.iniciarComboBox();
		
	}

	private void obterTiposDestinatarios() {
		result.include("tiposDestinatarios", new TipoDestinatario[] {TipoDestinatario.COTA, TipoDestinatario.DISTRIBUIDOR, TipoDestinatario.FORNECEDOR});
	}
	
	private void obterFornecedoresDestinatarios() {
		result.include("fornecedoresDestinatarios", fornecedorService.obterFornecedoresDestinatarios(SituacaoCadastro.ATIVO));
	}

	private void obterTodosFornecedoresAtivos() {
		result.include("fornecedores", fornecedorService.obterFornecedoresIdNome(SituacaoCadastro.ATIVO, true));
	}
	
	/**
     * Inicia o combo Box
     */
    private void iniciarComboBox() {

	result.include("listaBox", this.roteirizacaoService.getComboTodosBoxes());
    }

	private void iniciarComboRoteiro() {
		//result.include("listaTipoNotaFiscal", this.carregarTipoNotaFiscal());
		
		List<Roteiro> roteiros = this.roteirizacaoService.buscarRoteiro(null, null);
		
		List<ItemDTO<Long, String>> listRoteiro = new ArrayList<ItemDTO<Long,String>>();
		
		for (Roteiro roteiro : roteiros){
			
			listRoteiro.add(new ItemDTO<Long, String>(roteiro.getId(), roteiro.getDescricaoRoteiro()));
		}
		
		result.include("roteiros", listRoteiro);
	}

	private void iniciarComboRota() {
		List<Rota> rotas = this.roteirizacaoService.buscarRota(null, null);
		
		List<ItemDTO<Long, String>> listRota = new ArrayList<ItemDTO<Long,String>>();
		
		for (Rota rota : rotas){
			
			listRota.add(new ItemDTO<Long, String>(rota.getId(), rota.getDescricaoRota()));
		}
		
		result.include("rotas", listRota);
	}
	
	@Post
	public void obterNaturezasOperacoesPorTipoDestinatario(TipoDestinatario tipoDestinatario) {
		
		List<ItemDTO<Long, String>> naturezasOperacoes = notaFiscalService.obterNaturezasOperacoesPorTipoDestinatario(tipoDestinatario);
	
		result.use(FlexiGridJson.class).from(naturezasOperacoes).serialize();
	}
	
	@Post
	public void pesquisar(FiltroViewNotaFiscalDTO filtro, String sortname, String sortorder, int rp, int page) {
		
		List<CotaExemplaresDTO> cotaExemplaresDTOs = null;
		List<FornecedorExemplaresDTO> fornecedorExemplaresDTOs = null;
		
		Long totalRegistros = 0L;
		request.getSession().setAttribute(FILTRO_SESSION_NOTA_FISCAL, filtro);
		
		PaginacaoVO paginacao = carregarPaginacao(sortname, sortorder, rp, page);
		
		filtro.setPaginacaoVO(paginacao);
		

		NaturezaOperacao naturezaOperacao = this.naturezaOperacaoRepository.obterNaturezaOperacao(filtro.getIdNaturezaOperacao());
		
		switch (naturezaOperacao.getTipoDestinatario()) {
		case COTA:
			cotaExemplaresDTOs = geracaoNFeService.consultaCotaExemplareSumarizado(filtro);			
			totalRegistros = geracaoNFeService.consultaCotaExemplareSumarizadoQtd(filtro);
			
			break;
			
		case DISTRIBUIDOR:
			
			
			break;
			
		case FORNECEDOR:			
			
			fornecedorExemplaresDTOs = geracaoNFeService.consultaFornecedorExemplarSumarizado(filtro);
			
			System.out.println("Chegou aqui ahahahahah");
			break;


		}
		
		//if (cotaExemplaresDTOs == null || cotaExemplaresDTOs.isEmpty() || fornecedorExemplaresDTOs == null || fornecedorExemplaresDTOs.isEmpty()){
			//throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum registro encontrado.");
		//}
		
		if(naturezaOperacao.getTipoDestinatario().getDescricao().equals("Fornecedor")){
			result.use(FlexiGridJson.class).from(fornecedorExemplaresDTOs).page(page).total(totalRegistros.intValue()).serialize();			
		}else{
			result.use(FlexiGridJson.class).from(cotaExemplaresDTOs).page(page).total(totalRegistros.intValue()).serialize();
		}
	}

	private PaginacaoVO carregarPaginacao(String sortname, String sortorder, int rp,
			int page) {
		PaginacaoVO paginacao = new PaginacaoVO();
		paginacao.setOrdenacao(Ordenacao.ASC);
	    paginacao.setPaginaAtual(page);
	    paginacao.setQtdResultadosPorPagina(rp);
	    paginacao.setSortOrder(sortorder);
	    paginacao.setSortColumn(sortname);
		return paginacao;
	}
	
	@Post("/buscaCotasSuspensas.json")
	public void buscaCotasSuspensas(FiltroViewNotaFiscalDTO filtro, List<Long> listIdFornecedor, Long tipoNotaFiscal, String sortname,
			String sortorder, int rp, int page) {
		
		Intervalo<Integer> intervaloBox = new Intervalo<Integer>(filtro.getIntervaloBoxInicial(), filtro.getIntervaloBoxFinal());
		
		Intervalo<Integer> intervaloCota = new Intervalo<Integer>(filtro.getIntervalorCotaInicial(), filtro.getIntervalorCotaInicial());
		
		Intervalo<Date> intervaloDateMovimento = new Intervalo<Date>(filtro.getDataInicial(), filtro.getDataFinal());
		
		List<CotaExemplaresDTO> cotaExemplaresDTOs = 
				geracaoNFeService.busca(intervaloBox, intervaloCota, intervaloDateMovimento, listIdFornecedor, 
						tipoNotaFiscal, null, null, sortname, sortorder, rp, page, SituacaoCadastro.SUSPENSO);
		
		result.use(FlexiGridJson.class).from(cotaExemplaresDTOs).page(page).total(cotaExemplaresDTOs.size()).serialize();
	}
	
	@Post("/hasCotasSuspensas.json")
	public void hasCotasSuspensas(Integer intervaloBoxDe, 	  Integer intervaloBoxAte,
			Integer intervaloCotaDe, Integer intervaloCotaAte,
			Date intervaloDateMovimentoDe, Date intervaloDateMovimentoAte, List<Long> listIdFornecedor,Long tipoNotaFiscal){

		boolean hasCotasSuspensas = false;
		
		Intervalo<Integer> intervaloBox = new Intervalo<Integer>(intervaloBoxDe, intervaloBoxAte);
		
		Intervalo<Integer> intervaloCota = new Intervalo<Integer>(intervaloCotaDe, intervaloCotaAte);
		
		List<Cota> cotasSuspensas = this.cotaService.obterCotasEntre(intervaloCota, intervaloBox, SituacaoCadastro.SUSPENSO);
		
		if (cotasSuspensas != null && !cotasSuspensas.isEmpty())
			hasCotasSuspensas = true;
		
		result.use(CustomJson.class).from(hasCotasSuspensas).serialize();
	}
	
	@Post("/gerar.json")
	@Rules(Permissao.ROLE_NFE_GERACAO_NFE_ALTERACAO)
	public void gerar(FiltroViewNotaFiscalDTO filtro, List<Long> idCotasSuspensas, boolean todasCotasSuspensa){
		try {
			this.geracaoNFeService.gerarNotaFiscal(filtro, idCotasSuspensas, null);
		} catch (IOException ioe){
			throw new ValidacaoException(TipoMensagem.WARNING, ioe.getMessage());
		} 
		
		result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS,"Geração de NFE com sucesso."),Constantes.PARAM_MSGS).serialize();
	}
	
	@Post("/transferirSuplementar.json")
	public void transferirSuplementar(List<Long> idsCota){
		
		//TODO
		
		result.use(CustomJson.class).from(true).serialize();
	}
	
	public List<ItemDTO<Long, String>> carregarTipoNotaFiscal() {
		
		List<ItemDTO<Long, String>> listaTipoNotaFiscal = 
				this.tipoNotaFiscalService.carregarComboTiposNotasFiscais(
						this.distribuidorService.tipoAtividade());
		
		return listaTipoNotaFiscal;
	}
	
	@SuppressWarnings("deprecation")
	public void exportar(Integer intervaloBoxDe, Integer intervaloBoxAte,
			Integer intervaloCotaDe, Integer intervaloCotaAte,
			Date intervaloDateMovimentoDe, Date intervaloDateMovimentoAte, List<Long> listIdFornecedor, Long tipoNotaFiscal,String sortname,
			String sortorder,FileType fileType) throws IOException {
		
		Intervalo<Integer> intervaloBox = new Intervalo<Integer>(intervaloBoxDe, intervaloBoxAte);
		
		Intervalo<Integer> intervalorCota = new Intervalo<Integer>(intervaloCotaDe, intervaloCotaAte);
		
		Intervalo<Date> intervaloDateMovimento = new Intervalo<Date>(intervaloDateMovimentoDe, intervaloDateMovimentoAte);
		
		List<CotaExemplaresDTO> cotaExemplaresDTOs =	
				geracaoNFeService.busca(intervaloBox, intervalorCota, intervaloDateMovimento, listIdFornecedor, 
						tipoNotaFiscal, null, null, sortname, sortorder, null, null, null);
		
		FileExporter.to("consignado-encalhe", fileType).inHTTPResponse(
				this.getNDSFileHeader(), null, null,
				cotaExemplaresDTOs, CotaExemplaresDTO.class,
				this.httpServletResponse);
		
		result.use(Results.nothing());
		
	}
}
