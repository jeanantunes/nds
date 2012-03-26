package br.com.abril.nds.controllers.financeiro;

import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.vo.CobrancaVO;
import br.com.abril.nds.client.vo.ValidacaoVO;
import br.com.abril.nds.controllers.exception.ValidacaoException;
import br.com.abril.nds.dto.ArquivoPagamentoBancoDTO;
import br.com.abril.nds.dto.ResumoBaixaBoletosDTO;
import br.com.abril.nds.model.financeiro.Boleto;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.service.BoletoService;
import br.com.abril.nds.service.LeitorArquivoBancoService;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.TipoMensagem;
import br.com.abril.nds.util.Util;
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
	
	@SuppressWarnings("unused")
	private HttpSession httpSession;
	
	private HttpServletRequest request;
	
	private ServletContext servletContext;
	
	@Autowired
	private BoletoService boletoService;
	
	@Autowired
	private LeitorArquivoBancoService leitorArquivoBancoService;
	
	private static final String FORMATO_DATA_DIRETORIO = "yyyy-MM-dd";
	
	private static final String DIRETORIO_TEMPORARIO_ARQUIVO_BANCO = "temp/arquivos_banco/";
	
	private static final String EXIBE_CAMPOS_BAIXA_AUTOMATICA_REQUEST_ATTRIBUTE = "exibeCamposBaixaAutomatica";
	
	private static final String RESUMO_BAIXA_AUTOMATICA_REQUEST_ATTRIBUTE = "resumoBaixaAutomaticaBoleto";
	
	public BaixaFinanceiraController(Result result, Localization localization,
									 HttpSession httpSession, HttpServletRequest request,
									 ServletContext servletContext) {
		
		this.result = result;
		this.localization = localization;
		this.httpSession = httpSession;
		this.request = request;
		this.servletContext = servletContext;
	}
	
	@Get
	public void baixa() {
		
		if (request.getAttribute(EXIBE_CAMPOS_BAIXA_AUTOMATICA_REQUEST_ATTRIBUTE) == null) {
			
			request.setAttribute(EXIBE_CAMPOS_BAIXA_AUTOMATICA_REQUEST_ATTRIBUTE, false);
		}
	}
	
	@Post
	public void baixa(UploadedFile uploadedFile, String valorFinanceiro) {
		
		request.setAttribute(EXIBE_CAMPOS_BAIXA_AUTOMATICA_REQUEST_ATTRIBUTE, true);
		
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
				boletoService.baixarBoletos(arquivoPagamento, valorFinanceiroFormatado,
											obterUsuario());
		
		} finally {
			
			//Deleta os arquivos dentro do diretório temporário
			deletarArquivoTemporario();
		}
		
		request.setAttribute(RESUMO_BAIXA_AUTOMATICA_REQUEST_ATTRIBUTE, resumoBaixaBoleto);
		
		result.forwardTo(BaixaFinanceiraController.class).baixa();
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
			
			validacao.setTipoMensagem(TipoMensagem.ERROR);
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

		validarBuscaBoleto(nossoNumero);

		CobrancaVO cobranca = this.boletoService.obterCobranca(nossoNumero);
		
		if (cobranca==null) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum registro encontrado.");
		} 
		
		result.use(Results.json()).from(cobranca,"result").recursive().serialize();
	}
	
	
	public void validarBuscaBoleto(String nossoNumero){
		if (nossoNumero==null || ("".equals(nossoNumero))){
		    throw new ValidacaoException(TipoMensagem.ERROR, "Digite o número do boleto.");
		}
    }
		
	
	
}
