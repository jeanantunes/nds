package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class MovimentoFinanceiroDTO implements Serializable {
	
	private static final long serialVersionUID = 1437500155210419731L;

	private String descricao;
	private Date data;
	private BigDecimal valor;
	
	public MovimentoFinanceiroDTO() {
		
	}
			
	public MovimentoFinanceiroDTO(String descricao, Date data, BigDecimal valor) {
		super();
		this.descricao = descricao;
		this.data = data;
		this.valor = valor;
	}

	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public Date getData() {
		return data;
	}
	public void setData(Date data) {
		this.data = data;
	}
	public BigDecimal getValor() {
		return valor;
	}
	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}
		
}
