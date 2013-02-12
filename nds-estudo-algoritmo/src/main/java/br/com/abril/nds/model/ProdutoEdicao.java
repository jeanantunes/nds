package br.com.abril.nds.model;

import java.math.BigDecimal;

public class ProdutoEdicao {

	private Long id;
	private String nome;
	private Long numeroEdicao;
	private Integer peso;
	private BigDecimal reparte;
	private BigDecimal venda;
	private boolean parcial;
	private Integer pacotePadrao;
	private boolean edicaoAberta;
	private boolean colecao; // Atributo que define se o Produto é um fascículo/coleção
	private BigDecimal reparteMinimo; // Reparte mínimo configurado na tela de Mix de Produto 
	private BigDecimal reparteMaximo; // Reparte máximo configurado na tela de Mix de Produto

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getNumeroEdicao() {
		return numeroEdicao;
	}

	public void setNumeroEdicao(Long numeroEdicao) {
		this.numeroEdicao = numeroEdicao;
	}

	public Integer getPeso() {
		return peso;
	}

	public void setPeso(Integer peso) {
		this.peso = peso;
	}

	public BigDecimal getReparte() {
		return reparte;
	}

	public void setReparte(BigDecimal reparte) {
		this.reparte = reparte;
	}

	public BigDecimal getVenda() {
		return venda;
	}

	public void setVenda(BigDecimal venda) {
		this.venda = venda;
	}

	public boolean getParcial() {
		return parcial;
	}

	public void setParcial(boolean parcial) {
		this.parcial = parcial;
	}

	public Integer getPacotePadrao() {
		return pacotePadrao;
	}

	public void setPacotePadrao(Integer pacotePadrao) {
		this.pacotePadrao = pacotePadrao;
	}

	public boolean isEdicaoAberta() {
		return edicaoAberta;
	}

	public void setEdicaoAberta(boolean edicaoAberta) {
		this.edicaoAberta = edicaoAberta;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public boolean isColecao() {
		return colecao;
	}

	public void setColecao(boolean colecao) {
		this.colecao = colecao;
	}

	public BigDecimal getReparteMaximo() {
		return reparteMaximo;
	}

	public void setReparteMaximo(BigDecimal reparteMaximo) {
		this.reparteMaximo = reparteMaximo;
	}

	public BigDecimal getReparteMinimo() {
		return reparteMinimo;
	}

	public void setReparteMinimo(BigDecimal reparteMinimo) {
		this.reparteMinimo = reparteMinimo;
	}
}
