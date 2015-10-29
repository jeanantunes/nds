package br.com.abril.nds.controllers.nfe;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.client.vo.NfeVO;
import br.com.abril.nds.controllers.BaseController;
import br.com.abril.nds.dto.InfoNfeDTO;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.NfeDTO;
import br.com.abril.nds.dto.filtro.FiltroMonitorNfeDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.fiscal.StatusEmissaoNfe;
import br.com.abril.nds.model.fiscal.TipoDestinatario;
import br.com.abril.nds.model.fiscal.TipoEmissaoNfe;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.service.FornecedorService;
import br.com.abril.nds.service.MonitorNFEService;
import br.com.abril.nds.service.NaturezaOperacaoService;
import br.com.abril.nds.service.NotaFiscalService;
import br.com.abril.nds.service.ProcessoService;
import br.com.abril.nds.service.integracao.DistribuidorService;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.Constantes;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.export.FileExporter;
import br.com.abril.nds.util.export.FileExporter.FileType;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.abril.nds.vo.PaginacaoVO.Ordenacao;
import br.com.abril.nds.vo.ValidacaoVO;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

/**
 * 
 * Classe responsável por controlar as ações da pagina Painel Monitor NFe.
 * 
 * @author Discover Technology
 *
 */
@Resource
@Path(value="/nfe/painelMonitorNFe")
@Rules(Permissao.ROLE_NFE_PAINEL_MONITOR_NFE)
public class PainelMonitorNFEController extends BaseController {

	@Autowired
	private HttpServletResponse httpResponse;

	@Autowired
	private MonitorNFEService monitorNFEService;
	
	@Autowired
	private Result result;

	@Autowired
	private DistribuidorService distribuidorService;
	
	@Autowired
	private static HttpSession session;
	
	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	private NotaFiscalService notaFiscalService;
	
	@Autowired
	private NaturezaOperacaoService naturezaOperacaoService;
	
	private static final String LISTA_NFE = "listaNFE";
	
	private static final String NFES_PARA_IMPRESSAO_DANFES= "nfesParaImpressaoDanfes";
	
	@Autowired
	private FornecedorService fornecedorService;
	
	@Autowired
    private ProcessoService processoService;
	
	@Path("/")
	@Rules(Permissao.ROLE_NFE_PAINEL_MONITOR_NFE)
	public void index(){
		
		this.carregarComboSituacaoNfe();
		this.carregarComboTipoNfe();
		this.obterTiposDestinatarios();
		this.obterFornecedoresDestinatarios();
		this.validarTipoServico();
	}
	
	private void obterTiposDestinatarios() {
		result.include("tiposDestinatarios", new TipoDestinatario[] {TipoDestinatario.COTA, TipoDestinatario.DISTRIBUIDOR, TipoDestinatario.FORNECEDOR});
	}
	
	private void obterFornecedoresDestinatarios() {
		result.include("fornecedoresDestinatarios", fornecedorService.obterFornecedoresDestinatarios(SituacaoCadastro.ATIVO));
	}
	
	private void validarTipoServico(){
		result.include("emissor", true);
	}
	
	@Post
	@Path("/pesquisar")
	public void pesquisar(FiltroMonitorNfeDTO filtro, String sortname, String sortorder, int rp, int page) {
		
		// Paginação 
		PaginacaoVO paginacao = carregarPaginacao(sortname, sortorder, rp, page);
		
		filtro.setPaginacao(paginacao);
		
		InfoNfeDTO info = this.monitorNFEService.pesquisarNotaFiscal(filtro);
		
		List<NfeDTO> listaResultado = info.getListaNfeDTO();
		
		if (listaResultado == null || listaResultado.isEmpty()) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum registro encontrado.");
		}
		
		List<NfeVO> listaNfeVO = getListaNfeVO(listaResultado);
		
		TableModel<CellModelKeyValue<NfeVO>> tableModel = new TableModel<CellModelKeyValue<NfeVO>>();
		
		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listaNfeVO));
		tableModel.setTotal( (info.getQtdeRegistros()!= null) ? info.getQtdeRegistros() : 0);
		tableModel.setPage(1);
		
		setListaNfeToSession(tableModel.getRows());
		
		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
		
	}
	
	public void prepararDanfeUnicaImpressao(Integer lineIdImpressaoDanfe) {
		
		List<CellModelKeyValue<NfeVO>> listaNfeVO = getListaNfeFromSession();

		if(listaNfeVO == null || listaNfeVO.isEmpty()) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum item selecionado");
		}
		
		NfeVO nfeParaImpressao = null;
		
		for(CellModelKeyValue<NfeVO> cell : listaNfeVO) {
			
			if(lineIdImpressaoDanfe.intValue() == cell.getId()) {
				
				monitorNFEService.validarEmissaoDanfe(cell.getCell().getIdNotaFiscal(), false);
				
				nfeParaImpressao = cell.getCell();
				
				break;	
			}
		
		}
		
		if(nfeParaImpressao == null) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum item selecionado");
		}
		
		List<NfeVO> nfesParaImpressaoDanfes = new ArrayList<NfeVO>();
		
		nfesParaImpressaoDanfes.add(nfeParaImpressao);
		
		request.getSession().setAttribute(NFES_PARA_IMPRESSAO_DANFES, nfesParaImpressaoDanfes);
		
		result.use(Results.json()).from("OK").serialize();
		
	}
	
	public void prepararDanfesImpressao(List<Integer> listaLineIdsImpressaoDanfes, boolean indEmissaoDepec) {
		
		if(listaLineIdsImpressaoDanfes==null || listaLineIdsImpressaoDanfes.isEmpty()) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum item selecionado");
		}
		
		List<CellModelKeyValue<NfeVO>> listaNfeVO = getListaNfeFromSession();
		
		if(listaNfeVO == null || listaNfeVO.isEmpty()) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum item selecionado");
		}
		
		List<NfeVO> listaNfeParaImpressao = new ArrayList<NfeVO>();
		
		for(Integer lineId : listaLineIdsImpressaoDanfes ) {

			for(CellModelKeyValue<NfeVO> cell : listaNfeVO) {
				
				if(lineId.intValue() == cell.getId()) {
					
					monitorNFEService.validarEmissaoDanfe(cell.getCell().getIdNotaFiscal(), indEmissaoDepec);
					
					listaNfeParaImpressao.add(cell.getCell());
					
				}
				
			}
			
		}
		
		if(listaNfeParaImpressao == null || listaNfeParaImpressao.isEmpty()) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum item selecionado");
		}
		
		request.getSession().setAttribute(NFES_PARA_IMPRESSAO_DANFES, listaNfeParaImpressao);
		
		result.use(Results.json()).from("").serialize();
		
	}
	
	/**
	 * Exporta os dados da pesquisa.
	 * 
	 * @param fileType - tipo de arquivo
	 * 
	 * @throws IOException Exceção de E/S
	 */
	@SuppressWarnings("deprecation")
	@Get
	public void exportar(final FiltroMonitorNfeDTO filtro, FileType fileType) throws IOException {

		InfoNfeDTO infoNfe = monitorNFEService.pesquisarNotaFiscal(filtro);
		
		List<NfeVO> listaNfeVO = getListaNfeVO(infoNfe.getListaNfeDTO());
		
		FileExporter.to("nfe", fileType).inHTTPResponse(this.getNDSFileHeader(), filtro, null, listaNfeVO, NfeVO.class, this.httpResponse);
		httpResponse.setHeader("Set-Cookie", "fileDownload=true; path=/");
	}

	@Rules(Permissao.ROLE_NFE_PAINEL_MONITOR_NFE_ALTERACAO)
	public void cancelarNfe(final FiltroMonitorNfeDTO filtro) throws FileNotFoundException, IOException {
		
		// cancelamento de nota fiscal
		this.monitorNFEService.cancelarNfe(filtro);
		
		result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "NF-e(s) cancelada(s) com sucesso."), Constantes.PARAM_MSGS).serialize();
		
	}
	
	@Rules(Permissao.ROLE_NFE_PAINEL_MONITOR_NFE_ALTERACAO)
	public void imprimirDanfes(final boolean indEmissaoDepec) {
		
		List<NfeVO> listaNfesParaImpressaoDanfe = (List<NfeVO>) getListaGridNfeFromSession();
		
		byte[] danfeBytes = monitorNFEService.obterDanfes(listaNfesParaImpressaoDanfe, indEmissaoDepec);
		
		try {
			
			escreverArquivoParaResponse(danfeBytes, "danfe-painel-processamento");
			
		} catch(IOException e) {
			
			throw new ValidacaoException(TipoMensagem.ERROR, "Falha na geração do arquivo.");
			
		}
		
	}

	
	private void escreverArquivoParaResponse(byte[] arquivo, String nomeArquivo) throws IOException {
		
		this.httpResponse.setContentType("application/pdf");
		
		this.httpResponse.setHeader("Content-Disposition", "attachment; filename="+ nomeArquivo + "_"+ new Date() +".pdf");

		OutputStream output = this.httpResponse.getOutputStream();
		
		output.write(arquivo);

		httpResponse.getOutputStream().close();
		
		result.use(Results.nothing());
		
	}
	
	/**
	 * Obtém a descrição a partir da chave do enum TipoEmissaoNfe
	 * @param chave
	 * @return String - descricao
	 */
	private String obterDescricaoTipoEmissaoNfe(String chave) {
		if(chave  == null) {
			return "";
		}
		TipoEmissaoNfe tipoEmissaoNfe = TipoEmissaoNfe.valueOf(chave);
		return ((tipoEmissaoNfe == null) ? "" : tipoEmissaoNfe.getDescricao());
	}
	
	private List<NfeVO> getListaNfeVO( List<NfeDTO> listaNfeDTO ) {
		
		List<NfeVO> listaNfeVO = new ArrayList<NfeVO>();
		
		NfeVO nfeVO = null;
		
		String cnpjDestinatario 	= null;
		String cpfDestinatario 		= null;

		String cnpjRemetente 		= null;
		String cpfRemetente 		= null;
		
		String emissao 				= null;
		String tipoEmissao			= null;
		String movimentoIntegracao 	= null;
		Long numero 				= null;
		String serie 				= null;
		String statusNfe 			= null;
		String tipoNfe 				= null;
		
		for(NfeDTO nfeDTO : listaNfeDTO) {
			
			cnpjDestinatario 		= (nfeDTO.getCnpjDestinatario() == null) ? "-" : nfeDTO.getCnpjDestinatario();
			cpfDestinatario			= (nfeDTO.getCpfDestinatario() == null) ? "-" : nfeDTO.getCpfDestinatario();
			
			cnpjRemetente			= (nfeDTO.getCnpjRemetente() == null) ? "-" : nfeDTO.getCnpjRemetente();
			cpfRemetente			= (nfeDTO.getCpfRemetente() == null) ? "-" :  nfeDTO.getCpfRemetente();
			
			emissao 				= DateUtil.formatarDataPTBR(nfeDTO.getEmissao());
			tipoEmissao				= obterDescricaoTipoEmissaoNfe(nfeDTO.getTipoEmissao().name());
			movimentoIntegracao 	= nfeDTO.getMovimentoIntegracao();
			numero 					= nfeDTO.getNumero();
			serie 					= nfeDTO.getSerie().toString();
			statusNfe 				= nfeDTO.getStatusNfe().name();
			tipoNfe 				= nfeDTO.getTipoNfe();
			
			nfeVO = new NfeVO();
			
			// nfeVO.setTipoOperacao(nfeDTO.getTipoNfe());
			
			nfeVO.setIdNotaFiscal(nfeDTO.getIdNotaFiscal());
			
			nfeVO.setCnpjDestinatario(cnpjDestinatario);
			nfeVO.setCpfDestinatario(cpfDestinatario);
			
			nfeVO.setCnpjRemetente(cnpjRemetente);
			nfeVO.setCpfRemetente(cpfRemetente);
			
			nfeVO.setEmissao(emissao);
			nfeVO.setTipoEmissao(tipoEmissao);
			nfeVO.setMovimentoIntegracao(movimentoIntegracao);
			nfeVO.setNumero(numero);
			nfeVO.setSerie(serie);
			nfeVO.setStatusNfe(statusNfe);
			nfeVO.setTipoNfe(tipoNfe);
			
			listaNfeVO.add(nfeVO);
			
		}
		
		return listaNfeVO;
	}	
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void carregarComboSituacaoNfe() {
		
		List<ItemDTO<String, String>> comboStatusNfe = new ArrayList<ItemDTO<String, String>>();
		
		for(StatusEmissaoNfe statusEmissaoNfe: StatusEmissaoNfe.values()) {
			comboStatusNfe.add(new ItemDTO(statusEmissaoNfe.name(), statusEmissaoNfe.getDescricao()));
		}
		
		result.include("comboStatusNfe", comboStatusNfe);
		
	}
	
	private void carregarComboTipoNfe() {
	    String parametros[] = {"ENVIO", "ENTRADA_ENCALHE", "FALTA_REPARTE", "FALTA_ENCALHE", "SOBRA_REPARTE", "SOBRA_ENCALHE", "VENDA", "DEVOLUCAO_AO_FORNECEDOR"};
		List<ItemDTO<String, String>> comboTipoNfe = processoService.buscarProcessos(parametros);
		result.include("comboTipoNfe", comboTipoNfe);
	}
	
	private PaginacaoVO carregarPaginacao(String sortname, String sortorder, int rp, int page) {
		PaginacaoVO paginacao = new PaginacaoVO();
		paginacao.setOrdenacao(sortorder.equalsIgnoreCase(Ordenacao.ASC.name()) ? Ordenacao.ASC : Ordenacao.DESC);
	    paginacao.setPaginaAtual(page);
	    paginacao.setQtdResultadosPorPagina(rp);
	    paginacao.setSortOrder(sortorder);
	    paginacao.setSortColumn(sortname);
		return paginacao;
	}
	
	@SuppressWarnings("unchecked")
	public List<CellModelKeyValue<NfeVO>> getListaNfeFromSession() {
		return (List<CellModelKeyValue<NfeVO>>) request.getSession().getAttribute(LISTA_NFE);
	}

	public void setListaNfeToSession(List<CellModelKeyValue<NfeVO>> listaNfe) {
		request.getSession().setAttribute(LISTA_NFE, listaNfe);
	}
	
	public List<NfeVO> getListaGridNfeFromSession() {
		return (List<NfeVO>) request.getSession().getAttribute(NFES_PARA_IMPRESSAO_DANFES);
	}

	public void setListaGridNfeToSession(List<NfeVO> listaNfe) {
		request.getSession().setAttribute(NFES_PARA_IMPRESSAO_DANFES, listaNfe);
	}
}