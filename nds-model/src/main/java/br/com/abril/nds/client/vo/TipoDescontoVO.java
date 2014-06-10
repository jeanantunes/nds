package br.com.abril.nds.client.vo;

import br.com.abril.nds.util.export.Export;

public class TipoDescontoVO {
	
	private String id;
	
	@Export(label = "Data Alteração")
	private String dtAlteracao;
	
	@Export(label = "Desconto")
	private String desconto;
	
	@Export(label = "Usuário")
	private String usuario;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDtAlteracao() {
		return dtAlteracao;
	}

	public void setDataAlteracao(String dtAlteracao) {
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

}
