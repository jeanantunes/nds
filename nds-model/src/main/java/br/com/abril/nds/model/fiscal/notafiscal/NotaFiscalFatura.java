package br.com.abril.nds.model.fiscal.notafiscal;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="NOTA_FISCAL_FATURA")
public class NotaFiscalFatura implements Serializable {

	private static final long serialVersionUID = -3352176934862817525L;

	@Id
	@GeneratedValue
	@Column(name="ID")
	private Long id;
	
	@Column(name="NUMERO")
	private String numero;
	
	@Column(name="VALOR")
	private BigDecimal valor;
	
	@Column(name="VENCIMENTO")
	private Date vencimento;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	public String getNumero() {
		return numero;
	}
	
	public void setNumero(String numero) {
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

	@Override
	public String toString() {
		return "NotaFiscalFatura [id=" + id + ", numero=" + numero + ", valor="
				+ valor + ", vencimento=" + vencimento + "]";
	}
	
}
