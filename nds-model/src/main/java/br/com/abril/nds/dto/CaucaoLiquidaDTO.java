package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * DTO com as informações de caucao líquida
 * 
 * @author francisco.garcia
 **/
public class CaucaoLiquidaDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private BigDecimal valor;
	
	private Date atualizacao;

    public CaucaoLiquidaDTO(BigDecimal valor, Date atualizacao) {
        this.valor = valor;
        this.atualizacao = atualizacao;
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
     * @return the atualizacao
     */
    public Date getAtualizacao() {
        return atualizacao;
    }

    /**
     * @param atualizacao the atualizacao to set
     */
    public void setAtualizacao(Date atualizacao) {
        this.atualizacao = atualizacao;
    }

}
