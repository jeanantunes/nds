package br.com.abril.nds.service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import br.com.abril.nds.dto.BalanceamentoRecolhimentoDTO;
import br.com.abril.nds.dto.CotaOperacaoDiferenciadaDTO;
import br.com.abril.nds.dto.ProdutoRecolhimentoDTO;
import br.com.abril.nds.model.seguranca.Usuario;
import br.com.abril.nds.util.Intervalo;
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
	 * @param anoNumeroSemana - número da semana para balanceamento
	 * @param listaIdsFornecedores - lista de id's dos fornecedores
	 * @param tipoBalanceamentoRecolhimento - tipo de balanceamento de recolhimento
	 * @param forcarBalanceamento - indica se é necessário forçar o balanceamento da matriz
	 * 
	 * @return {@link BalanceamentoRecolhimentoDTO}
	 */
	BalanceamentoRecolhimentoDTO obterMatrizBalanceamento(Integer anoNumeroSemana,
														  List<Long> listaIdsFornecedores,
														  TipoBalanceamentoRecolhimento tipoBalanceamentoRecolhimento,
														  boolean forcarBalanceamento);
	
	/**
	 * Salva o balanceamento da matriz de recolhimento.
	 * 
	 * @param usuario - usuário
	 * @param balanceamentoRecolhimentoDTO
	 */
	void salvarBalanceamentoRecolhimento(Usuario idUsuario,
										 BalanceamentoRecolhimentoDTO balanceamentoRecolhimentoDTO);

	/**
	 * Confirma o balanceamento da matriz de recolhimento.
	 * 
	 * @param matrizRecolhimento - matriz de recolhimento
	 * @param numeroSemana - número da semana
	 * @param datasConfirmadas - datas para confirmação
	 * @param usuario - usuário
	 * @param produtosRecolhimentoAgrupados
	 * 
	 * @return matriz de recolhimento confirmada
	 */
	TreeMap<Date, List<ProdutoRecolhimentoDTO>> confirmarBalanceamentoRecolhimento(
											Map<Date, List<ProdutoRecolhimentoDTO>> matrizRecolhimento,
											Integer numeroSemana,
											List<Date> datasConfirmadas,
											Usuario usuario,
											List<ProdutoRecolhimentoDTO> produtosRecolhimentoAgrupados);
	
	Intervalo<Date> getPeriodoRecolhimento(Integer anoNumeroSemana);

	/**
	 * Desfaz alterações de recolhimento sobre os lançamentos da semana desejada.
	 * 
	 * @param numeroSemana - Número da semana
	 * @param fornecedores
	 * @param usuario
	 */
	void voltarConfiguracaoOriginal(Integer numeroSemana, List<Long> fornecedores, Usuario usuario);
	
	void verificaDataOperacao(Date data);
	
	/**
	 * Processa os produtos que não foram confirmados em uma matriz já fechada, para proxima semana de recolhimento
	 * 
	 * @param produtos - produtos a serem processados 
	 * @param numeroSemana - número da semana 
	 * 
	 */
	void processarProdutosProximaSemanaRecolhimento(List<ProdutoRecolhimentoDTO> produtos, Integer numeroSemana);
	
	void montarMapasOperacaoDiferenciada(Map<Date, List<CotaOperacaoDiferenciadaDTO>> mapOperacaoDifAdicionar,
										 Map<Date, List<CotaOperacaoDiferenciadaDTO>> mapOperacaoDifRemover,
										 TreeMap<Date, List<ProdutoRecolhimentoDTO>> matrizRecolhimento,
										 List<CotaOperacaoDiferenciadaDTO> cotasOperacaoDiferenciada);
	
}
