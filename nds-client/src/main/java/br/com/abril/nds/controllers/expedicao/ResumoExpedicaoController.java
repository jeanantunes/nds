package br.com.abril.nds.controllers.expedicao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.vo.ResultadoResumoExpedicaoVO;
import br.com.abril.nds.client.vo.ResumoExpedicaoVO;
import br.com.abril.nds.controllers.exception.ValidacaoException;
import br.com.abril.nds.dto.ExpedicaoDTO;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.filtro.FiltroResumoExpedicaoDTO;
import br.com.abril.nds.service.ExpedicaoService;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.DateUtil;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.TipoMensagem;
import br.com.abril.nds.util.Util;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

/**
 * Classe responsável pelo controle das ações/requisições referente a 
 * tela de resumo de expedição de produto/box 
 * 
 * @author Discover Technology
 *
 */
@Resource
@Path(value="/expedicao")
public class ResumoExpedicaoController {
	
	@Autowired
	private Result result;
	
	@Autowired
	private HttpSession session;
	
	@Autowired
	private ExpedicaoService expedicaoService;
	
	private static final String FILTRO_SESSION_ATTRIBUTE = "filtroResumo";
	
	private enum TipoPesquisaResumoExpedicao{
		
		PRODUTO("Produto"),
		BOX("Box");
		
		private TipoPesquisaResumoExpedicao(String nome) {
			this.nome = nome;
		}
		
		public String getNome() {
			return nome;
		}
		private String nome;
	}
	

	/**
	 * Carrega as informações default da tela de pesquisa.
	 */
	@Get
	public void resumo(){
		
		carregarTiposResumo();
	}
	
	/**
	 * Pesquisa de resumo de expedição de edições agrupadas por produto.
	 * 
	 * @param filtro
	 * @param tipoPesquisa
	 * @param sortorder
	 * @param sortname
	 * @param page
	 * @param rp
	 */
	@Post
	@Path("/resumo/pesquisar/produto")
	public void pesquisarResumoExpedicaoProduto(String dataLancamento, String sortorder, String sortname, int page, int rp){
		
		this.validarPrametroPesquisa(dataLancamento);
		
		FiltroResumoExpedicaoDTO filtro = new FiltroResumoExpedicaoDTO();
		
		filtro.setDataLancamento(DateUtil.parseData(dataLancamento, "dd/MM/yyyy"));
		
		this.configurarPaginacaoPesquisaResumoProduto(filtro, sortorder, sortname, page, rp);
		
		this.tratarFiltro(filtro);
		
		this.pesquisarResumoProduto(filtro);
		
	}
	
	/**
	 * Pesquisa de resumo de expedição de edições agrupadas por box.
	 * 
	 * @param dataLancamento
	 * @param sortorder
	 * @param sortname
	 * @param page
	 * @param rp
	 */
	@Post
	@Path("/resumo/pesquisar/box")
	public void pesquisarResumoExpedicaoBox(String dataLancamento,String sortorder, String sortname, int page, int rp){
		
		this.validarPrametroPesquisa(dataLancamento);
		
		FiltroResumoExpedicaoDTO filtro = new FiltroResumoExpedicaoDTO();
		
		filtro.setDataLancamento(DateUtil.parseData(dataLancamento, "dd/MM/yyyy"));
		
		this.configurarPaginacaoPesquisaResumoBox(filtro, sortorder, sortname, page, rp);
		
		this.tratarFiltro(filtro);
		
		this.pesquisarResumoBox(filtro);
		
	}
	
	/**
	 * Executa tratamento de paginação em função de alteração do filtro de pesquisa.
	 * 
	 * @param filtroResumoExpedicao
	 */
	private void tratarFiltro(FiltroResumoExpedicaoDTO filtroResumoExpedicao) {

		FiltroResumoExpedicaoDTO filtroResumoExpedicaoSession = (FiltroResumoExpedicaoDTO) session
				.getAttribute(FILTRO_SESSION_ATTRIBUTE);
		
		if (filtroResumoExpedicaoSession != null
				&& !filtroResumoExpedicaoSession.equals(filtroResumoExpedicao)) {

			filtroResumoExpedicao.getPaginacao().setPaginaAtual(1);
		}
		
		session.setAttribute(FILTRO_SESSION_ATTRIBUTE, filtroResumoExpedicao);
	}
	
	/**
	 * Verifica se os parametros de pesquisas são validos.
	 * 
	 * @param dataLancamento
	 */
	private void validarPrametroPesquisa(String dataLancamento){
		
		
		if (dataLancamento == null || dataLancamento.isEmpty()) {
			
			throw new ValidacaoException(TipoMensagem.WARNING,"O preenchimento do campo \"Data Lançamento\" é obrigatório." );
		}
		
		if (!DateUtil.isValidDate(dataLancamento, "dd/MM/yyyy")) {
			
			throw new ValidacaoException(TipoMensagem.ERROR,"Data Lançamento inválida." );
		}
	}
	
	/**
	 * Monta os dados de pesquisa de box expedidos para exibição na tela.
	 * 
	 * @param page
	 */
	private void pesquisarResumoBox(FiltroResumoExpedicaoDTO filtro){
		
		List<ResumoExpedicaoVO> listaLancamentosExpedidos = new LinkedList<ResumoExpedicaoVO>();
		
		BigDecimal qtdeTotalReparte = BigDecimal.TEN;
		BigDecimal valorTotalFaturado = BigDecimal.ONE;
		
		//Obter no banco de dados os dados referentes a pesquisa de Box
		
		int quantidadeRegistros = 30; //Obter no banco de dados o total de registros
		
		for (int i = 0; i < quantidadeRegistros; i++){
			
			ResumoExpedicaoVO resumoExpedicaoVO = new ResumoExpedicaoVO();
			resumoExpedicaoVO.setId(i);
			resumoExpedicaoVO.setCodigoBox(i*7+"");
			resumoExpedicaoVO.setDescricaoBox("Box " + i);
			resumoExpedicaoVO.setDataLancamento("10/10/2010");
			resumoExpedicaoVO.setQntProduto(i*2+"");
			resumoExpedicaoVO.setReparte("2000");
			resumoExpedicaoVO.setValorFaturado("5555555");
			
			listaLancamentosExpedidos.add(resumoExpedicaoVO);
		}
		
		TableModel<CellModelKeyValue<ResumoExpedicaoVO>> tableModel = new TableModel<CellModelKeyValue<ResumoExpedicaoVO>>();

		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listaLancamentosExpedidos));

		tableModel.setTotal(quantidadeRegistros);

		tableModel.setPage(filtro.getPaginacao().getPaginaAtual());

		String valorTotalFaturadoFormatado = CurrencyUtil.formatarValor(valorTotalFaturado);
		

		ResultadoResumoExpedicaoVO resultadoResumoExpedicao = new ResultadoResumoExpedicaoVO(tableModel, qtdeTotalReparte.intValue(), valorTotalFaturadoFormatado);

		result.use(Results.json()).withoutRoot().from(resultadoResumoExpedicao).recursive().serialize();
	}
	
	/**
	 * Monta os dados de pesquisa de produtos expedidos para exibição na tela.
	 * 
	 * @param page
	 */
	private void pesquisarResumoProduto(FiltroResumoExpedicaoDTO filtro){
		
		Long quantidadeRegistros = expedicaoService.obterQuantidadeResumoExpedicaoPorProduto(filtro);
		
		List<ExpedicaoDTO> list = expedicaoService.obterResumoExpedicaoPorProduto(filtro);
		
		if (list == null || list.isEmpty()){
			throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum registro encontrado.");
		}
		
		List<ResumoExpedicaoVO> listaLancamentosExpedidos = new LinkedList<ResumoExpedicaoVO>();
		
		BigDecimal qtdeTotalReparte = BigDecimal.ZERO;
		BigDecimal valorTotalFaturado = BigDecimal.ZERO;
		
		ResumoExpedicaoVO resumoExpedicaoVO = null;
		
		for (ExpedicaoDTO expd  : list){
			
			resumoExpedicaoVO = new ResumoExpedicaoVO();
			resumoExpedicaoVO.setCodigoProduto(expd.getCodigoProduto());
			resumoExpedicaoVO.setDescricaoProduto(expd.getNomeProduto());
			resumoExpedicaoVO.setEdicaoProduto(expd.getNumeroEdicao()+"");
			resumoExpedicaoVO.setPrecoCapa(CurrencyUtil.formatarValor(expd.getPrecoCapa()));
			resumoExpedicaoVO.setReparte(expd.getQntReparte().intValue()+"");
			resumoExpedicaoVO.setValorFaturado(CurrencyUtil.formatarValor(expd.getValorFaturado()));
			resumoExpedicaoVO.setQntDiferenca(expd.getQntDiferenca().intValue()+"");
			
			valorTotalFaturado = valorTotalFaturado.add(expd.getValorFaturado());
			qtdeTotalReparte = qtdeTotalReparte.add(expd.getQntReparte());
			
			listaLancamentosExpedidos.add(resumoExpedicaoVO);
		}
		
		TableModel<CellModelKeyValue<ResumoExpedicaoVO>> tableModel = new TableModel<CellModelKeyValue<ResumoExpedicaoVO>>();

		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listaLancamentosExpedidos));

		tableModel.setTotal((quantidadeRegistros!= null)? quantidadeRegistros.intValue():0);

		tableModel.setPage(filtro.getPaginacao().getPaginaAtual());

		String valorTotalFaturadoFormatado = CurrencyUtil.formatarValor(valorTotalFaturado);
		
		
		ResultadoResumoExpedicaoVO resultadoResumoExpedicao = new ResultadoResumoExpedicaoVO(tableModel, qtdeTotalReparte.intValue(), valorTotalFaturadoFormatado);

		result.use(Results.json()).withoutRoot().from(resultadoResumoExpedicao).recursive().serialize();

	}
	
	/**
	 * Configura paginação da lista de resumo de expedição por produto.
	 * 
	 * @param filtro
	 * @param sortorder
	 * @param sortname
	 * @param page
	 * @param rp
	 */
	private void configurarPaginacaoPesquisaResumoProduto(FiltroResumoExpedicaoDTO filtro, 
														 String sortorder,String sortname, 
														 int page, int rp) {

		if (filtro != null) {
		
			PaginacaoVO paginacao = new PaginacaoVO(page, rp, sortorder);
			
			filtro.setPaginacao(paginacao);
			
			filtro.setOrdenacaoColunaProduto(Util.getEnumByStringValue(FiltroResumoExpedicaoDTO.OrdenacaoColunaProduto.values(), sortname));
			
		}
	}
	
	
	/**
	 * Configura paginação da lista de resumo se expedição por box.
	 * 
	 * @param filtro
	 * @param sortorder
	 * @param sortname
	 * @param page
	 * @param rp
	 */
	private void configurarPaginacaoPesquisaResumoBox(FiltroResumoExpedicaoDTO filtro, 
														  String sortorder, String sortname,
														  int page, int rp) {

		if (filtro != null) {

			PaginacaoVO paginacao = new PaginacaoVO(page, rp, sortorder);

			filtro.setPaginacao(paginacao);

			filtro.setOrdenacaoColunaProduto(Util.getEnumByStringValue(FiltroResumoExpedicaoDTO.OrdenacaoColunaProduto.values(),sortname));
		}
	}
		
	/**
	 * Carrega o combo de Tipo de Consulta.
	 */
	private void carregarTiposResumo(){
		
		List<ItemDTO<TipoPesquisaResumoExpedicao, String>> listaTipoResumo = new ArrayList<ItemDTO<TipoPesquisaResumoExpedicao,String>>();
		
		listaTipoResumo.add(new ItemDTO<TipoPesquisaResumoExpedicao, String>(TipoPesquisaResumoExpedicao.BOX,TipoPesquisaResumoExpedicao.BOX.getNome()));
		listaTipoResumo.add( new ItemDTO<TipoPesquisaResumoExpedicao, String>(TipoPesquisaResumoExpedicao.PRODUTO,TipoPesquisaResumoExpedicao.PRODUTO.getNome()));
		
		result.include("listaTipoResumo",listaTipoResumo );
	}
	
}
