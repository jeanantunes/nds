package br.com.abril.nds.dto;

import java.io.Serializable;

import br.com.abril.nds.util.export.Exportable;

@Exportable
public class ItemNotaFiscalPendenteDTO implements Serializable {

	private static final long serialVersionUID = 4238499960020175179L;
	
	private String codigoProduto;
	private String nomeProduto;
	private Long numeroEdicao;
	
	public ItemNotaFiscalPendenteDTO() {}

	public ItemNotaFiscalPendenteDTO(String codigoProduto, String nomeProduto,
			Long numeroEdicao) {
		super();
		this.codigoProduto = codigoProduto;
		this.nomeProduto = nomeProduto;
		this.numeroEdicao = numeroEdicao;
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

	public Long getNumeroEdicao() {
		return numeroEdicao;
	}

	public void setNumeroEdicao(Long numeroEdicao) {
		this.numeroEdicao = numeroEdicao;
	}
	
	
}
