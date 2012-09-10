package br.com.abril.nds.controllers.cadastro;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.vo.ContratoVO;
import br.com.abril.nds.client.vo.ParametrosDistribuidorVO;
import br.com.abril.nds.dto.FormaCobrancaDTO;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.ParametroCobrancaCotaDTO;
import br.com.abril.nds.dto.ParametroCobrancaDTO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.FormaCobranca;
import br.com.abril.nds.model.cadastro.PoliticaCobranca;
import br.com.abril.nds.model.cadastro.TipoCobranca;
import br.com.abril.nds.model.cadastro.TipoCota;
import br.com.abril.nds.model.cadastro.TipoFormaCobranca;
import br.com.abril.nds.serialization.custom.CustomMapJson;
import br.com.abril.nds.serialization.custom.PlainJSONSerialization;
import br.com.abril.nds.service.BancoService;
import br.com.abril.nds.service.CotaService;
import br.com.abril.nds.service.EntregadorService;
import br.com.abril.nds.service.FileService;
import br.com.abril.nds.service.ParametroCobrancaCotaService;
import br.com.abril.nds.service.ParametrosDistribuidorService;
import br.com.abril.nds.service.PoliticaCobrancaService;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.Constantes;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.TipoMensagem;
import br.com.abril.nds.util.export.FileExporter.FileType;
import br.com.abril.nds.vo.ValidacaoVO;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.Validator;
import br.com.caelum.vraptor.interceptor.multipart.UploadedFile;
import br.com.caelum.vraptor.validator.Message;
import br.com.caelum.vraptor.view.Results;

@Resource
@Path("/cota/parametroCobrancaCota")
public class ParametroCobrancaCotaController {
	
	private Result result;

	@Autowired
	private ParametroCobrancaCotaService parametroCobrancaCotaService;
	
	@Autowired
	private PoliticaCobrancaService politicaCobrancaService;
	
	@Autowired
	private BancoService bancoService;
	
	@Autowired
	private CotaService cotaService;
	
	@Autowired
	private ParametrosDistribuidorService parametroDistribuidorService;
	
	@Autowired
	private Validator validator;	
	
	@Autowired
	private FileService fileService;
	
	@Autowired
	private EntregadorService entregadorService;
	
	@Autowired
	private ServletContext servletContext;
	
	@Autowired
	private HttpSession session;
    
    private HttpServletResponse httpResponse;
	
	private static List<ItemDTO<Integer,String>> listaBancos =  new ArrayList<ItemDTO<Integer,String>>();

    private static List<ItemDTO<TipoCobranca,String>> listaTiposCobranca =  new ArrayList<ItemDTO<TipoCobranca,String>>();
     
    private static List<ItemDTO<Long,String>> listaFornecedores =  new ArrayList<ItemDTO<Long,String>>();
    
    private static List<ItemDTO<TipoCota,String>> listaTiposCota =  new ArrayList<ItemDTO<TipoCota,String>>();
    
    public static final FileType[] extensoesAceitas = {FileType.DOC, FileType.DOCX, FileType.BMP, 
    												   FileType.GIF, FileType.PDF, FileType.JPEG, 
    												   FileType.JPG, FileType.PNG};
    
    
    /**
	 * Constante que representa o nome do atributo com os dados de 'cota cobranca'
	 * armazenado na sessão para serem persistidos na base. 
	 */
	public static String ATRIBUTO_SESSAO_FINANCEIRO_SALVAR = "financeiroSalvarSessao";
	
	private static final String CONTRATO_UPLOADED = "contratoUploaded";
	
	
	/**
	 * Construtor da classe
	 * @param result
	 * @param session
	 * @param httpResponse
	 */
	public ParametroCobrancaCotaController(Result result, HttpServletResponse httpResponse) {
		this.result = result;
		this.httpResponse = httpResponse;
	}
	
	
	/**
	 * Método de chamada da página
	 * Pré-carrega itens da pagina com informações default.
	 */
	@Get
	public void index() {
		
	}
	
	
	/**
	 * Método de Pré-carregamento de itens da pagina com informações default.
	 */
	public void preCarregamento(){
		listaBancos = this.bancoService.getComboBancos();
		listaTiposCobranca = this.parametroCobrancaCotaService.getComboTiposCobranca();
		listaTiposCota = this.cotaService.getComboTiposCota();
		result.include("listaBancos",listaBancos);
		result.include("listaTiposCobranca",listaTiposCobranca);
		result.include("listaTiposCota",listaTiposCota);
	}

	
	/**
	 * Método de Pré-carregamento de fornecedores relacionados com a Cota.
	 * @param idCota
	 */
	@Post
	@Path("/fornecedoresCota")
	public void fornecedoresCota(Long idCota){
		listaFornecedores = this.parametroCobrancaCotaService.getComboFornecedoresCota(idCota);
		result.use(Results.json()).from(listaFornecedores, "result").recursive().serialize();
	}

	
	/**
	 * Método responsável por obter os parametros de cobranca da Cota para a aba 'Financeiro'.
	 * @throws Mensagens de Validação.
	 * @param idCota
	 */
	@Post
	@Path("/obterParametroCobranca")
	public void obterParametroCobranca(Long idCota, ModoTela modoTela, Long idHistorico){
		
		validar();
		
		if (idCota==null) {
			throw new ValidacaoException(TipoMensagem.WARNING, "O código da cota informado náo é válido.");
		} 

		ParametroCobrancaCotaDTO parametroCobranca;
		if (ModoTela.CADASTRO_COTA == modoTela) {
		    parametroCobranca = this.parametroCobrancaCotaService.obterDadosParametroCobrancaPorCota(idCota);
		    
		    if (parametroCobranca==null){
		        parametroCobranca = new ParametroCobrancaCotaDTO();
		        parametroCobranca.setIdCota(idCota);
		        parametroCobranca.setContrato(false);
		        parametroCobranca.setFatorVencimento(0);
		        parametroCobranca.setQtdDividasAberto(0);
		        parametroCobranca.setSugereSuspensao(false);
		        parametroCobranca.setValorMinimo(BigDecimal.ZERO);
		        parametroCobranca.setVrDividasAberto(BigDecimal.ZERO);
		        
		        this.parametroCobrancaCotaService.postarParametroCobranca(parametroCobranca);
		    }
		    parametroCobranca = this.parametroCobrancaCotaService.obterDadosParametroCobrancaPorCota(idCota);
		} else {
		    parametroCobranca = parametroCobrancaCotaService.obterParametrosCobrancaHistoricoTitularidadeCota(idCota, idHistorico);
		}
		
		result.use(Results.json()).from(parametroCobranca,"result").recursive().serialize();
	}
	
	
	/**
	 * Método responsável por obter os dados da uma forma de cobranca do parametro de cobranca da Cota para a aba 'Financeiro'.
	 * @throws Mensagens de Validação.
	 * @param idFormaCobranca
	 */
	@Post
	@Path("/obterFormaCobranca")
	public void obterFormaCobranca(Long idFormaCobranca){
		
		validar();
		
		if (idFormaCobranca==null) {
			throw new ValidacaoException(TipoMensagem.WARNING, "O código da forma de cobrança informado náo é válido.");
		} 

		FormaCobrancaDTO formaCobranca = this.parametroCobrancaCotaService.obterDadosFormaCobranca(idFormaCobranca);
		
		if (formaCobranca==null) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Nenhuma forma de cobrança encontrada para o código de forma de cobrança.");
		} 
		
		result.use(Results.json()).from(formaCobranca,"result").recursive().serialize();
	}

	
	/**
     * Retorna formas de cobrança da cota para preencher a grid da view
     * @param idCota
     */
    @Post
	@Path("/obterFormasCobranca")
	public void obterFormasCobranca(Long idCota, ModoTela modoTela, Long idHistorico){
        List<FormaCobrancaDTO> listaFormasCobranca = null;
        int qtdeRegistros;
        
        if (ModoTela.CADASTRO_COTA == modoTela) {
            //VALIDACOES
            validar();	
            
            //BUSCA FORMAS DE COBRANCA DA COTA
            listaFormasCobranca = this.parametroCobrancaCotaService.obterDadosFormasCobrancaPorCota(idCota);
            qtdeRegistros = this.parametroCobrancaCotaService.obterQuantidadeFormasCobrancaCota(idCota);
            
        } else {
            listaFormasCobranca = parametroCobrancaCotaService.obterFormasCobrancaHistoricoTitularidadeCota(idCota, idHistorico);
            qtdeRegistros = listaFormasCobranca.size();
        }
				
		TableModel<CellModelKeyValue<FormaCobrancaDTO>> tableModel =
				new TableModel<CellModelKeyValue<FormaCobrancaDTO>>();
			
		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listaFormasCobranca));
		tableModel.setPage(1);
		tableModel.setTotal(qtdeRegistros);

		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
    }

    
    /**
   	 * Método responsável por obter os dados default da forma de cobranca principal dos parametros de cobrança do distribuidor.
   	 * @throws Mensagens de Validação.
   	 */
    @Post
	@Path("/obterFormaCobrancaDefault")
	public void obterFormaCobrancaDefault() {

    	PoliticaCobranca politicaPrincipal = this.politicaCobrancaService.obterPoliticaCobrancaPrincipal();
		
    	if (politicaPrincipal==null){
    		throw new ValidacaoException(TipoMensagem.WARNING, "Nenhuma forma de cobrança default encontrada.");
    	}
		
    	ParametroCobrancaDTO parametroCobrancaDistribuidor = this.politicaCobrancaService.obterDadosPoliticaCobranca(politicaPrincipal.getId());

		result.use(Results.json()).from(parametroCobrancaDistribuidor,"result").recursive().serialize();
    }

	/**
	 * Método responsável por postar os dados do parametro de cobrança da cota.
	 * @param cotaCobranca: Data Transfer Object com os dados cadastrados ou alterados pelo usuário
	 */
	@Post
	@Path("/postarParametroCobranca")
	public void postarParametroCobranca(ParametroCobrancaCotaDTO parametroCobranca){	
		
		//validar();
		
	    List<FormaCobranca> formasCobranca = this.parametroCobrancaCotaService.obterFormasCobrancaCota(parametroCobranca.getIdCota());
		if ((formasCobranca==null)||(formasCobranca.size()<=0)){
			throw new ValidacaoException(TipoMensagem.WARNING, "Adicione ao menos uma Forma de Cobrança para a Cota.");
		}
		
		if(parametroCobranca.getTipoCota()==null){
			throw new ValidacaoException(TipoMensagem.WARNING, "Escolha o Tipo da Cota.");
		}
		
		this.parametroCobrancaCotaService.postarParametroCobranca(parametroCobranca);	
		this.salvarContrato();
	    result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Parametros de Cobrança Cadastrados."),Constantes.PARAM_MSGS).recursive().serialize();
	}

	/**
	 *Formata os dados de FormaCobranca, apagando valores que não são compatíveis com o Tipo de Cobranca escolhido.
	 * @param formaCobranca
	 */
	private FormaCobrancaDTO formatarFormaCobranca(FormaCobrancaDTO formaCobranca){
		
		if (formaCobranca.getTipoFormaCobranca()==TipoFormaCobranca.SEMANAL){
			formaCobranca.setDiaDoMes(null);
			formaCobranca.setPrimeiroDiaQuinzenal(null);
			formaCobranca.setSegundoDiaQuinzenal(null);
		}
		
		if (formaCobranca.getTipoFormaCobranca()==TipoFormaCobranca.MENSAL){
			formaCobranca.setDomingo(false);
			formaCobranca.setSegunda(false);
			formaCobranca.setTerca(false);
			formaCobranca.setQuarta(false);
			formaCobranca.setQuinta(false);
			formaCobranca.setSexta(false);
			formaCobranca.setSabado(false);
			formaCobranca.setPrimeiroDiaQuinzenal(null);
			formaCobranca.setSegundoDiaQuinzenal(null);
		}
		
		if (formaCobranca.getTipoFormaCobranca()==TipoFormaCobranca.DIARIA){
			formaCobranca.setDomingo(false);
			formaCobranca.setSegunda(false);
			formaCobranca.setTerca(false);
			formaCobranca.setQuarta(false);
			formaCobranca.setQuinta(false);
			formaCobranca.setSexta(false);
			formaCobranca.setSabado(false);
			formaCobranca.setDiaDoMes(null);
			formaCobranca.setPrimeiroDiaQuinzenal(null);
			formaCobranca.setSegundoDiaQuinzenal(null);
		}
		
		if (formaCobranca.getTipoFormaCobranca()==TipoFormaCobranca.QUINZENAL){
			formaCobranca.setDomingo(false);
			formaCobranca.setSegunda(false);
			formaCobranca.setTerca(false);
			formaCobranca.setQuarta(false);
			formaCobranca.setQuinta(false);
			formaCobranca.setSexta(false);
			formaCobranca.setSabado(false);
			formaCobranca.setDiaDoMes(null);
		}
		
		if ((formaCobranca.getTipoCobranca()==TipoCobranca.BOLETO)||(formaCobranca.getTipoCobranca()==TipoCobranca.BOLETO_EM_BRANCO)){
	    	formaCobranca.setNumBanco("");
			formaCobranca.setNomeBanco("");
			formaCobranca.setAgencia(null);
			formaCobranca.setAgenciaDigito("");
			formaCobranca.setConta(null);
		    formaCobranca.setContaDigito("");
	    }
		else if ((formaCobranca.getTipoCobranca()==TipoCobranca.CHEQUE)||(formaCobranca.getTipoCobranca()==TipoCobranca.TRANSFERENCIA_BANCARIA)){
			formaCobranca.setRecebeEmail(false);
		}    
		else if (formaCobranca.getTipoCobranca()==TipoCobranca.DEPOSITO){
			formaCobranca.setRecebeEmail(false);
			formaCobranca.setNumBanco("");
			formaCobranca.setNomeBanco("");
			formaCobranca.setAgencia(null);
			formaCobranca.setAgenciaDigito("");
			formaCobranca.setConta(null);
		    formaCobranca.setContaDigito("");
		}    
		else{
			formaCobranca.setRecebeEmail(false);
			formaCobranca.setNumBanco("");
			formaCobranca.setNomeBanco("");
			formaCobranca.setAgencia(null);
			formaCobranca.setAgenciaDigito("");
			formaCobranca.setConta(null);
		    formaCobranca.setContaDigito("");
		    formaCobranca.setIdBanco(null);
		}
		return formaCobranca;
	}
	
	
	/**
	 * Método responsável por persistir os dados da forma de cobranca no banco de dados.
	 * @param formaCobranca
	 * @param tipoFormaCobranca
	 * @param listaIdsFornecedores
	 */
	@Post
	@Path("/postarFormaCobranca")
	public void postarFormaCobranca(FormaCobrancaDTO formaCobranca, String tipoFormaCobranca, List<Long> listaIdsFornecedores){	
		
		if ((tipoFormaCobranca!=null)&&(!"".equals(tipoFormaCobranca))){
		    formaCobranca.setTipoFormaCobranca(TipoFormaCobranca.valueOf(tipoFormaCobranca));
		}
		
		if ((listaIdsFornecedores!=null)&&(listaIdsFornecedores.size()>0)){
			formaCobranca.setFornecedoresId(listaIdsFornecedores);
		}
		
		formaCobranca = formatarFormaCobranca(formaCobranca);
		
		validarFormaCobranca(formaCobranca);
		
		this.parametroCobrancaCotaService.postarFormaCobranca(formaCobranca);	
	    
		result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Forma de Cobrança Cadastrada."),Constantes.PARAM_MSGS).recursive().serialize();
	}
	
	@Post
	public void carregarArquivoContrato(Long idCota, int numeroCota) {
		
		File tempDir = this.getTemporaryDirUpload(numeroCota);
		
		ContratoVO contrato = this.parametroCobrancaCotaService.obterArquivoContratoRecebido(idCota, tempDir);
		
		if (contrato != null) {
			this.session.setAttribute(CONTRATO_UPLOADED, contrato);
			this.result.include("isRecebido", contrato.isRecebido());
			
			File arquivo = contrato.getTempFile();
			
			String fileName = "";
			
			if (arquivo != null) {
				
				fileName = arquivo.getName();
				this.result.include("fileName", fileName);
			}
			
			result.use(CustomMapJson.class).put("fileName", fileName).put("isRecebido",  contrato.isRecebido()).serialize();
		} else {
			this.result.nothing();
		}
	}
    
    /**
     * Método responsável por desativar Forma de Cobranca
     * @param idFormaCobranca
     */
    @Post
	@Path("/excluirFormaCobranca")
	public void excluirFormaCobranca(Long idFormaCobranca){	

		this.parametroCobrancaCotaService.excluirFormaCobranca(idFormaCobranca);	
	    
		result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Forma de Cobrança Excluida."),Constantes.PARAM_MSGS).recursive().serialize();
	}
    
    
    /**
	 * Exibe o contrato em formato PDF.
	 * @param idCota
	 * @throws Exception
	 */
	@Get
	@Path("/imprimeContrato")
	public void imprimeContrato(Long idCota, Date dataInicio, Date dataTermino, boolean isRecebido) throws Exception {
		
		byte[] b = null;
		
		ContratoVO contrato = (ContratoVO) (this.session.getAttribute(CONTRATO_UPLOADED));
		
		File arquivo = null;
		
		if (contrato != null) {
			
			arquivo = contrato.getTempFile();
			
			contrato.setDataInicio(dataInicio);
			contrato.setDataTermino(dataTermino);
			contrato.setIdCota(idCota);
			contrato.setRecebido(isRecebido);
		
		} else {
			contrato = new ContratoVO(dataInicio, dataTermino, isRecebido, idCota);
		}
		
		if (isRecebido && (arquivo != null)) {
			b = this.obterArquivoAnexo();
		} else {
			b = this.parametroCobrancaCotaService.geraImpressaoContrato(idCota, dataInicio, dataTermino);
		}
		
		this.session.setAttribute(CONTRATO_UPLOADED, contrato);
		
		this.httpResponse.setContentType("application/pdf");
		this.httpResponse.setHeader("Content-Disposition", "attachment; filename=contrato.pdf");

		OutputStream output = this.httpResponse.getOutputStream();
		output.write(b);

		httpResponse.flushBuffer();
	}
    
	/**
	 * @return obtém arquivo anexo
	 */
	private byte[] obterArquivoAnexo() {
		
		ContratoVO contrato = (ContratoVO) this.session.getAttribute(CONTRATO_UPLOADED);
		
		File arquivoAnexo = contrato.getTempFile();
		
		byte[] arquivo = null;
		
		try {
			
			FileInputStream fis = FileUtils.openInputStream(arquivoAnexo);
			
			arquivo = IOUtils.toByteArray(fis);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return arquivo;
	}

    private void salvarContrato() {
		
    	if (this.session.getAttribute(CONTRATO_UPLOADED) != null) {
    		
    		ContratoVO contrato = (ContratoVO) this.session.getAttribute(CONTRATO_UPLOADED);
    		
    		this.parametroCobrancaCotaService.salvarContrato(contrato.getIdCota(), contrato.isRecebido(), contrato.getDataInicio(), contrato.getDataTermino());
    		
    		File file = contrato.getTempFile();
    		
    		if (file != null) {
    			//TODO: salvar arquivo no local fixo
    		}
    	} 
		
	}
    
	
	@Post
	public void uploadContratoAnexo(UploadedFile uploadedFile, String numeroCota) throws IOException {
		
		if (uploadedFile == null) 
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING,"Falha ao carregar arquivo!"));
		
		this.fileService.validarArquivo(1, 
				uploadedFile, extensoesAceitas);
		
		ContratoVO contrato = new ContratoVO();
		
		if (this.session.getAttribute(CONTRATO_UPLOADED) != null) {
			
			contrato = (ContratoVO) this.session.getAttribute(CONTRATO_UPLOADED);
			
			File arquivo = contrato.getTempFile();
			
			if (arquivo != null && arquivo.exists()) {
				arquivo.delete();
			}
		}

		String fileName = uploadedFile.getFileName();
		
		File diretorio = this.getTemporaryDirUpload(Integer.parseInt(numeroCota));
		
		diretorio.mkdirs();

		File arquivo = new File(diretorio, fileName);
		
		FileOutputStream fileOutStream = null;;
		
		try {
		
			fileOutStream = new FileOutputStream(arquivo);
			
			IOUtils.copyLarge(uploadedFile.getFile(), fileOutStream);
		
		} catch (IOException ioe) {
			throw new ValidacaoException(TipoMensagem.ERROR,
					"Falha ao gravar o arquivo em disco!");
		} finally {
			try { 
				if (fileOutStream != null) {
					fileOutStream.close();
				}
			} catch (Exception e) {
				throw new ValidacaoException(TipoMensagem.ERROR,
					"Falha ao gravar o arquivo em disco!");
			}
		}
		
		contrato.setTempFile(arquivo);
		
		this.session.setAttribute(CONTRATO_UPLOADED, contrato);
		
		result.use(PlainJSONSerialization.class).from(fileName, "fileName").recursive().serialize();
	}

	/**
	 * Remove arquivo anexo
	 */
	@Post("/removerUpload.json")
	public void removerUpload() {
		
		ContratoVO contrato =  (ContratoVO) this.session.getAttribute(CONTRATO_UPLOADED);
		
		if (contrato != null) {
			
			File arquivo = contrato.getTempFile();
			
			if(arquivo != null && arquivo.delete()) {
				
				contrato.setTempFile(null);
				contrato.setRecebido(false);
			}
		}
		
		result.nothing();
	}
	
	/**
	 * Obtém um diretório temporario para upload
	 * 
	 * @param numeroCota
	 * @return diretorio temporario
	 */
	private File getTemporaryDirUpload(int numeroCota) {
		
		String pathAplicacao = servletContext.getRealPath("");
		
		pathAplicacao = pathAplicacao.replace("\\", "/");
		
		File diretorioContratos = new File(pathAplicacao, "contratos");
		
		String diretorioCotaTemp = numeroCota+File.separator+"temp";
		
		return new File(diretorioContratos, diretorioCotaTemp);
	}

	/**
	 * Método responsável pela validação dos dados e rotinas.
	 */
	public void validar(){
		
		if (validator.hasErrors()) {
			List<String> mensagens = new ArrayList<String>();
			for (Message message : validator.getErrors()) {
				mensagens.add(message.getMessage());
			}
			ValidacaoVO validacao = new ValidacaoVO(TipoMensagem.WARNING, mensagens);
			throw new ValidacaoException(validacao);
		}
	}
	
	
	/**
	 * Método responsável pela validação dos dados da Forma de Cobranca.
	 * @param formaCobranca
	 */
	public void validarFormaCobranca(FormaCobrancaDTO formaCobranca){
		
		validar();
		
		if(formaCobranca.getTipoCobranca()==null){
			throw new ValidacaoException(TipoMensagem.WARNING, "Escolha um Tipo de Pagamento.");
		}
		
		if (formaCobranca.getTipoFormaCobranca()==null){
			throw new ValidacaoException(TipoMensagem.WARNING, "Selecione um tipo de concentração de Pagamentos.");
		}
		
		if(formaCobranca.getTipoFormaCobranca()==TipoFormaCobranca.MENSAL){
			if (formaCobranca.getDiaDoMes()==null){
				throw new ValidacaoException(TipoMensagem.WARNING, "Para o tipo de cobrança Mensal é necessário informar o dia do mês.");
			}
			else{
				if ((formaCobranca.getDiaDoMes()>31)||(formaCobranca.getDiaDoMes()<1)){
					throw new ValidacaoException(TipoMensagem.WARNING, "Dia do mês inválido.");
				}
			}
			
		}
		
		if(formaCobranca.getTipoFormaCobranca()==TipoFormaCobranca.QUINZENAL){
			if ((formaCobranca.getPrimeiroDiaQuinzenal()==null) || (formaCobranca.getSegundoDiaQuinzenal()==null)){
				throw new ValidacaoException(TipoMensagem.WARNING, "Para o tipo de cobrança Quinzenal é necessário informar dois dias do mês.");
			}
			else{
				if ((formaCobranca.getPrimeiroDiaQuinzenal()>31)||(formaCobranca.getPrimeiroDiaQuinzenal()<1)||(formaCobranca.getSegundoDiaQuinzenal()>31)||(formaCobranca.getSegundoDiaQuinzenal()<1)){
					throw new ValidacaoException(TipoMensagem.WARNING, "Dia do mês inválido.");
				}
			}
			
		}
		
		if(formaCobranca.getTipoFormaCobranca()==TipoFormaCobranca.SEMANAL){
			if((!formaCobranca.isDomingo())&&
			   (!formaCobranca.isSegunda())&&
			   (!formaCobranca.isTerca())&&
			   (!formaCobranca.isQuarta())&&
			   (!formaCobranca.isQuinta())&&
			   (!formaCobranca.isSexta())&&
			   (!formaCobranca.isSabado())){
				throw new ValidacaoException(TipoMensagem.WARNING, "Para o tipo de cobrança Semanal é necessário marcar ao menos um dia da semana.");      	
			}
		}
		
		if (formaCobranca.getIdBanco()==null){
		    if ((formaCobranca.getTipoCobranca()==TipoCobranca.BOLETO)||
		    	(formaCobranca.getTipoCobranca()==TipoCobranca.BOLETO_EM_BRANCO)||
		    	(formaCobranca.getTipoCobranca()==TipoCobranca.CHEQUE)||
		    	(formaCobranca.getTipoCobranca()==TipoCobranca.TRANSFERENCIA_BANCARIA)||
		    	(formaCobranca.getTipoCobranca()==TipoCobranca.DEPOSITO)){
		    	throw new ValidacaoException(TipoMensagem.WARNING, "Para o Tipo de Cobrança selecionado é necessário a escolha de um Banco.");
		    }
		}
		
		if ((formaCobranca.getTipoCobranca()==TipoCobranca.CHEQUE)||
		    (formaCobranca.getTipoCobranca()==TipoCobranca.TRANSFERENCIA_BANCARIA)){
			
			if((formaCobranca.getNomeBanco()==null) || ("".equals(formaCobranca.getNomeBanco()))){
				throw new ValidacaoException(TipoMensagem.WARNING, "Para o Tipo de Cobrança selecionado é necessário digitar o nome do Banco.");
			}
			if((formaCobranca.getNumBanco()==null) || ("".equals(formaCobranca.getNumBanco()))){
				throw new ValidacaoException(TipoMensagem.WARNING, "Para o Tipo de Cobrança selecionado é necessário digitar o numero do Banco.");
			}
			
			if((formaCobranca.getConta()==null) || ("".equals(formaCobranca.getConta()))){
				throw new ValidacaoException(TipoMensagem.WARNING, "Para o Tipo de Cobrança selecionado é necessário digitar o numero da Conta.");
			}
			if((formaCobranca.getContaDigito()==null) || ("".equals(formaCobranca.getContaDigito()))){
				throw new ValidacaoException(TipoMensagem.WARNING, "Para o Tipo de Cobrança selecionado é necessário digitar o dígito da Conta.");
			}
			
			if((formaCobranca.getAgencia()==null) || ("".equals(formaCobranca.getAgencia()))){
				throw new ValidacaoException(TipoMensagem.WARNING, "Para o Tipo de Cobrança selecionado é necessário digitar o numero da Agência.");
			}
			if((formaCobranca.getAgenciaDigito()==null) || ("".equals(formaCobranca.getAgenciaDigito()))){
				throw new ValidacaoException(TipoMensagem.WARNING, "Para o Tipo de Cobrança selecionado é necessário digitar o dígito da Agência.");
			}
		}
		
		if (formaCobranca.isRecebeEmail()){
			Cota cota = this.cotaService.obterPorId(formaCobranca.getIdCota());
			if (cota.getPessoa().getEmail()==null){
				throw new ValidacaoException(TipoMensagem.WARNING, "Cadastre um e-mail para a cota ou desmarque a opção de envio de email.");
			}
		}
		
		if ((formaCobranca.getFornecedoresId()!=null)&&(formaCobranca.getFornecedoresId().size()>0)){

			//VERIFICA SE A FORMA DE COBRANÇA JA EXISTE PARA O FORNECEDOR, TIPO E DIA DA CONCENTRAÇÃO MENSAL
			if (formaCobranca.getTipoFormaCobranca()==TipoFormaCobranca.MENSAL){
				if (!this.parametroCobrancaCotaService.validarFormaCobrancaMensal(formaCobranca.getIdFormaCobranca(),formaCobranca.getIdCota(),formaCobranca.getTipoCobranca(), formaCobranca.getFornecedoresId(), formaCobranca.getDiaDoMes())){
					throw new ValidacaoException(TipoMensagem.WARNING, "Esta forma de cobrança já está configurada para a Cota.");
				}
			}
			
			//VERIFICA SE A FORMA DE COBRANÇA JA EXISTE PARA O FORNECEDOR, TIPO E DIA DA CONCENTRAÇÃO SEMANAL
			if (formaCobranca.getTipoFormaCobranca()==TipoFormaCobranca.SEMANAL){
				if (!this.parametroCobrancaCotaService.validarFormaCobrancaSemanal(formaCobranca.getIdFormaCobranca(),
						                                                formaCobranca.getIdCota(),formaCobranca.getTipoCobranca(), formaCobranca.getFornecedoresId(), 
																		formaCobranca.isDomingo(),
																	    formaCobranca.isSegunda(),
																	    formaCobranca.isTerca(),
																	    formaCobranca.isQuarta(),
																	    formaCobranca.isQuinta(),
																	    formaCobranca.isSexta(),
																	    formaCobranca.isSabado())){
					throw new ValidacaoException(TipoMensagem.WARNING, "Esta forma de cobrança já está configurada para a Cota.");
				}
			}	
		}	
	}
	
	@Post("/calcularDataTermino.json")
	public void calcularDataTermino(Date dataInicio) {
				
		ParametrosDistribuidorVO parametrosDistribuidor = this.parametroDistribuidorService.getParametrosDistribuidor();
		
		int prazoContratoEmMeses = parametrosDistribuidor.getPrazoContrato();
		
		Calendar dataTermino = Calendar.getInstance();
		
		dataTermino.setTime(dataInicio);
		
		dataTermino.add(Calendar.MONTH, prazoContratoEmMeses);
		
		result.use(Results.json()).from(dataTermino.getTime(),"dataTermino").recursive().serialize();
	}
	
	@Post
	public void verificarEntregador(Long idCota){
		
		result.use(Results.json()).from(this.entregadorService.verificarEntregador(idCota), "result").serialize();
	}
}
