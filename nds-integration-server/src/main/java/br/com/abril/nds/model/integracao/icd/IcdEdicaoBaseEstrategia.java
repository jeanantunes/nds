package br.com.abril.nds.model.integracao.icd;

import java.io.Serializable;
import java.math.BigDecimal;

public class IcdEdicaoBaseEstrategia implements Serializable {

	private static final long serialVersionUID = -2752476981818369215L;

    private String codigoPublicacao;
    private Long numeroEdicao;
    private Integer periodo;
    private Long peso;

    public String getCodigoPublicacao() {
        return codigoPublicacao;
    }

    public void setCodigoPublicacao(String codigoPublicacao) {
        this.codigoPublicacao = codigoPublicacao;
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

    public Long getPeso() {
        return peso;
    }

    public void setPeso(Long peso) {
        this.peso = peso;
    }

    @Override
    public String toString() {
        return "IcdEdicaoBaseEstrategia{" +
                "codigoPublicacao='" + codigoPublicacao + '\'' +
                ", numeroEdicao=" + numeroEdicao +
                ", periodo=" + periodo +
                ", peso=" + peso +
                '}';
    }
}
