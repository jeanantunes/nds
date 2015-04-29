package br.com.abril.nds.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import br.com.abril.nds.client.vo.CobrancaDividaVO;
import br.com.abril.nds.client.vo.CobrancaVO;
import br.com.abril.nds.client.vo.DetalhesDividaVO;
import br.com.abril.nds.dto.PagamentoDividasDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaDividasCotaDTO;
import br.com.abril.nds.exception.ValidacaoException;
import br.com.abril.nds.model.cadastro.Banco;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.FormaCobranca;
import br.com.abril.nds.model.cadastro.TipoCobranca;
import br.com.abril.nds.model.financeiro.Cobranca;

public interface CobrancaService {
    

	/**
	 * Obtém o valor do juros, respeitando a ordem dos parâmetros informados,
	 * ou seja, se existir taxa de juros no banco informado, utiliza essa taxa.
	 * Senão utilizada da cota caso exista ou do distribuídor.
	 * 
	 * @param banco - banco
	 * @param idCota - cota
	 * @param valor - valor a ser calculado
	 * @param dataVencimento - data de vencimento
	 * @param dataCalculoJuros - data a ser calculado o juros
	 * @param formaCobrancaPrincipal - forma de cobrança principal
	 * 
	 * @return valor calculado com o juros 
	 */	
	public BigDecimal calcularJuros(Banco banco, Long idCota,
									BigDecimal valor, Date dataVencimento, Date dataCalculoJuros,
									FormaCobranca formaCobrancaPrincipal);
	
	/**
	 * Obtém o valor da multa, respeitando a ordem dos parâmetros informados,
	 * ou seja, se existir taxa ou valor de multa no banco informado, utiliza essa informação.
	 * Senão utilizada da cota caso exista ou do distribuidor.
	 * 
	 * @param banco - banco
	 * @param cota - cota
	 * @param valor - valor a ser calculado
	 * @return valor calculado com a multa
	 * @param formaCobrancaPrincipal - forma de cobrança principal
	 */
	public BigDecimal calcularMulta(Banco banco, Cota cota,
									BigDecimal valor,
									FormaCobranca formaCobrancaPrincipal);

	
	/**
	 * Método responsável por obter cobranças por numero da cota e vencimento
	 * @param filtro
	 * @return Lista de cobrancas encontrados
	 */
	List<Cobranca> obterCobrancasPorCota(FiltroConsultaDividasCotaDTO filtro);

	
	/**
	 * Método responsável por obter quantidade cobranças por numero da cota e vencimento
	 * @param filtro
	 * @return int
	 */
	int obterQuantidadeCobrancasPorCota(FiltroConsultaDividasCotaDTO filtro);

	
	/**
	 * Método responsável por obter lista de dados de cobranças por numero da cota e vencimento
	 * @param filtro
	 * @return Lista de value objects com dados de cobrancas encontradas
	 */
	List<CobrancaVO> obterDadosCobrancasPorCota(FiltroConsultaDividasCotaDTO filtro);
	
	
	/**
	 * Método responsável por obter dados de cobrança por código
	 * @param idCobranca
	 * @param dataPagamento
	 * @return value object com dados da cobranca encontrada
	 */
	CobrancaVO obterDadosCobranca(Long idCobranca, Date dataPagamento);
	
	CobrancaVO obterDadosCobrancaBoletoAntecipado(String nossoNumero, Date dataPagamento, BigDecimal valor);
	
	/**
	 * Método responsável por obter dados somados de cobranças por códigos
	 * @param List<Long> idCobrancas
	 * @return Data Transfer object com dados somados das cobrancas encontradas e calculadas.
	 */
	CobrancaDividaVO obterDadosCobrancas(List<Long> idCobrancas, Date dataPagamento);
	
	
	/**
	 *Método responsável por validar negociação, verificando se as datas de vencimento das dividas estão de acordo com a configuração do Distribuidor
	 * @param idCobrancas
	 */
	boolean validaNegociacaoDividas(List<Long> idCobrancas);
	
	
	/**
	 *Método responsável por baixar dividas manualmente 
	 * @param pagamento
	 * @param idCobrancas
	 * @param manterPendente
	 */
	void baixaManualDividas(PagamentoDividasDTO pagamento, List<Long> idCobrancas, Boolean manterPendente);
	
	
	void reverterBaixaManualDividas(List<Long> idCobrancas);
	
	/**
	 * Obtém saldo financeiro da cota
	 * @param idCota
	 * @return
	 */
	BigDecimal obterSaldoCota(Integer numeroCota);
	
	
	/**
	 * Obtém detalhes da Cobranca(Dívida)
	 * 
	 * @param idCobranca
	 * @return
	 */
	List<DetalhesDividaVO> obterDetalhesDivida(Long idCobranca);
	
	
	/**
	 * Obtém saldo da Cobranca(Dívida)
	 * 
	 * @param idCobranca
	 * @return
	 */
	BigDecimal obterSaldoDivida(Long idCobranca);
	
	void confirmarBaixaManualDividas(List<Long> idsBaixaCobranca);

	List<TipoCobranca> obterTiposCobrancaCadastradas();
	
	/**
	 * Valida se a data de pagamento é maior que a data de emissao das cobrancas selecionadas
	 * 
	 * @param idCobrancas
	 * @param dataPagamento
	 */
	void validarDataPagamentoCobranca(List<Long> idCobrancas, Date dataPagamento) throws ValidacaoException;

	public abstract List<Cobranca> obterCobrancasEfetuadaNaDataOperacaoDistribuidor(
			Date dataOperacaoDistribuidor);

	public abstract void processarExportacaoCobranca(Date dataDistribuicaoDistribuidor);

	public abstract void processarCobrancaConsolidada(Date dataOperacaoDistribuidor);

	public abstract Cobranca obterCobrancaPorNossoNumeroConsolidado(String nossoNumeroConsolidado);

	public abstract Long qtdCobrancasConsolidadasBaixadas(Date dataOperacao);
	
}
