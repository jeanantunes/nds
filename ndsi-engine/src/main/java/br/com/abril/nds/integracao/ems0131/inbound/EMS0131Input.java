package br.com.abril.nds.integracao.ems0131.inbound;

import java.io.Serializable;

import com.ancientprogramming.fixedformat4j.annotation.Field;
import com.ancientprogramming.fixedformat4j.annotation.Record;

@Record
public class EMS0131Input implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// CAMPO -- NOME DA TABELA
	
	private Integer codigoDaCota; //NUMERO_COTA -- COTA
	private String nomeDoJornaleiro; //NOME -- PESSOA
	//private Integer quantidadeDeCotas; 
	private String endereco; //LOGRADOURO -- ENDERECO
	//private codigoDoBairro;
	private String municipio; //CIDADE -- ENDERECO
	private String siglaUf; //UF -- ENDERECO
	private String cep; //CEP -- ENDEERCO
	private String ddd; //DDD -- TELEFONE
	private String telefone; //NUMERO -- TELEFONE
	private String situacaoCota; //SITUACAO_CADASTRO -- COTA
	//private condPrazoPagamento;
	private Long codigoDoBox; //CODIGO -- BOX
	private Long codigoTipoBox; //TIPO_BOX -- BOX
	//private repartePorPdv;
	private Long codigoDoCapataz; //CODIGO -- ENTREGADOR
	private String cpfCnpj; //CPF -- PESSOA / CNPJ -- PESSOA
	private String tipoDePessoa; //TIPO -- PESSOA
	private Integer numeroDoLogradouro; //NUMERO -- ENDERECO
	//private codigoDaCidade;
	private String inscricaoEstadual; //INSC_ESTADUAL -- PESSOA
	//private inscricaoMunicipal;
	
	@Field(offset = 1, length = 4)
	public Integer getCodigoDaCota() {
		return codigoDaCota;
	}
	public void setCodigoDaCota(Integer codigoDaCota) {
		this.codigoDaCota = codigoDaCota;
	}
	
	@Field(offset = 5, length = 30)
	public String getNomeDoJornaleiro() {
		return nomeDoJornaleiro;
	}
	public void setNomeDoJornaleiro(String nomeDoJornaleiro) {
		this.nomeDoJornaleiro = nomeDoJornaleiro;
	}
	
	@Field(offset = 39, length = 40)
	public String getEndereco() {
		return endereco;
	}
	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}
	
	@Field(offset = 84, length = 20)
	public String getMunicipio() {
		return municipio;
	}
	public void setMunicipio(String municipio) {
		this.municipio = municipio;
	}
	
	@Field(offset = 104, length = 2)
	public String getSiglaUf() {
		return siglaUf;
	}
	public void setSiglaUf(String siglaUf) {
		this.siglaUf = siglaUf;
	}
	
	@Field(offset = 106, length = 8)
	public String getCep() {
		return cep;
	}
	public void setCep(String cep) {
		this.cep = cep;
	}
	
	@Field(offset = 114, length = 4)
	public String getDdd() {
		return ddd;
	}
	public void setDdd(String ddd) {
		this.ddd = ddd;
	}
	
	@Field(offset = 118, length = 7)
	public String getTelefone() {
		return telefone;
	}
	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}
	
	@Field(offset = 125, length = 1)
	public String getSituacaoCota() {
		return situacaoCota;
	}
	public void setSituacaoCota(String situacaoCota) {
		this.situacaoCota = situacaoCota;
	}
	
	@Field(offset = 127, length = 2)
	public Long getCodigoDoBox() {
		return codigoDoBox;
	}
	public void setCodigoDoBox(Long codigoDoBox) {
		this.codigoDoBox = codigoDoBox;
	}
	
	@Field(offset = 129, length = 2)
	public Long getCodigoTipoBox() {
		return codigoTipoBox;
	}
	public void setCodigoTipoBox(Long codigoTipoBox) {
		this.codigoTipoBox = codigoTipoBox;
	}
	
	@Field(offset = 132, length = 6)
	public Long getCodigoDoCapataz() {
		return codigoDoCapataz;
	}
	public void setCodigoDoCapataz(Long codigoDoCapataz) {
		this.codigoDoCapataz = codigoDoCapataz;
	}
	
	@Field(offset = 138, length = 14)
	public String getCpfCnpj() {
		return cpfCnpj;
	}
	public void setCpfCnpj(String cpfCnpj) {
		this.cpfCnpj = cpfCnpj;
	}
	
	@Field(offset = 152, length = 1)
	public String getTipoDePessoa() {
		return tipoDePessoa;
	}
	public void setTipoDePessoa(String tipoDePessoa) {
		this.tipoDePessoa = tipoDePessoa;
	}
	
	@Field(offset = 153, length = 6)
	public Integer getNumeroDoLogradouro() {
		return numeroDoLogradouro;
	}
	public void setNumeroDoLogradouro(Integer numeroDoLogradouro) {
		this.numeroDoLogradouro = numeroDoLogradouro;
	}
	
	@Field(offset = 166, length = 20)
	public String getInscricaoEstadual() {
		return inscricaoEstadual;
	}
	public void setInscricaoEstadual(String inscricaoEstadual) {
		this.inscricaoEstadual = inscricaoEstadual;
	}
	
	@Override
	public String toString() {
		return "EMS0131Input [codigoDaCota=" + codigoDaCota
				+ ", nomeDoJornaleiro=" + nomeDoJornaleiro + ", endereco="
				+ endereco + ", municipio=" + municipio + ", siglaUf="
				+ siglaUf + ", cep=" + cep + ", ddd=" + ddd + ", telefone="
				+ telefone + ", situacaoCota=" + situacaoCota
				+ ", codigoDoBox=" + codigoDoBox + ", codigoTipoBox="
				+ codigoTipoBox + ", codigoDoCapataz=" + codigoDoCapataz
				+ ", cpfCnpj=" + cpfCnpj + ", tipoDePessoa=" + tipoDePessoa
				+ ", numeroDoLogradouro=" + numeroDoLogradouro
				+ ", inscricaoEstadual=" + inscricaoEstadual + "]";
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cep == null) ? 0 : cep.hashCode());
		result = prime * result
				+ ((codigoDaCota == null) ? 0 : codigoDaCota.hashCode());
		result = prime * result
				+ ((codigoDoBox == null) ? 0 : codigoDoBox.hashCode());
		result = prime * result
				+ ((codigoDoCapataz == null) ? 0 : codigoDoCapataz.hashCode());
		result = prime * result
				+ ((codigoTipoBox == null) ? 0 : codigoTipoBox.hashCode());
		result = prime * result + ((cpfCnpj == null) ? 0 : cpfCnpj.hashCode());
		result = prime * result + ((ddd == null) ? 0 : ddd.hashCode());
		result = prime * result
				+ ((endereco == null) ? 0 : endereco.hashCode());
		result = prime
				* result
				+ ((inscricaoEstadual == null) ? 0 : inscricaoEstadual
						.hashCode());
		result = prime * result
				+ ((municipio == null) ? 0 : municipio.hashCode());
		result = prime
				* result
				+ ((nomeDoJornaleiro == null) ? 0 : nomeDoJornaleiro.hashCode());
		result = prime
				* result
				+ ((numeroDoLogradouro == null) ? 0 : numeroDoLogradouro
						.hashCode());
		result = prime * result + ((siglaUf == null) ? 0 : siglaUf.hashCode());
		result = prime * result
				+ ((situacaoCota == null) ? 0 : situacaoCota.hashCode());
		result = prime * result
				+ ((telefone == null) ? 0 : telefone.hashCode());
		result = prime * result
				+ ((tipoDePessoa == null) ? 0 : tipoDePessoa.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EMS0131Input other = (EMS0131Input) obj;
		if (cep == null) {
			if (other.cep != null)
				return false;
		} else if (!cep.equals(other.cep))
			return false;
		if (codigoDaCota == null) {
			if (other.codigoDaCota != null)
				return false;
		} else if (!codigoDaCota.equals(other.codigoDaCota))
			return false;
		if (codigoDoBox == null) {
			if (other.codigoDoBox != null)
				return false;
		} else if (!codigoDoBox.equals(other.codigoDoBox))
			return false;
		if (codigoDoCapataz == null) {
			if (other.codigoDoCapataz != null)
				return false;
		} else if (!codigoDoCapataz.equals(other.codigoDoCapataz))
			return false;
		if (codigoTipoBox == null) {
			if (other.codigoTipoBox != null)
				return false;
		} else if (!codigoTipoBox.equals(other.codigoTipoBox))
			return false;
		if (cpfCnpj == null) {
			if (other.cpfCnpj != null)
				return false;
		} else if (!cpfCnpj.equals(other.cpfCnpj))
			return false;
		if (ddd == null) {
			if (other.ddd != null)
				return false;
		} else if (!ddd.equals(other.ddd))
			return false;
		if (endereco == null) {
			if (other.endereco != null)
				return false;
		} else if (!endereco.equals(other.endereco))
			return false;
		if (inscricaoEstadual == null) {
			if (other.inscricaoEstadual != null)
				return false;
		} else if (!inscricaoEstadual.equals(other.inscricaoEstadual))
			return false;
		if (municipio == null) {
			if (other.municipio != null)
				return false;
		} else if (!municipio.equals(other.municipio))
			return false;
		if (nomeDoJornaleiro == null) {
			if (other.nomeDoJornaleiro != null)
				return false;
		} else if (!nomeDoJornaleiro.equals(other.nomeDoJornaleiro))
			return false;
		if (numeroDoLogradouro == null) {
			if (other.numeroDoLogradouro != null)
				return false;
		} else if (!numeroDoLogradouro.equals(other.numeroDoLogradouro))
			return false;
		if (siglaUf == null) {
			if (other.siglaUf != null)
				return false;
		} else if (!siglaUf.equals(other.siglaUf))
			return false;
		if (situacaoCota == null) {
			if (other.situacaoCota != null)
				return false;
		} else if (!situacaoCota.equals(other.situacaoCota))
			return false;
		if (telefone == null) {
			if (other.telefone != null)
				return false;
		} else if (!telefone.equals(other.telefone))
			return false;
		if (tipoDePessoa == null) {
			if (other.tipoDePessoa != null)
				return false;
		} else if (!tipoDePessoa.equals(other.tipoDePessoa))
			return false;
		return true;
	}
	
	
	
	

}
