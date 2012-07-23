package br.com.abril.nds.controllers.expedicao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

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
import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.model.cadastro.Rota;
import br.com.abril.nds.model.cadastro.TipoBox;
import br.com.abril.nds.serialization.custom.FlexiGridJson;
import br.com.abril.nds.service.BoxService;
import br.com.abril.nds.service.MapaAbastecimentoService;
import br.com.abril.nds.service.RoteirizacaoService;
import br.com.abril.nds.util.TipoMensagem;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

@Resource
@Path("/mapaAbastecimento")
public class MapaAbastecimentoController {

	private static final String FILTRO_SESSION_ATTRIBUTE = "filtroMapaAbastecimento";
	
	@Autowired
	private HttpSession session;
	
	@Autowired
	private Result result;
	
	@Autowired
	private MapaAbastecimentoService mapaAbastecimentoService;
	
	@Autowired
	private BoxService boxService;
	
	@Autowired
	private RoteirizacaoService roteirizacaoService;
	
	public void mapaAbastecimento() {
		
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
		
		result.forwardTo(MapaAbastecimentoController.class).mapaAbastecimento();
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
	
	
	@Post
	public void pesquisar(FiltroMapaAbastecimentoDTO filtro, Integer page, Integer rp, String sortname, String sortorder) {
				
		if(filtro.getTipoConsulta() == null)
			throw new ValidacaoException(TipoMensagem.WARNING, " 'Tipo de consulta' deve ser selecionado.");
				
		if(filtro.getDataDate() == null && !filtro.getDataLancamento().isEmpty())
			throw new ValidacaoException(TipoMensagem.WARNING, "'Data de Lançamento' não é válida.");
		
		if(filtro.getDataLancamento() == null || filtro.getDataLancamento().isEmpty())
			throw new ValidacaoException(TipoMensagem.WARNING, "'Data de Lançamento' é obrigatória.");
		
		filtro.setPaginacao(new PaginacaoVO(page, rp, sortorder,sortname));
		
		tratarFiltro(filtro);
		
		List<AbastecimentoDTO> lista = mapaAbastecimentoService.obterDadosAbastecimento(filtro);
		
		Long totalRegistros = mapaAbastecimentoService.countObterDadosAbastecimento(filtro);

		result.use(FlexiGridJson.class).from(lista).page(1).total(totalRegistros.intValue()).serialize();
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
				if(filtroAtual.getCodigoProduto()==null)
					throw new ValidacaoException(TipoMensagem.WARNING, "'Produto' não foi preenchido.");
				break;
			case ROTA:
				if(filtroAtual.getRota()==null)
					throw new ValidacaoException(TipoMensagem.WARNING, "'Rota' não foi preenchida.");
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
				
		Object[] combos = new Object[2];
		
		
		
		if(numeroCota != null) {
			List<Box> boxes = new ArrayList<Box>();
			boxes.add(boxService.obterBoxPorCota(numeroCota));
			combos[0] = carregarBoxes(boxes);
			combos[1] = carregarRota(roteirizacaoService.obterRotasPorCota(numeroCota));
		} else {
			combos[0] = carregarBoxes(boxService.buscarTodos(TipoBox.LANCAMENTO));
			combos[1] = carregarRota(roteirizacaoService.buscarRotas());
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
		
		HashMap<String, HashMap<String, ProdutoMapaRotaDTO>> produtosMapa = mapaAbastecimentoService.obterMapaDeImpressaoPorBoxRota(filtro);
		
		result.include("mapa",produtosMapa);
		
	}
	
	public void impressaoPorProduto(FiltroMapaAbastecimentoDTO filtro) {
		
		MapaCotaDTO mapaCota = mapaAbastecimentoService.obterMapaDeImpressaoPorCota(filtro);
		
		result.include("mapa", mapaCota);
		
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
}
