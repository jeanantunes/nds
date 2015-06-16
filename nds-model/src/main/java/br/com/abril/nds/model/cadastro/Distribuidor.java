package br.com.abril.nds.model.cadastro;

import java.math.BigDecimal;
import java.math.BigInteger;
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
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang.Validate;

import br.com.abril.nds.model.DiaSemana;
import br.com.abril.nds.model.fiscal.NaturezaOperacao;

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
	
	@Column(name = "FATOR_DESCONTO", precision=18, scale=4)
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
	 * Flag indicando que este distribuidor aceita recolhimento
	 * de produto edicao parcial em atraso. (1o 2o 3o 4o e/ou 5o dia 
	 * de acordo com parametrização).
	 */
	@Column(name = "ACEITA_RECOLHIMENTO_PARCIAL_ATRASO")
	private boolean aceitaRecolhimentoParcialAtraso;
	
	/**
	 * Parametrização de Política de Chamadão do Distribuidor
	 */
	@Embedded
	private PoliticaChamadao politicaChamadao;
	
	/**
	 * Capacidade de distribuição diária do distribuidor, em número de exemplares
	 */
	@Column(name = "CAPACIDADE_DISTRIBUICAO", nullable = false)
	private BigInteger capacidadeDistribuicao;
	
	/**
	 * Capacidade de recolhimento diária do distribuidor, em número de exemplares
	 */
	@Column(name = "CAPACIDADE_RECOLHIMENTO", nullable = false)
	private BigInteger capacidadeRecolhimento;
	
	/**
	 * Número de reprogramações permitidas no lançamento
	 */
	@Column(name = "NUM_REPROG_LANCAMENTO", nullable = false)
	private int numeroReprogramacoesLancamento = 3;
	
	/**
	 * Dia de inicio da semana do distribuidor Lancamento.
	 */
	@Enumerated(EnumType.STRING)
	@Column(name = "INICIO_SEMANA_LANCAMENTO", nullable = false)
	private DiaSemana inicioSemanaLancamento = DiaSemana.SEGUNDA_FEIRA;
	
	/**
	 * Dia de inicio da semana do distribuidor Recolhimento.
	 */
	@Enumerated(EnumType.STRING)
	@Column(name = "INICIO_SEMANA_RECOLHIMENTO", nullable = false)
	private DiaSemana inicioSemanaRecolhimento = DiaSemana.QUARTA_FEIRA;
	
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
	@Column(name = "VALOR_CONSIGNADO_SUSPENSAO_COTAS",  nullable = true, precision=18, scale=4)
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
	
	@Column(name="QNT_DIAS_VENCIMENTO_VENDA_ENCALHE")
	private Integer qntDiasVencinemtoVendaEncalhe;
	
	@Column(name ="CNAE", nullable = false)
	private String cnae;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "TIPO_ATIVIDADE", nullable = true)
	private TipoAtividade tipoAtividade = TipoAtividade.MERCANTIL;

	@Column(name = "POSSUI_REGIME_ESPECIAL_DISPENSA_INTERNA", nullable = false)
	private boolean possuiRegimeEspecialDispensaInterna;
	
	@Column(name = "NF_INFORMACOES_ADICIONAIS", nullable = true)
	private String nfInformacoesAdicionais;
	
	@Column(name = "NUMERO_DISPOSITIVO_LEGAL")
	private String numeroDispositivoLegal;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "DATA_LIMITE_VIGENCIA_REGIME_ESPECIAL")
	private Date dataLimiteVigenciaRegimeEspecial;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "TIPO_IMPRESSAO_CE", nullable = true)
	private TipoImpressaoCE tipoImpressaoCE;	
	
	@Enumerated(EnumType.STRING)
	@Column(name = "TIPO_IMPRESSAO_INTERFACE_LED", nullable = true)
	private TipoImpressaoInterfaceLED tipoImpressaoInterfaceLED; 	
	
	@Column(name = "ARQUIVO_INTERFACE_LED_PICKING_1")
	private String arquivoInterfaceLedPicking1;
	
	@Column(name = "ARQUIVO_INTERFACE_LED_PICKING_2")
	private String arquivoInterfaceLedPicking2;
	
	@Column(name = "ARQUIVO_INTERFACE_LED_PICKING_3")
	private String arquivoInterfaceLedPicking3;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "TIPO_IMPRESSAO_NE_NECA_DANFE", nullable = true)
	private TipoImpressaoNENECADANFE tipoImpressaoNENECADANFE;	

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
	@Column(name="DESCONTO_COTA_PARA_NEGOCIACAO", precision=18, scale=4)
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
	
	@Column(name = "FECHAMENTO_DIARIO_EM_ANDAMENTO", nullable = true)
	private Boolean fechamentoDiarioEmAndamento;
	
	@Column(name = "ACEITA_BAIXA_PGTO_MAIOR")
	private Boolean aceitaBaixaPagamentoMaior;
	
	@Column(name = "ACEITA_BAIXA_PGTO_MENOR")
	private Boolean aceitaBaixaPagamentoMenor;
	
	@Column(name = "ACEITA_BAIXA_PGTO_VENCIDO")
	private Boolean aceitaBaixaPagamentoVencido;	
	
	@Column(name = "NUM_DIAS_NOVA_COBRANCA")
	private Integer numeroDiasNovaCobranca;
	
	@Column(name = "ASSUNTO_EMAIL_COBRANCA")
	private String assuntoEmailCobranca;
	
	@Column(name = "MENSAGEM_EMAIL_COBRANCA")
	private String mensagemEmailCobranca;
	
	@Column(name = "DESCRICAO_TAXA_EXTRA")
	private String descricaoTaxaExtra;
	
	@Column(name = "PERCENTUAL_TAXA_EXTRA",precision=18, scale=2)
	private BigDecimal percentualTaxaExtra;
	
	@Column(name = "PRACA_VERANEIO")
	private boolean pracaVeraneio;

	// Caso as interfaces 106, 107, 108 e 111 estejam em execução
	@Column(name = "INTERFACES_MATRIZ_EM_EXECUCAO")
	private boolean interfacesMatrizExecucao;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATA_INICIO_INTERFACES_MATRIZ_EM_EXECUCAO")
	private Date dataInicioInterfacesMatrizExecucao;
	
	@Column(name = "PARAR_ACUM_DIVIDAS", nullable = false)
	private boolean pararAcumuloDividas;
	
	@OneToMany(mappedBy="distribuidor", cascade = CascadeType.ALL)
	@OrderBy("id")
	private Set<DistribuidorTipoNotaFiscal> tiposNotaFiscalDistribuidor = new HashSet<DistribuidorTipoNotaFiscal>();
	
	@OneToMany
	@JoinTable(
	            name="DISTRIBUIDOR_TIPOS_EMISSOES_NOTA_FISCAL",
	            joinColumns={
	            		@JoinColumn(table="DISTRIBUIDOR", name="DISTRIBUIDOR_ID", referencedColumnName="id", nullable=false)
	                    },
	            inverseJoinColumns=@JoinColumn(table="DISTRIBUIDOR_NOTA_FISCAL_TIPO_EMISSAO", name="NOTA_FISCAL_TIPO_EMISSAO_ID", referencedColumnName="id"))
	@OrderBy("sequencia")
	private Set<NotaFiscalTipoEmissao> tiposEmissoesNotaFiscalDistribuidor = new HashSet<NotaFiscalTipoEmissao>();

	@OneToMany(mappedBy="distribuidor", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<DistribuidorClassificacaoCota> listClassificacaoCota;
	
	@OneToOne(mappedBy = "distribuidor", cascade={CascadeType.PERSIST, CascadeType.MERGE})
	private DistribuidorGridDistribuicao gridDistribuicao;
	
	@OneToMany(mappedBy="distribuidor", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<DistribuidorPercentualExcedente> listPercentualExcedente;
	
	@OneToOne
	@JoinColumn(name="REGIME_TRIBUTARIO_ID")
	private RegimeTributario regimeTributario;
	
	/**
	 * Lista de naturezas de operacoes para Notas de Envio
	 * Deve existir 1, e apenas 1, para cada tipo de Atividade
	 */
	@OneToMany
	@JoinTable(
	            name="NATUREZA_OPERACAO_NOTA_ENVIO",
	            joinColumns={
	            		@JoinColumn(table="DISTRIBUIDOR", name="DISTRIBUIDOR_ID", referencedColumnName="id", nullable=false)
	                    },
	            inverseJoinColumns=@JoinColumn(table="NATUREZA_OPERACAO", name="NATUREZA_OPERACAO_ID", referencedColumnName="id"))
	private Set<NaturezaOperacao> naturezasOperacoesNotasEnvio = new HashSet<NaturezaOperacao>();
	
	@Column(name = "SUGERE_SUSPENSAO", nullable = false)
	private boolean sugereSuspensao = true;
	
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

	public BigInteger getCapacidadeDistribuicao() {
		return capacidadeDistribuicao;
	}

	public void setCapacidadeDistribuicao(BigInteger capacidadeDistribuicao) {
		this.capacidadeDistribuicao = capacidadeDistribuicao;
	}

	public BigInteger getCapacidadeRecolhimento() {
		return capacidadeRecolhimento;
	}

	public void setCapacidadeRecolhimento(BigInteger capacidadeRecolhimento) {
		this.capacidadeRecolhimento = capacidadeRecolhimento;
	}
	
	public int getNumeroReprogramacoesLancamento() {
		return numeroReprogramacoesLancamento;
	}
	
	public void setNumeroReprogramacoesLancamento(
			int numeroReprogramacoesLancamento) {
		this.numeroReprogramacoesLancamento = numeroReprogramacoesLancamento;
	}
	
	public DiaSemana getInicioSemanaRecolhimento() {
		return inicioSemanaRecolhimento;
	}
	
	public void setInicioSemanaRecolhimento(DiaSemana inicioSemanaRecolhimento) {
		this.inicioSemanaRecolhimento = inicioSemanaRecolhimento;
	}
	
	public DiaSemana getInicioSemanaLancamento() {
		return inicioSemanaLancamento;
	}
	
	public void setInicioSemanaLancamento(DiaSemana inicioSemanaLancamento) {
		this.inicioSemanaLancamento = inicioSemanaLancamento;
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
	 * @return the sugereSuspensao
	 */
	public boolean isSugereSuspensao() {
		return sugereSuspensao;
	}

	/**
	 * @param sugereSuspensao the sugereSuspensao to set
	 */
	public void setSugereSuspensao(boolean sugereSuspensao) {
		this.sugereSuspensao = sugereSuspensao;
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
	
	public String getCnae() {
		return cnae;
	}

	public void setCnae(String cnae) {
		this.cnae = cnae;
	}

	public TipoAtividade getTipoAtividade() {
		return tipoAtividade;
	}

	public void setTipoAtividade(TipoAtividade tipoAtividade) {
		this.tipoAtividade = tipoAtividade;
	}

	public boolean isPossuiRegimeEspecialDispensaInterna() {
		return (possuiRegimeEspecialDispensaInterna && dataLimiteVigenciaRegimeEspecial != null ? dataLimiteVigenciaRegimeEspecial.after(new Date()) : false);
	}

	public void setPossuiRegimeEspecialDispensaInterna(
			boolean possuiRegimeEspecialDispensaInterna) {
		this.possuiRegimeEspecialDispensaInterna = possuiRegimeEspecialDispensaInterna;
	}

	public String getNfInformacoesAdicionais() {
		return nfInformacoesAdicionais;
	}

	public void setNfInformacoesAdicionais(String nfInformacoesAdicionais) {
		this.nfInformacoesAdicionais = nfInformacoesAdicionais;
	}

	public String getNumeroDispositivoLegal() {
		return numeroDispositivoLegal;
	}

	public void setNumeroDispositivoLegal(String numeroDispositivoLegal) {
		this.numeroDispositivoLegal = numeroDispositivoLegal;
	}

	public Date getDataLimiteVigenciaRegimeEspecial() {
		return dataLimiteVigenciaRegimeEspecial;
	}

	public void setDataLimiteVigenciaRegimeEspecial(
			Date dataLimiteVigenciaRegimeEspecial) {
		this.dataLimiteVigenciaRegimeEspecial = dataLimiteVigenciaRegimeEspecial;
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

	/**
	 * @return the arquivoInterfaceLedPicking1
	 */
	public String getArquivoInterfaceLedPicking1() {
		return arquivoInterfaceLedPicking1;
	}

	/**
	 * @param arquivoInterfaceLedPicking1 the arquivoInterfaceLedPicking1 to set
	 */
	public void setArquivoInterfaceLedPicking1(String arquivoInterfaceLedPicking1) {
		this.arquivoInterfaceLedPicking1 = arquivoInterfaceLedPicking1;
	}

	/**
	 * @return the arquivoInterfaceLedPicking2
	 */
	public String getArquivoInterfaceLedPicking2() {
		return arquivoInterfaceLedPicking2;
	}

	/**
	 * @param arquivoInterfaceLedPicking2 the arquivoInterfaceLedPicking2 to set
	 */
	public void setArquivoInterfaceLedPicking2(String arquivoInterfaceLedPicking2) {
		this.arquivoInterfaceLedPicking2 = arquivoInterfaceLedPicking2;
	}

	/**
	 * @return the arquivoInterfaceLedPicking3
	 */
	public String getArquivoInterfaceLedPicking3() {
		return arquivoInterfaceLedPicking3;
	}

	/**
	 * @param arquivoInterfaceLedPicking3 the arquivoInterfaceLedPicking3 to set
	 */
	public void setArquivoInterfaceLedPicking3(String arquivoInterfaceLedPicking3) {
		this.arquivoInterfaceLedPicking3 = arquivoInterfaceLedPicking3;
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
	 * @return the fechamentoDiarioEmAndamento
	 */
	public Boolean getFechamentoDiarioEmAndamento() {
		return fechamentoDiarioEmAndamento;
	}

	/**
	 * @param fechamentoDiarioEmAndamento the fechamentoDiarioEmAndamento to set
	 */
	public void setFechamentoDiarioEmAndamento(Boolean fechamentoDiarioEmAndamento) {
		this.fechamentoDiarioEmAndamento = fechamentoDiarioEmAndamento;
	}

	/**
	 * @return the aceitaBaixaPagamentoMaior
	 */
	public Boolean getAceitaBaixaPagamentoMaior() {
		return aceitaBaixaPagamentoMaior;
	}

	/**
	 * @param aceitaBaixaPagamentoMaior the aceitaBaixaPagamentoMaior to set
	 */
	public void setAceitaBaixaPagamentoMaior(Boolean aceitaBaixaPagamentoMaior) {
		this.aceitaBaixaPagamentoMaior = aceitaBaixaPagamentoMaior;
	}

	/**
	 * @return the aceitaBaixaPagamentoMenor
	 */
	public Boolean getAceitaBaixaPagamentoMenor() {
		return aceitaBaixaPagamentoMenor;
	}

	/**
	 * @param aceitaBaixaPagamentoMenor the aceitaBaixaPagamentoMenor to set
	 */
	public void setAceitaBaixaPagamentoMenor(Boolean aceitaBaixaPagamentoMenor) {
		this.aceitaBaixaPagamentoMenor = aceitaBaixaPagamentoMenor;
	}

	/**
	 * @return the aceitaBaixaPagamentoVencido
	 */
	public Boolean getAceitaBaixaPagamentoVencido() {
		return aceitaBaixaPagamentoVencido;
	}

	/**
	 * @param aceitaBaixaPagamentoVencido the aceitaBaixaPagamentoVencido to set
	 */
	public void setAceitaBaixaPagamentoVencido(Boolean aceitaBaixaPagamentoVencido) {
		this.aceitaBaixaPagamentoVencido = aceitaBaixaPagamentoVencido;
	}

	/**
	 * @return the numeroDiasNovaCobranca
	 */
	public Integer getNumeroDiasNovaCobranca() {
		return numeroDiasNovaCobranca;
	}

	/**
	 * @param numeroDiasNovaCobranca the numeroDiasNovaCobranca to set
	 */
	public void setNumeroDiasNovaCobranca(Integer numeroDiasNovaCobranca) {
		this.numeroDiasNovaCobranca = numeroDiasNovaCobranca;
	}

	/**
	 * @return the assuntoEmailCobranca
	 */
	public String getAssuntoEmailCobranca() {
		return assuntoEmailCobranca;
	}

	/**
	 * @param assuntoEmailCobranca the assuntoEmailCobranca to set
	 */
	public void setAssuntoEmailCobranca(String assuntoEmailCobranca) {
		this.assuntoEmailCobranca = assuntoEmailCobranca;
	}

	/**
	 * @return the mensagemEmailCobranca
	 */
	public String getMensagemEmailCobranca() {
		return mensagemEmailCobranca;
	}

	/**
	 * @param mensagemEmailCobranca the mensagemEmailCobranca to set
	 */
	public void setMensagemEmailCobranca(String mensagemEmailCobranca) {
		this.mensagemEmailCobranca = mensagemEmailCobranca;
	}

	public String getDescricaoTaxaExtra() {
		return descricaoTaxaExtra;
	}

	public void setDescricaoTaxaExtra(String descricaoTaxaExtra) {
		this.descricaoTaxaExtra = descricaoTaxaExtra;
	}

	public BigDecimal getPercentualTaxaExtra() {
		return percentualTaxaExtra;
	}

	public void setPercentualTaxaExtra(BigDecimal percentualTaxaExtra) {
		this.percentualTaxaExtra = percentualTaxaExtra;
	}

	public boolean isPracaVeraneio() {
		return pracaVeraneio;
	}

	public void setPracaVeraneio(boolean pracaVeraneio) {
		this.pracaVeraneio = pracaVeraneio;
	}

	public boolean isInterfacesMatrizExecucao() {
		return interfacesMatrizExecucao;
	}

	public void setInterfacesMatrizExecucao(boolean interfacesMatrizExecucao) {
		this.interfacesMatrizExecucao = interfacesMatrizExecucao;
	}

	public void setUtilizaSugestaoIncrementoCodigo(
			boolean utilizaSugestaoIncrementoCodigo) {
		this.utilizaSugestaoIncrementoCodigo = utilizaSugestaoIncrementoCodigo;
	}

	public Date getDataInicioInterfacesMatrizExecucao() {
		return dataInicioInterfacesMatrizExecucao;
	}

	public void setDataInicioInterfacesMatrizExecucao(
			Date dataInicioInterfacesMatrizExecucao) {
		this.dataInicioInterfacesMatrizExecucao = dataInicioInterfacesMatrizExecucao;
	}

	/**
	 * @return the pararAcumuloDividas
	 */
	public boolean isPararAcumuloDividas() {
		return pararAcumuloDividas;
	}

	/**
	 * @param pararAcumuloDividas the pararAcumuloDividas to set
	 */
	public void setPararAcumuloDividas(boolean pararAcumuloDividas) {
		this.pararAcumuloDividas = pararAcumuloDividas;
	}

	/**
	 * @return
	 */
	public Set<DistribuidorTipoNotaFiscal> getTiposNotaFiscalDistribuidor() {
		return tiposNotaFiscalDistribuidor;
	}

	/**
	 * @param tiposNotaFiscalDistribuidor
	 */
	public void setTiposNotaFiscalDistribuidor(
			Set<DistribuidorTipoNotaFiscal> tiposNotaFiscalDistribuidor) {
		this.tiposNotaFiscalDistribuidor = tiposNotaFiscalDistribuidor;
	}

	public Set<NotaFiscalTipoEmissao> getTiposEmissoesNotaFiscalDistribuidor() {
		return tiposEmissoesNotaFiscalDistribuidor;
	}

	public void setTiposEmissoesNotaFiscalDistribuidor(
			Set<NotaFiscalTipoEmissao> tiposEmissoesNotaFiscalDistribuidor) {
		this.tiposEmissoesNotaFiscalDistribuidor = tiposEmissoesNotaFiscalDistribuidor;
	}
	
	public List<DistribuidorClassificacaoCota> getListClassificacaoCota() {
		return listClassificacaoCota;
	}

	public void setListClassificacaoCota(
            List<DistribuidorClassificacaoCota> listClassificacaoCota) {
		this.listClassificacaoCota = listClassificacaoCota;
	}

	public DistribuidorGridDistribuicao getGridDistribuicao() {
		return gridDistribuicao;
	}

	public void setGridDistribuicao(DistribuidorGridDistribuicao gridDistribuicao) {
		this.gridDistribuicao = gridDistribuicao;
	}

	public List<DistribuidorPercentualExcedente> getListPercentualExcedente() {
		return listPercentualExcedente;
	}

	public void setListPercentualExcedente(
            List<DistribuidorPercentualExcedente> listPercentualExcedente) {
		this.listPercentualExcedente = listPercentualExcedente;
	}
	
	public RegimeTributario getRegimeTributario() {
		return regimeTributario;
	}

	public void setRegimeTributario(RegimeTributario regimeTributario) {
		this.regimeTributario = regimeTributario;
	}

	public List<TributoAliquota> getRegimeTributarioTributoAliquota() {
		return regimeTributario != null ? regimeTributario.getTributosAliquotas() : null;
	}
	
	public Set<NaturezaOperacao> getNaturezasOperacoesNotasEnvio() {
		return naturezasOperacoesNotasEnvio;
	}

	public void setNaturezasOperacoesNotasEnvio(
			Set<NaturezaOperacao> naturezasOperacoesNotasEnvio) {
		this.naturezasOperacoesNotasEnvio = naturezasOperacoesNotasEnvio;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.getId() == null) ? 0 : this.getId().hashCode());
		result = prime * result + ((this.getCodigo() == null) ? 0 : this.getCodigo().hashCode());
		result = prime * result + ((this.getCodigoDistribuidorDinap() == null) ? 0 : this.getCodigoDistribuidorDinap().hashCode());
		result = prime * result + ((this.getCodigoDistribuidorFC() == null) ? 0 : this.getCodigoDistribuidorFC().hashCode());
		return result;
	}
	
	public boolean isAceitaRecolhimentoParcialAtraso() {
		return aceitaRecolhimentoParcialAtraso;
	}

	public void setAceitaRecolhimentoParcialAtraso(
			boolean aceitaRecolhimentoParcialAtraso) {
		this.aceitaRecolhimentoParcialAtraso = aceitaRecolhimentoParcialAtraso;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Distribuidor other = (Distribuidor) obj;
		if (this.getId() == null) {
			if (other.getId() != null)
				return false;
		} else if (!this.getId().equals(other.getId()))
			return false;
		if (this.getCodigo() == null) {
			if (other.getCodigo() != null)
				return false;
		} else if (!this.getCodigo().equals(other.getCodigo()))
			return false;
		if (this.getCodigoDistribuidorDinap() == null) {
			if (other.getCodigoDistribuidorDinap() != null)
				return false;
		} else if (!this.getCodigoDistribuidorDinap()
				.equals(other.getCodigoDistribuidorDinap()))
			return false;
		if (this.getCodigoDistribuidorFC() == null) {
			if (other.getCodigoDistribuidorFC() != null)
				return false;
		} else if (!this.getCodigoDistribuidorFC().equals(other.getCodigoDistribuidorFC()))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Distribuidor [id=" + id + ", parametroCobrancaDistribuidor="
				+ parametroCobrancaDistribuidor
				+ ", parametrosRecolhimentoDistribuidor="
				+ parametrosRecolhimentoDistribuidor
				+ ", parametrosAprovacaoDistribuidor="
				+ parametrosAprovacaoDistribuidor + ", codigo=" + codigo
				+ ", dataOperacao=" + dataOperacao + ", juridica=" + juridica
				+ ", fatorDesconto=" + fatorDesconto + ", politicasCobranca="
				+ politicasCobranca + ", politicaSuspensao="
				+ politicaSuspensao + ", telefones=" + telefones
				+ ", parametrosDistribuidorEmissaoDocumentos="
				+ parametrosDistribuidorEmissaoDocumentos
				+ ", parametrosDistribuidorFaltasSobras="
				+ parametrosDistribuidorFaltasSobras + ", aceitaJuramentado="
				+ aceitaJuramentado + ", tipoContabilizacaoCE="
				+ tipoContabilizacaoCE + ", supervisionaVendaNegativa="
				+ supervisionaVendaNegativa + ", politicaChamadao="
				+ politicaChamadao + ", capacidadeDistribuicao="
				+ capacidadeDistribuicao + ", capacidadeRecolhimento="
				+ capacidadeRecolhimento + ", numeroReprogramacoesLancamento="
				+ numeroReprogramacoesLancamento + ", inicioSemana="
				+ inicioSemanaRecolhimento + ", executaRecolhimentoParcial="
				+ executaRecolhimentoParcial + ", preenchimentoAutomaticoPDV="
				+ preenchimentoAutomaticoPDV + ", fatorRelancamentoParcial="
				+ fatorRelancamentoParcial + ", valorConsignadoSuspensaoCotas="
				+ valorConsignadoSuspensaoCotas
				+ ", quantidadeDiasSuspensaoCotas="
				+ quantidadeDiasSuspensaoCotas + ", parametroContratoCota="
				+ parametroContratoCota + ", tiposGarantiasAceita="
				+ tiposGarantiasAceita
				+ ", requerAutorizacaoEncalheSuperaReparte="
				+ requerAutorizacaoEncalheSuperaReparte
				+ ", qntDiasReutilizacaoCodigoCota="
				+ qntDiasReutilizacaoCodigoCota
				+ ", utilizaSugestaoIncrementoCodigo="
				+ utilizaSugestaoIncrementoCodigo
				+ ", qtdDiasEncalheAtrasadoAceitavel="
				+ qtdDiasEncalheAtrasadoAceitavel
				+ ", qntDiasVencinemtoVendaEncalhe="
				+ qntDiasVencinemtoVendaEncalhe + ", tipoAtividade="
				+ tipoAtividade + ", possuiRegimeEspecialDispensaInterna="
				+ possuiRegimeEspecialDispensaInterna
				+ ", cnae =" + cnae
				+ ", numeroDispositivoLegal=" + numeroDispositivoLegal
				+ ", dataLimiteVigenciaRegimeEspecial="
				+ dataLimiteVigenciaRegimeEspecial + ", tipoImpressaoCE="
				+ tipoImpressaoCE + ", tipoImpressaoInterfaceLED="
				+ tipoImpressaoInterfaceLED + ", arquivoInterfaceLedPicking1="
				+ arquivoInterfaceLedPicking1
				+ ", arquivoInterfaceLedPicking2="
				+ arquivoInterfaceLedPicking2
				+ ", arquivoInterfaceLedPicking3="
				+ arquivoInterfaceLedPicking3 + ", tipoImpressaoNENECADANFE="
				+ tipoImpressaoNENECADANFE + ", utilizaProcuracaoEntregadores="
				+ utilizaProcuracaoEntregadores
				+ ", informacoesComplementaresProcuracao="
				+ informacoesComplementaresProcuracao + ", utilizaGarantiaPdv="
				+ utilizaGarantiaPdv + ", parcelamentoDividas="
				+ parcelamentoDividas + ", negociacaoAteParcelas="
				+ negociacaoAteParcelas + ", utilizaControleAprovacao="
				+ utilizaControleAprovacao + ", prazoFollowUp=" + prazoFollowUp
				+ ", prazoAvisoPrevioValidadeGarantia="
				+ prazoAvisoPrevioValidadeGarantia
				+ ", qtdDiasLimiteParaReprogLancamento="
				+ qtdDiasLimiteParaReprogLancamento
				+ ", descontoCotaNegociacao=" + descontoCotaNegociacao
				+ ", parametroEntregaBanca=" + parametroEntregaBanca
				+ ", codigoDistribuidorDinap=" + codigoDistribuidorDinap
				+ ", codigoDistribuidorFC=" + codigoDistribuidorFC
				+ ", enderecoDistribuidor=" + enderecoDistribuidor
				+ ", controleArquivoCobranca=" + controleArquivoCobranca
				+ ", fechamentoDiarioEmAndamento="
				+ fechamentoDiarioEmAndamento + ", aceitaBaixaPagamentoMaior="
				+ aceitaBaixaPagamentoMaior + ", aceitaBaixaPagamentoMenor="
				+ aceitaBaixaPagamentoMenor + ", aceitaBaixaPagamentoVencido="
				+ aceitaBaixaPagamentoVencido + ", numeroDiasNovaCobranca="
				+ numeroDiasNovaCobranca + ", assuntoEmailCobranca="
				+ assuntoEmailCobranca + ", mensagemEmailCobranca="
				+ mensagemEmailCobranca + ", pracaVeraneio=" + pracaVeraneio
				+ ", interfacesMatrizExecucao=" + interfacesMatrizExecucao
				+ ", dataInicioInterfacesMatrizExecucao="
				+ dataInicioInterfacesMatrizExecucao + ", pararAcumuloDividas="
				+ pararAcumuloDividas + ", tiposNotaFiscalDistribuidor="
				+ tiposNotaFiscalDistribuidor
				+ ", tiposEmissoesNotaFiscalDistribuidor="
				+ tiposEmissoesNotaFiscalDistribuidor
				+ ", listClassificacaoCota=" + listClassificacaoCota
				+ ", gridDistribuicao=" + gridDistribuicao
				+ ", listPercentualExcedente=" + listPercentualExcedente + "]";
	}
}
