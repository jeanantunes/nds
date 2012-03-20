package br.com.abril.nds.controllers.financeiro;

import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.vo.ValidacaoVO;
import br.com.abril.nds.controllers.exception.ValidacaoException;
import br.com.abril.nds.dto.ArquivoPagamentoBancoDTO;
import br.com.abril.nds.dto.ResumoBaixaBoletosDTO;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.service.BoletoService;
import br.com.abril.nds.service.LeitorArquivoBancoService;
import br.com.abril.nds.util.TipoMensagem;
import br.com.abril.nds.util.Util;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.core.Localization;
import br.com.caelum.vraptor.interceptor.multipart.UploadedFile;

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
		
		//TODO: validar tamanho arquivo, caminho para gravar, tamanho do nome do arquivo
		
		validarEntradaDados(uploadedFile, valorFinanceiro);
		
		BigDecimal valorFinanceiroFormatado = new BigDecimal(valorFinanceiro);
					
		String pathAplicacao = servletContext.getRealPath("");
		
		pathAplicacao = pathAplicacao.replace("\\", "/");
		
		File fileDir = new File(pathAplicacao, "temp/arquivo");
		
		fileDir.mkdirs();
		
		File fileArquivoRetorno = new File(fileDir, uploadedFile.getFileName());
		
		//TODO:
		try {
			IOUtils.copyLarge(uploadedFile.getFile(), new FileOutputStream(fileArquivoRetorno));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		ArquivoPagamentoBancoDTO arquivoPagamento =
				leitorArquivoBancoService.obterPagamentosBanco(fileArquivoRetorno,
															   uploadedFile.getFileName());
		
		ResumoBaixaBoletosDTO resumoBaixaBoleto = 
			boletoService.baixarBoletos(arquivoPagamento, valorFinanceiroFormatado,
										obterUsuario());
		
		request.setAttribute(RESUMO_BAIXA_AUTOMATICA_REQUEST_ATTRIBUTE, resumoBaixaBoleto);
		
		result.forwardTo(BaixaFinanceiraController.class).baixa();
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
	
}
