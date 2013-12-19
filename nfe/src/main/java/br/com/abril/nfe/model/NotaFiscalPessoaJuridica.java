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
public class NotaFiscalPessoaJuridica extends NotaFiscalPessoa implements Serializable, Pessoa {
	
	private static final long serialVersionUID = 8373385180282371004L;

	@Column(name="RAZAO_SOCIAL")
	private String razaoSocial;
	
	@Column(name="NOME_FANTASIA")
	private String nomeFantasia;

	@Column(name="CNPJ")
	private String cnpj;
	
	@Column(name="INSCRICAO_ESTADUAL")
	private String inscricaoEstadual;
	
	@OneToOne(mappedBy="notaFicalEndereco")
	@JoinColumn(name="NOTA_FICAL_ENDERECO_ID", unique=true)
	private NotaFicalEndereco notaFicalEndereco;

	public String getRazaoSocial() {
		return razaoSocial;
	}

	public void setRazaoSocial(String razaoSocial) {
		this.razaoSocial = razaoSocial;
	}

	public String getNomeFantasia() {
		return nomeFantasia;
	}

	public void setNomeFantasia(String nomeFantasia) {
		this.nomeFantasia = nomeFantasia;
	}

	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

	public String getInscricaoEstadual() {
		return inscricaoEstadual;
	}

	public void setInscricaoEstadual(String inscricaoEstadual) {
		this.inscricaoEstadual = inscricaoEstadual;
	}

	public NotaFicalEndereco getNotaFicalEndereco() {
		return notaFicalEndereco;
	}

	public void setNotaFicalEndereco(NotaFicalEndereco notaFicalEndereco) {
		this.notaFicalEndereco = notaFicalEndereco;
	}

	@Override
	public String toString() {
		return "EmitenteDestinario [id=" + super.getId() + ", nomeRazaoSocial="
				+ razaoSocial + ", cnpjCpf=" + cnpj + ", "
				+ ", notaFicalEndereco=" + notaFicalEndereco + "]";
	}

	@Override
	public String getNome() {
		return this.getNomeFantasia() == null || this.getNomeFantasia().trim().endsWith("") ? this.getRazaoSocial() : this.getNomeFantasia();
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
