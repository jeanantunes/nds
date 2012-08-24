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

import br.com.abril.nds.model.cadastro.BaseCalculo;

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
     * Observação
     */
    @Column(name = "DISTRIBUICAO_OBSERVACAO")
    private String observacao;
    
    /**
     * Flag indicando se é arrendatário 
     */
    @Column(name = "DISTRIBUICAO_ARRENDATARIO")
    private boolean arrendatario;
    
    /**
     * Flag indicando se o reparte é por ponto de venda
     */
    @Column(name = "DISTRIBUICAO_REPARTE_POR_PONTO_VENDA")
    private boolean repartePorPontoVenda;
    
    /**
     * Flag indicando se solicita números atrasados
     */
    @Column(name = "DISTRIBUICAO_SOLICITA_NUM_ATRASADOS")
    private boolean solicitaNumAtras;
    
    /**
     * Flag indicando se recebe ou recolhe parciais
     */
    @Column(name = "DISTRIBUICAO_RECEBE_RECOLHE_PARCIAIS")
    private boolean recebeRecolheParcias;
    
    /**
     * Flag indicando se a nota é impressa
     */
    @Column(name = "DISTRIBUICAO_NOTA_ENVIO_IMPRESSO")
    private boolean notaEnvioImpresso;
    
    /**
     * Flag indicando se a nota é enviada por email
     */
    @Column(name = "DISTRIBUICAO_NOTA_ENVIO_EMAIL")
    private boolean notaEnvioEmail;
    
    /**
     * Flag indicando se a chamada de encalhe é impressa
     */
    @Column(name = "DISTRIBUICAO_CHAMADA_ENCALHE_IMPRESSO")
    private boolean chamadaEncalheImpresso;
    
    /**
     * Flag indicando se a chamada de encalhe é enviada por email
     */
    @Column(name = "DISTRIBUICAO_CHAMADA_ENCALHE_EMAIL")
    private boolean chamadaEncalheEmail;
    
    /**
     * Flag indicando se o slip é impresso
     */
    @Column(name = "DISTRIBUICAO_SLIP_IMPRESSO")
    private boolean slipImpresso;
    
    /**
     * Flag indicando se o slip é enviado por email
     */
    @Column(name = "DISTRIBUICAO_SLIP_EMAIL")
    private boolean slipEmail;
    
    /**
     * Flag indicando se utiliza procuração
     */
    @Column(name = "DISTRIBUICAO_PROCURACAO_ASSINADA")
    private boolean procuracaoAssinada;
    
    /**
     * Tipo de entrega utilizada
     */
    @Column(name = "DISTRIBUICAO_TIPO_ENTREGA")
    private String tipoEntrega;
    
    /**
     * Taxa fica de entrega
     */
    @Column(name = "DISTRIBUICAO_TAXA_FIXA_ENTREGA")
    private BigDecimal taxaFixaEntrega;
    
    /**
     * Percentual do faturamento que corresponde a entrega
     */
    @Column(name = "DISTRIBUICAO_PERCENTUAL_FATURAMENTO_ENTREGA")
    private Float percentualFaturamentoEntrega;
    
    /**
     * Base de cálculo da entrega
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "DISTRIBUICAO_BASE_CALCULO_ENTREGA")
    private BaseCalculo baseCalculoEntrega;
    
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
     * @return the arrendatario
     */
    public boolean isArrendatario() {
        return arrendatario;
    }

    /**
     * @param arrendatario the arrendatario to set
     */
    public void setArrendatario(boolean arrendatario) {
        this.arrendatario = arrendatario;
    }

    /**
     * @return the repartePorPontoVenda
     */
    public boolean isRepartePorPontoVenda() {
        return repartePorPontoVenda;
    }

    /**
     * @param repartePorPontoVenda the repartePorPontoVenda to set
     */
    public void setRepartePorPontoVenda(boolean repartePorPontoVenda) {
        this.repartePorPontoVenda = repartePorPontoVenda;
    }

    /**
     * @return the solicitaNumAtras
     */
    public boolean isSolicitaNumAtras() {
        return solicitaNumAtras;
    }

    /**
     * @param solicitaNumAtras the solicitaNumAtras to set
     */
    public void setSolicitaNumAtras(boolean solicitaNumAtras) {
        this.solicitaNumAtras = solicitaNumAtras;
    }

    /**
     * @return the recebeRecolheParcias
     */
    public boolean isRecebeRecolheParcias() {
        return recebeRecolheParcias;
    }

    /**
     * @param recebeRecolheParcias the recebeRecolheParcias to set
     */
    public void setRecebeRecolheParcias(boolean recebeRecolheParcias) {
        this.recebeRecolheParcias = recebeRecolheParcias;
    }

    /**
     * @return the notaEnvioImpresso
     */
    public boolean isNotaEnvioImpresso() {
        return notaEnvioImpresso;
    }

    /**
     * @param notaEnvioImpresso the notaEnvioImpresso to set
     */
    public void setNotaEnvioImpresso(boolean notaEnvioImpresso) {
        this.notaEnvioImpresso = notaEnvioImpresso;
    }

    /**
     * @return the notaEnvioEmail
     */
    public boolean isNotaEnvioEmail() {
        return notaEnvioEmail;
    }

    /**
     * @param notaEnvioEmail the notaEnvioEmail to set
     */
    public void setNotaEnvioEmail(boolean notaEnvioEmail) {
        this.notaEnvioEmail = notaEnvioEmail;
    }

    /**
     * @return the chamadaEncalheImpresso
     */
    public boolean isChamadaEncalheImpresso() {
        return chamadaEncalheImpresso;
    }

    /**
     * @param chamadaEncalheImpresso the chamadaEncalheImpresso to set
     */
    public void setChamadaEncalheImpresso(boolean chamadaEncalheImpresso) {
        this.chamadaEncalheImpresso = chamadaEncalheImpresso;
    }

    /**
     * @return the chamadaEncalheEmail
     */
    public boolean isChamadaEncalheEmail() {
        return chamadaEncalheEmail;
    }

    /**
     * @param chamadaEncalheEmail the chamadaEncalheEmail to set
     */
    public void setChamadaEncalheEmail(boolean chamadaEncalheEmail) {
        this.chamadaEncalheEmail = chamadaEncalheEmail;
    }

    /**
     * @return the slipImpresso
     */
    public boolean isSlipImpresso() {
        return slipImpresso;
    }

    /**
     * @param slipImpresso the slipImpresso to set
     */
    public void setSlipImpresso(boolean slipImpresso) {
        this.slipImpresso = slipImpresso;
    }

    /**
     * @return the slipEmail
     */
    public boolean isSlipEmail() {
        return slipEmail;
    }

    /**
     * @param slipEmail the slipEmail to set
     */
    public void setSlipEmail(boolean slipEmail) {
        this.slipEmail = slipEmail;
    }

    /**
     * @return the procuracaoAssinada
     */
    public boolean isProcuracaoAssinada() {
        return procuracaoAssinada;
    }

    /**
     * @param procuracaoAssinada the procuracaoAssinada to set
     */
    public void setProcuracaoAssinada(boolean procuracaoAssinada) {
        this.procuracaoAssinada = procuracaoAssinada;
    }

    /**
     * @return the tipoEntrega
     */
    public String getTipoEntrega() {
        return tipoEntrega;
    }

    /**
     * @param tipoEntrega the tipoEntrega to set
     */
    public void setTipoEntrega(String tipoEntrega) {
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
    public Float getPercentualFaturamentoEntrega() {
        return percentualFaturamentoEntrega;
    }

    /**
     * @param percentualFaturamentoEntrega the percentualFaturamentoEntrega to set
     */
    public void setPercentualFaturamentoEntrega(Float percentualFaturamentoEntrega) {
        this.percentualFaturamentoEntrega = percentualFaturamentoEntrega;
    }

    /**
     * @return the baseCalculoEntrega
     */
    public BaseCalculo getBaseCalculoEntrega() {
        return baseCalculoEntrega;
    }

    /**
     * @param baseCalculoEntrega the baseCalculoEntrega to set
     */
    public void setBaseCalculoEntrega(BaseCalculo baseCalculoEntrega) {
        this.baseCalculoEntrega = baseCalculoEntrega;
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

}
