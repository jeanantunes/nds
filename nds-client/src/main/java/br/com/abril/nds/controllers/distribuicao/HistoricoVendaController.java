package br.com.abril.nds.controllers.distribuicao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.client.util.PessoaUtil;
import br.com.abril.nds.controllers.BaseController;
import br.com.abril.nds.dto.AnaliseHistoricoDTO;
import br.com.abril.nds.dto.CotaDTO;
import br.com.abril.nds.dto.CotaQueRecebeExcecaoDTO;
import br.com.abril.nds.dto.HistoricoVendaPopUpCotaDto;
import br.com.abril.nds.dto.HistoricoVendaPopUpDTO;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.PdvDTO;
import br.com.abril.nds.dto.ProdutoEdicaoDTO;
import br.com.abril.nds.dto.ProdutoRecebidoDTO;
import br.com.abril.nds.dto.RegiaoDTO;
import br.com.abril.nds.dto.filtro.FiltroDTO;
import br.com.abril.nds.dto.filtro.FiltroExcecaoSegmentoParciaisDTO;
import br.com.abril.nds.dto.filtro.FiltroHistoricoVendaDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.pdv.AreaInfluenciaPDV;
import br.com.abril.nds.model.cadastro.pdv.TipoGeradorFluxoPDV;
import br.com.abril.nds.model.cadastro.pdv.TipoPontoPDV;
import br.com.abril.nds.model.distribuicao.Regiao;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.service.CapaService;
import br.com.abril.nds.service.CotaService;
import br.com.abril.nds.service.EnderecoService;
import br.com.abril.nds.service.PdvService;
import br.com.abril.nds.service.ProdutoEdicaoService;
import br.com.abril.nds.service.RegiaoService;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.ComponentesPDV;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.UfEnum;
import br.com.abril.nds.util.export.FileExporter;
import br.com.abril.nds.util.export.FileExporter.FileType;
import br.com.abril.nds.vo.ValidacaoVO;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.serialization.xstream.XStreamBuilder;
import br.com.caelum.vraptor.serialization.xstream.XStreamJSONSerialization;
import br.com.caelum.vraptor.view.Results;

@Resource
@Path("/distribuicao/historicoVenda")
public class HistoricoVendaController extends BaseController {

	private static final String FILTRO_SESSION_ATTRIBUTE = "FiltroHistoricoVendaDTO";
	
	private static final ValidacaoVO VALIDACAO_VO_SUCESSO = new ValidacaoVO(TipoMensagem.SUCCESS, "Operação realizada com sucesso.");
	private static final ValidacaoVO VALIDACAO_VO_LISTA_VAZIA = new ValidacaoVO(TipoMensagem.WARNING, "Nenhum registro encontrado.");
	
	@Autowired
	private CapaService capaService;
	
	@Autowired
	private RegiaoService regiaoService;
	
	@Autowired
	private XStreamJSONSerialization jsonSerializer;
	
	@Autowired
	private XStreamBuilder xStreamBuilder; 
	
	@Autowired
	private ProdutoEdicaoService produtoEdicaoService;
	
	@Autowired
	private CotaService cotaService;

	@Autowired
	private PdvService pdvService;
	
	@Autowired
	private EnderecoService enderecoService;
	
	@Autowired
	private Result result;

	@Autowired
	private HttpSession session;
	
	@Autowired
	private HttpServletResponse httpResponse;
	
	@Path("/index")
	@Rules(Permissao.ROLE_DISTRIBUICAO_HISTORICO_VENDA)
	public void historicoVenda(){
		result.include("componenteList", ComponentesPDV.values());
	}
	
	@Post
	public void pesquisaProduto(FiltroHistoricoVendaDTO filtro){
		// valida se o filtro foi devidamente preenchido pelo usuário
		filtroValidate(filtro.validarEntradaFiltroProduto(), filtro);
		
		List<ProdutoEdicaoDTO> listEdicoesProdutoDto = this.produtoEdicaoService.obterEdicoesProduto(filtro);
		
		TableModel<CellModelKeyValue<ProdutoEdicaoDTO>> tableModel = new TableModel<CellModelKeyValue<ProdutoEdicaoDTO>>();
		
		this.configurarTableModelSemPaginacao(listEdicoesProdutoDto, tableModel);
		
		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
	}
	
	@Post
	public void pesquisaCotaPorQtdReparte(FiltroHistoricoVendaDTO filtro){
		// valida se existe algum produto selecionado
		filtroValidate(filtro.validarListaProduto(), filtro);

		// valida se o campo percentual venda está preenchido
		filtroValidate(filtro.validarPorQtdReparte(), filtro);
		
		List<CotaDTO> cotas = cotaService.buscarCotasQueInquadramNoRangeDeReparte(filtro.getQtdReparteInicial(), filtro.getQtdReparteFinal(), filtro.getListProdutoEdicaoDTO(), filtro.isCotasAtivas());
		
		validarLista(cotas);
		
		TableModel<CellModelKeyValue<CotaDTO>> tableModel = new TableModel<CellModelKeyValue<CotaDTO>>();
		
		this.configurarTableModelSemPaginacao(cotas, tableModel);

		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
	}
	
	@Post
	public void pesquisaCotaPorQtdVenda(FiltroHistoricoVendaDTO filtro){
		// valida se existe algum produto selecionado
		filtroValidate(filtro.validarListaProduto(), filtro);

		// valida se o campo percentual venda está preenchido
		filtroValidate(filtro.validarPorQtdVenda(), filtro);
		
		List<CotaDTO> cotas = cotaService.buscarCotasQueInquadramNoRangeVenda(filtro.getQtdVendaInicial(), filtro.getQtdVendaFinal(), filtro.getListProdutoEdicaoDTO(), filtro.isCotasAtivas());
		
		validarLista(cotas);
		
		TableModel<CellModelKeyValue<CotaDTO>> tableModel = new TableModel<CellModelKeyValue<CotaDTO>>();
		
		this.configurarTableModelSemPaginacao(cotas, tableModel);

		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
	}
	
	@Post
	public void pesquisaCotaPorPercentualVenda(FiltroHistoricoVendaDTO filtro){
		// valida se existe algum produto selecionado
		filtroValidate(filtro.validarListaProduto(), filtro);

		// valida se o campo percentual venda está preenchido
		filtroValidate(filtro.validarPorPercentualVenda(), filtro);
		
		List<CotaDTO> cotas = cotaService.buscarCotasQuePossuemPercentualVendaSuperior(filtro.getPercentualVenda(), filtro.getListProdutoEdicaoDTO(), filtro.isCotasAtivas());
		
		validarLista(cotas);
		
		TableModel<CellModelKeyValue<CotaDTO>> tableModel = new TableModel<CellModelKeyValue<CotaDTO>>();
		
		this.configurarTableModelSemPaginacao(cotas, tableModel);

		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
	}

	@Post
	public void pesquisaCotaPorNumeroOuNome(FiltroHistoricoVendaDTO filtro){
		
		// valida se existem produtos selecionados
		filtroValidate(filtro.validarListaProduto(), filtro);
		
		// valida se o código ou nome da cota foram informados
		filtroValidate(filtro.validarPorCota(), filtro);
		
		filtro.getCotaDto().setNomePessoa(PessoaUtil.removerSufixoDeTipo(filtro.getCotaDto().getNomePessoa()));
		
		List<CotaDTO> cotas = cotaService.buscarCotasPorNomeOuNumero(filtro.getCotaDto(), filtro.getListProdutoEdicaoDTO(), filtro.isCotasAtivas());
		
		validarLista(cotas);
		
		TableModel<CellModelKeyValue<CotaDTO>> tableModel = new TableModel<CellModelKeyValue<CotaDTO>>();
		
		this.configurarTableModelSemPaginacao(cotas, tableModel);

		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
	}
	
	@Post
	public void pesquisaCotaPorComponentes(FiltroHistoricoVendaDTO filtro){
		// valida se existem produtos selecionados
		filtroValidate(filtro.validarListaProduto(), filtro);
		
		// valida se algum componente foi informado
		filtroValidate(filtro.validarPorComponentes(), filtro);
		
		List<CotaDTO> cotas = this.cotaService.buscarCotasPorComponentes(filtro.getComponentesPdv(), filtro.getElemento(), filtro.getListProdutoEdicaoDTO(), filtro.isCotasAtivas());
		
		validarLista(cotas);
		
		TableModel<CellModelKeyValue<CotaDTO>> tableModel = new TableModel<CellModelKeyValue<CotaDTO>>();
		
		this.configurarTableModelSemPaginacao(cotas, tableModel);

		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
	}
	
	/**
	 * Faz a entrada da análise histórico de vendas (TELA ANÁLISE)
	 * 
	 */
	@Post
	public void analiseHistorico(List<ProdutoEdicaoDTO> listProdutoEdicaoDto, List<Cota> cotas){
		
		Collections.sort(listProdutoEdicaoDto);
		
		result.include("listProdutoEdicao", listProdutoEdicaoDto);
		
		session.setAttribute("listProdutoEdicao", listProdutoEdicaoDto);
		session.setAttribute("listCotas", cotas);
	}
	
	@Post
	public void carregarGridAnaliseHistorico(){
		List<ProdutoEdicaoDTO> listProdutoEdicaoDTO = (List<ProdutoEdicaoDTO>) session.getAttribute("listProdutoEdicao");
		
		List<Cota> listCota = (List<Cota>) session.getAttribute("listCotas");
		
		List<AnaliseHistoricoDTO> listAnaliseHistorico = cotaService.buscarHistoricoCotas(listProdutoEdicaoDTO, listCota);
		
		TableModel<CellModelKeyValue<AnaliseHistoricoDTO>> tableModel = new TableModel<CellModelKeyValue<AnaliseHistoricoDTO>>();
		
		this.configurarTableModelSemPaginacao(listAnaliseHistorico, tableModel);
		
		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
	}
	
	@Post
	public void carregarPdv(Integer numeroCota){
		HistoricoVendaPopUpDTO historicoVendaPopUpDTO = new HistoricoVendaPopUpDTO();
		
		List<PdvDTO> list = pdvService.obterPDVs(numeroCota);
		HistoricoVendaPopUpCotaDto popUpDto = cotaService.buscarCota(numeroCota);

		historicoVendaPopUpDTO.setTableModel(new TableModel<CellModelKeyValue<PdvDTO>>());
		configurarTableModelSemPaginacao(list, historicoVendaPopUpDTO.getTableModel());

		historicoVendaPopUpDTO.setCotaDto(popUpDto);
		
		result.use(Results.json()).withoutRoot().from(historicoVendaPopUpDTO).recursive().serialize();
	}
	
	private void validarLista(List<?> list){
		if (list != null && list.isEmpty()) {
			throw new ValidacaoException(VALIDACAO_VO_LISTA_VAZIA);
		}
	}
	
	@Post
	public void carregarElementos(ComponentesPDV componente){
		List<ItemDTO<Long, String>> resultList = new ArrayList<ItemDTO<Long, String>>();
	
		switch (componente) {
		case TipoPontodeVenda:
			for(TipoPontoPDV tipo : pdvService.obterTiposPontoPDVPrincipal()){
				resultList.add(new ItemDTO(tipo.getCodigo(),tipo.getDescricao()));
			}
			break;
		case Area_de_Influência:
			for(AreaInfluenciaPDV tipo : pdvService.obterAreasInfluenciaPDV()){
				resultList.add(new ItemDTO(tipo.getCodigo(),tipo.getDescricao()));
			}
			break;

		case Bairro:
			for(String tipo : enderecoService.obterBairrosCotas()){
				resultList.add(new ItemDTO(tipo,tipo));
			}
			break;
		case Distrito:
			for(UfEnum tipo : UfEnum.values()){
				resultList.add(new ItemDTO(tipo.getSigla(),tipo.getSigla()));
			}
			break;
		case GeradorDeFluxo:
			for(TipoGeradorFluxoPDV tipo : pdvService.obterTodosTiposGeradorFluxo()){
				resultList.add(new ItemDTO(tipo.getCodigo(),tipo.getDescricao()));
			}
			break;
		case CotasAVista:
			
			break;
		case CotasNovasRetivadas :
			
			break;
		case Região:
			for (RegiaoDTO regiao : regiaoService.buscarRegiao()) {
				resultList.add(new ItemDTO(regiao.getIdRegiao(), regiao.getNomeRegiao()));
			}
			break;
		default:
			break;
		}
	
			result.use(Results.json()).from(resultList, "result").recursive().serialize();
	}
	
	@SuppressWarnings("unchecked")
	@Get
	public void exportar(FileType fileType) throws IOException {
		
		List<ProdutoEdicaoDTO> listProdutoEdicaoDTO = (List<ProdutoEdicaoDTO>) session.getAttribute("listProdutoEdicao");
		List<Cota> listCota = (List<Cota>) session.getAttribute("listCotas");
		
		List<AnaliseHistoricoDTO> dto = cotaService.buscarHistoricoCotas(listProdutoEdicaoDTO, listCota);
		
		FileExporter.to("Analise Historico Venda", fileType).inHTTPResponse(this.getNDSFileHeader(), null, null, dto,
				AnaliseHistoricoDTO.class, this.httpResponse);
		
		result.nothing();
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private TableModel configurarTableModelSemPaginacao( List listaDto, TableModel tableModel){
		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listaDto));

		tableModel.setPage(1);

		tableModel.setTotal(listaDto.size());
		
		return tableModel;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked"})
	private TableModel configurarTableModelComPaginacao( List listaDto, TableModel tableModel, FiltroDTO filtro){
		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listaDto));

		tableModel.setPage(filtro.getPaginacao().getPaginaAtual());

		tableModel.setTotal(filtro.getPaginacao().getQtdResultadosTotal());
		
		return tableModel;
	}
	
	private void guardarFiltroNaSession(FiltroHistoricoVendaDTO filtro) {
		
		FiltroHistoricoVendaDTO filtroSession = (FiltroHistoricoVendaDTO) session.getAttribute(FILTRO_SESSION_ATTRIBUTE);
		
		if (filtroSession != null && !filtroSession.equals(filtro)){
			filtro.getPaginacao().setPaginaAtual(1);
		}
		
		session.setAttribute(FILTRO_SESSION_ATTRIBUTE, filtro);
	}
	
	private void filtroValidate(boolean isValid, FiltroHistoricoVendaDTO filtro){
		if (!isValid) {
			throw new ValidacaoException(TipoMensagem.WARNING, filtro.getValidationMsg());
		}
	}
	
}
