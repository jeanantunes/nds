package br.com.abril.nds.model.titularidade;

import java.util.Calendar;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Represenata a garantia do tipo "CHEQUE CAUCAO" no histórico de
 * titularidade da cota
 * 
 * @author francisco.garcia
 * 
 */
@Entity
@DiscriminatorValue("CHEQUE_CAUCAO")
public class HistoricoTitularidadeCotaChequeCaucao extends HistoricoTitularidadeCotaGarantia {
    
    @Column(name = "CHEQUE_CAUCAO_NUMERO_BANCO")
    private String numeroBanco;
    
    @Column(name = "CHEQUE_CAUCAO_NOME_BANCO")
    private String nomeBanco;
    
    @Column(name = "CHEQUE_CAUCAO_AGENCIA")
    private Long agencia;
    
    @Column(name = "CHEQUE_CAUCAO_DV_AGENCIA")
    private String dvAgencia;
    
    @Column(name = "CHEQUE_CAUCAO_CONTA")
    private Long conta;
    
    @Column(name = "CHEQUE_CAUCAO_DV_CONTA")
    private String dvConta;
    
    @Column(name="CHEQUE_CAUCAO_VALOR")
    private Double valor;
    
    @Column(name="CHEQUE_CAUCAO_NUMERO_CHEQUE")
    private String numeroCheque;
    
    @Temporal(TemporalType.DATE)
    @Column(name="CHEQUE_CAUCAO_DATA_EMISSAO")
    private Calendar emissao;
    
    @Temporal(TemporalType.DATE)
    @Column(name="CHEQUE_CAUCAO_DATA_VALIDADE")
    private Calendar validade;
    
    @Column(name="CHEQUE_CAUCAO_CORRENTISTA")
    private String correntista;
    
    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "CHEQUE_CAUCAO_IMAGEM")
    private byte[] imagem;

    /**
     * @return the numeroBanco
     */
    public String getNumeroBanco() {
        return numeroBanco;
    }

    /**
     * @param numeroBanco the numeroBanco to set
     */
    public void setNumeroBanco(String numeroBanco) {
        this.numeroBanco = numeroBanco;
    }

    /**
     * @return the nomeBanco
     */
    public String getNomeBanco() {
        return nomeBanco;
    }

    /**
     * @param nomeBanco the nomeBanco to set
     */
    public void setNomeBanco(String nomeBanco) {
        this.nomeBanco = nomeBanco;
    }

    /**
     * @return the agencia
     */
    public Long getAgencia() {
        return agencia;
    }

    /**
     * @param agencia the agencia to set
     */
    public void setAgencia(Long agencia) {
        this.agencia = agencia;
    }

    /**
     * @return the dvAgencia
     */
    public String getDvAgencia() {
        return dvAgencia;
    }

    /**
     * @param dvAgencia the dvAgencia to set
     */
    public void setDvAgencia(String dvAgencia) {
        this.dvAgencia = dvAgencia;
    }

    /**
     * @return the conta
     */
    public Long getConta() {
        return conta;
    }

    /**
     * @param conta the conta to set
     */
    public void setConta(Long conta) {
        this.conta = conta;
    }

    /**
     * @return the dvConta
     */
    public String getDvConta() {
        return dvConta;
    }

    /**
     * @param dvConta the dvConta to set
     */
    public void setDvConta(String dvConta) {
        this.dvConta = dvConta;
    }

    /**
     * @return the valor
     */
    public Double getValor() {
        return valor;
    }

    /**
     * @param valor the valor to set
     */
    public void setValor(Double valor) {
        this.valor = valor;
    }

    /**
     * @return the numeroCheque
     */
    public String getNumeroCheque() {
        return numeroCheque;
    }

    /**
     * @param numeroCheque the numeroCheque to set
     */
    public void setNumeroCheque(String numeroCheque) {
        this.numeroCheque = numeroCheque;
    }

    /**
     * @return the emissao
     */
    public Calendar getEmissao() {
        return emissao;
    }

    /**
     * @param emissao the emissao to set
     */
    public void setEmissao(Calendar emissao) {
        this.emissao = emissao;
    }

    /**
     * @return the validade
     */
    public Calendar getValidade() {
        return validade;
    }

    /**
     * @param validade the validade to set
     */
    public void setValidade(Calendar validade) {
        this.validade = validade;
    }

    /**
     * @return the correntista
     */
    public String getCorrentista() {
        return correntista;
    }

    /**
     * @param correntista the correntista to set
     */
    public void setCorrentista(String correntista) {
        this.correntista = correntista;
    }

    /**
     * @return the imagem
     */
    public byte[] getImagem() {
        return imagem;
    }

    /**
     * @param imagem the imagem to set
     */
    public void setImagem(byte[] imagem) {
        this.imagem = imagem;
    }
    
}
