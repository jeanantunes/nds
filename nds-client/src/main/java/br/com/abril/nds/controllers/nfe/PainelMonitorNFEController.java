package br.com.abril.nds.controllers.nfe;

import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
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
import br.com.abril.nds.model.cadastro.Processo;
import br.com.abril.nds.model.fiscal.StatusEmissaoNfe;
import br.com.abril.nds.model.fiscal.TipoEmissaoNfe;
import br.com.abril.nds.model.fiscal.TipoOperacao;
import br.com.abril.nds.model.fiscal.nota.Status;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.service.MonitorNFEService;
import br.com.abril.nds.service.integracao.DistribuidorService;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.Util;
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
	private HttpSession session;
	
	@Autowired
	private HttpServletRequest request;
	
	private static final String LISTA_NFE = "listaNFE";
	
	// private static final String TIPO_DOCUMENTO_CPF = "cpf";
	
	private static final String FILTRO_SESSION_ATTRIBUTE = "filtroPesquisaNfe";
	
	private static final String NFES_PARA_IMPRESSAO_DANFES= "nfesParaImpressaoDanfes";
	
	@Path("/")
	public void index(){
		
		carregarComboSituacaoNfe();
		carregarComboTipoNfe();
		
	}
	
	private void validarFormatoData(List<String> mensagens, String field, String label){
		
		if (!DateUtil.isValidDate(field, "dd/MM/yyyy")) {
			mensagens.add("O campo " + label + " é inválido");
		} 
		
	}
	
	/**
	 * Trata mensagens de erro, caso tenha mensagem lança exceção de erro.
	 * 
	 * @param mensagensErro
	 */
	private void tratarErro(List<String> mensagensErro){
		
		ValidacaoVO validacao = new ValidacaoVO();
		
		validacao.setTipoMensagem(TipoMensagem.ERROR);
		
		if(!mensagensErro.isEmpty()) {
			
			validacao.setListaMensagens(mensagensErro);
			
			throw new ValidacaoException(validacao);
			
		}
	}
	
	private List<String> validarCampos(Date de, Date ate) {
		SimpleDateFormat formatar = new SimpleDateFormat();
		List<String> mensagens = new ArrayList<String>();
		
		if(de != null && !"".equals(de)) {
			
			String dataInicial = formatar.format(de);
			validarFormatoData(mensagens, dataInicial, "Período de");
		}
		
		if(ate != null && !"".equals(ate)) {
			String dataFinal = formatar.format(ate);
			validarFormatoData(mensagens, dataFinal, "Até");
		}		
		
		return mensagens;

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
		
		session.setAttribute(NFES_PARA_IMPRESSAO_DANFES, nfesParaImpressaoDanfes);
		
		result.use(Results.json()).from("").serialize();
		
	}
	
	public void prepararDanfesImpressao(List<Integer> listaLineIdsImpressaoDanfes, boolean indEmissaoDepec) {
		
		if(listaLineIdsImpressaoDanfes==null ||
				listaLineIdsImpressaoDanfes.isEmpty()) {
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
		
		session.setAttribute(NFES_PARA_IMPRESSAO_DANFES, listaNfeParaImpressao);
		
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
	public void exportar(FileType fileType) throws IOException {

		FiltroMonitorNfeDTO filtroMonitorNfeDTO = obterFiltroExportacao();

		InfoNfeDTO infoNfe = monitorNFEService.pesquisarNFe(filtroMonitorNfeDTO);
		
		List<NfeVO> listaNfeVO =  getListaNfeVO(infoNfe.getListaNfeDTO());
		
		FileExporter.to("nfe", fileType).inHTTPResponse(this.getNDSFileHeader(), 
				filtroMonitorNfeDTO, 
				null, 
				listaNfeVO,
				NfeVO.class, this.httpResponse);
		
	}

	@Rules(Permissao.ROLE_NFE_PAINEL_MONITOR_NFE_ALTERACAO)
	public void cancelarNfe() {
		
		// cancelamento de nota fiscal
		
		
		
		result.use(Results.json()).from("").serialize();
		
	}
	
	/*
	 * Obtém o filtro para exportação.
	 */
	private FiltroMonitorNfeDTO obterFiltroExportacao() {
		
		FiltroMonitorNfeDTO filtro = (FiltroMonitorNfeDTO) this.session.getAttribute(FILTRO_SESSION_ATTRIBUTE);
		
		if (filtro != null) {
			
			if (filtro.getPaginacao() != null) {
				
				filtro.getPaginacao().setPaginaAtual(null);
				filtro.getPaginacao().setQtdResultadosPorPagina(null);
			}
			
		}
		
		return filtro;
	}
	

	@SuppressWarnings("unchecked")
	@Rules(Permissao.ROLE_NFE_PAINEL_MONITOR_NFE_ALTERACAO)
	public void imprimirDanfes(boolean indEmissaoDepec) {
		
		List<NfeVO> listaNfesParaImpressaoDanfe = (List<NfeVO>) session.getAttribute(NFES_PARA_IMPRESSAO_DANFES);
		
		session.setAttribute(NFES_PARA_IMPRESSAO_DANFES, null);
		
		byte[] danfeBytes = monitorNFEService.obterDanfes(listaNfesParaImpressaoDanfe, indEmissaoDepec);
		
		try {
			
			escreverArquivoParaResponse(danfeBytes, "danfes");
			
		} catch(IOException e) {
			
			throw new ValidacaoException(TipoMensagem.ERROR, "Falha na geração do arquivo.");
			
		}
		
	}

	
	private void escreverArquivoParaResponse(byte[] arquivo, String nomeArquivo) throws IOException {
		
		this.httpResponse.setContentType("application/pdf");
		
		this.httpResponse.setHeader("Content-Disposition", "attachment; filename="+nomeArquivo +".pdf");

		OutputStream output = this.httpResponse.getOutputStream();
		
		output.write(arquivo);

		httpResponse.getOutputStream().close();
		
		result.use(Results.nothing());
		
	}

	/**
	 * Configura paginação da lista de resultados
	 * 
	 * @param filtro
	 * @param sortorder
	 * @param sortname
	 * @param page
	 * @param rp
	 */
	private void configurarPaginacaoPesquisa(FiltroMonitorNfeDTO filtro, 
											String sortorder, String sortname,
											int page, int rp) {

		if (filtro != null) {

			PaginacaoVO paginacao = new PaginacaoVO(page, rp, sortorder);

			filtro.setPaginacao(paginacao);

			filtro.setOrdenacaoColuna((Util.getEnumByStringValue(FiltroMonitorNfeDTO.OrdenacaoColuna.values(),sortname)));
		}
	}
	
	/**
	 * Executa tratamento de paginação em função de alteração do filtro de pesquisa.
	 * 
	 * @param filtro
	 */
	private void tratarFiltro(FiltroMonitorNfeDTO filtro) {

		FiltroMonitorNfeDTO filtroSession = 
				(FiltroMonitorNfeDTO) session.getAttribute(FILTRO_SESSION_ATTRIBUTE);
		
		if (filtroSession != null && !filtroSession.equals(filtro)) {

			filtroSession.getPaginacao().setPaginaAtual(1);
		}
		
		session.setAttribute(FILTRO_SESSION_ATTRIBUTE, filtro);
	}
	
	
	
	/**
	 * Obtém a descrição a partir da chave do enum TipoEmissaoNfe
	 * 
	 * @param chave
	 * 
	 * @return String - descricao
	 */
	private String obterDescricaoTipoEmissaoNfe(String chave) {
		if(chave  == null) {
			return "";
		}
		TipoEmissaoNfe tipoEmissaoNfe = TipoEmissaoNfe.valueOf(chave);
		return ((tipoEmissaoNfe == null) ? "" : tipoEmissaoNfe.getDescricao());
	}

	
	/**
	 * Obtém a descrição a partir da chave do enum Status
	 * 
	 * @param chave
	 * 
	 * @return String - descricao
	 */
	private String obterDescricaoStatusEmissaoNfe(String chave) {
		
		if(chave  == null) {
			return "";
		}
		
		Status status = Status.valueOf(chave);
		
		return ((status == null) ? "" : status.getDescricao());
		
	}

	

	/**
	 * Obtém a descrição a partir da chave do enum TipoOperacao
	 * 
	 * @param chave
	 * 
	 * @return String - descricao
	 */
	private String obterDescricaoTipoOperacao(String chave) {
		if(chave  == null) {
			return "";
		}
		TipoOperacao tipoOperacao = TipoOperacao.valueOf(chave);
		return ((tipoOperacao == null) ? "" : tipoOperacao.getDescricao());
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
		
//		comboStatusNfe.add(new ItemDTO(StatusEmissaoNfe.AGUARDANDO_PROCESSAMENTO.name(), StatusEmissaoNfe.AGUARDANDO_PROCESSAMENTO.getDescricao()));
//		comboStatusNfe.add(new ItemDTO(StatusEmissaoNfe.EM_PROCESSAMENTO.name(), StatusEmissaoNfe.EM_PROCESSAMENTO.getDescricao()));
//		comboStatusNfe.add(new ItemDTO(StatusEmissaoNfe.PROCESSAMENTO_REJEITADO.name(), StatusEmissaoNfe.PROCESSAMENTO_REJEITADO.getDescricao()));
//		comboStatusNfe.add(new ItemDTO(StatusEmissaoNfe.AGUARDANDO_ACAO_DO_USUARIO.name(), StatusEmissaoNfe.AGUARDANDO_ACAO_DO_USUARIO.getDescricao()));
//		comboStatusNfe.add(new ItemDTO(StatusEmissaoNfe.NFE_AUTORIZADA.name(), StatusEmissaoNfe.NFE_AUTORIZADA.getDescricao()));
//		comboStatusNfe.add(new ItemDTO(StatusEmissaoNfe.NFE_REJEITADA.name(), StatusEmissaoNfe.NFE_REJEITADA.getDescricao()));
//		comboStatusNfe.add(new ItemDTO(StatusEmissaoNfe.NFE_DENEGADA.name(), StatusEmissaoNfe.NFE_DENEGADA.getDescricao()));

		result.include("comboStatusNfe", comboStatusNfe);
		
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void carregarComboTipoNfe() {
		
		List<ItemDTO<String, String>> comboTipoNfe = new ArrayList<ItemDTO<String, String>>();

		comboTipoNfe.add(new ItemDTO(Processo.ENVIO.name(), Processo.ENVIO.getDescricao()));
		comboTipoNfe.add(new ItemDTO(Processo.ENTRADA_ENCALHE.name(), Processo.ENTRADA_ENCALHE.getDescricao()));
		comboTipoNfe.add(new ItemDTO(Processo.FALTA_REPARTE.name(), Processo.FALTA_REPARTE.getDescricao()));
		comboTipoNfe.add(new ItemDTO(Processo.FALTA_ENCALHE.name(), Processo.FALTA_ENCALHE.getDescricao()));
		comboTipoNfe.add(new ItemDTO(Processo.SOBRA_REPARTE.name(), Processo.SOBRA_REPARTE.getDescricao()));
		comboTipoNfe.add(new ItemDTO(Processo.SOBRA_ENCALHE.name(), Processo.SOBRA_ENCALHE.getDescricao()));
		comboTipoNfe.add(new ItemDTO(Processo.VENDA.name(), Processo.VENDA.getDescricao()));
		comboTipoNfe.add(new ItemDTO(Processo.DEVOLUCAO_AO_FORNECEDOR.name(), Processo.DEVOLUCAO_AO_FORNECEDOR.getDescricao()));
		comboTipoNfe.add(new ItemDTO(Processo.CALCELADA.name(), Processo.CALCELADA.getDescricao()));

		result.include("comboTipoNfe", comboTipoNfe);
	}
	
	@Post
	@Path("/pesquisar")
	public void pesquisar(FiltroMonitorNfeDTO filtro, String sortname, String sortorder, int rp, int page) {
		
		List<String> mensagens = validarCampos(filtro.getDataInicial(), filtro.getDataFinal());
		tratarErro(mensagens);
		
		// Paginação 
		PaginacaoVO paginacao = carregarPaginacao(sortname, sortorder, rp, page);
		filtro.setPaginacao(paginacao);
		
		InfoNfeDTO info = monitorNFEService.pesquisarNFe(filtro);
		
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
	
	private PaginacaoVO carregarPaginacao(String sortname, String sortorder, int rp, int page) {
		PaginacaoVO paginacao = new PaginacaoVO();
		paginacao.setOrdenacao(Ordenacao.ASC);
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
	
}
