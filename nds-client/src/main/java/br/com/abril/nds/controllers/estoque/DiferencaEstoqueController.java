package br.com.abril.nds.controllers.estoque;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.vo.LancamentoDiferencaVO;
import br.com.abril.nds.client.vo.ResultadoLancamentoDiferencaVO;
import br.com.abril.nds.client.vo.ValidacaoVO;
import br.com.abril.nds.controllers.exception.ValidacaoException;
import br.com.abril.nds.controllers.lancamento.FuroProdutoController;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.filtro.FiltroLancamentoDiferencaEstoqueDTO;
import br.com.abril.nds.dto.filtro.FiltroLancamentoDiferencaEstoqueDTO.OrdenacaoColuna;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.estoque.Diferenca;
import br.com.abril.nds.model.estoque.TipoDiferenca;
import br.com.abril.nds.service.DiferencaEstoqueService;
import br.com.abril.nds.service.FornecedorService;
import br.com.abril.nds.util.CellModel;
import br.com.abril.nds.util.CellModelKeyValue;
import br.com.abril.nds.util.Constantes;
import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.TipoMensagem;
import br.com.abril.nds.util.Util;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.core.Localization;
import br.com.caelum.vraptor.view.Results;

@Resource
@Path("/estoque/diferenca")
public class DiferencaEstoqueController {

	private Result result;
	
	private Localization localization;
	
	@Autowired
	private FornecedorService fornecedorService;
	
	@Autowired
	private DiferencaEstoqueService diferencaEstoqueService;

	public DiferencaEstoqueController(Result result, Localization localization) {
		
		this.result = result;
		this.localization = localization;
	}
	
	@Get
	public void lancamento() {
		
		this.carregarCombosLancamento();
	}
	
	@Post
	@Path("/lancamento/pesquisa")
	public void pesquisarLancamentos(FiltroLancamentoDiferencaEstoqueDTO filtro, 
									 String sortorder, String sortname, int page, int rp) {

		this.configurarPaginacaoPesquisaLancamentos(filtro, sortorder, sortname, page, rp);
		
		this.processarDiferencasLancamentoMock(page);
		
		List<Diferenca> listaLancamentoDiferencas = 
			this.diferencaEstoqueService.obterDiferencasLancamento(filtro);
		
		if (listaLancamentoDiferencas == null || listaLancamentoDiferencas.isEmpty()) {
			
			//throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum registro encontrado.");
			
		} else {
			
			this.processarDiferencasLancamento(listaLancamentoDiferencas, filtro);
		}
	}

	@Get
	public void consulta() {
		this.carregarCombosConsulta();
		
		result.include("dataAtual", new SimpleDateFormat(Constantes.DATE_PATTERN_PT_BR).format(new Date()));
	}
	
	@Post
	@Path("/pesquisarDiferencas")
	public void pesquisarDiferencas(String codigoProduto, Long numeroEdicao,
									Long idFornecedor, String dataLancamentoDe,
									String dataLancamentoAte, TipoDiferenca tipoDiferenca,
									String sortorder, String sortname, int page, int rp) {
		
		//List<Diferenca> lista = diferencaEstoqueService.obterDiferencas(new FiltroConsultaDiferencaEstoqueDTO());
		
		Diferenca dto = new Diferenca();
		
		List<Diferenca> listaDiferencaDTO = new ArrayList<Diferenca>();
		
		listaDiferencaDTO.add(dto);
		listaDiferencaDTO.add(dto);
		listaDiferencaDTO.add(dto);
		listaDiferencaDTO.add(dto);
		listaDiferencaDTO.add(dto);
		listaDiferencaDTO.add(dto);
		listaDiferencaDTO.add(dto);
		listaDiferencaDTO.add(dto);
		listaDiferencaDTO.add(dto);
		listaDiferencaDTO.add(dto);
		listaDiferencaDTO.add(dto);
		listaDiferencaDTO.add(dto);
		listaDiferencaDTO.add(dto);
		listaDiferencaDTO.add(dto);
		listaDiferencaDTO.add(dto);
		listaDiferencaDTO.add(dto);
		listaDiferencaDTO.add(dto);
		listaDiferencaDTO.add(dto);
		listaDiferencaDTO.add(dto);
		listaDiferencaDTO.add(dto);
		listaDiferencaDTO.add(dto);		
		
		List<CellModel> listaModeloGenerico = new LinkedList<CellModel>();
		
		for (Diferenca diferencaDTO : listaDiferencaDTO) {
			
			String data = "a";
			String codigo = "b";
			String produto = "c";
			String edicao = "d";
			String tipoDiferencao = "e";
			String nota = "f";
			String exemplar = "g";
			String status = "h";
			
			listaModeloGenerico.add(new CellModel(1, data, codigo, produto, edicao, tipoDiferencao, nota, exemplar, status));
		}
		
		TableModel<CellModel> tm = new TableModel<CellModel>();
		
		//tm.setPage(1);
		
		tm.setTotal(listaModeloGenerico.size());
		
		tm.setRows(listaModeloGenerico);
		
		Map resultado = new HashMap();
		
		resultado.put("TblModelListaFaltasSobras", tm);
		
		result.use(Results.json()).withoutRoot().from(resultado).recursive().serialize();
	}

	/**
	 * Método responsável por carregar todos os combos da tela de consulta.
	 */
	public void carregarCombosConsulta() {
		this.carregarComboTiposDiferenca();
		this.carregarComboFornecedores();
	}
	
	@Post
	public void excluirFaltaSobra(Long idDiferenca){
		result.use(Results.json()).from(
				new ValidacaoVO(TipoMensagem.SUCCESS, "Operação efetuada com sucesso."), 
				Constantes.PARAM_MSGS).recursive().serialize();
		
		//result.forwardTo(DiferencaEstoqueController.class).lancamento();
	}
	
	/**
	 * Método responsável por carregar todos os combos da tela de lançamento.
	 */
	private void carregarCombosLancamento() {
		
		this.carregarComboTiposDiferenca();
	}
	
	/**
	 * Método responsável por carregar o combo de tipos de diferença.
	 */
	private void carregarComboTiposDiferenca() {

		List<ItemDTO<TipoDiferenca, String>> listaTiposDiferenca =
			new ArrayList<ItemDTO<TipoDiferenca, String>>();
		
		listaTiposDiferenca.add(
			new ItemDTO<TipoDiferenca, String>(TipoDiferenca.FALTA_DE, TipoDiferenca.FALTA_DE.getDescricao())
		);
		
		listaTiposDiferenca.add(
			new ItemDTO<TipoDiferenca, String>(TipoDiferenca.FALTA_EM, TipoDiferenca.FALTA_EM.getDescricao())
		);
		
		listaTiposDiferenca.add(
			new ItemDTO<TipoDiferenca, String>(TipoDiferenca.SOBRA_DE, TipoDiferenca.SOBRA_DE.getDescricao())
		);
		
		listaTiposDiferenca.add(
			new ItemDTO<TipoDiferenca, String>(TipoDiferenca.SOBRA_EM, TipoDiferenca.SOBRA_EM.getDescricao())
		);
		
		result.include("listaTiposDiferenca", listaTiposDiferenca);
	}
	
	/**
	 * Método responsável por carregar o combo de fornecedores.
	 */
	private void carregarComboFornecedores() {
		
		//TODO: adicionar todos os fornecedores no combo
		
		List<Fornecedor> listaFornecedor = fornecedorService.obterFornecedores();
		
		List<ItemDTO<Long, String>> listaFornecedoresCombo =
			new ArrayList<ItemDTO<Long,String>>();
		
		for (Fornecedor fornecedor : listaFornecedor) {
			listaFornecedoresCombo.add(
				new ItemDTO<Long, String>(fornecedor.getId(), fornecedor.getJuridica().getNomeFantasia())
			);
		}
		
		result.include("listaFornecedores", listaFornecedoresCombo);
	}
	
	/*
	 * Processa o resulta das diferenças para lançamento.
	 *  
	 * @param listaDiferencas - lista de diferenças
	 * @param filtro - filtro da pesquisa
	 */
	private void processarDiferencasLancamento(List<Diferenca> listaDiferencas, 
											   FiltroLancamentoDiferencaEstoqueDTO filtro) {

		List<LancamentoDiferencaVO> listaLancamentosDiferenca = new LinkedList<LancamentoDiferencaVO>();
		
		BigDecimal qtdeTotalDiferencas = BigDecimal.ZERO;
		BigDecimal valorTotalDiferencas = BigDecimal.ZERO;
		
		int quantidadeRegistros = 30;
		
		for (Diferenca diferenca : listaDiferencas) {
			
			ProdutoEdicao produtoEdicao = diferenca.getProdutoEdicao();
			
			Produto produto = produtoEdicao.getProduto();
			
			LancamentoDiferencaVO lancamentoDiferenca = new LancamentoDiferencaVO();
			
			lancamentoDiferenca.setId(diferenca.getId().intValue());
			lancamentoDiferenca.setCodigoProduto(produto.getCodigo());
			lancamentoDiferenca.setDescricaoProduto(produto.getDescricao());
			lancamentoDiferenca.setNumeroEdicao(produtoEdicao.getNumeroEdicao().toString());
			
			lancamentoDiferenca.setPrecoVenda(CurrencyUtil.formatarValor(produtoEdicao.getPrecoVenda()));
			
			lancamentoDiferenca.setPacotePadrao(String.valueOf(produtoEdicao.getPacotePadrao()));
			lancamentoDiferenca.setQuantidade(diferenca.getQtde().toString());
			lancamentoDiferenca.setTipoDiferenca(diferenca.getTipoDiferenca().getDescricao());
			
			BigDecimal valorDiferencas = 
				produtoEdicao.getPrecoVenda().multiply(diferenca.getQtde());
			
			lancamentoDiferenca.setValorTotalDiferenca(CurrencyUtil.formatarValor(valorDiferencas));
			
			listaLancamentosDiferenca.add(lancamentoDiferenca);
			
			qtdeTotalDiferencas = 
				qtdeTotalDiferencas.add(diferenca.getQtde());
			
			valorTotalDiferencas = valorTotalDiferencas.add(valorDiferencas);
		}
		
		TableModel<CellModelKeyValue<LancamentoDiferencaVO>> tableModel =
			new TableModel<CellModelKeyValue<LancamentoDiferencaVO>>();
		
		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listaLancamentosDiferenca));
		
		Long qtdeTotalRegistros = 
			this.diferencaEstoqueService.obterTotalDiferencasLancamento(filtro);
		
		tableModel.setTotal(qtdeTotalRegistros.intValue());
		
		tableModel.setPage(filtro.getPaginacao().getPaginaAtual());
		
		String valorTotalDiferencasFormatado = 
			CurrencyUtil.formatarValor(valorTotalDiferencas, this.localization);
		
		ResultadoLancamentoDiferencaVO resultadoLancamentoDiferenca = 
			new ResultadoLancamentoDiferencaVO(tableModel, qtdeTotalDiferencas, valorTotalDiferencasFormatado);
	
		result.use(Results.json()).withoutRoot().from(resultadoLancamentoDiferenca).recursive().serialize();
	}
	
	/*
	 * Processa o resulta das diferenças para lançamento.
	 *  
	 * @param listaDiferencas - lista de diferenças (DTO)
	 */
	private void processarDiferencasLancamentoMock(int page) {

		List<LancamentoDiferencaVO> listaLancamentosDiferenca = new LinkedList<LancamentoDiferencaVO>();
		
		BigDecimal qtdeTotalDiferencas = BigDecimal.ZERO;
		BigDecimal valorTotalDiferencas = BigDecimal.ZERO;
		
		int quantidadeRegistros = 30;
		
		for (int i = 0; i < quantidadeRegistros; i++) {
			
			LancamentoDiferencaVO lancamentoDiferenca = new LancamentoDiferencaVO();
			
			lancamentoDiferenca.setId(i);
			lancamentoDiferenca.setCodigoProduto(i + "");
			lancamentoDiferenca.setDescricaoProduto("Descrição Produto " + i);
			lancamentoDiferenca.setNumeroEdicao(i + "");
			
			lancamentoDiferenca.setPrecoVenda(CurrencyUtil.formatarValor(i + 100));
			
			lancamentoDiferenca.setPacotePadrao(i + "");
			lancamentoDiferenca.setQuantidade(i + "");
			lancamentoDiferenca.setTipoDiferenca(TipoDiferenca.FALTA_DE.getDescricao());
			
			BigDecimal valorDiferencas = new BigDecimal(i + 100).multiply(new BigDecimal(i));
			
			lancamentoDiferenca.setValorTotalDiferenca(CurrencyUtil.formatarValor(valorDiferencas));
			
			listaLancamentosDiferenca.add(lancamentoDiferenca);
			
			qtdeTotalDiferencas = 
				qtdeTotalDiferencas.add(new BigDecimal(i));
			
			valorTotalDiferencas = valorTotalDiferencas.add(valorDiferencas);
		}
		
		TableModel<CellModelKeyValue<LancamentoDiferencaVO>> tableModel =
			new TableModel<CellModelKeyValue<LancamentoDiferencaVO>>();
		
		tableModel.setRows(CellModelKeyValue.toCellModelKeyValue(listaLancamentosDiferenca));
		
		tableModel.setTotal(quantidadeRegistros);
		
		tableModel.setPage(page);
		
		String valorTotalDiferencasFormatado = 
			CurrencyUtil.formatarValor(valorTotalDiferencas, this.localization);
		
		ResultadoLancamentoDiferencaVO resultadoLancamentoDiferenca = 
			new ResultadoLancamentoDiferencaVO(tableModel, qtdeTotalDiferencas, valorTotalDiferencasFormatado);
	
		result.use(Results.json()).withoutRoot().from(resultadoLancamentoDiferenca).recursive().serialize();
	}
	
	/*
	 * Configura a paginação do filtro de pesquisa de lançamentos.
	 * 
	 * @param filtro - filtro da pesquisa
	 * @param sortorder - ordenação
	 * @param sortname - coluna para ordenação
	 * @param page - página atual
	 * @param rp - quantidade de registros para exibição
	 */
	private void configurarPaginacaoPesquisaLancamentos(FiltroLancamentoDiferencaEstoqueDTO filtro, 
														String sortorder,
														String sortname, 
														int page, 
														int rp) {
		
		if (filtro != null) {
			
			PaginacaoVO paginacao = new PaginacaoVO(page, rp, sortorder);
	
			filtro.setPaginacao(paginacao);
			
			filtro.setOrdenacaoColuna(Util.getEnumByStringValue(OrdenacaoColuna.values(), sortname));
		}
	}
	
}
