package br.com.abril.nds.controllers.financeiro;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.controllers.BaseController;
import br.com.abril.nds.dto.ExtratoEdicaoArquivoP7DTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.service.IntegracaoFiscalService;
import br.com.abril.nds.util.export.FileExporter;
import br.com.abril.nds.util.export.FileExporter.FileType;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.interceptor.download.Download;
import br.com.caelum.vraptor.interceptor.download.FileDownload;
import br.com.caelum.vraptor.view.Results;

@Resource
@Path("/financeiro/integracaoFiscalP7")
@Rules(Permissao.ROLE_FINANCEIRO_INTEGRACAO_FISCAL_P7)
public class IntegracaoFiscalP7Controller extends BaseController{
	
	@Autowired
	private IntegracaoFiscalService integracaoFiscalService;
	
	@Autowired
	private Result result;
	
	@Autowired
	private HttpServletResponse httpResponse;
	
	@Path("/")
	public void index(){
	}
	
	@Post
	public void verificarExportar(String mes, String ano){

		Calendar c = GregorianCalendar.getInstance();
		
		try {
			c.set(Calendar.MONTH, Integer.parseInt(mes));
			c.set(Calendar.YEAR, Integer.parseInt(ano));
		} catch (Exception e) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Data inválida, verifique novamente.");
		}

		final Integer count = integracaoFiscalService.countGeracaoArquivoP7(c.getTime());

		Object o = new Object(){
			Integer quantidadeGerada=count;
		};
        result.use(Results.json()).withoutRoot().from(o).serialize();
//        result.use(Results.json()).from(listaRegiao, "listaRegiao").recursive().serialize();
	}

	@Path("/exportar")
	public Download  exportar(String mes, String ano){
		
		Calendar c = GregorianCalendar.getInstance();
		c.set(Calendar.MONTH, Integer.parseInt(mes));
		c.set(Calendar.YEAR, Integer.parseInt(ano));

		String contentType = "application/octet-stream ";
        String filename = "arquivoP7.txt";
        
        File gerarArquivoP7 = integracaoFiscalService.gerarArquivoP7(c.getTime());
        
        return new FileDownload(gerarArquivoP7, contentType, filename);
	}
	
	@Post
	@Path("/exportarXLS")
	public void exportarXLS(String mes, String ano) throws IOException {
		
		FileType fileType = FileType.XLS;
		Calendar c = GregorianCalendar.getInstance();
		
		try {
			c.set(Calendar.MONTH, Integer.parseInt(mes));
			c.set(Calendar.YEAR, Integer.parseInt(ano));
		} catch (Exception e) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Data inválida, verifique novamente.");
		}

        List<ExtratoEdicaoArquivoP7DTO> p7dto = integracaoFiscalService.inventarioP7(c.getTime());
		
        FileExporter.to("Integração Fiscal P7", fileType).inHTTPResponse(this.getNDSFileHeader(), null, p7dto, ExtratoEdicaoArquivoP7DTO.class, this.httpResponse);

        result.nothing();
	}
	
}
