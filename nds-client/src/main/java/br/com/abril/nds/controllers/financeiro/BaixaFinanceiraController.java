package br.com.abril.nds.controllers.financeiro;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.client.util.DataHolder;
import br.com.abril.nds.client.vo.CobrancaDividaVO;
import br.com.abril.nds.client.vo.CobrancaVO;
import br.com.abril.nds.client.vo.DetalhesDividaVO;
import br.com.abril.nds.dto.ArquivoPagamentoBancoDTO;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.PagamentoDTO;
import br.com.abril.nds.dto.PagamentoDividasDTO;
import br.com.abril.nds.dto.ResumoBaixaBoletosDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaBancosDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaDividasCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaDividasCotaDTO.OrdenacaoColunaDividas;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.integracao.service.DistribuidorService;
import br.com.abril.nds.model.StatusCobranca;
import br.com.abril.nds.model.cadastro.Banco;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.Pessoa;
import br.com.abril.nds.model.cadastro.PessoaFisica;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.cadastro.PoliticaCobranca;
import br.com.abril.nds.model.cadastro.TipoCobranca;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.serialization.custom.PlainJSONSerialization;
import br.com.abril.nds.service.BancoService;
import br.com.abril.nds.service.BoletoService;
import br.com.abril.nds.service.CalendarioService;
import br.com.abril.nds.service.CobrancaService;
import br.com.abril.nds.service.CotaService;
import br.com.abril.nds.service.DividaService;
import br.com.abril.nds.service.LeitorArquivoBancoService;
import br.com.abril.nds.service.PoliticaCobrancaService;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.Constantes;
import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.TipoBaixaCobranca;
import br.com.abril.nds.util.TipoMensagem;
import br.com.abril.nds.util.Util;
import br.com.abril.nds.util.export.FileExporter;
import br.com.abril.nds.util.export.FileExporter.FileType;
import br.com.abril.nds.util.export.NDSFileHeader;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.abril.nds.vo.ValidacaoVO;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.core.Localization;
import br.com.caelum.vraptor.interceptor.multipart.UploadedFile;
import br.com.caelum.vraptor.view.Results;

@Resource
@Path("/financeiro")
public class BaixaFinanceiraController {

	private Result result;
	
	@SuppressWarnings("unused")
	private Localization localization;
	
	private HttpSession httpSession;
	
	private HttpServletResponse httpResponse;
	
	private ServletContext servletContext;
	
	@Autowired
	private BancoService bancoService;
	
	@Autowired
	private BoletoService boletoService;
	
	@Autowired
	private CobrancaService cobrancaService;
	
	@Autowired
	private PoliticaCobrancaService politicaCobrancaService;
	
	@Autowired
	private DistribuidorService distribuidorService;
	
	@Autowired
	private CalendarioService calendarioService;
	
	@Autowired
	private CotaService cotaService;
	
	@Autowired
	private LeitorArquivoBancoService leitorArquivoBancoService;
	
	@Autowired
	private DividaService dividaService;
	
	private static List<ItemDTO<TipoCobranca,String>> listaTiposCobranca =  new ArrayList<ItemDTO<TipoCobranca,String>>();
	
	private static final String FORMATO_DATA_DIRETORIO = "yyyy-MM-dd";
	
	private static final String DIRETORIO_TEMPORARIO_ARQUIVO_BANCO = "temp/arquivos_banco/";

	private static final String FILTRO_PESQUISA_SESSION_ATTRIBUTE = "filtroPesquisaConsultaDividas";
	   
	public BaixaFinanceiraController(Result result, Localization localization,
									 HttpSession httpSession, ServletContext servletContext,  HttpServletResponse httpResponse) {
		
		this.result = result;
		this.localization = localization;
		this.httpSession = httpSession;
		this.servletContext = servletContext;
		this.httpResponse = httpResponse;
	}
		
	@Get
	@Path("/baixa")
	@Rules(Permissao.ROLE_FINANCEIRO_BAIXA_BANCARIA)
	public void baixa() {
		listaTiposCobranca.clear();
		listaTiposCobranca.add(new ItemDTO<TipoCobranca,String>(TipoCobranca.DINHEIRO, TipoCobranca.DINHEIRO.getDescTipoCobranca()));
		listaTiposCobranca.add(new ItemDTO<TipoCobranca,String>(TipoCobranca.DEPOSITO, TipoCobranca.DEPOSITO.getDescTipoCobranca()));
		listaTiposCobranca.add(new ItemDTO<TipoCobranca,String>(TipoCobranca.TRANSFERENCIA_BANCARIA, TipoCobranca.TRANSFERENCIA_BANCARIA.getDescTipoCobranca()));
		
		FiltroConsultaBancosDTO filtro = new FiltroConsultaBancosDTO();
		filtro.setAtivo(true);
		List<Banco> bancos = bancoService.obterBancos(filtro);

		result.include("bancos", bancos);
		result.include("listaTiposCobranca",listaTiposCobranca);
	}
	
	@Post
	public void realizarBaixaAutomatica(UploadedFile uploadedFile, String valorFinanceiro) {
		
		validarEntradaDados(uploadedFile, valorFinanceiro);
		
		BigDecimal valorFinanceiroConvertido = CurrencyUtil.converterValor(valorFinanceiro);
		
		ResumoBaixaBoletosDTO resumoBaixaBoleto = null;
		
		try {
		
			//Grava o arquivo em disco e retorna o File do arquivo
			File fileArquivoBanco = gravarArquivoTemporario(uploadedFile);
			
			ArquivoPagamentoBancoDTO arquivoPagamento =
					leitorArquivoBancoService.obterPagamentosBanco(fileArquivoBanco,
																   uploadedFile.getFileName());
			
			resumoBaixaBoleto = 
				boletoService.baixarBoletosAutomatico(arquivoPagamento, valorFinanceiroConvertido,
													  obterUsuario());
		
		} finally {
			
			//Deleta os arquivos dentro do diretório temporário
			deletarArquivoTemporario();
		}
		
		result.use(PlainJSONSerialization.class)
			.from(resumoBaixaBoleto, "result").recursive().serialize();
	}
	
	private File gravarArquivoTemporario(UploadedFile uploadedFile) {

		String pathAplicacao = servletContext.getRealPath("");
		
		pathAplicacao = pathAplicacao.replace("\\", "/");
		
		String dirDataAtual = DateUtil.formatarData(new Date(), FORMATO_DATA_DIRETORIO);
		
		File fileDir = new File(pathAplicacao, DIRETORIO_TEMPORARIO_ARQUIVO_BANCO + dirDataAtual);
		
		fileDir.mkdirs();
		
		File fileArquivoBanco = new File(fileDir, uploadedFile.getFileName());
		
		FileOutputStream fos = null;
		
		try {
			
			fos = new FileOutputStream(fileArquivoBanco);
			
			IOUtils.copyLarge(uploadedFile.getFile(), fos);
		
		} catch (Exception e) {
			
			throw new ValidacaoException(TipoMensagem.ERROR,
				"Falha ao gravar o arquivo em disco!");
		
		} finally {
			try { 
				if (fos != null) {
					fos.close();
				}
			} catch (Exception e) {
				throw new ValidacaoException(TipoMensagem.ERROR,
					"Falha ao gravar o arquivo em disco!");
			}
		}
		
		return fileArquivoBanco;
	}
	
	private void deletarArquivoTemporario() {
		
		String pathAplicacao = servletContext.getRealPath("");
		
		pathAplicacao = pathAplicacao.replace("\\", "/");
		
		File fileDir = new File(pathAplicacao, DIRETORIO_TEMPORARIO_ARQUIVO_BANCO);
		
		removerFiles(fileDir);
	}
	
	public void removerFiles(File file) {
        
		if (file.isDirectory()) {
        	
            File[] files = file.listFiles();
            
            for (File f : files) {
                
            	removerFiles(f);
            }
        }
		
        file.delete();
    }

	private void validarEntradaDados(UploadedFile uploadedFile, String valorFinanceiro) {
		
		List<String> listaMensagens = new ArrayList<String>();
		
		//Valida se o arquivo foi anexado
		if (uploadedFile == null) {
			
			listaMensagens.add("O preenchimento do campo [Arquivo] é obrigatório!");
		}
		
		//Valida se o valor financeiro foi informado
		if (valorFinanceiro == null || valorFinanceiro.trim().length() == 0) {
			
			listaMensagens.add("O preenchimento do campo [Valor Financeiro] é obrigatório!");
		} else {
			
			BigDecimal valorFinanceiroConvertido = CurrencyUtil.converterValor(valorFinanceiro);
			
			//Valida se o valor financeiro é numérico
			if (valorFinanceiroConvertido == null) {
			
				listaMensagens.add("O campo [Valor Financeiro] deve ser numérico!");
			}
			
			//Valida se o valor financeiro é maior que 0
			if (valorFinanceiroConvertido.compareTo(BigDecimal.ZERO) == 0) {
			
				listaMensagens.add("O campo [Valor Financeiro] deve ser maior que 0!");
			}
		}
		
		if (!listaMensagens.isEmpty()) {
			
			ValidacaoVO validacao = new ValidacaoVO();
			
			validacao.setTipoMensagem(TipoMensagem.WARNING);
			validacao.setListaMensagens(listaMensagens);
			
			throw new ValidacaoException(validacao);
		}
	}
	
	
	/**
	 * TO-DO: OBTER USUARIO LOGADO
	 */
	private Usuario obterUsuario() {
		//TODO: obter usuário
		Usuario usuario = new Usuario();
		usuario.setNome("João");
		usuario.setId(1L);
		return usuario;
	}
	
	/**
	 * Método responsavel pela busca de boleto individual
	 * @param nossoNumero
	 */
	@Post
	@Path("/buscaBoleto")
	public void buscaBoleto(String nossoNumero){
		
		if ((nossoNumero==null)||("".equals(nossoNumero.trim()))){
		    throw new ValidacaoException(TipoMensagem.WARNING, "Digite o número da cota ou o número do boleto.");
		}
		
		CobrancaVO cobranca = this.boletoService.obterDadosBoletoPorNossoNumero(nossoNumero);
		if (cobranca==null) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum registro encontrado.");
		} 
		result.use(Results.json()).from(cobranca,"result").recursive().serialize();
	}
	
	
	/**
	 * Método responsável pela baixa de boleto individual manualmente.	
	 * @param nossoNumero
	 * @param valor
	 * @param dataVencimento
	 * @param desconto
	 * @param juros
	 * @param multa
	 * @throws Mensagem de validação
	 */
	@Post
	@Path("/baixaManualBoleto")
	public void baixaManualBoleto(String nossoNumero, 
					              String valor,
					              Date dataVencimento,
					              String desconto, 
					              String juros,
					              String multa) {        
        
		Distribuidor distribuidor = distribuidorService.obter();
		
		Date dataNovoMovimento =
			calendarioService.adicionarDiasUteis(distribuidor.getDataOperacao(), 1);
		
        BigDecimal valorConvertido = CurrencyUtil.converterValor(valor);
        BigDecimal jurosConvertido = CurrencyUtil.converterValor(juros);
        BigDecimal multaConvertida = CurrencyUtil.converterValor(multa);
        BigDecimal descontoConvertido = CurrencyUtil.converterValor(desconto);

        if (descontoConvertido.compareTo(
        		valorConvertido.add(jurosConvertido).add(multaConvertida)) == 1) {
        	
        	throw new ValidacaoException(TipoMensagem.WARNING,
        		"O desconto não deve ser maior do que o valor a pagar.");
        }

        PagamentoDTO pagamento = new PagamentoDTO();
		pagamento.setDataPagamento(dataNovoMovimento);
		pagamento.setNossoNumero(nossoNumero);
		pagamento.setNumeroRegistro(null);
		pagamento.setValorPagamento(valorConvertido);
		pagamento.setValorJuros(jurosConvertido);
		pagamento.setValorMulta(multaConvertida);
		pagamento.setValorDesconto(descontoConvertido);
		
		PoliticaCobranca politicaPrincipal = this.politicaCobrancaService.obterPoliticaCobrancaPrincipal();

		boletoService.baixarBoleto(TipoBaixaCobranca.MANUAL, pagamento, obterUsuario(),
								   null,politicaPrincipal , distribuidor,
								   dataNovoMovimento, null);
			
		result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Boleto "+nossoNumero+" baixado com sucesso."),Constantes.PARAM_MSGS).recursive().serialize();
	}
	
	
	/**
	 * Método responsável pela busca de dívidas(Cobranças Geradas)
	 * @param numCota
	 * @param sortorder
	 * @param sortname
	 * @param page
	 * @param rp
	 */
	@Post
	@Path("/buscaDividas")
	public void buscaDividas(Integer numCota,
			                 String sortorder, 
			                 String sortname,
			                 int page, 
			                 int rp){

		if (numCota==null){
		    throw new ValidacaoException(TipoMensagem.WARNING, "Digite o número da cota ou o número do boleto.");
		}

		//OBTER DISTRIBUIDOR PARA BUSCAR DATA DE OPERAÇÃO
		Distribuidor distribuidor = distribuidorService.obter();
		
        //CONFIGURAR PAGINA DE PESQUISA
		FiltroConsultaDividasCotaDTO filtroAtual = new FiltroConsultaDividasCotaDTO(numCota, distribuidor.getDataOperacao(),StatusCobranca.NAO_PAGO);
		PaginacaoVO paginacao = new PaginacaoVO(page, rp, sortorder);
		filtroAtual.setPaginacao(paginacao);
		filtroAtual.setOrdenacaoColuna(Util.getEnumByStringValue(OrdenacaoColunaDividas.values(), sortname));
	    
		FiltroConsultaDividasCotaDTO filtroSessao = (FiltroConsultaDividasCotaDTO) this.httpSession.getAttribute(FILTRO_PESQUISA_SESSION_ATTRIBUTE);
		
		if (filtroSessao != null && !filtroSessao.equals(filtroAtual)) {
			filtroAtual.getPaginacao().setPaginaAtual(1);
		}
		
		this.httpSession.setAttribute(FILTRO_PESQUISA_SESSION_ATTRIBUTE, filtroAtual);
		
		
		//BUSCA COBRANCAS
		List<CobrancaVO> cobrancasVO = this.cobrancaService.obterDadosCobrancasPorCota(filtroAtual);
		
		if ((cobrancasVO==null)||(cobrancasVO.size()<=0)) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Não há dividas em aberto nesta data para esta Cota.");
		} 
		

    	//TRATAMENTO DE DIVIDAS SELECIONADAS
		DataHolder dataHolder = (DataHolder) this.httpSession.getAttribute(DataHolder.SESSION_ATTRIBUTE_NAME);
		if (dataHolder != null) {
		    for (CobrancaVO itemCobrancaVO:cobrancasVO){
		    	String dividaMarcada = dataHolder.getData("baixaManual", itemCobrancaVO.getCodigo(), "checado");
		    	if (dividaMarcada!=null){
		    	    itemCobrancaVO.setCheck(dividaMarcada.equals("true")?true:false); 
		    	}
		    }
		}
		

		int qtdRegistros = this.cobrancaService.obterQuantidadeCobrancasPorCota(filtroAtual);
			
		TableModel<CellModelKeyValue<CobrancaVO>> tableModel = new TableModel<CellModelKeyValue<CobrancaVO>>();
			
		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(cobrancasVO));
		tableModel.setPage(page);
		tableModel.setTotal(qtdRegistros);

		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();

	}
	
	
	/**
	 * Método responsável por obter detalhes da Dívida(Cobrança)
	 * @param idCobranca
	 */
	@Post
	@Path("obterDetalhesDivida")
	public void obterDetalhesDivida(Long idCobranca){

		//BUSCA DETALHES DA DIVIDA
		List<DetalhesDividaVO> detalhes = this.cobrancaService.obterDetalhesDivida(idCobranca);
		
		if ((detalhes.size()==0)||(detalhes==null)) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Não há dividas em aberto nesta data para esta Cota.");
		} 
			
		TableModel<CellModelKeyValue<DetalhesDividaVO>> tableModel = new TableModel<CellModelKeyValue<DetalhesDividaVO>>();
			
		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(detalhes));
		tableModel.setPage(1);
		tableModel.setTotal(10);

		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
	}
	
	
	/**
	 * Método responsável por obter os calculos de juros, multa e totais referentes às dividas escolhidas pelo usuário
	 * @param idCobrancas: Dividas(Cobranças) marcadas pelo usuário
	 * @throws Exception: Tratamento na obtenção dos calculos 
	 */
	@Post
	@Path("obterPagamentoDividas")
	public void obterPagamentoDividas(List<Long> idCobrancas){
		
		if ((idCobrancas==null) || (idCobrancas.size() <=0)){
			throw new ValidacaoException(TipoMensagem.WARNING, "É necessário selecionar ao menos uma dívida.");
		}
		
		CobrancaDividaVO pagamento;
	    pagamento = this.cobrancaService.obterDadosCobrancas(idCobrancas);

		result.use(Results.json()).from(pagamento,"result").recursive().serialize();
	}
	
	/**
	 * Método responsável por efetuar a baixa das dívidas marcadas pelo usuário
	 * @param pagamento
	 * @param idCobrancas
	 */
	@Post
	@Path("baixaManualDividas")
	public void baixaManualDividas(Boolean manterPendente,
			                       String valorDividas, 
								   String valorMulta, 
								   String valorJuros,
								   String valorDesconto,
								   String valorSaldo,
								   String valorPagamento,
	                               TipoCobranca tipoPagamento,
	                               String observacoes,
	                               List<Long> idCobrancas,
	                               Banco banco){
		
		BigDecimal valorDividasConvertido = CurrencyUtil.converterValor(valorDividas);
		BigDecimal valorMultaConvertido = CurrencyUtil.converterValor(valorMulta);
	    BigDecimal valorJurosConvertido = CurrencyUtil.converterValor(valorJuros);
		BigDecimal valorDescontoConvertido = CurrencyUtil.converterValor(valorDesconto);
		BigDecimal valorSaldoConvertido = CurrencyUtil.converterValor(valorSaldo);
		BigDecimal valorPagamentoConvertido = CurrencyUtil.converterValor(valorPagamento);
		
		if (valorDescontoConvertido.compareTo(
				valorDescontoConvertido.add(valorJurosConvertido).add(valorMultaConvertido)) == 1) {	
        	throw new ValidacaoException(TipoMensagem.WARNING,"O desconto não deve ser maior do que o valor a pagar.");
        }
		
		if (!this.cobrancaService.validaBaixaManualDividas(idCobrancas) && (valorSaldoConvertido.floatValue() > 0)){
			throw new ValidacaoException(TipoMensagem.WARNING,"Não é permitida a baixa parcial de dívidas do tipo [Boleto].");
		}
		
		if (tipoPagamento==null){
			throw new ValidacaoException(TipoMensagem.WARNING,"É obrigatório a escolha de uma [Forma de Recebimento].");
		}
		
		PagamentoDividasDTO pagamento = new PagamentoDividasDTO();
		pagamento.setValorDividas(valorDividasConvertido);
		pagamento.setValorMulta(valorMultaConvertido);
		pagamento.setValorJuros(valorJurosConvertido);
		pagamento.setValorDesconto(valorDescontoConvertido);
		pagamento.setValorSaldo(valorSaldoConvertido);
		pagamento.setValorPagamento(valorPagamentoConvertido);
		pagamento.setTipoPagamento(tipoPagamento);
		pagamento.setObservacoes(observacoes);
		pagamento.setDataPagamento(this.distribuidorService.obter().getDataOperacao());
		pagamento.setUsuario(this.obterUsuario());
		
		try{
		    this.cobrancaService.baixaManualDividas(pagamento, idCobrancas, manterPendente);
		}
		catch(Exception e){
			throw new ValidacaoException(TipoMensagem.ERROR,"Erro ao efetuar a baixa manual de [Dívida]!("+e.getMessage()+")");
		}
		
		result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Dividas baixadas com sucesso."), "result").recursive().serialize();
	}
		
	/**
	 * Método responsável por validar se é possível a negociação de dividas(Cobranças)
	 * @param idCobrancas
	 */
	@Post
	@Path("obterNegociacao")
	public void obterNegociacao(List<Long> idCobrancas){
		
		if ((idCobrancas==null) || (idCobrancas.size() <=0)){
			throw new ValidacaoException(TipoMensagem.WARNING, "É necessário marcar ao menos uma dívida.");
		}
		
		if (!this.cobrancaService.validaNegociacaoDividas(idCobrancas)){
		    throw new ValidacaoException(TipoMensagem.WARNING, "Negociação não permitida.");
		}
		
		//TO-DO: Obter dados para negociação de dívidas aqui.
		
		result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "TO-DO: Negociação permitida."),Constantes.PARAM_MSGS).recursive().serialize();
	}
	
	
	/**
	 * Método responsável por validar se é possível a postergação de dividas(Cobranças)
	 * @param idCobrancas
	 */
	@Post
	@Path("obterPostergacao")
	public void obterPostergacao(Date dataPostergacao, List<Long> idCobrancas) {
		
		if ((idCobrancas==null) || (idCobrancas.size() <=0)) {
			this.result.use(
				Results.json()).from(
					new ValidacaoVO(TipoMensagem.WARNING, "É necessário marcar ao menos uma dívida."), "result").recursive().serialize();
			throw new ValidacaoException();
		}
		
		if (dataPostergacao == null) {
			dataPostergacao = Calendar.getInstance().getTime();
		}
		
		BigDecimal encargos = this.dividaService.calcularEncargosPostergacao(idCobrancas, dataPostergacao);
		
		String encargosResult = CurrencyUtil.formatarValor(encargos);
		
		if (encargos != null) {
			this.result.use(Results.json()).from(encargosResult, "result").recursive().serialize();
		}
	}
	
	@Post
	@Path("finalizarPostergacao")
	public void finalizarPostergacao(Date dataPostergacao, boolean isIsento, List<Long> idCobrancas) {
		
		List<String> listaMensagens = new ArrayList<String>();

		if (idCobrancas == null || idCobrancas.isEmpty()) {
			listaMensagens.add("É necessário marcar ao menos uma dívida.");
		}
		
		if (dataPostergacao == null || dataPostergacao.before(Calendar.getInstance().getTime())) {
			listaMensagens.add("A Data para postergação tem que ser maior que a data atual!");
		}
		
		// TODO: Pegar o id do usuário e passar.
		Long idUsuario = 1L;
		
		if (idUsuario == null || idUsuario <= 0L) {
			listaMensagens.add("Usuário inválido!");
		}
				
		if (listaMensagens != null && !listaMensagens.isEmpty()) {
			this.result.use(
				Results.json()).from(
					new ValidacaoVO(TipoMensagem.WARNING, listaMensagens), "result").recursive().serialize();
			throw new ValidacaoException();
		}
		
		try {
		
			this.dividaService.postergarCobrancaCota(idCobrancas, dataPostergacao, idUsuario, isIsento);
			
		} catch (Exception e) {
			
			if(e instanceof ValidacaoException){
				
				ValidacaoException ex = (ValidacaoException)e;
				
				this.result.use(
						Results.json()).from(ex.getValidacao(), "result").recursive().serialize();
				return;
			}
			else{
			
				this.result.use(
					Results.json()).from(
							new ValidacaoVO(
								TipoMensagem.ERROR, "Ocorreu um erro ao tentar postergar as cobranças!"), "result").recursive().serialize();
				throw new ValidacaoException();
			}
		}
		
		this.result.use(
			Results.json()).from(
				new ValidacaoVO(TipoMensagem.SUCCESS, "Cobrança postergada com sucesso!"), "result").recursive().serialize();
	}
	
	
	/**
	 * Obtém o filtro de pesquisa para exportação.
	 */
	private FiltroConsultaDividasCotaDTO obterFiltroExportacao() {
		
		FiltroConsultaDividasCotaDTO filtro= (FiltroConsultaDividasCotaDTO) this.httpSession.getAttribute(FILTRO_PESQUISA_SESSION_ATTRIBUTE);
		
		if (filtro != null) {
			
			if (filtro.getPaginacao() != null) {
				filtro.getPaginacao().setPaginaAtual(null);
				filtro.getPaginacao().setQtdResultadosPorPagina(null);
			}
			
			if (filtro.getNumeroCota() != null) {
				Cota cota = this.cotaService.obterPorNumeroDaCota(filtro.getNumeroCota());
				if (cota != null) {
					Pessoa pessoa = cota.getPessoa();
					if (pessoa instanceof PessoaFisica) {
						filtro.setNomeCota(((PessoaFisica) pessoa).getNome());					
					} else if (pessoa instanceof PessoaJuridica) {
						filtro.setNomeCota(((PessoaJuridica) pessoa).getRazaoSocial());
					}
				}
			}
		}
		
		return filtro;
	}
	
	/**
	 * Obtém os dados do cabeçalho de exportação.
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
		ndsFileHeader.setNomeUsuario(this.obterUsuario().getNome());
		return ndsFileHeader;
	}
	
	/**
	 * Exporta os dados da pesquisa.
	 * @param fileType - tipo de arquivo
	 * @throws IOException Exceção de E/S
	 */
	public void exportar(FileType fileType) throws IOException {
		FiltroConsultaDividasCotaDTO filtro = this.obterFiltroExportacao();
		List<CobrancaVO> cobrancasVO = this.cobrancaService.obterDadosCobrancasPorCota(filtro);
		FileExporter.to("dividas-cota", fileType)
			.inHTTPResponse(this.getNDSFileHeader(), filtro, null, 
					cobrancasVO, CobrancaVO.class, this.httpResponse);
	}
	
}
