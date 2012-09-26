package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

import br.com.abril.nds.util.CurrencyUtil;

public class AbastecimentoDTO implements Serializable {

	private static final long serialVersionUID = -1022730482920692146L;
	
	private String data;
	private Long idBox;
	private String box;
	private Integer totalProduto;
	private Integer totalReparte;
	private String totalBox;
	
	public AbastecimentoDTO() {
		
	}	
	
	public AbastecimentoDTO(String data, Long idBox, String box,
			Integer totalProduto, Integer totalReparte, String totalBox) {
		super();
		this.data = data;
		this.idBox = idBox;
		this.box = box;
		this.totalProduto = totalProduto;
		this.totalReparte = totalReparte;
		this.totalBox = totalBox;
	}

	/**
	 * @return the data
	 */
	public String getData() {
		return data;
	}
	/**
	 * @param data the data to set
	 */
	public void setData(String data) {
		this.data = data;
	}
	/**
	 * @return the idBox
	 */
	public Long getIdBox() {
		return idBox;
	}
	/**
	 * @param idBox the idBox to set
	 */
	public void setIdBox(Long idBox) {
		this.idBox = idBox;
	}
	/**
	 * @return the box
	 */
	public String getBox() {
		return box;
	}
	/**
	 * @param box the box to set
	 */
	public void setBox(String box) {
		this.box = box;
	}
	/**
	 * @return the totalProduto
	 */
	public Integer getTotalProduto() {
		return totalProduto;
	}
	/**
	 * @param totalProduto the totalProduto to set
	 */
	public void setTotalProduto(Long totalProduto) {
		this.totalProduto = totalProduto.intValue();
	}
	/**
	 * @return the totalReparte
	 */
	public Integer getTotalReparte() {
		return totalReparte;
	}
	/**
	 * @param totalReparte the totalReparte to set
	 */
	public void setTotalReparte(BigInteger totalReparte) {
		this.totalReparte = totalReparte.intValue();
	}
	/**
	 * @return the totalBox
	 */
	public String getTotalBox() {
		return totalBox;
	}
	/**
	 * @param totalBox the totalBox to set
	 */
	public void setTotalBox(BigDecimal totalBox) {
		this.totalBox = CurrencyUtil.formatarValor(totalBox);
	}
	
	
	
}
