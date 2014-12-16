package br.com.abril.nds.dto;

import java.io.Serializable;
import java.util.List;

public class BoxRoterioRotaDTO implements Serializable {

	private static final long serialVersionUID = -1022730482920692146L;
	
	private Integer codigoBox;
	
	private String roteiro;
	
	private String codigoRota;
	
	private String nomeRota;
	
	private List<ProdutoEdicaoAbastecimentoDTO> produtos;

	public Integer getCodigoBox() {
		return codigoBox;
	}

	public void setCodigoBox(Integer codigoBox) {
		this.codigoBox = codigoBox;
	}

	public String getRoteiro() {
		return roteiro;
	}

	public void setRoteiro(String roteiro) {
		this.roteiro = roteiro;
	}

	public String getCodigoRota() {
		return codigoRota;
	}

	public void setCodigoRota(String codigoRota) {
		this.codigoRota = codigoRota;
	}

	public String getNomeRota() {
		return nomeRota;
	}

	public void setNomeRota(String nomeRota) {
		this.nomeRota = nomeRota;
	}
	
	public List<ProdutoEdicaoAbastecimentoDTO> getProdutos() {
		return produtos;
	}

	public void setProdutos(List<ProdutoEdicaoAbastecimentoDTO> produtos) {
		this.produtos = produtos;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((codigoBox == null) ? 0 : codigoBox.hashCode());
		result = prime * result
				+ ((codigoRota == null) ? 0 : codigoRota.hashCode());
		result = prime * result
				+ ((nomeRota == null) ? 0 : nomeRota.hashCode());
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
		BoxRoterioRotaDTO other = (BoxRoterioRotaDTO) obj;
		if (codigoBox == null) {
			if (other.codigoBox != null)
				return false;
		} else if (!codigoBox.equals(other.codigoBox))
			return false;
		if (codigoRota == null) {
			if (other.codigoRota != null)
				return false;
		} else if (!codigoRota.equals(other.codigoRota))
			return false;
		if (nomeRota == null) {
			if (other.nomeRota != null)
				return false;
		} else if (!nomeRota.equals(other.nomeRota))
			return false;
		if (roteiro == null) {
			if (other.roteiro != null)
				return false;
		} else if (!roteiro.equals(other.roteiro))
			return false;
		return true;
	}
	
	
}