package br.com.abril.nds.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Exportable;
import br.com.abril.nds.util.export.Export.Alignment;

@Exportable
public class SuplementarFecharDiaDTO implements Serializable {

	private static final long serialVersionUID = 1712356619355112111L;
	
	@Export(label = "Código", alignment=Alignment.LEFT, exhibitionOrder = 1)
	private String codigo;
	
	@Export(label = "Produto", alignment=Alignment.LEFT, exhibitionOrder = 2)
	private String nomeProduto;
	
	@Export(label = "Edição", alignment=Alignment.LEFT, exhibitionOrder = 3)
	private Long numeroEdicao;
	
	@Export(label = "Preço Capa", alignment=Alignment.RIGHT, exhibitionOrder = 4)
	private BigDecimal precoVenda;
	
	private Integer quantidadeContabil;
	
	private Integer quantidadeFisico;
	
	private Integer diferenca;
	
	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
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

	public BigDecimal getPrecoVenda() {
		return precoVenda;
	}

	public void setPrecoVenda(BigDecimal precoVenda) {
		this.precoVenda = precoVenda;
	}

	public Integer getQuantidadeContabil() {
		return quantidadeContabil;
	}

	public void setQuantidadeContabil(Integer quantidadeContabil) {
		this.quantidadeContabil = quantidadeContabil;
	}

	public Integer getQuantidadeFisico() {
		return quantidadeFisico;
	}

	public void setQuantidadeFisico(Integer quantidadeFisico) {
		this.quantidadeFisico = quantidadeFisico;
	}

	public Integer getDiferenca() {
		return diferenca;
	}

	public void setDiferenca(Integer diferenca) {
		this.diferenca = diferenca;
	}
	
	

}
