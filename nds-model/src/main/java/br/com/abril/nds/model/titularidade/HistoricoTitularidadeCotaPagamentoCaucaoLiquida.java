package br.com.abril.nds.model.titularidade;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.com.abril.nds.model.DiaSemana;
import br.com.abril.nds.model.cadastro.TipoCobrancaCotaGarantia;
import br.com.abril.nds.model.cadastro.TipoFormaCobranca;


/**
 * Representa a forma de pagamento do caução líquida, como o valor da caução
 * líquida foi captado
 * 
 * @author francisco.garcia
 * 
 */
@Entity
@Table(name = "HISTORICO_TITULARIDADE_COTA_PGTO_CAUCAO_LIQUIDA")
@SequenceGenerator(name = "HIST_TIT_COTA_PGTO_CAUCAO_LIQ_SEQ", initialValue = 1, allocationSize = 1)
public class HistoricoTitularidadeCotaPagamentoCaucaoLiquida implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(generator = "HIST_TIT_COTA_PGTO_CAUCAO_LIQ_SEQ")
    @Column(name = "ID")
    private Long id;

    /**
     * Tipo da cobrança da caução líquida
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "TIPO_COBRANCA", nullable = false)
    private TipoCobrancaCotaGarantia tipoCobranca; 
    
    /**
    * Qtde de parcelas do boleto 
    */
    @Column(name = "QTDE_PARCELAS_BOLETO")
    private Integer qtdeParcelasBoleto;
    
    /**
     * Valor das parcelas do boleto
     */
    @Column(name = "VALOR_PARCELAS_BOLETO")
    private BigDecimal valorParcelaBoleto;
    
    /**
     * Periodicidade da cobrança do boleto
     */
    @Column(name = "PERIODICIDADE_BOLETO")
    @Enumerated(EnumType.STRING)
    private TipoFormaCobranca periodicidadeBoleto;
    
    /**
     * Dias da semana para cobrança do boleto
     */
    @ElementCollection
    @CollectionTable(name = "HISTORICO_TITULARIDADE_COTA_PGTO_CAUCAO_LIQUIDA_DIA_SEMANA", 
        joinColumns = { @JoinColumn(name = "HISTORICO_TITULARIDADE_COTA_PGTO_CAUCAO_LIQUIDA_ID")})
    @Column(name = "DIA_SEMANA")
    private Collection<DiaSemana> diasSemanaBoleto;
    
    /**
     * Dias do mês para cobrança do boleto
     */
    @ElementCollection
    @CollectionTable(name = "HISTORICO_TITULARIDADE_COTA_PGTO_CAUCAO_LIQUIDA_DIA_MES", 
        joinColumns = { @JoinColumn(name = "HISTORICO_TITULARIDADE_COTA_PGTO_CAUCAO_LIQUIDA_ID")})
    @Column(name = "DIA_MES")
    private Collection<Integer> diasMesBoleto;
    
    /**
     * Desconto normal da cota
     */
    @Column(name = "DESCONTO_NORMAL_COTA")
    private BigDecimal descontoNormal;
    
    /**
     * Desconto reduzido da cota
     */
    @Column(name = "DESCONTO_REDUZIDO_COTA")
    private BigDecimal descontoReduzido;
    
    /**
     * Percentual utilizado do desconto da cota
     */
    @Column(name = "PERCENTUAL_UTILIZADO_DESCONTO")
    private BigDecimal porcentagemUtilizada;

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
     * @return the tipoCobranca
     */
    public TipoCobrancaCotaGarantia getTipoCobranca() {
        return tipoCobranca;
    }

    /**
     * @param tipoCobranca the tipoCobranca to set
     */
    public void setTipoCobranca(TipoCobrancaCotaGarantia tipoCobranca) {
        this.tipoCobranca = tipoCobranca;
    }

    /**
     * @return the qtdeParcelasBoleto
     */
    public Integer getQtdeParcelasBoleto() {
        return qtdeParcelasBoleto;
    }

    /**
     * @param qtdeParcelasBoleto the qtdeParcelasBoleto to set
     */
    public void setQtdeParcelasBoleto(Integer qtdeParcelasBoleto) {
        this.qtdeParcelasBoleto = qtdeParcelasBoleto;
    }

    /**
     * @return the valorParcelasBoleto
     */
    public BigDecimal getValorParcelasBoleto() {
        return valorParcelaBoleto;
    }

    /**
     * @param valorParcelasBoleto the valorParcelasBoleto to set
     */
    public void setValorParcelasBoleto(BigDecimal valorParcelasBoleto) {
        this.valorParcelaBoleto = valorParcelasBoleto;
    }

    /**
     * @return the periodicidadeBoleto
     */
    public TipoFormaCobranca getPeriodicidadeBoleto() {
        return periodicidadeBoleto;
    }

    /**
     * @param periodicidadeBoleto the periodicidadeBoleto to set
     */
    public void setPeriodicidadeBoleto(TipoFormaCobranca periodicidadeBoleto) {
        this.periodicidadeBoleto = periodicidadeBoleto;
    }

    /**
     * @return the diasSemanaBoleto
     */
    public Collection<DiaSemana> getDiasSemanaBoleto() {
        return diasSemanaBoleto;
    }

    /**
     * @param diasSemanaBoleto the diasSemanaBoleto to set
     */
    public void setDiasSemanaBoleto(Collection<DiaSemana> diasSemanaBoleto) {
        this.diasSemanaBoleto = diasSemanaBoleto;
    }

    /**
     * @return the diasMesBoleto
     */
    public Collection<Integer> getDiasMesBoleto() {
        return diasMesBoleto;
    }

    /**
     * @param diasMesBoleto the diasMesBoleto to set
     */
    public void setDiasMesBoleto(Collection<Integer> diasMesBoleto) {
        this.diasMesBoleto = diasMesBoleto;
    }

    /**
     * @return the descontoNormal
     */
    public BigDecimal getDescontoNormal() {
        return descontoNormal;
    }

    /**
     * @param descontoNormal the descontoNormal to set
     */
    public void setDescontoNormal(BigDecimal descontoNormal) {
        this.descontoNormal = descontoNormal;
    }

    /**
     * @return the descontoReduzido
     */
    public BigDecimal getDescontoReduzido() {
        return descontoReduzido;
    }

    /**
     * @param descontoReduzido the descontoReduzido to set
     */
    public void setDescontoReduzido(BigDecimal descontoReduzido) {
        this.descontoReduzido = descontoReduzido;
    }

    /**
     * @return the porcentagemUtilizada
     */
    public BigDecimal getPorcentagemUtilizada() {
        return porcentagemUtilizada;
    }

    /**
     * @param porcentagemUtilizada the porcentagemUtilizada to set
     */
    public void setPorcentagemUtilizada(BigDecimal porcentagemUtilizada) {
        this.porcentagemUtilizada = porcentagemUtilizada;
    }

    /**
     * Adiciona um dia da semana para o pagamento da caução líquida por meio de
     * boleto
     * 
     * @param diaSemana
     *            dia da semana para inclusão
     */
    public void addDiaSemanaBoleto(DiaSemana diaSemana) {
        if (this.diasSemanaBoleto == null) {
            this.diasSemanaBoleto = new ArrayList<DiaSemana>();
        }
        this.diasSemanaBoleto.add(diaSemana);
    }

    /**
     * Adiciona um dia do mês para o pagamento da caução líquida por meio de
     * boleto
     * 
     * @param diaMes
     *            dia do mês para inclusão
     */
    public void addDiaMesBoleto(Integer diaMes) {
        if (this.diasMesBoleto == null) {
            this.diasMesBoleto = new ArrayList<Integer>();
        }
        this.diasMesBoleto.add(diaMes);
    }
    
}
