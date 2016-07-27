package br.com.abril.nds.controllers.expedicao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.controllers.BaseController;
import br.com.abril.nds.dto.CotaExemplaresDTO;
import br.com.abril.nds.dto.FornecedorExemplaresDTO;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.RegiaoDTO;
import br.com.abril.nds.dto.filtro.FiltroNFeDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.NotaFiscalTipoEmissao.NotaFiscalTipoEmissaoEnum;
import br.com.abril.nds.model.cadastro.Rota;
import br.com.abril.nds.model.cadastro.Roteiro;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.fiscal.NaturezaOperacao;
import br.com.abril.nds.model.fiscal.NotaFiscalTipoEmissaoRegimeEspecial;
import br.com.abril.nds.model.fiscal.TipoDestinatario;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.serialization.custom.CustomJson;
import br.com.abril.nds.serialization.custom.FlexiGridJson;
import br.com.abril.nds.service.CotaService;
import br.com.abril.nds.service.FornecedorService;
import br.com.abril.nds.service.NFeService;
import br.com.abril.nds.service.NaturezaOperacaoService;
import br.com.abril.nds.service.RegiaoService;
import br.com.abril.nds.service.RoteirizacaoService;
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

	private static final Logger LOGGER = LoggerFactory.getLogger(GeracaoNFeController.class);
	
	@Autowired
	private Result result;
	
	@Autowired
	private DistribuidorService distribuidorService;
	
	@Autowired 
	private NFeService nfeService;
	
	@Autowired
	private FornecedorService fornecedorService;
	
	@Autowired
	private NaturezaOperacaoService tipoNotaFiscalService;
	
	@Autowired
	private HttpServletResponse httpServletResponse;

	@Autowired
	private CotaService cotaService;
	
	@Autowired
	private RoteirizacaoService roteirizacaoService;
	
	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	private NaturezaOperacaoService naturezaOperacaoService;
	
	@Autowired
	private RegiaoService regiaoService;
	
	private static final String FILTRO_SESSION_NOTA_FISCAL = "filtroNotaFiscal";
	
	@Path("/")
	public void index() {
		
		this.obterFornecedoresDestinatarios();
		this.obterTodosFornecedoresAtivos();
		this.iniciarComboRoteiro();
		this.iniciarComboRota();
		this.obterTiposDestinatarios();
		this.iniciarComboBox();
		this.iniciarTiposEmissaoRegimeEspecial();
		this.carregarComboRegiao();
		
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
	
	private void iniciarTiposEmissaoRegimeEspecial() {
		result.include("tiposEmissaoRegimeEspecial", NotaFiscalTipoEmissaoRegimeEspecial.values());
	}
	
	/**
     * Inicia o combo Box
     */
    private void iniciarComboBox() {

    	result.include("listaBox", this.roteirizacaoService.getComboTodosBoxes());
    }
    
	public void carregarComboRegiao() {

		List<ItemDTO<Long,String>> comboRegiao =  new ArrayList<ItemDTO<Long,String>>();
		List<RegiaoDTO> regioes = regiaoService.buscarRegiao();

		for (RegiaoDTO itemRegiao : regioes) {
			comboRegiao.add(new ItemDTO<Long,String>(itemRegiao.getIdRegiao() , itemRegiao.getNomeRegiao()));
		}

		result.include("listaRegiao",comboRegiao);
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
	public void verificarRegimeEspecialNaturezaOperacao(Long naturezaOperacaoId) {
		
		NotaFiscalTipoEmissaoEnum tipoEmissao = null;
		if(naturezaOperacaoId != null && naturezaOperacaoId > 0) {
			
			tipoEmissao = naturezaOperacaoService.verificarRegimeEspecialNaturezaOperacao(naturezaOperacaoId);
		}
	
		if(tipoEmissao != null) {
			result.use(Results.json()).from(tipoEmissao, "tipoEmissaoRegimeEspecial").serialize();
		} else {
			result.use(Results.json()).from("", "tipoEmissaoRegimeEspecial").serialize();
		}
	}
	
	@Post
	@Transactional
	public void pesquisar(final FiltroNFeDTO filtro, NotaFiscalTipoEmissaoRegimeEspecial notaFiscalTipoEmissaoRegimeEspecial, final String sortname, final String sortorder, final int rp, final int page) {
		
		if(filtro.getIdNaturezaOperacao() != null && filtro.getIdNaturezaOperacao() < 0) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Selecione uma Natureza de Operação.");
		}
		
		//FIXME: vRaptor nao instanciou dentro do filtro 
		filtro.setNotaFiscalTipoEmissao(notaFiscalTipoEmissaoRegimeEspecial);
		
		List<CotaExemplaresDTO> cotaExemplaresDTOs = null;
		List<FornecedorExemplaresDTO> fornecedorExemplaresDTOs = null;
		
		Long totalRegistros = 0L;
		request.getSession().setAttribute(FILTRO_SESSION_NOTA_FISCAL, filtro);
		
		final PaginacaoVO paginacao = carregarPaginacao(sortname, sortorder, rp, page);
		
		filtro.setPaginacaoVO(paginacao);
		
		final NaturezaOperacao naturezaOperacao = this.naturezaOperacaoService.obterNaturezaOperacaoPorId(filtro.getIdNaturezaOperacao());
		
		switch (naturezaOperacao.getTipoDestinatario()) {
		
			case COTA:
				cotaExemplaresDTOs = nfeService.consultaCotaExemplaresSumarizados(filtro, naturezaOperacao);
				totalRegistros = nfeService.consultaCotaExemplareSumarizadoQtd(filtro, naturezaOperacao);			
				break;
				
			case DISTRIBUIDOR:
				cotaExemplaresDTOs = nfeService.consultaCotaExemplaresSumarizados(filtro, naturezaOperacao);			
				totalRegistros = nfeService.consultaCotaExemplareSumarizadoQtd(filtro, naturezaOperacao);
				break;
				
			case FORNECEDOR:			
				fornecedorExemplaresDTOs = nfeService.consultaFornecedorExemplarSumarizado(filtro, naturezaOperacao);
				totalRegistros = nfeService.consultaFornecedorExemplaresSumarizadosQtd(filtro, naturezaOperacao);
				break;
			
		}
		
		if(naturezaOperacao.getTipoDestinatario().equals(TipoDestinatario.FORNECEDOR)) {
			if (fornecedorExemplaresDTOs == null || fornecedorExemplaresDTOs.isEmpty()){
				throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum registro encontrado.");
			}
			result.use(FlexiGridJson.class).from(fornecedorExemplaresDTOs).page(page).total(totalRegistros.intValue()).serialize();			
		} else {
			if (cotaExemplaresDTOs == null || cotaExemplaresDTOs.isEmpty()){
				throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum registro encontrado.");
			}
			result.use(FlexiGridJson.class).from(cotaExemplaresDTOs).page(page).total(totalRegistros.intValue()).serialize();
		}
	}

	private PaginacaoVO carregarPaginacao(String sortname, String sortorder, int rp, int page) {
		PaginacaoVO paginacao = new PaginacaoVO();
		paginacao.setOrdenacao(Ordenacao.ASC);
	    paginacao.setPaginaAtual(page);
	    paginacao.setQtdResultadosPorPagina(rp);
	    paginacao.setSortOrder(sortorder);
	    paginacao.setSortColumn(sortname);
		return paginacao;
	}
	
	@Post("/buscaCotasSuspensas.json")
	public void buscaCotasSuspensas(FiltroNFeDTO filtro, List<Long> listIdFornecedor, Long tipoNotaFiscal, String sortname,
			String sortorder, int rp, int page) {
		
		Intervalo<Integer> intervaloBox = new Intervalo<Integer>(filtro.getIntervaloBoxInicial(), filtro.getIntervaloBoxFinal());
		
		Intervalo<Integer> intervaloCota = new Intervalo<Integer>(filtro.getIntervalorCotaInicial(), filtro.getIntervalorCotaInicial());
		
		Intervalo<Date> intervaloDateMovimento = new Intervalo<Date>(filtro.getDataInicial(), filtro.getDataFinal());
		
		List<CotaExemplaresDTO> cotaExemplaresDTOs = nfeService.busca(intervaloBox, intervaloCota, intervaloDateMovimento, listIdFornecedor, 
						tipoNotaFiscal, null, null, sortname, sortorder, rp, page, SituacaoCadastro.SUSPENSO);
		
		result.use(FlexiGridJson.class).from(cotaExemplaresDTOs).page(page).total(cotaExemplaresDTOs.size()).serialize();
	}
	
	@Post("/hasCotasSuspensas.json")
	public void hasCotasSuspensas(Integer intervaloBoxDe, Integer intervaloBoxAte, Integer intervaloCotaDe, Integer intervaloCotaAte,
			Date intervaloDateMovimentoDe, Date intervaloDateMovimentoAte, List<Long> listIdFornecedor,Long tipoNotaFiscal){

		boolean hasCotasSuspensas = false;
		
		Intervalo<Integer> intervaloBox = new Intervalo<Integer>(intervaloBoxDe, intervaloBoxAte);
		
		Intervalo<Integer> intervaloCota = new Intervalo<Integer>(intervaloCotaDe, intervaloCotaAte);
		
		List<Cota> cotasSuspensas = this.cotaService.obterCotasEntre(intervaloCota, intervaloBox, SituacaoCadastro.SUSPENSO);
		
		if (cotasSuspensas != null && !cotasSuspensas.isEmpty())
			hasCotasSuspensas = true;
		
		result.use(CustomJson.class).from(hasCotasSuspensas).serialize();
	}
	
	@Post
	@Rules(Permissao.ROLE_NFE_GERACAO_NFE_ALTERACAO)
	public void gerarNotasFiscais(FiltroNFeDTO filtro, NotaFiscalTipoEmissaoRegimeEspecial notaFiscalTipoEmissaoRegimeEspecial, List<Long> idCotasSuspensas, boolean todasCotasSuspensa){
		
		try {

			if(filtro.getIdNaturezaOperacao() != null && filtro.getIdNaturezaOperacao() < 0) {
				throw new ValidacaoException(TipoMensagem.WARNING, "Selecione uma Natureza de Operação.");
			}
			
			//FIXME: vRaptor nao instanciou dentro do filtro 
			filtro.setNotaFiscalTipoEmissao(notaFiscalTipoEmissaoRegimeEspecial);
			
			this.nfeService.gerarNotaFiscal(filtro);
			
		} catch (Exception e) {
			
			LOGGER.error("Erro ao gerar NF-e.", e);
			throw new ValidacaoException(TipoMensagem.WARNING, e.getMessage());
		} 
		
		result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "NF-e(s) gerada(s) com sucesso."), Constantes.PARAM_MSGS).serialize();
	}
	
	public List<ItemDTO<Long, String>> carregarTipoNotaFiscal() {
		
		List<ItemDTO<Long, String>> listaTipoNotaFiscal = this.tipoNotaFiscalService.carregarComboNaturezasOperacoes(this.distribuidorService.tipoAtividade());
		
		return listaTipoNotaFiscal;
	}
	
	@Post
    @Rules(Permissao.ROLE_NFE_GERACAO_NFE_ALTERACAO)
	public void exportar(final FiltroNFeDTO filtro, NotaFiscalTipoEmissaoRegimeEspecial notaFiscalTipoEmissaoRegimeEspecial, final String sortname, final String sortorder, final int rp, final int page, FileType fileType) throws IOException {
		
	    List<CotaExemplaresDTO> cotaExemplaresDTOs = null;
        List<FornecedorExemplaresDTO> fornecedorExemplaresDTOs = null;
	    
        filtro.setNotaFiscalTipoEmissao(notaFiscalTipoEmissaoRegimeEspecial);
        
	    final NaturezaOperacao naturezaOperacao = this.naturezaOperacaoService.obterNaturezaOperacaoPorId(filtro.getIdNaturezaOperacao());
	    
	    switch (naturezaOperacao.getTipoDestinatario()) {
        
            case COTA:
                cotaExemplaresDTOs = nfeService.consultaCotaExemplaresSumarizados(filtro, naturezaOperacao);            
                break;
                
            case DISTRIBUIDOR:
                cotaExemplaresDTOs = nfeService.consultaCotaExemplaresSumarizados(filtro, naturezaOperacao);            
                break;
                
            case FORNECEDOR:            
                fornecedorExemplaresDTOs = nfeService.consultaFornecedorExemplarSumarizado(filtro, naturezaOperacao);
                break;        
	    }
	    
	    if(cotaExemplaresDTOs != null ){
	        FileExporter.to("consignado-encalhe", fileType).inHTTPResponse(this.getNDSFileHeader(), null, cotaExemplaresDTOs, CotaExemplaresDTO.class, this.httpServletResponse);
	    } else if (fornecedorExemplaresDTOs != null) {
	        FileExporter.to("consignado-encalhe", fileType).inHTTPResponse(this.getNDSFileHeader(), null, fornecedorExemplaresDTOs, FornecedorExemplaresDTO.class, this.httpServletResponse);
	    } else {
	        throw new ValidacaoException(TipoMensagem.WARNING ,"Problema ao expostar as informções para excel.");
	    } 
		
	    result.use(Results.nothing());
		
	}
}