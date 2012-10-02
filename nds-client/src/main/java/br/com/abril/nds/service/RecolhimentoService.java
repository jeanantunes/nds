package br.com.abril.nds.service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import br.com.abril.nds.dto.BalanceamentoRecolhimentoDTO;
import br.com.abril.nds.dto.ProdutoRecolhimentoDTO;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.util.TipoBalanceamentoRecolhimento;

/**
 * Interface que define serviços referentes ao recolhimento.
 * 
 * @author Discover Technology
 *
 */
public interface RecolhimentoService {
	
	/**
	 * Obtém a matriz de balanceamento de recolhimento.
	 * 
	 * @param numeroSemana - número da semana para balanceamento
	 * @param listaIdsFornecedores - lista de id's dos fornecedores
	 * @param tipoBalanceamentoRecolhimento - tipo de balanceamento de recolhimento
	 * @param forcarBalanceamento - indica se é necessário forçar o balanceamento da matriz
	 * @param dataBalanceamento - data de balanceamento
	 * 
	 * @return {@link BalanceamentoRecolhimentoDTO}
	 */
	BalanceamentoRecolhimentoDTO obterMatrizBalanceamento(Integer numeroSemana,
														  List<Long> listaIdsFornecedores,
														  TipoBalanceamentoRecolhimento tipoBalanceamentoRecolhimento,
														  boolean forcarBalanceamento,
														  Date dataBalanceamento);
	
	/**
	 * Salva o balanceamento da matriz de recolhimento.
	 * 
	 * @param matrizRecolhimento - matriz de recolhimento
	 * @param usuario - usuário
	 */
	void salvarBalanceamentoRecolhimento(Map<Date, List<ProdutoRecolhimentoDTO>> matrizRecolhimento,
										 Usuario idUsuario);

	/**
	 * Confirma o balanceamento da matriz de recolhimento.
	 * 
	 * @param matrizRecolhimento - matriz de recolhimento
	 * @param numeroSemana - número da semana
	 * @param datasConfirmadas - datas para confirmação
	 * @param usuario - usuário
	 * 
	 * @return matriz de recolhimento confirmada
	 */
	TreeMap<Date, List<ProdutoRecolhimentoDTO>> confirmarBalanceamentoRecolhimento(
											Map<Date, List<ProdutoRecolhimentoDTO>> matrizRecolhimento,
											Integer numeroSemana,
											List<Date> datasConfirmadas,
											Usuario usuario);
	
	/**
	 * Exclui um balanceamento da matriz de recolhimento.
	 * 
	 * @param idLancamento - identificador do lançamento
	 */
	void excluiBalanceamento(Long idLancamento);
	
}
