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

	@Column(name = "DATA_EMISSAO", nullable = false)
	protected Date dataEmissao;
	
	@Column(name = "DATA_EXPEDICAO", nullable = false)
	protected Date dataExpedicao;
	
	@Column(name = "NUMERO", nullable = false)
	protected String numero;
	
	@Column(name = "SERIE", nullable  = false)
	protected String serie;
	
	@Column(name = "CHAVE_ACESSO")
	protected String chaveAcesso;
	
	@Column(name = "VALOR_BRUTO", nullable = false)
	protected BigDecimal valorBruto;
	
	@Column(name = "VALOR_LIQUIDO", nullable = false)
	protected BigDecimal valorLiquido;
	
	@Column(name = "VALOR_DESCONTO", nullable = false)
	protected BigDecimal valorDesconto;	
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "CFOP_ID")
	protected CFOP cfop;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "PJ_ID")
	protected PessoaJuridica emitente;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "TIPO_NF_ID")
	protected TipoNotaFiscal tipoNotaFiscal;
	
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
	
	public String getNumero() {
		return numero;
	}
	
	public void setNumero(String numero) {
		this.numero = numero;
	}
	
	public String getSerie() {
		return serie;
	}
	
	public void setSerie(String serie) {
		this.serie = serie;
	}
	
	public String getChaveAcesso() {
		return chaveAcesso;
	}
	
	public void setChaveAcesso(String chaveAcesso) {
		this.chaveAcesso = chaveAcesso;
	}
	
	public CFOP getCfop() {
		return cfop;
	}

	public void setCfop(CFOP cfop) {
		this.cfop = cfop;
	}

	public PessoaJuridica getEmitente() {
		return emitente;
	}
	
	public void setEmitente(PessoaJuridica emitente) {
		this.emitente = emitente;
	}
	
	public TipoNotaFiscal getTipoNotaFiscal() {
		return tipoNotaFiscal;
	}

	public void setTipoNotaFiscal(TipoNotaFiscal tipoNotaFiscal) {
		this.tipoNotaFiscal = tipoNotaFiscal;
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
	
	
}