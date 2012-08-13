package br.com.abril.nds.model.cadastro;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.com.abril.nds.model.DiaSemana;
import br.com.abril.nds.model.LeiautePicking;

/**
 * Cadastro do Distribuidor
 * @author T30541
 * @version 1.0
 * @created 14-fev-2012 11:35:31
 */
@Entity
@Table(name = "DISTRIBUIDOR")
@SequenceGenerator(name="DISTRIB_SEQ", initialValue = 1, allocationSize = 1)
public class Distribuidor {

	@Id
	@GeneratedValue(generator = "DISTRIB_SEQ")
	@Column(name = "ID")
	private Long id;
	
	@Embedded
	private ParametroCobrancaDistribuidor parametroCobrancaDistribuidor;

	@Embedded
	private ParametrosRecolhimentoDistribuidor parametrosRecolhimentoDistribuidor;

	@Embedded
	private ParametrosAprovacaoDistribuidor parametrosAprovacaoDistribuidor;

	@Column(name = "CODIGO", nullable = false)
	private Integer codigo;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "DATA_OPERACAO", nullable = false)
	private Date dataOperacao;
	
	@OneToOne(optional = false)
	@JoinColumn(name = "PJ_ID")
	private PessoaJuridica juridica;
	
	@Column(name = "FATOR_DESCONTO")
	private BigDecimal fatorDesconto;
	
	@OneToMany(mappedBy="distribuidor")
	private Set<PoliticaCobranca> politicasCobranca = new HashSet<PoliticaCobranca>();
	
	@Embedded
	private PoliticaSuspensao politicaSuspensao;
	
	@OneToMany(mappedBy = "distribuidor")
	private List<EnderecoDistribuidor> enderecos = new ArrayList<EnderecoDistribuidor>();
	
	@OneToMany(mappedBy = "distribuidor")
	private List<TelefoneDistribuidor> telefones = new ArrayList<TelefoneDistribuidor>();
	
	/**
	 * Emissões de documentos controladas pelo distribuidor
	 */
	@OneToMany(mappedBy = "distribuidor")
	private List<ParametrosDistribuidorEmissaoDocumento> parametrosDistribuidorEmissaoDocumentos = new ArrayList<ParametrosDistribuidorEmissaoDocumento>();

	/**
	 * Faltas e sobras controladas pelo distribuidor
	 */
	@OneToMany(mappedBy = "distribuidor")
	private List<ParametrosDistribuidorFaltasSobras> parametrosDistribuidorFaltasSobras = new ArrayList<ParametrosDistribuidorFaltasSobras>();
	
	/**
	 * Flag que indica se o distribuidor aceita a conferência de um encalhe juramentado.
	 */
	@Column(name = "ACEITA_JURAMENTADO")
	private boolean aceitaJuramentado;
	
	/**
	 * Tipo da contabilização selecionada pelo distribuidor
	 * na chamada de encalhe
	 */
	@Column(name = "TIPO_CONT_CE")
	@Enumerated(EnumType.STRING)
	private TipoContabilizacaoCE tipoContabilizacaoCE;
	
	/**
	 * Flag indicando se a venda negativa deve ser supervisionada
	 */
	@Column(name = "SUPERVISIONA_VENDA_NEGATIVA")
	private boolean supervisionaVendaNegativa;
	
	/**
	 * Parametrização de Política de Chamadão do Distribuidor
	 */
	@Embedded
	private PoliticaChamadao politicaChamadao;
	
	/**
	 * Capacidade de distribuição diária do distribuidor, em número de exemplares
	 */
	@Column(name = "CAPACIDADE_DISTRIBUICAO", nullable = false)
	private BigDecimal capacidadeDistribuicao;
	
	/**
	 * Capacidade de recolhimento diária do distribuidor, em número de exemplares
	 */
	@Column(name = "CAPACIDADE_RECOLHIMENTO", nullable = false)
	private BigDecimal capacidadeRecolhimento;
	
	/**
	 * Número de reprogramações permitidas no lançamento
	 */
	@Column(name = "NUM_REPROG_LANCAMENTO", nullable = false)
	private int numeroReprogramacoesLancamento = 3;
	
	/**
	 * Dia de inicio da semana do distribuidor
	 */
	@Enumerated(EnumType.STRING)
	@Column(name = "INICIO_SEMANA", nullable = false)
	private DiaSemana inicioSemana = DiaSemana.QUARTA_FEIRA;
	
	/**
	 * Flag indicando se o distribuidor executa recolhimento de
	 * lançamento parcial
	 */
	@Column(name = "EXECUTA_RECOLHIMENTO_PARCIAL", nullable = false)
	private boolean executaRecolhimentoParcial;
	
	/**
	 * Flag indicando se o distribuidor possui preenchimento automático de Quantidade de PDVs
	 * (Aba de Distribuidor-Cadastro de Cota)
	 */
	@Column(name = "AUTO_PREENCHE_QTDE_PDV", nullable = false)
	private boolean preenchimentoAutomaticoPDV;
	
	/**
	 * Fator em número de dias para o cálculo do relançamento parcial
	 */
	@Column(name = "FATOR_RELANCAMENTO_PARCIAL",  nullable = false)
	private int fatorRelancamentoParcial;
	
	/**
	 * Filtro de seleção de cotas suspensas com 
     * valor consigando total menor ou igual ao informado 
     * nesta coluna.
	 */
	@Column(name = "VALOR_CONSIGNADO_SUSPENSAO_COTAS",  nullable = true)
	private BigDecimal valorConsignadoSuspensaoCotas;
	
	/**
	 * Filtro de seleção de cotas suspensas cujos
       dias de suspensão sejam maiores ou iguais 
     * ao informado nesta coluna.
	 */
	@Column(name = "QTD_DIAS_SUSPENSAO_COTAS",  nullable = true)
	private int quantidadeDiasSuspensaoCotas;
	
	/**
	 * Parametrização do contrato entre cota e distribuidor
	 */
	@OneToOne
	@JoinColumn(name = "PARAMETRO_CONTRATO_COTA_ID")
	private ParametroContratoCota parametroContratoCota;
	
	@OneToMany(mappedBy="distribuidor")
	private List<TipoGarantiaAceita> tiposGarantiasAceita;
	
	@Column(name = "REQUER_AUTORIZACAO_ENCALHE_SUPERA_REPARTE", nullable = false)
	private boolean requerAutorizacaoEncalheSuperaReparte;
    
	@Column(name="QNT_DIAS_REUTILIZACAO_CODIGO_COTA")
	private Long qntDiasReutilizacaoCodigoCota;
	
	/**
	 * Parâmetro relativo a quantidade de dias após a data de recolhimento 
	 * que um envio de encalhe pode ser aceito pelo distribuidor.
	 */
	@Column(name = "QTD_DIAS_ENCALHE_ATRASADO_ACEITAVEL", nullable = false)
	private int qtdDiasEncalheAtrasadoAceitavel = 4;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "LEIAUTE_PICKING", length = 20, nullable = true)
	private LeiautePicking leiautePicking;
	
	@Column(name="QNT_DIAS_VENCIMENTO_VENDA_ENCALHE")
	private Integer qntDiasVencinemtoVendaEncalhe;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "TIPO_ATIVIDADE", nullable = true)
	private TipoAtividade tipoAtividade = TipoAtividade.MERCANTIL;

	@Column(name = "OBRIGACAO_FISCAL", nullable = false)
	private boolean obrigacaoFiscao;	
	
	@Column(name = "REGIME_ESPECIAL", nullable = false)
	private boolean regimeEspecial;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "TIPO_IMPRESSAO_CE", nullable = true)
	private TipoImpressaoCE tipoImpressaoCE = TipoImpressaoCE.MODELO_1;	
	
	@Enumerated(EnumType.STRING)
	@Column(name = "TIPO_IMPRESSAO_NE", nullable = true)
	private TipoImpressaoNE tipoImpressaoNE = TipoImpressaoNE.MODELO_1; 	
	
	@Enumerated(EnumType.STRING)
	@Column(name = "TIPO_IMPRESSAO_NECA_DANFE", nullable = true)
	private TipoImpressaoNECADANFE tipoImpressaoNECADANFE = TipoImpressaoNECADANFE.MODELO_1;	

	@Column(name = "UTILIZA_PROCURACAO_ENTREGADORES", nullable = true)
	private boolean utilizaProcuracaoEntregadores;	
	
	@Column(name = "INFORMACOES_COMPLEMENTARES_PROCURACAO", nullable = true)
	private String informacoesComplementaresProcuracao;

	@Column(name = "UTILIZA_GARANTIA_PDV", nullable = true)
	private boolean utilizaGarantiaPdv;	

	@Column(name = "PARCELAMENTO_DIVIDAS", nullable = true)
	private boolean parcelamentoDividas;	
	
	@Column(name = "NEGOCIACAO_ATE_PARCELAS", nullable = true)
	private Integer negociacaoAteParcelas;

	@Column(name = "UTILIZA_CONTROLE_APROVACAO", nullable = true)
	private boolean utilizaControleAprovacao;	

	@Column(name = "PRAZO_FOLLOW_UP", nullable = true)
	private Integer prazoFollowUp;
	
	@Column(name = "PRAZO_AVISO_PREVIO_VALIDADE_GARANTIA", nullable = true)
	private Integer prazoAvisoPrevioValidadeGarantia;

	@Column(name="QTD_DIAS_LIMITE_PARA_REPROG_LANCAMENTO", nullable = false)
	private Integer qtdDiasLimiteParaReprogLancamento;
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public ParametroCobrancaDistribuidor getParametroCobrancaDistribuidor() {
		return parametroCobrancaDistribuidor;
	}

	public void setParametroCobrancaDistribuidor(
			ParametroCobrancaDistribuidor parametroCobrancaDistribuidor) {
		this.parametroCobrancaDistribuidor = parametroCobrancaDistribuidor;
	}

	public Date getDataOperacao() {
		return dataOperacao;
	}
	
	public void setDataOperacao(Date dataOperacao) {
		this.dataOperacao = dataOperacao;
	}
	
	public PessoaJuridica getJuridica() {
		return juridica;
	}
	
	public void setJuridica(PessoaJuridica juridica) {
		this.juridica = juridica;
	}
	
	public BigDecimal getFatorDesconto() {
		return fatorDesconto;
	}
	
	public void setFatorDesconto(BigDecimal fatorDesconto) {
		this.fatorDesconto = fatorDesconto;
	}
	
	public Set<PoliticaCobranca> getPoliticasCobranca() {
		return politicasCobranca;
	}

	public void setPoliticasCobranca(Set<PoliticaCobranca> politicasCobranca) {
		this.politicasCobranca = politicasCobranca;
	}

	public PoliticaSuspensao getPoliticaSuspensao() {
		return politicaSuspensao;
	}
	
	public void setPoliticaSuspensao(PoliticaSuspensao politicaSuspensao) {
		this.politicaSuspensao = politicaSuspensao;
	}
	
	public List<EnderecoDistribuidor> getEnderecos() {
		return enderecos;
	}
	
	public void setEnderecos(List<EnderecoDistribuidor> enderecos) {
		this.enderecos = enderecos;
	}
	
	public List<TelefoneDistribuidor> getTelefones() {
		return telefones;
	}
	
	public void setTelefones(List<TelefoneDistribuidor> telefones) {
		this.telefones = telefones;
	}

	public BigDecimal getCapacidadeDistribuicao() {
		return capacidadeDistribuicao;
	}

	public void setCapacidadeDistribuicao(BigDecimal capacidadeDistribuicao) {
		this.capacidadeDistribuicao = capacidadeDistribuicao;
	}

	public BigDecimal getCapacidadeRecolhimento() {
		return capacidadeRecolhimento;
	}

	public void setCapacidadeRecolhimento(BigDecimal capacidadeRecolhimento) {
		this.capacidadeRecolhimento = capacidadeRecolhimento;
	}
	
	public int getNumeroReprogramacoesLancamento() {
		return numeroReprogramacoesLancamento;
	}
	
	public void setNumeroReprogramacoesLancamento(
			int numeroReprogramacoesLancamento) {
		this.numeroReprogramacoesLancamento = numeroReprogramacoesLancamento;
	}
	
	public DiaSemana getInicioSemana() {
		return inicioSemana;
	}
	
	public void setInicioSemana(DiaSemana inicioSemana) {
		this.inicioSemana = inicioSemana;
	}
	
	public boolean isExecutaRecolhimentoParcial() {
		return executaRecolhimentoParcial;
	}
	
	public void setExecutaRecolhimentoParcial(boolean executaRecolhimentoParcial) {
		this.executaRecolhimentoParcial = executaRecolhimentoParcial;
	}
	
	public int getFatorRelancamentoParcial() {
		return fatorRelancamentoParcial;
	}
	
	public void setFatorRelancamentoParcial(int fatorRelancamentoParcial) {
		this.fatorRelancamentoParcial = fatorRelancamentoParcial;
	}

	/**
	 * @return the parametroContratoCota
	 */
	public ParametroContratoCota getParametroContratoCota() {
		return parametroContratoCota;
	}

	/**
	 * @param parametroContratoCota the parametroContratoCota to set
	 */
	public void setParametroContratoCota(ParametroContratoCota parametroContratoCota) {
		this.parametroContratoCota = parametroContratoCota;
	}

	/**
	 * @return the tiposGarantiasAceita
	 */
	public List<TipoGarantiaAceita> getTiposGarantiasAceita() {
		return tiposGarantiasAceita;
	}

	/**
	 * @param tiposGarantiasAceita the tiposGarantiasAceita to set
	 */
	public void setTiposGarantiasAceita(
			List<TipoGarantiaAceita> tiposGarantiasAceita) {
		this.tiposGarantiasAceita = tiposGarantiasAceita;
	}
	

	public boolean isRequerAutorizacaoEncalheSuperaReparte() {
		return requerAutorizacaoEncalheSuperaReparte;
	}

	public void setRequerAutorizacaoEncalheSuperaReparte(
			boolean requerAutorizacaoEncalheSuperaReparte) {
		this.requerAutorizacaoEncalheSuperaReparte = requerAutorizacaoEncalheSuperaReparte;
	}

	/**
	 * @return the codigo
	 */
	public Integer getCodigo() {
		return codigo;
	}

	/**
	 * @param codigo the codigo to set
	 */
	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	/**
	 * @return the preenchimentoAutomaticoPDV
	 */
	public boolean isPreenchimentoAutomaticoPDV() {
		return preenchimentoAutomaticoPDV;
	}

	/**
	 * @param preenchimentoAutomaticoPDV the preenchimentoAutomaticoPDV to set
	 */
	public void setPreenchimentoAutomaticoPDV(boolean preenchimentoAutomaticoPDV) {
		this.preenchimentoAutomaticoPDV = preenchimentoAutomaticoPDV;
	}

	/**
	 * @return the qntDiasReutilizacaoCodigoCota
	 */
	public Long getQntDiasReutilizacaoCodigoCota() {
		return qntDiasReutilizacaoCodigoCota;
	}

	/**
	 * @param qntDiasReutilizacaoCodigoCota the qntDiasReutilizacaoCodigoCota to set
	 */
	public void setQntDiasReutilizacaoCodigoCota(Long qntDiasReutilizacaoCodigoCota) {
		this.qntDiasReutilizacaoCodigoCota = qntDiasReutilizacaoCodigoCota;
	}

	/**
	 * @return the qtdDiasEncalheAtrasadoAceitavel
	 */
	public int getQtdDiasEncalheAtrasadoAceitavel() {
		return qtdDiasEncalheAtrasadoAceitavel;
	}

	/**
	 * @param qtdDiasEncalheAtrasadoAceitavel the qtdDiasEncalheAtrasadoAceitavel to set
	 */
	public void setQtdDiasEncalheAtrasadoAceitavel(
			int qtdDiasEncalheAtrasadoAceitavel) {
		this.qtdDiasEncalheAtrasadoAceitavel = qtdDiasEncalheAtrasadoAceitavel;
	}

	/**
	 * Obtém aceitaJuramentado
	 *
	 * @return boolean
	 */
	public boolean isAceitaJuramentado() {
		return aceitaJuramentado;
	}

	/**
	 * Atribuí aceitaJuramentado
	 * @param aceitaJuramentado 
	 */
	public void setAceitaJuramentado(boolean aceitaJuramentado) {
		this.aceitaJuramentado = aceitaJuramentado;
	}
	
	/**
     * @return the tipoContabilizacaoCE
     */
    public TipoContabilizacaoCE getTipoContabilizacaoCE() {
        return tipoContabilizacaoCE;
    }

    /**
     * @param tipoContabilizacaoCE the tipoContabilizacaoCE to set
     */
    public void setTipoContabilizacaoCE(TipoContabilizacaoCE tipoContabilizacaoCE) {
        this.tipoContabilizacaoCE = tipoContabilizacaoCE;
    }

    /**
     * @return the supervisionaVendaNegativa
     */
    public boolean isSupervisionaVendaNegativa() {
        return supervisionaVendaNegativa;
    }

    /**
     * @param supervisionaVendaNegativa the supervisionaVendaNegativa to set
     */
    public void setSupervisionaVendaNegativa(boolean supervisionaVendaNegativa) {
        this.supervisionaVendaNegativa = supervisionaVendaNegativa;
    }

    /**
     * @return the politicaChamadao
     */
    public PoliticaChamadao getPoliticaChamadao() {
        return politicaChamadao;
    }

    /**
     * @param politicaChamadao the politicaChamadao to set
     */
    public void setPoliticaChamadao(PoliticaChamadao politicaChamadao) {
        this.politicaChamadao = politicaChamadao;
    }

    /**
	 * @return the leiautePicking
	 */
	public LeiautePicking getLeiautePicking() {
		return leiautePicking;
	}

	/**
	 * @param leiautePicking the leiautePicking to set
	 */
	public void setLeiautePicking(LeiautePicking leiautePicking) {
		this.leiautePicking = leiautePicking;
	}

	public BigDecimal getValorConsignadoSuspensaoCotas() {
		return valorConsignadoSuspensaoCotas;
	}

	public void setValorConsignadoSuspensaoCotas(
			BigDecimal valorConsignadoSuspensaoCotas) {
		this.valorConsignadoSuspensaoCotas = valorConsignadoSuspensaoCotas;
	}

	public int getQuantidadeDiasSuspensaoCotas() {
		return quantidadeDiasSuspensaoCotas;
	}

	public void setQuantidadeDiasSuspensaoCotas(int quantidadeDiasSuspensaoCotas) {
		this.quantidadeDiasSuspensaoCotas = quantidadeDiasSuspensaoCotas;
	}
	
	/**
	 * @return the qntDiasVencinemtoVendaEncalhe
	 */
	public Integer getQntDiasVencinemtoVendaEncalhe() {
		return qntDiasVencinemtoVendaEncalhe;
	}

	/**
	 * @param qntDiasVencinemtoVendaEncalhe the qntDiasVencinemtoVendaEncalhe to set
	 */
	public void setQntDiasVencinemtoVendaEncalhe(Integer qntDiasVencinemtoVendaEncalhe) {
		this.qntDiasVencinemtoVendaEncalhe = qntDiasVencinemtoVendaEncalhe;
	}

	public List<ParametrosDistribuidorEmissaoDocumento> getParametrosDistribuidorEmissaoDocumentos() {
		return parametrosDistribuidorEmissaoDocumentos;
	}

	public void setParametrosDistribuidorEmissaoDocumentos(
			List<ParametrosDistribuidorEmissaoDocumento> parametrosDistribuidorEmissaoDocumentos) {
		this.parametrosDistribuidorEmissaoDocumentos = parametrosDistribuidorEmissaoDocumentos;
	}

	public List<ParametrosDistribuidorFaltasSobras> getParametrosDistribuidorFaltasSobras() {
		return parametrosDistribuidorFaltasSobras;
	}

	public void setParametrosDistribuidorFaltasSobras(
			List<ParametrosDistribuidorFaltasSobras> parametrosDistribuidorFaltasSobras) {
		this.parametrosDistribuidorFaltasSobras = parametrosDistribuidorFaltasSobras;
	}

	public ParametrosRecolhimentoDistribuidor getParametrosRecolhimentoDistribuidor() {
		return parametrosRecolhimentoDistribuidor;
	}

	public void setParametrosRecolhimentoDistribuidor(
			ParametrosRecolhimentoDistribuidor parametrosRecolhimentoDistribuidor) {
		this.parametrosRecolhimentoDistribuidor = parametrosRecolhimentoDistribuidor;
	}

	public ParametrosAprovacaoDistribuidor getParametrosAprovacaoDistribuidor() {
		return parametrosAprovacaoDistribuidor;
	}

	public void setParametrosAprovacaoDistribuidor(
			ParametrosAprovacaoDistribuidor parametrosAprovacaoDistribuidor) {
		this.parametrosAprovacaoDistribuidor = parametrosAprovacaoDistribuidor;
	}

	public TipoAtividade getTipoAtividade() {
		return tipoAtividade;
	}

	public void setTipoAtividade(TipoAtividade tipoAtividade) {
		this.tipoAtividade = tipoAtividade;
	}

	public boolean isObrigacaoFiscao() {
		return obrigacaoFiscao;
	}

	public void setObrigacaoFiscao(boolean obrigacaoFiscao) {
		this.obrigacaoFiscao = obrigacaoFiscao;
	}

	public boolean isRegimeEspecial() {
		return regimeEspecial;
	}

	public void setRegimeEspecial(boolean regimeEspecial) {
		this.regimeEspecial = regimeEspecial;
	}

	public TipoImpressaoCE getTipoImpressaoCE() {
		return tipoImpressaoCE;
	}

	public void setTipoImpressaoCE(TipoImpressaoCE tipoImpressaoCE) {
		this.tipoImpressaoCE = tipoImpressaoCE;
	}

	public TipoImpressaoNE getTipoImpressaoNE() {
		return tipoImpressaoNE;
	}

	public void setTipoImpressaoNE(TipoImpressaoNE tipoImpressaoNE) {
		this.tipoImpressaoNE = tipoImpressaoNE;
	}

	public TipoImpressaoNECADANFE getTipoImpressaoNECADANFE() {
		return tipoImpressaoNECADANFE;
	}

	public void setTipoImpressaoNECADANFE(
			TipoImpressaoNECADANFE tipoImpressaoNECADANFE) {
		this.tipoImpressaoNECADANFE = tipoImpressaoNECADANFE;
	}

	public boolean isUtilizaProcuracaoEntregadores() {
		return utilizaProcuracaoEntregadores;
	}

	public void setUtilizaProcuracaoEntregadores(
			boolean utilizaProcuracaoEntregadores) {
		this.utilizaProcuracaoEntregadores = utilizaProcuracaoEntregadores;
	}

	public String getInformacoesComplementaresProcuracao() {
		return informacoesComplementaresProcuracao;
	}

	public void setInformacoesComplementaresProcuracao(
			String informacoesComplementaresProcuracao) {
		this.informacoesComplementaresProcuracao = informacoesComplementaresProcuracao;
	}

	public boolean isUtilizaGarantiaPdv() {
		return utilizaGarantiaPdv;
	}

	public void setUtilizaGarantiaPdv(boolean utilizaGarantiaPdv) {
		this.utilizaGarantiaPdv = utilizaGarantiaPdv;
	}

	public boolean isParcelamentoDividas() {
		return parcelamentoDividas;
	}

	public void setParcelamentoDividas(boolean parcelamentoDividas) {
		this.parcelamentoDividas = parcelamentoDividas;
	}

	public Integer getNegociacaoAteParcelas() {
		return negociacaoAteParcelas;
	}

	public void setNegociacaoAteParcelas(Integer negociacaoAteParcelas) {
		this.negociacaoAteParcelas = negociacaoAteParcelas;
	}

	public boolean isUtilizaControleAprovacao() {
		return utilizaControleAprovacao;
	}

	public void setUtilizaControleAprovacao(boolean utilizaControleAprovacao) {
		this.utilizaControleAprovacao = utilizaControleAprovacao;
	}

	public Integer getPrazoFollowUp() {
		return prazoFollowUp;
	}

	public void setPrazoFollowUp(Integer prazoFollowUp) {
		this.prazoFollowUp = prazoFollowUp;
	}

	public Integer getPrazoAvisoPrevioValidadeGarantia() {
		return prazoAvisoPrevioValidadeGarantia;
	}

	public void setPrazoAvisoPrevioValidadeGarantia(
			Integer prazoAvisoPrevioValidadeGarantia) {
		this.prazoAvisoPrevioValidadeGarantia = prazoAvisoPrevioValidadeGarantia;
	}

	/**
	 * @return the qtdDiasLimiteParaReprogLancamento
	 */
	public Integer getQtdDiasLimiteParaReprogLancamento() {
		return qtdDiasLimiteParaReprogLancamento;
	}

	/**
	 * @param qtdDiasLimiteParaReprogLancamento the qtdDiasLimiteParaReprogLancamento to set
	 */
	public void setQtdDiasLimiteParaReprogLancamento(
			Integer qtdDiasLimiteParaReprogLancamento) {
		this.qtdDiasLimiteParaReprogLancamento = qtdDiasLimiteParaReprogLancamento;
	}
	
}
