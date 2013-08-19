package br.com.abril.nds.model.dne;

import java.io.Serializable;
import java.util.List;

import br.com.abril.nds.integracao.model.canonic.IntegracaoDocument;


/**
 * @author Discover Technology
 *
 */
public class Localidade  extends IntegracaoDocument implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4438288966441513488L;


	private String abreviatura;

	private String cep;

	private Long chaveSubordinacao;

	private Long codigoMunicipioIBGE;

	private String nome;

	private String tipoLocalidade;

	private String sit;

	private UnidadeFederacao unidadeFederacao;

	private List<Bairro> bairros;

	private List<Logradouro> logradouros;

    public Localidade() {
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
	 * @return the chaveSubordinacao
	 */
	public Long getChaveSubordinacao() {
		return chaveSubordinacao;
	}

	/**
	 * @param chaveSubordinacao the chaveSubordinacao to set
	 */
	public void setChaveSubordinacao(Long chaveSubordinacao) {
		this.chaveSubordinacao = chaveSubordinacao;
	}

	/**
	 * @return the codigoMunicipioIBGE
	 */
	public Long getCodigoMunicipioIBGE() {
		return codigoMunicipioIBGE;
	}

	/**
	 * @param codigoMunicipioIBGE the codigoMunicipioIBGE to set
	 */
	public void setCodigoMunicipioIBGE(Long codigoMunicipioIBGE) {
		this.codigoMunicipioIBGE = codigoMunicipioIBGE;
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
	 * @return the tipoLocalidade
	 */
	public String getTipoLocalidade() {
		return tipoLocalidade;
	}

	/**
	 * @param tipoLocalidade the tipoLocalidade to set
	 */
	public void setTipoLocalidade(String tipoLocalidade) {
		this.tipoLocalidade = tipoLocalidade;
	}

	/**
	 * @return the sit
	 */
	public String getSit() {
		return sit;
	}

	/**
	 * @param sit the sit to set
	 */
	public void setSit(String sit) {
		this.sit = sit;
	}

	/**
	 * @return the unidadeFederacao
	 */
	public UnidadeFederacao getUnidadeFederacao() {
		return unidadeFederacao;
	}

	/**
	 * @param unidadeFederacao the unidadeFederacao to set
	 */
	public void setUnidadeFederacao(UnidadeFederacao unidadeFederacao) {
		this.unidadeFederacao = unidadeFederacao;
	}

	/**
	 * @return the bairros
	 */
	public List<Bairro> getBairros() {
		return bairros;
	}

	/**
	 * @param bairros the bairros to set
	 */
	public void setBairros(List<Bairro> bairros) {
		this.bairros = bairros;
	}

	/**
	 * @return the logradouros
	 */
	public List<Logradouro> getLogradouros() {
		return logradouros;
	}

	/**
	 * @param logradouros the logradouros to set
	 */
	public void setLogradouros(List<Logradouro> logradouros) {
		this.logradouros = logradouros;
	}

}