package br.com.abril.nds.model;

import java.util.List;

public class Cota {

	private Long id;
	private Integer nome;
	private ClassificacaoCota classificacao;
	private Integer reparteCalculado;
	private List<EstoqueProdutoCota> estoqueProdutoCotas;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getNome() {
		return nome;
	}

	public void setNome(Integer nome) {
		this.nome = nome;
	}

	public ClassificacaoCota getClassificacao() {
		return classificacao;
	}

	public void setClassificacao(ClassificacaoCota classificacao) {
		this.classificacao = classificacao;
	}

	public Integer getReparteCalculado() {
		return reparteCalculado;
	}

	public void setReparteCalculado(Integer reparteCalculado) {
		this.reparteCalculado = reparteCalculado;
	}

	public List<EstoqueProdutoCota> getEstoqueProdutoCotas() {
		return estoqueProdutoCotas;
	}

	public void setEstoqueProdutoCotas(
			List<EstoqueProdutoCota> estoqueProdutoCotas) {
		this.estoqueProdutoCotas = estoqueProdutoCotas;
	}

}
