package br.com.abril.nds.controllers.administracao;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.vo.ValidacaoVO;
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
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.desconto.TipoDesconto;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.serialization.custom.FlexiGridJson;
import br.com.abril.nds.service.DescontoService;
import br.com.abril.nds.service.FornecedorService;
import br.com.abril.nds.util.Constantes;
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
	private DescontoService descontoService;

	@Autowired
	private HttpSession session;
	
	@Autowired
	private DistribuidorService distribuidorService;
	
	@Autowired
	private FornecedorService fornecedorService;
	
	@Autowired
	private HttpServletResponse httpServletResponse;

	private String FILTRO_PESQUISA_TIPO_DESCONTO_PRODUTO_SESSION_ATTRIBUTE= "filtroPesquisaPorProduto";

	private String FILTRO_PESQUISA_TIPO_DESCONTO_SESSION_ATTRIBUTE = "filtroPesquisaPorGeral";
	
	private static final String FILTRO_PESQUISA_TIPO_DESCONTO_COTA_SESSION_ATTRIBUTE = "filtroPesquisaPorCota";
	
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
			
		result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Desconto cadastrado com sucesso"),"result").recursive().serialize();
	}

	@Post
	@Path("/novoDescontoEspecifico")
	public void novoDescontoEspecifico(Integer numeroCota, BigDecimal desconto, List<Long> fornecedores) {
		
		if(numeroCota == null ){
			throw new ValidacaoException(TipoMensagem.WARNING,"O campo Cota deve ser preenchido!");
		}
		
		if(fornecedores == null || fornecedores.isEmpty()){
			throw new ValidacaoException(TipoMensagem.WARNING,"O campo Fornecedores selecionados deve ser preenchido!");
		}
		
		if(desconto == null ){
			throw new ValidacaoException(TipoMensagem.WARNING,"O campo Desconto deve ser preenchido!");
		}
		
		//FIXME revisar a implementação da inclusão de um novo desconto especifico
		
		result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Desconto cadastrado com sucesso"),"result").recursive().serialize();
	}
	
	@Post
	@Path("/novoDescontoProduto")
	public void novoDescontoProduto(String codigoProduto, Long edicaoProduto, Integer quantidadeEdicoes, 
									boolean isCheckedEdicao, boolean hasCotaEspecifica,
									BigDecimal descontoProduto, List<Integer> cotas, boolean descontoPredominante) {		
		//FIXME revisar a implementação da inclusão de um novo desconto de produto
		
		List<String> mensagens = new ArrayList<String>();
		
		if (codigoProduto == null || codigoProduto.isEmpty()) {
			
			mensagens.add("O campo Código deve ser preenchido!");
		}
		
		if (isCheckedEdicao && (edicaoProduto == null || quantidadeEdicoes == null)) {
			
			mensagens.add("O campo Edição específica ou Edições deve ser preenchido!");
		}

		if (descontoProduto == null) {
			
			mensagens.add("O campo Desconto deve ser preenchido!");
		}
		
		if (hasCotaEspecifica && cotas == null) {
			
			mensagens.add("Ao menos uma cota deve ser selecionada!");
		}
		
		if (!mensagens.isEmpty()) {
			
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, mensagens));
		}
		
		this.result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Desconto cadastrado com sucesso"),"result").recursive().serialize();
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
	@Path("/excluirDesconto")
	public void excluirDesconto(Long idDesconto, TipoDesconto tipoDesconto ){
		
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
