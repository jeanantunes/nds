package br.com.abril.nds.model.titularidade;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * Representa a pessoa física que detém a titularidade da cota em um determinado
 * período
 * 
 * @author francisco.garcia
 *
 */
@Embeddable
public class HistoricoTitularidadePessoaJuridica {
    
    /**
     * Razão social da PJ titular da cota
     */
    @Column(name = "RAZAO_SOCIAL")
    private String razaoSocial;
    
    /**
     * Nome fantasia da PJ titular da cota
     */
    @Column(name = "NOME_FANTASIA", length = 60)
    private String nomeFantasia;
    
    /**
     * CNPJ da PJ titular da cota
     */
    @Column(name = "CNPJ")
    private String cnpj;
    
    /**
     * Inscrição estadual da PJ titular da cota
     */
    @Column(name = "INSC_ESTADUAL", length=18)
    private String inscricaoEstadual;
    
    /**
     * Inscrição municipal da PJ titular da cota
     */
    @Column(name = "INSC_MUNICIPAL", length=15)
    private String inscricaoMunicipal;

    /**
     * @return the razaoSocial
     */
    public String getRazaoSocial() {
        return razaoSocial;
    }

    /**
     * @param razaoSocial the razaoSocial to set
     */
    public void setRazaoSocial(String razaoSocial) {
        this.razaoSocial = razaoSocial;
    }

    /**
     * @return the nomeFantasia
     */
    public String getNomeFantasia() {
        return nomeFantasia;
    }

    /**
     * @param nomeFantasia the nomeFantasia to set
     */
    public void setNomeFantasia(String nomeFantasia) {
        this.nomeFantasia = nomeFantasia;
    }

    /**
     * @return the cnpj
     */
    public String getCnpj() {
        return cnpj;
    }

    /**
     * @param cnpj the cnpj to set
     */
    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    /**
     * @return the inscricaoEstadual
     */
    public String getInscricaoEstadual() {
        return inscricaoEstadual;
    }

    /**
     * @param inscricaoEstadual the inscricaoEstadual to set
     */
    public void setInscricaoEstadual(String inscricaoEstadual) {
        this.inscricaoEstadual = inscricaoEstadual;
    }

    /**
     * @return the inscricaoMunicipal
     */
    public String getInscricaoMunicipal() {
        return inscricaoMunicipal;
    }

    /**
     * @param inscricaoMunicipal the inscricaoMunicipal to set
     */
    public void setInscricaoMunicipal(String inscricaoMunicipal) {
        this.inscricaoMunicipal = inscricaoMunicipal;
    }

}
