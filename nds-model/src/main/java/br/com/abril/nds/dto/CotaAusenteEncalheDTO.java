package br.com.abril.nds.dto;

import java.io.Serializable;
import java.util.Date;

import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Export.Alignment;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class CotaAusenteEncalheDTO implements Serializable {

	private static final long serialVersionUID = -5167121794665878284L;
	
	private Long idCota;

	@Export(label = "Cota", alignment = Alignment.CENTER, exhibitionOrder = 1, widthPercent=10)
	private Integer numeroCota;

	@Export(label = "Nome", alignment = Alignment.CENTER, exhibitionOrder = 2, widthPercent=30)
	private String colaboradorName = "";

	@Export(label = "Box", alignment = Alignment.CENTER, exhibitionOrder = 3, widthPercent=10)
	private String boxName = "";

	@Export(label = "Roteiro", alignment = Alignment.CENTER, exhibitionOrder = 4)
	private String roteiroName = "";

	@Export(label = "Rota", alignment = Alignment.CENTER, exhibitionOrder = 5)
	private String rotaName = "";

	@Export(label = "Ação", alignment = Alignment.CENTER, exhibitionOrder = 6, widthPercent=15)
	private String acao = "";

	private Boolean fechado;

	private Boolean postergado;
	
	private Date dataEncalhe;
	
	private String check;
	
	/**
	 * Construtor Padrão
	 */
	public CotaAusenteEncalheDTO() {
		
	}

	/**
	 * @return the numeroCota
	 */
	public Integer getNumeroCota() {
		return numeroCota;
	}

	/**
	 * @param numeroCota the numeroCota to set
	 */
	public void setNumeroCota(Integer numeroCota) {
		this.numeroCota = numeroCota;
	}

	/**
	 * @return the colaboradorName
	 */
	public String getColaboradorName() {
		return colaboradorName;
	}

	/**
	 * @param colaboradorName the colaboradorName to set
	 */
	public void setColaboradorName(String colaboradorName) {
		this.colaboradorName = colaboradorName != null ? colaboradorName : "";
	}

	/**
	 * @return the boxName
	 */
	public String getBoxName() {
		return boxName;
	}

	/**
	 * @param boxName the boxName to set
	 */
	public void setBoxName(String boxName) {
		this.boxName = boxName != null ? boxName : "";
	}

	/**
	 * @return the roteiroName
	 */
	public String getRoteiroName() {
		return roteiroName;
	}

	/**
	 * @param roteiroName the roteiroName to set
	 */
	public void setRoteiroName(String roteiroName) {
		this.roteiroName = roteiroName!= null ? roteiroName : "";
	}

	/**
	 * @return the rotaName
	 */
	public String getRotaName() {
		return rotaName;
	}

	/**
	 * @param rotaName the rotaName to set
	 */
	public void setRotaName(String rotaName) {
		this.rotaName = rotaName!= null ? rotaName : "";
	}

	/**
	 * @return the acao
	 */
	public String getAcao() {
		return acao;
	}

	/**
	 * @param acao the acao to set
	 */
	public void setAcao(String acao) {
		this.acao = acao;
	}
	
	/**
	 * @return the check
	 */
	public String getCheck() {
		return check;
	}

	/**
	 * @param check the check to set
	 */
	public void setCheck(String check) {
		this.check = check;
	}

	/**
	 * @return the idCota
	 */
	public Long getIdCota() {
		return idCota;
	}

	/**
	 * @param idCota the idCota to set
	 */
	public void setIdCota(Long idCota) {
		this.idCota = idCota;
	}

	/**
	 * @return the dataEncalhe
	 */
	public Date getDataEncalhe() {
		return dataEncalhe;
	}

	/**
	 * @param dataEncalhe the dataEncalhe to set
	 */
	public void setDataEncalhe(Date dataEncalhe) {
		this.dataEncalhe = dataEncalhe;
	}

	/**
	 * @return the fechado
	 */
	public Boolean getFechado() {
		return fechado;
	}

	/**
	 * @param fechado the fechado to set
	 */
	public void setFechado(Boolean fechado) {
		this.fechado = fechado;
	}

	/**
	 * @return the postergado
	 */
	public Boolean getPostergado() {
		return postergado;
	}

	/**
	 * @param postergado the postergado to set
	 */
	public void setPostergado(Boolean postergado) {
		this.postergado = postergado;
	}
	
}
