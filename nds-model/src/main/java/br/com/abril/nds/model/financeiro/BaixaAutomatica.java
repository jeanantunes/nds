package br.com.abril.nds.model.financeiro;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * @author Discover Technology
 * @version 1.0
 * @created 15-mar-2012 10:00:00
 */
@Entity
@DiscriminatorValue(value = "AUTOMATICA")
public class BaixaAutomatica extends BaixaCobranca {
	
	@Column(name = "NOME_ARQUIVO", nullable = true)
	private String nomeArquivo;
	
	@Column(name = "NUM_REGISTRO_ARQUIVO", nullable = true)
	private Integer numeroRegistroArquivo;
	
	@Column(name = "NOSSO_NUMERO", nullable = true)
	private String nossoNumero;

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
	 * @return the nossoNumero
	 */
	public String getNossoNumero() {
		return nossoNumero;
	}

	/**
	 * @param nossoNumero the nossoNumero to set
	 */
	public void setNossoNumero(String nossoNumero) {
		this.nossoNumero = nossoNumero;
	}

}
