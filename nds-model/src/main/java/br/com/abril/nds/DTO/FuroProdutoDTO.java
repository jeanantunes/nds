package br.com.abril.nds.DTO;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FuroProdutoDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -580201558784688016L;
	
	private Long idProduto;
	
	private String nomeProduto;
	
	private Long edicao;
	
	private Long quantidadeExemplares;
	
	private Date novaData;
	
	private String novaDataString;
	
	private String pathImagem;
	
	public FuroProdutoDTO(Long idProduto, String nomeProduto, Long edicao, Long quantidadeExemplares, 
			Date novaData, String pathImagem){
		this.idProduto = idProduto;
		this.nomeProduto = nomeProduto;
		this.edicao = edicao;
		this.quantidadeExemplares = quantidadeExemplares;
		this.novaData = novaData;
		this.pathImagem = pathImagem;
		
		if (this.novaData != null){
			this.novaDataString = new SimpleDateFormat("dd/MM/yyyy").format(this.novaData);
		}
	}
	
	public Long getIdProduto() {
		return idProduto;
	}

	public void setIdProduto(Long idProduto) {
		this.idProduto = idProduto;
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

	public String getNovaDataString() {
		return novaDataString;
	}

	public void setNovaDataString(String novaDataString) {
		this.novaDataString = novaDataString;
	}
}