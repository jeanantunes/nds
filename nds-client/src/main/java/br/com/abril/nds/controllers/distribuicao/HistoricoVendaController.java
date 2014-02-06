package br.com.abril.nds.controllers.distribuicao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.client.util.PessoaUtil;
import br.com.abril.nds.controllers.BaseController;
import br.com.abril.nds.dto.AnaliseHistoricoDTO;
import br.com.abril.nds.dto.CotaDTO;
import br.com.abril.nds.dto.HistoricoVendaPopUpCotaDto;
import br.com.abril.nds.dto.HistoricoVendaPopUpDTO;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.PdvDTO;
import br.com.abril.nds.dto.ProdutoEdicaoDTO;
import br.com.abril.nds.dto.RegiaoDTO;
import br.com.abril.nds.dto.filtro.FiltroDTO;
import br.com.abril.nds.dto.filtro.FiltroHistoricoVendaDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.pdv.AreaInfluenciaPDV;
import br.com.abril.nds.model.cadastro.pdv.TipoGeradorFluxoPDV;
import br.com.abril.nds.model.cadastro.pdv.TipoPontoPDV;
import br.com.abril.nds.model.distribuicao.TipoClassificacaoProduto;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.service.CapaService;
import br.com.abril.nds.service.CotaService;
import br.com.abril.nds.service.EnderecoService;
import br.com.abril.nds.service.InformacoesProdutoService;
import br.com.abril.nds.service.PdvService;
import br.com.abril.nds.service.ProdutoEdicaoService;
import br.com.abril.nds.service.ProdutoService;
import br.com.abril.nds.service.RegiaoService;
import br.com.abril.nds.service.TipoClassificacaoProdutoService;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.ComponentesPDV;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.UfEnum;
import br.com.abril.nds.util.Util;
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
@Path("/distribuicao/historicoVenda")
@Rules(Permissao.ROLE_DISTRIBUICAO_HISTORICO_VENDA)
public class HistoricoVendaController extends BaseController {

	private static final String FILTRO_SESSION_ATTRIBUTE = "FiltroHistoricoVendaDTO";
	
	private static final ValidacaoVO VALIDACAO_VO_LISTA_VAZIA = new ValidacaoVO(TipoMensagem.WARNING, "Nenhum registro encontrado.");
	
	@Autowired
	private CapaService capaService;
	
	@Autowired
	private RegiaoService regiaoService;
	
	@Autowired
	private InformacoesProdutoService infoProdService;
	
	@Autowired
	private ProdutoEdicaoService produtoEdicaoService;
	
	@Autowired
	private CotaService cotaService;

	@Autowired
	private PdvService pdvService;
	
	@Autowired
	private EnderecoService enderecoService;
	
	@Autowired
	private TipoClassificacaoProdutoService tipoClassificacaoProdutoService;
	
	@Autowired
	private ProdutoService produtoService;
	
	@Autowired
	private Result result;

	@Autowired
	private HttpSession session;
	
	@Autowired
	private HttpServletResponse httpResponse;
	
	@Path("/")
	public void historicoVenda(){
		result.include("componenteList", ComponentesPDV.values());
		this.carregarComboClassificacao();
		result.include("classificacaoProduto",tipoClassificacaoProdutoService.obterTodos());
	}
	
	@Post
	public void pesquisaProduto(FiltroHistoricoVendaDTO filtro, Long tipoClassificacaoProdutoId, String sortorder, String sortname, int page, int rp){
		
		filtro.setTipoClassificacaoProdutoId(tipoClassificacaoProdutoId);
		
		filtro.setPaginacao(new PaginacaoVO(page, rp, sortorder,sortname));
			
		filtro.setOrdemColuna(Util.getEnumByStringValue(FiltroHistoricoVendaDTO.OrdemColuna.values(), sortname));
		
//		Produto produto = this.produtoService.obterProdutoPorCodigo(filtro.getProdutoDto().getCodigoProduto());
		filtro.getProdutoDto().setCodigoProduto(Util.padLeft(filtro.getProdutoDto().getCodigoProduto(), "0", 8));
		
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
		
		List<CotaDTO> cotas = cotaService.buscarCotasQueEnquadramNoRangeDeReparte(filtro.getQtdReparteInicial(), filtro.getQtdReparteFinal(), filtro.getListProdutoEdicaoDTO(), filtro.isCotasAtivas());
		
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
		
		List<CotaDTO> cotas = cotaService.buscarCotasQueEnquadramNoRangeVenda(filtro.getQtdVendaInicial(), filtro.getQtdVendaFinal(), filtro.getListProdutoEdicaoDTO(), filtro.isCotasAtivas());
		
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
	public void pesquisarTodasAsCotas(FiltroHistoricoVendaDTO filtro){
		
		// valida se existem produtos selecionados
		filtroValidate(filtro.validarListaProduto(), filtro);
		
		// valida se o código ou nome da cota foram informados
		//filtroValidate(filtro.validarPorCota(), filtro);
		
//		filtro.getCotaDto().setNomePessoa(PessoaUtil.removerSufixoDeTipo(filtro.getCotaDto().getNomePessoa()));
		
		List<CotaDTO> cotas = cotaService.buscarCotasHistorico(filtro.getListProdutoEdicaoDTO(), filtro.isCotasAtivas());
		
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
		
		ordenarEdicoesMaiorParaMenor(listProdutoEdicaoDto);
		
		result.include("listProdutoEdicao", listProdutoEdicaoDto);
		
		session.setAttribute("listProdutoEdicao", listProdutoEdicaoDto);
		session.setAttribute("listCotas", cotas);
	}

	private void ordenarEdicoesMaiorParaMenor(List<ProdutoEdicaoDTO> listProdutoEdicaoDto) {
		Collections.sort(listProdutoEdicaoDto, new Comparator<ProdutoEdicaoDTO>() {

			@Override
			public int compare(ProdutoEdicaoDTO o1, ProdutoEdicaoDTO o2) {				
				return o2.getNumeroEdicao().compareTo(o1.getNumeroEdicao());
			}
		});
	}
	
	@SuppressWarnings("unchecked")
	@Post
	public void carregarGridAnaliseHistorico(String sortorder, String sortname){
		List<ProdutoEdicaoDTO> listProdutoEdicaoDTO = (List<ProdutoEdicaoDTO>) session.getAttribute("listProdutoEdicao");
		
		List<Cota> listCota = (List<Cota>) session.getAttribute("listCotas");
		
		List<AnaliseHistoricoDTO> listAnaliseHistorico = cotaService.buscarHistoricoCotas(listProdutoEdicaoDTO, listCota, sortorder, sortname);
		
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
		case TIPO_PONTO_DE_VENDA:
			for(TipoPontoPDV tipo : pdvService.obterTiposPontoPDVPrincipal()){
				resultList.add(new ItemDTO(tipo.getCodigo(),tipo.getDescricao()));
			}
			break;
		case AREA_DE_INFLUENCIA:
			for(AreaInfluenciaPDV tipo : pdvService.obterAreasInfluenciaPDV()){
				resultList.add(new ItemDTO(tipo.getCodigo(),tipo.getDescricao()));
			}
			break;

		case BAIRRO:
			for(String tipo : enderecoService.obterBairrosCotas()){
				resultList.add(new ItemDTO(tipo,tipo));
			}
			break;
		case DISTRITO:
			for(UfEnum tipo : UfEnum.values()){
				resultList.add(new ItemDTO(tipo.getSigla(),tipo.getSigla()));
			}
			break;
		case GERADOR_DE_FLUXO:
			for(TipoGeradorFluxoPDV tipo : pdvService.obterTodosTiposGeradorFluxoOrdenado()){
				resultList.add(new ItemDTO(tipo.getCodigo(),tipo.getDescricao()));
			}
			break;
		case COTAS_A_VISTA:
		    resultList.add(new ItemDTO("CONSIGNADO", "Consignado"));
		    resultList.add(new ItemDTO("A_VISTA", "Cotas à Vista"));
			break;
		case COTAS_NOVAS_RETIVADAS :
		    resultList.add(new ItemDTO(1, "Sim"));
		    resultList.add(new ItemDTO(0, "Não"));
			break;
		case REGIAO:
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
		
		List<AnaliseHistoricoDTO> dto = cotaService.buscarHistoricoCotas(listProdutoEdicaoDTO, listCota, null, null);
		
		try {
			FileExporter.to("Analise Historico Venda", fileType).inHTTPResponse(this.getNDSFileHeader(), null, null, dto,
					AnaliseHistoricoDTO.class, this.httpResponse);
		} catch (Exception e) {
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.ERROR, "Não foi possível gerar o arquivo ." + fileType.toString()));
		}
		
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
	
	private void carregarComboClassificacao(){
		List<ItemDTO<Long,String>> comboClassificacao =  new ArrayList<ItemDTO<Long,String>>();
		List<TipoClassificacaoProduto> classificacoes = infoProdService.buscarClassificacao();
		
		for (TipoClassificacaoProduto tipoClassificacaoProduto : classificacoes) {
			comboClassificacao.add(new ItemDTO<Long,String>(tipoClassificacaoProduto.getId(), tipoClassificacaoProduto.getDescricao()));
		}
		result.include("listaClassificacao",comboClassificacao);		
	}
}