package br.com.abril.nds.controllers.distribuicao;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellReference;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.client.util.PessoaUtil;
import br.com.abril.nds.controllers.BaseController;
import br.com.abril.nds.dto.AnaliseHistoricoDTO;
import br.com.abril.nds.dto.AnaliseHistoricoXLSDTO;
import br.com.abril.nds.dto.CotaDTO;
import br.com.abril.nds.dto.HistoricoVendaPopUpCotaDto;
import br.com.abril.nds.dto.HistoricoVendaPopUpDTO;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.PdvDTO;
import br.com.abril.nds.dto.ProdutoEdicaoDTO;
import br.com.abril.nds.dto.RegiaoDTO;
import br.com.abril.nds.dto.filtro.FiltroHistoricoVendaDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.enums.TipoParametroSistema;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.TipoCota;
import br.com.abril.nds.model.cadastro.pdv.AreaInfluenciaPDV;
import br.com.abril.nds.model.cadastro.pdv.TipoGeradorFluxoPDV;
import br.com.abril.nds.model.cadastro.pdv.TipoPontoPDV;
import br.com.abril.nds.model.distribuicao.TipoClassificacaoProduto;
import br.com.abril.nds.model.integracao.ParametroSistema;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.service.CotaService;
import br.com.abril.nds.service.EnderecoService;
import br.com.abril.nds.service.InformacoesProdutoService;
import br.com.abril.nds.service.PdvService;
import br.com.abril.nds.service.ProdutoEdicaoService;
import br.com.abril.nds.service.ProdutoService;
import br.com.abril.nds.service.RegiaoService;
import br.com.abril.nds.service.TipoClassificacaoProdutoService;
import br.com.abril.nds.service.integracao.ParametroSistemaService;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.ComponentesPDV;
import br.com.abril.nds.util.DateUtil;
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

	private static final ValidacaoVO VALIDACAO_VO_LISTA_VAZIA = new ValidacaoVO(TipoMensagem.WARNING, "Nenhum registro encontrado.");
	
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
	private Result result;

	@Autowired
	private HttpSession session;
	
	@Autowired
	private HttpServletResponse httpResponse;
	
	@Autowired
	private ParametroSistemaService parametroSistemaService;
	
	@Autowired
	private ProdutoService produtoService;
	
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
		
		session.setAttribute("isFiltroTodasCotas", Boolean.FALSE);
		
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
		
		session.setAttribute("isFiltroTodasCotas", Boolean.FALSE);
		
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
		
		session.setAttribute("isFiltroTodasCotas", Boolean.FALSE);
		
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
		
		session.setAttribute("isFiltroTodasCotas", Boolean.FALSE);
		
		TableModel<CellModelKeyValue<CotaDTO>> tableModel = new TableModel<CellModelKeyValue<CotaDTO>>();
		
		this.configurarTableModelSemPaginacao(cotas, tableModel);

		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
	}
	
	@Post
	public void pesquisarTodasAsCotas(FiltroHistoricoVendaDTO filtro){
		
		// valida se existem produtos selecionados
		filtroValidate(filtro.validarListaProduto(), filtro);
		
		List<CotaDTO> cotas = cotaService.buscarCotasComEsemReparte(filtro.getListProdutoEdicaoDTO());
		
		session.setAttribute("isFiltroTodasCotas", Boolean.TRUE);
		
		validarLista(cotas);
		
		TableModel<CellModelKeyValue<CotaDTO>> tableModel = new TableModel<CellModelKeyValue<CotaDTO>>();
		
		this.configurarTableModelSemPaginacao(cotas, tableModel);

		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
	}
	
	@Post
	public void pesquisarCotasDoHistorico(FiltroHistoricoVendaDTO filtro){
		
		// valida se existem produtos selecionados
		filtroValidate(filtro.validarListaProduto(), filtro);
		
		List<CotaDTO> cotas = cotaService.buscarCotasHistorico(filtro.getListProdutoEdicaoDTO(), filtro.isCotasAtivas());
		
		validarLista(cotas);
		
		session.setAttribute("isFiltroTodasCotas",  Boolean.FALSE);
		
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
		
		session.setAttribute("isFiltroTodasCotas", Boolean.FALSE);
		
		TableModel<CellModelKeyValue<CotaDTO>> tableModel = new TableModel<CellModelKeyValue<CotaDTO>>();
		
		this.configurarTableModelSemPaginacao(cotas, tableModel);

		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
	}
	
	/**
	 * Faz a entrada da análise histórico de vendas (TELA ANÁLISE)
	 * 
	 */
	@Post
	public void analiseHistorico(List<ProdutoEdicaoDTO> listProdutoEdicaoDto, List<Integer> cotas){
		
		result.include("listProdutoEdicao", listProdutoEdicaoDto);
		
		session.setAttribute("listProdutoEdicao", listProdutoEdicaoDto);
		session.setAttribute("listCotas", cotas);
	}
	
	@SuppressWarnings("unchecked")
	@Post
	public void carregarGridAnaliseHistorico(String sortorder, String sortname){
		
		List<ProdutoEdicaoDTO> listProdutoEdicaoDTO = (List<ProdutoEdicaoDTO>) session.getAttribute("listProdutoEdicao");
		
		List<Integer> listCota = (List<Integer>) session.getAttribute("listCotas");
		
		Boolean isFiltroTodasCotas = (Boolean) session.getAttribute("isFiltroTodasCotas"); 
		
		if(isFiltroTodasCotas == null){
			isFiltroTodasCotas = false;
		}
		
		List<AnaliseHistoricoDTO> listAnaliseHistorico = 
			cotaService.buscarHistoricoCotas(
				listProdutoEdicaoDTO, listCota, sortorder, sortname, isFiltroTodasCotas);
		
		AnaliseHistoricoDTO suma = new AnaliseHistoricoDTO();
		suma.setReparteMedio(0d);
		suma.setVendaMedia(0d);
		
		for (AnaliseHistoricoDTO dto : listAnaliseHistorico){
			
			suma.setNumeroCota(suma.getNumeroCota() + (dto.getNumeroCota() == null ? 0 : 1));
			suma.setQtdPdv(dto.getQtdPdv() + suma.getQtdPdv());
			suma.setReparteMedio((dto.getReparteMedio() != null ? dto.getReparteMedio() : 0d)  + suma.getReparteMedio());
			
			if (dto.getVendaMedia() != null){
				suma.setVendaMedia(dto.getVendaMedia() + suma.getVendaMedia());
			}

			if (dto.getEd1Reparte() != null){
				suma.setEd1Reparte(dto.getEd1Reparte() + (suma.getEd1Reparte() == null ? 0 : suma.getEd1Reparte()));
				
				if (dto.getEd1Venda() != null){
				    suma.setEd1Venda(dto.getEd1Venda() + (suma.getEd1Venda() == null ? 0 : suma.getEd1Venda()));
				}
			}
			
			if (dto.getEd2Reparte() != null){
				suma.setEd2Reparte(dto.getEd2Reparte() + (suma.getEd2Reparte() == null ? 0 : suma.getEd2Reparte()));
				
				if (dto.getEd2Venda() != null){
				    suma.setEd2Venda(dto.getEd2Venda() + (suma.getEd2Venda() == null ? 0 : suma.getEd2Venda()));
				}
			}
			
			if (dto.getEd3Reparte() != null){
				suma.setEd3Reparte(dto.getEd3Reparte() + (suma.getEd3Reparte() == null ? 0 : suma.getEd3Reparte()));
				
				if (dto.getEd3Venda() != null){
				    suma.setEd3Venda(dto.getEd3Venda() + (suma.getEd3Venda() == null ? 0 : suma.getEd3Venda()));
				}
			}
			
			if (dto.getEd4Reparte() != null){
				suma.setEd4Reparte(dto.getEd4Reparte() + (suma.getEd4Reparte() == null ? 0 : suma.getEd4Reparte()));
				
				if (dto.getEd4Venda() != null){
				    suma.setEd4Venda(dto.getEd4Venda() + (suma.getEd4Venda() == null ? 0 : suma.getEd4Venda()));
				}
			}
			
			if (dto.getEd5Reparte() != null){
				suma.setEd5Reparte(dto.getEd5Reparte() + (suma.getEd5Reparte() == null ? 0 : suma.getEd5Reparte()));
				
				if (dto.getEd5Venda() != null){
				    suma.setEd5Venda(dto.getEd5Venda() + (suma.getEd5Venda() == null ? 0 : suma.getEd5Venda()));
				}
			}
			
			if (dto.getEd6Reparte() != null){
				suma.setEd6Reparte(dto.getEd6Reparte() + (suma.getEd6Reparte() == null ? 0 : suma.getEd6Reparte()));
				
				if (dto.getEd6Venda() != null){
				    suma.setEd6Venda(dto.getEd6Venda() + (suma.getEd6Venda() == null ? 0 : suma.getEd6Venda()));
				}
			}
		}
		
		listAnaliseHistorico.add(suma);
		
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
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Post
	public void carregarElementos(ComponentesPDV componente){
		List<ItemDTO<Long, String>> resultList = new ArrayList<ItemDTO<Long, String>>();
	
		switch (componente) {
		case TIPO_PONTO_DE_VENDA:
			for(TipoPontoPDV tipo : pdvService.obterTiposPontoPDVPrincipal()){
				resultList.add(new ItemDTO<Long, String>(tipo.getCodigo(),tipo.getDescricao()));
			}
			break;
		case AREA_DE_INFLUENCIA:
			for(AreaInfluenciaPDV tipo : pdvService.obterAreasInfluenciaPDV()){
				resultList.add(new ItemDTO<Long, String>(tipo.getCodigo(),tipo.getDescricao()));
			}
			break;

		case BAIRRO:
			for(String tipo : enderecoService.obterBairrosCotas()){
				resultList.add(new ItemDTO(tipo,tipo));
			}
			break;
		case DISTRITO:
			for(UfEnum tipo : UfEnum.values()){
				resultList.add(new ItemDTO<Long, String>(Long.valueOf(tipo.getSigla()),tipo.getSigla().toString()));
			}
			break;
		case GERADOR_DE_FLUXO:
			for(TipoGeradorFluxoPDV tipo : pdvService.obterTodosTiposGeradorFluxoOrdenado()){
				resultList.add(new ItemDTO(tipo.getCodigo(),tipo.getDescricao()));
			}
			break;
		case COTAS_A_VISTA:
		    resultList.add(new ItemDTO(TipoCota.CONSIGNADO.name(), TipoCota.CONSIGNADO.getDescTipoCota()));
		    resultList.add(new ItemDTO(TipoCota.A_VISTA.name(), TipoCota.A_VISTA.getDescTipoCota()));
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
		List<Integer> listCota = (List<Integer>) session.getAttribute("listCotas");
		
		
		for (ProdutoEdicaoDTO produtoEdicaoDTO : listProdutoEdicaoDTO) {
			String nomeProduto = produtoService.obterNomeProdutoPorCodigo(produtoEdicaoDTO.getCodigoProduto());
			
			String prodEdicao = produtoEdicaoDTO.getCodigoProduto() + " - " + nomeProduto + " - " + produtoEdicaoDTO.getNumeroEdicao();
			
			
			produtoEdicaoDTO.setNomeProduto(prodEdicao);
		}
		
		Boolean isFiltroTodasCotas = (Boolean) session.getAttribute("isFiltroTodasCotas");
		
		if(isFiltroTodasCotas == null){
			isFiltroTodasCotas = false;
		}
		
		List<AnaliseHistoricoDTO> listDto = cotaService.buscarHistoricoCotas(listProdutoEdicaoDTO, listCota, null, null, isFiltroTodasCotas);
		
		if (fileType == FileType.XLS) {

			Map<Integer, AnaliseHistoricoXLSDTO> cotaComdadosPdvDTO = cotaService.dadosPDVhistoricoXLS(listCota);

			List<AnaliseHistoricoXLSDTO> listCotasComPDV = new ArrayList<>();

			parseListaRetorno(listDto, cotaComdadosPdvDTO, listCotasComPDV);

			String pathSystem = getPathFileSystem();
			
			pathSystem = "/Users/romulohpa/opt/ambiente1/parametros_nds/historicoVendaTemp/";

			ByteArrayOutputStream os = new ByteArrayOutputStream();

			try {

				String fileName = "Analise Historico Venda - " + DateUtil.formatarDataPTBR(new Date());

				String pathFileName = pathSystem + "histOutTemp.xls";
				
				FileExporter.to(fileName, fileType).inOutputStream(this.getNDSFileHeader(), null, null, listCotasComPDV,
						AnaliseHistoricoXLSDTO.class, os);

				try {
					OutputStream out = new FileOutputStream(pathFileName);
					out.write(os.toByteArray());
					out.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}

				HSSFWorkbook workbook = null;

				try {
					FileInputStream file = new FileInputStream(new File(pathFileName));

					workbook = new HSSFWorkbook(file);

					HSSFSheet sheetHistorico = workbook.getSheetAt(0);

					// Criando cabecalho para produtos/edicoes 
					
					createCell(listProdutoEdicaoDTO, sheetHistorico, "Q7", 0);
					createCell(listProdutoEdicaoDTO, sheetHistorico, "S7", 1);
					createCell(listProdutoEdicaoDTO, sheetHistorico, "U7", 2);
					createCell(listProdutoEdicaoDTO, sheetHistorico, "W7", 3);
					createCell(listProdutoEdicaoDTO, sheetHistorico, "Y7", 4);
					createCell(listProdutoEdicaoDTO, sheetHistorico, "AA7", 5);

					file.close();

					FileOutputStream outFile = new FileOutputStream(new File(pathFileName));

					workbook.write(outFile);
					outFile.close();

					System.out.println("Arquivo Excel editado com sucesso!");

				} catch (FileNotFoundException e) {
					e.printStackTrace();
					System.out.println("Arquivo Excel não encontrado!");
				} catch (IOException e) {
					e.printStackTrace();
					System.out.println("Erro na edição do arquivo!");
				}

				this.httpResponse.setHeader("Content-Disposition", "attachment; filename= " + fileName + ".xls");

				OutputStream output;

				output = this.httpResponse.getOutputStream();

				ByteArrayOutputStream outBA = new ByteArrayOutputStream();

				workbook.write(outBA);

				output.write(outBA.toByteArray());

				output.flush();
				output.close();

				httpResponse.getOutputStream().close();

			} catch (Exception e) {
				throw new ValidacaoException(new ValidacaoVO(TipoMensagem.ERROR,
						"Não foi possível gerar o arquivo ." + fileType.toString()));
			}
		}else{
			try {
				FileExporter.to("Analise Historico Venda", fileType).inHTTPResponse(this.getNDSFileHeader(), null, listDto,
						AnaliseHistoricoDTO.class, this.httpResponse);
			} catch (Exception e) {
				throw new ValidacaoException(new ValidacaoVO(TipoMensagem.ERROR, "Não foi possível gerar o arquivo ." + fileType.toString()));
			}
		}
		
	}

	private void createCell(List<ProdutoEdicaoDTO> listProdutoEdicaoDTO, HSSFSheet sheetHistorico, String cellReferenceId, int idListProdutoEdicaoDTO) {
		
		CellReference cellReference = new CellReference(cellReferenceId);
		
		Row row = sheetHistorico.getRow(cellReference.getRow());

		Cell cell = row.getCell(cellReference.getCol());

		String nomeProduto = "";
		
		if(idListProdutoEdicaoDTO < listProdutoEdicaoDTO.size()){
			nomeProduto = listProdutoEdicaoDTO.get(idListProdutoEdicaoDTO).getNomeProduto() != null ? listProdutoEdicaoDTO.get(idListProdutoEdicaoDTO).getNomeProduto() : ""; 
		}
		
		if (cell == null) {
			
			cell = row.createCell(cellReference.getCol());
			
			cell.setCellValue(nomeProduto);
		}else{
			cell.setCellValue(nomeProduto);
		}
	}

	private String getPathFileSystem() {
		
		ParametroSistema pathSistem = this.parametroSistemaService.buscarParametroPorTipoParametro(TipoParametroSistema.PATH_TRANSFERENCIA_ARQUIVO);

		String pathFile = pathSistem.getValor();

		pathFile.replace('/',File.separatorChar);
		pathFile.replace('\\',File.separatorChar);
		
		pathFile = pathFile+File.separator+"historicoVendaTemp";
		
		File diretorioRaiz = new File(pathFile);
		diretorioRaiz.mkdirs();
		
		pathFile += File.separatorChar;
		
		return pathFile;
		
	}
	
	private void parseListaRetorno(List<AnaliseHistoricoDTO> listDto,
			Map<Integer, AnaliseHistoricoXLSDTO> cotaComdadosPdvDTO, List<AnaliseHistoricoXLSDTO> listCotasComPDV) {
		
		for (AnaliseHistoricoDTO dto : listDto) {
			
			AnaliseHistoricoXLSDTO pdvComDados = cotaComdadosPdvDTO.get(dto.getNumeroCota());
			
			pdvComDados.setStatusCota(dto.getStatusCota());
			pdvComDados.setNomePessoa(dto.getNomePessoa());
			pdvComDados.setQtdPdv(dto.getQtdPdv());
			pdvComDados.setReparteMedioFormat(dto.getReparteMedioFormat());
			pdvComDados.setVendaMediaFormat(dto.getVendaMediaFormat());
			
			if(dto.getEd1Reparte() != null){
				pdvComDados.setEd1Reparte(dto.getEd1Reparte());
			}
			
			if(dto.getEd2Reparte() != null){
				pdvComDados.setEd2Reparte(dto.getEd2Reparte());
			}
			
			if(dto.getEd3Reparte() != null){
				pdvComDados.setEd3Reparte(dto.getEd3Reparte());
			}
			
			if(dto.getEd4Reparte() != null){
				pdvComDados.setEd4Reparte(dto.getEd4Reparte());
			}
			
			if(dto.getEd5Reparte() != null){
				pdvComDados.setEd5Reparte(dto.getEd5Reparte());
			}
			
			if(dto.getEd6Reparte() != null){
				pdvComDados.setEd6Reparte(dto.getEd6Reparte());
			}
			
			if(dto.getEd1Venda() != null){
				pdvComDados.setEd1Venda(dto.getEd1Venda());
			}
			
			if(dto.getEd2Venda() != null){
				pdvComDados.setEd2Venda(dto.getEd2Venda());
			}
			
			if(dto.getEd3Venda() != null){
				pdvComDados.setEd3Venda(dto.getEd3Venda());
			}

			if(dto.getEd4Venda() != null){
				pdvComDados.setEd4Venda(dto.getEd4Venda());
			}
			
			if(dto.getEd5Venda() != null){
				pdvComDados.setEd5Venda(dto.getEd5Venda());
			}
			
			if(dto.getEd6Venda() != null){
				pdvComDados.setEd6Venda(dto.getEd6Venda());
			}
			
			if(dto.getProduto01() != null){
				pdvComDados.setProduto01(dto.getProduto01());
			}
			
			if(dto.getProduto02() != null){
				pdvComDados.setProduto02(dto.getProduto02());
			}
			
			if(dto.getProduto03() != null){
				pdvComDados.setProduto03(dto.getProduto03());
			}
			
			if(dto.getProduto04() != null){
				pdvComDados.setProduto04(dto.getProduto04());
			}
			
			if(dto.getProduto05() != null){
				pdvComDados.setProduto05(dto.getProduto05());
			}
			
			if(dto.getProduto06() != null){
				pdvComDados.setProduto06(dto.getProduto06());
			}
			
			listCotasComPDV.add(pdvComDados);
		}
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private TableModel configurarTableModelSemPaginacao( List listaDto, TableModel tableModel){
		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listaDto));

		tableModel.setPage(1);

		tableModel.setTotal(listaDto.size());
		
		return tableModel;
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