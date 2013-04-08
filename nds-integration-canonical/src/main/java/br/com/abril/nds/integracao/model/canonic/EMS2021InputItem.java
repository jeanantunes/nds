package br.com.abril.nds.integracao.model.canonic;

import java.io.Serializable;

import com.ancientprogramming.fixedformat4j.annotation.Record;

@Record
public class EMS2021InputItem extends IntegracaoDocumentDetail implements Serializable {

    private static final long serialVersionUID = -6768730628645056820L;

    private Long codigoProduto;
    private Integer numeroEdicao;
    private Integer periodo;
    private Integer peso;
    private String cesta;

    public Long getCodigoProduto() {
	return codigoProduto;
    }

    public void setCodigoProduto(Long codigoProduto) {
	this.codigoProduto = codigoProduto;
    }

    public Integer getNumeroEdicao() {
	return numeroEdicao;
    }

    public void setNumeroEdicao(Integer numeroEdicao) {
	this.numeroEdicao = numeroEdicao;
    }

    public Integer getPeriodo() {
	return periodo;
    }

    public void setPeriodo(Integer periodo) {
	this.periodo = periodo;
    }

    public Integer getPeso() {
	return peso;
    }

    public void setPeso(Integer peso) {
	this.peso = peso;
    }

    public String getCesta() {
	return cesta;
    }

    public void setCesta(String cesta) {
	this.cesta = cesta;
    }
}