package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * DTO com as informações da garantia tipo "OUTROS"
 * 
 * @author francisco.garcia
 *
 */
public class GarantiaOutrosDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String descricao;
	
	private BigDecimal valor;
	
	private Date validade;
	
    public GarantiaOutrosDTO(String descricao, BigDecimal valor, Date validade) {
        this.descricao = descricao;
        this.valor = valor;
        this.validade = validade;
    }

    /**
     * @return the descricao
     */
    public String getDescricao() {
        return descricao;
    }

    /**
     * @param descricao the descricao to set
     */
    public void setDescricao(String descricao) {
        this.descricao = descricao;
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
     * @return the validade
     */
    public Date getValidade() {
        return validade;
    }

    /**
     * @param validade the validade to set
     */
    public void setValidade(Date validade) {
        this.validade = validade;
    }
	
}
