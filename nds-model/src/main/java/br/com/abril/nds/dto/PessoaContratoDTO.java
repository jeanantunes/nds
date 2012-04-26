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

	@Override
	public String toString() {
		return "PessoaContratoDTO [nome=" + nome + ", documento=" + documento
				+ ", tipoPessoa=" + tipoPessoa + ", endereco=" + endereco
				+ ", nomeGestor=" + nomeGestor + ", telefones=" + telefones
				+ ", emailGestor=" + emailGestor + ", enderecoGestor="
				+ enderecoGestor + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((documento == null) ? 0 : documento.hashCode());
		result = prime * result
				+ ((emailGestor == null) ? 0 : emailGestor.hashCode());
		result = prime * result
				+ ((endereco == null) ? 0 : endereco.hashCode());
		result = prime * result
				+ ((enderecoGestor == null) ? 0 : enderecoGestor.hashCode());
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
		result = prime * result
				+ ((nomeGestor == null) ? 0 : nomeGestor.hashCode());
		result = prime * result
				+ ((telefones == null) ? 0 : telefones.hashCode());
		result = prime * result
				+ ((tipoPessoa == null) ? 0 : tipoPessoa.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PessoaContratoDTO other = (PessoaContratoDTO) obj;
		if (documento == null) {
			if (other.documento != null)
				return false;
		} else if (!documento.equals(other.documento))
			return false;
		if (emailGestor == null) {
			if (other.emailGestor != null)
				return false;
		} else if (!emailGestor.equals(other.emailGestor))
			return false;
		if (endereco == null) {
			if (other.endereco != null)
				return false;
		} else if (!endereco.equals(other.endereco))
			return false;
		if (enderecoGestor == null) {
			if (other.enderecoGestor != null)
				return false;
		} else if (!enderecoGestor.equals(other.enderecoGestor))
			return false;
		if (nome == null) {
			if (other.nome != null)
				return false;
		} else if (!nome.equals(other.nome))
			return false;
		if (nomeGestor == null) {
			if (other.nomeGestor != null)
				return false;
		} else if (!nomeGestor.equals(other.nomeGestor))
			return false;
		if (telefones == null) {
			if (other.telefones != null)
				return false;
		} else if (!telefones.equals(other.telefones))
			return false;
		if (tipoPessoa != other.tipoPessoa)
			return false;
		return true;
	}
	
	

}
