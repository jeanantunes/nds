package br.com.abril.nds.integracao.ems0130.outbound;

import java.io.Serializable;

import com.ancientprogramming.fixedformat4j.annotation.Field;
import com.ancientprogramming.fixedformat4j.annotation.Record;

@Record
public class EMS0130Output implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Integer codigoDaCota;
	private String endereco;
	private String codigoBairro;
	private String nomeMunicipio;
	private String siglaUf;
	private String cep;
	private String ddd;
	private String telefone;
	private Long tipoPontoVenda;
	private String pontoDeReferencia;
	
	
	@Field(offset=1, length=4)
	public Integer getCodigoDaCota() {
		return codigoDaCota;
	}
	public void setCodigoDaCota(Integer codigoDaCota) {
		this.codigoDaCota = codigoDaCota;
	}
	
	@Field(offset=5, length=40)
	public String getEndereco() {
		return endereco;
	}
	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}
	
	@Field(offset=45, length=5)
	public String getCodigoBairro() {
		return codigoBairro;
	}
	public void setCodigoBairro(String codigoBairro) {
		this.codigoBairro = codigoBairro;
	}
	
	@Field(offset=50, length=20)
	public String getNomeMunicipio() {
		return nomeMunicipio;
	}
	public void setNomeMunicipio(String nomeMunicipio) {
		this.nomeMunicipio = nomeMunicipio;
	}
	
	@Field(offset=70, length=2)
	public String getSiglaUf() {
		return siglaUf;
	}
	public void setSiglaUf(String siglaUf) {
		this.siglaUf = siglaUf;
	}
	
	@Field(offset=72, length=8)
	public String getCep() {
		return cep;
	}
	public void setCep(String cep) {
		this.cep = cep;
	}
	
	@Field(offset=80, length=4)
	public String getDdd() {
		return ddd;
	}
	public void setDdd(String ddd) {
		this.ddd = ddd;
	}
	
	@Field(offset=84, length=7)
	public String getTelefone() {
		return telefone;
	}
	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}
	
	@Field(offset=91, length=2)
	public Long getTipoPontoVenda() {
		return tipoPontoVenda;
	}
	public void setTipoPontoVenda(Long tipoPontoVenda) {
		this.tipoPontoVenda = tipoPontoVenda;
	}
	
	@Field(offset=93, length=40)
	public String getPontoDeReferencia() {
		return pontoDeReferencia;
	}
	public void setPontoDeReferencia(String pontoDeReferencia) {
		this.pontoDeReferencia = pontoDeReferencia;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cep == null) ? 0 : cep.hashCode());
		result = prime * result
				+ ((codigoBairro == null) ? 0 : codigoBairro.hashCode());
		result = prime * result
				+ ((codigoDaCota == null) ? 0 : codigoDaCota.hashCode());
		result = prime * result + ((ddd == null) ? 0 : ddd.hashCode());
		result = prime * result
				+ ((endereco == null) ? 0 : endereco.hashCode());
		result = prime * result
				+ ((nomeMunicipio == null) ? 0 : nomeMunicipio.hashCode());
		result = prime
				* result
				+ ((pontoDeReferencia == null) ? 0 : pontoDeReferencia
						.hashCode());
		result = prime * result + ((siglaUf == null) ? 0 : siglaUf.hashCode());
		result = prime * result
				+ ((telefone == null) ? 0 : telefone.hashCode());
		result = prime * result
				+ ((tipoPontoVenda == null) ? 0 : tipoPontoVenda.hashCode());
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
		EMS0130Output other = (EMS0130Output) obj;
		if (cep == null) {
			if (other.cep != null)
				return false;
		} else if (!cep.equals(other.cep))
			return false;
		if (codigoBairro == null) {
			if (other.codigoBairro != null)
				return false;
		} else if (!codigoBairro.equals(other.codigoBairro))
			return false;
		if (codigoDaCota == null) {
			if (other.codigoDaCota != null)
				return false;
		} else if (!codigoDaCota.equals(other.codigoDaCota))
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
		if (nomeMunicipio == null) {
			if (other.nomeMunicipio != null)
				return false;
		} else if (!nomeMunicipio.equals(other.nomeMunicipio))
			return false;
		if (pontoDeReferencia == null) {
			if (other.pontoDeReferencia != null)
				return false;
		} else if (!pontoDeReferencia.equals(other.pontoDeReferencia))
			return false;
		if (siglaUf == null) {
			if (other.siglaUf != null)
				return false;
		} else if (!siglaUf.equals(other.siglaUf))
			return false;
		if (telefone == null) {
			if (other.telefone != null)
				return false;
		} else if (!telefone.equals(other.telefone))
			return false;
		if (tipoPontoVenda == null) {
			if (other.tipoPontoVenda != null)
				return false;
		} else if (!tipoPontoVenda.equals(other.tipoPontoVenda))
			return false;
		return true;
	}
	
	
	

}
