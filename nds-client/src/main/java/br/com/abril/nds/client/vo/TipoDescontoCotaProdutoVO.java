package br.com.abril.nds.client.vo;

import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class TipoDescontoCotaProdutoVO {
	
	private String id;
	
	@Export(label = "Data Alteração")
	private String dtAlteracao;
	
	@Export(label = "Desconto")
	private String desconto;
	
	@Export(label = "Usuário")
	private String usuario;
	
	@Export(label = "Código")
	private String codigo;
	
	@Export(label = "Produto")
	private String produto;
	
	@Export(label = "Edição")
	private String edicao;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDtAlteracao() {
		return dtAlteracao;
	}

	public void setDtAlteracao(String dtAlteracao) {
		this.dtAlteracao = dtAlteracao;
	}

	public String getDesconto() {
		return desconto;
	}

	public void setDesconto(String desconto) {
		this.desconto = desconto;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getProduto() {
		return produto;
	}

	public void setProduto(String produto) {
		this.produto = produto;
	}

	public String getEdicao() {
		return edicao;
	}

	public void setEdicao(String edicao) {
		this.edicao = edicao;
	}
	
	
	

	
}
