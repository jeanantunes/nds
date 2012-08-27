package br.com.abril.nds.model.titularidade;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Represenata a garantia do tipo "CHEQUE CAUCAO" no histórico de titularidade
 * da cota
 * 
 * @author francisco.garcia
 * 
 */
@Entity
@DiscriminatorValue("CHEQUE_CAUCAO")
public class HistoricoTitularidadeCotaChequeCaucao extends HistoricoTitularidadeCotaGarantia {
    
    private static final long serialVersionUID = 1L;

    /**
     * Número do banco do cheque
     */
    @Column(name = "CHEQUE_CAUCAO_NUMERO_BANCO")
    private String numeroBanco;
    
    /**
     * Nome do banco do cheque
     */
    @Column(name = "CHEQUE_CAUCAO_NOME_BANCO")
    private String nomeBanco;
    
    /**
     * Número da agencia do banco do cheque
     */
    @Column(name = "CHEQUE_CAUCAO_AGENCIA")
    private Long agencia;
    
    /**
     * Dígito verificador da agência do cheque
     */
    @Column(name = "CHEQUE_CAUCAO_DV_AGENCIA")
    private String dvAgencia;
    
    /**
     * Número da conta do cheque
     */
    @Column(name = "CHEQUE_CAUCAO_CONTA")
    private Long conta;
    
    /**
     * Dígito verificador da conta do cheque
     */
    @Column(name = "CHEQUE_CAUCAO_DV_CONTA")
    private String dvConta;
    
    /**
     * Valor do cheque
     */
    @Column(name="CHEQUE_CAUCAO_VALOR")
    private BigDecimal valor;
    
    /**
     * Número do cheque
     */
    @Column(name="CHEQUE_CAUCAO_NUMERO_CHEQUE")
    private String numeroCheque;
    
    /**
     * Data de emissão do cheque
     */
    @Temporal(TemporalType.DATE)
    @Column(name="CHEQUE_CAUCAO_DATA_EMISSAO")
    private Date emissao;
    
    /**
     * Validade do cheque
     */
    @Temporal(TemporalType.DATE)
    @Column(name="CHEQUE_CAUCAO_DATA_VALIDADE")
    private Date validade;
    
    /**
     * Nome do correntista
     */
    @Column(name="CHEQUE_CAUCAO_CORRENTISTA")
    private String correntista;
    
    /**
     * Imagem do cheque
     */
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
    public Date getEmissao() {
        return emissao;
    }

    /**
     * @param emissao the emissao to set
     */
    public void setEmissao(Date emissao) {
        this.emissao = emissao;
    }

    /**
     * @return the validade
     */
    public Date getValidade() {
        return validade;
    }

    /**
     * @param validade the validade to set
     */
    public void setValidade(Date validade) {
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
