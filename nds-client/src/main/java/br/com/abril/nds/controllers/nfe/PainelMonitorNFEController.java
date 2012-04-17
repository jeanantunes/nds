package br.com.abril.nds.controllers.nfe;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.vo.NfeVO;
import br.com.abril.nds.client.vo.ValidacaoVO;
import br.com.abril.nds.dto.RecebimentoFisicoDTO;
import br.com.abril.nds.dto.filtro.FiltroMonitorNfeDTO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.service.DistribuidorService;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.TipoMensagem;
import br.com.abril.nds.util.export.FileExporter;
import br.com.abril.nds.util.export.FileExporter.FileType;
import br.com.abril.nds.util.export.NDSFileHeader;
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
	private Result result;

	@Autowired
	private DistribuidorService distribuidorService;
	
	@Autowired
	private HttpServletRequest request;
	
	private static final String LISTA_NFE = "listaNFE";
	
	@Path("/")
	public void index(){
		
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
	
	/**
	 * Valida o preenchimento obrigatório do campo informado.
	 */
	private void validarPreenchimentoObrigatorio(List<String> mensagens, String field, String label){
		
		if (field == null || field.isEmpty()) {
			mensagens.add(" O preenchimento do campo " + field + " é obrigatório ");
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
	
	public void imprimirDanfeUnica(Integer lineIdImpressaoDanfe) {
		
		List<CellModelKeyValue<NfeVO>> listaNfeVO = getListaNfeFromSession();

		NfeVO nfeParaImpressao = null;
		
		for(CellModelKeyValue<NfeVO> cell : listaNfeVO) {
			
			if(lineIdImpressaoDanfe.intValue() == cell.getId()) {
				
				nfeParaImpressao = cell.getCell();
				
				break;	
			}
		
		}
		
		//TODO imprimir a listaNfeParaImpressao...
		
	}
	
	public void imprimirDanfes(List<Integer> listaLineIdsImpressaoDanfes) {
		
		
		if(listaLineIdsImpressaoDanfes==null ||
				!listaLineIdsImpressaoDanfes.isEmpty()) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum item selecionado");
			
		}
		
		List<CellModelKeyValue<NfeVO>> listaNfeVO = getListaNfeFromSession();
		
		List<NfeVO> listaNfeParaImpressao = new ArrayList<NfeVO>();
		
		for(Integer lineId : listaLineIdsImpressaoDanfes ) {

			for(CellModelKeyValue<NfeVO> cell : listaNfeVO) {
				
				if(lineId.intValue() == cell.getId()) {
					
					listaNfeParaImpressao.add(cell.getCell());
					
				}
				
			}
			
		}
		
		//TODO imprimir a listaNfeParaImpressao...
		
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

		FiltroMonitorNfeDTO filtroMonitorNfeDTO = null;

		List<NfeVO> listaNfeVO =  null;
		
		FileExporter.to("painel-monitor-nfe", fileType).inHTTPResponse(
				this.getNDSFileHeader(), 
				filtroMonitorNfeDTO, 
				null, 
				listaNfeVO,
				NfeVO.class, this.httpResponse);
		
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
	
	/**
	 * Renderiza o arquivo de impressão de dividas
	 * @param filtro
	 * @throws IOException
	 */
	private void imprimirDanfes(byte[] arquivo, String nomeArquivo) throws IOException {
		
		this.httpResponse.setContentType("application/pdf");
		
		this.httpResponse.setHeader("Content-Disposition", "attachment; filename="+nomeArquivo +".pdf");

		OutputStream output = this.httpResponse.getOutputStream();
		
		output.write(arquivo);

		httpResponse.getOutputStream().close();
		
		result.use(Results.nothing());
		
	}
	
	private TableModel getTableModelDataMock() {
		
		TableModel<CellModelKeyValue<NfeVO>> tableModel = new TableModel<CellModelKeyValue<NfeVO>>();
		
		List<NfeVO> lista = new ArrayList<NfeVO>();
		
		int contador = 0;
		
		while(contador++<10) {
			
			NfeVO nfe = new NfeVO();
			
			nfe.setCnpjDestinatario("000000000"+contador);
			nfe.setCnpjEmissor("0000000"+contador);
			nfe.setEmissao("01/02/2012");
			nfe.setMovimentoIntegracao("movimento_");
			nfe.setNumero("000"+contador);
			nfe.setSerie("000"+contador);
			nfe.setStatusNfe("status_");
			nfe.setTipoNfe("tiponfe_");
			
			lista.add(nfe);
			
		}

		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(lista));
		tableModel.setTotal(lista.size());
		tableModel.setPage(1);
		
		return tableModel;
		
	}
	
	
	@Path("/pesquisar")
	public void pesquisar(
			String box,
			String dataInicial,
			String dataFinal,
			String cpf,
			String cnpj,
			String documento,
			String tipoNfe,
			String numeroInicial,
			String numeroFinal,
			String chaveAcesso,
			String situacaoNfe
			) {

		List<String> mensagens = validarCampos(dataInicial, dataFinal);
		
		tratarErro(mensagens);
		
		FiltroMonitorNfeDTO filtroMonitorNfeDTO = new FiltroMonitorNfeDTO();
		
		filtroMonitorNfeDTO.setBox(box);
		filtroMonitorNfeDTO.setChaveAcesso(chaveAcesso);
		filtroMonitorNfeDTO.setDataInicial(DateUtil.parseData(dataInicial, "dd/MM/yyyy"));
		filtroMonitorNfeDTO.setDataFinal(DateUtil.parseData(dataFinal, "dd/MM/yyyy"));
		filtroMonitorNfeDTO.setDestinatario(documento);
		filtroMonitorNfeDTO.setNumeroNotaFinal(numeroInicial);
		filtroMonitorNfeDTO.setNumeroNotaInicial(numeroFinal);
		filtroMonitorNfeDTO.setSituacaoNfe(situacaoNfe);
		filtroMonitorNfeDTO.setTipoNfe(tipoNfe);
		
		System.out.println(box);
		System.out.println(dataInicial);
		System.out.println(dataFinal);
		System.out.println(cpf);
		System.out.println(cnpj);
		System.out.println(documento);
		System.out.println(tipoNfe);
		System.out.println(numeroInicial);
		System.out.println(numeroFinal);
		System.out.println(chaveAcesso);
		System.out.println(situacaoNfe);
		
		TableModel<CellModelKeyValue<NfeVO>> tableModel = getTableModelDataMock();
		
		setListaNfeToSession(tableModel.getRows());
		
		result.use(Results.json()).from(tableModel).recursive().serialize();
		
	}
	
	@SuppressWarnings("unchecked")
	public List<CellModelKeyValue<NfeVO>> getListaNfeFromSession() {
		return (List<CellModelKeyValue<NfeVO>>) request.getSession().getAttribute(LISTA_NFE);
	}

	public void setListaNfeToSession(List<CellModelKeyValue<NfeVO>> listaNfe) {
		request.getSession().setAttribute(LISTA_NFE, listaNfe);
	}
	
}
