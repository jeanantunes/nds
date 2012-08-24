package br.com.abril.nds.controllers.nfe;

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
import br.com.abril.nds.dto.InfoNfeDTO;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.NfeDTO;
import br.com.abril.nds.dto.filtro.FiltroMonitorNfeDTO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.integracao.service.DistribuidorService;
import br.com.abril.nds.model.aprovacao.StatusAprovacao;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.fiscal.StatusEmissaoNfe;
import br.com.abril.nds.model.fiscal.StatusEmissaoNotaFiscal;
import br.com.abril.nds.model.fiscal.TipoEmissaoNfe;
import br.com.abril.nds.model.fiscal.TipoOperacao;
import br.com.abril.nds.model.fiscal.nota.Status;
import br.com.abril.nds.model.fiscal.nota.StatusProcessamentoInterno;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.service.MonitorNFEService;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.TipoMensagem;
import br.com.abril.nds.util.Util;
import br.com.abril.nds.util.export.FileExporter;
import br.com.abril.nds.util.export.FileExporter.FileType;
import br.com.abril.nds.util.export.NDSFileHeader;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.abril.nds.vo.ValidacaoVO;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
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
public class PainelMonitorNFEController {

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
	
	private static final String TIPO_DOCUMENTO_CPF = "cpf";
	
	private static final String FILTRO_SESSION_ATTRIBUTE = "filtroPesquisaNfe";
	
	private static final String NFES_PARA_IMPRESSAO_DANFES= "nfesParaImpressaoDanfes";
	
	@Path("/")
	@Rules(Permissao.ROLE_NFE_PAINEL_MONITOR_NFE)
	public void index(){
		
		carregarComboSituacaoNfe();
		
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
	
	private List<String> validarCampos(String dataInicial, String dataFinal) {
		
		List<String> mensagens = new ArrayList<String>();
		
		if(dataInicial != null && !dataInicial.trim().isEmpty()) {
			validarFormatoData(mensagens, dataInicial, "Período de");
		}
		
		if(dataFinal != null && !dataFinal.trim().isEmpty()) {
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
	@Get
	public void exportar(FileType fileType) throws IOException {

		FiltroMonitorNfeDTO filtroMonitorNfeDTO = obterFiltroExportacao();

		InfoNfeDTO infoNfe = monitorNFEService.pesquisarNFe(filtroMonitorNfeDTO);

		List<NfeVO> listaNfeVO =  getListaNfeVO(infoNfe.getListaNfeDTO());
		
		FileExporter.to("nfe", fileType).inHTTPResponse(
				this.getNDSFileHeader(), 
				filtroMonitorNfeDTO, 
				null, 
				listaNfeVO,
				NfeVO.class, this.httpResponse);
		
	}

	public void cancelarNfe() {

		result.use(Results.json()).from("").serialize();
		
	}
	
	/*
	 * Obtém o filtro para exportação.
	 */
	private FiltroMonitorNfeDTO obterFiltroExportacao() {
		
		FiltroMonitorNfeDTO filtro = 
				(FiltroMonitorNfeDTO) this.session.getAttribute(FILTRO_SESSION_ATTRIBUTE);
		
		if (filtro != null) {
			
			if (filtro.getPaginacao() != null) {
				
				filtro.getPaginacao().setPaginaAtual(null);
				filtro.getPaginacao().setQtdResultadosPorPagina(null);
			}
			
		}
		
		return filtro;
	}
	
	/*
	 * Obtém os dados do cabeçalho de exportação.
	 * 
	 * @return NDSFileHeader
	 */
	private NDSFileHeader getNDSFileHeader() {
		
		NDSFileHeader ndsFileHeader = new NDSFileHeader();
		
		Distribuidor distribuidor = this.distribuidorService.obter();
		
		if (distribuidor != null) {
			
			ndsFileHeader.setNomeDistribuidor(distribuidor.getJuridica().getRazaoSocial());
			ndsFileHeader.setCnpjDistribuidor(distribuidor.getJuridica().getCnpj());
		}
		
		ndsFileHeader.setData(new Date());
		
		ndsFileHeader.setNomeUsuario(this.getUsuario().getNome());
		
		return ndsFileHeader;
	}
	
	//TODO: não há como reconhecer usuario, ainda
	private Usuario getUsuario() {
		
		Usuario usuario = new Usuario();
		
		usuario.setId(1L);
		
		usuario.setNome("Jornaleiro da Silva");
		
		return usuario;
	}
	
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
			tipoEmissao				= obterDescricaoTipoEmissaoNfe(nfeDTO.getTipoEmissao());
			movimentoIntegracao 	= nfeDTO.getMovimentoIntegracao();
			numero 					= nfeDTO.getNumero();
			serie 					= nfeDTO.getSerie();
			statusNfe 				= obterDescricaoStatusEmissaoNfe(nfeDTO.getStatusNfe());
			tipoNfe 				= obterDescricaoTipoOperacao(nfeDTO.getTipoNfe());
			
			nfeVO = new NfeVO();
			
			nfeVO.setTipoOperacao(TipoOperacao.valueOf(nfeDTO.getTipoNfe()));
			
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
		
		
		comboStatusNfe.add(new ItemDTO(StatusEmissaoNfe.AGUARDANDO_PROCESSAMENTO.name(), StatusEmissaoNfe.AGUARDANDO_PROCESSAMENTO.getDescricao()));
		comboStatusNfe.add(new ItemDTO(StatusEmissaoNfe.EM_PROCESSAMENTO.name(), StatusEmissaoNfe.EM_PROCESSAMENTO.getDescricao()));
		comboStatusNfe.add(new ItemDTO(StatusEmissaoNfe.PROCESSAMENTO_REJEITADO.name(), StatusEmissaoNfe.PROCESSAMENTO_REJEITADO.getDescricao()));
		comboStatusNfe.add(new ItemDTO(StatusEmissaoNfe.AGUARDANDO_ACAO_DO_USUARIO.name(), StatusEmissaoNfe.AGUARDANDO_ACAO_DO_USUARIO.getDescricao()));
		comboStatusNfe.add(new ItemDTO(StatusEmissaoNfe.NFE_AUTORIZADA.name(), StatusEmissaoNfe.NFE_AUTORIZADA.getDescricao()));
		comboStatusNfe.add(new ItemDTO(StatusEmissaoNfe.NFE_REJEITADA.name(), StatusEmissaoNfe.NFE_REJEITADA.getDescricao()));
		comboStatusNfe.add(new ItemDTO(StatusEmissaoNfe.NFE_DENEGADA.name(), StatusEmissaoNfe.NFE_DENEGADA.getDescricao()));

		result.include("comboStatusNfe", comboStatusNfe);
		
	}
	
	
	@Path("/pesquisar")
	public void pesquisar(
			String tipoDocumento,
			Integer box,
			String dataInicial,
			String dataFinal,
			String documento,
			String tipoNfe,
			Long numeroInicial,
			Long numeroFinal,
			String chaveAcesso,
			String situacaoNfe,
			Integer serieNfe,
			String sortorder, 
			String sortname, 
			int page, 
			int rp
			) {

		List<String> mensagens = validarCampos(dataInicial, dataFinal);
		
		tratarErro(mensagens);
		
		FiltroMonitorNfeDTO filtroMonitorNfeDTO = new FiltroMonitorNfeDTO();
		
		filtroMonitorNfeDTO.setBox(box);
		filtroMonitorNfeDTO.setChaveAcesso(chaveAcesso);
		filtroMonitorNfeDTO.setDataInicial(DateUtil.parseData(dataInicial, "dd/MM/yyyy"));
		filtroMonitorNfeDTO.setDataFinal(DateUtil.parseData(dataFinal, "dd/MM/yyyy"));
		filtroMonitorNfeDTO.setDocumentoPessoa(documento);

		filtroMonitorNfeDTO.setNumeroNotaInicial(numeroInicial);
		
		filtroMonitorNfeDTO.setNumeroNotaFinal(numeroFinal);
		
		filtroMonitorNfeDTO.setSituacaoNfe(situacaoNfe);
		
		filtroMonitorNfeDTO.setSerie(serieNfe);
		
		filtroMonitorNfeDTO.setTipoNfe(tipoNfe);
		filtroMonitorNfeDTO.setIndDocumentoCPF(TIPO_DOCUMENTO_CPF.equals(tipoDocumento));
		
		configurarPaginacaoPesquisa(filtroMonitorNfeDTO, sortorder, sortname, page, rp);
		
		tratarFiltro(filtroMonitorNfeDTO);
		
		InfoNfeDTO info = monitorNFEService.pesquisarNFe(filtroMonitorNfeDTO);
		
		List<NfeDTO> listaResultado = info.getListaNfeDTO();
		
		Integer quantidadeRegistros = info.getQtdeRegistros();
		
		if (listaResultado == null || listaResultado.isEmpty()) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum registro encontrado.");
		}
		
		List<NfeVO> listaNfeVO = getListaNfeVO(listaResultado);
		
		TableModel<CellModelKeyValue<NfeVO>> tableModel = new TableModel<CellModelKeyValue<NfeVO>>();
		
		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listaNfeVO));
		tableModel.setTotal( (quantidadeRegistros!= null) ? quantidadeRegistros : 0);
		tableModel.setPage(filtroMonitorNfeDTO.getPaginacao().getPaginaAtual());
		
		setListaNfeToSession(tableModel.getRows());
		
		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
		
	}
	
	@SuppressWarnings("unchecked")
	public List<CellModelKeyValue<NfeVO>> getListaNfeFromSession() {
		return (List<CellModelKeyValue<NfeVO>>) request.getSession().getAttribute(LISTA_NFE);
	}

	public void setListaNfeToSession(List<CellModelKeyValue<NfeVO>> listaNfe) {
		request.getSession().setAttribute(LISTA_NFE, listaNfe);
	}
	
}
