package br.com.abril.nds.model.titularidade;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import br.com.abril.nds.model.cadastro.DescricaoTipoEntrega;

/**
 * Representa as informações de distribuição da cota no histórico de
 * titularidade
 * 
 * @author francisco.garcia
 * 
 */
@Embeddable
public class HistoricoTitularidadeCotaDistribuicao implements Serializable {
    
    private static final long serialVersionUID = 1L;

    /**
     * Quantidade de pdvs
     */
    @Column(name = "DISTRIBUICAO_QTDE_PDV")
    private Integer qtdePDV;
    
    /**
     * Nome do assistente comercial
     */
    @Column(name = "DISTRIBUICAO_ASSISTENTE_COMERCIAL")
    private String assistenteComercial;
    
    /**
     * Nome do gerente comercial
     */
    @Column(name = "DISTRIBUICAO_GERENTE_COMERCIAL")
    private String gerenteComercial;
    
    /**
     * Observação
     */
    @Column(name = "DISTRIBUICAO_OBSERVACAO")
    private String observacao;
    
    /**
     * Flag indicando se entrega reoparte de venda
     */
    @Column(name = "DISTRIBUICAO_ENTREGA_REPARTE_VENDA")
    private Boolean entregaReparteVenda;
    
    /**
     * Flag indicando se solicita números atrasados
     */
    @Column(name = "DISTRIBUICAO_SOLICITA_NUM_ATRASADOS")
    private Boolean solicitaNumAtrasados;
    
    /**
     * Flag indicando se recebe ou recolhe parciais
     */
    @Column(name = "DISTRIBUICAO_RECEBE_RECOLHE_PARCIAIS")
    private Boolean recebeRecolheParcias;
    
    /**
     * Flag indicando se a nota é impressa
     */
    @Column(name = "DISTRIBUICAO_NOTA_ENVIO_IMPRESSO")
    private Boolean notaEnvioImpresso;
    
    /**
     * Flag indicando se a nota é enviada por email
     */
    @Column(name = "DISTRIBUICAO_NOTA_ENVIO_EMAIL")
    private Boolean notaEnvioEmail;
    
    /**
     * Flag indicando se a chamada de encalhe é impressa
     */
    @Column(name = "DISTRIBUICAO_CHAMADA_ENCALHE_IMPRESSO")
    private Boolean chamadaEncalheImpresso;
    
    /**
     * Flag indicando se a chamada de encalhe é enviada por email
     */
    @Column(name = "DISTRIBUICAO_CHAMADA_ENCALHE_EMAIL")
    private Boolean chamadaEncalheEmail;
    
    /**
     * Flag indicando se o slip é impresso
     */
    @Column(name = "DISTRIBUICAO_SLIP_IMPRESSO")
    private Boolean slipImpresso;
    
    /**
     * Flag indicando se o slip é enviado por email
     */
    @Column(name = "DISTRIBUICAO_SLIP_EMAIL")
    private Boolean slipEmail;
    
    /**
     * Flag indicando se o boleto é impresso
     */
    @Column(name = "DISTRIBUICAO_BOLETO_IMPRESSO")
    private Boolean boletoImpresso;
    
    /**
     * Flag indicando se o boleto é enviado por email
     */
    @Column(name = "DISTRIBUICAO_BOLETO_EMAIL")
    private Boolean boletoEmail;
    
    /**
     * Flag indicando se o boleto + slip é impresso
     */
    @Column(name = "DISTRIBUICAO_BOLETO_SLIP_IMPRESSO")
    private Boolean boletoSlipImpresso;
    
    /**
     * Flag indicando se o boleto + slip é enviado por email
     */
    @Column(name = "DISTRIBUICAO_BOLETO_SLIP_EMAIL")
    private Boolean boletoSlipEmail;
    
    /**
     * Flag indicando se o recibo é impresso
     */
    @Column(name = "DISTRIBUICAO_RECIBO_IMPRESSO")
    private Boolean reciboImpresso;
    
    /**
     * Flag indicando se o recibo é enviado por email
     */
    @Column(name = "DISTRIBUICAO_RECIBO_EMAIL")
    private Boolean reciboEmail;
    
    /**
     * Flag indicando se utiliza procuração
     */
    @Column(name = "DISTRIBUICAO_PROCURACAO_ASSINADA")
    private Boolean procuracaoAssinada;
    
    /**
     * Flag indicando se possui procuração
     */
    @Column(name = "DISTRIBUICAO_POSSUI_PROCURACAO")
    private Boolean possuiProcuracao;
    
    /**
     * Flag indicando se possui termo de adesão
     */
    @Column(name = "DISTRIBUICAO_POSSUI_TERMO_ADESAO")
    private Boolean possuiTermoAdesao;
    
    /**
     * Flag indicando se termo de adesão está assinado
     */
    @Column(name = "DISTRIBUICAO_TERMO_ADESAO_ASSINADO")
    private Boolean termoAdesaoAssinado;
    
    /**
     * Tipo de entrega utilizada
     */
    @Column(name = "DISTRIBUICAO_TIPO_ENTREGA")
    @Enumerated(EnumType.STRING)
    private DescricaoTipoEntrega tipoEntrega;
    
    /**
     * Taxa fica de entrega
     */
    @Column(name = "DISTRIBUICAO_TAXA_FIXA_ENTREGA")
    private BigDecimal taxaFixaEntrega;
    
    /**
     * Percentual do faturamento que corresponde a entrega
     */
    @Column(name = "DISTRIBUICAO_PERCENTUAL_FATURAMENTO_ENTREGA")
    private BigDecimal percentualFaturamentoEntrega;
    
   
    /**
     * Início do período de carencia da entrega
     */
    @Column(name = "DISTRIBUICAO_INICIO_PERIODO_CARENCIA")
    @Temporal(TemporalType.DATE)
    private Date inicioPeriodoCarencia;
    
    /**
     * Fim do período de carência da entrega
     */
    @Column(name = "DISTRIBUICAO_FIM_PERIODO_CARENCIA")
    @Temporal(TemporalType.DATE)
    private Date fimPeriodoCarencia;
    
    @Transient
    private HistoricoTitularidadeCota historicoTitularidadeCota;

    /**
     * @return the qtdePDV
     */
    public Integer getQtdePDV() {
        return qtdePDV;
    }

    /**
     * @param qtdePDV the qtdePDV to set
     */
    public void setQtdePDV(Integer qtdePDV) {
        this.qtdePDV = qtdePDV;
    }

    /**
     * @return the assistenteComercial
     */
    public String getAssistenteComercial() {
        return assistenteComercial;
    }

    /**
     * @param assistenteComercial the assistenteComercial to set
     */
    public void setAssistenteComercial(String assistenteComercial) {
        this.assistenteComercial = assistenteComercial;
    }

    /**
     * @return the gerenteComercial
     */
    public String getGerenteComercial() {
        return gerenteComercial;
    }

    /**
     * @param gerenteComercial the gerenteComercial to set
     */
    public void setGerenteComercial(String gerenteComercial) {
        this.gerenteComercial = gerenteComercial;
    }

    /**
     * @return the observacao
     */
    public String getObservacao() {
        return observacao;
    }

    /**
     * @param observacao the observacao to set
     */
    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    /**
     * @return the entregaReparteVenda
     */
    public Boolean getEntregaReparteVenda() {
        return entregaReparteVenda;
    }

    /**
     * @param entregaReparteVenda the entregaReparteVenda to set
     */
    public void setEntregaReparteVenda(Boolean entregaReparteVenda) {
        this.entregaReparteVenda = entregaReparteVenda;
    }

    /**
     * @return the solicitaNumAtrasados
     */
    public Boolean getSolicitaNumAtrasados() {
        return solicitaNumAtrasados;
    }

    /**
     * @param solicitaNumAtrasados the solicitaNumAtrasados to set
     */
    public void setSolicitaNumAtrasados(Boolean solicitaNumAtrasados) {
        this.solicitaNumAtrasados = solicitaNumAtrasados;
    }

    /**
     * @return the recebeRecolheParcias
     */
    public Boolean getRecebeRecolheParcias() {
        return recebeRecolheParcias;
    }

    /**
     * @param recebeRecolheParcias the recebeRecolheParcias to set
     */
    public void setRecebeRecolheParcias(Boolean recebeRecolheParcias) {
        this.recebeRecolheParcias = recebeRecolheParcias;
    }

    /**
     * @return the notaEnvioImpresso
     */
    public Boolean getNotaEnvioImpresso() {
        return notaEnvioImpresso;
    }

    /**
     * @param notaEnvioImpresso the notaEnvioImpresso to set
     */
    public void setNotaEnvioImpresso(Boolean notaEnvioImpresso) {
        this.notaEnvioImpresso = notaEnvioImpresso;
    }

    /**
     * @return the notaEnvioEmail
     */
    public Boolean getNotaEnvioEmail() {
        return notaEnvioEmail;
    }

    /**
     * @param notaEnvioEmail the notaEnvioEmail to set
     */
    public void setNotaEnvioEmail(Boolean notaEnvioEmail) {
        this.notaEnvioEmail = notaEnvioEmail;
    }

    /**
     * @return the chamadaEncalheImpresso
     */
    public Boolean getChamadaEncalheImpresso() {
        return chamadaEncalheImpresso;
    }

    /**
     * @param chamadaEncalheImpresso the chamadaEncalheImpresso to set
     */
    public void setChamadaEncalheImpresso(Boolean chamadaEncalheImpresso) {
        this.chamadaEncalheImpresso = chamadaEncalheImpresso;
    }

    /**
     * @return the chamadaEncalheEmail
     */
    public Boolean getChamadaEncalheEmail() {
        return chamadaEncalheEmail;
    }

    /**
     * @param chamadaEncalheEmail the chamadaEncalheEmail to set
     */
    public void setChamadaEncalheEmail(Boolean chamadaEncalheEmail) {
        this.chamadaEncalheEmail = chamadaEncalheEmail;
    }

    /**
     * @return the slipImpresso
     */
    public Boolean getSlipImpresso() {
        return slipImpresso;
    }

    /**
     * @param slipImpresso the slipImpresso to set
     */
    public void setSlipImpresso(Boolean slipImpresso) {
        this.slipImpresso = slipImpresso;
    }

    /**
     * @return the slipEmail
     */
    public Boolean getSlipEmail() {
        return slipEmail;
    }

    /**
     * @param slipEmail the slipEmail to set
     */
    public void setSlipEmail(Boolean slipEmail) {
        this.slipEmail = slipEmail;
    }

    /**
     * @return the boletoImpresso
     */
    public Boolean getBoletoImpresso() {
        return boletoImpresso;
    }

    /**
     * @param boletoImpresso the boletoImpresso to set
     */
    public void setBoletoImpresso(Boolean boletoImpresso) {
        this.boletoImpresso = boletoImpresso;
    }

    /**
     * @return the boletoEmail
     */
    public Boolean getBoletoEmail() {
        return boletoEmail;
    }

    /**
     * @param boletoEmail the boletoEmail to set
     */
    public void setBoletoEmail(Boolean boletoEmail) {
        this.boletoEmail = boletoEmail;
    }

    /**
     * @return the boletoSlipImpresso
     */
    public Boolean getBoletoSlipImpresso() {
        return boletoSlipImpresso;
    }

    /**
     * @param boletoSlipImpresso the boletoSlipImpresso to set
     */
    public void setBoletoSlipImpresso(Boolean boletoSlipImpresso) {
        this.boletoSlipImpresso = boletoSlipImpresso;
    }

    /**
     * @return the boletoSlipEmail
     */
    public Boolean getBoletoSlipEmail() {
        return boletoSlipEmail;
    }

    /**
     * @param boletoSlipEmail the boletoSlipEmail to set
     */
    public void setBoletoSlipEmail(Boolean boletoSlipEmail) {
        this.boletoSlipEmail = boletoSlipEmail;
    }

    /**
     * @return the reciboImpresso
     */
    public Boolean getReciboImpresso() {
        return reciboImpresso;
    }

    /**
     * @param reciboImpresso the reciboImpresso to set
     */
    public void setReciboImpresso(Boolean reciboImpresso) {
        this.reciboImpresso = reciboImpresso;
    }

    /**
     * @return the reciboEmail
     */
    public Boolean getReciboEmail() {
        return reciboEmail;
    }

    /**
     * @param reciboEmail the reciboEmail to set
     */
    public void setReciboEmail(Boolean reciboEmail) {
        this.reciboEmail = reciboEmail;
    }

    /**
     * @return the procuracaoAssinada
     */
    public Boolean getProcuracaoAssinada() {
        return procuracaoAssinada;
    }

    /**
     * @param procuracaoAssinada the procuracaoAssinada to set
     */
    public void setProcuracaoAssinada(Boolean procuracaoAssinada) {
        this.procuracaoAssinada = procuracaoAssinada;
    }
    
    /**
     * @return the possuiProcuracao
     */
    public Boolean getPossuiProcuracao() {
        return possuiProcuracao;
    }

    /**
     * @param possuiProcuracao the possuiProcuracao to set
     */
    public void setPossuiProcuracao(Boolean possuiProcuracao) {
        this.possuiProcuracao = possuiProcuracao;
    }

    /**
     * @return the possuiTermoAdesao
     */
    public Boolean getPossuiTermoAdesao() {
        return possuiTermoAdesao;
    }

    /**
     * @param possuiTermoAdesao the possuiTermoAdesao to set
     */
    public void setPossuiTermoAdesao(Boolean possuiTermoAdesao) {
        this.possuiTermoAdesao = possuiTermoAdesao;
    }

    /**
     * @return the termoAdesaoAssinado
     */
    public Boolean getTermoAdesaoAssinado() {
        return termoAdesaoAssinado;
    }

    /**
     * @param termoAdesaoAssinado the termoAdesaoAssinado to set
     */
    public void setTermoAdesaoAssinado(Boolean termoAdesaoAssinado) {
        this.termoAdesaoAssinado = termoAdesaoAssinado;
    }

    /**
     * @return the tipoEntrega
     */
    public DescricaoTipoEntrega getTipoEntrega() {
        return tipoEntrega;
    }

    /**
     * @param tipoEntrega the tipoEntrega to set
     */
    public void setTipoEntrega(DescricaoTipoEntrega tipoEntrega) {
        this.tipoEntrega = tipoEntrega;
    }

    /**
     * @return the taxaFixaEntrega
     */
    public BigDecimal getTaxaFixaEntrega() {
        return taxaFixaEntrega;
    }

    /**
     * @param taxaFixaEntrega the taxaFixaEntrega to set
     */
    public void setTaxaFixaEntrega(BigDecimal taxaFixaEntrega) {
        this.taxaFixaEntrega = taxaFixaEntrega;
    }

    /**
     * @return the percentualFaturamentoEntrega
     */
    public BigDecimal getPercentualFaturamentoEntrega() {
        return percentualFaturamentoEntrega;
    }

    /**
     * @param percentualFaturamentoEntrega the percentualFaturamentoEntrega to set
     */
    public void setPercentualFaturamentoEntrega(
            BigDecimal percentualFaturamentoEntrega) {
        this.percentualFaturamentoEntrega = percentualFaturamentoEntrega;
    }


    /**
     * @return the inicioPeriodoCarencia
     */
    public Date getInicioPeriodoCarencia() {
        return inicioPeriodoCarencia;
    }

    /**
     * @param inicioPeriodoCarencia the inicioPeriodoCarencia to set
     */
    public void setInicioPeriodoCarencia(Date inicioPeriodoCarencia) {
        this.inicioPeriodoCarencia = inicioPeriodoCarencia;
    }

    /**
     * @return the fimPeriodoCarencia
     */
    public Date getFimPeriodoCarencia() {
        return fimPeriodoCarencia;
    }

    /**
     * @param fimPeriodoCarencia the fimPeriodoCarencia to set
     */
    public void setFimPeriodoCarencia(Date fimPeriodoCarencia) {
        this.fimPeriodoCarencia = fimPeriodoCarencia;
    }

    /**
     * @return the historicoTitularidadeCota
     */
    public HistoricoTitularidadeCota getHistoricoTitularidadeCota() {
        return historicoTitularidadeCota;
    }

    /**
     * @param historicoTitularidadeCota the historicoTitularidadeCota to set
     */
    public void setHistoricoTitularidadeCota(
            HistoricoTitularidadeCota historicoTitularidadeCota) {
        this.historicoTitularidadeCota = historicoTitularidadeCota;
    }
    
}
