package br.com.abril.nfe.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "NOTA_FISCAL_EMITENTE_DESTINARIO")
public class EmitenteDestinario extends NotaFiscalPessoa implements Serializable {
	
	private static final long serialVersionUID = 8373385180282371004L;

	@Id
	@GeneratedValue()
	@Column(name="ID")
	private Long id;
	
	@Column(name="NOME_RAZAO_SOCIAL")
	private String nomeRazaoSocial;

	@Column(name="CNPJ_CPF")
	private String cnpjCpf;
	
	@Column(name="EMISSAO")
	private Date emissao;
	
	@Column(name = "HORA_SAIDA")
	private	String horaSaida;

	@Column(name = "DATA_EMISSAO", nullable = false)
	protected Date dataEmissao;
	
	@Column(name = "DATA_EXPEDICAO", nullable = false)
	protected Date dataExpedicao;
	
	@OneToOne(mappedBy="notaFicalEndereco")
	@JoinColumn(name="NOTA_FICAL_ENDERECO_ID", unique=true)
	private NotaFicalEndereco notaFicalEndereco;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNomeRazaoSocial() {
		return nomeRazaoSocial;
	}

	public void setNomeRazaoSocial(String nomeRazaoSocial) {
		this.nomeRazaoSocial = nomeRazaoSocial;
	}

	public String getCnpjCpf() {
		return cnpjCpf;
	}

	public void setCnpjCpf(String cnpjCpf) {
		this.cnpjCpf = cnpjCpf;
	}

	public Date getEmissao() {
		return emissao;
	}

	public void setEmissao(Date emissao) {
		this.emissao = emissao;
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
	
	public Date getDataExpedicao() {
		return dataExpedicao;
	}
	
	public void setDataExpedicao(Date dataExpedicao) {
		this.dataExpedicao = dataExpedicao;
	}

	@Override
	public String toString() {
		return "EmitenteDestinario [id=" + id + ", nomeRazaoSocial="
				+ nomeRazaoSocial + ", cnpjCpf=" + cnpjCpf + ", emissao="
				+ emissao + ", horaSaida=" + horaSaida + ", dataEmissao="
				+ dataEmissao + ", dataExpedicao=" + dataExpedicao
				+ ", notaFicalEndereco=" + notaFicalEndereco + "]";
	}
}
