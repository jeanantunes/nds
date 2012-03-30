package br.com.abril.nds.controllers.financeiro;

import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.vo.CobrancaVO;
import br.com.abril.nds.client.vo.ValidacaoVO;
import br.com.abril.nds.controllers.exception.ValidacaoException;
import br.com.abril.nds.dto.ArquivoPagamentoBancoDTO;
import br.com.abril.nds.dto.PagamentoDTO;
import br.com.abril.nds.dto.ResumoBaixaBoletosDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaBoletosCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaBoletosCotaDTO.OrdenacaoColunaBoletos;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.financeiro.Boleto;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.serialization.custom.PlainJSONSerialization;
import br.com.abril.nds.service.BoletoService;
import br.com.abril.nds.service.CalendarioService;
import br.com.abril.nds.service.DistribuidorService;
import br.com.abril.nds.service.LeitorArquivoBancoService;
import br.com.abril.nds.util.CellModel;
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
	private DistribuidorService distribuidorService;
	
	@Autowired
	private CalendarioService calendarioService;
	
	@Autowired
	private LeitorArquivoBancoService leitorArquivoBancoService;
	
	private static final String FORMATO_DATA_DIRETORIO = "yyyy-MM-dd";
	
	private static final String DIRETORIO_TEMPORARIO_ARQUIVO_BANCO = "temp/arquivos_banco/";

	private static final String FILTRO_PESQUISA_SESSION_ATTRIBUTE = "filtroPesquisaConsultaBoletos";
	   
	public BaixaFinanceiraController(Result result, Localization localization,
									 HttpSession httpSession, ServletContext servletContext) {
		
		this.result = result;
		this.localization = localization;
		this.httpSession = httpSession;
		this.servletContext = servletContext;
	}
	
	
	
	
	@Get
	public void baixa() {
		
	}
	
	
	
	@Post
	public void realizarBaixaAutomatica(UploadedFile uploadedFile, String valorFinanceiro) {
		
		validarEntradaDados(uploadedFile, valorFinanceiro);
		
		BigDecimal valorFinanceiroFormatado = new BigDecimal(valorFinanceiro);
		
		ResumoBaixaBoletosDTO resumoBaixaBoleto = null;
		
		try {
		
			//Grava o arquivo em disco e retorna o File do arquivo
			File fileArquivoBanco = gravarArquivoTemporario(uploadedFile);
			
			ArquivoPagamentoBancoDTO arquivoPagamento =
					leitorArquivoBancoService.obterPagamentosBanco(fileArquivoBanco,
																   uploadedFile.getFileName());
			
			resumoBaixaBoleto = 
				boletoService.baixarBoletosAutomatico(arquivoPagamento, valorFinanceiroFormatado,
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
			
			//Valida se o valor financeiro é numérico
			if (!Util.isNumeric(valorFinanceiro)) {
			
				listaMensagens.add("O campo [Valor Financeiro] deve ser numérico!");
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
	
	@Post
	@Path("/buscaBoleto")
	public void buscaBoleto(String nossoNumero){
		
		if ((nossoNumero==null)||("".equals(nossoNumero.trim()))){
		    throw new ValidacaoException(TipoMensagem.WARNING, "Digite o número da cota ou o número do boleto.");
		}
		
		CobrancaVO cobranca = this.boletoService.obterDadosCobranca(nossoNumero);
		if (cobranca==null) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum registro encontrado.");
		} 
		result.use(Results.json()).from(cobranca,"result").recursive().serialize();
	}
	
	@Post
	@Path("/buscaBoletos")
	public void buscaBoletos(Integer numCota,
			                 String sortorder, 
			                 String sortname, 
			                 int page, 
			                 int rp){

		if ((numCota==null)||(numCota<=0)){
		    throw new ValidacaoException(TipoMensagem.WARNING, "Digite o número da cota ou o número do boleto.");
		}

        //CONFIGURAR PAGINA DE PESQUISA
		FiltroConsultaBoletosCotaDTO filtroAtual = new FiltroConsultaBoletosCotaDTO(numCota);
		PaginacaoVO paginacao = new PaginacaoVO(page, rp, sortorder);
		filtroAtual.setPaginacao(paginacao);
		filtroAtual.setOrdenacaoColuna(Util.getEnumByStringValue(OrdenacaoColunaBoletos.values(), sortname));
	
		FiltroConsultaBoletosCotaDTO filtroSessao = (FiltroConsultaBoletosCotaDTO) this.httpSession.getAttribute(FILTRO_PESQUISA_SESSION_ATTRIBUTE);
		
		if (filtroSessao != null && !filtroSessao.equals(filtroAtual)) {
		
			filtroAtual.getPaginacao().setPaginaAtual(1);
		}
		
		this.httpSession.setAttribute(FILTRO_PESQUISA_SESSION_ATTRIBUTE, filtroAtual);

		//BUSCA BOLETOS
		List<Boleto> boletos = this.boletoService.obterBoletosPorCota(filtroAtual);
		
		//CARREGA DIRETO DA ENTIDADE PARA A TABELA
		List<CellModel> listaModelo = new LinkedList<CellModel>();
		
		if (boletos.size()==0) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum registro encontrado.");
		} 
		
		
		for (Boleto boleto : boletos){	
			listaModelo.add(new CellModel(1,
										  (boleto.getNossoNumero()!=null?boleto.getNossoNumero():""),
										  "",
										  (boleto.getDataEmissao()!=null?DateUtil.formatarData(boleto.getDataEmissao(),"dd/MM/yyyy"):""),
										  (boleto.getDataVencimento()!=null?DateUtil.formatarData(boleto.getDataVencimento(),"dd/MM/yyyy"):""),
										  (boleto.getValor()!=null?boleto.getValor().toString():""),
										  "",
										  ""
                      					  )
                            );
		}
		
		
		TableModel<CellModel> tm = new TableModel<CellModel>();

		//DEFINE TOTAL DE REGISTROS NO TABLEMODEL
		tm.setTotal( (int) this.boletoService.obterQuantidadeBoletosPorCota(filtroAtual));
		
		//DEFINE CONTEUDO NO TABLEMODEL
		tm.setRows(listaModelo);
		
		//DEFINE PAGINA ATUAL NO TABLEMODEL
		tm.setPage(filtroAtual.getPaginacao().getPaginaAtual());
		
		
		//PREPARA RESULTADO PARA A VIEW (HASHMAP)
		Map<String, TableModel<CellModel>> resultado = new HashMap<String, TableModel<CellModel>>();
		resultado.put("TblModelBoletos", tm);
		
		//RETORNA HASHMAP EM FORMATO JSON PARA A VIEW
		result.use(Results.json()).withoutRoot().from(resultado).recursive().serialize();
	
	}
	
		
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
		
        BigDecimal valorFormatado = CurrencyUtil.converterValor(valor);
        BigDecimal jurosFormatado = CurrencyUtil.converterValor(juros);
        BigDecimal multaFormatado = CurrencyUtil.converterValor(multa);
        BigDecimal descontoFormatado = CurrencyUtil.converterValor(desconto);

        if (descontoFormatado.compareTo(
        		valorFormatado.add(jurosFormatado).add(multaFormatado)) == 1) {
        	
        	throw new ValidacaoException(TipoMensagem.WARNING,
        		"O desconto não deve ser maior do que o valor a pagar.");
        }

        PagamentoDTO pagamento = new PagamentoDTO();
		pagamento.setDataPagamento(dataNovoMovimento);
		pagamento.setNossoNumero(nossoNumero);
		pagamento.setNumeroRegistro(null);
		pagamento.setValorPagamento(valorFormatado);
		pagamento.setValorJuros(jurosFormatado);
		pagamento.setValorMulta(multaFormatado);
		pagamento.setValorDesconto(descontoFormatado);

		boletoService.baixarBoleto(TipoBaixaCobranca.MANUAL, pagamento, obterUsuario(),
								   null,distribuidor.getPoliticaCobranca() , distribuidor,
								   dataNovoMovimento, null);
			
		result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Boleto "+nossoNumero+" baixado com sucesso."),Constantes.PARAM_MSGS).recursive().serialize();
	}
	

}
