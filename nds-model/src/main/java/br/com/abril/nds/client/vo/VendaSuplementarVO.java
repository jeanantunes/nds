package br.com.abril.nds.client.vo;

import java.io.Serializable;

public class VendaSuplementarVO implements Serializable {

	
	private static final long serialVersionUID = -4008717189078779935L;
	
	private Long idVendaSuplementar;
	
	private Long idEncalheSuplementar;
	
	private Long idProdutoVenda;
	
	private Long idProdutoEncalhe;

	public Long getIdVendaSuplementar() {
		return idVendaSuplementar;
	}

	public void setIdVendaSuplementar(Long idVendaSuplementar) {
		this.idVendaSuplementar = idVendaSuplementar;
	}

	public Long getIdEncalheSuplementar() {
		return idEncalheSuplementar;
	}

	public void setIdEncalheSuplementar(Long idEncalheSuplementar) {
		this.idEncalheSuplementar = idEncalheSuplementar;
	}

	public Long getIdProdutoVenda() {
		return idProdutoVenda;
	}

	public void setIdProdutoVenda(Long idProdutoVenda) {
		this.idProdutoVenda = idProdutoVenda;
	}

	public Long getIdProdutoEncalhe() {
		return idProdutoEncalhe;
	}

	public void setIdProdutoEncalhe(Long idProdutoEncalhe) {
		this.idProdutoEncalhe = idProdutoEncalhe;
	}
	
}
