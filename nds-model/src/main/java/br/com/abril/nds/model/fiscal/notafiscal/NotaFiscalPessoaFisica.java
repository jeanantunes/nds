package br.com.abril.nds.model.fiscal.notafiscal;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
@DiscriminatorValue(value = "F")
public class NotaFiscalPessoaFisica extends NotaFiscalPessoa implements Serializable {
	
	private static final long serialVersionUID = 8373385180282371004L;

	@Column(name="CPF")
	private String cpf;
	
	@Column(name="RG")
	private String rg;

	@OneToOne
	@JoinColumn(name="NOTA_FICAL_ENDERECO_ID", unique=true)
	private NotaFiscalEndereco notaFicalEndereco;

	public NotaFiscalEndereco getNotaFicalEndereco() {
		return notaFicalEndereco;
	}

	public void setNotaFicalEndereco(NotaFiscalEndereco notaFicalEndereco) {
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
		return super.getNome();
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getRg() {
		return rg;
	}

	public void setRg(String rg) {
		this.rg = rg;
	}

}
