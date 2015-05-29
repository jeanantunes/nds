package br.com.abril.nds.model.fiscal;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

import br.com.abril.nds.model.cadastro.PessoaJuridica;

@MappedSuperclass
public abstract class NotaFiscal implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Column(name = "DESCRICAO_NATUREZA_OPERACAO")
	private	String descricaoNaturezaOperacao;
	
	@Column(name = "FORMA_PAGAMENTO")
	private	String formaPagamento;
	
	@Column(name = "HORA_SAIDA")
	private	String horaSaida;
	
	@Column(name = "AMBIENTE")
	private	String ambiente;
	
	@Column(name = "PROTOCOLO")
	private	String protocolo;
	
	@Column(name = "VERSAO")
	private	String versao;
	
	@Column(name = "EMISSOR_INSCRICAO_ESTADUAL_SUBSTITUTO")
	private	String emissorInscricaoEstadualSubstituto;
	
	@Column(name = "EMISSOR_INSCRICAO_MUNICIPAL")
	private	String emissorInscricaoMunicipal;
	
	@Column(name = "VALOR_BASE_ICMS")
	private	BigDecimal valorBaseICMS;
	
	@Column(name = "VALOR_ICMS")
	private	BigDecimal valorICMS;
	
	@Column(name = "VALOR_BASE_ICMS_SUBSTITUTO")
	private	BigDecimal valorBaseICMSSubstituto;
	
	@Column(name = "VALOR_ICMS_SUBSTITUTO")
	private	BigDecimal valorICMSSubstituto;
	
	@Column(name = "VALOR_PRODUTOS")
	private	BigDecimal valorProdutos;
	
	@Column(name = "VALOR_FRETE")
	private	BigDecimal valorFrete;
	
	@Column(name = "VALOR_SEGURO")
	private	BigDecimal valorSeguro;
	
	@Column(name = "VALOR_OUTRO")
	private	BigDecimal valorOutro;
	
	@Column(name = "VALOR_IPI")
	private	BigDecimal valorIPI;
	
	@Column(name = "VALOR_NF")
	private	BigDecimal valorNF;
	
	@Column(name = "FRETE")
	private	Integer frete;
	
	@Column(name = "TRANSPORTADORA_CNPJ")
	private	String transportadoraCNPJ;
	
	@Column(name = "TRANSPORTADORA_NOME")
	private	String transportadoraNome;
	
	@Column(name = "TRANSPORTADORA_INSCRICAO_ESTADUAL")
	private	String transportadoraInscricaoEstadual;
	
	@Column(name = "TRANSPORTADORA_ENDERECO")
	private	String transportadoraEndereco;
	
	@Column(name = "TRANSPORTADORA_MUNICIPIO")
	private	String transportadoraMunicipio;
	
	@Column(name = "TRANSPORTADORA_UF")
	private	String transportadoraUF;
	
	@Column(name = "TRANSPORTADORA_QUANTIDADE")
	private	String transportadoraQuantidade;
	
	@Column(name = "TRANSPORTADORA_ESPECIE")
	private	String transportadoraEspecie;
	
	@Column(name = "TRANSPORTADORA_MARCA")
	private	String transportadoraMarca;
	
	@Column(name = "TRANSPORTADORA_NUMERACAO")
	private	String transportadoraNumeracao;
	
	@Column(name = "TRANSPORTADORA_PESO_BRUTO")
	private	BigDecimal transportadoraPesoBruto;
	
	@Column(name = "TRANSPORTADORA_PESO_LIQUIDO")
	private	BigDecimal transportadoraPesoLiquido;
	
	@Column(name = "TRANSPORTADORA_ANTT")
	private	String transportadoraANTT;
	
	@Column(name = "TRANSPORTADORA_PLACA_VEICULO")
	private	String transportadoraPlacaVeiculo;
	
	@Column(name = "TRANSPORTADORA_PLACA_VEICULO_UF")
	private	String transportadoraPlacaVeiculoUF;
	
	@Column(name = "ISSQN_TOTAL")
	private	BigDecimal ISSQNTotal;
	
	@Column(name = "ISSQN_BASE")
	private	BigDecimal ISSQNBase;
	
	@Column(name = "ISSQN_VALOR")
	private	BigDecimal ISSQNValor;
	
	@Column(name = "INFORMACOES_COMPLEMENTARES")
	private	String informacoesComplementares;
	
	@Column(name = "NUMERO_FATURA")
	private	String numeroFatura;
	
	@Column(name = "VALOR_FATURA")
	private	BigDecimal valorFatura;
	
	@Column(name = "DATA_EMISSAO", nullable = false)
	protected Date dataEmissao;
	
	@Column(name = "DATA_EXPEDICAO", nullable = false)
	protected Date dataExpedicao;
	
	@Column(name = "NUMERO", nullable = false)
	protected Long numero;
	
	@Column(name = "SERIE", nullable  = false)
	protected Long serie;
	
	@Column(name = "CHAVE_ACESSO")
	protected String chaveAcesso;
	
	@Column(name = "VALOR_BRUTO", nullable = true)
	protected BigDecimal valorBruto;
	
	@Column(name = "VALOR_LIQUIDO", nullable = true)
	protected BigDecimal valorLiquido;
	
	@Column(name = "VALOR_DESCONTO", nullable = true)
	protected BigDecimal valorDesconto;	
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "NATUREZA_OPERACAO_ID")
	protected NaturezaOperacao naturezaOperacao;
	
	@Column(name = "VALOR_INFORMADO", nullable = true)
	protected BigDecimal valorInformado;
	
	/**
	 * Status de emissão da nota fiscal, deve ser
	 * preenchido qdo a nota for emitida pelo distribuidor
	 */
	@Enumerated(EnumType.STRING)
	@Column(name = "STATUS_EMISSAO")
	protected StatusEmissaoNotaFiscal statusEmissao;
	
	/**
	 * Flag indicando se a nota foi emitida pelo distribuidor ou
	 * recebida de terceiros
	 * true indica que a nota foi emitida pelo distribuidor
	 * false que a nota foi recebida de terceiros
	 */
	@Column(name = "EMITIDA", nullable = false)
	protected boolean emitida;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "TIPO_EMISSAO_NFE")
	private TipoEmissaoNfe tipoEmissaoNfe;
	
	/**
	 * Campo com a descrição do ocorrido na 
	 * integração da nfe.
	 */
	@Column(name="MOVIMENTO_INTEGRACAO")
	private String movimentoIntegracao;
	
	@Enumerated(EnumType.STRING)
	@Column(name="STATUS_EMISSAO_NFE") 
	private StatusEmissaoNfe statusEmissaoNfe;
	
	@ManyToOne(optional = true)
	@JoinColumn(name = "PJ_ID")
	protected PessoaJuridica emitente;
	
	public Date getDataEmissao() {
		return dataEmissao;
	}
	
	public void setDataEmissao(Date dataEmissao) {
		this.dataEmissao = dataEmissao;
	}
	
	public Date getDataExpedicao() {
		return dataExpedicao;
	}
	
	public void setDataExpedicao(Date dataExpedicao) {
		this.dataExpedicao = dataExpedicao;
	}
	
	public Long getNumero() {
		return numero;
	}
	
	public void setNumero(Long numero) {
		this.numero = numero;
	}
	
	public Long getSerie() {
		return serie;
	}
	
	public void setSerie(Long serie) {
		this.serie = serie;
	}
	
	public String getChaveAcesso() {
		return chaveAcesso;
	}
	
	public void setChaveAcesso(String chaveAcesso) {
		this.chaveAcesso = chaveAcesso;
	}

	public NaturezaOperacao getNaturezaOperacao() {
		return naturezaOperacao;
	}

	public void setNaturezaOperacao(NaturezaOperacao naturezaOperacao) {
		this.naturezaOperacao = naturezaOperacao;
	}

	public BigDecimal getValorBruto() {
		return valorBruto;
	}

	public void setValorBruto(BigDecimal valorBruto) {
		this.valorBruto = valorBruto;
	}

	public BigDecimal getValorLiquido() {
		return valorLiquido;
	}

	public void setValorLiquido(BigDecimal valorLiquido) {
		this.valorLiquido = valorLiquido;
	}

	public BigDecimal getValorDesconto() {
		return valorDesconto;
	}

	public void setValorDesconto(BigDecimal valorDesconto) {
		this.valorDesconto = valorDesconto;
	}
	
	public boolean isEmitida() {
		return emitida;
	}
	
	public void setEmitida(boolean emitida) {
		this.emitida = emitida;
	}
	
	public StatusEmissaoNotaFiscal getStatusEmissao() {
		return statusEmissao;
	}
	
	public void setStatusEmissao(StatusEmissaoNotaFiscal statusEmissao) {
		this.statusEmissao = statusEmissao;
	}

	/**
	 * Obtém tipoEmissaoNfe
	 *
	 * @return TipoEmissaoNfe
	 */
	public TipoEmissaoNfe getTipoEmissaoNfe() {
		return tipoEmissaoNfe;
	}

	/**
	 * Atribuí tipoEmissaoNfe
	 * @param tipoEmissaoNfe 
	 */
	public void setTipoEmissaoNfe(TipoEmissaoNfe tipoEmissaoNfe) {
		this.tipoEmissaoNfe = tipoEmissaoNfe;
	}

	/**
	 * Obtém movimentoIntegracao
	 *
	 * @return String
	 */
	public String getMovimentoIntegracao() {
		return movimentoIntegracao;
	}

	/**
	 * Atribuí movimentoIntegracao
	 * @param movimentoIntegracao 
	 */
	public void setMovimentoIntegracao(String movimentoIntegracao) {
		this.movimentoIntegracao = movimentoIntegracao;
	}

	/**
	 * Obtém statusEmissaoNfe
	 *
	 * @return StatusEmissaoNfe
	 */
	public StatusEmissaoNfe getStatusEmissaoNfe() {
		return statusEmissaoNfe;
	}

	/**
	 * Atribuí statusEmissaoNfe
	 * @param statusEmissaoNfe 
	 */
	public void setStatusEmissaoNfe(StatusEmissaoNfe statusEmissaoNfe) {
		this.statusEmissaoNfe = statusEmissaoNfe;
	}

	/**
	 * Obtém naturezaOperacao
	 *
	 * @return String
	 */
	public String getDescricaoNaturezaOperacao() {
		return descricaoNaturezaOperacao;
	}

	/**
	 * Atribuí naturezaOperacao
	 * @param naturezaOperacao 
	 */
	public void setNaturezaOperacao(String descricaoNaturezaOperacao) {
		this.descricaoNaturezaOperacao = descricaoNaturezaOperacao;
	}

	/**
	 * Obtém formaPagamento
	 *
	 * @return String
	 */
	public String getFormaPagamento() {
		return formaPagamento;
	}

	/**
	 * Atribuí formaPagamento
	 * @param formaPagamento 
	 */
	public void setFormaPagamento(String formaPagamento) {
		this.formaPagamento = formaPagamento;
	}

	/**
	 * Obtém horaSaida
	 *
	 * @return String
	 */
	public String getHoraSaida() {
		return horaSaida;
	}

	/**
	 * Atribuí horaSaida
	 * @param horaSaida 
	 */
	public void setHoraSaida(String horaSaida) {
		this.horaSaida = horaSaida;
	}

	/**
	 * Obtém ambiente
	 *
	 * @return String
	 */
	public String getAmbiente() {
		return ambiente;
	}

	/**
	 * Atribuí ambiente
	 * @param ambiente 
	 */
	public void setAmbiente(String ambiente) {
		this.ambiente = ambiente;
	}

	/**
	 * Obtém protocolo
	 *
	 * @return String
	 */
	public String getProtocolo() {
		return protocolo;
	}

	/**
	 * Atribuí protocolo
	 * @param protocolo 
	 */
	public void setProtocolo(String protocolo) {
		this.protocolo = protocolo;
	}

	/**
	 * Obtém versao
	 *
	 * @return String
	 */
	public String getVersao() {
		return versao;
	}

	/**
	 * Atribuí versao
	 * @param versao 
	 */
	public void setVersao(String versao) {
		this.versao = versao;
	}

	/**
	 * Obtém emissorInscricaoEstadualSubstituto
	 *
	 * @return String
	 */
	public String getEmissorInscricaoEstadualSubstituto() {
		return emissorInscricaoEstadualSubstituto;
	}

	/**
	 * Atribuí emissorInscricaoEstadualSubstituto
	 * @param emissorInscricaoEstadualSubstituto 
	 */
	public void setEmissorInscricaoEstadualSubstituto(
			String emissorInscricaoEstadualSubstituto) {
		this.emissorInscricaoEstadualSubstituto = emissorInscricaoEstadualSubstituto;
	}

	/**
	 * Obtém emissorInscricaoMunicipal
	 *
	 * @return String
	 */
	public String getEmissorInscricaoMunicipal() {
		return emissorInscricaoMunicipal;
	}

	/**
	 * Atribuí emissorInscricaoMunicipal
	 * @param emissorInscricaoMunicipal 
	 */
	public void setEmissorInscricaoMunicipal(String emissorInscricaoMunicipal) {
		this.emissorInscricaoMunicipal = emissorInscricaoMunicipal;
	}

	/**
	 * Obtém valorBaseICMS
	 *
	 * @return BigDecimal
	 */
	public BigDecimal getValorBaseICMS() {
		return valorBaseICMS;
	}

	/**
	 * Atribuí valorBaseICMS
	 * @param valorBaseICMS 
	 */
	public void setValorBaseICMS(BigDecimal valorBaseICMS) {
		this.valorBaseICMS = valorBaseICMS;
	}

	/**
	 * Obtém valorICMS
	 *
	 * @return BigDecimal
	 */
	public BigDecimal getValorICMS() {
		return valorICMS;
	}

	/**
	 * Atribuí valorICMS
	 * @param valorICMS 
	 */
	public void setValorICMS(BigDecimal valorICMS) {
		this.valorICMS = valorICMS;
	}

	/**
	 * Obtém valorBaseICMSSubstituto
	 *
	 * @return BigDecimal
	 */
	public BigDecimal getValorBaseICMSSubstituto() {
		return valorBaseICMSSubstituto;
	}

	/**
	 * Atribuí valorBaseICMSSubstituto
	 * @param valorBaseICMSSubstituto 
	 */
	public void setValorBaseICMSSubstituto(BigDecimal valorBaseICMSSubstituto) {
		this.valorBaseICMSSubstituto = valorBaseICMSSubstituto;
	}

	/**
	 * Obtém valorICMSSubstituto
	 *
	 * @return BigDecimal
	 */
	public BigDecimal getValorICMSSubstituto() {
		return valorICMSSubstituto;
	}

	/**
	 * Atribuí valorICMSSubstituto
	 * @param valorICMSSubstituto 
	 */
	public void setValorICMSSubstituto(BigDecimal valorICMSSubstituto) {
		this.valorICMSSubstituto = valorICMSSubstituto;
	}

	/**
	 * Obtém valorProdutos
	 *
	 * @return BigDecimal
	 */
	public BigDecimal getValorProdutos() {
		return valorProdutos;
	}

	/**
	 * Atribuí valorProdutos
	 * @param valorProdutos 
	 */
	public void setValorProdutos(BigDecimal valorProdutos) {
		this.valorProdutos = valorProdutos;
	}

	/**
	 * Obtém valorFrete
	 *
	 * @return BigDecimal
	 */
	public BigDecimal getValorFrete() {
		return valorFrete;
	}

	/**
	 * Atribuí valorFrete
	 * @param valorFrete 
	 */
	public void setValorFrete(BigDecimal valorFrete) {
		this.valorFrete = valorFrete;
	}

	/**
	 * Obtém valorSeguro
	 *
	 * @return BigDecimal
	 */
	public BigDecimal getValorSeguro() {
		return valorSeguro;
	}

	/**
	 * Atribuí valorSeguro
	 * @param valorSeguro 
	 */
	public void setValorSeguro(BigDecimal valorSeguro) {
		this.valorSeguro = valorSeguro;
	}

	/**
	 * Obtém valorOutro
	 *
	 * @return BigDecimal
	 */
	public BigDecimal getValorOutro() {
		return valorOutro;
	}

	/**
	 * Atribuí valorOutro
	 * @param valorOutro 
	 */
	public void setValorOutro(BigDecimal valorOutro) {
		this.valorOutro = valorOutro;
	}

	/**
	 * Obtém valorIPI
	 *
	 * @return BigDecimal
	 */
	public BigDecimal getValorIPI() {
		return valorIPI;
	}

	/**
	 * Atribuí valorIPI
	 * @param valorIPI 
	 */
	public void setValorIPI(BigDecimal valorIPI) {
		this.valorIPI = valorIPI;
	}

	/**
	 * Obtém valorNF
	 *
	 * @return BigDecimal
	 */
	public BigDecimal getValorNF() {
		return valorNF;
	}

	/**
	 * Atribuí valorNF
	 * @param valorNF 
	 */
	public void setValorNF(BigDecimal valorNF) {
		this.valorNF = valorNF;
	}

	/**
	 * Obtém frete
	 *
	 * @return Integer
	 */
	public Integer getFrete() {
		return frete;
	}

	/**
	 * Atribuí frete
	 * @param frete 
	 */
	public void setFrete(Integer frete) {
		this.frete = frete;
	}

	/**
	 * Obtém transportadoraCNPJ
	 *
	 * @return String
	 */
	public String getTransportadoraCNPJ() {
		return transportadoraCNPJ;
	}

	/**
	 * Atribuí transportadoraCNPJ
	 * @param transportadoraCNPJ 
	 */
	public void setTransportadoraCNPJ(String transportadoraCNPJ) {
		this.transportadoraCNPJ = transportadoraCNPJ;
	}

	/**
	 * Obtém transportadoraNome
	 *
	 * @return String
	 */
	public String getTransportadoraNome() {
		return transportadoraNome;
	}

	/**
	 * Atribuí transportadoraNome
	 * @param transportadoraNome 
	 */
	public void setTransportadoraNome(String transportadoraNome) {
		this.transportadoraNome = transportadoraNome;
	}

	/**
	 * Obtém transportadoraInscricaoEstadual
	 *
	 * @return String
	 */
	public String getTransportadoraInscricaoEstadual() {
		return transportadoraInscricaoEstadual;
	}

	/**
	 * Atribuí transportadoraInscricaoEstadual
	 * @param transportadoraInscricaoEstadual 
	 */
	public void setTransportadoraInscricaoEstadual(
			String transportadoraInscricaoEstadual) {
		this.transportadoraInscricaoEstadual = transportadoraInscricaoEstadual;
	}

	/**
	 * Obtém transportadoraEndereco
	 *
	 * @return String
	 */
	public String getTransportadoraEndereco() {
		return transportadoraEndereco;
	}

	/**
	 * Atribuí transportadoraEndereco
	 * @param transportadoraEndereco 
	 */
	public void setTransportadoraEndereco(String transportadoraEndereco) {
		this.transportadoraEndereco = transportadoraEndereco;
	}

	/**
	 * Obtém transportadoraMunicipio
	 *
	 * @return String
	 */
	public String getTransportadoraMunicipio() {
		return transportadoraMunicipio;
	}

	/**
	 * Atribuí transportadoraMunicipio
	 * @param transportadoraMunicipio 
	 */
	public void setTransportadoraMunicipio(String transportadoraMunicipio) {
		this.transportadoraMunicipio = transportadoraMunicipio;
	}

	/**
	 * Obtém transportadoraUF
	 *
	 * @return String
	 */
	public String getTransportadoraUF() {
		return transportadoraUF;
	}

	/**
	 * Atribuí transportadoraUF
	 * @param transportadoraUF 
	 */
	public void setTransportadoraUF(String transportadoraUF) {
		this.transportadoraUF = transportadoraUF;
	}

	/**
	 * Obtém transportadoraQuantidade
	 *
	 * @return String
	 */
	public String getTransportadoraQuantidade() {
		return transportadoraQuantidade;
	}

	/**
	 * Atribuí transportadoraQuantidade
	 * @param transportadoraQuantidade 
	 */
	public void setTransportadoraQuantidade(String transportadoraQuantidade) {
		this.transportadoraQuantidade = transportadoraQuantidade;
	}

	/**
	 * Obtém transportadoraEspecie
	 *
	 * @return String
	 */
	public String getTransportadoraEspecie() {
		return transportadoraEspecie;
	}

	/**
	 * Atribuí transportadoraEspecie
	 * @param transportadoraEspecie 
	 */
	public void setTransportadoraEspecie(String transportadoraEspecie) {
		this.transportadoraEspecie = transportadoraEspecie;
	}

	/**
	 * Obtém transportadoraMarca
	 *
	 * @return String
	 */
	public String getTransportadoraMarca() {
		return transportadoraMarca;
	}

	/**
	 * Atribuí transportadoraMarca
	 * @param transportadoraMarca 
	 */
	public void setTransportadoraMarca(String transportadoraMarca) {
		this.transportadoraMarca = transportadoraMarca;
	}

	/**
	 * Obtém transportadoraNumeracao
	 *
	 * @return String
	 */
	public String getTransportadoraNumeracao() {
		return transportadoraNumeracao;
	}

	/**
	 * Atribuí transportadoraNumeracao
	 * @param transportadoraNumeracao 
	 */
	public void setTransportadoraNumeracao(String transportadoraNumeracao) {
		this.transportadoraNumeracao = transportadoraNumeracao;
	}

	/**
	 * Obtém transportadoraPesoBruto
	 *
	 * @return BigDecimal
	 */
	public BigDecimal getTransportadoraPesoBruto() {
		return transportadoraPesoBruto;
	}

	/**
	 * Atribuí transportadoraPesoBruto
	 * @param transportadoraPesoBruto 
	 */
	public void setTransportadoraPesoBruto(BigDecimal transportadoraPesoBruto) {
		this.transportadoraPesoBruto = transportadoraPesoBruto;
	}

	/**
	 * Obtém transportadoraPesoLiquido
	 *
	 * @return BigDecimal
	 */
	public BigDecimal getTransportadoraPesoLiquido() {
		return transportadoraPesoLiquido;
	}

	/**
	 * Atribuí transportadoraPesoLiquido
	 * @param transportadoraPesoLiquido 
	 */
	public void setTransportadoraPesoLiquido(BigDecimal transportadoraPesoLiquido) {
		this.transportadoraPesoLiquido = transportadoraPesoLiquido;
	}

	/**
	 * Obtém transportadoraANTT
	 *
	 * @return String
	 */
	public String getTransportadoraANTT() {
		return transportadoraANTT;
	}

	/**
	 * Atribuí transportadoraANTT
	 * @param transportadoraANTT 
	 */
	public void setTransportadoraANTT(String transportadoraANTT) {
		this.transportadoraANTT = transportadoraANTT;
	}

	/**
	 * Obtém transportadoraPlacaVeiculo
	 *
	 * @return String
	 */
	public String getTransportadoraPlacaVeiculo() {
		return transportadoraPlacaVeiculo;
	}

	/**
	 * Atribuí transportadoraPlacaVeiculo
	 * @param transportadoraPlacaVeiculo 
	 */
	public void setTransportadoraPlacaVeiculo(String transportadoraPlacaVeiculo) {
		this.transportadoraPlacaVeiculo = transportadoraPlacaVeiculo;
	}

	/**
	 * Obtém transportadoraPlacaVeiculoUF
	 *
	 * @return String
	 */
	public String getTransportadoraPlacaVeiculoUF() {
		return transportadoraPlacaVeiculoUF;
	}

	/**
	 * Atribuí transportadoraPlacaVeiculoUF
	 * @param transportadoraPlacaVeiculoUF 
	 */
	public void setTransportadoraPlacaVeiculoUF(String transportadoraPlacaVeiculoUF) {
		this.transportadoraPlacaVeiculoUF = transportadoraPlacaVeiculoUF;
	}

	/**
	 * Obtém iSSQNTotal
	 *
	 * @return BigDecimal
	 */
	public BigDecimal getISSQNTotal() {
		return ISSQNTotal;
	}

	/**
	 * Atribuí iSSQNTotal
	 * @param iSSQNTotal 
	 */
	public void setISSQNTotal(BigDecimal iSSQNTotal) {
		ISSQNTotal = iSSQNTotal;
	}

	/**
	 * Obtém iSSQNBase
	 *
	 * @return BigDecimal
	 */
	public BigDecimal getISSQNBase() {
		return ISSQNBase;
	}

	/**
	 * Atribuí iSSQNBase
	 * @param iSSQNBase 
	 */
	public void setISSQNBase(BigDecimal iSSQNBase) {
		ISSQNBase = iSSQNBase;
	}

	/**
	 * Obtém iSSQNValor
	 *
	 * @return BigDecimal
	 */
	public BigDecimal getISSQNValor() {
		return ISSQNValor;
	}

	/**
	 * Atribuí iSSQNValor
	 * @param iSSQNValor 
	 */
	public void setISSQNValor(BigDecimal iSSQNValor) {
		ISSQNValor = iSSQNValor;
	}

	/**
	 * Obtém informacoesComplementares
	 *
	 * @return String
	 */
	public String getInformacoesComplementares() {
		return informacoesComplementares;
	}

	/**
	 * Atribuí informacoesComplementares
	 * @param informacoesComplementares 
	 */
	public void setInformacoesComplementares(String informacoesComplementares) {
		this.informacoesComplementares = informacoesComplementares;
	}

	/**
	 * Obtém numeroFatura
	 *
	 * @return String
	 */
	public String getNumeroFatura() {
		return numeroFatura;
	}

	/**
	 * Atribuí numeroFatura
	 * @param numeroFatura 
	 */
	public void setNumeroFatura(String numeroFatura) {
		this.numeroFatura = numeroFatura;
	}

	/**
	 * Obtém valorFatura
	 *
	 * @return BigDecimal
	 */
	public BigDecimal getValorFatura() {
		return valorFatura;
	}

	/**
	 * Atribuí valorFatura
	 * @param valorFatura 
	 */
	public void setValorFatura(BigDecimal valorFatura) {
		this.valorFatura = valorFatura;
	}

	/**
	 * @return the emitente
	 */
	public PessoaJuridica getEmitente() {
		return emitente;
	}

	/**
	 * @param emitente the emitente to set
	 */
	public void setEmitente(PessoaJuridica emitente) {
		this.emitente = emitente;
	}

	public BigDecimal getValorInformado() {
		return valorInformado;
	}

	public void setValorInformado(BigDecimal valorInformado) {
		this.valorInformado = valorInformado;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.getNumero() == null) ? 0 : this.getNumero().hashCode());
		result = prime * result + ((this.getSerie() == null) ? 0 : this.getSerie().hashCode());
		result = prime * result + ((this.getChaveAcesso() == null) ? 0 : this.getChaveAcesso().hashCode());
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
		NotaFiscal other = (NotaFiscal) obj;
		if (this.getSerie() == null) {
			if (other.getSerie() != null)
				return false;
		} else if (!this.getSerie().equals(other.getSerie()))
			return false;
		if (this.getNumero() == null) {
			if (other.getNumero() != null)
				return false;
		} else if (!this.getNumero().equals(other.getNumero()))
			return false;
		if (this.getChaveAcesso() == null) {
			if (other.getChaveAcesso() != null)
				return false;
		} else if (!this.getChaveAcesso().equals(other.getChaveAcesso()))
			return false;
		
		
		return true;
	}
	
	
	
}