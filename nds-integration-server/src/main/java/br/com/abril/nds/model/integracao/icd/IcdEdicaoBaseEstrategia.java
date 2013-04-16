package br.com.abril.nds.model.integracao.icd;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class IcdEdicaoBaseEstrategia implements Serializable {

	private static final long serialVersionUID = -2752476981818369215L;
	
	@Id
    private String codigoProduto;
	@Id
    private BigDecimal numeroEdicao;
    private BigDecimal periodo;
    private BigDecimal peso;
    @Id
    private BigDecimal estrategia;
    
    public String getCodigoProduto() {
        return codigoProduto;
    }
    public void setCodigoProduto(String codigoProduto) {
        this.codigoProduto = codigoProduto;
    }
    public BigDecimal getNumeroEdicao() {
        return numeroEdicao;
    }
    public void setNumeroEdicao(BigDecimal numeroEdicao) {
        this.numeroEdicao = numeroEdicao;
    }
    public BigDecimal getPeriodo() {
        return periodo;
    }
    public void setPeriodo(BigDecimal periodo) {
        this.periodo = periodo;
    }
    public BigDecimal getPeso() {
        return peso;
    }
    public void setPeso(BigDecimal peso) {
        this.peso = peso;
    }
	public BigDecimal getEstrategia() {
		return estrategia;
	}
	public void setEstrategia(BigDecimal estrategia) {
		this.estrategia = estrategia;
	}
}
