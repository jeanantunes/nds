package br.com.abril.nds.model.titularidade;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import br.com.abril.nds.model.cadastro.ContaBancariaDeposito;
import br.com.abril.nds.model.cadastro.TipoGarantia;

/**
 * Representa a garantia do tipo "CAUCAO LIQUIDA" no histórico de titularidade
 * da cota
 * 
 * @author francisco.garcia
 * 
 */
@Entity
@DiscriminatorValue("CAUCAO_LIQUIDA")
public class HistoricoTitularidadeCotaCaucaoLiquida extends HistoricoTitularidadeCotaGarantia {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * Valor da caução líquida
     */
    @Column(name = "VALOR_CAUCAO_LIQUIDA")
    private BigDecimal valor;

    /**
     * Atualizações do valor da cuação líquida
     */
    @ElementCollection
    @CollectionTable(name = "HISTORICO_TITULARIDADE_COTA_ATUALIZACAO_CAUCAO_LIQUIDA", 
        joinColumns = { @JoinColumn(name = "HISTORICO_TITULARIDADE_COTA_PGTO_CAUCAO_LIQUIDA_ID")})
    private Collection<HistoricoTitularidadeCotaAtualizacaoCaucaoLiquida> atualizacoes;
    
    /**
     * Conta bancária onde o valor da caução está depositada
     */
    @Embedded
    private ContaBancariaDeposito contaBancariaDeposito;
    
    /**
     * Forma de pagamento da caução líquida
     */
    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "HISTORICO_TITULARIDADE_COTA_PGTO_CAUCAO_LIQUIDA_ID")
    private HistoricoTitularidadeCotaPagamentoCaucaoLiquida pagamento;

    public HistoricoTitularidadeCotaCaucaoLiquida() {
        this.tipoGarantia = TipoGarantia.CAUCAO_LIQUIDA;
    }
    
    /**
     * @return the valor
     */
    public BigDecimal getValor() {
        return valor;
    }

    /**
     * @param valor the valor to set
     */
    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    /**
     * @return the atualizacoes
     */
    public Collection<HistoricoTitularidadeCotaAtualizacaoCaucaoLiquida> getAtualizacoes() {
        return atualizacoes;
    }

    /**
     * @param atualizacoes the atualizacoes to set
     */
    public void setAtualizacoes(
            Collection<HistoricoTitularidadeCotaAtualizacaoCaucaoLiquida> atualizacoes) {
        this.atualizacoes = atualizacoes;
    }

    /**
     * @return the contaBancariaDeposito
     */
    public ContaBancariaDeposito getContaBancariaDeposito() {
        return contaBancariaDeposito;
    }

    /**
     * @param contaBancariaDeposito the contaBancariaDeposito to set
     */
    public void setContaBancariaDeposito(ContaBancariaDeposito contaBancariaDeposito) {
        this.contaBancariaDeposito = contaBancariaDeposito;
    }

    /**
     * @return the pagamento
     */
    public HistoricoTitularidadeCotaPagamentoCaucaoLiquida getPagamento() {
        return pagamento;
    }

    /**
     * @param pagamento the pagamento to set
     */
    public void setPagamento(
            HistoricoTitularidadeCotaPagamentoCaucaoLiquida pagamento) {
        this.pagamento = pagamento;
    }
    
    /**
     * Adiciona uma atualização para a caução líquida
     * 
     * @param atualizacao
     *            atualização para inclusão
     */
    public void addAtualizacao(HistoricoTitularidadeCotaAtualizacaoCaucaoLiquida atualizacao) {
        if (this.atualizacoes == null) {
            this.atualizacoes = new ArrayList<HistoricoTitularidadeCotaAtualizacaoCaucaoLiquida>();
        }
        this.atualizacoes.add(atualizacao);
    }

}
