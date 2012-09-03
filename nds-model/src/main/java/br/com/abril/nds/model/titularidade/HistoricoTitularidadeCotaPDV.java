package br.com.abril.nds.model.titularidade;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.com.abril.nds.model.cadastro.pdv.CaracteristicasPDV;
import br.com.abril.nds.model.cadastro.pdv.StatusPDV;
import br.com.abril.nds.model.cadastro.pdv.TamanhoPDV;
import br.com.abril.nds.model.cadastro.pdv.TipoCaracteristicaSegmentacaoPDV;

/**
 * Representa o PDV no histórico de titularidade da cota
 * 
 * @author francisco.garcia
 * 
 */
@Entity
@Table(name = "HISTORICO_TITULARIDADE_COTA_PDV")
@SequenceGenerator(name = "HIST_TIT_COTA_PDV_SEQ", initialValue = 1, allocationSize = 1)
public class HistoricoTitularidadeCotaPDV implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "HIST_TIT_COTA_PDV_SEQ")
    @Column(name = "ID")
    private Long id;

    /**
     * Identificador do pdv de origem
     */
    @Column(name = "ID_ORIGEM")
    private Long idOrigem;

    @ManyToOne(optional = false)
    @JoinColumn(name = "HISTORICO_TITULARIDADE_COTA_ID")
    private HistoricoTitularidadeCota historicoTitularidadeCota;

    /**
     * Data de inclusão do pdv
     */
    @Temporal(TemporalType.DATE)
    @Column(name = "DATA_INCLUSAO")
    private Date dataInclusao;

    /**
     * Status do PDV
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS_PDV")
    private StatusPDV status;

    /**
     * Nome do PDV
     */
    @Column(name = "NOME", nullable = false)
    private String nome;

    /**
     * Nome do contato no PDV
     */
    @Column(name = "CONTATO")
    private String contato;

    /**
     * Site do PDV
     */
    @Column(name = "SITE")
    private String site;

    /**
     * Email do PDV
     */
    @Column(name = "EMAIL")
    private String email;

    /**
     * Ponto de referência do PDV
     */
    @Column(name = "PONTO_REFERENCIA")
    private String pontoReferencia;

    /**
     * Endereços do PDV
     */
    @ElementCollection
    @CollectionTable(name = "HISTORICO_TITULARIDADE_COTA_PDV_ENDERECO", joinColumns = { @JoinColumn(name = "HISTORICO_TITULARIDADE_COTA_PDV_ID") })
    private Collection<HistoricoTitularidadeCotaEndereco> enderecos;

    /**
     * Telefones do PDV
     */
    @ElementCollection
    @CollectionTable(name = "HISTORICO_TITULARIDADE_COTA_PDV_TELEFONE", joinColumns = { @JoinColumn(name = "HISTORICO_TITULARIDADE_COTA_PDV_ID") })
    private Collection<HistoricoTitularidadeCotaTelefone> telefones;

    /**
     * Flag indicando se o pdv esta dentro de outro estabelecimento
     */
    @Column(name = "DENTRO_OUTRO_ESTABELECIMENTO")
    private boolean dentroOutroEstabelecimento;

    /**
     * Flag indicando se o pdv possui arrendatário
     */
    @Column(name = "ARRENDATARIO")
    private boolean arrendatario;

    /**
     * Tamanho do PDV
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "TAMANHO_PDV", nullable = true)
    private TamanhoPDV tamanhoPDV;

    /**
     * Flag indicando se o PDV possui o Sistema IPV
     */
    @Column(name = "POSSUI_SISTEMA_IPV")
    private boolean possuiSistemaIPV;

    /**
     * Quantidade de funcionários do PDV
     */
    @Column(name = "QTDE_FUNCIONARIOS")
    private int qtdeFuncionarios;

    /**
     * Porcentagem que o PDV representa no total do faturamento da cota
     */
    @Column(name = "PORCENTAGEM_FATURAMENTO")
    private BigDecimal porcentagemFaturamento;

    /**
     * Períodos de funcionamento do PDV
     */
    @ElementCollection
    @CollectionTable(name = "HISTORICO_TITULARIDADE_COTA_FUNCIONAMENTO_PDV", joinColumns = { @JoinColumn(name = "HISTORICO_TITULARIDADE_COTA_PDV_ID") })
    private Collection<HistoricoTitularidadeCotaFuncionamentoPDV> periodos;

    /**
     * Licença municipal do PDV
     */
    @Embedded
    @AttributeOverrides(value = {
            @AttributeOverride(name = "codigo", column = @Column(name = "CODIGO_TIPO_LICENCA_MUNICIPAL")),
            @AttributeOverride(name = "descricao", column = @Column(name = "DESCRICAO_TIPO_LICENCA_MUNICIPAL")) })
    private HistoricoTitularidadeCotaCodigoDescricao tipoLicencaMunicipal;
    
    @Column(name = "NUMERO_LICENCA_MUNICIPAL")
    private String numeroLicencaMunicipal;
    
    @Column(name = "NOME_LICENCA_LICENCA_MUNICIPAL")
    private String nomeLicencaMunicipal;
    
    /**
     * Características do PDV
     */
    @Embedded
    private CaracteristicasPDV caracteristicas;

    /**
     * Tipo do ponto do PDV
     */
    @Embedded
    @AttributeOverrides(value = {
            @AttributeOverride(name = "codigo", column = @Column(name = "CODIGO_TIPO_PONTO")),
            @AttributeOverride(name = "descricao", column = @Column(name = "DESCRICAO_TIPO_PONTO")) })
    private HistoricoTitularidadeCotaCodigoDescricao tipoPonto;

    /**
     * Tipo característica segmentação do PDV
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "TIPO_CARACTERISTICA_PDV")
    private TipoCaracteristicaSegmentacaoPDV tipoCaracteristica;

    /**
     * Área de influência do PDV
     */
    @Embedded
    @AttributeOverrides(value = {
            @AttributeOverride(name = "codigo", column = @Column(name = "CODIGO_AREA_INFLUENCIA")),
            @AttributeOverride(name = "descricao", column = @Column(name = "DESCRICAO_AREA_INFLUENCIA")) })
    private HistoricoTitularidadeCotaCodigoDescricao areaInfluencia;

    /**
     * Materiais promocionais do PDV
     */
    @ElementCollection
    @CollectionTable(name = "HISTORICO_TITULARIDADE_COTA_MATERIAL_PROMOCIONAL_PDV", joinColumns = { @JoinColumn(name = "HISTORICO_TITULARIDADE_COTA_PDV_ID") })
    @AttributeOverrides(value = {
            @AttributeOverride(name = "codigo", column = @Column(name = "CODIGO_MATERIAL")),
            @AttributeOverride(name = "descricao", column = @Column(name = "DESCRICAO_MATERIAL")) })
    private Collection<HistoricoTitularidadeCotaCodigoDescricao> materiais;

    /**
     * Gerador de fluxo principal do PDV
     */
    @Embedded
    @AttributeOverrides(value = {
            @AttributeOverride(name = "codigo", column = @Column(name = "CODIGO_GERADOR_FLUXO_PRINCIPAL")),
            @AttributeOverride(name = "descricao", column = @Column(name = "DESCRICAO_GERADOR_FLUXO_PRINCIPAL")) })
    private HistoricoTitularidadeCotaCodigoDescricao geradorFluxoPrincipal;

    /**
     * Geradores de fluxo secundários do PDV
     */
    @ElementCollection
    @CollectionTable(name = "HISTORICO_TITULARIDADE_COTA_GERADOR_FLUXO_SECUNDARIO", joinColumns = { @JoinColumn(name = "HISTORICO_TITULARIDADE_COTA_PDV_ID") })
    @AttributeOverrides(value = {
            @AttributeOverride(name = "codigo", column = @Column(name = "CODIGO_GERADOR_FLUXO")),
            @AttributeOverride(name = "descricao", column = @Column(name = "DESCRICAO_GERADOR_FLUXO")) })
    private Collection<HistoricoTitularidadeCotaCodigoDescricao> geradoresFluxoSecundarios;

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
     * @return the idOrigem
     */
    public Long getIdOrigem() {
        return idOrigem;
    }

    /**
     * @param idOrigem
     *            the idOrigem to set
     */
    public void setIdOrigem(Long idOrigem) {
        this.idOrigem = idOrigem;
    }

    /**
     * @return the historicoTitularidadeCota
     */
    public HistoricoTitularidadeCota getHistoricoTitularidadeCota() {
        return historicoTitularidadeCota;
    }

    /**
     * @param historicoTitularidadeCota
     *            the historicoTitularidadeCota to set
     */
    public void setHistoricoTitularidadeCota(
            HistoricoTitularidadeCota historicoTitularidadeCota) {
        this.historicoTitularidadeCota = historicoTitularidadeCota;
    }

    /**
     * @return the dataInclusao
     */
    public Date getDataInclusao() {
        return dataInclusao;
    }

    /**
     * @param dataInclusao
     *            the dataInclusao to set
     */
    public void setDataInclusao(Date dataInclusao) {
        this.dataInclusao = dataInclusao;
    }

    /**
     * @return the status
     */
    public StatusPDV getStatus() {
        return status;
    }

    /**
     * @param status
     *            the status to set
     */
    public void setStatus(StatusPDV status) {
        this.status = status;
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
     * @return the contato
     */
    public String getContato() {
        return contato;
    }

    /**
     * @param contato
     *            the contato to set
     */
    public void setContato(String contato) {
        this.contato = contato;
    }

    /**
     * @return the site
     */
    public String getSite() {
        return site;
    }

    /**
     * @param site
     *            the site to set
     */
    public void setSite(String site) {
        this.site = site;
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
     * @return the pontoReferencia
     */
    public String getPontoReferencia() {
        return pontoReferencia;
    }

    /**
     * @param pontoReferencia
     *            the pontoReferencia to set
     */
    public void setPontoReferencia(String pontoReferencia) {
        this.pontoReferencia = pontoReferencia;
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
     * @return the dentroOutroEstabelecimento
     */
    public boolean isDentroOutroEstabelecimento() {
        return dentroOutroEstabelecimento;
    }

    /**
     * @param dentroOutroEstabelecimento
     *            the dentroOutroEstabelecimento to set
     */
    public void setDentroOutroEstabelecimento(boolean dentroOutroEstabelecimento) {
        this.dentroOutroEstabelecimento = dentroOutroEstabelecimento;
    }

    /**
     * @return the arrendatario
     */
    public boolean isArrendatario() {
        return arrendatario;
    }

    /**
     * @param arrendatario
     *            the arrendatario to set
     */
    public void setArrendatario(boolean arrendatario) {
        this.arrendatario = arrendatario;
    }

    /**
     * @return the tamanhoPDV
     */
    public TamanhoPDV getTamanhoPDV() {
        return tamanhoPDV;
    }

    /**
     * @param tamanhoPDV
     *            the tamanhoPDV to set
     */
    public void setTamanhoPDV(TamanhoPDV tamanhoPDV) {
        this.tamanhoPDV = tamanhoPDV;
    }

    /**
     * @return the possuiSistemaIPV
     */
    public boolean isPossuiSistemaIPV() {
        return possuiSistemaIPV;
    }

    /**
     * @param possuiSistemaIPV
     *            the possuiSistemaIPV to set
     */
    public void setPossuiSistemaIPV(boolean possuiSistemaIPV) {
        this.possuiSistemaIPV = possuiSistemaIPV;
    }

    /**
     * @return the qtdeFuncionarios
     */
    public int getQtdeFuncionarios() {
        return qtdeFuncionarios;
    }

    /**
     * @param qtdeFuncionarios
     *            the qtdeFuncionarios to set
     */
    public void setQtdeFuncionarios(int qtdeFuncionarios) {
        this.qtdeFuncionarios = qtdeFuncionarios;
    }

    /**
     * @return the porcentagemFaturamento
     */
    public BigDecimal getPorcentagemFaturamento() {
        return porcentagemFaturamento;
    }

    /**
     * @param porcentagemFaturamento
     *            the porcentagemFaturamento to set
     */
    public void setPorcentagemFaturamento(BigDecimal porcentagemFaturamento) {
        this.porcentagemFaturamento = porcentagemFaturamento;
    }

    /**
     * @return the periodos
     */
    public Collection<HistoricoTitularidadeCotaFuncionamentoPDV> getPeriodos() {
        return periodos;
    }

    /**
     * @param periodos
     *            the periodos to set
     */
    public void setPeriodos(
            Collection<HistoricoTitularidadeCotaFuncionamentoPDV> periodos) {
        this.periodos = periodos;
    }

    /**
     * @return the tipoLicencaMunicipal
     */
    public HistoricoTitularidadeCotaCodigoDescricao getTipoLicencaMunicipal() {
        return tipoLicencaMunicipal;
    }

    /**
     * @param tipoLicencaMunicipal the tipoLicencaMunicipal to set
     */
    public void setTipoLicencaMunicipal(
            HistoricoTitularidadeCotaCodigoDescricao tipoLicencaMunicipal) {
        this.tipoLicencaMunicipal = tipoLicencaMunicipal;
    }

    /**
     * @return the numeroLicencaMunicipal
     */
    public String getNumeroLicencaMunicipal() {
        return numeroLicencaMunicipal;
    }

    /**
     * @param numeroLicencaMunicipal the numeroLicencaMunicipal to set
     */
    public void setNumeroLicencaMunicipal(String numeroLicencaMunicipal) {
        this.numeroLicencaMunicipal = numeroLicencaMunicipal;
    }

    /**
     * @return the nomeLicencaMunicipal
     */
    public String getNomeLicencaMunicipal() {
        return nomeLicencaMunicipal;
    }

    /**
     * @param nomeLicencaMunicipal the nomeLicencaMunicipal to set
     */
    public void setNomeLicencaMunicipal(String nomeLicencaMunicipal) {
        this.nomeLicencaMunicipal = nomeLicencaMunicipal;
    }

    /**
     * @return the caracteristicas
     */
    public CaracteristicasPDV getCaracteristicas() {
        return caracteristicas;
    }

    /**
     * @param caracteristicas
     *            the caracteristicas to set
     */
    public void setCaracteristicas(CaracteristicasPDV caracteristicas) {
        this.caracteristicas = caracteristicas;
    }

    /**
     * @return the tipoPonto
     */
    public HistoricoTitularidadeCotaCodigoDescricao getTipoPonto() {
        return tipoPonto;
    }

    /**
     * @param tipoPonto
     *            the tipoPonto to set
     */
    public void setTipoPonto(HistoricoTitularidadeCotaCodigoDescricao tipoPonto) {
        this.tipoPonto = tipoPonto;
    }

    /**
     * @return the tipoCaracteristica
     */
    public TipoCaracteristicaSegmentacaoPDV getTipoCaracteristica() {
        return tipoCaracteristica;
    }

    /**
     * @param tipoCaracteristica
     *            the tipoCaracteristica to set
     */
    public void setTipoCaracteristica(
            TipoCaracteristicaSegmentacaoPDV tipoCaracteristica) {
        this.tipoCaracteristica = tipoCaracteristica;
    }

    /**
     * @return the areaInfluencia
     */
    public HistoricoTitularidadeCotaCodigoDescricao getAreaInfluencia() {
        return areaInfluencia;
    }

    /**
     * @param areaInfluencia
     *            the areaInfluencia to set
     */
    public void setAreaInfluencia(
            HistoricoTitularidadeCotaCodigoDescricao areaInfluencia) {
        this.areaInfluencia = areaInfluencia;
    }

    /**
     * @return the materiais
     */
    public Collection<HistoricoTitularidadeCotaCodigoDescricao> getMateriais() {
        return materiais;
    }

    /**
     * @param materiais
     *            the materiais to set
     */
    public void setMateriais(
            Collection<HistoricoTitularidadeCotaCodigoDescricao> materiais) {
        this.materiais = materiais;
    }

    /**
     * @return the geradorFluxoPrincipal
     */
    public HistoricoTitularidadeCotaCodigoDescricao getGeradorFluxoPrincipal() {
        return geradorFluxoPrincipal;
    }

    /**
     * @param geradorFluxoPrincipal
     *            the geradorFluxoPrincipal to set
     */
    public void setGeradorFluxoPrincipal(
            HistoricoTitularidadeCotaCodigoDescricao geradorFluxoPrincipal) {
        this.geradorFluxoPrincipal = geradorFluxoPrincipal;
    }

    /**
     * @return the geradoresFluxoSecundarios
     */
    public Collection<HistoricoTitularidadeCotaCodigoDescricao> getGeradoresFluxoSecundarios() {
        return geradoresFluxoSecundarios;
    }

    /**
     * @param geradoresFluxoSecundarios
     *            the geradoresFluxoSecundarios to set
     */
    public void setGeradoresFluxoSecundarios(
            Collection<HistoricoTitularidadeCotaCodigoDescricao> geradoresFluxoSecundarios) {
        this.geradoresFluxoSecundarios = geradoresFluxoSecundarios;
    }

    /**
     * Adiciona um período de funcionamento ao PDV
     * 
     * @param periodo
     *            período de funcionamento para inclusão
     */
    public void addPeriodoFuncionamento(
            HistoricoTitularidadeCotaFuncionamentoPDV periodo) {
        if (periodos == null) {
            periodos = new ArrayList<HistoricoTitularidadeCotaFuncionamentoPDV>();
        }
        periodos.add(periodo);
    }

}
