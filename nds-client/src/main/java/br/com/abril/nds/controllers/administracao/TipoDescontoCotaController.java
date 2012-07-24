package br.com.abril.nds.controllers.administracao;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.util.PaginacaoUtil;
import br.com.abril.nds.client.vo.TipoDescontoCotaEspecificoVO;
import br.com.abril.nds.client.vo.TipoDescontoCotaProdutoVO;
import br.com.abril.nds.client.vo.TipoDescontoCotaVO;
import br.com.abril.nds.client.vo.ValidacaoVO;
import br.com.abril.nds.dto.CotaDTO;
import br.com.abril.nds.dto.filtro.FiltroCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroCotaDTO.OrdemColuna;
import br.com.abril.nds.dto.filtro.FiltroTipoDescontoCotaDTO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.integracao.service.DistribuidorService;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.EspecificacaoDesconto;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.TipoDescontoCota;
import br.com.abril.nds.model.cadastro.TipoDescontoDistribuidor;
import br.com.abril.nds.model.cadastro.TipoDescontoProduto;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.service.CotaService;
import br.com.abril.nds.service.ProdutoEdicaoService;
import br.com.abril.nds.service.ProdutoService;
import br.com.abril.nds.service.TipoDescontoCotaService;
import br.com.abril.nds.service.TipoDescontoDistribuidorService;
import br.com.abril.nds.service.TipoDescontoProdutoService;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.Constantes;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.TipoMensagem;
import br.com.abril.nds.util.Util;
import br.com.abril.nds.util.export.FileExporter;
import br.com.abril.nds.util.export.FileExporter.FileType;
import br.com.abril.nds.util.export.NDSFileHeader;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

@Resource
@Path("/administracao/tipoDescontoCota")
public class TipoDescontoCotaController {
	
	private Result result;
	
	@Autowired
	private TipoDescontoDistribuidorService tipoDescontoDistribuidorService;
	
	@Autowired
	private TipoDescontoCotaService tipoDescontoCotaService;
	
	@Autowired
	private TipoDescontoProdutoService tipoDescontoProdutoService;
	
	@Autowired
	private CotaService cotaService;
	
	@Autowired
	private ProdutoEdicaoService produtoEdicaoService;
	
	@Autowired
	private ProdutoService produtoService;
	
	@Autowired
	private HttpSession session;
	
	@Autowired
	private DistribuidorService distribuidorService;
	
	private HttpServletResponse httpServletResponse;
	
	private static final String FILTRO_PESQUISA_TIPO_DESCONTO_COTA_SESSION_ATTRIBUTE = "filtroPesquisaTipoDescontoCota";
	
	public TipoDescontoCotaController(Result result, HttpSession session, HttpServletResponse httpServletResponse) {
		this.result = result; 
		this.session = session;
		this.httpServletResponse = httpServletResponse;
	}
	
	@Path("/")
	public void index() {		
		inserirDataAtual();		
	}
	
	@Post
	@Path("/novoDescontoGeral")
	public void novoDescontoGeral(TipoDescontoDistribuidor descontoDistribuidor){
		atualizarDistribuidor(new BigDecimal(descontoDistribuidor.getDesconto()));
		List<Distribuidor> listaDeDistribuidor = this.tipoDescontoCotaService.obterDistribuidores();
		for(Distribuidor dist: listaDeDistribuidor){
			descontoDistribuidor.setDistribuidor(dist);
			descontoDistribuidor.setUsuario(getUsuario());
			salvarDescontoDistribuidor(descontoDistribuidor);
		}		
		result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Desconto cadastrado com sucesso"),"result").recursive().serialize();
	}

	
	
	@Post
	@Path("/novoDescontoEspecifico")
	public void novoDescontoEspecifico(String cotaEspecifica, String nomeEspecifico, String descontoEspecifico, String dataAlteracaoEspecifico, String usuarioEspecifico){
		try {
			Cota cotaParaAtualizar = this.cotaService.obterCotaPDVPorNumeroDaCota(Integer.parseInt(cotaEspecifica));		
			atualizarCota(new BigDecimal(descontoEspecifico), cotaParaAtualizar);
			TipoDescontoCota especifico = popularDescontoCota(descontoEspecifico, dataAlteracaoEspecifico, usuarioEspecifico, cotaParaAtualizar);			
			salvarDescontoCota(especifico);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Desconto cadastrado com sucesso"),"result").recursive().serialize();
	}
	
	@Post
	@Path("/novoDescontoProduto")
	public void novoDescontoProduto(String codigo, String produto, String edicaoProduto, String descontoProduto, String dataAlteracaoProduto, String usuarioProduto){
		try {
			ProdutoEdicao produtoEdicao = this.produtoEdicaoService.obterProdutoEdicaoPorCodProdutoNumEdicao(codigo, edicaoProduto);
			produtoEdicao.setDesconto(new BigDecimal(descontoProduto));
			this.produtoEdicaoService.alterarProdutoEdicao(produtoEdicao);
			TipoDescontoProduto tipoDescontoProduto = popularDescontoProduto(descontoProduto, dataAlteracaoProduto, usuarioProduto, produtoEdicao);
			salvarDescontoProduto(tipoDescontoProduto);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Desconto cadastrado com sucesso"),"result").recursive().serialize();
	}
	
	@Path("/pesquisarDescontoGeral")
	public void pesquisarDescontoGeral(String sortorder, String sortname, int page, int rp) throws Exception {
		
		FiltroTipoDescontoCotaDTO filtro = carregarFiltroPesquisaDescontoGeral(sortorder, sortname, page, rp);	
		
		List<TipoDescontoCotaVO> listaDescontoCotaVO = null;		
			
		listaDescontoCotaVO = tipoDescontoCotaService.obterTipoDescontoDistribuidor();
		
		Integer totalDeRegistros = this.tipoDescontoCotaService.buscarTotalDescontosPorCota();
		
		if (totalDeRegistros == 0) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum registro encontrado.");
		} 
		
		int qtdeTotalRegistros = listaDescontoCotaVO.size();
		
		List<TipoDescontoCotaVO> listaTipoDescontoCOtaPaginada = 
				PaginacaoUtil.paginarEOrdenarEmMemoria(
						listaDescontoCotaVO, filtro.getPaginacao(), filtro.getOrdenacaoColuna().toString());
	
		TableModel<CellModelKeyValue<TipoDescontoCotaVO>> tableModel =
				new TableModel<CellModelKeyValue<TipoDescontoCotaVO>>();

		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listaTipoDescontoCOtaPaginada));
		tableModel.setPage(filtro.getPaginacao().getPaginaAtual());
		tableModel.setTotal(qtdeTotalRegistros);
		
		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
		
	}
	
	@Post
	@Path("/pesquisarDescontoEspecifico")
	public void pesquisarDescontoEspecifico(String cotaEspecifica, String nomeEspecifico, String sortorder, String sortname, int page, int rp) throws Exception {
		
		FiltroCotaDTO filtroCotaDTO = popularFiltroCotaDTO(cotaEspecifica,	nomeEspecifico);
		List<CotaDTO> listaDeCotas = this.cotaService.obterCotas(filtroCotaDTO);
		
		FiltroTipoDescontoCotaDTO filtro = carregarFiltroPesquisaDescontoEspecifico(cotaEspecifica, nomeEspecifico, sortorder, sortname, page, rp);
		
		List<TipoDescontoCotaVO> listaDescontoCotaVO = 	null;
		try {			
			listaDescontoCotaVO = popularTipoDescontoCotaVOParaCota(listaDeCotas);
			} catch (Exception e) {

			if (e instanceof ValidacaoException){
				throw e;
			} else {
				throw new ValidacaoException(TipoMensagem.ERROR, "Erro ao pesquisar produto: " + e.getMessage());
			}
		}		
		if (listaDescontoCotaVO == null || listaDescontoCotaVO.isEmpty()) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum registro encontrado.");
		} else {
			int qtdeTotalRegistros = listaDescontoCotaVO.size();
			
			List<TipoDescontoCotaVO> listaTipoDescontoCOtaPaginada =
					PaginacaoUtil.paginarEOrdenarEmMemoria(
							listaDescontoCotaVO, filtro.getPaginacao(), filtro.getOrdenacaoColuna().toString());
		
			TableModel<CellModelKeyValue<TipoDescontoCotaVO>> tableModel =
					new TableModel<CellModelKeyValue<TipoDescontoCotaVO>>();
	
			tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listaTipoDescontoCOtaPaginada));			
			tableModel.setPage(filtro.getPaginacao().getPaginaAtual());
			tableModel.setTotal(qtdeTotalRegistros);
	
			result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
		}
	}
	
	@Post
	@Path("/pesquisarDescontoProduto")
	public void pesquisarDescontoProduto(String codigo, String produto, String sortorder, String sortname, int page, int rp) throws Exception {
		Produto produtoPesquisado = null;
		List<TipoDescontoCotaVO> listaDescontoCotaVO = 	null;
		FiltroTipoDescontoCotaDTO filtro = carregarFiltroPesquisaDescontoProduto(codigo,sortorder, sortname, page, rp);
		try {
			if(!codigo.equals("")){
				produtoPesquisado = this.produtoService.obterProdutoPorCodigo(codigo);			
			}else{
				produtoPesquisado = new Produto();
				produtoPesquisado.setCodigo("0");
			}
			listaDescontoCotaVO = popularTipoDescontoCotaVOParaProduto(produtoPesquisado);
			} catch (Exception e) {

			if (e instanceof ValidacaoException){
				throw e;
			} else {
				throw new ValidacaoException(TipoMensagem.ERROR, "Erro ao pesquisar produto: " + e.getMessage());
			}
		}		
		if (listaDescontoCotaVO == null || listaDescontoCotaVO.isEmpty()) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum registro encontrado.");
		} else {
			int qtdeTotalRegistros = listaDescontoCotaVO.size();
			
			List<TipoDescontoCotaVO> listaTipoDescontoCOtaPaginada =
					PaginacaoUtil.paginarEOrdenarEmMemoria(
							listaDescontoCotaVO, filtro.getPaginacao(), filtro.getOrdenacaoColuna().toString());
		
			TableModel<CellModelKeyValue<TipoDescontoCotaVO>> tableModel =
					new TableModel<CellModelKeyValue<TipoDescontoCotaVO>>();
	
			tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listaTipoDescontoCOtaPaginada));
			tableModel.setPage(filtro.getPaginacao().getPaginaAtual());
			tableModel.setTotal(qtdeTotalRegistros);
	
			result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
		}
	}
	@Post
	@Path("/excluirDesconto")
	public void excluirDesconto(long idDesconto, String tipoDesconto){
		TipoDescontoCota desconto = this.tipoDescontoCotaService.obterTipoDescontoCotaPorId(idDesconto);
		
		Date dataAtual = new Date();		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String hoje = sdf.format(dataAtual);
		
		Date dataDesconto = desconto.getDataAlteracao();
		String dataDescontoFormatada = sdf.format(dataDesconto);
		
		if(dataDescontoFormatada.equals(hoje)){
			this.tipoDescontoCotaService.excluirDesconto(desconto);			
			result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Operação realizada com sucesso."),
					Constantes.PARAM_MSGS).recursive().serialize();			
		}else{
			result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.WARNING, "Só pode excluir desconto de data vigente!"),
					Constantes.PARAM_MSGS).recursive().serialize();
		}
	}
	
	@Get
	public void exportar(FileType fileType, String tipoDesconto) throws IOException {
		if (fileType == null) {
			throw new ValidacaoException(TipoMensagem.ERROR, "Tipo de arquivo não encontrado!");
		}
		
		FiltroTipoDescontoCotaDTO filtroSessao = this.obterFiltroParaExportacao();
		
		List<TipoDescontoCotaVO> listaDescontoCotaVO = null;
		if(tipoDesconto.equals("geral")){
			//listaDescontoCotaVO = tipoDescontoCotaService.obterTipoDescontoDistribuidor(EspecificacaoDesconto.GERAL);
			FileExporter.to("consulta-tipo-desconto-cota", fileType).inHTTPResponse(this.getNDSFileHeader(), filtroSessao, null, listaDescontoCotaVO, TipoDescontoCotaVO.class, this.httpServletResponse);
		}else if(tipoDesconto.equals("especifico")){
			FiltroCotaDTO filtroCotaDTO = new FiltroCotaDTO();			
			if(filtroSessao.getIdCota() == null  || filtroSessao.getNomeEspecifico() == null){
				filtroCotaDTO = popularFiltroCotaDTO("", "");
			}else{
				filtroCotaDTO = popularFiltroCotaDTO(filtroSessao.getIdCota().toString(), filtroSessao.getNomeEspecifico());				
			}
			List<CotaDTO> listaDeCotas = this.cotaService.obterCotas(filtroCotaDTO);
			listaDescontoCotaVO = popularTipoDescontoCotaVOParaCota(listaDeCotas);
			List<TipoDescontoCotaEspecificoVO> listaEspecifica = new ArrayList<TipoDescontoCotaEspecificoVO>();
			for(TipoDescontoCotaVO vo: listaDescontoCotaVO ){
				TipoDescontoCotaEspecificoVO especificoVO = new TipoDescontoCotaEspecificoVO();
				especificoVO.setDesconto(vo.getDesconto());
				especificoVO.setDtAlteracao(vo.getDtAlteracao());
				especificoVO.setUsuario(vo.getUsuario());
				especificoVO.setCota(vo.getCota());
				especificoVO.setNome(vo.getNome());
				listaEspecifica.add(especificoVO);
			}
			FileExporter.to("consulta-tipo-desconto-cota", fileType).inHTTPResponse(this.getNDSFileHeader(), filtroSessao, null, listaEspecifica, TipoDescontoCotaEspecificoVO.class, this.httpServletResponse);
		}else if(tipoDesconto.equals("produto")){
			Produto produtoPesquisado = null;
			if(filtroSessao.getIdProduto() != null){
				produtoPesquisado = this.produtoService.obterProdutoPorCodigo(filtroSessao.getIdProduto().toString());			
			}else{
				produtoPesquisado = new Produto();
			}			
			
			listaDescontoCotaVO = popularTipoDescontoCotaVOParaProduto(produtoPesquisado);			
			List<TipoDescontoCotaProdutoVO> listaEspecifica = new ArrayList<TipoDescontoCotaProdutoVO>();
			for(TipoDescontoCotaVO vo: listaDescontoCotaVO ){
				TipoDescontoCotaProdutoVO produtoVO = new TipoDescontoCotaProdutoVO();
				produtoVO.setDesconto(vo.getDesconto());
				produtoVO.setDtAlteracao(vo.getDtAlteracao());
				produtoVO.setUsuario(vo.getUsuario());
				produtoVO.setCodigo(vo.getCodigo());
				produtoVO.setProduto(vo.getProduto());
				produtoVO.setEdicao(vo.getEdicao());				
				listaEspecifica.add(produtoVO);
			}
			FileExporter.to("consulta-tipo-desconto-cota", fileType).inHTTPResponse(this.getNDSFileHeader(), filtroSessao, null, listaEspecifica, TipoDescontoCotaProdutoVO.class, this.httpServletResponse);			
		}		
	}
	
	/*
	 * Obtém o filtro de pesquisa para exportação.
	 */
	private FiltroTipoDescontoCotaDTO obterFiltroParaExportacao() {
		
		FiltroTipoDescontoCotaDTO filtroSessao =
			(FiltroTipoDescontoCotaDTO) 
				this.session.getAttribute(FILTRO_PESQUISA_TIPO_DESCONTO_COTA_SESSION_ATTRIBUTE);
		
		if (filtroSessao != null) {
			
			if (filtroSessao.getPaginacao() != null) {				
				filtroSessao.getPaginacao().setPaginaAtual(null);
				filtroSessao.getPaginacao().setQtdResultadosPorPagina(null);
			}
		}
		return filtroSessao;
	}
	
	/*
	 * Obtém os dados do cabeçalho de exportação.
	 * 
	 * @return NDSFileHeader
	 */
	private NDSFileHeader getNDSFileHeader() {
		
		NDSFileHeader ndsFileHeader = new NDSFileHeader();
		
		Distribuidor distribuidor = this.distribuidorService.obter();
		
		if (distribuidor != null) {
			
			ndsFileHeader.setNomeDistribuidor(distribuidor.getJuridica().getRazaoSocial());
			ndsFileHeader.setCnpjDistribuidor(distribuidor.getJuridica().getCnpj());
		}
		
		ndsFileHeader.setData(new Date());
		
		ndsFileHeader.setNomeUsuario(this.getUsuario().getNome());
		
		return ndsFileHeader;
	}

	private FiltroCotaDTO popularFiltroCotaDTO(String cotaEspecifica,String nomeEspecifico) {
		FiltroCotaDTO filtroCotaDTO = new FiltroCotaDTO();
		if(!cotaEspecifica.equals("")){
			filtroCotaDTO.setNumeroCota(Integer.parseInt(cotaEspecifica));			
		}		
		filtroCotaDTO.setOrdemColuna(OrdemColuna.NUMERO_COTA);
		return filtroCotaDTO;
	}
	
	private List<TipoDescontoCotaVO> popularTipoDescontoCotaVOParaCota(List<CotaDTO> listaDeCotas) {
		List<TipoDescontoCotaVO> listaVO = this.tipoDescontoCotaService.obterTipoDescontoDistribuidor();
		List<TipoDescontoCotaVO> listaAux = new ArrayList<TipoDescontoCotaVO>();
		for (TipoDescontoCotaVO tipoDescontoCotaVO : listaVO) {
			for(CotaDTO cotaDTO: listaDeCotas){
				if(tipoDescontoCotaVO.getCota().equals(cotaDTO.getNumeroCota().toString())){
					tipoDescontoCotaVO.setNome(cotaDTO.getNomePessoa());
					listaAux.add(tipoDescontoCotaVO);
				}
			}
			
		}
		return listaAux;
	}
	
	private List<TipoDescontoCotaVO> popularTipoDescontoCotaVOParaProduto(Produto produto) {
		List<TipoDescontoCotaVO> listaVO = this.tipoDescontoCotaService.obterTipoDescontoDistribuidor();
		List<TipoDescontoCotaVO> listaAux = new ArrayList<TipoDescontoCotaVO>();
		for (TipoDescontoCotaVO tipoDescontoCotaVO : listaVO) {
			ProdutoEdicao pr = this.produtoEdicaoService.obterProdutoEdicaoPorCodProdutoNumEdicao(tipoDescontoCotaVO.getCodigo(), tipoDescontoCotaVO.getEdicao());
			if(tipoDescontoCotaVO.getCodigo().equals(produto.getCodigo())){
				tipoDescontoCotaVO.setCodigo(pr.getProduto().getCodigo());
				tipoDescontoCotaVO.setProduto(pr.getProduto().getNome());
				tipoDescontoCotaVO.setEdicao(pr.getNumeroEdicao().toString());
				listaAux.add(tipoDescontoCotaVO);				
			}
		}
		
		if(listaAux.isEmpty()){
			for (TipoDescontoCotaVO tipoDescontoCotaVO : listaVO) {
					ProdutoEdicao pr = this.produtoEdicaoService.obterProdutoEdicaoPorCodProdutoNumEdicao(tipoDescontoCotaVO.getCodigo(), tipoDescontoCotaVO.getEdicao());
					tipoDescontoCotaVO.setCodigo(pr.getProduto().getCodigo());
					tipoDescontoCotaVO.setProduto(pr.getProduto().getNome());
					tipoDescontoCotaVO.setEdicao(pr.getNumeroEdicao().toString());
					listaAux.add(tipoDescontoCotaVO);				
				}
		}		
		
		
		return listaAux;
	}

	private void inserirDataAtual() {		
		result.include("dataAtual", DateUtil.formatarData(new Date(), "dd/MM/yyyy"));
	}
	
	private TipoDescontoDistribuidor popularDescontoDistribuidor(String desconto,	String dataAlteracao, String usuario, EspecificacaoDesconto especificacaoDesconto) throws ParseException {
		TipoDescontoDistribuidor descontoDistribuidor = new TipoDescontoDistribuidor();
		descontoDistribuidor.setDesconto(Float.parseFloat(desconto));
		SimpleDateFormat sdf = new SimpleDateFormat(Constantes.DATE_PATTERN_PT_BR);
		Date dataFormatada;
		dataFormatada = sdf.parse(dataAlteracao);
		descontoDistribuidor.setDataAlteracao(dataFormatada);
		descontoDistribuidor.setUsuario(getUsuario());		
		return descontoDistribuidor;
	}
	
	private TipoDescontoCota popularDescontoCota(String desconto,String dataAlteracao, String usuario, Cota cotaParaAtualizar) throws ParseException {
		TipoDescontoCota descontoCota = new TipoDescontoCota();
		descontoCota.setDesconto(Float.parseFloat(desconto));
		SimpleDateFormat sdf = new SimpleDateFormat(Constantes.DATE_PATTERN_PT_BR);
		Date dataFormatada;
		dataFormatada = sdf.parse(dataAlteracao);
		descontoCota.setDataAlteracao(dataFormatada);
		descontoCota.setUsuario(getUsuario());	
		descontoCota.setCota(cotaParaAtualizar);
		return descontoCota;
	}
	
	private TipoDescontoProduto popularDescontoProduto(String desconto,String dataAlteracao, String usuario, ProdutoEdicao produtoEdicao) throws ParseException {
		TipoDescontoProduto descontoProduto = new TipoDescontoProduto();
		descontoProduto.setDesconto(Float.parseFloat(desconto));
		SimpleDateFormat sdf = new SimpleDateFormat(Constantes.DATE_PATTERN_PT_BR);
		Date dataFormatada;
		dataFormatada = sdf.parse(dataAlteracao);
		descontoProduto.setDataAlteracao(dataFormatada);
		descontoProduto.setUsuario(getUsuario());
		descontoProduto.setProdutoEdicao(produtoEdicao);
		return descontoProduto;
	}
	
	private void salvarDescontoDistribuidor(TipoDescontoDistribuidor tipoDescontoDistribuidor){
		int sequencial = this.tipoDescontoDistribuidorService.obterUltimoSequencial();		
		tipoDescontoDistribuidor.setSequencial(++sequencial);
		this.tipoDescontoDistribuidorService.incluirDescontoDistribuidor(tipoDescontoDistribuidor);
	}
	
	private void salvarDescontoCota(TipoDescontoCota tipoDescontoCota) {
		this.tipoDescontoCotaService.incluirDesconto(tipoDescontoCota);
	}
	
	private void salvarDescontoProduto(TipoDescontoProduto tipoDescontoProduto) {
		this.tipoDescontoProdutoService.incluirDesconto(tipoDescontoProduto);
	}


	private void atualizarDistribuidor(BigDecimal desconto) {		
		this.tipoDescontoDistribuidorService.atualizarDistribuidores(desconto);
	}
	
	private void atualizarCota(BigDecimal descontoEspecifico, Cota cotaParaAtualizar) {
		cotaParaAtualizar.setFatorDesconto(descontoEspecifico);
		this.cotaService.alterarCota(cotaParaAtualizar);
	}
	
	private FiltroTipoDescontoCotaDTO carregarFiltroPesquisaDescontoGeral(String sortorder, String sortname, int page, int rp) {

		FiltroTipoDescontoCotaDTO filtro = new FiltroTipoDescontoCotaDTO();
		
		this.configurarPaginacaoPesquisaLancamentos(filtro, sortorder, sortname, page, rp);

		FiltroTipoDescontoCotaDTO filtroSessao = (FiltroTipoDescontoCotaDTO) this.session.getAttribute(FILTRO_PESQUISA_TIPO_DESCONTO_COTA_SESSION_ATTRIBUTE);

		if (filtroSessao != null && !filtroSessao.equals(filtro)) {
			filtro.getPaginacao().setPaginaAtual(1);
		}
		
		session.setAttribute(FILTRO_PESQUISA_TIPO_DESCONTO_COTA_SESSION_ATTRIBUTE, filtro);
		
		return filtro;
	}
	
	private FiltroTipoDescontoCotaDTO carregarFiltroPesquisaDescontoEspecifico(String cotaEspecifica, String nomeEspecifico,String sortorder, String sortname, int page, int rp) {

		FiltroTipoDescontoCotaDTO filtro = new FiltroTipoDescontoCotaDTO();
		
		this.configurarPaginacaoPesquisaLancamentos(filtro, sortorder, sortname, page, rp);

		FiltroTipoDescontoCotaDTO filtroSessao = (FiltroTipoDescontoCotaDTO) this.session.getAttribute(FILTRO_PESQUISA_TIPO_DESCONTO_COTA_SESSION_ATTRIBUTE);

		if (filtroSessao != null && !filtroSessao.equals(filtro)) {
			filtro.getPaginacao().setPaginaAtual(1);
		}
		if(!cotaEspecifica.equals("")){
			filtro.setIdCota(Long.parseLong(cotaEspecifica));
		}
		if(!nomeEspecifico.equals("")){
			filtro.setNomeEspecifico(nomeEspecifico);			
		}
		
		session.setAttribute(FILTRO_PESQUISA_TIPO_DESCONTO_COTA_SESSION_ATTRIBUTE, filtro);
		
		return filtro;
	}
	
	private FiltroTipoDescontoCotaDTO carregarFiltroPesquisaDescontoProduto(String codigo, String sortorder, String sortname, int page, int rp) {

		FiltroTipoDescontoCotaDTO filtro = new FiltroTipoDescontoCotaDTO();
		
		this.configurarPaginacaoPesquisaLancamentos(filtro, sortorder, sortname, page, rp);

		FiltroTipoDescontoCotaDTO filtroSessao = (FiltroTipoDescontoCotaDTO) this.session.getAttribute(FILTRO_PESQUISA_TIPO_DESCONTO_COTA_SESSION_ATTRIBUTE);

		if (filtroSessao != null && !filtroSessao.equals(filtro)) {
			filtro.getPaginacao().setPaginaAtual(1);
		}
		if(!codigo.equals("")){
			filtro.setIdProduto(Long.parseLong(codigo));			
		}
		
		session.setAttribute(FILTRO_PESQUISA_TIPO_DESCONTO_COTA_SESSION_ATTRIBUTE, filtro);
		
		return filtro;
	}
	

	private void configurarPaginacaoPesquisaLancamentos(FiltroTipoDescontoCotaDTO filtro, String sortorder,	String sortname, int page,int rp) {

		if (filtro != null) {
		
			PaginacaoVO paginacao = new PaginacaoVO(page, rp, sortorder);
			
			filtro.setPaginacao(paginacao);
		
			filtro.setOrdenacaoColuna(Util.getEnumByStringValue(FiltroTipoDescontoCotaDTO.OrdenacaoColunaConsulta.values(), sortname));
		}
	}
	
	
		private Usuario getUsuario() {
			
			Usuario usuario = new Usuario();
			
			usuario.setId(1L);
			
			usuario.setNome("Jornaleiro da Silva");
			
			return usuario;
		}

}
