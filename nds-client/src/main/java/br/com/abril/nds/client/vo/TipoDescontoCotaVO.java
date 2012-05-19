package br.com.abril.nds.client.vo;

import br.com.abril.nds.util.export.Export;

public class TipoDescontoCotaVO {
	
	private String id;
	
	@Export(label = "Data Alteração")
	private String dtAlteracao;
	
	@Export(label = "Desconto")
	private String desconto;
	
	@Export(label = "Usuário")
	private String usuario;
	
	@Export(label = "")
	private String seq;
	
	@Export(label = "Cota")
	private String cota;
	
	@Export(label = "Nome")
	private String nome;

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

	public String getSeq() {
		return seq;
	}

	public void setSeq(String seq) {
		this.seq = seq;
	}

	public String getCota() {
		return cota;
	}

	public void setCota(String cota) {
		this.cota = cota;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
	
	
	

}
