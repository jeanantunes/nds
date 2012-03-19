package br.com.abril.nds.controllers.financeiro;

import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;

import javax.servlet.http.HttpSession;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.controllers.exception.ValidacaoException;
import br.com.abril.nds.controllers.lancamento.FuroProdutoController;
import br.com.abril.nds.dto.ArquivoPagamentoBancoDTO;
import br.com.abril.nds.dto.ResumoBaixaBoletosDTO;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.service.BoletoService;
import br.com.abril.nds.service.LeitorRetornoBancoService;
import br.com.abril.nds.util.TipoMensagem;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.core.Localization;
import br.com.caelum.vraptor.interceptor.multipart.UploadedFile;
import br.com.caelum.vraptor.ioc.spring.VRaptorRequestHolder;
import br.com.caelum.vraptor.view.Results;

@Resource
@Path("/financeiro")
public class BaixaFinanceiraController {

	private Result result;
	
	private Localization localization;
	
	private HttpSession httpSession;
	
	@Autowired
	private BoletoService boletoService;
	
	@Autowired
	private LeitorRetornoBancoService leitorRetornoBancoService;
	
	public BaixaFinanceiraController(Result result, Localization localization,
									 HttpSession httpSession) {
		
		this.result = result;
		this.localization = localization;
		this.httpSession = httpSession;
	}
	
	@Get
	public void baixa() {
		
	}
	
	@Post
	public void baixa(UploadedFile uploadedFile, String valorFinanceiro) {
			
		//TODO: validar tamanho arquivo, caminho para gravar, obrigatoriedade dos campos, tamanho do nome do arquivo, etc
		
		//TODO: refactor nomes classes
		
		String pathAplicacao = VRaptorRequestHolder.currentRequest().getServletContext().getRealPath("");
		
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
				leitorRetornoBancoService.obterPagamentosBanco(fileArquivoRetorno,
															   uploadedFile.getFileName());
		
		ResumoBaixaBoletosDTO resumoBaixaBoleto = 
			boletoService.baixarBoletos(arquivoPagamento, new BigDecimal(valorFinanceiro),
										obterUsuario());
		
		result.include("resumoBaixaBoleto", resumoBaixaBoleto);
		
		result.forwardTo(BaixaFinanceiraController.class).baixa();
	}
	
	private Usuario obterUsuario() {
		
		//TODO: obter usuário
		
		Usuario usuario = new Usuario();
		
		usuario.setId(1L);
		
		return usuario;
	}
	
}
