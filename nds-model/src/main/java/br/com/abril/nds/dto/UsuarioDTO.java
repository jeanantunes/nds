package br.com.abril.nds.dto;

import java.io.Serializable;
import java.util.List;

import br.com.abril.nds.model.seguranca.Permissao;

/**
 * @author InfoA2
 */
public class UsuarioDTO implements Serializable {

	private static final long serialVersionUID = 8177352750699708272L;

	public static final String ATIVA = "ativa";
	
	private Long id;

	private String nome;

	private String sobrenome;

	private String login;

	private String email;

	private String senha;

	private String lembreteSenha;

	private String ddd;

	private String telefone;

	private String contaAtiva;

	private String endereco;

	private String cidade;

	private String pais;

	private String cep;

	private String confirmaSenha;

	private String senhaAntiga;

	private String permissoesSelecionadas;

	private String gruposSelecionados;

	private List<GrupoPermissaoDTO> grupos;

	private List<Permissao> permissoes;

	private List<GrupoPermissaoDTO> gruposSelecionadosList;

	private List<Permissao> permissoesSelecionadasList;

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

	public String getSobrenome() {
		return sobrenome;
	}

	public void setSobrenome(String sobrenome) {
		this.sobrenome = sobrenome;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
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

	public String getSenhaAntiga() {
		return senhaAntiga;
	}

	public void setSenhaAntiga(String senhaAntiga) {
		this.senhaAntiga = senhaAntiga;
	}

	public String getConfirmaSenha() {
		return confirmaSenha;
	}

	public void setConfirmaSenha(String confirmaSenha) {
		this.confirmaSenha = confirmaSenha;
	}

	public String getPermissoesSelecionadas() {
		return permissoesSelecionadas;
	}

	public void setPermissoesSelecionadas(String permissoesSelecionadas) {
		this.permissoesSelecionadas = permissoesSelecionadas;
	}

	public String getGruposSelecionados() {
		return gruposSelecionados;
	}

	public void setGruposSelecionados(String gruposSelecionados) {
		this.gruposSelecionados = gruposSelecionados;
	}

	public List<GrupoPermissaoDTO> getGrupos() {
		return grupos;
	}

	public void setGrupos(List<GrupoPermissaoDTO> grupos) {
		this.grupos = grupos;
	}

	public List<Permissao> getPermissoes() {
		return permissoes;
	}

	public void setPermissoes(List<Permissao> permissoes) {
		this.permissoes = permissoes;
	}

	public String getContaAtiva() {
		return contaAtiva;
	}

	public void setContaAtiva(String contaAtiva) {
		this.contaAtiva = contaAtiva;
	}

	public List<GrupoPermissaoDTO> getGruposSelecionadosList() {
		return gruposSelecionadosList;
	}

	public void setGruposSelecionadosList(
			List<GrupoPermissaoDTO> gruposSelecionadosList) {
		this.gruposSelecionadosList = gruposSelecionadosList;
	}

	public List<Permissao> getPermissoesSelecionadasList() {
		return permissoesSelecionadasList;
	}

	public void setPermissoesSelecionadasList(
			List<Permissao> permissoesSelecionadasList) {
		this.permissoesSelecionadasList = permissoesSelecionadasList;
	}
	
}
