package br.com.abril.nds.model;

public class Produto {

	private Integer id;
	private String descricao;
	private Integer numeroEdicao;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public Integer getNumeroEdicao() {
		return numeroEdicao;
	}
	public void setNumeroEdicao(Integer numeroEdicao) {
		this.numeroEdicao = numeroEdicao;
	}
}
