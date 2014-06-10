package br.com.abril.nds.dto;

import java.io.Serializable;
import java.util.Date;

public class EmissaoBandeiraDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 120096531838818465L;

	private String codigoProduto;
	
	private String nomeProduto;
	
	private Long edicao;
	
	private Integer pacotePadrao;
	
	private Date data;
	
	private String destino;
	
	private Integer prioridade;

	public String getCodigoProduto() {
		return codigoProduto;
	}

	public void setCodigoProduto(String codigoProduto) {
		this.codigoProduto = codigoProduto;
	}

	public String getNomeProduto() {
		return nomeProduto;
	}

	public void setNomeProduto(String nomeProduto) {
		this.nomeProduto = nomeProduto;
	}

	public Long getEdicao() {
		return edicao;
	}

	public void setEdicao(Long edicao) {
		this.edicao = edicao;
	}

	public Integer getPacotePadrao() {
		return pacotePadrao;
	}

	public void setPacotePadrao(Integer pacotePadrao) {
		this.pacotePadrao = pacotePadrao;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public String getDestino() {
		return destino;
	}

	public void setDestino(String destino) {
		this.destino = destino;
	}

	public Integer getPrioridade() {
		return prioridade;
	}

	public void setPrioridade(Integer prioridade) {
		this.prioridade = prioridade;
	}
	
		

}
