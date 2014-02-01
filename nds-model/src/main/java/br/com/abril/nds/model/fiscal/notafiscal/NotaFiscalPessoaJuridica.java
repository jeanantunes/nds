package br.com.abril.nds.model.fiscal.notafiscal;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
@DiscriminatorValue(value = "J")
public class NotaFiscalPessoaJuridica extends NotaFiscalPessoa implements Serializable {
	
	private static final long serialVersionUID = 8373385180282371004L;

	@Column(name="RAZAO_SOCIAL")
	private String razaoSocial;
	
	@Column(name="NOME_FANTASIA")
	private String nomeFantasia;

	@Column(name="CNPJ")
	private String cnpj;
	
	@Column(name="INSCRICAO_ESTADUAL")
	private String inscricaoEstadual;
	
	@OneToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="NOTA_FICAL_ENDERECO_ID", unique=true)
	private NotaFiscalEndereco notaFicalEndereco;

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

	public NotaFiscalEndereco getNotaFicalEndereco() {
		return notaFicalEndereco;
	}

	public void setNotaFicalEndereco(NotaFiscalEndereco notaFicalEndereco) {
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
}
