package br.com.abril.nds.model;

import java.math.BigDecimal;

public class ProdutoEdicao extends ProdutoEdicaoBase {

    private static final long serialVersionUID = 733461221142668493L;
    
    private Integer peso;
    private BigDecimal reparte;
    private BigDecimal venda;
    private Integer pacotePadrao;
    private BigDecimal reparteMinimo; // Reparte mínimo configurado na tela de Mix de Produto
    private BigDecimal reparteMaximo; // Reparte máximo configurado na tela de Mix de Produto
    private BigDecimal indiceCorrecao;
    private BigDecimal vendaCorrigida;

    public ProdutoEdicao() {
	this.reparte = BigDecimal.ZERO;
	this.venda = BigDecimal.ZERO;
    	this.peso = 0;
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

    public Integer getPacotePadrao() {
	return pacotePadrao;
    }

    public void setPacotePadrao(Integer pacotePadrao) {
	this.pacotePadrao = pacotePadrao;
    }

    public BigDecimal getReparteMinimo() {
	return reparteMinimo;
    }

    public void setReparteMinimo(BigDecimal reparteMinimo) {
	this.reparteMinimo = reparteMinimo;
    }

    public BigDecimal getReparteMaximo() {
	return reparteMaximo;
    }

    public void setReparteMaximo(BigDecimal reparteMaximo) {
	this.reparteMaximo = reparteMaximo;
    }

    public BigDecimal getIndiceCorrecao() {
	return indiceCorrecao;
    }

    public void setIndiceCorrecao(BigDecimal indiceCorrecao) {
	this.indiceCorrecao = indiceCorrecao;
    }

    public BigDecimal getVendaCorrigida() {
	return vendaCorrigida;
    }

    public void setVendaCorrigida(BigDecimal vendaCorrigida) {
	this.vendaCorrigida = vendaCorrigida;
    }
}
