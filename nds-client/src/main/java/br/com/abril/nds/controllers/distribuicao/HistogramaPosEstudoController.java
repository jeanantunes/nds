package br.com.abril.nds.controllers.distribuicao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.controllers.BaseController;
import br.com.abril.nds.dto.EdicaoBaseEstudoDTO;
import br.com.abril.nds.dto.HistogramaPosEstudoAnaliseFaixaReparteDTO;
import br.com.abril.nds.dto.HistogramaPosEstudoDadoInicioDTO;
import br.com.abril.nds.dto.ProdutoBaseSugeridaDTO;
import br.com.abril.nds.dto.ResumoEstudoHistogramaPosAnaliseDTO;
import br.com.abril.nds.dto.TipoSegmentoProdutoDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.distribuicao.TipoSegmentoProduto;
import br.com.abril.nds.model.planejamento.EstudoGerado;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.service.EstudoProdutoEdicaoBaseService;
import br.com.abril.nds.service.EstudoService;
import br.com.abril.nds.service.HistogramaPosEstudoFaixaReparteService;
import br.com.abril.nds.service.MatrizDistribuicaoService;
import br.com.abril.nds.service.ProdutoBaseSugeridaService;
import br.com.abril.nds.service.ProdutoEdicaoService;
import br.com.abril.nds.service.ProdutoService;
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
	
	private String[] faixaReparteInicial = {"0-4","5-9","10-19","20-49","50-9999999"}; 
	
	@Autowired
	private Result result;
	
	@Autowired
	private HttpSession session;
	
	@Autowired
	private EstudoProdutoEdicaoBaseService estudoProdutoEdicaoBaseService; 
	
	@Autowired
	private ProdutoService produtoService;
	
	@Autowired
	private EstudoService estudoService;
	
	@Autowired
	private ProdutoEdicaoService produtoEdicaoService;
	
	@Autowired
	private HistogramaPosEstudoFaixaReparteService histogramaPosEstudoFaixaReparteService;
	
	@Autowired
	private ProdutoBaseSugeridaService baseSugeridaService;

    @Autowired
    private MatrizDistribuicaoService matrizDistribuicaoService;
    
    @Autowired
	private UsuarioService usuarioService;
    
    public static final String MAPA_ANALISE_ESTUDO_CONTEXT_ATTRIBUTE = "mapa_analise_estudo";

    @Path("/index")
	public void histogramaPosEstudo(String codigoProduto, String edicao) {
	    if (codigoProduto != null && !codigoProduto.isEmpty() && edicao != null && !edicao.isEmpty()) {
			ProdutoEdicao produtoEdicao = produtoEdicaoService.obterProdutoEdicaoPorCodProdutoNumEdicao(codigoProduto, edicao);
			String modoAnalise = "NORMAL";
			
			if (produtoEdicao.isParcial()) {
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
		
    	Produto produto = produtoService.obterProdutoPorCodigo(selecionado.getCodigoProduto());
		EstudoGerado estudo = (EstudoGerado) estudoService.obterEstudo(Long.parseLong(selecionado.getEstudo()));
		ProdutoEdicao produtoEdicao = produtoEdicaoService.obterProdutoEdicaoPorCodProdutoNumEdicao(selecionado.getCodigoProduto(), selecionado.getEdicao());
		
		String loginUsuario = super.getUsuarioLogado().getLogin();
		
		this.bloquearAnaliseEstudo(produtoEdicao.getId(), this.session, loginUsuario);
		
		selecionado.setParcial(produtoEdicao.isParcial());

		String modoAnalise = "NORMAL";
		if (produtoEdicao.isParcial()) {
			selecionado.setPeriodicidadeProduto(produto.getPeriodicidade().getOrdem());
			modoAnalise = "PARCIAL";
		}
		
		TipoSegmentoProduto segmento = produto.getTipoSegmentoProduto();
		
		if (segmento == null) {
			
            throw new ValidacaoException(TipoMensagem.WARNING, "É necessário cadastrar um segmento para o produto.");
		}
		
		TipoSegmentoProdutoDTO segmentoDTO = new TipoSegmentoProdutoDTO();
		
		if (segmento != null) {
		
			segmentoDTO.setIdSegmento(segmento.getId());
			segmentoDTO.setDescricao(segmento.getDescricao());
		}
		
		selecionado.setTipoSegmentoProduto(segmentoDTO);
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
		ResumoEstudoHistogramaPosAnaliseDTO resumo = estudoService.obterResumoEstudo(estudoId);
		
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
		
		for (String faixas : faixaIterator) {
			int faixaDe = Integer.parseInt(faixas.split("-")[0]);
			int faixaAte = Integer.parseInt(faixas.split("-")[1]);
			
			HistogramaPosEstudoAnaliseFaixaReparteDTO baseEstudoAnaliseFaixaReparteDTO = histogramaPosEstudoFaixaReparteService.obterHistogramaPosEstudo(faixaDe, faixaAte, estudoId, listaIdEdicaoBaseEstudo);
			
			base.add(baseEstudoAnaliseFaixaReparteDTO);
		}
		
        // última faixa
		HistogramaPosEstudoAnaliseFaixaReparteDTO baseEstudoAnaliseFaixaReparteDTO = histogramaPosEstudoFaixaReparteService.obterHistogramaPosEstudo(0, 999999999, estudoId, listaIdEdicaoBaseEstudo);
		base.add(baseEstudoAnaliseFaixaReparteDTO);
		
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
		
		Map<Long, String> mapaAnaliseEstudo = 
			(Map<Long, String>) session.getServletContext().getAttribute(
				MAPA_ANALISE_ESTUDO_CONTEXT_ATTRIBUTE);
		
		if (mapaAnaliseEstudo != null) {
			
			String loginUsuarioBloqueio = mapaAnaliseEstudo.get(idProdutoEdicao);
			
			if (loginUsuarioBloqueio != null
					&& !loginUsuarioBloqueio.equals(loginUsuario)) {
				
				throw new ValidacaoException(
					new ValidacaoVO(TipoMensagem.WARNING, 
						"Este estudo já está sendo analisado pelo usuário [" 
							+ this.usuarioService.obterNomeUsuarioPorLogin(loginUsuarioBloqueio) + "]."));
			}
		} else {
		
			mapaAnaliseEstudo = new HashMap<Long, String>();
		}
		
		mapaAnaliseEstudo.put(idProdutoEdicao, loginUsuario);
		
		session.getServletContext().setAttribute(
			MAPA_ANALISE_ESTUDO_CONTEXT_ATTRIBUTE, mapaAnaliseEstudo);
	}
	
	@SuppressWarnings("unchecked")
	public static void desbloquearAnaliseEstudo(ServletContext servletContext, String loginUsuario) {

		Map<Long, String> mapaAnaliseEstudo = 
			(Map<Long, String>) servletContext.getAttribute(
				MAPA_ANALISE_ESTUDO_CONTEXT_ATTRIBUTE);
		
		if (mapaAnaliseEstudo != null) {
			
			for (Map.Entry<Long, String> entry : mapaAnaliseEstudo.entrySet()) {
				
				if (entry.getValue().equals(loginUsuario)) {
					
					mapaAnaliseEstudo.remove(entry.getKey());
					
					break;
				}
			}
		}
	}
	
}
