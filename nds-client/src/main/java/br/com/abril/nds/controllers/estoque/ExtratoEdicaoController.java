package br.com.abril.nds.controllers.estoque;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.controllers.lancamento.FuroProdutoController;
import br.com.abril.nds.dto.ExtratoEdicaoDTO;
import br.com.abril.nds.dto.FuroProdutoDTO;
import br.com.abril.nds.dto.InfoGeralExtratoEdicaoDTO;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.Produto;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.service.ExtratoEdicaoService;
import br.com.abril.nds.service.ProdutoService;
import br.com.abril.nds.util.CellModel;
import br.com.abril.nds.util.Constantes;
import br.com.abril.nds.util.ItemAutoComplete;
import br.com.abril.nds.util.TableModel;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

@Resource
@Path("/estoque/extratoEdicao")
public class ExtratoEdicaoController {
	
	private Result result;
	
	@Autowired
	private ProdutoService produtoService;
	
	@Autowired
	private ExtratoEdicaoService extratoEdicaoService;
	
	public ExtratoEdicaoController(Result result) {
		this.result = result;
	}
	
	public void index(){
		
	}
	
	@Post
	public void pesquisarPorNomeProduto(String nomeProduto){
		
		List<Produto> listaProdutoEdicao = null;
		try {
			listaProdutoEdicao = this.produtoService.obterProdutoPorNomeProduto(nomeProduto);
		} catch (Exception e) {
			result.use(Results.json()).from(new String[]{Constantes.TIPO_MSG_ERROR, 
					"Erro ao pesquisar produto: " + e.getMessage()}, Constantes.PARAM_MSGS).serialize();
			result.forwardTo(FuroProdutoController.class).index();
			return;
		}
		
		if (listaProdutoEdicao != null){
			List<ItemAutoComplete> listaProdutos = new ArrayList<ItemAutoComplete>();
			for (Produto produto : listaProdutoEdicao){
				listaProdutos.add(
						new ItemAutoComplete(
								produto.getNome(), 
								null,
								new FuroProdutoDTO(
										produto.getCodigo())));
			}
			
			result.use(Results.json()).from(listaProdutos, "result").include("value", "chave").serialize();
		}
		
		result.forwardTo(FuroProdutoController.class).index();
	}
	
	public void obterFornecedorDeProduto(String codigoProduto){
		
		Fornecedor fornecedor = extratoEdicaoService.obterFornecedorDeProduto(codigoProduto);
		
	}
	
	public void obterProdutoEdicao(String codigoProduto, Long numeroEdicao) {
		
		ProdutoEdicao produtoEdicao = extratoEdicaoService.obterProdutoEdicao(codigoProduto, numeroEdicao);
		
	}
	
	/**
	 * 
	 * @throws Exception
	 */
	public void pesquisaExtratoEdicao(Long codigoProduto, String descProduto, Long idProdutoEdicao) throws Exception {
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
		
		InfoGeralExtratoEdicaoDTO infoGeralExtratoEdicao = extratoEdicaoService.obterInfoGeralExtratoEdicao(1L);
		
		if(	infoGeralExtratoEdicao == null || 
			infoGeralExtratoEdicao.getListaExtratoEdicao()==null ||
			infoGeralExtratoEdicao.getListaExtratoEdicao().isEmpty()) {
			return;
		}
		
		List<ExtratoEdicaoDTO> listaExtratoEdicao = infoGeralExtratoEdicao.getListaExtratoEdicao();
		
		List<CellModel> listaModeloGenerico = new LinkedList<CellModel>();
		
		for(ExtratoEdicaoDTO extrato : listaExtratoEdicao) {
			
			String dataMovimento 		= sdf.format(extrato.getDataMovimento());
			
			String descTipoMovimento 	= extrato.getDescMovimento();
			
			String qtdEntrada 			= extrato.getQtdEdicaoEntrada().doubleValue() < 0.0D ? "-" : extrato.getQtdEdicaoEntrada().toString();
			
			String qtdSaida 			= extrato.getQtdEdicaoSaida().doubleValue() < 0.0D ? "-" : extrato.getQtdEdicaoSaida().toString();
			
			String qtdParcial 			= extrato.getQtdParcial().doubleValue() < 0.0D ? "-" : extrato.getQtdParcial().toString();
			
			listaModeloGenerico.add(new CellModel(extrato.getIdMovimento().intValue(), dataMovimento, descTipoMovimento, qtdEntrada, qtdSaida, qtdParcial));
			
		}
		
		TableModel<CellModel> tm = new TableModel<CellModel>();
		
		tm.setPage(1);
		
		tm.setTotal(listaModeloGenerico.size());
		
		tm.setRows(listaModeloGenerico);

		Map resultado = new HashMap();
		
		resultado.put("TblModelListaExtratoEdicao", tm);
		
		resultado.put("saldoTotalExtratoEdicao", infoGeralExtratoEdicao.getSaldoTotalExtratoEdicao());
		
		result.use(Results.json()).withoutRoot().from(resultado).recursive().serialize();
		
	}
	
}
