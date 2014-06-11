package br.com.abril.nds.model.dne;

import java.io.Serializable;

import br.com.abril.nds.integracao.model.canonic.IntegracaoDocument;


/**
 * @author Discover Technology
 *
 */
public class UnidadeFederacao  extends IntegracaoDocument implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1528902275097474122L;

	private String sigla;

	private String faixaCepInicial;

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