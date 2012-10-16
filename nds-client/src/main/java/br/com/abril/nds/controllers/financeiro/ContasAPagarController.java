package br.com.abril.nds.controllers.financeiro;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.client.vo.ContasAPagarConsignadoVO;
import br.com.abril.nds.client.vo.ContasAPagarConsultaPorProdutoVO;
import br.com.abril.nds.client.vo.ContasAPagarConsultaProdutoVO;
import br.com.abril.nds.client.vo.ContasAPagarEncalheVO;
import br.com.abril.nds.client.vo.ContasAPagarFaltasSobrasVO;
import br.com.abril.nds.client.vo.ContasAPagarGridPrincipalProdutoVO;
import br.com.abril.nds.client.vo.ContasAPagarParcialVO;
import br.com.abril.nds.dto.ContasAPagarConsignadoDTO;
import br.com.abril.nds.dto.ContasAPagarConsultaProdutoDTO;
import br.com.abril.nds.dto.ContasAPagarEncalheDTO;
import br.com.abril.nds.dto.ContasAPagarFaltasSobrasDTO;
import br.com.abril.nds.dto.ContasAPagarParcialDTO;
import br.com.abril.nds.dto.FlexiGridDTO;
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
import br.com.caelum.vraptor.view.Results;

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
		
//		ContasAPagarGridPrincipalProdutoDTO dto = contasAPagarService.pesquisarPorProduto(filtro, sortname, sortorder, rp, page);
//		
//		if (dto == null) {
//			// TODO: msg erro busca sem resultados
//		}
//		
//		result.use(Results.json()).from(new ContasAPagarGridPrincipalProdutoVO(dto), "result").recursive().serialize();
		
		// mock
		List<ContasAPagarConsultaPorProdutoVO> listVO = new ArrayList<ContasAPagarConsultaPorProdutoVO>();
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
		
		ContasAPagarGridPrincipalProdutoVO gridVO = new ContasAPagarGridPrincipalProdutoVO();
		gridVO.setTotalPagto("111.111,00");
		gridVO.setTotalDesconto("22.222,00");
		gridVO.setValorLiquido("3.333,00");
		gridVO.setGrid(listVO);
		gridVO.setTotalGrid(34);
		
		result.use(Results.json()).from(gridVO, "result").recursive().serialize();
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
	public void pesquisarParcial(FiltroContasAPagarDTO filtro, String sortname, String sortorder, int rp, int page) {
		
		List<ContasAPagarParcialVO> listVO = new ArrayList<ContasAPagarParcialVO>();
		FlexiGridDTO<ContasAPagarParcialDTO> flexiDTO = contasAPagarService.pesquisarParcial(filtro, sortname, sortorder, rp, page);
		
		for (ContasAPagarParcialDTO dto : flexiDTO.getGrid()) {
			listVO.add(new ContasAPagarParcialVO(dto));
		}
		
		result.use(FlexiGridJson.class).from(listVO).total(flexiDTO.getTotalGrid()).serialize();
	}
	
	
	@Path("/pesquisarConsignado.json")
	public void pesquisarConsignado(FiltroContasAPagarDTO filtro, String sortname, String sortorder, int rp, int page) {
		
		List<ContasAPagarConsignadoVO> listVO = new ArrayList<ContasAPagarConsignadoVO>();
		List<ContasAPagarConsignadoDTO> listDTO = contasAPagarService.pesquisarDetalheConsignado(filtro, sortname, sortorder, rp, page);
		
		for (ContasAPagarConsignadoDTO dto : listDTO) {
			listVO.add(new ContasAPagarConsignadoVO(dto));
		}
		
		result.use(FlexiGridJson.class).from(listVO).total(listVO.size()).serialize();
	}
	
	
	@Path("/pesquisarEncalhe.json")
	public void pesquisarEncalhe(FiltroContasAPagarDTO filtro, String sortname, String sortorder, int rp, int page) {
		
		List<ContasAPagarEncalheVO> listVO = new ArrayList<ContasAPagarEncalheVO>();
		List<ContasAPagarEncalheDTO> listDTO = contasAPagarService.pesquisarDetalheEncalhe(filtro, sortname, sortorder, rp, page);
		
		for (ContasAPagarEncalheDTO dto : listDTO) {
			listVO.add(new ContasAPagarEncalheVO(dto));
		}
		
		result.use(FlexiGridJson.class).from(listVO).total(listVO.size()).serialize();
	}
	
	
	@Path("/pesquisarFaltasSobras.json")
	public void pesquisarFaltasSobras(FiltroContasAPagarDTO filtro, String sortname, String sortorder, int rp, int page) {
		
		List<ContasAPagarFaltasSobrasVO> listVO = new ArrayList<ContasAPagarFaltasSobrasVO>();
		List<ContasAPagarFaltasSobrasDTO> listDTO = contasAPagarService.pesquisarDetalheFaltasSobras(filtro, sortname, sortorder, rp, page);
		
		for (ContasAPagarFaltasSobrasDTO dto : listDTO) {
			listVO.add(new ContasAPagarFaltasSobrasVO(dto));
		}
		
		result.use(FlexiGridJson.class).from(listVO).total(listVO.size()).serialize();
	}
}