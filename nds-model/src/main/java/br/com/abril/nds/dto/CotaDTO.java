package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import br.com.abril.nds.model.cadastro.ClassificacaoEspectativaFaturamento;
import br.com.abril.nds.model.cadastro.EstadoCivil;
import br.com.abril.nds.model.cadastro.Sexo;
import br.com.abril.nds.model.cadastro.SituacaoCadastro;
import br.com.abril.nds.model.cadastro.TipoCota;
import br.com.abril.nds.model.cadastro.TipoDistribuicaoCota;

public class CotaDTO implements Serializable {

    /**
     * Serial version UID
     */
    private static final long serialVersionUID = 1L;

    /**
     * Atributos utilizados no grid da consulta
     */
    private Integer numeroCota;
    private String nomePessoa;
    private String numeroCpfCnpj;
    private String contato;
    private String telefone;
    private String email;
    private SituacaoCadastro status;
    private Long idCota;

    /**
     * Atributos utilizados no cadastro da cota
     */
    private Long idBox;
    private TipoPessoa tipoPessoa;
    private Date dataInclusao;
    private String razaoSocial;
    private String nomeFantasia;
    private String numeroCnpj;
    private String inscricaoEstadual;
    private String inscricaoMunicipal;
    private String emailNF;
    private boolean exigeNFE;
    private boolean contribuinteICMS;
    private Date inicioPeriodo;
    private Date fimPeriodo;
    private Integer historicoPrimeiraCota;
    private Integer historicoSegundaCota;
    private Integer historicoTerceiraCota;
    private BigDecimal historicoPrimeiraPorcentagem;
    private BigDecimal historicoSegundaPorcentagem;
    private BigDecimal historicoTerceiraPorcentagem;
    private ClassificacaoEspectativaFaturamento classificacaoSelecionada;
    private List<ItemDTO<String, String>> listaClassificacao;
    private String numeroCPF;
    private String numeroRG;
    private Date dataNascimento;
    private String orgaoEmissor;
    private String estadoSelecionado;
    private EstadoCivil estadoCivilSelecionado;
    private Sexo sexoSelecionado;
    private String nacionalidade;
    private String natural;
    private String descricaoBox;
    private Boolean recebeComplementar;
    private List<TitularidadeCotaDTO> proprietarios = new ArrayList<TitularidadeCotaDTO>();
    private boolean alteracaoTitularidade;
    private TipoDistribuicaoCota tipoDistribuicaoCota;
    private String tipoCota;
    private TipoCota tipoCotaFinanceiro;
    private BigDecimal percentualCotaBase = BigDecimal.ZERO;
    private Integer qtdeRankingSegmento;
    private BigDecimal faturamento;
    private Date dataGeracaoRank;
    private Date mixDataAlteracao;
    private BigInteger mixRepMin;
    private BigInteger mixRepMax;
    private String nomeUsuario;
    
    private List<CotaBaseDTO> cotasBases;

    private Date fxDataAlteracao;
    private Integer fxEdicaoInicial;
    private Integer fxEdicaoFinal;
    private Integer fxEdicoesAtendidas;
    private Integer fxQuantidadeEdicoes;
    private Integer fxQuantidadeExemplares;

    public String getNumeroRG() {
        return numeroRG;
    }

    public void setNumeroRG(String numeroRG) {
        this.numeroRG = numeroRG;
    }

    public Date getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(Date dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getOrgaoEmissor() {
        return orgaoEmissor;
    }

    public void setOrgaoEmissor(String orgaoEmissor) {
        this.orgaoEmissor = orgaoEmissor;
    }

    public String getEstadoSelecionado() {
        return estadoSelecionado;
    }

    public void setEstadoSelecionado(String estadoSelecionado) {
        this.estadoSelecionado = estadoSelecionado;
    }

    public EstadoCivil getEstadoCivilSelecionado() {
        return estadoCivilSelecionado;
    }

    public void setEstadoCivilSelecionado(EstadoCivil estadoCivilSelecionado) {
        this.estadoCivilSelecionado = estadoCivilSelecionado;
    }

    public Sexo getSexoSelecionado() {
        return sexoSelecionado;
    }

    public void setSexoSelecionado(Sexo sexoSelecionado) {
        this.sexoSelecionado = sexoSelecionado;
    }

    public String getNacionalidade() {
        return nacionalidade;
    }

    public void setNacionalidade(String nacionalidade) {
        this.nacionalidade = nacionalidade;
    }

    public String getNatural() {
        return natural;
    }

    public void setNatural(String natural) {
        this.natural = natural;
    }

    public String getDescricaoBox() {
        return descricaoBox;
    }

    public void setDescricaoBox(String descricaoBox) {
        this.descricaoBox = descricaoBox;
    }

    /**
     * @return the numeroCPF
     */
    public String getNumeroCPF() {
        return numeroCPF;
    }

    /**
     * @param numeroCPF the numeroCPF to set
     */
    public void setNumeroCPF(String numeroCPF) {
        this.numeroCPF = numeroCPF;
    }

    /**
     * @return the numeroCota
     */
    public Integer getNumeroCota() {
        return numeroCota;
    }

    /**
     * @param numeroCota the numeroCota to set
     */
    public void setNumeroCota(Integer numeroCota) {
        this.numeroCota = numeroCota;
    }

    /**
     * @return the nomePessoa
     */
    public String getNomePessoa() {
        return nomePessoa;
    }

    /**
     * @param nomePessoa the nomePessoa to set
     */
    public void setNomePessoa(String nomePessoa) {
        this.nomePessoa = nomePessoa;
    }

    /**
     * @return the numeroCpfCnpj
     */
    public String getNumeroCpfCnpj() {
        return numeroCpfCnpj;
    }

    /**
     * @param numeroCpfCnpj the numeroCpfCnpj to set
     */
    public void setNumeroCpfCnpj(String numeroCpfCnpj) {
        this.numeroCpfCnpj = numeroCpfCnpj;
    }

    /**
     * @return the contato
     */
    public String getContato() {
        return contato;
    }

    /**
     * @param contato the contato to set
     */
    public void setContato(String contato) {
        this.contato = contato;
    }

    /**
     * @return the telefone
     */
    public String getTelefone() {
        return telefone;
    }

    /**
     * @param telefone the telefone to set
     */
    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the status
     */
    public SituacaoCadastro getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(SituacaoCadastro status) {
        this.status = status;
    }

    /**
     * @return the idCota
     */
    public Long getIdCota() {
        return idCota;
    }

    /**
     * @param idCota the idCota to set
     */
    public void setIdCota(Long idCota) {
        this.idCota = idCota;
    }

    /**
     * @return the idBox
     */
    public Long getIdBox() {
        return idBox;
    }

    /**
     * @param idBox the idBox to set
     */
    public void setIdBox(Long idBox) {
        this.idBox = idBox;
    }

    /**
     * @return the tipoPessoa
     */
    public TipoPessoa getTipoPessoa() {
        return tipoPessoa;
    }

    /**
     * @param tipoPessoa the tipoPessoa to set
     */
    public void setTipoPessoa(TipoPessoa tipoPessoa) {
        this.tipoPessoa = tipoPessoa;
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
     * @return the numeroCnpj
     */
    public String getNumeroCnpj() {
        return numeroCnpj;
    }

    /**
     * @param numeroCnpj the numeroCnpj to set
     */
    public void setNumeroCnpj(String numeroCnpj) {
        this.numeroCnpj = numeroCnpj;
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

    /**
     * @return the emailNF
     */
    public String getEmailNF() {
        return emailNF;
    }

    /**
     * @param emailNF the emailNF to set
     */
    public void setEmailNF(String emailNF) {
        this.emailNF = emailNF;
    }

    /**
     * @return the exigeNFE
     */
    public boolean isExigeNFE() {
        return exigeNFE;
    }

    /**
     * @param exigeNFE the exigeNFE to set
     */
    public void setExigeNFE(boolean exigeNFE) {
        this.exigeNFE = exigeNFE;
    }

    /**
     * @return the inicioPeriodo
     */
    public Date getInicioPeriodo() {
        return inicioPeriodo;
    }

    /**
     * @param inicioPeriodo the inicioPeriodo to set
     */
    public void setInicioPeriodo(Date inicioPeriodo) {
        this.inicioPeriodo = inicioPeriodo;
    }

    /**
     * @return the fimPeriodo
     */
    public Date getFimPeriodo() {
        return fimPeriodo;
    }

    /**
     * @param fimPeriodo the fimPeriodo to set
     */
    public void setFimPeriodo(Date fimPeriodo) {
        this.fimPeriodo = fimPeriodo;
    }

    /**
     * @return the historicoPrimeiraCota
     */
    public Integer getHistoricoPrimeiraCota() {
        return historicoPrimeiraCota;
    }

    /**
     * @param historicoPrimeiraCota the historicoPrimeiraCota to set
     */
    public void setHistoricoPrimeiraCota(Integer historicoPrimeiraCota) {
        this.historicoPrimeiraCota = historicoPrimeiraCota;
    }

    /**
     * @return the historicoSegundaCota
     */
    public Integer getHistoricoSegundaCota() {
        return historicoSegundaCota;
    }

    /**
     * @param historicoSegundaCota the historicoSegundaCota to set
     */
    public void setHistoricoSegundaCota(Integer historicoSegundaCota) {
        this.historicoSegundaCota = historicoSegundaCota;
    }

    /**
     * @return the historicoterceiraCota
     */
    public Integer getHistoricoTerceiraCota() {
        return historicoTerceiraCota;
    }

    /**
     * @param historicoterceiraCota the historicoterceiraCota to set
     */
    public void setHistoricoTerceiraCota(Integer historicoterceiraCota) {
        this.historicoTerceiraCota = historicoterceiraCota;
    }

    /**
     * @return the historicoPrimeiraPorcentagem
     */
    public BigDecimal getHistoricoPrimeiraPorcentagem() {
        return historicoPrimeiraPorcentagem;
    }

    /**
     * @param historicoPrimeiraPorcentagem the historicoPrimeiraPorcentagem to set
     */
    public void setHistoricoPrimeiraPorcentagem(
            BigDecimal historicoPrimeiraPorcentagem) {
        this.historicoPrimeiraPorcentagem = historicoPrimeiraPorcentagem;
    }

    /**
     * @return the historicoSegundaPorcentagem
     */
    public BigDecimal getHistoricoSegundaPorcentagem() {
        return historicoSegundaPorcentagem;
    }

    /**
     * @param historicoSegundaPorcentagem the historicoSegundaPorcentagem to set
     */
    public void setHistoricoSegundaPorcentagem(
            BigDecimal historicoSegundaPorcentagem) {
        this.historicoSegundaPorcentagem = historicoSegundaPorcentagem;
    }

    /**
     * @return the historicoTerceiraPorcentagem
     */
    public BigDecimal getHistoricoTerceiraPorcentagem() {
        return historicoTerceiraPorcentagem;
    }

    /**
     * @param historicoTerceiraPorcentagem the historicoTerceiraPorcentagem to set
     */
    public void setHistoricoTerceiraPorcentagem(
            BigDecimal historicoTerceiraPorcentagem) {
        this.historicoTerceiraPorcentagem = historicoTerceiraPorcentagem;
    }

    /**
     * @return the classificacaoSelecionada
     */
    public ClassificacaoEspectativaFaturamento getClassificacaoSelecionada() {
        return classificacaoSelecionada;
    }

    /**
     * @param classificacaoSelecionada the classificacaoSelecionada to set
     */
    public void setClassificacaoSelecionada(
            ClassificacaoEspectativaFaturamento classificacaoSelecionada) {
        this.classificacaoSelecionada = classificacaoSelecionada;
    }

    /**
     * @return the listaClassificacao
     */
    public List<ItemDTO<String, String>> getListaClassificacao() {
        return listaClassificacao;
    }

    /**
     * @param listaClassificacao the listaClassificacao to set
     */
    public void setListaClassificacao(
            List<ItemDTO<String, String>> listaClassificacao) {
        this.listaClassificacao = listaClassificacao;
    }

    /**
     * @return the proprietarios
     */
    public Collection<TitularidadeCotaDTO> getProprietarios() {
        return proprietarios;
    }

    /**
     * @return the isAlteracaoTitularidade
     */
    public boolean isAlteracaoTitularidade() {
        return alteracaoTitularidade;
    }

    /**
     * @param isAlteracaoTitularidade the isAlteracaoTitularidade to set
     */
    public void setAlteracaoTitularidade(boolean isAlteracaoTitularidade) {
        this.alteracaoTitularidade = isAlteracaoTitularidade;
    }

    /**
     * Adiciona um proprietário a cota
     *
     * @param proprietario proprietário para inclusão
     */
    public void addProprietario(TitularidadeCotaDTO proprietario) {
        proprietarios.add(proprietario);
    }

    /**
     * Adiciona um item de classificação à lista de classificação de faturamento
     *
     * @param classificacao classificação para inclusão
     */
    public void addItemClassificacaoFaturamento(ClassificacaoEspectativaFaturamento classificacao) {
        if (classificacao != null) {
            if (this.listaClassificacao == null) {
                this.listaClassificacao = new ArrayList<ItemDTO<String, String>>();
            }
            this.listaClassificacao.add(new ItemDTO<String, String>(classificacao.name(), classificacao.getDescricao()));
        }
    }

    public TipoDistribuicaoCota getTipoDistribuicaoCota() {
        return tipoDistribuicaoCota;
    }

    public void setTipoDistribuicaoCota(TipoDistribuicaoCota tipoDistribuicaoCota) {
        this.tipoDistribuicaoCota = tipoDistribuicaoCota;
    }

    public String getTipoCota() {
        return tipoCota;
    }

    public void setTipoCota(String tipoCota) {
        this.tipoCota = tipoCota;
    }

	public TipoCota getTipoCotaFinanceiro() {
		return tipoCotaFinanceiro;
	}

	public void setTipoCotaFinanceiro(TipoCota tipoCotaFinanceiro) {
		this.tipoCotaFinanceiro = tipoCotaFinanceiro;
	}

	public BigDecimal getPercentualCotaBase() {
        return percentualCotaBase;
    }

    public void setPercentualCotaBase(BigDecimal percentualCotaBase) {
        this.percentualCotaBase = percentualCotaBase;
    }

    public Boolean isRecebeComplementar() {
        return recebeComplementar;
    }

    public void setRecebeComplementar(Boolean recebeComplementar) {
        this.recebeComplementar = recebeComplementar;
    }

	public Integer getQtdeRankingSegmento() {
		return qtdeRankingSegmento;
	}

	public void setQtdeRankingSegmento(Integer qtdeRankingSegmento) {
		this.qtdeRankingSegmento = qtdeRankingSegmento;
	}

	public BigDecimal getFaturamento() {
        return faturamento;
    }

    public void setFaturamento(BigDecimal faturamento) {
        this.faturamento = faturamento;
    }

    public Date getDataGeracaoRank() {
        return dataGeracaoRank;
    }

    public void setDataGeracaoRank(Date dataGeracaoRank) {
        this.dataGeracaoRank = dataGeracaoRank;
    }

    public Date getMixDataAlteracao() {
        return mixDataAlteracao;
    }

    public void setMixDataAlteracao(Date mixDataAlteracao) {
        this.mixDataAlteracao = mixDataAlteracao;
    }

    public BigInteger getMixRepMin() {
        return mixRepMin;
    }

    public void setMixRepMin(BigInteger mixRepMin) {
        this.mixRepMin = mixRepMin;
    }

    public BigInteger getMixRepMax() {
        return mixRepMax;
    }

    public void setMixRepMax(BigInteger mixRepMax) {
        this.mixRepMax = mixRepMax;
    }

    public String getNomeUsuario() {
        return nomeUsuario;
    }

    public void setNomeUsuario(String nomeUsuario) {
        this.nomeUsuario = nomeUsuario;
    }

    public enum TipoPessoa {
        JURIDICA, FISICA;
    }

	public List<CotaBaseDTO> getCotasBases() {
		return cotasBases;
	}

	public void setCotasBases(List<CotaBaseDTO> cotasBases) {
		this.cotasBases = cotasBases;
	}
    public Date getFxDataAlteracao() {
        return fxDataAlteracao;
    }

    public void setFxDataAlteracao(Date fxDataAlteracao) {
        this.fxDataAlteracao = fxDataAlteracao;
    }

    public Integer getFxEdicaoInicial() {
        return fxEdicaoInicial;
    }

    public void setFxEdicaoInicial(Integer fxEdicaoInicial) {
        this.fxEdicaoInicial = fxEdicaoInicial;
    }

    public Integer getFxEdicaoFinal() {
        return fxEdicaoFinal;
    }

    public void setFxEdicaoFinal(Integer fxEdicaoFinal) {
        this.fxEdicaoFinal = fxEdicaoFinal;
    }

    public Integer getFxEdicoesAtendidas() {
        return fxEdicoesAtendidas;
    }

    public void setFxEdicoesAtendidas(Integer fxEdicoesAtendidas) {
        this.fxEdicoesAtendidas = fxEdicoesAtendidas;
    }

    public Integer getFxQuantidadeEdicoes() {
        return fxQuantidadeEdicoes;
    }

    public void setFxQuantidadeEdicoes(Integer fxQuantidadeEdicoes) {
        this.fxQuantidadeEdicoes = fxQuantidadeEdicoes;
    }

    public Integer getFxQuantidadeExemplares() {
        return fxQuantidadeExemplares;
    }

    public void setFxQuantidadeExemplares(Integer fxQuantidadeExemplares) {
        this.fxQuantidadeExemplares = fxQuantidadeExemplares;
    }

	public boolean isContribuinteICMS() {
		return contribuinteICMS;
	}

	public void setContribuinteICMS(boolean contribuinteICMS) {
		this.contribuinteICMS = contribuinteICMS;
	}
}
