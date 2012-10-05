package br.com.abril.nds.model.financeiro;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "BAIXA_COBRANCA")
@SequenceGenerator(name = "BAIXA_COBRANCA_SEQ", initialValue = 1, allocationSize = 1)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TIPO_BAIXA", discriminatorType = DiscriminatorType.STRING)
public abstract class BaixaCobranca {

	@Id
	@GeneratedValue(generator = "BAIXA_COBRANCA_SEQ")
	@Column(name = "ID")
	private Long id;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "DATA_BAIXA", nullable = false)
	private Date dataBaixa;
	
	@Column(name = "VALOR_PAGO", nullable = false)
	private BigDecimal valorPago;
	
	@OneToOne(optional = true)
	@JoinColumn(name = "COBRANCA_ID")
	private Cobranca cobranca;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "STATUS", nullable = true)
	private StatusBaixa status;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getDataBaixa() {
		return dataBaixa;
	}

	public void setDataBaixa(Date dataBaixa) {
		this.dataBaixa = dataBaixa;
	}

	public BigDecimal getValorPago() {
		return valorPago;
	}

	public void setValorPago(BigDecimal valorPago) {
		this.valorPago = valorPago;
	}
	
	public Cobranca getCobranca() {
		return cobranca;
	}

	public void setCobranca(Cobranca cobranca) {
		this.cobranca = cobranca;
	}
	
	/**
	 * @return the status
	 */
	public StatusBaixa getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(StatusBaixa status) {
		this.status = status;
	}
	
}
