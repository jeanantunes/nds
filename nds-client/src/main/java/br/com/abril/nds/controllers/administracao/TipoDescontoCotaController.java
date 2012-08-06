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
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.TipoDescontoCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroCotaDTO.OrdemColuna;
import br.com.abril.nds.dto.filtro.FiltroTipoDescontoCotaDTO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.integracao.service.DistribuidorService;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.EspecificacaoDesconto;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.TipoDescontoCota;
import br.com.abril.nds.model.cadastro.TipoDescontoDistribuidor;
import br.com.abril.nds.model.cadastro.TipoDescontoProduto;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.service.CotaService;
import br.com.abril.nds.service.FornecedorService;
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
	
	@Autowired
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
	
	@Autowired
	private FornecedorService fornecedorService;
	
	@Autowired
	private HttpServletResponse httpServletResponse;
	
	private static final String FILTRO_PESQUISA_TIPO_DESCONTO_COTA_SESSION_ATTRIBUTE = "filtroPesquisaTipoDescontoCota";
	
	public enum TipoDescontoSelecionado{
		GERAL,ESPECIFICO,PRODUTO
	}
	
	@Path("/")
	public void index() {}
	
	@Post
	@Path("/novoDescontoGeral")
	public void novoDescontoGeral(BigDecimal desconto, List<Long> fornecedores){
		
		if(fornecedores == null || fornecedores.isEmpty()){
			throw new ValidacaoException(TipoMensagem.WARNING,"O campo Fornecedores selecionados deve ser preenchido!");
		}
		
		if(desconto == null ){
			throw new ValidacaoException(TipoMensagem.WARNING,"O campo Desconto deve ser preenchido!");
		}
		
		//FIXME alterar a logica de inclusão de cadastro de Desconto Geral
		
		/*atualizarDistribuidor(new BigDecimal(descontoDistribuidor.getDesconto()));
		List<Distribuidor> listaDeDistribuidor = this.tipoDescontoDistribuidorService.obterDistribuidores();
		for(Distribuidor dist: listaDeDistribuidor){
			descontoDistribuidor.setDistribuidor(dist);
			descontoDistribuidor.setUsuario(getUsuario());
			salvarDescontoDistribuidor(descontoDistribuidor);
		}*/		
		result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Desconto cadastrado com sucesso"),"result").recursive().serialize();
	}

	@Post
	@Path("/novoDescontoEspecifico")
	public void novoDescontoEspecifico(String cotaEspecifica, TipoDescontoCota descontoCota) throws ParseException{
		
		//FIXME revisar a implementação da inclusão de um novo desconto especifico
		
		/*Cota cotaParaAtualizar = this.cotaService.obterCotaPDVPorNumeroDaCota(Integer.parseInt(cotaEspecifica));		
		atualizarCota(new BigDecimal(descontoCota.getDesconto()), cotaParaAtualizar);				
		descontoCota.setUsuario(getUsuario());	
		descontoCota.setCota(cotaParaAtualizar);
		this.tipoDescontoCotaService.incluirDesconto(descontoCota);
		result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Desconto cadastrado com sucesso"),"result").recursive().serialize();*/
	}
	
	@Post
	@Path("/novoDescontoProduto")
	public void novoDescontoProduto(String codigo, String produto, String edicaoProduto, String descontoProduto, String dataAlteracaoProduto, String usuarioProduto){
		
		//FIXME revisar a implementação da inclusão de um novo desconto de produto
		
		/*try {
			ProdutoEdicao produtoEdicao = this.produtoEdicaoService.obterProdutoEdicaoPorCodProdutoNumEdicao(codigo, edicaoProduto);
			produtoEdicao.setDesconto(new BigDecimal(descontoProduto));
			this.produtoEdicaoService.alterarProdutoEdicao(produtoEdicao);
			TipoDescontoProduto tipoDescontoProduto = popularDescontoProduto(descontoProduto, dataAlteracaoProduto, usuarioProduto, produtoEdicao);
			salvarDescontoProduto(tipoDescontoProduto);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Desconto cadastrado com sucesso"),"result").recursive().serialize();*/
	}
	
	@Path("/pesquisarDescontoGeral")
	public void pesquisarDescontoGeral(String sortorder, String sortname, int page, int rp) throws Exception {
		
		FiltroTipoDescontoCotaDTO filtro = carregarFiltroPesquisaDescontoGeral(sortorder, sortname, page, rp);	
		
		List<TipoDescontoCotaVO> listaDescontoCotaVO = null;		
			
		//listaDescontoCotaVO = tipoDescontoDistribuidorService.obterTipoDescontoDistribuidor();
		
		listaDescontoCotaVO = getMock();
		
		//Integer totalDeRegistros = this.tipoDescontoDistribuidorService.buscarTotalDescontosDistribuidor();
		
		Integer totalDeRegistros = getMock().size();
		
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
	
	public List<TipoDescontoCotaVO> getMock(){
		
		List<TipoDescontoCotaVO> lista = new ArrayList<TipoDescontoCotaVO>();
		
		for (int i = 0; i < 10; i++) {
			
			TipoDescontoCotaVO tp  = new TipoDescontoCotaVO(); 
			
			tp.setCodigo("Codigo");
			tp.setCota("Cota");
			tp.setDataAlteracao("10/10/2010");
			tp.setDesconto("10");
			tp.setEdicao("123");
			tp.setEspecificacaoDesconto("Esp");
			tp.setId(i+"");
			tp.setSequencial(i+"");
			tp.setUsuario("Usuario");
			tp.setFornecedor("Fornecedor");
		
			lista.add(tp);
		}
		
		return lista;
	}
	
	@Post
	@Path("/pesquisarDescontoEspecifico")
	public void pesquisarDescontoEspecifico(Integer cotaEspecifica, String nomeEspecifico, String sortorder, String sortname, int page, int rp) throws Exception {
		
		//FIXME revisar a implementação do preenchimento do grid
		
		/*Cota cota = null;
		FiltroTipoDescontoCotaDTO filtro = null;
		if(cotaEspecifica != null){
			cota = this.cotaService.obterCotaPDVPorNumeroDaCota(cotaEspecifica);			
			filtro = carregarFiltroPesquisaDescontoEspecifico(cotaEspecifica.toString(), nomeEspecifico, sortorder, sortname, page, rp);
		}else{
			filtro = carregarFiltroPesquisaDescontoEspecifico("", nomeEspecifico, sortorder, sortname, page, rp);
		}
		
		
		List<TipoDescontoCotaDTO> listaDescontoCotaDTO = this.tipoDescontoCotaService.obterTipoDescontosCota(cota);		
			
		Integer totalRegistros = this.tipoDescontoCotaService.buscarTotalDescontosPorCota();			
				
		if (totalRegistros == 0) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum registro encontrado.");
		} 
		
		TableModel<CellModelKeyValue<TipoDescontoCotaDTO>> tableModel = new TableModel<CellModelKeyValue<TipoDescontoCotaDTO>>();

		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listaDescontoCotaDTO));			
		tableModel.setPage(filtro.getPaginacao().getPaginaAtual());
		tableModel.setTotal(totalRegistros);

		result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();*/
	}
	
	@Post
	@Path("/pesquisarDescontoProduto")
	public void pesquisarDescontoProduto(String codigo, String produto, String sortorder, String sortname, int page, int rp) throws Exception {
		
		//FIXME revisar a implementação do preenchimento do grid
		
		/*Produto produtoPesquisado = null;
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
		}*/
	}
	
	@Post
	@Path("/excluirDesconto")
	public void excluirDesconto(Long idDesconto, TipoDescontoSelecionado tipoDesconto ){
		
		//TODO verificar se a exclusão é apenas para registro da data vigente
		
		switch (tipoDesconto) {
			case ESPECIFICO:
				excluirDescontoEspecifico(idDesconto);
				break;
			case GERAL:
				excluirDescontoGeral(idDesconto);
				break;
			case PRODUTO:
				excluirDescontoProduto(idDesconto);
				break;
		}		
	}
	
	private  void excluirDescontoProduto(Long idDesconto){
		
		//FIXME implementar a lopgica de exclusão
		
		result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Operação realizada com sucesso."),
				Constantes.PARAM_MSGS).recursive().serialize();
	}
	
	private  void excluirDescontoGeral(Long idDesconto){
		
		//FIXME implementar a lopgica de exclusão
		
		result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Operação realizada com sucesso."),
				Constantes.PARAM_MSGS).recursive().serialize();
	}
	
	private void excluirDescontoEspecifico(Long idDesconto){
		
		//FIXME implementar a lopgica de exclusão
		
		result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Operação realizada com sucesso."),
				Constantes.PARAM_MSGS).recursive().serialize();
		
	}
	
	private void exportarDescontoGeral(FileType fileType){
		
		//FIXME refazer a parte de importação
		
		//listaDescontoCotaVO = tipoDescontoCotaService.obterTipoDescontoDistribuidor(EspecificacaoDesconto.GERAL);
		
		//FileExporter.to("consulta-tipo-desconto-cota", fileType).inHTTPResponse(this.getNDSFileHeader(), filtroSessao, null, listaDescontoCotaVO, TipoDescontoCotaVO.class, this.httpServletResponse);

	}
	
	private void exportarDescontoEspecifico(FileType fileType){
		
		//FIXME refazer a parte de importação
		
		/*FiltroCotaDTO filtroCotaDTO = new FiltroCotaDTO();			
		
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
		FileExporter.to("consulta-tipo-desconto-cota", fileType).inHTTPResponse(this.getNDSFileHeader(), filtroSessao, null, listaEspecifica, TipoDescontoCotaEspecificoVO.class, this.httpServletResponse);*/
	
	}
	
	private void exportarDescontoProduto(FileType fileType){
		
		//FIXME refazer a parte de importação
		
		/*FiltroTipoDescontoCotaDTO filtroSessao = this.obterFiltroParaExportacao();
		
		Produto produtoPesquisado = null;
		if(filtroSessao.getIdProduto() != null){
			produtoPesquisado = this.produtoService.obterProdutoPorCodigo(filtroSessao.getIdProduto().toString());			
		}else{
			produtoPesquisado = new Produto();
		}			
		
		List<TipoDescontoCotaVO> listaDescontoCotaVO  =  popularTipoDescontoCotaVOParaProduto(produtoPesquisado);
		
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
*/	}

	@Get
	public void exportar(FileType fileType, TipoDescontoSelecionado tipoDesconto) throws IOException {
		
		if (fileType == null) {
			throw new ValidacaoException(TipoMensagem.ERROR, "Tipo de arquivo não encontrado!");
		}
	
		switch (tipoDesconto) {
			case ESPECIFICO:
				exportarDescontoEspecifico(fileType);
				break;
			case GERAL:
				exportarDescontoGeral(fileType);		
				break;
			case PRODUTO:
				exportarDescontoProduto(fileType);
				break;
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
	
	/**
	 * Obtem os fornecedores que não possui associação com a cota informada
	 * 
	 * @param idCota -identificador da cota
	 */
	@Post
	@Path("/obterFornecedores")
	public void obterFornecedores(Long idCota){
		
		List<Fornecedor> fornecedores =   fornecedorService.obterFornecedores();
		
		result.use(Results.json()).from(this.getFornecedores(fornecedores),"result").recursive().serialize();
	}
	
	/**
	 * Retorna uma lista de fornecedores para exibição na tela
	 * 
	 * @param fornecedores - lista de fornecedores
	 * 
	 * @return List<ItemDTO<Long, String>>
	 */
	private List<ItemDTO<Long, String>> getFornecedores(List<Fornecedor> fornecedores){
		
		List<ItemDTO<Long, String>> itensFornecedor = new ArrayList<ItemDTO<Long,String>>();
		
		for(Fornecedor fornecedor : fornecedores){
			
			itensFornecedor.add(new ItemDTO<Long, String>(fornecedor.getId(), fornecedor.getJuridica().getRazaoSocial()));
		}
		
		return itensFornecedor;
	}

}
