package br.com.abril.nds.controllers.expedicao;

import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.controllers.BaseController;
import br.com.abril.nds.dto.AbastecimentoDTO;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.MapaCotaDTO;
import br.com.abril.nds.dto.MapaProdutoCotasDTO;
import br.com.abril.nds.dto.ProdutoAbastecimentoDTO;
import br.com.abril.nds.dto.ProdutoEdicaoMapaDTO;
import br.com.abril.nds.dto.ProdutoMapaDTO;
import br.com.abril.nds.dto.ProdutoMapaRotaDTO;
import br.com.abril.nds.dto.filtro.FiltroMapaAbastecimentoDTO;
import br.com.abril.nds.dto.filtro.FiltroProdutoDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.model.cadastro.Entregador;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.Rota;
import br.com.abril.nds.model.cadastro.Roteiro;
import br.com.abril.nds.model.cadastro.TipoBox;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.repository.MovimentoEstoqueCotaRepository;
import br.com.abril.nds.serialization.custom.CustomMapJson;
import br.com.abril.nds.serialization.custom.FlexiGridJson;
import br.com.abril.nds.service.BoxService;
import br.com.abril.nds.service.CotaService;
import br.com.abril.nds.service.EntregadorService;
import br.com.abril.nds.service.MapaAbastecimentoService;
import br.com.abril.nds.service.ProdutoService;
import br.com.abril.nds.service.RotaService;
import br.com.abril.nds.service.RoteirizacaoService;
import br.com.abril.nds.service.integracao.DistribuidorService;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.abril.nds.vo.PaginacaoVO.Ordenacao;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

@Resource
@Path("/mapaAbastecimento")
@Rules(Permissao.ROLE_EXPEDICAO_MAPA_ABASTECIMENTO)
public class MapaAbastecimentoController extends BaseController {

	private static final String FILTRO_SESSION_ATTRIBUTE = "filtroMapaAbastecimento";

	private static final Integer QTD_MAX_COLUMN_IMPRESSAO_PRODUTO_X_COTA = 4;

	@Autowired
	private HttpSession session;

	@Autowired
	private Result result;

	@Autowired
	private MapaAbastecimentoService mapaAbastecimentoService;

	@Autowired
	private CotaService cotaService;

	@Autowired
	private MovimentoEstoqueCotaRepository movimentoEstoqueCotaRepository;

	@Autowired
	private BoxService boxService;

	@Autowired
	private RoteirizacaoService roteirizacaoService;

	@Autowired
	private RotaService rotaService;

	@Autowired
	private DistribuidorService distribuidorService;

	@Autowired
	private ProdutoService produtoService;

	@Autowired
	private EntregadorService entregadorService;

	public void mapaAbastecimento() {

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
		result.include("listaRotas",carregarRota(rotaService.obterRotas()));

		result.forwardTo(MapaAbastecimentoController.class).mapaAbastecimento();
	}

	/**
	 * Carrega a lista de Produtos.
	 */
	@Post
	public void getProdutos(Date dataLancamento) {

		List<Produto> listaProdutos = produtoService.obterProdutosBalanceadosOrdenadosNome(dataLancamento);

		if (listaProdutos.isEmpty()) {

			throw new ValidacaoException(
					TipoMensagem.WARNING, "Não existem produtos balanceados na data informada!");
		}

		List<ItemDTO<String, String>> produtos = new ArrayList<ItemDTO<String,String>>();

		for(Produto produto : listaProdutos) {

			produtos.add(new ItemDTO<String, String>(produto.getCodigo(), produto.getNome()));
		}

		this.result.use(Results.json()).from(produtos, "result").recursive().serialize();
	}

	@Post
	public void getProdutosPorCodigo(FiltroProdutoDTO filtro, Date dataLancamento) throws ValidacaoException{

		Produto produto = produtoService.obterProdutoBalanceadosPorCodigo(filtro.getCodigo(), dataLancamento);

		if (produto == null) {

			throw new ValidacaoException(
					TipoMensagem.WARNING, "Não existe produto balanceado com o código \"" + filtro.getCodigo() + "\" na data informada!");

		} else {

			result.use(Results.json()).from(produto, "result").serialize();
		}	
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

			listaRotas.add(new ItemDTO<Long, String>(rota.getId(), rota.getDescricaoRota()));
		}

		return listaRotas;
	}

	/**
	 * Retorna uma lista de Rota no formato ItemDTO
	 * @param rotas
	 * @return
	 * @return List<ItemDTO<Long, String>>
	 */
	private List<ItemDTO<Long, String>> carregarRoteiro(List<Roteiro> roteiros){

		List<ItemDTO<Long, String>> listaRoteiros = new ArrayList<ItemDTO<Long,String>>();

		for(Roteiro item : roteiros){

			listaRoteiros.add(new ItemDTO<Long, String>(item.getId(),item.getDescricaoRoteiro()));
		}

		return listaRoteiros;
	}

	@Post
	public void pesquisar(FiltroMapaAbastecimentoDTO filtro, Integer page, Integer rp, String sortname, String sortorder) {

		validarFiltroPesquisa(filtro);

		filtro.setPaginacao(new PaginacaoVO(page, rp, sortorder, sortname));

		tratarFiltro(filtro);

		switch(filtro.getTipoConsulta()) {

		case BOX:
			this.popularGridPorBox(filtro);
			break;
		case COTA:
			this.popularGridPorCota(filtro);
			break;
		case ROTA:
			this.popularGridPorRota(filtro);
			break;
		case PRODUTO:
			this.popularGridPorProduto(filtro);
			break;
		case PROMOCIONAL:
			this.popularGridPorRepartePromocional(filtro);
			break;
		case PRODUTO_ESPECIFICO:
			this.popularGridPorProdutoEspecifico(filtro);
			break;
		case PRODUTO_X_COTA:
			this.popularGridPorProdutoCota(filtro);
			break;
		case ENTREGADOR:
			this.popularGridPorEntregador(filtro);
		default:
			break;
		}
	}

	private void validarFiltroPesquisa(FiltroMapaAbastecimentoDTO filtro) {

		if(filtro == null) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Parâmetros de consulta inválidos.");
		}

		if(filtro.getTipoConsulta() == null)
			throw new ValidacaoException(TipoMensagem.WARNING, " 'Tipo de consulta' deve ser selecionado.");

		if(filtro.getDataDate() == null)
			throw new ValidacaoException(TipoMensagem.WARNING, "'Data de Lançamento' não é válida.");

		if(filtro.getDataLancamento() == null)
			throw new ValidacaoException(TipoMensagem.WARNING, "'Data de Lançamento' é obrigatória.");
	}

	@Post
	public void pesquisarDetalhes(Long idBox, Integer numeroCota, String data, String sortname, String sortorder) {

		FiltroMapaAbastecimentoDTO filtro = (FiltroMapaAbastecimentoDTO) session.getAttribute(FILTRO_SESSION_ATTRIBUTE);	
		filtro.setDataLancamento(data);
		filtro.setCodigoCota(numeroCota);

		filtro.setPaginacaoDetalhes(new PaginacaoVO(null, null, sortorder, sortname));

		List<ProdutoAbastecimentoDTO> lista = mapaAbastecimentoService.obterDetlhesDadosAbastecimento(idBox, filtro);

		result.use(FlexiGridJson.class).from(lista).page(1).total(lista.size()).serialize();
	}

	/**
	 * Executa tratamento de paginação em função de alteração do filtro de pesquisa.
	 *
	 * @param filtroResumoExpedicao
	 */
	private void tratarFiltro(FiltroMapaAbastecimentoDTO filtroAtual) {

		switch(filtroAtual.getTipoConsulta()) {	
		case BOX:
			break;
		case COTA:
			if(filtroAtual.getCodigoCota()==null)
				throw new ValidacaoException(TipoMensagem.WARNING, "'Cota' não foi preenchida.");
			break;
		case PRODUTO:
			if(filtroAtual.getCodigosProduto()==null)
				throw new ValidacaoException(TipoMensagem.WARNING, "'Produto' não foi preenchido.");
			break;
		case PROMOCIONAL:
			
			break;
		case PRODUTO_ESPECIFICO:

			List<String> mensagens = new ArrayList<String>();

			if (filtroAtual.getCodigosProduto()==null)
				mensagens.add("'Produto' não foi preenchido.");
			else if (filtroAtual.getCodigosProduto().size() > 1)
				mensagens.add("Deve ser escolhido apenas um 'Produto'.");
			if (filtroAtual.getEdicaoProduto()==null)
				mensagens.add("'Edição' não foi preenchida.");
			if (!mensagens.isEmpty())
				throw new ValidacaoException(TipoMensagem.WARNING, mensagens);
			break;
		case PRODUTO_X_COTA:
			if(filtroAtual.getCodigosProduto()==null)
				throw new ValidacaoException(TipoMensagem.WARNING, "'Produto' não foi preenchido.");
			break;
		case ROTA:

			break;
		case ENTREGADOR:
			if(filtroAtual.getIdEntregador()==null)
				throw new ValidacaoException(TipoMensagem.WARNING, "'Entregador' não foi preenchido.");
			break;
		default:
			throw new ValidacaoException(TipoMensagem.WARNING, "Tipo de consulta inexistente.");
		}

		FiltroMapaAbastecimentoDTO filtroSession = (FiltroMapaAbastecimentoDTO) session
				.getAttribute(FILTRO_SESSION_ATTRIBUTE);

		if (filtroSession != null && !filtroSession.equals(filtroAtual)) {

			filtroAtual.getPaginacao().setPaginaAtual(filtroAtual.getPaginacao().getPaginaAtual());
		}

		session.setAttribute(FILTRO_SESSION_ATTRIBUTE, filtroAtual);
	}

	@Post
	public void buscarBoxRotaPorCota(Integer numeroCota) {

		Object[] combos = new Object[3];



		if(numeroCota != null) {
			List<Box> boxes = new ArrayList<Box>();
			Box box = boxService.obterBoxPorCota(numeroCota);

			if(box != null)
				boxes.add(box);

			combos[0] = carregarBoxes(boxes);
			combos[1] = carregarRota(roteirizacaoService.obterRotasPorCota(numeroCota));
			combos[2] = carregarRoteiro(roteirizacaoService.obterRoteirosPorCota(numeroCota));
		} else {
			combos[0] = carregarBoxes(boxService.buscarTodos(TipoBox.LANCAMENTO));
			combos[1] = carregarRota(roteirizacaoService.buscarRotas());
			combos[2] = carregarRoteiro(roteirizacaoService.obterRoteirosPorCota(null));
		}


		this.result.use(Results.json()).from(combos, "result").recursive().serialize();
	}




	public void imprimirMapaAbastecimento() throws IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchMethodException {

		FiltroMapaAbastecimentoDTO filtro = (FiltroMapaAbastecimentoDTO) session.getAttribute(FILTRO_SESSION_ATTRIBUTE);

		validarFiltroPesquisa(filtro);

		if(filtro == null) {	
			result.forwardTo(MapaAbastecimentoController.class).impressaoFalha("Nenhuma pesquisa foi realizada.");
			return;
		}

		try {

			switch(filtro.getTipoConsulta()) {
			case BOX:
				filtro.setPaginacao(null);
				Map<String, ProdutoMapaDTO> produtosMapaBox = mapaAbastecimentoService.obterMapaDeImpressaoPorBox(filtro);

				setaNomeParaImpressao();
				result.forwardTo(MapaAbastecimentoController.class).impressaoPorBox(produtosMapaBox);
			break;
			case ROTA:
				
				result.forwardTo(MapaAbastecimentoController.class).impressaoPorRota(filtro);
				
			break;
			case COTA:
				filtro.getPaginacao().setQtdResultadosPorPagina(null);
				filtro.getPaginacao().setPaginaAtual(null);
				setaNomeParaImpressao();
				result.forwardTo(MapaAbastecimentoController.class).impressaoPorCota(filtro);
			break;
			case PRODUTO:

				if(filtro.getEdicaoProduto()!=null) {	

					filtro.getPaginacao().setQtdResultadosPorPagina(null);
					filtro.getPaginacao().setPaginaAtual(null);

					ProdutoEdicaoMapaDTO produtoEdicaoMapaEdicaoProduto = mapaAbastecimentoService.obterMapaDeImpressaoPorProdutoEdicao(filtro);

					result.forwardTo(MapaAbastecimentoController.class).impressaoPorProdutoEdicao(produtoEdicaoMapaEdicaoProduto);

				} else {
					
					result.forwardTo(MapaAbastecimentoController.class).impressaoPorProduto(filtro);
				}
			break;	
			case PROMOCIONAL:
				result.include("isPromocional", true);
				result.forwardTo(MapaAbastecimentoController.class).impressaoPorProduto(filtro);
								
			break;
			case PRODUTO_ESPECIFICO:
				filtro.setPaginacao(null);
				
				ProdutoEdicaoMapaDTO produtoEdicaoMapaEspecifico = mapaAbastecimentoService.obterMapaDeImpressaoPorProdutoEdicao(filtro);

				result.forwardTo(MapaAbastecimentoController.class).impressaoPorProdutoEdicao(produtoEdicaoMapaEspecifico);

			break;
			case PRODUTO_X_COTA:
				
				result.forwardTo(MapaAbastecimentoController.class).impressaoPorProdutoQuebraCota(filtro);
				
			break;	
			case ENTREGADOR:
				
				filtro.getPaginacao().setQtdResultadosPorPagina(null);
				filtro.getPaginacao().setPaginaAtual(null);
				
				result.forwardTo(MapaAbastecimentoController.class).impressaoPorEntregador(filtro);
				break;
			default:
				throw new ValidacaoException(TipoMensagem.WARNING, "Tipo de consulta inexistente.");
			}

		} catch(ValidacaoException e) {
			impressaoFalha(e.getMessage());
		}

	}

	public void impressaoPorBox(Map<String, ProdutoMapaDTO> produtosMapa) {

		result.include("produtosMapa",produtosMapa.values());

	}

	public void impressaoPorRota(FiltroMapaAbastecimentoDTO filtro) {

		filtro.setPaginacao(null);

		Map<Integer, Map<String, ProdutoMapaRotaDTO>> produtosMapa = mapaAbastecimentoService.obterMapaDeImpressaoPorBoxRota(filtro);
		setaNomeParaImpressao();
		result.include("mapa", produtosMapa);

	}

	public void impressaoPorProduto(FiltroMapaAbastecimentoDTO filtro) {

		filtro.setPaginacao(null);

		MapaCotaDTO mapaCota = mapaAbastecimentoService.obterMapaDeImpressaoPorCota(filtro);

		setaNomeParaImpressao();
		result.include("mapa", mapaCota);

	}

	public void impressaoPorEntregador(FiltroMapaAbastecimentoDTO filtro) {

		HashMap<Long, MapaProdutoCotasDTO> mapa = mapaAbastecimentoService.obterMapaDeImpressaoPorEntregador(filtro);

		Entregador entregador = entregadorService.buscarPorId(filtro.getIdEntregador());

		result.include("distribuidor", distribuidorService.obterRazaoSocialDistribuidor());

		result.include("entregador", entregador);

		String data = new SimpleDateFormat("dd/MM/yyyy").format(new Date());

		result.include("data",data);

		result.include("mapa", mapa);

	}

	public void impressaoPorCota(FiltroMapaAbastecimentoDTO filtro) {

		filtro.getPaginacao().setQtdResultadosPorPagina(null);
		filtro.getPaginacao().setPaginaAtual(null);

		filtro.getPaginacao().setSortColumn("nomeEdicao");
		filtro.getPaginacao().setOrdenacao(Ordenacao.ASC);

		MapaCotaDTO mapaCota = mapaAbastecimentoService.obterMapaDeImpressaoPorCota(filtro);
		setaNomeParaImpressao();
		result.include("mapa", mapaCota);

	}

	public void impressaoPorProdutoEdicao(ProdutoEdicaoMapaDTO produtoEdicaoMapa) {	
		setaNomeParaImpressao();
		result.include("mapa",produtoEdicaoMapa);

	}

	public void impressaoPorProdutoQuebraCota(FiltroMapaAbastecimentoDTO filtro) throws IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchMethodException {	

		filtro.getPaginacao().setQtdResultadosPorPagina(null);
		filtro.getPaginacao().setPaginaAtual(null);

		MapaProdutoCotasDTO produtoCotaMapa = mapaAbastecimentoService.obterMapaDeImpressaoPorProdutoQuebrandoPorCota(filtro);
		setaNomeParaImpressao();

		Integer qtdMaxRow = 30;

		List<MapaProdutoCotasDTO> maps = getMapaProdutoCotasDTO(produtoCotaMapa);

		result.include("maps", maps);
		result.include("mapBoxQtdes", produtoCotaMapa.getBoxQtdes());
		result.include("qtdMaxRow", qtdMaxRow);
	}

	private List<MapaProdutoCotasDTO> getMapaProdutoCotasDTO(MapaProdutoCotasDTO produtoCotaMapa)
			throws IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchMethodException {

		Integer maxPerPage = QTD_MAX_COLUMN_IMPRESSAO_PRODUTO_X_COTA * 30;

		int pageBreak = 0;

		Map<Integer, Integer> newMap = null;

		MapaProdutoCotasDTO mapaProdutoCotaDTO = null;

		List<MapaProdutoCotasDTO> maps = new ArrayList<MapaProdutoCotasDTO>();

		for (Entry<Integer, Integer> entry : produtoCotaMapa.getCotasQtdes().entrySet()) {

			if (pageBreak == 0 || pageBreak == maxPerPage) {

				pageBreak = 0;

				if(newMap != null) {

					mapaProdutoCotaDTO = (MapaProdutoCotasDTO) BeanUtils.cloneBean(produtoCotaMapa);

					mapaProdutoCotaDTO.setCotasQtdes(newMap);

					maps.add(mapaProdutoCotaDTO);
				}

				newMap = new TreeMap<Integer, Integer>();

				pageBreak++;
			}

			newMap.put(entry.getKey(), entry.getValue());

			pageBreak++;
		}

		mapaProdutoCotaDTO = (MapaProdutoCotasDTO) BeanUtils.cloneBean(produtoCotaMapa);

		mapaProdutoCotaDTO.setCotasQtdes(newMap);

		maps.add(mapaProdutoCotaDTO);

		return maps;
	}

	private void setaNomeParaImpressao() {
		result.include("nomeDistribuidor", distribuidorService.obterRazaoSocialDistribuidor());
	}

	public void impressaoFalha(String mensagemErro){
		setaNomeParaImpressao();
		result.include(mensagemErro);	
	}

	private void mostrarMensagemListaVazia() {
		throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum registro encontrado na pesquisa.");
	}

	private void popularGridPorBox(FiltroMapaAbastecimentoDTO filtro) {

		List<AbastecimentoDTO> lista = this.mapaAbastecimentoService.obterDadosAbastecimento(filtro);

		if (lista == null || lista.isEmpty()) {
			mostrarMensagemListaVazia();
		}

		Long totalRegistros = this.mapaAbastecimentoService.countObterDadosAbastecimento(filtro);

		result.use(FlexiGridJson.class).from(lista).page(filtro.getPaginacao().getPaginaAtual()).total(totalRegistros.intValue()).serialize();
	}

	private void popularGridPorCota(FiltroMapaAbastecimentoDTO filtro) {

		List<ProdutoAbastecimentoDTO> lista = this.mapaAbastecimentoService.obterMapaAbastecimentoPorCota(filtro);

		if (lista == null || lista.isEmpty()) {
			mostrarMensagemListaVazia();
		}

		Long totalRegistros = mapaAbastecimentoService.countObterMapaAbastecimentoPorCota(filtro);

		result.use(FlexiGridJson.class).from(lista).page(filtro.getPaginacao().getPaginaAtual()).total(totalRegistros.intValue()).serialize();
	}

	private void popularGridPorRota(FiltroMapaAbastecimentoDTO filtro) {

		List<ProdutoAbastecimentoDTO> lista = this.mapaAbastecimentoService.obterMapaAbastecimentoPorBoxRota(filtro);

		if (lista == null || lista.isEmpty()) {
			mostrarMensagemListaVazia();
		}

		Long totalRegistros = mapaAbastecimentoService.countObterMapaAbastecimentoPorBoxRota(filtro);

		result.use(FlexiGridJson.class).from(lista).page(filtro.getPaginacao().getPaginaAtual()).total(totalRegistros.intValue()).serialize();
	}

	private void popularGridPorProduto(FiltroMapaAbastecimentoDTO filtro) {

		List<ProdutoAbastecimentoDTO> lista = this.mapaAbastecimentoService.obterMapaAbastecimentoPorProdutoEdicao(filtro);

		if (lista == null || lista.isEmpty()) {
			mostrarMensagemListaVazia();
		}

		Long totalRegistros = mapaAbastecimentoService.countObterMapaAbastecimentoPorProdutoEdicao(filtro);

		result.use(FlexiGridJson.class).from(lista).page(filtro.getPaginacao().getPaginaAtual()).total(totalRegistros.intValue()).serialize();
	}
	
	private void popularGridPorRepartePromocional(FiltroMapaAbastecimentoDTO filtro) {
		
		List<ProdutoAbastecimentoDTO> lista = this.mapaAbastecimentoService.obterMapaAbastecimentoPorCota(filtro);

		if (lista == null || lista.isEmpty()) {
			mostrarMensagemListaVazia();
		}

		Long totalRegistros = mapaAbastecimentoService.countObterMapaAbastecimentoPorCota(filtro);

		result.use(FlexiGridJson.class).from(lista).page(filtro.getPaginacao().getPaginaAtual()).total(totalRegistros.intValue()).serialize();
	}

	private void popularGridPorProdutoEspecifico(FiltroMapaAbastecimentoDTO filtro) {

		List<ProdutoAbastecimentoDTO> lista = this.mapaAbastecimentoService.obterMapaAbastecimentoPorProdutoEdicao(filtro);

		if (lista == null || lista.isEmpty()) {
			mostrarMensagemListaVazia();
		}

		Long totalRegistros = mapaAbastecimentoService.countObterMapaAbastecimentoPorProdutoEdicao(filtro);

		result.use(FlexiGridJson.class).from(lista).page(filtro.getPaginacao().getPaginaAtual()).total(totalRegistros.intValue()).serialize();
	}

	private void popularGridPorProdutoCota(FiltroMapaAbastecimentoDTO filtro) {

		List<ProdutoAbastecimentoDTO> lista = this.mapaAbastecimentoService.obterMapaDeAbastecimentoPorProdutoQuebrandoPorCota(filtro);

		if (lista == null || lista.isEmpty()) {
			mostrarMensagemListaVazia();
		}

		Long totalRegistros = mapaAbastecimentoService.countObterMapaDeAbastecimentoPorProdutoQuebrandoPorCota(filtro);

		result.use(FlexiGridJson.class).from(lista).page(filtro.getPaginacao().getPaginaAtual()).total(totalRegistros.intValue()).serialize();
	}

	private void popularGridPorEntregador(FiltroMapaAbastecimentoDTO filtro) {

		List<ProdutoAbastecimentoDTO> lista = this.mapaAbastecimentoService.obterMapaDeAbastecimentoPorEntregador(filtro);

		if (lista == null || lista.isEmpty()) {
			mostrarMensagemListaVazia();
		}

		Long totalRegistros = mapaAbastecimentoService.countObterMapaDeAbastecimentoPorEntregador(filtro);

		result.use(FlexiGridJson.class).from(lista).page(filtro.getPaginacao().getPaginaAtual()).total(totalRegistros.intValue()).serialize();

	}

	@Post
	public void buscarRotaPorRoteiro(Long idRoteiro) {
		List<Rota> rotas = roteirizacaoService.buscarRotasPorRoteiro(idRoteiro);	
		result.use(CustomMapJson.class).put("rotas", carregarRota(rotas)).serialize();
	}

	@Post
	public void buscarRoteiroPorBox(Long idBox) {
		List<Roteiro> roteiros = roteirizacaoService.buscarRoteiroDeBox(idBox);
		List<Rota> rotas;
		if (idBox != null) {
			rotas = roteirizacaoService.buscarRotaDeBox(idBox);
		}else{
			rotas = roteirizacaoService.buscarRotas();
		}
		result.use(CustomMapJson.class).put("roteiros", carregarRoteiro(roteiros)).put("rotas", carregarRota(rotas)).serialize();

	}

}
