package br.com.abril.nds.repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import br.com.abril.nds.dto.GeraDividaDTO;
import br.com.abril.nds.dto.StatusDividaDTO;
import br.com.abril.nds.dto.filtro.FiltroCotaInadimplenteDTO;
import br.com.abril.nds.dto.filtro.FiltroDividaGeradaDTO;
import br.com.abril.nds.model.financeiro.Divida;

public interface DividaRepository extends Repository<Divida, Long>{

	Divida obterDividaParaAcumuloPorCota(Long idCota, Date diaDivida);
	
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
	
	Divida obterDividaPorIdConsolidado(Long idConsolidado);
	
	/**
     * Obtem somatório das dividas em aberto de uma Cota
     * @param idCota
     * @return BigDecimal
     */
	BigDecimal obterTotalDividasAbertoCota(Long idCota);

	BigDecimal obterValorDividasDataOperacao(boolean dividaVencendo, boolean dividaAcumulada);

	BigDecimal obterValoresDividasGeradasDataOperacao(boolean postergada);
}
