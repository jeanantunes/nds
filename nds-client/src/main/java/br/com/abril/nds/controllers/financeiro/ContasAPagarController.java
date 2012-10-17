package br.com.abril.nds.controllers.financeiro;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.client.vo.ContasAPagarConsignadoVO;
import br.com.abril.nds.client.vo.ContasAPagarConsultaProdutoVO;
import br.com.abril.nds.client.vo.ContasAPagarEncalheVO;
import br.com.abril.nds.client.vo.ContasAPagarFaltasSobrasVO;
import br.com.abril.nds.client.vo.ContasAPagarGridPrincipalFornecedorVO;
import br.com.abril.nds.client.vo.ContasAPagarGridPrincipalProdutoVO;
import br.com.abril.nds.client.vo.ContasAPagarParcialVO;
import br.com.abril.nds.dto.ContasAPagarConsignadoDTO;
import br.com.abril.nds.dto.ContasAPagarConsultaProdutoDTO;
import br.com.abril.nds.dto.ContasAPagarEncalheDTO;
import br.com.abril.nds.dto.ContasAPagarFaltasSobrasDTO;
import br.com.abril.nds.dto.ContasAPagarGridPrincipalFornecedorDTO;
import br.com.abril.nds.dto.ContasAPagarGridPrincipalProdutoDTO;
import br.com.abril.nds.dto.ContasAPagarParcialDTO;
import br.com.abril.nds.dto.FlexiGridDTO;
import br.com.abril.nds.dto.filtro.FiltroContasAPagarDTO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.serialization.custom.FlexiGridJson;
import br.com.abril.nds.service.ContasAPagarService;
import br.com.abril.nds.service.FornecedorService;
import br.com.abril.nds.util.TipoMensagem;
import br.com.abril.nds.vo.ValidacaoVO;
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
	
	
	@Path("/pesquisarProduto.json")
	public void pesquisarProduto(FiltroContasAPagarDTO filtro) {

		List<ContasAPagarConsultaProdutoDTO> produtos = contasAPagarService.pesquisarProdutos(filtro);
		List<ContasAPagarConsultaProdutoVO> produtosVO = new ArrayList<ContasAPagarConsultaProdutoVO>();
		
		for(ContasAPagarConsultaProdutoDTO dto : produtos){
			produtosVO.add(new ContasAPagarConsultaProdutoVO(dto));
		}
		
		result.use(FlexiGridJson.class).from(produtosVO).total(produtosVO.size()).serialize();
	}
	
	
	@Path("/pesquisarPorProduto.json")
	public void pesquisarPorProduto(FiltroContasAPagarDTO filtro, String sortname, String sortorder, int rp, int page) {
		
		ContasAPagarGridPrincipalProdutoDTO dto = contasAPagarService.pesquisarPorProduto(filtro, sortname, sortorder, rp, page);
		
		if (dto == null) {
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, "A busca não retornou resultados"));
		}
		
		ContasAPagarGridPrincipalProdutoVO vo = new ContasAPagarGridPrincipalProdutoVO(dto);
		
		if (filtro.isPrimeiraCarga()) {
			result.use(Results.json()).from(vo, "result").recursive().serialize();
		} else {
			result.use(FlexiGridJson.class).from(vo.getGrid()).total(dto.getTotalGrid()).page(page).serialize();
		}
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
	
	
	@Path("/pesquisarPorFornecedor.json")
	public void pesquisarPorFornecedor(FiltroContasAPagarDTO filtro, String sortname, String sortorder, int rp, int page) {
		
		ContasAPagarGridPrincipalFornecedorDTO dto = contasAPagarService.pesquisarPorDistribuidor(filtro, sortname, sortorder, rp, page);
		
		if (dto == null) {
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, "A busca não retornou resultados"));
		}
		
		result.use(Results.json()).from(new ContasAPagarGridPrincipalFornecedorVO(dto), "result").recursive().serialize();
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