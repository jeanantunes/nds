package br.com.abril.nds.service;

import java.util.Date;
import java.util.List;

import br.com.abril.nds.dto.ParcialVendaDTO;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.model.seguranca.Usuario;

/**
 * Interface que define os serviços referentes a Parciais
 * 
 * @author guilherme.morais
 * 
 */
public interface ParciaisService {

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
	 * Cria novos período parcial para determinada edição de um produto
	 * 
	 * @param produtoEdicao
	 *            - Produto edição
	 * @param qtdePeriodos
	 *            - Quantidade de períodos
	 * @param usuario
	 *            - Usuário
	 *            
	 * @param distribuidor
	 *            - Distribuídor
	 */
	void gerarPeriodosParcias(ProdutoEdicao produtoEdicao,Integer qtdePeriodos, Usuario usuario);

	/**
	 * Altera data de Período de Lancamento Parcial
	 * 
	 * @param idLancamento
	 * @param dataLancamento
	 * @param dataRecolhimento
	 */
	void alterarPeriodo(Long idLancamento, Date dataLancamento,
			Date dataRecolhimento);

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
	 * Atualiza a quantidade de reparte do próximo lançamento parcial
	 * 
	 * @param lancamento
	 */
	void atualizarReparteDoProximoLancamentoParcial(Lancamento lancamento);

}
