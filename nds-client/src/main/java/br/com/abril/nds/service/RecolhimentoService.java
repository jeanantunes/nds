package br.com.abril.nds.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

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
	 * 
	 * @return {@link BalanceamentoRecolhimentoDTO}
	 */
	BalanceamentoRecolhimentoDTO obterMatrizBalanceamento(Integer numeroSemana,
														  List<Long> listaIdsFornecedores,
														  TipoBalanceamentoRecolhimento tipoBalanceamentoRecolhimento,
														  boolean forcarBalanceamento);
	
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
	 * @param usuario - usuário
	 */
	void confirmarBalanceamentoRecolhimento(Map<Date, List<ProdutoRecolhimentoDTO>> matrizRecolhimento,
											Integer numeroSemana, Usuario usuario);
}
