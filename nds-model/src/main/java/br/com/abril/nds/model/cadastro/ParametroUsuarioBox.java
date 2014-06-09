package br.com.abril.nds.model.cadastro;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.com.abril.nds.model.seguranca.Usuario;

@Entity
@Table(name = "PARAMETRO_USUARIO_BOX")
@SequenceGenerator(name="PARAMETRO_USUARIO_BOX_SEQ", initialValue = 1, allocationSize = 1)
public class ParametroUsuarioBox {

	@Id
	@GeneratedValue(generator = "PARAMETRO_USUARIO_BOX_SEQ")
	@Column(name = "ID")
	private Long id;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "USUARIO_ID")
	private Usuario usuario;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "BOX_ID")
	private Box box;
	
	@Column(name = "PRINCIPAL")
	private boolean principal;

	/**
	 * Obtém id
	 *
	 * @return Long
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Atribuí id
	 * @param id 
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Obtém usuario
	 *
	 * @return Usuario
	 */
	public Usuario getUsuario() {
		return usuario;
	}

	/**
	 * Atribuí usuario
	 * @param usuario 
	 */
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	/**
	 * Obtém box
	 *
	 * @return Box
	 */
	public Box getBox() {
		return box;
	}

	/**
	 * Atribuí box
	 * @param box 
	 */
	public void setBox(Box box) {
		this.box = box;
	}

	public boolean isPrincipal() {
		return principal;
	}

	public void setPrincipal(boolean principal) {
		this.principal = principal;
	}

	
	
}
