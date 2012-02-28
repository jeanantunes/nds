package br.com.abril.nds.controllers.estoque;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.dto.DiferencaDTO;
import br.com.abril.nds.dto.ItemDTO;
import br.com.abril.nds.dto.filtro.FiltroLancamentoDiferencaEstoqueDTO;
import br.com.abril.nds.dto.filtro.FiltroLancamentoDiferencaEstoqueDTO.OrdenacaoColuna;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.estoque.TipoDiferenca;
import br.com.abril.nds.service.DiferencaEstoqueService;
import br.com.abril.nds.service.FornecedorService;
import br.com.abril.nds.util.CellModel;
import br.com.abril.nds.util.Constantes;
import br.com.abril.nds.util.CurrencyUtil;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.Util;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

@Resource
@Path("/estoque/diferenca")
public class DiferencaEstoqueController {

	private Result result;
	
	@Autowired
	private FornecedorService fornecedorService;
	
	@Autowired
	private DiferencaEstoqueService diferencaEstoqueService;

	public DiferencaEstoqueController(Result result) {
		
		this.result = result;
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

		List<DiferencaDTO> listaLancamentoDiferencas = 
			this.diferencaEstoqueService.obterDiferencasLancamento(filtro);
		
		if (listaLancamentoDiferencas == null || listaLancamentoDiferencas.isEmpty()) {
			
			result.use(Results.json()).from(
				new String[] {Constantes.TIPO_MSG_WARNING, "Nenhum registro encontrado."}, Constantes.PARAM_MSGS).serialize();
			
		} else {
			
			TableModel<CellModel> tableModel = getTableModelDiferencasLancamento(listaLancamentoDiferencas);
			
			result.use(Results.json()).withoutRoot().from(tableModel).recursive().serialize();
		}
	}

	@Get
	public void consulta() {
		this.carregarCombosConsulta();
	}
	
	@Post
	@Path("/pesquisarDiferencas")
	public void pesquisarDiferencas(String codigoProduto, Long numeroEdicao,
									Long idFornecedor, String dataLancamentoDe,
									String dataLancamentoAte, TipoDiferenca tipoDiferenca,
									String sortorder, String sortname, int page, int rp) {
		
		DiferencaDTO dto = new DiferencaDTO(null, null, null);
		
		List<DiferencaDTO> listaDiferencaDTO = new ArrayList<DiferencaDTO>();
		
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
		
		for (DiferencaDTO diferencaDTO : listaDiferencaDTO) {
			
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
	 * Obtém o table model de diferenças para lançamento.
	 *  
	 * @param listaDiferencas - lista de diferenças (DTO)
	 * 
	 * @return TableModel
	 */
	private TableModel<CellModel> getTableModelDiferencasLancamento(List<DiferencaDTO> listaDiferencas) {

		if (listaDiferencas == null) {
			
			return null;
		}
		
		List<CellModel> listaCellModels = new LinkedList<CellModel>();

		for (DiferencaDTO diferencaDTO : listaDiferencas) {

			String precoVenda = 
				CurrencyUtil.formatarValor(
					diferencaDTO.getProdutoEdicao().getPrecoVenda().doubleValue(), Constantes.CODIGO_MOEDA_BR);
			
			String vlrTotal = 
				diferencaDTO.getProdutoEdicao().getPrecoVenda().multiply(
					diferencaDTO.getMovimentoEstoque().getDiferenca().getQtde()).toString();
			
			CellModel cellModel = new CellModel(
				
				diferencaDTO.getMovimentoEstoque().getId().intValue(),
				diferencaDTO.getProdutoEdicao().getProduto().getCodigo().toString(),
				diferencaDTO.getProdutoEdicao().getProduto().getDescricao(),
				diferencaDTO.getProdutoEdicao().getNumeroEdicao().toString(),
				precoVenda,
				String.valueOf(diferencaDTO.getProdutoEdicao().getPacotePadrao()),
				diferencaDTO.getMovimentoEstoque().getDiferenca().getQtde().toString(),
				diferencaDTO.getMovimentoEstoque().getDiferenca().getTipoDiferenca().getDescricao(),
				vlrTotal,
				diferencaDTO.getMovimentoEstoque().getId().toString()
			);
			
			listaCellModels.add(cellModel);
		}
		
		TableModel<CellModel> tableModel = new TableModel<CellModel>();
		
		tableModel.setRows(listaCellModels);

		return tableModel;
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
