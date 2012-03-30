package br.com.abril.nds.controllers.expedicao;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.repository.CotaAusenteRepository;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

@Resource
public class CotaAusenteController {

	protected static final String MSG_PESQUISA_SEM_RESULTADO = "Não há resultados para a pesquisa realizada.";
	
	@Autowired
	private CotaAusenteRepository cotaAusenteRepository;
	
	
	private static final Logger LOG = LoggerFactory
			.getLogger(CotaAusenteController.class);
	
	private final Result result;
	private final HttpSession session;
	
	public CotaAusenteController(Result result, HttpSession session) {
		this.result=result;
		this.session = session;
	}
	
	public void cotaAusente() {
		
	}
	
	/**
	 * Inicializa dados da tela
	 */
	public void index() {
		
		result.forwardTo(CotaAusenteController.class).cotaAusente();
	}
	
	/**
	 * Realiza pesquisa de Cotas Ausentes
	 * 
	 * @param dataAusente
	 * @param numCota
	 * @param nomeCota
	 * @param box
	 */
	public void pesquisarCotasAusentes(String dataAusencia, Integer numCota, String nomeCota, String box) {
		
		System.out.println("\n\n\n\n_____Guilherme" +
				"\nDataAusente-" + dataAusencia+
				"\numCota-"+numCota
				+"\nnomeCota-" +nomeCota
				+"\nbox-" +box);
		
		
		//TODO  validar parametros
		
		//TODO gerar Grid
		
		//TODO retornar grid + mensagens
		
		Object[] retorno = new Object[3];
		retorno[0] = "grid";
		retorno[1] = "mensagens";
		retorno[2] = "status";
		
		result.use(Results.json()).withoutRoot().from(retorno).serialize();		
	}
}
