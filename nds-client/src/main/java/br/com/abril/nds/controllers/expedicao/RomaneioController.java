package br.com.abril.nds.controllers.expedicao;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.RomaneioDTO;
import br.com.abril.nds.dto.filtro.FiltroRomaneioDTO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Box;
import br.com.abril.nds.model.cadastro.Rota;
import br.com.abril.nds.model.cadastro.Roteiro;
import br.com.abril.nds.model.cadastro.TipoBox;
import br.com.abril.nds.service.BoxService;
import br.com.abril.nds.service.RomaneioService;
import br.com.abril.nds.service.RoteirizacaoService;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.TipoMensagem;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.abril.nds.vo.PaginacaoVO.Ordenacao;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

@Resource
@Path("/romaneio")
public class RomaneioController {
	
	private static final String FILTRO_SESSION_ATTRIBUTE = "filtroRomaneio";
	
	@Autowired
	private BoxService boxService;
	
	@Autowired
	private RoteirizacaoService roteirizacaoService;
	
	@Autowired
	private RomaneioService romaneioService;
	
	@Autowired
	private Result result;
	
	@Autowired
	private HttpSession session;
	
	
	@Path("/")
	public void index() {
		carregarComboBox();
		carregarComboRoteiro();
		carregarComboRota(); 
	}
	@Post
	@Path("/pesquisarRomaneio")
	public void pesquisarRomaneio(FiltroRomaneioDTO filtro, String sortorder, String sortname, int page, int rp){
		
		filtro.setPaginacao(new PaginacaoVO(page, rp, sortorder,sortname));
		
		this.validarEntrada(filtro);
		
		this.tratarFiltro(filtro);
		
		TableModel<CellModelKeyValue<RomaneioDTO>> tableModel = efetuarConsultaRomaneio(filtro);
		
		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
		
	}
	
	private TableModel<CellModelKeyValue<RomaneioDTO>> efetuarConsultaRomaneio(
			FiltroRomaneioDTO filtro) {
		
		List<RomaneioDTO> listaRomaneios = this.romaneioService.buscarRomaneio(filtro);
		
		TableModel<CellModelKeyValue<RomaneioDTO>> tableModel = new TableModel<CellModelKeyValue<RomaneioDTO>>();

		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listaRomaneios));
		
		tableModel.setPage(filtro.getPaginacao().getPaginaAtual());
		
		tableModel.setTotal(listaRomaneios.size());
		
		return tableModel;
	}

	private void validarEntrada(FiltroRomaneioDTO filtro) {
		
		if(filtro.getIdBox()==null)
			throw new ValidacaoException(TipoMensagem.WARNING, "Box é obrigatório.");
		if(filtro.getIdRoteiro()==null )
			throw new ValidacaoException(TipoMensagem.WARNING, "Roteiro é obrigatório.");
		if(filtro.getIdRota()==null )
			throw new ValidacaoException(TipoMensagem.WARNING, "Rota é obrigatório.");
		
	}
	
	private void tratarFiltro(FiltroRomaneioDTO filtroAtual) {

		FiltroRomaneioDTO filtroSession = (FiltroRomaneioDTO) session
				.getAttribute(FILTRO_SESSION_ATTRIBUTE);
		
		if (filtroSession != null && !filtroSession.equals(filtroAtual)) {

			filtroAtual.getPaginacao().setPaginaAtual(1);
		}
		
		session.setAttribute(FILTRO_SESSION_ATTRIBUTE, filtroAtual);
	}

	private void carregarComboBox() {
		List<Box> boxs = boxService.buscarPorTipo(TipoBox.LANCAMENTO);
		
			List<ItemDTO<Long, String>> lista =
				new ArrayList<ItemDTO<Long,String>>();
		
		for (Box box : boxs) {
			
			lista.add(
				new ItemDTO<Long, String>(box.getId(), box.getNome()));
		}
		
		result.include("listaBox", lista);
	}
	
	private void carregarComboRoteiro() {
		List<Roteiro> roteiros = roteirizacaoService.buscarRoteiro("descricaoRoteiro", Ordenacao.ASC);
		
			List<ItemDTO<Long, String>> lista =
				new ArrayList<ItemDTO<Long,String>>();
		
		for (Roteiro roteiro : roteiros) {
			try{
				roteiro.getRotas();
			} catch (Exception e) {
				e.getMessage();
			}
			
			lista.add(
				new ItemDTO<Long, String>(roteiro.getId(), roteiro.getDescricaoRoteiro()));
		}
		
		result.include("listaRoteiro", lista);
	}
	
	private void carregarComboRota() {
		List<Rota> rotas = roteirizacaoService.buscarRota("descricaoRota", Ordenacao.ASC);
		
			List<ItemDTO<Long, String>> lista =
				new ArrayList<ItemDTO<Long,String>>();
		
		for (Rota rota : rotas) {
			
			lista.add(
				new ItemDTO<Long, String>(rota.getId(), rota.getDescricaoRota()));
		}
		
		result.include("listaRota", lista);
	}

}
