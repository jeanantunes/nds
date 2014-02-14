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
			Date dataRecolhimento, Long idProdutoEdicao);
	
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
	
	void excluirRedistribuicaoParcial(Long idLancamentoRedistribuicao);
	
	List<Lancamento> obterRedistribuicoes(Long idPeriodo);
	
	void reajustarRedistribuicoes(PeriodoLancamentoParcial periodo,Date dataLancamento, Date dataRecolhimento);
	
	void atualizarReparteDoProximoLancamentoPeriodo(Lancamento lancamento, Usuario usuario, BigInteger reparte);
	
	Lancamento getProximoLancamentoPeriodo(Lancamento lancamento);
	
}
