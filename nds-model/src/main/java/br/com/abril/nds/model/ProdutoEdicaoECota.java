package br.com.abril.nds.model;

public class ProdutoEdicaoECota {
	
	private Long produtoEdicaoID;
	
	private Long cotaID;

	public Long getProdutoEdicaoID() {
		return produtoEdicaoID;
	}

	public void setProdutoEdicaoID(Long produtoEdicaoID) {
		this.produtoEdicaoID = produtoEdicaoID;
	}

	public Long getCotaID() {
		return cotaID;
	}

	public void setCotaID(Long cotaID) {
		this.cotaID = cotaID;
	}

	@Override
	public String toString() {
		return "ProdutoEdicaoECota [produtoEdicaoID=" + produtoEdicaoID + ", cotaID=" + cotaID + "]";
	}

	
	
}
