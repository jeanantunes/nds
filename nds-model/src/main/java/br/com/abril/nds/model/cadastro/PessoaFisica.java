package br.com.abril.nds.model.cadastro;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * @author francisco.garcia
 * @version 1.0
 * @created 14-fev-2012 11:35:31
 */
@Entity
@DiscriminatorValue(value = "F")
public class PessoaFisica extends Pessoa {

	@Column(name = "NOME")
	private String nome;
	
	@Column(name = "CPF")
	private String cpf;
	
	@Column(name = "RG")
	private String rg;
	
	@Column(name = "ORGAO_EMISSOR")
	private String orgaoEmissor;
	
	@Column(name = "UF_ORGAO_EMISSOR")
	private String ufOrgaoEmissor;
	
	@Column(name = "DATA_NASCIMENTO")
	@Temporal(TemporalType.DATE)
	private Date dataNascimento;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "ESTADO_CIVIL")
	private EstadoCivil estadoCivil;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "SEXO")
	private Sexo sexo;
	
	@Column(name = "NACIONALIDADE")
	private String nacionalidade;
	
	@Column(name = "NATURAL")
	private String natural;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
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

	public String getOrgaoEmissor() {
		return orgaoEmissor;
	}

	public void setOrgaoEmissor(String orgaoEmissor) {
		this.orgaoEmissor = orgaoEmissor;
	}

	public String getUfOrgaoEmissor() {
		return ufOrgaoEmissor;
	}

	public void setUfOrgaoEmissor(String ufOrgaoEmissor) {
		this.ufOrgaoEmissor = ufOrgaoEmissor;
	}

	public Date getDataNascimento() {
		return dataNascimento;
	}

	public void setDataNascimento(Date dataNascimento) {
		this.dataNascimento = dataNascimento;
	}

	public EstadoCivil getEstadoCivil() {
		return estadoCivil;
	}

	public void setEstadoCivil(EstadoCivil estadoCivil) {
		this.estadoCivil = estadoCivil;
	}

	public Sexo getSexo() {
		return sexo;
	}

	public void setSexo(Sexo sexo) {
		this.sexo = sexo;
	}

	public String getNacionalidade() {
		return nacionalidade;
	}

	public void setNacionalidade(String nacionalidade) {
		this.nacionalidade = nacionalidade;
	}

	public String getNatural() {
		return natural;
	}

	public void setNatural(String natural) {
		this.natural = natural;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cpf == null) ? 0 : cpf.hashCode());
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
		PessoaFisica other = (PessoaFisica) obj;
		if (cpf == null) {
			if (other.cpf != null)
				return false;
		} else if (!cpf.equals(other.cpf))
			return false;
		return true;
	}

}