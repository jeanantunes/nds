package br.com.abril.nds.controllers.financeiro;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * Classe responsável pelo controle das ações referentes à tela de chamadão de
 * publicações.
 * 
 * @author Discover Technology
 */
@Resource
@Path("financeiro/boletoEmail")
public class BoletoEmailController extends BaseController {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(BoletoEmailController.class);
	
	@Autowired
	private Result result;

	@Autowired
	protected BoletoService boletoService;
	
	@Autowired
	protected BoletoEmailService boletoEmailService;
	
	private static final String STATUS_ENVIO_FINALIZADO = "ENVIO_FINALIZADO";
	
	private static final String KEY_ENVIO_BOLETO_EMAIL = "envioBoletoEmail";
	
	private static final ConcurrentMap<String, String> CACHE_ENVIO_BOLETO = new ConcurrentHashMap<>();
	
	@Path("/")
	public void index() {
		
		CACHE_ENVIO_BOLETO.clear();
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
		
		CACHE_ENVIO_BOLETO.clear();
		
		for(BoletoEmail bm : listaBoletoEmail){
			
			String status = "Enviando boleto " + (++boletosEmitidos) + " de " + totalBoletosEmitir;
			
			CACHE_ENVIO_BOLETO.put(KEY_ENVIO_BOLETO_EMAIL, status);
			
		    try{

				this.boletoEmailService.enviarBoletoEmail(bm);

				LOGGER.info("Boleto [" + bm.getCobranca().getNossoNumero() + "] enviado com sucesso, para a cota: " + bm.getCobranca().getCota().getNumeroCota());

			} catch(Exception e) {
            	
            	Cota cota = bm.getCobranca().getCota();
        	
        	    mensagensBoletosNaoEmitidos.add("Cota "+cota.getNumeroCota()+" - "+cota.getPessoa().getNome());
        	    
                LOGGER.error("Boleto [" + bm.getCobranca().getNossoNumero() + "] não enviado, para a cota: "
                    + bm.getCobranca().getCota().getNumeroCota(), e);
        	    

        	    boletosNaoEmitidos = true;
            }
		}
		
		CACHE_ENVIO_BOLETO.put(KEY_ENVIO_BOLETO_EMAIL, STATUS_ENVIO_FINALIZADO);
		
		if (boletosNaoEmitidos){
			
			this.result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.WARNING, mensagensBoletosNaoEmitidos), "result").recursive().serialize();
		}
		else{
			
			this.result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Boletos pendentes emitidos com sucesso!"), "result").recursive().serialize();
		}
	}
	
	@Post
	public void obterStatusEmissaoBoletosFechamentoEncalhe() {
		
		String status = CACHE_ENVIO_BOLETO.get(KEY_ENVIO_BOLETO_EMAIL);
		
		result.use(Results.json()).withoutRoot().from(status==null?"Enviando boletos por email..." : status).recursive().serialize();
	}
}
