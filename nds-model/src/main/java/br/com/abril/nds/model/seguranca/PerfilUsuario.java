package br.com.abril.nds.model.seguranca;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * @author francisco.garcia
 * @version 1.0
 * @created 14-fev-2012 11:35:32
 */
@Entity
@Table(name = "PERFIL_USUARIO")
@SequenceGenerator(name="PERFIL_USUARIO_SEQ", initialValue = 1, allocationSize = 1)
public class PerfilUsuario {

	@Id
	@GeneratedValue(generator = "PERFIL_USUARIO_SEQ")
	@Column(name = "ID")
	private Long id;
	@Column(name = "DESCRICAO", nullable = false)
	private String descricao;
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getDescricao() {
		return descricao;
	}
	
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

}