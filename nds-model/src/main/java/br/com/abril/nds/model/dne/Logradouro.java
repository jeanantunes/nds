package br.com.abril.nds.model.dne;

import java.io.Serializable;

import br.com.abril.nds.integracao.model.canonic.IntegracaoDocument;


/**
 * @author Discover Technology
 *
 */
public class Logradouro  extends IntegracaoDocument implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3210879486950002174L;


	private String abreviatura;

	private String cep;

	private Bairro bairroFinal;

	private Bairro bairroInicial;

	private String status;
	
	private String nome;

	private String complemento;

	private String uf;

	private String tipoLogradouro;

	private Localidade localidade;

    public Logradouro() {
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
	 * @return the cep
	 */
	public String getCep() {
		return cep;
	}

	/**
	 * @param cep the cep to set
	 */
	public void setCep(String cep) {
		this.cep = cep;
	}



	/**
	 * @return the bairroFinal
	 */
	public Bairro getBairroFinal() {
		return bairroFinal;
	}


	/**
	 * @param bairroFinal the bairroFinal to set
	 */
	public void setBairroFinal(Bairro bairroFinal) {
		this.bairroFinal = bairroFinal;
	}


	/**
	 * @return the bairroInicial
	 */
	public Bairro getBairroInicial() {
		return bairroInicial;
	}


	/**
	 * @param bairroInicial the bairroInicial to set
	 */
	public void setBairroInicial(Bairro bairroInicial) {
		this.bairroInicial = bairroInicial;
	}


	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
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
	 * @return the complemento
	 */
	public String getComplemento() {
		return complemento;
	}

	/**
	 * @param complemento the complemento to set
	 */
	public void setComplemento(String complemento) {
		this.complemento = complemento;
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
	 * @return the tipoLogradouro
	 */
	public String getTipoLogradouro() {
		return tipoLogradouro;
	}

	/**
	 * @param tipoLogradouro the tipoLogradouro to set
	 */
	public void setTipoLogradouro(String tipoLogradouro) {
		this.tipoLogradouro = tipoLogradouro;
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