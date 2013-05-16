package br.com.abril.nds.controllers.administracao;

import java.util.Date;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.controllers.BaseController;
import br.com.abril.nds.integracao.ems0129.route.EMS0129Route;
import br.com.abril.nds.integracao.ems0197.route.EMS0197Route;
import br.com.abril.nds.integracao.ems0198.route.EMS0198Route;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.service.integracao.DistribuidorService;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

/**
 * @author InfoA2
 * Controller de Cadastro de Tipo de Notas
 */
@Resource
@Path("/administracao/geracaoArquivos")
@Rules(Permissao.ROLE_ADMINISTRACAO_GERACAO_ARQUIVO)
public class GeracaoArquivosController extends BaseController {

	@Autowired
	private ApplicationContext applicationContext;

	@Autowired
	private EMS0129Route route129;		

	@Autowired
	private EMS0197Route route197;		
	
	@Autowired
	private EMS0198Route route198;		
	
	@Autowired
	DistribuidorService distribuidorService;
	
	@Autowired
	private Result result;
	
	@Autowired
	private HttpSession session;
	
	@Autowired
	private HttpServletResponse httpServletResponse;
	
	@Path("/")
	public void index() {
	}

	@Post
	public void gerar(Date dataLctoPrevisto, String operacao) {
		// Inclui o pacote na classe
		
		int qtdArquivosGerados = 0;
		try {
			if (operacao.equals("PICKING")) {
				route129.execute(getUsuarioLogado().getLogin(), dataLctoPrevisto);
				qtdArquivosGerados = 1; 
			} else if (operacao.equals("REPARTE")) {
				qtdArquivosGerados = route197.execute(getUsuarioLogado().getLogin(),
						dataLctoPrevisto);
			} else {
				qtdArquivosGerados = route198.execute(getUsuarioLogado().getLogin(),
						dataLctoPrevisto);
			}
		} catch	(RuntimeException e) {
			if (e.getMessage().equals("Nenhum registro encontrado!")) {
				qtdArquivosGerados = 0;
			}
		}
		
		result.use(Results.json()).from(Integer.valueOf(qtdArquivosGerados), "result").serialize();
	}

}
