package br.com.abril.nds.repository;

import java.util.List;

import br.com.abril.nds.model.DiaSemana;
import br.com.abril.nds.model.cadastro.DistribuicaoFornecedor;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.OperacaoDistribuidor;

/**
 * Interface que define as regras de acesso a dados referentes a entidade
 * {@link br.com.abril.nds.model.cadastro.DistribuicaoFornecedor }  
 * 
 * @author InfoA2
 */
public interface DistribuicaoFornecedorRepository extends Repository<DistribuicaoFornecedor, Long>{

	List<Integer> obterDiasSemanaDistribuicao(String codigoProduto, Long idProdutoEdicao);
	
	boolean verificarDistribuicaoDiaSemana(String codigoProduto, Long idProdutoEdicao, DiaSemana diaSemana);
	
	boolean verificarRecolhimentoDiaSemana(String codigoProduto,Long idProdutoEdicao, DiaSemana diaSemana);
	
	/**
	 * Obtem uma lista de todos os dados de DistribuicaoFornecedor ordenados por Fornecedor
	 * @return
	 */
	public List<DistribuicaoFornecedor> obterTodosOrdenadoId();

	/**
	 * Exxlui todas as informações relacionadas ao fornecedor (dias de lançamento e recolhimento)
	 * @param fornecedor
	 */
	public void excluirDadosFornecedor(Fornecedor fornecedor);

	/**
	 * Atualiza os dados do fornecedor, setando os novos dias de lançamento e recolhimento (sobrescreve os dados anteriores)
	 * @param listaDistribuicaoFornecedor
	 */
	public void gravarAtualizarDadosFornecedor(List<DistribuicaoFornecedor> listaDistribuicaoFornecedor);

	/**
	 * Obtém os códigos referente aos dias da distribuição de acordo com o fornecedor e a operação.
	 * 
	 * @param idFornecedor
	 * @param operacaoDistribuidor
	 * 
	 * @return {@link List<Integer>}
	 * 
	 */
	List<Integer> obterCodigosDiaDistribuicaoFornecedor(Long idFornecedor, OperacaoDistribuidor operacaoDistribuidor);
}
