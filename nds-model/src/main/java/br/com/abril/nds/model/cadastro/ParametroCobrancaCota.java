package br.com.abril.nds.model.cadastro;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "PARAMETRO_COBRANCA_COTA")
@SequenceGenerator(name="PARAMETRO_COBRANCA_COTA_SEQ", initialValue = 1, allocationSize = 1)
public class ParametroCobrancaCota {
	
	@Id
	@Column(name = "ID")
	@GeneratedValue(generator = "PARAMETRO_COBRANCA_COTA_SEQ")
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "FORMA_COBRANCA_ID")
	private FormaCobranca formaCobranca;
	
	@ManyToOne
	@JoinColumn(name = "COTA_ID")
	private Cota cota;
	
	@Column(name = "RECEBE_COBRANCA_EMAIL")
	private boolean recebeCobrancaEmail;
	
	@Column(name = "VALOR_MINIMO_COBRANCA")
	private BigDecimal valorMininoCobranca;
	
	@Column(name = "FATOR_VENCIMENTO")
	private int fatorVencimento;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public FormaCobranca getFormaCobranca() {
		return formaCobranca;
	}

	public void setFormaCobranca(FormaCobranca formaCobranca) {
		this.formaCobranca = formaCobranca;
	}

	public Cota getCota() {
		return cota;
	}

	public void setCota(Cota cota) {
		this.cota = cota;
	}

	public boolean isRecebeCobrancaEmail() {
		return recebeCobrancaEmail;
	}

	public void setRecebeCobrancaEmail(boolean recebeCobrancaEmail) {
		this.recebeCobrancaEmail = recebeCobrancaEmail;
	}

	public BigDecimal getValorMininoCobranca() {
		return valorMininoCobranca;
	}

	public void setValorMininoCobranca(BigDecimal valorMininoCobranca) {
		this.valorMininoCobranca = valorMininoCobranca;
	}

	public int getFatorVencimento() {
		return fatorVencimento;
	}

	public void setFatorVencimento(int fatorVencimento) {
		this.fatorVencimento = fatorVencimento;
	}

}
