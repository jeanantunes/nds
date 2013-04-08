package br.com.abril.nds.dto;

import java.io.Serializable;

import br.com.abril.nds.model.cadastro.Cota;

public class CotaQueNaoEntrouNoEstudoDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	private Cota cota;
	private String motivo;
	private String quantidade;

	public CotaQueNaoEntrouNoEstudoDTO(Cota cota) {
		super();
		this.cota = cota;
	}
	
	public Cota getCota() {
		return cota;
	}

	public String getNumero() {
		return cota.getNumeroCota().toString();
	}

	public String getNome() {
		return cota.getPessoa().getNome();
	}

	public String getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(String quantidade) {
		this.quantidade = quantidade;
	}
	
	public String getMotivo() {
		return motivo;
	}

	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cota == null) ? 0 : cota.hashCode());
		result = prime * result + ((motivo == null) ? 0 : motivo.hashCode());
		result = prime * result
				+ ((quantidade == null) ? 0 : quantidade.hashCode());
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
		CotaQueNaoEntrouNoEstudoDTO other = (CotaQueNaoEntrouNoEstudoDTO) obj;
		if (cota == null) {
			if (other.cota != null)
				return false;
		} else if (!cota.equals(other.cota))
			return false;
		if (motivo == null) {
			if (other.motivo != null)
				return false;
		} else if (!motivo.equals(other.motivo))
			return false;
		if (quantidade == null) {
			if (other.quantidade != null)
				return false;
		} else if (!quantidade.equals(other.quantidade))
			return false;
		return true;
	}

}
