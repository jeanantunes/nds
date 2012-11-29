package br.com.abril.nds.repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import br.com.abril.nds.dto.ChamadaAntecipadaEncalheDTO;
import br.com.abril.nds.dto.filtro.FiltroChamadaAntecipadaEncalheDTO;
import br.com.abril.nds.model.planejamento.ChamadaEncalheCota;


public interface ChamadaEncalheCotaRepository extends Repository<ChamadaEncalheCota,Long> {
	
	/**
	 * Obtém obtém o reparte referente a chamada encalhe relacionadas a um 
	 * numeroCota e dataRecolhimento.  
	 * 
	 * Se flag indPesquisaCEFutura for true sera pesquisado registro com 
	 * dataRecolhimento >= a data passado por parâmetro, senão, pesquisará 
	 * registros com dataRecolhimento igual data passada por parâmetro.
	 * 
	 * @param numeroCota
	 * @param dataOperacao
	 * @param conferido
	 * @param postergado
	 * 
	 * @return List - Long
	 */
	public BigDecimal obterReparteDaChamaEncalheCota(Integer numeroCota, Date dataOperacao, boolean conferido, boolean postergado);
	
	
	/**
	 * Obtém qtde registro chamada encalhe cota a partir do 
	 * numeroCota e dataRecolhimento.  
	 * 
	 * Se flag indPesquisaCEFutura for true sera pesquisado registro com 
	 * dataRecolhimento >= a data passado por parâmetro, senão, pesquisará 
	 * registros com dataRecolhimento igual data passada por parâmetro.
	 * 
	 * @param numeroCota
	 * @param dataOperacao
	 * @param idProdutoEdicao
	 * @param indPesquisaCEFutura
	 * @param conferido
	 * @param postergado
	 * 
	 * @return Long
	 */
	public Long obterQtdListaChamaEncalheCota(Integer numeroCota, Date dataOperacao, Long idProdutoEdicao, boolean indPesquisaCEFutura, boolean conferido, boolean postergado);

	
	/**
	 * Obtém lista de chamada encalhe cota a partir do 
	 * numeroCota e dataRecolhimento.  
	 * 
	 * Se flag indPesquisaCEFutura for true sera pesquisado registro com 
	 * dataRecolhimento >= a data passado por parâmetro, senão, pesquisará 
	 * registros com dataRecolhimento igual data passada por parâmetro.
	 * 
	 * @param numeroCota
	 * @param dataOperacao
	 * @param idProdutoEdicao
	 * @param indPesquisaCEFutura
	 * @param conferido
	 * @param postergado
	 * 
	 * @return List - ChamadaEncalheCota
	 */
	public List<ChamadaEncalheCota> obterListaChamaEncalheCota(
			Integer numeroCota, 
			Date dataOperacao, 
			Long idProdutoEdicao, 
			boolean indPesquisaCEFutura, 
			boolean conferido,
			boolean postergado);
	
	
	/**
	 * Retorna uma chamada de encalhe cota en função da chamada de encahe e a cota informados nos parâmetros 
	 * @param idChamadaEncalhe - identificador da chamda de encalhe
	 * @param idCota - identificador da cota
	 * @return ChamadaEncalheCota
	 */
	ChamadaEncalheCota buscarPorChamadaEncalheECota(Long idChamadaEncalhe,Long idCota);
	
	/**
	 * 
	 * Retorna quantidade de chamadas de encalhe cota que foram programadas para antecipação de recolhimento
	 * em função dos parâmetros informados no FiltroChamadaAntecipadaEncalheDTO
	 * 
	 * @param filtro - filtro com os parâmetros de consulta
	 * 
	 * @return Long
	 */
	Long obterQntCotasProgramadaParaAntecipacoEncalhe(FiltroChamadaAntecipadaEncalheDTO filtro);
	
	/**
	 * Retorna uma lista de chamadas de encalhe cota que foram programadas para antecipação de recolhimento
	 * em função dos parâmetros informados no FiltroChamadaAntecipadaEncalheDTO
	 * 
	 * @param filtro - filtro com os parâmetros de consulta
	 * 
	 * @return List<ChamadaAntecipadaEncalheDTO>
	 */
	List<ChamadaAntecipadaEncalheDTO> obterCotasProgramadaParaAntecipacoEncalhe(FiltroChamadaAntecipadaEncalheDTO filtro);
	
	/**
	 * 
	 * Retorna a quantidade prevista de encalhe de uma cota programada para antecipação de encalhe.
	 * 
	 * @param filtro -filtro com os parâmetros de consulta
	 * 
	 * @return BigDecimal
	 */
	BigDecimal obterQntExemplaresComProgramacaoAntecipadaEncalheCota(FiltroChamadaAntecipadaEncalheDTO filtro);
	
	/**
	 * Retorna a quantidade de chamada encalhe cota relacionada a uma chamada de encalhe
	 * 
	 * @param idChamadaEncalhe  -identificador da chamada de encalhe
	 * 
	 * @return Long
	 */
	Long obterQntChamadaEncalheCota(Long idChamadaEncalhe);
}
