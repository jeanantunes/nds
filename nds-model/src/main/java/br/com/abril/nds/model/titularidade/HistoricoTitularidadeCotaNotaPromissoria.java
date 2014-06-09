package br.com.abril.nds.model.titularidade;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.com.abril.nds.model.cadastro.TipoGarantia;

/**
 * Representa a garantia do tipo "NOTA PROMISSORIA" no histórico de titularidade
 * da cota
 * 
 * @author francisco.garcia
 * 
 */
@Entity
@DiscriminatorValue("NOTA_PROMISSORIA")
public class HistoricoTitularidadeCotaNotaPromissoria extends
        HistoricoTitularidadeCotaGarantia {

    private static final long serialVersionUID = 1L;

    /**
     * Vencimento da nota promissória
     */
    @Temporal(TemporalType.DATE)
    @Column(name = "NOTA_PROMISSORIA_VENCIMENTO")
    private Date vencimento;

    /**
     * Valor da nota promissória
     */
    @Column(name = "NOTA_PROMISSORIA_VALOR")
    private BigDecimal valor;

    /**
     * Valor da nota promissória por extenso
     */
    @Column(name = "NOTA_PROMISSORIA_VALOR_EXTENSO")
    private String valorExtenso;
    
    public HistoricoTitularidadeCotaNotaPromissoria() {
        this.tipoGarantia = TipoGarantia.NOTA_PROMISSORIA;
    }

    /**
     * @return the vencimento
     */
    public Date getVencimento() {
        return vencimento;
    }

    /**
     * @param vencimento
     *            the vencimento to set
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
     * @param valor
     *            the valor to set
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
     * @param valorExtenso
     *            the valorExtenso to set
     */
    public void setValorExtenso(String valorExtenso) {
        this.valorExtenso = valorExtenso;
    }

}
