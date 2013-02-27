package br.com.abril.nds.model.integracao;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "EVENTO_EXECUCAO")
public class EventoExecucao implements Serializable {
	
	private static final long serialVersionUID = 6094606168019620055L;

	@Id
	@Column(name = "ID")
	private Long id;

	@Column(name = "NOME", length = 30, nullable = false)
	private String nome;
	
	@Column(name = "DESCRICAO", length = 100, nullable = false)
	private String descricao;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
}
