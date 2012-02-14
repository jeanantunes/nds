package br.com.abril.nds.model.seguranca;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * @author T30541
 * @version 1.0
 * @created 14-fev-2012 11:35:33
 */
@Entity
@Table(name = "USUARIO")
public class Usuario {

	@Id
	private Long id;
	private String nome;
	@OneToMany
	private List<PerfilUsuario> perfilUsuario;
	
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

}