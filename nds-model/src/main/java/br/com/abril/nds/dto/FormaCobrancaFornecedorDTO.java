package br.com.abril.nds.dto;

import java.io.Serializable;

public class FormaCobrancaFornecedorDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -611261116938844454L;
	
	private Long idCota;
	private Long numeroCota;
	private Long idFornecedor;
	
	public FormaCobrancaFornecedorDTO() {
		
	}	
	
	public FormaCobrancaFornecedorDTO(Long idCota, Long idFornecedor) {
		super();
		this.idCota = idCota;
		this.idFornecedor = idFornecedor;
	}

	public Long getIdCota() {
		return idCota;
	}

	public void setIdCota(Long idCota) {
		this.idCota = idCota;
	}

	public Long getNumeroCota() {
		return numeroCota;
	}

	public void setNumeroCota(Long numeroCota) {
		this.numeroCota = numeroCota;
	}

	public Long getIdFornecedor() {
		return idFornecedor;
	}

	public void setIdFornecedor(Long idFornecedor) {
		this.idFornecedor = idFornecedor;
	}
	
}