package br.com.abril.nds.dto;

import java.util.Date;

public class MovimentoFinanceiroDTO {

	private String descricao;
	private Date data;
	private String valor;
	
	public MovimentoFinanceiroDTO() {
		
	}
			
	public MovimentoFinanceiroDTO(String descricao, Date data, String valor) {
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
	public String getValor() {
		return valor;
	}
	public void setValor(String valor) {
		this.valor = valor;
	}
		
}
