package br.com.abril.nds.repository;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

import br.com.abril.nds.client.vo.ProdutoDistribuicaoVO;
import br.com.abril.nds.dto.CotaOperacaoDiferenciadaDTO;
import br.com.abril.nds.dto.InformeEncalheDTO;
import br.com.abril.nds.dto.LancamentoDTO;
import br.com.abril.nds.dto.ProdutoLancamentoCanceladoDTO;
import br.com.abril.nds.dto.ProdutoLancamentoDTO;
import br.com.abril.nds.dto.ProdutoRecolhimentoDTO;
import br.com.abril.nds.dto.ResumoPeriodoBalanceamentoDTO;
import br.com.abril.nds.dto.SumarioLancamentosDTO;
import br.com.abril.nds.dto.filtro.FiltroLancamentoDTO;
import br.com.abril.nds.model.cadastro.Fornecedor;
import br.com.abril.nds.model.cadastro.GrupoProduto;
import br.com.abril.nds.model.cadastro.ProdutoEdicao;
import br.com.abril.nds.model.estoque.Expedicao;
import br.com.abril.nds.model.estoque.MovimentoEstoqueCota;
import br.com.abril.nds.model.estoque.TipoMovimentoEstoque;
import br.com.abril.nds.model.planejamento.Estudo;
import br.com.abril.nds.model.planejamento.Lancamento;
import br.com.abril.nds.model.planejamento.StatusLancamento;
import br.com.abril.nds.model.planejamento.TipoChamadaEncalhe;
import br.com.abril.nds.util.Intervalo;
import br.com.abril.nds.vo.PaginacaoVO;
import br.com.abril.nds.vo.PaginacaoVO.Ordenacao;

public interface LancamentoRepository extends Repository<Lancamento, Long> {
	
	public List<Lancamento> obterRecolhimentosEmBalanceamentoRecolhimento(
			List<Date> datasConfirmadas);

	SumarioLancamentosDTO sumarioBalanceamentoMatrizLancamentos(Date data,
			List<Long> idsFornecedores);

	void atualizarLancamento(Long idLancamento, Date novaDataLancamentoPrevista);

	List<ResumoPeriodoBalanceamentoDTO> buscarResumosPeriodo(
			List<Date> periodoDistribuicao, List<Long> fornecedores,
			GrupoProduto grupoCromo);

	List<Lancamento> obterLancamentosNaoExpedidos(PaginacaoVO paginacaoVO,
			Date data, Long idFornecedor, Boolean estudo);

	Long obterTotalLancamentosNaoExpedidos(Date data, Long idFornecedor,
			Boolean estudo);

	Lancamento obterLancamentoPosteriorDataLancamento(Date dataPrevista, Long idProdutoEdicao);

	Lancamento obterLancamentoAnteriorDataLancamento(Date dataLancamento, Long idProdutoEdicao);
	
	Date obterDataRecolhimentoDistribuidor(String codigoProduto, Long numeroEdicao);

	Boolean isLancamentoParcial(Long idLancamento);
	
	/**
	 * Método que retorna o balanceamento do recolhimento referentes a um
	 * periodo e determinados fornecedores.
	 * 
	 * @param periodoRecolhimento
	 * 
	 * @param listaIdsFornecedores
	 * 
	 * @param grupoCromo
	 * 
	 * @return List<ProdutoRecolhimentoDTO>
	 */
	List<ProdutoRecolhimentoDTO> obterBalanceamentoRecolhimento(
			Intervalo<Date> periodoRecolhimento, List<Long> fornecedores,
			GrupoProduto grupoCromo);

	/**
	 * Método que retorna o balanceamento do recolhimento referentes a um
	 * periodo ordernados por editor e data de recolhimento do distribuidor.
	 * 
	 * @param periodoRecolhimento
	 * 
	 * @param listaIdsFornecedores
	 * 
	 * @param grupoCromo
	 * 
	 * @return List<ProdutoRecolhimentoDTO>
	 */
	List<ProdutoRecolhimentoDTO> obterBalanceamentoRecolhimentoPorEditorData(
			Intervalo<Date> periodoRecolhimento, List<Long> fornecedores,
			GrupoProduto grupoCromo);

	/**
	 * Método que retorna expectativas de encalhe baseadas nas datas do período
	 * informado.
	 * 
	 * @param periodoRecolhimento
	 * 
	 * @param fornecedores
	 * 
	 * @param grupoCromo
	 * 
	 * @return Map<Date, BigDecimal>
	 */
	TreeMap<Date, BigDecimal> obterExpectativasEncalhePorData(
			Intervalo<Date> periodoRecolhimento, List<Long> fornecedores,
			GrupoProduto grupoCromo);

	/**
	 * Obtém o último Lancamento de determinado ProdutoEdicao
	 * 
	 * @param idProdutoEdicao
	 *            - Id do ProdutoEdicao
	 * @param dataLimiteLancamento TODO
	 * @return Lancamento
	 */
	Lancamento obterUltimoLancamentoDaEdicao(Long idProdutoEdicao, Date dataLimiteLancamento);
	
	List<Lancamento> obterLancamentosDaEdicao(Long idProdutoEdicao);

	/**
	 * Obtém uma lista de lancamentos de acordo com o parâmetro informado
	 * 
	 * @param idsLancamento
	 *            - identificadores de lancamento
	 * 
	 * @return {@link List<Lancamento>}
	 */
	List<Lancamento> obterLancamentosPorIdOrdenados(Set<Long> idsLancamento);

	/**
	 * Obtem a quantidade de registros de lançamentos respeitantdo os
	 * paramentros.
	 * 
	 * @param idFornecedor
	 *            (Opcional) Identificador do {@link Fornecedor}
	 * @param dataInicioRecolhimento
	 *            Inicio do intervalo para recolhimento.
	 * @param dataFimRecolhimento
	 *            Fim do intervalo para recolhimento.
	 * @return
	 */
	public abstract Long quantidadeLancamentoInformeRecolhimento(
			Long idFornecedor, Calendar dataInicioRecolhimento,
			Calendar dataFimRecolhimento);

	/**
	 * Obtem Dados de informe encalhe dos lançamentos respeitando os parametros.
	 * 
	 * @param idFornecedor
	 *            (Opcional) Identificador do {@link Fornecedor}
	 * @param dataInicioRecolhimento
	 *            Inicio do intervalo para recolhimento.
	 * @param dataFimRecolhimento
	 *            Fim do intervalo para recolhimento.
	 * @param orderBy
	 *            (Opcional) nome do campo para compor a ordenação
	 * @param ordenacao
	 *            (Opcional) tipo da ordenação
	 * @param initialResult
	 *            resultado inicial
	 * @param maxResults
	 *            numero maximo de resultados
	 * @return
	 */
	public abstract List<InformeEncalheDTO> obterLancamentoInformeRecolhimento(
			Long idFornecedor, Calendar dataInicioRecolhimento,
			Calendar dataFimRecolhimento, String orderBy, Ordenacao ordenacao,
			Integer initialResult, Integer maxResults);

	/**
	 * 
	 * Obtém a ultima (mais atual) dataLancamentoDistribuidor de determinado
	 * produtoEdicao, sendo esta dataLancamentoDistribuidor anterior a
	 * dataOperacao passada como parâmetro. É feito inner join com
	 * lancamentoParcial.
	 * 
	 * @param idProdutoEdicao
	 * @param dataOperacao
	 * 
	 * @return Date
	 */
	public abstract Date obterDataUltimoLancamentoParcial(Long idProdutoEdicao,
			Date dataOperacao);

	/**
	 * Obtém a ultima (mais atual) dataLancamentoDistribuidor de determinado
	 * produtoEdicao, sendo esta dataLancamentoDistribuidor anterior a
	 * dataOperacao passada como parâmetro.
	 * 
	 * @param idProdutoEdicao
	 * @param dataOperacao
	 * 
	 * @return Date.
	 */
	public Date obterDataUltimoLancamento(Long idProdutoEdicao,
			Date dataOperacao);

	/**
	 * Obtem Dados de informe encalhe dos lançamentos respeitando os parametros.
	 * 
	 * @param idFornecedor
	 *            (Opcional) Identificador do {@link Fornecedor}
	 * @param dataInicioRecolhimento
	 *            Inicio do intervalo para recolhimento.
	 * @param dataFimRecolhimento
	 *            Fim do intervalo para recolhimento.
	 * @return
	 */
	public abstract List<InformeEncalheDTO> obterLancamentoInformeRecolhimento(
			Long idFornecedor, Calendar dataInicioRecolhimento,
			Calendar dataFimRecolhimento);

	/**
	 * Método que retorna os produtos do balanceamento do lançamento referentes
	 * a um periodo e determinados fornecedores.
	 * 
	 * @param periodoDistribuicao
	 *            - período de distribuição
	 * @param fornecedores
	 *            - fornecedores
	 * @param list 
	 * 
	 * @return lista de produtos do balanceamento do lançamento
	 */
	List<ProdutoLancamentoDTO> obterBalanceamentoLancamento(Date dataLancamento,Intervalo<Date> periodoDistribuicao, List<Long> fornecedores);

	/**
	 * Burca último balançeamento de lançamento realizado no dia
	 * 
	 * @param dataOperacao
	 * @return Date
	 */
	public Date buscarUltimoBalanceamentoLancamentoRealizadoDia(
			Date dataOperacao);

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
	public Date buscarUltimoBalanceamentoRecolhimentoRealizadoDia(
			Date dataOperacao);

	/**
	 * Busca último balanceamento de recolhimento realizado no sistema
	 * 
	 * @return Date
	 */
	public Date buscarDiaUltimoBalanceamentoRecolhimentoRealizado();

	/**
	 * Retorna um lançamento de produto onde as datas de lançamento e
	 * recolhimento previstas forem iguais aos parâmetros informados.
	 * 
	 * @param produtoEdicao
	 *            - produto edição
	 * @param dataLancamentoPrevista
	 *            - data a ser comparada com a data de lançamento prevista
	 * @param dataRecolhimentoPrevista
	 *            - data a ser comparada com a data de recolhimento prevista
	 * @return Lancamento
	 */
	public List<Lancamento> obterLancamentosProdutoEdicaoPorDataLancamentoOuDataRecolhimento(
			ProdutoEdicao produtoEdicao, Date dataLancamentoPrevista,
			Date dataRecolhimentoPrevista);

	Long obterQuantidadeLancamentos(StatusLancamento statusLancamento);

	BigDecimal obterConsignadoDia(StatusLancamento statusLancamento);

	Long obterLancamentoProdutoPorDataLancamentoDataLancamentoDistribuidor(
			ProdutoEdicao produtoEdicao, Date dataLancamentoPrevista,
			Date dataLancamentoDistribuidor);

	/**
	 * Retorna os produtos dos lançamentos cancelados referentes a um periodo e
	 * determinados fornecedores.
	 * 
	 * @param periodo
	 *            - periodo
	 * @param idFornecedores
	 *            - fornecedores dos produtos
	 * @return lista lancamentos cancelados
	 */
	public List<ProdutoLancamentoCanceladoDTO> obterLancamentosCanceladosPor(
			Intervalo<Date> periodo, List<Long> idFornecedores);

	/**
	 * Verifica se existe Matriz de Balanciamento co status Planejado ou
	 * Confimado.
	 * 
	 * @param data
	 *            - Dia de Verificação.
	 * @return - true se encontrar e false se não encontrar.
	 */
	public Boolean existeMatrizBalanceamentoConfirmado(Date data);

	/**
	 * Retorna um lançamento logo após o que foi especificado por parâmetro.
	 * 
	 * @param lancamentoAtual
	 *            - Lançamento atual.
	 * 
	 * @return {@link br.com.abril.nds.model.planejamento.Lancamento}
	 */
	Lancamento obterProximoLancamento(Lancamento lancamentoAtual);

	Date obterDataMinimaProdutoEdicao(Long idProdutoEdicao,
			String propertyLancamentoDistribuidor);

	Date obterDataMaximaProdutoEdicao(Long idProdutoEdicao,
			String propertyLancamentoDistribuidor);

	List<MovimentoEstoqueCota> buscarMovimentosEstoqueCotaParaFuro(
			Lancamento lancamento, TipoMovimentoEstoque tipoMovimentoFuroCota);
	
	Boolean existeLancamentoNaoBalanceado(Date dataLancamento);

	List<Lancamento> obterLancamentosDistribuidorPorPeriodo(Intervalo<Date> periodoDistribuicao);
	
	List<Lancamento> obterLancamentosPrevistosPorPeriodo(Intervalo<Date> periodoDistribuicao);
	
	List<Lancamento> obterLancamentosARecolherNaSemana(
			Intervalo<Date> periodoRecolhimento, List<Long> fornecedores);
	
	boolean existeCobrancaParaLancamento(Long idLancamento);

	List<Lancamento> obterLancamentosEdicao(Long idEdicao);

	LancamentoDTO obterLancamentoPorID(Long idLancamento);

	void alterarLancamento(Long idLancamento,Date date, StatusLancamento expedido,
			Expedicao expedicao);

	List<Long> obterIdsLancamentosNaoExpedidos(PaginacaoVO paginacaoVO, Date data, Long idFornecedor, Boolean isSaldoInsuficiente);
	
	BigInteger obterQtdLancamentoProdutoEdicaoCopiados(ProdutoDistribuicaoVO produtoDistribuicaoVO);
	
	Set<Date> obterDatasLancamentosExpedidos(Intervalo<Date> intervalo);
	
	List<LancamentoDTO> obterDatasStatusAgrupados(FiltroLancamentoDTO filtro,Intervalo<Date> intervalo);

	List<ProdutoLancamentoDTO> verificarDataConfirmada(Date dataLancamento);

	public List<Lancamento> obterLancamentoInStatus(Date dataLancamentoDistribuidor, List<StatusLancamento> status);
	
	void atualizarDataRecolhimentoDistribuidor(Date dataRecolhimento, Long... idLancamento);
	
	Boolean existeRecolhimentoBalanceado(Date dataRecolhimento);
	
	List<Lancamento> obterLancamentosBalanceadosPorDataRecolhimentoDistrib(Date dataRecolhimentoDistribuidor);
	
	List<Lancamento> obterLancamentosEmRecolhimentoVencidos(Date dataBase);
	
	List<Lancamento> obterLancamentosRecolhidosPorEdicoes(Set<Long> idsProdutoEdicao);
	
	List<CotaOperacaoDiferenciadaDTO> obterLancamentosEncalhesPorCota(Set<Long> idsCota, Set<Long> idsLancamento);

	boolean existeMatrizRecolhimentoConfirmado(Date dataChamadao);

	Lancamento obterUltimoLancamentoDaEdicaoParaCota(Long idProdutoEdicao,Long idCota, Date dataLimiteLancamento);
	
	Integer obterProximaSequenciaMatrizPorData(Date dataLancamento);
	
	Lancamento obterLancamentoParcialFinal(Long idProdutoEdicao);

	Lancamento obterPrimeiroLancamentoDaEdicao(Long idProdutoEdicao);

	Integer obterUltimoNumeroLancamento(Long idProdutoEdicao, Long idPeriodo);
	
	Date getMaiorDataLancamentoPrevisto(Long idProdutoEdicao);
	
	Date getMaiorDataLancamentoDistribuidor(Long idProdutoEdicao);

	Lancamento obterLancamentoPorItemRecebimento(Long idItem);

	List<Lancamento> obterLancamentosConfirmados(List<Date> datasConfirmadas);
	
	List<Lancamento> obterMatrizLancamentosConfirmados(List<Date> datasConfirmadas);
	
	List<Lancamento> obterMatrizLancamentosExpedidos(List<Date> datasConfirmadas);
	
	boolean existeMatrizLancamentosExpedidos(List<Date> datasConfirmadas);
	
	List<Lancamento> obterRecolhimentosConfirmados(List<Date> datasConfirmadas);
	
	boolean existeConferenciaEncalheParaLancamento(Long idLancamento);

	boolean existeConferenciaEncalheParaLancamento(Long id,
			TipoChamadaEncalhe matrizRecolhimento);
	
	public List<Lancamento> obterPorEstudo(Estudo estudo);

	Lancamento buscarPorIdSemEstudo(Long lancamentoId);

	public abstract BigInteger obterUltimoRepartePorProduto(Long codigoProduto);

	public Lancamento buscarPorDataLancamentoProdutoEdicao(Date dtLancamento, Long produtoEdicaoId );
	
	Lancamento obterLancamentoParcialChamadaEncalhe(Long idChamdaEncalhe);
	
	void desvincularEstudos(List<Long> idsEstudos);
	
	List<Object[]> buscarDiasMatrizLancamentoAbertos();
	
	boolean existeProdutoEdicaoParaDia(ProdutoLancamentoDTO produtoLancamentoDTO,Date novaData);
	
	LinkedList<Lancamento> obterLancamentosRedistribuicoes(List<ProdutoLancamentoDTO> lista);

	List<Lancamento> obterLancamentosEmRecolhimentoParaFechamento(Date dataBase);
	
	List<Lancamento> obterRedistribuicoes(Long idProdutoEdicao, Integer numeroPeriodo);
	
	Lancamento obterLancamentoOriginalDaRedistribuicao(Long idProdutoEdicao, Integer numeroPeriodo);
	
	List<Lancamento> obterLancamentosDoPeriodoParcial(Long idPeriodo);
	
	List<Date> obterDatasLancamentoValidas();
	
	List<Date> obterDatasRecolhimentoValidas();
	
	Lancamento obterParaAtualizar(Long id);
	
	Integer obterRepartePromocionalEdicao(final Long codigoProduto, final Long numeroEdicao );
	
	StatusLancamento obterStatusDoPrimeiroLancamentoDaEdicao(Long idProdutoEdicao);

	boolean existeLancamentoParaOsStatus(final Long idProdutoEdicao,final StatusLancamento ...statusLancamento );
	
	boolean existemLancamentosConfirmados(Date dataRecolhimento);

	boolean isRedistribuicao(String codigoProduto, Long numeroEdicao);
}
