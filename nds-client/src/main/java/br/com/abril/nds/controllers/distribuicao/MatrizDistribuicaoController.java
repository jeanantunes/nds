package br.com.abril.nds.controllers.distribuicao;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.SerializationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.client.util.PaginacaoUtil;
import br.com.abril.nds.client.vo.CopiaProporcionalDeDistribuicaoVO;
import br.com.abril.nds.client.vo.ParametrosDistribuidorVO;
import br.com.abril.nds.client.vo.ProdutoDistribuicaoVO;
import br.com.abril.nds.client.vo.TotalizadorProdutoDistribuicaoVO;
import br.com.abril.nds.controllers.BaseController;
import br.com.abril.nds.dto.filtro.FiltroDistribuicaoDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.estudo.EstudoTransient;
import br.com.abril.nds.model.estudo.ProdutoEdicaoEstudo;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.service.CalendarioService;
import br.com.abril.nds.service.EstudoAlgoritmoService;
import br.com.abril.nds.service.FornecedorService;
import br.com.abril.nds.service.MatrizDistribuicaoService;
import br.com.abril.nds.service.ParametrosDistribuidorService;
import br.com.abril.nds.service.SomarEstudosService;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.HTMLTableUtil;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Exportable;
import br.com.abril.nds.util.export.FileExporter;
import br.com.abril.nds.util.export.FileExporter.FileType;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.abril.nds.vo.ValidacaoVO;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

@Resource
@Path("/matrizDistribuicao")
public class MatrizDistribuicaoController extends BaseController {

	Logger log = LoggerFactory.getLogger(MatrizDistribuicaoController.class);

	@Autowired
	private Result result;

	@Autowired
	private FornecedorService fornecedorService;

	@Autowired
	private MatrizDistribuicaoService matrizDistribuicaoService;

	@Autowired
	private HttpSession session;

	@Autowired
	private HttpServletResponse httpResponse;

	@Autowired
	private CalendarioService calendarioService;

	@Autowired
	private EstudoAlgoritmoService estudoAlgoritmoService;

	@Autowired
	private SomarEstudosService somarEstudosService;

	@Autowired
	private ParametrosDistribuidorService parametrosDistribuidorService;

	private static final String FILTRO_SESSION_ATTRIBUTE = "filtroMatrizDistribuicao";
	private static final String LISTA_DE_DUPLICACOES = "LISTA_DE_DUPLICACOES";
	private static final int MAX_DUPLICACOES_PERMITIDAS = 3;

	@Path("/matrizDistribuicao")
	@Rules(Permissao.ROLE_DISTRIBUICAO_MATRIZ_DISTRIBUICAO)
	public void index() {

		session.setAttribute(FILTRO_SESSION_ATTRIBUTE, null);
		
		List<Fornecedor> fornecedores = fornecedorService.obterFornecedores(true, SituacaoCadastro.ATIVO);
		String data = DateUtil.formatarDataPTBR(new Date());
		result.include("data", data);
		result.include("fornecedores", fornecedores);
	}

	@SuppressWarnings("unchecked")
	private List<ProdutoDistribuicaoVO> obterListaDeItensDuplicadosNaSessao() {
		
		return (List<ProdutoDistribuicaoVO>)session.getAttribute(LISTA_DE_DUPLICACOES);
	}
	
	@Post
	public void obterMatrizDistribuicao(Date dataLancamento, List<Long> idsFornecedores) {

		ParametrosDistribuidorVO parametrosDistribuidorVO = parametrosDistribuidorService.getParametrosDistribuidor();
		validarDadosPesquisa(dataLancamento);
		configurarFiltropesquisa(dataLancamento, idsFornecedores);
		this.result.use(Results.json()).from(parametrosDistribuidorVO).recursive().serialize();
	}

	private void preparaItensParaVisualizacaoMatrizDistribuicao(List<ProdutoDistribuicaoVO> itens) {
		
		for (int i=0; i < itens.size(); i++) {
			itens.get(i).setIdRow(i);
		}
	}
	
	@Post
	public void obterGridMatrizDistribuicao(String sortorder, String sortname, int page, int rp) {
		
		FiltroDistribuicaoDTO filtro = obterFiltroSessao();
		filtro.setPaginacao(new PaginacaoVO(page, rp, sortorder, sortname));
		TotalizadorProdutoDistribuicaoVO vo = matrizDistribuicaoService.obterMatrizDistribuicao(filtro);
		
		preparaItensParaVisualizacaoMatrizDistribuicao(vo.getListProdutoDistribuicao());
		
		List<ProdutoDistribuicaoVO> listCopias = obterListaDeItensDuplicadosNaSessao();
		
		if (listCopias != null && !listCopias.isEmpty()) {
			
			List<ProdutoDistribuicaoVO> newList = new ArrayList<ProdutoDistribuicaoVO>();
			
			for (ProdutoDistribuicaoVO distribuicaoVO:vo.getListProdutoDistribuicao()) {
				
				newList.add(distribuicaoVO);
				
				for (ProdutoDistribuicaoVO distribuicaoVOCopia:listCopias) {
					
					if (distribuicaoVO.getIdLancamento().equals(distribuicaoVOCopia.getIdLancamento())) {
						
						newList.add(distribuicaoVOCopia);
					}
				}
			}
		
			vo.setListProdutoDistribuicao(newList);
 		}
		
		filtro.setTotalRegistrosEncontrados(vo.getListProdutoDistribuicao().size());
		session.setAttribute(FILTRO_SESSION_ATTRIBUTE, filtro);
		processarDistribuicao(vo, filtro);
	}

	@Post
	public void carregarProdutoEdicaoPorEstudo(BigInteger estudo) {

		ProdutoDistribuicaoVO produtoDistribuicaoVO = matrizDistribuicaoService.obterProdutoDistribuicaoPorEstudo(estudo);

		if (produtoDistribuicaoVO == null) {

			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, "Estudo: [" + estudo + "] não encontrado."));
		}

		result.use(Results.json()).from(produtoDistribuicaoVO, "result").recursive().serialize();
	}

	@Post
	public void confirmarCopiarProporcionalDeEstudo(CopiaProporcionalDeDistribuicaoVO copiaProporcionalDeDistribuicaoVO) {

		Long idEstudo = matrizDistribuicaoService.confirmarCopiarProporcionalDeEstudo(copiaProporcionalDeDistribuicaoVO);
		
		result.use(Results.json()).from(idEstudo, "result").recursive().serialize();
	}

	private void processarDistribuicao(TotalizadorProdutoDistribuicaoVO totProdDistVO, FiltroDistribuicaoDTO filtro) {

		PaginacaoVO paginacao = filtro.getPaginacao();
		List<ProdutoDistribuicaoVO> listProdutosDistrib = (totProdDistVO.isMatrizFinalizada()) ? 
				new ArrayList<ProdutoDistribuicaoVO>() : totProdDistVO.getListProdutoDistribuicao();
		listProdutosDistrib = PaginacaoUtil.paginarEmMemoria(listProdutosDistrib, paginacao);
		TableModel<CellModelKeyValue<ProdutoDistribuicaoVO>> tm = new TableModel<CellModelKeyValue<ProdutoDistribuicaoVO>>();
		List<CellModelKeyValue<ProdutoDistribuicaoVO>> cells = CellModelKeyValue.toCellModelKeyValue(listProdutosDistrib);

		List<Object> resultado = new ArrayList<Object>();
		tm.setRows(cells);
		tm.setPage(paginacao.getPaginaAtual());
		tm.setTotal(filtro.getTotalRegistrosEncontrados());
		resultado.add(tm);
		resultado.add(totProdDistVO.getTotalEstudosGerados());
		resultado.add(totProdDistVO.getTotalEstudosLiberados());
		resultado.add(totProdDistVO.isMatrizFinalizada());
		result.use(Results.json()).withoutRoot().from(resultado).recursive().serialize();
	}

	@Post
	public void finalizarMatrizDistribuicao(List<ProdutoDistribuicaoVO> produtosDistribuicao) {

		FiltroDistribuicaoDTO filtro = obterFiltroSessao();
		matrizDistribuicaoService.finalizarMatrizDistribuicao(filtro, produtosDistribuicao);

		result.use(Results.json()).from(Results.nothing()).serialize();
	}

	@Post
	public void finalizarMatrizDistribuicaoTodosItens(List<ProdutoDistribuicaoVO> produtosDistribuicao) {

		FiltroDistribuicaoDTO filtro = obterFiltroSessao();

		matrizDistribuicaoService.finalizarMatrizDistribuicaoTodosItens(filtro, produtosDistribuicao);

		result.use(Results.json()).from(Results.nothing()).serialize();
	}

	@Post
	public void reabrirMatrizDistribuicao(List<ProdutoDistribuicaoVO> produtosDistribuicao) {

		matrizDistribuicaoService.reabrirMatrizDistribuicao(produtosDistribuicao);

		result.use(Results.json()).from(Results.nothing()).serialize();
	}

	@Post
	public void reabrirMatrizDistribuicaoTodosItens() {

		FiltroDistribuicaoDTO filtro = obterFiltroSessao();

		matrizDistribuicaoService.reabrirMatrizDistribuicaoTodosItens(filtro);

		result.use(Results.json()).from(Results.nothing()).serialize();
	}

	@Exportable
	public class RodapeDTO {
		@Export(label = "Estudos gerados:")
		private String totalEstudosGerados;
		@Export(label = "Estudos liberados:")
		private String totalEstudosLiberado;

		public RodapeDTO(String totalGerado, String totalLiberado) {
			this.totalEstudosGerados = totalGerado;
			this.totalEstudosLiberado = totalLiberado;

		}

		public String getTotalEstudosGerados() {
			return totalEstudosGerados;
		}

		public String getTotalEstudosLiberado() {
			return totalEstudosLiberado;
		}

	}

	/**
	 * Exporta os dados da pesquisa.
	 * 
	 * @param fileType - tipo de arquivo
	 * 
	 * @throws IOException - Exceção de E/S
	 */
	@Get
	public void exportar(FileType fileType) throws IOException {

		FiltroDistribuicaoDTO filtro = obterFiltroSessao();

		TotalizadorProdutoDistribuicaoVO totalizadorProdutoDistribuicaoVO = matrizDistribuicaoService.obterMatrizDistribuicao(filtro);

		if (totalizadorProdutoDistribuicaoVO != null && 
				totalizadorProdutoDistribuicaoVO.getListProdutoDistribuicao() != null && 
				!totalizadorProdutoDistribuicaoVO.getListProdutoDistribuicao().isEmpty()) {

			RodapeDTO rodapeDTO = new RodapeDTO(CurrencyUtil.formatarValor(totalizadorProdutoDistribuicaoVO.getTotalEstudosGerados()),
			CurrencyUtil.formatarValor(totalizadorProdutoDistribuicaoVO.getTotalEstudosLiberados()));

			FileExporter.to("matriz_distribuicao", fileType).inHTTPResponse(
					this.getNDSFileHeader(),
					filtro,
					rodapeDTO,
					totalizadorProdutoDistribuicaoVO.getListProdutoDistribuicao(),
					ProdutoDistribuicaoVO.class, this.httpResponse);
		}

		result.nothing();
	}

	/**
	 * Configura o filtro informado na tela e o armazena na sessÃ£o.
	 * 
	 * @param dataPesquisa - data da pesquisa
	 * @param listaIdsFornecedores - lista de identificadores de fornecedores
	 */
	private FiltroDistribuicaoDTO configurarFiltropesquisa(Date dataPesquisa, List<Long> listaIdsFornecedores) {

		FiltroDistribuicaoDTO filtro = new FiltroDistribuicaoDTO(dataPesquisa, listaIdsFornecedores);

		if (listaIdsFornecedores != null && !listaIdsFornecedores.isEmpty()) {

			filtro.setNomesFornecedor(this.montarNomeFornecedores(listaIdsFornecedores));
		}

		this.session.setAttribute(FILTRO_SESSION_ATTRIBUTE, filtro);

		return filtro;
	}

	private String montarNomeFornecedores(List<Long> idsFornecedores) {

		String nomeFornecedores = "";

		List<Fornecedor> listaFornecedor = fornecedorService.obterFornecedoresPorId(idsFornecedores);

		if (listaFornecedor != null && !listaFornecedor.isEmpty()) {

			for (Fornecedor fornecedor : listaFornecedor) {

				if (!nomeFornecedores.isEmpty()) {

					nomeFornecedores += " / ";
				}

				nomeFornecedores += fornecedor.getJuridica().getRazaoSocial();
			}
		}

		return nomeFornecedores;
	}

	/**
	 * Valida os dados da pesquisa.
	 * 
	 * @param dataPesquisa - data da pesquisa
	 */
	private void validarDadosPesquisa(Date dataPesquisa) {

		List<String> listaMensagens = new ArrayList<String>();
		if (dataPesquisa == null) {
			
			listaMensagens.add("O preenchimento do campo [Data] é obrigatório!");
		}
		if (!listaMensagens.isEmpty()) {
			
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, listaMensagens));
		}
	}

	/**
	 * Obtem o filtro para pesquisa da sessão.
	 * 
	 * @return filtro
	 */
	private FiltroDistribuicaoDTO obterFiltroSessao() {

		FiltroDistribuicaoDTO filtro = (FiltroDistribuicaoDTO) this.session.getAttribute(FILTRO_SESSION_ATTRIBUTE);
		
		if (filtro == null) {
			
			throw new ValidacaoException(TipoMensagem.ERROR, "Sessão expirada!");
		}
		return filtro;
	}

	@Post
	public void duplicarLinha(ProdutoDistribuicaoVO produtoDistribuicao) {

		FiltroDistribuicaoDTO filtro = obterFiltroSessao();

		TotalizadorProdutoDistribuicaoVO totalizadorProdutoDistribuicaoVO = matrizDistribuicaoService.obterMatrizDistribuicao(filtro);
		
		List<ProdutoDistribuicaoVO> list = totalizadorProdutoDistribuicaoVO.getListProdutoDistribuicao();
		
		preparaItensParaVisualizacaoMatrizDistribuicao(list);
		
		Comparator<ProdutoDistribuicaoVO> comparator = new Comparator<ProdutoDistribuicaoVO>() {
			
			@Override
			public int compare(ProdutoDistribuicaoVO o1, ProdutoDistribuicaoVO o2) {
				
				return o1.getIdLancamento().compareTo(o2.getIdLancamento());
			}
		};
		
		Collections.sort(list, comparator);
		
		int index = Collections.binarySearch(list, produtoDistribuicao, comparator);
		
		ProdutoDistribuicaoVO distribuicaoVOCopia = (ProdutoDistribuicaoVO)SerializationUtils.clone(list.get(index));
		distribuicaoVOCopia.setIdEstudo(null);
		
		List<ProdutoDistribuicaoVO> listProdutoDistribuicaoVO = obterListaDeItensDuplicadosNaSessao();
		
		if (listProdutoDistribuicaoVO == null) {
			
			listProdutoDistribuicaoVO = new ArrayList<ProdutoDistribuicaoVO>();
		}
	
		int qtdDuplicacoes = 1;
		
		for(ProdutoDistribuicaoVO distribVO:listProdutoDistribuicaoVO) {
			
			if (distribVO.getIdLancamento().equals(distribuicaoVOCopia.getIdLancamento())) {
				
				qtdDuplicacoes++;
				
				if (qtdDuplicacoes > MAX_DUPLICACOES_PERMITIDAS) {
					
					throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING,"Não é permitido mais do que " + MAX_DUPLICACOES_PERMITIDAS + " duplicações"));
				}
			}
		}
		
		distribuicaoVOCopia.setIdCopia(qtdDuplicacoes);
		
		listProdutoDistribuicaoVO.add(distribuicaoVOCopia);
		
		session.setAttribute(LISTA_DE_DUPLICACOES, listProdutoDistribuicaoVO);
		
//		produtoDistribuicao.setIdUsuario(getUsuarioLogado().getId());
//		matrizDistribuicaoService.duplicarLinhas(produtoDistribuicao);
		this.result.use(Results.json()).from(Results.nothing()).serialize();
	}
	
	public void removeItemListaDeItensDuplicadosNaSessao(BigInteger idLancamento, Integer idCopia) {
		
		if (idCopia != null) {
			
			List<ProdutoDistribuicaoVO> distribuicaoVOs = obterListaDeItensDuplicadosNaSessao();
			
			if (distribuicaoVOs != null) {
				
				ProdutoDistribuicaoVO produtoDistribuicaoVO = null;
				int i = 0;
				
				for (ProdutoDistribuicaoVO distribuicaoVO:distribuicaoVOs) {
					
					if (distribuicaoVO.getIdLancamento().equals(idLancamento) && distribuicaoVO.getIdCopia().equals(idCopia)) {
						
						produtoDistribuicaoVO = distribuicaoVO;
						break;
					}
				}
				
				distribuicaoVOs.remove(produtoDistribuicaoVO);
			}
		}
	}

	@Post
	public void excluirEstudosSelecionados(List<ProdutoDistribuicaoVO> produtosDistribuicao) {

		if (produtosDistribuicao == null || produtosDistribuicao.isEmpty()) {
			
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, "Selecione um estudo para excluir!"));
		}
		
		matrizDistribuicaoService.excluirEstudos(produtosDistribuicao);
		
		for (ProdutoDistribuicaoVO distribuicaoVO:produtosDistribuicao) {
			
			removeItemListaDeItensDuplicadosNaSessao(distribuicaoVO.getIdLancamento(), distribuicaoVO.getIdCopia());
		}
		
		this.result.use(Results.json()).from(Results.nothing()).serialize();
	}

	@Post
	public void reabrirEstudosSelecionados(List<ProdutoDistribuicaoVO> produtosDistribuicao) {

		if (produtosDistribuicao == null || produtosDistribuicao.isEmpty()) {
			
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, "Selecione um ou mais estudos para ser(em) reaberto(s)!"));
		}
		
		matrizDistribuicaoService.reabrirEstudos(produtosDistribuicao);
		this.result.use(Results.json()).from(Results.nothing()).serialize();
	}

	@Post
	public void gerarEstudoAutomatico(String codigoProduto, Long numeroEdicao, BigDecimal reparte, BigInteger idLancamento, Integer idCopia) {
		
		EstudoTransient estudoAutomatico;
		try {
			ProdutoEdicaoEstudo produto = new ProdutoEdicaoEstudo(codigoProduto);
			produto.setNumeroEdicao(numeroEdicao);
			estudoAutomatico = estudoAlgoritmoService.gerarEstudoAutomatico(produto, reparte.toBigInteger(), this.getUsuarioLogado());
		} catch (Exception e) {
			log.error("Erro na geração automatica do estudo.", e);
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.ERROR, e.getMessage()));
		}
		
		removeItemListaDeItensDuplicadosNaSessao(idLancamento, idCopia);

		String htmlEstudo = HTMLTableUtil.estudoToHTML(estudoAutomatico);
		result.use(Results.json()).from(htmlEstudo, "estudo").recursive().serialize();
	}

	@Get
	public void histogramaPosEstudo() {
		
		result.forwardTo(HistogramaPosEstudoController.class).histogramaPosEstudo();
	}


    @Post
    public void somarEstudos(Long idEstudoBase, ProdutoDistribuicaoVO distribuicaoVO) {
		somarEstudosService.somarEstudos(idEstudoBase, distribuicaoVO);
		result.use(Results.json()).from(Results.nothing()).serialize();
    }

	public HttpSession getSession() {
		return session;
	}

	public void setSession(HttpSession session) {
		this.session = session;
	}

    
}
