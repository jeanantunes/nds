package br.com.abril.nds.repository;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import br.com.abril.nds.dto.GeraDividaDTO;
import br.com.abril.nds.dto.StatusDividaDTO;
import br.com.abril.nds.dto.fechamentodiario.SumarizacaoDividasDTO;
import br.com.abril.nds.dto.filtro.FiltroCotaInadimplenteDTO;
import br.com.abril.nds.dto.filtro.FiltroDividaGeradaDTO;
import br.com.abril.nds.model.financeiro.Cobranca;
import br.com.abril.nds.model.financeiro.ConsolidadoFinanceiroCota;
import br.com.abril.nds.model.financeiro.Divida;
import br.com.abril.nds.vo.PaginacaoVO;

public interface DividaRepository extends Repository<Divida, Long>{

	/**
	 * Exclui os registros da table de ligação divida_consolidado
	 * referente ao divida em questão.
	 *  
	 * @param idDivida
	 */
	void excluirDividaConsolidado(final Long idDivida);
	
	Divida obterDividaParaAcumuloPorCota(Long idCota);
	
	/**
	 * Retorna as dividas geradas conforme parametros informados no filtro.
	 * @param filtro
	 * @return List<GeraDividaDTO> 
	 */
	List<GeraDividaDTO> obterDividasGeradas(FiltroDividaGeradaDTO filtro);
	
	/**
	 *  Retorna as dividas geradas conforme parametros informados no filtro, sem dividas do tipo Boleto.
	 * @param filtro
	 * @return
	 */
	List<GeraDividaDTO> obterDividasGeradasSemBoleto(FiltroDividaGeradaDTO filtro);
	
	/**
	 * Retorna a quantidade de registros da consulta de dividas geradas, conforme filtro informado.
	 * @param filtro
	 * @return Long
	 */
	Long obterQuantidadeRegistroDividasGeradas(FiltroDividaGeradaDTO filtro);
	
	/**
	 * Retorna a quantidade de registros da consulta de dividas geradas.
	 * @param dataMovimento
	 * @return Long
	 */
	Long obterQunatidadeDividaGeradas(Date dataMovimento);
	
	/**
	 * Obter dívidas não pagas da cota
	 * 
	 * @param filtro - filtro de pesquisa
	 * @return
	 */
	List<StatusDividaDTO> obterInadimplenciasCota(FiltroCotaInadimplenteDTO filtro);
	

	/**
	 * Obtém quantidade de inadimplencias da cota (count de obterInadimplenciasCota)
	 * 
	 * @param filtro filtro de pesquisa
	 * @return Quantidade de inadimplencias
	 */
	Long obterTotalInadimplenciasCota(FiltroCotaInadimplenteDTO filtro);

	/**
	 * Obtém quantidade de cotas distintas da pesquisa de inadimplencias da cota 
	 * 
	 * @param filtro filtro de pesquisa
	 * @return Quantidade de cotas
	 */
	Long obterTotalCotasInadimplencias(FiltroCotaInadimplenteDTO filtro);

	/**
	 * Obter soma de dividas referentes ao filtro
	 * 
	 * @param filtro
	 * @return
	 */
	Double obterSomaDividas(FiltroCotaInadimplenteDTO filtro);
	
	Divida obterDividaPorIdConsolidadoNaoNegociado(Long idConsolidado);
	
	Divida obterDividaPorIdConsolidado(Long idConsolidado);
	
	/**
     * Obtem somatório das dividas em aberto de uma Cota
     * 
     * @param idCota
     * @return BigDecimal
     */
	BigDecimal obterTotalDividasAbertoCota(Long idCota);
	
	/**
     * Obtem total de dividas vencidas em aberto da cota
     * 
     * @param idCota
     * @param data
     * @return BigDecimal
     */
	BigDecimal obterTotalDividasVencidasCota(Long idCota, Date data);

	BigDecimal obterValorDividasDataOperacao(boolean dividaVencendo, boolean dividaAcumulada);

	BigDecimal obterValoresDividasGeradasDataOperacao(boolean postergada);

	/**
     * Sumariza as dívidas a receber em uma determinada data
     * 
     * @param data
     *            data para sumarização das dívidas
     * @return mapa com as sumarizações das dívidas a receber em uma 
     *         determinada data
     */
	List<SumarizacaoDividasDTO> sumarizacaoDividasReceberEm(Date data);

	/**
     * Sumariza as dívidas a vencer após um determinada data
     * 
     * @param data
     *            data para sumarização das dívidas
     * @return mapa com as sumarizações das dívidas a receber em uma
     *         determinada data
     */
	List<SumarizacaoDividasDTO> sumarizacaoDividasVencerApos(Date data);

    /**
     * Obtém as dívidas a receber em uma determinada data
     * 
     * @param data
     *            data para recuperação das dívidas à receber
     * @param paginacao
     *            parâmtros de paginação das dívidas, permite {@code null},
     *            neste caso retorna todas as dívidas
     * @return lista de dividas à receber em uma determinada data
     */
    List<Cobranca> obterDividasReceberEm(Date data, PaginacaoVO paginacao);

    /**
     * Obtém as dívidas a vencer após uma determinada data
     * 
     * @param data
     *            data para recuperação das dívidas à vencer
     * @param paginacao
     *            parâmtros de paginação das dívidas, permite {@code null},
     *            neste caso retorna todas as dívidas
     * @return lista de dividas à vencer após uma determinada data
     */
    List<Cobranca> obterDividasVencerApos(Date data, PaginacaoVO paginacao);

    /**
     * Conta a quantidade de dívidas à receber em uma determinada data
     * 
     * @param data
     *            data para contagem das dívidas
     * @return quantidade de dividas a receber em uma determinada data
     */
    long contarDividasReceberEm(Date data);

    /**
     * Conta a quantidade de dívidas à vencer após uma determinada data
     * 
     * @param data
     *            data para contagem das dívidas
     * @return quantidade de dívidas a vencer após determinada data
     */
    long contarDividasVencerApos(Date data);
    
    Long verificarEnvioDeEmail(GeraDividaDTO dividaGerada);

    BigInteger obterIdDividaPendente(ConsolidadoFinanceiroCota consolidado, Date dataOperaçao);
}
