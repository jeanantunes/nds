package br.com.abril.nds.controllers.financeiro;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.client.vo.ContasAPagarConsultaProdutoVO;
import br.com.abril.nds.dto.ContasAPagarConsultaProdutoDTO;
import br.com.abril.nds.dto.filtro.FiltroContasAPagarDTO;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.serialization.custom.FlexiGridJson;
import br.com.abril.nds.service.ContasAPagarService;
import br.com.abril.nds.service.FornecedorService;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;

/**
 * Classe responsável pelo controle das ações referentes à tela de Follow Up do
 * Sistema.
 * 
 * @author InfoA2 - Alex
 */

@Resource
@Path("financeiro/contasAPagar")
public class ContasAPagarController {
	
	@Autowired
	private Result result;
	
	@Autowired
	private FornecedorService fornecedorService;
	
	@Autowired
	private ContasAPagarService contasAPagarService;
	
	public ContasAPagarController(Result result) {
		super();
		this.result = result;
	}
	
	@Path("/")
	@Rules(Permissao.ROLE_FINANCEIRO_CONTAS_A_PAGAR)
	public void index() {
		
		List<Fornecedor> fornecedores = fornecedorService.obterFornecedores(
				true, SituacaoCadastro.ATIVO);
		result.include("fornecedores", fornecedores);
		
	}
	
	@Path("/pesquisar.json")
	public void pesquisar(FiltroContasAPagarDTO filtro){
		
		System.out.println("teste"+filtro.getProdutoEdicaoIDs().get(0));
		
	
		
		//result.use(FlexiGridJson.class).from(busca).total(busca.size()).serialize();
	
		
	}
	
	@Path("/pesquisarProduto.json")
	public void pesquisarProduto(FiltroContasAPagarDTO filtro){
		System.out.println("teste");
		
		List<ContasAPagarConsultaProdutoDTO> produtos = contasAPagarService.pesquisaProdutoContasAPagar(filtro.getProduto(), filtro.getEdicao());
		List<ContasAPagarConsultaProdutoVO> produtosVO = new ArrayList<ContasAPagarConsultaProdutoVO>();
		
		for(ContasAPagarConsultaProdutoDTO dto : produtos){
			
			produtosVO.add(new ContasAPagarConsultaProdutoVO(dto));
			
		}
		
		result.use(FlexiGridJson.class).from(produtosVO).total(produtosVO.size()).serialize();
		
	}
	
	
	
	
}

