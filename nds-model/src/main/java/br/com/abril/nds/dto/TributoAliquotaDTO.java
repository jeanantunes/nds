package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class TributoAliquotaDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long id;
	
	private Long tributoId;
	
	private String tributo;
	
	private String tributoDescricao;
	
	private String tipoAliquota;
	
	private BigDecimal valor;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getTributoId() {
		return tributoId;
	}

	public void setTributoId(Long tributoId) {
		this.tributoId = tributoId;
	}

	public String getTributo() {
		return tributo;
	}

	public void setTributo(String tributo) {
		this.tributo = tributo;
	}

	public String getTributoDescricao() {
		return tributoDescricao;
	}

	public void setTributoDescricao(String tributoDescricao) {
		this.tributoDescricao = tributoDescricao;
	}

	public String getTipoAliquota() {
		return tipoAliquota;
	}

	public void setTipoAliquota(String tipoAliquota) {
		this.tipoAliquota = tipoAliquota;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}
	
}