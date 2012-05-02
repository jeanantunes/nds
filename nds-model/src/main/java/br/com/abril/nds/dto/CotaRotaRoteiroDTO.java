package br.com.abril.nds.dto;

import java.io.Serializable;

import br.com.abril.nds.model.cadastro.Rota;
import br.com.abril.nds.model.cadastro.Roteiro;

/**
 * 
 * @author Diego Fernandes
 *
 */
public class CotaRotaRoteiroDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6933180219261652814L;
	

	private Integer numeroCota;
	private String nome;
	
	private String rota;
	private String roteiro;
	public Integer getNumeroCota() {
		return numeroCota;
	}
	public void setNumeroCota(Integer numeroCota) {
		this.numeroCota = numeroCota;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getRota() {
		return rota;
	}
	public void setRota(String rota) {
		this.rota = rota;
	}
	public String getRoteiro() {
		return roteiro;
	}
	public void setRoteiro(String roteiro) {
		this.roteiro = roteiro;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
		result = prime * result
				+ ((numeroCota == null) ? 0 : numeroCota.hashCode());
		result = prime * result + ((rota == null) ? 0 : rota.hashCode());
		result = prime * result + ((roteiro == null) ? 0 : roteiro.hashCode());
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
		CotaRotaRoteiroDTO other = (CotaRotaRoteiroDTO) obj;
		if (nome == null) {
			if (other.nome != null)
				return false;
		} else if (!nome.equals(other.nome))
			return false;
		if (numeroCota == null) {
			if (other.numeroCota != null)
				return false;
		} else if (!numeroCota.equals(other.numeroCota))
			return false;
		if (rota == null) {
			if (other.rota != null)
				return false;
		} else if (!rota.equals(other.rota))
			return false;
		if (roteiro == null) {
			if (other.roteiro != null)
				return false;
		} else if (!roteiro.equals(other.roteiro))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "CotaRotaRoteiroDTO [numeroCota=" + numeroCota + ", nome="
				+ nome + ", rota=" + rota + ", roteiro=" + roteiro + "]";
	}
	
	
	
	
	

}
