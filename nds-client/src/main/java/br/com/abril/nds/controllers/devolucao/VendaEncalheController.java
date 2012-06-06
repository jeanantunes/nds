package br.com.abril.nds.controllers.devolucao;

import java.io.OutputStream;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.service.VendaEncalheService;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;

/**
 * 
 * Classe responsável pelo controle das ações referentes a
 * tela de venda de encalhe.
 * 
 * @author Discover Technology
 *
 */

@Resource
@Path("/devolucao/vendaEncalhe")
public class VendaEncalheController {
	
	
	 private HttpServletResponse httpResponse;
	 
	 @Autowired
	 private VendaEncalheService vendaEncalheService;
	 
	 
	
	/**
	 * Construtor da classe
	 * @param result
	 * @param session
	 * @param httpResponse
	 */
	public VendaEncalheController(Result result, HttpServletResponse httpResponse) {
		this.httpResponse = httpResponse;
	}
	
	
	
	@Path("/")
	public void index() {}
	
	
	
	/**
	 * Exibe o contrato em formato PDF.
	 * @param idCota
	 * @param dataInicio
	 * @param dataFim
	 * @throws Exception
	 */
	@Get
	@Path("/imprimeSlipVendaEncalhe")
	public void imprimeSlipVendaEncalhe(Long idCota, Date dataInicio, Date dataFim) throws Exception{

		byte[] b = this.vendaEncalheService.geraImpressaoVendaEncalhe(idCota,dataInicio,dataFim);

		this.httpResponse.setContentType("application/pdf");
		this.httpResponse.setHeader("Content-Disposition", "attachment; filename=SlipVE.pdf");

		OutputStream output = this.httpResponse.getOutputStream();
		output.write(b);

		httpResponse.flushBuffer();
	}
	
}
