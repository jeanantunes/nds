package br.com.abril.nds.model.titularidade;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;

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
    @OneToMany
    @JoinColumn(name = "HISTORICO_TITULARIDADE_COTA_FINANCEIRO_ID")
    private Collection<HistoricoTitularidadeCotaFormaPagamento> formasPagamento;

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
    

}
