package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigInteger;


public class EstoqueDTO implements Serializable {

	private static final long serialVersionUID = -6182474537696330927L;
	
	private String desc;
	private String nameEnum;
	private BigInteger qtde;
	
	
	
	public EstoqueDTO(String nameEnum, String desc, BigInteger qtde) {
		this.desc = desc;
		this.nameEnum = nameEnum;
		this.qtde = qtde;
	}
	/**
	 * @return the desc
	 */
	public String getDesc() {
		return desc;
	}
	/**
	 * @param desc the desc to set
	 */
	public void setDesc(String desc) {
		this.desc = desc;
	}
	/**
	 * @return the nameEnum
	 */
	public String getNameEnum() {
		return nameEnum;
	}
	/**
	 * @param nameEnum the nameEnum to set
	 */
	public void setNameEnum(String nameEnum) {
		this.nameEnum = nameEnum;
	}
	/**
	 * @return the qtde
	 */
	public BigInteger getQtde() {
		return qtde;
	}
	/**
	 * @param qtde the qtde to set
	 */
	public void setQtde(BigInteger qtde) {
		this.qtde = qtde;
	}
	
	
}
