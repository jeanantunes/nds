package br.com.abril.nds.service;

import java.util.List;

import br.com.abril.nds.client.vo.RegistroDiaOperacaoFornecedorVO;
import br.com.abril.nds.model.cadastro.DistribuicaoFornecedor;
import br.com.abril.nds.model.cadastro.Distribuidor;
import br.com.abril.nds.model.cadastro.OperacaoDistribuidor;

/**
 * Interface que define as regras de acesso a serviços referentes a entidade
 * {@link br.com.abril.nds.model.cadastro.DistribuicaoFornecedor}  
 * @author InfoA2
 */
public interface DistribuicaoFornecedorService {

	/**
	 * Retorna uma lista de DistribuicaoFornecedor
	 * @return List<DistribuicaoFornecedor>
	 */
	public List<RegistroDiaOperacaoFornecedorVO> buscarDiasOperacaoFornecedor();

	/**
	 * Exclui todos os dados de distribuição relacionado ao fornecedor
	 * @param codigoFornecedor
	 */
	public void excluirDadosFornecedor(long codigoFornecedor);

	/**
	 * Atualiza os dados de dias de lanãmento e dias de recolhimento do fornecedor
	 * @param selectFornecedoresLancamento
	 * @param selectDiasLancamento
	 * @param selectDiasRecolhimento
	 */
	public void gravarAtualizarDadosFornecedor(List<String> listaCodigoFornecedoresLancamento, List<String> listaDiasLancamento, List<String> listaDiasRecolhimento, Distribuidor distribuidor);

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
