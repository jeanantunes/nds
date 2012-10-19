package br.com.abril.nds.controllers.financeiro;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.client.vo.ContasAPagarConsignadoVO;
import br.com.abril.nds.client.vo.ContasAPagarConsultaPorProdutoVO;
import br.com.abril.nds.client.vo.ContasAPagarConsultaProdutoVO;
import br.com.abril.nds.client.vo.ContasAPagarEncalheVO;
import br.com.abril.nds.client.vo.ContasAPagarFaltasSobrasVO;
import br.com.abril.nds.client.vo.ContasAPagarGridPrincipalFornecedorVO;
import br.com.abril.nds.client.vo.ContasAPagarGridPrincipalProdutoVO;
import br.com.abril.nds.client.vo.ContasAPagarParcialVO;
import br.com.abril.nds.client.vo.ContasAPagarTotalDistribVO;
import br.com.abril.nds.client.vo.ContasApagarConsultaPorDistribuidorVO;
import br.com.abril.nds.dto.ContasAPagarConsignadoDTO;
import br.com.abril.nds.dto.ContasAPagarConsultaProdutoDTO;
import br.com.abril.nds.dto.ContasAPagarEncalheDTO;
import br.com.abril.nds.dto.ContasAPagarFaltasSobrasDTO;
import br.com.abril.nds.dto.ContasAPagarGridPrincipalFornecedorDTO;
import br.com.abril.nds.dto.ContasAPagarGridPrincipalProdutoDTO;
import br.com.abril.nds.dto.ContasAPagarParcialDTO;
import br.com.abril.nds.dto.ContasAPagarTotalDistribDTO;
import br.com.abril.nds.dto.ContasApagarConsultaPorDistribuidorDTO;
import br.com.abril.nds.dto.ContasApagarConsultaPorProdutoDTO;
import br.com.abril.nds.dto.FlexiGridDTO;
import br.com.abril.nds.dto.filtro.FiltroContasAPagarDTO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.integracao.service.DistribuidorService;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.serialization.custom.FlexiGridJson;
import br.com.abril.nds.service.ContasAPagarService;
import br.com.abril.nds.service.FornecedorService;
import br.com.abril.nds.util.TipoMensagem;
import br.com.abril.nds.util.export.FileExporter;
import br.com.abril.nds.util.export.FileExporter.FileType;
import br.com.abril.nds.util.export.NDSFileHeader;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.abril.nds.vo.ValidacaoVO;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

@Resource
@Path("financeiro/contasAPagar")
public class ContasAPagarController {
	private static final String FILTRO_CONTAS_A_PAGAR = "FILTRO_CONTAS_A_PAGAR";

	private static final String FILTRO_DETALHE_PARCIAL = "FILTRO_DETALHE_PARCIAL";
	
	private static final String FILTRO_DETALHE_CONSIGNADO = "FILTRO_DETALHE_CONSIGNADO";
	
	private static final String FILTRO_DETALHE_ENCALHE = "FILTRO_DETALHE_ENCALHE";
	
	private static final String FILTRO_DETALHE_FALTAS_SOBRAS = "FILTRO_DETALHE_FALTAS_SOBRAS";
	
	@Autowired
	private Result result;

	@Autowired
	private FornecedorService fornecedorService;

	@Autowired
	private ContasAPagarService contasAPagarService;
	
	@Autowired
	private HttpSession session;
	
	@Autowired
	private HttpServletResponse httpServletResponse;
	
	@Autowired
	private DistribuidorService distribuidorService;


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
		
		this.session.setAttribute(FILTRO_CONTAS_A_PAGAR, filtro);

		ContasAPagarGridPrincipalProdutoDTO dto = contasAPagarService.pesquisarPorProduto(filtro, sortname, sortorder, rp, page);
		
		if (dto == null || dto.getGrid() == null || dto.getGrid().size() == 0) {
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, "A busca não retornou resultados"));
		}
		
		ContasAPagarGridPrincipalProdutoVO vo = new ContasAPagarGridPrincipalProdutoVO(dto);
		
		if (filtro.isPrimeiraCarga()) {
			result.use(Results.json()).from(vo, "result").recursive().serialize();
		} else {
			result.use(FlexiGridJson.class).from(vo.getGrid()).total(vo.getTotalGrid()).page(page).serialize();
		}
	}


	@Path("/pesquisarParcial.json")
	public void pesquisarParcial(FiltroContasAPagarDTO filtro, String sortname, String sortorder, int rp, int page) {
		
		this.session.setAttribute(FILTRO_DETALHE_PARCIAL, filtro);

		List<ContasAPagarParcialVO> listVO = new ArrayList<ContasAPagarParcialVO>();
		FlexiGridDTO<ContasAPagarParcialDTO> flexiDTO = contasAPagarService.pesquisarParcial(filtro, sortname, sortorder, rp, page);

		for (ContasAPagarParcialDTO dto : flexiDTO.getGrid()) {
			listVO.add(new ContasAPagarParcialVO(dto));
		}
		
		result.use(FlexiGridJson.class).from(listVO).total(flexiDTO.getTotalGrid()).page(page).serialize();
	}


	@Path("/pesquisarPorFornecedor.json")
	public void pesquisarPorFornecedor(FiltroContasAPagarDTO filtro, String sortname, String sortorder, int rp, int page) {
		
		this.session.setAttribute(FILTRO_CONTAS_A_PAGAR, filtro);

		PaginacaoVO paginacaoVO = new PaginacaoVO(page, rp, sortorder, sortname);
		filtro.setPaginacaoVO(paginacaoVO);
		
		ContasAPagarGridPrincipalFornecedorDTO dto = contasAPagarService.pesquisarPorDistribuidor(filtro);
		
		if (dto == null || dto.getGrid() == null || dto.getGrid().size() == 0) {

			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, "A busca não retornou resultados"));
		}
		
		ContasAPagarGridPrincipalFornecedorVO vo = new ContasAPagarGridPrincipalFornecedorVO(dto);
		
		if (filtro.isPrimeiraCarga()) {
			result.use(Results.json()).from(vo, "result").recursive().serialize();
		} else {
			result.use(FlexiGridJson.class).from(vo.getGrid()).total(vo.getTotalGrid()).page(page).serialize();
		}
	}


	@Path("/pesquisarConsignado.json")
	public void pesquisarConsignado(FiltroContasAPagarDTO filtro, String sortname, String sortorder, int rp, int page) {

		this.session.setAttribute(FILTRO_DETALHE_CONSIGNADO, filtro);
		
		ContasAPagarTotalDistribDTO<ContasAPagarConsignadoDTO> dto = contasAPagarService.pesquisarDetalheConsignado(filtro, sortname, sortorder, rp, page);
		
		ContasAPagarTotalDistribVO<ContasAPagarConsignadoVO, ContasAPagarConsignadoDTO> vo = 
				new ContasAPagarTotalDistribVO<ContasAPagarConsignadoVO, ContasAPagarConsignadoDTO>(dto);
		
		for (ContasAPagarConsignadoDTO to : dto.getGrid()) {
			vo.getGrid().add(new ContasAPagarConsignadoVO(to));
		}
		
		result.use(Results.json()).from(vo, "result").recursive().serialize();
	}


	@Path("/pesquisarEncalhe.json")
	public void pesquisarEncalhe(FiltroContasAPagarDTO filtro, String sortname, String sortorder, int rp, int page) {

		this.session.setAttribute(FILTRO_DETALHE_ENCALHE, filtro);
		
		ContasAPagarTotalDistribDTO<ContasAPagarEncalheDTO> dto = contasAPagarService.pesquisarDetalheEncalhe(filtro, sortname, sortorder, rp, page);
		
		ContasAPagarTotalDistribVO<ContasAPagarEncalheVO, ContasAPagarEncalheDTO> vo = 
				new ContasAPagarTotalDistribVO<ContasAPagarEncalheVO, ContasAPagarEncalheDTO>(dto);
		
		for (ContasAPagarEncalheDTO to : dto.getGrid()) {
			vo.getGrid().add(new ContasAPagarEncalheVO(to));
		}
		
		result.use(Results.json()).from(vo, "result").recursive().serialize();
	}


	@Path("/pesquisarFaltasSobras.json")
	public void pesquisarFaltasSobras(FiltroContasAPagarDTO filtro, String sortname, String sortorder, int rp, int page) {

		this.session.setAttribute(FILTRO_DETALHE_FALTAS_SOBRAS, filtro);

		ContasAPagarTotalDistribDTO<ContasAPagarFaltasSobrasDTO> dto = contasAPagarService.pesquisarDetalheFaltasSobras(filtro, sortname, sortorder, rp, page);
		
		ContasAPagarTotalDistribVO<ContasAPagarFaltasSobrasVO, ContasAPagarFaltasSobrasDTO> vo = 
				new ContasAPagarTotalDistribVO<ContasAPagarFaltasSobrasVO, ContasAPagarFaltasSobrasDTO>(dto);
		
		for (ContasAPagarFaltasSobrasDTO to : dto.getGrid()) {
			vo.getGrid().add(new ContasAPagarFaltasSobrasVO(to));
		}
		
		result.use(Results.json()).from(vo, "result").recursive().serialize();
	}

	
	@Path("/exportPesquisarPorProduto")
	public void exportPesquisarPorProduto(FileType fileType) throws IOException {
		
		
		FiltroContasAPagarDTO filtro = (FiltroContasAPagarDTO) this.session.getAttribute(FILTRO_CONTAS_A_PAGAR);
		
		ContasAPagarGridPrincipalProdutoDTO listContasAPagar =  contasAPagarService.pesquisarPorProduto(filtro, null, null, 0, 0);
		List <ContasAPagarConsultaPorProdutoVO> listVO = new ArrayList<ContasAPagarConsultaPorProdutoVO>();
		
		for(ContasApagarConsultaPorProdutoDTO dto : listContasAPagar.getGrid() ){
			listVO.add(new ContasAPagarConsultaPorProdutoVO(dto));
		}
		
		FileExporter.to("contas-a-pagar", fileType).inHTTPResponse(this.getNDSFileHeader(new Date()), null, null,
						listVO, ContasAPagarConsultaPorProdutoVO.class,
						this.httpServletResponse);
		
		result.use(Results.nothing());
	}
	
	@Path("/exportPesquisarPorDistribuidor")
	public void exportPesquisarPorDistribuidor(FileType fileType) throws IOException {
		
		FiltroContasAPagarDTO filtro = (FiltroContasAPagarDTO) this.session.getAttribute(FILTRO_CONTAS_A_PAGAR);
		
		ContasAPagarGridPrincipalFornecedorDTO listContasAPagar = contasAPagarService.pesquisarPorDistribuidor(filtro);
		List <ContasApagarConsultaPorDistribuidorVO> listVO = new ArrayList<ContasApagarConsultaPorDistribuidorVO>();
		
		for(ContasApagarConsultaPorDistribuidorDTO dto : listContasAPagar.getGrid()){
			listVO.add(new ContasApagarConsultaPorDistribuidorVO(dto));
		}
		
		FileExporter.to("contas-a-pagar",fileType).inHTTPResponse(this.getNDSFileHeader(new Date()), null, null, 
						listVO, ContasApagarConsultaPorDistribuidorVO.class, 
						this.httpServletResponse);
		
		result.use(Results.nothing());
		
		
	}
	
	@Path("/exportPesquisarParcial")
	public void exportPesquisarParcial(FileType fileType) throws IOException {
		
		FiltroContasAPagarDTO filtro = (FiltroContasAPagarDTO) this.session.getAttribute(FILTRO_DETALHE_PARCIAL);
		
		List <ContasAPagarParcialVO> listVO = new ArrayList<ContasAPagarParcialVO>(); 
		FlexiGridDTO<ContasAPagarParcialDTO> listDTO = contasAPagarService.pesquisarParcial(filtro, null, null, 0, 0);
		
		for(ContasAPagarParcialDTO dto : listDTO.getGrid()){
			listVO.add(new ContasAPagarParcialVO(dto));
		}
		
		FileExporter.to("detalhe-parcial", fileType).inHTTPResponse(this.getNDSFileHeader(new Date()), null, null,
						listVO, ContasAPagarParcialVO.class, 
						this.httpServletResponse);
		
		result.use(Results.nothing());
	}
	
	@Path("/exportPesquisarDetalheConsignado")
	public void exportPesquisarDetalheConsignado(FileType fileType) throws IOException {
		
		// TODO
	}
	
	@Path("/exportPesquisarDetalheEncalhe")
	public void exportPesquisarDetalheEncalhe(FileType fileType) throws IOException {
		
		// TODO
	}
	
	
	@Path("/exportPesquisarDetalheFaltasSobras")
	public void exportPesquisarDetalheFaltasSobras(FileType fileType) throws IOException {
		
		FiltroContasAPagarDTO filtro = (FiltroContasAPagarDTO) session.getAttribute(FILTRO_CONTAS_A_PAGAR);
		
		ContasAPagarTotalDistribDTO<ContasAPagarFaltasSobrasDTO> dto = contasAPagarService.pesquisarDetalheFaltasSobras(filtro, null, null, 0, 0);

		List <ContasAPagarFaltasSobrasVO> listVO = new ArrayList<ContasAPagarFaltasSobrasVO>();
		
		for (ContasAPagarFaltasSobrasDTO to : dto.getGrid()) {
			listVO.add(new ContasAPagarFaltasSobrasVO(to));
		}
		
		FileExporter.to("detalhe-faltas-sobras", fileType).inHTTPResponse(this.getNDSFileHeader(new Date()), null, null,
				listVO, ContasAPagarFaltasSobrasVO.class, this.httpServletResponse);
		
		result.use(Results.nothing());
	}
	
	
	private NDSFileHeader getNDSFileHeader(Date data) {

		NDSFileHeader ndsFileHeader = new NDSFileHeader();
		Distribuidor distribuidor = distribuidorService.obter();

		if (distribuidor != null) {
			ndsFileHeader.setNomeDistribuidor(distribuidor.getJuridica().getRazaoSocial());
			ndsFileHeader.setCnpjDistribuidor(distribuidor.getJuridica().getCnpj());
		}

		ndsFileHeader.setData(data);
		ndsFileHeader.setNomeUsuario(getUsuario().getNome());
		return ndsFileHeader;
	}
	
	
	// TODO: não há como reconhecer usuario, ainda
	private Usuario getUsuario() {

		Usuario usuario = new Usuario();
		usuario.setId(1L);
		usuario.setNome("Jornaleiro da Silva");

		return usuario;
	}
	
}