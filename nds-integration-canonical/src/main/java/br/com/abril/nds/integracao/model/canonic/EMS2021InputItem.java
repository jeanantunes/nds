package br.com.abril.nds.integracao.model.canonic;

import java.io.Serializable;

import com.ancientprogramming.fixedformat4j.annotation.Record;

@Record
public class EMS2021InputItem extends IntegracaoDocumentDetail implements Serializable {

    private static final long serialVersionUID = -6768730628645056820L;

    private String codigoProduto;
    private Long numeroEdicao;
    private Integer periodo;
    private Integer peso;

    public String getCodigoProduto() {
	return codigoProduto;
    }

    public void setCodigoProduto(String codigoProduto) {
	this.codigoProduto = codigoProduto;
    }

    public Long getNumeroEdicao() {
	return numeroEdicao;
    }

    public void setNumeroEdicao(Long numeroEdicao) {
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
}