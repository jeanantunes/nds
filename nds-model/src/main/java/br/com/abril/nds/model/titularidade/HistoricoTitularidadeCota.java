package br.com.abril.nds.model.titularidade;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

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
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.com.abril.nds.model.cadastro.ClassificacaoEspectativaFaturamento;
import br.com.abril.nds.model.cadastro.Cota;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.cadastro.TipoGarantia;

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
    
    @Temporal(TemporalType.DATE)
    @Column(name = "DATA_INCLUSAO")
    private Date dataInclusao;

    /**
     * Início da titularidade para a cota
     */
    @Temporal(TemporalType.DATE)
    @Column(name = "INICIO_TITULARIDADE", nullable = false)
    private Date inicio;

    /**
     * Fim da titularidade para a cota
     */
    @Temporal(TemporalType.DATE)
    @Column(name = "FIM_TITULARIDADE", nullable = false)
    private Date fim;

    /**
     * Número da cota durante a titularidade
     */
    @Column(name = "NUMERO_COTA", nullable = false)
    private Integer numeroCota;

    /**
     * Situação do cadastro da cota na alteração da titularidade
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
     * Flag indicando se a cota emite NFE
     */
    @Column(name = "EXIGE_NF_E")
    private boolean exigeNfe;
    
    /**
     * Flag indicando se a cota emite NFE
     */
    @Column(name = "CONTRIBUINTE_ICMS")
    private boolean contribuinteICMS;
    
    /**
     * Email de utilização da NFE
     */
    @Column(name = "EMAIL_NFE")
    private String emailNfe;

    /**
     * Pessoa Física titular da cota do histórico de titularidade 
     */
    @Embedded
    private HistoricoTitularidadeCotaPessoaFisica pessoaFisica;

    /**
     * Pessoa Jurídica titular da cota do histórico de titularidade 
     */
    @Embedded
    private HistoricoTitularidadeCotaPessoaJuridica pessoaJuridica;

    /**
     * Expectativa de faturamento da cota do histórico de titularidade 
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "CLASSIFICACAO_EXPECTATIVA_FATURAMENTO")
    private ClassificacaoEspectativaFaturamento classificacaoExpectativaFaturamento;

    /**
     * Endereços do histórico de titularidade 
     */
    @ElementCollection
    @CollectionTable(name = "HISTORICO_TITULARIDADE_COTA_ENDERECO", 
        joinColumns = { @JoinColumn(name = "HISTORICO_TITULARIDADE_COTA_ID") })
    private Collection<HistoricoTitularidadeCotaEndereco> enderecos;

    /**
     * Telefones do histórico de titularidade 
     */
    @ElementCollection
    @CollectionTable(name = "HISTORICO_TITULARIDADE_COTA_TELEFONE", 
        joinColumns = { @JoinColumn(name = "HISTORICO_TITULARIDADE_COTA_ID") })
    private Collection<HistoricoTitularidadeCotaTelefone> telefones;

    /**
     * Fornecedores associados a titularidade da cota
     */
    @OneToMany(cascade = { CascadeType.ALL })
    @JoinColumn(name = "HISTORICO_TITULARIDADE_COTA_ID")
    private Collection<HistoricoTitularidadeCotaFornecedor> fornecedores;

    /**
     * PDV's da cota
     */
    @OneToMany(mappedBy = "historicoTitularidadeCota", cascade = { CascadeType.ALL })
    private Collection<HistoricoTitularidadeCotaPDV> pdvs;

    /**
     * Garantias da cota no histórico de titularidade 
     */
    @OneToMany(mappedBy = "historicoTitularidadeCota", cascade = { CascadeType.ALL })
    private Collection<HistoricoTitularidadeCotaGarantia> garantias;

    /**
     * Início do período base para cota no histórico de titularidade 
     */
    @Temporal(TemporalType.DATE)
    @Column(name = "INICIO_PERIODO_COTA_BASE")
    private Date inicioPeriodoCotaBase;

    /**
     * Fim do período base para a cota no histórico de titularidade 
     */
    @Temporal(TemporalType.DATE)
    @Column(name = "FIM_PERIODO_COTA_BASE")
    private Date fimPeriodoCotaBase;

    /**
     * Cotas utilizadas como referência no histórico de titularidade 
     */
    @ElementCollection
    @CollectionTable(name = "HISTORICO_TITULARIDADE_COTA_REFERENCIA_COTA", 
        joinColumns = { @JoinColumn(name = "HISTORICO_TITULARIDADE_COTA_ID") })
    private Collection<HistoricoTitularidadeCotaReferenciaCota> referencias;

    /**
     * Informações de desconto do histórico de titularidade 
     */
    @OneToMany(mappedBy = "historicoTitularidadeCota", cascade = { CascadeType.ALL })
    private Collection<HistoricoTitularidadeCotaDesconto> descontos;
    
    @OneToOne(cascade = { CascadeType.ALL })
    @JoinColumn(name = "HISTORICO_TITULARIDADE_COTA_FINANCEIRO_ID")
    private HistoricoTitularidadeCotaFinanceiro financeiro;
    
    /**
     * Parâmetros de distribuição do histórico de titularidade
     */
    @Embedded
    private HistoricoTitularidadeCotaDistribuicao distribuicao;
    
    /**
     * Sócios da cota do histórico de titularidade
     */
    @OneToMany(cascade = { CascadeType.ALL })
    @JoinColumn(name = "HISTORICO_TITULARIDADE_COTA_ID")
    private Collection<HistoricoTitularidadeCotaSocio> socios;
   
    /**
     * Box do histórico de titularidade da cota
     */
    @Column(name = "BOX")
    private String box;

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
     * @return the dataInclusao
     */
    public Date getDataInclusao() {
        return dataInclusao;
    }

    /**
     * @param dataInclusao the dataInclusao to set
     */
    public void setDataInclusao(Date dataInclusao) {
        this.dataInclusao = dataInclusao;
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
     * @return the exigeNfe
     */
    public boolean isExigeNfe() {
        return exigeNfe;
    }

    /**
     * @param exigeNfe the exigeNfe to set
     */
    public void setExigeNfe(boolean exigeNfe) {
        this.exigeNfe = exigeNfe;
    }
    
    public boolean isContribuinteICMS() {
		return contribuinteICMS;
	}

	public void setContribuinteICMS(boolean contribuinteICMS) {
		this.contribuinteICMS = contribuinteICMS;
	}

	/**
     * @return the emailNfe
     */
    public String getEmailNfe() {
        return emailNfe;
    }

    /**
     * @param emailNfe the emailNfe to set
     */
    public void setEmailNfe(String emailNfe) {
        this.emailNfe = emailNfe;
    }

    /**
     * @return the pessoaFisica
     */
    public HistoricoTitularidadeCotaPessoaFisica getPessoaFisica() {
        return pessoaFisica;
    }

    /**
     * @param pessoaFisica
     *            the pessoaFisica to set
     */
    public void setPessoaFisica(HistoricoTitularidadeCotaPessoaFisica pessoaFisica) {
        this.pessoaFisica = pessoaFisica;
    }

    /**
     * @return the pessoaJuridica
     */
    public HistoricoTitularidadeCotaPessoaJuridica getPessoaJuridica() {
        return pessoaJuridica;
    }

    /**
     * @param pessoaJuridica
     *            the pessoaJuridica to set
     */
    public void setPessoaJuridica(
            HistoricoTitularidadeCotaPessoaJuridica pessoaJuridica) {
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
    public Collection<HistoricoTitularidadeCotaReferenciaCota> getReferencias() {
        return referencias;
    }

    /**
     * @param referencias
     *            the referencias to set
     */
    public void setReferencias(
            Collection<HistoricoTitularidadeCotaReferenciaCota> referencias) {
        this.referencias = referencias;
    }

    /**
     * @return the descontos
     */
    public Collection<HistoricoTitularidadeCotaDesconto> getDescontos() {
        return descontos;
    }

    /**
     * @param descontos
     *            the descontos to set
     */
    public void setDescontos(
            Collection<HistoricoTitularidadeCotaDesconto> descontos) {
        this.descontos = descontos;
    }

    /**
     * @return the financeiro
     */
    public HistoricoTitularidadeCotaFinanceiro getFinanceiro() {
        return financeiro;
    }

    /**
     * @param financeiro the financeiro to set
     */
    public void setFinanceiro(HistoricoTitularidadeCotaFinanceiro financeiro) {
        this.financeiro = financeiro;
    }

    /**
     * @return the distribuicao
     */
    public HistoricoTitularidadeCotaDistribuicao getDistribuicao() {
        return distribuicao;
    }

    /**
     * @param distribuicao the distribuicao to set
     */
    public void setDistribuicao(HistoricoTitularidadeCotaDistribuicao distribuicao) {
        this.distribuicao = distribuicao;
        if (this.distribuicao != null) {
            this.distribuicao.setHistoricoTitularidadeCota(this);
        }
    }

    /**
     * @return the socios
     */
    public Collection<HistoricoTitularidadeCotaSocio> getSocios() {
        return socios;
    }

    /**
     * @param socios the socios to set
     */
    public void setSocios(Collection<HistoricoTitularidadeCotaSocio> socios) {
        this.socios = socios;
    }
    
    /**
     * @return the box
     */
    public String getBox() {
        return box;
    }

    /**
     * @param box the box to set
     */
    public void setBox(String box) {
        this.box = box;
    }

    public HistoricoTitularidadeCotaPessoa getPessoa() {
        return isPessoaFisica() ? pessoaFisica : pessoaJuridica;
    }
    
    /**
     * Verifica se a pessoa associada ao histórico de titularidade é uma pessoa
     * física
     * 
     * @return true se o pessoa associada for uma pessoa física, false caso
     *         contrário
     */
    public boolean isPessoaFisica() {
        return pessoaFisica != null;
    }
    
    /**
     * Adiciona um endereço ao histórico de titularidade da cota
     * 
     * @param endereco
     *            endereço para inclusão
     */
    public void addEndereco(HistoricoTitularidadeCotaEndereco endereco) {
        if (enderecos == null) {
            enderecos = new ArrayList<HistoricoTitularidadeCotaEndereco>();
        }
        enderecos.add(endereco);
    }

    /**
     * Adiciona um telefone ao histórico de titularidade da cota
     * 
     * @param telefone
     *            telefone para inclusão
     */
    public void addTelefone(HistoricoTitularidadeCotaTelefone telefone) {
       if (telefones == null) {
           telefones = new ArrayList<HistoricoTitularidadeCotaTelefone>();
       }
       telefones.add(telefone);
    }

    /**
     * Adiciona um pdv ao histórico de titularidade da cota
     * @param pdv para inclusão
     */
    public void addPdv(HistoricoTitularidadeCotaPDV pdv) {
        if (pdvs == null) {
            pdvs = new ArrayList<HistoricoTitularidadeCotaPDV>();
        }
        pdv.setHistoricoTitularidadeCota(this);
        pdvs.add(pdv);
    }
    
    /**
     * Adiciona um cota referência ao histórico de titularidade da cota
     * 
     * @param referencia
     *            referência para inclusão
     */
    public void addCotaReferencia(HistoricoTitularidadeCotaReferenciaCota referencia) {
        if (referencias == null) {
            referencias = new ArrayList<HistoricoTitularidadeCotaReferenciaCota>();
        }
        referencias.add(referencia);
    }

    /**
     * Adiciona um fornecedor ao histórico de titularidade da cota
     * 
     * @param fornecedor
     *            fornecedor para inclusão
     */
    public void addFornecedor(HistoricoTitularidadeCotaFornecedor fornecedor) {
        if (fornecedores == null) {
            fornecedores = new ArrayList<HistoricoTitularidadeCotaFornecedor>();
        }
        fornecedores.add(fornecedor);
    }

    /**
     * Recupera os descontos de produto associados ao histórico de titularidade
     * da cota
     * 
     * @return coleção de descontos de produto associados ao histórico de
     *         titularidade da cota
     */
    public Collection<HistoricoTitularidadeCotaDescontoProduto> getDescontosProduto() {
        List<HistoricoTitularidadeCotaDescontoProduto> descontosProduto = new ArrayList<HistoricoTitularidadeCotaDescontoProduto>();
        for (HistoricoTitularidadeCotaDesconto desconto : descontos) {
            if (desconto instanceof HistoricoTitularidadeCotaDescontoProduto) {
                descontosProduto.add(HistoricoTitularidadeCotaDescontoProduto.class.cast(desconto));
            }
        }
        return descontosProduto;
    }
    
    /**
     * Recupera os descontos da cota associados ao histórico de titularidade
     * da cota
     * 
     * @return coleção de descontos da cota associados ao histórico de
     *         titularidade da cota
     */
    public Collection<HistoricoTitularidadeCotaDescontoCota> getDescontosCota() {
        List<HistoricoTitularidadeCotaDescontoCota> descontosCota = new ArrayList<HistoricoTitularidadeCotaDescontoCota>();
        for (HistoricoTitularidadeCotaDesconto desconto : descontos) {
            if (desconto instanceof HistoricoTitularidadeCotaDescontoCota) {
                descontosCota.add(HistoricoTitularidadeCotaDescontoCota.class.cast(desconto));
            }
        }
        return descontosCota;
    }

    /**
     * Adiciona um desconto aos descontos associados ao histórico de
     * titularidade da cota
     * 
     * @param desconto
     *            desconto para inclusão
     */
    public void addDesconto(HistoricoTitularidadeCotaDesconto desconto) {
        if (descontos == null) {
            descontos = new ArrayList<HistoricoTitularidadeCotaDesconto>();
        }
        desconto.setHistoricoTitularidadeCota(this);
        descontos.add(desconto);
    }

    /**
     * Associa um sócio ao histórico de titularidade da cota
     * 
     * @param socio
     *            sócio para inclusão
     */
    public void addSocio(HistoricoTitularidadeCotaSocio socio) {
       if (socios == null) {
           socios = new ArrayList<HistoricoTitularidadeCotaSocio>();
       }
       socios.add(socio);
    }

    /**
     * Adiciona uma garantia ao histórico de titularidade da cota
     * 
     * @param garantia
     *            garantia para inclusão
     */
    public void addGarantia(HistoricoTitularidadeCotaGarantia garantia) {
        if (garantias == null) {
            garantias = new ArrayList<HistoricoTitularidadeCotaGarantia>();
        }
        garantia.setHistoricoTitularidadeCota(this);
        garantias.add(garantia);
    }
    
    /**
     * Retorna o tipo de garantia do histórico de titularidade da cota
     * 
     * @return tipo de garantia aceita no histórico de titularidade da cota
     */
    public TipoGarantia getTipoGarantia() {
        if (garantias == null || garantias.isEmpty()) {
            return null;
        }
        return garantias.iterator().next().getTipoGarantia();
    }
    
    /**
     * Retorna a garantia do tipo fiador do histórico de titularidade da cota
     * @return garantia fiador do histórico de titularidade da cota
     */
    public HistoricoTitularidadeCotaFiador getGarantiaFiador() {
        if (TipoGarantia.FIADOR == getTipoGarantia()) {
            return HistoricoTitularidadeCotaFiador.class.cast(garantias.iterator().next());
        }
        return null;
    }
    
    /**
     * Retorna a garantia do tipo cheque caução do histórico de titularidade da
     * cota
     * 
     * @return garantia cheque caução do histórico de titularidade da cota
     */
    public HistoricoTitularidadeCotaChequeCaucao getGarantiaChequeCaucao() {
        if (TipoGarantia.CHEQUE_CAUCAO == getTipoGarantia()) {
            return HistoricoTitularidadeCotaChequeCaucao.class.cast(garantias.iterator().next());
        }
        return null;
    }
    
    /***
     * Retorna as garantias do tipo imóvel do histórico de titularidade da cota
     * 
     * @return coleção de garantias do tipo imóvel para o histórico de titularidade da cota
     */
    public Collection<HistoricoTitularidadeCotaImovel> getGarantiasImovel() {
        List<HistoricoTitularidadeCotaImovel> imoveis = new ArrayList<HistoricoTitularidadeCotaImovel>();
        if (TipoGarantia.IMOVEL == getTipoGarantia()) {
            for(HistoricoTitularidadeCotaGarantia garantia : garantias) {
                imoveis.add(HistoricoTitularidadeCotaImovel.class.cast(garantia));
            }
        }
        return imoveis;
    }
    
    /**
     * Retorna a garantia do tipo nota promissória do histórico de titularidade da
     * cota
     * 
     * @return garantia nota promissória do histórico de titularidade da cota
     */
    public HistoricoTitularidadeCotaNotaPromissoria getGarantiaNotaPromissoria() {
        if (TipoGarantia.NOTA_PROMISSORIA == getTipoGarantia()) {
            return HistoricoTitularidadeCotaNotaPromissoria.class.cast(garantias.iterator().next());
        }
        return null;
    }
    
    /**
     * Retorna a garantia do tipo caução líquida do histórico de titularidade da
     * cota
     * 
     * @return garantia caução líquida do histórico de titularidade da cota
     */
    public HistoricoTitularidadeCotaCaucaoLiquida getGarantiaCaucaoLiquida() {
        if (TipoGarantia.CAUCAO_LIQUIDA == getTipoGarantia()) {
            return HistoricoTitularidadeCotaCaucaoLiquida.class.cast(garantias.iterator().next());
        }
        return null;
    }

    /***
     * Retorna as garantias do tipo outros do histórico de titularidade da cota
     * 
     * @return coleção de garantias do tipo outros para o histórico de
     *         titularidade da cota
     */
    public Collection<HistoricoTitularidadeCotaOutros> getGarantiasOutros() {
        List<HistoricoTitularidadeCotaOutros> outros = new ArrayList<HistoricoTitularidadeCotaOutros>();
        if (TipoGarantia.OUTROS == getTipoGarantia()) {
            for (HistoricoTitularidadeCotaGarantia garantia : garantias) {
                outros.add(HistoricoTitularidadeCotaOutros.class.cast(garantia));
            }
        }
        return outros;
    }

    @Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.getId() == null) ? 0 : this.getId().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		HistoricoTitularidadeCota other = (HistoricoTitularidadeCota) obj;
		if (this.getId() == null) {
			if (other.getId() != null)
				return false;
		} else if (!this.getId().equals(other.getId()))
			return false;
		return true;
	}
}
