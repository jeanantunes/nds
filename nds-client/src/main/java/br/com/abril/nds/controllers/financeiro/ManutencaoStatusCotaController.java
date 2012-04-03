package br.com.abril.nds.controllers.financeiro;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.model.cadastro.MotivoAlteracaoSituacao;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;

/**
 * Controller responsável pela Manutenção do Status da Cota.
 * 
 * @author Discover Technology
 *
 */
@Resource
@Path("/financeiro/manutencaoStatusCota")
public class ManutencaoStatusCotaController {

	@Autowired
	private Result result;
	
	@Autowired
	private HttpSession httpSession;
	
	@Autowired
	private HttpServletResponse httpServletResponse;
	
	@Get
	@Path("/")
	public void index() {

		this.carregarCombos();
	}
	
	@Post
	@Path("/pesquisar")
	public void pesquisar() {
		
		
	}
	
	@Post
	@Path("/novo")
	public void novo() {
		
		
	}
	
	@Post
	@Path("/novo/confirmar")
	public void confirmarNovo() {
		
		
	}
	
	/*
	 * Carrega os combos utilizados na tela.
	 */
	private void carregarCombos() {
		
		this.carregarComboStatusCota();
		this.carregarComboMotivoStatusCota();
	}
	
	/*
	 * Carrega o combo de status da cota. 
	 */
	private void carregarComboStatusCota() {
		
		List<ItemDTO<SituacaoCadastro, String>> listaSituacoesStatusCota =
			new ArrayList<ItemDTO<SituacaoCadastro, String>>();
		
		for (SituacaoCadastro situacaoCadastro : SituacaoCadastro.values()) {
			
			listaSituacoesStatusCota.add(
				new ItemDTO<SituacaoCadastro, String>(situacaoCadastro, situacaoCadastro.toString())
			);
		}
		
		result.include("listaSituacoesStatusCota", listaSituacoesStatusCota);
	}
	
	/*
	 * Carrega o combo de status da cota. 
	 */
	private void carregarComboMotivoStatusCota() {
		
		List<ItemDTO<MotivoAlteracaoSituacao, String>> listaMotivosStatusCota =
			new ArrayList<ItemDTO<MotivoAlteracaoSituacao, String>>();
		
		for (MotivoAlteracaoSituacao motivoAlteracaoSituacao : MotivoAlteracaoSituacao.values()) {
			
			listaMotivosStatusCota.add(
				new ItemDTO<MotivoAlteracaoSituacao, String>(
					motivoAlteracaoSituacao, motivoAlteracaoSituacao.toString())
			);
		}
		
		result.include("listaMotivosStatusCota", listaMotivosStatusCota);
	}
	
}
