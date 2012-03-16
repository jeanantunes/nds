package br.com.abril.nds.model.fiscal;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

import br.com.abril.nds.model.cadastro.PessoaJuridica;

@MappedSuperclass
public abstract class NotaFiscal {

	@Column(name = "DATA_EMISSAO", nullable = false)
	private Date dataEmissao;
	
	@Column(name = "DATA_EXPEDICAO", nullable = false)
	private Date dataExpedicao;
	
	@Column(name = "NUMERO", nullable = false)
	private String numero;
	
	@Column(name = "SERIE", nullable  = false)
	private String serie;
	
	@Column(name = "CHAVE_ACESSO")
	private String chaveAcesso;
	
	@Column(name = "VALOR_BRUTO", nullable = false)
	private BigDecimal valorBruto;
	
	@Column(name = "VALOR_LIQUIDO", nullable = false)
	private BigDecimal valorLiquido;
	
	@Column(name = "VALOR_DESCONTO", nullable = false)
	private BigDecimal valorDesconto;	
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "CFOP_ID")
	private CFOP cfop;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "PJ_ID")
	private PessoaJuridica emitente;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "TIPO_NF_ID")
	private TipoNotaFiscal tipoNotaFiscal;
	
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
	
}