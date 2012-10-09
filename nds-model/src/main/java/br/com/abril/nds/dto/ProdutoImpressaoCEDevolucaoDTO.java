package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Produto na impressão CE Devolução
 * 
 * @author francisco.garcia
 *
 */
public class ProdutoImpressaoCEDevolucaoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String codigo;
    
    private String produto;
    
    private Long edicao;
    
    private Integer sequenciaMatriz;
    
    private BigDecimal desconto;
    
    private Date dataLancamento;
    
    private List<Long> notasEnvio;
    
    private String tipoRecolhimento;
    
    private BigInteger reparte;
    
    private BigInteger devolucao;
    
    private BigInteger venda;
    
    private BigDecimal precoDesconto;
    
    private BigDecimal valorVenda;

    /**
     * @return the codigo
     */
    public String getCodigo() {
        return codigo;
    }

    /**
     * @param codigo the codigo to set
     */
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    /**
     * @return the produto
     */
    public String getProduto() {
        return produto;
    }

    /**
     * @param produto the produto to set
     */
    public void setProduto(String produto) {
        this.produto = produto;
    }

    /**
     * @return the edicao
     */
    public Long getEdicao() {
        return edicao;
    }

    /**
     * @param edicao the edicao to set
     */
    public void setEdicao(Long edicao) {
        this.edicao = edicao;
    }

    /**
     * @return the sequenciaMatriz
     */
    public Integer getSequenciaMatriz() {
        return sequenciaMatriz;
    }

    /**
     * @param sequenciaMatriz the sequenciaMatriz to set
     */
    public void setSequenciaMatriz(Integer sequenciaMatriz) {
        this.sequenciaMatriz = sequenciaMatriz;
    }

    /**
     * @return the desconto
     */
    public BigDecimal getDesconto() {
        return desconto;
    }

    /**
     * @param desconto the desconto to set
     */
    public void setDesconto(BigDecimal desconto) {
        this.desconto = desconto;
    }

    /**
     * @return the dataLancamento
     */
    public Date getDataLancamento() {
        return dataLancamento;
    }

    /**
     * @param dataLancamento the dataLancamento to set
     */
    public void setDataLancamento(Date dataLancamento) {
        this.dataLancamento = dataLancamento;
    }

    /**
     * @return the notasEnvio
     */
    public List<Long> getNotasEnvio() {
        return notasEnvio;
    }

    /**
     * @param notasEnvio the notasEnvio to set
     */
    public void setNotasEnvio(List<Long> notasEnvio) {
        this.notasEnvio = notasEnvio;
    }

    /**
     * @return the tipoRecolhimento
     */
    public String getTipoRecolhimento() {
        return tipoRecolhimento;
    }

    /**
     * @param tipoRecolhimento the tipoRecolhimento to set
     */
    public void setTipoRecolhimento(String tipoRecolhimento) {
        this.tipoRecolhimento = tipoRecolhimento;
    }

    /**
     * @return the reparte
     */
    public BigInteger getReparte() {
        return reparte;
    }

    /**
     * @param reparte the reparte to set
     */
    public void setReparte(BigInteger reparte) {
        this.reparte = reparte;
    }

    /**
     * @return the devolucao
     */
    public BigInteger getDevolucao() {
        return devolucao;
    }

    /**
     * @param devolucao the devolucao to set
     */
    public void setDevolucao(BigInteger devolucao) {
        this.devolucao = devolucao;
    }

    /**
     * @return the venda
     */
    public BigInteger getVenda() {
        return venda;
    }

    /**
     * @param venda the venda to set
     */
    public void setVenda(BigInteger venda) {
        this.venda = venda;
    }

    /**
     * @return the precoDesconto
     */
    public BigDecimal getPrecoDesconto() {
        return precoDesconto;
    }

    /**
     * @param precoDesconto the precoDesconto to set
     */
    public void setPrecoDesconto(BigDecimal precoDesconto) {
        this.precoDesconto = precoDesconto;
    }

    /**
     * @return the valorVenda
     */
    public BigDecimal getValorVenda() {
        return valorVenda;
    }

    /**
     * @param valorVenda the valorVenda to set
     */
    public void setValorVenda(BigDecimal valorVenda) {
        this.valorVenda = valorVenda;
    }
    
    public void addNotaEnvio(Long notaEnvio) {
        if (notasEnvio == null) {
            notasEnvio = new ArrayList<Long>();
        }
        notasEnvio.add(notaEnvio);
    }
    
}