package br.com.abril.nds.repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import br.com.abril.nds.client.vo.CobrancaVO;
import br.com.abril.nds.client.vo.NegociacaoDividaDetalheVO;
import br.com.abril.nds.dto.ExportarCobrancaDTO;
import br.com.abril.nds.dto.filtro.FiltroConsultaDividasCotaDTO;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.TipoCobranca;
import br.com.abril.nds.model.financeiro.Boleto;
import br.com.abril.nds.model.financeiro.Cobranca;
import br.com.abril.nds.model.movimentacao.DebitoCreditoCota;

public interface CobrancaRepository extends Repository<Cobranca, Long>{

	
	/**
	 * Obtem cobrancas em aberto que não estejam associadas a 
	 * operacao de encalhe em questão (caso flag seja true)
	 * 
	 * @param idCota
	 * @param idControleConfEncCota
	 * 
	 * @return List - DebitoCreditoCotaDTO
	 */
	List<DebitoCreditoCota> obterCobrancasDaCotaEmAbertoAssociacaoConferenciaEncalhe(Long idCota, Long idControleConfEncCota, Date data);
	
	/**
	 * Obtem data em que houve a primeira inadimplencia com cobrança ainda em aberto
	 * 
	 * @param idCota -  Código da Cota
	 * @return dia
	 */
	Date obterDataAberturaDividas(Long idCota);

	/**
	 * Obter cobraças não pagas da cota
	 * 
	 * @param idCota
	 * @return
	 */
	List<Cobranca> obterCobrancasDaCotaEmAberto(Long idCota, boolean obtemCobrancaOrigemNegociacao, Date data);
	
	/**
	 * Obtém a cobrança pelo nosso numero
	 * 
	 * @param nossoNumero
	 * @return Cobranca
	 */
	Cobranca obterCobrancaPorNossoNumero(String nossoNumero);
	
	/**
	 * Incrementa o valor de vias
	 * 
	 * @param nossoNumero
	 */
	void incrementarVia(String... nossoNumero);
	
	/**
	 * Método responsável por obter a quantidade de cobrancas
	 * @param filtro
	 * @return quantidade: quantidade de cobrancas
	 */
	long obterQuantidadeCobrancasPorCota(FiltroConsultaDividasCotaDTO filtro);
	
	/**
	 * Método responsável por obter uma lista de cobrancas
	 * @param filtro
	 * @return query.list(): lista de cobrancas
	 */
	public List<CobrancaVO> obterCobrancasPorCota(FiltroConsultaDividasCotaDTO filtro);


	void excluirCobrancaPorIdDivida(Long idDivida);

	
	/**
	 * Método responsável por obter uma lista de cobrancas ordenadas por data de vencimento
	 * @param List<Long>: Id's de cobranças
	 * @return query.list(): lista de cobrancas
	 */
	List<Cobranca> obterCobrancasOrdenadasPorVencimento(List<Long> idCobrancas);
	
	List<Cobranca> obterCobrancasPorIDS(List<Long> listaCobrancas);

	BigDecimal obterValorCobrancasQuitadasPorData(Date data);
	
	/**
	 * Retorna as cobranças efetuadas na data de operação do distribuidor.
	 * 
	 * @param dataOperacao - data operação do distribuidor
	 * 
	 * @return List<Cobranca>
	 */
	List<Cobranca> obterCobrancasEfetuadaNaDataOperacaoDistribuidor(Date dataOperacao);

	String obterNossoNumeroPorMovimentoFinanceiroCota(Long idMovimentoFinanceiro);

	List<NegociacaoDividaDetalheVO> obterDetalhesCobranca(Long idCobranca);

	void atualizarVias(Boleto boleto);

	List<TipoCobranca> obterTiposCobrancaCadastradas();

    /**
     * Obtem Cobrancas com a data de emissao maior que a data parametrizada
     * 
     * @param idCobrancas
     * @param dataPagamento
     * @return
     */
    List<Cobranca> obterCobrancasDataEmissaoMaiorQue(Date dataPagamento, List<Long> idCobrancas);

	public abstract List<ExportarCobrancaDTO> obterCobrancasNaDataDeOperacaoDoDistribuidor(Date dataOperacao);

	public abstract void updateNossoNumero(Date dataOperacao, Integer numeroCota,
			String nossoNumero);

	public abstract Cobranca obterCobrancaPorNossoNumeroConsolidado(String nossoNumero);

	public abstract Long qtdCobrancasConsolidadasBaixadas(Date dataOperacao);

	Cobranca obterCobrancaBoletoAvulso(Integer idCota);
}