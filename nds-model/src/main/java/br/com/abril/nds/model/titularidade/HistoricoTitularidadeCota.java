package br.com.abril.nds.model.titularidade;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.com.abril.nds.model.cadastro.ClassificacaoEspectativaFaturamento;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;

/**
 * Representa a titularidade da cota, utilizado como histórico de propriedade da
 * cota em um determinado peíodo
 * 
 * @author francisco.garcia
 * 
 */
@Entity
@Table(name = "HISTORICO_TITULARIDADE_COTA")
@SequenceGenerator(name = "HIST_TIT_COTA_SEQ", initialValue = 1, allocationSize = 1)
public class HistoricoTitularidadeCota implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "HIST_TIT_COTA_SEQ")
    @Column(name = "ID")
    private Long id;

    /**
     * Cota em que esta titularidade esta associada
     */
    @ManyToOne(optional = false)
    @JoinColumn(name = "COTA_ID")
    private Cota cota;

    /**
     * Início da titularidade para a cota
     */
    @Temporal(TemporalType.DATE)
    @Column(name = "INICIO", nullable = false)
    private Date inicio;

    /**
     * Fim da titularidade para a cota
     */
    @Temporal(TemporalType.DATE)
    @Column(name = "FIM", nullable = false)
    private Date fim;

    /**
     * Número da cota durante a titularidade
     */
    @Column(name = "NUMERO_COTA", nullable = false)
    private Integer numeroCota;

    /**
     * Situacao de cadastro da cota na alteração da titularidade
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "SITUACAO_CADASTRO", nullable = false)
    private SituacaoCadastro situacaoCadastro;

    /**
     * Email da cota na alteração da titularidade
     */
    @Column(name = "EMAIL")
    private String email;

    /**
     * Pessoa física titular da cota
     */
    @Embedded
    private HistoricoTitularidadePessoaFisica pessoaFisica;

    /**
     * Pessoa Jurídica titular da cota
     */
    @Embedded
    private HistoricoTitularidadePessoaJuridica pessoaJuridica;

    @Enumerated(EnumType.STRING)
    @Column(name = "CLASSIFICACAO_EXPECTATIVA_FATURAMENTO")
    private ClassificacaoEspectativaFaturamento classificacaoExpectativaFaturamento;

    /**
     * Endereços da titularidade da cota
     */
    @OneToMany(mappedBy = "historicoTitularidadeCota")
    private Collection<HistoricoTitularidadeCotaEndereco> enderecos;

    /**
     * Telefones da titularidade da cota
     */
    @OneToMany(mappedBy = "historicoTitularidadeCota")
    private Collection<HistoricoTitularidadeCotaTelefone> telefones;

    /**
     * Fornecedores associados a titularidade da cota
     */
    @OneToMany
    @JoinColumn(name = "HISTORICO_TITULARIDADE_COTA_ID")
    private Collection<HistoricoTitularidadeCotaFornecedor> fornecedores;

    /**
     * PDV's da cota
     */
    @OneToMany(mappedBy = "historicoTitularidadeCota", cascade = { CascadeType.ALL })
    private Collection<HistoricoTitularidadeCotaPDV> pdvs;

    /**
     * Garantias da cota
     */
    @OneToMany(mappedBy = "historicoTitularidadeCota", cascade = { CascadeType.ALL })
    private Collection<HistoricoTitularidadeCotaGarantia> garantias;

    /**
     * Início do período base para cota
     */
    @Temporal(TemporalType.DATE)
    @Column(name = "INICIO_PERIODO_COTA_BASE")
    private Date inicioPeriodoCotaBase;

    /**
     * Fim do período base para a cota
     */
    @Temporal(TemporalType.DATE)
    @Column(name = "FIM_PERIODO_COTA_BASE")
    private Date fimPeriodoCotaBase;

    /**
     * Cotas utilizadas como referência
     */
    @ElementCollection
    @CollectionTable(name = "HISTORICO_TITULARIDADE_REFERENCIA_COTA", joinColumns = { @JoinColumn(name = "HISTORICO_TITULARIDADE_COTA_ID") })
    private Collection<HistoricoTitularidadeReferenciaCota> referencias;

    @OneToMany(mappedBy = "historicoTitularidadeCota", cascade = { CascadeType.ALL })
    private Collection<HistoricoTitularidadeDescontoCota> descontos;

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id
     *            the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the cota
     */
    public Cota getCota() {
        return cota;
    }

    /**
     * @param cota
     *            the cota to set
     */
    public void setCota(Cota cota) {
        this.cota = cota;
    }

    /**
     * @return the inicio
     */
    public Date getInicio() {
        return inicio;
    }

    /**
     * @param inicio
     *            the inicio to set
     */
    public void setInicio(Date inicio) {
        this.inicio = inicio;
    }

    /**
     * @return the fim
     */
    public Date getFim() {
        return fim;
    }

    /**
     * @param fim
     *            the fim to set
     */
    public void setFim(Date fim) {
        this.fim = fim;
    }

    /**
     * @return the numeroCota
     */
    public Integer getNumeroCota() {
        return numeroCota;
    }

    /**
     * @param numeroCota
     *            the numeroCota to set
     */
    public void setNumeroCota(Integer numeroCota) {
        this.numeroCota = numeroCota;
    }

    /**
     * @return the situacaoCadastro
     */
    public SituacaoCadastro getSituacaoCadastro() {
        return situacaoCadastro;
    }

    /**
     * @param situacaoCadastro
     *            the situacaoCadastro to set
     */
    public void setSituacaoCadastro(SituacaoCadastro situacaoCadastro) {
        this.situacaoCadastro = situacaoCadastro;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email
     *            the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the pessoaFisica
     */
    public HistoricoTitularidadePessoaFisica getPessoaFisica() {
        return pessoaFisica;
    }

    /**
     * @param pessoaFisica
     *            the pessoaFisica to set
     */
    public void setPessoaFisica(HistoricoTitularidadePessoaFisica pessoaFisica) {
        this.pessoaFisica = pessoaFisica;
    }

    /**
     * @return the pessoaJuridica
     */
    public HistoricoTitularidadePessoaJuridica getPessoaJuridica() {
        return pessoaJuridica;
    }

    /**
     * @param pessoaJuridica
     *            the pessoaJuridica to set
     */
    public void setPessoaJuridica(
            HistoricoTitularidadePessoaJuridica pessoaJuridica) {
        this.pessoaJuridica = pessoaJuridica;
    }

    /**
     * @return the enderecos
     */
    public Collection<HistoricoTitularidadeCotaEndereco> getEnderecos() {
        return enderecos;
    }

    /**
     * @param enderecos
     *            the enderecos to set
     */
    public void setEnderecos(
            Collection<HistoricoTitularidadeCotaEndereco> enderecos) {
        this.enderecos = enderecos;
    }

    /**
     * @return the telefones
     */
    public Collection<HistoricoTitularidadeCotaTelefone> getTelefones() {
        return telefones;
    }

    /**
     * @param telefones
     *            the telefones to set
     */
    public void setTelefones(
            Collection<HistoricoTitularidadeCotaTelefone> telefones) {
        this.telefones = telefones;
    }

    /**
     * @return the classificacaoExpectativaFaturamento
     */
    public ClassificacaoEspectativaFaturamento getClassificacaoExpectativaFaturamento() {
        return classificacaoExpectativaFaturamento;
    }

    /**
     * @param classificacaoExpectativaFaturamento
     *            the classificacaoExpectativaFaturamento to set
     */
    public void setClassificacaoExpectativaFaturamento(
            ClassificacaoEspectativaFaturamento classificacaoExpectativaFaturamento) {
        this.classificacaoExpectativaFaturamento = classificacaoExpectativaFaturamento;
    }

    /**
     * @return the fornecedores
     */
    public Collection<HistoricoTitularidadeCotaFornecedor> getFornecedores() {
        return fornecedores;
    }

    /**
     * @param fornecedores
     *            the fornecedores to set
     */
    public void setFornecedores(
            Collection<HistoricoTitularidadeCotaFornecedor> fornecedores) {
        this.fornecedores = fornecedores;
    }

    /**
     * @return the pdvs
     */
    public Collection<HistoricoTitularidadeCotaPDV> getPdvs() {
        return pdvs;
    }

    /**
     * @param pdvs
     *            the pdvs to set
     */
    public void setPdvs(Collection<HistoricoTitularidadeCotaPDV> pdvs) {
        this.pdvs = pdvs;
    }

    /**
     * @return the garantias
     */
    public Collection<HistoricoTitularidadeCotaGarantia> getGarantias() {
        return garantias;
    }

    /**
     * @param garantias
     *            the garantias to set
     */
    public void setGarantias(
            Collection<HistoricoTitularidadeCotaGarantia> garantias) {
        this.garantias = garantias;
    }

    /**
     * @return the inicioPeriodoCotaBase
     */
    public Date getInicioPeriodoCotaBase() {
        return inicioPeriodoCotaBase;
    }

    /**
     * @param inicioPeriodoCotaBase
     *            the inicioPeriodoCotaBase to set
     */
    public void setInicioPeriodoCotaBase(Date inicioPeriodoCotaBase) {
        this.inicioPeriodoCotaBase = inicioPeriodoCotaBase;
    }

    /**
     * @return the fimPeriodoCotaBase
     */
    public Date getFimPeriodoCotaBase() {
        return fimPeriodoCotaBase;
    }

    /**
     * @param fimPeriodoCotaBase
     *            the fimPeriodoCotaBase to set
     */
    public void setFimPeriodoCotaBase(Date fimPeriodoCotaBase) {
        this.fimPeriodoCotaBase = fimPeriodoCotaBase;
    }

    /**
     * @return the referencias
     */
    public Collection<HistoricoTitularidadeReferenciaCota> getReferencias() {
        return referencias;
    }

    /**
     * @param referencias
     *            the referencias to set
     */
    public void setReferencias(
            Collection<HistoricoTitularidadeReferenciaCota> referencias) {
        this.referencias = referencias;
    }

    /**
     * @return the descontos
     */
    public Collection<HistoricoTitularidadeDescontoCota> getDescontos() {
        return descontos;
    }

    /**
     * @param descontos
     *            the descontos to set
     */
    public void setDescontos(
            Collection<HistoricoTitularidadeDescontoCota> descontos) {
        this.descontos = descontos;
    }

}
