package br.com.abril.nds.model.cadastro;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang.Validate;

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
	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "PARAMETRO_CONTRATO_COTA_ID")
	private ParametroContratoCota parametroContratoCota;
	
	@OneToMany(mappedBy="distribuidor", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<TipoGarantiaAceita> tiposGarantiasAceita = new HashSet<TipoGarantiaAceita>();
	
	@Column(name = "REQUER_AUTORIZACAO_ENCALHE_SUPERA_REPARTE", nullable = false)
	private boolean requerAutorizacaoEncalheSuperaReparte;
    
	@Column(name="QNT_DIAS_REUTILIZACAO_CODIGO_COTA")
	private Long qntDiasReutilizacaoCodigoCota;
	
	@Column(name="UTILIZA_SUGESTAO_INCREMENTO")
	private boolean utilizaSugestaoIncrementoCodigo;
	
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

	@Enumerated(EnumType.STRING)
	@Column(name = "OBRIGACAO_FISCAL", nullable = true)
	private ObrigacaoFiscal obrigacaoFiscal;
	
	@Column(name = "REGIME_ESPECIAL", nullable = false)
	private boolean regimeEspecial;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "TIPO_IMPRESSAO_CE", nullable = true)
	private TipoImpressaoCE tipoImpressaoCE = TipoImpressaoCE.MODELO_1;	
	
	@Enumerated(EnumType.STRING)
	@Column(name = "TIPO_IMPRESSAO_INTERFACE_LED", nullable = true)
	private TipoImpressaoInterfaceLED tipoImpressaoInterfaceLED = TipoImpressaoInterfaceLED.MODELO_1; 	
	
	@Enumerated(EnumType.STRING)
	@Column(name = "TIPO_IMPRESSAO_NE_NECA_DANFE", nullable = true)
	private TipoImpressaoNENECADANFE tipoImpressaoNENECADANFE = TipoImpressaoNENECADANFE.MODELO_1;	

	@Column(name = "UTILIZA_PROCURACAO_ENTREGADORES", nullable = true)
	private boolean utilizaProcuracaoEntregadores;	
	
	@Lob
	@Column(name = "INFORMACOES_COMPLEMENTARES_PROCURACAO")
	private String informacoesComplementaresProcuracao;

	@Column(name = "UTILIZA_GARANTIA_PDV", nullable = false)
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
	
	/**
	 * Desconto da cota para negociação (Parametros do Distribuidor / Aba de Negociação)
	 */
	@Column(name="DESCONTO_COTA_PARA_NEGOCIACAO")
	private BigDecimal descontoCotaNegociacao;
	
	@Embedded
	private ParametroEntregaBanca parametroEntregaBanca;
	
	@Column(name = "COD_DISTRIBUIDOR_DINAP", nullable = true)
	private String codigoDistribuidorDinap;
	
	@Column(name = "COD_DISTRIBUIDOR_FC", nullable = true)
	private String codigoDistribuidorFC;
	
	@OneToOne(mappedBy = "distribuidor", cascade={CascadeType.PERSIST, CascadeType.MERGE})
	private EnderecoDistribuidor enderecoDistribuidor;
	
	@Column(name = "CONTROLE_ARQUIVO_COBRANCA", nullable = true)
	private Long controleArquivoCobranca;
	
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
	public Set<TipoGarantiaAceita> getTiposGarantiasAceita() {
		return tiposGarantiasAceita;
	}

	/**
	 * @param tiposGarantiasAceita the tiposGarantiasAceita to set
	 */
	public void setTiposGarantiasAceita(
			Set<TipoGarantiaAceita> tiposGarantiasAceita) {
		this.tiposGarantiasAceita = tiposGarantiasAceita;
	}
	
	/**
	 * Adiciona um novo tipo de garantia aceita pelo distribuidor, 
	 * ou atualiza o tipo de garantia aceita existente com o(s) valore(s)
	 * recebido(s) como parâmetro
	 
	 * @param tipoGarantia tipo de garantia para o novo tipo de garantia aceita para
	 * inclusão ou alteração
	 * @param valor valor da tipo de garantia aceita para inclusão ou alteração
	 * 
	 * @throws IllegalArgumentException caso o parâmetro tipoGarantia e valor forem nulos 
	 */
	public void addTipoGarantiaAceita(TipoGarantia tipoGarantia, Integer valor) {
	    Validate.notNull(tipoGarantia,"Tipo de Garantia não deve ser nulo!");
	    Validate.notNull(tipoGarantia,"Valor não deve ser nulo!");
	    if (tiposGarantiasAceita == null) {
	        tiposGarantiasAceita = new HashSet<TipoGarantiaAceita>();
	    }
	    TipoGarantiaAceita existente = getTipoGarantiaAceitaByTipoGarantia(tipoGarantia);
	    if (existente == null) {
	        tiposGarantiasAceita.add(new TipoGarantiaAceita(tipoGarantia, valor, this));
	    } else {
	        existente.setValor(valor);
	    }
	}
	
	
    /**
     * Encontra um tipo de garantia aceita pelo tipo de garantia
     * 
     * @param tipoGarantia
     *            tipo de garantia para encontrar o tipo de garantia aceita
     * @return tipo de garantia aceita com o tipo de garantia recebido ou null
     *         caso não exista um tipo de garantia aceita com o tipo de garantia
     *         recebido
     * @throws IllegalArgumentException
     *             caso o parâmetro tipoGarantia for nulo
     */
    public TipoGarantiaAceita getTipoGarantiaAceitaByTipoGarantia(
            TipoGarantia tipoGarantia) {
        Validate.notNull(tipoGarantia, "Tipo de Garantia não deve ser nulo!");
        for (TipoGarantiaAceita tipoGarantiaAceita : tiposGarantiasAceita) {
            if (tipoGarantiaAceita.getTipoGarantia().equals(tipoGarantia)) {
                return tipoGarantiaAceita;
            }
        }
        return null;
    }
	
	/**
	 * Remove o tipo de garantia aceita que corresponde ao tipo de garantia
	 * recebido como parâmetro, caso exista
	 * @param tipoGarantia tipo de garantia para remoção do tipo de garantia aceita
	 * 
	 * @throws IllegalArgumentException caso o parâmetro tipoGarantia for nulo
	 * 
	 */
	public void removerTipoGarantiaAceita(TipoGarantia tipoGarantia) {
	    Validate.notNull(tipoGarantia,"Tipo de Garantia para remoção não deve ser nulo!");
	    if (tiposGarantiasAceita != null) {
	       Iterator<TipoGarantiaAceita> iterator = tiposGarantiasAceita.iterator();
	       while (iterator.hasNext()) {
	           TipoGarantiaAceita tipoGarantiaAceita = iterator.next();
	           if (tipoGarantiaAceita.getTipoGarantia().equals(tipoGarantia)) {
	               iterator.remove();
	               break;
	           }
	       }
	    }
	}
	
    /**
     * Remove/Desassocia os tipo de garantias aceitas do Distribuidor
     */
	public void removerTodosTiposGarantiasAceitas() {
        if (tiposGarantiasAceita != null) {
                tiposGarantiasAceita.clear();
        }
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
	 * @return the utilizaSugestaoIncrementoCodigo
	 */
	public Boolean getUtilizaSugestaoIncrementoCodigo() {
		return utilizaSugestaoIncrementoCodigo;
	}

	/**
	 * @param utilizaSugestaoIncrementoCodigo the utilizaSugestaoIncrementoCodigo to set
	 */
	public void setUtilizaSugestaoIncrementoCodigo(
			Boolean utilizaSugestaoIncrementoCodigo) {
		this.utilizaSugestaoIncrementoCodigo = utilizaSugestaoIncrementoCodigo;
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

	public ObrigacaoFiscal getObrigacaoFiscal() {
		return obrigacaoFiscal;
	}

	public void setObrigacaoFiscal(ObrigacaoFiscal obrigacaoFiscal) {
		this.obrigacaoFiscal = obrigacaoFiscal;
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

	public TipoImpressaoInterfaceLED getTipoImpressaoInterfaceLED() {
		return tipoImpressaoInterfaceLED;
	}

	public void setTipoImpressaoInterfaceLED(TipoImpressaoInterfaceLED tipoImpressaoInterfaceLED) {
		this.tipoImpressaoInterfaceLED = tipoImpressaoInterfaceLED;
	}

	public TipoImpressaoNENECADANFE getTipoImpressaoNENECADANFE() {
		return tipoImpressaoNENECADANFE;
	}

	public void setTipoImpressaoNENECADANFE(
			TipoImpressaoNENECADANFE tipoImpressaoNENECADANFE) {
		this.tipoImpressaoNENECADANFE = tipoImpressaoNENECADANFE;
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

	public BigDecimal getDescontoCotaNegociacao() {
		return descontoCotaNegociacao;
	}

	/**
	 * @param descontoCotaNegociacao the descontoCotaNegociacao to set
	 */
	public void setDescontoCotaNegociacao(BigDecimal descontoCotaNegociacao) {
		this.descontoCotaNegociacao = descontoCotaNegociacao;
	}

    /**
     * @return the parametroEntregaBanca
     */
    public ParametroEntregaBanca getParametroEntregaBanca() {
        return parametroEntregaBanca;
    }

    /**
     * @param parametroEntregaBanca the parametroEntregaBanca to set
     */
    public void setParametroEntregaBanca(ParametroEntregaBanca parametroEntregaBanca) {
        this.parametroEntregaBanca = parametroEntregaBanca;
    }

	/**
	 * @return the codigoDistribuidorDinap
	 */
	public String getCodigoDistribuidorDinap() {
		return codigoDistribuidorDinap;
	}

	/**
	 * @param codigoDistribuidorDinap the codigoDistribuidorDinap to set
	 */
	public void setCodigoDistribuidorDinap(String codigoDistribuidorDinap) {
		this.codigoDistribuidorDinap = codigoDistribuidorDinap;
	}

	/**
	 * @return the codigoDistribuidorFC
	 */
	public String getCodigoDistribuidorFC() {
		return codigoDistribuidorFC;
	}

	/**
	 * @param codigoDistribuidorFC the codigoDistribuidorFC to set
	 */
	public void setCodigoDistribuidorFC(String codigoDistribuidorFC) {
		this.codigoDistribuidorFC = codigoDistribuidorFC;
	}

	/**
	 * @return the enderecoDistribuidor
	 */
	public EnderecoDistribuidor getEnderecoDistribuidor() {
		return enderecoDistribuidor;
	}

	/**
	 * @param enderecoDistribuidor the enderecoDistribuidor to set
	 */
	public void setEnderecoDistribuidor(EnderecoDistribuidor enderecoDistribuidor) {
		this.enderecoDistribuidor = enderecoDistribuidor;
	}	
	
	/**
	 * @return the controleArquivoCobranca
	 */
	public Long getControleArquivoCobranca() {
		return controleArquivoCobranca;
	}

	/**
	 * @param controleArquivoCobranca the controleArquivoCobranca to set
	 */
	public void setControleArquivoCobranca(Long controleArquivoCobranca) {
		this.controleArquivoCobranca = controleArquivoCobranca;
	}

	/**
	 * Conforme esclarecido pela àrea de negócios qualquer
	 * valor de {@link ObrigacaoFiscal} atribuído ao Distribuidor
	 * indica que este possui obrigação fiscal.
	 * 
	 * @return true se o Distribuidor possui obrigação
	 * fiscal, false caso contrário
	 */
	public boolean possuiObrigacaoFiscal() {
	    return obrigacaoFiscal != null;
	}

}
