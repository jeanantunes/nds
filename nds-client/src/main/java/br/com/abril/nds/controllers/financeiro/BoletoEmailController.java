package br.com.abril.nds.controllers.financeiro;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.controllers.BaseController;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.financeiro.BoletoEmail;
import br.com.abril.nds.service.BoletoEmailService;
import br.com.abril.nds.service.BoletoService;
import br.com.abril.nds.vo.ValidacaoVO;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

/**
 * Classe responsável pelo controle das ações referentes à
 * tela de chamadão de publicações.
 * 
 * @author Discover Technology
 */
@Resource
@Path("financeiro/boletoEmail")
public class BoletoEmailController extends BaseController {
	
	@Autowired
	private Result result;

	@Autowired
	protected BoletoService boletoService;
	
	@Autowired
	protected BoletoEmailService boletoEmailService;
	
	@Autowired
	private HttpSession session;
	
	private static final String STATUS_ENVIO_FINALIZADO = "ENVIO_FINALIZADO";
	
	private static final String STATUS_BOLETO_EMAIL_SESSION = "statusCobrancaCotaSession";
	
	@Path("/")
	public void index() {
		
	}
	
	@Post
	public void emitirBoletosFechamentoEncalhe(){

		List<String> mensagensBoletosNaoEmitidos = new ArrayList<String>();
		
		mensagensBoletosNaoEmitidos.add("Boletos/Recibos não enviados:");
		
		boolean boletosNaoEmitidos = false;
			
		List<BoletoEmail> listaBoletoEmail = this.boletoEmailService.buscarTodos();
		
		if (listaBoletoEmail.isEmpty()){
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Não existem boletos pendentes de emissão.");
		}
		
		int totalBoletosEmitir = listaBoletoEmail.size();
		
		int boletosEmitidos = 0;
		
		for(BoletoEmail bm : listaBoletoEmail){
			
			this.session.setAttribute(STATUS_BOLETO_EMAIL_SESSION, "Enviando boleto " + (++boletosEmitidos) + " de " + totalBoletosEmitir);

		    try{

				this.boletoEmailService.enviarBoletoEmail(bm);
			}	
            catch(Exception e){
            	
            	e.printStackTrace();
            	
            	Cota cota = bm.getCobranca().getCota();
        	
        	    mensagensBoletosNaoEmitidos.add("Cota "+cota.getNumeroCota()+" - "+cota.getPessoa().getNome());
        	    
        	    boletosNaoEmitidos = true;
            }
		}
		
		this.session.setAttribute(STATUS_BOLETO_EMAIL_SESSION, STATUS_ENVIO_FINALIZADO);
		
		if (boletosNaoEmitidos){
			
			this.result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.WARNING, mensagensBoletosNaoEmitidos), "result").recursive().serialize();
		}
		else{
			
			this.result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Boletos pendentes emitidos com sucesso!"), "result").recursive().serialize();
		}
	}
	
	@Post
	public void obterStatusEmissaoBoletosFechamentoEncalhe() {
		
		String status = (String) this.session.getAttribute(STATUS_BOLETO_EMAIL_SESSION);
		
		result.use(Results.json()).withoutRoot().from(status==null?"Enviando boletos por email..." : status).recursive().serialize();
	}
}
