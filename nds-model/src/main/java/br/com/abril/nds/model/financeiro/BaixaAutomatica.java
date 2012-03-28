package br.com.abril.nds.model.financeiro;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * @author Discover Technology
 * @version 1.0
 * @created 15-mar-2012 10:00:00
 */
@Entity
@DiscriminatorValue(value = "AUTOMATICA")
public class BaixaAutomatica extends BaixaCobranca {

	@Enumerated(EnumType.STRING)
	@Column(name = "STATUS", nullable = true)
	private StatusBaixa status;
	
	@Column(name = "NOME_ARQUIVO", nullable = true)
	private String nomeArquivo;
	
	@Column(name = "NUM_REGISTRO_ARQUIVO", nullable = true)
	private Integer numeroRegistroArquivo;
	
	@ManyToOne(optional = true)
	@JoinColumn(name = "BOLETO_ID")
	private Boleto boleto;

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
	 * @return the numeroRegistroArquivo
	 */
	public Integer getNumeroRegistroArquivo() {
		return numeroRegistroArquivo;
	}

	/**
	 * @param numeroRegistroArquivo the numeroRegistroArquivo to set
	 */
	public void setNumeroRegistroArquivo(Integer numeroRegistroArquivo) {
		this.numeroRegistroArquivo = numeroRegistroArquivo;
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
