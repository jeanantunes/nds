package br.com.abril.nds.dto;

import java.io.Serializable;

public class VisaoEstoqueTransferenciaDTO implements Serializable {

	private static final long serialVersionUID = 7754167331745693689L;

	public Long produtoEdicaoId;
	public Long qtde;

	
	public Long getProdutoEdicaoId() {
		return produtoEdicaoId;
	}

	public void setProdutoEdicaoId(Long produtoEdicaoId) {
		this.produtoEdicaoId = produtoEdicaoId;
	}

	public Long getQtde() {
		return qtde;
	}

	public void setQtde(Long qtde) {
		this.qtde = qtde;
	}
}
