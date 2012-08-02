package br.com.abril.nds.dto;

import java.io.Serializable;

import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Exportable;
import br.com.abril.nds.util.export.Export.Alignment;

@Exportable
public class TipoDescontoProdutoDTO implements Serializable {

	private static final long serialVersionUID = 8061615658824921559L;
	
	@Export(label = "Código", alignment=Alignment.LEFT, exhibitionOrder = 1)
	private String codigoProduto;
	
	@Export(label = "Produto", alignment=Alignment.LEFT, exhibitionOrder = 2)
	private String nomeProduto;
	
	@Export(label = "Edição", alignment=Alignment.LEFT, exhibitionOrder = 3)
	private String numeroEdicao;
	
	@Export(label = "Desconto", alignment=Alignment.RIGHT, exhibitionOrder = 4)
	private Float desconto;
	
	@Export(label = "Data Alteração", alignment=Alignment.CENTER, exhibitionOrder = 5)
	private String dataAlteracao;
	
	@Export(label = "Usuário", alignment=Alignment.LEFT, exhibitionOrder = 6)
	private String nomeUsuario;
	
	public TipoDescontoProdutoDTO() {}

	public TipoDescontoProdutoDTO(String codigoProduto, String nomeProduto,
			String numeroEdicao, Float desconto, String dataAlteracao,
			String nomeUsuario) {
		super();
		this.codigoProduto = codigoProduto;
		this.nomeProduto = nomeProduto;
		this.numeroEdicao = numeroEdicao;
		this.desconto = desconto;
		this.dataAlteracao = dataAlteracao;
		this.nomeUsuario = nomeUsuario;
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

	public String getNumeroEdicao() {
		return numeroEdicao;
	}

	public void setNumeroEdicao(String numeroEdicao) {
		this.numeroEdicao = numeroEdicao;
	}

	public Float getDesconto() {
		return desconto;
	}

	public void setDesconto(Float desconto) {
		this.desconto = desconto;
	}

	public String getDataAlteracao() {
		return dataAlteracao;
	}

	public void setDataAlteracao(String dataAlteracao) {
		this.dataAlteracao = dataAlteracao;
	}

	public String getNomeUsuario() {
		return nomeUsuario;
	}

	public void setNomeUsuario(String nomeUsuario) {
		this.nomeUsuario = nomeUsuario;
	}
	
}
