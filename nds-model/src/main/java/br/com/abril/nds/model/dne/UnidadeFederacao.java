package br.com.abril.nds.model.dne;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * @author Discover Technology
 *
 */
@Entity
@Table(name="LOG_FAIXA_UF")
public class UnidadeFederacao implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1528902275097474122L;

	@Id
	@Column(name="UFE_SG", unique=true, nullable=false, length=4)
	private String sigla;

	@Column(name="UFE_CEP_INI", length=16)
	private String faixaCepInicial;

	@Column(name="UFE_CEP_FIM", length=16)
	private String faixaCepFinal;

    public UnidadeFederacao() {
    }

	/**
	 * @return the sigla
	 */
	public String getSigla() {
		return sigla;
	}

	/**
	 * @param sigla the sigla to set
	 */
	public void setSigla(String sigla) {
		this.sigla = sigla;
	}

	/**
	 * @return the faixaCepInicial
	 */
	public String getFaixaCepInicial() {
		return faixaCepInicial;
	}

	/**
	 * @param faixaCepInicial the faixaCepInicial to set
	 */
	public void setFaixaCepInicial(String faixaCepInicial) {
		this.faixaCepInicial = faixaCepInicial;
	}

	/**
	 * @return the faixaCepFinal
	 */
	public String getFaixaCepFinal() {
		return faixaCepFinal;
	}

	/**
	 * @param faixaCepFinal the faixaCepFinal to set
	 */
	public void setFaixaCepFinal(String faixaCepFinal) {
		this.faixaCepFinal = faixaCepFinal;
	}   
}