package br.com.abril.nds.controllers.devolucao;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.service.DistribuidorService;
import br.com.abril.nds.service.FornecedorService;
import br.com.abril.nds.util.DateUtil;
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
	
	@Autowired
	private FornecedorService fornecedorService;
	
	@Get
	@Path("/")
	public void index() {
		
		List<Fornecedor> fornecedores = this.fornecedorService.obterFornecedores(true, SituacaoCadastro.ATIVO);

		result.include("fornecedores", fornecedores);
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