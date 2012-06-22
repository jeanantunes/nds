package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class ConsultaConsignadoCotaPeloFornecedorDTO implements Serializable {

	private static final long serialVersionUID = -6080837731839488665L;
	
	private Integer numeroCota;
	private String nomeCota;
	private BigDecimal reparte;
	private BigDecimal total;
	private BigDecimal totalDesconto;
	private String nomeFornecedor;
	
	public ConsultaConsignadoCotaPeloFornecedorDTO() { }
	
	public ConsultaConsignadoCotaPeloFornecedorDTO(Integer numeroCota,
			String nomeCota, BigDecimal reparte, BigDecimal total,
			BigDecimal totalDesconto, String nomeFornecedor) {
		super();
		this.numeroCota = numeroCota;
		this.nomeCota = nomeCota;
		this.reparte = reparte;
		this.total = total;
		this.totalDesconto = totalDesconto;
		this.nomeFornecedor = nomeFornecedor;
	}



	public Integer getNumeroCota() {
		return numeroCota;
	}

	public void setNumeroCota(Integer numeroCota) {
		this.numeroCota = numeroCota;
	}

	public String getNomeCota() {
		return nomeCota;
	}

	public void setNomeCota(String nomeCota) {
		this.nomeCota = nomeCota;
	}

	public BigDecimal getReparte() {
		return reparte;
	}

	public void setReparte(BigDecimal reparte) {
		this.reparte = reparte;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public BigDecimal getTotalDesconto() {
		return totalDesconto;
	}

	public void setTotalDesconto(BigDecimal totalDesconto) {
		this.totalDesconto = totalDesconto;
	}

	public String getNomeFornecedor() {
		return nomeFornecedor;
	}

	public void setNomeFornecedor(String nomeFornecedor) {
		this.nomeFornecedor = nomeFornecedor;
	}
	

}
