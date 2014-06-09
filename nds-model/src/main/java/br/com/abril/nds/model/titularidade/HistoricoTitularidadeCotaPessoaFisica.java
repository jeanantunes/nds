package br.com.abril.nds.model.titularidade;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.com.abril.nds.model.cadastro.EstadoCivil;
import br.com.abril.nds.model.cadastro.Sexo;

/**
 * Representa a Pessoa Física no histórico de titularidade da cota
 * 
 * @author francisco.garcia
 * 
 */
@Embeddable
public class HistoricoTitularidadeCotaPessoaFisica implements HistoricoTitularidadeCotaPessoa, Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Nome da pessoa
     */
    @Column(name = "NOME")
    private String nome;

    /**
     * CPF da pessoa
     */
    @Column(name = "CPF")
    private String cpf;

    /**
     * RG da pessoa
     */
    @Column(name = "RG")
    private String rg;

    /**
     * Orgão emissor do RG da pessoa
     */
    @Column(name = "ORGAO_EMISSOR")
    private String orgaoEmissor;

    /**
     * UF do orgão emissor do RG da pessoa
     */
    @Column(name = "UF_ORGAO_EMISSOR")
    private String ufOrgaoEmissor;

    /**
     * Data de nascimento da pessoa
     */
    @Column(name = "DATA_NASCIMENTO")
    @Temporal(TemporalType.DATE)
    private Date dataNascimento;

    /**
     * Estado civil da pessoa
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "ESTADO_CIVIL")
    private EstadoCivil estadoCivil;

    /**
     * Sexo da pessoa
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "SEXO")
    private Sexo sexo;

    /**
     * Nacionalidade da pessoa
     */
    @Column(name = "NACIONALIDADE")
    private String nacionalidade;

    /**
     * Naturalidade da pessoa
     */
    @Column(name = "NATURALIDADE")
    private String natural;

    /**
     * Apelido da pessoa
     */
    @Column(name = "APELIDO", length = 25)
    private String apelido;
    
    public HistoricoTitularidadeCotaPessoaFisica() {
    }    

    public HistoricoTitularidadeCotaPessoaFisica(String nome, String cpf,
            String rg, String orgaoEmissor, String ufOrgaoEmissor,
            Date dataNascimento, EstadoCivil estadoCivil, Sexo sexo,
            String nacionalidade, String natural, String apelido) {
        this.nome = nome;
        this.cpf = cpf;
        this.rg = rg;
        this.orgaoEmissor = orgaoEmissor;
        this.ufOrgaoEmissor = ufOrgaoEmissor;
        this.dataNascimento = dataNascimento;
        this.estadoCivil = estadoCivil;
        this.sexo = sexo;
        this.nacionalidade = nacionalidade;
        this.natural = natural;
        this.apelido = apelido;
    }

    /**
     * @return the nome
     */
    public String getNome() {
        return nome;
    }

    /**
     * @param nome
     *            the nome to set
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * @return the cpf
     */
    public String getCpf() {
        return cpf;
    }

    /**
     * @param cpf
     *            the cpf to set
     */
    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    /**
     * @return the rg
     */
    public String getRg() {
        return rg;
    }

    /**
     * @param rg
     *            the rg to set
     */
    public void setRg(String rg) {
        this.rg = rg;
    }

    /**
     * @return the orgaoEmissor
     */
    public String getOrgaoEmissor() {
        return orgaoEmissor;
    }

    /**
     * @param orgaoEmissor
     *            the orgaoEmissor to set
     */
    public void setOrgaoEmissor(String orgaoEmissor) {
        this.orgaoEmissor = orgaoEmissor;
    }

    /**
     * @return the ufOrgaoEmissor
     */
    public String getUfOrgaoEmissor() {
        return ufOrgaoEmissor;
    }

    /**
     * @param ufOrgaoEmissor
     *            the ufOrgaoEmissor to set
     */
    public void setUfOrgaoEmissor(String ufOrgaoEmissor) {
        this.ufOrgaoEmissor = ufOrgaoEmissor;
    }

    /**
     * @return the dataNascimento
     */
    public Date getDataNascimento() {
        return dataNascimento;
    }

    /**
     * @param dataNascimento
     *            the dataNascimento to set
     */
    public void setDataNascimento(Date dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    /**
     * @return the estadoCivil
     */
    public EstadoCivil getEstadoCivil() {
        return estadoCivil;
    }

    /**
     * @param estadoCivil
     *            the estadoCivil to set
     */
    public void setEstadoCivil(EstadoCivil estadoCivil) {
        this.estadoCivil = estadoCivil;
    }

    /**
     * @return the sexo
     */
    public Sexo getSexo() {
        return sexo;
    }

    /**
     * @param sexo
     *            the sexo to set
     */
    public void setSexo(Sexo sexo) {
        this.sexo = sexo;
    }

    /**
     * @return the nacionalidade
     */
    public String getNacionalidade() {
        return nacionalidade;
    }

    /**
     * @param nacionalidade
     *            the nacionalidade to set
     */
    public void setNacionalidade(String nacionalidade) {
        this.nacionalidade = nacionalidade;
    }

    /**
     * @return the natural
     */
    public String getNatural() {
        return natural;
    }

    /**
     * @param natural
     *            the natural to set
     */
    public void setNatural(String natural) {
        this.natural = natural;
    }

    /**
     * @return the apelido
     */
    public String getApelido() {
        return apelido;
    }

    /**
     * @param apelido
     *            the apelido to set
     */
    public void setApelido(String apelido) {
        this.apelido = apelido;
    }

    @Override
    public String getDocumento() {
        return cpf;
    }

}
