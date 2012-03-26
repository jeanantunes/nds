package br.com.abril.nds.model.cadastro;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "FORMA_COBRANCA")
@SequenceGenerator(name="FORMA_COBRANCA_SEQ", initialValue = 1, allocationSize = 1)
public class FormaCobranca {
	
	@Id
	@GeneratedValue(generator = "FORMA_COBRANCA_SEQ")
	private Long id;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "TIPO_COBRANCA", nullable = false)
	private TipoCobranca tipoCobranca;
	
	@ManyToOne
	@JoinColumn(name = "BANCO_ID")
	private Banco banco;
	
	@Column(name = "VALOR_MINIMO_EMISSAO")
	private BigDecimal valorMinimoEmissao;
	
	@Column(name = "MULTA")
	private BigDecimal multa;
	
	@Column(name = "JUROS")
	private BigDecimal juros;
	
	@Column(name = "VENCIMENTO_DIA_UTIL", nullable = false)
	private boolean vencimentoDiaUtil;
	
	@Column(name = "PRINCIPAL", nullable = false)
	private boolean principal;
	
	@Column(name = "ENVIA_EMAIL", nullable = false)
	private boolean enviaEmail;
	
	@Column(name = "INSTRUCOES")
	private String instrucoes;
	
	@Column(name = "ATIVA", nullable = false)
	private boolean ativa;
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public TipoCobranca getTipoCobranca() {
		return tipoCobranca;
	}
	
	public void setTipoCobranca(TipoCobranca tipoCobranca) {
		this.tipoCobranca = tipoCobranca;
	}
	
	public Banco getBanco() {
		return banco;
	}
	
	public void setBanco(Banco banco) {
		this.banco = banco;
	}
	
	public BigDecimal getValorMinimoEmissao() {
		return valorMinimoEmissao;
	}
	
	public void setValorMinimoEmissao(BigDecimal valorMinimoEmissao) {
		this.valorMinimoEmissao = valorMinimoEmissao;
	}
	
	public BigDecimal getMulta() {
		return multa;
	}
	
	public void setMulta(BigDecimal multa) {
		this.multa = multa;
	}
	
	public BigDecimal getJuros() {
		return juros;
	}
	
	public void setJuros(BigDecimal juros) {
		this.juros = juros;
	}
	
	public boolean isVencimentoDiaUtil() {
		return vencimentoDiaUtil;
	}
	
	public void setVencimentoDiaUtil(boolean vencimentoDiaUtil) {
		this.vencimentoDiaUtil = vencimentoDiaUtil;
	}
	
	public boolean isPrincipal() {
		return principal;
	}

	public void setPrincipal(boolean principal) {
		this.principal = principal;
	}
	
	public boolean isEnviaEmail() {
		return enviaEmail;
	}
	
	public void setEnviaEmail(boolean enviaEmail) {
		this.enviaEmail = enviaEmail;
	}
	
	public String getInstrucoes() {
		return instrucoes;
	}
	
	public void setInstrucoes(String instrucoes) {
		this.instrucoes = instrucoes;
	}
	
	
	public boolean isAtiva() {
		return ativa;
	}
	
	public void setAtiva(boolean ativa) {
		this.ativa = ativa;
	}

}
