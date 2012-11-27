package br.com.abril.nds.integracao.ems0116.inbound;

import java.io.Serializable;

import com.ancientprogramming.fixedformat4j.annotation.Field;
import com.ancientprogramming.fixedformat4j.annotation.Record;

/**
 * Classe de entrada do arquivo BANCA.NEW
 * @author erick.dzadotz
 * @version 1.0
 */
@Record
public class EMS0116Input implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer codCota;
	private String endereco;
	private String codBairro;
	private String nomeMunicipio;
	private String siglaUF;
	private String cep;
	private String ddd;
	private String telefone;
	private Long tipoPontoVenda;
	private String pontoReferencia;
	
	@Field(offset = 1, length = 4)
	public Integer getCodCota() {
		return codCota;
	}
	
	public void setCodCota(Integer codCota) {
		this.codCota = codCota;
	}
	
	@Field(offset = 5, length = 40)
	public String getEndereco() {
		return endereco;
	}
	
	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}
	
	@Field(offset = 45, length = 5)
	public String getCodBairro() {
		return codBairro;
	}
	
	public void setCodBairro(String codBairro) {
		this.codBairro = codBairro;
	}
	
	@Field(offset = 50, length = 20)
	public String getNomeMunicipio() {
		return nomeMunicipio;
	}
	
	public void setNomeMunicipio(String nomeMunicipio) {
		this.nomeMunicipio = nomeMunicipio;
	}
	
	@Field(offset = 70, length = 2)
	public String getSiglaUF() {
		return siglaUF;
	}
	
	public void setSiglaUF(String siglaUF) {
		this.siglaUF = siglaUF;
	}
	
	@Field(offset = 72, length = 8)
	public String getCep() {
		return cep;
	}
	
	public void setCep(String cep) {
		this.cep = cep;
	}
	
	@Field(offset = 80, length = 4)
	public String getDdd() {
		return ddd;
	}
	
	public void setDdd(String ddd) {
		this.ddd = ddd;
	}
	
	@Field(offset = 84, length = 7)
	public String getTelefone() {
		return telefone;
	}
	
	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}
	
	@Field(offset = 91, length = 2)
	public Long getTipoPontoVenda() {
		return tipoPontoVenda;
	}
	
	public void setTipoPontoVenda(Long tipoPontoVenda) {
		this.tipoPontoVenda = tipoPontoVenda;
	}
	
	@Field(offset = 93, length = 40)
	public String getPontoReferencia() {
		return pontoReferencia;
	}
	
	public void setPontoReferencia(String pontoReferencia) {
		this.pontoReferencia = pontoReferencia;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cep == null) ? 0 : cep.hashCode());
		result = prime * result
				+ ((codBairro == null) ? 0 : codBairro.hashCode());
		result = prime * result + ((codCota == null) ? 0 : codCota.hashCode());
		result = prime * result + ((ddd == null) ? 0 : ddd.hashCode());
		result = prime * result
				+ ((endereco == null) ? 0 : endereco.hashCode());
		result = prime * result
				+ ((nomeMunicipio == null) ? 0 : nomeMunicipio.hashCode());
		result = prime * result
				+ ((pontoReferencia == null) ? 0 : pontoReferencia.hashCode());
		result = prime * result + ((siglaUF == null) ? 0 : siglaUF.hashCode());
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
		EMS0116Input other = (EMS0116Input) obj;
		if (cep == null) {
			if (other.cep != null)
				return false;
		} else if (!cep.equals(other.cep))
			return false;
		if (codBairro == null) {
			if (other.codBairro != null)
				return false;
		} else if (!codBairro.equals(other.codBairro))
			return false;
		if (codCota == null) {
			if (other.codCota != null)
				return false;
		} else if (!codCota.equals(other.codCota))
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
		if (pontoReferencia == null) {
			if (other.pontoReferencia != null)
				return false;
		} else if (!pontoReferencia.equals(other.pontoReferencia))
			return false;
		if (siglaUF == null) {
			if (other.siglaUF != null)
				return false;
		} else if (!siglaUF.equals(other.siglaUF))
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
	
	@Override
	public String toString() {
		return "EMS0116Input [codCota=" + codCota + ", endereco=" + endereco
				+ ", codBairro=" + codBairro + ", nomeMunicipio="
				+ nomeMunicipio + ", siglaUF=" + siglaUF + ", cep=" + cep
				+ ", ddd=" + ddd + ", telefone=" + telefone
				+ ", tipoPontoVenda=" + tipoPontoVenda + ", pontoReferencia="
				+ pontoReferencia + "]";
	}	
}
