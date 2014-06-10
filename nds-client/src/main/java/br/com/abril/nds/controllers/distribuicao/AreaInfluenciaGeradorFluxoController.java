package br.com.abril.nds.controllers.distribuicao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.client.util.PessoaUtil;
import br.com.abril.nds.controllers.BaseController;
import br.com.abril.nds.dto.AreaInfluenciaGeradorFluxoDTO;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.filtro.FiltroAreaInfluenciaGeradorFluxoDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.pdv.AreaInfluenciaPDV;
import br.com.abril.nds.model.cadastro.pdv.TipoGeradorFluxoPDV;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.service.AreaInfluenciaGeradorFluxoService;
import br.com.abril.nds.service.PdvService;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.export.FileExporter;
import br.com.abril.nds.util.export.FileExporter.FileType;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

@Resource
@Path("/distribuicao/areaInfluenciaGeradorFluxo")
public class AreaInfluenciaGeradorFluxoController extends BaseController {

	private static final String FILTRO_SESSION_ATTRIBUTE = "filtroAreaInfluenciaGeradorFluxo";
	
	@Autowired
	private PdvService pdvService;

	@Autowired
	private HttpSession session;
	
	@Autowired
	private Result result;

	@Autowired
	private AreaInfluenciaGeradorFluxoService areaInfluenciaGeradorFluxoService;
	
	@Autowired
	private HttpServletResponse httpResponse;
	
	@Rules(Permissao.ROLE_DISTRIBUICAO_AREAINFLUENCIA_GERADORFLUXO)
	public void index() {

		this.carregarComboAreaInfluencia();

		this.carregarGeradorFluxo();
	}

	@Post
	@Path("/pesquisarPorCota")
	public void pesquisarPorCota(FiltroAreaInfluenciaGeradorFluxoDTO filtro, String sortorder, String sortname, int page, int rp) {

		filtro.setPaginacao(new PaginacaoVO(page, rp, sortorder, sortname));

		validarEntradaPorCota(filtro);
		
		filtro.setNomeCota(PessoaUtil.removerSufixoDeTipo(filtro.getNomeCota()));
		
		this.tratarFiltro(filtro);
		
		TableModel<CellModelKeyValue<AreaInfluenciaGeradorFluxoDTO>> tableModel = efetuarConsultaAreaInfluenciaPorCota(filtro);

		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
	}

	@Post
	@Path("/pesquisarPorArea")
	public void pesquisarPorArea(FiltroAreaInfluenciaGeradorFluxoDTO filtro, String sortorder, String sortname, int page, int rp) {

		filtro.setPaginacao(new PaginacaoVO(page, rp, sortorder, sortname));

		validarEntradaPorArea(filtro);
		
		this.tratarFiltro(filtro);
		
		TableModel<CellModelKeyValue<AreaInfluenciaGeradorFluxoDTO>> tableModel = efetuarConsultaAreaInfluenciaPorArea(filtro);

		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
	}
	
	@Get
	public void exportar(FileType fileType) throws IOException {
		
		FiltroAreaInfluenciaGeradorFluxoDTO filtro = (FiltroAreaInfluenciaGeradorFluxoDTO) session.getAttribute(FILTRO_SESSION_ATTRIBUTE);
		
		List<AreaInfluenciaGeradorFluxoDTO> listaFiltroAreaInfluenciaGeradorFluxoDTO = this.areaInfluenciaGeradorFluxoService.buscarPorAreaInfluencia(filtro);
		
			if(listaFiltroAreaInfluenciaGeradorFluxoDTO.isEmpty()) {
				throw new ValidacaoException(TipoMensagem.WARNING,"A última pesquisa realizada não obteve resultado.");
			}
			//TODO Alterar para dois filtros. Um para cada opção de filtro: Area Influencia ou Cota
			FileExporter.to("areaInfluenciaGeradorFluxo", fileType).inHTTPResponse(this.getNDSFileHeader(), null, null, 
					listaFiltroAreaInfluenciaGeradorFluxoDTO, AreaInfluenciaGeradorFluxoDTO.class, this.httpResponse);
			
		result.nothing();
	}
	
	private TableModel<CellModelKeyValue<AreaInfluenciaGeradorFluxoDTO>> efetuarConsultaAreaInfluenciaPorCota(
			FiltroAreaInfluenciaGeradorFluxoDTO filtro) {

		List<AreaInfluenciaGeradorFluxoDTO> listaAreaInfluenciaGeradorFluxoDTOs = this.areaInfluenciaGeradorFluxoService.buscarPorCota(filtro); 
		
		return generateTableModel(filtro, listaAreaInfluenciaGeradorFluxoDTOs);
	}
	
	private TableModel<CellModelKeyValue<AreaInfluenciaGeradorFluxoDTO>> efetuarConsultaAreaInfluenciaPorArea(
			FiltroAreaInfluenciaGeradorFluxoDTO filtro) {

		List<AreaInfluenciaGeradorFluxoDTO> listaAreaInfluenciaGeradorFluxoDTO = this.areaInfluenciaGeradorFluxoService.buscarPorAreaInfluencia(filtro);
		
		return generateTableModel(filtro, listaAreaInfluenciaGeradorFluxoDTO);
	}
	
	private TableModel<CellModelKeyValue<AreaInfluenciaGeradorFluxoDTO>> generateTableModel(FiltroAreaInfluenciaGeradorFluxoDTO filtro,
			List<AreaInfluenciaGeradorFluxoDTO> listaAreaInfluenciaGeradorFluxoDTO) {
		
		if (listaAreaInfluenciaGeradorFluxoDTO == null || listaAreaInfluenciaGeradorFluxoDTO.isEmpty()) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum registro encontrado.");
		}

		TableModel<CellModelKeyValue<AreaInfluenciaGeradorFluxoDTO>> tableModel = new TableModel<CellModelKeyValue<AreaInfluenciaGeradorFluxoDTO>>();

		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listaAreaInfluenciaGeradorFluxoDTO));

		tableModel.setPage(filtro.getPaginacao().getPaginaAtual());

		tableModel.setTotal(filtro.getPaginacao().getQtdResultadosTotal());
		
		return tableModel;
	}

	private void validarEntradaPorCota(FiltroAreaInfluenciaGeradorFluxoDTO filtro) {
		
		if((filtro.getNumeroCota() == null || filtro.getNumeroCota() == 0) && (filtro.getNomeCota() == null || filtro.getNomeCota().trim().isEmpty()))
			throw new ValidacaoException(TipoMensagem.WARNING, "Código ou nome da cota é obrigatório.");		
	}
	
	private void validarEntradaPorArea(FiltroAreaInfluenciaGeradorFluxoDTO filtro) {
		if((filtro.getAreaInfluenciaId() == null || filtro.getAreaInfluenciaId() == 0) && 
				(filtro.getGeradorFluxoPrincipalId() == null || filtro.getGeradorFluxoPrincipalId() == 0 &&
				(filtro.getGeradorFluxoSecundarioId() == null || filtro.getGeradorFluxoSecundarioId() == 0)))
			throw new ValidacaoException(TipoMensagem.WARNING, "Pelo menos um item deve ser selecionado");
	}
	
	private void tratarFiltro(FiltroAreaInfluenciaGeradorFluxoDTO filtroAtual) {

		FiltroAreaInfluenciaGeradorFluxoDTO filtroSession = (FiltroAreaInfluenciaGeradorFluxoDTO) session.getAttribute(FILTRO_SESSION_ATTRIBUTE);
		
		if (filtroSession != null && !filtroSession.equals(filtroAtual)) {
			filtroAtual.getPaginacao().setPaginaAtual(1);
		}else if(filtroSession != null) {
			filtroAtual.getPaginacao().setQtdResultadosTotal(filtroSession.getPaginacao().getQtdResultadosTotal());
		}
		
		session.setAttribute(FILTRO_SESSION_ATTRIBUTE, filtroAtual);
	}
	
	private void carregarGeradorFluxo() {

		List<TipoGeradorFluxoPDV> listaTipoGeradorFluxoPDV = pdvService.obterTipoGeradorDeFluxo();

		List<ItemDTO<Long, String>> listaTipoGeradorFluxoPDVCombo = new ArrayList<ItemDTO<Long, String>>();

		for (TipoGeradorFluxoPDV tipoGeradorDeFluxoPDV : listaTipoGeradorFluxoPDV) {

			// Preenchendo a lista que irá representar o combobox de área de
			// influência na view
			listaTipoGeradorFluxoPDVCombo.add(new ItemDTO<Long, String>(
					tipoGeradorDeFluxoPDV.getCodigo(), tipoGeradorDeFluxoPDV
							.getDescricao()));
		}

		result.include("listaTipoGeradorFluxoPDV",
				listaTipoGeradorFluxoPDVCombo);

	}

	private void carregarComboAreaInfluencia() {

		List<AreaInfluenciaPDV> listaAreaInfluenciaPDV = pdvService.obterTipoAreaInfluencia();

		List<ItemDTO<Long, String>> listaAreaInfluenciaPDVCombo = new ArrayList<ItemDTO<Long, String>>();

		for (AreaInfluenciaPDV areaInfluenciaPDV : listaAreaInfluenciaPDV) {

			// Preenchendo a lista que irá representar o combobox de área de
			// influência na view
			listaAreaInfluenciaPDVCombo.add(new ItemDTO<Long, String>(areaInfluenciaPDV.getCodigo(), areaInfluenciaPDV.getDescricao()));
		}

		result.include("listaAreaInfluenciaPDV", listaAreaInfluenciaPDVCombo);
	}
	
}
