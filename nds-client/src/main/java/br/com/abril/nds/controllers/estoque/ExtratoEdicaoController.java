package br.com.abril.nds.controllers.estoque;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.abril.nds.client.vo.ValidacaoVO;
import br.com.abril.nds.controllers.exception.ValidacaoException;
import br.com.abril.nds.dto.ExtratoEdicaoDTO;
import br.com.abril.nds.dto.InfoGeralExtratoEdicaoDTO;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.service.ExtratoEdicaoService;
import br.com.abril.nds.util.CellModel;
import br.com.abril.nds.util.TableModel;
import br.com.abril.nds.util.TipoMensagem;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;

/**
 * Componente controller do extrato de edição.
 * 
 * @author michel.jader
 *
 */
@Resource
@Path("/estoque/extratoEdicao")
public class ExtratoEdicaoController {
	
	private Result result;
	
	private static final String GRID_RESULT = "gridResult";
	private static final String SALDO_TOTAL_EXTRATO_EDICAO = "saldoTotalExtratoEdicao";
	private static final String DESTACAR_SALDO_TOTAL_EXTRATO_EDICAO = "destacarSaldoTotalExtratoEdicao";
	
	
	
	@Autowired
	private ExtratoEdicaoService extratoEdicaoService;
	
	private HttpServletRequest request;
	
	public ExtratoEdicaoController(Result result, HttpServletRequest request) {
		this.result = result;
		this.request = request;
	}
	
	public void index(){
		
	}
	
	/**
	 * Obtem e serializa a razão social do fornecedor do produto.
	 * @param codigo
	 */
	public void obterFornecedorDeProduto(String codigo) {
		
		String resultado = "";
		
		if(codigo!=null && !codigo.trim().isEmpty()) {
		
			resultado = extratoEdicaoService.obterRazaoSocialFornecedorDeProduto(codigo);
			
			if(resultado == null) {
				resultado = "";
			}
			
		} 
		
		result.use(Results.json()).from(resultado, "result").serialize();
		
	}
	
	public void obterProdutoEdicao(String codigo, Long edicao) {
		
		String resultado = "";
		
		if(codigo!=null && !codigo.trim().isEmpty() && edicao != null) {
			
			ProdutoEdicao produtoEdicao = extratoEdicaoService.obterProdutoEdicao(codigo, edicao);
		
			if(produtoEdicao!=null) {
				
				resultado = (produtoEdicao.getPrecoVenda()!=null) ? produtoEdicao.getPrecoVenda().toString() : "0.00";
				
			}
			
		}
		
		result.use(Results.json()).from(resultado, "result").serialize();
		
	}
	
	/**
	 * Pesquisa o Extrato da Edição e o serializa.
	 * 
	 * @throws Exception
	 */
	public void pesquisaExtratoEdicao(String codigoProduto, Long numeroEdicao) throws ValidacaoException {
		
		TableModel<CellModel> tableModel = null;
		
		Map<String, Object> resultado = new HashMap<String, Object>();
		
		List<String> listaWarningMsg = validarParametrosPesquisa(codigoProduto, numeroEdicao);
		
		if(!listaWarningMsg.isEmpty()) {
			throw new ValidacaoException(new ValidacaoVO(TipoMensagem.WARNING, listaWarningMsg));
		}
		
		InfoGeralExtratoEdicaoDTO infoGeralExtratoEdicao = extratoEdicaoService.obterInfoGeralExtratoEdicao(codigoProduto, numeroEdicao);
		
		if(	infoGeralExtratoEdicao == null || 
			infoGeralExtratoEdicao.getListaExtratoEdicao()==null ||
			infoGeralExtratoEdicao.getListaExtratoEdicao().isEmpty()) {

			throw new ValidacaoException(TipoMensagem.WARNING, "Nenhum registro encontrado.");
			
		}
		
		tableModel = obterTableModelParaListaExtratoEdicao(infoGeralExtratoEdicao.getListaExtratoEdicao());
		
		String destacarValorSaldo = (infoGeralExtratoEdicao.getSaldoTotalExtratoEdicao().doubleValue() < 0.0D) ? "S" : "N";
		
		resultado.put(GRID_RESULT, tableModel);
		
		resultado.put(SALDO_TOTAL_EXTRATO_EDICAO, infoGeralExtratoEdicao.getSaldoTotalExtratoEdicao());
		
		resultado.put(DESTACAR_SALDO_TOTAL_EXTRATO_EDICAO, destacarValorSaldo);
		
		result.use(Results.json()).withoutRoot().from(resultado).recursive().serialize();
		
	}
	
	private List<String> validarParametrosPesquisa(String codigoProduto, Long numeroEdicao) {

		List<String> msgWarningValidacao = new LinkedList<String>();
		
		if(codigoProduto == null || codigoProduto.isEmpty() ) {
			msgWarningValidacao.add("O preenchimento do campo código é obrigatório!.");
		}
		
		if(numeroEdicao == null) {
			msgWarningValidacao.add("O preenchimento do campo edição é obrigatório!.");
		}
		
		return msgWarningValidacao;
		
	}

	private TableModel<CellModel> obterTableModelParaListaExtratoEdicao(List<ExtratoEdicaoDTO> listaExtratoEdicao) {
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
		
		TableModel<CellModel> tableModel = new TableModel<CellModel>();
		
		List<CellModel> listaModeloGenerico = new LinkedList<CellModel>();
		
		for(ExtratoEdicaoDTO extrato : listaExtratoEdicao) {
			
			String dataMovimento 		= sdf.format(extrato.getDataMovimento());
			String descTipoMovimento 	= extrato.getDescMovimento();
			String qtdEntrada 			= extrato.getQtdEdicaoEntrada().doubleValue() < 0.0D ? "-" : extrato.getQtdEdicaoEntrada().toString();
			String qtdSaida 			= extrato.getQtdEdicaoSaida().doubleValue() < 0.0D ? "-" : extrato.getQtdEdicaoSaida().toString();
			String qtdParcial 			= extrato.getQtdParcial().toString();
			String destacarValor		= (extrato.getQtdParcial().doubleValue() < 0.0D) ? "S" : "N";
			
			listaModeloGenerico.add(new CellModel(extrato.getIdMovimento().intValue(), dataMovimento, descTipoMovimento, qtdEntrada, qtdSaida, qtdParcial, destacarValor));
			
		}
		
		tableModel.setPage(1);
		tableModel.setTotal(listaModeloGenerico.size());
		tableModel.setRows(listaModeloGenerico);
		
		return tableModel;
		
	}
	
}
