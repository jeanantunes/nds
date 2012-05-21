package br.com.abril.nds.controllers.administracao;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.util.PaginacaoUtil;
import br.com.abril.nds.client.vo.TipoDescontoCotaVO;
import br.com.abril.nds.client.vo.ValidacaoVO;
import br.com.abril.nds.dto.CotaDTO;
import br.com.abril.nds.dto.filtro.FiltroCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroParciaisDTO;
import br.com.abril.nds.dto.filtro.FiltroCotaDTO.OrdemColuna;
import br.com.abril.nds.dto.filtro.FiltroTipoDescontoCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroTipoDescontoCotaDTO.OrdenacaoColunaConsulta;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.EspecificacaoDesconto;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.cadastro.TipoDescontoCota;
import br.com.abril.nds.service.CotaService;
import br.com.abril.nds.service.ProdutoEdicaoService;
import br.com.abril.nds.service.ProdutoService;
import br.com.abril.nds.service.TipoDescontoCotaService;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.Constantes;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.TipoMensagem;
import br.com.abril.nds.util.Util;
import br.com.abril.nds.vo.PaginacaoVO;
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
	private TipoDescontoCotaService tipoDescontoCotaService;
	
	@Autowired
	private CotaService cotaService;
	
	@Autowired
	private ProdutoEdicaoService produtoEdicaoService;
	
	@Autowired
	private ProdutoService produtoService;
	
	@Autowired
	private HttpSession session;
	
	private static final String FILTRO_PESQUISA_TIPO_DESCONTO_COTA_SESSION_ATTRIBUTE = "filtroPesquisaTipoDescontoCota";
	private static final String QTD_REGISTROS_PESQUISA_TIPO_DESCONTO_COTA_SESSION_ATTRIBUTE = "qtdRegistrosPesquisaTipoDescontoCota";

	
	public TipoDescontoCotaController(Result result, HttpSession session) {
		this.result = result; 
		this.session = session;
	}
	
	@Path("/")
	public void index() {		
		inserirDataAtual();		
	}
	
	@Post
	@Path("/novoDescontoGeral")
	public void novoDescontoGeral(String desconto, String dataAlteracao, String usuario){
		try {
			BigDecimal descontoFormatado = new BigDecimal(Double.parseDouble(desconto));
			TipoDescontoCota descontoGeral = popularDescontoParaCadastrar(desconto,dataAlteracao, usuario, EspecificacaoDesconto.GERAL);			
			atualizarDistribuidor(descontoFormatado);
			descontoGeral.setIdCota((long) 0);
			descontoGeral.setIdProduto(0l);
			descontoGeral.setNumeroEdicao(0l);
			salvarDesconto(descontoGeral);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Desconto cadastrado com sucesso"),"result").recursive().serialize();
	}

	
	
	@Post
	@Path("/novoDescontoEspecifico")
	public void novoDescontoEspecifico(String cotaEspecifica, String nomeEspecifico, String descontoEspecifico, String dataAlteracaoEspecifico, String usuarioEspecifico){
		try {
			Cota cotaParaAtualizar = this.cotaService.obterCotaPDVPorNumeroDaCota(Integer.parseInt(cotaEspecifica));		
			atualizarCota(new BigDecimal(descontoEspecifico), cotaParaAtualizar);
			TipoDescontoCota especifico = popularDescontoParaCadastrar(descontoEspecifico, dataAlteracaoEspecifico, usuarioEspecifico, EspecificacaoDesconto.ESPECIFICO);
			especifico.setIdCota(cotaParaAtualizar.getNumeroCota().longValue());
			especifico.setIdProduto(0l);
			especifico.setNumeroEdicao(0l);
			salvarDesconto(especifico);
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
			TipoDescontoCota tipoDescontoProduto = popularDescontoParaCadastrar(descontoProduto, dataAlteracaoProduto, usuarioProduto, EspecificacaoDesconto.PRODUTO);
			tipoDescontoProduto.setIdCota(0l);
			tipoDescontoProduto.setIdProduto(Long.parseLong(produtoEdicao.getProduto().getCodigo()));
			tipoDescontoProduto.setNumeroEdicao(Long.parseLong(edicaoProduto));
			salvarDesconto(tipoDescontoProduto);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		result.use(Results.json()).from(new ValidacaoVO(TipoMensagem.SUCCESS, "Desconto cadastrado com sucesso"),"result").recursive().serialize();
	}
	
	@Path("/pesquisarDescontoGeral")
	public void pesquisarDescontoGeral(String sortorder, String sortname, int page, int rp) throws Exception {
		
		FiltroTipoDescontoCotaDTO filtro = carregarFiltroPesquisaDescontoGeral(sortorder, sortname, page, rp);	
		
		List<TipoDescontoCotaVO> listaDescontoCotaVO = null;		
		try {			
			listaDescontoCotaVO = tipoDescontoCotaService.obterTipoDescontoCota(EspecificacaoDesconto.GERAL);
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
		
			TableModel<CellModelKeyValue<TipoDescontoCotaVO>> tableModel =
					new TableModel<CellModelKeyValue<TipoDescontoCotaVO>>();
	
			tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listaDescontoCotaVO));
			tableModel.setPage(filtro.getPaginacao().getPaginaAtual());
			tableModel.setTotal(qtdeTotalRegistros);
	
			result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
		}
	}
	
	@Post
	@Path("/pesquisarDescontoEspecifico")
	public void pesquisarDescontoEspecifico(String cotaEspecifica, String nomeEspecifico, String sortorder, String sortname, int page, int rp) throws Exception {
		FiltroCotaDTO filtroCotaDTO = popularFiltroCotaDTO(cotaEspecifica,	nomeEspecifico);
		List<CotaDTO> listaDeCotas = this.cotaService.obterCotas(filtroCotaDTO);
		
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
		
			TableModel<CellModelKeyValue<TipoDescontoCotaVO>> tableModel =
					new TableModel<CellModelKeyValue<TipoDescontoCotaVO>>();
	
			tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listaDescontoCotaVO));
			//tableModel.setPage(filtro.getPaginacao().getPaginaAtual());
			tableModel.setTotal(qtdeTotalRegistros);
	
			result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
		}
	}
	
	@Post
	@Path("/pesquisarDescontoProduto")
	public void pesquisarDescontoProduto(String codigo, String produto, String sortorder, String sortname, int page, int rp) throws Exception {
		Produto produtoPesquisado = null;
		List<TipoDescontoCotaVO> listaDescontoCotaVO = 	null;
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
		
			TableModel<CellModelKeyValue<TipoDescontoCotaVO>> tableModel =
					new TableModel<CellModelKeyValue<TipoDescontoCotaVO>>();
	
			tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listaDescontoCotaVO));
			//tableModel.setPage(filtro.getPaginacao().getPaginaAtual());
			tableModel.setTotal(qtdeTotalRegistros);
	
			result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
		}
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
		List<TipoDescontoCotaVO> listaVO = this.tipoDescontoCotaService.obterTipoDescontoCota(EspecificacaoDesconto.ESPECIFICO);
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
		List<TipoDescontoCotaVO> listaVO = this.tipoDescontoCotaService.obterTipoDescontoCota(EspecificacaoDesconto.PRODUTO);
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
	
	private TipoDescontoCota popularDescontoParaCadastrar(String desconto,	String dataAlteracao, String usuario, EspecificacaoDesconto especificacaoDesconto) throws ParseException {
		TipoDescontoCota tipoDescontoCota = new TipoDescontoCota();
		tipoDescontoCota.setDesconto(new BigDecimal(Double.parseDouble(desconto)));
		SimpleDateFormat sdf = new SimpleDateFormat(Constantes.DATE_PATTERN_PT_BR);
		Date dataFormatada;
		dataFormatada = sdf.parse(dataAlteracao);
		tipoDescontoCota.setDataAlteracao(dataFormatada);
		tipoDescontoCota.setUsuario(usuario);
		tipoDescontoCota.setEspecificacaoDesconto(especificacaoDesconto);
		return tipoDescontoCota;
	}
	
	private void salvarDesconto(TipoDescontoCota tipoDescontoCota) {
		if(tipoDescontoCota.getEspecificacaoDesconto().equals(EspecificacaoDesconto.GERAL)){
			int sequencial = this.tipoDescontoCotaService.obterUltimoSequencial();
			tipoDescontoCota.setSequencial(sequencial + 1);
		}else{
			tipoDescontoCota.setSequencial(0);
		}
		this.tipoDescontoCotaService.incluirDesconto(tipoDescontoCota);
	}

	private void atualizarDistribuidor(BigDecimal desconto) {
		this.tipoDescontoCotaService.atualizarDistribuidores(desconto);
	}
	
	private void atualizarCota(BigDecimal descontoEspecifico, Cota cotaParaAtualizar) {
		cotaParaAtualizar.setFatorDesconto(descontoEspecifico);
		this.cotaService.alterarCota(cotaParaAtualizar);
	}
	
	private FiltroTipoDescontoCotaDTO carregarFiltroPesquisaDescontoGeral(String sortorder, String sortname, int page, int rp) {

		FiltroTipoDescontoCotaDTO filtro = new FiltroTipoDescontoCotaDTO();
		
		this.configurarPaginacaoPesquisaLancamentos(filtro, sortorder, sortname,
				page, rp);

		FiltroTipoDescontoCotaDTO filtroSessao = (FiltroTipoDescontoCotaDTO) this.session
				.getAttribute(FILTRO_PESQUISA_TIPO_DESCONTO_COTA_SESSION_ATTRIBUTE);

		if (filtroSessao != null && !filtroSessao.equals(filtro)) {

			filtro.getPaginacao().setPaginaAtual(1);
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

}
