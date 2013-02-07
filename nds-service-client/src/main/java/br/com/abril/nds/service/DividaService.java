package br.com.abril.nds.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import br.com.abril.nds.dto.DividaComissaoDTO;
import br.com.abril.nds.dto.StatusDividaDTO;
import br.com.abril.nds.dto.fechamentodiario.SumarizacaoDividasDTO;
import br.com.abril.nds.dto.filtro.FiltroCotaInadimplenteDTO;
import br.com.abril.nds.model.financeiro.Divida;
import br.com.abril.nds.vo.PaginacaoVO;

public interface DividaService {

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
	 * Obtém soma de dívidas referente ao filtro
	 * 
	 * @param filtro
	 * @return
	 */
	Double obterSomaDividas(FiltroCotaInadimplenteDTO filtro);
	
	/**
	 * Obtém dados do acumulos da divida
	 * 
	 * @param idDivida
	 * @return
	 */
	List<Divida> getDividasAcumulo(Long idDivida);
	
	
	/**
	 * Obtém divida por código
	 * 
	 * @param idDivida
	 * @return
	 */
	Divida obterDividaPorId(Long idDivida);
	
	/**
	 * Posterga a dívida de uma cota.
	 * 
	 * @param listaIdsCobranca
	 * @param dataPostergacao
	 * @param juros
	 * @param multa
	 */
	void postergarCobrancaCota(List<Long> listaIdsCobranca, Date dataPostergacao, Long idUsuario, boolean isIsento);

	/**
	 * Método que calcula os encagos das cobranças.
	 * 
	 * @param listaIdsCobranca
	 * @param dataPostergacao
	 * @return
	 */
	BigDecimal calcularEncargosPostergacao(List<Long> listaIdsCobranca, Date dataPostergacao);

    /**
     * Obtem somatório das dividas em aberto de uma Cota
     * @param idCota
     * @return BigDecimal
     */
	BigDecimal obterTotalDividasAbertoCota(Long idCota);


	DividaComissaoDTO obterDadosDividaComissao(Long idDivida);
	
    /**
     * Sumariza as dívidas a receber em uma determinada data
     * 
     * @param data
     *            data para sumarização das dívidas
     * @return Lista com as sumarizações das dívidas a receber em uma
     *         determinada data
     */
	List<SumarizacaoDividasDTO> sumarizacaoDividasReceberEm(Date data);
	
    /**
     * Sumariza as dívidas a vencer após um determinada data
     * 
     * @param data
     *            data para sumarização das dívidas
     * @return Lista com as sumarizações das dívidas a receber em uma
     *         determinada data
     */
	List<SumarizacaoDividasDTO> sumarizacaoDividasVencerApos(Date data);
	
	
    /**
     * Recupera as dívidas a receber em uma determinada data
     * 
     * @param data
     *            data para recuperação das dívidas
     * @param paginacao
     *            VO com informções de paginação, permite {@code null}, 
     *            neste caso retorna todas as dívidas
     * @return lista de dividas a receber em uma determinada data
     */
	List<Divida> obterDividasReceberEm(Date data, PaginacaoVO paginacao);
	
	/**
     * Recupera as dívidas a vencer após uma determinada data
     * 
     * @param data
     *            data para recuperação das dívidas
     * @param paginacao
     *            VO com informções de paginação, permite {@code null}, 
     *            neste caso retorna todas as dívidas
     * @return lista de dividas a vencer após uma determinada data
     */
	List<Divida> obterDividasVencerApos(Date data, PaginacaoVO paginacao);


    /**
     * Conta a quantidade de dívidas a receber em uma determinada data
     * 
     * @param data
     *            data para contagem das dívidas
     * @return quantidade de dividas a receber em uma determinada data
     */
	long contarDividasReceberEm(Date data);
	
	
    /**
     * Conta a quantidade de dívidas a vencer apos uma determinada data
     * 
     * @param data
     *            data para contagem das dívidas
     * @return quantidade de dívidas a vencer após determinada data
     */
    long contarDividasVencerApos(Date data);
	
	
}
