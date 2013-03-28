package br.com.abril.nds.dto;

import java.io.Serializable;

import br.com.abril.nds.model.seguranca.Permissao;

public class AcessoDTO implements Serializable {

	private static final long serialVersionUID = -7936154707508705341L;
	
	private String descricao;
	
	private Permissao pai;
	
	private Permissao visualizacao;
	
	private Permissao alteracao;

	public AcessoDTO(Permissao permissao) {
		this.descricao = permissao.getDescricao();
		this.visualizacao = permissao;
		this.alteracao = permissao.getPermissaoAlteracao();
		this.setPai(permissao.getPermissaoPai());
	}
	
	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Permissao getVisualizacao() {
		return visualizacao;
	}

	public void setVisualizacao(Permissao visualizacao) {
		this.visualizacao = visualizacao;
	}

	public Permissao getAlteracao() {
		return alteracao;
	}

	public void setAlteracao(Permissao alteracao) {
		this.alteracao = alteracao;
	}

	public Permissao getPai() {
		return pai;
	}

	public void setPai(Permissao pai) {
		this.pai = pai;
	}
	
	
}
