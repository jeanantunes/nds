package br.com.abril.nds.dto.chamadaencalhe;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * DTO com as informações do Item da Chamada de Encalhe do Fornecedor para
 * recolhimento dos produtos do Distribuidor
 * 
 * @author francisco.garcia
 * 
 */
public class ItemChamadaEncalheFornecedorDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private Long editor;
    
    private String nome;
    
    private String descricao;
    
    private Long codigo;
    
    private Long edicao;
    
    private String formaDevolucao;
    
    private Date dataRecolhimento;
    
    private Long notaEnvio;
    
    private Integer item;
    
    private String tipoRecolhimento;
    
    private Long qtdeEnviada;
    
    private Long qtdeDevolvida;
    
    private Long qtdeVenda;
    
    private Long qtdeRecebida;
    
    private BigDecimal precoCapa;
    
    private BigDecimal valorVenda;
    
    private Integer pacotePadrao;

    /**
     * @return the editor
     */
    public Long getEditor() {
        return editor;
    }

    /**
     * @param editor the editor to set
     */
    public void setEditor(Long editor) {
        this.editor = editor;
    }

    /**
     * @return the nome
     */
    public String getNome() {
        return nome;
    }

    /**
     * @param nome the nome to set
     */
    public void setNome(String nome) {
        this.nome = nome;
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
     * @return the codigo
     */
    public Long getCodigo() {
        return codigo;
    }

    /**
     * @param codigo the codigo to set
     */
    public void setCodigo(Long codigo) {
        this.codigo = codigo;
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
     * @return the formaDevolucao
     */
    public String getFormaDevolucao() {
        return formaDevolucao;
    }

    /**
     * @param formaDevolucao the formaDevolucao to set
     */
    public void setFormaDevolucao(String formaDevolucao) {
        this.formaDevolucao = formaDevolucao;
    }

    /**
     * @return the dataRecolhimento
     */
    public Date getDataRecolhimento() {
        return dataRecolhimento;
    }

    /**
     * @param dataRecolhimento the dataRecolhimento to set
     */
    public void setDataRecolhimento(Date dataRecolhimento) {
        this.dataRecolhimento = dataRecolhimento;
    }

    /**
     * @return the notaEnvio
     */
    public Long getNotaEnvio() {
        return notaEnvio;
    }

    /**
     * @param notaEnvio the notaEnvio to set
     */
    public void setNotaEnvio(Long notaEnvio) {
        this.notaEnvio = notaEnvio;
    }

    /**
     * @return the item
     */
    public Integer getItem() {
        return item;
    }

    /**
     * @param item the item to set
     */
    public void setItem(Integer item) {
        this.item = item;
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
     * @return the qtdeEnviada
     */
    public Long getQtdeEnviada() {
        return qtdeEnviada;
    }

    /**
     * @param qtdeEnviada the qtdeEnviada to set
     */
    public void setQtdeEnviada(Long qtdeEnviada) {
        this.qtdeEnviada = qtdeEnviada;
    }

    /**
     * @return the qtdeDevolvida
     */
    public Long getQtdeDevolvida() {
        return qtdeDevolvida;
    }

    /**
     * @param qtdeDevolvida the qtdeDevolvida to set
     */
    public void setQtdeDevolvida(Long qtdeDevolvida) {
        this.qtdeDevolvida = qtdeDevolvida;
    }

    /**
     * @return the qtdeVenda
     */
    public Long getQtdeVenda() {
        return qtdeVenda;
    }

    /**
     * @param qtdeVenda the qtdeVenda to set
     */
    public void setQtdeVenda(Long qtdeVenda) {
        this.qtdeVenda = qtdeVenda;
    }

    /**
     * @return the qtdeRecebida
     */
    public Long getQtdeRecebida() {
        return qtdeRecebida;
    }

    /**
     * @param qtdeRecebida the qtdeRecebida to set
     */
    public void setQtdeRecebida(Long qtdeRecebida) {
        this.qtdeRecebida = qtdeRecebida;
    }

    /**
     * @return the precoCapa
     */
    public BigDecimal getPrecoCapa() {
        return precoCapa;
    }

    /**
     * @param precoCapa the precoCapa to set
     */
    public void setPrecoCapa(BigDecimal precoCapa) {
        this.precoCapa = precoCapa;
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

    /**
     * @return the pacotePadrao
     */
    public Integer getPacotePadrao() {
        return pacotePadrao;
    }

    /**
     * @param pacotePadrao the pacotePadrao to set
     */
    public void setPacotePadrao(Integer pacotePadrao) {
        this.pacotePadrao = pacotePadrao;
    }

}
