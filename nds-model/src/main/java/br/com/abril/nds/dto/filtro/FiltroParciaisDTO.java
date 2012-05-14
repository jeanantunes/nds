package br.com.abril.nds.dto.filtro;

import java.io.Serializable;

public class FiltroParciaisDTO implements Serializable  {

	private static final long serialVersionUID = -4620933960876071792L;
	
	private String codigoProduto;
	private String nomeProduto;
	private Integer edicaoProduto;
	private Long idFornecedor;
	private String dataInicial;
	private String dataFinal;
	private String status;
	
	
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
	public Integer getEdicaoProduto() {
		return edicaoProduto;
	}
	public void setEdicaoProduto(Integer edicaoProduto) {
		this.edicaoProduto = edicaoProduto;
	}
	public Long getIdFornecedor() {
		return idFornecedor;
	}
	public void setIdFornecedor(Long idFornecedor) {
		this.idFornecedor = idFornecedor;
	}
	public String getDataInicial() {
		return dataInicial;
	}
	public void setDataInicial(String dataInicial) {
		this.dataInicial = dataInicial;
	}
	public String getDataFinal() {
		return dataFinal;
	}
	public void setDataFinal(String dataFinal) {
		this.dataFinal = dataFinal;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

	
}
