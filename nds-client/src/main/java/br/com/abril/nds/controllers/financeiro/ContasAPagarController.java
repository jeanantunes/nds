package br.com.abril.nds.controllers.financeiro;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.client.vo.ContasAPagarConsultaPorProdutoVO;
import br.com.abril.nds.client.vo.ContasAPagarConsultaProdutoVO;
import br.com.abril.nds.dto.ContasAPagarConsultaProdutoDTO;
import br.com.abril.nds.dto.ContasApagarConsultaPorProdutoDTO;
import br.com.abril.nds.dto.VisaoEstoqueDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaVisaoEstoque;
import br.com.abril.nds.dto.filtro.FiltroContasAPagarDTO;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.serialization.custom.FlexiGridJson;
import br.com.abril.nds.service.ContasAPagarService;
import br.com.abril.nds.service.FornecedorService;
import br.com.abril.nds.util.export.FileExporter;
import br.com.abril.nds.util.export.FileExporter.FileType;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

/**
 * Classe responsável pelo controle das ações referentes à tela de Follow Up do
 * Sistema.
 * 
 * @author InfoA2 - Alex
 */

@Resource
@Path("financeiro/contasAPagar")
public class ContasAPagarController {
	
	private static final String FILTRO_CONTAS_A_PAGAR = "FILTRO_CONTAS_A_PAGAR";
	
	@Autowired
	private Result result;
	
	@Autowired
	private FornecedorService fornecedorService;
	
	@Autowired
	private ContasAPagarService contasAPagarService;
	
	@Autowired
	private HttpSession session;
	
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

		this.session.setAttribute(FILTRO_CONTAS_A_PAGAR, filtro);
		
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
	public void pesquisarProduto(FiltroContasAPagarDTO filtro){
		
		this.session.setAttribute(FILTRO_CONTAS_A_PAGAR, filtro);
		
		List<ContasAPagarConsultaProdutoDTO> produtos = contasAPagarService.pesquisaProdutoContasAPagar(filtro.getProduto(), filtro.getEdicao());
		List<ContasAPagarConsultaProdutoVO> produtosVO = new ArrayList<ContasAPagarConsultaProdutoVO>();
		
		for(ContasAPagarConsultaProdutoDTO dto : produtos){
			produtosVO.add(new ContasAPagarConsultaProdutoVO(dto));
		}
		
		result.use(FlexiGridJson.class).from(produtosVO).total(produtosVO.size()).serialize();
		
	}
	
	@Path("/exportar")
	public void exportar(FileType fileType) throws IOException {
		
		FiltroContasAPagarDTO filtro = (FiltroContasAPagarDTO) this.session.getAttribute(FILTRO_CONTAS_A_PAGAR);
		
		List<ContasApagarConsultaPorProdutoDTO> listConsultaPorProduto = contasAPagarService.pesquisaContasAPagarPorProduto(filtro.getProdutoEdicaoIDs());
		
		/*FileExporter.to("visao-estoque", fileType).inHTTPResponse(
				this.getNDSFileHeader(filtro.getDataMovimentacao()), null, null,
				listVisaoEstoque, VisaoEstoqueDTO.class,
				this.httpServletResponse);
		
		result.use(Results.nothing());*/


		HttpServletResponse response = null;  

                response.getWriter().write("<table><tr><td>linha 1</td></tr><tr><td>linha 2</td></tr></table>");  
                response.getWriter().flush();  
                response.getWriter().close();  
  
        
	}
	
	
	
	
}

