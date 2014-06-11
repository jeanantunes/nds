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
import br.com.abril.nds.controllers.BaseController;
import br.com.abril.nds.dto.ContasAPagarConsignadoDTO;
import br.com.abril.nds.dto.ContasAPagarDistribDTO;
import br.com.abril.nds.dto.ContasAPagarEncalheDTO;
import br.com.abril.nds.dto.ContasAPagarFaltasSobrasDTO;
import br.com.abril.nds.dto.ContasAPagarGridPrincipalProdutoDTO;
import br.com.abril.nds.dto.ContasAPagarParcialDTO;
import br.com.abril.nds.dto.ContasAPagarTotalDistribDTO;
import br.com.abril.nds.dto.ContasApagarConsultaPorProdutoDTO;
import br.com.abril.nds.dto.FlexiGridDTO;
import br.com.abril.nds.dto.filtro.FiltroContasAPagarDTO;
import br.com.abril.nds.enums.TipoMensagem;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.serialization.custom.FlexiGridJson;
import br.com.abril.nds.service.ContasAPagarService;
import br.com.abril.nds.service.DiferencaEstoqueService;
import br.com.abril.nds.service.FornecedorService;
import br.com.abril.nds.service.RecolhimentoService;
import br.com.abril.nds.service.integracao.DistribuidorService;
import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.Intervalo;
import br.com.abril.nds.util.export.FileExporter;
import br.com.abril.nds.util.export.FileExporter.FileType;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.abril.nds.vo.ValidacaoVO;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

@Resource
@Path("financeiro/contasAPagar")
@Rules(Permissao.ROLE_FINANCEIRO_CONTAS_A_PAGAR)
public class ContasAPagarController extends BaseController {
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
	
	@Autowired
	private RecolhimentoService recolhimentoService;
	
	@Autowired
	private DiferencaEstoqueService diferencaEstoqueService;


	public ContasAPagarController(Result result) {
		super();
		this.result = result;
	}


	@Path("/")
	public void index() {

		result.include("fornecedores", 
		        fornecedorService.obterFornecedores(SituacaoCadastro.ATIVO));
		
		final Integer numeroSemana = this.distribuidorService.obterNumeroSemana(new Date());
		
		result.include("numeroSemana", numeroSemana);
		
		final Intervalo<Date> periodo = this.recolhimentoService.getPeriodoRecolhimento(numeroSemana);
		
		result.include("dataDe", DateUtil.formatarDataPTBR(periodo.getDe()));
		result.include("dataAte", DateUtil.formatarDataPTBR(periodo.getAte()));
	}


	@Path("/pesquisarProduto.json")
	public void pesquisarProduto(FiltroContasAPagarDTO filtro, String sortname, String sortorder) {
		filtro.setPaginacaoVO(new PaginacaoVO(sortname, sortorder));
		List<ContasAPagarConsultaProdutoVO> produtos = contasAPagarService.pesquisarProdutos(filtro);
		
		result.use(FlexiGridJson.class).from(produtos).total(produtos.size()).serialize();
	}


	@Path("/pesquisarPorProduto.json")
	public void pesquisarPorProduto(FiltroContasAPagarDTO filtro, String sortname, String sortorder, int rp, int page) {
		
		filtro.setIdsFornecedores(null);
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
		
		PaginacaoVO paginacaoVO = new PaginacaoVO(page, rp, sortorder, sortname);
		filtro.setPaginacaoVO(paginacaoVO);
		
		List<ContasAPagarParcialVO> listVO = new ArrayList<ContasAPagarParcialVO>();
		FlexiGridDTO<ContasAPagarParcialDTO> flexiDTO = contasAPagarService.pesquisarParcial(filtro);
		
		if (flexiDTO.getGrid() == null || flexiDTO.getGrid().isEmpty()){
		    
		    throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum registro encontrado.");
		}

		for (ContasAPagarParcialDTO dto : flexiDTO.getGrid()) {
			listVO.add(new ContasAPagarParcialVO(dto));
		}
		
		result.use(FlexiGridJson.class).from(listVO).total(flexiDTO.getTotalGrid()).page(page).serialize();
	}


	@Path("/pesquisarPorFornecedor.json")
	public void pesquisarPorFornecedor(FiltroContasAPagarDTO filtro, String sortname, String sortorder, int rp, int page) {
		
	    if (filtro == null || filtro.getIdsFornecedores() == null || filtro.getIdsFornecedores().isEmpty()){
	        
	        throw new ValidacaoException(TipoMensagem.WARNING, "Selecione ao menos um fornecedor.");
	    }
	    
		this.session.setAttribute(FILTRO_CONTAS_A_PAGAR, filtro);

		final PaginacaoVO paginacaoVO = new PaginacaoVO(page, rp, sortorder, sortname);
		filtro.setPaginacaoVO(paginacaoVO);
		
		final ContasAPagarGridPrincipalFornecedorVO vo = this.contasAPagarService.pesquisarPorDistribuidor(filtro);
		
		if (filtro.isPrimeiraCarga()) {
			result.use(Results.json()).from(vo, "result").recursive().serialize();
		} else {
			result.use(FlexiGridJson.class).from(vo.getGrid()).total(vo.getTotalGrid()).page(page).serialize();
		}
	}

	@Path("/pesquisarConsignado.json")
	public void pesquisarConsignado(FiltroContasAPagarDTO filtro, String sortname, String sortorder, int rp, int page) {

		this.session.setAttribute(FILTRO_DETALHE_CONSIGNADO, filtro);
		
		PaginacaoVO paginacaoVO = new PaginacaoVO(page, rp, sortorder, sortname);
		filtro.setPaginacaoVO(paginacaoVO);
		
		ContasAPagarTotalDistribDTO<ContasAPagarConsignadoDTO> dto = contasAPagarService.pesquisarDetalheConsignado(filtro);
		
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
		
		PaginacaoVO paginacaoVO = new PaginacaoVO(page, rp, sortorder, sortname);
		filtro.setPaginacaoVO(paginacaoVO);
		
		ContasAPagarTotalDistribDTO<ContasAPagarEncalheDTO> dto = contasAPagarService.pesquisarDetalheEncalhe(filtro);
		
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
		
		PaginacaoVO paginacaoVO = new PaginacaoVO(page, rp, sortorder, sortname);
		filtro.setPaginacaoVO(paginacaoVO);

		ContasAPagarTotalDistribDTO<ContasAPagarFaltasSobrasDTO> dto = contasAPagarService.pesquisarDetalheFaltasSobras(filtro);
		
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
		filtro.setPaginacaoVO(null);
		
		ContasAPagarGridPrincipalProdutoDTO listContasAPagar =  contasAPagarService.pesquisarPorProduto(filtro, null, null, 0, 0);
		List <ContasAPagarConsultaPorProdutoVO> listVO = new ArrayList<ContasAPagarConsultaPorProdutoVO>();
		
		for(ContasApagarConsultaPorProdutoDTO dto : listContasAPagar.getGrid() ){
			listVO.add(new ContasAPagarConsultaPorProdutoVO(dto));
		}
		
		FileExporter.to("contas-a-pagar", fileType).inHTTPResponse(getNDSFileHeader(), null, 
						listVO, ContasAPagarConsultaPorProdutoVO.class,
						this.httpServletResponse);
		
		result.use(Results.nothing());
	}
	
	@Path("/exportPesquisarPorDistribuidor")
	public void exportPesquisarPorDistribuidor(FileType fileType) throws IOException {
		
		final FiltroContasAPagarDTO filtro = (FiltroContasAPagarDTO) this.session.getAttribute(FILTRO_CONTAS_A_PAGAR);
		filtro.setPaginacaoVO(null);
		
		final ContasAPagarGridPrincipalFornecedorVO vo = 
		        this.contasAPagarService.pesquisarPorDistribuidor(filtro);
		
		//adiciona totais
		ContasApagarConsultaPorDistribuidorVO total = new ContasApagarConsultaPorDistribuidorVO();
		vo.getGrid().add(total);
		
		total = new ContasApagarConsultaPorDistribuidorVO();
		total.setData("Total Bruto");
		total.setConsignado(vo.getTotalBruto());
		vo.getGrid().add(total);
		
		total = new ContasApagarConsultaPorDistribuidorVO();
        total.setData("Total Desconto");
        total.setConsignado(vo.getTotalDesconto());
        vo.getGrid().add(total);
        
        total = new ContasApagarConsultaPorDistribuidorVO();
        total.setData("Saldo a Pagar");
        total.setConsignado(vo.getSaldo());
        vo.getGrid().add(total);
		
		FileExporter.to("contas-a-pagar",fileType).inHTTPResponse(getNDSFileHeader(), null, 
		                vo.getGrid(), 
		                ContasApagarConsultaPorDistribuidorVO.class, 
						this.httpServletResponse);
		
		result.use(Results.nothing());
	}
	
	@Path("/exportPesquisarParcial")
	public void exportPesquisarParcial(FileType fileType) throws IOException {
		
		FiltroContasAPagarDTO filtro = (FiltroContasAPagarDTO) this.session.getAttribute(FILTRO_DETALHE_PARCIAL);
		filtro.setPaginacaoVO(null);
		
		List <ContasAPagarParcialVO> listVO = new ArrayList<ContasAPagarParcialVO>(); 
		FlexiGridDTO<ContasAPagarParcialDTO> listDTO = contasAPagarService.pesquisarParcial(filtro);
		
		for(ContasAPagarParcialDTO dto : listDTO.getGrid()){
			listVO.add(new ContasAPagarParcialVO(dto));
		}
		
		FileExporter.to("detalhe-parcial", fileType).inHTTPResponse(getNDSFileHeader(), null, 
						listVO, ContasAPagarParcialVO.class, 
						this.httpServletResponse);
		
		result.use(Results.nothing());
	}
	
	@Path("/exportPesquisarDetalheConsignado")
	public void exportPesquisarDetalheConsignado(FileType fileType) throws IOException {
    
	    FiltroContasAPagarDTO filtro = (FiltroContasAPagarDTO) session.getAttribute(FILTRO_DETALHE_CONSIGNADO);
	    filtro.setPaginacaoVO(null);
	    
	    ContasAPagarTotalDistribDTO<ContasAPagarConsignadoDTO> dto = contasAPagarService.pesquisarDetalheConsignado(filtro);
	    
	    List <ContasAPagarConsignadoVO> listVO = new ArrayList<ContasAPagarConsignadoVO>();
	    
	    for(ContasAPagarConsignadoDTO dt : dto.getGrid()){
	        
	        listVO.add(new ContasAPagarConsignadoVO(dt));
	    }
	    
	    //adiciona totais
	    listVO.add(new ContasAPagarConsignadoVO());
	    for (ContasAPagarDistribDTO sum : dto.getTotalDistrib()){
	        
	        ContasAPagarConsignadoVO s = new ContasAPagarConsignadoVO();
	        s.setFornecedor(sum.getNome());
	        s.setValorComDesconto(CurrencyUtil.formatarValor(CurrencyUtil.arredondarValorParaDuasCasas(sum.getTotal())));
	        
	        listVO.add(s);
	    }
	    
	    FileExporter.to("detalhe-consignado", fileType).inHTTPResponse(getNDSFileHeader(), null,
                    listVO, ContasAPagarConsignadoVO.class,
                    this.httpServletResponse);
	    
	    result.use(Results.nothing());
	}
	
	@Path("/exportPesquisarDetalheEncalhe")
	public void exportPesquisarDetalheEncalhe(FileType fileType) throws IOException {
		
		FiltroContasAPagarDTO filtro = (FiltroContasAPagarDTO) session.getAttribute(FILTRO_DETALHE_ENCALHE);
		filtro.setPaginacaoVO(null);
		
		ContasAPagarTotalDistribDTO<ContasAPagarEncalheDTO> dto = contasAPagarService.pesquisarDetalheEncalhe(filtro);
		
		List <ContasAPagarEncalheVO> listVO = new ArrayList<ContasAPagarEncalheVO>();
		
		for(ContasAPagarEncalheDTO dt : dto.getGrid()){
			
			listVO.add(new ContasAPagarEncalheVO(dt));
		}
		
		//adiciona totais
        listVO.add(new ContasAPagarEncalheVO());
        for (ContasAPagarDistribDTO sum : dto.getTotalDistrib()){
            
            ContasAPagarEncalheVO s = new ContasAPagarEncalheVO();
            s.setFornecedor(sum.getNome());
            s.setValor(CurrencyUtil.formatarValor(CurrencyUtil.arredondarValorParaDuasCasas(sum.getTotal())));
            
            listVO.add(s);
        }
		
		FileExporter.to("detalhe-encalhe", fileType).inHTTPResponse(getNDSFileHeader(), null, 
						listVO, ContasAPagarEncalheVO.class, 
						this.httpServletResponse);
		
		result.use(Results.nothing());

	}
	
	@Path("/exportPesquisarDetalheFaltasSobras")
	public void exportPesquisarDetalheFaltasSobras(FileType fileType) throws IOException {
		
		FiltroContasAPagarDTO filtro = (FiltroContasAPagarDTO) session.getAttribute(FILTRO_DETALHE_FALTAS_SOBRAS);
		filtro.setPaginacaoVO(null);
		
		ContasAPagarTotalDistribDTO<ContasAPagarFaltasSobrasDTO> dto = contasAPagarService.pesquisarDetalheFaltasSobras(filtro);

		List <ContasAPagarFaltasSobrasVO> listVO = new ArrayList<ContasAPagarFaltasSobrasVO>();
		
		for (ContasAPagarFaltasSobrasDTO to : dto.getGrid()) {
			listVO.add(new ContasAPagarFaltasSobrasVO(to));
		}
		
		//adiciona totais
        listVO.add(new ContasAPagarFaltasSobrasVO());
        for (ContasAPagarDistribDTO sum : dto.getTotalDistrib()){
            
            ContasAPagarFaltasSobrasVO s = new ContasAPagarFaltasSobrasVO();
            s.setFornecedor(sum.getNome());
            s.setValor(CurrencyUtil.formatarValor(CurrencyUtil.arredondarValorParaDuasCasas(sum.getTotal())));
            
            listVO.add(s);
        }
		
		FileExporter.to("detalhe-faltas-sobras", fileType).inHTTPResponse(getNDSFileHeader(), null, 
				listVO, ContasAPagarFaltasSobrasVO.class, this.httpServletResponse);
		
		result.use(Results.nothing());
	}
	
	@Path("/calcularPeriodoCE.json")
	public void calcularPeriodoCE(Integer semanaCE){
	    
	    this.result.use(Results.json()).from(
	            this.recolhimentoService.getPeriodoRecolhimento(semanaCE), "result").recursive().serialize();
	}
	
	@Path("/pesquisarDiferencas")
	public void pesquisarDiferencas(String codigoProduto, Long numeroEdicao, Date data){
	    
	    this.result.use(FlexiGridJson.class).from(
	            this.diferencaEstoqueService.pesquisarDiferncas(
	                    codigoProduto, numeroEdicao, data)).serialize();
	}
}
