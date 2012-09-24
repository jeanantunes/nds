package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * DTO com as informações da nota promissória utilizada como
 * garantia
 * 
 * @author francisco.garcia
 *
 */
public class GarantiaNotaPromissoriaDTO implements Serializable {
   
    private static final long serialVersionUID = 1L;
    
    private Date vencimento;

    private BigDecimal valor;

    private String valorExtenso;
    
    public GarantiaNotaPromissoriaDTO(Date vencimento, BigDecimal valor,
            String valorExtenso) {
        this.vencimento = vencimento;
        this.valor = valor;
        this.valorExtenso = valorExtenso;
    }

    /**
     * @return the vencimento
     */
    public Date getVencimento() {
        return vencimento;
    }

    /**
     * @param vencimento the vencimento to set
     */
    public void setVencimento(Date vencimento) {
        this.vencimento = vencimento;
    }

    /**
     * @return the valor
     */
    public BigDecimal getValor() {
        return valor;
    }

    /**
     * @param valor the valor to set
     */
    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    /**
     * @return the valorExtenso
     */
    public String getValorExtenso() {
        return valorExtenso;
    }

    /**
     * @param valorExtenso the valorExtenso to set
     */
    public void setValorExtenso(String valorExtenso) {
        this.valorExtenso = valorExtenso;
    }

}
