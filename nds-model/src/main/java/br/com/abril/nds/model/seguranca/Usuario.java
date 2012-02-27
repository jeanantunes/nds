package br.com.abril.nds.model.seguranca;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * @author T30541
 * @version 1.0
 * @created 14-fev-2012 11:35:33
 */
@Entity
@Table(name = "USUARIO")
@SequenceGenerator(name="USUARIO_SEQ", initialValue = 1, allocationSize = 1)
public class Usuario {

	@Id
	@GeneratedValue(generator = "USUARIO_SEQ")
	@Column(name = "ID")
	private Long id;
	@Column(name = "NOME", nullable = false)
	private String nome;
	@Column(name = "LOGIN", nullable = false)
	private String login;
	@Column(name = "SENHA", nullable = false)
	private String senha;
	
	@OneToMany
	private List<PerfilUsuario> perfilUsuario = new ArrayList<PerfilUsuario>();
	
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
	
	public List<PerfilUsuario> getPerfilUsuario() {
		return perfilUsuario;
	}
	
	public void setPerfilUsuario(List<PerfilUsuario> perfilUsuario) {
		this.perfilUsuario = perfilUsuario;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

}