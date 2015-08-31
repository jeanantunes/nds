package br.com.abril.nds.service;

import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import br.com.abril.nds.client.vo.ProdutoLancamentoVO;
import br.com.abril.nds.dto.ExpedicaoDTO;
import br.com.abril.nds.dto.InformeEncalheDTO;
import br.com.abril.nds.dto.LancamentoNaoExpedidoDTO;
import br.com.abril.nds.dto.ProdutoLancamentoDTO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.model.planejamento.PeriodoLancamentoParcial;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.abril.nds.vo.PaginacaoVO.Ordenacao;

public interface LancamentoService {

	        /**
     * Busca por Lançamentos com Recebimento físico ainda não expedidos
     * 
     * @param paginacaoVO - Objeto com informações pertinentes a paginação
     * @param data - Filtro - Data de Lançamento
     * @param idFornecedor - Filtro - Código do Fornecedor
     * @param estudo - Filtro - Booleando que define se filtrará apenas
     *            lançamentos com estudo gerado
     * @return
     */
	List<LancamentoNaoExpedidoDTO> obterLancamentosNaoExpedidos(
			PaginacaoVO paginacaoVO, Date data, Long idFornecedor, Boolean estudo);
	
	Long obterTotalLancamentosNaoExpedidos(Date data, Long idFornecedor, Boolean estudo);
	
	boolean isLancamentoParcial(Long idLancamento);
	
	
	        /**
     * Confirma expedição de lançamento
	         * @param expedicaoDTO TODO
     * 
     * @return String
     */
	String confirmarExpedicao(ExpedicaoDTO expedicaoDTO);
	
	Lancamento obterPorId(Long idLancamento);
	
	        /**
     * Obtem Dados de informe encalhe dos lançamentos respeitando os parametros.
     * 
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
			Calendar dataInicioRecolhimento, Calendar dataFimRecolhimento, String orderBy, Ordenacao ordenacao, Integer initialResult,
			Integer maxResults);
	    
    /**
     * Obtem a quantidade de registros de lançamentos respeitantdo os
     * paramentros.
     * 
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
	         * @param dataLimiteLancamento TODO
     * @return Lancamento
     */
	public Lancamento obterUltimoLancamentoDaEdicao(Long idProdutoEdicao, Date dataLimiteLancamento);
	
	public Lancamento obterPrimeiroLancamentoDaEdicao(Long idProdutoEdicao);
	
	        /**
     * Obtem Dados de informe encalhe dos lançamentos respeitando os parametros.
     * 
     * @param idFornecedor (Opcional) Identificador do {@link Fornecedor}
     * @param dataInicioRecolhimento Inicio do intervalo para recolhimento.
     * @param dataFimRecolhimento Fim do intervalo para recolhimento.
     * @return
     */
	public abstract List<InformeEncalheDTO> obterLancamentoInformeRecolhimento(
			Long idFornecedor, Calendar dataInicioRecolhimento,
			Calendar dataFimRecolhimento);

	        /**
     * Burca último balançeamento de lançamento realizado no dia
     * 
     * @param dataOperacao
     * @return Date
     */
	public Date buscarUltimoBalanceamentoLancamentoRealizadoDia(Date dataOperacao);

	        /**
     * Busca último balanceamento de lançamento realizado no sistema
     * 
     * @return Date
     */
	public Date buscarDiaUltimoBalanceamentoLancamentoRealizado();

	        /**
     * Busca último balanceamento de recolhimento realizado no dia
     * 
     * @param dataOperacao
     * @return Date
     */
	public Date buscarUltimoBalanceamentoRecolhimentoRealizadoDia(Date dataOperacao);

	        /**
     * Busca último balanceamento de recolhimento realizado no sistema
     * 
     * @return Date
     */
	public Date buscarDiaUltimoBalanceamentoRecolhimentoRealizado();
	
	        /**
     * Verifica se existe Matriz de Balanciamento co status Planejado ou
     * Confimado.
     * 
     * @param data - Dia de Verificação.
     * @return - true se encontrar e false se não encontrar.
     */
	public Boolean existeMatrizBalanceamentoConfirmado(Date data);

	public List<Lancamento> obterLancamentoDataDistribuidorInStatus(Date dataRecebimentoDistribuidor, List<StatusLancamento> status);
	
	public List<Lancamento> obterLancamentosEdicao(Long idEdicao);

	List<Long> obterIdsLancamentosNaoExpedidos(PaginacaoVO paginacaoVO,
			Date data, Long idFornecedor, Boolean isSaldoInsuficiente);

	boolean existeMatrizRecolhimentoConfirmado(Date dataChamadao);

	Lancamento obterUltimoLancamentoDaEdicaoParaCota(Long idProdutoEdicao,Long idCota, Date dataLimiteLancamento);
	
	Date getMaiorDataLancamento(Long idProdutoEdicao);
	
	Lancamento obterLancamentoNaMesmaSessao(Long id);
    
    /**
     * Remove o Lancamento restringido as regras:
     * <ul>
     * <li>Não pode ser o primeiro lançamento</li>
     * <li>O Lançamento não pode ser um Lançamento Parcial</li>
     * <li>O Status do Lançamento deve ser {@link StatusLancamento#PLANEJADO},
     * {@link StatusLancamento#CONFIRMADO},
     * {@link StatusLancamento#EM_BALANCEAMENTO} ou
     * {@link StatusLancamento#BALANCEADO}.</li>
     * </ul>
     * 
     * @param id Id do Lançamento.
     * @throws ValidacaoException Lançada caso o Lançamento não respeite as
     *             regras acima.
     */
    public abstract void removerLancamento(Long id);
	
    
    void atualizarReparteLancamento(Long idLancamento, BigInteger reparte, BigInteger repartePromocional);
    
    HashMap<String, Set<Date>> obterDiasMatrizLancamentoAbertos();

	boolean existeProdutoEdicaoParaDia(ProdutoLancamentoDTO produtoLancamentoDTO, Date novaData);

	Lancamento buscarPorId(Long id);
	
	void excluirLancamento(final ProdutoLancamentoVO produtoLancamento);
	
	LinkedList<Lancamento> obterLancamentosRedistribuicoes(List<ProdutoLancamentoDTO> lista);

	void atualizarRedistribuicoes(Lancamento lancamento, Date dataRecolhimento, boolean alterarStatusLancamento);
	
	void reajustarNumerosLancamento(PeriodoLancamentoParcial periodo);
	
	Date obterDataLancamentoValido(Date dataLancamento,Long idFornecedor);
	
	Integer obterRepartePromocionalEdicao(final Long codigoProduto, final Long numeroEdicao);
	
	StatusLancamento obterStatusDoPrimeiroLancamentoDaEdicao(Long idProdutoEdicao);

	public abstract boolean isRedistribuicao(String codigoProduto, Long numeroEdicao);
	
	List<Lancamento> obterLancamentosDaEdicao(Long idProdutoEdicao);
	
	void alterarStatus(Lancamento lancamento ,StatusLancamento status);

	
}
 