package br.com.abril.nds.dto.filtro;

import java.io.Serializable;

import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class FiltroDetalheVendaProdutoDTO implements Serializable {

	private static final long serialVersionUID = 2433782322485644813L;
	
	@Export(label = "Código")
	private String codigo;
	
	@Export(label = "Produto")
	private String nomeProduto;
	
	@Export(label = "Edição")
	private Long edicao;
	
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
	public Long getEdicao() {
		return edicao;
	}
	public void setEdicao(Long edicao) {
		this.edicao = edicao;
	}
	
}
