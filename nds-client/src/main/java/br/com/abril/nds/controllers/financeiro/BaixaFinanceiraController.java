package br.com.abril.nds.controllers.financeiro;

import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.vo.CobrancaVO;
import br.com.abril.nds.client.vo.DetalhesDividaVO;
import br.com.abril.nds.client.vo.ValidacaoVO;
import br.com.abril.nds.dto.ArquivoPagamentoBancoDTO;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.PagamentoDTO;
import br.com.abril.nds.dto.PagamentoDividasDTO;
import br.com.abril.nds.dto.ResumoBaixaBoletosDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaDividasCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaDividasCotaDTO.OrdenacaoColunaDividas;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.StatusCobranca;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.PoliticaCobranca;
import br.com.abril.nds.model.cadastro.TipoCobranca;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.serialization.custom.PlainJSONSerialization;
import br.com.abril.nds.service.BoletoService;
import br.com.abril.nds.service.CalendarioService;
import br.com.abril.nds.service.CobrancaService;
import br.com.abril.nds.service.DistribuidorService;
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
import br.com.abril.nds.vo.PaginacaoVO;
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
	
	private ServletContext servletContext;
	
	@Autowired
	private BoletoService boletoService;
	
	@Autowired
	private CobrancaService cobrancaService;
	
	@Autowired
	private DividaService dividaService;
	
	@Autowired
	private PoliticaCobrancaService politicaCobrancaService;
	
	@Autowired
	private DistribuidorService distribuidorService;
	
	@Autowired
	private CalendarioService calendarioService;
	
	@Autowired
	private LeitorArquivoBancoService leitorArquivoBancoService;
	
	private static List<ItemDTO<TipoCobranca,String>> listaTiposCobranca =  new ArrayList<ItemDTO<TipoCobranca,String>>();
	
	private static final String FORMATO_DATA_DIRETORIO = "yyyy-MM-dd";
	
	private static final String DIRETORIO_TEMPORARIO_ARQUIVO_BANCO = "temp/arquivos_banco/";

	private static final String FILTRO_PESQUISA_SESSION_ATTRIBUTE = "filtroPesquisaConsultaDividas";
	   
	public BaixaFinanceiraController(Result result, Localization localization,
									 HttpSession httpSession, ServletContext servletContext) {
		
		this.result = result;
		this.localization = localization;
		this.httpSession = httpSession;
		this.servletContext = servletContext;
	}
		
	@Get
	@Path("/baixa")
	public void baixa() {
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
	
	private Usuario obterUsuario() {
		
		//TODO: obter usuário
		
		Usuario usuario = new Usuario();
		
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
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	@Post
	@Path("obterDetalhesDivida")
	public void obterDetalhesDivida(Long idDivida){

		//BUSCA DETALHES DA DIVIDA
		List<DetalhesDividaVO> detalhes = this.dividaService.obterDetalhesDivida(idDivida);
		
		if ((detalhes.size()==0)||(detalhes==null)) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Não há dividas em aberto nesta data para esta Cota.");
		} 
			
		TableModel<CellModelKeyValue<DetalhesDividaVO>> tableModel = new TableModel<CellModelKeyValue<DetalhesDividaVO>>();
			
		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(detalhes));
		tableModel.setPage(1);
		tableModel.setTotal(10);

		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
	}
	
	@Post
	@Path("obterPagamentoDividas")
	public void obterPagamentoDividas(List<Long> idCobrancas){
		
		//Teste
		idCobrancas= new ArrayList<Long>();
		idCobrancas.add(1l);
		idCobrancas.add(2l);
		idCobrancas.add(3l);
		idCobrancas.add(4l);
		idCobrancas.add(5l);
		//----
		
		PagamentoDividasDTO pagamento = this.cobrancaService.obterDadosCobrancas(idCobrancas);
		
		result.use(Results.json()).from(pagamento,"result").recursive().serialize();
	}
	
	@Post
	@Path("baixaManualDivida")
	public void baixaManualDivida(PagamentoDividasDTO pagamento){
		
		//pagamento menor, somar primeiras ate atingir o valor, o restante vira crédito e épostado em historico!!!!!
		
		result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Dividas baixadas com sucesso."),Constantes.PARAM_MSGS).recursive().serialize();
	}
	
	@Post
	@Path("obterNegociacao")
	public void obterNegociacao(Date dataVencimento){
		Distribuidor distribuidor = distribuidorService.obter();
		Integer diasNegociacao=distribuidor.getParametroCobrancaDistribuidor().getDiasNegociacao();
		if (diasNegociacao!=null){
			if (  distribuidor.getDataOperacao().getTime() >  DateUtil.adicionarDias(dataVencimento, diasNegociacao).getTime()){
				throw new ValidacaoException(TipoMensagem.WARNING, "Distribuidor parametrizado para não permitir a negociação neste caso.");
			}
		}
		result.nothing();
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

		if ((numCota==null)||(numCota<=0)){
		    throw new ValidacaoException(TipoMensagem.WARNING, "Digite o número da cota.");
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
		
		//BUSCA COBRANCAS //!!
		List<CobrancaVO> cobrancas = this.cobrancaService.obterDadosCobrancasPorCota(filtroAtual);
		
		if ((cobrancas.size()==0)||(cobrancas==null)) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Não há dividas em aberto nesta data para esta Cota.");
		} 
		
		int qtdRegistros = this.cobrancaService.obterQuantidadeCobrancasPorCota(filtroAtual);
			
		TableModel<CellModelKeyValue<CobrancaVO>> tableModel = new TableModel<CellModelKeyValue<CobrancaVO>>();
			
		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(cobrancas));
		tableModel.setPage(page);
		tableModel.setTotal(qtdRegistros);

		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();

	}
	
	
	
}
