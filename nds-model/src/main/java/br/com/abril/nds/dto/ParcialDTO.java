package br.com.abril.nds.dto;

import java.io.Serializable;

public class ParcialDTO implements Serializable{
	
	private static final long serialVersionUID = 5323481858856643957L;
	
	private Long idProdutoEdicao;
	private String dataLancamento;
	private String dataRecolhimento;
	private String codigoProduto;
	private String nomeProduto;
	private Integer numEdicao;
	private String nomeFornecedor;
	private String statusParcial;
	
	public ParcialDTO() {}
		
	
	public ParcialDTO(String dataLancamento, String dataRecolhimento,
			String codigoProduto, String nomeProduto, Integer numEdicao,
			String nomeFornecedor, String statusParcial) {
		super();
		this.dataLancamento = dataLancamento;
		this.dataRecolhimento = dataRecolhimento;
		this.codigoProduto = codigoProduto;
		this.nomeProduto = nomeProduto;
		this.numEdicao = numEdicao;
		this.nomeFornecedor = nomeFornecedor;
		this.statusParcial = statusParcial;
	}

	public String getDataLancamento() {
		return dataLancamento;
	}
	public void setDataLancamento(String dataLancamento) {
		this.dataLancamento = dataLancamento;
	}
	public String getDataRecolhimento() {
		return dataRecolhimento;
	}
	public void setDataRecolhimento(String dataRecolhimento) {
		this.dataRecolhimento = dataRecolhimento;
	}
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
	public Integer getNumEdicao() {
		return numEdicao;
	}
	public void setNumEdicao(Integer numEdicao) {
		this.numEdicao = numEdicao;
	}
	public String getNomeFornecedor() {
		return nomeFornecedor;
	}
	public void setNomeFornecedor(String nomeFornecedor) {
		this.nomeFornecedor = nomeFornecedor;
	}
	public String getStatusParcial() {
		return statusParcial;
	}
	public void setStatusParcial(String statusParcial) {
		this.statusParcial = statusParcial;
	}


	/**
	 * @return the idProdutoEdicao
	 */
	public Long getIdProdutoEdicao() {
		return idProdutoEdicao;
	}


	/**
	 * @param idProdutoEdicao the idProdutoEdicao to set
	 */
	public void setIdProdutoEdicao(Long idProdutoEdicao) {
		this.idProdutoEdicao = idProdutoEdicao;
	}

	
	
}
