package br.com.abril.nds.controllers.distribuicao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.controllers.BaseController;
import br.com.abril.nds.dto.CotaDTO;
import br.com.abril.nds.dto.CotaNaoRecebeSegmentoDTO;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.SegmentoNaoRecebeCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroSegmentoNaoRecebidoDTO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.distribuicao.SegmentoNaoRecebido;
import br.com.abril.nds.model.distribuicao.TipoSegmentoProduto;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.service.CotaService;
import br.com.abril.nds.service.SegmentoNaoRecebidoService;
import br.com.abril.nds.service.UsuarioService;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.TipoMensagem;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.abril.nds.vo.ValidacaoVO;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

@Path("/distribuicao/segmentoNaoRecebido")
@Resource
public class SegmentoNaoRecebidoController extends BaseController {
	
	private static final String FILTRO_SESSION_ATTRIBUTE = "filtroSegmentoNaoRecebido";
	
	@Autowired
	private Result result;
	
	@Autowired
	private SegmentoNaoRecebidoService segmentoNaoRecebidoService;
	
	@Autowired
	private CotaService cotaService;
	
	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private HttpSession session;
	
	@Rules(Permissao.ROLE_DISTRIBUICAO_SEGMENTO_NAO_RECEBIDO)
	public void index(){
		// POPULANDO FILTROS
		this.carregarComboSegmento();
	}

	@Post("/pesquisarCotasNaoRecebemSegmento")
	public void pesquisarCotasNaoRecebemSegmento(FiltroSegmentoNaoRecebidoDTO filtro, String sortorder, String sortname, int page, int rp){
	
		filtro.setPaginacao(new PaginacaoVO(page, rp, sortorder, sortname));
		
		validarFiltroSegmento(filtro);
		
		tratarFiltro(filtro);
		
		List<CotaNaoRecebeSegmentoDTO> listaCotaNaoRecebeSegmentoDTO = segmentoNaoRecebidoService.obterCotasNaoRecebemSegmento(filtro);
		
		TableModel<CellModelKeyValue<CotaNaoRecebeSegmentoDTO>> tableModel = montarTableModelCotasNaoRecebemSegmento(filtro, listaCotaNaoRecebeSegmentoDTO);

		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
	}
	
	private TableModel<CellModelKeyValue<CotaNaoRecebeSegmentoDTO>> montarTableModelCotasNaoRecebemSegmento(
			FiltroSegmentoNaoRecebidoDTO filtro, 
			List<CotaNaoRecebeSegmentoDTO> listaCotaNaoRecebeSegmentoDTO) {
		
		if (listaCotaNaoRecebeSegmentoDTO == null || listaCotaNaoRecebeSegmentoDTO.isEmpty()) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum registro encontrado.");
		}
		
		TableModel<CellModelKeyValue<CotaNaoRecebeSegmentoDTO>> tableModel = new TableModel<CellModelKeyValue<CotaNaoRecebeSegmentoDTO>>();

		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listaCotaNaoRecebeSegmentoDTO));

		tableModel.setPage(filtro.getPaginacao().getPaginaAtual());

		tableModel.setTotal(filtro.getPaginacao().getQtdResultadosTotal());
		
		return tableModel;
	}
	
	@Post("/pesquisarSegmentosNaoRecebeCota")
	public void pesquisarSegmentosNaoRecebeCota(FiltroSegmentoNaoRecebidoDTO filtro, String sortorder, String sortname, int page, int rp){
	
		filtro.setPaginacao(new PaginacaoVO(page, rp, sortorder, sortname));
		
		validarFiltroSegmento(filtro);
		
		tratarFiltro(filtro);
		
		List<SegmentoNaoRecebeCotaDTO> listaSegmentoNaoRecebeCotaDTO = segmentoNaoRecebidoService.obterSegmentosNaoRecebemCota(filtro);
		
		TableModel<CellModelKeyValue<SegmentoNaoRecebeCotaDTO>> tableModel = montarTableModelSegmentosNaoRecebemCota(filtro, listaSegmentoNaoRecebeCotaDTO);

		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
	}
	
	private TableModel<CellModelKeyValue<SegmentoNaoRecebeCotaDTO>> montarTableModelSegmentosNaoRecebemCota(
			FiltroSegmentoNaoRecebidoDTO filtro, 
			List<SegmentoNaoRecebeCotaDTO> listaSegmentoNaoRecebeCotaDTO) {
		
		if (listaSegmentoNaoRecebeCotaDTO == null || listaSegmentoNaoRecebeCotaDTO.isEmpty()) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum registro encontrado.");
		}
		
		TableModel<CellModelKeyValue<SegmentoNaoRecebeCotaDTO>> tableModel = new TableModel<CellModelKeyValue<SegmentoNaoRecebeCotaDTO>>();

		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listaSegmentoNaoRecebeCotaDTO));

		tableModel.setPage(filtro.getPaginacao().getPaginaAtual());

		tableModel.setTotal(filtro.getPaginacao().getQtdResultadosTotal());
		
		return tableModel;
	}
	
	@Post("/excluirSegmentoNaoRecebido")
	public void excluirSegmentoNaoRecebido(Long segmentoNaoRecebidoId){
		
		segmentoNaoRecebidoService.excluirSegmentoNaoRecebido(segmentoNaoRecebidoId);
		
		result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Operação realizada com sucesso."),
				"result").recursive().serialize();
	}
	
	@Post("/pesquisarCotasNaoEstaoNoSegmento")
	public void pesquisarCotasNaoEstaoNoSegmento(FiltroSegmentoNaoRecebidoDTO filtro, String sortorder, int page, int rp){
		
		// fazer validação do numero e nome da cota;
		filtro.setPaginacao(new PaginacaoVO(page, rp, sortorder));
		
		List<CotaDTO> listaCotaDTO = segmentoNaoRecebidoService.obterCotasNaoEstaoNoSegmento(filtro);
		
		if (listaCotaDTO == null || listaCotaDTO.isEmpty()) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum registro encontrado.");
		}
		
		TableModel<CellModelKeyValue<CotaDTO>> tableModel = montarTableModelCotasParaInclusaoSegmento(filtro, listaCotaDTO);
		
		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
	}
	
	@Post("/incluirCotasSegmentoNaoRecebido")
	public void incluirCotasSegmentoNaoRecebido(Long[] idCotas, Long idTipoSegmento){
	
		// Valida o filtro (Seleção de cotas)
		if (idCotas.length == 0) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Nenhuma cota foi selecionada, selecione uma cota."); 
		}
		
		List<SegmentoNaoRecebido> listaSegmentoNaoRecebido = new ArrayList<SegmentoNaoRecebido>();
		
		for (Long idCota : idCotas) {
			SegmentoNaoRecebido segmentoNaoRecebido = new SegmentoNaoRecebido();
			segmentoNaoRecebido.setCota(cotaService.obterPorId(idCota));
			segmentoNaoRecebido.setTipoSegmentoProduto(segmentoNaoRecebidoService.obterTipoProdutoPorId(idTipoSegmento));
			segmentoNaoRecebido.setUsuario(usuarioService.getUsuarioLogado());
			segmentoNaoRecebido.setDataAlteracao(new Date());
			
			listaSegmentoNaoRecebido.add(segmentoNaoRecebido);
		}
		
		segmentoNaoRecebidoService.inserirCotasSegmentoNaoRecebido(listaSegmentoNaoRecebido);
		
		result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Operação realizada com sucesso."),
				"result").recursive().serialize();
		
	}
	
	private TableModel<CellModelKeyValue<CotaDTO>> montarTableModelCotasParaInclusaoSegmento(FiltroSegmentoNaoRecebidoDTO filtro, List<CotaDTO> listaCotaDTO) {
		
		TableModel<CellModelKeyValue<CotaDTO>> tableModel = new TableModel<CellModelKeyValue<CotaDTO>>();

		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listaCotaDTO));

		tableModel.setPage(filtro.getPaginacao().getPaginaAtual());

		tableModel.setTotal(listaCotaDTO.size());
		
		return tableModel;
	}
	
	private void carregarComboSegmento() {

		List<TipoSegmentoProduto> listaTipoSegmentoProduto = segmentoNaoRecebidoService.obterTipoSegmentoProduto();

		// Lista usada para popular o combobox
		List<ItemDTO<Long, String>> listaTipoSegmentoProdutoCombobox = new ArrayList<ItemDTO<Long, String>>();

		for (TipoSegmentoProduto tipoSegmentoProduto : listaTipoSegmentoProduto) {

			// Preenchendo a lista que irá representar o combobox TipoSegmentoProduto
			listaTipoSegmentoProdutoCombobox.add(new ItemDTO<Long, String>(tipoSegmentoProduto.getId(), tipoSegmentoProduto.getDescricao()));
		}

		result.include("listaTipoSegmentoProduto", listaTipoSegmentoProdutoCombobox);
	}
	
	private void validarFiltroSegmento(FiltroSegmentoNaoRecebidoDTO filtro) {
		if((filtro.getTipoSegmentoProdutoId() == null || filtro.getTipoSegmentoProdutoId() == 0))
			throw new ValidacaoException(TipoMensagem.WARNING, "Selecione um segmento.");
	}
	
	private void tratarFiltro(FiltroSegmentoNaoRecebidoDTO filtro) {
		
		FiltroSegmentoNaoRecebidoDTO filtroSession = (FiltroSegmentoNaoRecebidoDTO) session.getAttribute(FILTRO_SESSION_ATTRIBUTE);
		
		if (filtroSession != null && !filtroSession.equals(filtro)) 
			filtro.getPaginacao().setPaginaAtual(1);
		
		else if(filtroSession != null) 
			filtro.getPaginacao().setQtdResultadosTotal(filtroSession.getPaginacao().getQtdResultadosTotal());
		
		
		session.setAttribute(FILTRO_SESSION_ATTRIBUTE, filtro);
	}

}

