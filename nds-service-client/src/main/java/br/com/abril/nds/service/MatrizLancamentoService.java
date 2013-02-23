package br.com.abril.nds.service;

import java.util.Date;
import java.util.List;
import java.util.TreeMap;

import br.com.abril.nds.dto.BalanceamentoLancamentoDTO;
import br.com.abril.nds.dto.ProdutoLancamentoDTO;
import br.com.abril.nds.dto.filtro.FiltroLancamentoDTO;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.vo.ConfirmacaoVO;

public interface MatrizLancamentoService {

	/**
	 * Obtém a matriz de balanceamento de lançamento e todas as informações
	 * refentes ao balanceamento.
	 * 
	 * @param filtro
	 *            - filtro para realização do balanceamento
	 * 
	 * @return {@link BalanceamentoLancamentoDTO}
	 */
	BalanceamentoLancamentoDTO obterMatrizLancamento(FiltroLancamentoDTO filtro);

	/**
	 * Confirma as matriz de balanceamento de lançamento.
	 * 
	 * @param matrizLancamento
	 *            - matriz de balanceamento de lançamento
	 * @param datasConfirmadas
	 *            - datas para confirmação
	 * @param usuario
	 *            - usuário
	 * 
	 * @return {@link TreeMap<Date, List<ProdutoLancamentoDTO>>}
	 */
	TreeMap<Date, List<ProdutoLancamentoDTO>> confirmarMatrizLancamento(
			TreeMap<Date, List<ProdutoLancamentoDTO>> matrizLancamento,
			List<Date> datasConfirmadas, Usuario usuario);

	/**
	 * Trata os lançamentos quando já houver um mesmo produto com a mesma data
	 * de lançamento.
	 * 
	 * @param produtoLancamentoAdicionar
	 *            - produto lançamento que será adicionado na matriz de
	 *            balanceamento
	 * @param produtosLancamento
	 *            - lista de produtos de lançamento que já foram adicionados na
	 *            matriz de balanceamento
	 */
	void tratarAgrupamentoPorProdutoDataLcto(
			ProdutoLancamentoDTO produtoLancamentoAdicionar,
			List<ProdutoLancamentoDTO> produtosLancamento);
	
	/**
	 * Obtém as datas dos lançamento e se estão confirmadas ou não.
	 * 
	 * @param produtosLancamento - Produtos de lançamento
	 * 
	 * @return {@link ConfirmacaoVO}
	 */
	public List<ConfirmacaoVO> obterDatasConfirmacao(List<ProdutoLancamentoDTO> produtosLancamento);
	
	void voltarConfiguracaoInicial(Date dataLancamento, TreeMap<Date, List<ProdutoLancamentoDTO>> matrizLancamento);
	
	void verificaDataOperacao(Date data);
	
	boolean isProdutoBalanceavel(ProdutoLancamentoDTO produtoLancamento);

}
