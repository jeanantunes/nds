package br.com.abril.nds.service;

import java.util.List;

import br.com.abril.nds.client.vo.RegistroDiaOperacaoFornecedorVO;

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
	
}
