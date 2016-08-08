package br.com.abril.nds.vo;

import java.io.Serializable;

import br.com.abril.nds.integracao.model.canonic.IntegracaoDocument;
import br.com.abril.nds.model.cadastro.TipoEndereco;


public class EnderecoVO extends IntegracaoDocument implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String uf;
	
	private String cep;
	
	private Long codigoCidadeIBGE;
	
	private String idLocalidade;
	
	private String localidade;
	
	private String logradouro;
	
	private String tipoLogradouro;
		
	private String bairro;
	
	private TipoEndereco tipoEndereco;
	
	private String numero;
	
	private String complemento;
	
	private Long codigoUf;

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
		this.set_id(cep);
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
	public String getIdLocalidade() {
		return idLocalidade;
	}

	/**
	 * @param idLocalidade the idLocalidade to set
	 */
	public void setIdLocalidade(String idLocalidade) {
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

	/**
	 * @return the tipoEndereco
	 */
	public TipoEndereco getTipoEndereco() {
		return tipoEndereco;
	}

	/**
	 * @param tipoEndereco the tipoEndereco to set
	 */
	public void setTipoEndereco(TipoEndereco tipoEndereco) {
		this.tipoEndereco = tipoEndereco;
	}

	/**
	 * @return the numero
	 */
	public String getNumero() {
		return numero;
	}

	/**
	 * @param numero the numero to set
	 */
	public void setNumero(String numero) {
		this.numero = numero;
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

	public Long getCodigoUf() {
		return codigoUf;
	}

	public void setCodigoUf(Long codigoUf) {
		this.codigoUf = codigoUf;
	}
	
}
