package br.com.abril.nds.dto;

import java.io.Serializable;

import br.com.abril.nds.dto.filtro.FiltroDTO;

/*
 * Classe utilizada no detalhamento do produto. 
 * 
 * Funcionalidade: Informações do Produto.
 * 
 */

public class InformacoesBaseProdDTO extends FiltroDTO implements Serializable {

	private static final long serialVersionUID = 6651246829502909110L;

	private String codProduto;
	private String nomeProduto;
	private Long numeroEdicao;
	private Long peso;
	
	
	public String getCodProduto() {
		return codProduto;
	}
	public void setCodProduto(String codProduto) {
		this.codProduto = codProduto;
	}
	public Long getNumeroEdicao() {
		return numeroEdicao;
	}
	public void setNumeroEdicao(Long numeroEdicao) {
		this.numeroEdicao = numeroEdicao;
	}
	public String getNomeProduto() {
		return nomeProduto;
	}
	public void setNomeProduto(String nomeProduto) {
		this.nomeProduto = nomeProduto;
	}
	public Long getPeso() {
		return peso;
	}
	public void setPeso(Long peso) {
		this.peso = peso;
	}
}
