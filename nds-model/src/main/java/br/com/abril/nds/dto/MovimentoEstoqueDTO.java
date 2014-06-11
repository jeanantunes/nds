package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigInteger;

import br.com.abril.nds.model.estoque.GrupoMovimentoEstoque;

public class MovimentoEstoqueDTO implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -9160921967560123153L;

	private GrupoMovimentoEstoque grupoMovimentoEstoque ;
	
	public GrupoMovimentoEstoque getGrupoMovimentoEstoque() {
		return grupoMovimentoEstoque;
	}

	public void setGrupoMovimentoEstoque(GrupoMovimentoEstoque grupoMovimentoEstoque) {
		this.grupoMovimentoEstoque = grupoMovimentoEstoque;
	}

	private BigInteger qtde;
	
	private Long id;

	

	public BigInteger getQtde() {
		return qtde;
	}

	public void setQtde(BigInteger qtde) {
		this.qtde = qtde;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	

}
