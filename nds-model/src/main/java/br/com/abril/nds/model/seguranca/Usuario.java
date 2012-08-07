package br.com.abril.nds.model.seguranca;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
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
public class Usuario implements Serializable {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -574506866277940279L;
	
	@Id
	@GeneratedValue(generator = "USUARIO_SEQ")
	@Column(name = "ID")
	private Long id;
	
	@Column(name = "NOME", nullable = false)
	private String nome;
	
	@Column(name = "SOBRENOME", nullable = true)
	private String sobrenome;
	
	@Column(name = "LOGIN", nullable = false)
	private String login;
	
	@Column(name = "EMAIL", nullable = false)
	private String email;
	
	@Column(name = "SENHA", nullable = false)
	private String senha;
	
	@Column(name = "LEMBRETE_SENHA", nullable = true)
	private String lembreteSenha;

	@Column(name = "DDD", nullable = true)
	private String ddd;

	@Column(name = "TELEFONE", nullable = true)
	private String telefone;
	
	@Column(name = "CONTA_ATIVA")
	private boolean contaAtiva = false;

	@Column(name = "ENDERECO")
	private String endereco;

	@Column(name = "CIDADE")
	private String cidade;

	@Column(name = "PAIS")
	private String pais;

	@Column(name = "CEP")
	private String cep;

	@ManyToMany(fetch=FetchType.LAZY)
	@Column(name="GRUPO_PERMISSAO_ID")
	private Set<GrupoPermissao> gruposPermissoes = new HashSet<GrupoPermissao>();

	@ElementCollection(targetClass=Permissao.class, fetch=FetchType.EAGER)
	@Enumerated(EnumType.STRING)
    @JoinTable(
            name="USUARIO_PERMISSAO", // ref table.
            joinColumns = {@JoinColumn(name="USUARIO_ID")}
    )
    @Column(name="PERMISSAO_ID")
	private Set<Permissao> permissoes = new HashSet<Permissao>();
	
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

	public Set<GrupoPermissao> getGruposPermissoes() {
		return gruposPermissoes;
	}

	public void setGruposPermissoes(Set<GrupoPermissao> gruposPermissoes) {
		this.gruposPermissoes = gruposPermissoes;
	}

	public String getSobrenome() {
		return sobrenome;
	}

	public void setSobrenome(String sobrenome) {
		this.sobrenome = sobrenome;
	}

	public String getLembreteSenha() {
		return lembreteSenha;
	}

	public void setLembreteSenha(String lembreteSenha) {
		this.lembreteSenha = lembreteSenha;
	}

	public String getDdd() {
		return ddd;
	}

	public void setDdd(String ddd) {
		this.ddd = ddd;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

	public String getCidade() {
		return cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

	public String getPais() {
		return pais;
	}

	public void setPais(String pais) {
		this.pais = pais;
	}

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public Set<Permissao> getPermissoes() {
		return permissoes;
	}

	public void setPermissoes(Set<Permissao> permissoes) {
		this.permissoes = permissoes;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean isContaAtiva() {
		return contaAtiva;
	}

	public void setContaAtiva(boolean contaAtiva) {
		this.contaAtiva = contaAtiva;
	}
	
}