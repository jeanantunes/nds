package br.com.abril.nds.service;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import br.com.abril.nds.dto.ParcialVendaDTO;
import br.com.abril.nds.dto.RedistribuicaoParcialDTO;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.model.planejamento.PeriodoLancamentoParcial;
import br.com.abril.nds.model.seguranca.Usuario;

/**
 * Interface que define os serviços referentes a Parciais
 * 
 * @author guilherme.morais
 * 
 */
public interface ParciaisService {

	/**
	 * Cria um novo período parcial para determinada edição de um produto, 
	 * a partir de uma data de recolhimento definida pelo usuário. 
	 * 
	 * @param idProdutoEdicao
	 *            - identificador do produto edição
	 * @param qtdePeriodos
	 *            - Quantidade de períodos
	 * @param usuario
	 *            - Usuário
	 */
	void inserirNovoPeriodo(Long idProdutoEdicao, Date dataRecolhimento, Usuario usuario);
	
	/**
	 * Cria novos período parcial para determinada edição de um produto
	 * 
	 * @param idProdutoEdicao
	 *            - identificador do produto edição
	 * @param qtdePeriodos
	 *            - Quantidade de períodos
	 * @param usuario
	 *            - Usuário
	 */
	void gerarPeriodosParcias(Long idProdutoEdicao, Integer qtdePeriodos,Usuario usuario);

	/**
	 * Altera data de Período de Lancamento Parcial
	 * 
	 * @param idLancamento
	 * @param dataLancamento
	 * @param dataRecolhimento
	 * @param usuario
	 */
	void alterarPeriodo(Long idLancamento, Date dataLancamento,
			Date dataRecolhimento, Usuario usuario);

	/**
	 * Remove PeriodoLancamentoParcial e Lancamento referente ao mesmo
	 * 
	 * @param idLancamento
	 */
	void excluirPeriodo(Long idLancamento);

	/**
	 * Obtem detalhes das vendas do produtoEdição nas datas de Lancamento e
	 * Recolhimento
	 * 
	 * @param dataLancamento
	 * @param dataRecolhimento
	 * @param idProdutoEdicao
	 * @return List<ParcialVendaDTO>
	 */
	List<ParcialVendaDTO> obterDetalhesVenda(Date dataLancamento,
			Date dataRecolhimento, Long idProdutoEdicao, Long idPeriodo);
	
	/**
	 * Retorna a peb calculada de uma parcial referente a um produto edição
	 * 
	 * @param codigoProduto
	 * @param edicaoProduto
	 * @param qtdePeriodos
	 * @return Integer
	 */
	Integer calcularPebParcial(String codigoProduto, Long edicaoProduto, Integer qtdePeriodos);

	Date obterDataUtilMaisProxima(Date data);
	
	List<RedistribuicaoParcialDTO> obterRedistribuicoesParciais(Long idPeriodo);
	
	void incluirRedistribuicaoParcial(RedistribuicaoParcialDTO redistribuicaoParcialDTO);
	
	void salvarRedistribuicaoParcial(RedistribuicaoParcialDTO redistribuicaoParcialDTO);
	
	void excluirLancamentoParcial(Long idLancamentoRedistribuicao);
	
	List<Lancamento> obterRedistribuicoes(Long idPeriodo);
	
	void reajustarRedistribuicoes(PeriodoLancamentoParcial periodo,Date dataLancamento, Date dataRecolhimento);
	
	void atualizarReparteDoProximoLancamentoPeriodo(Lancamento lancamento, Usuario usuario, BigInteger reparte);
	
	Lancamento getProximoLancamentoPeriodo(Lancamento lancamento);
	
	/**
	 * Obtém a próxima data a partir do parametro <code> Fator de Relançamento </code> do Distribuidor.
	 * 
	 * @param dataRecolhimento - Data a ser utilizada no cálculo.
	 * 
	 * @return Data calculada.
	 */
	Date obterProximaDataComFatorRelancamentoParcialDistribuidor(Date dataRecolhimento);

	/**
	 * Altera o recolhimento de um lançamento parcial e toda a lógica necessária para manter a integridade
	 * entre os períodos e redistribuições já existentes.
	 * 
	 * @param lancamento - Lançamento a ser alterado.
	 * 
	 * @param novaData - Nova data de recolhimento.
	 * 
	 * @return {@link Lancamento} com as devidas alterações.
	 * 
	 */
	Lancamento alterarRecolhimento(Lancamento lancamento, Date novaData);
	
}
