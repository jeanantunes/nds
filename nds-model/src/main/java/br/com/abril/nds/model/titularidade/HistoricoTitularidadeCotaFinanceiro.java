package br.com.abril.nds.model.titularidade;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.com.abril.nds.model.cadastro.PoliticaSuspensao;
import br.com.abril.nds.model.cadastro.TipoCota;

/**
 * Representa as informações financeiras no histórico de titularidade da cota
 * 
 * @author francisco.garcia
 * 
 */
@Entity
@Table(name = "HISTORICO_TITULARIDADE_COTA_FINANCEIRO")
@SequenceGenerator(name = "HIST_TIT_COTA_FINANCEIRO_SEQ", initialValue = 1, allocationSize = 1)
public class HistoricoTitularidadeCotaFinanceiro implements Serializable {
    
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "HIST_TIT_COTA_FINANCEIRO_SEQ")
    @Column(name = "ID")
    private Long id;
    
    /**
     * Fator de vencimento nas cobranças
     */
    @Column(name = "FATOR_VENCIMENTO")
    private int fatorVencimento;
    
    /**
     * Valor mínimo de cobrança
     */
    @Column(name = "VALOR_MINIMO_COBRANCA")
    private BigDecimal valorMininoCobranca;
    
    /**
     * Tipo de cota
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "TIPO_COTA")
    private TipoCota tipoCota;
    
    /**
     * Política de suspensão da cota
     */
    @Embedded
    private PoliticaSuspensao politicaSuspensao;
    
    /**
     * Formas de pagamento
     */
    @OneToMany(cascade = {CascadeType.ALL})
    @JoinColumn(name = "HISTORICO_TITULARIDADE_COTA_FINANCEIRO_ID")
    private Collection<HistoricoTitularidadeCotaFormaPagamento> formasPagamento;
    
    /**
     * Flag indicando se a cota possui contrato
     */
    @Column(name = "POSSUI_CONTRATO")
    private boolean possuiContrato;
    
    /**
     * Data de início do contrato
     */
    @Temporal(TemporalType.DATE)
    @Column(name = "DATA_INICIO_CONTRATO")
    private Date dataInicioContrato;
    
    /**
     * Data de término do contrato
     */
    @Temporal(TemporalType.DATE)
    @Column(name = "DATA_TERMINO_CONTRATO")
    private Date dataTerminoContrato;
    
    /**
     * Flag indicando se o contrato foi recebido
     */
    @Column(name = "CONTRATO_RECEBIDO")
    private boolean contratoRecebido;

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the fatorVencimento
     */
    public int getFatorVencimento() {
        return fatorVencimento;
    }

    /**
     * @param fatorVencimento the fatorVencimento to set
     */
    public void setFatorVencimento(int fatorVencimento) {
        this.fatorVencimento = fatorVencimento;
    }

    /**
     * @return the valorMininoCobranca
     */
    public BigDecimal getValorMininoCobranca() {
        return valorMininoCobranca;
    }

    /**
     * @param valorMininoCobranca the valorMininoCobranca to set
     */
    public void setValorMininoCobranca(BigDecimal valorMininoCobranca) {
        this.valorMininoCobranca = valorMininoCobranca;
    }

    /**
     * @return the tipoCota
     */
    public TipoCota getTipoCota() {
        return tipoCota;
    }

    /**
     * @param tipoCota the tipoCota to set
     */
    public void setTipoCota(TipoCota tipoCota) {
        this.tipoCota = tipoCota;
    }

    /**
     * @return the politicaSuspensao
     */
    public PoliticaSuspensao getPoliticaSuspensao() {
        return politicaSuspensao;
    }

    /**
     * @param politicaSuspensao the politicaSuspensao to set
     */
    public void setPoliticaSuspensao(PoliticaSuspensao politicaSuspensao) {
        this.politicaSuspensao = politicaSuspensao;
    }

    /**
     * @return the formasPagamento
     */
    public Collection<HistoricoTitularidadeCotaFormaPagamento> getFormasPagamento() {
        return formasPagamento;
    }

    /**
     * @param formasPagamento the formasPagamento to set
     */
    public void setFormasPagamento(
            Collection<HistoricoTitularidadeCotaFormaPagamento> formasPagamento) {
        this.formasPagamento = formasPagamento;
    }

    /**
     * @return the possuiContrato
     */
    public boolean isPossuiContrato() {
        return possuiContrato;
    }

    /**
     * @param possuiContrato the possuiContrato to set
     */
    public void setPossuiContrato(boolean possuiContrato) {
        this.possuiContrato = possuiContrato;
    }

    /**
     * @return the dataInicioContrato
     */
    public Date getDataInicioContrato() {
        return dataInicioContrato;
    }

    /**
     * @param dataInicioContrato the dataInicioContrato to set
     */
    public void setDataInicioContrato(Date dataInicioContrato) {
        this.dataInicioContrato = dataInicioContrato;
    }

    /**
     * @return the dataTerminoContrato
     */
    public Date getDataTerminoContrato() {
        return dataTerminoContrato;
    }

    /**
     * @param dataTerminoContrato the dataTerminoContrato to set
     */
    public void setDataTerminoContrato(Date dataTerminoContrato) {
        this.dataTerminoContrato = dataTerminoContrato;
    }

    /**
     * @return the contratoRecebido
     */
    public boolean isContratoRecebido() {
        return contratoRecebido;
    }

    /**
     * @param contratoRecebido the contratoRecebido to set
     */
    public void setContratoRecebido(boolean contratoRecebido) {
        this.contratoRecebido = contratoRecebido;
    }
    

}
