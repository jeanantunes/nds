package br.com.abril.nds.dto.filtro;

import java.util.Date;
import java.util.List;

import br.com.abril.nds.util.Intervalo;
import br.com.abril.nds.util.export.Exportable;
import br.com.abril.nds.vo.PaginacaoVO;

@Exportable
public class FiltroConsultaNotaEnvioDTO {

	private Date dataEmissao;
	
	private List<Long> idFornecedores;
	
	private Long idRota;
	
	private Long idRoteiro;

	private Intervalo<Date> intervaloMovimento;
	
	private Intervalo<Integer> intervaloCota;
	
	private Intervalo<Integer> intervaloBox;
	
	private PaginacaoVO paginacaoVO;
	
	/**
	 * @return the dataEmissao
	 */
	public Date getDataEmissao() {
		return dataEmissao;
	}

	/**
	 * @param dataEmissao the dataEmissao to set
	 */
	public void setDataEmissao(Date dataEmissao) {
		this.dataEmissao = dataEmissao;
	}

	/**
	 * @return the idFornecedores
	 */
	public List<Long> getIdFornecedores() {
		return idFornecedores;
	}

	/**
	 * @param idFornecedores the idFornecedores to set
	 */
	public void setIdFornecedores(List<Long> idFornecedores) {
		this.idFornecedores = idFornecedores;
	}

	/**
	 * @return the idRota
	 */
	public Long getIdRota() {
		return idRota;
	}

	/**
	 * @param idRota the idRota to set
	 */
	public void setIdRota(Long idRota) {
		this.idRota = idRota;
	}

	/**
	 * @return the idRoteiro
	 */
	public Long getIdRoteiro() {
		return idRoteiro;
	}

	/**
	 * @param idRoteiro the idRoteiro to set
	 */
	public void setIdRoteiro(Long idRoteiro) {
		this.idRoteiro = idRoteiro;
	}

	/**
	 * @return the intervaloMovimento
	 */
	public Intervalo<Date> getIntervaloMovimento() {
		return intervaloMovimento;
	}

	/**
	 * @param intervaloMovimento the intervaloMovimento to set
	 */
	public void setIntervaloMovimento(Intervalo<Date> intervaloMovimento) {
		this.intervaloMovimento = intervaloMovimento;
	}

	/**
	 * @return the intervaloCota
	 */
	public Intervalo<Integer> getIntervaloCota() {
		return intervaloCota;
	}

	/**
	 * @param intervaloCota the intervaloCota to set
	 */
	public void setIntervaloCota(Intervalo<Integer> intervaloCota) {
		this.intervaloCota = intervaloCota;
	}

	/**
	 * @return the intervaloBox
	 */
	public Intervalo<Integer> getIntervaloBox() {
		return intervaloBox;
	}

	/**
	 * @param intervaloBox the intervaloBox to set
	 */
	public void setIntervaloBox(Intervalo<Integer> intervaloBox) {
		this.intervaloBox = intervaloBox;
	}

	/**
	 * @return the paginacaoVO
	 */
	public PaginacaoVO getPaginacaoVO() {
		return paginacaoVO;
	}

	/**
	 * @param paginacaoVO the paginacaoVO to set
	 */
	public void setPaginacaoVO(PaginacaoVO paginacaoVO) {
		this.paginacaoVO = paginacaoVO;
	}
	
}
