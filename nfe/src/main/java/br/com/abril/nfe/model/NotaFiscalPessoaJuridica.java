package br.com.abril.nfe.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
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
	
	@Column(name = "HORA_SAIDA")
	private	String horaSaida;

	@Column(name = "DATA_EMISSAO", nullable = false)
	private Date dataEmissao;
	
	@Column(name = "DATA_EXPEDICAO", nullable = false)
	private Date dataEntradaSaida;
	
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
	
	public String getHoraSaida() {
		return horaSaida;
	}

	public void setHoraSaida(String horaSaida) {
		this.horaSaida = horaSaida;
	}
	
	public Date getDataEmissao() {
		return dataEmissao;
	}
	
	public void setDataEmissao(Date dataEmissao) {
		this.dataEmissao = dataEmissao;
	}
	

	public Date getDataEntradaSaida() {
		return dataEntradaSaida;
	}

	public void setDataEntradaSaida(Date dataEntradaSaida) {
		this.dataEntradaSaida = dataEntradaSaida;
	}

	@Override
	public String toString() {
		return "EmitenteDestinario [id=" + super.getId() + ", nomeRazaoSocial="
				+ razaoSocial + ", cnpjCpf=" + cnpj + ", "
				+ ", horaSaida=" + horaSaida + ", dataEmissao="
				+ dataEmissao + ", dataEntradaSaida=" + dataEntradaSaida
				+ ", notaFicalEndereco=" + notaFicalEndereco + "]";
	}
}
