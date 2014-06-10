package br.com.abril.nds.client.vo;

import br.com.abril.nds.util.export.Export;
import br.com.abril.nds.util.export.Exportable;

@Exportable
public class TipoDescontoCotaVO {
	
	private String id;
	
	@Export(label = "Data Alteração",exhibitionOrder=4)
	private String dtAlteracao;
	
	@Export(label = "Desconto %",exhibitionOrder=2)
	private String desconto;
	
	@Export(label = "Usuário",exhibitionOrder=5)
	private String usuario;
	
	@Export(label = "",exhibitionOrder=1)
	private String sequencial;
	
	private String cota;
	
	private String nome;
	
	private String codigo;
	
	private String produto;
	
	private String edicao;
	
	private String especificacaoDesconto;
	
	@Export(label = "Fornecedores",exhibitionOrder=3)
	private String fornecedor;
	
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

	public String getSequencial() {
		return sequencial;
	}

	public void setSequencial(String sequencial) {
		this.sequencial = sequencial;
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

	public String getEspecificacaoDesconto() {
		return especificacaoDesconto;
	}

	public void setEspecificacaoDesconto(String especificacaoDesconto) {
		this.especificacaoDesconto = especificacaoDesconto;
	}

	/**
	 * @return the fornecedor
	 */
	public String getFornecedor() {
		return fornecedor;
	}

	/**
	 * @param fornecedor the fornecedor to set
	 */
	public void setFornecedor(String fornecedor) {
		this.fornecedor = fornecedor;
	}
	
	
}
