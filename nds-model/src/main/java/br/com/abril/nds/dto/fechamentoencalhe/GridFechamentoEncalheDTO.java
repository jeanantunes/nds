package br.com.abril.nds.dto.fechamentoencalhe;


public class GridFechamentoEncalheDTO {
	
	private boolean checkbox;
	
	private Long codigo;
	
	private Long produtoEdicao;
	
	private Long fisico;

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

	public Long getFisico() {
		return fisico;
	}

	public void setFisico(Long fisico) {
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
