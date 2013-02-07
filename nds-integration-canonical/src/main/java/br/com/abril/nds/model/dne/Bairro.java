package br.com.abril.nds.model.dne;

import java.io.Serializable;

import br.com.abril.nds.integracao.model.canonic.IntegracaoDocument;

/**
 * @author Discover Technology
 *
 */
public class Bairro extends IntegracaoDocument implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2098584768905693662L;

	private String abreviatura;

	private String nome;

	private String uf;
	
	private Localidade localidade;

    public Bairro() {
    }

	/**
	 * @return the abreviatura
	 */
	public String getAbreviatura() {
		return abreviatura;
	}

	/**
	 * @param abreviatura the abreviatura to set
	 */
	public void setAbreviatura(String abreviatura) {
		this.abreviatura = abreviatura;
	}

	/**
	 * @return the nome
	 */
	public String getNome() {
		return nome;
	}

	/**
	 * @param nome the nome to set
	 */
	public void setNome(String nome) {
		this.nome = nome;
	}

	/**
	 * @return the uf
	 */
	public String getUf() {
		return uf;
	}

	/**
	 * @param uf the uf to set
	 */
	public void setUf(String uf) {
		this.uf = uf;
	}

	/**
	 * @return the localidade
	 */
	public Localidade getLocalidade() {
		return localidade;
	}

	/**
	 * @param localidade the localidade to set
	 */
	public void setLocalidade(Localidade localidade) {
		this.localidade = localidade;
	}
}