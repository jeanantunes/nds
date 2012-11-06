package br.com.abril.nds.dto.fechamentodiario;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import br.com.abril.nds.model.cadastro.TipoCobranca;
import br.com.abril.nds.util.CurrencyUtil;

/**
 * DTO com informações de sumarização das dívidas para fechamento diário
 * 
 * @author francisco.garcia
 *
 */
public class SumarizacaoDividasDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private Date data;
    
    private TipoDivida tipoSumarizacao; 
    
    private TipoCobranca tipoCobranca;
    
    private BigDecimal total = BigDecimal.ZERO;
    
    private BigDecimal valorPago = BigDecimal.ZERO;
    
    private BigDecimal inadimplencia = BigDecimal.ZERO;

    
    public SumarizacaoDividasDTO(Date data, TipoDivida tipoSumarizacao, TipoCobranca tipoCobranca) {
        this.data = data;
        this.tipoSumarizacao = tipoSumarizacao;
        this.tipoCobranca = tipoCobranca;
    }

    public SumarizacaoDividasDTO(Date data, TipoDivida tipoSumarizacao, TipoCobranca tipoCobranca, BigDecimal total, BigDecimal valorPago,
            BigDecimal inadimplencia) {
        this(data, tipoSumarizacao, tipoCobranca);
        this.total = total;
        this.valorPago = valorPago;
        this.inadimplencia = inadimplencia;
    }
    
    /**
     * @return the data
     */
    public Date getData() {
        return data;
    }

    /**
     * @param data the data to set
     */
    public void setData(Date data) {
        this.data = data;
    }


    /**
     * @return the tipoSumarizacao
     */
    public TipoDivida getTipoSumarizacao() {
        return tipoSumarizacao;
    }


    /**
     * @param tipoSumarizacao the tipoSumarizacao to set
     */
    public void setTipoSumarizacao(TipoDivida tipoSumarizacao) {
        this.tipoSumarizacao = tipoSumarizacao;
    }

    /**
     * @return the tipoCobranca
     */
    public TipoCobranca getTipoCobranca() {
        return tipoCobranca;
    }


    /**
     * @param tipoCobranca the tipoCobranca to set
     */
    public void setTipoCobranca(TipoCobranca tipoCobranca) {
        this.tipoCobranca = tipoCobranca;
    }


    /**
     * @return the total
     */
    public BigDecimal getTotal() {
        return total;
    }

    /**
     * @param total the total to set
     */
    public void setTotal(BigDecimal total) {
        this.total = total;
    }


    /**
     * @return the valorPago
     */
    public BigDecimal getValorPago() {
        return valorPago;
    }


    /**
     * @param valorPago the valorPago to set
     */
    public void setValorPago(BigDecimal valorPago) {
        this.valorPago = valorPago;
    }


    /**
     * @return the inadimplencia
     */
    public BigDecimal getInadimplencia() {
        return inadimplencia;
    }


    /**
     * @param inadimplencia the inadimplencia to set
     */
    public void setInadimplencia(BigDecimal inadimplencia) {
        this.inadimplencia = inadimplencia;
    }
    
    /**
     * 
     * @return texto descritivo do tipo de cobrança
     */
    public String getDescricaoTipoCobranca() {
        return tipoCobranca.getDescricao();
    }
    
    /**
     * @return Valor total formatado
     */
    public String getTotalFormatado() {
        return CurrencyUtil.formatarValor(total);
    }
    
    /**
     * @return Valor pago formatado
     */
    public String getValorPagoFormatado() {
        return CurrencyUtil.formatarValor(valorPago);
    }
    
    /**
     * 
     * @return Valor Inadimplencia formatado
     */
    public String getInadimplenciaFormatado() {
        return CurrencyUtil.formatarValor(inadimplencia);
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((data == null) ? 0 : data.hashCode());
        result = prime * result
                + ((tipoCobranca == null) ? 0 : tipoCobranca.hashCode());
        result = prime * result
                + ((tipoSumarizacao == null) ? 0 : tipoSumarizacao.hashCode());
        return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        SumarizacaoDividasDTO other = (SumarizacaoDividasDTO) obj;
        if (data == null) {
            if (other.data != null) {
                return false;
            }
        } else if (!data.equals(other.data)) {
            return false;
        }
        if (tipoCobranca != other.tipoCobranca) {
            return false;
        }
        if (tipoSumarizacao != other.tipoSumarizacao) {
            return false;
        }
        return true;
    }

}
