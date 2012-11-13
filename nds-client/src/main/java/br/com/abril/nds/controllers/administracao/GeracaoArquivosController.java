package br.com.abril.nds.controllers.administracao;

import java.util.Date;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.integracao.ems0197.route.EMS0197Route;
import br.com.abril.nds.integracao.ems0198.route.EMS0198Route;
import br.com.abril.nds.integracao.service.DistribuidorService;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.model.seguranca.Usuario;
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
public class GeracaoArquivosController {

	@Autowired
	private ApplicationContext applicationContext;
	
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
	@Rules(Permissao.ROLE_ADMINISTRACAO_GERACAO_ARQUIVO)
	public void index() {
		
		
	}

	@Post
	public void gerar(Date dataLctoPrevisto, String operacao) {
		// Inclui o pacote na classe
		
		int qtdArquivosGerados = 0;
		try {
			if (operacao.equals("REPARTE")) {
				qtdArquivosGerados = route197.execute(getUsuario().getLogin(),
						dataLctoPrevisto);
			} else {
				qtdArquivosGerados = route198.execute(getUsuario().getLogin(),
						dataLctoPrevisto);
			}
		} catch	(RuntimeException e) {
			if (e.getMessage().equals("Nenhum registro encontrado!")) {
				qtdArquivosGerados = 0;
			}
		}
		
		result.use(Results.json()).from(Integer.valueOf(qtdArquivosGerados), "result").serialize();
	}

	/**
	 * Retorna o usuário logado
	 * @return
	 */
	// TODO: Implementar quando funcionar
	private Usuario getUsuario() {
		Usuario usuario = new Usuario();
		usuario.setId(1L);
		usuario.setNome("Jornaleiro da Silva");
		usuario.setLogin("jorlaleiroLogin");
		return usuario;
	}	
}
