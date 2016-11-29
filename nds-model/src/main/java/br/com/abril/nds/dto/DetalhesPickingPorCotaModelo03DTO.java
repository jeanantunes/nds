package br.com.abril.nds.dto;

public class DetalhesPickingPorCotaModelo03DTO {
	
	private String identificadorLinha;
	
	private String codigoCota;
	
	private String sequencia;
	
	private String produto;
	
	private String edicao;
	
	private String nome;
	
	private String preco;
	
	private String precoDesconto;
	
	private String desconto;
	
	private String quantidade;
	
	private String codigoDeBarras;
	
	public String getCodigoCota() {
		return codigoCota;
	}

	public String getSequencia() {
		return sequencia;
	}

	public String getProduto() {
		return produto;
	}

	public String getEdicao() {
		return edicao;
	}

	public String getNome() {
		return nome;
	}

	public String getPreco() {
		return preco;
	}

	public String getPrecoDesconto() {
		return precoDesconto;
	}

	public String getQuantidade() {
		return quantidade;
	}

	public void setCodigoCota(String codigoCota) {
		this.codigoCota = codigoCota;
	}

	public void setSequencia(String sequencia) {
		this.sequencia = sequencia;
	}

	public void setProduto(String produto) {
		this.produto = produto;
	}

	public void setEdicao(String edicao) {
		this.edicao = edicao;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public void setPreco(String preco) {
		this.preco = preco;
	}

	public void setPrecoDesconto(String precoDesconto) {
		this.precoDesconto = precoDesconto;
	}

	public void setQuantidade(String quantidade) {
		this.quantidade = quantidade;
	}

	public String getDesconto() {
		return desconto;
	}

	public void setDesconto(String desconto) {
		this.desconto = desconto;
	}

	public String getIdentificadorLinha() {
		return identificadorLinha;
	}

	public void setIdentificadorLinha(String identificadorLinha) {
		this.identificadorLinha = identificadorLinha;
	}
	
	public String getCodigoDeBarras() {
		return codigoDeBarras;
	}

	public void setCodigoDeBarras(String codigoDeBarras) {
		this.codigoDeBarras = codigoDeBarras;
	}
}
