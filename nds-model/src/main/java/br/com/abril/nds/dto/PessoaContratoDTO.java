package br.com.abril.nds.dto;

import java.io.Serializable;
import java.util.List;

import br.com.abril.nds.model.cadastro.Endereco;
import br.com.abril.nds.model.cadastro.Telefone;

public class PessoaContratoDTO implements Serializable {

	public enum TipoPessoa {
		JURIDICA,FISICA;

	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -2914580396045962188L;
	
	
	private String nome;
	
	private String documento;
	
	private TipoPessoa tipoPessoa;
	
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDocumento() {
		return documento;
	}

	public void setDocumento(String documento) {
		this.documento = documento;
	}

	public TipoPessoa getTipoPessoa() {
		return tipoPessoa;
	}

	public void setTipoPessoa(TipoPessoa tipoPessoa) {
		this.tipoPessoa = tipoPessoa;
	}

	public Endereco getEndereco() {
		return endereco;
	}

	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}

	public String getNomeGestor() {
		return nomeGestor;
	}

	public void setNomeGestor(String nomeGestor) {
		this.nomeGestor = nomeGestor;
	}

	public List<Telefone> getTelefones() {
		return telefones;
	}

	public void setTelefones(List<Telefone> telefones) {
		this.telefones = telefones;
	}

	public String getEmailGestor() {
		return emailGestor;
	}

	public void setEmailGestor(String emailGestor) {
		this.emailGestor = emailGestor;
	}

	public Endereco getEnderecoGestor() {
		return enderecoGestor;
	}

	public void setEnderecoGestor(Endereco enderecoGestor) {
		this.enderecoGestor = enderecoGestor;
	}

	private Endereco endereco;
	
	private String nomeGestor;
	
	private List<Telefone> telefones;
	
	private String emailGestor;
	
	private Endereco enderecoGestor;
	
	

}
