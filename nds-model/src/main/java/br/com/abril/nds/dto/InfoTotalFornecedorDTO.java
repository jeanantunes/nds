package br.com.abril.nds.dto;

import java.math.BigDecimal;

public class InfoTotalFornecedorDTO {
	
	private String nomeFornecedor;
	
	private BigDecimal valorTotal;
	
	
	public InfoTotalFornecedorDTO() {
	}
	
	
	
	
	public InfoTotalFornecedorDTO(String nomeFornecedor, BigDecimal valorTotal) {
		super();
		this.nomeFornecedor = nomeFornecedor;
		this.valorTotal = valorTotal;
	}




	public String getNomeFornecedor() {
		return nomeFornecedor;
	}
	public void setNomeFornecedor(String nomeFornecedor) {
		this.nomeFornecedor = nomeFornecedor;
	}
	public BigDecimal getValorTotal() {
		return valorTotal;
	}
	public void setValorTotal(BigDecimal valorTotal) {
		this.valorTotal = valorTotal;
	}
	

}
