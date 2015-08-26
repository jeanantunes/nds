package br.com.abril.nds.repository;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import br.com.abril.nds.dto.ChamadaAntecipadaEncalheDTO;
import br.com.abril.nds.dto.chamadaencalhe.ChamadaEncalheCotaDTO;
import br.com.abril.nds.dto.filtro.FiltroChamadaAntecipadaEncalheDTO;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.planejamento.ChamadaEncalhe;
import br.com.abril.nds.model.planejamento.ChamadaEncalheCota;


public interface ChamadaEncalheCotaRepository extends Repository<ChamadaEncalheCota,Long> {
	
	/**
	 * Obtém a chamada de encalhe mais próxima data de operação,
	 * Caso hajam chamadas de encalhe antes e depois da data de
	 * operação, será retornada a CE menor ou igual a data 
	 * de operação.
	 * 
	 * 
	 * @param cota
	 * @param idProdutoEdicao
	 * @param postergado
	 * @param dataOperacao
	 * 
	 * @return
	 */
	public Date obterDataChamadaEncalheCotaProximaDataOperacao(
			Cota cota, Long idProdutoEdicao, boolean postergado,
			Date dataOperacao);

	
	/**
	 * Retorna o id do registro de ChamadaEncalheCota.
	 * 
	 * @param idCota
	 * @param idProdutoEdicao
	 * @param dataRecolhimento
	 * 
	 * @return Long
	 */
	public Long obterIdChamadaEncalheCotaNaData(Long idCota, Long idProdutoEdicao, Date dataRecolhimento);
	
	/**
	 * Obtém obtém o reparte referente a chamada encalhe relacionadas a um 
	 * numeroCota e dataRecolhimento.  
	 * 
	 * @param numeroCota
	 * @param datas
	 * @param conferido
	 * @param postergado
	 * 
	 * @return BigDecimal
	 */
	public BigDecimal obterReparteDaChamaEncalheCota(Integer numeroCota, List<Date> datas, Boolean conferido, Boolean postergado);
	
	
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
	 * Obtém uma lista de chamadaEncalheCota para a 
	 * cota e produtoEdicao pesquisados.
	 * 
	 * @param idCota
	 * @param idProdutoEdicao
	 * 
	 * @return List<ChamadaEncalheCota>
	 */
	public List<ChamadaEncalheCota> obterListaChamadaEncalheCota(Long idCota, Long idProdutoEdicao);
	
	
	/**
	 * Obtém a ultima chamada encalhe cota a partir do 
	 * numeroCota e dataRecolhimento.  
	 * 
	 * 
	 * @param cota
	 * @param dataOperacao
	 * @param idProdutoEdicao
	 * @param postergado
	 * 
	 * @return ChamadaEncalheCota
	 */
	public ChamadaEncalheCota obterUltimaChamaEncalheCota(Cota cota,Long idProdutoEdicao,
			   											 boolean postergado,Date dataOperacao);
	
	
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


	/**
	 * Obtém chamadas encalhe cota pelo ID da chamada de Encalhe e pelo ID da cota
	 * 
	 * @param chamadaEncalheID
	 * @param cotaID
	 * @return
	 */
	List<ChamadaEncalheCota> obterListChamadaEncalheCota(Long cotaID, Date dataRecolhimento);

	BigDecimal obterTotalDescontoDaChamaEncalheCota(Integer numeroCota,
			Date dataOperacao, Boolean conferido, Boolean postergado);

	BigDecimal obterTotalDaChamaEncalheCotaSemDesconto(Integer numeroCota,
			Date dataOperacao, Boolean conferido, Boolean postergado);
	
	ChamadaEncalheCota obterChamadaEncalheCotaNaData(Cota cota,Long idProdutoEdicao,boolean postergado,Date dataOperacao);
	
	BigInteger quantidadeCotaAusenteFechamentoEncalhe(Integer numeroCota, Date dataRecolhimento);
	
	Boolean existeChamadaEncalheCota(Long idCota, Long idProdutoEdicao, Boolean fechado, Date dataRecolhimento);
	
	Boolean existeChamadaEncalheCota(Long idCota, Long idProdutoEdicao, Date dataRecolhimento);
	
	ChamadaEncalheCota obterChamadaEncalheCota(Long idCota, Long idProdutoEdicao, Date dataRecolhimento);
	
	Long quantidadeChamadasEncalheParaCota(Long idCota, Date periodoInicial, Date periodoFinal);

	List<ChamadaEncalheCotaDTO> buscarPorChamadaEncalhe(ChamadaEncalhe ce);

	/**
	 * Remove Chamadas de Encalhe de Cota por lista de ID da chamada de encalhe
	 * 
	 * @param ids
	 */
	void removerChamadaEncalheCotaPorIdsChamadaEncalhe(List<Long> ids);


	void removerChamadaEncalheCotaZerada(Date dataConfirmada);
	
}
