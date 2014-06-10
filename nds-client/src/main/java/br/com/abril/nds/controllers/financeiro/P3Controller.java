package br.com.abril.nds.controllers.financeiro;

import java.io.File;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.controllers.BaseController;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.service.P3Service;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.interceptor.download.Download;
import br.com.caelum.vraptor.interceptor.download.FileDownload;
import br.com.caelum.vraptor.view.Results;

@Resource
@Path("/financeiro/p3")
@Rules(Permissao.ROLE_FINANCEIRO_P3)
public class P3Controller extends BaseController {
	
	@Autowired
	private P3Service serviceP3;
	
	@Autowired
	private Result result;
	
	@Autowired
	private HttpServletResponse httpResponse;
	
	@Path("/")
	public void index(){
		
	}

	@Post
	public void validarQtdRegistros (Date dataInicial, Date dataFinal,String opcaoDeRelatorio){
		
		final Integer count = serviceP3.countBusca(dataInicial, dataFinal,opcaoDeRelatorio);

		
		Object o = new Object(){
			Integer quantidadeGerada=count;
		};
        result.use(Results.json()).withoutRoot().from(o).serialize();
	}
	
	@Get
	public Download gerarP3(Date dataInicial, Date dataFinal, String opcaoDeRelatorio){

		String contentType = "application/octet-stream ";
	    String filename = "arquivoP3.txt";
	    
	    File gerarArquivoP3 = null;
	    if(opcaoDeRelatorio.equals("p3")){
	    	gerarArquivoP3 = serviceP3.gerarTxt(dataInicial, dataFinal);	    	
	    }else if(opcaoDeRelatorio.equals("movCompleta")){
	    	gerarArquivoP3 = serviceP3.gerarMovimentacaoCompletaTxt(dataInicial, dataFinal);
	    }
		
		return new FileDownload(gerarArquivoP3, contentType, filename);
	}
}
