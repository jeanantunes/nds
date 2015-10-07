package br.com.abril.nds.controllers.financeiro;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.metamodel.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.client.util.DataHolder;
import br.com.abril.nds.client.vo.CobrancaDividaVO;
import br.com.abril.nds.client.vo.CobrancaVO;
import br.com.abril.nds.client.vo.DetalhesDividaVO;
import br.com.abril.nds.client.vo.baixaboleto.BaixaBoletoBaseVO;
import br.com.abril.nds.client.vo.baixaboleto.BaixaBoletoBaseVO.TipoBaixaBoleto;
import br.com.abril.nds.client.vo.baixaboleto.BaixaBoletoCotaVO;
import br.com.abril.nds.client.vo.baixaboleto.BaixaBoletoDivergenteVO;
import br.com.abril.nds.client.vo.baixaboleto.BaixaBoletoRejeitadoVO;
import br.com.abril.nds.client.vo.baixaboleto.BaixaTotalBancarioVO;
import br.com.abril.nds.controllers.BaseController;
import br.com.abril.nds.dto.ArquivoPagamentoBancoDTO;
import br.com.abril.nds.dto.BaixaBancariaConsolidadaDTO;
import br.com.abril.nds.dto.DetalheBaixaBoletoDTO;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.PagamentoDTO;
import br.com.abril.nds.dto.PagamentoDividasDTO;
import br.com.abril.nds.dto.ResumoBaixaBoletosDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaBancosDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaDividasCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaDividasCotaDTO.OrdenacaoColunaDividas;
import br.com.abril.nds.dto.filtro.FiltroDetalheBaixaBoletoDTO;
import br.com.abril.nds.dto.filtro.FiltroDetalheBaixaBoletoDTO.OrdenacaoColunaDetalheBaixaBoleto;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.StatusCobranca;
import br.com.abril.nds.model.StatusControle;
import br.com.abril.nds.model.cadastro.Banco;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Pessoa;
import br.com.abril.nds.model.cadastro.PessoaFisica;
import br.com.abril.nds.model.cadastro.PessoaJuridica;
import br.com.abril.nds.model.cadastro.TipoCobranca;
import br.com.abril.nds.model.financeiro.BoletoAntecipado;
import br.com.abril.nds.model.financeiro.Cobranca;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.serialization.custom.PlainJSONSerialization;
import br.com.abril.nds.service.BaixaCobrancaService;
import br.com.abril.nds.service.BancoService;
import br.com.abril.nds.service.BoletoService;
import br.com.abril.nds.service.CalendarioService;
import br.com.abril.nds.service.CobrancaService;
import br.com.abril.nds.service.ControleBaixaBancariaService;
import br.com.abril.nds.service.CotaService;
import br.com.abril.nds.service.DividaService;
import br.com.abril.nds.service.LeitorArquivoBaixaFinanConsolidadaService;
import br.com.abril.nds.service.LeitorArquivoBancoService;
import br.com.abril.nds.service.impl.GerarCobrancaServiceImpl;
import br.com.abril.nds.service.integracao.DistribuidorService;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.Constantes;
import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.TipoBaixaCobranca;
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
import br.com.caelum.vraptor.interceptor.multipart.UploadedFile;
import br.com.caelum.vraptor.serialization.JSONSerialization;
import br.com.caelum.vraptor.view.Results;

@Resource
@Path("/financeiro/baixa")
@Rules(Permissao.ROLE_FINANCEIRO_BAIXA_BANCARIA)
public class BaixaFinanceiraController extends BaseController {
	
	
	private static final Logger LOGGER = LoggerFactory.getLogger(BaixaFinanceiraController.class);

	@Autowired
	private Result result;
	
	@Autowired
	private HttpSession httpSession;
	
	@Autowired
	private HttpServletResponse httpResponse;
	
	@Autowired
	private ServletContext servletContext;
	
	@Autowired
	private BancoService bancoService;
	
	@Autowired
	private BoletoService boletoService;
	
	@Autowired
	private CobrancaService cobrancaService;
	
	@Autowired
	private DistribuidorService distribuidorService;
	
	@Autowired
	private CalendarioService calendarioService;
	
	@Autowired
	private CotaService cotaService;
	
	@Autowired
	private BaixaCobrancaService baixaCobrancaService;
	
	@Autowired
	private LeitorArquivoBancoService leitorArquivoBancoService;
	@Autowired
	private ControleBaixaBancariaService controleBaixaService;
	
	@Autowired
	private LeitorArquivoBaixaFinanConsolidadaService leitorArquivoBaixaFinanConsolidada;
	
	@Autowired
	private DividaService dividaService;
	
	private static List<ItemDTO<TipoCobranca,String>> listaTiposCobranca =  new ArrayList<ItemDTO<TipoCobranca,String>>();
	
	private static final String FORMATO_DATA_DIRETORIO = "yyyy-MM-dd";
	
	private static final String DIRETORIO_TEMPORARIO_ARQUIVO_BANCO = "temp/arquivos_banco/";

	private static final String FILTRO_PESQUISA_SESSION_ATTRIBUTE = "filtroPesquisaConsultaDividas";
	
	private static final String FILTRO_DETALHE_BOLETO_SESSION_ATTRIBUTE = "filtroDetalheBoleto";
		
	@Get
	@Path("/")
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
		result.include("dataOperacao", getDataOperacaoDistribuidor());
	}
	
	private String getDataOperacaoDistribuidor() {

		return DateUtil.formatarDataPTBR(
				this.distribuidorService.obterDataOperacaoDistribuidor());
	}
	
	@Post
	@Rules(Permissao.ROLE_FINANCEIRO_BAIXA_BANCARIA_ALTERACAO)
	public void realizarBaixaAutomatica(Date data, UploadedFile uploadedFile, String valorFinanceiro) {
		
		valorFinanceiro = CurrencyUtil.convertValorInternacional(valorFinanceiro);
		
		BigDecimal valorFinanceiroConvertido = new BigDecimal(valorFinanceiro);
		
		validarEntradaDados(uploadedFile, valorFinanceiroConvertido);
		
		ArquivoPagamentoBancoDTO arquivoPagamento = null;
		
		try {
		
			//Grava o arquivo em disco e retorna o File do arquivo
			File fileArquivoBanco = gravarArquivoTemporario(uploadedFile);
			
			arquivoPagamento = this.leitorArquivoBancoService.obterPagamentosBanco(fileArquivoBanco, uploadedFile.getFileName());
			
			this.boletoService.baixarBoletosAutomatico(arquivoPagamento, valorFinanceiroConvertido, getUsuarioLogado(), data);
		
		} finally {
			
            // Deleta os arquivos dentro do diretório temporário
			deletarArquivoTemporario();
		}
		
		ResumoBaixaBoletosDTO resumoBaixaBoletos = this.obterResumoBaixaFinanceira(data, arquivoPagamento);
		
		result.use(PlainJSONSerialization.class).from(resumoBaixaBoletos, "result").recursive().serialize();
	}
	
	@Post
	@Rules(Permissao.ROLE_FINANCEIRO_BAIXA_BANCARIA_ALTERACAO)
	public void realizarBaixaConsolidada(Date data, UploadedFile uploadedFile) {
		
		validarEntradaDados(uploadedFile);
		
		List<BaixaBancariaConsolidadaDTO> baixas = new ArrayList<>();
		
		List<Banco> bancos = new ArrayList<>();
		
		Integer qtdBaixasArquivo = 0;
		Long qtdBaixasExecutadas = 0L;
		
		Cobranca cobranca;
		 StringBuffer msg = new StringBuffer();
		try {
		
			//Grava o arquivo em disco e retorna o File do arquivo
			File fileArquivoBanco = gravarArquivoTemporario(uploadedFile);
			
			baixas = this.leitorArquivoBaixaFinanConsolidada.obterPagamentosParaBaixa(fileArquivoBanco, uploadedFile.getFileName());
			
			qtdBaixasArquivo = baixas.size();
           
            msg.append("Numero de Boletos no arquivo:"+baixas.size());
			for (BaixaBancariaConsolidadaDTO baixaBancaria : baixas) {
				
				cobranca = cobrancaService.obterCobrancaPorNossoNumeroConsolidado(baixaBancaria.getNossoNumeroConsolidado());
				if ( cobranca == null ) {
					if (  baixaBancaria.getValorDoBoleto().doubleValue() > 0.0 ) {
						LOGGER.warn("Cobranca nao encontrada pelo nosso numero consolidado. nosso numero:"+
					             baixaBancaria.getNossoNumeroConsolidado());
						 msg.append("</br>"+"Cobranca nao encontrada pelo nosso numero consolidado. NOSSO_NUMERO:"+
					             baixaBancaria.getNossoNumeroConsolidado()+"  valor:"+ baixaBancaria.getValorDoBoleto()+" cota:"+baixaBancaria.getCodJornaleiro());
						 continue;
					   }
					   else 
					   {
						   LOGGER.warn("Cobranca nao encontrada pelo nosso numero consolidado.Valor zero. NOSSO NUMERO:"+
					             baixaBancaria.getNossoNumeroConsolidado());
						   msg.append("</br>"+"Cobranca nao encontrada mas valor menor ou igual a zero.Desconsiderando Boleto. NOSSO NUMERO:"+
					             baixaBancaria.getNossoNumeroConsolidado()+"  valor:" + baixaBancaria.getValorDoBoleto());
					       continue;
					   }
					}
					  
				PagamentoDTO pagamento = new PagamentoDTO();
				
				pagamento.setDataPagamento(baixaBancaria.getDataPagamento());
				pagamento.setNossoNumero(cobranca.getNossoNumero());
				pagamento.setNumeroRegistro(null);
				pagamento.setValorPagamento(baixaBancaria.getValorPago());
				pagamento.setValorJuros(BigDecimal.ZERO);
				pagamento.setValorMulta(BigDecimal.ZERO);
				pagamento.setValorDesconto(BigDecimal.ZERO);
			   try {	
				boletoService.baixarBoleto(TipoBaixaCobranca.CONSOLIDADA, pagamento, getUsuarioLogado(), null, baixaBancaria.getDataPagamento(), null, null, baixaBancaria.getDataPagamento());
			   } catch (ValidacaoException ve ) {
				        
				   msg.append("</br>"+ve.getMessage()+"  "+
						   baixaBancaria.getNossoNumeroConsolidado()+"  valor:"+ baixaBancaria.getValorDoBoleto()+" cota:"+baixaBancaria.getCodJornaleiro());
						   
				   
			   }
			   
				if(!bancos.contains(cobranca.getBanco())){
					bancos.add(cobranca.getBanco());
				}
			}

			qtdBaixasExecutadas = cobrancaService.qtdCobrancasConsolidadasBaixadas(data);
			
		} finally {
			
            // Deleta os arquivos dentro do diretório temporário
			deletarArquivoTemporario();
			
			if((qtdBaixasArquivo.compareTo(qtdBaixasExecutadas.intValue()) != 0) && (bancos.size() > 0) && (qtdBaixasExecutadas > 0)){
				for (Banco bank : bancos) {
					controleBaixaService.alterarControleBaixa(StatusControle.CONCLUIDO_ERROS, this.distribuidorService.obterDataOperacaoDistribuidor(), data, getUsuarioLogado(), bank);
				}
			}
		}
		
		
		if((qtdBaixasArquivo.compareTo(qtdBaixasExecutadas.intValue()) == 0) && (bancos.size() > 0)){
			for (Banco bank : bancos) {
				controleBaixaService.alterarControleBaixa(StatusControle.CONCLUIDO_SUCESSO, this.distribuidorService.obterDataOperacaoDistribuidor(), data, getUsuarioLogado(), bank);
			}
		}
		msg.append("</br>Arquivo Processado..</br>Total de Baixas:"+qtdBaixasExecutadas);
		
		result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.WARNING, msg.toString()),"result").recursive().serialize();
	}

	@Post
	public void mostrarResumoBaixaFinanceira(Date data) {
		
		ResumoBaixaBoletosDTO resumoBaixaBoletos = this.obterResumoBaixaFinanceira(data, null);
		
		result.use(JSONSerialization.class)
			.from(resumoBaixaBoletos, "result").recursive().serialize();
	}
	
	private ResumoBaixaBoletosDTO obterResumoBaixaFinanceira(Date data, ArquivoPagamentoBancoDTO arquivoPagamento) {
		
		ResumoBaixaBoletosDTO resumoBaixaBoletosDTO = 
			this.boletoService.obterResumoBaixaFinanceiraBoletos(data);
		
		if (arquivoPagamento != null) {
		
			resumoBaixaBoletosDTO.setNomeArquivo(arquivoPagamento.getNomeArquivo());
			resumoBaixaBoletosDTO.setDataCompetencia(DateUtil.formatarDataPTBR(data));
			resumoBaixaBoletosDTO.setSomaPagamentos(arquivoPagamento.getSomaPagamentos());
		}
		
		return resumoBaixaBoletosDTO;
	}
	
	@Post
	@SuppressWarnings("unchecked")
	public void mostrarGridBoletosPrevisao(Date data, String sortorder,
										   String sortname, int page, int rp) {
		
		FiltroDetalheBaixaBoletoDTO filtro = this.carregarFiltroDetalheBoleto(data, sortorder, sortname, page, rp);
		
		List<DetalheBaixaBoletoDTO> listaDetalheBaixaBoleto = this.boletoService.obterBoletosPrevistos(filtro);
		
		int qtdeTotalRegistros = this.boletoService.obterQuantidadeBoletosPrevistos(filtro).intValue();

		List<BaixaBoletoBaseVO> listaBaixaBoletoVO =
			(List<BaixaBoletoBaseVO>) this.obterBaixaBoletoExportacaoVO(listaDetalheBaixaBoleto, TipoBaixaBoleto.PREVISTOS);
		
		this.criarTableModel(filtro, listaBaixaBoletoVO, qtdeTotalRegistros);
	}

	@Post
	@SuppressWarnings("unchecked")
	public void mostrarGridBoletosBaixados(Date data, String sortorder,
			   							   String sortname, int page, int rp) {
		
		FiltroDetalheBaixaBoletoDTO filtro =
			this.carregarFiltroDetalheBoleto(data, sortorder, sortname, page, rp);
		
		List<DetalheBaixaBoletoDTO> listaDetalheBaixaBoleto =
			this.boletoService.obterBoletosBaixados(filtro);
		
		int qtdeTotalRegistros =
			this.boletoService.obterQuantidadeBoletosBaixados(filtro).intValue();
		
		List<BaixaBoletoBaseVO> listaBaixaBoletoVO =
			(List<BaixaBoletoBaseVO>) this.obterBaixaBoletoExportacaoVO(listaDetalheBaixaBoleto, TipoBaixaBoleto.BAIXADOS);
		
		this.criarTableModel(filtro, listaBaixaBoletoVO, qtdeTotalRegistros);
	}
	
	@Post
	@SuppressWarnings("unchecked")
	public void mostrarGridBoletosRejeitados(Date data, String sortorder,
			   								 String sortname, int page, int rp) {
		
		FiltroDetalheBaixaBoletoDTO filtro =
			this.carregarFiltroDetalheBoleto(data, sortorder, sortname, page, rp);
		
		List<DetalheBaixaBoletoDTO> listaDetalheBaixaBoleto =
			this.boletoService.obterBoletosRejeitados(filtro);
		
		int qtdeTotalRegistros =
			this.boletoService.obterQuantidadeBoletosRejeitados(filtro).intValue();
		
		List<BaixaBoletoBaseVO> listaBaixaBoletoVO =
			(List<BaixaBoletoBaseVO>) this.obterBaixaBoletoExportacaoVO(listaDetalheBaixaBoleto, TipoBaixaBoleto.REJEITADOS);
		
		this.criarTableModel(filtro, listaBaixaBoletoVO, qtdeTotalRegistros);
	}
	
	@Post
	@SuppressWarnings("unchecked")
	public void mostrarGridBoletosBaixadosComDivergencia(Date data, String sortorder,
			   											 String sortname, int page, int rp) {
		
		FiltroDetalheBaixaBoletoDTO filtro =
			this.carregarFiltroDetalheBoleto(data, sortorder, sortname, page, rp);
		
		List<DetalheBaixaBoletoDTO> listaDetalheBaixaBoleto =
			this.boletoService.obterBoletosBaixadosComDivergencia(filtro);
		
		int qtdeTotalRegistros =
			this.boletoService.obterQuantidadeBoletosBaixadosComDivergencia(filtro).intValue();
		
		List<BaixaBoletoBaseVO> listaBaixaBoletoVO =
			(List<BaixaBoletoBaseVO>) this.obterBaixaBoletoExportacaoVO(listaDetalheBaixaBoleto, TipoBaixaBoleto.DIVERGENTES);
		
		this.criarTableModel(filtro, listaBaixaBoletoVO, qtdeTotalRegistros);
	}
	
	        /**
     * Obtém lista de inadimplentes na data de vencimento
     * 
     * @param data
     */
	@Post
	@SuppressWarnings("unchecked")
	public void mostrarGridBoletosInadimplentes(Date data, String sortorder,
			   									String sortname, int page, int rp) {
		
		FiltroDetalheBaixaBoletoDTO filtro =
			this.carregarFiltroDetalheBoleto(data, sortorder, sortname, page, rp);

		List<DetalheBaixaBoletoDTO> listaDetalheBaixaBoleto =
			this.boletoService.obterBoletosInadimplentes(filtro);
		
		int qtdeTotalRegistros =
			this.boletoService.obterQuantidadeBoletosInadimplentes(filtro).intValue();
		
		List<BaixaBoletoBaseVO> listaBaixaBoletoVO =
			(List<BaixaBoletoBaseVO>) this.obterBaixaBoletoExportacaoVO(listaDetalheBaixaBoleto, TipoBaixaBoleto.INADIMPLENTES);
		
		this.criarTableModel(filtro, listaBaixaBoletoVO, qtdeTotalRegistros);
	}
	
	@Post
	@SuppressWarnings("unchecked")
	public void mostrarGridTotalBancario(Date data, String sortorder,
										 String sortname, int page, int rp) {
		
		FiltroDetalheBaixaBoletoDTO filtro =
			this.carregarFiltroDetalheBoleto(data, sortorder, sortname, page, rp);
		
		List<DetalheBaixaBoletoDTO> listaDetalheBaixaBoleto =
			this.boletoService.obterTotalBancario(filtro);
		
		int qtdeTotalRegistros =
			this.boletoService.obterQuantidadeTotalBancario(filtro).intValue();
		
		List<BaixaBoletoBaseVO> listaBaixaBoletoVO =
			(List<BaixaBoletoBaseVO>) this.obterBaixaBoletoExportacaoVO(listaDetalheBaixaBoleto, TipoBaixaBoleto.TOTAL_BANCARIO);
		
		this.criarTableModel(filtro, listaBaixaBoletoVO, qtdeTotalRegistros);
	}
	
	private void criarTableModel(FiltroDetalheBaixaBoletoDTO filtro,
								 List<BaixaBoletoBaseVO> listaDetalheBaixaBoleto,
								 int qtdeTotalRegistros) {

		TableModel<CellModelKeyValue<BaixaBoletoBaseVO>> tableModel =
			new TableModel<CellModelKeyValue<BaixaBoletoBaseVO>>();

		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listaDetalheBaixaBoleto));
		tableModel.setPage(filtro.getPaginacao().getPaginaAtual());
		tableModel.setTotal(qtdeTotalRegistros);

		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
	}
	
	private FiltroDetalheBaixaBoletoDTO carregarFiltroDetalheBoleto(Date data, String sortorder,
																	String sortname, int page,
																	int rp) {

		PaginacaoVO paginacao = new PaginacaoVO(page, rp, sortorder);

		OrdenacaoColunaDetalheBaixaBoleto ordenacao = 
				Util.getEnumByStringValue(OrdenacaoColunaDetalheBaixaBoleto.values(), sortname);

		FiltroDetalheBaixaBoletoDTO filtro = new FiltroDetalheBaixaBoletoDTO();
		
		filtro.setData(data);
		filtro.setPaginacao(paginacao);
		filtro.setOrdenacaoColuna(ordenacao);
		
		httpSession.setAttribute(FILTRO_DETALHE_BOLETO_SESSION_ATTRIBUTE, filtro);
		
		return filtro;
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

	private void validarEntradaDados(UploadedFile uploadedFile, BigDecimal valorFinanceiro) {
		
		List<String> listaMensagens = new ArrayList<String>();
		
		validarValorFinanceiro(valorFinanceiro, listaMensagens);
		
		validarFile(uploadedFile, listaMensagens);
		
		validarListaMensagens(listaMensagens);
	}
	
	private void validarEntradaDados(UploadedFile uploadedFile) {
		
		List<String> listaMensagens = new ArrayList<String>();
		
		validarFile(uploadedFile, listaMensagens);
		
		validarListaMensagens(listaMensagens);
	}

	private void validarValorFinanceiro(BigDecimal valorFinanceiro,
			List<String> listaMensagens) {
		//Valida se o valor financeiro foi informado
		if (valorFinanceiro == null) {
			
			listaMensagens.add("O preenchimento do campo [Valor Financeiro] é obrigatório!");
		} else {
			
			// Valida se o valor financeiro é maior que 0
			if (valorFinanceiro.compareTo(BigDecimal.ZERO) <= 0) {
				
				listaMensagens.add("O campo [Valor Financeiro] deve ser maior que 0!");
			}
		}
	}

	private void validarListaMensagens(List<String> listaMensagens) {
		if (!listaMensagens.isEmpty()) {
			
			ValidacaoVO validacao = new ValidacaoVO();
			
			validacao.setTipoMensagem(TipoMensagem.WARNING);
			validacao.setListaMensagens(listaMensagens);
			
			throw new ValidacaoException(validacao);
		}
	}

	private void validarFile(UploadedFile uploadedFile, List<String> listaMensagens) {
		//Valida se o arquivo foi anexado
		if (uploadedFile == null) {
			
            listaMensagens.add("O preenchimento do campo [Arquivo] é obrigatório!");
		}
	}
	
	        /**
     * Método responsavel pela busca de boleto individual
     * 
     * @param nossoNumero
     */
	@Post
	@Path("/buscaBoleto")
	public void buscaBoleto(String nossoNumero, Date dataPagamento, BigDecimal valor){
		
		if ((nossoNumero==null)||("".equals(nossoNumero.trim()))){
            throw new ValidacaoException(TipoMensagem.WARNING, "Digite o número da cota ou o número do boleto.");
		}

		CobrancaVO cobranca = this.boletoService.obterDadosBoletoPorNossoNumero(nossoNumero, dataPagamento);
		
		if (cobranca==null) {

			cobranca = this.cobrancaService.obterDadosCobrancaBoletoAntecipado(nossoNumero, dataPagamento, valor);
			
			if (cobranca == null) {
				
				throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum registro encontrado.");
			}
		}

		this.result.use(Results.json()).from(cobranca,"result").recursive().serialize();
	}

	        /**
     * Método responsável pela baixa de boleto individual manualmente.
     * 
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
	@Rules(Permissao.ROLE_FINANCEIRO_BAIXA_BANCARIA_ALTERACAO)
	public void baixaManualBoleto(String nossoNumero, 
					              String valor,
					              Date dataPagamento,
					              String desconto, 
					              String juros,
					              String multa) {        
		
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
		pagamento.setDataPagamento(dataPagamento);
		pagamento.setNossoNumero(nossoNumero);
		pagamento.setNumeroRegistro(null);
		pagamento.setValorPagamento(valorConvertido);
		pagamento.setValorJuros(jurosConvertido);
		pagamento.setValorMulta(multaConvertida);
		pagamento.setValorDesconto(descontoConvertido);
		
		boletoService.baixarBoleto(TipoBaixaCobranca.MANUAL, pagamento, getUsuarioLogado(),
								   null, 
								   dataPagamento, null, null, dataPagamento);
			
		result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Boleto "+nossoNumero+" baixado com sucesso."),Constantes.PARAM_MSGS).recursive().serialize();
	}
	
	
	        /**
     * Método responsável pela busca de dívidas(Cobranças Geradas)
     * 
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

		//CONFIGURAR PAGINA DE PESQUISA
		FiltroConsultaDividasCotaDTO filtroAtual = 
				new FiltroConsultaDividasCotaDTO(numCota);
		
		filtroAtual.setStatusCobranca(StatusCobranca.NAO_PAGO);

		PaginacaoVO paginacao = new PaginacaoVO(page, rp, sortorder);
		filtroAtual.setSomenteBaixadas(false);
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
	
	@Post
	@Path("/buscaDividasBaixadas")
	public void buscaDividasBaixadas(Integer numCota,
									 String nossoNumero,
					                 String sortorder, 
					                 String sortname,
					                 int page, 
					                 int rp){
		if (numCota==null){
            throw new ValidacaoException(TipoMensagem.WARNING, "Digite o número da cota ou o número do boleto.");
		}

		//CONFIGURAR PAGINA DE PESQUISA
		FiltroConsultaDividasCotaDTO filtroAtual = 
				new FiltroConsultaDividasCotaDTO(numCota);
		PaginacaoVO paginacao = new PaginacaoVO(page, rp, sortorder);
		filtroAtual.setNossoNumero(nossoNumero);
		filtroAtual.setSomenteBaixadas(true);
		filtroAtual.setPaginacao(paginacao);
		filtroAtual.setOrdenacaoColuna(Util.getEnumByStringValue(OrdenacaoColunaDividas.values(), sortname));
	    
		FiltroConsultaDividasCotaDTO filtroSessao = (FiltroConsultaDividasCotaDTO) this.httpSession.getAttribute(FILTRO_PESQUISA_SESSION_ATTRIBUTE);
		
		if (filtroSessao != null && !filtroSessao.equals(filtroAtual)) {
			filtroAtual.getPaginacao().setPaginaAtual(1);
		}
		
		this.httpSession.setAttribute(FILTRO_PESQUISA_SESSION_ATTRIBUTE, filtroAtual);
		
		
		//BUSCA COBRANCAS
		List<CobrancaVO> cobrancasVO = this.baixaCobrancaService.buscarCobrancasBaixadas(filtroAtual); 

		if ((cobrancasVO==null)||(cobrancasVO.size()<=0)) {
            throw new ValidacaoException(TipoMensagem.WARNING, "Não há cobranças baixadas nesta data para esta Cota.");
		} 
		

    	//TRATAMENTO DE DIVIDAS SELECIONADAS
		DataHolder dataHolder = (DataHolder) this.httpSession.getAttribute(DataHolder.SESSION_ATTRIBUTE_NAME);
	    for (CobrancaVO itemCobrancaVO:cobrancasVO){

	    	if (dataHolder != null) {

		    	String dividaMarcada = dataHolder.getData("baixaManual", itemCobrancaVO.getCodigo(), "checado");
		    	if (dividaMarcada!=null){
		    	    itemCobrancaVO.setCheck(dividaMarcada.equals("true")?true:false); 
		    	}
		    }

	    	itemCobrancaVO.setDataEmissao(DateUtil.formatarDataPTBR(DateUtil.parseData(itemCobrancaVO.getDataEmissao(), "yyyy-MM-dd")));
	    	itemCobrancaVO.setDataVencimento(DateUtil.formatarDataPTBR(DateUtil.parseData(itemCobrancaVO.getDataVencimento(), "yyyy-MM-dd")));
	    	itemCobrancaVO.setValor(itemCobrancaVO.getValor()!=null?CurrencyUtil.convertValor(itemCobrancaVO.getValor(),2):"");
		}
		

		int qtdRegistros = this.baixaCobrancaService.countBuscarCobrancasBaixadas(filtroAtual).intValue();
			
		TableModel<CellModelKeyValue<CobrancaVO>> tableModel = new TableModel<CellModelKeyValue<CobrancaVO>>();
			
		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(cobrancasVO));
		tableModel.setPage(page);
		tableModel.setTotal(qtdRegistros);

		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
	}
	
	        /**
     * Método responsável por obter detalhes da Dívida(Cobrança)
     * 
     * @param idCobranca
     */
	@Post
	@Path("obterDetalhesDivida")
	public void obterDetalhesDivida(Long idCobranca, boolean isBoletoAntecipado){

		//BUSCA DETALHES DA DIVIDA
        List<DetalhesDividaVO> detalhes;
		
		if (isBoletoAntecipado) {
			
			detalhes = this.obterDetalhesBoletoAntecipado(idCobranca);
			
		} else {
			
			detalhes = this.cobrancaService.obterDetalhesDivida(idCobranca); 
		}
		
        if (detalhes == null || detalhes.isEmpty()) {
            throw new ValidacaoException(TipoMensagem.WARNING, "Não há dividas em aberto nesta data para esta Cota.");
		} 
			
		TableModel<CellModelKeyValue<DetalhesDividaVO>> tableModel = new TableModel<CellModelKeyValue<DetalhesDividaVO>>();
			
		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(detalhes));
		tableModel.setPage(1);
		tableModel.setTotal(10);

		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
	}
	
	private List<DetalhesDividaVO> obterDetalhesBoletoAntecipado(Long idBoletoAntecipado) {
		
		BoletoAntecipado boletoAntecipado = this.boletoService.obterBoletoEmBrancoPorId(idBoletoAntecipado);
		
		String dataDe = DateUtil.formatarDataPTBR(boletoAntecipado.getEmissaoBoletoAntecipado().getDataRecolhimentoCEDe());
		String dataAte = DateUtil.formatarDataPTBR(boletoAntecipado.getEmissaoBoletoAntecipado().getDataRecolhimentoCEAte());

		DetalhesDividaVO detalhesDivida = new DetalhesDividaVO();
		detalhesDivida.setData(dataDe + " - " + dataAte);
		detalhesDivida.setTipo("Boleto em Branco");
		detalhesDivida.setObservacao(StringUtils.EMPTY);
		detalhesDivida.setValor(CurrencyUtil.formatarValor(boletoAntecipado.getValor()));
		
		List<DetalhesDividaVO> detalhes = new ArrayList<>();
		
		detalhes.add(detalhesDivida);
		
		return detalhes;
	}
	
	
	        /**
     * Método responsável por obter os calculos de juros, multa e totais
     * referentes às dividas escolhidas pelo usuário
     * 
     * @param idCobrancas: Dividas(Cobranças) marcadas pelo usuário
     * @throws Exception: Tratamento na obtenção dos calculos
     */
	@Post
	@Path("obterPagamentoDividas")
	public void obterPagamentoDividas(List<Long> idCobrancas, Date dataPagamento){
		
		if ((idCobrancas==null) || (idCobrancas.size() <=0)){
            throw new ValidacaoException(TipoMensagem.WARNING, "É necessário selecionar ao menos uma dívida.");
		}
		
		CobrancaDividaVO pagamento = null;
		
			
		if ( dataPagamento == null){
			pagamento = new CobrancaDividaVO();
			pagamento.setValorJuros(CurrencyUtil.formatarValor(BigDecimal.ZERO));
			pagamento.setValorMulta(CurrencyUtil.formatarValor(BigDecimal.ZERO));
			pagamento.setValorDividas(CurrencyUtil.formatarValor(BigDecimal.ZERO));
			pagamento.setValorPagamento(CurrencyUtil.formatarValor(BigDecimal.ZERO));
			pagamento.setValorDesconto(CurrencyUtil.formatarValor(BigDecimal.ZERO));
			pagamento.setValorSaldo(CurrencyUtil.formatarValor(BigDecimal.ZERO));
		}
		else{
			pagamento = this.cobrancaService.obterDadosCobrancas(idCobrancas, dataPagamento);
		}
		
		result.use(Results.json()).from(pagamento,"result").recursive().serialize();
	}
	
	@Post
	@Path("calcularSaldoDivida")
	public void calacularSaldo(String vlDividaStr, 
			                   String vlMultaStr, 
			                   String vlJurosStr, 
			                   String vlDescontoStr, 
			                   String vlPagoStr){
		
		BigDecimal vlDivida =  BigDecimal.valueOf(Float.parseFloat(vlDividaStr));
        BigDecimal vlMulta =  BigDecimal.valueOf(Float.parseFloat(vlMultaStr));
        BigDecimal vlJuros =  BigDecimal.valueOf(Float.parseFloat(vlJurosStr));
        BigDecimal vlDesconto =  BigDecimal.valueOf(Float.parseFloat(vlDescontoStr));
        BigDecimal vlPago = BigDecimal.valueOf(Float.parseFloat(vlPagoStr));
		
		vlDivida = vlDivida.setScale(2, RoundingMode.HALF_EVEN);
		
		vlMulta  = vlMulta.setScale(4,RoundingMode.HALF_EVEN);
		
		vlJuros = vlJuros.setScale(4,RoundingMode.HALF_EVEN);
		
		vlDesconto = vlDesconto.setScale(4,RoundingMode.HALF_EVEN);
		
		vlPago = vlPago.setScale(2,RoundingMode.HALF_EVEN);
		
		vlPago = vlPago.add(vlDesconto);
		
		BigDecimal vlSaldo = BigDecimal.ZERO;
				
		vlSaldo = vlSaldo.add(vlDivida).add(vlMulta).add(vlJuros).subtract(vlPago).negate().setScale(2,RoundingMode.HALF_EVEN);
		
		result.use(Results.json()).from(CurrencyUtil.formatarValor(vlSaldo),"result").recursive().serialize();
	}
	
	@Post
	@Path("calculaTotalManualDividas")
	public void calculaTotalManualDividas(BigDecimal vlDivida, BigDecimal vlMulta, BigDecimal vlJuros, BigDecimal vlDesconto){
		
		vlDivida = vlDivida.setScale(2, RoundingMode.HALF_EVEN);
		
		vlMulta  = vlMulta.setScale(4,RoundingMode.HALF_EVEN);
		
		vlJuros = vlJuros.setScale(4,RoundingMode.HALF_EVEN);
		
		vlDesconto = vlDesconto.setScale(4,RoundingMode.HALF_EVEN);
		
		BigDecimal vlTotalDividas = BigDecimal.ZERO;
				
		vlTotalDividas = vlTotalDividas.add(vlDivida).add(vlMulta).add(vlJuros).subtract(vlDesconto);
		
		vlTotalDividas = vlTotalDividas.setScale(2,RoundingMode.HALF_EVEN);
		
		result.use(Results.json()).from(CurrencyUtil.formatarValor(vlTotalDividas),"result").recursive().serialize();
	}
	
	
	
	        /**
     * Método responsável por efetuar a baixa das dívidas marcadas pelo usuário
     * 
     * @param pagamento
     * @param idCobrancas
     */
	@Post
	@Path("baixaManualDividas")
	@Rules(Permissao.ROLE_FINANCEIRO_BAIXA_BANCARIA_ALTERACAO)
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
	                               Long idBanco,
	                               Date dataPagamento){
		
		BigDecimal valorDividasConvertido = CurrencyUtil.getBigDecimal(valorDividas);
		BigDecimal valorMultaConvertido = CurrencyUtil.getBigDecimal(valorMulta);
	    BigDecimal valorJurosConvertido = CurrencyUtil.getBigDecimal(valorJuros);
		BigDecimal valorDescontoConvertido = CurrencyUtil.getBigDecimal(valorDesconto);
		BigDecimal valorSaldoConvertido = CurrencyUtil.getBigDecimal(valorSaldo);
		BigDecimal valorPagamentoConvertido = CurrencyUtil.getBigDecimal(valorPagamento);
		
		this.validarBaixaManualDividas(tipoPagamento, idCobrancas, idBanco,valorMultaConvertido, 
								  		valorJurosConvertido,valorDescontoConvertido, valorSaldoConvertido,dataPagamento);
		
		Date dataOperacao = this.distribuidorService.obterDataOperacaoDistribuidor();
		
		PagamentoDividasDTO pagamento = new PagamentoDividasDTO();
		pagamento.setValorDividas(valorDividasConvertido);
		pagamento.setValorMulta(valorMultaConvertido);
		pagamento.setValorJuros(valorJurosConvertido);
		pagamento.setValorDesconto(valorDescontoConvertido);
		pagamento.setValorSaldo(valorSaldoConvertido);
		pagamento.setValorPagamento(valorPagamentoConvertido);
		pagamento.setTipoPagamento(tipoPagamento);
		pagamento.setObservacoes(observacoes);
		pagamento.setDataPagamento( (dataPagamento == null) ? dataOperacao : dataPagamento);
		pagamento.setDataOperacao(dataOperacao);
		pagamento.setUsuario(getUsuarioLogado());
		pagamento.setBanco(idBanco!=null?bancoService.obterBancoPorId(idBanco):null);
		
		this.cobrancaService.baixaManualDividas(pagamento, idCobrancas, manterPendente);

		result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Dividas baixadas com sucesso."), "result").recursive().serialize();
	}

	private void validarBaixaManualDividas(TipoCobranca tipoPagamento,
			List<Long> idCobrancas, Long idBanco,
			BigDecimal valorMultaConvertido, BigDecimal valorJurosConvertido,
			BigDecimal valorDescontoConvertido, BigDecimal valorSaldoConvertido, Date dataPagamento) {
		
		if(dataPagamento == null){
            throw new ValidacaoException(TipoMensagem.WARNING,
                    "É obrigatório a escolha de uma data de pagamento [Data Pagamento].");
		}
		
		if (valorDescontoConvertido.compareTo(
				valorDescontoConvertido.add(valorJurosConvertido).add(valorMultaConvertido)) == 1) {	
            throw new ValidacaoException(TipoMensagem.WARNING, "O desconto não deve ser maior do que o valor a pagar.");
        }
		
		if (tipoPagamento==null){
            throw new ValidacaoException(TipoMensagem.WARNING, "É obrigatório a escolha de uma [Forma de Recebimento].");
		}
		
		if(idBanco == null){
			if (!TipoCobranca.DINHEIRO.equals(tipoPagamento)&&!TipoCobranca.OUTROS.equals(tipoPagamento)){
                throw new ValidacaoException(TipoMensagem.WARNING, "É obrigatório a escolha de uma [Banco].");
		    }
		}
	}
	
	@Post
	@Path("validarBaixaManual")
	@Rules(Permissao.ROLE_FINANCEIRO_BAIXA_BANCARIA_ALTERACAO)
	public void validarBaixaManual(
								   String valorMulta, 
								   String valorJuros,
								   String valorDesconto,
								   String valorSaldo,
					               TipoCobranca tipoPagamento,
					               List<Long> idCobrancas,
					               Long idBanco,
					               Date dataPagamento){
		
		BigDecimal valorMultaConvertido = valorMulta == null ? BigDecimal.ZERO : CurrencyUtil.converterValor(valorMulta);
	    BigDecimal valorJurosConvertido = valorJuros == null ? BigDecimal.ZERO : CurrencyUtil.converterValor(valorJuros);
		BigDecimal valorDescontoConvertido = valorDesconto == null ? BigDecimal.ZERO : CurrencyUtil.converterValor(valorDesconto);
		BigDecimal valorSaldoConvertido = valorSaldo == null ? BigDecimal.ZERO : CurrencyUtil.converterValor(valorSaldo);
		
		this.validarBaixaManualDividas(tipoPagamento, idCobrancas, idBanco,valorMultaConvertido, 
		  		valorJurosConvertido,valorDescontoConvertido, valorSaldoConvertido,dataPagamento);
		
		this.cobrancaService.validarDataPagamentoCobranca(idCobrancas, dataPagamento);
		
		this.result.use(Results.json()).from("", "result").recursive().serialize();
	}
		
	        /**
     * Método responsável por validar se é possível a negociação de
     * dividas(Cobranças)
     * 
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
		
        // TO-DO: Obter dados para negociação de dívidas aqui.
		
        result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "TO-DO: Negociação permitida."),
                Constantes.PARAM_MSGS).recursive().serialize();
	}
	
	
	        /**
     * Método responsável por validar se é possível a postergação de
     * dividas(Cobranças)
     * 
     * @param idCobrancas
     */
	@Post
	@Path("obterPostergacao")
	public void obterPostergacao(Date dataPostergacao, List<Long> idCobrancas) {
		
		if (idCobrancas == null || idCobrancas.isEmpty()) {

			throw new ValidacaoException(
TipoMensagem.WARNING, "É necessário marcar ao menos uma dívida.");
		}
		
		BigDecimal encargos = null;

		if (dataPostergacao != null) {

			if (!this.calendarioService.isDiaUtil(dataPostergacao)) {
				
				throw new ValidacaoException(
					TipoMensagem.WARNING, 
                        "É necessário selecionar um dia útil para postergação da dívida.");
			}
			
			encargos = this.dividaService.calcularEncargosPostergacao(idCobrancas, dataPostergacao);
		}
		
		if (encargos != null) {
			
			String encargosResult = CurrencyUtil.formatarValor(encargos);
			
			this.result.use(Results.json()).from(encargosResult, "result").recursive().serialize();
			
		} else {
			
			this.result.use(Results.json()).from("", "result").recursive().serialize();
		}
	}
	
	@Post
	@Path("finalizarPostergacao")
	@Rules(Permissao.ROLE_FINANCEIRO_BAIXA_BANCARIA_ALTERACAO)
	public void finalizarPostergacao(Date dataPostergacao, boolean isIsento, List<Long> idCobrancas) {
		
		List<String> listaMensagens = new ArrayList<String>();

		if (idCobrancas == null 
				|| idCobrancas.isEmpty()) {
			
            listaMensagens.add("É necessário marcar ao menos uma dívida.");
		}
		
		if (!this.calendarioService.isDiaUtil(dataPostergacao)) {
			
			throw new ValidacaoException(
				TipoMensagem.WARNING, 
                    "É necessário selecionar um dia útil para postergação da dívida.");
		}
				
		this.dividaService.postergarCobrancaCota(idCobrancas, dataPostergacao, getUsuarioLogado().getId(), isIsento);
		
		this.result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Cobrança postergada com sucesso!"), "result").recursive().serialize();
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
     * Exporta os dados da pesquisa.
     * 
     * @param fileType - tipo de arquivo
     * @throws IOException Exceção de E/S
     */
	public void exportar(FileType fileType) throws IOException {

		FiltroConsultaDividasCotaDTO filtro = this.obterFiltroExportacao();

		List<CobrancaVO> cobrancasVO = filtro.isSomenteBaixadas() ? this.baixaCobrancaService.buscarCobrancasBaixadas(filtro)
																  : this.cobrancaService.obterDadosCobrancasPorCota(filtro);
		
		FileExporter.to("dividas-cota", fileType)
.inHTTPResponse(this.getNDSFileHeader(), filtro,
					cobrancasVO, CobrancaVO.class, this.httpResponse);
		
		this.result.nothing();
	}
	
	private FiltroDetalheBaixaBoletoDTO obterFiltroExportacaoDetalhe() {
		
		FiltroDetalheBaixaBoletoDTO filtro = 
				(FiltroDetalheBaixaBoletoDTO) httpSession.getAttribute(FILTRO_DETALHE_BOLETO_SESSION_ATTRIBUTE);
		
		if (filtro == null) {
			
            throw new ValidacaoException(TipoMensagem.WARNING, "Filtro inválido");
		}
		
		if (filtro.getPaginacao() != null) {
			
			filtro.getPaginacao().setPaginaAtual(null);
			filtro.getPaginacao().setQtdResultadosPorPagina(null);
			filtro.getPaginacao().setQtdResultadosTotal(null);
		}

		return filtro;
	}
	
	private List<? extends BaixaBoletoBaseVO> obterBaixaBoletoExportacaoVO(List<DetalheBaixaBoletoDTO> listaDetalhesBaixaBoleto, TipoBaixaBoleto tipoBaixaBoleto) {
		
		switch(tipoBaixaBoleto) {
			
			case BAIXADOS:
				
				return this.toBaixasBoletoCota(listaDetalhesBaixaBoleto);
				
			case DIVERGENTES:
				
				return this.toBaixasBoletoDivergentes(listaDetalhesBaixaBoleto);
			
			case INADIMPLENTES:
				
				return this.toBaixasBoletoCota(listaDetalhesBaixaBoleto);
				
			case PREVISTOS:
				
				return this.toBaixasBoletoCota(listaDetalhesBaixaBoleto);
	
			case REJEITADOS:
				
				return this.toBaixasBoletoRejeitados(listaDetalhesBaixaBoleto);
	
			case TOTAL_BANCARIO:
				
				return this.toTotalBancario(listaDetalhesBaixaBoleto);
				
			default:
				return null;
		}
	}
	
	private List<? extends BaixaBoletoBaseVO> obterBaixaBoletoExportacaoVO(FiltroDetalheBaixaBoletoDTO filtro, TipoBaixaBoleto tipoBaixaBoleto) {

		List<DetalheBaixaBoletoDTO> listaDetalhesBaixaBoleto = null;
		
		switch(tipoBaixaBoleto) {
		
		case BAIXADOS:
			
			listaDetalhesBaixaBoleto = this.boletoService.obterBoletosBaixados(filtro);
			
			return this.toBaixasBoletoCota(listaDetalhesBaixaBoleto);
			
		case DIVERGENTES:
			
			listaDetalhesBaixaBoleto = this.boletoService.obterBoletosBaixadosComDivergencia(filtro);
			
			return this.toBaixasBoletoDivergentes(listaDetalhesBaixaBoleto);
		
		case INADIMPLENTES:
			
			listaDetalhesBaixaBoleto = this.boletoService.obterBoletosInadimplentes(filtro);
			
			return this.toBaixasBoletoCota(listaDetalhesBaixaBoleto);
			
		case PREVISTOS:
			
			listaDetalhesBaixaBoleto = this.boletoService.obterBoletosPrevistos(filtro);
			
			return this.toBaixasBoletoCota(listaDetalhesBaixaBoleto);

		case REJEITADOS:
			
			listaDetalhesBaixaBoleto = this.boletoService.obterBoletosRejeitados(filtro);

			return this.toBaixasBoletoRejeitados(listaDetalhesBaixaBoleto);

		case TOTAL_BANCARIO:
			
			listaDetalhesBaixaBoleto = this.boletoService.obterTotalBancario(filtro);
			
			return this.toTotalBancario(listaDetalhesBaixaBoleto);
			
		default:
			
			return null;
		}
		
	}
	
	/*
	 * Transforma um objeto DetalheBaixaBoletoDTO em um BaixaBoletoCotaVO
	 */
	private List<BaixaBoletoCotaVO> toBaixasBoletoCota(List<DetalheBaixaBoletoDTO> detalhes) {
		
		List<BaixaBoletoCotaVO> lista = new ArrayList<BaixaBoletoCotaVO>();

		for (DetalheBaixaBoletoDTO detalhe : detalhes) {
			
			BaixaBoletoCotaVO baixa = new BaixaBoletoCotaVO();
			
			baixa.setDataVencimento(DateUtil.formatarDataPTBR(detalhe.getDataVencimento()));
			baixa.setNomeBanco(detalhe.getNomeBanco());
			baixa.setNomeCota(detalhe.getNomeCota());
			baixa.setNossoNumero(detalhe.getNossoNumero());
			baixa.setNumeroConta(detalhe.getNumeroConta());
			baixa.setNumeroCota(detalhe.getNumeroCota());
			baixa.setValorBoleto(detalhe.getValorBoleto() == null ? null : 
				detalhe.getValorBoleto().setScale(2, RoundingMode.HALF_EVEN));
			
			lista.add(baixa);
		}
		
		return lista;
	}

	/*
	 * Transforma um objeto DetalheBaixaBoletoDTO em um BaixaBoletoDivergenteVO
	 */
	private List<BaixaBoletoDivergenteVO> toBaixasBoletoDivergentes(List<DetalheBaixaBoletoDTO> detalhes) {
		
		List<BaixaBoletoDivergenteVO> lista = new ArrayList<BaixaBoletoDivergenteVO>();

		for (DetalheBaixaBoletoDTO detalhe : detalhes) {
			
			BaixaBoletoDivergenteVO baixa = new BaixaBoletoDivergenteVO();
			
			baixa.setDiferencaValor(detalhe.getValorDiferenca());
			baixa.setMotivoDivergencia(detalhe.getMotivoDivergencia());
			baixa.setNomeBanco(detalhe.getNomeBanco());
			baixa.setNumeroConta(detalhe.getNumeroConta());
			baixa.setValorBoleto(detalhe.getValorBoleto() == null ? null :
				detalhe.getValorBoleto().setScale(2, RoundingMode.HALF_EVEN));
			baixa.setValorPago(detalhe.getValorPago() == null ? null : 
				detalhe.getValorPago().setScale(2, RoundingMode.HALF_EVEN));
			
			lista.add(baixa);
		}
		
		return lista;
	}

	/*
	 * Transforma um objeto DetalheBaixaBoletoDTO em um BaixaBoletoRejeitadoVO
	 */
	private List<BaixaBoletoRejeitadoVO> toBaixasBoletoRejeitados(List<DetalheBaixaBoletoDTO> detalhes) {
		
		List<BaixaBoletoRejeitadoVO> lista = new ArrayList<BaixaBoletoRejeitadoVO>();

		for (DetalheBaixaBoletoDTO detalhe : detalhes) {
			
			BaixaBoletoRejeitadoVO baixa = new BaixaBoletoRejeitadoVO();
			
			baixa.setMotivoRejeitado(detalhe.getMotivoRejeitado());
			baixa.setNomeBanco(detalhe.getNomeBanco());
			baixa.setNumeroConta(detalhe.getNumeroConta());
			baixa.setValorBoleto(detalhe.getValorBoleto() == null ? null : 
				detalhe.getValorBoleto().setScale(2, RoundingMode.HALF_EVEN));
			
			lista.add(baixa);
		}
		
		return lista;
	}
	
	/*
	 * Transforma um objeto DetalheBaixaBoletoDTO em um BaixaTotalBancarioVO
	 */
	private List<BaixaTotalBancarioVO> toTotalBancario(List<DetalheBaixaBoletoDTO> detalhes) {
		
		List<BaixaTotalBancarioVO> lista = new ArrayList<BaixaTotalBancarioVO>();

		for (DetalheBaixaBoletoDTO detalhe : detalhes) {
			
			BaixaTotalBancarioVO baixa = new BaixaTotalBancarioVO();
			
			baixa.setNomeBanco(detalhe.getNomeBanco());
			baixa.setNumeroConta(detalhe.getNumeroConta());
			baixa.setValorPago(detalhe.getValorPago() == null ? null :
				detalhe.getValorPago().setScale(2, RoundingMode.HALF_EVEN));
			baixa.setDataPagamento(DateUtil.formatarDataPTBR(detalhe.getDataVencimento()));
			
			lista.add(baixa);
		}
		
		return lista;
	}

	        /**
     * Método que realiza a exportação dos dados exibidos nos diálogos de baixa
     * automática.
     * 
     * @param fileType
     * @param tipoBaixaBoleto
     * @throws IOException
     */
	@Get
	@SuppressWarnings("unchecked")
	public void exportarResumoBaixaAutomatica(FileType fileType, TipoBaixaBoleto tipoBaixaBoleto) throws IOException {

		FiltroDetalheBaixaBoletoDTO filtro = this.obterFiltroExportacaoDetalhe();
		
		List<BaixaBoletoBaseVO> lista = (List<BaixaBoletoBaseVO>) this.obterBaixaBoletoExportacaoVO(filtro, tipoBaixaBoleto);		
		
		FileExporter.to(tipoBaixaBoleto.getNomeArquivo(), fileType)
.inHTTPResponse(this.getNDSFileHeader(), filtro,
					lista, tipoBaixaBoleto.getTipoImpressaoVO(), this.httpResponse);
		
		this.result.nothing();
	}

	@Post
	@Rules(Permissao.ROLE_FINANCEIRO_BAIXA_BANCARIA_ALTERACAO)
	public void confirmarBaixaDividas(List<Long> idCobrancas) {
		
		if (idCobrancas == null || idCobrancas.isEmpty()) {
			
			throw new ValidacaoException(
TipoMensagem.WARNING, "É necessário selecionar ao menos uma dívida!");
		}
		
		this.cobrancaService.confirmarBaixaManualDividas(idCobrancas);
		
		this.result.use(Results.json()).from(
			new ValidacaoVO(TipoMensagem.SUCCESS, "Baixa confirmada com sucesso!"),
			Constantes.PARAM_MSGS).recursive().serialize();
	}
	
	@Post
	@Rules(Permissao.ROLE_FINANCEIRO_BAIXA_BANCARIA_ALTERACAO)
	public void cancelarBaixaDividas(List<Long> idCobrancas) {
		
		if (idCobrancas == null || idCobrancas.isEmpty()) {
			
			throw new ValidacaoException(
TipoMensagem.WARNING, "É necessário selecionar ao menos uma dívida!");
		}
		
		this.cobrancaService.reverterBaixaManualDividas(idCobrancas);
		
		this.result.use(Results.json()).from(
			new ValidacaoVO(TipoMensagem.SUCCESS, "Baixa cancelada com sucesso!"),
			Constantes.PARAM_MSGS).recursive().serialize();
	}
	
}
