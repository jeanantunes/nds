package br.com.abril.nfe.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import br.com.abril.nfe.model.interfaces.Endereco;
import br.com.abril.nfe.model.interfaces.Pessoa;
import br.com.abril.nfe.model.interfaces.Telefone;

@Entity
public class NotaFiscalPessoaFisica extends NotaFiscalPessoa implements Serializable, Pessoa {
	
	private static final long serialVersionUID = 8373385180282371004L;

	@Column(name="CPF")
	private String cpf;
	
	@Column(name="RG")
	private String rg;
	
	@OneToOne
	@JoinColumn(name="NOTA_FICAL_ENDERECO_ID", unique=true)
	private NotaFicalEndereco notaFicalEndereco;

	public NotaFicalEndereco getNotaFicalEndereco() {
		return notaFicalEndereco;
	}

	public void setNotaFicalEndereco(NotaFicalEndereco notaFicalEndereco) {
		this.notaFicalEndereco = notaFicalEndereco;
	}
	
	@Override
	public String toString() {
		return "EmitenteDestinario [id=" + this.getId() + ", nome="
				+ this.getNome() + ", cpf=" + cpf + ", "
				+ ", notaFicalEndereco=" + notaFicalEndereco + "]";
	}
	
	
	@Override
	public String getNome() {
		return this.getNome();
	}

	@Override
	public Telefone getTelefone() {
		return this.getTelefone();
	}

	@Override
	public Endereco getEndereco() {
		return this.getEndereco();
	}
}
