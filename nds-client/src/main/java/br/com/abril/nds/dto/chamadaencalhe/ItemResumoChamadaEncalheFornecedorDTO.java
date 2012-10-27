package br.com.abril.nds.dto.chamadaencalhe;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


/**
 * DTO com as informações dos Itens do Resumo da(s) Chamada(s) de Encalhe do
 * Fornecedor para recolhimento dos produtos do Distribuidor
 * 
 * @author francisco.garcia
 * 
 */
public class ItemResumoChamadaEncalheFornecedorDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private Integer linha;
    
    private Long numeroDocumento;
    
    private Date dataVencimento;
    
    private BigDecimal valorDocumento = BigDecimal.ZERO;
    
    private BigDecimal valorMargem = BigDecimal.ZERO;

    /**
     * @return the linha
     */
    public Integer getLinha() {
        return linha;
    }

    /**
     * @param linha the linha to set
     */
    public void setLinha(Integer linha) {
        this.linha = linha;
    }

    /**
     * @return the numeroDocumento
     */
    public Long getNumeroDocumento() {
        return numeroDocumento;
    }

    /**
     * @param numeroDocumento the numeroDocumento to set
     */
    public void setNumeroDocumento(Long numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }

    /**
     * @return the dataVencimento
     */
    public Date getDataVencimento() {
        return dataVencimento;
    }

    /**
     * @param dataVencimento the dataVencimento to set
     */
    public void setDataVencimento(Date dataVencimento) {
        this.dataVencimento = dataVencimento;
    }

    /**
     * @return the valorDocumento
     */
    public BigDecimal getValorDocumento() {
        return valorDocumento;
    }

    /**
     * @param valorDocumento the valorDocumento to set
     */
    public void setValorDocumento(BigDecimal valorDocumento) {
        this.valorDocumento = valorDocumento;
    }

    /**
     * @return the valorMargem
     */
    public BigDecimal getValorMargem() {
        return valorMargem;
    }

    /**
     * @param valorMargem the valorMargem to set
     */
    public void setValorMargem(BigDecimal valorMargem) {
        this.valorMargem = valorMargem;
    }

}
