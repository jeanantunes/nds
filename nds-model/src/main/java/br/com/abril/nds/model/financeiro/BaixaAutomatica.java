package br.com.abril.nds.model.financeiro;

import java.util.Date;

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "BAIXA_AUTOMATICA")
@SequenceGenerator(name = "BAIXA_AUTOMATICA_SEQ", initialValue = 1, allocationSize = 1)
public class BaixaAutomatica {

	@Id
	@GeneratedValue(generator = "BAIXA_AUTOMATICA_SEQ")
	@Column(name = "ID")
	private Long id;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "DT_BAIXA", nullable = false)
	private Date dataBaixa;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "STATUS", nullable = false)
	private StatusBaixa status;
	
	@Column(name = "NOME_ARQUIVO", nullable = false)
	private String nomeArquivo;
	
	@ManyToOne(optional = true)
	@JoinColumn(name = "BOLETO_ID")
	private Boleto boleto;

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the dataBaixa
	 */
	public Date getDataBaixa() {
		return dataBaixa;
	}

	/**
	 * @param dataBaixa the dataBaixa to set
	 */
	public void setDataBaixa(Date dataBaixa) {
		this.dataBaixa = dataBaixa;
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

	/**
	 * @return the nomeArquivo
	 */
	public String getNomeArquivo() {
		return nomeArquivo;
	}

	/**
	 * @param nomeArquivo the nomeArquivo to set
	 */
	public void setNomeArquivo(String nomeArquivo) {
		this.nomeArquivo = nomeArquivo;
	}

	/**
	 * @return the boleto
	 */
	public Boleto getBoleto() {
		return boleto;
	}

	/**
	 * @param boleto the boleto to set
	 */
	public void setBoleto(Boleto boleto) {
		this.boleto = boleto;
	}

}
