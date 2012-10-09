package br.com.abril.nds.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import br.com.abril.nds.dto.DividaComissaoDTO;
import br.com.abril.nds.dto.StatusDividaDTO;
import br.com.abril.nds.dto.filtro.FiltroCotaInadimplenteDTO;
import br.com.abril.nds.model.financeiro.Divida;

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
}
