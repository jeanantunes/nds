package br.com.abril.nds.dto;

import java.io.Serializable;
import java.util.List;

public class ProdutoEdicaoAbastecimentoDTO implements Serializable{

	private static final long serialVersionUID = -2951289520494037916L;
	
	private String codigoProduto;
	
	private String nomeProduto;
	
	private Long numeroEdicao;
	
	private String codigoDeBarras;
	
	private String precoCapa;
	
	private Integer qtdeExms;
	
	private List<ItemDTO<Integer, Integer>> itensCotas;
	
	public String getCodigoProduto() {
		return codigoProduto;
	}

	public void setCodigoProduto(String codigoProduto) {
		this.codigoProduto = codigoProduto;
	}

	public String getNomeProduto() {
		return nomeProduto;
	}

	public void setNomeProduto(String nomeProduto) {
		this.nomeProduto = nomeProduto;
	}

	public Long getNumeroEdicao() {
		return numeroEdicao;
	}

	public void setNumeroEdicao(Long numeroEdicao) {
		this.numeroEdicao = numeroEdicao;
	}

	public String getCodigoDeBarras() {
		return codigoDeBarras;
	}

	public void setCodigoDeBarras(String codigoDeBarras) {
		this.codigoDeBarras = codigoDeBarras;
	}

	public String getPrecoCapa() {
		return precoCapa;
	}

	public void setPrecoCapa(String precoCapa) {
		this.precoCapa = precoCapa;
	}

	public Integer getQtdeExms() {
		return qtdeExms;
	}

	public void setQtdeExms(Integer qtdeExms) {
		this.qtdeExms = qtdeExms;
	}

	public List<ItemDTO<Integer, Integer>> getItensCotas() {
		return itensCotas;
	}
	
	public void setItensCotas(List<ItemDTO<Integer, Integer>> itensCotas) {
		this.itensCotas = itensCotas;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((codigoDeBarras == null) ? 0 : codigoDeBarras.hashCode());
		result = prime * result
				+ ((codigoProduto == null) ? 0 : codigoProduto.hashCode());
		result = prime * result
				+ ((nomeProduto == null) ? 0 : nomeProduto.hashCode());
		result = prime * result
				+ ((numeroEdicao == null) ? 0 : numeroEdicao.hashCode());
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
		ProdutoEdicaoAbastecimentoDTO other = (ProdutoEdicaoAbastecimentoDTO) obj;
		if (codigoDeBarras == null) {
			if (other.codigoDeBarras != null)
				return false;
		} else if (!codigoDeBarras.equals(other.codigoDeBarras))
			return false;
		if (codigoProduto == null) {
			if (other.codigoProduto != null)
				return false;
		} else if (!codigoProduto.equals(other.codigoProduto))
			return false;
		if (nomeProduto == null) {
			if (other.nomeProduto != null)
				return false;
		} else if (!nomeProduto.equals(other.nomeProduto))
			return false;
		if (numeroEdicao == null) {
			if (other.numeroEdicao != null)
				return false;
		} else if (!numeroEdicao.equals(other.numeroEdicao))
			return false;
		return true;
	}

}