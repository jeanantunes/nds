package br.com.abril.nds.controllers.expedicao;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.dto.AbastecimentoDTO;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.MapaCotaDTO;
import br.com.abril.nds.dto.MapaProdutoCotasDTO;
import br.com.abril.nds.dto.ProdutoAbastecimentoDTO;
import br.com.abril.nds.dto.ProdutoEdicaoMapaDTO;
import br.com.abril.nds.dto.ProdutoMapaDTO;
import br.com.abril.nds.dto.ProdutoMapaRotaDTO;
import br.com.abril.nds.dto.filtro.FiltroMapaAbastecimentoDTO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.integracao.service.DistribuidorService;
import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.model.cadastro.Entregador;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.Rota;
import br.com.abril.nds.model.cadastro.Roteiro;
import br.com.abril.nds.model.cadastro.TipoBox;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.serialization.custom.FlexiGridJson;
import br.com.abril.nds.service.BoxService;
import br.com.abril.nds.service.EntregadorService;
import br.com.abril.nds.service.LancamentoService;
import br.com.abril.nds.service.MapaAbastecimentoService;
import br.com.abril.nds.service.ProdutoService;
import br.com.abril.nds.service.RoteirizacaoService;
import br.com.abril.nds.util.TipoMensagem;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.abril.nds.vo.ValidacaoVO;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

@Resource
@Path("/mapaAbastecimento")
public class MapaAbastecimentoController {

	private static final String FILTRO_SESSION_ATTRIBUTE = "filtroMapaAbastecimento";
	
	protected static final String MSG_MATRIZ_BALANCEAMENTO_NAO_CONFIRMADO = "Não há matriz de lancamento confirmada para esta data.";
	
	@Autowired
	private HttpSession session;
	
	@Autowired
	private Result result;
	
	@Autowired
	private MapaAbastecimentoService mapaAbastecimentoService;
	
	@Autowired
	private LancamentoService lancamentoService;
	
	@Autowired
	private BoxService boxService;
	
	@Autowired
	private RoteirizacaoService roteirizacaoService;
	
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
	@Rules(Permissao.ROLE_EXPEDICAO_MAPA_ABASTECIMENTO)
	public void index() {
		
		session.setAttribute(FILTRO_SESSION_ATTRIBUTE, null);
		
		String data = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
		
		result.include("data",data);
		
		result.include("listaBoxes",carregarBoxes(boxService.buscarTodos(TipoBox.LANCAMENTO)));
		result.include("listaRotas",carregarRota(roteirizacaoService.buscarRotas()));
		
		result.include("listaProdutos", this.carregarProdutos());
		
		result.forwardTo(MapaAbastecimentoController.class).mapaAbastecimento();
	}
	
	/**
	 * Carrega a lista de Produtos.
	 */
	private List<ItemDTO<String, String>> carregarProdutos() {
		
		List<Produto> listaProdutos = produtoService.obterProdutos();
		
		List<ItemDTO<String, String>> listaProdutosCombo = new ArrayList<ItemDTO<String,String>>();
				
		for(Produto produto : listaProdutos) {
			
			listaProdutosCombo.add(new ItemDTO<String, String>(produto.getCodigo(), produto.getNome()));
		}
		
		return listaProdutosCombo;			
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
			
			listaRotas.add(new ItemDTO<Long, String>(rota.getId(),rota.getCodigoRota() + " " + rota.getDescricaoRota()));
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
		
		for(Roteiro rota : roteiros){
			
			listaRoteiros.add(new ItemDTO<Long, String>(rota.getId(),rota.getId() + " " + rota.getDescricaoRoteiro()));
		}
		
		return listaRoteiros;
	}
		
	/**
	 * Válida
	 * 
	 * @param dataLancamento
	 */
	private void validarExistenciaMatriz(Date dataLancamento) {
		if(!lancamentoService.existeMatrizBalanceamentoConfirmado(dataLancamento)){
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING,MSG_MATRIZ_BALANCEAMENTO_NAO_CONFIRMADO ));
		}
	}	
	
	@Post
	public void pesquisar(FiltroMapaAbastecimentoDTO filtro, Integer page, Integer rp, String sortname, String sortorder) {
		
		if(filtro.getTipoConsulta() == null)
			throw new ValidacaoException(TipoMensagem.WARNING, " 'Tipo de consulta' deve ser selecionado.");
				
		if(filtro.getDataDate() == null && !filtro.getDataLancamento().isEmpty())
			throw new ValidacaoException(TipoMensagem.WARNING, "'Data de Lançamento' não é válida.");
		
		if(filtro.getDataLancamento() == null || filtro.getDataLancamento().isEmpty())
			throw new ValidacaoException(TipoMensagem.WARNING, "'Data de Lançamento' é obrigatória.");
		
		validarExistenciaMatriz(filtro.getDataDate());
		
		filtro.setPaginacao(new PaginacaoVO(page, rp, sortorder,sortname));
		
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

	@Post
	public void pesquisarDetalhes(Long idBox, String data, String sortname, String sortorder) {
				
		FiltroMapaAbastecimentoDTO filtro = (FiltroMapaAbastecimentoDTO) session.getAttribute(FILTRO_SESSION_ATTRIBUTE);		
		
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
				if(filtroAtual.getBox()==null)
					throw new ValidacaoException(TipoMensagem.WARNING, "'Box' não foi preenchido.");	
				break;
			case COTA:
				if(filtroAtual.getCodigoCota()==null)
					throw new ValidacaoException(TipoMensagem.WARNING, "'Cota' não foi preenchida.");
				break;
			case PRODUTO:
				if(filtroAtual.getCodigosProduto()==null)
					throw new ValidacaoException(TipoMensagem.WARNING, "'Produto' não foi preenchido.");
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
				if(filtroAtual.getRota()==null)
					throw new ValidacaoException(TipoMensagem.WARNING, "'Rota' não foi preenchida.");
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

			filtroAtual.getPaginacao().setPaginaAtual(1);
		}
		
		session.setAttribute(FILTRO_SESSION_ATTRIBUTE, filtroAtual);
	}
	
	@Post
	public void buscarBoxRotaPorCota(Integer numeroCota) {
				
		Object[] combos = new Object[3];
		
		
		
		if(numeroCota != null) {
			List<Box> boxes = new ArrayList<Box>();
			boxes.add(boxService.obterBoxPorCota(numeroCota));
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
		
	public void imprimirMapaAbastecimento() {

		FiltroMapaAbastecimentoDTO filtro = (FiltroMapaAbastecimentoDTO) session.getAttribute(FILTRO_SESSION_ATTRIBUTE);
		
		if(filtro == null) {	
			result.forwardTo(MapaAbastecimentoController.class).impressaoFalha("Nenhuma pesquisa foi realizada.");
			return;
		}
		
		try {
		
			switch(filtro.getTipoConsulta()) {
				case BOX:
					result.forwardTo(MapaAbastecimentoController.class).impressaoPorBox(filtro);	
					break;
				case ROTA:
					result.forwardTo(MapaAbastecimentoController.class).impressaoPorRota(filtro);
					break;
				case COTA:
					result.forwardTo(MapaAbastecimentoController.class).impressaoPorCota(filtro);
					break;
				case PRODUTO:
					if(filtro.getQuebraPorCota()) 
						result.forwardTo(MapaAbastecimentoController.class).impressaoPorProdutoQuebraCota(filtro);
					else if(filtro.getEdicaoProduto()!=null)	
						result.forwardTo(MapaAbastecimentoController.class).impressaoPorProdutoEdicao(filtro);
					else 
						result.forwardTo(MapaAbastecimentoController.class).impressaoPorProduto(filtro);
					break;
				
				case ENTREGADOR:				
						result.forwardTo(MapaAbastecimentoController.class).impressaoPorEntregador(filtro);
					break;
					
				default:
					throw new ValidacaoException(TipoMensagem.WARNING, "Tipo de consulta inexistente.");
			}
		
		}catch(ValidacaoException e) {
			impressaoFalha(e.getMessage());
		}
				
	}
	
	public void impressaoPorBox(FiltroMapaAbastecimentoDTO filtro) {
		
		HashMap<String, ProdutoMapaDTO> produtosMapa = mapaAbastecimentoService.obterMapaDeImpressaoPorBox(filtro);
		
		result.include("produtosMapa",produtosMapa.values());
	}
	
	public void impressaoPorRota(FiltroMapaAbastecimentoDTO filtro) {
		
		HashMap<Integer, HashMap<String, ProdutoMapaRotaDTO>> produtosMapa = mapaAbastecimentoService.obterMapaDeImpressaoPorBoxRota(filtro);
		
		result.include("mapa",produtosMapa);
		
	}
	
	public void impressaoPorProduto(FiltroMapaAbastecimentoDTO filtro) {
		
		MapaCotaDTO mapaCota = mapaAbastecimentoService.obterMapaDeImpressaoPorCota(filtro);
		
		result.include("mapa", mapaCota);
		
	}
	
	public void impressaoPorEntregador(FiltroMapaAbastecimentoDTO filtro) {
		
		HashMap<Long, MapaProdutoCotasDTO> mapa = mapaAbastecimentoService.obterMapaDeImpressaoPorEntregador(filtro);
		
		Entregador entregador = entregadorService.buscarPorId(filtro.getIdEntregador());
				
		result.include("distribuidor", distribuidorService.obter().getJuridica().getRazaoSocial());
		
		result.include("entregador", entregador);
		
		String data = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
		
		result.include("data",data);
		
		result.include("mapa", mapa);
		
	}

	public void impressaoPorCota(FiltroMapaAbastecimentoDTO filtro) {
				
		MapaCotaDTO mapaCota = mapaAbastecimentoService.obterMapaDeImpressaoPorCota(filtro);
		
		result.include("mapa", mapaCota);
		
	}
	
	public void impressaoPorProdutoEdicao(FiltroMapaAbastecimentoDTO filtro) {		

		ProdutoEdicaoMapaDTO produtoEdicaoMapa = mapaAbastecimentoService.obterMapaDeImpressaoPorProdutoEdicao(filtro);
		
		result.include("mapa",produtoEdicaoMapa);
		
	}
	
	public void impressaoPorProdutoQuebraCota(FiltroMapaAbastecimentoDTO filtro) {		

		MapaProdutoCotasDTO produtoCotaMapa = mapaAbastecimentoService.obterMapaDeImpressaoPorProdutoQuebrandoPorCota(filtro);
		
		result.include("mapa",produtoCotaMapa);
		
	}
	
	public void impressaoFalha(String mensagemErro){
		result.include(mensagemErro);					
	}
	
	private void popularGridPorBox(FiltroMapaAbastecimentoDTO filtro) {
		
		List<AbastecimentoDTO> lista = this.mapaAbastecimentoService.obterDadosAbastecimento(filtro);
		
		Long totalRegistros = this.mapaAbastecimentoService.countObterDadosAbastecimento(filtro);

		result.use(FlexiGridJson.class).from(lista).page(filtro.getPaginacao().getPaginaAtual()).total(totalRegistros.intValue()).serialize();
	}

	private void popularGridPorCota(FiltroMapaAbastecimentoDTO filtro) {

		List<ProdutoAbastecimentoDTO> lista = this.mapaAbastecimentoService.obterMapaAbastecimentoPorCota(filtro);
		
		Long totalRegistros = mapaAbastecimentoService.countObterMapaAbastecimentoPorCota(filtro);

		result.use(FlexiGridJson.class).from(lista).page(filtro.getPaginacao().getPaginaAtual()).total(totalRegistros.intValue()).serialize();
	}
	
	private void popularGridPorRota(FiltroMapaAbastecimentoDTO filtro) {

		List<ProdutoAbastecimentoDTO> lista = this.mapaAbastecimentoService.obterMapaAbastecimentoPorBoxRota(filtro);
		
		Long totalRegistros = mapaAbastecimentoService.countObterMapaAbastecimentoPorBoxRota(filtro);

		result.use(FlexiGridJson.class).from(lista).page(filtro.getPaginacao().getPaginaAtual()).total(totalRegistros.intValue()).serialize();
	}

	private void popularGridPorProduto(FiltroMapaAbastecimentoDTO filtro) {

		List<ProdutoAbastecimentoDTO> lista = this.mapaAbastecimentoService.obterMapaAbastecimentoPorCota(filtro);
		
		Long totalRegistros = mapaAbastecimentoService.countObterMapaAbastecimentoPorCota(filtro);

		result.use(FlexiGridJson.class).from(lista).page(filtro.getPaginacao().getPaginaAtual()).total(totalRegistros.intValue()).serialize();
	}
	
	private void popularGridPorProdutoEspecifico(FiltroMapaAbastecimentoDTO filtro) {

		List<ProdutoAbastecimentoDTO> lista = this.mapaAbastecimentoService.obterMapaAbastecimentoPorProdutoEdicao(filtro);
		
		Long totalRegistros = mapaAbastecimentoService.countObterMapaAbastecimentoPorProdutoEdicao(filtro);

		result.use(FlexiGridJson.class).from(lista).page(filtro.getPaginacao().getPaginaAtual()).total(totalRegistros.intValue()).serialize();
	}
	
	private void popularGridPorProdutoCota(FiltroMapaAbastecimentoDTO filtro) {

		List<ProdutoAbastecimentoDTO> lista = this.mapaAbastecimentoService.obterMapaDeAbastecimentoPorProdutoQuebrandoPorCota(filtro);
		
		Long totalRegistros = mapaAbastecimentoService.countObterMapaDeAbastecimentoPorProdutoQuebrandoPorCota(filtro);

		result.use(FlexiGridJson.class).from(lista).page(filtro.getPaginacao().getPaginaAtual()).total(totalRegistros.intValue()).serialize();
	}
	
	private void popularGridPorEntregador(FiltroMapaAbastecimentoDTO filtro) {
		
		List<ProdutoAbastecimentoDTO> lista = this.mapaAbastecimentoService.obterMapaDeAbastecimentoPorEntregador(filtro);
		
		Long totalRegistros = mapaAbastecimentoService.countObterMapaDeAbastecimentoPorEntregador(filtro);

		result.use(FlexiGridJson.class).from(lista).page(filtro.getPaginacao().getPaginaAtual()).total(totalRegistros.intValue()).serialize();
		
	}

}
