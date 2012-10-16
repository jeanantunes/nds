package br.com.abril.nds.controllers.financeiro;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.client.vo.ContasAPagarConsultaPorProdutoVO;
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
		
		List<Fornecedor> fornecedores = fornecedorService.obterFornecedores(true, SituacaoCadastro.ATIVO);
		result.include("fornecedores", fornecedores);
		
	}
	
	@Path("/pesquisar.json")
	public void pesquisar(FiltroContasAPagarDTO filtro, String sortname, String sortorder, int rp, int page) {
		
		List<ContasAPagarConsultaPorProdutoVO> listVO = new ArrayList<ContasAPagarConsultaPorProdutoVO>();
		
		// Mock
		ContasAPagarConsultaPorProdutoVO vo = new ContasAPagarConsultaPorProdutoVO();
		vo.setProdutoEdicaoId("1");
		vo.setRctl("15/12/2011");
		vo.setCodigo("9090");
		vo.setProduto("Veja");
		vo.setEdicao("4343");
		vo.setTipo("P");
		vo.setReparte("20");
		vo.setSuplementacao("5");
		vo.setEncalhe("10");
		vo.setVenda("10");
		vo.setFaltasSobras("848");
		vo.setDebitosCreditos("848");
		vo.setSaldoAPagar("2.000,00");
		vo.setFornecedor("FC");
		vo.setDataLcto("12/02/2012");
		vo.setDataFinal("10/04/2012");
		listVO.add(vo);
		
		result.use(FlexiGridJson.class).from(listVO).total(listVO.size()).serialize();
	}
	

	@Path("/pesquisarProduto.json")
	public void pesquisarProduto(FiltroContasAPagarDTO filtro) {

		List<ContasAPagarConsultaProdutoDTO> produtos = contasAPagarService.pesquisarProdutos(filtro);
		List<ContasAPagarConsultaProdutoVO> produtosVO = new ArrayList<ContasAPagarConsultaProdutoVO>();
		
		for(ContasAPagarConsultaProdutoDTO dto : produtos){
			produtosVO.add(new ContasAPagarConsultaProdutoVO(dto));
		}
		
		result.use(FlexiGridJson.class).from(produtosVO).total(produtosVO.size()).serialize();
	}
	
	
	@Path("/pesquisarParcial.json")
	public void pesquisarParcial(FiltroContasAPagarDTO filtro, String sortname, String sortorder, int rp, int page){
		
		
	}
	
}

