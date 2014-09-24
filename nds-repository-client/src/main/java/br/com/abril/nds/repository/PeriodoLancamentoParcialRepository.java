package br.com.abril.nds.repository;

import java.util.Date;
import java.util.List;

import br.com.abril.nds.dto.ParcialVendaDTO;
import br.com.abril.nds.dto.PeriodoParcialDTO;
import br.com.abril.nds.dto.RedistribuicaoParcialDTO;
import br.com.abril.nds.dto.filtro.FiltroParciaisDTO;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.model.planejamento.PeriodoLancamentoParcial;

public interface PeriodoLancamentoParcialRepository extends
		Repository<PeriodoLancamentoParcial, Long> {

	/**
	 * Retorna DTOs de periodos parciais de acordo com o filtro
	 * 
	 * @param filtro
	 */
	List<PeriodoParcialDTO> obterPeriodosParciais(FiltroParciaisDTO filtro);

	/**
	 * Retorna COUNT total da pesquisa "obterPeriodosParciais"
	 * 
	 * @param filtro
	 * @return
	 */
	Integer totalObterPeriodosParciais(FiltroParciaisDTO filtro);

	/**
	 * Obtém Periodo por id do Lancamento
	 * 
	 * @param idLancamento
	 * @return
	 */
	PeriodoLancamentoParcial obterPeriodoPorIdLancamento(Long idLancamento);

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
	 * Obtém lançamento de periodo parcial anterior.
	 * 
	 * @param dataRecolhimento
	 * 
	 * @param idLancamento
	 * @return
	 */
	Lancamento obterLancamentoAnterior(Long idProdutoEdicao,
			Date dataLancamento);
	
	/**
	 * Retorna o primeiro período de um lançamento parcial de um produto edição. 
	 *
	 * @param idProdutoEdicao - identificador do produto edição
	 *
	 * @return LancamentoParcial
	 */
	PeriodoLancamentoParcial obterPrimeiroLancamentoParcial(Long idProdutoEdicao);
	
	/**
	 * Retorna o último período de um lançamento parcial de um produto edição. 
	 *
	 * @param idProdutoEdicao - identificador do produto edição
	 *
	 * @return LancamentoParcial
	 */
	PeriodoLancamentoParcial obterUltimoLancamentoParcial(Long idProdutoEdicao);
	

	/**
	 * Retorna lançamentos parciais após seu balanceamento.
	 * 
	 * @param idLancamentoParcial
	 * 
	 * @return Long
	 */
	Long obterQntPeriodosAposBalanceamentoRealizado(Long idLancamentoParcial);

	List<RedistribuicaoParcialDTO> obterRedistribuicoesParciais(Long idPeriodo);
	
	PeriodoLancamentoParcial obterPeriodoPorNumero(Integer numeroPeriodo,Long idLancamentoParcial);
	
	List<Lancamento> obterRedistribuicoesPosterioresAoLancamento(Long idPeriodo,Date dataLancamento);
	
	List<Lancamento> obterRedistribuicoesAnterioresAoLancamento(Long idPeriodo,Date dataLancamento);
	
	List<Lancamento> obterRedistribuicoes(Long idPeriodo);
	
	List<PeriodoLancamentoParcial> obterProximosPeriodos(Integer numeroPeriodo,Long idLancamentoParcial) ;
	
	List<Lancamento> obterLancamentosParciais(Long idLancamentoParcial);
	
	boolean isLancamentoConferenciaEncalheCotaPeriodoFinal(Long idProdutoEdicao,Long idCota,Date dataRecolhimento);
	
	/**
	 * Obtém o primeiro período antes da data de recolhimento especificada.
	 * 
	 * @param dataRecolhimento
	 * 
	 * @param idProdutoEdicao
	 * 
	 * @return PeriodoLancamentoParcial
	 */
	PeriodoLancamentoParcial obterPeriodoAnterior(Date dataRecolhimento, Long idProdutoEdicao);
	
	/**
	 * Obtém o primeiro período após a data de recolhimento especificada.
	 * 
	 * @param dataRecolhimento
	 * 
	 * @param idProdutoEdicao
	 * 
	 * @return PeriodoLancamentoParcial
	 */
	PeriodoLancamentoParcial obterPeriodoPosterior(Date dataRecolhimento, Long idProdutoEdicao);
	
	Lancamento obterPrimeiroLancamentoNaoJuramentado(Integer numeroPeriodo, Long idLancamentoParcial);
	
}

