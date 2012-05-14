package br.com.abril.nds.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import br.com.abril.nds.dto.BalanceamentoRecolhimentoDTO;
import br.com.abril.nds.dto.ProdutoRecolhimentoDTO;
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
	 * 
	 * @return {@link BalanceamentoRecolhimentoDTO}
	 */
	BalanceamentoRecolhimentoDTO obterMatrizBalanceamento(Integer numeroSemana,
														  List<Long> listaIdsFornecedores,
														  TipoBalanceamentoRecolhimento tipoBalanceamentoRecolhimento);
	
	/**
	 * Salva o balanceamento da matriz de recolhimento.
	 * 
	 * @param matrizRecolhimento - matriz de recolhimento
	 */
	void salvarBalanceamentoRecolhimento(Map<Date, List<ProdutoRecolhimentoDTO>> matrizRecolhimento);

	/**
	 * Confirma o balanceamento da matriz de recolhimento.
	 * 
	 * @param matrizRecolhimento - matriz de recolhimento
	 */
	void confirmarBalanceamentoRecolhimento(Map<Date, List<ProdutoRecolhimentoDTO>> matrizRecolhimento);
}
