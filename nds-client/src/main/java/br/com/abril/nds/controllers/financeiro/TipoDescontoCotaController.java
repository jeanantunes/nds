package br.com.abril.nds.controllers.financeiro;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Future;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.annotation.Rules;
import br.com.abril.nds.client.util.PaginacaoUtil;
import br.com.abril.nds.component.singleton.ProcessoCadastroDescontoSingleton;
import br.com.abril.nds.dto.CotaDescontoProdutoDTO;
import br.com.abril.nds.dto.DescontoProdutoDTO;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.TipoDescontoCotaDTO;
import br.com.abril.nds.dto.TipoDescontoDTO;
import br.com.abril.nds.dto.TipoDescontoProdutoDTO;
import br.com.abril.nds.dto.filtro.FiltroDTO;
import br.com.abril.nds.dto.filtro.FiltroTipoDescontoCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroTipoDescontoDTO;
import br.com.abril.nds.dto.filtro.FiltroTipoDescontoProdutoDTO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.integracao.service.DistribuidorService;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.desconto.TipoDesconto;
import br.com.abril.nds.model.seguranca.Permissao;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.serialization.custom.FlexiGridJson;
import br.com.abril.nds.service.CotaService;
import br.com.abril.nds.service.DescontoService;
import br.com.abril.nds.service.FornecedorService;
import br.com.abril.nds.util.Constantes;
import br.com.abril.nds.util.TipoMensagem;
import br.com.abril.nds.util.Util;
import br.com.abril.nds.util.export.FileExporter;
import br.com.abril.nds.util.export.FileExporter.FileType;
import br.com.abril.nds.util.export.NDSFileHeader;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.abril.nds.vo.PaginacaoVO.Ordenacao;
import br.com.abril.nds.vo.ValidacaoVO;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

@Resource
@Path("/financeiro/tipoDescontoCota")
public class TipoDescontoCotaController {
	
	@Autowired
	private Result result;
	
	@Autowired
	private DescontoService descontoService;

	@Autowired
	private HttpSession session;
	
	@Autowired
	private DistribuidorService distribuidorService;
	
	@Autowired
	private FornecedorService fornecedorService;
	
	@Autowired
	private CotaService cotaService;
	
	@Autowired
	private HttpServletResponse httpServletResponse;

	private String FILTRO_PESQUISA_TIPO_DESCONTO_PRODUTO_SESSION_ATTRIBUTE= "filtroPesquisaPorProduto";

	private String FILTRO_PESQUISA_TIPO_DESCONTO_SESSION_ATTRIBUTE = "filtroPesquisaPorGeral";
	
	private static final String FILTRO_PESQUISA_TIPO_DESCONTO_COTA_SESSION_ATTRIBUTE = "filtroPesquisaPorCota";
	
	@Path("/")
	@Rules(Permissao.ROLE_FINANCEIRO_TIPO_DESCONTO_COTA)
	public void index() {}
	
	@Post
	@Path("/novoDescontoGeral")
	public void novoDescontoGeral(BigDecimal desconto, List<Long> fornecedores){

		Future<String> future = descontoService.executarDescontoGeral(desconto, fornecedores, getUsuario());
		
		this.resultProcessoCadastroDesconto(future);
	}
		
	@Post("/novoDescontoEspecifico")
	public void novoDescontoEspecifico(Integer numeroCota, BigDecimal desconto, List<Long> fornecedores) {
		
		Future<String> future = descontoService.executarDescontoEspecifico(numeroCota, desconto, fornecedores, getUsuario());

		this.resultProcessoCadastroDesconto(future);
	}

	@Post
	@Path("/novoDescontoProduto")
	public void novoDescontoProduto(DescontoProdutoDTO descontoDTO, List<Integer> cotas) {		

		Future<String> future = descontoService.executarDescontoProduto(descontoDTO, cotas, getUsuario());
		
		this.resultProcessoCadastroDesconto(future);
	}
	
	@Path("/pesquisarDescontoGeral")
	public void pesquisarDescontoGeral(String sortorder, String sortname, int page, int rp) throws Exception {
		
		FiltroTipoDescontoDTO filtro = carregarFiltroPesquisaDescontoGeral(sortorder, sortname, page, rp);	
		
		List<TipoDescontoDTO> listaTipoDescontoGeral = descontoService.buscarTipoDesconto(filtro);
			
		if (listaTipoDescontoGeral.isEmpty()) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum registro encontrado.");
		} 
		
		Integer totalDeRegistros = descontoService.buscarQntTipoDesconto(filtro);
		
		result.use(FlexiGridJson.class).from(listaTipoDescontoGeral).total(totalDeRegistros).page(page).serialize();
	}
	
	@Post
	@Path("/pesquisarDescontoEspecifico")
	public void pesquisarDescontoEspecifico(Integer cotaEspecifica, String nomeEspecifico, String sortorder, String sortname, int page, int rp) throws Exception {
		
		FiltroTipoDescontoCotaDTO filtro = carregarFiltroPesquisaDescontoEspecifico(cotaEspecifica,nomeEspecifico,sortorder, sortname, page, rp);
		
		List<TipoDescontoCotaDTO> listaDescontoCotaEspecifica = descontoService.buscarTipoDescontoCota(filtro);
			
		if (listaDescontoCotaEspecifica.isEmpty()) {
			throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum registro encontrado.");
		} 
		
		Integer totalRegistros  = descontoService.buscarQuantidadeTipoDescontoCota(filtro);
		
		result.use(FlexiGridJson.class).from(listaDescontoCotaEspecifica).total(totalRegistros).page(page).serialize();
	}
	
	@Post
	@Path("/pesquisarDescontoProduto")
	public void pesquisarDescontoProduto(String codigo, String produto, String sortorder, String sortname, int page, int rp) throws Exception {
		
		FiltroTipoDescontoProdutoDTO filtro = carregarFiltroPesquisaDescontoProduto(codigo,produto,sortorder, sortname, page, rp);
		
		List<TipoDescontoProdutoDTO> listaTipoDescontoProduto  = descontoService.buscarTipoDescontoProduto(filtro);
		
		if(listaTipoDescontoProduto.isEmpty()){
			throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum registro encontrado.");
		}
		
		Integer totalRegistros  = descontoService.buscarQuantidadeTipoDescontoProduto(filtro);
		
		result.use(FlexiGridJson.class).from(listaTipoDescontoProduto).total(totalRegistros).page(page).serialize();
	}
	
	@Post
	@Path("/exibirCotasTipoDescontoProduto")
	public void exibirCotasTipoDescontoProduto(Long idTipoDescontoProduto, String sortorder) {

		PaginacaoVO paginacaoVO = new PaginacaoVO(null, null, sortorder);

		List<CotaDescontoProdutoDTO> cotas = 
					this.descontoService.obterCotasDoTipoDescontoProduto(idTipoDescontoProduto, paginacaoVO.getOrdenacao());

		if (cotas == null || cotas.isEmpty()) {
			
			throw new ValidacaoException(TipoMensagem.WARNING, "Nenhuma cota cadastrada para esse tipo de desconto.");
		}
		
		this.result.use(FlexiGridJson.class).from(cotas).total(cotas.size()).serialize();
	}
	
	@Post
	@Path("/excluirDesconto")
	public void excluirDesconto(Long idDesconto, TipoDesconto tipoDesconto ){
		
		descontoService.excluirDesconto(idDesconto, tipoDesconto);
		
		result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Operação realizada com sucesso."),
				Constantes.PARAM_MSGS).recursive().serialize();
	}
	
	private void exportarDescontoGeral(FileType fileType) throws IOException{
		
		FiltroTipoDescontoDTO filtroSessao = (FiltroTipoDescontoDTO) obterFiltroParaExportacao(FiltroTipoDescontoDTO.class);
		
		List<TipoDescontoDTO> listaTipoDescontoGeral  = descontoService.buscarTipoDesconto(filtroSessao);
		
		FileExporter.to("consulta-tipo-desconto-cota", fileType).inHTTPResponse(this.getNDSFileHeader(), filtroSessao, null, listaTipoDescontoGeral, TipoDescontoDTO.class, this.httpServletResponse);

	}
	
	private void exportarDescontoEspecifico(FileType fileType) throws IOException{
		
		FiltroTipoDescontoCotaDTO filtroSessao = (FiltroTipoDescontoCotaDTO) obterFiltroParaExportacao(FiltroTipoDescontoCotaDTO.class);
		
		List<TipoDescontoCotaDTO> listaDescontoCotaEspecifica = descontoService.buscarTipoDescontoCota(filtroSessao);
		
		FileExporter.to("consulta-tipo-desconto-cota", fileType).inHTTPResponse(this.getNDSFileHeader(), filtroSessao, null, listaDescontoCotaEspecifica,TipoDescontoCotaDTO.class, this.httpServletResponse);
	}
	
	private void exportarDescontoProduto(FileType fileType) throws IOException{
		
		FiltroTipoDescontoProdutoDTO filtroSessao = (FiltroTipoDescontoProdutoDTO) obterFiltroParaExportacao(FiltroTipoDescontoProdutoDTO.class);
		
		List<TipoDescontoProdutoDTO> listaTipoDescontoProduto  = descontoService.buscarTipoDescontoProduto(filtroSessao);
		
		FileExporter.to("consulta-tipo-desconto-cota", fileType).inHTTPResponse(this.getNDSFileHeader(), filtroSessao, null, listaTipoDescontoProduto, TipoDescontoProdutoDTO.class, this.httpServletResponse);
	}

	@Get
	public void exportar(FileType fileType, TipoDesconto tipoDesconto) throws IOException {
		
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
	private FiltroDTO obterFiltroParaExportacao(Class<?> clazz) {
		
		FiltroDTO filtro = null;
		
		if(FiltroTipoDescontoDTO.class.equals(clazz)){
		
			FiltroTipoDescontoDTO filtroSessao =
					(FiltroTipoDescontoDTO) this.session.getAttribute(FILTRO_PESQUISA_TIPO_DESCONTO_SESSION_ATTRIBUTE);
			
			filtro = filtroSessao;				
		}
		else if (FiltroTipoDescontoCotaDTO.class.equals(clazz)){
			
			FiltroTipoDescontoCotaDTO filtroSessao =
					(FiltroTipoDescontoCotaDTO) this.session.getAttribute(FILTRO_PESQUISA_TIPO_DESCONTO_COTA_SESSION_ATTRIBUTE);
			
			filtro = filtroSessao;
		}
		else if (FiltroTipoDescontoProdutoDTO.class.equals(clazz)){
			
			FiltroTipoDescontoProdutoDTO filtroSessao =
					(FiltroTipoDescontoProdutoDTO) this.session.getAttribute(FILTRO_PESQUISA_TIPO_DESCONTO_PRODUTO_SESSION_ATTRIBUTE);

			filtro = filtroSessao;
		}
		
		if (filtro != null) {
			
			if (filtro.getPaginacao() != null) {				
				filtro.getPaginacao().setPaginaAtual(null);
				filtro.getPaginacao().setQtdResultadosPorPagina(null);
			}
		}
		return filtro;
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
	
	private FiltroTipoDescontoDTO carregarFiltroPesquisaDescontoGeral(String sortorder, String sortname, int page, int rp) {

		FiltroTipoDescontoDTO filtro = new FiltroTipoDescontoDTO();
		
		PaginacaoVO paginacao = new PaginacaoVO(page, rp, sortorder);
		
		filtro.setPaginacao(paginacao);
	
		filtro.setOrdenacaoColuna(Util.getEnumByStringValue(FiltroTipoDescontoDTO.OrdenacaoColunaConsulta.values(), sortname));

		FiltroTipoDescontoDTO filtroSessao = (FiltroTipoDescontoDTO) 
				this.session.getAttribute(FILTRO_PESQUISA_TIPO_DESCONTO_SESSION_ATTRIBUTE);

		if (filtroSessao != null && !filtroSessao.equals(filtro)) {
			filtro.getPaginacao().setPaginaAtual(1);
		}
		
		session.setAttribute(FILTRO_PESQUISA_TIPO_DESCONTO_SESSION_ATTRIBUTE, filtro);
		
		return filtro;
	}
	
	private FiltroTipoDescontoCotaDTO carregarFiltroPesquisaDescontoEspecifico(Integer numeroCota,String nomeCota,
																			   String sortorder, String sortname, 
																			   int page, int rp) {

		FiltroTipoDescontoCotaDTO filtro = new FiltroTipoDescontoCotaDTO();
		
		filtro.setNumeroCota(numeroCota);
		filtro.setNomeCota(nomeCota);
		
		PaginacaoVO paginacao = new PaginacaoVO(page, rp, sortorder);
		
		filtro.setPaginacao(paginacao);
	
		filtro.setOrdenacaoColuna(Util.getEnumByStringValue(FiltroTipoDescontoCotaDTO.OrdenacaoColunaConsulta.values(), sortname));
		
		FiltroTipoDescontoCotaDTO filtroSessao = (FiltroTipoDescontoCotaDTO) 
				this.session.getAttribute(FILTRO_PESQUISA_TIPO_DESCONTO_COTA_SESSION_ATTRIBUTE);

		if (filtroSessao != null && !filtroSessao.equals(filtro)) {
			filtro.getPaginacao().setPaginaAtual(1);
		}
		
		session.setAttribute(FILTRO_PESQUISA_TIPO_DESCONTO_COTA_SESSION_ATTRIBUTE, filtro);
		
		return filtro;
	}
	
	private FiltroTipoDescontoProdutoDTO carregarFiltroPesquisaDescontoProduto(String codigo,String nomeProduto ,
																			   String sortorder, String sortname, 
																			   int page, int rp) {

		FiltroTipoDescontoProdutoDTO filtro = new FiltroTipoDescontoProdutoDTO();
		
		filtro.setCodigoProduto(codigo);
		filtro.setNomeProduto(nomeProduto);
			
		PaginacaoVO paginacao = new PaginacaoVO(page, rp, sortorder);
			
		filtro.setPaginacao(paginacao);
		
		filtro.setOrdenacaoColuna(Util.getEnumByStringValue(FiltroTipoDescontoProdutoDTO.OrdenacaoColunaConsulta.values(), sortname));
		
		FiltroTipoDescontoProdutoDTO filtroSessao = (FiltroTipoDescontoProdutoDTO) 
				this.session.getAttribute(FILTRO_PESQUISA_TIPO_DESCONTO_PRODUTO_SESSION_ATTRIBUTE);

		if (filtroSessao != null && !filtroSessao.equals(filtro)) {
			filtro.getPaginacao().setPaginaAtual(1);
		}
				
		session.setAttribute(FILTRO_PESQUISA_TIPO_DESCONTO_PRODUTO_SESSION_ATTRIBUTE, filtro);
		
		return filtro;
	}
	
	private Usuario getUsuario() {
		
		Usuario usuario = new Usuario();
		
		usuario.setId(1L);
		
		usuario.setNome("Jornaleiro da Silva");
		
		return usuario;
	}
	
	
	@Post
	@Path("/obterFornecedores")
	public void obterFornecedores(){
		
		List<Fornecedor> fornecedores =   fornecedorService.obterFornecedores();
		
		result.use(Results.json()).from(this.getFornecedores(fornecedores),"result").recursive().serialize();
	}
	
	@Post
	@Path("/obterFornecedoresCota")
	public void obterFornecedoresCota(Integer numeroCota){
		
		Cota cota = cotaService.obterPorNumeroDaCota(numeroCota);
		
		List<Fornecedor> fornecedores =   cotaService.obterFornecedoresCota(cota.getId());
		
		if(fornecedores.isEmpty()){
			throw new ValidacaoException(TipoMensagem.WARNING,"A cota informada não possui fornecedores associados!");
		}
		
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
	
	@Post
	@Path("/obterFornecedoresAssociadosDesconto")
	public void obterFornecedoresAssociadosDesconto(Long idDesconto, TipoDesconto tipoDesconto,String sortorder, String sortname){
		
		List<Fornecedor> fornecedores =   descontoService.busacarFornecedoresAssociadosADesconto(idDesconto, tipoDesconto);
		
		List<ItemDTO<Long, String>> lista = getFornecedores(fornecedores);
		
		Ordenacao ordenacao = Util.getEnumByStringValue(Ordenacao.values(), sortorder);
		
		PaginacaoUtil.ordenarEmMemoria(lista, ordenacao, sortname);
		
		result.use(FlexiGridJson.class).from(lista).total(fornecedores.size()).page(1).serialize();
	}

	private void resultProcessoCadastroDesconto(Future<String> future) {
		
		String resultado = null;		
			
		try {
			
			if (future.isDone()) {
				resultado = future.get();
				result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS,  resultado),"result").recursive().serialize();
			} else {
				result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Inicio do procedimento de cadastros de Tipo Desconto foi inicializado"),"result").recursive().serialize();

			}

		} catch (Exception e) {
			
			List<String> mensagens = new ArrayList<String>();
			
			if (e instanceof ValidacaoException) {
			
				mensagens = ((ValidacaoException) e).getValidacao().getListaMensagens();
			
			} else {
				mensagens.add(e.getMessage());
			}
			
			throw new ValidacaoException(TipoMensagem.ERROR, mensagens);
		}
		
	}
	
	@Get
	@Path("/verificaProgressoGravacaoDescontoGeral")
	public void verificaProgressoGravacaoDescontoGeral() {
		result.use(Results.json()).from(ProcessoCadastroDescontoSingleton.getInstance(),"result").recursive().serialize();
	}

	@Get
	@Path("/verificaProgressoGravacaoDescontoEspecifico")
	public void verificaProgressoGravacaoDescontoEspecifico() {
		result.use(Results.json()).from(ProcessoCadastroDescontoSingleton.getInstance(), "result").recursive().serialize();
	}

	@Get
	@Path("/verificaProgressoGravacaoDescontoProduto")
	public void verificaProgressoGravacaoDescontoProduto() {
		result.use(Results.json()).from(ProcessoCadastroDescontoSingleton.getInstance(),"result").recursive().serialize();
	}

}
