package br.com.abril.nds.dto;

import java.io.Serializable;
import java.util.Date;

public class FuroProdutoDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -580201558784688016L;
	
	private String codigoProduto;
	
	private String nomeProduto;
	
	private Long edicao;
	
	private Long quantidadeExemplares;
	
	private Date novaData;
	
	private String pathImagem;
	
	public FuroProdutoDTO(String codigoProduto, String nomeProduto, Long edicao, Long quantidadeExemplares, 
			Date novaData, String pathImagem){
		this.codigoProduto = codigoProduto;
		this.nomeProduto = nomeProduto;
		this.edicao = edicao;
		this.quantidadeExemplares = quantidadeExemplares;
		this.novaData = novaData;
		this.pathImagem = pathImagem;
	}
	
	public FuroProdutoDTO(String codigoProduto, String nomeProduto, Long edicao, Long quantidadeExemplares, String pathCapas){
		this(codigoProduto, nomeProduto, edicao, quantidadeExemplares, null, pathCapas + codigoProduto + edicao);
	}
	
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

	public Long getQuantidadeExemplares() {
		return quantidadeExemplares;
	}

	public void setQuantidadeExemplares(Long quantidadeExemplares) {
		this.quantidadeExemplares = quantidadeExemplares;
	}

	public Date getNovaData() {
		return novaData;
	}

	public void setNovaData(Date novaData) {
		this.novaData = novaData;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getPathImagem() {
		return pathImagem;
	}

	public void setPathImagem(String pathImagem) {
		this.pathImagem = pathImagem;
	}
}