package br.com.abril.nds.controllers.devolucao;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;

/**
 * Controller responsável pela Matriz de Recolhimento.
 * 
 * @author Discover Technology
 *
 */
@Resource
@Path("/devolucao/balanceamentoMatriz")
public class MatrizRecolhimentoController {

	@Autowired
	private Result result;
	
	@Autowired
	private HttpSession httpSession;
	
	@Get
	@Path("/")
	public void index() {
		
		
	}
	
	@Post
	@Path("/pesquisar")
	public void pesquisar() {
		
		
	}
	
	@Post
	@Path("/configurarNovaDataRecolhimento")
	public void configurarNovaDataRecolhimento() {
		
		
	}
	
	@Post
	@Path("/reprogramar")
	public void reprogramar() {
		
		
	}
	
	@Post
	@Path("/exibirMatrizBalanceamento")
	public void exibirMatrizBalanceamento() {
		
		
	}
	
	@Post
	@Path("/exibirMatrizBalanceamentoPorDia")
	public void exibirMatrizBalanceamentoDoDia() {
		
		
	}
	
	@Post
	@Path("/balancearPorEditor")
	public void balancearPorEditor() {
		
		
	}
	
	@Post
	@Path("/balancearPorValor")
	public void balancearPorValor() {
		
		
	}
	
	@Post
	@Path("/voltarConfiguracaoOriginal")
	public void voltarConfiguracaoOriginal() {
		
		
	}
	
	@Post
	@Path("/confirmar")
	public void confirmar() {
		
		
	}
	
}