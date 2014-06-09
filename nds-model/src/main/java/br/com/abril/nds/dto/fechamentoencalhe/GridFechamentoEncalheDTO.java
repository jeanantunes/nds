package br.com.abril.nds.dto.fechamentoencalhe;

import java.math.BigInteger;


public class GridFechamentoEncalheDTO {
	
	private boolean checkbox;
	
	private Long codigo;
	
	private Long produtoEdicao;
	
	private BigInteger fisico;

	public boolean isCheckbox() {
		return checkbox;
	}

	public void setCheckbox(boolean checkbox) {
		this.checkbox = checkbox;
	}

	public Long getCodigo() {
		return codigo;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

	public BigInteger getFisico() {
		return fisico;
	}

	public void setFisico(BigInteger fisico) {
		this.fisico = fisico;
	}

	/**
	 * @return the produtoEdicao
	 */
	public Long getProdutoEdicao() {
		return produtoEdicao;
	}

	/**
	 * @param produtoEdicao the produtoEdicao to set
	 */
	public void setProdutoEdicao(Long produtoEdicao) {
		this.produtoEdicao = produtoEdicao;
	}

	/**
	 * @return the listaGrid
	 */
}
