package br.com.abril.nds.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import br.com.abril.nds.dto.InformeEncalheDTO;
import br.com.abril.nds.dto.LancamentoNaoExpedidoDTO;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.abril.nds.vo.PaginacaoVO.Ordenacao;

public interface LancamentoService {

	/**
	 * Busca por Lançamentos com Recebimento físico ainda não expedidos
	 * 
	 * @param paginacaoVO - Objeto com informações pertinentes a paginação
	 * @param data - Filtro - Data de Lançamento
	 * @param idFornecedor - Filtro - Código do Fornecedor
	 * @param estudo - Filtro - Booleando que define se filtrará apenas lançamentos com estudo gerado
	 * @return
	 */
	List<LancamentoNaoExpedidoDTO> obterLancamentosNaoExpedidos(
			PaginacaoVO paginacaoVO, Date data, Long idFornecedor, Boolean estudo);
	
	Long obterTotalLancamentosNaoExpedidos(Date data, Long idFornecedor, Boolean estudo);
	
	/**
	 * Confirma expedição de lançamento
	 * 
	 * @param idLancamento - Código do lançamento
	 * @param idUsuario - Código do usuario
	 */
	void confirmarExpedicao(Long idLancamento, Long idUsuario);
	
	void confirmarExpedicoes(List<Long> idLancamentos, Long idUsuario);
	
	Lancamento obterPorId(Long idLancamento);
	
	/**
	 * Obtem Dados de informe encalhe dos lançamentos respeitando os parametros.
	 * @param idFornecedor (Opcional) Identificador do {@link Fornecedor}
	 * @param dataInicioRecolhimento Inicio do intervalo para recolhimento.
	 * @param dataFimRecolhimento Fim do intervalo para recolhimento.
	 * @param orderBy (Opcional) nome do campo para compor a ordenação
	 * @param ordenacao (Opcional) tipo da ordenação
	 * @param initialResult resultado inicial
	 * @param maxResults numero maximo de resultados
	 * @return
	 */
	public abstract List<InformeEncalheDTO> obterLancamentoInformeRecolhimento(Long idFornecedor,
			Calendar dataInicioRecolhimento, Calendar dataFimRecolhimento, String orderBy, Ordenacao ordenacao, int initialResult,
			int maxResults);
	/**
	 * Obtem a quantidade de registros de lançamentos respeitantdo os paramentros.
	 * @param idFornecedor (Opcional) Identificador do {@link Fornecedor}
	 * @param dataInicioRecolhimento Inicio do intervalo para recolhimento.
	 * @param dataFimRecolhimento Fim do intervalo para recolhimento.
	 * @return
	 */
	public abstract Long quantidadeLancamentoInformeRecolhimento(
			Long idFornecedor, Calendar dataInicioRecolhimento, Calendar dataFimRecolhimento);
	
	 /**
	  * Obtém o último Lancamento de determinado ProdutoEdicao
	  * 
	  * @param idProdutoEdicao - Id do ProdutoEdicao
	  * @return Lancamento
	  */
	public Lancamento obterUltimoLancamentoDaEdicao(Long idProdutoEdicao);
}
 