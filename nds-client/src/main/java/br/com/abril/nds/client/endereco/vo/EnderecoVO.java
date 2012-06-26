package br.com.abril.nds.client.endereco.vo;

import java.io.Serializable;


public class EnderecoVO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String uf;
	
	private String cep;
	
	private Long codigoCidadeIBGE;
	
	private Long idLocalidade;
	
	private String localidade;
	
	private String logradouro;
	
	private String tipoLogradouro;
	
	private Long codigoBairro;
	
	private String bairro;

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
	 * @return the codigoCidadeIBGE
	 */
	public Long getCodigoCidadeIBGE() {
		return codigoCidadeIBGE;
	}

	/**
	 * @param codigoCidadeIBGE the codigoCidadeIBGE to set
	 */
	public void setCodigoCidadeIBGE(Long codigoCidadeIBGE) {
		this.codigoCidadeIBGE = codigoCidadeIBGE;
	}

	/**
	 * @return the idLocalidade
	 */
	public Long getIdLocalidade() {
		return idLocalidade;
	}

	/**
	 * @param idLocalidade the idLocalidade to set
	 */
	public void setIdLocalidade(Long idLocalidade) {
		this.idLocalidade = idLocalidade;
	}

	/**
	 * @return the localidade
	 */
	public String getLocalidade() {
		return localidade;
	}

	/**
	 * @param localidade the localidade to set
	 */
	public void setLocalidade(String localidade) {
		this.localidade = localidade;
	}

	/**
	 * @return the logradouro
	 */
	public String getLogradouro() {
		return logradouro;
	}

	/**
	 * @param logradouro the logradouro to set
	 */
	public void setLogradouro(String logradouro) {
		this.logradouro = logradouro;
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
	 * @return the codigoBairro
	 */
	public Long getCodigoBairro() {
		return codigoBairro;
	}

	/**
	 * @param codigoBairro the codigoBairro to set
	 */
	public void setCodigoBairro(Long codigoBairro) {
		this.codigoBairro = codigoBairro;
	}

	/**
	 * @return the bairro
	 */
	public String getBairro() {
		return bairro;
	}

	/**
	 * @param bairro the bairro to set
	 */
	public void setBairro(String bairro) {
		this.bairro = bairro;
	}
}
