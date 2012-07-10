package br.com.abril.nds.model.seguranca;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "USUARIO_EXCESSAO_PERMISSAO", uniqueConstraints = {
	   @UniqueConstraint(columnNames= {"USUARIO_ID"}),
	   @UniqueConstraint(columnNames= {"GRUPO_PERMISSOES_ID"}),
	   @UniqueConstraint(columnNames= {"PERMISSAO_ID"}) })
public class UsuarioExcecaoPermissao implements Serializable {
	
	private static final long serialVersionUID = 5170861422594122559L;

	@Id  
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)  
    @JoinColumn(name = "USUARIO_ID", unique = true, nullable = false, insertable = false, updatable = false)  
    private Usuario usuario;
	
    @Id  
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)  
    @JoinColumn(name = "GRUPO_PERMISSOES_ID", unique = true, nullable = false, insertable = false, updatable = false)  
    private GrupoPermissoes grupoPermissoes;
	
    @Id  
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)  
    @JoinColumn(name = "PERMISSAO_ID", unique = true, nullable = false, insertable = false, updatable = false)  
    private Permissao permissao;

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public GrupoPermissoes getGrupoPermissoes() {
		return grupoPermissoes;
	}

	public void setGrupoPermissoes(GrupoPermissoes grupoPermissoes) {
		this.grupoPermissoes = grupoPermissoes;
	}

	public Permissao getPermissao() {
		return permissao;
	}

	public void setPermissao(Permissao permissao) {
		this.permissao = permissao;
	}

}
