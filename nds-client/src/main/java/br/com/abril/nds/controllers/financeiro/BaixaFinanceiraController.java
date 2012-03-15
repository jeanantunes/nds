package br.com.abril.nds.controllers.financeiro;

import java.io.File;
import java.io.FileOutputStream;

import javax.servlet.http.HttpSession;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.ArquivoPagamentoBancoDTO;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.service.BoletoService;
import br.com.abril.nds.service.LeitorRetornoBancoService;
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
	public void baixarBoletosAutomatico(UploadedFile uploadedFile) {
		
		try {
			
			File file = new File("/" + uploadedFile.getFileName());
			
			IOUtils.copyLarge(uploadedFile.getFile(), new FileOutputStream(file));
			
			ArquivoPagamentoBancoDTO arquivoPagamento =
					leitorRetornoBancoService.obterPagamentosBanco(file);
			
			boletoService.baixarBoletos(arquivoPagamento, obterUsuario());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private Usuario obterUsuario() {
		//TODO:
		Usuario usuario = new Usuario();
		usuario.setId(1L);
		
		return usuario;
	}
	
}
