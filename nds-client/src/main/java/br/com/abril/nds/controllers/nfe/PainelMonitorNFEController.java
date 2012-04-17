package br.com.abril.nds.controllers.nfe;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;

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

	
	@Path("/")
	public void index(){
		
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
	
	
	@Path("")
	public void pesquisar() {
		
		
		
	}
	
}
