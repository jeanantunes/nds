package br.com.abril.nfe.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="NOTA_FISCAL_FATURA")
@SequenceGenerator(name = "NOTA_FISCAL_FATURA_SEQ", initialValue = 1, allocationSize = 1)
public class NotaFiscalFatura implements Serializable {

	private static final long serialVersionUID = -3352176934862817525L;

	@Id
	@Column(name="ID")
	private Long id;
	
	@Column(name="NUMERO")
	private BigInteger numero;
	
	@Column(name="VALOR")
	private BigDecimal valor;
	
	@Column(name="VENCIMENTO")
	private Date vencimento;
	
	@Column(name="NOTA_FISCAL_ID")
	@OneToMany(mappedBy="notaFiscal")
	private NotaFiscal notaFiscal;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public BigInteger getNumero() {
		return numero;
	}

	public void setNumero(BigInteger numero) {
		this.numero = numero;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public Date getVencimento() {
		return vencimento;
	}

	public void setVencimento(Date vencimento) {
		this.vencimento = vencimento;
	}

	public NotaFiscal getNotaFiscal() {
		return notaFiscal;
	}

	public void setNotaFiscal(NotaFiscal notaFiscal) {
		this.notaFiscal = notaFiscal;
	}

	@Override
	public String toString() {
		return "NotaFiscalFatura [id=" + id + ", numero=" + numero + ", valor="
				+ valor + ", vencimento=" + vencimento + "]";
	}
	
}