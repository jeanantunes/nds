package br.com.abril.nds.controllers.distribuicao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.controllers.BaseController;
import br.com.abril.nds.dto.EdicaoBaseEstudoDTO;
import br.com.abril.nds.dto.HistogramaPosEstudoAnaliseFaixaReparteDTO;
import br.com.abril.nds.dto.HistogramaPosEstudoDadoInicioDTO;
import br.com.abril.nds.dto.ProdutoBaseSugeridaDTO;
import br.com.abril.nds.dto.ResumoEstudoHistogramaPosAnaliseDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.planejamento.EstudoGeradoPreAnaliseDTO;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.service.EstudoProdutoEdicaoBaseService;
import br.com.abril.nds.service.EstudoService;
import br.com.abril.nds.service.HistogramaPosEstudoFaixaReparteService;
import br.com.abril.nds.service.LancamentoService;
import br.com.abril.nds.service.MatrizDistribuicaoService;
import br.com.abril.nds.service.PeriodoLancamentoParcialService;
import br.com.abril.nds.service.ProdutoBaseSugeridaService;
import br.com.abril.nds.service.UsuarioService;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.vo.ValidacaoVO;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

@Path("/distribuicao/histogramaPosEstudo")
@Resource
@Rules(Permissao.ROLE_DISTRIBUICAO_HISTOGRAMA_POS_ESTUDO)
public class HistogramaPosEstudoController extends BaseController{
	
    private static final Logger LOGGER = LoggerFactory.getLogger(HistogramaPosEstudoController.class);
	private final String[] faixaReparteInicial = {"0-4","5-9","10-19","20-49","50-9999999"}; 
	
	@Autowired
	private Result result;
	
	@Autowired
	private HttpSession session;
	
	@Autowired
	private EstudoProdutoEdicaoBaseService estudoProdutoEdicaoBaseService; 
	
	@Autowired
	private EstudoService estudoService;
	
	@Autowired
	private HistogramaPosEstudoFaixaReparteService histogramaPosEstudoFaixaReparteService;
	
	@Autowired
	private ProdutoBaseSugeridaService baseSugeridaService;

    @Autowired
    private MatrizDistribuicaoService matrizDistribuicaoService;
    
    @Autowired
    private PeriodoLancamentoParcialService periodoLancamentoParcialService;  
    
    @Autowired
	private UsuarioService usuarioService;
    
    public static final String MAPA_ANALISE_ESTUDO_CONTEXT_ATTRIBUTE = "mapa_analise_estudo";

    @Autowired
    private LancamentoService lancamentoService;
    
    @Path("/index")
	public void histogramaPosEstudo(Long idLancamento) {
	    
        if (idLancamento != null) {
			
			String modoAnalise = "NORMAL";
			
			if (this.lancamentoService.isLancamentoParcial(idLancamento)) {

			    modoAnalise = "PARCIAL";
			}
			
			result.include("modoAnalise", modoAnalise);
	    }
	}

    @Post
    @Transactional
    @Rules(Permissao.ROLE_DISTRIBUICAO_HISTOGRAMA_POS_ESTUDO_ALTERACAO)
    public void excluirEstudo(long id) {
        matrizDistribuicaoService.removeEstudo(id);
        result.use(Results.json()).withoutRoot().from(
                new ValidacaoException(TipoMensagem.SUCCESS, "Operação realizada com sucesso!")).recursive()
                .serialize();
    }

    @Post
	public void carregarDadosFieldsetHistogramaPreAnalise(HistogramaPosEstudoDadoInicioDTO selecionado ){
		
    	EstudoGeradoPreAnaliseDTO estudo = this.estudoService.obterEstudoPreAnalise(Long.parseLong(selecionado.getEstudo()));
				
    	if ( estudo == null )
    		   throw new ValidacaoException(TipoMensagem.WARNING, "É necessário cadastrar um segmento para o produto.");
    	
		String loginUsuario = super.getUsuarioLogado().getLogin();
		
		this.bloquearAnaliseEstudo(estudo.getIdProdutoEdicao(), this.session, loginUsuario);
		
		selecionado.setParcial(estudo.isParcial());

		String modoAnalise = "NORMAL";
		if (estudo.isParcial()) {
			
			selecionado.setPeriodicidadeProduto(periodoLancamentoParcialService.obterPeriodoPorIdLancamento(selecionado.getIdLancamento()).getNumeroPeriodo());
			
			modoAnalise = "PARCIAL";
		}
     
		if (estudo.getTipoSegmentoProduto() == null) {
			
            throw new ValidacaoException(TipoMensagem.WARNING, "É necessário cadastrar um segmento para o produto.");
		}
		
		selecionado.setTipoSegmentoProduto(estudo.getTipoSegmentoProduto());
		if (estudo != null && estudo.isLiberado()) {
			selecionado.setEstudoLiberado(Boolean.TRUE);
		} else {
			selecionado.setEstudoLiberado(Boolean.FALSE);
		}
		result.include("modoAnalise", modoAnalise);
		result.use(Results.json()).withoutRoot().from(selecionado).recursive().serialize();
	}
	
	@Post
	public void carregarDadosFieldSetResumoEstudo(long estudoId){
		ResumoEstudoHistogramaPosAnaliseDTO resumo = estudoService.obterResumoEstudo(estudoId, null, null);
		
		result.use(Results.json()).withoutRoot().from(resumo).recursive().serialize();
	}

	@Post
	public void carregarGridAnalise(String[] faixasReparte, int estudoId){

		List<HistogramaPosEstudoAnaliseFaixaReparteDTO> base = new ArrayList<>();
		
		String[] faixaIterator = faixasReparte;
		
		if (faixaIterator == null || faixaIterator.length == 0) {
			faixaIterator = faixaReparteInicial;
		}
		
		List<Long> listaIdEdicaoBaseEstudo = histogramaPosEstudoFaixaReparteService.obterIdEdicoesBase(Long.valueOf(estudoId));

		int lFaixas = faixaReparteInicial.length;
		
		Integer[][] faixasAgrupadas = new Integer[lFaixas][2];
		
		int i = 0;
		
		for (String faixas : faixaIterator) {
			
			int faixaDe = Integer.parseInt(faixas.split("-")[0]);
			
			int faixaAte = Integer.parseInt(faixas.split("-")[1]);
			
			faixasAgrupadas[i][0] = faixaDe;
			
			faixasAgrupadas[i][1] = faixaAte;
			
			i++;
		}
		
		base = this.histogramaPosEstudoFaixaReparteService.obterListaHistogramaPosEstudo(faixasAgrupadas, estudoId, listaIdEdicaoBaseEstudo);

		TableModel<CellModelKeyValue<HistogramaPosEstudoAnaliseFaixaReparteDTO>> tableModel = new TableModel<>();
		
		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(base));

		tableModel.setPage(1);

		tableModel.setTotal(faixaIterator.length);
		
		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
	}
	
	@Post
	public void carregarGridBaseSugerida(long estudoId){
		List<ProdutoBaseSugeridaDTO> baseSugeridaDTO = baseSugeridaService.obterBaseSugerida(estudoId);
		
		TableModel<CellModelKeyValue<ProdutoBaseSugeridaDTO>> tableModel = new TableModel<>();
		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(baseSugeridaDTO));
		tableModel.setPage(1);
		tableModel.setTotal(6);
		
		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
	}
	
	@Post
	public void carregarGridBaseEstudo(long estudoId){

		List<EdicaoBaseEstudoDTO> estudoProdutoEdicaoBaseDTO = estudoProdutoEdicaoBaseService.obterEdicoesBase(estudoId);
		
		TableModel<CellModelKeyValue<EdicaoBaseEstudoDTO>> tableModel = new TableModel<>();
		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(estudoProdutoEdicaoBaseDTO));
		tableModel.setPage(1);
		tableModel.setTotal(6);
		
		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
	}
	
	@SuppressWarnings("unchecked")
	private void bloquearAnaliseEstudo(Long idProdutoEdicao, 
									   HttpSession session, 
									   String loginUsuario) {
		
		if (idProdutoEdicao == null) {
			
			throw new ValidacaoException(
				new ValidacaoVO(TipoMensagem.WARNING, "Estudo inválido!"));
		}
		
		// conferir se ja nao tem um estudo aberto em outra aba nesta sessao
    	String windowname_estudo=(String) session.getAttribute("WINDOWNAME_ESTUDO");
    	String windowname=(String) session.getAttribute("WINDOWNAME");
    	
      	 
    	if ( windowname_estudo != null && windowname != null && !windowname_estudo.equals(windowname))
    	{
    		 throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, "Ja existe um Estudo sendo analisado em outra aba/janela"));
    	}
    	
    	
		
		Map<Long, String> mapaAnaliseEstudo = 
			(Map<Long, String>) session.getServletContext().getAttribute(
				MAPA_ANALISE_ESTUDO_CONTEXT_ATTRIBUTE);
		
		if (mapaAnaliseEstudo != null) {
			
			String loginUsuarioBloqueio = mapaAnaliseEstudo.get(idProdutoEdicao);
			
			if (loginUsuarioBloqueio != null
					&& !loginUsuarioBloqueio.equals(loginUsuario+";"+session.getAttribute("WINDOWNAME_ESTUDO"))) {
				LOGGER.error("ESTE ESTUDO ja ESTA SENDO ANALISADO PELO USUARIO "+this.usuarioService.obterNomeUsuarioPorLogin(loginUsuarioBloqueio)+
						"  BLOQUEADO COM="+session.getAttribute("WINDOWNAME_ESTUDO"));
				LOGGER.error("MAPA_ANALISE_ESTUDO_CONTEXT_ATTRIBUTE=" +mapaAnaliseEstudo.toString());
				
				throw new ValidacaoException(
					new ValidacaoVO(TipoMensagem.WARNING, 
						"Este estudo já está sendo analisado pelo usuário [" 
							+ this.usuarioService.obterNomeUsuarioPorLogin(loginUsuarioBloqueio.split(";")[0]) + "]."));
			}
			
		} else {
		
			mapaAnaliseEstudo = new HashMap<Long, String>();
		}
		session.setAttribute("WINDOWNAME_ESTUDO",windowname);
		mapaAnaliseEstudo.put(idProdutoEdicao, loginUsuario+";"+session.getAttribute("WINDOWNAME_ESTUDO"));
		LOGGER.warn("TRAVANDO ESTUDO COM  "+loginUsuario+";"+session.getAttribute("WINDOWNAME_ESTUDO"));
		
		session.getServletContext().setAttribute(
			MAPA_ANALISE_ESTUDO_CONTEXT_ATTRIBUTE, mapaAnaliseEstudo);
	}
	
	@SuppressWarnings("unchecked")
	public static void desbloquearAnaliseEstudo(HttpSession session, String loginUsuario) {

	
		
		Map<Long, String> mapaAnaliseEstudo = 
			(Map<Long, String>) session.getServletContext().getAttribute(
				MAPA_ANALISE_ESTUDO_CONTEXT_ATTRIBUTE);
		LOGGER.warn("DESBLOQUEANDO ESTUDO com "+loginUsuario+";"+session.getAttribute("WINDOWNAME_ESTUDO"));
		
		if (mapaAnaliseEstudo != null) {
			
			for (Map.Entry<Long, String> entry : mapaAnaliseEstudo.entrySet()) {
				
				if (entry.getValue().equals(loginUsuario+";"+session.getAttribute("WINDOWNAME_ESTUDO"))) {
					mapaAnaliseEstudo.remove(entry.getKey());
					session.removeAttribute("WINDOWNAME_ESTUDO");
					break;
				}
			}
		}
	}
	
	@Post 
	@Rules(Permissao.ROLE_DISTRIBUICAO_HISTOGRAMA_POS_ESTUDO_ALTERACAO)
	public void validar(){
		result.use(Results.json()).withoutRoot().from("").recursive().serialize();
	}
	
}
